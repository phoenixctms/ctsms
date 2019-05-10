package org.phoenixctms.ctsms.pdf;

import java.io.IOException;
import java.text.DateFormat;
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
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.LecturerCompetenceVO;
import org.phoenixctms.ctsms.vo.LecturerOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;

public class CourseParticipantListPDFBlock {

	public enum BlockType {
		SPACER, HEAD, COURSE_NAME, DATA_AS_OF, COURSE_SELF_REGISTRATION, COURSE_PERIOD, COURSE_VALIDITY, COURSE_DESCRIPTION, TRIAL_TITLE, COURSE_CV_TITLE, COURSE_INSTITUTION, COURSE_CV_COMMENT_PRESET, COURSE_COMPETENCE_LECTURERS, PARTICIPANT_TABLE_HEAD, PARTICIPANT_TABLE_ROW
	}

	private CourseOutVO course;
	private TrialOutVO trial;
	private CourseParticipationStatusEntryOutVO participation;
	private LecturerCompetenceVO competence;
	private Collection<LecturerOutVO> lecturers;
	private Collection<InventoryBookingOutVO> bookings;
	private Date now;
	private BlockType type;

	public CourseParticipantListPDFBlock() {
		type = BlockType.SPACER;
	}

	public CourseParticipantListPDFBlock(BlockType type) {
		this.type = type;
	}

	public CourseParticipantListPDFBlock(Collection<InventoryBookingOutVO> bookings) {
		this.bookings = bookings;
		this.type = BlockType.PARTICIPANT_TABLE_HEAD;
	}

	public CourseParticipantListPDFBlock(CourseOutVO course, BlockType type) {
		this.course = course;
		this.type = type;
	}

	public CourseParticipantListPDFBlock(CourseOutVO course, Date now, BlockType type) {
		this.course = course;
		this.now = now;
		this.type = type;
	}

	public CourseParticipantListPDFBlock(CourseParticipationStatusEntryOutVO participation) {
		this.participation = participation;
		this.type = BlockType.PARTICIPANT_TABLE_ROW;
	}

	public CourseParticipantListPDFBlock(CourseParticipationStatusEntryOutVO participation, Collection<InventoryBookingOutVO> bookings) {
		this.participation = participation;
		this.bookings = bookings;
		this.type = BlockType.PARTICIPANT_TABLE_ROW;
	}

	public CourseParticipantListPDFBlock(LecturerCompetenceVO competence, Collection<LecturerOutVO> lecturers) {
		this.competence = competence;
		this.lecturers = lecturers;
		this.type = BlockType.COURSE_COMPETENCE_LECTURERS;
	}

	public CourseParticipantListPDFBlock(TrialOutVO trial) {
		this.trial = trial;
		this.type = BlockType.TRIAL_TITLE;
	}

	private String getBookingString(InventoryBookingOutVO booking) {
		if (booking != null) {
			DateFormat bookingDateFormat = Settings.getSimpleDateFormat(CourseParticipantListPDFSettingCodes.BOOKING_DATE_TIME_PATTERN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
					CourseParticipantListPDFDefaultSettings.BOOKING_DATE_TIME_PATTERN, Locales.COURSE_PARTICIPANT_LIST_PDF); // CoreUtil.getDateFormat(Settings.getCvPositionDatePattern());
			return L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.BOOKING_FROM_TO, PDFUtil.DEFAULT_LABEL,
					booking.getInventory().getName(), bookingDateFormat.format(booking.getStart()), bookingDateFormat.format(booking.getStop()));
		}
		return "";
	}

	public CourseOutVO getCourse() {
		return course;
	}

	private String getCourseCvCommentPreset() {
		if (course != null) {
			return course.getCvCommentPreset();
		}
		return "";
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
		if (course != null && course.getInstitution() != null) {
			return course.getInstitution().getNameWithTitles();
		}
		return "";
	}

	private String getCourseName() {
		if (course != null) {
			return course.getName();
		}
		return "";
	}

	private String getCoursePeriodString() {
		if (course != null) {
			Date start = course.getStart();
			Date stop = course.getStop();
			DateFormat coursePeriodDateFormat = Settings.getSimpleDateFormat(CourseParticipantListPDFSettingCodes.COURSE_DATE_TIME_PATTERN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
					CourseParticipantListPDFDefaultSettings.COURSE_DATE_TIME_PATTERN, Locales.COURSE_PARTICIPANT_LIST_PDF); // CoreUtil.getDateFormat(Settings.getCvPositionDatePattern());
			if (start != null && stop != null) {
				return L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_FROM_TO, PDFUtil.DEFAULT_LABEL,
						coursePeriodDateFormat.format(start), coursePeriodDateFormat.format(stop));
			} else if (stop != null) {
				return L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_DATE, PDFUtil.DEFAULT_LABEL,
						coursePeriodDateFormat.format(stop));
			} else {
			}
		}
		return "";
	}

	private String getCourseSelfRegistrationString() {
		if (course != null) {
			if (course.isSelfRegistration()) {
				return L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.TRUE, PDFUtil.DEFAULT_LABEL);
			} else {
				return L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.FALSE, PDFUtil.DEFAULT_LABEL);
			}
		}
		return "";
	}

	private String getCourseValidityString() {
		if (course != null && course.getValidityPeriod() != null) {
			if (!VariablePeriod.EXPLICIT.equals(course.getValidityPeriod().getPeriod())) {
				return L10nUtil.getVariablePeriodName(Locales.COURSE_PARTICIPANT_LIST_PDF, course.getValidityPeriod().getPeriod().name());
			} else {
				return L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.DAYS_LABEL, PDFUtil.DEFAULT_LABEL, course
						.getValidityPeriodDays().toString());
			}
		}
		return "";
	}

	public float getHeight(CourseParticipantListPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	private String getLecturerCompetenceString() {
		StringBuilder sb = new StringBuilder();
		if (competence != null) {
			sb.append(L10nUtil.getLecturerCompetenceName(Locales.COURSE_PARTICIPANT_LIST_PDF, competence.getNameL10nKey()));
			sb.append(L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COMPETENCE_SUFFIX, ""));
		}
		return sb.toString();
	}

	private String getLecturerName(LecturerOutVO lecturer) {
		if (lecturer != null && lecturer.getStaff() != null) {
			return lecturer.getStaff().getNameWithTitles();
		}
		return null;
	}

	private String getParticipantName() {
		if (participation != null) {
			return participation.getStaff().getNameWithTitles();
		}
		return "";
	}

	private boolean getShowBookingColumns() {
		if (bookings != null
				&& bookings.size() > 0
				&& Settings.getInt(CourseParticipantListPDFSettingCodes.BOOKING_COLUMN_COUNT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.BOOKING_COLUMN_COUNT) > 0) {
			return true;
		}
		return false;
	}

	private float getSignatureColumnWidth(float blockWidth) {
		float columnWidth = 0.0f;
		int bookingColumnCount = Settings.getInt(CourseParticipantListPDFSettingCodes.BOOKING_COLUMN_COUNT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
				CourseParticipantListPDFDefaultSettings.BOOKING_COLUMN_COUNT);
		if (getShowBookingColumns()) {
			columnWidth = blockWidth
					- Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT)
					- Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH);
			if (bookings.size() < bookingColumnCount) {
				columnWidth = columnWidth / (bookings.size());
			} else {
				columnWidth = columnWidth / (bookingColumnCount);
			}
		}
		return columnWidth;
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

	public float renderBlock(PDPageContentStream contentStream, CourseParticipantListPDFBlockCursor cursor) throws Exception {
		float x;
		float y;
		float y1;
		float y2;
		float y3;
		float height;
		float columnWidth;
		float signatureColumnWidth;
		boolean dateColumn;
		switch (type) {
			case HEAD:
				String line1 = L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.HEADLINE_LINE1,
						PDFUtil.DEFAULT_LABEL);
				String line2 = L10nUtil.getCourseParticipantListPDFLabel(
						Locales.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFLabelCodes.HEADLINE_LINE2,
						PDFUtil.DEFAULT_LABEL,
						now == null ? null
								: Settings.getSimpleDateFormat(CourseParticipantListPDFSettingCodes.NOW_DATE_TIME_PATTERN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.NOW_DATE_TIME_PATTERN, Locales.COURSE_PARTICIPANT_LIST_PDF).format(now));
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line1)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.LARGE, Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR,
							Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR), line1, x, y, PDFUtil.Alignment.TOP_CENTER);
				}
				if (!CommonUtil.isEmptyString(line2)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM, Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR,
							Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR), line2, x, y, PDFUtil.Alignment.TOP_CENTER);
				}
				height = cursor.getBlockY() - y;
				break;
			case DATA_AS_OF:
				String line = L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.DATA_AS_OF, PDFUtil.DEFAULT_LABEL);
				if (!CommonUtil.isEmptyString(line)) {
					y1 = cursor.getBlockY()
							- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
					x = cursor.getBlockX()
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
					y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR,
							Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR), line, x, y1, PDFUtil.Alignment.TOP_LEFT);
					y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
					y2 = cursor.getBlockY()
							- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
					x = cursor.getBlockX()
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
					y2 -= PDFUtil.renderTextLine(
							contentStream,
							cursor.getFontB(),
							PDFUtil.FontSize.MEDIUM,
							Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
							now == null ? ""
									: Settings.getSimpleDateFormat(CourseParticipantListPDFSettingCodes.NOW_DATE_TIME_PATTERN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
											CourseParticipantListPDFDefaultSettings.NOW_DATE_TIME_PATTERN, Locales.COURSE_PARTICIPANT_LIST_PDF).format(now),
							x, y2,
							PDFUtil.Alignment.TOP_LEFT);
					y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
					height = cursor.getBlockY() - Math.min(y1, y2);
				} else {
					height = 0.0f;
				}
				break;
			case COURSE_NAME:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_NAME, PDFUtil.DEFAULT_LABEL), x,
						y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCourseName(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_SELF_REGISTRATION:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil
						.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR,
								Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
								L10nUtil.getCourseParticipantListPDFLabel(
										Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_SELF_REGISTRATION, PDFUtil.DEFAULT_LABEL),
								x, y1,
								PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCourseSelfRegistrationString(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_PERIOD:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_PERIOD, PDFUtil.DEFAULT_LABEL), x,
						y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCoursePeriodString(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_DESCRIPTION:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR,
						Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(
								Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_DESCRIPTION, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCourseDescription(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
												CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case TRIAL_TITLE:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.TRIAL_TITLE, PDFUtil.DEFAULT_LABEL), x,
						y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getTrialTitle(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
												CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_VALIDITY:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_VALIDITY, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCourseValidityString(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_INSTITUTION:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR,
						Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(
								Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_INSTITUTION, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCourseInstitutionString(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_CV_TITLE:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_CV_TITLE, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCourseCvTitle(), x, y2, PDFUtil.Alignment.TOP_LEFT);
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_CV_COMMENT_PRESET:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil
						.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR,
								Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
								L10nUtil.getCourseParticipantListPDFLabel(
										Locales.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFLabelCodes.COURSE_CV_COMMENT_PRESET, PDFUtil.DEFAULT_LABEL),
								x, y1,
								PDFUtil.Alignment.TOP_LEFT);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y2 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getCourseCvCommentPreset(),
						x,
						y2,
						PDFUtil.Alignment.TOP_LEFT,
						cursor.getBlockWidth()
								- Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
												CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT));
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case COURSE_COMPETENCE_LECTURERS:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
				y1 -= PDFUtil.renderMultilineText(
						contentStream,
						cursor.getFontA(),
						PDFUtil.FontSize.MEDIUM,
						Settings.getColor(CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
						getLecturerCompetenceString(),
						x,
						y1,
						PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
								- 2.0f
										* Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
												CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT));
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				if (lecturers != null) {
					x = cursor.getBlockX()
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_COLUMN_INDENT)
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_FRAME_INDENT);
					Iterator<LecturerOutVO> it = lecturers.iterator();
					while (it.hasNext()) {
						String lecturerName = getLecturerName(it.next());
						if (lecturerName != null) {
							y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontC(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
									CourseParticipantListPDFSettingCodes.TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.TEXT_COLOR),
									lecturerName, x, y2, PDFUtil.Alignment.TOP_LEFT);
						}
					}
				}
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				break;
			case PARTICIPANT_TABLE_HEAD:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT) / 2.0f;
				y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_TEXT_COLOR),
						L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFLabelCodes.PARTICIPANT_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x, y1, PDFUtil.Alignment.TOP_CENTER);
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT);
				y2 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT);
				if (getShowBookingColumns()) {
					columnWidth = getSignatureColumnWidth(cursor.getBlockWidth());
					x = cursor.getBlockX()
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT)
							+ columnWidth
									/ 2.0f
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_FRAME_INDENT);
					float headHeight = 0.0f;
					Iterator<InventoryBookingOutVO> bookingIt = bookings.iterator();
					while (bookingIt.hasNext()) {
						InventoryBookingOutVO booking = bookingIt.next();
						headHeight = Math.max(headHeight, PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontA(),
								PDFUtil.FontSize.SMALL,
								Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_TEXT_COLOR),
								getBookingString(booking),
								x,
								y2,
								PDFUtil.Alignment.TOP_CENTER,
								columnWidth
										- 2
												* Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
														CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_FRAME_INDENT)));
						x += columnWidth;
					}
					y2 -= headHeight;
				} else {
					x = cursor.getBlockX()
							+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT)
							+ (cursor.getBlockWidth()
									- Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
											CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT)
									- Settings.getFloat(
											CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
											CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH))
									/ 2.0f;
					y2 -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
							CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_TEXT_COLOR),
							L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFLabelCodes.SIGNATURE_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
							x, y2, PDFUtil.Alignment.TOP_CENTER);
				}
				columnWidth = Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH);
				if (columnWidth > 0.0f) {
					y3 = cursor.getBlockY()
							- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT);
					y3 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
							CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_TEXT_COLOR),
							L10nUtil.getCourseParticipantListPDFLabel(Locales.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFLabelCodes.LECTURER_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
							cursor.getBlockX() + cursor.getBlockWidth() - columnWidth / 2.0f, y3,
							PDFUtil.Alignment.TOP_CENTER);
					y2 = Math.min(y2, y3);
				}
				y2 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT);
				height = cursor.getBlockY() - Math.min(y1, y2);
				PDFUtil.renderFrame(contentStream, Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockWidth(), height,
						PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH,
								Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT),
						cursor.getBlockY(),
						cursor.getBlockX()
								+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(
								CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				if (columnWidth > 0.0f) {
					PDFUtil.renderLine(contentStream, Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR), cursor.getBlockX() + cursor.getBlockWidth() - columnWidth, cursor.getBlockY(),
							cursor.getBlockX() + cursor.getBlockWidth() - columnWidth, cursor.getBlockY() - height, Settings.getFloat(
									CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				}
				renderBookingColumnBorders(contentStream, cursor, height, false);
				break;
			case PARTICIPANT_TABLE_ROW:
				y1 = cursor.getBlockY()
						- Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_PARTICIPANT_TABLE_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.Y_PARTICIPANT_TABLE_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_FRAME_INDENT);
				y1 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.MEDIUM, Settings.getColor(
						CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_TEXT_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_TEXT_COLOR), getParticipantName(), x, y1, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(
								CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT));
				y1 -= Settings.getFloat(CourseParticipantListPDFSettingCodes.Y_PARTICIPANT_TABLE_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.Y_PARTICIPANT_TABLE_FRAME_INDENT);
				height = cursor.getBlockY() - y1; // Math.min(y1, 22);
				PDFUtil.renderFrame(contentStream, Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR), cursor.getBlockX(), cursor.getBlockY(), cursor.getBlockWidth(), height,
						PDFUtil.Alignment.TOP_LEFT, Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH,
								Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR),
						cursor.getBlockX()
								+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT),
						cursor.getBlockY(),
						cursor.getBlockX()
								+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT),
						cursor.getBlockY() - height, Settings.getFloat(
								CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH));
				columnWidth = Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH);
				dateColumn = Settings.getBoolean(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_SIGNATURE_DATE_COLUMN, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_SIGNATURE_DATE_COLUMN);
				if (columnWidth > 0.0f) {
					PDFUtil.renderLine(contentStream, Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR), cursor.getBlockX() + cursor.getBlockWidth() - columnWidth, cursor.getBlockY(),
							cursor.getBlockX() + cursor.getBlockWidth() - columnWidth, cursor.getBlockY() - height, Settings.getFloat(
									CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_BLOCK_FRAME_LINE_WIDTH));
					signatureColumnWidth = Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LECTURER_SIGNATURE_WIDTH,
							Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LECTURER_SIGNATURE_WIDTH);
					if (dateColumn && signatureColumnWidth > 0.0f && columnWidth > signatureColumnWidth) {
						PDFUtil.renderLine(contentStream,
								Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR),
								cursor.getBlockX() + cursor.getBlockWidth() - signatureColumnWidth,
								cursor.getBlockY(),
								cursor.getBlockX() + cursor.getBlockWidth() - signatureColumnWidth,
								cursor.getBlockY() - height,
								Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
										CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH));
					}
				}
				renderBookingColumnBorders(contentStream, cursor, height, dateColumn);
				break;
			case SPACER:
				height = Settings.getFloat(CourseParticipantListPDFSettingCodes.SPACER_HEIGHT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
						CourseParticipantListPDFDefaultSettings.SPACER_HEIGHT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}

	private void renderBookingColumnBorders(PDPageContentStream contentStream, CourseParticipantListPDFBlockCursor cursor, float height, boolean dateColumn) throws IOException {
		float signatureColumnWidth = Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_PARTICIPANT_SIGNATURE_WIDTH,
				Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_SIGNATURE_SIGNATURE_WIDTH);
		float x;
		float columnWidth;
		if (getShowBookingColumns()) {
			columnWidth = getSignatureColumnWidth(cursor.getBlockWidth());
			x = cursor.getBlockX()
					+ Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_COLUMN_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_COLUMN_INDENT)
					+ columnWidth;
			for (int i = 0; i < bookings.size(); i++) {
				if (i < bookings.size() - 1) {
					PDFUtil.renderLine(contentStream, Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR), x, cursor.getBlockY(), x, cursor.getBlockY() - height,
							Settings.getFloat(
									CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH));
				}
				if (dateColumn && signatureColumnWidth > 0.0f && columnWidth > signatureColumnWidth) {
					PDFUtil.renderLine(contentStream,
							Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR),
							x - signatureColumnWidth,
							cursor.getBlockY(),
							x - signatureColumnWidth,
							cursor.getBlockY() - height,
							Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
									CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH));
				}
				x += columnWidth;
			}
		} else {
			x = cursor.getBlockX()
					+ cursor.getBlockWidth()
					- Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH);
			columnWidth = x
					- cursor.getBlockX()
					- Settings.getFloat(CourseParticipantListPDFSettingCodes.X_PARTICIPANT_TABLE_FRAME_INDENT, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.X_PARTICIPANT_TABLE_FRAME_INDENT);
			if (dateColumn && signatureColumnWidth > 0.0f && columnWidth > signatureColumnWidth) {
				PDFUtil.renderLine(contentStream,
						Settings.getColor(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_FRAME_COLOR, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_FRAME_COLOR),
						x - signatureColumnWidth,
						cursor.getBlockY(),
						x - signatureColumnWidth,
						cursor.getBlockY() - height,
						Settings.getFloat(CourseParticipantListPDFSettingCodes.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH, Bundle.COURSE_PARTICIPANT_LIST_PDF,
								CourseParticipantListPDFDefaultSettings.PARTICIPANT_TABLE_COLUMN_LINE_WIDTH));
			}
		}
	}
}