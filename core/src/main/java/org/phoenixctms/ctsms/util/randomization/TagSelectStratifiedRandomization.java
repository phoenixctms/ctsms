package org.phoenixctms.ctsms.util.randomization;

import java.util.TreeSet;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;

public class TagSelectStratifiedRandomization extends Randomization {


	protected TagSelectStratifiedRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao);
		// TODO Auto-generated constructor stub
	}

	protected  void checkProbandListEntryTagField(Trial trial,InputField field) throws ServiceException {
		if (!ServiceUtil.isInputFieldTypeSelectOne(field.getFieldType())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_FIELD_NOT_SELECT_ONE);
		}
	}

	// @Override
	// protected void checkProbandGroupTokenInput(Trial trial, ProbandGroupInVO probandGroupIn) throws ServiceException {
	// checkProbandGroupToken(probandGroupIn.getToken());
	// }

	@Override
	protected void checkStratificationRandomizationListRandomizationListInput(Trial trial, StratificationRandomizationListInVO randomizationListIn) throws ServiceException {
		splitInputFieldSelectionSetValueValues(randomizationListIn.getRandomizationList(),
				getInputFieldSelectionSetValueValueMap(getRandomizationInputFieldSelectionSetValues(trial)), null);
	}

	@Override
	protected TreeSet<String> getRandomizationListItems(Trial trial) throws Exception {
		return getRandomizationListInputFieldSelectionSetValueValues(trial);
	}

	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.TAG_SELECT_STRATIFIED;
	}

	@Override
	public RandomizationType getType() {
		return RandomizationType.TAG_SELECT;
	}


}
