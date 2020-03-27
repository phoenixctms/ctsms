package org.phoenixctms.ctsms.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.pdf.TrainingRecordPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordPDFVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;

public class TrainingRecordPDFPainter extends PDFPainterBase implements PDFOutput {

	protected int blockIndex;
	protected ArrayList<TrainingRecordPDFBlock> blocks;
	protected TrainingRecordPDFBlockCursor cursor;
	protected TrainingRecordPDFVO pdfVO;
	protected Collection<StaffOutVO> staffVOs;
	protected Collection<TrainingRecordSectionVO> allTrainingRecordSectionVOs;
	protected HashMap<Long, HashMap<Long, Collection<CourseParticipationStatusEntryOutVO>>> participationVOMap;
	protected HashMap<Long, Collection<StaffTagValueOutVO>> staffTagValueVOMap;
	protected float pageWidth;
	protected float pageHeight;
	protected PDFont fontA;
	protected PDFont fontB;
	protected PDFont fontC;
	protected PDFJpeg checkboxCheckedImage;
	protected PDFJpeg checkboxUncheckedImage;
	protected final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	protected static final String TRAINING_RECORD_PDF_FILENAME_PREFIX = "training_record_";

	public TrainingRecordPDFPainter() {
		super();
		blocks = new ArrayList<TrainingRecordPDFBlock>();
		pdfVO = new TrainingRecordPDFVO();
		cursor = new TrainingRecordPDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(TrainingRecordPDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	@Override
	public void drawNextBlock(PDPageContentStream contentStream) throws Exception {
		TrainingRecordPDFBlock block = blocks.get(blockIndex);
		cursor.setBlockY(cursor.getBlockY() - block.renderBlock(contentStream, cursor));
		blockIndex++;
	}

	protected String getSignatureLabel() {
		if (cursor.getStaff() != null && cursor.getStaff().isPerson()) {
			return L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.SIGNATURE_ANNOTATION, PDFUtil.DEFAULT_LABEL,
					CommonUtil.getCvStaffName(cursor.getStaff()),
					now == null ? null
							: Settings
									.getSimpleDateFormat(TrainingRecordPDFSettingCodes.SIGNATURE_DATE_PATTERN, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.SIGNATURE_DATE_PATTERN, Locales.TRAINING_RECORD_PDF)
									.format(now));
		}
		return "";
	}

	@Override
	public void drawPage(PDPageContentStream contentStream) throws Exception {
		if (cursor.getStaff() != null) {
			float y = Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_LOWER_MARGIN)
					- Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_UPPER_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.Y_FRAME_UPPER_INDENT_SIGNATURE);
			float x = Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_LEFT_MARGIN)
					+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE);
			float x1 = pageWidth
					- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
					- Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE)
					- Settings.getFloat(TrainingRecordPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SIGNATURE_LINE_LENGTH);
			if (Settings.getFloat(TrainingRecordPDFSettingCodes.DATE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.DATE_LINE_LENGTH) > 0.0f) {
				x1 -= Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE)
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.DATE_LABEL_WIDTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.DATE_LABEL_WIDTH)
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.DATE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.DATE_LINE_LENGTH);
			}
			y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
					Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
					L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.SIGNATURE, PDFUtil.DEFAULT_LABEL), x, y, PDFUtil.Alignment.TOP_LEFT,
					x1 - x - Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE));
			float y1 = y;
			PDFUtil.renderLine(contentStream,
					Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR), x1, y,
					x1 + Settings.getFloat(TrainingRecordPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SIGNATURE_LINE_LENGTH),
					y,
					Settings.getFloat(TrainingRecordPDFSettingCodes.SIGNATURE_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SIGNATURE_LINE_WIDTH));
			y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_OFFSET_SIGNATURE_ANNOTATION, Bundle.TRAINING_RECORD_PDF,
					TrainingRecordPDFDefaultSettings.Y_OFFSET_SIGNATURE_ANNOTATION);
			y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.TINY,
					Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR), getSignatureLabel(),
					x1 + Settings.getFloat(TrainingRecordPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SIGNATURE_LINE_LENGTH)
							/ 2.0f,
					y,
					PDFUtil.Alignment.TOP_CENTER);
			y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_LOWER_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
					TrainingRecordPDFDefaultSettings.Y_FRAME_LOWER_INDENT_SIGNATURE);
			if (Settings.getFloat(TrainingRecordPDFSettingCodes.DATE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.DATE_LINE_LENGTH) > 0.0f) {
				y = y1;
				x = x1 + Settings.getFloat(TrainingRecordPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SIGNATURE_LINE_LENGTH)
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE);
				x1 = x + Settings.getFloat(TrainingRecordPDFSettingCodes.DATE_LABEL_WIDTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.DATE_LABEL_WIDTH);
				PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.DATE, PDFUtil.DEFAULT_LABEL), x, y,
						PDFUtil.Alignment.BOTTOM_LEFT,
						x1 - x - Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE));
				PDFUtil.renderLine(contentStream,
						Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR), x1, y,
						x1 + Settings.getFloat(TrainingRecordPDFSettingCodes.DATE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.DATE_LINE_LENGTH), y,
						Settings.getFloat(TrainingRecordPDFSettingCodes.SIGNATURE_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SIGNATURE_LINE_WIDTH));
				y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_OFFSET_SIGNATURE_ANNOTATION, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_OFFSET_SIGNATURE_ANNOTATION);
				y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.TINY,
						Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.DATE_ANNOTATION, PDFUtil.DEFAULT_LABEL),
						x1 + Settings.getFloat(TrainingRecordPDFSettingCodes.DATE_LINE_LENGTH, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.DATE_LINE_LENGTH)
								/ 2.0f,
						y,
						PDFUtil.Alignment.TOP_CENTER);
				y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_LOWER_INDENT_SIGNATURE, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_FRAME_LOWER_INDENT_SIGNATURE);
			}
		}
	}

	@Override
	protected void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException {
		PDPageContentStream contentStream = writer.openContentStream(page);
		PDFUtil.renderTextLine(
				contentStream,
				fontA,
				PDFUtil.FontSize.TINY,
				Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(TrainingRecordPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.PAGE_LEFT_MARGIN)
						+ (pageWidth
								- Settings.getFloat(TrainingRecordPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.PAGE_LEFT_MARGIN)
								- Settings.getFloat(
										TrainingRecordPDFSettingCodes.PAGE_RIGHT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.PAGE_RIGHT_MARGIN)),
				Settings.getFloat(TrainingRecordPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.PAGE_LOWER_MARGIN),
				PDFUtil.Alignment.BOTTOM_RIGHT);
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
		if (Settings.getBoolean(TrainingRecordPDFSettingCodes.LANDSCAPE, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public TrainingRecordPDFVO getPdfVO() {
		return pdfVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(TrainingRecordPDFSettingCodes.TEMPLATE_FILE_NAME, cursor.getStaff());
		if (Settings.containsKey(key, Bundle.TRAINING_RECORD_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.TRAINING_RECORD_PDF, null);
		}
		return Settings.getPDFTemplateFilename(TrainingRecordPDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.TRAINING_RECORD_PDF, null);
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(TrainingRecordPDFSettingCodes.FONT_A, Bundle.TRAINING_RECORD_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(TrainingRecordPDFSettingCodes.FONT_B, Bundle.TRAINING_RECORD_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(TrainingRecordPDFSettingCodes.FONT_C, Bundle.TRAINING_RECORD_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) throws Exception {
		int quality = Settings.getInt(TrainingRecordPDFSettingCodes.SELECTION_ITEM_IMAGE_QUALITY, Bundle.TRAINING_RECORD_PDF,
				TrainingRecordPDFDefaultSettings.SELECTION_ITEM_IMAGE_QUALITY);
		int dpi = Settings.getInt(TrainingRecordPDFSettingCodes.SELECTION_ITEM_IMAGE_DPI, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SELECTION_ITEM_IMAGE_DPI);
		Color bgColor = Settings.getColor(TrainingRecordPDFSettingCodes.SELECTION_ITEM_IMAGE_BG_COLOR, Bundle.TRAINING_RECORD_PDF,
				TrainingRecordPDFDefaultSettings.SELECTION_ITEM_IMAGE_BG_COLOR);
		int selectionItemImageWidth = Settings.getInt(TrainingRecordPDFSettingCodes.SELECTION_ITEM_IMAGE_WIDTH, Bundle.TRAINING_RECORD_PDF,
				TrainingRecordPDFDefaultSettings.SELECTION_ITEM_IMAGE_WIDTH);
		int selectionItemImageHeight = Settings.getInt(TrainingRecordPDFSettingCodes.SELECTION_ITEM_IMAGE_HEIGHT, Bundle.TRAINING_RECORD_PDF,
				TrainingRecordPDFDefaultSettings.SELECTION_ITEM_IMAGE_HEIGHT);
		checkboxCheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(TrainingRecordPDFSettingCodes.CHECKBOX_CHECKED_IMAGE_FILE_NAME, Bundle.TRAINING_RECORD_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
		checkboxUncheckedImage = PDFJpeg.prepareScaledImage(doc,
				PDFUtil.loadImage(Settings.getImageFilename(TrainingRecordPDFSettingCodes.CHECKBOX_UNCHECKED_IMAGE_FILE_NAME, Bundle.TRAINING_RECORD_PDF, null)),
				selectionItemImageWidth, selectionItemImageHeight, quality, dpi, bgColor);
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		TrainingRecordPDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && (BlockType.HEAD.equals(block.getType()) || BlockType.NEW_PAGE.equals(block.getType()))) {
			return false;
		} else {
			return (cursor.getBlockY() - block.getHeight(cursor)) > Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.TRAINING_RECORD_PDF,
					TrainingRecordPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public boolean nextBlockFitsOnFullPage() throws Exception {
		TrainingRecordPDFBlock block = blocks.get(blockIndex);
		if (BlockType.SECTION.equals(block.getType())) {
			return (pageHeight
					- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_UPPER_MARGIN)
					- block.getHeight(cursor)) > Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		} else {
			return true;
		}
	}

	@Override
	public void splitNextBlock() throws Exception {
		TrainingRecordPDFBlock block = blocks.get(blockIndex);
		if (BlockType.SECTION.equals(block.getType())) {
			ArrayList<CourseParticipationStatusEntryOutVO> blockParticipations = new ArrayList<CourseParticipationStatusEntryOutVO>();
			TrainingRecordPDFBlock testBlock = new TrainingRecordPDFBlock(block.getTrainingRecordSection(), new ArrayList<CourseParticipationStatusEntryOutVO>(), block.hasTrials(),
					block.hasInstitutions());
			Iterator<CourseParticipationStatusEntryOutVO> it = block.getParticipations().iterator();
			while (it.hasNext()) {
				CourseParticipationStatusEntryOutVO participation = it.next();
				testBlock.getParticipations().add(participation);
				if ((pageHeight
						- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_UPPER_MARGIN)
						- testBlock.getHeight(cursor)) > Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.BLOCKS_LOWER_MARGIN)) {
					blockParticipations.add(participation);
				} else {
					break;
				}
			}
			if (blockParticipations.size() > 0) {
				blocks.add(blockIndex, new TrainingRecordPDFBlock(block.getTrainingRecordSection(), blockParticipations));
				block.getParticipations().removeAll(blockParticipations);
			}
		}
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (staffVOs != null) {
			Iterator<StaffOutVO> staffIt = staffVOs.iterator();
			while (staffIt.hasNext()) {
				StaffOutVO staffVO = staffIt.next();
				blocks.add(new TrainingRecordPDFBlock(staffVO, BlockType.HEAD));
				blocks.add(new TrainingRecordPDFBlock());
				blocks.add(new TrainingRecordPDFBlock(staffVO, BlockType.NAME));
				if (staffTagValueVOMap != null) {
					Collection<StaffTagValueOutVO> tagValues = staffTagValueVOMap.get(staffVO.getId());
					if (tagValues != null) {
						blocks.add(new TrainingRecordPDFBlock());
						blocks.add(new TrainingRecordPDFBlock(tagValues));
					}
				}
				if (allTrainingRecordSectionVOs != null) {
					Iterator<TrainingRecordSectionVO> sectionIt = allTrainingRecordSectionVOs.iterator();
					boolean sectionsAppended = false;
					if (participationVOMap != null) {
						HashMap<Long, Collection<CourseParticipationStatusEntryOutVO>> staffParticipationVOMap = participationVOMap.get(staffVO.getId());
						if (staffParticipationVOMap != null) {
							while (sectionIt.hasNext()) {
								TrainingRecordSectionVO sectionVO = sectionIt.next();
								Collection<CourseParticipationStatusEntryOutVO> participations = staffParticipationVOMap.get(sectionVO.getId());
								if (participations != null && participations.size() > 0) {
									if (Settings.getBoolean(TrainingRecordPDFSettingCodes.SECTION_NEW_PAGE_PER_TRIAL, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.SECTION_NEW_PAGE_PER_TRIAL)) {
										Iterator<CourseParticipationStatusEntryOutVO> participationsIt = participations.iterator();
										LinkedHashMap<Long, Collection<CourseParticipationStatusEntryOutVO>> trialParticipationVOMap = new LinkedHashMap<Long, Collection<CourseParticipationStatusEntryOutVO>>();
										//participations are sorted chronologically. put those w/o course trial at the beginning.
										trialParticipationVOMap.put(null, new ArrayList<CourseParticipationStatusEntryOutVO>());
										while (participationsIt.hasNext()) {
											CourseParticipationStatusEntryOutVO participation = participationsIt.next();
											Long trialId = (participation.getCourse().getTrial() != null ? participation.getCourse().getTrial().getId() : null);
											Collection<CourseParticipationStatusEntryOutVO> trialParticipations;
											if (trialParticipationVOMap.containsKey(trialId)) {
												trialParticipations = trialParticipationVOMap.get(trialId);
											} else {
												trialParticipations = new ArrayList<CourseParticipationStatusEntryOutVO>();
												trialParticipationVOMap.put(trialId, trialParticipations);
											}
											trialParticipations.add(participation);
										}
										if (trialParticipationVOMap.get(null).size() == 0) { //there were no particiaptions of courses w/o trial
											trialParticipationVOMap.remove(null);
										}
										if (trialParticipationVOMap.keySet().size() > 1
												|| trialParticipationVOMap.keySet().iterator().next() != null) {
											trialParticipationVOMap.put(null, trialParticipationVOMap.remove(null)); //move those w/o course trial at the end
											Iterator<Long> trialIt = trialParticipationVOMap.keySet().iterator();
											while (trialIt.hasNext()) {
												Collection<CourseParticipationStatusEntryOutVO> trialParticipations = trialParticipationVOMap.get(trialIt.next());
												blocks.add(new TrainingRecordPDFBlock(BlockType.NEW_PAGE));
												blocks.add(new TrainingRecordPDFBlock(sectionVO, trialParticipations));
											}
										} else {
											blocks.add(new TrainingRecordPDFBlock());
											blocks.add(new TrainingRecordPDFBlock(sectionVO, participations));
										}
									} else {
										blocks.add(new TrainingRecordPDFBlock());
										blocks.add(new TrainingRecordPDFBlock(sectionVO, participations));
									}
								} else if (sectionVO.getVisible()) {
									blocks.add(new TrainingRecordPDFBlock());
									blocks.add(new TrainingRecordPDFBlock(sectionVO, new ArrayList<CourseParticipationStatusEntryOutVO>()));
								}
							}
							sectionsAppended = true;
						}
					}
					if (!sectionsAppended) {
						while (sectionIt.hasNext()) {
							TrainingRecordSectionVO sectionVO = sectionIt.next();
							if (sectionVO.getVisible()) {
								blocks.add(new TrainingRecordPDFBlock());
								blocks.add(new TrainingRecordPDFBlock(sectionVO, new ArrayList<CourseParticipationStatusEntryOutVO>()));
							}
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
				- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(
				pageWidth - Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
						- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setStaff(null);
		fontA = null;
		fontB = null;
		fontC = null;
		checkboxCheckedImage = null;
		checkboxUncheckedImage = null;
		updateTrainingRecordPDFVO();
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		byte[] documentData = pdfStream.toByteArray();
		pdfVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		pdfVO.setSize(documentData.length);
		pdfVO.setDocumentDatas(documentData);
		return true;
	}

	public void setAllTrainingRecordSectionVOs(Collection<TrainingRecordSectionVO> allTrainingRecordSectionVOs) {
		this.allTrainingRecordSectionVOs = allTrainingRecordSectionVOs;
	}

	public void setParticipationVOMap(HashMap<Long, HashMap<Long, Collection<CourseParticipationStatusEntryOutVO>>> participationVOMap) {
		this.participationVOMap = participationVOMap;
	}

	public void setStaffTagValueVOMap(HashMap<Long, Collection<StaffTagValueOutVO>> staffTagValueVOMap) {
		this.staffTagValueVOMap = staffTagValueVOMap;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight
				- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(
				pageWidth - Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
						- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	public void setStaffVOs(Collection<StaffOutVO> staffVOs) {
		this.staffVOs = staffVOs;
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.HEAD.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight
				- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(
				pageWidth - Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
						- Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	@Override
	public void updateCursor() {
		TrainingRecordPDFBlock block = blocks.get(blockIndex);
		if (BlockType.HEAD.equals(block.getType())) {
			cursor.setStaff(block.getStaff());
		}
	}

	protected void updateTrainingRecordPDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.setStafves(staffVOs);
		StringBuilder fileName = new StringBuilder(TRAINING_RECORD_PDF_FILENAME_PREFIX);
		if (staffVOs != null && staffVOs.size() == 1) {
			fileName.append(staffVOs.iterator().next().getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
	}

	public PDFJpeg getCheckboxCheckedImage() {
		return checkboxCheckedImage;
	}

	public PDFJpeg getCheckboxUncheckedImage() {
		return checkboxUncheckedImage;
	}
}
