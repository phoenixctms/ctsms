package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import org.phoenixctms.ctsms.util.CommonUtil;

/**
 * Splits the model's final answer into the query body and an optional Note / Hinweis line.
 */
public final class QueryTextAnswer {

	private static final String[] NOTE_MARKERS = { "Note:", "Hinweis:" };
	private static final String QUERY_SYMBOLS = "∧∨∩∪∖";

	private final String query;
	private final String note;

	private QueryTextAnswer(String query, String note) {
		this.query = query == null ? "" : query;
		this.note = note;
	}

	public String getQuery() {
		return query;
	}

	public String getNote() {
		return note;
	}

	public static QueryTextAnswer parse(String answer) {
		String text = stripFence(answer);
		if (CommonUtil.isEmptyString(text)) {
			return new QueryTextAnswer("", null);
		}
		int noteAt = -1;
		String marker = null;
		for (int m = 0; m < NOTE_MARKERS.length; m++) {
			int at = indexOfLineStartMarker(text, NOTE_MARKERS[m]);
			if (at >= 0 && (noteAt < 0 || at < noteAt)) {
				noteAt = at;
				marker = NOTE_MARKERS[m];
			}
		}
		if (noteAt < 0) {
			return new QueryTextAnswer(text.trim(), null);
		}
		String query = text.substring(0, noteAt).trim();
		String rest = text.substring(noteAt + marker.length()).trim();
		if (query.length() == 0) {
			// the note came first: a query after a blank line, or right after the note line if it looks like one;
			// otherwise the further lines continue the note, so a note-only answer stays note-only
			int blank = rest.indexOf("\n\n");
			int lineEnd = rest.indexOf('\n');
			if (blank >= 0) {
				query = rest.substring(blank).trim();
				rest = rest.substring(0, blank);
			} else if (lineEnd >= 0 && looksLikeQuery(rest.substring(lineEnd + 1).trim())) {
				query = rest.substring(lineEnd + 1).trim();
				rest = rest.substring(0, lineEnd);
			}
		}
		String note = rest.replaceAll("\\s+", " ").trim();
		return new QueryTextAnswer(query, CommonUtil.isEmptyString(note) ? null : note);
	}

	// a query has a tie symbol, or starts with a parenthesis, a JSON array or a bracketed criterion
	private static boolean looksLikeQuery(String text) {
		if (CommonUtil.isEmptyString(text)) {
			return false;
		}
		char first = text.charAt(0);
		if (first == '(' || first == '[' || first == '<') {
			return true;
		}
		for (int i = 0; i < QUERY_SYMBOLS.length(); i++) {
			if (text.indexOf(QUERY_SYMBOLS.charAt(i)) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Note / Hinweis only at the start of a line (prompt: blank line then one Note: line). Mid-line
	 * markers stay inside criterion values, e.g. {@code Text value = Patient Note: pending}.
	 */
	private static int indexOfLineStartMarker(String text, String marker) {
		String lower = text.toLowerCase();
		String needle = marker.toLowerCase();
		int from = 0;
		while (from <= lower.length() - needle.length()) {
			int at = lower.indexOf(needle, from);
			if (at < 0) {
				return -1;
			}
			if (isLineStart(text, at)) {
				return at;
			}
			from = at + 1;
		}
		return -1;
	}

	private static boolean isLineStart(String text, int at) {
		int i = at - 1;
		while (i >= 0 && (text.charAt(i) == ' ' || text.charAt(i) == '\t')) {
			i--;
		}
		return i < 0 || text.charAt(i) == '\n' || text.charAt(i) == '\r';
	}

	private static String stripFence(String text) {
		if (text == null) {
			return "";
		}
		String trimmed = text.trim();
		// reasoning models may put their thinking before the answer
		int think = trimmed.lastIndexOf("</think>");
		if (think >= 0) {
			trimmed = trimmed.substring(think + "</think>".length()).trim();
		}
		// take the last fenced block, models tend to wrap the query in prose
		int end = trimmed.lastIndexOf("```");
		int start = end > 0 ? trimmed.lastIndexOf("```", end - 1) : -1;
		if (start >= 0) {
			String block = trimmed.substring(start + 3, end);
			int newline = block.indexOf('\n');
			if (newline >= 0) {
				block = block.substring(newline + 1);
			}
			String after = trimmed.substring(end + 3).trim();
			trimmed = block.trim();
			// keep a trailing Note / Hinweis that the model put after the fence
			if (!CommonUtil.isEmptyString(after)) {
				trimmed = trimmed + "\n" + after;
			}
		}
		return trimmed;
	}
}
