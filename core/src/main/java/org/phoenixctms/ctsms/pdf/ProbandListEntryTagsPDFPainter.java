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
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.pdf.ProbandListEntryTagsPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagsPDFVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class ProbandListEntryTagsPDFPainter extends PDFPainterBase implements PDFOutput {

	protected int blockIndex;
	protected ArrayList<ProbandListEntryTagsPDFBlock> blocks;
	protected ProbandListEntryTagsPDFBlockCursor cursor;
	protected HashMap<Long, HashMap<Long, PDFJpeg>> images;
	protected ProbandListEntryTagsPDFVO pdfVO;
	protected Collection<ProbandListEntryOutVO> listEntryVOs;
	protected HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> valueVOMap;
	protected HashMap<Long, InputFieldImageVO> imageVOMap;
	protected boolean blank;
	protected float pageWidth;
	protected float pageHeight;
	protected PDFont fontA;
	protected PDFont fontB;
	protected PDFont fontC;
	protected PDFont fontD;
	protected PDFJpeg checkboxCheckedImage;
	protected PDFJpeg checkboxUncheckedImage;
	protected PDFJpeg radioOnImage;
	protected PDFJpeg radioOffImage;
	protected PDFJpeg selectboxCheckedImage;
	protected PDFJpeg selectboxUncheckedImage;
	protected PDFJpeg checkboxCheckedPresetImage;
	protected PDFJpeg radioOnPresetImage;
	protected PDFJpeg selectboxCheckedPresetImage;
	protected final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	protected static final String PROBAND_LIST_ENTRY_TAGS_PDF_FILENAME_PREFIX = "probandlistentrytags_";

	public ProbandListEntryTagsPDFPainter() {
		super();
		blocks = new ArrayList<ProbandListEntryTagsPDFBlock>();
		images = new HashMap<Long, HashMap<Long, PDFJpeg>>();
		pdfVO = new ProbandListEntryTagsPDFVO();
		cursor = new ProbandListEntryTagsPDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	protected void drawBlock(PDPageContentStream contentStream, ProbandListEntryTagsPDFBlock block) throws Exception {
		cursor.setBlocks(block);
		cursor.setBlockY(cursor.getBlockY() - block.renderBlock(contentStream, cursor));
	}

	@Override
	public void drawNextBlock(PDPageContentStream contentStream) throws Exception {
		drawBlock(contentStream, blocks.get(blockIndex));
		blockIndex++;
	}

	@Override
	public void drawPage(PDPageContentStream contentStream) throws Exception {
		if (cursor.getListEntryBlock() != null) {
			(new ProbandListEntryTagsPDFBlock(cursor.getListEntryBlock(), BlockType.PAGE_TITLE, true)).renderBlock(contentStream, cursor);
		}
	}

	@Override
	public void drawPageBreakNewPage(PDPageContentStream contentStream) throws Exception {
	}

	@Override
	public void drawPageBreakOldPage(PDPageContentStream contentStream) throws Exception {
	}

	@Override
	protected void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException {
		PDPageContentStream contentStream = writer.openContentStream(page);
		PDFUtil.renderTextLine(
				contentStream,
				fontA,
				PDFUtil.FontSize.TINY,
				Settings.getColor(ProbandListEntryTagsPDFSettingCodes.TEXT_COLOR, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getProbandListEntryTagsPDFLabel(Locales.PROBAND_LIST_ENTRY_TAGS_PDF, EcrfPDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.PAGE_LEFT_MARGIN)
						+ (pageWidth
								- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.PAGE_LEFT_MARGIN)
								- Settings
										.getFloat(
												ProbandListEntryTagsPDFSettingCodes.PAGE_RIGHT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
												ProbandListEntryTagsPDFDefaultSettings.PAGE_RIGHT_MARGIN))
								/ 2.0f,
				Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.PAGE_LOWER_MARGIN),
				PDFUtil.Alignment.BOTTOM_CENTER);
		writer.closeContentStream();
	}

	public PDFJpeg getCheckboxCheckedImage() {
		return checkboxCheckedImage;
	}

	public PDFJpeg getCheckboxCheckedPresetImage() {
		return checkboxCheckedPresetImage;
	}

	public PDFJpeg getCheckboxUncheckedImage() {
		return checkboxUncheckedImage;
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

	@Override
	public PageOrientation getPageOrientation() {
		if (Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.LANDSCAPE, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, ProbandListEntryTagsPDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public ProbandListEntryTagsPDFVO getPdfVO() {
		return pdfVO;
	}

	public PDFJpeg getRadioOffImage() {
		return radioOffImage;
	}

	public PDFJpeg getRadioOnImage() {
		return radioOnImage;
	}

	public PDFJpeg getRadioOnPresetImage() {
		return radioOnPresetImage;
	}

	public PDFJpeg getSelectboxCheckedImage() {
		return selectboxCheckedImage;
	}

	public PDFJpeg getSelectboxCheckedPresetImage() {
		return selectboxCheckedPresetImage;
	}

	public PDFJpeg getSelectboxUncheckedImage() {
		return selectboxUncheckedImage;
	}

	protected PDFJpeg getSketchImage(ProbandListEntryTagValueOutVO value) {
		InputFieldOutVO field = value.getTag().getField();
		HashMap<Long, PDFJpeg> sketchImages = images.get(field.getId());
		if (sketchImages != null) {
			return sketchImages.get(value.getId());
		}
		return null;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(ProbandListEntryTagsPDFSettingCodes.TEMPLATE_FILE_NAME, cursor.getListEntry() != null ? cursor.getListEntry().getTrial()
				: (TrialOutVO) null);
		if (Settings.containsKey(key, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null);
		}
		return Settings.getPDFTemplateFilename(ProbandListEntryTagsPDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null);
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(ProbandListEntryTagsPDFSettingCodes.FONT_A, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(ProbandListEntryTagsPDFSettingCodes.FONT_B, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(ProbandListEntryTagsPDFSettingCodes.FONT_C, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null), doc, DEFAULT_BASE_FONT);
		fontD = PDFUtil.loadFont(Settings.getPDFFontName(ProbandListEntryTagsPDFSettingCodes.FONT_D, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) throws Exception {
		int quality = Settings.getInt(ProbandListEntryTagsPDFSettingCodes.SELECTION_ITEM_IMAGE_QUALITY, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SELECTION_ITEM_IMAGE_QUALITY);
		int dpi = Settings.getInt(ProbandListEntryTagsPDFSettingCodes.SELECTION_ITEM_IMAGE_DPI, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SELECTION_ITEM_IMAGE_DPI);
		Color bgColor = Settings.getColor(ProbandListEntryTagsPDFSettingCodes.SELECTION_ITEM_IMAGE_BG_COLOR, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SELECTION_ITEM_IMAGE_BG_COLOR);
		int selectionItemImageWidth = Settings.getInt(ProbandListEntryTagsPDFSettingCodes.SELECTION_ITEM_IMAGE_WIDTH, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SELECTION_ITEM_IMAGE_WIDTH);
		int selectionItemImageHeight = Settings.getInt(ProbandListEntryTagsPDFSettingCodes.SELECTION_ITEM_IMAGE_HEIGHT, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.SELECTION_ITEM_IMAGE_HEIGHT);
		checkboxCheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.CHECKBOX_CHECKED_IMAGE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		checkboxCheckedPresetImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.CHECKBOX_CHECKED_PRESET_IMAGE_FILE_NAME,
						Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		checkboxUncheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.CHECKBOX_UNCHECKED_IMAGE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		radioOnImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.RADIO_ON_IMAGE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		radioOnPresetImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.RADIO_ON_PRESET_IMAGE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		radioOffImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.RADIO_OFF_IMAGE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		selectboxCheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.SELECTBOX_CHECKED_IMAGE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		selectboxCheckedPresetImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.SELECTBOX_CHECKED_PRESET_IMAGE_FILE_NAME,
						Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		selectboxUncheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(ProbandListEntryTagsPDFSettingCodes.SELECTBOX_UNCHECKED_IMAGE_FILE_NAME, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		if (valueVOMap != null
				&& imageVOMap != null
				&& Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.RENDER_SKETCH_IMAGES, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.RENDER_SKETCH_IMAGES)) {
			Iterator<Collection<ProbandListEntryTagValueOutVO>> listEntryMapIt = valueVOMap.values().iterator();
			while (listEntryMapIt.hasNext()) {
				Iterator<ProbandListEntryTagValueOutVO> valuesIt = listEntryMapIt.next().iterator();
				while (valuesIt.hasNext()) {
					putSketchImage(valuesIt.next(), doc);
				}
			}
		}
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		ProbandListEntryTagsPDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && BlockType.NEW_LIST_ENTRY.equals(block.getType())) {
			return false;
		} else {
			float height = block.getHeight(cursor);
			return (cursor.getBlockY() - height) > Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
					ProbandListEntryTagsPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (listEntryVOs != null && valueVOMap != null) {
			Iterator<ProbandListEntryOutVO> listEntryIt = listEntryVOs.iterator();
			while (listEntryIt.hasNext()) {
				ProbandListEntryOutVO listEntryVO = listEntryIt.next();
				blocks.add(new ProbandListEntryTagsPDFBlock(listEntryVO, now, blank));
				Collection<ProbandListEntryTagValueOutVO> valueVOs = valueVOMap.get(listEntryVO == null ? null : listEntryVO.getId());
				if (valueVOs != null && valueVOs.size() > 0) {
					Iterator<ProbandListEntryTagValueOutVO> valueIt = valueVOs.iterator();
					while (valueIt.hasNext()) {
						ProbandListEntryTagValueOutVO valueVO = valueIt.next();
						blocks.add(new ProbandListEntryTagsPDFBlock(valueVO, getSketchImage(valueVO), blank));
					}
				}
			}
		}
	}

	protected boolean putSketchImage(ProbandListEntryTagValueOutVO value, PDDocument doc) throws Exception {
		InputFieldOutVO field = value.getTag().getField();
		InputFieldImageVO inputFieldImage = imageVOMap.get(field.getId());
		if (inputFieldImage != null) {
			HashMap<Long, PDFJpeg> sketchImages;
			if (images.containsKey(field.getId())) {
				sketchImages = images.get(field.getId());
			} else {
				sketchImages = new HashMap<Long, PDFJpeg>();
				images.put(field.getId(), sketchImages);
			}
			if (!sketchImages.containsKey(value.getId())) {
				int quality = Settings.getInt(ProbandListEntryTagsPDFSettingCodes.SKETCH_IMAGE_QUALITY, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.SKETCH_IMAGE_QUALITY);
				int dpi = Settings.getInt(ProbandListEntryTagsPDFSettingCodes.SKETCH_IMAGE_DPI, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.SKETCH_IMAGE_DPI);
				Color bgColor = Settings.getColor(ProbandListEntryTagsPDFSettingCodes.SKETCH_IMAGE_BG_COLOR, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.SKETCH_IMAGE_BG_COLOR);
				sketchImages.put(value.getId(),
						PDFJpeg.prepareSketchImage(
								doc,
								field,
								inputFieldImage,
								value.getInkValues(),
								Settings.getBoolean(ProbandListEntryTagsPDFSettingCodes.SHOW_SKETCH_REGIONS, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
										ProbandListEntryTagsPDFDefaultSettings.SHOW_SKETCH_REGIONS),
								!blank,
								cursor.getBlockIndentedWidth(false),
								quality, dpi, bgColor));
				return true;
			}
		}
		return false;
	}

	@Override
	public void reset() {
		super.reset();
		blockIndex = 0;
		pageWidth = DEFAULT_PAGE_SIZE.getWidth();
		pageHeight = DEFAULT_PAGE_SIZE.getHeight();
		cursor.setBlockY(pageHeight
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.clearBlocks();
		cursor.setListEntry(null);
		fontA = null;
		fontB = null;
		fontC = null;
		fontD = null;
		checkboxCheckedImage = null;
		checkboxCheckedPresetImage = null;
		checkboxUncheckedImage = null;
		radioOnImage = null;
		radioOnPresetImage = null;
		radioOffImage = null;
		selectboxCheckedImage = null;
		selectboxCheckedPresetImage = null;
		selectboxUncheckedImage = null;
		images.clear();
		updateProbandListEntryTagsPDFVO();
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		byte[] documentData = pdfStream.toByteArray();
		pdfVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		pdfVO.setSize(documentData.length);
		pdfVO.setDocumentDatas(documentData);
		return true;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	public void setImageVOMap(HashMap<Long, InputFieldImageVO> imageVOMap) {
		this.imageVOMap = imageVOMap;
	}

	public void setListEntryVOs(Collection<ProbandListEntryOutVO> listEntryVOs) {
		this.listEntryVOs = listEntryVOs;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	public void setValueVOMap(HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> valueVOMap) {
		this.valueVOMap = valueVOMap;
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.NEW_LIST_ENTRY.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight - Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(ProbandListEntryTagsPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
						ProbandListEntryTagsPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	@Override
	public void updateCursor() {
		ProbandListEntryTagsPDFBlock block = blocks.get(blockIndex);
		if (BlockType.NEW_LIST_ENTRY.equals(block.getType())) {
			cursor.setListEntry(block.getListEntry());
		}
	}

	protected void updateProbandListEntryTagsPDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.getListEntries().clear();
		if (listEntryVOs != null) {
			pdfVO.getListEntries().addAll(listEntryVOs);
		}
		StringBuilder fileName = new StringBuilder(PROBAND_LIST_ENTRY_TAGS_PDF_FILENAME_PREFIX);
		if (listEntryVOs != null && listEntryVOs.size() == 1) {
			ProbandListEntryOutVO listEntryVO = listEntryVOs.iterator().next();
			if (listEntryVO != null) {
				fileName.append(listEntryVO.getId());
				fileName.append("_");
			}
			if (blank) {
				fileName.append("blank");
				fileName.append("_");
			}
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
	}
}
