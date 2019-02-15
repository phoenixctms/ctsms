package org.phoenixctms.ctsms.util.randomization;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;

public class TagTextStratifiedRandomization extends Randomization {


	protected TagTextStratifiedRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao);
		// TODO Auto-generated constructor stub
	}


	protected void checkProbandListEntryTagField(Trial trial, InputField field) throws ServiceException {
		if (!InputFieldType.SINGLE_LINE_TEXT.equals(field.getFieldType())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_FIELD_NOT_SINGLE_LINE_TEXT);
		}
	}




	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.TAG_TEXT_STRATIFIED;
	}
}
