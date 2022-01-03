package org.phoenixctms.ctsms.excel;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;

import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;

public class ExcelCellFormat {

	public static ExcelCellFormat fromStringList(ArrayList<String> format) throws Exception {
		try {
			return fromString(format.get(0), format.get(1), format.get(2), format.get(3), format.get(4),
					Boolean.parseBoolean(format.get(5)),
					Boolean.parseBoolean(format.get(6)),
					Boolean.parseBoolean(format.get(7)));
		} catch (Exception e) {
			return fromString(format.get(0), format.get(1), format.get(2), format.get(3), format.get(4), DEFAULT_TIME_USER_TIMEZONE, DEFAULT_DATE_USER_TIMEZONE,
					DEFAULT_DATE_TIME_USER_TIMEZONE);
		}
	}

	private static ExcelCellFormat fromString(String fontName, String fontSize, String bold, String bgColor, String overrideFormat,
			boolean isTimeUserTimezone, boolean isDateUserTimezone, boolean isDateTimeUserTimezone) throws Exception {
		return new ExcelCellFormat(
				jxl.write.WritableFont.createFont(fontName),
				Integer.parseInt(fontSize),
				Boolean.parseBoolean(bold),
				bgColor != null && bgColor.length() > 0 && !CommonUtil.getIsValueNull(bgColor) ? Color.fromString(bgColor) : null,
				Boolean.parseBoolean(overrideFormat),
				isTimeUserTimezone,
				isDateUserTimezone,
				isDateTimeUserTimezone);
	}

	private FontName fontName;
	private int fontSize;
	private boolean bold;
	private Color bgColor;
	private boolean overrideFormat;
	private boolean isTimeUserTimezone;
	private boolean isDateUserTimezone;
	private boolean isDateTimeUserTimezone;
	private WritableFont font;
	private static final FontName DEFAULT_HEAD_FONT_NAME = WritableFont.ARIAL;
	private static final int DEFAULT_HEAD_FONT_SIZE = 10;
	private static final Color DEFAULT_HEAD_BG_COLOR = Color.LIGHTGRAY;
	private static final boolean DEFAULT_HEAD_BOLD = true;
	private static final ExcelCellFormat DEFAULT_HEAD_FORMAT = new ExcelCellFormat(DEFAULT_HEAD_FONT_NAME, DEFAULT_HEAD_FONT_SIZE, DEFAULT_HEAD_BOLD, DEFAULT_HEAD_BG_COLOR, true,
			false, false, false);
	private static final boolean DEFAULT_TIME_USER_TIMEZONE = true;
	private static final boolean DEFAULT_DATE_USER_TIMEZONE = true;
	private static final boolean DEFAULT_DATE_TIME_USER_TIMEZONE = true;
	private static final FontName DEFAULT_ROW_FONT_NAME = WritableFont.ARIAL;
	private static final int DEFAULT_ROW_FONT_SIZE = 10;
	private static final boolean DEFAULT_ROW_BOLD = false;
	private static final ExcelCellFormat DEFAULT_ROW_FORMAT = new ExcelCellFormat(DEFAULT_ROW_FONT_NAME, DEFAULT_ROW_FONT_SIZE, DEFAULT_ROW_BOLD, null, true,
			DEFAULT_TIME_USER_TIMEZONE, DEFAULT_DATE_USER_TIMEZONE, DEFAULT_DATE_TIME_USER_TIMEZONE);

	public static ExcelCellFormat getDefaultHeadFormat() {
		return new ExcelCellFormat(DEFAULT_HEAD_FORMAT);
	}

	public static ExcelCellFormat getDefaultRowFormat() {
		return new ExcelCellFormat(DEFAULT_ROW_FORMAT);
	}

	private ExcelCellFormat(ExcelCellFormat f) {
		this.fontName = f.fontName;
		this.fontSize = f.fontSize;
		this.bold = f.bold;
		this.bgColor = f.bgColor;
		this.overrideFormat = f.overrideFormat;
		this.isTimeUserTimezone = f.isTimeUserTimezone;
		this.isDateUserTimezone = f.isDateUserTimezone;
		this.isDateTimeUserTimezone = f.isDateTimeUserTimezone;
		updateFont();
	}

	public ExcelCellFormat(FontName fontName, int fontSize, boolean bold,
			Color bgColor, boolean overrideFormat, boolean isTimeUserTimezone, boolean isDateUserTimezone, boolean isDateTimeUserTimezone) {
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.bold = bold;
		this.bgColor = bgColor;
		this.overrideFormat = overrideFormat;
		this.isTimeUserTimezone = isTimeUserTimezone;
		this.isDateUserTimezone = isDateUserTimezone;
		this.isDateTimeUserTimezone = isDateTimeUserTimezone;
		updateFont();
	}

	public Color getBgColor() {
		return bgColor;
	}

	public WritableFont getFont() {
		return font;
	}

	public FontName getFontName() {
		return fontName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isOverrideFormat() {
		return overrideFormat;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
		updateFont();
	}

	public void setFontName(FontName fontName) {
		this.fontName = fontName;
		updateFont();
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		updateFont();
	}

	public void setOverrideFormat(boolean overrideFormat) {
		this.overrideFormat = overrideFormat;
	}

	private void updateFont() {
		font = new WritableFont(fontName, fontSize, bold ? WritableFont.BOLD : WritableFont.NO_BOLD);
	}

	public boolean isTimeUserTimezone() {
		return isTimeUserTimezone;
	}

	public void setTimeUserTimezone(boolean isTimeUserTimezone) {
		this.isTimeUserTimezone = isTimeUserTimezone;
	}

	public boolean isDateUserTimezone() {
		return isDateUserTimezone;
	}

	public void setDateUserTimezone(boolean isDateUserTimezone) {
		this.isDateUserTimezone = isDateUserTimezone;
	}

	public boolean isDateTimeUserTimezone() {
		return isDateTimeUserTimezone;
	}

	public void setDateTimeUserTimezone(boolean isDateTimeUserTimezone) {
		this.isDateTimeUserTimezone = isDateTimeUserTimezone;
	}
}
