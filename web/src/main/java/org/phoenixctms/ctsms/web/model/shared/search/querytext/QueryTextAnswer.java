package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import org.phoenixctms.ctsms.util.CommonUtil;

/**
 * Splits the model's final answer into the query body and an optional Note / Hinweis line.
 */
public final class QueryTextAnswer {

	private static final String[] NOTE_MARKERS = { "Note:", "Hinweis:" };

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
			int at = indexOfMarker(text, NOTE_MARKERS[m]);
			if (at >= 0 && (noteAt < 0 || at < noteAt)) {
				noteAt = at;
				marker = NOTE_MARKERS[m];
			}
		}
		if (noteAt < 0) {
			return new QueryTextAnswer(text.trim(), null);
		}
		String query = text.substring(0, noteAt).trim();
		String note = text.substring(noteAt + marker.length()).trim().replaceAll("\\s+", " ").trim();
		return new QueryTextAnswer(query, CommonUtil.isEmptyString(note) ? null : note);
	}

	// at the start of the text, after a newline, or after other whitespace (inline after the query)
	private static int indexOfMarker(String text, String marker) {
		String lower = text.toLowerCase();
		String needle = marker.toLowerCase();
		int from = 0;
		while (from <= lower.length() - needle.length()) {
			int at = lower.indexOf(needle, from);
			if (at < 0) {
				return -1;
			}
			if (at == 0 || Character.isWhitespace(text.charAt(at - 1))) {
				return at;
			}
			from = at + 1;
		}
		return -1;
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
			trimmed = block.trim();
		}
		return trimmed;
	}
}
