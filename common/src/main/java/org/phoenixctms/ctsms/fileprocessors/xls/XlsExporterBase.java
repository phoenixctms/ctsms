package org.phoenixctms.ctsms.fileprocessors.xls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.phoenixctms.ctsms.fileprocessors.ProcessorJobOutput;
import org.phoenixctms.ctsms.util.CommonUtil;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;

public abstract class XlsExporterBase {

	protected ProcessorJobOutput jobOutput;

	private static final String DEFAULT_XLS_FILE_NAME = "export." + XlsArchive.XLS_FILENAME_EXTENSION;

	private static void writeFile(String fileName, byte[] data) throws IOException {
		FileOutputStream stream = new FileOutputStream(fileName);
		try {
			stream.write(data);
			stream.flush();
		} finally {
			stream.close();
		}
	}

	protected void addEmailXlsAttachment(byte[] data) {
	}

	protected void addEmailZipAttachment(byte[] data) {
	}

	/**
	 * @return the name of the spreadsheet within a zip archive exported to the job
	 */
	protected String getJobXlsFileName() {
		return DEFAULT_XLS_FILE_NAME;
	}

	protected long printRows(XlsExporterContext context, String encoding, RowWriter writer) throws Throwable {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			WritableWorkbook workbook;
			WorkbookSettings workbookSettings = writer.getWorkbookSettings(encoding);
			if (workbookSettings != null) {
				workbook = Workbook.createWorkbook(buffer, workbookSettings);
			} else {
				workbook = Workbook.createWorkbook(buffer);
			}
			context.setWorkbook(workbook);
			writer.init();
			writer.printHeaderRow();
			writer.printRows();
			printAdditionalSheets(context);
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			throw new IllegalArgumentException("row " + (writer.getLineCount() + 1) + ": error writing row", e);
		}
		byte[] data = buffer.toByteArray();
		String fileName = context.getFileName();
		if (context.getFiles().size() > 0) {
			// bundle the files the rows reference (e.g. sketch background images) with the spreadsheet:
			String xlsFileName = CommonUtil.isEmptyString(fileName) ? getJobXlsFileName() : XlsArchive.getXlsFileName(new File(fileName).getName());
			data = XlsArchive.write(xlsFileName, data, context.getFiles());
			jobOutput.println(context.getFiles().size() + " files bundled with " + xlsFileName + " in a zip archive");
			if (!CommonUtil.isEmptyString(fileName)) {
				fileName = XlsArchive.getZipFileName(fileName);
				jobOutput.println("writing to file " + fileName);
				writeFile(fileName, data);
			} else {
				addEmailZipAttachment(data);
			}
		} else if (!CommonUtil.isEmptyString(fileName)) {
			jobOutput.println("writing to file " + fileName);
			writeFile(fileName, data);
		} else {
			addEmailXlsAttachment(data);
		}
		jobOutput.println(writer.getLineCount() + " rows exported");
		return writer.getLineCount();
	}

	/**
	 * Sheets written after the rows of the main writer, before the workbook is closed.
	 */
	protected void printAdditionalSheets(XlsExporterContext context) throws Throwable {
	}

	public void setJobOutput(ProcessorJobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
