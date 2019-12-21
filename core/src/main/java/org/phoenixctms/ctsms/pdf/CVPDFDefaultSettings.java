package org.phoenixctms.ctsms.pdf;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;

public final class CVPDFDefaultSettings {

	public final static int GRAPH_MAX_STAFF_INSTANCES = 10;
	public final static boolean LANDSCAPE = false;
	public static final float PAGE_UPPER_MARGIN = 70.0f;
	public static final float PAGE_LOWER_MARGIN = 60.0f;
	public static final float PAGE_LEFT_MARGIN = 60.0f;
	public static final float PAGE_RIGHT_MARGIN = 60.0f;
	public final static float X_COLUMN_INDENT = 200.0f;
	public final static float X_FRAME_INDENT = 5.0f;
	public final static float Y_FRAME_INDENT = 5.0f;
	public final static float SPACER_HEIGHT = 20.0f;
	public static final float BLOCK_FRAME_LINE_WIDTH = 1.0f;
	public static final float COLUMN_LINE_WIDTH = 0.6f;
	public static final float BLOCKS_UPPER_MARGIN = PAGE_UPPER_MARGIN + Y_FRAME_INDENT;
	public static final float BLOCKS_LOWER_MARGIN = PAGE_LOWER_MARGIN + Y_FRAME_INDENT + 10.0f;
	public static final float BLOCKS_LEFT_MARGIN = PAGE_LEFT_MARGIN + X_FRAME_INDENT;
	public static final float BLOCKS_RIGHT_MARGIN = PAGE_RIGHT_MARGIN + X_FRAME_INDENT;
	public static final float Y_FRAME_UPPER_INDENT_SIGNATURE = 38.0f;
	public static final float Y_FRAME_LOWER_INDENT_SIGNATURE = 3.0f;
	public static final float X_FRAME_INDENT_SIGNATURE = 7.0f; // 24.0f;
	public static final float SIGNATURE_LINE_WIDTH = 0.3f;
	public static final float SIGNATURE_LINE_LENGTH = 175.0f; // 250.0f;
	public static final float Y_OFFSET_SIGNATURE_ANNOTATION = 2.0f;
	public static final float DATE_LABEL_WIDTH = 35.0f;
	public static final float DATE_LINE_LENGTH = 100.0f;
	public static final String DATE_OF_BIRTH_DATE_PATTERN = "d. MMMM yyyy";
	public static final String SIGNATURE_DATE_PATTERN = "d. MMMM yyyy";
	public final static String POSITION_COURSE_DATE_LABEL = " ({0})";
	public final static String POSITION_FROM_TO_LABEL = " ({0} - {1}";
	public final static String POSITION_FROM_LABEL = " (from {0})";
	public final static String POSITION_TO_LABEL = " (until {0})";
	public final static String POSITION_DATE_PATTERN = "yyyy";
	public static final Color TEXT_COLOR = Color.BLACK;
	public static final Color FRAME_COLOR = Color.BLACK;
	public static final boolean SHOW_IMAGES = true;
	public static final Float IMAGE_WIDTH = null;
	public static final Float IMAGE_HEIGHT = 180.0f;
	public static final boolean SHOW_PAGE_NUMBERS = true;
	public static final int IMAGE_QUALITY = 75;
	public static final int IMAGE_DPI = 72;
	public static final float X_COLUMN_INDENT_PHOTO = 130.0f;
	public static final boolean IMAGE_CENTERED = false;
	public static final float Y_IMAGE_INDENT = 15.0f;
	public static final float X_IMAGE_INDENT = 15.0f;
	public static final String PAINTER_CLASS = null;
	public static final ArrayList<String> PAINTER_SOURCE_FILES = null;

	private CVPDFDefaultSettings() {
	}
}
