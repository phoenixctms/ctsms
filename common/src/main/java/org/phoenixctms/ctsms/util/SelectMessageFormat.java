package org.phoenixctms.ctsms.util;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helpers for ICU-style {@code {0,select,...}} patterns used with Java {@link MessageFormat}.
 */
public final class SelectMessageFormat {

	private static final String ARG0_COMMA_PREFIX = "{0,";
	private static final String SELECT_STYLE = "select";
	private static final String SELECT_OTHER = "other";
	private static final String NESTED_ARG0 = "{0}";

	private SelectMessageFormat() {
	}

	/**
	 * Parses and validates a {@code {0,select,...}} element (if present).
	 * Ensures case bodies are well-formed and include {@code other}.
	 * Non-select argument styles are ignored.
	 */
	public static void validate(String format) {
		int start = indexOfArg0Select(format);
		if (start < 0) {
			return;
		}
		int end = findMatchingBrace(format, start);
		if (end < 0) {
			throw new IllegalArgumentException();
		}
		String body = extractSelectBody(format.substring(start, end + 1));
		Map<String, String> cases = parseSelectCases(body);
		if (!cases.containsKey(SELECT_OTHER)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Resolves ICU-style {@code {0,select,...}} on arg 0 for Java {@link MessageFormat}:
	 * maps {@code argument} via select cases and replaces the complex element with {@code {0}}.
	 * Non-select patterns are left unchanged.
	 */
	public static SelectMessageFormatResolved resolve(String format, String argument) {
		validate(format);
		int start = indexOfArg0Select(format);
		if (start < 0) {
			return new SelectMessageFormatResolved(format, argument);
		}
		int end = findMatchingBrace(format, start);
		String body = extractSelectBody(format.substring(start, end + 1));
		String resolvedArgument = applySelect(argument, body);
		String sanitized = format.substring(0, start) + NESTED_ARG0 + format.substring(end + 1);
		return new SelectMessageFormatResolved(sanitized, resolvedArgument);
	}

	/**
	 * Validates then replaces a {@code {0,select,...}} element with plain {@code {0}}
	 * so Java {@link MessageFormat} can parse the pattern.
	 * Non-select patterns are left unchanged.
	 */
	public static String sanitize(String format) {
		validate(format);
		int start = indexOfArg0Select(format);
		if (start < 0) {
			return format;
		}
		int end = findMatchingBrace(format, start);
		return format.substring(0, start) + NESTED_ARG0 + format.substring(end + 1);
	}

	/**
	 * Index of a {@code {0,select,...}} element, allowing whitespace around {@code select}.
	 * Returns -1 for null, absent select, or other arg-0 styles ({@code number}, etc.).
	 */
	private static int indexOfArg0Select(String format) {
		if (format == null) {
			return -1;
		}
		int fromIndex = 0;
		while (fromIndex < format.length()) {
			int start = format.indexOf(ARG0_COMMA_PREFIX, fromIndex);
			if (start < 0) {
				return -1;
			}
			int i = start + ARG0_COMMA_PREFIX.length();
			while (i < format.length() && Character.isWhitespace(format.charAt(i))) {
				i++;
			}
			if (i + SELECT_STYLE.length() <= format.length()
					&& format.regionMatches(i, SELECT_STYLE, 0, SELECT_STYLE.length())) {
				i += SELECT_STYLE.length();
				while (i < format.length() && Character.isWhitespace(format.charAt(i))) {
					i++;
				}
				if (i < format.length() && format.charAt(i) == ',') {
					return start;
				}
			}
			fromIndex = start + 1;
		}
		return -1;
	}

	private static String extractSelectBody(String element) {
		String afterArg = element.substring(ARG0_COMMA_PREFIX.length(), element.length() - 1);
		int comma = afterArg.indexOf(',');
		if (comma < 0) {
			throw new IllegalArgumentException();
		}
		return afterArg.substring(comma + 1);
	}

	private static int findMatchingBrace(String s, int openIndex) {
		int depth = 0;
		for (int i = openIndex; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '{') {
				depth++;
			} else if (c == '}') {
				depth--;
				if (depth == 0) {
					return i;
				}
			}
		}
		return -1;
	}

	private static Map<String, String> parseSelectCases(String selectBody) {
		Map<String, String> cases = new LinkedHashMap<String, String>();
		int i = 0;
		int n = selectBody.length();
		while (i < n) {
			while (i < n && Character.isWhitespace(selectBody.charAt(i))) {
				i++;
			}
			if (i >= n) {
				break;
			}
			int keyStart = i;
			while (i < n && selectBody.charAt(i) != '{') {
				i++;
			}
			if (i >= n) {
				throw new IllegalArgumentException();
			}
			String key = selectBody.substring(keyStart, i).trim();
			if (key.length() == 0) {
				throw new IllegalArgumentException();
			}
			int msgEnd = findMatchingBrace(selectBody, i);
			if (msgEnd < 0) {
				throw new IllegalArgumentException();
			}
			cases.put(key, selectBody.substring(i + 1, msgEnd));
			i = msgEnd + 1;
		}
		return cases;
	}

	private static String applySelect(String argument, String selectBody) {
		Map<String, String> cases = parseSelectCases(selectBody);
		String message = cases.get(argument);
		if (message == null) {
			message = cases.get(SELECT_OTHER);
		}
		if (message == null) {
			throw new IllegalArgumentException();
		}
		if (NESTED_ARG0.equals(message)) {
			return argument;
		}
		return message;
	}
}
