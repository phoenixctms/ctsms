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
	 * The style classes of all grades, which searchInput.xhtml adds to the page. The intermediate sets chart reads the colors from them, too.
	 */
	public static String getStyles() {
		int gradeStep = getGradeStep();
		int last = ROW_SELECT_COLOR_SECTIONS * gradeStep;
		StringBuilder styles = new StringBuilder();
		for (int grade = 0; grade <= last; grade++) {
			styles.append('.').append(ROW_SELECT_COLOR_STYLECLASS_PREFIX).append(grade).append("{background-color:#");
			for (int i = 0; i < ROW_SELECT_COLOR_FROM.length; i++) {
				// rounded down like the former colors, which are the grades at every gradeStep
				int value = ROW_SELECT_COLOR_FROM[i] + Math.floorDiv((ROW_SELECT_COLOR_TO[i] - ROW_SELECT_COLOR_FROM[i]) * grade, last);
				styles.append(String.format("%02X", value));
			}
			// on one line and without quotes, as the page puts it into a script string
			styles.append("} ");
		}
		return styles.toString();
	}

	private CriterionInstantVO getInstantCriterion(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : instantCriterionsMap.get(criterionVO.getPosition()));
	}
}
