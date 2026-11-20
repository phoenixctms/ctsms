package org.phoenixctms.ctsms.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
 * Parsing, per-key diffing and merging of .properties texts, for the editable localization texts.
 */
public final class InputFieldL10nTextUtil {

	/**
	 * A key change: old value null = added, new value null = removed.
	 */
	public static final class Change {

		private final String oldValue;
		private final String newValue;

		public Change(String oldValue, String newValue) {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public String getNewValue() {
			return newValue;
		}

		public String getOldValue() {
			return oldValue;
		}

		boolean sameResult(Change other) {
			return other != null && (newValue == null ? other.newValue == null : newValue.equals(other.newValue));
		}
	}

	/**
	 * Properties that keep the order of the keys as they appear in the text.
	 */
	private static final class OrderedProperties extends Properties {

		private static final long serialVersionUID = 1L;
		private final LinkedHashMap<String, String> ordered = new LinkedHashMap<String, String>();

		@Override
		public synchronized Object put(Object key, Object value) {
			ordered.remove(key); // the last definition of a key wins, as for Properties
			ordered.put((String) key, (String) value);
			return super.put(key, value);
		}
	}

	private final static String LINE_SEPARATOR = "\n";

	/**
	 * @return true if the key is defined differently (or only) in one of both maps
	 */
	private static boolean differs(String oldValue, String newValue) {
		return oldValue == null ? newValue != null : !oldValue.equals(newValue);
	}

	public static LinkedHashMap<String, Change> diff(Map<String, String> oldEntries, Map<String, String> newEntries) {
		LinkedHashMap<String, Change> result = new LinkedHashMap<String, Change>();
		Iterator<Entry<String, String>> it = oldEntries.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String newValue = newEntries.get(entry.getKey());
			if (differs(entry.getValue(), newValue)) {
				result.put(entry.getKey(), new Change(entry.getValue(), newValue));
			}
		}
		it = newEntries.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			if (!oldEntries.containsKey(entry.getKey())) {
				result.put(entry.getKey(), new Change(null, entry.getValue()));
			}
		}
		return result;
	}

	/**
	 * The keys that both change sets change to different results.
	 */
	public static Set<String> getConflicts(Map<String, Change> mine, Map<String, Change> theirs) {
		HashSet<String> result = new HashSet<String>();
		Iterator<Entry<String, Change>> it = mine.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Change> entry = it.next();
			Change their = theirs.get(entry.getKey());
			if (their != null && !entry.getValue().sameResult(their)) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	/**
	 * Applies key changes to a text: changed keys are replaced in place, removed keys are dropped,
	 * added keys are appended. Comments, blank lines and the other keys stay as they are.
	 */
	public static String apply(String text, Map<String, Change> changes) throws IOException {
		StringBuilder result = new StringBuilder();
		HashSet<String> done = new HashSet<String>();
		Iterator<String> logicalLines = getLogicalLines(text).iterator();
		while (logicalLines.hasNext()) {
			String logicalLine = logicalLines.next();
			String key = getKey(logicalLine);
			if (key != null && changes.containsKey(key)) {
				if (done.add(key)) {
					String newValue = changes.get(key).getNewValue();
					if (newValue != null) {
						result.append(format(key, newValue));
						result.append(LINE_SEPARATOR);
					}
				}
				// further definitions of the key are dropped
			} else {
				result.append(logicalLine);
				result.append(LINE_SEPARATOR);
			}
		}
		Iterator<Entry<String, Change>> it = changes.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Change> entry = it.next();
			if (!done.contains(entry.getKey()) && entry.getValue().getNewValue() != null) {
				result.append(format(entry.getKey(), entry.getValue().getNewValue()));
				result.append(LINE_SEPARATOR);
			}
		}
		return result.toString();
	}

	/**
	 * One escaped "key=value" line, as Properties.store writes it (but without escaping non-ASCII characters,
	 * since the bundles are read as UTF-8).
	 */
	public static String format(String key, String value) throws IOException {
		Properties single = new Properties();
		single.setProperty(key, value);
		StringWriter writer = new StringWriter();
		single.store(writer, null);
		Iterator<String> lines = getLogicalLines(writer.toString()).iterator();
		while (lines.hasNext()) {
			String line = lines.next();
			if (!isCommentOrBlank(line)) {
				return line;
			}
		}
		throw new IllegalStateException();
	}

	private static String getKey(String logicalLine) throws IOException {
		if (isCommentOrBlank(logicalLine)) {
			return null;
		}
		Properties single = new Properties();
		single.load(new StringReader(logicalLine));
		Iterator<Object> it = single.keySet().iterator();
		return it.hasNext() ? (String) it.next() : null;
	}

	/**
	 * The lines of a text, with continued lines (ending with an odd number of backslashes) joined to one.
	 */
	private static ArrayList<String> getLogicalLines(String text) {
		ArrayList<String> result = new ArrayList<String>();
		if (text == null || text.length() == 0) {
			return result;
		}
		String[] lines = text.split("\r\n|\r|\n", -1);
		StringBuilder logicalLine = null;
		int count = lines.length;
		if (count > 0 && lines[count - 1].length() == 0) {
			count--; // trailing line break
		}
		for (int i = 0; i < count; i++) {
			String line = lines[i];
			if (logicalLine == null) {
				if (isCommentOrBlank(line)) {
					result.add(line);
					continue;
				}
				logicalLine = new StringBuilder(line);
			} else {
				logicalLine.append(LINE_SEPARATOR);
				logicalLine.append(line);
			}
			if (!isContinued(line)) {
				result.add(logicalLine.toString());
				logicalLine = null;
			}
		}
		if (logicalLine != null) {
			result.add(logicalLine.toString());
		}
		return result;
	}

	private static boolean isCommentOrBlank(String line) {
		String trimmed = line.trim();
		return trimmed.length() == 0 || trimmed.charAt(0) == '#' || trimmed.charAt(0) == '!';
	}

	private static boolean isContinued(String line) {
		int backslashes = 0;
		for (int i = line.length() - 1; i >= 0 && line.charAt(i) == '\\'; i--) {
			backslashes++;
		}
		return backslashes % 2 == 1;
	}

	/**
	 * Parses a .properties text.
	 *
	 * @return the entries in the order of the text
	 * @throws IllegalArgumentException
	 *             for malformed \\uXXXX escapes
	 */
	public static LinkedHashMap<String, String> parse(String text) throws IOException {
		OrderedProperties properties = new OrderedProperties();
		if (text != null) {
			properties.load(new StringReader(text));
		}
		return properties.ordered;
	}

	/**
	 * The entries sorted by key, e.g. for the download of an effective bundle.
	 */
	public static String toText(Map<String, String> entries) throws IOException {
		StringBuilder result = new StringBuilder();
		Iterator<Entry<String, String>> it = new TreeMap<String, String>(entries).entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			result.append(format(entry.getKey(), entry.getValue()));
			result.append(LINE_SEPARATOR);
		}
		return result.toString();
	}

	private InputFieldL10nTextUtil() {
	}
}
