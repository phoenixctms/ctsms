package org.phoenixctms.ctsms.pdf;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusTypeVO;
import org.phoenixctms.ctsms.vo.LecturerCompetenceVO;
import org.phoenixctms.ctsms.vo.LecturerOutVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class CourseCertificatePDFBlock {

	public enum BlockType {
		SPACER,
		SPACER_SMALL,
		HEAD,
		CONFIRMATION_1,
		PARTICIPANT,
		COURSE_PERIOD,
		CONFIRMATION_2,
		COURSE_TITLE,
		CONFIRMATION_3,
		COURSE_DESCRIPTION,
		TRIAL_TITLE,
		COURSE_VALIDITY,
		COURSE_COMPETENCE_LECTURERS,
		INSTITUTION,
		INSTITUTION_ADDRESS
	}

	private static void populateOrganisationList(ArrayList<String> organisationList, StaffOutVO staff) {
		if (staff != null) {
			if (!staff.isPerson()) {
				organisationList.add(0, staff.getNameWithTitles());
			}
			populateOrganisationList(organisationList, staff.getParent());
		}
	}

	private CourseOutVO course;
	private TrialOutVO trial;
	private StaffOutVO participant;
	private CourseParticipationStatusEntryOutVO participation;
	private LecturerCompetenceVO competence;
	private Collection<LecturerOutVO> lecturers;
	private StaffOutVO institution;
	private StaffAddressOutVO address;
	private BlockType type;

	public CourseCertificatePDFBlock() {
		this.type = BlockType.SPACER;
	}

	public CourseCertificatePDFBlock(BlockType type) {
		this.type = type;
	}

	public CourseCertificatePDFBlock(CourseOutVO course, BlockType type) {
		this.course = course;
		this.type = type;
	}

	public CourseCertificatePDFBlock(CourseParticipationStatusEntryOutVO participation) {
		this.participation = participation;
		this.type = BlockType.CONFIRMATION_3;
	}

	public CourseCertificatePDFBlock(CourseParticipationStatusEntryOutVO participation, BlockType type) {
		this.participation = participation;
		this.type = type;
	}

	public CourseCertificatePDFBlock(LecturerCompetenceVO competence, Collection<LecturerOutVO> lecturers) {
		this.competence = competence;
		this.lecturers = lecturers;
		this.type = BlockType.COURSE_COMPETENCE_LECTURERS;
	}

	public CourseCertificatePDFBlock(StaffOutVO staff, BlockType type) {
		this.participant = staff;
		this.institution = staff;
		this.type = type;
	}

	public CourseCertificatePDFBlock(StaffOutVO institution, StaffAddressOutVO address) {
		this.institution = institution;
		this.address = address;
		this.type = BlockType.INSTITUTION_ADDRESS;
	}

	public CourseCertificatePDFBlock(TrialOutVO trial) {
		this.trial = trial;
		this.type = BlockType.TRIAL_TITLE;
	}

	private String getAddress() {
		StringBuilder sb = new StringBuilder();
		if (institution != null && address != null) {
			ArrayList<String> organisationList = new ArrayList<String>();
			populateOrganisationList(organisationList, institution);
			Iterator<String> it = organisationList.iterator();
			while (it.hasNext()) {
				CommonUtil.appendString(sb, it.next(), PDFUtil.PDF_LINE_BREAK);
			}
			StringBuilder hed = new StringBuilder();
			StringBuilder zc = new StringBuilder();
			CommonUtil.appendString(sb, address.getStreetName(), PDFUtil.PDF_LINE_BREAK, "?");
			CommonUtil.appendString(hed, address.getHouseNumber(), null, "?");
			CommonUtil.appendString(hed, address.getEntrance(), "/");
			CommonUtil.appendString(hed, address.getDoorNumber(), "/");
			CommonUtil.appendString(sb, hed.toString(), " ");
			CommonUtil.appendString(zc, address.getZipCode(), null, "?");
			CommonUtil.appendString(zc, address.getCityName(), " ", "?");
			CommonUtil.appendString(sb, zc.toString(), PDFUtil.PDF_LINE_BREAK);
			CommonUtil.appendString(sb, address.getCountryName(), PDFUtil.PDF_LINE_BREAK);
		}
		return sb.toString();
	}

	private String getCourseCvTitle() {
		if (course != null) {
			return course.getCvTitle();
		}
		return "";
	}

	private String getCourseDescription() {
		if (course != null) {
			return course.getDescription();
		}
		return "";
	}

	private String getCourseInstitutionString() {
		if (institution != null) {
			return institution.getNameWithTitles();
		}
		return "";
	}

	private String getCoursePeriodString() {
		if (course != null) {
			Date start = course.getStart();
			Date stop = course.getStop();
			DateFormat coursePeriodDateFormat = Settings.getSimpleDateFormat(CourseCertificatePDFSettingCodes.COURSE_DATE_TIME_PATTERN, Bundle.COURSE_CERTIFICATE_PDF,
					CourseCertificatePDFDefaultSettings.COURSE_DATE_TIME_PATTERN, Locales.COURSE_CERTIFICATE_PDF); // CoreUtil.getDateFormat(Settings.getCvPositionDatePattern());
			if (start != null && stop != null) {
				return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.COURSE_FROM_TO, PDFUtil.DEFAULT_LABEL,
						coursePeriodDateFormat.format(start), coursePeriodDateFormat.format(stop));
			} else if (stop != null) {
				return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.COURSE_DATE, PDFUtil.DEFAULT_LABEL,
						coursePeriodDateFormat.format(stop));
			} else {
			}
		}
		return "";
	}

	private String getCourseValidityString() {
		if (course != null && course.getValidityPeriod() != null) {
			String courseValidityString;
			DateFormat coursePeriodDateFormat = Settings.getSimpleDateFormat(CourseCertificatePDFSettingCodes.COURSE_DATE_TIME_PATTERN, Bundle.COURSE_CERTIFICATE_PDF,
					CourseCertificatePDFDefaultSettings.COURSE_DATE_TIME_PATTERN, Locales.COURSE_CERTIFICATE_PDF);
			if (!VariablePeriod.EXPLICIT.equals(course.getValidityPeriod().getPeriod())) {
				courseValidityString = L10nUtil.getVariablePeriodName(Locales.COURSE_CERTIFICATE_PDF, course.getValidityPeriod().getPeriod().name());
			} else {
				courseValidityString = L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.DAYS_LABEL, PDFUtil.DEFAULT_LABEL,
						course.getValidityPeriodDays().toString());
			}
			return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.COURSE_VALIDITY_STRING, PDFUtil.DEFAULT_LABEL,
					courseValidityString, coursePeriodDateFormat.format(course.getExpiration()));
		}
		return "";
	}

	private String getGenderSpecificParticipantName() {
		if (participant != null) {
			switch (participant.getGender().getSex()) {
				case MALE:
				case TRANSGENDER_MALE:
					return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.PARTICIPANT_MALE_SALUTATION, PDFUtil.DEFAULT_LABEL,
							participant.getNameWithTitles());
				case FEMALE:
				case TRANSGENDER_FEMALE:
					return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.PARTICIPANT_FEMALE_SALUTATION,
							PDFUtil.DEFAULT_LABEL, participant.getNameWithTitles());
				default:
					return participant.getNameWithTitles();
			}
		}
		return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.PARTICIPANT_MALE_FEMALE_SALUTATION, PDFUtil.DEFAULT_LABEL);
	}

	public float getHeight(CourseCertificatePDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	private String getLecturerCompetenceString() {
		StringBuilder sb = new StringBuilder();
		if (competence != null) {
			sb.append(L10nUtil.getLecturerCompetenceName(Locales.COURSE_CERTIFICATE_PDF, competence.getNameL10nKey()));
			sb.append(L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.COMPETENCE_SUFFIX, ""));
		}
		return sb.toString();
	}

	private String getLecturerName(LecturerOutVO lecturer) {
		if (lecturer != null && lecturer.getStaff() != null) {
			return lecturer.getStaff().getName();
		}
		return null;
	}

	public CourseParticipationStatusEntryOutVO getParticipation() {
		return participation;
	}

	private String getPassedString() {
		if (participation != null) {
			CourseParticipationStatusTypeVO status = participation.getStatus();
			if (status == null || status.getPass()) {
				return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.CONFIRMATION_3_PASSED, PDFUtil.DEFAULT_LABEL);
			} else {
				return L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.CONFIRMATION_3_NOT_PASSED, PDFUtil.DEFAULT_LABEL);
			}
		}
		return "";
	}

	private String getTrialTitle() {
		if (trial != null) {
			return trial.getTitle();
		}
		return "";
	}

	public BlockType getType() {
		return type;
	}

	public float renderBlock(PDPageContentStream contentStream, CourseCertificatePDFBlockCursor cursor) throws Exception {
		String line;
		float x;
		float y;
		float y1;
		float y2;
		float height;
		switch (type) {
			case HEAD:
				line = L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.HEADLINE, PDFUtil.DEFAULT_LABEL);
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.LARGE,
							Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR), line, x,
							y, PDFUtil.Alignment.TOP_CENTER);
				}
				height = cursor.getBlockY() - y;
				break;
			case CONFIRMATION_1:
				line = L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.CONFIRMATION_1, PDFUtil.DEFAULT_LABEL);
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.BIG,
							Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR), line, x,
							y, PDFUtil.Alignment.TOP_CENTER);
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				}
				height = cursor.getBlockY() - y;
				break;
			case COURSE_PERIOD:
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.BIG,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						getCoursePeriodString(), x, y, PDFUtil.Alignment.TOP_CENTER);
				y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - y;
				break;
			case PARTICIPANT:
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.BIG,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						getGenderSpecificParticipantName(), x, y, PDFUtil.Alignment.TOP_CENTER);
				y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - y;
				break;
			case CONFIRMATION_2:
				line = L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.CONFIRMATION_2, PDFUtil.DEFAULT_LABEL);
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.BIG,
							Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR), line, x,
							y, PDFUtil.Alignment.TOP_CENTER);
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				}
				height = cursor.getBlockY() - y;
				break;
			case COURSE_TITLE:
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				y -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontB(),
						PDFUtil.FontSize.BIG,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						getCourseCvTitle(),
						x,
						y,
						PDFUtil.Alignment.TOP_CENTER,
						cursor.getBlockWidth()
								- 2.0f
										* Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
												CourseCertificatePDFDefaultSettings.X_FRAME_INDENT));
				y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - y;
				break;
			case CONFIRMATION_3:
				line = getPassedString();
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.BIG,
							Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR), line, x,
							y, PDFUtil.Alignment.TOP_CENTER);
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				}
				height = cursor.getBlockY() - y;
				break;
			case COURSE_DESCRIPTION:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.COURSE_DESCRIPTION, PDFUtil.DEFAULT_LABEL), x, y1,
						PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						getCourseDescription(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(CourseCertificatePDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
										CourseCertificatePDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
												CourseCertificatePDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case TRIAL_TITLE:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.TRIAL_TITLE, PDFUtil.DEFAULT_LABEL), x, y1,
						PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						getTrialTitle(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(CourseCertificatePDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
										CourseCertificatePDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
												CourseCertificatePDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_VALIDITY:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseCertificatePDFLabel(Locales.COURSE_CERTIFICATE_PDF, CourseCertificatePDFLabelCodes.COURSE_VALIDITY, PDFUtil.DEFAULT_LABEL), x, y1,
						PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						getCourseValidityString(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_COMPETENCE_LECTURERS:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
						getLecturerCompetenceString(),
						x,
						y1,
						PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CourseCertificatePDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
												CourseCertificatePDFDefaultSettings.X_FRAME_INDENT));
				y1 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				if (lecturers != null) {
					x = cursor.getBlockX()
							+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
									CourseCertificatePDFDefaultSettings.X_COLUMN_INDENT)
							+ Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.X_FRAME_INDENT);
					Iterator<LecturerOutVO> it = lecturers.iterator();
					while (it.hasNext()) {
						String lecturerName = getLecturerName(it.next());
						if (lecturerName != null) {
							y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontC(), PDFUtil.FontSize.MEDIUM,
									Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
									lecturerName, x, y2, PDFUtil.Alignment.TOP_LEFT);
						}
					}
				}
				y2 -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case INSTITUTION:
				line = getCourseInstitutionString();
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.BIG,
							Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR), line, x,
							y, PDFUtil.Alignment.TOP_CENTER);
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				}
				height = cursor.getBlockY() - y;
				break;
			case INSTITUTION_ADDRESS:
				line = getAddress();
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderMultilineText(
							contentStream,
							cursor.getFontA(),
							PDFUtil.FontSize.SMALL,
							Settings.getColor(CourseCertificatePDFSettingCodes.TEXT_COLOR, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.TEXT_COLOR),
							getAddress(),
							x,
							y,
							PDFUtil.Alignment.TOP_CENTER,
							cursor.getBlockWidth()
									- 2.0f
											* Settings.getFloat(CourseCertificatePDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF,
													CourseCertificatePDFDefaultSettings.X_FRAME_INDENT));
					y -= Settings.getFloat(CourseCertificatePDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.Y_FRAME_INDENT);
				}
				height = cursor.getBlockY() - y;
				break;
			case SPACER:
				height = Settings.getFloat(CourseCertificatePDFSettingCodes.SPACER_HEIGHT, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.SPACER_HEIGHT);
				break;
			case SPACER_SMALL:
				height = Settings.getFloat(CourseCertificatePDFSettingCodes.SPACER_SMALL_HEIGHT, Bundle.COURSE_CERTIFICATE_PDF,
						CourseCertificatePDFDefaultSettings.SPACER_SMALL_HEIGHT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}
}
