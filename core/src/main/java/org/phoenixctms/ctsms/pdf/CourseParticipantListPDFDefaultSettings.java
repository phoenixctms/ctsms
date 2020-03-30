package org.phoenixctms.ctsms.pdf;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;

public final class CourseParticipantListPDFDefaultSettings {

	public final static int GRAPH_MAX_COURSE_INSTANCES = 1;
	public final static boolean LANDSCAPE = true;
	public static final int BOOKING_COLUMN_COUNT = 3;
	public final static float X_COLUMN_INDENT = 200.0f;
	public final static float X_FRAME_INDENT = 2.5f;
	public final static float Y_FRAME_INDENT = 2.5f;
	public final static float SPACER_HEIGHT = 20.0f;
	public static final float PAGE_FRAME_LINE_WIDTH = 2.0f;
	public final static float X_PARTICIPANT_TABLE_COLUMN_PARTICIPANT_WIDTH = 230.0f;
	public final static float X_PARTICIPANT_TABLE_FRAME_INDENT = 10.0f;
	public final static float Y_PARTICIPANT_TABLE_FRAME_INDENT = 10.0f;
	public final static float Y_PARTICIPANT_TABLE_HEAD_FRAME_INDENT = 3.0f;
	public static final float PARTICIPANT_TABLE_FRAME_LINE_WIDTH = 1.5f;
	public static final float PARTICIPANT_TABLE_COLUMN_LINE_WIDTH = 0.8f;
	public static final float PAGE_UPPER_MARGIN = 70.0f;
	public static final float PAGE_LOWER_MARGIN = 60.0f;
	public static final float PAGE_LEFT_MARGIN = 60.0f;
	public static final float PAGE_RIGHT_MARGIN = 60.0f;
	public static final float BLOCKS_UPPER_MARGIN = PAGE_UPPER_MARGIN + Math.max(Y_FRAME_INDENT, Y_PARTICIPANT_TABLE_FRAME_INDENT);
	public static final float BLOCKS_LOWER_MARGIN = PAGE_LOWER_MARGIN + Math.max(Y_FRAME_INDENT, Y_PARTICIPANT_TABLE_FRAME_INDENT) + 10.0f;
	public static final float BLOCKS_LEFT_MARGIN = PAGE_LEFT_MARGIN + Math.max(X_FRAME_INDENT, X_PARTICIPANT_TABLE_FRAME_INDENT);
	public static final float BLOCKS_RIGHT_MARGIN = PAGE_RIGHT_MARGIN + Math.max(X_FRAME_INDENT, X_PARTICIPANT_TABLE_FRAME_INDENT);
	public static final String NOW_DATE_TIME_PATTERN = "d. MMMM yyyy";
	public static final String BOOKING_DATE_TIME_PATTERN = "d. MMMM yyyy HH:mm";
	public static final String COURSE_DATE_TIME_PATTERN = "d. MMMM yyyy";
	public static final Color TEXT_COLOR = Color.BLACK;
	public static final Color FRAME_COLOR = Color.BLACK;
	public static final Color PARTICIPANT_TABLE_TEXT_COLOR = Color.BLACK;
	public static final Color PARTICIPANT_TABLE_FRAME_COLOR = Color.DARKGREY;
	public static final float PARTICIPANT_TABLE_COLUMN_LECTURER_WIDTH = 0.0f;
	public static final boolean PARTICIPANT_TABLE_SIGNATURE_DATE_COLUMN = false;
	public static final float PARTICIPANT_TABLE_COLUMN_LECTURER_SIGNATURE_WIDTH = 110.0f;
	public static final float PARTICIPANT_TABLE_COLUMN_SIGNATURE_SIGNATURE_WIDTH = 110.0f;
	public static final int BLANK_ROWS = 10;
	public static final boolean SHOW_PAGE_NUMBERS = true;
	public static final String PAINTER_CLASS = null;
	public static final ArrayList<String> PAINTER_SOURCE_FILES = null;

	private CourseParticipantListPDFDefaultSettings() {
	}
}
