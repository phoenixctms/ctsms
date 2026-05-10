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
import org.phoenixctms.ctsms.pdf.EcrfSpecificationPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFSpecificationPDFVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class EcrfSpecificationPDFPainter extends PDFPainterBase implements PDFOutput {

	protected int blockIndex;
	protected ArrayList<EcrfSpecificationPDFBlock> blocks;
	protected EcrfSpecificationPDFBlockCursor cursor;
	protected ECRFSpecificationPDFVO pdfVO;
	protected TrialOutVO trialVO;
	protected Collection<ECRFOutVO> ecrfVOs;
	protected HashMap<Long, Collection<ECRFFieldOutVO>> ecrfFieldVOMap;
	protected float pageWidth;
	protected float pageHeight;
	protected PDFont fontA;
	protected PDFont fontB;
	protected PDFont fontC;
	protected final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	protected static final String ECRF_SPECIFICATION_PDF_FILENAME_PREFIX = "ecrf_specification_";

	public EcrfSpecificationPDFPainter() {
		super();
		blocks = new ArrayList<EcrfSpecificationPDFBlock>();
		pdfVO = new ECRFSpecificationPDFVO();
		cursor = new EcrfSpecificationPDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(EcrfSpecificationPDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.ECRF_SPECIFICATION_PDF,
				EcrfSpecificationPDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	@Override
	public void drawNextBlock(PDPageContentStream contentStream) throws Exception {
		drawBlock(contentStream, blocks.get(blockIndex));
		blockIndex++;
	}

	@Override
	public void drawPage(PDPageContentStream contentStream) throws Exception {
		if (cursor.getEcrf() != null) {
			(new EcrfSpecificationPDFBlock(cursor.getEcrf(), BlockType.PAGE_TITLE)).renderBlock(contentStream, cursor);
		} else {
			(new EcrfSpecificationPDFBlock(trialVO, BlockType.PAGE_TITLE)).renderBlock(contentStream, cursor);
		}
	}

	@Override
	protected void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException {
		PDPageContentStream contentStream = writer.openContentStream(page);
		PDFUtil.renderTextLine(
				contentStream,
				fontA,
				PDFUtil.FontSize.TINY,
				Settings.getColor(EcrfSpecificationPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getEcrfSpecificationPDFLabel(Locales.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(EcrfSpecificationPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.PAGE_LEFT_MARGIN)
						+ (pageWidth
								- Settings.getFloat(EcrfSpecificationPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
										EcrfSpecificationPDFDefaultSettings.PAGE_LEFT_MARGIN)
								- Settings.getFloat(EcrfSpecificationPDFSettingCodes.PAGE_RIGHT_MARGIN,
										Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.PAGE_RIGHT_MARGIN))
								/ 2.0f,
				Settings.getFloat(
						EcrfSpecificationPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.PAGE_LOWER_MARGIN),
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
		if (Settings.getBoolean(EcrfSpecificationPDFSettingCodes.LANDSCAPE, Bundle.ECRF_SPECIFICATION_PDF, EcrfSpecificationPDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public ECRFSpecificationPDFVO getPdfVO() {
		return pdfVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(EcrfSpecificationPDFSettingCodes.TEMPLATE_FILE_NAME, trialVO); //cursor.getTrial());
		if (Settings.containsKey(key, Bundle.ECRF_SPECIFICATION_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.ECRF_SPECIFICATION_PDF, null);
		}
		return Settings.getPDFTemplateFilename(EcrfSpecificationPDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.ECRF_SPECIFICATION_PDF, null);
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_A, Bundle.ECRF_SPECIFICATION_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_B, Bundle.ECRF_SPECIFICATION_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_C, Bundle.ECRF_SPECIFICATION_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) throws Exception {
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		EcrfSpecificationPDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && BlockType.ECRF_HEAD.equals(block.getType())) {
			return false;
		} else {
			return (cursor.getBlockY() - block.getHeight(cursor)) > Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
					EcrfSpecificationPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public void drawPageBreakNewPage(PDPageContentStream contentStream) throws Exception {
		EcrfSpecificationPDFBlock block = blocks.get(blockIndex);
		if (BlockType.SPECIFICATION_TABLE_ROW.equals(block.getType())) {
			drawBlock(contentStream, new EcrfSpecificationPDFBlock(BlockType.SPECIFICATION_TABLE_HEAD));
		}
	}

	protected void drawBlock(PDPageContentStream contentStream, EcrfSpecificationPDFBlock block) throws Exception {
		cursor.setBlockY(cursor.getBlockY() - block.renderBlock(contentStream, cursor));
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (trialVO != null) {
			blocks.add(new EcrfSpecificationPDFBlock(trialVO, BlockType.HEAD));
			blocks.add(new EcrfSpecificationPDFBlock());
			blocks.add(new EcrfSpecificationPDFBlock(trialVO, BlockType.TRIAL_TITLE));
			blocks.add(new EcrfSpecificationPDFBlock(trialVO, BlockType.TRIAL_DESCRIPTION));
			blocks.add(new EcrfSpecificationPDFBlock());
			blocks.add(new EcrfSpecificationPDFBlock(BlockType.GENERATED_BY));
			blocks.add(new EcrfSpecificationPDFBlock(now));
			if (ecrfVOs != null) {
				Iterator<ECRFOutVO> ecrfIt = ecrfVOs.iterator();
				while (ecrfIt.hasNext()) {
					ECRFOutVO ecrfVO = ecrfIt.next();
					blocks.add(new EcrfSpecificationPDFBlock(ecrfVO, BlockType.ECRF_HEAD));
					blocks.add(new EcrfSpecificationPDFBlock());
					blocks.add(new EcrfSpecificationPDFBlock(ecrfVO, BlockType.ECRF_TITLE));
					blocks.add(new EcrfSpecificationPDFBlock(ecrfVO, BlockType.ECRF_REVISION));
					blocks.add(new EcrfSpecificationPDFBlock(ecrfVO, BlockType.ECRF_DESCRIPTION));
					blocks.add(new EcrfSpecificationPDFBlock());
					blocks.add(new EcrfSpecificationPDFBlock(ecrfVO, BlockType.ECRF_VISITS));
					blocks.add(new EcrfSpecificationPDFBlock(ecrfVO, BlockType.ECRF_GROUPS));
					blocks.add(new EcrfSpecificationPDFBlock());
					blocks.add(new EcrfSpecificationPDFBlock(BlockType.SPECIFICATION_TABLE_HEAD));
					Collection<ECRFFieldOutVO> ecrfFieldVOs = ecrfFieldVOMap.get(ecrfVO.getId());
					if (ecrfFieldVOs != null && ecrfFieldVOs.size() > 0) {
						Iterator<ECRFFieldOutVO> ecrfFieldVOsIt = ecrfFieldVOs.iterator();
						while (ecrfFieldVOsIt.hasNext()) {
							ECRFFieldOutVO ecrfFieldVO = ecrfFieldVOsIt.next();
							blocks.add(new EcrfSpecificationPDFBlock(ecrfFieldVO));
						}
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
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
				EcrfSpecificationPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setEcrf(null);
		fontA = null;
		fontB = null;
		fontC = null;
		updateECRFSpecificationPDFVO();
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		byte[] documentData = pdfStream.toByteArray();
		pdfVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		pdfVO.setSize(documentData.length);
		pdfVO.setDocumentDatas(documentData);
		return true;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
	}

	public void setTrialVO(TrialOutVO trialVO) {
		this.trialVO = trialVO;
	}

	public void setEcrfVOs(Collection<ECRFOutVO> ecrfVOs) {
		this.ecrfVOs = ecrfVOs;
	}

	public void setEcrfFieldVOMap(HashMap<Long, Collection<ECRFFieldOutVO>> ecrfFieldVOMap) {
		this.ecrfFieldVOMap = ecrfFieldVOMap;
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.HEAD.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
				EcrfSpecificationPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
						EcrfSpecificationPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	protected void updateECRFSpecificationPDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.setTrial(trialVO);
		StringBuilder fileName = new StringBuilder(ECRF_SPECIFICATION_PDF_FILENAME_PREFIX);
		fileName.append(trialVO.getId());
		fileName.append("_");
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
	}

	@Override
	public void updateCursor() {
		EcrfSpecificationPDFBlock block = blocks.get(blockIndex);
		if (BlockType.ECRF_HEAD.equals(block.getType())) {
			cursor.setEcrf(block.getEcrf());
		}
	}

	@Override
	public void splitNextBlock() throws Exception {
		EcrfSpecificationPDFBlock block = blocks.get(blockIndex);
		if (BlockType.SPECIFICATION_TABLE_ROW.equals(block.getType())) {
			ECRFFieldOutVO blockEcrfField = new ECRFFieldOutVO(block.ecrfField);
			blockEcrfField.setField(new InputFieldOutVO(blockEcrfField.getField()));
			ArrayList<InputFieldSelectionSetValueOutVO> selectionSetValues = new ArrayList<InputFieldSelectionSetValueOutVO>(blockEcrfField.getField().getSelectionSetValues());
			if (selectionSetValues.size() > 3) {
				blockEcrfField.getField().setSelectionSetValues(selectionSetValues);
				EcrfSpecificationPDFBlock testBlock = new EcrfSpecificationPDFBlock(blockEcrfField);
				//ArrayList<String> lines = PDFUtil.getTextLines(blockCourse.getDescription());
				int i;
				for (i = 0; i < selectionSetValues.size(); i++) {
					blockEcrfField.getField().setSelectionSetValues(selectionSetValues.subList(0, i + 1));
					if ((cursor.getBlockY()
							- testBlock.getHeight(cursor)) <= Settings.getFloat(EcrfSpecificationPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.ECRF_SPECIFICATION_PDF,
									EcrfSpecificationPDFDefaultSettings.BLOCKS_LOWER_MARGIN)) {
						break;
					}
				}
				if (i > 0 && i < selectionSetValues.size()) {
					//blockCourse.setDescription(PDFUtil.getLinesText(lines.subList(0, i)));
					blockEcrfField.getField().setSelectionSetValues(selectionSetValues.subList(0, i));
					blocks.add(blockIndex, new EcrfSpecificationPDFBlock(blockEcrfField));
					blockEcrfField = new ECRFFieldOutVO(block.ecrfField);
					blockEcrfField.setField(new InputFieldOutVO(blockEcrfField.getField()));
					selectionSetValues = new ArrayList<InputFieldSelectionSetValueOutVO>(selectionSetValues.subList(i, selectionSetValues.size()));
					blockEcrfField.getField().setSelectionSetValues(selectionSetValues);
					block.ecrfField = blockEcrfField;
				}
			}
		}
	}
}
