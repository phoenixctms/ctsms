package org.phoenixctms.ctsms.pdf;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.pdf.PDFUtil.Alignment;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CvPositionPDFVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;

public class CVPDFBlock {

	public enum BlockType {
		SPACER, IMAGE_CENTERED, IMAGE_RIGHT, HEAD, FULL_NAME, DATE_OF_BIRTH, ACADEMIC_TITLE, ADDRESS, CV_SECTION_POSITIONS, SIGNATURE_DATE
	}

	protected StaffOutVO staff;
	protected CvSectionVO cvSection;
	protected Collection<CvPositionPDFVO> cvPositions;
	protected StaffAddressOutVO address;
	protected Date now;
	protected BlockType type;
	protected PDFJpeg ximage;

	public CVPDFBlock() {
		type = BlockType.SPACER;
	}

	public CVPDFBlock(CvSectionVO cvSection, Collection<CvPositionPDFVO> cvPositions) {
		this.cvSection = cvSection;
		this.cvPositions = cvPositions;
		this.type = BlockType.CV_SECTION_POSITIONS;
	}

	public CVPDFBlock(PDFJpeg ximage) {
		this.ximage = ximage;
		type = BlockType.IMAGE_CENTERED;
	}

	public CVPDFBlock(StaffOutVO staff, BlockType type) {
		this.staff = staff;
		this.type = type;
	}

	public CVPDFBlock(StaffOutVO staff, Date now) {
		this.staff = staff;
		this.now = now;
		this.type = BlockType.SIGNATURE_DATE;
	}

	public CVPDFBlock(StaffOutVO staff, StaffAddressOutVO address) {
		this.address = address;
		this.staff = staff;
		this.type = BlockType.ADDRESS;
	}

	public CVPDFBlock(StaffOutVO staff, StaffAddressOutVO address, PDFJpeg ximage) {
		this.address = address;
		this.staff = staff;
		this.ximage = ximage;
		type = BlockType.IMAGE_RIGHT;
	}

	protected String getCvAcademicTitle() {
		if (staff != null && staff.isPerson() && staff.getCvAcademicTitle() != null) {
			return staff.getCvAcademicTitle();
		}
		return "";
	}

	protected String getDateOfBirth() {
		if (staff != null && staff.isPerson() && staff.getDateOfBirth() != null) {
			return Settings
					.getSimpleDateFormat(CVPDFSettingCodes.DATE_OF_BIRTH_DATE_PATTERN, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_OF_BIRTH_DATE_PATTERN, Locales.CV_PDF) //no usertimezone
					.format(staff.getDateOfBirth());
		}
		return "";
	}

	protected String getFullName() {
		StringBuilder sb = new StringBuilder();
		if (staff != null && staff.isPerson()) {
			sb.append(staff.getLastName().toUpperCase(L10nUtil.getLocale(Locales.CV_PDF)));
			sb.append(", ");
			sb.append(staff.getFirstName());
		}
		return sb.toString();
	}

	public float getHeight(CVPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	protected String getSectionDescription() {
		if (cvSection != null) {
			return L10nUtil.getCvSectionDescription(Locales.CV_PDF, cvSection.getDescriptionL10nKey());
		}
		return "";
	}

	protected String getSectionName() {
		StringBuilder sb = new StringBuilder();
		if (cvSection != null) {
			sb.append(L10nUtil.getCvSectionName(Locales.CV_PDF, cvSection.getNameL10nKey()));
			sb.append(L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.SECTION_SUFFIX, ""));
		}
		return sb.toString();
	}

	protected String getSignatureLabel() {
		if (staff != null && staff.isPerson()) {
			return L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.SIGNATURE_ANNOTATION, PDFUtil.DEFAULT_LABEL, CommonUtil.getCvStaffName(staff), now == null ? null
					: Settings
							.getSimpleDateFormat(CVPDFSettingCodes.SIGNATURE_DATE_PATTERN, Bundle.CV_PDF, CVPDFDefaultSettings.SIGNATURE_DATE_PATTERN, Locales.CV_PDF,
									Settings.getBoolean(CVPDFSettingCodes.DATE_USER_TIME_ZONE, Bundle.SETTINGS, CVPDFDefaultSettings.DATE_USER_TIME_ZONE))
							.format(now));
		}
		return "";
	}

	public StaffOutVO getStaff() {
		return staff;
	}

	public BlockType getType() {
		return type;
	}

	public CvSectionVO getCvSection() {
		return cvSection;
	}

	public Collection<CvPositionPDFVO> getCvPositions() {
		return cvPositions;
	}

	public float renderBlock(PDPageContentStream contentStream, CVPDFBlockCursor cursor) throws Exception {
		float x;
		float y;
		float x1;
		float y1;
		float y2;
		float height;
		switch (type) {
			case HEAD:
				String line1 = L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.HEADLINE_LINE1, PDFUtil.DEFAULT_LABEL);
				String line2 = L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.HEADLINE_LINE2, PDFUtil.DEFAULT_LABEL);
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line1)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.BIG,
							Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), line1, x, y, PDFUtil.Alignment.TOP_CENTER);
				}
				if (!CommonUtil.isEmptyString(line2)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.LARGE,
							Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), line2, x, y, PDFUtil.Alignment.TOP_CENTER);
				}
				height = cursor.getBlockY() - y;
				break;
			case FULL_NAME:
				y1 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.FULL_NAME, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), getFullName(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				PDFUtil.renderFrame(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR), cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CVPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT), cursor.getBlockY(),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(CVPDFSettingCodes.COLUMN_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.COLUMN_LINE_WIDTH));
				break;
			case DATE_OF_BIRTH:
				y1 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.DATE_OF_BIRTH, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), getDateOfBirth(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				PDFUtil.renderFrame(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR), cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CVPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT), cursor.getBlockY(),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(CVPDFSettingCodes.COLUMN_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.COLUMN_LINE_WIDTH));
				break;
			case ACADEMIC_TITLE:
				y1 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.ACADEMIC_TITLE, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), getCvAcademicTitle(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				PDFUtil.renderFrame(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR), cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CVPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT), cursor.getBlockY(),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(CVPDFSettingCodes.COLUMN_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.COLUMN_LINE_WIDTH));
				break;
			case ADDRESS:
				y1 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.ADDRESS, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontB(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						CommonUtil.getCvAddressBlock(address, staff, PDFUtil.PDF_LINE_BREAK),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth() - Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT) - 2.0f
								* Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				PDFUtil.renderFrame(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR), cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CVPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT), cursor.getBlockY(),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(CVPDFSettingCodes.COLUMN_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.COLUMN_LINE_WIDTH));
				break;
			case IMAGE_CENTERED:
				if (ximage != null) {
					y1 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_IMAGE_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_IMAGE_INDENT);
					PDFUtil.renderImage(contentStream, ximage, cursor.getBlockCenterX(), y1, Alignment.TOP_CENTER);
					y1 -= ximage.getHeightPoints();
					height = cursor.getBlockY() - y1 + Settings.getFloat(CVPDFSettingCodes.Y_IMAGE_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_IMAGE_INDENT);
				} else {
					height = 0.0f;
				}
				break;
			case IMAGE_RIGHT:
				y1 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.FULL_NAME, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT_PHOTO, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT_PHOTO)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), getFullName(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				y1 = cursor.getBlockY() - height - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.DATE_OF_BIRTH, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - height - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT_PHOTO, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT_PHOTO)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), getDateOfBirth(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height += cursor.getBlockY() - height - Math.min(y1, y2);
				y1 = cursor.getBlockY() - height - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.ACADEMIC_TITLE, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - height - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT_PHOTO, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT_PHOTO)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), getCvAcademicTitle(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height += cursor.getBlockY() - height - Math.min(y1, y2);
				y1 = cursor.getBlockY() - height - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.ADDRESS, PDFUtil.DEFAULT_LABEL), x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - height - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT_PHOTO, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT_PHOTO)
						+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						CommonUtil.getCvAddressBlock(address, staff, PDFUtil.PDF_LINE_BREAK), x, y2, PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth() -
								(ximage == null ? 0.0f
										: ximage.getWidthPoints()
												+ Settings.getFloat(CVPDFSettingCodes.X_IMAGE_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_IMAGE_INDENT))
								- Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT_PHOTO, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT_PHOTO) - 2.0f
										* Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height += cursor.getBlockY() - height - Math.min(y1, y2);
				y1 = cursor.getBlockY();
				PDFUtil.renderImage(contentStream, ximage, cursor.getBlockX() + cursor.getBlockWidth(), y1, Alignment.TOP_RIGHT);
				if (ximage != null) {
					y1 -= ximage.getHeightPoints();
				}
				height = Math.max(height, cursor.getBlockY() - y1);
				PDFUtil.renderFrame(
						contentStream,
						Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX(),
						cursor.getBlockY(),
						cursor.getBlockWidth()
								- (ximage == null ? 0.0f
										: ximage.getWidthPoints()
												+ Settings.getFloat(CVPDFSettingCodes.X_IMAGE_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_IMAGE_INDENT)),
						height,
						PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(CVPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				height += Settings.getFloat(CVPDFSettingCodes.Y_IMAGE_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_IMAGE_INDENT);
				break;
			case CV_SECTION_POSITIONS:
				y1 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						getSectionName(),
						x,
						y1,
						PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT) - 2.0f
								* Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT));
				String description = getSectionDescription();
				if (!CommonUtil.isEmptyString(description)) {
					y1 -= PDFUtil.renderMultilineText(
							contentStream,
							cursor.getFontA(),
							PDFUtil.FontSize.SMALL,
							Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
							description,
							x,
							y1,
							PDFUtil.Alignment.TOP_LEFT,
							Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT) - 2.0f
									* Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT));
				}
				y1 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				if (cvPositions != null) {
					x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT)
							+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT);
					Iterator<CvPositionPDFVO> it = cvPositions.iterator();
					while (it.hasNext()) {
						CvPositionPDFVO cvPosition = it.next();
						String label1 = cvPosition.getLabel1();
						String label2 = cvPosition.getLabel2();
						String label3 = cvPosition.getLabel3();
						if (!CommonUtil.isEmptyString(label1)) {
							y2 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
									Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), label1, x, y2, PDFUtil.Alignment.TOP_LEFT,
									cursor.getBlockWidth() - Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT) - 2.0f
											* Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT));
						}
						if (!CommonUtil.isEmptyString(label2)) {
							y2 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontC(), PDFUtil.FontSize.SMALL,
									Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), label2, x, y2, PDFUtil.Alignment.TOP_LEFT,
									cursor.getBlockWidth() - Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT) - 2.0f
											* Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT));
						}
						if (!CommonUtil.isEmptyString(label3)) {
							y2 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.SMALL,
									Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), label3, x, y2, PDFUtil.Alignment.TOP_LEFT,
									cursor.getBlockWidth() - Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT) - 2.0f
											* Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT));
						}
					}
				}
				y2 -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				PDFUtil.renderFrame(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR), cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CVPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT), cursor.getBlockY(),
						cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_COLUMN_INDENT, Bundle.CV_PDF, CVPDFDefaultSettings.X_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(CVPDFSettingCodes.COLUMN_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.COLUMN_LINE_WIDTH));
				break;
			case SIGNATURE_DATE:
				y = cursor.getBlockY() - Settings.getFloat(CVPDFSettingCodes.Y_FRAME_UPPER_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_UPPER_INDENT_SIGNATURE);
				x = cursor.getBlockX() + Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE);
				x1 = cursor.getBlockX() + cursor.getBlockWidth()
						- Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE)
						- Settings.getFloat(CVPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.SIGNATURE_LINE_LENGTH);
				if (Settings.getFloat(CVPDFSettingCodes.DATE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_LINE_LENGTH) > 0.0f) {
					x1 -= Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE)
							+ Settings.getFloat(CVPDFSettingCodes.DATE_LABEL_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_LABEL_WIDTH)
							+ Settings.getFloat(CVPDFSettingCodes.DATE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_LINE_LENGTH);
				}
				y -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.SIGNATURE, PDFUtil.DEFAULT_LABEL), x, y, PDFUtil.Alignment.TOP_LEFT,
						x1 - x - Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE));
				y1 = y;
				PDFUtil.renderLine(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR), x1, y,
						x1 + Settings.getFloat(CVPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.SIGNATURE_LINE_LENGTH), y,
						Settings.getFloat(CVPDFSettingCodes.SIGNATURE_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.SIGNATURE_LINE_WIDTH));
				y -= Settings.getFloat(CVPDFSettingCodes.Y_OFFSET_SIGNATURE_ANNOTATION, Bundle.CV_PDF, CVPDFDefaultSettings.Y_OFFSET_SIGNATURE_ANNOTATION);
				y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.TINY,
						Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR), getSignatureLabel(),
						x1 + Settings.getFloat(CVPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.SIGNATURE_LINE_LENGTH) / 2.0f, y,
						PDFUtil.Alignment.TOP_CENTER);
				y -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_LOWER_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_LOWER_INDENT_SIGNATURE);
				height = cursor.getBlockY() - y;
				if (Settings.getFloat(CVPDFSettingCodes.DATE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_LINE_LENGTH) > 0.0f) {
					y = y1;
					x = x1 + Settings.getFloat(CVPDFSettingCodes.SIGNATURE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.SIGNATURE_LINE_LENGTH)
							+ Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE);
					x1 = x + Settings.getFloat(CVPDFSettingCodes.DATE_LABEL_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_LABEL_WIDTH);
					PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
							Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
							L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.DATE, PDFUtil.DEFAULT_LABEL), x, y, PDFUtil.Alignment.BOTTOM_LEFT,
							x1 - x - Settings.getFloat(CVPDFSettingCodes.X_FRAME_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.X_FRAME_INDENT_SIGNATURE));
					PDFUtil.renderLine(contentStream, Settings.getColor(CVPDFSettingCodes.FRAME_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.FRAME_COLOR), x1, y,
							x1 + Settings.getFloat(CVPDFSettingCodes.DATE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_LINE_LENGTH), y,
							Settings.getFloat(CVPDFSettingCodes.SIGNATURE_LINE_WIDTH, Bundle.CV_PDF, CVPDFDefaultSettings.SIGNATURE_LINE_WIDTH));
					y -= Settings.getFloat(CVPDFSettingCodes.Y_OFFSET_SIGNATURE_ANNOTATION, Bundle.CV_PDF, CVPDFDefaultSettings.Y_OFFSET_SIGNATURE_ANNOTATION);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.TINY,
							Settings.getColor(CVPDFSettingCodes.TEXT_COLOR, Bundle.CV_PDF, CVPDFDefaultSettings.TEXT_COLOR),
							L10nUtil.getCVPDFLabel(Locales.CV_PDF, CVPDFLabelCodes.DATE_ANNOTATION, PDFUtil.DEFAULT_LABEL),
							x1 + Settings.getFloat(CVPDFSettingCodes.DATE_LINE_LENGTH, Bundle.CV_PDF, CVPDFDefaultSettings.DATE_LINE_LENGTH) / 2.0f, y,
							PDFUtil.Alignment.TOP_CENTER);
					y -= Settings.getFloat(CVPDFSettingCodes.Y_FRAME_LOWER_INDENT_SIGNATURE, Bundle.CV_PDF, CVPDFDefaultSettings.Y_FRAME_LOWER_INDENT_SIGNATURE);
				}
				break;
			case SPACER:
				height = Settings.getFloat(CVPDFSettingCodes.SPACER_HEIGHT, Bundle.CV_PDF, CVPDFDefaultSettings.SPACER_HEIGHT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}
}