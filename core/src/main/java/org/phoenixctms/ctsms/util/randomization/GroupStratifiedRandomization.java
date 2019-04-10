package org.phoenixctms.ctsms.util.randomization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationList;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;

public class GroupStratifiedRandomization extends Randomization {


	protected GroupStratifiedRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected void checkStratificationRandomizationListRandomizationListInput(Trial trial, StratificationRandomizationListInVO randomizationListIn) throws ServiceException {
		splitProbandGroupTokens(randomizationListIn.getRandomizationList(),
				getProbandGroupTokenMap(getRandomizationGroups(trial)),
				null);
	}

	@Override
	protected TreeSet<String> getRandomizationListItems(Trial trial) throws Exception {
		return getRandomizationListGroups(trial);
	}

	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.GROUP_STRATIFIED;
	}

	private int getTotalGroupsSize(Long excludeListEntryId, Collection<ProbandGroup> probandGroups, StratificationRandomizationList randomizationList) { // long trialId,
		int result = 0;
		Set<Long> selectionSetValueIds = getInputFieldSelectionSetValueIdMap(randomizationList.getSelectionSetValues()).keySet();
		Iterator<ProbandGroup> it = probandGroups.iterator();
		while (it.hasNext()) {
			ProbandGroup group = it.next();
			result += CommonUtil.safeLongToInt(probandListEntryDao.getTrialGroupStratificationTagValuesCount(null, group.getId(), selectionSetValueIds, excludeListEntryId));
		}
		return result;
	}

	@Override
	public RandomizationType getType() {
		return RandomizationType.GROUP;
	}

	@Override
	public ProbandGroup randomizeProbandGroup(Trial trial, ProbandListEntry exclude) throws Exception {
		Collection<ProbandGroup> probandGroups = getRandomizationGroups(trial);
		HashMap<String, ProbandGroup> probandGroupMap = getProbandGroupTokenMap(probandGroups);
		initGroupsInfo(probandGroups);
		ArrayList<String> tokens = new ArrayList<String>();
		StratificationRandomizationList randomizationList = getStratificationRandomizationList(trial, exclude);
		splitProbandGroupTokens(randomizationList.getRandomizationList(), probandGroupMap, tokens);
		ProbandGroup group = null;
		if (tokens.size() > 0) {
			int totalGroupsSize = getTotalGroupsSize(exclude != null ? exclude.getId() : null, probandGroups, randomizationList);
			String token = tokens.get(totalGroupsSize % tokens.size());
			group = probandGroupMap.get(token);
			randomizationInfo.setTotalSize(totalGroupsSize);
		}
		randomizationInfo.setRandomizationListItems(tokens);
		initStratificationValuesInfo(randomizationList);
		return group;
	}


}
