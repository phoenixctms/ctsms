package org.phoenixctms.ctsms.fileprocessors.xls;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.phoenixctms.ctsms.fileprocessors.ProcessorJobOutput;
import org.phoenixctms.ctsms.util.CommonUtil;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;

public abstract class XlsExporterBase {

	protected ProcessorJobOutput jobOutput;

	protected void addEmailXlsAttachment(byte[] data) {
	}

	protected long printRows(XlsExporterContext context, RowWriter writer) throws Throwable {
		ByteArrayOutputStream buffer = null;
		try {
			WritableWorkbook workbook;
			WorkbookSettings workbookSettings = writer.getWorkbookSettings();
			if (!CommonUtil.isEmptyString(context.getFileName())) {
				jobOutput.println("writing to file " + context.getFileName());
				if (workbookSettings != null) {
					workbook = Workbook.createWorkbook(new File(context.getFileName()), workbookSettings);
				} else {
					workbook = Workbook.createWorkbook(new File(context.getFileName()));
				}
			} else {
				buffer = new ByteArrayOutputStream();
				if (workbookSettings != null) {
					workbook = Workbook.createWorkbook(buffer, workbookSettings);
				} else {
					workbook = Workbook.createWorkbook(buffer);
				}
			}
			context.setWorkbook(workbook);
			writer.init();
			writer.printHeaderRow();
			writer.printRows();
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			throw new IllegalArgumentException("row " + (writer.getLineCount() + 1) + ": error writing row", e);
		}
		if (buffer != null) {
			addEmailXlsAttachment(buffer.toByteArray());
		}
		jobOutput.println(writer.getLineCount() + " rows exported");
		return writer.getLineCount();
	}

	public void setJobOutput(ProcessorJobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
