package org.phoenixctms.ctsms.pdf;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;

public final class TrainingRecordPDFDefaultSettings {

	public final static int GRAPH_MAX_STAFF_INSTANCES = 10;
	public final static boolean LANDSCAPE = false;
	public static final int SELECTION_ITEM_IMAGE_QUALITY = 100;
	public static final int SELECTION_ITEM_IMAGE_DPI = 600;
	public static final int SELECTION_ITEM_IMAGE_WIDTH = 12;
	public static final int SELECTION_ITEM_IMAGE_HEIGHT = 12;
	public static final Color SELECTION_ITEM_IMAGE_BG_COLOR = null;
	public static final float PAGE_UPPER_MARGIN = 70.0f;
	public static final float PAGE_LOWER_MARGIN = 60.0f;
	public static final float PAGE_LEFT_MARGIN = 60.0f;
	public static final float PAGE_RIGHT_MARGIN = 60.0f;
	public static final float Y_NAME_INDENT = 10.0f;
	public static final float Y_STAFF_TAGS_INDENT = 10.0f;
	public static final float X_STAFF_TAGS_INDENT = 10.0f;
	public static final float X_STAFF_TAG_NAME_WIDTH = 160.0f;
	public final static float X_SECTION_TABLE_COLUMN_COURSE_STOP_WIDTH = 67.0f;
	public final static float X_SECTION_TABLE_COLUMN_COURSE_TRIAL_WIDTH = 120.0f;
	public final static float X_SECTION_TABLE_COLUMN_COURSE_INSTITUTION_WIDTH = 120.0f;
	public final static float X_SECTION_TABLE_COLUMN_COURSE_CERTIFICATE_WIDTH = 30.0f;
	public final static float Y_SECTION_TABLE_HEAD_FRAME_INDENT = 3.0f;
	public final static float X_SECTION_TABLE_FRAME_INDENT = 3.0f;
	public final static float Y_SECTION_TABLE_FRAME_INDENT = 3.0f;
	public final static Color SECTION_TABLE_TEXT_COLOR = Color.BLACK;
	public final static Color SECTION_TABLE_FRAME_COLOR = Color.BLACK;
	public final static float SECTION_TABLE_FRAME_LINE_WIDTH = 0.8f;
	public final static float X_FRAME_INDENT = 3.0f;
	public final static float Y_FRAME_INDENT = 3.0f;
	public final static float SPACER_HEIGHT = 20.0f;
	public static final float BLOCK_FRAME_LINE_WIDTH = 0.8f;
	public static final float BLOCKS_UPPER_MARGIN = PAGE_UPPER_MARGIN + Y_FRAME_INDENT;
	public static final float BLOCKS_LOWER_MARGIN = PAGE_LOWER_MARGIN + Y_FRAME_INDENT + 10.0f;
	public static final float BLOCKS_LEFT_MARGIN = PAGE_LEFT_MARGIN + X_FRAME_INDENT;
	public static final float BLOCKS_RIGHT_MARGIN = PAGE_RIGHT_MARGIN + X_FRAME_INDENT;
	public static final float Y_FRAME_UPPER_INDENT_SIGNATURE = 38.0f;
	public static final float Y_FRAME_LOWER_INDENT_SIGNATURE = 3.0f;
	public static final float X_FRAME_INDENT_SIGNATURE = 7.0f;
	public static final float SIGNATURE_LINE_WIDTH = 0.3f;
	public static final float SIGNATURE_LINE_LENGTH = 175.0f;
	public static final float Y_OFFSET_SIGNATURE_ANNOTATION = 2.0f;
	public static final float DATE_LABEL_WIDTH = 35.0f;
	public static final float DATE_LINE_LENGTH = 100.0f;
	public static final String COURSE_STOP_DATE_PATTERN = "d.M.yyyy";
	public static final String SIGNATURE_DATE_PATTERN = "d. MMMM yyyy";
	public static final Color TEXT_COLOR = Color.BLACK;
	public static final Color FRAME_COLOR = Color.BLACK;
	public static final boolean SHOW_PAGE_NUMBERS = true;
	public static final boolean SECTION_NEW_PAGE_PER_TRIAL = false;
	public static final boolean SECTION_NEW_PAGE_PER_COURSE = false;
	public static final boolean STAFF_SIGNATURE = true;
	public static final boolean LAST_INSTITUTION_SIGNATURE = false;
	public static final boolean SIGNATURE_PER_PAGE = true;
	public static final boolean PASSED_COURSES_ONLY = true;
	public static final String PAINTER_CLASS = null;
	public static final boolean INSTITUTION_STAFF_PATH = true;
	public static final ArrayList<String> PAINTER_SOURCE_FILES = null;
	public static final boolean DATE_USER_TIME_ZONE = true;

	private TrainingRecordPDFDefaultSettings() {
	}
}
