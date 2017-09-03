package org.phoenixctms.ctsms.excel;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;

public abstract class WorkbookWriter implements ExcelWriter, ExcelOutput {

	protected final static HashMap<String, Integer> getColumnIndexMap(ArrayList<String> columns) {
		if (columns != null) {
			HashMap<String, Integer> map = new HashMap<String, Integer>(columns.size());
			Iterator<String> it = columns.iterator();
			int i = 0;
			while (it.hasNext()) {
				map.put(it.next(), i);
				i++;
			}
			return map;
		}
		return null;
	}

	protected Date now;
	protected ByteArrayOutputStream buffer;
	protected WritableWorkbook workbook;
	private ArrayList<SpreadSheetWriter> spreadSheetWriters;

	public WorkbookWriter() {
		now = new Date();
		buffer = new ByteArrayOutputStream();
		spreadSheetWriters = null;
	}

	protected abstract void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception;

	protected abstract void applyWorkbookSettings(WorkbookSettings setting);

	@Override
	public OutputStream getOutputStream() {
		return buffer;
	}

	@Override
	public WorkbookSettings getSettings() {
		WorkbookSettings settings = new WorkbookSettings();
		applyWorkbookSettings(settings);
		settings.setLocale(L10nUtil.getLocale(Locales.USER));
		return settings;
	}

	public ArrayList<SpreadSheetWriter> getSpreadSheetWriters() {
		if (spreadSheetWriters == null) {
			spreadSheetWriters = new ArrayList<SpreadSheetWriter>();
		}
		return spreadSheetWriters;
	}

	@Override
	public void init() throws Exception {
		now.setTime(System.currentTimeMillis());
		buffer.reset();
		Iterator<SpreadSheetWriter> spreadSheetWriterIt = getSpreadSheetWriters().iterator();
		while (spreadSheetWriterIt.hasNext()) {
			spreadSheetWriterIt.next().init();
		}
		updateExcelVO();
	}

	public abstract void setSpreadSheetName(String spreadSheetName);

	public void setSpreadSheetWriters(
			ArrayList<SpreadSheetWriter> spreadSheetWriters) {
		this.spreadSheetWriters = spreadSheetWriters;
	}

	protected abstract void updateExcelVO();

	@Override
	public void writeSpreadSheets(WritableWorkbook workbook) throws Exception { // WritableWorkbook workbook) throws Exception {
		Iterator<SpreadSheetWriter> spreadSheetWriterIt = getSpreadSheetWriters().iterator();
		int sheetIndex = 0;
		while (spreadSheetWriterIt.hasNext()) {
			applySpreadsheetSettings(spreadSheetWriterIt.next().writeSpreadSheet(workbook), sheetIndex); // workbook);
			sheetIndex++;
		}
	}
}
