package org.phoenixctms.ctsms.excel;

import org.phoenixctms.ctsms.enumeration.Color;

import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;

public interface ExcelWriter {

	public String getColumnTitle(String l10nKey);

	public WorkbookSettings getSettings();

	public String getTemplateFileName() throws Exception;

	public void init() throws Exception;

	public Color voToRowColor(Object vo);

	public Color voToCellColor(Object vo, String columnName);

	public void writeSpreadSheets(WritableWorkbook workbook) throws Exception;
}
