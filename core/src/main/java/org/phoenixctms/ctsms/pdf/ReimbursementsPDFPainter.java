package org.phoenixctms.ctsms.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.pdf.ReimbursementsPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.MoneyTransferByBankAccountSummaryDetailVO;
import org.phoenixctms.ctsms.vo.MoneyTransferByCostTypeSummaryDetailVO;
import org.phoenixctms.ctsms.vo.MoneyTransferByPaymentMethodSummaryDetailVO;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ReimbursementsPDFVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialTagValueOutVO;

public class ReimbursementsPDFPainter extends PDFPainterBase implements PDFOutput {

	private static abstract class MoneyTransferByCostTypeSummaryDetailVOIterator<T> implements Iterator<T> {

		private MoneyTransferByCostTypeSummaryDetailVO current;
		private Iterator<MoneyTransferByCostTypeSummaryDetailVO> byCostTypesIt;
		private Iterator<String> commentsIt;
		private float heightLimit;

		protected MoneyTransferByCostTypeSummaryDetailVOIterator(T detail, float heightLimit) {
			byCostTypesIt = getByCostTypes(detail).iterator();
			this.heightLimit = heightLimit;
		}

		protected abstract T createNewDetail();

		protected abstract float getBlockHeight(T newDetail);

		protected abstract Collection<MoneyTransferByCostTypeSummaryDetailVO> getByCostTypes(T detail);

		@Override
		public boolean hasNext() {
			return byCostTypesIt.hasNext() || (commentsIt != null && commentsIt.hasNext());
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T result = createNewDetail();
			while (hasNext() && getBlockHeight(result) < heightLimit) {
				MoneyTransferByCostTypeSummaryDetailVO next = new MoneyTransferByCostTypeSummaryDetailVO();
				if (current == null || !commentsIt.hasNext()) {
					current = byCostTypesIt.next();
					commentsIt = current.getComments().iterator();
					next.setTotal(current.getTotal());
				}
				next.setCostType(current.getCostType());
				next.setCount(current.getCount());
				next.setDecrypted(current.getDecrypted());
				getByCostTypes(result).add(next);
				while (commentsIt.hasNext() && getBlockHeight(result) < heightLimit) {
					next.getComments().add(commentsIt.next());
				}
			}
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static Iterator<MoneyTransferByBankAccountSummaryDetailVO> createByCostTypeIterator(
			final MoneyTransferByBankAccountSummaryDetailVO detail, final ReimbursementsPDFBlockCursor cursor) {
		return new MoneyTransferByCostTypeSummaryDetailVOIterator<MoneyTransferByBankAccountSummaryDetailVO>(detail,
				Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_ROW_HEIGHT_LIMIT, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_ROW_HEIGHT_LIMIT)) {

			@Override
			protected MoneyTransferByBankAccountSummaryDetailVO createNewDetail() {
				MoneyTransferByBankAccountSummaryDetailVO newDetail = new MoneyTransferByBankAccountSummaryDetailVO();
				newDetail.setId(detail.getId());
				newDetail.setBankAccount(detail.getBankAccount());
				newDetail.setCount(detail.getCount());
				newDetail.setTotal(detail.getTotal());
				return newDetail;
			}

			@Override
			protected float getBlockHeight(MoneyTransferByBankAccountSummaryDetailVO newDetail) {
				try {
					ReimbursementsPDFBlock block = new ReimbursementsPDFBlock(newDetail, false, true);
					block.setNewPage(true);
					return block.getHeight(cursor);
				} catch (Exception e) {
					return 0.0f;
				}
			}

			@Override
			protected Collection<MoneyTransferByCostTypeSummaryDetailVO> getByCostTypes(MoneyTransferByBankAccountSummaryDetailVO detail) {
				return detail.getByCostTypes();
			}
		};
	}

	private static Iterator<MoneyTransferByPaymentMethodSummaryDetailVO> createByCostTypeIterator(
			final MoneyTransferByPaymentMethodSummaryDetailVO detail, final ReimbursementsPDFBlockCursor cursor) {
		return new MoneyTransferByCostTypeSummaryDetailVOIterator<MoneyTransferByPaymentMethodSummaryDetailVO>(detail,
				Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_ROW_HEIGHT_LIMIT, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_ROW_HEIGHT_LIMIT)) {

			@Override
			protected MoneyTransferByPaymentMethodSummaryDetailVO createNewDetail() {
				MoneyTransferByPaymentMethodSummaryDetailVO newDetail = new MoneyTransferByPaymentMethodSummaryDetailVO();
				newDetail.setMethod(detail.getMethod());
				newDetail.setCount(detail.getCount());
				newDetail.setTotal(detail.getTotal());
				return newDetail;
			}

			@Override
			protected float getBlockHeight(MoneyTransferByPaymentMethodSummaryDetailVO newDetail) {
				try {
					ReimbursementsPDFBlock block = new ReimbursementsPDFBlock(newDetail, false, true);
					block.setNewPage(true);
					return block.getHeight(cursor);
				} catch (Exception e) {
					return 0.0f;
				}
			}

			@Override
			protected Collection<MoneyTransferByCostTypeSummaryDetailVO> getByCostTypes(MoneyTransferByPaymentMethodSummaryDetailVO detail) {
				return detail.getByCostTypes();
			}
		};
	}

	private int blockIndex;
	private ArrayList<ReimbursementsPDFBlock> blocks;
	private ReimbursementsPDFBlockCursor cursor;
	private ReimbursementsPDFVO pdfVO;
	private TrialOutVO trialVO;
	private Collection<TrialTagValueOutVO> trialTagValueVOs;
	private Collection<ProbandOutVO> probandVOs;
	private HashMap<Long, MoneyTransferSummaryVO> summaryMap;
	private HashMap<Long, ProbandAddressOutVO> addressVOMap;
	private float pageWidth;
	private float pageHeight;
	private PDFont fontA;
	private PDFont fontB;
	private PDFont fontC;
	private PDFont fontD;
	private PDFont fontE;
	private PDFont fontF;
	private final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	private static final String REIMBURSEMENTS_PDF_FILENAME_PREFIX = "reimbursements_";
	private static final String REIMBURSEMENTS_PDF_FILENAME_TRIAL = "trial_";
	private static final String REIMBURSEMENTS_PDF_FILENAME_PROBAND = "proband_";

	public ReimbursementsPDFPainter() {
		super();
		blocks = new ArrayList<ReimbursementsPDFBlock>();
		pdfVO = new ReimbursementsPDFVO();
		cursor = new ReimbursementsPDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(ReimbursementsPDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	@Override
	public void drawNextBlock(PDPageContentStream contentStream) throws Exception {
		ReimbursementsPDFBlock block = blocks.get(blockIndex);
		cursor.setBlockY(cursor.getBlockY() - block.renderBlock(contentStream, cursor));
		blockIndex++;
	}

	@Override
	public void drawPage(PDPageContentStream contentStream) throws Exception {
	}

	@Override
	public void drawPageBreakNewPage(PDPageContentStream contentStream) throws Exception {
		ReimbursementsPDFBlock block = blocks.get(blockIndex);
		if (BlockType.PAYMENT_METHOD_TABLE_ROW.equals(block.getType())
				|| BlockType.BANK_ACCOUNT_TABLE_ROW.equals(block.getType())) {
			// paint ecrf header again
			cursor.setBlockY(cursor.getBlockY() - (new ReimbursementsPDFBlock(BlockType.MONEY_TRANSFER_TABLE_HEAD)).renderBlock(contentStream, cursor));
			block.setNewPage(true);
		}
	}

	@Override
	public void drawPageBreakOldPage(PDPageContentStream contentStream) throws Exception {
		ReimbursementsPDFBlock block = blocks.get(blockIndex - 1);
		if (BlockType.PAYMENT_METHOD_TABLE_ROW.equals(block.getType())
				|| BlockType.BANK_ACCOUNT_TABLE_ROW.equals(block.getType())) {
			if (!block.isLastTableRow()) {
				PDFUtil.renderLine(contentStream, Settings.getColor(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_FRAME_COLOR, Bundle.REIMBURSEMENTS_PDF,
						ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockX() + cursor.getBlockWidth(),
						cursor.getBlockY(), Settings.getFloat(ReimbursementsPDFSettingCodes.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.REIMBURSEMENTS_PDF,
								ReimbursementsPDFDefaultSettings.PAYMENT_TABLE_BLOCK_FRAME_LINE_WIDTH));
			}
		}
	}

	@Override
	protected void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException {
		PDPageContentStream contentStream = writer.openContentStream(page);
		PDFUtil.renderTextLine(
				contentStream,
				fontD,
				PDFUtil.FontSize.TINY,
				Settings.getColor(ReimbursementsPDFSettingCodes.TEXT_COLOR, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getReimbursementsPDFLabel(Locales.REIMBURSEMENTS_PDF, ReimbursementsPDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(ReimbursementsPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAGE_LEFT_MARGIN)
						+ (pageWidth
								- Settings.getFloat(ReimbursementsPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAGE_LEFT_MARGIN)
								- Settings
										.getFloat(ReimbursementsPDFSettingCodes.PAGE_RIGHT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAGE_RIGHT_MARGIN))
								/ 2.0f,
				Settings.getFloat(ReimbursementsPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAGE_LOWER_MARGIN),
				PDFUtil.Alignment.BOTTOM_CENTER);
		writer.closeContentStream();
	}

	@Override
	public PDRectangle getDefaultPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	public PDFont getFontA() {
		return fontA;
	}

	public PDFont getFontB() {
		return fontB;
	}

	public PDFont getFontC() {
		return fontC;
	}

	public PDFont getFontD() {
		return fontD;
	}

	public PDFont getFontE() {
		return fontE;
	}

	public PDFont getFontF() {
		return fontF;
	}

	@Override
	public PageOrientation getPageOrientation() {
		if (Settings.getBoolean(ReimbursementsPDFSettingCodes.LANDSCAPE, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public ReimbursementsPDFVO getPdfVO() {
		return pdfVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(ReimbursementsPDFSettingCodes.TEMPLATE_FILE_NAME, cursor.getTrial());
		if (Settings.containsKey(key, Bundle.REIMBURSEMENTS_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.REIMBURSEMENTS_PDF, null);
		}
		return Settings.getPDFTemplateFilename(ReimbursementsPDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.REIMBURSEMENTS_PDF, null);
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(ReimbursementsPDFSettingCodes.FONT_A, Bundle.REIMBURSEMENTS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(ReimbursementsPDFSettingCodes.FONT_B, Bundle.REIMBURSEMENTS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(ReimbursementsPDFSettingCodes.FONT_C, Bundle.REIMBURSEMENTS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontD = PDFUtil.loadFont(Settings.getPDFFontName(ReimbursementsPDFSettingCodes.FONT_D, Bundle.REIMBURSEMENTS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontE = PDFUtil.loadFont(Settings.getPDFFontName(ReimbursementsPDFSettingCodes.FONT_E, Bundle.REIMBURSEMENTS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontF = PDFUtil.loadFont(Settings.getPDFFontName(ReimbursementsPDFSettingCodes.FONT_F, Bundle.REIMBURSEMENTS_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) {
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		ReimbursementsPDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && (BlockType.NEW_REIMBURSEMENT.equals(block.getType()) || BlockType.NEW_PAGE.equals(block.getType()))) {
			return false;
		} else {
			return (cursor.getBlockY() - block.getHeight(cursor)) > Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.REIMBURSEMENTS_PDF,
					ReimbursementsPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (probandVOs != null && addressVOMap != null && summaryMap != null) {
			Iterator<ProbandOutVO> probandIt = probandVOs.iterator();
			while (probandIt.hasNext()) {
				ProbandOutVO probandVO = probandIt.next();
				ProbandAddressOutVO addressVO = addressVOMap.get(probandVO.getId());
				MoneyTransferSummaryVO summary = summaryMap.get(probandVO.getId());
				if (summary != null && summary.getCount() > 0l) {
					blocks.add(new ReimbursementsPDFBlock(trialVO, probandVO, BlockType.NEW_REIMBURSEMENT));
					blocks.add(new ReimbursementsPDFBlock(addressVO, probandVO, BlockType.RECIPIENT_ADDRESS));
					blocks.add(new ReimbursementsPDFBlock(now, BlockType.FIRST_PAGE_DATE));
					blocks.add(new ReimbursementsPDFBlock(trialVO, BlockType.FIRST_PAGE_SUBJECT));
					blocks.add(new ReimbursementsPDFBlock(probandVO, BlockType.SALUTATION));
					if (trialVO != null) {
						blocks.add(new ReimbursementsPDFBlock(trialVO, BlockType.TRIAL_TITLE));
					}
					for (int i = 0; i < Settings.getInt(ReimbursementsPDFSettingCodes.COPIES, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.COPIES); i++) {
						blocks.add(new ReimbursementsPDFBlock(BlockType.NEW_PAGE));
						blocks.add(new ReimbursementsPDFBlock(addressVO, probandVO, BlockType.SENDER_ADDRESS));
						blocks.add(new ReimbursementsPDFBlock(trialVO, BlockType.RETURN_ADDRESS));
						blocks.add(new ReimbursementsPDFBlock(now, BlockType.SECOND_PAGE_DATE));
						blocks.add(new ReimbursementsPDFBlock(trialVO, probandVO, BlockType.SECOND_PAGE_SUBJECT));
						blocks.add(new ReimbursementsPDFBlock());
						if (trialTagValueVOs != null && trialTagValueVOs.size() > 0) {
							blocks.add(new ReimbursementsPDFBlock(trialTagValueVOs));
							blocks.add(new ReimbursementsPDFBlock());
						}
						blocks.add(new ReimbursementsPDFBlock(BlockType.MONEY_TRANSFER_TABLE_HEAD));
						Iterator it = summary.getTotalsByBankAccounts().iterator();
						while (it.hasNext()) {
							MoneyTransferByBankAccountSummaryDetailVO detail = (MoneyTransferByBankAccountSummaryDetailVO) it.next();
							if (detail.getCount() > 0l) {
								Iterator<MoneyTransferByBankAccountSummaryDetailVO> pager = createByCostTypeIterator(detail, cursor);
								boolean first = true;
								while (pager.hasNext()) {
									blocks.add(new ReimbursementsPDFBlock(pager.next(), first, !pager.hasNext()));
									first = false;
								}
							}
						}
						it = summary.getTotalsByPaymentMethods().iterator();
						while (it.hasNext()) {
							MoneyTransferByPaymentMethodSummaryDetailVO detail = (MoneyTransferByPaymentMethodSummaryDetailVO) it.next();
							if (detail.getCount() > 0l && !PaymentMethod.WIRE_TRANSFER.equals(detail.getMethod().getPaymentMethod())) {
								Iterator<MoneyTransferByPaymentMethodSummaryDetailVO> pager = createByCostTypeIterator(detail, cursor);
								boolean first = true;
								while (pager.hasNext()) {
									blocks.add(new ReimbursementsPDFBlock(pager.next(), first, !pager.hasNext()));
									first = false;
								}
							}
						}
						blocks.add(new ReimbursementsPDFBlock(probandVO, summary));
					}
				}
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		blockIndex = 0;
		pageWidth = DEFAULT_PAGE_SIZE.getWidth();
		pageHeight = DEFAULT_PAGE_SIZE.getHeight();
		cursor.setBlockY(pageHeight
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setProband(null);
		cursor.setTrial(null);
		fontA = null;
		fontB = null;
		fontC = null;
		fontD = null;
		fontE = null;
		fontF = null;
		updateReimbursementsPDFVO();
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		byte[] documentData = pdfStream.toByteArray();
		pdfVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		pdfVO.setSize(documentData.length);
		pdfVO.setDocumentDatas(documentData);
		return true;
	}

	public void setAddressVOMap(HashMap<Long, ProbandAddressOutVO> addressVOMap) {
		this.addressVOMap = addressVOMap;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	public void setProbandVOs(Collection<ProbandOutVO> probandVOs) {
		this.probandVOs = probandVOs;
	}

	public void setSummaryMap(HashMap<Long, MoneyTransferSummaryVO> summaryMap) {
		this.summaryMap = summaryMap;
	}

	public void setTrialTagValueVOs(Collection<TrialTagValueOutVO> trialTagValueVOs) {
		this.trialTagValueVOs = trialTagValueVOs;
	}

	public void setTrialVO(TrialOutVO trialVO) {
		this.trialVO = trialVO;
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.NEW_REIMBURSEMENT.equals(blocks.get(blockIndex).getType()),
				!hasNextBlock() || BlockType.NEW_REIMBURSEMENT.equals(blocks.get(blockIndex).getType())
						|| BlockType.NEW_PAGE.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ReimbursementsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	@Override
	public void updateCursor() {
		ReimbursementsPDFBlock block = blocks.get(blockIndex);
		if (BlockType.NEW_REIMBURSEMENT.equals(block.getType())) {
			cursor.setProband(block.getProband());
			cursor.setTrial(block.getTrial());
		}
	}

	private void updateReimbursementsPDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.setProbands(probandVOs);
		pdfVO.setTrial(trialVO);
		StringBuilder fileName = new StringBuilder(REIMBURSEMENTS_PDF_FILENAME_PREFIX);
		if (trialVO != null) {
			fileName.append(REIMBURSEMENTS_PDF_FILENAME_TRIAL);
			fileName.append(trialVO.getId());
			fileName.append("_");
		}
		if (probandVOs != null && probandVOs.size() == 1) {
			fileName.append(REIMBURSEMENTS_PDF_FILENAME_PROBAND);
			fileName.append(probandVOs.iterator().next().getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
	}
}
