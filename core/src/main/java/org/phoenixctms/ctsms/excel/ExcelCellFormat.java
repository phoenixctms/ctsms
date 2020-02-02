package org.phoenixctms.ctsms.excel;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;

import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;

public class ExcelCellFormat {

	public static ExcelCellFormat fromString(String fontName, String fontSize, String bold, String bgColor, String overrideFormat) throws Exception {
		return new ExcelCellFormat(
				jxl.write.WritableFont.createFont(fontName),
				Integer.parseInt(fontSize),
				Boolean.parseBoolean(bold),
				bgColor != null && bgColor.length() > 0 && !CommonUtil.getIsValueNull(bgColor) ? Color.fromString(bgColor) : null,
				Boolean.parseBoolean(overrideFormat));
	}

	private FontName fontName;
	private int fontSize;
	private boolean bold;
	private Color bgColor;
	private boolean overrideFormat;
	private WritableFont font;
	private final static FontName DEFAULT_HEAD_FONT_NAME = WritableFont.ARIAL;
	private final static int DEFAULT_HEAD_FONT_SIZE = 10;
	private final static Color DEFAULT_HEAD_BG_COLOR = Color.LIGHTGRAY;
	private final static boolean DEFAULT_HEAD_BOLD = true;
	private final static ExcelCellFormat DEFAULT_HEAD_FORMAT = new ExcelCellFormat(DEFAULT_HEAD_FONT_NAME, DEFAULT_HEAD_FONT_SIZE, DEFAULT_HEAD_BOLD, DEFAULT_HEAD_BG_COLOR, true);
	private final static FontName DEFAULT_ROW_FONT_NAME = WritableFont.ARIAL;
	private final static int DEFAULT_ROW_FONT_SIZE = 10;
	private final static boolean DEFAULT_ROW_BOLD = false;
	private final static ExcelCellFormat DEFAULT_ROW_FORMAT = new ExcelCellFormat(DEFAULT_ROW_FONT_NAME, DEFAULT_ROW_FONT_SIZE, DEFAULT_ROW_BOLD, null, true);

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
		updateFont();
	}

	public ExcelCellFormat(FontName fontName, int fontSize, boolean bold,
			Color bgColor, boolean overrideFormat) {
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.bold = bold;
		this.bgColor = bgColor;
		this.overrideFormat = overrideFormat;
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
}
