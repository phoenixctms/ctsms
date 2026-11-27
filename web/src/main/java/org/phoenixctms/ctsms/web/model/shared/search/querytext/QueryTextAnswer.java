package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.util.regex.Pattern;

import org.phoenixctms.ctsms.util.CommonUtil;

/**
 * Splits the model's final answer into the query body and an optional Note / Hinweis line.
 */
public final class QueryTextAnswer {

	private static final String[] NOTE_MARKERS = { "Note:", "Hinweis:" };
	private static final String QUERY_SYMBOLS = "∧∨∩∪∖";
	private static final Pattern QUERY_COMPARISON = Pattern.compile("\\S\\s+(=|<>|!=|<=|>=|<|>)\\s+\\S");

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
			// the note came first: the query starts at the first later line that looks like one, with or without a blank line before it;
			// the lines before continue the note, so a note-only answer, also one of several paragraphs, stays note-only
			int lineStart = rest.indexOf('\n') + 1;
			while (lineStart > 0 && lineStart < rest.length()) {
				int lineEnd = rest.indexOf('\n', lineStart);
				String line = (lineEnd < 0 ? rest.substring(lineStart) : rest.substring(lineStart, lineEnd)).trim();
				if (looksLikeQuery(line)) {
					query = rest.substring(lineStart).trim();
					rest = rest.substring(0, lineStart);
					break;
				}
				lineStart = lineEnd + 1;
			}
		}
		String note = rest.replaceAll("\\s+", " ").trim();
		return new QueryTextAnswer(query, CommonUtil.isEmptyString(note) ? null : note);
	}

	private static boolean startsWithNoteMarker(String text) {
		for (int m = 0; m < NOTE_MARKERS.length; m++) {
			if (text.regionMatches(true, 0, NOTE_MARKERS[m], 0, NOTE_MARKERS[m].length())) {
				return true;
			}
		}
		return false;
	}

	// a query line has a tie symbol, a comparison between spaces such as On proband list of trial = StudyA, a facet placeholder,
	// or starts with a parenthesis, a JSON array, a bracketed criterion or a comment
	private static boolean looksLikeQuery(String text) {
		if (CommonUtil.isEmptyString(text)) {
			return false;
		}
		char first = text.charAt(0);
		if (first == '(' || first == '[' || first == '<' || first == '{' || first == '#' || text.startsWith("//")) {
			return true;
		}
		for (int i = 0; i < QUERY_SYMBOLS.length(); i++) {
			if (text.indexOf(QUERY_SYMBOLS.charAt(i)) >= 0) {
				return true;
			}
		}
		return QUERY_COMPARISON.matcher(text).find();
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
			// keep a trailing Note / Hinweis that the model put after the fence, but no other prose, which would end up in the query
			if (!CommonUtil.isEmptyString(after) && startsWithNoteMarker(after)) {
				trimmed = trimmed + "\n" + after;
			}
		}
		return trimmed;
	}
}
