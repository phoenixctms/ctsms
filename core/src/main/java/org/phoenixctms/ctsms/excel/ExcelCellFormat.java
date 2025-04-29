package org.phoenixctms.ctsms.excel;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;

public class ExcelCellFormat {

	private enum BorderLineStyles {

		NONE(BorderLineStyle.NONE),
		THIN(BorderLineStyle.THIN),
		MEDIUM(BorderLineStyle.MEDIUM),
		DASHED(BorderLineStyle.DASHED),
		DOTTED(BorderLineStyle.DOTTED),
		THICK(BorderLineStyle.THICK),
		DOUBLE(BorderLineStyle.DOUBLE),
		HAIR(BorderLineStyle.HAIR),
		MEDIUM_DASHED(BorderLineStyle.MEDIUM_DASHED),
		DASH_DOT(BorderLineStyle.DASH_DOT),
		MEDIUM_DASH_DOT(BorderLineStyle.MEDIUM_DASH_DOT),
		DASH_DOT_DOT(BorderLineStyle.DASH_DOT_DOT),
		MEDIUM_DASH_DOT_DOT(BorderLineStyle.MEDIUM_DASH_DOT_DOT),
		SLANTED_DASH_DOT(BorderLineStyle.SLANTED_DASH_DOT);

		private final BorderLineStyle style;

		private BorderLineStyles(final BorderLineStyle style) {
			this.style = style;
		}

		public BorderLineStyle getStyle() {
			return style;
		}

		@Override
		public String toString() {
			return style.toString();
		}
	}

	private enum Borders {

		NONE(Border.NONE),
		ALL(Border.ALL),
		TOP(Border.TOP),
		BOTTOM(Border.BOTTOM),
		LEFT(Border.LEFT),
		RIGHT(Border.RIGHT);

		private final Border border;

		private Borders(final Border border) {
			this.border = border;
		}

		public Border getBorder() {
			return border;
		}

		@Override
		public String toString() {
			return border.toString();
		}
	}

	private enum Alignments {

		GENERAL(Alignment.GENERAL),
		LEFT(Alignment.LEFT),
		CENTRE(Alignment.CENTRE),
		CENTER(Alignment.CENTRE),
		RIGHT(Alignment.RIGHT),
		FILL(Alignment.FILL),
		JUSTIFY(Alignment.JUSTIFY);

		private final Alignment alignment;

		private Alignments(final Alignment alignment) {
			this.alignment = alignment;
		}

		public Alignment getAlignment() {
			return alignment;
		}

		@Override
		public String toString() {
			return alignment.toString();
		}
	}

	private enum VerticalAlignments {

		TOP(VerticalAlignment.TOP),
		CENTRE(VerticalAlignment.CENTRE),
		CENTER(VerticalAlignment.CENTRE),
		BOTTOM(VerticalAlignment.BOTTOM),
		JUSTIFY(VerticalAlignment.JUSTIFY);

		private final VerticalAlignment verticalAlignment;

		private VerticalAlignments(final VerticalAlignment verticalAlignment) {
			this.verticalAlignment = verticalAlignment;
		}

		public VerticalAlignment getVerticalAlignment() {
			return verticalAlignment;
		}

		@Override
		public String toString() {
			return verticalAlignment.toString();
		}
	}

	public static ExcelCellFormat fromStringList(ArrayList<String> format) throws Exception {
		try {
			return fromString(
					format.get(0),
					format.get(1),
					format.get(2),
					format.get(3),
					format.get(4),
					format.get(5),
					format.get(6),
					format.get(7),
					format.get(8),
					Boolean.parseBoolean(format.get(9)),
					Boolean.parseBoolean(format.get(10)),
					Boolean.parseBoolean(format.get(11)));
		} catch (Exception e) {
			return fromString(format.get(0), format.get(1), format.get(2), format.get(3), format.get(4), format.get(5), format.get(6), format.get(7), format.get(8),
					DEFAULT_TIME_USER_TIMEZONE,
					DEFAULT_DATE_USER_TIMEZONE,
					DEFAULT_DATE_TIME_USER_TIMEZONE);
		}
	}

	private static ExcelCellFormat fromString(String fontName, String fontSize, String bold, String bgColor, String border, String borderStyle,
			String alignment,
			String verticalAlignment,
			String overrideFormat,
			boolean isTimeUserTimezone, boolean isDateUserTimezone, boolean isDateTimeUserTimezone) throws Exception {
		return new ExcelCellFormat(
				jxl.write.WritableFont.createFont(fontName),
				Integer.parseInt(fontSize),
				Boolean.parseBoolean(bold),
				bgColor != null && bgColor.length() > 0 && !CommonUtil.getIsValueNull(bgColor) ? Color.fromString(bgColor) : null,
				border != null && border.length() > 0 && !CommonUtil.getIsValueNull(border) ? Borders.valueOf(border).getBorder() : null,
				borderStyle != null && borderStyle.length() > 0 && !CommonUtil.getIsValueNull(borderStyle)
						? BorderLineStyles.valueOf(borderStyle).getStyle()
						: null,
				alignment != null && alignment.length() > 0 && !CommonUtil.getIsValueNull(alignment) ? Alignments.valueOf(alignment).getAlignment() : null,
				verticalAlignment != null && verticalAlignment.length() > 0 && !CommonUtil.getIsValueNull(verticalAlignment)
						? VerticalAlignments.valueOf(verticalAlignment).getVerticalAlignment()
						: null,
				Boolean.parseBoolean(overrideFormat), isTimeUserTimezone, isDateUserTimezone, isDateTimeUserTimezone);
	}

	private FontName fontName;
	private int fontSize;
	private boolean bold;
	private Color bgColor;
	private Border border;
	private BorderLineStyle borderStyle;
	private Alignment alignment;
	private VerticalAlignment verticalAlignment;
	private boolean overrideFormat;
	private boolean isTimeUserTimezone;
	private boolean isDateUserTimezone;
	private boolean isDateTimeUserTimezone;
	private WritableFont font;
	private static final FontName DEFAULT_HEAD_FONT_NAME = WritableFont.ARIAL;
	private static final int DEFAULT_HEAD_FONT_SIZE = 10;
	private static final Color DEFAULT_HEAD_BG_COLOR = Color.LIGHTGRAY;
	private static final Alignment DEFAULT_HEAD_ALIGNMENT = null;
	private static final VerticalAlignment DEFAULT_HEAD_VERTICAL_ALIGNMENT = null;
	private static final Border DEFAULT_HEAD_BORDER = null; //Border.NONE;
	private static final BorderLineStyle DEFAULT_HEAD_BORDER_STYLE = null; //BorderLineStyle.NONE;
	private static final boolean DEFAULT_HEAD_BOLD = true;
	private static final ExcelCellFormat DEFAULT_HEAD_FORMAT = new ExcelCellFormat(DEFAULT_HEAD_FONT_NAME, DEFAULT_HEAD_FONT_SIZE, DEFAULT_HEAD_BOLD, DEFAULT_HEAD_BG_COLOR,
			DEFAULT_HEAD_BORDER, DEFAULT_HEAD_BORDER_STYLE, DEFAULT_HEAD_ALIGNMENT, DEFAULT_HEAD_VERTICAL_ALIGNMENT, true,
			false, false, false);
	private static final boolean DEFAULT_TIME_USER_TIMEZONE = true;
	private static final boolean DEFAULT_DATE_USER_TIMEZONE = true;
	private static final boolean DEFAULT_DATE_TIME_USER_TIMEZONE = true;
	private static final FontName DEFAULT_ROW_FONT_NAME = WritableFont.ARIAL;
	private static final int DEFAULT_ROW_FONT_SIZE = 10;
	private static final Border DEFAULT_ROW_BORDER = null; //Border.NONE;
	private static final BorderLineStyle DEFAULT_ROW_BORDER_STYLE = null; //BorderLineStyle.NONE;
	private static final Alignment DEFAULT_ROW_ALIGNMENT = null;
	private static final VerticalAlignment DEFAULT_ROW_VERTICAL_ALIGNMENT = null;
	private static final Color DEFAULT_ROW_BG_COLOR = null;
	private static final boolean DEFAULT_ROW_BOLD = false;
	private static final ExcelCellFormat DEFAULT_ROW_FORMAT = new ExcelCellFormat(DEFAULT_ROW_FONT_NAME, DEFAULT_ROW_FONT_SIZE, DEFAULT_ROW_BOLD, DEFAULT_ROW_BG_COLOR,
			DEFAULT_ROW_BORDER,
			DEFAULT_ROW_BORDER_STYLE, DEFAULT_ROW_ALIGNMENT, DEFAULT_ROW_VERTICAL_ALIGNMENT, true,
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
		this.border = f.border;
		this.borderStyle = f.borderStyle;
		this.alignment = f.alignment;
		this.verticalAlignment = f.verticalAlignment;
		this.overrideFormat = f.overrideFormat;
		this.isTimeUserTimezone = f.isTimeUserTimezone;
		this.isDateUserTimezone = f.isDateUserTimezone;
		this.isDateTimeUserTimezone = f.isDateTimeUserTimezone;
		updateFont();
	}

	public ExcelCellFormat(FontName fontName, int fontSize, boolean bold,
			Color bgColor, Border border, BorderLineStyle borderStyle, Alignment alignment, VerticalAlignment verticalAlignment, boolean overrideFormat, boolean isTimeUserTimezone,
			boolean isDateUserTimezone,
			boolean isDateTimeUserTimezone) {
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.bold = bold;
		this.bgColor = bgColor;
		this.border = border;
		this.borderStyle = borderStyle;
		this.alignment = alignment;
		this.verticalAlignment = verticalAlignment;
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

	public Border getBorder() {
		return border;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public BorderLineStyle getBorderStyle() {
		return borderStyle;
	}

	public void setBorderStyle(BorderLineStyle borderStyle) {
		this.borderStyle = borderStyle;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}
}
