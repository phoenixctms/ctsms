package org.phoenixctms.ctsms.util.randomization;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.ProbandListEntryTagRandomizeCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandListEntryTagStratificationFieldCollisionFinder;
import org.phoenixctms.ctsms.adapt.StratificationRandomizationListCollisionFinder;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTag;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.StratificationRandomizationList;
import org.phoenixctms.ctsms.domain.StratificationRandomizationListDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagInVO;
import org.phoenixctms.ctsms.vo.RandomizationInfoVO;
import org.phoenixctms.ctsms.vo.RandomizationListInfoVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;
import org.phoenixctms.ctsms.vo.TrialInVO;

public abstract class Randomization {

	public enum RandomizationType {
		GROUP, TAG_SELECT, TAG_TEXT
	}

	private final static String RANDOMIZATION_SEED_RANDOM_ALGORITHM = CoreUtil.RANDOM_ALGORITHM;
	private final static String RANDOMIZATION_LIST_SEED_RANDOM_ALGORITHM = CoreUtil.RANDOM_ALGORITHM;
	private final static String RANDOMIZATION_BLOCK_LINE_SEPARATOR = "\n";
	private final static String RANDOMIZATION_BLOCK_SPLIT_SEPARATOR = ";";
	private final static String RANDOMIZATION_BLOCK_SPLIT_REGEX_PATTERN = "(\\r\\n)|\\r|\\n|[," + Pattern.quote(RANDOMIZATION_BLOCK_SPLIT_SEPARATOR) + "]";
	private final static Pattern RANDOMIZATION_BLOCK_SPLIT_REGEXP = Pattern.compile(RANDOMIZATION_BLOCK_SPLIT_REGEX_PATTERN);
	private final static boolean TRIM_PROBAND_GROUP_TOKEN = true;
	private final static boolean TRIM_INPUT_FIELD_SELECTION_SET_VALUE_VALUE = true;
	private final static boolean TRIM_INPUT_FIELD_TEXT_VALUE = true;

	public final static void checkInputFieldSelectionSetValueInput(InputFieldSelectionSetValueInVO inputFieldSelectionSetValueIn, ProbandListEntryTagDao probandListEntryTagDao) throws ServiceException {
		if ( probandListEntryTagDao.getCountByFieldStratificationRandomize(inputFieldSelectionSetValueIn.getFieldId(), null, true) > 0) {
			checkInputFieldSelectionSetValueValue(inputFieldSelectionSetValueIn.getValue());
		}
	}

	private final static void checkInputFieldSelectionSetValueValue(String value) throws ServiceException {
		if (RANDOMIZATION_BLOCK_SPLIT_REGEXP.matcher(value).find()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INPUT_FIELD_SELECTION_SET_VALUE_VALUE, value); // ,probandGroupIn.getToken());
			// //,probandGroupIn.getToken());
		}
		if (TRIM_INPUT_FIELD_SELECTION_SET_VALUE_VALUE && !value.trim().equals(value)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.WHITESPACE_INPUT_FIELD_SELECTION_SET_VALUE_VALUE, value); // ,probandGroupIn.getToken()); //_whitespace
		}
	}

	// protected abstract void checkInputFieldSelectionSetValueValueInput(Trial trial, InputFieldSelectionSetValueInVO inputFieldSelectionSetValueIn) throws ServiceException;
	public final static void checkProbandGroupInput(Trial trial, ProbandGroupInVO probandGroupIn) throws ServiceException {
		if (probandGroupIn.getRandomize()) {
			checkProbandGroupToken(probandGroupIn.getToken());
			// if (RANDOMIZATION_BLOCK_SPLIT_REGEXP.matcher(probandGroupIn.getToken()).find()) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_GROUP_TOKEN, probandGroupIn.getToken()); // ,probandGroupIn.getToken());
			// // //,probandGroupIn.getToken());
			// }
			// if (TRIM_PROBAND_GROUP_TOKEN && !probandGroupIn.getToken().trim().equals(probandGroupIn.getToken())) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.WHITESPACE_PROBAND_GROUP_TOKEN, probandGroupIn.getToken()); // ,probandGroupIn.getToken()); //_whitespace
			// }
			// checkProbandGroupToken(probandGroupIn.getToken());
			// getInstance(trial.getRandomization(),trialDao,probandGroupDao,probandListEntryDao,stratificationRandomizationListDao).checkProbandGroupTokenInput(trial,
			// probandGroupIn);
		}
	}

	private final static void checkProbandGroupToken(String token) throws ServiceException {
		if (RANDOMIZATION_BLOCK_SPLIT_REGEXP.matcher(token).find()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_PROBAND_GROUP_TOKEN, token); // ,probandGroupIn.getToken()); //,probandGroupIn.getToken());
		}
		if (TRIM_PROBAND_GROUP_TOKEN && !token.trim().equals(token)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.WHITESPACE_PROBAND_GROUP_TOKEN, token); // ,probandGroupIn.getToken()); //_whitespace
		}
	}

	public final static void checkProbandListEntryTagInput(Trial trial, InputField field, ProbandListEntryTagInVO listTagIn, TrialDao trialDao, ProbandGroupDao probandGroupDao,
			ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao)
					throws ServiceException {
		if (listTagIn.isStratification()) {
			if (listTagIn.isRandomize()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_STRATIFIKATION_AND_RANDOMIZE);
			}
			if (!ServiceUtil.isInputFieldTypeSelectOne(field.getFieldType())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_STRATIFICATION_FIELD_NOT_SELECT_ONE);
			}
			if ((new ProbandListEntryTagStratificationFieldCollisionFinder(trialDao, probandListEntryTagDao)).collides(listTagIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_STRATIFICATION_FIELD_NOT_UNIQUE);
			}
		} else if (listTagIn.isRandomize()) {
			if (trial.getRandomization() != null) {
				getInstance(trial.getRandomization(), trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao).checkProbandListEntryTagField(trial, field);
			}

			if ((new ProbandListEntryTagRandomizeCollisionFinder(trialDao, probandListEntryTagDao)).collides(listTagIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_NOT_UNIQUE);
			}
			Iterator<InputFieldSelectionSetValue> it = field.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				checkInputFieldSelectionSetValueValue(it.next().getValue());
			}
		}
	}

	// public static void checkInputFieldSelectionSetValueInput( InputFieldSelectionSetValueInVO inputFieldSelectionSetValueIn) throws ServiceException {
	// if ( probandListEntryTagDao.getCountByFieldStratificationRandomize(inputFieldSelectionSetValueIn.getFieldId(), null, true)) {
	// checkInputFieldSelectionSetValueValueInput(inputFieldSelectionSetValueIn);
	// }
	// }
	public final static void checkStratificationRandomizationListInput(Trial trial, StratificationRandomizationListInVO randomizationListIn,
			TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) throws ServiceException {
		Iterator<ProbandListEntryTag> it = probandListEntryTagDao.findByTrialFieldStratificationRandomize(randomizationListIn.getTrialId(), null, true, null).iterator();
		HashMap<Long, InputField> tagFieldMap = new HashMap<Long, InputField>();
		while (it.hasNext()) {
			InputField field = it.next().getField();
			tagFieldMap.put(field.getId(), field);
		}
		if (tagFieldMap.size() == 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_PROBAND_LIST_ENTRY_TAG_STRATIFICATION_FIELDS);
		}
		HashSet<Long> selectionSetValueFieldIds = new HashSet<Long>(randomizationListIn.getSelectionSetValueIds().size());
		Iterator<Long> selectionSetValueIdsIt = randomizationListIn.getSelectionSetValueIds().iterator();
		while (selectionSetValueIdsIt.hasNext()) {
			InputFieldSelectionSetValue selectionSetValue = CheckIDUtil.checkInputFieldSelectionSetValueId(selectionSetValueIdsIt.next(), inputFieldSelectionSetValueDao);
			InputField field = selectionSetValue.getField();
			if (!selectionSetValueFieldIds.add(field.getId())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.AMBIGOUS_SELECTION_SET_VALUE_FOR_STRATIFICATION_FIELD, selectionSetValue.getNameL10nKey(),
						field.getNameL10nKey());
			}
			if (tagFieldMap.remove(field.getId()) == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_FIELD_NOT_A_STRATIFICATION_FIELD, selectionSetValue.getNameL10nKey(),
						field.getNameL10nKey());
			}
		}
		if (tagFieldMap.size() > 0) {
			Iterator<InputField> missingFieldsIt = tagFieldMap.values().iterator();
			StringBuilder sb = new StringBuilder();
			while (missingFieldsIt.hasNext()) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(missingFieldsIt.next().getNameL10nKey());
			}
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MISSING_STRATIFICATION_FIELD_SELECTION_SET_VALUES, sb.toString());
		}
		if ((new StratificationRandomizationListCollisionFinder(trialDao, stratificationRandomizationListDao)).collides(randomizationListIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STRATIFICATION_FIELD_SELECTION_SET_VALUES_NOT_UNIQUE);
		}
		if (trial.getRandomization() != null) {
			getInstance(trial.getRandomization(), trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
					inputFieldSelectionSetValueDao, probandListEntryTagValueDao).checkStratificationRandomizationListRandomizationListInput(trial, randomizationListIn);
		}
	}

	public final static void checkTrialInput(Trial trial, TrialInVO trialIn, TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) throws ServiceException {
		if (trialIn.getRandomization() == null) {
			if (trialIn.getRandomizationList() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_RANDOMIZATION_LIST_NOT_NULL);
				// ,L10nUtil.getRandomizationModeName(Locales.USER, trialIn.getRandomization().name()));
			}
		} else {
			getInstance(trialIn.getRandomization(), trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
					inputFieldSelectionSetValueDao, probandListEntryTagValueDao).checkTrialRandomizationInput(trial,
							trialIn);
		}
	}

	protected final static HashMap<Long, InputFieldSelectionSetValue> getInputFieldSelectionSetValueIdMap(Collection<InputFieldSelectionSetValue> inputFieldSelectionSetValues) {
		HashMap<Long, InputFieldSelectionSetValue> result = new HashMap<Long, InputFieldSelectionSetValue>();
		Iterator<InputFieldSelectionSetValue> it = inputFieldSelectionSetValues.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValue inputFieldSelectionSetValue = it.next();
			result.put(inputFieldSelectionSetValue.getId(), inputFieldSelectionSetValue);
		}
		return result;
	}

	protected final static HashMap<String, InputFieldSelectionSetValue> getInputFieldSelectionSetValueValueMap(
			Collection<InputFieldSelectionSetValue> inputFieldSelectionSetValues) {
		HashMap<String, InputFieldSelectionSetValue> result = new HashMap<String, InputFieldSelectionSetValue>();
		Iterator<InputFieldSelectionSetValue> it = inputFieldSelectionSetValues.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValue inputFieldSelectionSetValue = it.next();
			result.put(inputFieldSelectionSetValue.getValue(), inputFieldSelectionSetValue);
		}
		return result;
	}

	public final static Randomization getInstance(RandomizationMode mode, TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		switch (mode) {
			case GROUP_COIN:
				return new GroupCoinRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case GROUP_ADAPTIVE:
				return new GroupAdaptiveRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case GROUP_LIST:
				return new GroupListRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case GROUP_STRATIFIED:
				return new GroupStratifiedRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case TAG_COIN:
				return new TagCoinRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case TAG_ADAPTIVE:
				return new TagAdaptiveRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case TAG_SELECT_LIST:
				return new TagSelectListRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case TAG_SELECT_STRATIFIED:
				return new TagSelectStratifiedRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case TAG_TEXT_LIST:
				return new TagTextListRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			case TAG_TEXT_STRATIFIED:
				return new TagTextStratifiedRandomization(trialDao, probandGroupDao, probandListEntryDao, stratificationRandomizationListDao, probandListEntryTagDao,
						inputFieldSelectionSetValueDao, probandListEntryTagValueDao);
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_RANDOMIZATION_MODE, DefaultMessages.UNSUPPORTED_RANDOMIZATION_MODE, mode));
		}
	}

	protected final static HashMap<Long, ProbandGroup> getProbandGroupIdMap(Collection<ProbandGroup> probandGroups) {
		HashMap<Long, ProbandGroup> result = new HashMap<Long, ProbandGroup>();
		Iterator<ProbandGroup> it = probandGroups.iterator();
		while (it.hasNext()) {
			ProbandGroup probandGroup = it.next();
			result.put(probandGroup.getId(), probandGroup);
		}
		return result;
	}

	protected final static HashMap<String, ProbandGroup> getProbandGroupTokenMap(Collection<ProbandGroup> probandGroups) {
		HashMap<String, ProbandGroup> result = new HashMap<String, ProbandGroup>();
		Iterator<ProbandGroup> it = probandGroups.iterator();
		while (it.hasNext()) {
			ProbandGroup probandGroup = it.next();
			result.put(probandGroup.getToken(), probandGroup);
		}
		return result;
	}

	protected final static void splitInputFieldSelectionSetValueValues(String randomizationBlock, HashMap<String, InputFieldSelectionSetValue> inputFieldSelectionSetValueMap, ArrayList<String> values)
			throws ServiceException {
		// ArrayList<String> result = new ArrayList<String>();

		if (!CommonUtil.isEmptyString(randomizationBlock)) {
			String[] block  =RANDOMIZATION_BLOCK_SPLIT_REGEXP.split(randomizationBlock, -1);
			for (int i = 0; i < block.length; i++) {
				String value = (TRIM_INPUT_FIELD_SELECTION_SET_VALUE_VALUE ? block[i].trim() : block[i]);
				if (!CommonUtil.isEmptyString(value)) {
					if (inputFieldSelectionSetValueMap.containsKey(value)) {
						if (values != null) {
							values.add(value);
						}
					} else {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.UNKNOWN_INPUT_FIELD_SELECTION_SET_VALUE_VALUE, value);
					}
				}
			}
		}
		// return result;
	}

	protected final static void splitInputFieldTextValues(String randomizationBlock, ArrayList<String> values)
			throws ServiceException {
		// ArrayList<String> result = new ArrayList<String>();
		if (!CommonUtil.isEmptyString(randomizationBlock)) {
			String[] block = RANDOMIZATION_BLOCK_SPLIT_REGEXP.split(randomizationBlock, -1);
			for (int i = 0; i < block.length; i++) {
				String value = (TRIM_INPUT_FIELD_TEXT_VALUE ? block[i].trim() : block[i]);
				if (!CommonUtil.isEmptyString(value)) {
					// if (inputFieldSelectionSetValueMap.containsKey(value)) {
					if (values != null) {
						values.add(value);
					}
					// } else {
					// throw L10nUtil.initServiceException(ServiceExceptionCodes.UNKNOWN_INPUT_FIELD_SELECTION_SET_VALUE_VALUE, value);
					// }
				}
			}
		}
		// return result;
	}

	protected final static void splitProbandGroupTokens(String randomizationBlock, HashMap<String, ProbandGroup> probandGroupMap, ArrayList<String> tokens)
			throws ServiceException {
		// ArrayList<String> result = new ArrayList<String>();

		if (!CommonUtil.isEmptyString(randomizationBlock)) {
			String[] block  =RANDOMIZATION_BLOCK_SPLIT_REGEXP.split(randomizationBlock, -1);
			for (int i = 0; i < block.length; i++) {
				String token = (TRIM_PROBAND_GROUP_TOKEN ? block[i].trim() : block[i]);
				if (!CommonUtil.isEmptyString(token)) {
					if (probandGroupMap.containsKey(token)) {
						if (tokens != null) {
							tokens.add(token);
						}
					} else {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.UNKNOWN_PROBAND_GROUP_TOKEN,token);
					}
				}
			}
		}
		// return result;
	}

	protected TrialDao trialDao;

	protected ProbandGroupDao probandGroupDao;

	protected ProbandListEntryDao probandListEntryDao;

	protected StratificationRandomizationListDao stratificationRandomizationListDao;

	protected ProbandListEntryTagDao probandListEntryTagDao;

	protected InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	protected ProbandListEntryTagValueDao probandListEntryTagValueDao;

	private Random random;

	private boolean locked;

	protected RandomizationInfoVO randomizationInfo;

	private RandomizationListInfoVO randomizationListInfo;


	protected Randomization(TrialDao trialDao, ProbandGroupDao probandGroupDao, ProbandListEntryDao probandListEntryDao,
			StratificationRandomizationListDao stratificationRandomizationListDao, ProbandListEntryTagDao probandListEntryTagDao,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao, ProbandListEntryTagValueDao probandListEntryTagValueDao) {
		this.trialDao = trialDao;
		this.probandGroupDao = probandGroupDao;
		this.probandListEntryDao = probandListEntryDao;
		this.stratificationRandomizationListDao = stratificationRandomizationListDao;
		this.probandListEntryTagDao = probandListEntryTagDao;
		this.inputFieldSelectionSetValueDao = inputFieldSelectionSetValueDao;
		this.probandListEntryTagValueDao = probandListEntryTagValueDao;
		locked = false;
	}

	protected void checkProbandListEntryTagField(Trial trial, InputField field) throws ServiceException {
		if (!(ServiceUtil.isInputFieldTypeSelectOne(field.getFieldType()) || InputFieldType.SINGLE_LINE_TEXT.equals(field.getFieldType()))) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_RANDOMIZE_FIELD_NOT_SELECT_ONE_OR_SINGLE_LINE_TEXT);
		}
	}

	// protected abstract void checkProbandGroupTokenInput(Trial trial, ProbandGroupInVO probandGroupIn) throws ServiceException;

	// protected abstract void checkProbandListEntryTagInputFieldSelectionSetValueValuesInput() throws ServiceException ;

	protected void checkStratificationRandomizationListRandomizationListInput(Trial trial, StratificationRandomizationListInVO randomizationListIn)
			throws ServiceException {
	}

	protected void checkTrialRandomizationInput(Trial trial, TrialInVO trialIn) throws ServiceException {
	}

	public final String generateRandomizationList(Trial trial, int n) throws Exception {
		randomizationListInfo = new RandomizationListInfoVO();
		TreeSet<String> items = getRandomizationListItems(trial);

		randomizationListInfo.setItems(new ArrayList<String>(items));
		if (n > 0) {
			randomizationListInfo.setN(n);
			StringBuilder sb =  new StringBuilder();
			//ArrayList<String> result = new ArrayList<String>(tokens.size() * n);
			int blocks = (int) Math.ceil(((double) n) / ((double) items.size()));
			int count = 0;
			long seed = SecureRandom.getInstance(RANDOMIZATION_LIST_SEED_RANDOM_ALGORITHM).nextLong();
			Random random = new Random(seed); // reproducable
			randomizationListInfo.setPrngClass(CoreUtil.getPrngClassDescription(random));
			randomizationListInfo.setSeed(seed);
			for (int i = 0; i < blocks; i++) {
				ArrayList<String> permutation = new ArrayList<String>(items);
				Collections.shuffle(permutation, random);
				boolean newLine = true;
				Iterator<String> itemIt = permutation.iterator();
				while (itemIt.hasNext() && count < n) {
					if (sb.length() > 0) {
						if (newLine) {
							sb.append(RANDOMIZATION_BLOCK_LINE_SEPARATOR);
						} else {
							sb.append(RANDOMIZATION_BLOCK_SPLIT_SEPARATOR);
						}
					}
					newLine = false;
					String item = itemIt.next();
					sb.append(item);
					count++;
					randomizationListInfo.getRandomizationLists().add(item);
				}
				//result.addAll(permutation);
			}
			return sb.toString();
		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.POPULATION_LESS_THAN_ONE);
		}

	}

	protected final Random getRandom(Trial trial) throws NoSuchAlgorithmException, ClassNotFoundException, IOException {
		if (random == null) {
			byte[] randomSerialized = trial.getRandom();
			if (randomSerialized != null) {
				random = (Random) CoreUtil.deserialize(randomSerialized);
			} else {
				long seed = SecureRandom.getInstance(RANDOMIZATION_SEED_RANDOM_ALGORITHM).nextLong();
				random = new Random(seed); // reproducable (securerandom cannot be initialized with a dedicated seed)
				randomizationInfo.setPrngClass(CoreUtil.getPrngClassDescription(random));
				randomizationInfo.setSeed(seed);
			}
		}
		return random;
	}


	protected final Collection<ProbandGroup> getRandomizationGroups(Trial trial) throws ServiceException {
		return probandGroupDao.findByTrialRandomize(trial.getId(), true);
		// Collection<ProbandGroup> groups = probandGroupDao.findByTrialRandomize(trial.getId(), true);
		// if (groups.size() == 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_PROBAND_GROUPS);
		// }
		// return groups;
	}

	public final RandomizationInfoVO getRandomizationInfoVO() {
		return randomizationInfo;
	}

	protected final Collection<InputFieldSelectionSetValue> getRandomizationInputFieldSelectionSetValues(Trial trial) {
		try {
			return probandListEntryTagDao.findByTrialFieldStratificationRandomize(trial.getId(), null, null, true).iterator().next().getField().getSelectionSetValues();
		} catch (NoSuchElementException e) {
			return new ArrayList<InputFieldSelectionSetValue>();
		}
		// Collection<ProbandListEntryTag> randomizeTags = probandListEntryTagDao.findByTrialFieldStratificationRandomize(trial.getId(), null, true);
		// if (randomizeTags.size() == 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_INPUT_FIELD_SELECTION_SET_VALUES);
		// } else {
		// Collection<InputFieldSelectionSetValue> inputFieldSelectionSetValues = randomizeTags.iterator().next().getField().getSelectionSetValues();
		// if (inputFieldSelectionSetValues.size() == 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_INPUT_FIELD_SELECTION_SET_VALUES);
		// }
		// return inputFieldSelectionSetValues;
		// }
	}

	protected final TreeSet<String> getRandomizationListGroups(Trial trial) throws Exception {
		TreeSet<String> tokens = new TreeSet<String>();
		Iterator<ProbandGroup> it = getRandomizationGroups(trial).iterator();
		while (it.hasNext()) {
			String token = it.next().getToken();
			checkProbandGroupToken(token);
			tokens.add(token);
		}
		if (tokens.size() == 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_PROBAND_GROUPS);
		}
		return tokens;
	}

	public final RandomizationListInfoVO getRandomizationListInfoVO() {
		return randomizationListInfo;
	}



	protected final TreeSet<String> getRandomizationListInputFieldSelectionSetValueValues(Trial trial) throws Exception {
		TreeSet<String> values = new TreeSet<String>();
		Iterator<InputFieldSelectionSetValue> it = getRandomizationInputFieldSelectionSetValues(trial).iterator();
		while (it.hasNext()) {
			String value = it.next().getValue();
			checkInputFieldSelectionSetValueValue(value);
			values.add(value);
		}
		if (values.size() == 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_INPUT_FIELD_SELECTION_SET_VALUES);
		}
		return values;
	}


	protected TreeSet<String> getRandomizationListItems(Trial trial) throws Exception {
		throw L10nUtil.initServiceException(ServiceExceptionCodes.RANDOMIZATION_LISTS_NOT_SUPPORTED,
				L10nUtil.getRandomizationModeName(Locales.USER, getRandomizationMode().name()));
	}

	protected abstract RandomizationMode getRandomizationMode();

	public final InputFieldSelectionSetValue getRandomizedInputFieldSelectionSetValue(Trial trial, ProbandListEntry exclude) throws Exception {
		if (!locked) {
			trialDao.lock(trial, LockMode.PESSIMISTIC_WRITE);
			locked = true;
		}
		randomizationInfo = new RandomizationInfoVO();
		randomizationInfo.setRandomization(L10nUtil.createRandomizationModeVO(Locales.JOURNAL, getRandomizationMode()));
		InputFieldSelectionSetValue value = randomizeInputFieldSelectionSetValue(trial, exclude);
		if (value == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_INPUT_FIELD_SELECTION_SET_VALUE_RANDOMIZED);
		} else {
			randomizationInfo.setRandomized(value.getValue());
		}
		return value;
	}

	public final String getRandomizedInputFieldTextValue(Trial trial, ProbandListEntry exclude) throws Exception {
		if (!locked) {
			trialDao.lock(trial, LockMode.PESSIMISTIC_WRITE);
			locked = true;
		}
		randomizationInfo = new RandomizationInfoVO();
		randomizationInfo.setRandomization(L10nUtil.createRandomizationModeVO(Locales.JOURNAL, getRandomizationMode()));
		String value = randomizeInputFieldTextValue(trial, exclude);
		if (value == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_INPUT_FIELD_TEXT_VALUE_RANDOMIZED);
		} else {
			randomizationInfo.setRandomized(value);
		}
		return value;
	}

	public final ProbandGroup getRandomizedProbandGroup(Trial trial, ProbandListEntry exclude) throws Exception {
		if (!locked) {
			trialDao.lock(trial, LockMode.PESSIMISTIC_WRITE);
			locked = true;
		}
		randomizationInfo = new RandomizationInfoVO();
		randomizationInfo.setRandomization(L10nUtil.createRandomizationModeVO(Locales.JOURNAL, getRandomizationMode()));
		ProbandGroup group = randomizeProbandGroup(trial, exclude);
		if (group == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_PROBAND_GROUP_RANDOMIZED);
		} else {
			randomizationInfo.setRandomized(group.getToken());
		}
		return group;
	}

	protected final StratificationRandomizationList getStratificationRandomizationList(Trial trial,ProbandListEntry exclude) throws ServiceException {
		if (exclude != null) {

			Iterator<ProbandListEntryTag> tagIt = probandListEntryTagDao.findByTrialFieldStratificationRandomize(trial.getId(), null, true, null).iterator();
			HashSet<Long> selectionSetValueIds = new HashSet<Long>();
			StringBuilder sb = new StringBuilder();

			while (tagIt.hasNext()) {
				ProbandListEntryTag tag = tagIt.next();
				InputFieldSelectionSetValue selectionSetValue;
				try {
					selectionSetValue = probandListEntryTagValueDao.findByListEntryListEntryTag(exclude.getId(), tag.getId()).iterator().next().getValue().getSelectionValues().iterator().next();
				} catch (NoSuchElementException e) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.STRATIFICATION_PROBAND_LIST_ENTRY_TAG_VALUE_REQUIRED,probandListEntryTagDao.toProbandListEntryTagOutVO(tag).getUniqueName());
				}
				selectionSetValueIds.add(selectionSetValue.getId());
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(selectionSetValue.getNameL10nKey());
			}
			StratificationRandomizationList result;
			try {
				result = stratificationRandomizationListDao.findByTrialTagValues(trial.getId(), selectionSetValueIds).iterator().next();
			} catch (NoSuchElementException e) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MISSING_STRATIFICATION_RANDOMIZATION_LIST, sb.toString());
			}
			return result;
		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.RANDOMIZATION_PROBAND_LIST_ENTRY_REQUIRED);
		}
	}

	public abstract RandomizationType getType();

	// protected abstract InputFieldSelectionSetValue randomizeInputFieldSelectionSetValue(Trial trial, ProbandListEntry exclude) throws Exception;
	// protected abstract String randomizeInputFieldTextValue(Trial trial, ProbandListEntry exclude) throws Exception;
	// protected abstract ProbandGroup randomizeProbandGroup(Trial trial, ProbandListEntry exclude) throws Exception;

	protected final void initGroupsInfo(Collection<ProbandGroup> probandGroups) {
		randomizationInfo.setSizes(new HashMap<String, Long>());
		Iterator<ProbandGroup> it = probandGroups.iterator();
		while (it.hasNext()) {
			randomizationInfo.getSizes().put(it.next().getToken(), null);
		}
	}

	protected final void initStratificationValuesInfo(StratificationRandomizationList randomizationList) {
		Iterator<InputFieldSelectionSetValue> it = randomizationList.getSelectionSetValues().iterator();
		while (it.hasNext()) {
			randomizationInfo.getStratificationValues().add(it.next().getValue());
		}

	}

	protected final void initValuesInfo(Collection<InputFieldSelectionSetValue> values) {
		randomizationInfo.setSizes(new HashMap<String, Long>());
		Iterator<InputFieldSelectionSetValue> it = values.iterator();
		while (it.hasNext()) {
			randomizationInfo.getSizes().put(it.next().getValue(), null);
		}
	}

	protected InputFieldSelectionSetValue randomizeInputFieldSelectionSetValue(Trial trial, ProbandListEntry exclude) throws Exception {
		throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_SELECTION_SET_VALUE_RANDOMIZATION_NOT_SUPPORTED,
				L10nUtil.getRandomizationModeName(Locales.USER, getRandomizationMode().name()));
	}

	protected String randomizeInputFieldTextValue(Trial trial, ProbandListEntry exclude) throws Exception {
		throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_TEXT_VALUE_RANDOMIZATION_NOT_SUPPORTED,
				L10nUtil.getRandomizationModeName(Locales.USER, getRandomizationMode().name()));
	}

	protected ProbandGroup randomizeProbandGroup(Trial trial, ProbandListEntry exclude) throws Exception {
		throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GROUP_RANDOMIZATION_NOT_SUPPORTED,
				L10nUtil.getRandomizationModeName(Locales.USER, getRandomizationMode().name()));
	}

	protected final void saveRandom(Trial trial) throws IOException {
		trial.setRandom(CoreUtil.serialize(random));
	}
}
