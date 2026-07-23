package org.phoenixctms.ctsms.util;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helpers for ICU-style {@code {0,select,...}} patterns used with Java {@link MessageFormat}.
 */
public final class SelectMessageFormat {

	private static final String ARG0_COMPLEX_PREFIX = "{0,";
	private static final String SELECT_STYLE = "select";
	private static final String SELECT_OTHER = "other";
	private static final String NESTED_ARG0 = "{0}";

	private SelectMessageFormat() {
	}

	/**
	 * Resolves ICU-style {@code {0,select,...}} on arg 0 for Java {@link MessageFormat}:
	 * maps {@code argument} via select cases and replaces the complex element with {@code {0}}.
	 */
	public static SelectMessageFormatResolved resolve(String format, String argument) {
		int start = indexOfArg0Complex(format);
		if (start < 0) {
			return new SelectMessageFormatResolved(format, argument);
		}
		int end = findMatchingBrace(format, start);
		if (end < 0) {
			throw new IllegalArgumentException();
		}
		String element = format.substring(start, end + 1);
		String afterArg = element.substring(ARG0_COMPLEX_PREFIX.length());
		int comma = afterArg.indexOf(',');
		if (comma < 0) {
			throw new IllegalArgumentException();
		}
		String style = afterArg.substring(0, comma).trim();
		String body = afterArg.substring(comma + 1, afterArg.length() - 1);
		String resolvedArgument = argument;
		if (SELECT_STYLE.equals(style)) {
			resolvedArgument = applySelect(argument, body);
		}
		String sanitized = format.substring(0, start) + NESTED_ARG0 + format.substring(end + 1);
		return new SelectMessageFormatResolved(sanitized, resolvedArgument);
	}

	/**
	 * Replaces a complex {@code {0,...}} element with plain {@code {0}} so Java {@link MessageFormat} can parse the pattern.
	 */
	public static String sanitize(String format) {
		int start = indexOfArg0Complex(format);
		if (start < 0) {
			return format;
		}
		int end = findMatchingBrace(format, start);
		if (end < 0) {
			throw new IllegalArgumentException();
		}
		return format.substring(0, start) + NESTED_ARG0 + format.substring(end + 1);
	}

	private static int indexOfArg0Complex(String format) {
		if (format == null) {
			return -1;
		}
		return format.indexOf(ARG0_COMPLEX_PREFIX);
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

	private static String applySelect(String argument, String selectBody) {
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
