package org.phoenixctms.ctsms.pdf;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Compile;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public final class PDFPainterFactory {

	public static CVPDFPainter createCVPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(CVPDFSettingCodes.PAINTER_CLASS, Bundle.CV_PDF, CVPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new CVPDFPainter();
		} else {
			return (CVPDFPainter) Compile.loadClass(className,
					Settings.getStringList(CVPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.CV_PDF, CVPDFDefaultSettings.PAINTER_SOURCE_FILES)).newInstance();
		}
	}

	public static TrainingRecordPDFPainter createTrainingRecordPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(TrainingRecordPDFSettingCodes.PAINTER_CLASS, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new TrainingRecordPDFPainter();
		} else {
			return (TrainingRecordPDFPainter) Compile.loadClass(className,
					Settings.getStringList(TrainingRecordPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	public static CourseCertificatePDFPainter createCourseCertificatePDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(CourseCertificatePDFSettingCodes.PAINTER_CLASS, Bundle.COURSE_CERTIFICATE_PDF, CourseCertificatePDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new CourseCertificatePDFPainter();
		} else {
			return (CourseCertificatePDFPainter) Compile.loadClass(className,
					Settings.getStringList(CourseCertificatePDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.COURSE_CERTIFICATE_PDF,
							CourseCertificatePDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	public static CourseParticipantListPDFPainter createCourseParticipantListPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(CourseParticipantListPDFSettingCodes.PAINTER_CLASS, Bundle.COURSE_PARTICIPANT_LIST_PDF,
				CourseParticipantListPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new CourseParticipantListPDFPainter();
		} else {
			return (CourseParticipantListPDFPainter) Compile.loadClass(className,
					Settings.getStringList(CourseParticipantListPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.COURSE_PARTICIPANT_LIST_PDF,
							CourseParticipantListPDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	public static EcrfPDFPainter createEcrfPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(EcrfPDFSettingCodes.PAINTER_CLASS, Bundle.ECRF_PDF,
				EcrfPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new EcrfPDFPainter();
		} else {
			return (EcrfPDFPainter) Compile.loadClass(className,
					Settings.getStringList(EcrfPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.ECRF_PDF,
							EcrfPDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	public static InquiriesPDFPainter createInquiriesPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(InquiriesPDFSettingCodes.PAINTER_CLASS, Bundle.INQUIRIES_PDF,
				InquiriesPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new InquiriesPDFPainter();
		} else {
			return (InquiriesPDFPainter) Compile.loadClass(className,
					Settings.getStringList(InquiriesPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.INQUIRIES_PDF,
							InquiriesPDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	public static ProbandLetterPDFPainter createProbandLetterPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(ProbandLetterPDFSettingCodes.PAINTER_CLASS, Bundle.PROBAND_LETTER_PDF,
				ProbandLetterPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new ProbandLetterPDFPainter();
		} else {
			return (ProbandLetterPDFPainter) Compile.loadClass(className,
					Settings.getStringList(ProbandLetterPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.PROBAND_LETTER_PDF,
							ProbandLetterPDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	public static ProbandListEntryTagsPDFPainter createProbandListEntryTagsPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(ProbandListEntryTagsPDFSettingCodes.PAINTER_CLASS, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
				ProbandListEntryTagsPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new ProbandListEntryTagsPDFPainter();
		} else {
			return (ProbandListEntryTagsPDFPainter) Compile.loadClass(className,
					Settings.getStringList(ProbandListEntryTagsPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.PROBAND_LIST_ENTRY_TAGS_PDF,
							ProbandListEntryTagsPDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	public static ReimbursementsPDFPainter createReimbursementsPDFPainter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = Settings.getString(ReimbursementsPDFSettingCodes.PAINTER_CLASS, Bundle.REIMBURSEMENTS_PDF, ReimbursementsPDFDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new ReimbursementsPDFPainter();
		} else {
			return (ReimbursementsPDFPainter) Compile.loadClass(className,
					Settings.getStringList(ReimbursementsPDFSettingCodes.PAINTER_SOURCE_FILES, Bundle.REIMBURSEMENTS_PDF,
							ReimbursementsPDFDefaultSettings.PAINTER_SOURCE_FILES))
					.newInstance();
		}
	}

	private PDFPainterFactory() {
	}
}
