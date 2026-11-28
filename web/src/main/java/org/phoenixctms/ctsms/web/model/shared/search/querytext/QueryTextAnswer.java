package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.util.CommonUtil;

/**
 * Splits the model's final answer into the query body and an optional Note / Hinweis line.
 */
public final class QueryTextAnswer {

	private static final String[] NOTE_MARKERS = { "Note:", "Hinweis:" };
	private static final String QUERY_SYMBOLS = "∧∨∩∪∖";
	private static final Pattern EMPTY_NOTE = Pattern.compile("(?:none|nothing|n/?a|keine[rs]?|nichts|-+)[ .!]*", Pattern.CASE_INSENSITIVE);
	private static final Pattern QUERY_COMPARISON = Pattern.compile("\\S\\s+(=|<>|!=|<=|>=|<|>)\\s+\\S");

	// a line naming the query or describing it, also as a comment line of either format, e.g. # Title: Type 1 diabetics on a pump
	private static final Pattern TITLE_LINE = Pattern.compile("^[ \\t]*(?:#|//)?[ \\t]*(?:Title|Titel)[ \\t]*:[ \\t]*(.*?)[ \\t]*$",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	private static final Pattern DESCRIPTION_LINE = Pattern.compile("^[ \\t]*(?:#|//)?[ \\t]*(?:Description|Beschreibung)[ \\t]*:[ \\t]*(.*?)[ \\t]*$",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	private final String query;
	private final String note;
	private String title;
	private String description;

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

	/**
	 * The short name the model gave the query, if any.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The sentence the model wrote on what the query finds, if any.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The first title line of a text, such as a query text with the # Title: comment line on top. Null if there is none.
	 */
	public static String findTitle(String text) {
		return findLine(TITLE_LINE, text);
	}

	/**
	 * The first description line of a text. Null if there is none.
	 */
	public static String findDescription(String text) {
		return findLine(DESCRIPTION_LINE, text);
	}

	private static String findLine(Pattern pattern, String text) {
		if (text == null) {
			return null;
		}
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			if (!CommonUtil.isEmptyString(matcher.group(1))) {
				return matcher.group(1);
			}
		}
		return null;
	}

	public static QueryTextAnswer parse(String answer) {
		// the title and description lines go anywhere in the answer, also after a code fence, and must not end up in the query or the note
		String visible = afterThinking(answer);
		String title = findTitle(visible);
		String description = findDescription(visible);
		String text = stripFence(answer);
		if (CommonUtil.isEmptyString(text)) {
			QueryTextAnswer empty = new QueryTextAnswer("", null);
			empty.title = title;
			empty.description = description;
			return empty;
		}
		text = DESCRIPTION_LINE.matcher(TITLE_LINE.matcher(text).replaceAll("")).replaceAll("").trim();
		QueryTextAnswer result = parseQueryAndNote(text);
		result.title = title;
		result.description = description;
		return result;
	}

	private static QueryTextAnswer parseQueryAndNote(String text) {
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
		// a note that says there is nothing to note, such as Note: None., is no note
		return new QueryTextAnswer(query, CommonUtil.isEmptyString(note) || EMPTY_NOTE.matcher(note).matches() ? null : note);
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

	// reasoning models may put their thinking before the answer
	private static String afterThinking(String text) {
		if (text == null) {
			return "";
		}
		String trimmed = text.trim();
		int think = trimmed.lastIndexOf("</think>");
		if (think >= 0) {
			trimmed = trimmed.substring(think + "</think>".length()).trim();
		}
		return trimmed;
	}

	private static String stripFence(String text) {
		String trimmed = afterThinking(text);
		// take the last fenced block, models tend to wrap the query in prose
		int end = trimmed.lastIndexOf("```");
		int start = end > 0 ? trimmed.lastIndexOf("```", end - 1) : -1;
		if (start >= 0) {
			String block = trimmed.substring(start + 3, end);
			int newline = block.indexOf('\n');
			if (newline >= 0) {
				block = block.substring(newline + 1);
			}
			// the title and description lines are taken from the whole answer, so a Note after them still counts
			String after = DESCRIPTION_LINE.matcher(TITLE_LINE.matcher(trimmed.substring(end + 3)).replaceAll("")).replaceAll("").trim();
			trimmed = block.trim();
			// keep a trailing Note / Hinweis that the model put after the fence, but no other prose, which would end up in the query
			if (!CommonUtil.isEmptyString(after) && startsWithNoteMarker(after)) {
				trimmed = trimmed + "\n" + after;
			}
		}
		return trimmed;
	}
}
