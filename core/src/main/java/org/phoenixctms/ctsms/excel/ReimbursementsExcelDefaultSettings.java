package org.phoenixctms.ctsms.excel;

import java.util.ArrayList;

public final class ReimbursementsExcelDefaultSettings {

	public static final int VO_GRAPH_RECURSION_DEPTH = 2;
	public static final String SPREADSHEET_NAME = null;
	public static final String TEMPLATE_FILE_NAME = null;
	public final static ArrayList<String> VO_FIELD_COLUMNS = new ArrayList<String>();
	public static final boolean AUTOSIZE = true;
	public static final boolean WRITEHEAD = true;
	public static final Integer PAGE_BREAK_AT_ROW = null;
	public static final boolean ROW_COLORS = false;
	public static final ExcelCellFormat HEAD_FORMAT = ExcelCellFormat.getDefaultHeadFormat();
	public static final ExcelCellFormat ROW_FORMAT = ExcelCellFormat.getDefaultRowFormat();
	public static final Integer SCALE_FACTOR = null;
	public static final boolean APPEND_HEADER_FOOTER = true;
	public static final boolean SHOW_ADDRESSES = false;
	public static final boolean AGGREGATE_ADDRESSES = false;
	public static final String PETTY_CASH_SPREADSHEET_NAME = null;
	public static final String PETTY_CASH_TEMPLATE_FILE_NAME = null;
	public final static ArrayList<String> PETTY_CASH_VO_FIELD_COLUMNS = new ArrayList<String>();
	public static final boolean PETTY_CASH_AUTOSIZE = true;
	public static final boolean PETTY_CASH_WRITEHEAD = true;
	public static final Integer PETTY_CASH_PAGE_BREAK_AT_ROW = null;
	public static final boolean PETTY_CASH_ROW_COLORS = false;
	public static final ExcelCellFormat PETTY_CASH_HEAD_FORMAT = ExcelCellFormat.getDefaultHeadFormat();
	public static final ExcelCellFormat PETTY_CASH_ROW_FORMAT = ExcelCellFormat.getDefaultRowFormat();
	public static final Integer PETTY_CASH_SCALE_FACTOR = null;
	public static final boolean PETTY_CASH_APPEND_HEADER_FOOTER = true;
	public static final boolean PETTY_CASH_SHOW_ADDRESSES = false;
	public static final boolean PETTY_CASH_AGGREGATE_ADDRESSES = false;
	public static final String VOUCHER_SPREADSHEET_NAME = null;
	public static final String VOUCHER_TEMPLATE_FILE_NAME = null;
	public final static ArrayList<String> VOUCHER_VO_FIELD_COLUMNS = new ArrayList<String>();
	public static final boolean VOUCHER_AUTOSIZE = true;
	public static final boolean VOUCHER_WRITEHEAD = true;
	public static final Integer VOUCHER_PAGE_BREAK_AT_ROW = null;
	public static final boolean VOUCHER_ROW_COLORS = true;
	public static final ExcelCellFormat VOUCHER_HEAD_FORMAT = ExcelCellFormat.getDefaultHeadFormat();
	public static final ExcelCellFormat VOUCHER_ROW_FORMAT = ExcelCellFormat.getDefaultRowFormat();
	public static final Integer VOUCHER_SCALE_FACTOR = null;
	public static final boolean VOUCHER_APPEND_HEADER_FOOTER = true;
	public static final boolean VOUCHER_SHOW_ADDRESSES = false;
	public static final boolean VOUCHER_AGGREGATE_ADDRESSES = false;
	public static final String WIRE_TRANSFER_SPREADSHEET_NAME = null;
	public static final String WIRE_TRANSFER_TEMPLATE_FILE_NAME = null;
	public final static ArrayList<String> WIRE_TRANSFER_VO_FIELD_COLUMNS = new ArrayList<String>();
	public static final boolean WIRE_TRANSFER_AUTOSIZE = true;
	public static final boolean WIRE_TRANSFER_WRITEHEAD = true;
	public static final Integer WIRE_TRANSFER_PAGE_BREAK_AT_ROW = null;
	public static final boolean WIRE_TRANSFER_ROW_COLORS = false;
	public static final ExcelCellFormat WIRE_TRANSFER_HEAD_FORMAT = ExcelCellFormat.getDefaultHeadFormat();
	public static final ExcelCellFormat WIRE_TRANSFER_ROW_FORMAT = ExcelCellFormat.getDefaultRowFormat();
	public static final Integer WIRE_TRANSFER_SCALE_FACTOR = null;
	public static final boolean WIRE_TRANSFER_APPEND_HEADER_FOOTER = true;
	public static final boolean WIRE_TRANSFER_SHOW_ADDRESSES = false;
	public static final boolean WIRE_TRANSFER_AGGREGATE_ADDRESSES = false;
	static {
		VO_FIELD_COLUMNS.add("trial" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("proband" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("costType");
		VO_FIELD_COLUMNS.add("amount");
		VO_FIELD_COLUMNS.add("transactionTimestamp");
		VO_FIELD_COLUMNS.add("method" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		VO_FIELD_COLUMNS.add("paid");
		WIRE_TRANSFER_VO_FIELD_COLUMNS
				.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "proband" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		WIRE_TRANSFER_VO_FIELD_COLUMNS.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "accountHolderName");
		WIRE_TRANSFER_VO_FIELD_COLUMNS.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "accountNumber");
		WIRE_TRANSFER_VO_FIELD_COLUMNS.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "bankCodeNumber");
		WIRE_TRANSFER_VO_FIELD_COLUMNS.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "iban");
		WIRE_TRANSFER_VO_FIELD_COLUMNS.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "bic");
		WIRE_TRANSFER_VO_FIELD_COLUMNS.add("bankAccount" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "bankName");
		WIRE_TRANSFER_VO_FIELD_COLUMNS.add("total");
		PETTY_CASH_VO_FIELD_COLUMNS.add("trial" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		PETTY_CASH_VO_FIELD_COLUMNS.add("proband" + ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR + "name");
		PETTY_CASH_VO_FIELD_COLUMNS.add("costType");
		PETTY_CASH_VO_FIELD_COLUMNS.add("amount");
		PETTY_CASH_VO_FIELD_COLUMNS.add("transactionTimestamp");
		PETTY_CASH_VO_FIELD_COLUMNS.add("paid");
	}

	public ReimbursementsExcelDefaultSettings() {
	}
}
