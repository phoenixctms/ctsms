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
import org.phoenixctms.ctsms.pdf.CourseCertificatePDFBlock.BlockType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CourseCertificatePDFVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.LecturerCompetenceVO;
import org.phoenixctms.ctsms.vo.LecturerOutVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;

public class CourseCertificatePDFPainter extends PDFPainterBase implements PDFOutput {

	private int blockIndex;
	private ArrayList<CourseCertificatePDFBlock> blocks;
	private CourseCertificatePDFBlockCursor cursor;
	private CourseCertificatePDFVO pdfVO;
	private Collection<CourseParticipationStatusEntryOutVO> participantVOs;
	private HashMap<Long, StaffOutVO> institutionVOMap;
	private HashMap<Long, StaffAddressOutVO> institutionAddressVOMap;
	private Collection<LecturerCompetenceVO> allCompetenceVOs;
	private HashMap<Long, HashMap<Long, Collection<LecturerOutVO>>> lecturerVOMap;
	private float pageWidth;
	private float pageHeight;
	private PDFont fontA;
	private PDFont fontB;
	private PDFont fontC;
	private final static PDRectangle DEFAULT_PAGE_SIZE = PDPage.PAGE_SIZE_A4;
	private static final String COURSE_CERTIFICATE_PDF_FILENAME_PREFIX = "course_certificate_";

	public CourseCertificatePDFPainter() {
		super();
		blocks = new ArrayList<CourseCertificatePDFBlock>();
		pdfVO = new CourseCertificatePDFVO();
		cursor = new CourseCertificatePDFBlockCursor(this);
		setDrawPageNumbers(Settings.getBoolean(CourseCertificatePDFSettingCodes.SHOW_PAGE_NUMBERS, Bundle.COURSE_CERTIFICATE_PDF,
				CourseCertificatePDFDefaultSettings.SHOW_PAGE_NUMBERS));
	}

	@Override
	public void drawNextBlock(PDPageContentStream contentStream) throws Exception {
		CourseCertificatePDFBlock block = blocks.get(blockIndex);
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
				Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
				L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.PAGE_NUMBER, "", pageNumber, totalPages),
				Settings.getFloat(CourseCertificatePDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.PAGE_LEFT_MARGIN)
						+ (pageWidth
								- Settings.getFloat(CourseCertificatePDFSettingCodes.PAGE_LEFT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF,
										CourseCertificatePDFDefaultSettings.PAGE_LEFT_MARGIN)
								- Settings.getFloat(CourseCertificatePDFSettingCodes.PAGE_RIGHT_MARGIN,
										Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.PAGE_RIGHT_MARGIN))
								/ 2.0f,
				Settings.getFloat(CourseCertificatePDFSettingCodes.PAGE_LOWER_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.PAGE_LOWER_MARGIN),
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
		if (Settings.getBoolean(CourseCertificatePDFSettingCodes.LANDSCAPE, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.LANDSCAPE)) {
			return PageOrientation.LANDSCAPE;
		} else {
			return PageOrientation.PORTRAIT;
		}
	}

	public CourseCertificatePDFVO getPdfVO() {
		return pdfVO;
	}

	@Override
	public String getTemplateFileName() throws Exception {
		String key = L10nUtil.getDepartmentL10nKey(CourseCertificatePDFSettingCodes.TEMPLATE_FILE_NAME, cursor.getParticipation() != null ? cursor.getParticipation().getCourse()
				: (CourseOutVO) null);
		if (Settings.containsKey(key, Bundle.COURSE_CERTIFICATE_PDF)) {
			return Settings.getPDFTemplateFilename(key, Bundle.COURSE_CERTIFICATE_PDF, null);
		}
		return Settings.getPDFTemplateFilename(CourseCertificatePDFSettingCodes.TEMPLATE_FILE_NAME, Bundle.COURSE_CERTIFICATE_PDF, null);
	}

	@Override
	public boolean hasNextBlock() {
		return blockIndex < blocks.size();
	}

	@Override
	public void loadFonts(PDDocument doc) throws Exception {
		fontA = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_A, Bundle.COURSE_CERTIFICATE_PDF, null), doc, DEFAULT_BASE_FONT);
		fontB = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_B, Bundle.COURSE_CERTIFICATE_PDF, null), doc, DEFAULT_BASE_FONT);
		fontC = PDFUtil.loadFont(Settings.getPDFFontName(CVPDFSettingCodes.FONT_C, Bundle.COURSE_CERTIFICATE_PDF, null), doc, DEFAULT_BASE_FONT);
	}

	@Override
	public void loadImages(PDDocument doc) throws Exception {
	}

	@Override
	public boolean nextBlockFitsOnPage() throws Exception {
		CourseCertificatePDFBlock block = blocks.get(blockIndex);
		if (blockIndex > 0 && BlockType.HEAD.equals(block.getType())) {
			return false;
		} else {
			return (cursor.getBlockY() - block.getHeight(cursor)) > Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_LOWER_MARGIN, Bundle.COURSE_CERTIFICATE_PDF,
					CourseCertificatePDFDefaultSettings.BLOCKS_LOWER_MARGIN);
		}
	}

	@Override
	public void populateBlocks() {
		blocks.clear();
		if (participantVOs != null) {
			Iterator<CourseParticipationStatusEntryOutVO> participantIt = participantVOs.iterator();
			while (participantIt.hasNext()) {
				CourseParticipationStatusEntryOutVO participationVO = participantIt.next();
				blocks.add(new CourseCertificatePDFBlock(participationVO, BlockType.HEAD));
				blocks.add(new CourseCertificatePDFBlock());
				blocks.add(new CourseCertificatePDFBlock(BlockType.CONFIRMATION_1));
				blocks.add(new CourseCertificatePDFBlock(BlockType.SPACER_SMALL));
				blocks.add(new CourseCertificatePDFBlock(participationVO.getStaff(), BlockType.PARTICIPANT));
				blocks.add(new CourseCertificatePDFBlock(BlockType.SPACER_SMALL));
				CourseOutVO courseVO = participationVO.getCourse();
				blocks.add(new CourseCertificatePDFBlock(courseVO, BlockType.COURSE_PERIOD));
				blocks.add(new CourseCertificatePDFBlock(BlockType.SPACER_SMALL));
				blocks.add(new CourseCertificatePDFBlock(BlockType.CONFIRMATION_2));
				blocks.add(new CourseCertificatePDFBlock(BlockType.SPACER_SMALL));
				blocks.add(new CourseCertificatePDFBlock(courseVO, BlockType.COURSE_TITLE));
				blocks.add(new CourseCertificatePDFBlock(BlockType.SPACER_SMALL));
				blocks.add(new CourseCertificatePDFBlock(participationVO));
				blocks.add(new CourseCertificatePDFBlock());
				if (courseVO.getTrial() != null) {
					blocks.add(new CourseCertificatePDFBlock(courseVO.getTrial()));
				}
				if (courseVO.getDescription() != null && courseVO.getDescription().length() > 0) {
					blocks.add(new CourseCertificatePDFBlock(courseVO, BlockType.COURSE_DESCRIPTION));
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
									blocks.add(new CourseCertificatePDFBlock(competenceVO, lecturers));
								}
							}
						}
					}
				}
				StaffOutVO institutionVO = institutionVOMap.get(participationVO.getCourse().getId());
				if (institutionVO != null) {
					blocks.add(new CourseCertificatePDFBlock());
					blocks.add(new CourseCertificatePDFBlock());
					blocks.add(new CourseCertificatePDFBlock(institutionVO, BlockType.INSTITUTION));
					blocks.add(new CourseCertificatePDFBlock(BlockType.SPACER_SMALL));
					blocks.add(new CourseCertificatePDFBlock(institutionVO, institutionAddressVOMap.get(participationVO.getCourse().getId())));
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
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF,
				CourseCertificatePDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setParticipation(null);
		fontA = null;
		fontB = null;
		fontC = null;
		updateCourseCertificatePDFVO();
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

	public void setInstitutionAddressVOMap(
			HashMap<Long, StaffAddressOutVO> institutionAddressVOMap) {
		this.institutionAddressVOMap = institutionAddressVOMap;
	}

	public void setInstitutionVOMap(HashMap<Long, StaffOutVO> institutionVOMap) {
		this.institutionVOMap = institutionVOMap;
	}

	public void setLecturerVOMap(HashMap<Long, HashMap<Long, Collection<LecturerOutVO>>> lecturerVOMap) {
		this.lecturerVOMap = lecturerVOMap;
	}

	@Override
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
		cursor.setBlockY(pageHeight
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_UPPER_MARGIN));
	}

	@Override
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	public void setParticipantVOs(Collection<CourseParticipationStatusEntryOutVO> participantVOs) {
		this.participantVOs = participantVOs;
	}

	@Override
	public void startNewPage() {
		super.startNewPage(!hasNextBlock() || BlockType.HEAD.equals(blocks.get(blockIndex).getType()));
		cursor.setBlockY(pageHeight
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_UPPER_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_UPPER_MARGIN));
		cursor.setBlockX(Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF,
				CourseCertificatePDFDefaultSettings.BLOCKS_LEFT_MARGIN));
		cursor.setBlockWidth(pageWidth
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_RIGHT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_RIGHT_MARGIN)
				- Settings.getFloat(CourseCertificatePDFSettingCodes.BLOCKS_LEFT_MARGIN, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.BLOCKS_LEFT_MARGIN));
	}

	private void updateCourseCertificatePDFVO() {
		pdfVO.setContentTimestamp(now);
		pdfVO.setContentType(CoreUtil.getPDFMimeType());
		pdfVO.setParticipants(participantVOs);
		StringBuilder fileName = new StringBuilder(COURSE_CERTIFICATE_PDF_FILENAME_PREFIX);
		if (participantVOs != null && participantVOs.size() == 1) {
			fileName.append(participantVOs.iterator().next().getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.PDF_FILENAME_EXTENSION);
		pdfVO.setFileName(fileName.toString());
	}

	@Override
	public void updateCursor() {
		CourseCertificatePDFBlock block = blocks.get(blockIndex);
		if (BlockType.HEAD.equals(block.getType())) {
			cursor.setParticipation(block.getParticipation());
		}
	}
}
