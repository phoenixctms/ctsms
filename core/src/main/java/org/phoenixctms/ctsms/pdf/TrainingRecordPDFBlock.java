package org.phoenixctms.ctsms.pdf;

import java.util.Collection;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.phoenixctms.ctsms.pdf.PDFUtil.Alignment;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipationStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffTagVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;

public class TrainingRecordPDFBlock {

	public enum BlockType {
		NEW_PAGE,
		SPACER,
		HEAD,
		NAME,
		STAFF_TAGS,
		SECTION,
	}

	protected StaffOutVO staff;
	protected StaffOutVO lastInstitution;
	protected Collection<StaffTagValueOutVO> staffTagValues;
	protected TrainingRecordSectionVO trainingRecordSection;
	protected Collection<CourseParticipationStatusEntryOutVO> participations;
	protected boolean hasTrials;
	protected boolean hasInstitutions;
	protected boolean hasCertificates;
	protected BlockType type;

	public TrainingRecordPDFBlock() {
		type = BlockType.SPACER;
	}

	public TrainingRecordPDFBlock(TrainingRecordSectionVO trainingRecordSection, Collection<CourseParticipationStatusEntryOutVO> participations) {
		this.trainingRecordSection = trainingRecordSection;
		setParticipations(participations);
		this.type = BlockType.SECTION;
	}

	public TrainingRecordPDFBlock(TrainingRecordSectionVO trainingRecordSection, Collection<CourseParticipationStatusEntryOutVO> participations, boolean hasTrials,
			boolean hasInstitutions, boolean hasCertificates) {
		this.trainingRecordSection = trainingRecordSection;
		this.hasTrials = hasTrials;
		this.hasInstitutions = hasInstitutions;
		this.hasCertificates = hasCertificates;
		this.participations = participations;
		this.type = BlockType.SECTION;
	}

	private void setParticipations(Collection<CourseParticipationStatusEntryOutVO> participations) {
		hasTrials = false;
		hasInstitutions = false;
		hasCertificates = false;
		this.participations = participations;
		lastInstitution = null;
		if (participations != null) {
			Iterator<CourseParticipationStatusEntryOutVO> it = participations.iterator();
			while (it.hasNext()) {
				CourseOutVO course = it.next().getCourse();
				if (course.getTrial() != null) {
					hasTrials = true;
				}
				if (course.getInstitution() != null) {
					hasInstitutions = true;
				}
				if (hasCourseCertificate(course)) {
					hasCertificates = true;
				}
				lastInstitution = course.getInstitution();
			}
		}
	}

	public TrainingRecordPDFBlock(Collection<StaffTagValueOutVO> staffTagValues) {
		this.staffTagValues = staffTagValues;
		this.type = BlockType.STAFF_TAGS;
	}

	public TrainingRecordPDFBlock(StaffOutVO staff, BlockType type) {
		this.staff = staff;
		this.type = type;
	}

	public TrainingRecordPDFBlock(BlockType type) {
		this.type = type;
	}

	protected String getName() {
		if (staff != null && staff.isPerson()) {
			return CommonUtil.getCvStaffName(staff, true, L10nUtil.getLocale(Locales.TRAINING_RECORD_PDF));
		}
		return "";
	}

	public float getHeight(TrainingRecordPDFBlockCursor cursor) throws Exception {
		return renderBlock(null, cursor);
	}

	protected String getSectionDescription() {
		if (trainingRecordSection != null) {
			return L10nUtil.getTrainingRecordSectionDescription(Locales.TRAINING_RECORD_PDF, trainingRecordSection.getDescriptionL10nKey());
		}
		return "";
	}

	protected String getSectionName() {
		if (trainingRecordSection != null) {
			return L10nUtil.getTrainingRecordSectionName(Locales.TRAINING_RECORD_PDF, trainingRecordSection.getNameL10nKey());
		}
		return "";
	}

	public StaffOutVO getStaff() {
		return staff;
	}

	public StaffOutVO getLastInstitution() {
		return lastInstitution;
	}

	public BlockType getType() {
		return type;
	}

	public TrainingRecordSectionVO getTrainingRecordSection() {
		return trainingRecordSection;
	}

	public Collection<CourseParticipationStatusEntryOutVO> getParticipations() {
		return participations;
	}

	public Collection<StaffTagValueOutVO> getStaffTagValues() {
		return staffTagValues;
	}

	protected static String getCourseStopString(CourseOutVO course) {
		if (course != null) {
			return Settings.getSimpleDateFormat(TrainingRecordPDFSettingCodes.COURSE_STOP_DATE_PATTERN, Bundle.TRAINING_RECORD_PDF,
					TrainingRecordPDFDefaultSettings.COURSE_STOP_DATE_PATTERN, Locales.TRAINING_RECORD_PDF).format(course.getStop());
		}
		return "";
	}

	protected static String getCourseTrialString(CourseOutVO course) {
		if (course != null && course.getTrial() != null) {
			return CommonUtil.trialOutVOToString(course.getTrial());
		}
		return "";
	}

	protected static String getCourseTitleString(CourseOutVO course) {
		if (course != null) {
			if (CommonUtil.isEmptyString(course.getCvTitle())) {
				return CommonUtil.courseOutVOToString(course);
			} else {
				return course.getCvTitle();
			}
		}
		return "";
	}

	protected String getCourseInstitutionString(CourseOutVO course) {
		if (course != null && course.getInstitution() != null) {
			if (Settings.getBoolean(TrainingRecordPDFSettingCodes.INSTITUTION_STAFF_PATH, Bundle.TRAINING_RECORD_PDF,
					TrainingRecordPDFDefaultSettings.INSTITUTION_STAFF_PATH)) {
				return ServiceUtil.getCvStaffPathString(course.getInstitution());
			} else {
				return CommonUtil.getCvStaffName(course.getInstitution());
			}
		}
		return "";
	}

	protected static String getStaffTagName(StaffTagVO tag) {
		return L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.STAFF_TAG_NAME_LABEL, PDFUtil.DEFAULT_LABEL,
				L10nUtil.getStaffTagName(Locales.TRAINING_RECORD_PDF, tag.getNameL10nKey()));
	}

	public boolean hasTrials() {
		return hasTrials;
	}

	public boolean hasInstitutions() {
		return hasInstitutions;
	}

	public boolean hasCertificates() {
		return hasCertificates;
	}

	public float renderBlock(PDPageContentStream contentStream, TrainingRecordPDFBlockCursor cursor) throws Exception {
		String line;
		float x;
		float y;
		float x1;
		float x2;
		float x3;
		float x4;
		float x5;
		float y1;
		float y2;
		float y3;
		float y4;
		float y5;
		float height;
		switch (type) {
			case HEAD:
				line = L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.HEADLINE, PDFUtil.DEFAULT_LABEL);
				x = cursor.getBlockCenterX();
				y = cursor.getBlockY();
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.LARGE,
							Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR), line, x,
							y, PDFUtil.Alignment.TOP_CENTER);
				}
				height = cursor.getBlockY() - y;
				break;
			case NAME:
				y = cursor.getBlockY();
				x = cursor.getBlockX()
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.X_FRAME_INDENT);
				line = L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.NAME, PDFUtil.DEFAULT_LABEL);
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
							line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				line = L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.NAME_DESCRIPTION, PDFUtil.DEFAULT_LABEL);
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE10,
							Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
							line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_NAME_INDENT, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_NAME_INDENT);
				x = cursor.getBlockCenterX();
				y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.BIG,
						Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR), getName(), x, y,
						PDFUtil.Alignment.TOP_CENTER);
				y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_NAME_INDENT, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_NAME_INDENT);
				height = cursor.getBlockY() - y;
				PDFUtil.renderFrame(contentStream,
						Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR), cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				break;
			case STAFF_TAGS:
				y = cursor.getBlockY();
				x = cursor.getBlockX()
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.X_FRAME_INDENT);
				line = L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.ROLES, PDFUtil.DEFAULT_LABEL);
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
							line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				line = L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF, TrainingRecordPDFLabelCodes.ROLES_DESCRIPTION, PDFUtil.DEFAULT_LABEL);
				if (!CommonUtil.isEmptyString(line)) {
					y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_FRAME_INDENT);
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE10,
							Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
							line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
				}
				y1 = y - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_STAFF_TAGS_INDENT, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_STAFF_TAGS_INDENT);
				y2 = y1;
				x1 = cursor.getBlockX()
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_STAFF_TAGS_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.X_STAFF_TAGS_INDENT);
				x2 = x1
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_STAFF_TAG_NAME_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_STAFF_TAG_NAME_WIDTH);
				if (staffTagValues != null) {
					Iterator<StaffTagValueOutVO> it = staffTagValues.iterator();
					while (it.hasNext()) {
						StaffTagValueOutVO tagValue = it.next();
						y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12,
								Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
								getStaffTagName(tagValue.getTag()), x1, y1, PDFUtil.Alignment.TOP_LEFT);
						y2 -= PDFUtil.renderMultilineText(
								contentStream,
								cursor.getFontB(),
								PDFUtil.FontSize.SIZE12,
								Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
								tagValue.getValue(),
								x2,
								y2,
								PDFUtil.Alignment.TOP_LEFT,
								cursor.getBlockX() + cursor.getBlockWidth()
										- x2
										- Settings.getFloat(TrainingRecordPDFSettingCodes.X_STAFF_TAGS_INDENT, Bundle.TRAINING_RECORD_PDF,
												TrainingRecordPDFDefaultSettings.X_STAFF_TAGS_INDENT));
						y1 = Math.min(y1, y2);
						if (it.hasNext()) {
							y1 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_STAFF_TAGS_INDENT, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.Y_STAFF_TAGS_INDENT);
						}
						y2 = y1;
					}
				}
				y = y1 - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_STAFF_TAGS_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_STAFF_TAGS_INDENT);
				height = cursor.getBlockY() - y;
				PDFUtil.renderFrame(contentStream,
						Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR), cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				break;
			case SECTION:
				y = cursor.getBlockY()
						- Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_FRAME_INDENT);
				x = cursor.getBlockX()
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.X_FRAME_INDENT);
				line = L10nUtil.getTrainingRecordSectionName(Locales.TRAINING_RECORD_PDF, trainingRecordSection.getNameL10nKey());
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE12,
							Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
							line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
					y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_FRAME_INDENT);
				}
				line = L10nUtil.getTrainingRecordSectionDescription(Locales.TRAINING_RECORD_PDF, trainingRecordSection.getDescriptionL10nKey());
				if (!CommonUtil.isEmptyString(line)) {
					y -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE10,
							Settings.getColor(TrainingRecordPDFSettingCodes.TEXT_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.TEXT_COLOR),
							line, x, y,
							PDFUtil.Alignment.TOP_LEFT);
					y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.Y_FRAME_INDENT);
				}
				PDFUtil.renderLine(contentStream,
						Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX(),
						y,
						cursor.getBlockX() + cursor.getBlockWidth(),
						y,
						Settings.getFloat(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
				y1 = y - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
				x1 = cursor.getBlockX()
						+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH) / 2.0f;
				y1 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE11, Settings.getColor(
						TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
						L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF,
								TrainingRecordPDFLabelCodes.COURSE_STOP_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x1, y1, PDFUtil.Alignment.TOP_CENTER, Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH));
				x1 += Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH) / 2.0f;
				y1 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
				if (hasTrials) {
					y2 = y - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
					x2 = x1 + Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH) / 2.0f;
					y2 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE11, Settings.getColor(
							TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
							L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF,
									TrainingRecordPDFLabelCodes.COURSE_TRIAL_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
							x2, y2, PDFUtil.Alignment.TOP_CENTER,
							Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH));
					x2 += Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH) / 2.0f;
					y2 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
				} else {
					x2 = x1;
					y2 = y1;
				}
				y3 = y - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
				x3 = x2 + (cursor.getBlockWidth()
						- Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH)
						- (hasTrials ? Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH) : 0.0f)
						- (hasInstitutions ? Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH) : 0.0f)
						- (hasCertificates ? Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_CERTIFICATE_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_CERTIFICATE_WIDTH) : 0.0f))
						/ 2.0f;
				y3 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE11, Settings.getColor(
						TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
						L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF,
								TrainingRecordPDFLabelCodes.COURSE_TITLE_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
						x3, y3, PDFUtil.Alignment.TOP_CENTER, (x3 - x2) * 2.0f);
				x3 += x3 - x2;
				y3 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
						TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
				if (hasInstitutions) {
					y4 = y - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
					x4 = x3 + Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH) / 2.0f;
					y4 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE11, Settings.getColor(
							TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
							L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF,
									TrainingRecordPDFLabelCodes.COURSE_INSTITUTION_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
							x4, y4, PDFUtil.Alignment.TOP_CENTER,
							Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH));
					x4 += Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH) / 2.0f;
					y4 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
				} else {
					x4 = x3;
					y4 = y3;
				}
				if (hasCertificates) {
					y5 = y - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
					x5 = x4 + Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_CERTIFICATE_WIDTH, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_CERTIFICATE_WIDTH) / 2.0f;
					y5 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontB(), PDFUtil.FontSize.SIZE11, Settings.getColor(
							TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
							L10nUtil.getTrainingRecordPDFLabel(Locales.TRAINING_RECORD_PDF,
									TrainingRecordPDFLabelCodes.COURSE_CERTIFICATE_COLUMN_NAME, PDFUtil.DEFAULT_LABEL),
							x5, y5, PDFUtil.Alignment.TOP_CENTER,
							Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_COLUMN_COURSE_CERTIFICATE_WIDTH, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_COLUMN_COURSE_CERTIFICATE_WIDTH));
					y5 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_HEAD_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
							TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_HEAD_FRAME_INDENT);
				} else {
					x5 = x4;
					y5 = y4;
				}
				height = y - Math.min(y1, Math.min(y2, Math.min(y3, Math.min(y4, y5))));
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
						x1,
						y,
						x1,
						y - height, Settings.getFloat(
								TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
				if (hasTrials) {
					PDFUtil.renderLine(
							contentStream,
							Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
							x2,
							y,
							x2,
							y - height, Settings.getFloat(
									TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
				}
				PDFUtil.renderLine(
						contentStream,
						Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
						x3,
						y,
						x3,
						y - height, Settings.getFloat(
								TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
				if (hasInstitutions) {
					PDFUtil.renderLine(
							contentStream,
							Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
							x4,
							y,
							x4,
							y - height, Settings.getFloat(
									TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
				}
				if (participations != null) {
					Iterator<CourseParticipationStatusEntryOutVO> it = participations.iterator();
					while (it.hasNext()) {
						CourseParticipationStatusEntryOutVO participation = it.next();
						y -= height;
						PDFUtil.renderLine(contentStream,
								Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR),
								cursor.getBlockX(),
								y,
								cursor.getBlockX() + cursor.getBlockWidth(),
								y,
								Settings.getFloat(
										TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
						y1 = y - Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						y1 -= PDFUtil.renderTextLine(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12, Settings.getColor(
								TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
								getCourseStopString(participation.getCourse()),
								x,
								y1, PDFUtil.Alignment.TOP_LEFT);
						y1 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						if (hasTrials) {
							y2 = y
									- Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
							y2 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12, Settings.getColor(
									TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
									getCourseTrialString(participation.getCourse()),
									x1 + Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT),
									y2, PDFUtil.Alignment.TOP_LEFT,
									x2 - x1 - 2.0f * Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT));
							y2 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						} else {
							y2 = y1;
						}
						y3 = y
								- Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						y3 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12, Settings.getColor(
								TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
								getCourseTitleString(participation.getCourse()),
								x2 + Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT),
								y3, PDFUtil.Alignment.TOP_LEFT,
								x3 - x2 - 2.0f * Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT));
						y3 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						if (hasInstitutions) {
							y4 = y
									- Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
							y4 -= PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE12, Settings.getColor(
									TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
									getCourseInstitutionString(participation.getCourse()),
									x3 + Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT),
									y4, PDFUtil.Alignment.TOP_LEFT,
									x4 - x3 - 2.0f * Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT));
							y4 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						} else {
							y4 = y3;
						}
						if (hasCertificates) {
							y5 = y
									- Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
							y5 -= renderHasCourseCertificate(x5,
									y5, contentStream, cursor, participation.getCourse());
							y5 -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						} else {
							y5 = y4;
						}
						height = y - Math.min(y1, Math.min(y2, Math.min(y3, Math.min(y4, y5))));
						PDFUtil.renderLine(
								contentStream,
								Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
								x1,
								y,
								x1,
								y - height, Settings.getFloat(
										TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
						if (hasTrials) {
							PDFUtil.renderLine(
									contentStream,
									Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
									x2,
									y,
									x2,
									y - height, Settings.getFloat(
											TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
						}
						PDFUtil.renderLine(
								contentStream,
								Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
								x3,
								y,
								x3,
								y - height, Settings.getFloat(
										TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
										TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
						if (hasInstitutions) {
							PDFUtil.renderLine(
									contentStream,
									Settings.getColor(TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_COLOR, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_COLOR),
									x4,
									y,
									x4,
									y - height, Settings.getFloat(
											TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
						}
						if (participation.getShowCommentTrainingRecord() && !CommonUtil.isEmptyString(participation.getComment())) {
							y -= height;
							PDFUtil.renderLine(contentStream,
									Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR),
									cursor.getBlockX(),
									y,
									cursor.getBlockX() + cursor.getBlockWidth(),
									y,
									Settings.getFloat(
											TrainingRecordPDFSettingCodes.SECTION_TABLE_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
											TrainingRecordPDFDefaultSettings.SECTION_TABLE_FRAME_LINE_WIDTH));
							y -= Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
							height = PDFUtil.renderMultilineText(contentStream, cursor.getFontA(), PDFUtil.FontSize.SIZE10, Settings.getColor(
									TrainingRecordPDFSettingCodes.SECTION_TABLE_TEXT_COLOR, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.SECTION_TABLE_TEXT_COLOR),
									participation.getComment(),
									x, //+ Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
									//		TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT),
									y, PDFUtil.Alignment.TOP_LEFT,
									cursor.getBlockX() + cursor.getBlockWidth() - x
											- Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
													TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT));
							// - 2.0f * Settings.getFloat(TrainingRecordPDFSettingCodes.X_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
							//		TrainingRecordPDFDefaultSettings.X_SECTION_TABLE_FRAME_INDENT));
							height += Settings.getFloat(TrainingRecordPDFSettingCodes.Y_SECTION_TABLE_FRAME_INDENT, Bundle.TRAINING_RECORD_PDF,
									TrainingRecordPDFDefaultSettings.Y_SECTION_TABLE_FRAME_INDENT);
						}
					}
				}
				y -= height;
				height = cursor.getBlockY() - y;
				PDFUtil.renderFrame(contentStream,
						Settings.getColor(TrainingRecordPDFSettingCodes.FRAME_COLOR, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.FRAME_COLOR),
						cursor.getBlockX(),
						cursor.getBlockY(), cursor.getBlockWidth(), height, PDFUtil.Alignment.TOP_LEFT,
						Settings.getFloat(TrainingRecordPDFSettingCodes.BLOCK_FRAME_LINE_WIDTH, Bundle.TRAINING_RECORD_PDF,
								TrainingRecordPDFDefaultSettings.BLOCK_FRAME_LINE_WIDTH));
				break;
			case SPACER:
				height = Settings.getFloat(TrainingRecordPDFSettingCodes.SPACER_HEIGHT, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.SPACER_HEIGHT);
				break;
			default:
				height = 0.0f;
				break;
		}
		return height;
	}

	private static boolean hasCourseCertificate(CourseOutVO course) {
		if (course != null && course.getCertificate()) { //todo: check for uploaded certificate ...
			return true;
		}
		return false;
	}

	private static float renderHasCourseCertificate(float x, float y, PDPageContentStream contentStream, TrainingRecordPDFBlockCursor cursor, CourseOutVO course) throws Exception {
		PDFJpeg ximage;
		if (hasCourseCertificate(course)) {
			ximage = cursor.getCheckboxCheckedImage();
		} else {
			ximage = cursor.getCheckboxUncheckedImage();
		}
		PDFUtil.renderImage(contentStream, ximage, x, y, Alignment.TOP_CENTER);
		return ximage.getHeightPoints();
	}
}