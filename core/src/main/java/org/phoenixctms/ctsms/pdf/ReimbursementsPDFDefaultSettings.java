package org.phoenixctms.ctsms.pdf;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;

public final class ReimbursementsPDFDefaultSettings {

	public static final String DATE_PATTERN = "d. MMMM yyyy";
	public final static boolean LANDSCAPE = false;
	public final static float X_FRAME_INDENT = 2.5f;
	public final static float Y_FRAME_INDENT = 2.5f;
	public static final float PAGE_UPPER_MARGIN = 70.0f;
	public static final float PAGE_LOWER_MARGIN = 60.0f;
	public static final float PAGE_LEFT_MARGIN = 60.0f;
	public static final float PAGE_RIGHT_MARGIN = 60.0f;
	public static final float BLOCKS_UPPER_MARGIN = PAGE_UPPER_MARGIN + Y_FRAME_INDENT;
	public static final float BLOCKS_LOWER_MARGIN = PAGE_LOWER_MARGIN + Y_FRAME_INDENT + 10.0f;
	public static final float BLOCKS_LEFT_MARGIN = PAGE_LEFT_MARGIN + X_FRAME_INDENT;
	public static final float BLOCKS_RIGHT_MARGIN = PAGE_RIGHT_MARGIN + X_FRAME_INDENT;
	public static final float FIRST_PAGE_DATE_Y = 300.0f;
	public static final float SECOND_PAGE_DATE_Y = 300.0f;
	public static final float FIRST_PAGE_SUBJECT_Y = 320.0f;
	public static final float BODY_SALUTATION_Y = 350.0f;
	public static final float TRIAL_TITLE_Y = 300.0f;
	public static final float SECOND_PAGE_SUBJECT_Y = 320.0f;
	public static final Color TEXT_COLOR = Color.BLACK;
	public static final Color FRAME_COLOR = Color.BLACK;
	public static final boolean SHOW_PAGE_NUMBERS = true;
	public final static float SPACER_HEIGHT = 20.0f;
	public final static float X_PAYMENT_TABLE_COLUMN_PAYMENT_METHOD_WIDTH = 230.0f;
	public final static float X_PAYMENT_TABLE_FRAME_INDENT = 10.0f;
	public final static float Y_PAYMENT_TABLE_FRAME_INDENT = 10.0f;
	public final static float Y_PAYMENT_TABLE_HEAD_FRAME_INDENT = 3.0f;
	public static final float PAYMENT_TABLE_FRAME_LINE_WIDTH = 1.5f;
	public static final float PAYMENT_TABLE_COLUMN_LINE_WIDTH = 0.8f;
	public static final Color PAYMENT_TABLE_TEXT_COLOR = Color.BLACK;
	public static final Color PAYMENT_TABLE_FRAME_COLOR = Color.DARKGREY;
	public static final float PAYMENT_TABLE_AMOUNT_COLUMN_WIDTH = 0.0f;
	public static final float Y_FRAME_UPPER_INDENT_SIGNATURE = 38.0f;
	public static final float Y_FRAME_LOWER_INDENT_SIGNATURE = 3.0f;
	public static final float X_FRAME_INDENT_SIGNATURE = 7.0f;
	public static final float SIGNATURE_LINE_WIDTH = 0.3f;
	public static final float SIGNATURE_LINE_LENGTH = 175.0f;
	public static final float Y_OFFSET_SIGNATURE_ANNOTATION = 2.0f;
	public static final float DATE_LABEL_WIDTH = 35.0f;
	public static final float DATE_LINE_LENGTH = 100.0f;
	public static final String SIGNATURE_DATE_PATTERN = "d. MMMM yyyy";
	public static final float Y_TRIAL_TAGS_INDENT = 10.0f;
	public static final float X_TRIAL_TAGS_INDENT = 10.0f;
	public static final float X_TRIAL_TAG_NAME_WIDTH = 160.0f;
	public static final int COPIES = 1;
	public static final float ADDRESS_Y = 680.0f;
	public static final float PAYMENT_TABLE_ROW_HEIGHT_LIMIT = 220.0f;
	public final static float Y_TOTALS_INDENT = 10.0f;
	public static final String PAINTER_CLASS = null;
	public static final ArrayList<String> PAINTER_SOURCE_FILES = null;
	public static final boolean DATE_USER_TIME_ZONE = true;

	private ReimbursementsPDFDefaultSettings() {
	}
}
