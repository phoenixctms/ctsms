package org.phoenixctms.ctsms.executable.xls;

import java.util.HashSet;
import java.util.Locale;

import jxl.Cell;
import jxl.WorkbookSettings;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JobOutput;

public abstract class RowProcessor {

	private final static String DEFAULT_COMMENT_CHAR = "#";
	private final static boolean DEFAULT_TRIM_VALUES = true;
	private final static boolean DEFAULT_FILTER_DUPES = true;
	private boolean trimValues;
	private String commentChar;
	private boolean useComments;
	protected Integer acceptCommentsIndex;
	protected boolean filterDupes;
	protected boolean multipleInsert;
	protected HashSet<Integer> dupeCheck;
	protected JobOutput jobOutput;

	protected XlsImporterContext context;

	protected RowProcessor() {
		dupeCheck = new HashSet<Integer>();
		this.setTrimValues(DEFAULT_TRIM_VALUES);
		this.setFilterDupes(DEFAULT_FILTER_DUPES);
		this.setCommentChar(DEFAULT_COMMENT_CHAR);
		acceptCommentsIndex = null;
		multipleInsert = false;
		context = null;
	}

	protected String getColumnValue(String[] values, int index) {
		if (values != null && index >= 0 && index < values.length) {
			return values[index];
		} else {
			return null;
		}
	}

	public String getCommentChar() {
		return commentChar;
	}

	public XlsImporterContext getContext() {
		return context;
	}

	public boolean getFilterDupes() {
		return filterDupes;
	}

	public String getSheetName() {
		return null;
	}

	public int getSheetNum() {
		return 0;
	}

	public boolean getTrimValues() {
		return trimValues;
	}

	public WorkbookSettings getWorkbookSettings() {
		// return null;
		WorkbookSettings workbookSettings = new WorkbookSettings();
		// Locale locale = Locale.getDefault();
		workbookSettings.setLocale(Locale.getDefault());
		// wbSettings.setEncoding("UTF-8");
		return workbookSettings;
	}

	public void init() throws Throwable {
		dupeCheck.clear();
	}

	protected abstract int lineHashCode(String[] values);

	protected abstract void postProcess();

	public final int processRow(Cell[] row, long rowNumber) throws Throwable {
		if (row != null && row.length > 0) {
			boolean commented = false;
			String[] values = new String[row.length];
			for (int i = 0; i < row.length; i++) {
				String value = "";
				if (row[i] != null && !commented) {
					value = row[i].getContents();
					if (value != null) {
						if (useComments && (acceptCommentsIndex == null || acceptCommentsIndex == i)) {
							int commentPos = value.indexOf(commentChar);
							if (commentPos >= 0) {
								value = trimValues ? value.substring(0, commentPos).trim() : value.substring(0, commentPos);
								commented = true;
							} else {
								value = trimValues ? value.trim() : value;
							}
						} else {
							value = trimValues ? value.trim() : value;
						}
					}
				}
				values[i] = value;
			}
			if (testNotNullRowFields( values, rowNumber)) {
				if (multipleInsert || (!filterDupes || dupeCheck.add(lineHashCode(values)))) {
					return processRow( values, rowNumber);
				} else {
					jobOutput.println("row " + rowNumber + ": dupe ignored");
					return 0;
				}
			}
		}
		return 0;
	}

	protected abstract int processRow(String[] values, long rowNumber) throws Throwable;

	public void setCommentChar(String commentChar) {
		this.commentChar = commentChar;
		useComments = !CommonUtil.isEmptyString(commentChar);
	}

	public void setContext(XlsImporterContext context) {
		this.context = context;
	}

	public void setFilterDupes(boolean filterDupes) {
		this.filterDupes = filterDupes;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	public void setTrimValues(boolean trimValues) {
		this.trimValues = trimValues;
	}

	protected abstract boolean testNotNullRowFields(String[] values, long rowNumber);
}
