package org.phoenixctms.ctsms.excel;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SimpleTimeZone;

import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.ExportStatus;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

import jxl.Cell;
import jxl.biff.DisplayFormat;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.write.DateTime;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

public final class ExcelUtil {

	public final static String DEFAULT_LABEL = "<label>";
	public final static String EXCEL_LINE_BREAK = "\n";
	public final static String EXCEL_HEADER_FOOTER_LINE_BREAK = "\n";
	public final static String EXCEL_DATE_PATTERN = "dd.MM.yyyy"; // "yyyy-MM-dd";
	public final static String EXCEL_DATE_TIME_PATTERN = "dd.MM.yyyy hh:mm"; // HH:mm:ss"; // "yyyy-MM-dd HH:mm:ss";
	public final static String EXCEL_TIME_PATTERN = "hh:mm"; // "HH:mm";
	public final static String COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR = AssociationPath.ASSOCIATION_PATH_SEPARATOR;// ".";
	public final static boolean COLUMN_NAME_LOWER_CASE_FIELD_NAMES = false;
	// http://office.microsoft.com/en-us/excel-help/excel-specifications-and-limits-HP005199291.aspx
	private final static int EXCEL_MAX_CELL_TEXT_LENGTH = 32737; // 1024;
	private final static HashMap<Color, Colour> COLOR_MAPPING = new HashMap<Color, Colour>();
	// private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	static {
		// http://jexcelapi.sourceforge.net/resources/javadocs/2_6_10/docs/jxl/format/Colour.html
		COLOR_MAPPING.put(Color.LIGHTYELLOW, Colour.VERY_LIGHT_YELLOW);
		COLOR_MAPPING.put(Color.ORANGE, Colour.GOLD);
		COLOR_MAPPING.put(Color.DARKORANGE, Colour.LIGHT_ORANGE);
		COLOR_MAPPING.put(Color.SPRINGGREEN, Colour.LIGHT_GREEN);
		COLOR_MAPPING.put(Color.LIMEGREEN, Colour.BRIGHT_GREEN);
		COLOR_MAPPING.put(Color.LIME, Colour.LIME);
		COLOR_MAPPING.put(Color.TOMATO, Colour.DARK_RED);
		COLOR_MAPPING.put(Color.MEDIUMSEAGREEN, Colour.SEA_GREEN);
		COLOR_MAPPING.put(Color.RED, Colour.RED);
		COLOR_MAPPING.put(Color.ORANGERED, Colour.ORANGE);
		COLOR_MAPPING.put(Color.LIGHTGRAY, Colour.GREY_25_PERCENT);
		COLOR_MAPPING.put(Color.GREEN, Colour.GREEN);
		COLOR_MAPPING.put(Color.GAINSBORO, Colour.GREY_25_PERCENT);
		COLOR_MAPPING.put(Color.SALMON, Colour.CORAL);
		COLOR_MAPPING.put(Color.LIGHTSKYBLUE, Colour.SKY_BLUE);
	}

	// formatter to convert from current timezone
	private static final SimpleDateFormat DATE_FORMATTER_FROM_CURRENT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// formatter to convert to GMT timezone
	private static final SimpleDateFormat DATE_FORMATTER_TO_GMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static {
		// initialize the GMT formatter
		final Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
		DATE_FORMATTER_TO_GMT.setCalendar(cal);
	}

	private static void addCell(WritableCell cell, WritableSheet spreadSheet, int c, int r, ExcelCellFormat f) throws WriteException {
		if (cell != null) {
			if (!f.isOverrideFormat()) {
				WritableCellFormat cellFormat = getRowCellFormat(spreadSheet, c, r);
				if (cellFormat == null && f.getBgColor() != null) {
					cellFormat = new WritableCellFormat();
				}
				if (f.getBgColor() != null) {
					setBgColor(cellFormat, f.getBgColor());
				}
				if (cellFormat != null) {
					cell.setCellFormat(cellFormat);
				}
			}
			spreadSheet.addCell(cell);
		}
	}

	public static Colour convertColor(org.phoenixctms.ctsms.enumeration.Color color) {
		return COLOR_MAPPING.get(color);
	}

	private static WritableCell createCell(Class returnType, int c, int r, Object value, ExcelCellFormat f, HashMap<String, WritableCellFormat> cellFormats) {
		// WritableCell cell;
		if (returnType != null) {
			if (returnType.equals(String.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					try {
						cellFormat.setWrap(true);
					} catch (WriteException e) {
					}
					return new jxl.write.Label(c, r, CommonUtil.clipString((String) value, EXCEL_MAX_CELL_TEXT_LENGTH),
							cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, CommonUtil.clipString((String) value, EXCEL_MAX_CELL_TEXT_LENGTH));
				}
			} else if (returnType.equals(Long.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.INTEGER, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, ((Long) value).doubleValue(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, ((Long) value).doubleValue());
				}
			} else if (returnType.equals(java.lang.Long.TYPE)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.INTEGER, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, ((Long) value).doubleValue(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, ((Long) value).doubleValue());
				}
			} else if (returnType.equals(Integer.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.INTEGER, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, ((Integer) value).doubleValue(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, ((Integer) value).doubleValue());
				}
			} else if (returnType.equals(java.lang.Integer.TYPE)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.INTEGER, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, ((Integer) value).doubleValue(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, ((Integer) value).doubleValue());
				}
			} else if (returnType.equals(Boolean.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.DEFAULT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Boolean(c, r, (java.lang.Boolean) value, cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Boolean(c, r, (java.lang.Boolean) value);
				}
			} else if (returnType.equals(java.lang.Boolean.TYPE)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.DEFAULT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Boolean(c, r, (java.lang.Boolean) value, cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Boolean(c, r, (java.lang.Boolean) value);
				}
			} else if (returnType.equals(Float.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.FLOAT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, ((Float) value).doubleValue(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, floatToDouble((Float) value));
				}
			} else if (returnType.equals(java.lang.Float.TYPE)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.FLOAT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, ((Float) value).doubleValue(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, floatToDouble((Float) value));
				}
			} else if (returnType.equals(Double.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.FLOAT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, (Double) value, cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, (Double) value);
				}
			} else if (returnType.equals(java.lang.Double.TYPE)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.FLOAT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Number(c, r, (Double) value, cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Number(c, r, (Double) value);
				}
			} else if (returnType.equals(Date.class)
					|| returnType.equals(Timestamp.class) // should not be used by any out vo
					|| returnType.equals(Time.class)) { // should not be used by any out vo
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat;
					if (value != null && DateCalc.isTime((Date) value)) {
						cellFormat = getRowCellFormat(new jxl.write.DateFormat(EXCEL_TIME_PATTERN), f, cellFormats);
					} else if (value == null || DateCalc.isDatetime((Date) value)) {
						cellFormat = getRowCellFormat(new jxl.write.DateFormat(EXCEL_DATE_TIME_PATTERN), f, cellFormats);
					} else {
						cellFormat = getRowCellFormat(new jxl.write.DateFormat(EXCEL_DATE_PATTERN), f, cellFormats);
					}
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.DateTime(c, r, toGMT((Date) value), cellFormat, DateTime.GMT);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.DateTime(c, r, toGMT((Date) value), DateTime.GMT);
				}
				// } else if (returnType.equals(Timestamp.class)) { // should not be used by any out vo
				// if (f.isOverrideFormat()) {
				// WritableCellFormat cellFormat;
				// if (value != null && DateCalc.isTime((Date) value)) {
				// cellFormat = getRowCellFormat(new jxl.write.DateFormat(EXCEL_TIME_PATTERN), f, cellFormats);
				// } else if (value == null || DateCalc.isDatetime((Date) value)) {
				// cellFormat = getRowCellFormat(new jxl.write.DateFormat(EXCEL_DATE_TIME_PATTERN), f, cellFormats);
				// } else {
				// cellFormat = getRowCellFormat(new jxl.write.DateFormat(EXCEL_DATE_PATTERN), f, cellFormats);
				// }
				// //WritableCellFormat cellFormat = getRowCellFormat(new jxl.write.DateFormat(EXCEL_DATE_TIME_PATTERN), f, cellFormats);
				// if (value == null) {
				// return new jxl.write.Blank(c, r, cellFormat);
				// }
				// return new jxl.write.DateTime(c, r, toGMT((Date) value), cellFormat, DateTime.GMT);
				// } else {
				// if (value == null) {
				// return new jxl.write.Blank(c, r);
				// }
				// return new jxl.write.DateTime(c, r, toGMT((Date) value), DateTime.GMT);
				// }
			} else if (returnType.equals(VariablePeriod.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((VariablePeriod) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((VariablePeriod) value).name());
				}
			} else if (returnType.equals(AuthenticationType.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((AuthenticationType) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((AuthenticationType) value).name());
				}
			} else if (returnType.equals(Sex.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((Sex) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((Sex) value).name());
				}
			} else if (returnType.equals(RandomizationMode.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((RandomizationMode) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((RandomizationMode) value).name());
				}
			} else if (returnType.equals(DBModule.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((DBModule) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((DBModule) value).name());
				}
			} else if (returnType.equals(HyperlinkModule.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((HyperlinkModule) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((HyperlinkModule) value).name());
				}
			} else if (returnType.equals(JournalModule.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((JournalModule) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((JournalModule) value).name());
				}
			} else if (returnType.equals(FileModule.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((FileModule) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((FileModule) value).name());
				}
			} else if (returnType.equals(Color.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((Color) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((Color) value).name());
				}
			} else if (returnType.equals(InputFieldType.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((InputFieldType) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((InputFieldType) value).name());
				}
			} else if (returnType.equals(EventImportance.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((EventImportance) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((EventImportance) value).name());
				}
			} else if (returnType.equals(ExportStatus.class)) {
				if (f.isOverrideFormat()) {
					WritableCellFormat cellFormat = getRowCellFormat(NumberFormats.TEXT, f, cellFormats);
					if (value == null) {
						return new jxl.write.Blank(c, r, cellFormat);
					}
					return new jxl.write.Label(c, r, ((ExportStatus) value).name(), cellFormat);
				} else {
					if (value == null) {
						return new jxl.write.Blank(c, r);
					}
					return new jxl.write.Label(c, r, ((ExportStatus) value).name());
				}
			}
		}
		return null;
	}

	private static double floatToDouble(float value) {
		try {
			return Double.parseDouble(Float.toString(value));
		} catch (NumberFormatException e) {
			return ((Float) value).doubleValue();
		}
	}

	private static String getFormatCode(boolean head, DisplayFormat format, Color bgColor) {
		StringBuilder formatCode = new StringBuilder(head ? "h" : "r");
		formatCode.append("-");
		formatCode.append(format.getFormatIndex());
		if (bgColor != null) {
			formatCode.append("-");
			formatCode.append(bgColor.name());
		}
		return formatCode.toString();
	}

	private static WritableCellFormat getHeadCellFormat(ExcelCellFormat f, HashMap<String, WritableCellFormat> cellFormats) {
		WritableCellFormat cellFormat;
		String formatCode = getFormatCode(true, NumberFormats.TEXT, f.getBgColor());
		if (!cellFormats.containsKey(formatCode)) {
			cellFormat = new WritableCellFormat(f.getFont(), NumberFormats.TEXT);
			setBgColor(cellFormat, f.getBgColor());
			cellFormats.put(formatCode, cellFormat);
		} else {
			cellFormat = cellFormats.get(formatCode);
		}
		return cellFormat;
	}

	private static WritableCellFormat getRowCellFormat(DisplayFormat format, ExcelCellFormat f, HashMap<String, WritableCellFormat> cellFormats) {
		WritableCellFormat cellFormat;
		String formatCode = getFormatCode(false, format, f.getBgColor());
		if (!cellFormats.containsKey(formatCode)) {
			cellFormat = new WritableCellFormat(f.getFont(), format);
			setBgColor(cellFormat, f.getBgColor());
			cellFormats.put(formatCode, cellFormat);
		} else {
			cellFormat = cellFormats.get(formatCode);
		}
		return cellFormat;
	}

	private static WritableCellFormat getRowCellFormat(WritableSheet spreadSheet, int c, int r) {
		if (spreadSheet != null) {
			Cell cell = spreadSheet.getCell(c, r);
			if (cell != null) {
				CellFormat cellFormat = cell.getCellFormat();
				if (cellFormat != null) {
					return new WritableCellFormat(cellFormat);
				}
			}
		}
		return null;
	}

	public static boolean isWriteableType(Class returnType, HashMap<String, WritableCellFormat> cellFormats) {
		return createCell(returnType, 0, 0, null, ExcelCellFormat.getDefaultRowFormat(), cellFormats) != null;
	}

	public static String selectionSetValuesToString(Collection<InputFieldSelectionSetValueOutVO> selectionSetValues) {
		StringBuilder sb = new StringBuilder();
		if (selectionSetValues != null && selectionSetValues.size() > 0) {
			Iterator<InputFieldSelectionSetValueOutVO> it = selectionSetValues.iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
				if (sb.length() > 0) {
					sb.append(EXCEL_LINE_BREAK);
				}
				if (selectionSetValue != null) {
					String value = selectionSetValue.getValue();
					if (value != null && value.length() > 0) {
						sb.append(value);
					}
				}
			}
		}
		return sb.toString();
	}

	private static void setBgColor(WritableCellFormat cellFormat, Color bgColor) {
		Colour colour = convertColor(bgColor);
		if (colour != null) {
			try {
				cellFormat.setBackground(colour);
			} catch (WriteException e) {
			}
		}
	}

	// private static Date toGMT(Date date) {
	// //http://stackoverflow.com/questions/7695859/how-to-convert-a-data-from-1-timezone-to-another-timezone
	// GregorianCalendar gmt = new GregorianCalendar(); // default timezone
	// gmt.set(Calendar.YEAR, date.getYear() + 1900);
	// gmt.set(Calendar.MONTH, date.getMonth());
	// gmt.set(Calendar.DAY_OF_MONTH, date.getDate());
	// gmt.set(Calendar.HOUR_OF_DAY, date.getHours());
	// gmt.set(Calendar.MINUTE, date.getMinutes());
	// gmt.set(Calendar.SECOND, date.getSeconds());
	// gmt.setTimeZone(GMT);
	// return new Date(gmt.get(Calendar.YEAR) - 1900, gmt.get(Calendar.MONTH), gmt.get(Calendar.DAY_OF_MONTH), gmt.get(Calendar.HOUR_OF_DAY), gmt.get(Calendar.MINUTE),
	// gmt.get(Calendar.SECOND));
	// }
	public static Date toGMT(final Date base) {
		// http://stackoverflow.com/questions/8579082/jxl-and-timezone-writing-an-excel
		try {
			// convert to string and after that convert it back
			final String date = DATE_FORMATTER_FROM_CURRENT.format(base);
			return DATE_FORMATTER_TO_GMT.parse(date);
		} catch (ParseException e) {
			// log.error( "Date parsing failed. Conversion to GMT wasn't performed.", e );
			return base;
		}
		// seem to be the only solution
	}

	public static void writeCell(WritableSheet spreadSheet, int c, int r, Class returnType, Object value, ExcelCellFormat f, HashMap<String, WritableCellFormat> cellFormats)
			throws Exception {
		if (spreadSheet != null) {
			WritableCell cell = createCell(returnType, c, r, value, f, cellFormats);
			addCell(cell, spreadSheet, c, r, f);
		}
	}

	public static void writeHead(WritableSheet spreadSheet, int c, int r, String columnName, ExcelCellFormat f, HashMap<String, WritableCellFormat> cellFormats) throws Exception {
		if (spreadSheet != null) {
			WritableCell cell;
			if (f.isOverrideFormat()) {
				cell = new jxl.write.Label(c, r, columnName, getHeadCellFormat(f, cellFormats));
			} else {
				cell = new jxl.write.Label(c, r, columnName);
			}
			addCell(cell, spreadSheet, c, r, f);
		}
	}

	private ExcelUtil() {
	}
}
