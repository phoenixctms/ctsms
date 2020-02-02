package org.phoenixctms.ctsms.util.randomization;

import java.util.Collection;
import java.util.Iterator;

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

public class TagCoinRandomization extends Randomization {

	protected TagCoinRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao,
			RandomizationListCodeDao randomizationListCodeDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao, randomizationListCodeDao);
	}

	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.GROUP_COIN;
	}

	private InputFieldSelectionSetValue getRandomValue(Trial trial, Collection<InputFieldSelectionSetValue> values) throws Exception {
		InputFieldSelectionSetValue result = null;
		if (values.size() > 0) {
			int index = getRandom(trial).nextInt(values.size());
			Iterator<InputFieldSelectionSetValue> it = values.iterator();
			for (int i = 0; i <= index; i++) {
				result = it.next();
			}
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
		initValuesInfo(values);
		InputFieldSelectionSetValue value = getRandomValue(trial, values);
		saveRandom(trial);
		return value;
	}
}
