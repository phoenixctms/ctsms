package org.phoenixctms.ctsms.pdf;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;

public final class InquiriesPDFDefaultSettings {

	public static final boolean SHOW_PAGE_NUMBERS = true;
	public final static boolean LANDSCAPE = false;
	public static final String PROBAND_DATE_OF_BIRTH_DATE_PATTERN = "d. MMMM yyyy";
	public static final String CONTENT_TIMESTAMP_DATETIME_PATTERN = "d. MMMM yyyy HH:MM";
	public static final String MODIFIED_TIMESTAMP_PATTERN = "d. MMMM yyyy HH:MM";
	public static final String DECIMAL_SEPARATOR = null;
	public static final float PAGE_UPPER_MARGIN = 70.0f;
	public static final float PAGE_LOWER_MARGIN = 60.0f;
	public static final float PAGE_LEFT_MARGIN = 60.0f;
	public static final float PAGE_RIGHT_MARGIN = 60.0f;
	public static final float BLOCKS_UPPER_MARGIN = PAGE_UPPER_MARGIN;
	public static final float BLOCKS_LOWER_MARGIN = PAGE_LOWER_MARGIN;
	public static final float BLOCKS_LEFT_MARGIN = PAGE_LEFT_MARGIN;
	public static final float BLOCKS_RIGHT_MARGIN = PAGE_RIGHT_MARGIN;
	public static final float MULTI_LINE_TEXT_MIN_HEIGHT = 50.0f;
	public static final float FIELD_FRAME_LINE_WIDTH = 1.0f;
	public static final float VALUE_FRAME_LINE_WIDTH = 0.5f;
	public static final float INDEX_FRAME_LINE_WIDTH = 0.75f;
	public static final float HEAD_FRAME_LINE_WIDTH = 0.75f;
	public final static float PAGE_TITLE_Y = 789.0f;
	public final static float Y_HEADLINE_INDENT = 25.0f;
	public final static float X_HEAD_COLUMN_INDENT = 50.0f;
	public final static float X_HEAD_COLUMN1_INDENT = 80.0f;
	public final static float X_FIELD_COLUMN_INDENT = 200.0f;
	public final static float X_FRAME_INDENT = 4.0f;
	public final static float Y_FRAME_INDENT = 4.0f;
	public final static float X_VALUE_FRAME_INDENT = X_FRAME_INDENT - 2.0f;
	public final static float Y_VALUE_FRAME_INDENT = Y_FRAME_INDENT - 2.0f;
	public final static float X_BOX_FRAME_INDENT = 6.0f;
	public final static float Y_BOX_FRAME_INDENT = 6.0f;
	public static final Color TEXT_COLOR = Color.BLACK;
	public static final Color PRESET_TEXT_COLOR = Color.GAINSBORO;
	public static final Color FRAME_COLOR = Color.BLACK;
	public static final Color VALIDATION_ERROR_MESSAGE_TEXT_COLOR = Color.INDIANRED;
	public final static String DATE_VALUE_PATTERN = "yyyy/MM/dd";
	public final static String TIMESTAMP_VALUE_PATTERN = "yyyy/MM/dd HH:mm";
	public final static String TIME_VALUE_PATTERN = "HH:mm";
	public final static String FLOAT_VALUE_PATTERN = "#.######";
	public final static String INTEGER_VALUE_PATTERN = "######";
	public static final int SELECTION_ITEM_IMAGE_QUALITY = 100;
	public static final int SELECTION_ITEM_IMAGE_DPI = 600;
	public static final int SELECTION_ITEM_IMAGE_WIDTH = 12;
	public static final int SELECTION_ITEM_IMAGE_HEIGHT = 12;
	public static final Color SELECTION_ITEM_IMAGE_BG_COLOR = null;
	public final static Integer SIGNATURE_VALUE_LENGTH = null;
	public static final int SIGNATURE_IMAGE_QUALITY = 100;
	public static final int SIGNATURE_IMAGE_DPI = 300;
	public static final int SIGNATURE_IMAGE_WIDTH = 50;
	public static final int SIGNATURE_IMAGE_HEIGHT = 30;
	public static final Color SIGNATURE_IMAGE_BG_COLOR = null;
	public static final Float HORIZONTAL_SELECTION_ITEM_WIDTH = null;
	public static final float X_SELECTION_ITEM_INDENT = 10.0f;
	public static final float X_SELECTION_ITEM_IMAGE_LABEL_OFFSET = 4.0f;
	public static final float Y_SELECTION_ITEM_INDENT = 4.0f;
	public static final float Y_SELECTION_ITEM_IMAGE_LABEL_OFFSET = 1.0f;
	public static final boolean SHOW_PRESET_VALUES = true;
	public static final int SKETCH_IMAGE_QUALITY = 100;
	public static final int SKETCH_IMAGE_DPI = 144;
	public static final Color SKETCH_IMAGE_BG_COLOR = null;
	public static final boolean RENDER_SKETCH_IMAGES = true;
	public static final boolean SHOW_SKETCH_REGIONS = false;
	public static final int LONG_TITLE_LENGTH = 36;
	public static final boolean SHOW_MODIFIED_LABEL = false;
	public static final String PAINTER_CLASS = null;
	public static final ArrayList<String> PAINTER_SOURCE_FILES = null;
	public static final boolean DATE_TIME_USER_TIME_ZONE = true;

	private InquiriesPDFDefaultSettings() {
	}
}
