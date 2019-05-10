package org.phoenixctms.ctsms.executable.xls;

import java.util.Locale;

import jxl.WorkbookSettings;

import org.phoenixctms.ctsms.util.JobOutput;

public abstract class RowWriter {

	private final static String DEFAULT_COMMENT_CHAR = "#";
	protected JobOutput jobOutput;
	private String commentChar;
	protected int lineNumber;
	// private WritableSheet spreadSheet;
	protected XlsExporterContext context;

	protected RowWriter() {
		setCommentChar(DEFAULT_COMMENT_CHAR);
		context = null;
	}

	public String getCommentChar() {
		return commentChar;
	}

	public XlsExporterContext getContext() {
		return context;
	}

	public int getLineCount() {
		return lineNumber;
	}

	public String getSheetName() {
		return null;
	}

	public int getSheetNum() {
		return 0;
	}

	public WorkbookSettings getWorkbookSettings() {
		// return null;
		WorkbookSettings workbookSettings = new WorkbookSettings();
		// Locale locale = Locale.getDefault();
		workbookSettings.setLocale(Locale.getDefault());
		// wbSettings.setEncoding("UTF-8");
		return workbookSettings;
	}

	public void init() {
		lineNumber = 0;
	}

	private final void printBlankCell(int c) throws Exception {
		context.getSpreadSheet(this).addCell(new jxl.write.Blank(c, lineNumber));
	}

	protected final void printBlankRow() throws Exception {
		printBlankCell(0);
		lineNumber++;
	}

	protected final void printComment(String line) throws Exception {
		printRow(new String[] { line }, true);
	}

	protected final void printRow(String[] values) throws Exception {
		printRow(values, false);
	}

	protected final void printRow(String[] values, boolean isComment) throws Exception {
		if (values != null) {
			if (values.length > 0) {
				for (int c = 0; c < values.length; c++) {
					StringBuilder sb = new StringBuilder();
					if (c == 0 && isComment) {
						sb.append(getCommentChar());
					} else {
						isComment = false;
					}
					//sb.append(CommonUtil.clipString(it.next(), ExcelUtil.EXCEL_MAX_CELL_TEXT_LENGTH));
					if (values[c] != null) {
						sb.append(values[c]);
					}
					if (values[c] == null && !isComment) {
						printBlankCell(c);
					} else {
						context.getSpreadSheet(this).addCell(new jxl.write.Label(c, lineNumber, sb.toString()));
					}
				}
			} else {
				printBlankCell(0);
			}
			lineNumber++;
		}
	}

	public abstract void printRows() throws Throwable;

	public void setCommentChar(String commentChar) {
		this.commentChar = commentChar;
	}

	public void setContext(XlsExporterContext context) {
		this.context = context;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
