package org.phoenixctms.ctsms.pdf;

import org.phoenixctms.ctsms.enumeration.Color;

public final class EcrfSpecificationPDFDefaultSettings {

	public final static boolean LANDSCAPE = false;
	public final static float X_COLUMN_INDENT = 200.0f;
	public final static float X_FRAME_INDENT = 2.5f;
	public final static float Y_FRAME_INDENT = 2.5f;
	public final static float SPACER_HEIGHT = 20.0f;
	public final static float X_SPECIFICATION_TABLE_FRAME_INDENT = 10.0f;
	public final static float Y_SPECIFICATION_TABLE_FRAME_INDENT = 10.0f;
	public final static float Y_SPECIFICATION_TABLE_HEAD_FRAME_INDENT = 3.0f;
	public final static float X_SPECIFICATION_TABLE_HEAD_FRAME_INDENT = 3.0f;
	public static final float SPECIFICATION_TABLE_FRAME_LINE_WIDTH = 1.5f;
	public static final float PAGE_UPPER_MARGIN = 70.0f;
	public static final float PAGE_LOWER_MARGIN = 60.0f;
	public static final float PAGE_LEFT_MARGIN = 60.0f;
	public static final float PAGE_RIGHT_MARGIN = 60.0f;
	public static final float BLOCKS_UPPER_MARGIN = PAGE_UPPER_MARGIN + Math.max(Y_FRAME_INDENT, Y_SPECIFICATION_TABLE_FRAME_INDENT);
	public static final float BLOCKS_LOWER_MARGIN = PAGE_LOWER_MARGIN + Math.max(Y_FRAME_INDENT, Y_SPECIFICATION_TABLE_FRAME_INDENT) + 10.0f;
	public static final float BLOCKS_LEFT_MARGIN = PAGE_LEFT_MARGIN + Math.max(X_FRAME_INDENT, X_SPECIFICATION_TABLE_FRAME_INDENT);
	public static final float BLOCKS_RIGHT_MARGIN = PAGE_RIGHT_MARGIN + Math.max(X_FRAME_INDENT, X_SPECIFICATION_TABLE_FRAME_INDENT);
	public static final String NOW_DATE_PATTERN = "d. MMMM yyyy";
	public static final Color TEXT_COLOR = Color.BLACK;
	//	public static final Color FRAME_COLOR = Color.BLACK;
	public static final Color SPECIFICATION_TABLE_TEXT_COLOR = Color.BLACK;
	public static final Color SPECIFICATION_TABLE_FRAME_COLOR = Color.DARKGREY;
	public static final float SPECIFICATION_TABLE_COLUMN_SECTION_WIDTH = 100.0f;
	public static final float SPECIFICATION_TABLE_COLUMN_FIELD_TITLE_WIDTH = 100.0f;
	public static final float SPECIFICATION_TABLE_COLUMN_FIELD_TYPE_WIDTH = 100.0f;
	public static final boolean SHOW_PAGE_NUMBERS = true;
	//	public static final String PAINTER_CLASS = null;
	//	public static final ArrayList<String> PAINTER_SOURCE_FILES = null;
	public static final boolean DATE_TIME_USER_TIME_ZONE = true;
	public static final boolean DATE_USER_TIME_ZONE = true;

	private EcrfSpecificationPDFDefaultSettings() {
	}
}
