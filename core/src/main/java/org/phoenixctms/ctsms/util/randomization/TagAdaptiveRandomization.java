package org.phoenixctms.ctsms.util.randomization;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.phoenixctms.ctsms.compare.ComparatorFactory;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;

public class TagAdaptiveRandomization extends Randomization {

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
	private final static Comparator<InputFieldSelectionSetValue> ID_COMPARATOR = ComparatorFactory.createNullSafe(InputFieldSelectionSetValue::getId);

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
	private final static long getOtherValuesSize(long valueId, Set<Entry<Long, Long>> valueSizes) {
		Iterator<Entry<Long, Long>> it = valueSizes.iterator();
		long otherValuesSize = 0l;
		while (it.hasNext()) {
			Entry<Long, Long> valueSize = it.next();
			if (valueId != valueSize.getKey()) {
				otherValuesSize += valueSize.getValue();
			}
		}
		return otherValuesSize;
	}

	protected TagAdaptiveRandomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		super(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao, inputFieldSelectionSetValueDao,
				probandListEntryTagValueDao);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RandomizationMode getRandomizationMode() {
		return RandomizationMode.GROUP_ADAPTIVE;
	}

	private InputFieldSelectionSetValue getRandomValue(Trial trial, TreeMap<InputFieldSelectionSetValue, Double> pMap) throws Exception {
		if (pMap.size() > 0) {
			double r = getRandom(trial).nextDouble();
			Iterator<Entry<InputFieldSelectionSetValue, Double>> it = pMap.entrySet().iterator();
			double pFrom = 0.0d;
			double pTo;
			while (it.hasNext()) {
				Entry<InputFieldSelectionSetValue, Double> p = it.next();
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
	public RandomizationType getType() {
		return RandomizationType.TAG_SELECT;
	}

	private HashMap<Long, Long> getValueSizes(Long excludeListEntryId, Collection<InputFieldSelectionSetValue> values) { // long trialId,
		HashMap<Long, Long> result = new HashMap<Long, Long>();
		Iterator<InputFieldSelectionSetValue> it = values.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValue value = it.next();
			result.put(value.getId(), probandListEntryDao.getTrialRandomizeSelectStratificationTagValuesCount(null, value.getId(), null, excludeListEntryId));
		}
		// result.put(null, probandListEntryDao.getTrialGroupCount(trialId, null, excludeListEntryId));
		return result;
	}

	@Override
	protected InputFieldSelectionSetValue randomizeInputFieldSelectionSetValue(Trial trial, ProbandListEntry exclude) throws Exception {
		Collection<InputFieldSelectionSetValue> values = getRandomizationInputFieldSelectionSetValues(trial);
		HashMap<Long, InputFieldSelectionSetValue> valueMap = getInputFieldSelectionSetValueIdMap(values);
		Set<Entry<Long, Long>> valueSizes = getValueSizes(exclude != null ? exclude.getId() : null, values).entrySet(); // trialId
		Iterator<Entry<Long, Long>> it = valueSizes.iterator();
		long dividend = 0l;
		HashMap<Long, Long> divisors = new HashMap<Long, Long>();
		randomizationInfo.setSizes(new HashMap<String, Long>());
		while (it.hasNext()) {
			Entry<Long, Long> valueSize = it.next();
			long divisor = getOtherValuesSize(valueSize.getKey(), valueSizes) + 1l;
			divisors.put(valueSize.getKey(), divisor);
			dividend += divisor;
			randomizationInfo.getSizes().put(valueMap.get(valueSize.getKey()).getValue(), valueSize.getValue());
		}
		it = valueSizes.iterator();
		TreeMap<InputFieldSelectionSetValue, Double> pMap = new TreeMap<InputFieldSelectionSetValue, Double>(ID_COMPARATOR);
		while (it.hasNext()) {
			Entry<Long, Long> valueSize = it.next();
			// double p = (1.0d + ((double) otherGroupSizes.get(it.next().getKey()))) / (((double) groupSizes.size()) + ((double) d));
			double p = ((double) divisors.get(valueSize.getKey())) / ((double) dividend);
			pMap.put(valueMap.get(valueSize.getKey()), p);
		}
		InputFieldSelectionSetValue value = getRandomValue(trial, pMap);
		saveRandom(trial);
		return value;
	}
}
