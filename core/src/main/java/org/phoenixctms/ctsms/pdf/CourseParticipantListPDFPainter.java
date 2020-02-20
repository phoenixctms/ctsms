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
import org.phoenixctms.ctsms.pdf.CourseParticipantListPDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.LecturerCompetenceVO;
import org.phoenixctms.ctsms.vo.LecturerOutVO;

public class CourseParticipantListPDFPainter extends PDFPainterBase implements PDFOutput {

	protected int blockIndex;
	protected ArrayList<CourseParticipantListPDFBlock> blocks;
	protected CourseParticipantListPDFBlockCursor cursor;
	protected CourseParticipantListPDFVO pdfVO;
	protected Collection<CourseOutVO> courseVOs;
	protected HashMap<Long, Collection<CourseParticipationStatusEntryOutVO>> participationVOMap;
	protected Collection<LecturerCompetenceVO> allCompetenceVOs;
	protected HashMap<Long, HashMap<Long, Collection<LecturerOutVO>>> lecturerVOMap;
	protected HashMap<Long, Collection<InventoryBookingOutVO>> bookingVOMap;
	protected float pageWidth;
	protected float pageHeight;
	protected PDFont fontA;
	protected PDFont fontB;
	protected PDFont fontC;
	protected final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	protected static final String COURSE_PARTICIPANT_LIST_PDF_FILENAME_PREFIX = "course_participant_list_";

	public CourseParticipantListPDFPainter() {
		super();
		blocks = new ArrayList<CourseParticipantListPDFBlock>();
		pdfVO = new CourseParticipantListPDFVO();
		cursor = new CourseParticipantListPDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(CourseParticipantListPDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.COURSE_PARTICIPANT_LIST_PDF,
				CourseParticipantListPDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	@Override
	public void drawNextBlock(PDPageContentStream contentStream) throws Exception {
		CourseParticipantListPDFBlock block = blocks.get(blockIndex);
		cursor.setBlockY(cursor.getBlockY() - block.renderBlock(contentStream, cursor));
		blockIndex++;
	}

	@Override
	public void drawPage(PDPageContentStream contentStream) throws Exception {
	}

	@Override
	protected void drawPageNumber(PDFImprinter writer, PDPage page, int pageNumber, int totalPages) throws IOException {
		PDPageContentStream contentStream = writer.openContentStream(page);
		PDFUtil.renderTextLine(
				contentStream,
				fontA,
				PDFUtil.FontSize.TINY,
				Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(CourseParticipantListPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PAGE_LEFT_MARGIN)
						+ (pageWidth
								- Settings.getFloat(CourseParticipantListPDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.PAGE_LEFT_MARGIN)
								- Settings.getFloat(CourseParticipantListPDFSettingCodes.PAGE_RIGHT_MARGIN,
										Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.PAGE_RIGHT_MARGIN))
								/ 2.0f,
				Settings.getFloat(
						CourseParticipantListPDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.PAGE_LOWER_MARGIN),
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
		if (Settings.getBoolean(CourseParticipantListPDFSettingCodes.LANDSCAPE, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public CourseParticipantListPDFVO getPdfVO() {
		return pdfVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(CourseParticipantListPDFSettingCodes.TEMPLATE_FILE_NAME, cursor.getCourse());
		if (Settings.containsKey(key, Bundle.COURSE_PARTICIPANT_LIST_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.COURSE_PARTICIPANT_LIST_PDF, null);
		}
		return Settings.getPDFTemplateFilename(CourseParticipantListPDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.COURSE_PARTICIPANT_LIST_PDF, null);
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_A, Bundle.COURSE_PARTICIPANT_LIST_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_B, Bundle.COURSE_PARTICIPANT_LIST_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_C, Bundle.COURSE_PARTICIPANT_LIST_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) throws Exception {
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		CourseParticipantListPDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && BlockType.HEAD.equals(block.getType())) {
			return false;
		} else {
			return (cursor.getBlockY() - block.getHeight(cursor)) > Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
					CourseParticipantListPDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (courseVOs != null) {
			Iterator<CourseOutVO> courseIt = courseVOs.iterator();
			while (courseIt.hasNext()) {
				CourseOutVO courseVO = courseIt.next();
				blocks.add(new CourseParticipantListPDFBlock(courseVO, now, BlockType.HEAD));
				blocks.add(new CourseParticipantListPDFBlock());
				blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.COURSE_NAME));
				blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.COURSE_SELF_REGISTRATION));
				blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.COURSE_PERIOD));
				if (courseVO.getTrial() != null) {
					blocks.add(new CourseParticipantListPDFBlock(courseVO.getTrial()));
				}
				if (courseVO.getDescription() != null && courseVO.getDescription().length() > 0) {
					blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.COURSE_DESCRIPTION));
				}
				if (courseVO.getShowCvPreset()) {
					blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.COURSE_CV_TITLE));
					if (courseVO.getInstitution() != null) {
						blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.COURSE_INSTITUTION));
					}
					if (courseVO.getShowCommentCvPreset()) {
						blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.COURSE_CV_COMMENT_PRESET));
					}
				}
				if (allCompetenceVOs != null) {
					Iterator<LecturerCompetenceVO> competenceIt = allCompetenceVOs.iterator();
					if (lecturerVOMap != null) {
						HashMap<Long, Collection<LecturerOutVO>> competenceLecturerVOMap = lecturerVOMap.get(courseVO.getId());
						if (competenceLecturerVOMap != null) {
							while (competenceIt.hasNext()) {
								LecturerCompetenceVO competenceVO = competenceIt.next();
								Collection<LecturerOutVO> lecturers = competenceLecturerVOMap.get(competenceVO.getId());
								if (lecturers != null && lecturers.size() > 0) {
									blocks.add(new CourseParticipantListPDFBlock(competenceVO, lecturers));
								}
							}
						}
					}
				}
				ArrayList<InventoryBookingOutVO> bookingVOs = null;
				if (bookingVOMap != null) {
					Collection<InventoryBookingOutVO> bookings = bookingVOMap.get(courseVO.getId());
					if (bookings != null && bookings.size() > 0) {
						bookingVOs = new ArrayList<InventoryBookingOutVO>(bookings);
					}
				}
				if (bookingVOs == null) {
					bookingVOs = new ArrayList<InventoryBookingOutVO>();
				}
				Collection<CourseParticipationStatusEntryOutVO> participationVOs = null;
				if (participationVOMap != null) {
					participationVOs = participationVOMap.get(courseVO.getId());
				}
				if (participationVOs == null) {
					participationVOs = new ArrayList<CourseParticipationStatusEntryOutVO>();
				}
				int i = 0;
				if (participationVOs.size() == 0) {
					for (i = 0; i < Settings.getInt(CourseParticipantListPDFSettingCodes.BLANK_ROWS, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.BLANK_ROWS); i++) {
						participationVOs.add(null);
					}
				}
				i = 0;
				int bookingColumnCount = Settings.getInt(CourseParticipantListPDFSettingCodes.BOOKING_COLUMN_COUNT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BOOKING_COLUMN_COUNT);
				do {
					if (bookingVOs.size() > 0 && bookingColumnCount > 0) {
						blocks.add(new CourseParticipantListPDFBlock());
						ArrayList<InventoryBookingOutVO> bookingsRange = CoreUtil.getSubList(bookingVOs, i, bookingColumnCount);
						blocks.add(new CourseParticipantListPDFBlock(bookingsRange));
						Iterator<CourseParticipationStatusEntryOutVO> participationIt = participationVOs.iterator();
						while (participationIt.hasNext()) {
							blocks.add(new CourseParticipantListPDFBlock(participationIt.next(), bookingsRange));
						}
						i += bookingsRange.size();
					} else {
						blocks.add(new CourseParticipantListPDFBlock());
						blocks.add(new CourseParticipantListPDFBlock(courseVO, BlockType.PARTICIPANT_TABLE_HEAD));
						Iterator<CourseParticipationStatusEntryOutVO> participationIt = participationVOs.iterator();
						while (participationIt.hasNext()) {
							blocks.add(new CourseParticipantListPDFBlock(participationIt.next()));
						}
						i = bookingVOs.size();
					}
				} while (i < bookingVOs.size());
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
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
				CourseParticipantListPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setCourse(null);
		fontA = null;
		fontB = null;
		fontC = null;
		updateCourseParticipantListPDFVO();
	}

	@Override
	public boolean save(ByteArrayOutputStream pdfStream) throws Exception {
		byte[] documentData = pdfStream.toByteArray();
		pdfVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		pdfVO.setSize(documentData.length);
		pdfVO.setDocumentDatas(documentData);
		return true;
	}

	public void setAllCompetenceVOs(Collection<LecturerCompetenceVO> allCompetenceVOs) {
		this.allCompetenceVOs = allCompetenceVOs;
	}

	public void setBookingVOMap(HashMap<Long, Collection<InventoryBookingOutVO>> bookingVOMap) {
		this.bookingVOMap = bookingVOMap;
	}

	public void setCourseVOs(Collection<CourseOutVO> courseVOs) {
		this.courseVOs = courseVOs;
	}

	public void setLecturerVOMap(HashMap<Long, HashMap<Long, Collection<LecturerOutVO>>> lecturerVOMap) {
		this.lecturerVOMap = lecturerVOMap;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	public void setParticipationVOMap(HashMap<Long, Collection<CourseParticipationStatusEntryOutVO>> participationVOMap) {
		this.participationVOMap = participationVOMap;
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.HEAD.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
				CourseParticipantListPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(CourseParticipantListPDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	protected void updateCourseParticipantListPDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.setCourses(courseVOs);
		StringBuilder fileName = new StringBuilder(COURSE_PARTICIPANT_LIST_PDF_FILENAME_PREFIX);
		if (courseVOs != null && courseVOs.size() == 1) {
			fileName.append(courseVOs.iterator().next().getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
	}

	@Override
	public void updateCursor() {
		CourseParticipantListPDFBlock block = blocks.get(blockIndex);
		if (BlockType.HEAD.equals(block.getType())) {
			cursor.setCourse(block.getCourse());
		}
	}
}
