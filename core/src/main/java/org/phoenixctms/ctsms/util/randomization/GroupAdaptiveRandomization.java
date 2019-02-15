package org.phoenixctms.ctsms.util.randomization;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.phoenixctms.ctsms.compare.EntityIDComparator;
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

public class GroupAdaptiveRandomization extends Randomization {

	// https://www.phoenixctms.org/features/modules/trials/ecrf/
	// p = (1 + $probandGroups[1].size) /
	// (1 + $probandGroups[0].size + 1 + $probandGroups[1].size);
	// 2 GROUPS:
	//
	// p0a: 0 + 1 / 0 + 2 = 1 / 2
	// p0b: 0 + 1 / 0 + 2 = 1 / 2
	// ->assigned to b
	//
	// p1a: 1 + 1 / 1 + 2 = 2 / 3
	// p1b: 0 + 1 / 1 + 2 = 1 / 3
	// ->assigned to b
	//
	// p2a: 2 + 1 / 2 + 2 = 3 / 4
	// p2b: 0 + 1 / 2 + 2 = 1 / 4
	// ->...

	// 3 GROUPS:
	//
	// p0a: 0 + 1 / 0 + 3 = 1 / 3
	// p0b: 0 + 1 / 0 + 3 = 1 / 3
	// p0c: 0 + 1 / 0 + 3 = 1 / 3
	// ->assigned to b
	//
	// p1a: 1 + 1 / 2 + 3 = 2 / 5
	// p1b: 0 + 1 / 2 + 3 = 1 / 5
	// p1c: 1 + 1 / 2 + 3 = 2 / 5
	// ->assigned to b ->assigned to a
	//
	// p2a: 2 + 1 / 4 + 3 = 3 / 7 p2a: 1 + 1 / 4 + 3 = 2 / 7
	// p2b: 0 + 1 / 4 + 3 = 1 / 7 p2b: 1 + 1 / 4 + 3 = 2 / 7
	// p2c: 2 + 1 / 4 + 3 = 3 / 7 p2c: 2 + 1 / 4 + 3 = 3 / 7
	// ->...
	private final static EntityIDComparator ID_COMPARATOR = new EntityIDComparator<ProbandGroup>(false);

	private final static long getOtherGroupsSize(long groupId, Set<Entry<Long, Long>> groupSizes) {
		Iterator<Entry<Long, Long>> it = groupSizes.iterator();
		long otherGroupsSize = 0l;
		while (it.hasNext()) {
			Entry<Long, Long> groupSize = it.next();
			if (groupId != groupSize.getKey()) {
				otherGroupsSize += groupSize.getValue();
			}
		}
		return otherGroupsSize;
	}

	protected GroupAdaptiveRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
		// TODO Auto-generated constructor stub
	}









	private HashMap<Long, Long> getGroupSizes(Long excludeListEntryId, Collection<ProbandGroup> probandGroups) { // long trialId,
		HashMap<Long, Long> result = new HashMap<Long, Long>();
		Iterator<ProbandGroup> it = probandGroups.iterator();
		while (it.hasNext()) {
			ProbandGroup group = it.next();
			result.put(group.getId(), probandListEntryDao.getTrialGroupStratificationTagValuesCount(null, group.getId(), null, excludeListEntryId));
		}
		// result.put(null, probandListEntryDao.getTrialGroupCount(trialId, null, excludeListEntryId));
		return result;
	}

	private ProbandGroup getRandomGroup(Trial trial, TreeMap<ProbandGroup, Double> pMap) throws Exception {
		if (pMap.size() > 0) {
			double r = getRandom(trial).nextDouble();
			Iterator<Entry<ProbandGroup, Double>> it = pMap.entrySet().iterator();
			double pFrom = 0.0d;
			double pTo;
			while (it.hasNext()) {
				Entry<ProbandGroup, Double> p = it.next();
				pTo = pFrom + p.getValue();
				if (r >= pFrom && r < pTo) {
					return p.getKey();
				}
				pFrom = pTo;
			}
		}
		return null;
	}



	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.GROUP_ADAPTIVE;
	}




	@Override
	protected ProbandGroup randomizeProbandGroup(Trial trial, ProbandListEntry exclude) throws Exception {
		Collection<ProbandGroup> probandGroups = getRandomizationGroups(trial);
		HashMap<Long, ProbandGroup> probandGroupMap = getProbandGroupIdMap(probandGroups);
		Set<Entry<Long, Long>> groupSizes = getGroupSizes(exclude != null ? exclude.getId() : null, probandGroups).entrySet(); // trialId
		Iterator<Entry<Long, Long>> it = groupSizes.iterator();
		long dividend = 0l;
		HashMap<Long, Long> divisors = new HashMap<Long, Long>();
		randomizationInfo.setSizes(new HashMap<String, Long>());
		while (it.hasNext()) {
			Entry<Long, Long> groupSize = it.next();
			long divisor = getOtherGroupsSize(groupSize.getKey(), groupSizes) + 1l;
			divisors.put(groupSize.getKey(), divisor);
			dividend += divisor;
			randomizationInfo.getSizes().put(probandGroupMap.get(groupSize.getKey()).getToken(), groupSize.getValue());
		}
		it = groupSizes.iterator();
		TreeMap<ProbandGroup, Double> pMap = new TreeMap<ProbandGroup, Double>(ID_COMPARATOR);
		while (it.hasNext()) {
			Entry<Long, Long> groupSize = it.next();
			// double p = (1.0d + ((double) otherGroupSizes.get(it.next().getKey()))) / (((double) groupSizes.size()) + ((double) d));
			double p = ((double) divisors.get(groupSize.getKey())) / ((double) dividend);
			pMap.put(probandGroupMap.get(groupSize.getKey()), p);
		}
		ProbandGroup group = getRandomGroup(trial, pMap);
		saveRandom(trial);
		return group;
	}
}
