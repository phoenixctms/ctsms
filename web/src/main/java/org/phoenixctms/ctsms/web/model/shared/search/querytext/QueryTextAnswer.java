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
		String[] lines = text.split("\r\n|\n|\r");
		int noteIndex = -1;
		String marker = null;
		for (int i = 0; i < lines.length; i++) {
			String trimmed = lines[i].trim();
			for (int m = 0; m < NOTE_MARKERS.length; m++) {
				String candidate = NOTE_MARKERS[m];
				if (trimmed.length() >= candidate.length() && trimmed.regionMatches(true, 0, candidate, 0, candidate.length())) {
					noteIndex = i;
					marker = candidate;
					break;
				}
			}
			if (noteIndex >= 0) {
				break;
			}
		}
		if (noteIndex < 0) {
			return new QueryTextAnswer(text.trim(), null);
		}
		StringBuilder query = new StringBuilder();
		for (int i = 0; i < noteIndex; i++) {
			if (query.length() > 0) {
				query.append('\n');
			}
			query.append(lines[i]);
		}
		StringBuilder note = new StringBuilder(lines[noteIndex].trim().substring(marker.length()).trim());
		for (int i = noteIndex + 1; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line.length() == 0) {
				continue;
			}
			if (note.length() > 0) {
				note.append(' ');
			}
			note.append(line);
		}
		String noteText = note.toString().trim();
		return new QueryTextAnswer(query.toString().trim(), CommonUtil.isEmptyString(noteText) ? null : noteText);
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
