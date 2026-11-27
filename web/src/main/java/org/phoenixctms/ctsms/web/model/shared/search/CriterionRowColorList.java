package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

public class CriterionRowColorList extends CriterionListBase<String> {

	private static final String ROWS_INVALID_STYLECLASS = "ctsms-search-criterion-rows-invalid-color";
	private static final String ROWS_UNCHECKED_STYLECLASS = "ctsms-search-criterion-rows-unchecked-color";
	private static final String ROW_SELECT_COLOR_STYLECLASS_PREFIX = "ctsms-search-criterion-row-select-color-";
	private static final String ROW_SET_OPERATION_STYLECLASS = "ctsms-search-criterion-row-set-operation";
	// the result sets are colored along a gradient from light to dark green, which used to be 17 colors
	private static final int[] ROW_SELECT_COLOR_FROM = { 0xBC, 0xF2, 0xBB };
	private static final int[] ROW_SELECT_COLOR_TO = { 0x07, 0x42, 0x05 };
	private static final int ROW_SELECT_COLOR_SECTIONS = 16;
	private static final String INTERMEDIATE_SET_BLANK_STYLECLASS = "ctsms-search-intermediateset-blank-color";
	private static final String INTERMEDIATE_SET_HIGHLIGHT_STYLECLASS = "ctsms-search-intermediateset-highlight-circle-stroke-color";
	private static final String INTERMEDIATE_SET_HIGHLIGHT_DARK_STYLECLASS = "ctsms-search-intermediateset-highlight-circle-stroke-dark-color";
	private static final String INTERMEDIATE_SET_TINT_STYLECLASS_PREFIX = "ctsms-search-intermediateset-intermediate-color-";
	// from misty rose, the color of set operation rows, to a darker rose
	private static final int[] INTERMEDIATE_SET_COLOR_FROM = { 0xFF, 0xE4, 0xE1 };
	private static final int[] INTERMEDIATE_SET_COLOR_TO = { 0xEC, 0xA0, 0x98 };
	private static final int INTERMEDIATE_SET_TINTS = 8;
	private HashMap<Long, CriterionInstantVO> instantCriterionsMap;
	private CriteriaInstantVO instantCriteria;
	private boolean parsed;
	private int gradeStep;

	public CriterionRowColorList(ArrayList<CriterionInVO> criterionsIn, CriteriaInstantVO instantCriteria, HashMap<Long, CriterionInstantVO> instantCriterionsMap, boolean parsed) {
		super(criterionsIn);
		this.instantCriteria = (instantCriteria == null ? new CriteriaInstantVO() : instantCriteria);
		this.instantCriterionsMap = (instantCriterionsMap == null ? new HashMap<Long, CriterionInstantVO>() : instantCriterionsMap);
		this.parsed = parsed;
		this.gradeStep = getGradeStep();
	}

	@Override
	public String get(int index) {
		if (parsed) {
			CriterionInVO criterionIn = getCriterionIn(index);
			CriterionInstantVO instantCriterion = getInstantCriterion(criterionIn);
			if (instantCriterion != null) {
				Integer selectIndex = instantCriterion.getSelectStatementIndex();
				Integer selectCount = instantCriteria.getSelectStatementCount();
				if (selectIndex != null) {
					return ROW_SELECT_COLOR_STYLECLASS_PREFIX + Integer.toString(getSelectColorGrade(selectIndex.intValue(), selectCount, gradeStep));
				} else if (selectCount != null && selectCount > 0) {
					return ROW_SET_OPERATION_STYLECLASS;
				}
			}
			return ROWS_INVALID_STYLECLASS;
		} else {
			return ROWS_UNCHECKED_STYLECLASS;
		}
	}

	/**
	 * The grades between two of the 17 former colors, so the gradient has a grade for each result set a query of max_criterions can have:
	 * each result set takes at least one criterion, and a set operator between two of them takes one more.
	 */
	static int getGradeStep() {
		int maxCriterions = Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS);
		int maxSelects = (Math.max(maxCriterions, 1) + 1) / 2;
		return Math.max(1, (maxSelects - 1 + ROW_SELECT_COLOR_SECTIONS - 1) / ROW_SELECT_COLOR_SECTIONS);
	}

	/**
	 * Up to 17 result sets keep the 17 former colors. More result sets spread evenly over the whole gradient, so none share a color.
	 */
	static int getSelectColorGrade(int selectIndex, Integer selectCount, int gradeStep) {
		int last = ROW_SELECT_COLOR_SECTIONS * gradeStep;
		int count = selectCount == null ? 0 : selectCount.intValue();
		if (count <= ROW_SELECT_COLOR_SECTIONS + 1) {
			return Math.min(selectIndex * gradeStep, last);
		}
		return Math.min((int) Math.round((double) selectIndex * last / (count - 1)), last);
	}

	/**
	 * The style classes of all grades, which searchInput.xhtml adds to the page. The intermediate sets chart reads the colors from them, too,
	 * and from the chart classes derived from the same gradient: the blank chart has the first grade, the hover stroke the middle grade,
	 * or the first grade on a dark circle.
	 */
	public static String getStyles() {
		int gradeStep = getGradeStep();
		int last = ROW_SELECT_COLOR_SECTIONS * gradeStep;
		StringBuilder styles = new StringBuilder();
		for (int grade = 0; grade <= last; grade++) {
			appendStyle(styles, ROW_SELECT_COLOR_STYLECLASS_PREFIX + grade, gradeColor(ROW_SELECT_COLOR_FROM, ROW_SELECT_COLOR_TO, grade, last));
		}
		appendStyle(styles, INTERMEDIATE_SET_BLANK_STYLECLASS, gradeColor(ROW_SELECT_COLOR_FROM, ROW_SELECT_COLOR_TO, 0, last));
		appendStyle(styles, INTERMEDIATE_SET_HIGHLIGHT_STYLECLASS, gradeColor(ROW_SELECT_COLOR_FROM, ROW_SELECT_COLOR_TO, last / 2, last));
		appendStyle(styles, INTERMEDIATE_SET_HIGHLIGHT_DARK_STYLECLASS, gradeColor(ROW_SELECT_COLOR_FROM, ROW_SELECT_COLOR_TO, 0, last));
		// the sets combined by an earlier step: tints of misty rose, in an order that sets neighbouring steps apart
		for (int tint = 0; tint < INTERMEDIATE_SET_TINTS; tint++) {
			int grade = (tint % 2 == 0 ? tint / 2 : (INTERMEDIATE_SET_TINTS + tint) / 2);
			appendStyle(styles, INTERMEDIATE_SET_TINT_STYLECLASS_PREFIX + tint, gradeColor(INTERMEDIATE_SET_COLOR_FROM, INTERMEDIATE_SET_COLOR_TO, grade, INTERMEDIATE_SET_TINTS - 1));
		}
		return styles.toString();
	}

	private static String gradeColor(int[] from, int[] to, int grade, int last) {
		StringBuilder color = new StringBuilder("#");
		for (int i = 0; i < from.length; i++) {
			// rounded down like the former colors, which are the grades at every gradeStep
			int value = from[i] + (last == 0 ? 0 : Math.floorDiv((to[i] - from[i]) * grade, last));
			color.append(String.format("%02X", value));
		}
		return color.toString();
	}

	private static void appendStyle(StringBuilder styles, String styleClass, String color) {
		// on one line and without quotes, as the page puts it into a script string
		styles.append('.').append(styleClass).append("{background-color:").append(color).append("} ");
	}

	private CriterionInstantVO getInstantCriterion(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : instantCriterionsMap.get(criterionVO.getPosition()));
	}
}
