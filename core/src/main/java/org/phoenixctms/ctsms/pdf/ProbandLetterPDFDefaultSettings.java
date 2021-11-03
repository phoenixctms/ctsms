package org.phoenixctms.ctsms.pdf;

import java.util.ArrayList;

import org.phoenixctms.ctsms.enumeration.Color;

public final class ProbandLetterPDFDefaultSettings {

	public static final int GRAPH_MAX_PROBAND_INSTANCES = 1;
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
	public static final float BODY_SALUTATION_Y = 350.0f;
	public static final Color TEXT_COLOR = Color.BLACK;
	public static final boolean SHOW_PAGE_NUMBERS = true;
	public static final float PROBAND_ID_Y = 120.0f;
	public static final float ADDRESS_Y = 680.0f;
	public static final String PAINTER_CLASS = null;
	public static final ArrayList<String> PAINTER_SOURCE_FILES = null;
	public static final boolean DATE_USER_TIME_ZONE = true;

	private ProbandLetterPDFDefaultSettings() {
	}
}
