package org.phoenixctms.ctsms.excel;

import java.io.File;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class ExcelExporter {

	private ExcelWriter writer;
	private ExcelOutput output;

	public ExcelExporter() {
	}

	public ExcelExporter(ExcelWriter writer, ExcelOutput output) {
		this.writer = writer;
		this.output = output;
	}

	public ExcelOutput getOutput() {
		return output;
	}

	public ExcelWriter getWriter() {
		return writer;
	}

	public void setOutput(ExcelOutput output) {
		this.output = output;
	}

	public void setWriter(ExcelWriter writer) {
		this.writer = writer;
	}

	public boolean write() throws Exception {
		if (writer != null) {
			writer.init();
		}
		if (output != null && writer != null) {
			Workbook templateWorkbook = null;
			WritableWorkbook workbook = null;
			boolean closed = false;
			try {
				String templateFileName = writer.getTemplateFileName();
				if (templateFileName != null && templateFileName.length() > 0) {
					templateWorkbook = Workbook.getWorkbook(new File(templateFileName));
					workbook = Workbook.createWorkbook(output.getOutputStream(), templateWorkbook, writer.getSettings());
				} else {
					workbook = Workbook.createWorkbook(output.getOutputStream(), writer.getSettings());
				}
				writer.writeSpreadSheets(workbook);
				workbook.write();
				workbook.close(); // before save!
				closed = true;
				return output.save();
			} finally {
				if (workbook != null && !closed) {
					workbook.close();
				}
				if (templateWorkbook != null) {
					templateWorkbook.close();
				}
			}
		} else {
			return false;
		}
	}
}
