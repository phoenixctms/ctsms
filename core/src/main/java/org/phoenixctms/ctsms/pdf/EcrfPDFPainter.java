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
import org.phoenixctms.ctsms.pdf.EcrfPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class EcrfPDFPainter extends PDFPainterBase implements PDFOutput {

	private int blockIndex;
	private ArrayList<EcrfPDFBlock> blocks;
	private EcrfPDFBlockCursor cursor;
	private HashMap<Long, HashMap<Long, PDFJpeg>> images;
	private ECRFPDFVO pdfVO;
	private Collection<ProbandListEntryOutVO> listEntryVOs;
	private HashMap<Long, Collection<ECRFOutVO>> ecrfVOMap;
	private HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>> valueVOMap;
	private HashMap<Long, HashMap<Long, HashMap<Long, Collection>>> logVOMap;
	private HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> listEntryTagValuesVOMap;
	private HashMap<Long, HashMap<Long, ECRFStatusEntryVO>> statusEntryVOMap;
	private HashMap<Long, SignatureVO> signatureVOMap;
	private HashMap<Long, InputFieldImageVO> imageVOMap;
	private boolean blank;
	private float pageWidth;
	private float pageHeight;
	private PDFont fontA;
	private PDFont fontB;
	private PDFont fontC;
	private PDFont fontD;
	private PDFJpeg checkboxCheckedImage;
	private PDFJpeg checkboxUncheckedImage;
	private PDFJpeg radioOnImage;
	private PDFJpeg radioOffImage;
	private PDFJpeg selectboxCheckedImage;
	private PDFJpeg selectboxUncheckedImage;
	private PDFJpeg checkboxCheckedPresetImage;
	private PDFJpeg radioOnPresetImage;
	private PDFJpeg selectboxCheckedPresetImage;
	private PDFJpeg signatureValidImage;
	private PDFJpeg signatureInvalidImage;
	private PDFJpeg signatureAvailableImage;
	private final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	private static final String ECRF_PDF_FILENAME_PREFIX = "ecrf_";

	public EcrfPDFPainter() {
		super();
		blocks = new ArrayList<EcrfPDFBlock>();
		images = new HashMap<Long, HashMap<Long, PDFJpeg>>();
		pdfVO = new ECRFPDFVO();
		cursor = new EcrfPDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(EcrfPDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	private void drawBlock(PDPageContentStream contentStream, EcrfPDFBlock block) throws Exception {
		if (BlockType.NEW_ECRF.equals(block.getType())) {
			cursor.setSectionY(cursor.getBlockY());
			cursor.setIndexY(cursor.getBlockY());
		} else if (BlockType.NEW_SECTION.equals(block.getType())) {
			cursor.setSectionY(cursor.getBlockY());
			cursor.setIndexY(cursor.getBlockY());
		} else if (BlockType.NEW_INDEX.equals(block.getType())) {
			cursor.setIndexY(cursor.getBlockY());
		}
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
		if (cursor.getEcrfBlock() != null) {
			(new EcrfPDFBlock(cursor.getEcrfBlock(), BlockType.PAGE_TITLE, true)).renderBlock(contentStream, cursor);
		}
	}

	@Override
	public void drawPageBreakNewPage(PDPageContentStream contentStream) throws Exception {
		EcrfPDFBlock block = blocks.get(blockIndex);
		if (BlockType.INPUT_FIELD.equals(block.getType())
				|| BlockType.AUDIT_TRAIL_VALUE.equals(block.getType())
				|| BlockType.FIELD_STATUS_ENTRY.equals(block.getType())
				|| BlockType.NEW_SECTION.equals(block.getType())
				|| BlockType.NEW_INDEX.equals(block.getType())) {
			// paint ecrf header again
			drawBlock(contentStream, new EcrfPDFBlock(cursor.getEcrfBlock(), true));
		}
		if (BlockType.INPUT_FIELD.equals(block.getType())
				|| BlockType.AUDIT_TRAIL_VALUE.equals(block.getType())
				|| BlockType.FIELD_STATUS_ENTRY.equals(block.getType())) {
			// start section+index again
			drawBlock(contentStream, new EcrfPDFBlock(cursor.getSectionBlock(), true));
			drawBlock(contentStream, new EcrfPDFBlock(cursor.getIndexBlock(), true));
		}
	}

	@Override
	public void drawPageBreakOldPage(PDPageContentStream contentStream) throws Exception {
		EcrfPDFBlock block = blocks.get(blockIndex - 1);
		if (BlockType.INPUT_FIELD.equals(block.getType())
				|| BlockType.AUDIT_TRAIL_VALUE.equals(block.getType())
				|| BlockType.FIELD_STATUS_ENTRY.equals(block.getType())) {
			drawBlock(contentStream, new EcrfPDFBlock(BlockType.END_OF_INDEX, true));
			drawBlock(contentStream, new EcrfPDFBlock(BlockType.END_OF_SECTION, true));
		}
	}

	@Override
	protected void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException {
		PDPageContentStream contentStream = writer.openContentStream(page);
		PDFUtil.renderTextLine(
				contentStream,
				fontA,
				PDFUtil.FontSize.TINY,
				Settings.getColor(EcrfPDFSettingCodes.TEXT_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getEcrfPDFLabel(Locales.ECRF_PDF, EcrfPDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(EcrfPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.PAGE_LEFT_MARGIN)
						+ (pageWidth - Settings.getFloat(EcrfPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.PAGE_LEFT_MARGIN) - Settings.getFloat(
								EcrfPDFSettingCodes.PAGE_RIGHT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.PAGE_RIGHT_MARGIN)) / 2.0f,
				Settings.getFloat(EcrfPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.PAGE_LOWER_MARGIN), PDFUtil.Alignment.BOTTOM_CENTER);
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

	private ECRFStatusEntryVO getEcrfStatusEntry(Long listEntryId, Long ecrfId) {
		if (statusEntryVOMap != null) {
			HashMap<Long, ECRFStatusEntryVO> ecrfMap = statusEntryVOMap.get(listEntryId);
			if (ecrfMap != null) {
				return ecrfMap.get(ecrfId);
			}
		}
		return null;
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
		if (Settings.getBoolean(EcrfPDFSettingCodes.LANDSCAPE, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public ECRFPDFVO getPdfVO() {
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

	public PDFJpeg getSignatureAvailableImage() {
		return signatureAvailableImage;
	}

	public PDFJpeg getSignatureInvalidImage() {
		return signatureInvalidImage;
	}

	public PDFJpeg getSignatureValidImage() {
		return signatureValidImage;
	}

	private PDFJpeg getSketchImage(ECRFFieldValueOutVO value) {
		InputFieldOutVO field = value.getEcrfField().getField();
		HashMap<Long, PDFJpeg> sketchImages = images.get(field.getId());
		if (sketchImages != null) {
			return sketchImages.get(value.getId());
		}
		return null;
	}

	private PDFJpeg getSketchImage(ProbandListEntryTagValueOutVO value) {
		InputFieldOutVO field = value.getTag().getField();
		HashMap<Long, PDFJpeg> sketchImages = images.get(field.getId());
		if (sketchImages != null) {
			return sketchImages.get(value.getId());
		}
		return null;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(EcrfPDFSettingCodes.TEMPLATE_FILE_NAME, cursor.getListEntry() != null ? cursor.getListEntry().getTrial() : (TrialOutVO) null);
		if (Settings.containsKey(key, Bundle.ECRF_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.ECRF_PDF, null);
		}
		return Settings.getPDFTemplateFilename(EcrfPDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.ECRF_PDF, null);
	}

	private Collection getValueLog(ECRFFieldValueOutVO value) {
		if (logVOMap != null) {
			HashMap<Long, HashMap<Long, Collection>> ecrfLogVOMap = logVOMap.get(value.getListEntry().getId());
			if (ecrfLogVOMap != null) {
				HashMap<Long, Collection> fieldLogVOMap = ecrfLogVOMap.get(value.getEcrfField().getId());
				return fieldLogVOMap.get(value.getIndex());
			}
		}
		return null;
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(EcrfPDFSettingCodes.FONT_A, Bundle.ECRF_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(EcrfPDFSettingCodes.FONT_B, Bundle.ECRF_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(EcrfPDFSettingCodes.FONT_C, Bundle.ECRF_PDF, null), doc, DEFAULT_BASE_FONT);
		fontD = PDFUtil.loadFont(Settings.getPDFFontName(EcrfPDFSettingCodes.FONT_D, Bundle.ECRF_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) throws Exception {
		int quality = Settings.getInt(EcrfPDFSettingCodes.SELECTION_ITEM_IMAGE_QUALITY, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SELECTION_ITEM_IMAGE_QUALITY);
		int dpi = Settings.getInt(EcrfPDFSettingCodes.SELECTION_ITEM_IMAGE_DPI, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SELECTION_ITEM_IMAGE_DPI);
		Color bgColor = Settings.getColor(EcrfPDFSettingCodes.SELECTION_ITEM_IMAGE_BG_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SELECTION_ITEM_IMAGE_BG_COLOR);
		int selectionItemImageWidth = Settings.getInt(EcrfPDFSettingCodes.SELECTION_ITEM_IMAGE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SELECTION_ITEM_IMAGE_WIDTH);
		int selectionItemImageHeight = Settings.getInt(EcrfPDFSettingCodes.SELECTION_ITEM_IMAGE_HEIGHT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SELECTION_ITEM_IMAGE_HEIGHT);
		checkboxCheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.CHECKBOX_CHECKED_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		checkboxCheckedPresetImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.CHECKBOX_CHECKED_PRESET_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		checkboxUncheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.CHECKBOX_UNCHECKED_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		radioOnImage = PDFJpeg.prepareScaledImage(doc, PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.RADIO_ON_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		radioOnPresetImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.RADIO_ON_PRESET_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		radioOffImage = PDFJpeg.prepareScaledImage(doc, PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.RADIO_OFF_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		selectboxCheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.SELECTBOX_CHECKED_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		selectboxCheckedPresetImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.SELECTBOX_CHECKED_PRESET_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		selectboxUncheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.SELECTBOX_UNCHECKED_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		quality = Settings.getInt(EcrfPDFSettingCodes.SIGNATURE_IMAGE_QUALITY, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_IMAGE_QUALITY);
		dpi = Settings.getInt(EcrfPDFSettingCodes.SIGNATURE_IMAGE_DPI, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_IMAGE_DPI);
		bgColor = Settings.getColor(EcrfPDFSettingCodes.SIGNATURE_IMAGE_BG_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_IMAGE_BG_COLOR);
		int signatureImageWidth = Settings.getInt(EcrfPDFSettingCodes.SIGNATURE_IMAGE_WIDTH, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_IMAGE_WIDTH);
		int signatureImageHeight = Settings.getInt(EcrfPDFSettingCodes.SIGNATURE_IMAGE_HEIGHT, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SIGNATURE_IMAGE_HEIGHT);
		signatureValidImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.SIGNATURE_VALID_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				signatureImageWidth, signatureImageHeight, quality, dpi, bgColor);
		signatureInvalidImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.SIGNATURE_INVALID_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				signatureImageWidth, signatureImageHeight, quality, dpi, bgColor);
		signatureAvailableImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(EcrfPDFSettingCodes.SIGNATURE_AVAILABLE_IMAGE_FILE_NAME, Bundle.ECRF_PDF, null)),
				signatureImageWidth, signatureImageHeight, quality, dpi, bgColor);
		if (imageVOMap != null && Settings.getBoolean(EcrfPDFSettingCodes.RENDER_SKETCH_IMAGES, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.RENDER_SKETCH_IMAGES)) {
			if (valueVOMap != null) {
				Iterator<HashMap<Long, Collection<ECRFFieldValueOutVO>>> listEntryMapIt = valueVOMap.values().iterator();
				while (listEntryMapIt.hasNext()) {
					Iterator<Collection<ECRFFieldValueOutVO>> ecrfMapIt = listEntryMapIt.next().values().iterator();
					while (ecrfMapIt.hasNext()) {
						Iterator<ECRFFieldValueOutVO> valuesIt = ecrfMapIt.next().iterator();
						while (valuesIt.hasNext()) {
							putSketchImage(valuesIt.next(), doc);
						}
					}
				}
			}
			if (listEntryTagValuesVOMap != null) {
				Iterator<Collection<ProbandListEntryTagValueOutVO>> listEntryCollectionIt = listEntryTagValuesVOMap.values().iterator();
				while (listEntryCollectionIt.hasNext()) {
					Iterator<ProbandListEntryTagValueOutVO> valuesIt = listEntryCollectionIt.next().iterator();
					while (valuesIt.hasNext()) {
						putSketchImage(valuesIt.next(), doc);
					}
				}
			}
		}
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		EcrfPDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && BlockType.NEW_ECRF.equals(block.getType())) {
			return false;
		} else {
			float height = 0.0f;
			if (BlockType.NEW_SECTION.equals(block.getType())) {
				if (cursor.getBlockY() > Settings.getFloat(EcrfPDFSettingCodes.SECTION_PAGE_BREAK_LOWER_MARGIN, Bundle.ECRF_PDF,
						EcrfPDFDefaultSettings.SECTION_PAGE_BREAK_LOWER_MARGIN)) {
					height += blocks.get(blockIndex + 1).getHeight(cursor);
					height += blocks.get(blockIndex + 2).getHeight(cursor);
				} else {
					return false;
				}
			} else if (BlockType.NEW_INDEX.equals(block.getType())) {
				height += blocks.get(blockIndex + 1).getHeight(cursor);
			} else if (BlockType.END_OF_INDEX.equals(block.getType())) {
				return true;
			} else if (BlockType.END_OF_SECTION.equals(block.getType())) {
				return true;
			}
			height += block.getHeight(cursor);
			return (cursor.getBlockY() - height) > Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.ECRF_PDF,
					EcrfPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (listEntryVOs != null && ecrfVOMap != null && valueVOMap != null) {
			Iterator<ProbandListEntryOutVO> listEntryIt = listEntryVOs.iterator();
			while (listEntryIt.hasNext()) {
				ProbandListEntryOutVO listEntryVO = listEntryIt.next();
				Collection<ECRFOutVO> ecrfVOs = ecrfVOMap.get(listEntryVO == null ? null : listEntryVO.getId());
				if (ecrfVOs != null) {
					Iterator<ECRFOutVO> ecrfIt = ecrfVOs.iterator();
					while (ecrfIt.hasNext()) {
						ECRFOutVO ecrfVO = ecrfIt.next();
						ECRFStatusEntryVO statusEntryVO = getEcrfStatusEntry(listEntryVO == null ? null : listEntryVO.getId(), ecrfVO.getId());
						SignatureVO signatureVO = ((signatureVOMap != null && statusEntryVO != null) ? signatureVOMap.get(statusEntryVO.getId()) : null);
						blocks.add(new EcrfPDFBlock(listEntryVO, ecrfVO, statusEntryVO, signatureVO, now, blank));
						if (listEntryTagValuesVOMap != null) {
							Collection<ProbandListEntryTagValueOutVO> tagValueVOs = listEntryTagValuesVOMap.get(listEntryVO == null ? null : listEntryVO.getId());
							if (tagValueVOs != null) {
								Iterator<ProbandListEntryTagValueOutVO> tagValueVOsIt = tagValueVOs.iterator();
								while (tagValueVOsIt.hasNext()) {
									ProbandListEntryTagValueOutVO tagValueVO = tagValueVOsIt.next();
									blocks.add(new EcrfPDFBlock(tagValueVO, getSketchImage(tagValueVO), blank, tagValueVOsIt.hasNext()));
								}
							}
						}
						HashMap<Long, Collection<ECRFFieldValueOutVO>> ecrfValueVOMap = valueVOMap.get(listEntryVO == null ? null : listEntryVO.getId());
						if (ecrfValueVOMap != null) {
							Collection<ECRFFieldValueOutVO> valueVOs = ecrfValueVOMap.get(ecrfVO.getId());
							if (valueVOs != null && valueVOs.size() > 0) {
								boolean first = true;
								String previousSection = null;
								Long previousIndex = null;
								Iterator<ECRFFieldValueOutVO> valueIt = valueVOs.iterator();
								while (valueIt.hasNext()) {
									ECRFFieldValueOutVO valueVO = valueIt.next();
									String section = valueVO.getEcrfField().getSection();
									Long index = valueVO.getIndex();
									if (first) {
										// first section
										blocks.add(new EcrfPDFBlock(section));
										blocks.add(new EcrfPDFBlock(section, index));
										first = false;
									} else {
										if (previousSection == null ? section == null : previousSection.equals(section)) {
											if (previousIndex == null ? index == null : previousIndex.equals(index)) {
												// same section, same index
											} else {
												// same section, new index
												blocks.add(new EcrfPDFBlock(BlockType.END_OF_INDEX, false));
												blocks.add(new EcrfPDFBlock(section, index));
											}
										} else {
											// new section
											blocks.add(new EcrfPDFBlock(BlockType.END_OF_INDEX, false));
											blocks.add(new EcrfPDFBlock(BlockType.END_OF_SECTION, false));
											blocks.add(new EcrfPDFBlock(section));
											blocks.add(new EcrfPDFBlock(section, index));
										}
									}
									Collection valueLog = getValueLog(valueVO);
									boolean hasLog = (valueLog != null && valueLog.size() > 0);
									blocks.add(new EcrfPDFBlock(valueVO, getSketchImage(valueVO), blank, hasLog));
									if (valueLog != null && valueLog.size() > 0) {
										Iterator valueLogIt = valueLog.iterator();
										while (valueLogIt.hasNext()) {
											blocks.add(new EcrfPDFBlock(valueLogIt.next(), valueLogIt.hasNext()));
										}
									}
									previousSection = section;
									previousIndex = index;
								}
								blocks.add(new EcrfPDFBlock(BlockType.END_OF_INDEX, false));
								blocks.add(new EcrfPDFBlock(BlockType.END_OF_SECTION, false));
							}
						}
					}
				}
			}
		}
	}

	private boolean putSketchImage(ECRFFieldValueOutVO value, PDDocument doc) throws Exception {
		InputFieldOutVO field = value.getEcrfField().getField();
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
				int quality = Settings.getInt(EcrfPDFSettingCodes.SKETCH_IMAGE_QUALITY, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SKETCH_IMAGE_QUALITY);
				int dpi = Settings.getInt(EcrfPDFSettingCodes.SKETCH_IMAGE_DPI, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SKETCH_IMAGE_DPI);
				Color bgColor = Settings.getColor(EcrfPDFSettingCodes.SKETCH_IMAGE_BG_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SKETCH_IMAGE_BG_COLOR);
				sketchImages.put(value.getId(),
						PDFJpeg.prepareSketchImage(
								doc,
								field,
								inputFieldImage,
								value.getInkValues(),
								Settings.getBoolean(EcrfPDFSettingCodes.SHOW_SKETCH_REGIONS, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SHOW_SKETCH_REGIONS),
								!blank,
								cursor.getBlockIndentedWidth(false),
								quality, dpi, bgColor));
				return true;
			}
		}
		return false;
	}

	private boolean putSketchImage(ProbandListEntryTagValueOutVO value, PDDocument doc) throws Exception {
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
				int quality = Settings.getInt(EcrfPDFSettingCodes.SKETCH_IMAGE_QUALITY, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SKETCH_IMAGE_QUALITY);
				int dpi = Settings.getInt(EcrfPDFSettingCodes.SKETCH_IMAGE_DPI, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SKETCH_IMAGE_DPI);
				Color bgColor = Settings.getColor(EcrfPDFSettingCodes.SKETCH_IMAGE_BG_COLOR, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SKETCH_IMAGE_BG_COLOR);
				sketchImages.put(value.getId(),
						PDFJpeg.prepareSketchImage(
								doc,
								field,
								inputFieldImage,
								value.getInkValues(),
								Settings.getBoolean(EcrfPDFSettingCodes.SHOW_SKETCH_REGIONS, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.SHOW_SKETCH_REGIONS),
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
		cursor.setBlockY(pageHeight - Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth - Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
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
		signatureValidImage = null;
		signatureInvalidImage = null;
		signatureAvailableImage = null;
		images.clear();
		updateECRFPDFVO();
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

	public void setEcrfVOMap(HashMap<Long, Collection<ECRFOutVO>> ecrfVOMap) {
		this.ecrfVOMap = ecrfVOMap;
	}

	public void setImageVOMap(HashMap<Long, InputFieldImageVO> imageVOMap) {
		this.imageVOMap = imageVOMap;
	}

	public void setListEntryTagValuesVOMap(HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> listEntryTagValuesVOMap) {
		this.listEntryTagValuesVOMap = listEntryTagValuesVOMap;
	}

	public void setListEntryVOs(Collection<ProbandListEntryOutVO> listEntryVOs) {
		this.listEntryVOs = listEntryVOs;
	}

	public void setLogVOMap(HashMap<Long, HashMap<Long, HashMap<Long, Collection>>> logVOMap) {
		this.logVOMap = logVOMap;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight - Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setSectionY(cursor.getBlockY());
		cursor.setIndexY(cursor.getBlockY());
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(pageWidth - Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	public void setSignatureVOMap(HashMap<Long, SignatureVO> signatureVOMap) {
		this.signatureVOMap = signatureVOMap;
	}

	public void setStatusEntryVOMap(HashMap<Long, HashMap<Long, ECRFStatusEntryVO>> statusEntryVOMap) {
		this.statusEntryVOMap = statusEntryVOMap;
	}

	public void setValueVOMap(HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>> valueVOMap) {
		this.valueVOMap = valueVOMap;
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.NEW_ECRF.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight - Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth - Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(EcrfPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setSectionY(cursor.getBlockY());
		cursor.setIndexY(cursor.getBlockY());
	}

	@Override
	public void updateCursor() {
		EcrfPDFBlock block = blocks.get(blockIndex);
		if (BlockType.NEW_ECRF.equals(block.getType())) {
			cursor.setListEntry(block.getListEntry());
		}
	}

	private void updateECRFPDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.getStatusEntries().clear();
		if (listEntryVOs != null && ecrfVOMap != null) {
			Iterator<ProbandListEntryOutVO> listEntryIt = listEntryVOs.iterator();
			while (listEntryIt.hasNext()) {
				ProbandListEntryOutVO listEntryVO = listEntryIt.next();
				Collection<ECRFOutVO> ecrfVOs = ecrfVOMap.get(listEntryVO == null ? null : listEntryVO.getId());
				if (ecrfVOs != null) {
					Iterator<ECRFOutVO> ecrfIt = ecrfVOs.iterator();
					while (ecrfIt.hasNext()) {
						ECRFOutVO ecrfVO = ecrfIt.next();
						ECRFStatusEntryVO statusEntryVO = getEcrfStatusEntry(listEntryVO == null ? null : listEntryVO.getId(), ecrfVO.getId());
						if (statusEntryVO == null) {
							statusEntryVO = new ECRFStatusEntryVO();
							statusEntryVO.setEcrf(ecrfVO);
							statusEntryVO.setListEntry(listEntryVO);
						}
						pdfVO.getStatusEntries().add(statusEntryVO);
					}
				}
			}
		}
		StringBuilder fileName = new StringBuilder(ECRF_PDF_FILENAME_PREFIX);
		if (listEntryVOs != null && listEntryVOs.size() == 1) {
			ProbandListEntryOutVO listEntryVO = listEntryVOs.iterator().next();
			if (listEntryVO != null) {
				fileName.append(listEntryVO.getId());
				fileName.append("_");
			}
			Collection<ECRFOutVO> ecrfVOs;
			if (ecrfVOMap != null && (ecrfVOs = ecrfVOMap.get(listEntryVO == null ? null : listEntryVO.getId())) != null && ecrfVOs.size() == 1) {
				fileName.append(ecrfVOs.iterator().next().getId());
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
