package org.phoenixctms.ctsms.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.pdf.ProbandLetterPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;

public class ProbandLetterPDFPainter extends PDFPainterBase implements PDFOutput {

	private int blockIndex;
	private ArrayList<ProbandLetterPDFBlock> blocks;
	private ProbandLetterPDFBlockCursor cursor;
	private ProbandLetterPDFVO pdfVO;
	private Collection<ProbandOutVO> probandVOs;
	private HashMap<Long, Collection<ProbandAddressOutVO>> addressVOMap;
	private float pageWidth;
	private float pageHeight;
	private PDFont fontA;
	private PDFont fontB;
	private PDFont fontC;
	private final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	private static final String PROBAND_LETTER_PDF_FILENAME_PREFIX = "proband_letter_";

	public ProbandLetterPDFPainter() {
		super();
		blocks = new ArrayList<ProbandLetterPDFBlock>();
		pdfVO = new ProbandLetterPDFVO();
		cursor = new ProbandLetterPDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(ProbandLetterPDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	@Override
	public void drawNextBlock(PDPageContentStream contentStream) throws Exception {
		ProbandLetterPDFBlock block = blocks.get(blockIndex);
		cursor.setBlockY(cursor.getBlockY() - block.renderBlock(contentStream, cursor));
		blockIndex++;
	}

	@Override
	public void drawPage(PDPageContentStream contentStream) throws Exception {
		// PDFUtil.renderFrame(contentStream, FRAME_COLOR, Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF,
		// ProbandLetterPDFDefaultSettings.PAGE_LEFT_MARGIN), Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.PROBAND_LETTER_PDF,
		// ProbandLetterPDFDefaultSettings.PAGE_LOWER_MARGIN), pageWidth - Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF,
		// ProbandLetterPDFDefaultSettings.PAGE_LEFT_MARGIN) - Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_RIGHT_MARGIN, Bundle.PROBAND_LETTER_PDF,
		// ProbandLetterPDFDefaultSettings.PAGE_RIGHT_MARGIN), pageHeight - PAGE_UPPER_MARGIN - Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_LOWER_MARGIN,
		// Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.PAGE_LOWER_MARGIN), PDFUtil.Alignment.BOTTOM_LEFT, PAGE_FRAME_LINE_WIDTH);
	}

	@Override
	protected void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException {
		PDPageContentStream contentStream = writer.openContentStream(page);
		PDFUtil.renderTextLine(
				contentStream,
				fontA,
				PDFUtil.FontSize.TINY,
				Settings.getColor(ProbandLetterPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getProbandLetterPDFLabel(Locales.PROBAND_LETTER_PDF, ProbandLetterPDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.PAGE_LEFT_MARGIN)
				+ (pageWidth
						- Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.PAGE_LEFT_MARGIN) - Settings
						.getFloat(ProbandLetterPDFSettingCodes.PAGE_RIGHT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.PAGE_RIGHT_MARGIN)) / 2.0f,
						Settings.getFloat(ProbandLetterPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.PAGE_LOWER_MARGIN),
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

	@Override
	public PageOrientation getPageOrientation() {
		if (Settings.getBoolean(ProbandLetterPDFSettingCodes.LANDSCAPE, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public ProbandLetterPDFVO getPdfVO() {
		return pdfVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(ProbandLetterPDFSettingCodes.TEMPLATE_FILE_NAME, cursor.getAddress() != null ? cursor.getAddress().getProband()
				: (ProbandOutVO) null);
		if (Settings.containsKey(key, Bundle.PROBAND_LETTER_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.PROBAND_LETTER_PDF, null);
		}
		return Settings.getPDFTemplateFilename(ProbandLetterPDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.PROBAND_LETTER_PDF, null);
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_A, Bundle.PROBAND_LETTER_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_B, Bundle.PROBAND_LETTER_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_C, Bundle.PROBAND_LETTER_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) throws Exception {
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		ProbandLetterPDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && (BlockType.NEW_LETTER.equals(block.getType()) || BlockType.NEW_PAGE.equals(block.getType()))) {
			return false;
		} else {
			return (cursor.getBlockY() - block.getHeight(cursor)) > Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.PROBAND_LETTER_PDF,
					ProbandLetterPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (probandVOs != null && addressVOMap != null) {
			Iterator<ProbandOutVO> probandIt = probandVOs.iterator();
			while (probandIt.hasNext()) {
				Collection<ProbandAddressOutVO> addressVOs = addressVOMap.get(probandIt.next().getId());
				if (addressVOs != null) {
					Iterator<ProbandAddressOutVO> addressIt = addressVOs.iterator();
					while (addressIt.hasNext()) {
						ProbandAddressOutVO addressVO = addressIt.next();
						// blocks.add(new ProbandLetterPDFBlock(true));
						blocks.add(new ProbandLetterPDFBlock(addressVO, BlockType.NEW_LETTER));
						blocks.add(new ProbandLetterPDFBlock(addressVO, BlockType.ADDRESS));
						blocks.add(new ProbandLetterPDFBlock(now, BlockType.FIRST_PAGE_DATE));
						blocks.add(new ProbandLetterPDFBlock(addressVO, BlockType.SALUTATION));
						// blocks.add(new ProbandLetterPDFBlock(false));
						blocks.add(new ProbandLetterPDFBlock(BlockType.NEW_PAGE));
						blocks.add(new ProbandLetterPDFBlock(now, BlockType.SECOND_PAGE_DATE));
						blocks.add(new ProbandLetterPDFBlock(addressVO, BlockType.PROBAND_ID));
						// blocks.add(new ProbandLetterPDFBlock(false));
						blocks.add(new ProbandLetterPDFBlock(BlockType.NEW_PAGE));
						blocks.add(new ProbandLetterPDFBlock(now, BlockType.SECOND_PAGE_DATE));
						blocks.add(new ProbandLetterPDFBlock(addressVO, BlockType.PROBAND_ID));
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
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setAddress(null);
		fontA = null;
		fontB = null;
		fontC = null;
		updateProbandLetterPDFVO();
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		byte[] documentData = pdfStream.toByteArray();
		pdfVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		pdfVO.setSize(documentData.length);
		pdfVO.setDocumentDatas(documentData);
		return true;
	}

	public void setAddressVOMap(
			HashMap<Long, Collection<ProbandAddressOutVO>> addressVOMap) {
		this.addressVOMap = addressVOMap;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	public void setProbandVOs(Collection<ProbandOutVO> probandVOs) {
		this.probandVOs = probandVOs;
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.NEW_LETTER.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ProbandLetterPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	@Override
	public void updateCursor() {
		ProbandLetterPDFBlock block = blocks.get(blockIndex);
		if (BlockType.NEW_LETTER.equals(block.getType())) {
			cursor.setAddress(block.getAddress());
		}
	}

	private void updateProbandLetterPDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.setProbands(probandVOs);
		StringBuilder fileName = new StringBuilder(PROBAND_LETTER_PDF_FILENAME_PREFIX);
		if (probandVOs != null && probandVOs.size() == 1) {
			Long probandId = probandVOs.iterator().next().getId();
			fileName.append(probandId);
			fileName.append("_");
			if (addressVOMap != null && addressVOMap.containsKey(probandId)
					&& addressVOMap.get(probandId).size() == 1) {
				fileName.append(addressVOMap.get(probandId).iterator().next().getId());
				fileName.append("_");
			}
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
	}
}
