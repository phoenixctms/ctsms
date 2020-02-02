package org.phoenixctms.ctsms.util.randomization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValue;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.RandomizationListCode;
import org.phoenixctms.ctsms.domain.RandomizationListCodeDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationList;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.RandomizationListCodeInVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;

public class TagTextStratifiedRandomization extends Randomization {

	protected TagTextStratifiedRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao,
			RandomizationListCodeDao randomizationListCodeDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao, randomizationListCodeDao);
	}

	@Override
	protected void checkProbandListEntryTagField(Trial trial, InputField field) throws ServiceException {
		if (!InputFieldType.SINGLE_LINE_TEXT.equals(field.getFieldType())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_FIELD_NOT_SINGLE_LINE_TEXT);
		}
	}

	@Override
	protected void checkStratificationRandomizationListRandomizationListInput(Trial trial, StratificationRandomizationListInVO randomizationListIn) throws ServiceException {
		checkRandomizeProbandListEntryTag(trial);
	}

	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.TAG_TEXT_STRATIFIED;
	}

	private int getTotalNonEmptyTextsSize(Trial trial, Long excludeListEntryId, StratificationRandomizationList randomizationList) {
		Set<Long> selectionSetValueIds = getInputFieldSelectionSetValueIdMap(randomizationList.getSelectionSetValues()).keySet();
		return CommonUtil.safeLongToInt(probandListEntryDao.getTrialRandomizeTextStratificationTagValuesCount(trial.getId(), false, selectionSetValueIds, excludeListEntryId));
	}

	@Override
	public RandomizationType getType() {
		return RandomizationType.TAG_TEXT;
	}

	@Override
	protected String randomizeInputFieldTextValue(Trial trial, ProbandListEntry exclude) throws Exception {
		ArrayList<String> textValues = new ArrayList<String>();
		StratificationRandomizationList randomizationList = getStratificationRandomizationList(trial, exclude);
		splitInputFieldTextValues(randomizationList.getRandomizationList(), textValues);
		String value = null;
		if (textValues.size() > 0) {
			int totalNonEmtpyTextsSize = getTotalNonEmptyTextsSize(trial, exclude != null ? exclude.getId() : null, randomizationList);
			value = textValues.get(totalNonEmtpyTextsSize % textValues.size());
			randomizationInfo.setTotalSize(totalNonEmtpyTextsSize);
		}
		randomizationInfo.setRandomizationListItems(textValues);
		initStratificationValuesInfo(randomizationList);
		return value;
	}

	@Override
	protected ArrayList<RandomizationListCodeInVO> checkRandomizationListCodesInput(
			String randomizationList, Collection<RandomizationListCodeInVO> codes) throws ServiceException {
		return sanitizeRandomizationListCodesInput(randomizationList, codes);
	}

	@Override
	public RandomizationListCode getRandomizationListCode(ProbandListEntry listEntry) throws ServiceException {
		StratificationRandomizationList randomizationList = getStratificationRandomizationList(listEntry.getTrial(), listEntry);
		ProbandListEntryTagValue tagValue = getRandomizationTagValue(listEntry);
		String code = tagValue.getValue().getStringValue();
		try {
			return randomizationListCodeDao.findByTrialRandomizationListCode(null, randomizationList.getId(), code).iterator().next();
		} catch (NoSuchElementException e) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.RANDOMIZATION_LIST_CODE_NOT_FOUND, code);
		}
	}
}
