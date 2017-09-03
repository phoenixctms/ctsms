package org.phoenixctms.ctsms.excel;

import java.util.ArrayList;

public final class AuditTrailExcelDefaultSettings {

	public static final int VO_GRAPH_RECURSION_DEPTH = 2;
	public static final String TEMPLATE_FILE_NAME = null;
	public static final boolean AUDIT_TRAIL_AUTOSIZE = true;
	public static final Integer AUDIT_TRAIL_PAGE_BREAK_AT_ROW = null;
	public static final Integer AUDIT_TRAIL_SCALE_FACTOR = null;
	public static final ExcelCellFormat AUDIT_TRAIL_HEAD_FORMAT = ExcelCellFormat.getDefaultHeadFormat();
	public static final ExcelCellFormat AUDIT_TRAIL_ROW_FORMAT = ExcelCellFormat.getDefaultRowFormat();
	public static final boolean AUDIT_TRAIL_ROW_COLORS = false;
	public final static ArrayList<String> AUDIT_TRAIL_VO_FIELD_COLUMNS = new ArrayList<String>();
	public static final boolean AUDIT_TRAIL_WRITEHEAD = true;
	public static final boolean AUDIT_TRAIL_APPEND_HEADER_FOOTER = false;

	public static final boolean ECRF_FIELD_STATUS_AUTOSIZE = true;
	public static final Integer ECRF_FIELD_STATUS_PAGE_BREAK_AT_ROW = null;
	public static final Integer ECRF_FIELD_STATUS_SCALE_FACTOR = null;
	public static final ExcelCellFormat ECRF_FIELD_STATUS_HEAD_FORMAT = ExcelCellFormat.getDefaultHeadFormat();
	public static final ExcelCellFormat ECRF_FIELD_STATUS_ROW_FORMAT = ExcelCellFormat.getDefaultRowFormat();
	public static final boolean ECRF_FIELD_STATUS_ROW_COLORS = false;
	public final static ArrayList<String> ECRF_FIELD_STATUS_VO_FIELD_COLUMNS = new ArrayList<String>();
	public static final boolean ECRF_FIELD_STATUS_WRITEHEAD = true;
	public static final boolean ECRF_FIELD_STATUS_APPEND_HEADER_FOOTER = false;
	public static final ArrayList<String> ECRF_FIELD_STATUS_QUEUES = new ArrayList<String>();
	static
	{
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("listEntry" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "proband" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "id");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "ecrf" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "section");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("index");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "position");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "field" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("version");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("reasonForChange");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("changeComment");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("modifiedUser" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		AUDIT_TRAIL_VO_FIELD_COLUMNS.add("modifiedTimestamp");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS
		.add("listEntry" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "proband" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "id");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "ecrf" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "section");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("index");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "position");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS
		.add("ecrfField" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "field" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("status" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("status" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "resolved");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("comment");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("modifiedUser" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		ECRF_FIELD_STATUS_VO_FIELD_COLUMNS.add("modifiedTimestamp");
		ECRF_FIELD_STATUS_QUEUES.add("VALIDATION");
		ECRF_FIELD_STATUS_QUEUES.add("QUERY");
		ECRF_FIELD_STATUS_QUEUES.add("ANNOTATION");
	}

	private AuditTrailExcelDefaultSettings() {
	}
}
