package org.phoenixctms.ctsms.util.randomization;

import java.util.Collection;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;

public class GroupCoinRandomization extends Randomization {


	protected GroupCoinRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao);
		// TODO Auto-generated constructor stub
	}



	private ProbandGroup getRandomGroup(Trial trial, Collection<ProbandGroup> groups) throws Exception {
		ProbandGroup result = null;
		if (groups.size() > 0) {
			int index = getRandom(trial).nextInt(groups.size());
			Iterator<ProbandGroup> it = groups.iterator();
			for (int i = 0; i <= index; i++) {
				result = it.next();
			}
		}
		return result;
	}



	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.GROUP_COIN;
	}

	@Override
	public RandomizationType getType() {
		return RandomizationType.GROUP;
	}


	@Override
	protected ProbandGroup randomizeProbandGroup(Trial trial, ProbandListEntry exclude) throws Exception {
		Collection<ProbandGroup> probandGroups = getRandomizationGroups(trial);
		initGroupsInfo(probandGroups);
		ProbandGroup group = getRandomGroup(trial, probandGroups);
		saveRandom(trial);
		return group;

	}
}
