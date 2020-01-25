package org.phoenixctms.ctsms.executable.csv;

import java.util.HashSet;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JobOutput;

public abstract class LineProcessor {

	private final static String DEFAULT_COMMENT_CHAR = "#";
	private final static String DEFAULT_VALUE_ENCLOSING_CHAR = "";
	private final static String DEFAULT_LINE_TERMINATOR_PATTERN = "\r\n|[\r\n\u2028\u2029\u0085]";
	private final static String DEFAULT_FIELD_SEPARATOR_PATTERN = ";|\\t";
	private final static boolean DEFAULT_TRIM_VALUES = true;
	private final static boolean DEFAULT_FILTER_DUPES = true;
	private Pattern fieldSeparatorRegexp;
	private Pattern lineTerminatorRegexp;
	private String commentChar;
	private String valueEnclosingChar;
	private boolean useComments;
	private boolean trimValues;
	private boolean removeValueEnclosing;
	protected boolean filterDupes;
	protected boolean multipleInsert;
	protected HashSet<Integer> dupeCheck;
	protected JobOutput jobOutput;

	protected LineProcessor() {
		dupeCheck = new HashSet<Integer>();
		this.setFieldSeparatorRegexpPattern(DEFAULT_FIELD_SEPARATOR_PATTERN);
		this.setLineTerminatorRegexpPattern(DEFAULT_LINE_TERMINATOR_PATTERN);
		this.setCommentChar(DEFAULT_COMMENT_CHAR);
		this.setValueEnclosingChar(DEFAULT_VALUE_ENCLOSING_CHAR);
		this.setTrimValues(DEFAULT_TRIM_VALUES);
		this.setFilterDupes(DEFAULT_FILTER_DUPES);
		multipleInsert = false;
	}

	public String getCommentChar() {
		return commentChar;
	}

	public String getFieldSeparatorRegexpPattern() {
		return fieldSeparatorRegexp.pattern();
	}

	public boolean getFilterDupes() {
		return filterDupes;
	}

	public String getLineTerminatorRegexpPattern() {
		return lineTerminatorRegexp.pattern();
	}

	public boolean getTrimValues() {
		return trimValues;
	}

	public String getValueEnclosingChar() {
		return valueEnclosingChar;
	}

	public void init() {
		dupeCheck.clear();
	}

	protected abstract int lineHashCode(String[] values);

	protected abstract void postProcess() throws Exception;

	public final boolean processHeaderLine(String line) throws Exception {
		if (line != null) {
			String preparedLine = trimValues ? line.trim() : line;
			if (preparedLine.length() > 0) {
				String[] values;
				if (fieldSeparatorRegexp != null) {
					values = fieldSeparatorRegexp.split(preparedLine, -1);
					if (removeValueEnclosing) {
						for (int i = 0; i < values.length; i++) {
							if (values[i].startsWith(valueEnclosingChar) && values[i].endsWith(valueEnclosingChar)) {
								values[i] = values[i].substring(valueEnclosingChar.length(), values[i].length() - valueEnclosingChar.length());
							}
						}
					}
					if (trimValues) {
						for (int i = 0; i < values.length; i++) {
							values[i] = values[i].trim();
						}
					}
				} else {
					values = new String[1];
					values[0] = line;
				}
				return processHeaderLine(values);
			}
		}
		return false;
	}

	public final int processLine(String line, long lineNumber) throws Exception {
		if (line != null) {
			String preparedLine;
			if (useComments) {
				int commentPos = line.indexOf(commentChar);
				if (commentPos >= 0) {
					preparedLine = trimValues ? line.substring(0, commentPos).trim() : line.substring(0, commentPos);
				} else {
					preparedLine = trimValues ? line.trim() : line;
				}
			} else {
				preparedLine = trimValues ? line.trim() : line;
			}
			if (preparedLine.length() > 0) {
				String[] values;
				if (fieldSeparatorRegexp != null) {
					values = fieldSeparatorRegexp.split(preparedLine, -1);
					if (removeValueEnclosing) {
						for (int i = 0; i < values.length; i++) {
							if (values[i].startsWith(valueEnclosingChar) && values[i].endsWith(valueEnclosingChar)) {
								values[i] = values[i].substring(valueEnclosingChar.length(), values[i].length() - valueEnclosingChar.length());
							}
						}
					}
					if (trimValues) {
						for (int i = 0; i < values.length; i++) {
							values[i] = values[i].trim();
						}
					}
				} else {
					values = new String[1];
					values[0] = line;
				}
				if (testNotNullRowFields(values, lineNumber)) {
					if (multipleInsert || (!filterDupes || dupeCheck.add(lineHashCode(values)))) {
						return processLine(values, lineNumber);
					} else {
						jobOutput.println("line " + lineNumber + ": dupe ignored");
						return 0;
					}
				}
			}
		}
		return 0;
	}

	protected abstract int processLine(String[] values, long lineNumber) throws Exception;

	protected boolean processHeaderLine(String[] values) throws Exception {
		return false;
	}

	public void setCommentChar(String commentChar) {
		this.commentChar = commentChar;
		useComments = !CommonUtil.isEmptyString(commentChar);
	}

	public void setFieldSeparatorRegexpPattern(String fieldSeparatorRegexpPattern) {
		this.fieldSeparatorRegexp = Pattern.compile(fieldSeparatorRegexpPattern);
	}

	public void setFilterDupes(boolean filterDupes) {
		this.filterDupes = filterDupes;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	public void setLineTerminatorRegexpPattern(String lineTerminatorRegexpPattern) {
		this.lineTerminatorRegexp = Pattern.compile(lineTerminatorRegexpPattern);
	}

	public void setTrimValues(boolean trimValues) {
		this.trimValues = trimValues;
	}

	public void setValueEnclosingChar(String valueEnclosingChar) {
		this.valueEnclosingChar = valueEnclosingChar;
		if (valueEnclosingChar != null && valueEnclosingChar.length() > 0) {
			removeValueEnclosing = true;
		} else {
			removeValueEnclosing = false;
		}
	}

	protected abstract boolean testNotNullRowFields(String[] values, long lineNumber);
}
