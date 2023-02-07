package org.phoenixctms.ctsms.fileprocessors.xls;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.fileprocessors.ProcessorJobOutput;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public abstract class XlsImporterBase {

	protected ProcessorJobOutput jobOutput;

	protected abstract InputStream getInputStream(String fileName, AuthenticationVO auth) throws AuthenticationException,
			AuthorisationException, ServiceException, FileNotFoundException;

	protected long readRows(XlsImporterContext context, RowProcessor processor) throws Throwable {
		processor.init();
		long rowCount = 0l;
		long lineNumber = 1l;
		Workbook workbook = null;
		try {
			InputStream inputStream = getInputStream(context.getFileName(), context.getAuth());
			WorkbookSettings workbookSettings = processor.getWorkbookSettings();
			if (workbookSettings != null) {
				workbook = Workbook.getWorkbook(inputStream, workbookSettings);
			} else {
				workbook = Workbook.getWorkbook(inputStream);
			}
			Sheet sheet;
			String sheetName = processor.getSheetName();
			if (sheetName != null) {
				sheet = workbook.getSheet(sheetName);
			} else {
				try {
					sheet = workbook.getSheet(processor.getSheetNum());
				} catch (IndexOutOfBoundsException e) {
					sheet = null;
				}
			}
			if (sheet != null) {
				sheetName = sheet.getName();
				if (!CommonUtil.isEmptyString(sheetName)) {
					jobOutput.println("processing sheet '" + sheetName + "'");
				}
				int sheetRowCount = sheet.getRows();
				if (sheetRowCount > 0) {
					Cell[] row = sheet.getRow(0);
					if (!processor.processHeaderRow(row)) {
						rowCount += processor.processRow(row, lineNumber);
					}
					lineNumber++;
					for (int i = 1; i < sheetRowCount; i++) {
						row = sheet.getRow(i);
						rowCount += processor.processRow(row, lineNumber);
						lineNumber++;
					}
				}
			} else {
				if (context.isMandatory(processor)) {
					throw new IllegalArgumentException("spreadsheet not found");
				} else {
					jobOutput.println("spreadsheet not found, skipping");
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("line " + lineNumber + ": error reading line", e);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		processor.postProcess();
		jobOutput.println(rowCount + " rows processed");
		return rowCount;
	}

	public void setJobOutput(ProcessorJobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
