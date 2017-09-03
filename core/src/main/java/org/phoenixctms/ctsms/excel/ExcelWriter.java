package org.phoenixctms.ctsms.excel;

import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;
import org.phoenixctms.ctsms.enumeration.Color;

public interface ExcelWriter {

	public String getColumnTitle(String l10nKey);

	public WorkbookSettings getSettings();

	public String getTemplateFileName() throws Exception;

	public void init() throws Exception;

	public Color voToColor(Object vo);

	public void writeSpreadSheets(WritableWorkbook workbook) throws Exception; // WritableWorkbook workbook) throws Exception;
}
