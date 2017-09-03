package org.phoenixctms.ctsms.util;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//http://stackoverflow.com/questions/2206378/how-to-split-a-string-but-also-keep-the-delimiters
public class StringSplitter {

	private Pattern regExp;
	private boolean keepSeparators;

	public StringSplitter(Pattern regExp, boolean keepSeparators) {
		this.regExp = regExp;
		this.keepSeparators = keepSeparators;
	}

	public StringSplitter(String pattern, boolean keepSeparators) {
		this(Pattern.compile(pattern == null ? "" : pattern), keepSeparators);
	}

	public String[] split(String string) {
		if (string == null) {
			string = "";
		}
		int lastMatch = 0;
		LinkedList<String> splitted = new LinkedList<String>();
		Matcher m = this.regExp.matcher(string);
		while (m.find()) {
			splitted.add(string.substring(lastMatch, m.start()));
			if (this.keepSeparators) {
				splitted.add(m.group());
			}
			lastMatch = m.end();
		}
		splitted.add(string.substring(lastMatch));
		return splitted.toArray(new String[splitted.size()]);
	}
}
/* Example: > java Splitter "\W+" "Hello World!" Part 1: "Hello" Part 2: " " Part 3: "World" Part 4: "!" Part 5: "" */