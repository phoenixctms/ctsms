package org.phoenixctms.ctsms.executable.xls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.fileprocessors.xls.RowProcessor;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.randomization.Randomization;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeInVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeValueVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodesVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListOutVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialRandomizationListVO;
import org.springframework.beans.factory.annotation.Autowired;

public class RandomizationListCodeRowProcessor extends RowProcessor {

	private static class Stratification {

		private ArrayList<String> randomizationList;
		private ArrayList<String[]> valuesList;
		private ArrayList<Long> selectionSetValueIds;

		public Stratification() {
			randomizationList = new ArrayList<String>();
			valuesList = new ArrayList<String[]>();
		}

		public Stratification(ArrayList<Long> selectionSetValueIds) {
			this();
			this.selectionSetValueIds = selectionSetValueIds;
		}

		public ArrayList<String> getRandomizationList() {
			return randomizationList;
		}

		public ArrayList<String[]> getValuesList() {
			return valuesList;
		}

		public ArrayList<Long> getSelectionSetValueIds() {
			return selectionSetValueIds;
		}
	}

	private static final int CODE_COLUMN_INDEX = 0;
	private int codeColumnIndex;
	private LinkedHashMap<Integer, ProbandListEntryTagOutVO> stratificationTagIndexMap;
	private LinkedHashMap<Integer, String> codeValueNameIndexMap;
	private Map stratificationsMap;
	private Stratification trialStratification;
	private HashMap<Long, HashMap<String, InputFieldSelectionSetValueOutVO>> stratificationTagValueMap;
	private boolean purge;
	@Autowired
	protected TrialService trialService;

	public RandomizationListCodeRowProcessor() {
		super();
		filterDupes = false;
		acceptCommentsIndex = 0;
		stratificationTagIndexMap = new LinkedHashMap<Integer, ProbandListEntryTagOutVO>();
		codeValueNameIndexMap = new LinkedHashMap<Integer, String>();
		stratificationsMap = null;
		trialStratification = null;
		stratificationTagValueMap = null;
		purge = false;
	}

	private String getCode(String[] values) {
		return getColumnValue(values, codeColumnIndex);
	}

	public void setPurge(boolean purge) {
		this.purge = purge;
	}

	@Override
	public void init() throws Throwable {
		super.init();
		codeColumnIndex = CODE_COLUMN_INDEX;
		stratificationTagIndexMap.clear();
		codeValueNameIndexMap.clear();
		stratificationsMap = null;
		trialStratification = null;
		stratificationTagValueMap = null;
	}

	@Override
	protected int lineHashCode(String[] values) {
		HashCodeBuilder hb = new HashCodeBuilder(1249046965, -82296885);
		Iterator<Integer> stratificationTagIndexIt = stratificationTagIndexMap.keySet().iterator();
		while (stratificationTagIndexIt.hasNext()) {
			hb.append(getColumnValue(values, stratificationTagIndexIt.next()));
		}
		hb.append(getCode(values));
		return hb.toHashCode();
	}

	private RandomizationListCodeInVO valuesToCodeInVO(String[] values) {
		RandomizationListCodeInVO in = new RandomizationListCodeInVO();
		in.setCode(getCode(values));
		Iterator<Integer> it = codeValueNameIndexMap.keySet().iterator();
		while (it.hasNext()) {
			RandomizationListCodeValueVO value = new RandomizationListCodeValueVO();
			Integer index = it.next();
			value.setValue(getColumnValue(values, index));
			value.setBlinded(true); //all of them are secret for now
			value.setName(codeValueNameIndexMap.get(index));
			in.getValues().add(value);
		}
		return in;
	}

	private void appendCode(Stratification stratification, StringBuilder randomizationList, RandomizationListCodesVO codes) {
		Iterator<String> randomizationListIt = stratification.getRandomizationList().iterator();
		Iterator<String[]> valuesListIt = stratification.getValuesList().iterator();
		while (randomizationListIt.hasNext()) {
			if (randomizationList.length() > 0) {
				randomizationList.append(Randomization.RANDOMIZATION_BLOCK_SPLIT_SEPARATOR);
			}
			randomizationList.append(randomizationListIt.next());
			codes.getCodes().add(valuesToCodeInVO(valuesListIt.next()));
		}
	}

	private void addUpdateStratificationRandomizationLists(Map stratificationsSubMap, LinkedList<String> stratificationTagValues, HashSet<Long> stratificationListIds)
			throws Exception {
		Iterator<String> it = stratificationsSubMap.keySet().iterator();
		while (it.hasNext()) {
			String stratificationTagValue = it.next();
			stratificationTagValues.addLast(stratificationTagValue);
			Object value = stratificationsSubMap.get(stratificationTagValue);
			if (value instanceof Stratification) {
				Stratification stratification = (Stratification) value;
				StringBuilder randomizationList = new StringBuilder();
				RandomizationListCodesVO codes = new RandomizationListCodesVO();
				appendCode(stratification, randomizationList, codes);
				StratificationRandomizationListInVO in = new StratificationRandomizationListInVO();
				in.setRandomizationList(randomizationList.toString());
				in.setTrialId(context.getEntityId());
				in.setSelectionSetValueIds(stratification.getSelectionSetValueIds());
				StratificationRandomizationListOutVO out = trialService.addUpdateStratificationRandomizationList(context.getAuth(), in, codes, false);
				jobOutput.println("stratification randomization list " + getStratificationrandomizationListName(out) + ": " + codes.getCodes().size() + " codes");
				stratificationListIds.add(out.getId());
			} else {
				addUpdateStratificationRandomizationLists((Map) value, stratificationTagValues, stratificationListIds);
			}
			stratificationTagValues.removeLast();
		}
	}

	@Override
	public void postProcess() throws Exception {
		if (stratificationsMap != null) {
			HashSet<Long> stratificationListIds = new HashSet<Long>();
			addUpdateStratificationRandomizationLists(stratificationsMap, new LinkedList<String>(), stratificationListIds);
			if (purge) {
				Iterator<StratificationRandomizationListOutVO> it = trialService.getStratificationRandomizationListList(context.getAuth(), context.getEntityId(),
						null).iterator();
				while (it.hasNext()) {
					StratificationRandomizationListOutVO out = it.next();
					if (!stratificationListIds.contains(out.getId())) {
						out = trialService.deleteStratificationRandomizationList(context.getAuth(), out.getId());
						jobOutput.println("stratification randomization list " + getStratificationrandomizationListName(out) + " removed");
					}
				}
				TrialRandomizationListVO trialRandomizationList = trialService.getTrialRandomizationList(context.getAuth(), context.getEntityId());
				if (trialRandomizationList.getCodeCount() > 0l) {
					TrialInVO trialIn = new TrialInVO();
					CommonUtil.copyTrialOutToIn(trialIn, trialService.getTrial(context.getAuth(), context.getEntityId()));
					RandomizationListCodesVO codes = new RandomizationListCodesVO();
					TrialOutVO out = trialService.updateTrial(context.getAuth(), trialIn, codes, true);
					jobOutput.println("trial randomization list removed");
				}
			}
		} else if (trialStratification != null) {
			TrialInVO trialIn = new TrialInVO();
			CommonUtil.copyTrialOutToIn(trialIn, trialService.getTrial(context.getAuth(), context.getEntityId()));
			StringBuilder randomizationList = new StringBuilder();
			RandomizationListCodesVO codes = new RandomizationListCodesVO();
			appendCode(trialStratification, randomizationList, codes);
			trialIn.setRandomizationList(randomizationList.toString());
			TrialOutVO out = trialService.updateTrial(context.getAuth(), trialIn, codes, false);
			jobOutput.println("trial randomization list: " + codes.getCodes().size() + " codes");
			if (purge) {
				Iterator<StratificationRandomizationListOutVO> it = trialService.getStratificationRandomizationListList(context.getAuth(), context.getEntityId(),
						null).iterator();
				while (it.hasNext()) {
					StratificationRandomizationListOutVO stratificationRandomizationList = it.next();
					stratificationRandomizationList = trialService.deleteStratificationRandomizationList(context.getAuth(), stratificationRandomizationList.getId());
					jobOutput.println("stratification randomization list " + getStratificationrandomizationListName(stratificationRandomizationList) + " removed");
				}
			}
		}
	}

	private final static String getStratificationrandomizationListName(StratificationRandomizationListOutVO randomizationList) {
		Iterator<InputFieldSelectionSetValueOutVO> it = randomizationList.getSelectionSetValues().iterator();
		StringBuilder result = new StringBuilder();
		while (it.hasNext()) {
			if (result.length() > 0) {
				result.append("/");
			}
			result.append(it.next().getName());
		}
		return result.toString();
	}

	@Override
	protected boolean processHeaderRow(String[] values) throws Throwable {
		Iterator<ProbandListEntryTagOutVO> it = trialService.getProbandListEntryTagList(context.getAuth(), context.getEntityId(), null, true).iterator();
		while (it.hasNext()) {
			ProbandListEntryTagOutVO stratificationTag = it.next();
			for (int i = codeColumnIndex; i < values.length; i++) {
				if (stratificationTag.getField().getNameL10nKey().equals(values[i])
						&& !stratificationTagIndexMap.containsKey(i)) {
					stratificationTagIndexMap.put(i, stratificationTag);
					break;
				}
			}
		}
		for (int i = codeColumnIndex; i < values.length; i++) {
			if (!stratificationTagIndexMap.containsKey(i)) {
				codeValueNameIndexMap.put(i, values[i]);
			}
		}
		return true;
	}

	private Long getSelectionSetValueId(ProbandListEntryTagOutVO stratificationTag, String value) {
		if (stratificationTagValueMap == null) {
			stratificationTagValueMap = new HashMap<Long, HashMap<String, InputFieldSelectionSetValueOutVO>>();
		}
		HashMap<String, InputFieldSelectionSetValueOutVO> selectionSetValueMap;
		if (stratificationTagValueMap.containsKey(stratificationTag.getField().getId())) {
			selectionSetValueMap = stratificationTagValueMap.get(stratificationTag.getField().getId());
		} else {
			selectionSetValueMap = new HashMap<String, InputFieldSelectionSetValueOutVO>();
			Iterator<InputFieldSelectionSetValueOutVO> it = stratificationTag.getField().getSelectionSetValues().iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionSetValue = it.next();
				selectionSetValueMap.put(selectionSetValue.getValue(), selectionSetValue);
			}
			stratificationTagValueMap.put(stratificationTag.getField().getId(), selectionSetValueMap);
		}
		if (selectionSetValueMap.containsKey(value)) {
			return selectionSetValueMap.get(value).getId();
		} else {
			throw new IllegalArgumentException("unknown selection set value '" + value + "' for stratification field " + stratificationTag.getUniqueName());
		}
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		Stratification stratification;
		Iterator<Integer> stratificationTagIndexIt = stratificationTagIndexMap.keySet().iterator();
		if (stratificationTagIndexIt.hasNext()) {
			if (stratificationsMap == null) {
				stratificationsMap = new LinkedHashMap();
			}
			stratification = null;
			Map stratificationsSubMap = stratificationsMap;
			ArrayList<Long> selectionSetValueIds = new ArrayList<Long>(stratificationTagIndexMap.size());
			while (stratificationTagIndexIt.hasNext()) {
				Integer stratificationTagIndex = stratificationTagIndexIt.next();
				String value = getColumnValue(values, stratificationTagIndex);
				selectionSetValueIds.add(getSelectionSetValueId(stratificationTagIndexMap.get(stratificationTagIndex), value));
				if (stratificationTagIndexIt.hasNext()) {
					if (!stratificationsSubMap.containsKey(value)) {
						stratificationsSubMap.put(value, new LinkedHashMap());
					}
					stratificationsSubMap = (Map) stratificationsSubMap.get(value);
				} else {
					if (!stratificationsSubMap.containsKey(value)) {
						stratificationsSubMap.put(value, new Stratification(selectionSetValueIds));
					}
					stratification = (Stratification) stratificationsSubMap.get(value);
				}
			}
		} else {
			if (trialStratification == null) {
				trialStratification = new Stratification();
			}
			stratification = trialStratification;
		}
		stratification.getRandomizationList().add(getCode(values));
		stratification.getValuesList().add(values);
		return 1;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (CommonUtil.isEmptyString(getCode(values))) {
			jobOutput.println("row " + rowNumber + ": empty code");
			return false;
		}
		return true;
	}
}
