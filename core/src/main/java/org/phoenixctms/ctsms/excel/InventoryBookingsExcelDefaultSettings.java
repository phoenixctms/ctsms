package org.phoenixctms.ctsms.excel;




import java.util.ArrayList;

public final class InventoryBookingsExcelDefaultSettings {

	// public static final int GRAPH_MAX_STAFF_INSTANCES = 10;
	public static final String TEMPLATE_FILE_NAME = null;
	public static final boolean AUTOSIZE = true;
	public static final int VO_GRAPH_RECURSION_DEPTH = 2;
	public static final String SPREADSHEET_NAME = null;
	public static final Integer PAGE_BREAK_AT_ROW = null;
	public static final Integer SCALE_FACTOR = null;
	public static final ExcelCellFormat HEAD_FORMAT = ExcelCellFormat.getDefaultHeadFormat();
	public static final ExcelCellFormat ROW_FORMAT = ExcelCellFormat.getDefaultRowFormat();
	public static final boolean ROW_COLORS = true;
	public final static ArrayList<String> VO_FIELD_COLUMNS = new ArrayList<String>();
	public static final boolean WRITEHEAD = true;
	// public static final boolean SHOW_TAGS = false;
	// public static final boolean SHOW_ADDRESSES = false;
	// public static final boolean SHOW_CONTACT_DETAILS = false;
	// public static final boolean SHOW_CV_ADDRESS_BLOCK = false;
	// public static final boolean AGGREGATE_ADDRESSES = false;
	// public static final boolean AGGREGATE_CONTACT_DETAILS = false;
	public static final boolean APPEND_HEADER_FOOTER = true;
	static
	{
		VO_FIELD_COLUMNS.add("calendar");
		VO_FIELD_COLUMNS.add("proband" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "nameWithTitles");
		VO_FIELD_COLUMNS.add("trial" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("course" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("inventory" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("start");
		VO_FIELD_COLUMNS.add("stop");
		VO_FIELD_COLUMNS.add("onBehalfOf" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("comment");
	}

	private InventoryBookingsExcelDefaultSettings() {
	}
}
