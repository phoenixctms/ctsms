package org.phoenixctms.ctsms.util.randomization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.RandomizationListCodeDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;
import org.phoenixctms.ctsms.vo.TrialInVO;

public class TagSelectListRandomization extends Randomization {

	protected TagSelectListRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao,
			RandomizationListCodeDao randomizationListCodeDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao, randomizationListCodeDao);
	}

	@Override
	protected void checkProbandListEntryTagField(Trial trial, InputField field) throws ServiceException {
		if (!ServiceUtil.isInputFieldTypeSelectOne(field.getFieldType())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_FIELD_NOT_SELECT_ONE);
		}
	}

	@Override
	protected void checkStratificationRandomizationListRandomizationListInput(Trial trial, StratificationRandomizationListInVO randomizationListIn) throws ServiceException {
		checkRandomizeProbandListEntryTag(trial);
		splitInputFieldSelectionSetValueValues(randomizationListIn.getRandomizationList(),
				getInputFieldSelectionSetValueValueMap(getRandomizationInputFieldSelectionSetValues(trial)), null);
	}

	@Override
	protected void checkTrialRandomizationInput(Trial trial, TrialInVO trialIn) throws ServiceException {
		if (CommonUtil.isEmptyString(trialIn.getRandomizationList())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_RANDOMIZATION_LIST_REQUIRED);
		}
		if (trial != null) {
			splitInputFieldSelectionSetValueValues(trialIn.getRandomizationList(), getInputFieldSelectionSetValueValueMap(getRandomizationInputFieldSelectionSetValues(trial)),
					null);
		}
	}

	@Override
	protected TreeSet<String> getRandomizationListItems(Trial trial) throws Exception {
		return getRandomizationListInputFieldSelectionSetValueValues(trial);
	}

	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.TAG_SELECT_LIST;
	}

	private int getTotalValuesSize(Long excludeListEntryId, Collection<InputFieldSelectionSetValue> values) {
		int result = 0;
		Iterator<InputFieldSelectionSetValue> it = values.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValue value = it.next();
			result += CommonUtil.safeLongToInt(probandListEntryDao.getTrialRandomizeSelectStratificationTagValuesCount(null, value.getId(), null, excludeListEntryId));
		}
		return result;
	}

	@Override
	public RandomizationType getType() {
		return RandomizationType.TAG_SELECT;
	}

	@Override
	protected InputFieldSelectionSetValue randomizeInputFieldSelectionSetValue(Trial trial, ProbandListEntry exclude) throws Exception {
		Collection<InputFieldSelectionSetValue> values = getRandomizationInputFieldSelectionSetValues(trial);
		HashMap<String, InputFieldSelectionSetValue> valueMap = getInputFieldSelectionSetValueValueMap(values);
		initValuesInfo(values);
		ArrayList<String> valueValues = new ArrayList<String>();
		splitInputFieldSelectionSetValueValues(trial.getRandomizationList(), valueMap, valueValues);
		InputFieldSelectionSetValue value = null;
		if (valueValues.size() > 0) {
			int totalValuesSize = getTotalValuesSize(exclude != null ? exclude.getId() : null, values);
			String valueValue = valueValues.get(totalValuesSize % valueValues.size());
			value = valueMap.get(valueValue);
			randomizationInfo.setTotalSize(totalValuesSize);
		}
		randomizationInfo.setRandomizationListItems(valueValues);
		return value;
	}
}
