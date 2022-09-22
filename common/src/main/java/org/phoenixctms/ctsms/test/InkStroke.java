package org.phoenixctms.ctsms.test;

import java.io.UnsupportedEncodingException;

import org.phoenixctms.ctsms.util.CommonUtil;

public final class InkStroke {

	private final static String INK_VALUE_CHARSET = "UTF8";
	public String color;
	public String path;
	public String strokesId;
	public String value;
	public boolean valueSet;

	private InkStroke() {
		color = null;
		path = null;
		strokesId = CommonUtil.generateUUID();
		value = null;
		valueSet = false;
	}

	public InkStroke(String path) {
		this();
		this.color = "#00ff00";
		this.path = path;
	}

	public InkStroke(String path, String value) {
		this(path);
		setValue(value);
	}

	public InkStroke(String color, String path, String value) {
		this();
		this.color = color;
		this.path = path;
		setValue(value);
	}

	public byte[] getBytes() throws UnsupportedEncodingException {
		return toString().getBytes(INK_VALUE_CHARSET);
	}

	private void setValue(String value) {
		this.value = value;
		valueSet = true;
	}

	@Override
	public String toString() {
		return String.format("[{" +
				"\"fill\": \"%s\"," +
				"\"stroke\": \"%s\"," +
				"\"path\": \"%s\"," +
				"\"stroke-opacity\": 0.4," +
				"\"stroke-width\": 2," +
				"\"stroke-linecap\": \"round\"," +
				"\"stroke-linejoin\": \"round\"," +
				"\"transform\": []," +
				"\"type\": \"path\"," +
				"\"fill-opacity\": 0.2," +
				"\"strokes-id\": \"%s\"}]", color, color, path, strokesId);
	}
}
