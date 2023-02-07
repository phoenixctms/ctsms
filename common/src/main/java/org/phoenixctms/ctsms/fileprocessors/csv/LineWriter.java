package org.phoenixctms.ctsms.fileprocessors.csv;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.phoenixctms.ctsms.fileprocessors.ProcessorJobOutput;

public abstract class LineWriter {

	private final static String DEFAULT_COMMENT_CHAR = "#";
	private final static String DEFAULT_LINE_TERMINATOR = "\n";
	private final static String DEFAULT_FIELD_SEPARATOR = ";";
	protected ProcessorJobOutput jobOutput;
	private String commentChar;
	private String lineTerminator;
	private String fieldSeparator;
	protected long lineNumber;
	private boolean firstLine;
	private PrintWriter writer;

	protected LineWriter() {
		setCommentChar(DEFAULT_COMMENT_CHAR);
		setFieldSeparator(DEFAULT_FIELD_SEPARATOR);
		setLineTerminator(DEFAULT_LINE_TERMINATOR);
	}

	public String getCommentChar() {
		return commentChar;
	}

	public String getFieldSeparator() {
		return fieldSeparator;
	}

	public long getLineCount() {
		return lineNumber;
	}

	public String getLineTerminator() {
		return lineTerminator;
	}

	public void init(PrintWriter writer) {
		firstLine = true;
		lineNumber = 0;
		this.writer = writer;
	}

	protected final void printBlankLine() {
		if (!firstLine) {
			writer.write(lineTerminator);
		}
		firstLine = false;
	}

	protected final void printComment(String line) {
		if (!firstLine) {
			writer.write(lineTerminator);
		}
		writer.write(commentChar);
		writer.write(line);
		firstLine = false;
	}

	protected final void printLine(Collection values) {
		if (values != null) {
			if (!firstLine) {
				writer.write(lineTerminator);
			}
			StringBuffer buffer = new StringBuffer();
			Iterator iter = values.iterator();
			while (iter.hasNext()) {
				buffer.append(iter.next());
				if (iter.hasNext()) {
					buffer.append(fieldSeparator);
				}
			}
			writer.append(buffer);
			firstLine = false;
			lineNumber++;
		}
	}

	public abstract void printLines();

	public void setCommentChar(String commentChar) {
		this.commentChar = commentChar;
	}

	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	public void setJobOutput(ProcessorJobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	public void setLineTerminator(String lineTerminator) {
		this.lineTerminator = lineTerminator;
	}
}
