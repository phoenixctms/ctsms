package org.phoenixctms.ctsms.excel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.excel.VisitScheduleExcelWriter.Styles;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Compile;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public final class ExcelWriterFactory {

	public static AuditTrailExcelWriter createAuditTrailExcelWriter(boolean omitFields, ECRFFieldStatusQueue... queues) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(AuditTrailExcelSettingCodes.PAINTER_CLASS, Bundle.AUDIT_TRAIL_EXCEL, AuditTrailExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new AuditTrailExcelWriter(omitFields, queues);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(AuditTrailExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.AUDIT_TRAIL_EXCEL, AuditTrailExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(java.lang.Boolean.TYPE, ECRFFieldStatusQueue[].class);
			return (AuditTrailExcelWriter) ctor.newInstance(omitFields, queues);
		}
	}

	public static InventoryBookingsExcelWriter createInventoryBookingsExcelWriter(boolean omitFields) throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(InventoryBookingsExcelSettingCodes.PAINTER_CLASS, Bundle.INVENTORY_BOOKINGS_EXCEL,
				InventoryBookingsExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new InventoryBookingsExcelWriter(omitFields);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(InventoryBookingsExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.INVENTORY_BOOKINGS_EXCEL,
							InventoryBookingsExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(java.lang.Boolean.TYPE);
			return (InventoryBookingsExcelWriter) ctor.newInstance(omitFields);
		}
	}

	public static JournalExcelWriter createJournalExcelWriter(JournalModule module, boolean omitFields) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(JournalExcelSettingCodes.PAINTER_CLASS, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new JournalExcelWriter(module, omitFields);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(JournalExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.JOURNAL_EXCEL, JournalExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(JournalModule.class, java.lang.Boolean.TYPE);
			return (JournalExcelWriter) ctor.newInstance(module, omitFields);
		}
	}

	public static ProbandListExcelWriter createProbandListExcelWriter(ProbandListStatusLogLevel logLevel, boolean omitFields) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(ProbandListExcelSettingCodes.PAINTER_CLASS, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new ProbandListExcelWriter(logLevel, omitFields);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(ProbandListExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(ProbandListStatusLogLevel.class, java.lang.Boolean.TYPE);
			return (ProbandListExcelWriter) ctor.newInstance(logLevel, omitFields);
		}
	}

	public static ReimbursementsExcelWriter createReimbursementsExcelWriter(boolean omitFields, PaymentMethod method) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(ReimbursementsExcelSettingCodes.PAINTER_CLASS, Bundle.REIMBURSEMENTS_EXCEL, ReimbursementsExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new ReimbursementsExcelWriter(omitFields, method);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(ReimbursementsExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.REIMBURSEMENTS_EXCEL,
							ReimbursementsExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(java.lang.Boolean.TYPE, PaymentMethod.class);
			return (ReimbursementsExcelWriter) ctor.newInstance(omitFields, method);
		}
	}

	public static SearchResultExcelWriter createSearchResultExcelWriter(DBModule module, boolean omitFields) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(SearchResultExcelSettingCodes.PAINTER_CLASS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new SearchResultExcelWriter(module, omitFields);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(SearchResultExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(DBModule.class, java.lang.Boolean.TYPE);
			return (SearchResultExcelWriter) ctor.newInstance(module, omitFields);
		}
	}

	public static TeamMembersExcelWriter createTeamMembersExcelWriter(boolean omitFields) throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(TeamMembersExcelSettingCodes.PAINTER_CLASS, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new TeamMembersExcelWriter(omitFields);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(TeamMembersExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(java.lang.Boolean.TYPE);
			return (TeamMembersExcelWriter) ctor.newInstance(omitFields);
		}
	}

	public static VisitScheduleExcelWriter createVisitScheduleExcelWriter(boolean omitFields, Styles style) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String className = Settings.getString(VisitScheduleExcelSettingCodes.PAINTER_CLASS, Bundle.VISIT_SCHEDULE_EXCEL, VisitScheduleExcelDefaultSettings.PAINTER_CLASS);
		if (CommonUtil.isEmptyString(className)) {
			return new VisitScheduleExcelWriter(omitFields, style);
		} else {
			Constructor<?> ctor = Compile.loadClass(className,
					Settings.getStringList(VisitScheduleExcelSettingCodes.PAINTER_SOURCE_FILES, Bundle.VISIT_SCHEDULE_EXCEL,
							VisitScheduleExcelDefaultSettings.PAINTER_SOURCE_FILES))
					.getDeclaredConstructor(java.lang.Boolean.TYPE, Styles.class);
			return (VisitScheduleExcelWriter) ctor.newInstance(omitFields, style);
		}
	}

	private ExcelWriterFactory() {
	}
}
