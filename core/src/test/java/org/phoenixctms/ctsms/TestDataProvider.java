package org.phoenixctms.ctsms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.CriteriaDao;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestriction;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.domain.CriterionTie;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.SponsoringTypeDao;
import org.phoenixctms.ctsms.domain.SurveyStatusTypeDao;
import org.phoenixctms.ctsms.domain.TrialStatusTypeDao;
import org.phoenixctms.ctsms.domain.TrialTypeDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.domain.UserPermissionProfile;
import org.phoenixctms.ctsms.domain.UserPermissionProfileDao;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.service.shared.SearchService;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.test.InkStroke;
import org.phoenixctms.ctsms.test.InputFieldValuesEnum;
import org.phoenixctms.ctsms.test.InputFieldsEnum;
import org.phoenixctms.ctsms.test.OutputLogger;
import org.phoenixctms.ctsms.test.SearchCriteriaEnum;
import org.phoenixctms.ctsms.test.SearchCriterion;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.InputFieldInVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryInVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.PasswordInVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserInVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

public class TestDataProvider { //extends ProductionDataProvider {

	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserPermissionProfileDao userPermissionProfileDao;
	@Autowired
	private InputFieldDao inputFieldDao;
	//	@Autowired
	//	protected MimeTypeDao mimeTypeDao;
	@Autowired
	private ToolsService toolsService;
	@Autowired
	private InputFieldService inputFieldService;
	@Autowired
	private TrialService trialService;
	@Autowired
	private TrialStatusTypeDao trialStatusTypeDao;
	@Autowired
	private TrialTypeDao trialTypeDao;
	@Autowired
	private SponsoringTypeDao sponsoringTypeDao;
	@Autowired
	private SurveyStatusTypeDao surveyStatusTypeDao;
	@Autowired
	private CriterionTieDao criterionTieDao;
	@Autowired
	private CriterionPropertyDao criterionPropertyDao;
	@Autowired
	private CriterionRestrictionDao criterionRestrictionDao;
	@Autowired
	private CriteriaDao criteriaDao;
	@Autowired
	private InquiryDao inquiryDao;
	@Autowired
	private SearchService searchService;
	@Autowired
	private InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	private OutputLogger log;
	private AuthenticationVO auth;

	public void setLog(OutputLogger log) {
		this.log = log;
	}

	private void info(String msg) {
		if (log != null) {
			log.info(msg);
		}
	}

	private void debug(String msg) {
		if (log != null) {
			log.debug(msg);
		}
	}

	private void error(String msg) {
		if (log != null) {
			log.error(msg);
		}
	}

	public void setAuth(AuthenticationVO auth) {
		this.auth = auth;
	}

	public DepartmentVO createDepartment(String nameL10nKey, boolean visible, String plainDepartmentPassword) throws Exception {
		Department department = Department.Factory.newInstance();
		department.setNameL10nKey(nameL10nKey);
		department.setVisible(visible);
		CryptoUtil.encryptDepartmentKey(department, CryptoUtil.createRandomKey().getEncoded(), plainDepartmentPassword);
		DepartmentVO out = departmentDao.toDepartmentVO(departmentDao.create(department));
		info("department ID " + out.getId() + " created: " + out.getName());
		return out;
	}

	public UserOutVO createUser(UserInVO newUser, PasswordInVO newPassword, String departmentPassword, ArrayList<PermissionProfile> profiles) throws Exception {
		UserOutVO user = toolsService.addUser(newUser, newPassword, departmentPassword);
		info("user ID " + user.getId() + " created: " + user.getName());
		addUserPermissionProfiles(user, profiles);
		return user;
	}

	private ArrayList<UserPermissionProfile> addUserPermissionProfiles(UserOutVO userVO, ArrayList<PermissionProfile> profiles) throws Exception {
		User user = userDao.load(userVO.getId());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		ArrayList<UserPermissionProfile> result = new ArrayList<UserPermissionProfile>(profiles.size());
		Iterator<PermissionProfile> profilesIt = profiles.iterator();
		while (profilesIt.hasNext()) {
			PermissionProfile profile = profilesIt.next();
			UserPermissionProfile userPermissionProfile = UserPermissionProfile.Factory.newInstance();
			userPermissionProfile.setActive(true);
			userPermissionProfile.setProfile(profile);
			userPermissionProfile.setUser(user);
			CoreUtil.modifyVersion(userPermissionProfile, now, null);
			result.add(userPermissionProfileDao.create(userPermissionProfile));
			info("permission profile " + profile.toString() + " added");
		}
		return result;
	}

	private void addInkRegions(InputFieldOutVO inputField, TreeMap<InputFieldValuesEnum, InkStroke> inkRegions) throws Exception {
		Iterator<Entry<InputFieldValuesEnum, InkStroke>> it = inkRegions.entrySet().iterator();
		while (it.hasNext()) {
			Entry<InputFieldValuesEnum, InkStroke> inkRegion = it.next();
			InkStroke stroke = inkRegion.getValue();
			InputFieldSelectionSetValueInVO newSelectionSetValue = new InputFieldSelectionSetValueInVO();
			newSelectionSetValue.setFieldId(inputField.getId());
			newSelectionSetValue.setName(inkRegion.getKey().toString());
			newSelectionSetValue.setPreset(false);
			newSelectionSetValue.setValue(stroke.valueSet ? stroke.value : inkRegion.getKey().toString());
			newSelectionSetValue.setInkRegions(stroke.getBytes());
			newSelectionSetValue.setStrokesId(stroke.strokesId);
			InputFieldSelectionSetValueOutVO out = inputFieldService.addSelectionSetValue(auth, newSelectionSetValue);
			info("ink region created: " + out.getName());
		}
	}

	private void addSelectionSetValues(InputFieldOutVO inputField, TreeMap<InputFieldValuesEnum, Boolean> selectionSetValues) throws Exception {
		Iterator<Entry<InputFieldValuesEnum, Boolean>> it = selectionSetValues.entrySet().iterator();
		while (it.hasNext()) {
			Entry<InputFieldValuesEnum, Boolean> selectionSetValue = it.next();
			InputFieldSelectionSetValueInVO newSelectionSetValue = new InputFieldSelectionSetValueInVO();
			newSelectionSetValue.setFieldId(inputField.getId());
			newSelectionSetValue.setName(selectionSetValue.getKey().toString());
			newSelectionSetValue.setPreset(selectionSetValue.getValue());
			newSelectionSetValue.setValue(selectionSetValue.getKey().name());
			InputFieldSelectionSetValueOutVO out = inputFieldService.addSelectionSetValue(auth, newSelectionSetValue);
			info("selection set value created: " + out.getName());
		}
	}

	public InputFieldOutVO getInputField(InputFieldsEnum inputField) throws Throwable {
		String name = inputField.name();
		Iterator<InputField> it = inputFieldDao.findByNameL10nKeyLocalized(name, false).iterator();
		if (it.hasNext()) {
			return inputFieldService.getInputField(auth, it.next().getId());
		}
		return null;
	}

	public CriteriaOutVO getCriteria(SearchCriteriaEnum criteria, String category) throws Throwable {
		String name = criteria.toString();
		Iterator<Criteria> it = (new ArrayList<Criteria>(criteriaDao.search(new Search(new SearchParameter[] {
				new SearchParameter("category", category, SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("label", name, SearchParameter.EQUAL_COMPARATOR) })))).iterator();
		if (it.hasNext()) {
			return searchService.getCriteria(auth, it.next().getId());
		}
		return null;
	}

	public InquiryOutVO getInquiry(TrialOutVO trial, String category, int position) throws Throwable {
		Iterator<Inquiry> it = (new ArrayList<Inquiry>(inquiryDao.search(new Search(new SearchParameter[] {
				new SearchParameter("trial.id", trial.getId(), SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("category", category, SearchParameter.EQUAL_COMPARATOR),
				new SearchParameter("position", position, SearchParameter.EQUAL_COMPARATOR) })))).iterator();
		if (it.hasNext()) {
			return trialService.getInquiry(auth, it.next().getId());
		}
		return null;
	}

	public TrialOutVO createTrial(TrialInVO newTrial, String statusL10nKey, String typeL10nKey, String sponsoringL10nKey, String surveyStatusL10nKey) throws Exception {
		newTrial.setStatusId(trialStatusTypeDao.searchUniqueNameL10nKey(statusL10nKey).getId());
		newTrial.setTypeId(trialTypeDao.searchUniqueNameL10nKey(typeL10nKey).getId());
		newTrial.setSponsoringId(sponsoringTypeDao.searchUniqueNameL10nKey(sponsoringL10nKey).getId());
		newTrial.setSurveyStatusId(surveyStatusTypeDao.searchUniqueNameL10nKey(surveyStatusL10nKey).getId());
		TrialOutVO trial = trialService.addTrial(auth, newTrial, null);
		info("trial ID " + trial.getId() + " created: " + trial.getName());
		return trial;
	}

	public InquiryOutVO createInquiry(InputFieldOutVO inputField, TrialOutVO trial, String category, int position, boolean active, boolean activeSignup,
			boolean optional,
			boolean disabled, boolean excelValue, boolean excelDate, String title, String comment, String jsVariableName, String jsValueExpression, String jsOutputExpression)
			throws Throwable {
		InquiryInVO newInquiry = new InquiryInVO();
		newInquiry.setCategory(category);
		newInquiry.setActive(active);
		newInquiry.setActiveSignup(activeSignup);
		newInquiry.setOptional(optional);
		newInquiry.setDisabled(disabled);
		newInquiry.setExcelValue(excelValue);
		newInquiry.setExcelDate(excelDate);
		newInquiry.setFieldId(inputField.getId());
		newInquiry.setTrialId(trial.getId());
		newInquiry.setPosition(new Long(position));
		newInquiry.setComment(comment);
		newInquiry.setTitle(title);
		newInquiry.setJsVariableName(jsVariableName);
		newInquiry.setJsValueExpression(jsValueExpression);
		newInquiry.setJsOutputExpression(jsOutputExpression);
		InquiryOutVO out = trialService.addInquiry(auth, newInquiry);
		info("inquiry ID " + out.getId() + " created: " + out.getUniqueName());
		return out;
	}

	public InquiryOutVO createInquiry(InputFieldOutVO inputField, TrialOutVO trial, String category, int position) throws Throwable {
		return createInquiry(inputField, trial, category, position, true, true, true, false, true, true, null, null, null, null, null);
	}

	public InputFieldOutVO createIntegerField(String name, String category, String title, String comment, Long longPreset, Long lowerLimit,
			Long upperLimit, String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.INTEGER);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setLongLowerLimit(lowerLimit);
		newInputField.setLongUpperLimit(upperLimit);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setLongPreset(longPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("integer input field ID " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createIntegerField(String name, String title) throws Exception {
		return createIntegerField(name, null, title, null, null, null, null, null);
	}

	public InputFieldOutVO createMultiLineTextField(String name, String category, String title, String comment, String textPreset, String regExp,
			String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.MULTI_LINE_TEXT);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setRegExp(regExp);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTextPreset(textPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("multi line text input field ID " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createMultiLineTextField(String name, String title) throws Exception {
		return createMultiLineTextField(name, null, title, null, null, null, null);
	}

	public InputFieldOutVO createSelectManyField(String name, String category, String title, String comment,
			boolean vertical, TreeMap<InputFieldValuesEnum, Boolean> selectionSetValues,
			Integer minSelections, Integer maxSelections, String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(vertical ? InputFieldType.SELECT_MANY_V : InputFieldType.SELECT_MANY_H);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinSelections(minSelections);
		newInputField.setMaxSelections(maxSelections);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		info("select many input field ID " + inputField.getId() + " created: " + inputField.getName());
		addSelectionSetValues(inputField, selectionSetValues);
		return inputField;
	}

	public InputFieldOutVO createSelectManyField(String name, String title, TreeMap<InputFieldValuesEnum, Boolean> selectionSetValues) throws Exception {
		return createSelectManyField(name, null, title, null, true, selectionSetValues, null, null, null);
	}

	public InputFieldOutVO createSelectOneDropdownField(String name, String category, String title, String comment,
			TreeMap<InputFieldValuesEnum, Boolean> selectionSetValues)
			throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.SELECT_ONE_DROPDOWN);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		info("select one dropdown input field ID " + inputField.getId() + " created: " + inputField.getName());
		addSelectionSetValues(inputField, selectionSetValues);
		return inputField;
	}

	public InputFieldOutVO createSelectOneDropdownField(String name, String title, TreeMap<InputFieldValuesEnum, Boolean> selectionSetValues) throws Exception {
		return createSelectOneDropdownField(name, null, title, null, selectionSetValues);
	}

	public InputFieldOutVO createSelectOneRadioField(String name, String category, String title, String comment, boolean vertical,
			TreeMap<InputFieldValuesEnum, Boolean> selectionSetValues)
			throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(vertical ? InputFieldType.SELECT_ONE_RADIO_V : InputFieldType.SELECT_ONE_RADIO_H);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		info("select one radio input field " + inputField.getId() + " created: " + inputField.getName());
		addSelectionSetValues(inputField, selectionSetValues);
		return inputField;
	}

	public InputFieldOutVO createSelectOneRadioField(String name, String title, TreeMap<InputFieldValuesEnum, Boolean> selectionSetValues) throws Exception {
		return createSelectOneRadioField(name, null, title, null, true, selectionSetValues);
	}

	public InputFieldOutVO createSingleLineTextField(String name, String category, String title, String comment, String textPreset, String regExp,
			String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.SINGLE_LINE_TEXT);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setRegExp(regExp);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTextPreset(textPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("single line text input field " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createSingleLineTextField(String name, String title) throws Exception {
		return createSingleLineTextField(name, null, title, null, null, null, null);
	}

	public InputFieldOutVO createAutoCompleteField(String name, String category, String title, String comment, boolean learn, boolean strict) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.AUTOCOMPLETE);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setLearn(learn);
		newInputField.setStrict(strict);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("autocomplete input field " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createAutoCompleteField(String name, String title) throws Exception {
		return createAutoCompleteField(name, null, title, null, true, false);
	}

	public InputFieldOutVO createSketchField(String name, String category, String title, String comment, String resourceFileName,
			TreeMap<InputFieldValuesEnum, InkStroke> inkRegions,
			Integer minSelections, Integer maxSelections, String validationErrorMessage) throws Throwable {
		InputFieldInVO newInputField = new InputFieldInVO();
		ClassPathResource resource = new ClassPathResource("/" + resourceFileName);
		byte[] data = CommonUtil.inputStreamToByteArray(resource.getInputStream());
		newInputField.setFieldType(InputFieldType.SKETCH);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinSelections(minSelections);
		newInputField.setMaxSelections(maxSelections);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setDatas(data);
		newInputField.setMimeType(CommonUtil.getMimeType(data, resource.getFilename()));
		newInputField.setFileName(resource.getFilename());
		InputFieldOutVO inputField = inputFieldService.addInputField(auth, newInputField);
		info("select many input field " + inputField.getId() + " created: " + inputField.getName());
		addInkRegions(inputField, inkRegions);
		return inputField;
	}

	public InputFieldOutVO createSketchField(String name, String title, TreeMap<InputFieldValuesEnum, InkStroke> inkRegions) throws Throwable {
		return createSketchField(name, null, title, null, null, inkRegions, null, null, null);
	}

	public InputFieldOutVO createTimeField(String name, String category, String title, String comment, Date timePreset, Date minTime, Date maxTime,
			String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.TIME);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinTime(minTime);
		newInputField.setMaxTime(maxTime);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTimePreset(timePreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("time input field " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createTimeField(String name, String title) throws Exception {
		return createTimeField(name, null, title, null, null, null, null, null);
	}

	public InputFieldOutVO createTimestampField(String name, String category, String title, String comment, Date timestampPreset, Date minTimestamp,
			Date maxTimestamp, boolean isUserTimeZone, String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.TIMESTAMP);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinTimestamp(minTimestamp);
		newInputField.setMaxTimestamp(maxTimestamp);
		newInputField.setUserTimeZone(isUserTimeZone);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setTimestampPreset(timestampPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("timestamp input field " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createTimestampField(String name, String title) throws Exception {
		return createTimestampField(name, null, title, null, null, null, null, false, null);
	}

	public InputFieldOutVO createCheckBoxField(String name, String category, String title, String comment, boolean booleanPreset) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.CHECKBOX);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setBooleanPreset(booleanPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("check box input field " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createCheckBoxField(String name, String title) throws Exception {
		return createCheckBoxField(name, null, title, null, false);
	}

	public InputFieldOutVO createDateField(String name, String category, String title, String comment, Date datePreset, Date minDate, Date maxDate,
			String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.DATE);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setMinDate(minDate);
		newInputField.setMaxDate(maxDate);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setDatePreset(datePreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("date input field " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createDateField(String name, String title) throws Exception {
		return createDateField(name, null, title, null, null, null, null, null);
	}

	public InputFieldOutVO createFloatField(String name, String category, String title, String comment, Float floatPreset, Float lowerLimit,
			Float upperLimit, String validationErrorMessage) throws Exception {
		InputFieldInVO newInputField = new InputFieldInVO();
		newInputField.setFieldType(InputFieldType.FLOAT);
		newInputField.setName(name);
		newInputField.setTitle(title);
		newInputField.setCategory(category);
		newInputField.setComment(comment);
		newInputField.setFloatLowerLimit(lowerLimit);
		newInputField.setFloatUpperLimit(upperLimit);
		newInputField.setValidationErrorMsg(validationErrorMessage);
		newInputField.setFloatPreset(floatPreset);
		InputFieldOutVO out = inputFieldService.addInputField(auth, newInputField);
		info("float input field ID " + out.getId() + " created: " + out.getName());
		return out;
	}

	public InputFieldOutVO createFloatField(String name, String title) throws Exception {
		return createFloatField(name, null, title, null, null, null, null, null);
	}

	public CriteriaOutVO createCriteria(CriteriaInVO newCriteria, List<SearchCriterion> criterions)
			throws Exception {
		LinkedHashSet<CriterionInVO> newCriterions = new LinkedHashSet<CriterionInVO>(criterions.size());
		Iterator<SearchCriterion> it = criterions.iterator();
		int position = 1;
		while (it.hasNext()) {
			SearchCriterion criterion = it.next();
			newCriterions.add(criterion.buildCriterionInVO(this, newCriteria.getModule(), position));
			position++;
		}
		CriteriaOutVO out = searchService.addCriteria(auth, newCriteria, newCriterions);
		info("criteria ID " + out.getId() + " created: " + out.getLabel());
		searchService.getIntermediateSetsByCriteria(auth, out.getId(), null);
		info(searchService.getIntermediateSetsByCriteria(auth, out.getId(), null).getParsed().getCriterionExpression());
		return out;
	}

	public CriterionTie getCriterionTie(org.phoenixctms.ctsms.enumeration.CriterionTie junction) {
		return junction == null ? null : criterionTieDao.searchUniqueTie(junction);
	}

	public CriterionProperty getCriterionProperty(String property, DBModule module) {
		return CommonUtil.isEmptyString(property) ? null
				: (new ArrayList<CriterionProperty>(criterionPropertyDao.search(new Search(new SearchParameter[] {
						new SearchParameter("module", module, SearchParameter.EQUAL_COMPARATOR),
						new SearchParameter("property", property, SearchParameter.EQUAL_COMPARATOR),
				})))).iterator().next();
	}

	public CriterionRestriction getCriterionRestriction(org.phoenixctms.ctsms.enumeration.CriterionRestriction operator) {
		return operator == null ? null : criterionRestrictionDao.searchUniqueRestriction(operator);
	}

	public InputFieldSelectionSetValue getInputFieldValue(Long fieldId, InputFieldValuesEnum value) {
		return inputFieldSelectionSetValueDao.findByFieldNameL10nKeyLocalized(fieldId, value.toString(), false).iterator().next();
	}
	//	public MimeType createMimeType(FileModule module, String mimeTypeString, String fileNameExtensions, String nodeStyleClass) {
	//		MimeType mimeType = MimeType.Factory.newInstance();
	//		mimeType.setModule(module);
	//		mimeType.setMimeType(mimeTypeString);
	//		mimeType.setFileNameExtensions(fileNameExtensions);
	//		mimeType.setNodeStyleClass(nodeStyleClass);
	//		mimeType = mimeTypeDao.create(mimeType);
	//		return mimeType;
	//	}
	//
	//	public MimeType createMimeType(FileModule module, String mimeTypeString, String fileNameExtensions, String fileNameExtensionsPattern, String nodeStyleClass) {
	//		return createMimeType(module, mimeTypeString, CommonUtil.FILE_EXTENSION_REGEXP_MODE ? fileNameExtensionsPattern : fileNameExtensions, nodeStyleClass);
	//	}
	//
	//	public void createMimeTypes() {
	//		createMimeTypes(FileModule.INVENTORY_DOCUMENT);
	//		createMimeTypes(FileModule.STAFF_DOCUMENT);
	//		createMimeTypes(FileModule.COURSE_DOCUMENT);
	//		createMimeTypes(FileModule.TRIAL_DOCUMENT);
	//		createMimeTypes(FileModule.PROBAND_DOCUMENT);
	//		createMimeTypes(FileModule.INPUT_FIELD_IMAGE);
	//		createMimeTypes(FileModule.MASS_MAIL_DOCUMENT);
	//		createMimeTypes(FileModule.STAFF_IMAGE);
	//		createMimeTypes(FileModule.PROBAND_IMAGE);
	//		createMimeTypes(FileModule.JOB_FILE);
	//		createMimeTypes(FileModule.COURSE_CERTIFICATE);
	//	}
	//
	//	public void createMimeTypes(FileModule module) {
	//		// http://svn.apache.org/viewvc/httpd/httpd/trunk/docs/conf/mime.types?view=co
	//		createMimeType(module, "application/andrew-inset", "*.ez", "ez", "ctsms-mimetype-file");
	//		createMimeType(module, "application/applixware", "*.aw", "aw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/atom+xml", "*.atom", "atom", "ctsms-mimetype-file");
	//		createMimeType(module, "application/atomcat+xml", "*.atomcat", "atomcat", "ctsms-mimetype-file");
	//		createMimeType(module, "application/atomsvc+xml", "*.atomsvc", "atomsvc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/ccxml+xml", "*.ccxml", "ccxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/cdmi-capability", "*.cdmia", "cdmia", "ctsms-mimetype-file");
	//		createMimeType(module, "application/cdmi-container", "*.cdmic", "cdmic", "ctsms-mimetype-file");
	//		createMimeType(module, "application/cdmi-domain", "*.cdmid", "cdmid", "ctsms-mimetype-file");
	//		createMimeType(module, "application/cdmi-object", "*.cdmio", "cdmio", "ctsms-mimetype-file");
	//		createMimeType(module, "application/cdmi-queue", "*.cdmiq", "cdmiq", "ctsms-mimetype-file");
	//		createMimeType(module, "application/cu-seeme", "*.cu", "cu", "ctsms-mimetype-file");
	//		createMimeType(module, "application/davmount+xml", "*.davmount", "davmount", "ctsms-mimetype-file");
	//		createMimeType(module, "application/dssc+der", "*.dssc", "dssc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/dssc+xml", "*.xdssc", "xdssc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/ecmascript", "*.ecma", "ecma", "ctsms-mimetype-file");
	//		createMimeType(module, "application/emma+xml", "*.emma", "emma", "ctsms-mimetype-file");
	//		createMimeType(module, "application/epub+zip", "*.epub", "epub", "ctsms-mimetype-file");
	//		createMimeType(module, "application/exi", "*.exi", "exi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/font-tdpfr", "*.pfr", "pfr", "ctsms-mimetype-file");
	//		createMimeType(module, "application/hyperstudio", "*.stk", "stk", "ctsms-mimetype-file");
	//		createMimeType(module, "application/ipfix", "*.ipfix", "ipfix", "ctsms-mimetype-file");
	//		createMimeType(module, "application/java-archive", "*.jar", "jar", "ctsms-mimetype-file");
	//		createMimeType(module, "application/java-serialized-object", "*.ser", "ser", "ctsms-mimetype-file");
	//		createMimeType(module, "application/java-vm", "*.class", "class", "ctsms-mimetype-file");
	//		createMimeType(module, "application/javascript", "*.js", "js", "ctsms-mimetype-file");
	//		createMimeType(module, "application/json", "*.json", "json", "ctsms-mimetype-file");
	//		createMimeType(module, "application/lost+xml", "*.lostxml", "lostxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mac-binhex40", "*.hqx", "hqx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mac-compactpro", "*.cpt", "cpt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mads+xml", "*.mads", "mads", "ctsms-mimetype-file");
	//		createMimeType(module, "application/marc", "*.mrc", "mrc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/marcxml+xml", "*.mrcx", "mrcx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mathematica", "*.ma;*.nb;*.mb", "ma|nb|mb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mathml+xml", "*.mathml", "mathml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mbox", "*.mbox", "mbox", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mediaservercontrol+xml", "*.mscml", "mscml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/metalink4+xml", "*.meta4", "meta4", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mets+xml", "*.mets", "mets", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mods+xml", "*.mods", "mods", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mp21", "*.m21;*.mp21", "m21|mp21", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mp4", "*.mp4s", "mp4s", "ctsms-mimetype-file");
	//		createMimeType(module, "application/msword", "*.doc;*.dot", "doc|dot", "ctsms-mimetype-file");
	//		createMimeType(module, "application/mxf", "*.mxf", "mxf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/octet-stream", "*.bin;*.dms;*.lha;*.lrf;*.lzh;*.so;*.iso;*.dmg;*.dist;*.distz;*.pkg;*.bpk;*.dump;*.elc;*.deploy",
	//				"bin|dms|lha|lrf|lzh|so|iso|dmg|dist|distz|pkg|bpk|dump|elc|deploy", "ctsms-mimetype-file");
	//		createMimeType(module, "application/oda", "*.oda", "oda", "ctsms-mimetype-file");
	//		createMimeType(module, "application/oebps-package+xml", "*.opf", "opf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/ogg", "*.ogx", "ogx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/onenote", "*.onetoc;*.onetoc2;*.onetmp;*.onepkg", "onetoc|onetoc2|onetmp|onepkg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/patch-ops-error+xml", "*.xer", "xer", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pdf", "*.pdf", "pdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pgp-encrypted", "*.pgp", "pgp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pgp-signature", "*.asc;*.sig", "asc|sig", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pics-rules", "*.prf", "prf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkcs10", "*.p10", "p10", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkcs7-mime", "*.p7m;*.p7c", "p7m|p7c", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkcs7-signature", "*.p7s", "p7s", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkcs8", "*.p8", "p8", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkix-attr-cert", "*.ac", "ac", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkix-cert", "*.cer", "cer", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkix-crl", "*.crl", "crl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkix-pkipath", "*.pkipath", "pkipath", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pkixcmp", "*.pki", "pki", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pls+xml", "*.pls", "pls", "ctsms-mimetype-file");
	//		createMimeType(module, "application/postscript", "*.ai;*.eps;*.ps", "ai|eps|ps", "ctsms-mimetype-file");
	//		createMimeType(module, "application/prs.cww", "*.cww", "cww", "ctsms-mimetype-file");
	//		createMimeType(module, "application/pskc+xml", "*.pskcxml", "pskcxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/rdf+xml", "*.rdf", "rdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/reginfo+xml", "*.rif", "rif", "ctsms-mimetype-file");
	//		createMimeType(module, "application/relax-ng-compact-syntax", "*.rnc", "rnc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/resource-lists+xml", "*.rl", "rl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/resource-lists-diff+xml", "*.rld", "rld", "ctsms-mimetype-file");
	//		createMimeType(module, "application/rls-services+xml", "*.rs", "rs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/rsd+xml", "*.rsd", "rsd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/rss+xml", "*.rss", "rss", "ctsms-mimetype-file");
	//		createMimeType(module, "application/rtf", "*.rtf", "rtf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/sbml+xml", "*.sbml", "sbml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/scvp-cv-request", "*.scq", "scq", "ctsms-mimetype-file");
	//		createMimeType(module, "application/scvp-cv-response", "*.scs", "scs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/scvp-vp-request", "*.spq", "spq", "ctsms-mimetype-file");
	//		createMimeType(module, "application/scvp-vp-response", "*.spp", "spp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/sdp", "*.sdp", "sdp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/set-payment-initiation", "*.setpay", "setpay", "ctsms-mimetype-file");
	//		createMimeType(module, "application/set-registration-initiation", "*.setreg", "setreg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/shf+xml", "*.shf", "shf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/smil+xml", "*.smi;*.smil", "smi|smil", "ctsms-mimetype-file");
	//		createMimeType(module, "application/sparql-query", "*.rq", "rq", "ctsms-mimetype-file");
	//		createMimeType(module, "application/sparql-results+xml", "*.srx", "srx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/srgs", "*.gram", "gram", "ctsms-mimetype-file");
	//		createMimeType(module, "application/srgs+xml", "*.grxml", "grxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/sru+xml", "*.sru", "sru", "ctsms-mimetype-file");
	//		createMimeType(module, "application/ssml+xml", "*.ssml", "ssml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/tei+xml", "*.tei;*.teicorpus", "tei|teicorpus", "ctsms-mimetype-file");
	//		createMimeType(module, "application/thraud+xml", "*.tfi", "tfi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/timestamped-data", "*.tsd", "tsd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.3gpp.pic-bw-large", "*.plb", "plb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.3gpp.pic-bw-small", "*.psb", "psb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.3gpp.pic-bw-var", "*.pvb", "pvb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.3gpp2.tcap", "*.tcap", "tcap", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.3m.post-it-notes", "*.pwn", "pwn", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.accpac.simply.aso", "*.aso", "aso", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.accpac.simply.imp", "*.imp", "imp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.acucobol", "*.acu", "acu", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.acucorp", "*.atc;*.acutc", "atc|acutc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.adobe.air-application-installer-package+zip", "*.air", "air", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.adobe.fxp", "*.fxp;*.fxpl", "fxp|fxpl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.adobe.xdp+xml", "*.xdp", "xdp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.adobe.xfdf", "*.xfdf", "xfdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ahead.space", "*.ahead", "ahead", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.airzip.filesecure.azf", "*.azf", "azf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.airzip.filesecure.azs", "*.azs", "azs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.amazon.ebook", "*.azw", "azw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.americandynamics.acc", "*.acc", "acc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.amiga.ami", "*.ami", "ami", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.android.package-archive", "*.apk", "apk", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.anser-web-certificate-issue-initiation", "*.cii", "cii", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.anser-web-funds-transfer-initiation", "*.fti", "fti", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.antix.game-component", "*.atx", "atx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.apple.installer+xml", "*.mpkg", "mpkg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.apple.mpegurl", "*.m3u8", "m3u8", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.aristanetworks.swi", "*.swi", "swi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.audiograph", "*.aep", "aep", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.blueice.multipass", "*.mpm", "mpm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.bmi", "*.bmi", "bmi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.businessobjects", "*.rep", "rep", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.chemdraw+xml", "*.cdxml", "cdxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.chipnuts.karaoke-mmd", "*.mmd", "mmd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.cinderella", "*.cdy", "cdy", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.claymore", "*.cla", "cla", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.cloanto.rp9", "*.rp9", "rp9", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.clonk.c4group", "*.c4g;*.c4d;*.c4f;*.c4p;*.c4u", "c4g|c4d|c4f|c4p|c4u", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.cluetrust.cartomobile-config", "*.c11amc", "c11amc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.cluetrust.cartomobile-config-pkg", "*.c11amz", "c11amz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.commonspace", "*.csp", "csp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.contact.cmsg", "*.cdbcmsg", "cdbcmsg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.cosmocaller", "*.cmc", "cmc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.crick.clicker", "*.clkx", "clkx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.crick.clicker.keyboard", "*.clkk", "clkk", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.crick.clicker.palette", "*.clkp", "clkp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.crick.clicker.template", "*.clkt", "clkt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.crick.clicker.wordbank", "*.clkw", "clkw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.criticaltools.wbs+xml", "*.wbs", "wbs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ctc-posml", "*.pml", "pml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.cups-ppd", "*.ppd", "ppd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.curl.car", "*.car", "car", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.curl.pcurl", "*.pcurl", "pcurl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.data-vision.rdz", "*.rdz", "rdz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dece.data", "*.uvf;*.uvvf;*.uvd;*.uvvd", "uvf|uvvf|uvd|uvvd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dece.ttml+xml", "*.uvt;*.uvvt", "uvt|uvvt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dece.unspecified", "*.uvx;*.uvvx", "uvx|uvvx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.denovo.fcselayout-link", "*.fe_launch", "fe_launch", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dna", "*.dna", "dna", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dolby.mlp", "*.mlp", "mlp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dpgraph", "*.dpg", "dpg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dreamfactory", "*.dfac", "dfac", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dvb.ait", "*.ait", "ait", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dvb.service", "*.svc", "svc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.dynageo", "*.geo", "geo", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ecowin.chart", "*.mag", "mag", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.enliven", "*.nml", "nml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.epson.esf", "*.esf", "esf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.epson.msf", "*.msf", "msf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.epson.quickanime", "*.qam", "qam", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.epson.salt", "*.slt", "slt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.epson.ssf", "*.ssf", "ssf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.eszigno3+xml", "*.es3;*.et3", "es3|et3", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ezpix-album", "*.ez2", "ez2", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ezpix-package", "*.ez3", "ez3", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fdf", "*.fdf", "fdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fdsn.mseed", "*.mseed", "mseed", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fdsn.seed", "*.seed;*.dataless", "seed|dataless", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.flographit", "*.gph", "gph", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fluxtime.clip", "*.ftc", "ftc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.framemaker", "*.fm;*.frame;*.maker;*.book", "fm|frame|maker|book", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.frogans.fnc", "*.fnc", "fnc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.frogans.ltf", "*.ltf", "ltf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fsc.weblaunch", "*.fsc", "fsc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujitsu.oasys", "*.oas", "oas", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujitsu.oasys2", "*.oa2", "oa2", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujitsu.oasys3", "*.oa3", "oa3", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujitsu.oasysgp", "*.fg5", "fg5", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujitsu.oasysprs", "*.bh2", "bh2", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujixerox.ddd", "*.ddd", "ddd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujixerox.docuworks", "*.xdw", "xdw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fujixerox.docuworks.binder", "*.xbd", "xbd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.fuzzysheet", "*.fzs", "fzs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.genomatix.tuxedo", "*.txd", "txd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.geogebra.file", "*.ggb", "ggb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.geogebra.tool", "*.ggt", "ggt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.geometry-explorer", "*.gex;*.gre", "gex|gre", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.geonext", "*.gxt", "gxt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.geoplan", "*.g2w", "g2w", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.geospace", "*.g3w", "g3w", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.gmx", "*.gmx", "gmx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.google-earth.kml+xml", "*.kml", "kml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.google-earth.kmz", "*.kmz", "kmz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.grafeq", "*.gqf;*.gqs", "gqf|gqs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.groove-account", "*.gac", "gac", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.groove-help", "*.ghf", "ghf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.groove-identity-message", "*.gim", "gim", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.groove-injector", "*.grv", "grv", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.groove-tool-message", "*.gtm", "gtm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.groove-tool-template", "*.tpl", "tpl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.groove-vcard", "*.vcg", "vcg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hal+xml", "*.hal", "hal", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.handheld-entertainment+xml", "*.zmm", "zmm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hbci", "*.hbci", "hbci", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hhe.lesson-player", "*.les", "les", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hp-hpgl", "*.hpgl", "hpgl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hp-hpid", "*.hpid", "hpid", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hp-hps", "*.hps", "hps", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hp-jlyt", "*.jlt", "jlt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hp-pcl", "*.pcl", "pcl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hp-pclxl", "*.pclxl", "pclxl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hydrostatix.sof-data", "*.sfd-hdstx", "sfd-hdstx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.hzn-3d-crossword", "*.x3d", "x3d", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ibm.minipay", "*.mpy", "mpy", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ibm.modcap", "*.afp;*.listafp;*.list3820", "afp|listafp|list3820", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ibm.rights-management", "*.irm", "irm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ibm.secure-container", "*.sc", "sc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.iccprofile", "*.icc;*.icm", "icc|icm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.igloader", "*.igl", "igl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.immervision-ivp", "*.ivp", "ivp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.immervision-ivu", "*.ivu", "ivu", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.insors.igm", "*.igm", "igm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.intercon.formnet", "*.xpw;*.xpx", "xpw|xpx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.intergeo", "*.i2g", "i2g", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.intu.qbo", "*.qbo", "qbo", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.intu.qfx", "*.qfx", "qfx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ipunplugged.rcprofile", "*.rcprofile", "rcprofile", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.irepository.package+xml", "*.irp", "irp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.is-xpr", "*.xpr", "xpr", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.isac.fcs", "*.fcs", "fcs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.jam", "*.jam", "jam", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.jcp.javame.midlet-rms", "*.rms", "rms", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.jisp", "*.jisp", "jisp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.joost.joda-archive", "*.joda", "joda", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kahootz", "*.ktz;*.ktr", "ktz|ktr", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.karbon", "*.karbon", "karbon", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.kchart", "*.chrt", "chrt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.kformula", "*.kfo", "kfo", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.kivio", "*.flw", "flw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.kontour", "*.kon", "kon", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.kpresenter", "*.kpr;*.kpt", "kpr|kpt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.kspread", "*.ksp", "ksp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kde.kword", "*.kwd;*.kwt", "kwd|kwt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kenameaapp", "*.htke", "htke", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kidspiration", "*.kia", "kia", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kinar", "*.kne;*.knp", "kne|knp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.koan", "*.skp;*.skd;*.skt;*.skm", "skp|skd|skt|skm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.kodak-descriptor", "*.sse", "sse", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.las.las+xml", "*.lasxml", "lasxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.llamagraphics.life-balance.desktop", "*.lbd", "lbd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.llamagraphics.life-balance.exchange+xml", "*.lbe", "lbe", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.lotus-1-2-3", "*.123", "123", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.lotus-approach", "*.apr", "apr", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.lotus-freelance", "*.pre", "pre", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.lotus-notes", "*.nsf", "nsf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.lotus-organizer", "*.org", "org", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.lotus-screencam", "*.scm", "scm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.lotus-wordpro", "*.lwp", "lwp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.macports.portpkg", "*.portpkg", "portpkg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mcd", "*.mcd", "mcd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.medcalcdata", "*.mc1", "mc1", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mediastation.cdkey", "*.cdkey", "cdkey", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mfer", "*.mwf", "mwf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mfmp", "*.mfm", "mfm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.micrografx.flo", "*.flo", "flo", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.micrografx.igx", "*.igx", "igx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mif", "*.mif", "mif", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mobius.daf", "*.daf", "daf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mobius.dis", "*.dis", "dis", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mobius.mbk", "*.mbk", "mbk", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mobius.mqy", "*.mqy", "mqy", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mobius.msl", "*.msl", "msl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mobius.plc", "*.plc", "plc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mobius.txf", "*.txf", "txf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mophun.application", "*.mpn", "mpn", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mophun.certificate", "*.mpc", "mpc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mozilla.xul+xml", "*.xul", "xul", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-artgalry", "*.cil", "cil", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-cab-compressed", "*.cab", "cab", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-excel", "*.xls;*.xlm;*.xla;*.xlc;*.xlt;*.xlw", "xls|xlm|xla|xlc|xlt|xlw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-excel.addin.macroenabled.12", "*.xlam", "xlam", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-excel.sheet.binary.macroenabled.12", "*.xlsb", "xlsb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-excel.sheet.macroenabled.12", "*.xlsm", "xlsm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-excel.template.macroenabled.12", "*.xltm", "xltm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-fontobject", "*.eot", "eot", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-htmlhelp", "*.chm", "chm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-ims", "*.ims", "ims", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-lrm", "*.lrm", "lrm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-officetheme", "*.thmx", "thmx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-pki.seccat", "*.cat", "cat", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-pki.stl", "*.stl", "stl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-powerpoint", "*.ppt;*.pps;*.pot", "ppt|pps|pot", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-powerpoint.addin.macroenabled.12", "*.ppam", "ppam", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-powerpoint.presentation.macroenabled.12", "*.pptm", "pptm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-powerpoint.slide.macroenabled.12", "*.sldm", "sldm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-powerpoint.slideshow.macroenabled.12", "*.ppsm", "ppsm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-powerpoint.template.macroenabled.12", "*.potm", "potm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-project", "*.mpp;*.mpt", "mpp|mpt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-word.document.macroenabled.12", "*.docm", "docm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-word.template.macroenabled.12", "*.dotm", "dotm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-works", "*.wps;*.wks;*.wcm;*.wdb", "wps|wks|wcm|wdb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-wpl", "*.wpl", "wpl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ms-xpsdocument", "*.xps", "xps", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.mseq", "*.mseq", "mseq", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.musician", "*.mus", "mus", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.muvee.style", "*.msty", "msty", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.neurolanguage.nlu", "*.nlu", "nlu", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.noblenet-directory", "*.nnd", "nnd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.noblenet-sealer", "*.nns", "nns", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.noblenet-web", "*.nnw", "nnw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.nokia.n-gage.data", "*.ngdat", "ngdat", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.nokia.n-gage.symbian.install", "*.n-gage", "n-gage", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.nokia.radio-preset", "*.rpst", "rpst", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.nokia.radio-presets", "*.rpss", "rpss", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.novadigm.edm", "*.edm", "edm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.novadigm.edx", "*.edx", "edx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.novadigm.ext", "*.ext", "ext", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.chart", "*.odc", "odc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.chart-template", "*.otc", "otc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.database", "*.odb", "odb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.formula", "*.odf", "odf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.formula-template", "*.odft", "odft", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.graphics", "*.odg", "odg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.graphics-template", "*.otg", "otg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.image", "*.odi", "odi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.image-template", "*.oti", "oti", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.presentation", "*.odp", "odp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.presentation-template", "*.otp", "otp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.spreadsheet", "*.ods", "ods", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.spreadsheet-template", "*.ots", "ots", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.text", "*.odt", "odt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.text-master", "*.odm", "odm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.text-template", "*.ott", "ott", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oasis.opendocument.text-web", "*.oth", "oth", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.olpc-sugar", "*.xo", "xo", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.oma.dd2+xml", "*.dd2", "dd2", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openofficeorg.extension", "*.oxt", "oxt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.presentationml.presentation", "*.pptx", "pptx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.presentationml.slide", "*.sldx", "sldx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.presentationml.slideshow", "*.ppsx", "ppsx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.presentationml.template", "*.potx", "potx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "*.xlsx", "xlsx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.spreadsheetml.template", "*.xltx", "xltx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "*.docx", "docx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.openxmlformats-officedocument.wordprocessingml.template", "*.dotx", "dotx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.osgeo.mapguide.package", "*.mgp", "mgp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.osgi.dp", "*.dp", "dp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.palm", "*.pdb;*.pqa;*.oprc", "pdb|pqa|oprc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.pawaafile", "*.paw", "paw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.pg.format", "*.str", "str", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.pg.osasli", "*.ei6", "ei6", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.picsel", "*.efif", "efif", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.pmi.widget", "*.wg", "wg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.pocketlearn", "*.plf", "plf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.powerbuilder6", "*.pbd", "pbd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.previewsystems.box", "*.box", "box", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.proteus.magazine", "*.mgz", "mgz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.publishare-delta-tree", "*.qps", "qps", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.pvi.ptid1", "*.ptid", "ptid", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.quark.quarkxpress", "*.qxd;*.qxt;*.qwd;*.qwt;*.qxl;*.qxb", "qxd|qxt|qwd|qwt|qxl|qxb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.realvnc.bed", "*.bed", "bed", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.recordare.musicxml", "*.mxl", "mxl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.recordare.musicxml+xml", "*.musicxml", "musicxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.rig.cryptonote", "*.cryptonote", "cryptonote", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.rim.cod", "*.cod", "cod", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.rn-realmedia", "*.rm", "rm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.route66.link66+xml", "*.link66", "link66", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sailingtracker.track", "*.st", "st", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.seemail", "*.see", "see", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sema", "*.sema", "sema", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.semd", "*.semd", "semd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.semf", "*.semf", "semf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.shana.informed.formdata", "*.ifm", "ifm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.shana.informed.formtemplate", "*.itp", "itp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.shana.informed.interchange", "*.iif", "iif", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.shana.informed.package", "*.ipk", "ipk", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.simtech-mindmapper", "*.twd;*.twds", "twd|twds", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.smaf", "*.mmf", "mmf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.smart.teacher", "*.teacher", "teacher", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.solent.sdkm+xml", "*.sdkm;*.sdkd", "sdkm|sdkd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.spotfire.dxp", "*.dxp", "dxp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.spotfire.sfs", "*.sfs", "sfs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.stardivision.calc", "*.sdc", "sdc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.stardivision.draw", "*.sda", "sda", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.stardivision.impress", "*.sdd", "sdd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.stardivision.math", "*.smf", "smf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.stardivision.writer", "*.sdw;*.vor", "sdw|vor", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.stardivision.writer-global", "*.sgl", "sgl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.stepmania.stepchart", "*.sm", "sm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.calc", "*.sxc", "sxc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.calc.template", "*.stc", "stc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.draw", "*.sxd", "sxd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.draw.template", "*.std", "std", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.impress", "*.sxi", "sxi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.impress.template", "*.sti", "sti", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.math", "*.sxm", "sxm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.writer", "*.sxw", "sxw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.writer.global", "*.sxg", "sxg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sun.xml.writer.template", "*.stw", "stw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.sus-calendar", "*.sus;*.susp", "sus|susp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.svd", "*.svd", "svd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.symbian.install", "*.sis;*.sisx", "sis|sisx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.syncml+xml", "*.xsm", "xsm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.syncml.dm+wbxml", "*.bdm", "bdm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.syncml.dm+xml", "*.xdm", "xdm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.tao.intent-module-archive", "*.tao", "tao", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.tmobile-livetv", "*.tmo", "tmo", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.trid.tpt", "*.tpt", "tpt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.triscape.mxs", "*.mxs", "mxs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.trueapp", "*.tra", "tra", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.ufdl", "*.ufd;*.ufdl", "ufd|ufdl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.uiq.theme", "*.utz", "utz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.umajin", "*.umj", "umj", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.unity", "*.unityweb", "unityweb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.uoml+xml", "*.uoml", "uoml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.vcx", "*.vcx", "vcx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.visio", "*.vsd;*.vst;*.vss;*.vsw", "vsd|vst|vss|vsw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.visionary", "*.vis", "vis", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.vsf", "*.vsf", "vsf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.wap.wbxml", "*.wbxml", "wbxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.wap.wmlc", "*.wmlc", "wmlc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.wap.wmlscriptc", "*.wmlsc", "wmlsc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.webturbo", "*.wtb", "wtb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.wolfram.player", "*.nbp", "nbp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.wordperfect", "*.wpd", "wpd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.wqd", "*.wqd", "wqd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.wt.stf", "*.stf", "stf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.xara", "*.xar", "xar", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.xfdl", "*.xfdl", "xfdl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yamaha.hv-dic", "*.hvd", "hvd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yamaha.hv-script", "*.hvs", "hvs", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yamaha.hv-voice", "*.hvp", "hvp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yamaha.openscoreformat", "*.osf", "osf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yamaha.openscoreformat.osfpvg+xml", "*.osfpvg", "osfpvg", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yamaha.smaf-audio", "*.saf", "saf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yamaha.smaf-phrase", "*.spf", "spf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.yellowriver-custom-menu", "*.cmp", "cmp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.zul", "*.zir;*.zirz", "zir|zirz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/vnd.zzazz.deck+xml", "*.zaz", "zaz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/voicexml+xml", "*.vxml", "vxml", "ctsms-mimetype-file");
	//		createMimeType(module, "application/widget", "*.wgt", "wgt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/winhlp", "*.hlp", "hlp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/wsdl+xml", "*.wsdl", "wsdl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/wspolicy+xml", "*.wspolicy", "wspolicy", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-7z-compressed", "*.7z", "7z", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-abiword", "*.abw", "abw", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-ace-compressed", "*.ace", "ace", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-authorware-bin", "*.aab;*.x32;*.u32;*.vox", "aab|x32|u32|vox", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-authorware-map", "*.aam", "aam", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-authorware-seg", "*.aas", "aas", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-bcpio", "*.bcpio", "bcpio", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-bittorrent", "*.torrent", "torrent", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-bzip", "*.bz", "bz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-bzip2", "*.bz2;*.boz", "bz2|boz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-cdlink", "*.vcd", "vcd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-chat", "*.chat", "chat", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-chess-pgn", "*.pgn", "pgn", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-cpio", "*.cpio", "cpio", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-csh", "*.csh", "csh", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-debian-package", "*.deb;*.udeb", "deb|udeb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-director", "*.dir;*.dcr;*.dxr;*.cst;*.cct;*.cxt;*.w3d;*.fgd;*.swa", "dir|dcr|dxr|cst|cct|cxt|w3d|fgd|swa", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-doom", "*.wad", "wad", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-dtbncx+xml", "*.ncx", "ncx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-dtbook+xml", "*.dtb", "dtb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-dtbresource+xml", "*.res", "res", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-dvi", "*.dvi", "dvi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-bdf", "*.bdf", "bdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-ghostscript", "*.gsf", "gsf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-linux-psf", "*.psf", "psf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-otf", "*.otf", "otf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-pcf", "*.pcf", "pcf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-snf", "*.snf", "snf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-ttf", "*.ttf;*.ttc", "ttf|ttc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-type1", "*.pfa;*.pfb;*.pfm;*.afm", "pfa|pfb|pfm|afm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-font-woff", "*.woff", "woff", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-futuresplash", "*.spl", "spl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-gnumeric", "*.gnumeric", "gnumeric", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-gtar", "*.gtar", "gtar", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-hdf", "*.hdf", "hdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-java-jnlp-file", "*.jnlp", "jnlp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-latex", "*.latex", "latex", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-mobipocket-ebook", "*.prc;*.mobi", "prc|mobi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-ms-application", "*.application", "application", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-ms-wmd", "*.wmd", "wmd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-ms-wmz", "*.wmz", "wmz", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-ms-xbap", "*.xbap", "xbap", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msaccess", "*.mdb", "mdb", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msbinder", "*.obd", "obd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-mscardfile", "*.crd", "crd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msclip", "*.clp", "clp", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msdownload", "*.exe;*.dll;*.com;*.bat;*.msi", "exe|dll|com|bat|msi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msmediaview", "*.mvb;*.m13;*.m14", "mvb|m13|m14", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msmetafile", "*.wmf", "wmf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msmoney", "*.mny", "mny", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-mspublisher", "*.pub", "pub", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msschedule", "*.scd", "scd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-msterminal", "*.trm", "trm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-mswrite", "*.wri", "wri", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-netcdf", "*.nc;*.cdf", "nc|cdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-pkcs12", "*.p12;*.pfx", "p12|pfx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-pkcs7-certificates", "*.p7b;*.spc", "p7b|spc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-pkcs7-certreqresp", "*.p7r", "p7r", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-rar-compressed", "*.rar", "rar", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-sh", "*.sh", "sh", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-shar", "*.shar", "shar", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-shockwave-flash", "*.swf", "swf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-silverlight-app", "*.xap", "xap", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-stuffit", "*.sit", "sit", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-stuffitx", "*.sitx", "sitx", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-sv4cpio", "*.sv4cpio", "sv4cpio", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-sv4crc", "*.sv4crc", "sv4crc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-tar", "*.tar", "tar", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-tcl", "*.tcl", "tcl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-tex", "*.tex", "tex", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-tex-tfm", "*.tfm", "tfm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-texinfo", "*.texinfo;*.texi", "texinfo|texi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-ustar", "*.ustar", "ustar", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-wais-source", "*.src", "src", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-x509-ca-cert", "*.der;*.crt", "der|crt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-xfig", "*.fig", "fig", "ctsms-mimetype-file");
	//		createMimeType(module, "application/x-xpinstall", "*.xpi", "xpi", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xcap-diff+xml", "*.xdf", "xdf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xenc+xml", "*.xenc", "xenc", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xhtml+xml", "*.xhtml;*.xht", "xhtml|xht", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xml", "*.xml;*.xsl", "xml|xsl", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xml-dtd", "*.dtd", "dtd", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xop+xml", "*.xop", "xop", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xslt+xml", "*.xslt", "xslt", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xspf+xml", "*.xspf", "xspf", "ctsms-mimetype-file");
	//		createMimeType(module, "application/xv+xml", "*.mxml;*.xhvml;*.xvml;*.xvm", "mxml|xhvml|xvml|xvm", "ctsms-mimetype-file");
	//		createMimeType(module, "application/yang", "*.yang", "yang", "ctsms-mimetype-file");
	//		createMimeType(module, "application/yin+xml", "*.yin", "yin", "ctsms-mimetype-file");
	//		createMimeType(module, "application/zip", "*.zip", "zip", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/adpcm", "*.adp", "adp", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/basic", "*.au;*.snd", "au|snd", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/midi", "*.mid;*.midi;*.kar;*.rmi", "mid|midi|kar|rmi", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/mp4", "*.mp4a", "mp4a", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/mpeg", "*.mpga;*.mp2;*.mp2a;*.mp3;*.m2a;*.m3a", "mpga|mp2|mp2a|mp3|m2a|m3a", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/ogg", "*.oga;*.ogg;*.spx", "oga|ogg|spx", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.dece.audio", "*.uva;*.uvva", "uva|uvva", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.digital-winds", "*.eol", "eol", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.dra", "*.dra", "dra", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.dts", "*.dts", "dts", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.dts.hd", "*.dtshd", "dtshd", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.lucent.voice", "*.lvp", "lvp", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.ms-playready.media.pya", "*.pya", "pya", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.nuera.ecelp4800", "*.ecelp4800", "ecelp4800", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.nuera.ecelp7470", "*.ecelp7470", "ecelp7470", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.nuera.ecelp9600", "*.ecelp9600", "ecelp9600", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/vnd.rip", "*.rip", "rip", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/webm", "*.weba", "weba", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-aac", "*.aac", "aac", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-aiff", "*.aif;*.aiff;*.aifc", "aif|aiff|aifc", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-mpegurl", "*.m3u", "m3u", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-ms-wax", "*.wax", "wax", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-ms-wma", "*.wma", "wma", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-pn-realaudio", "*.ram;*.ra", "ram|ra", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-pn-realaudio-plugin", "*.rmp", "rmp", "ctsms-mimetype-file");
	//		createMimeType(module, "audio/x-wav", "*.wav", "wav", "ctsms-mimetype-file");
	//		createMimeType(module, "chemical/x-cdx", "*.cdx", "cdx", "ctsms-mimetype-file");
	//		createMimeType(module, "chemical/x-cif", "*.cif", "cif", "ctsms-mimetype-file");
	//		createMimeType(module, "chemical/x-cmdf", "*.cmdf", "cmdf", "ctsms-mimetype-file");
	//		createMimeType(module, "chemical/x-cml", "*.cml", "cml", "ctsms-mimetype-file");
	//		createMimeType(module, "chemical/x-csml", "*.csml", "csml", "ctsms-mimetype-file");
	//		createMimeType(module, "chemical/x-xyz", "*.xyz", "xyz", "ctsms-mimetype-file");
	//		createMimeType(module, "image/bmp", "*.bmp", "bmp", "ctsms-mimetype-file");
	//		createMimeType(module, "image/cgm", "*.cgm", "cgm", "ctsms-mimetype-file");
	//		createMimeType(module, "image/g3fax", "*.g3", "g3", "ctsms-mimetype-file");
	//		createMimeType(module, "image/gif", "*.gif", "gif", "ctsms-mimetype-file");
	//		createMimeType(module, "image/ief", "*.ief", "ief", "ctsms-mimetype-file");
	//		createMimeType(module, "image/jpeg", "*.jpeg;*.jpg;*.jpe", "jpeg|jpg|jpe", "ctsms-mimetype-file");
	//		createMimeType(module, "image/ktx", "*.ktx", "ktx", "ctsms-mimetype-file");
	//		createMimeType(module, "image/png", "*.png", "png", "ctsms-mimetype-file");
	//		createMimeType(module, "image/prs.btif", "*.btif", "btif", "ctsms-mimetype-file");
	//		createMimeType(module, "image/svg+xml", "*.svg;*.svgz", "svg|svgz", "ctsms-mimetype-file");
	//		createMimeType(module, "image/tiff", "*.tiff;*.tif", "tiff|tif", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.adobe.photoshop", "*.psd", "psd", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.dece.graphic", "*.uvi;*.uvvi;*.uvg;*.uvvg", "uvi|uvvi|uvg|uvvg", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.dvb.subtitle", "*.sub", "sub", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.djvu", "*.djvu;*.djv", "djvu|djv", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.dwg", "*.dwg", "dwg", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.dxf", "*.dxf", "dxf", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.fastbidsheet", "*.fbs", "fbs", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.fpx", "*.fpx", "fpx", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.fst", "*.fst", "fst", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.fujixerox.edmics-mmr", "*.mmr", "mmr", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.fujixerox.edmics-rlc", "*.rlc", "rlc", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.ms-modi", "*.mdi", "mdi", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.net-fpx", "*.npx", "npx", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.wap.wbmp", "*.wbmp", "wbmp", "ctsms-mimetype-file");
	//		createMimeType(module, "image/vnd.xiff", "*.xif", "xif", "ctsms-mimetype-file");
	//		createMimeType(module, "image/webp", "*.webp", "webp", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-cmu-raster", "*.ras", "ras", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-cmx", "*.cmx", "cmx", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-freehand", "*.fh;*.fhc;*.fh4;*.fh5;*.fh7", "fh|fhc|fh4|fh5|fh7", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-icon", "*.ico", "ico", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-pcx", "*.pcx", "pcx", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-pict", "*.pic;*.pct", "pic|pct", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-portable-anymap", "*.pnm", "pnm", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-portable-bitmap", "*.pbm", "pbm", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-portable-graymap", "*.pgm", "pgm", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-portable-pixmap", "*.ppm", "ppm", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-rgb", "*.rgb", "rgb", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-xbitmap", "*.xbm", "xbm", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-xpixmap", "*.xpm", "xpm", "ctsms-mimetype-file");
	//		createMimeType(module, "image/x-xwindowdump", "*.xwd", "xwd", "ctsms-mimetype-file");
	//		createMimeType(module, "message/rfc822", "*.eml;*.mime", "eml|mime", "ctsms-mimetype-file");
	//		createMimeType(module, "model/iges", "*.igs;*.iges", "igs|iges", "ctsms-mimetype-file");
	//		createMimeType(module, "model/mesh", "*.msh;*.mesh;*.silo", "msh|mesh|silo", "ctsms-mimetype-file");
	//		createMimeType(module, "model/vnd.collada+xml", "*.dae", "dae", "ctsms-mimetype-file");
	//		createMimeType(module, "model/vnd.dwf", "*.dwf", "dwf", "ctsms-mimetype-file");
	//		createMimeType(module, "model/vnd.gdl", "*.gdl", "gdl", "ctsms-mimetype-file");
	//		createMimeType(module, "model/vnd.gtw", "*.gtw", "gtw", "ctsms-mimetype-file");
	//		createMimeType(module, "model/vnd.mts", "*.mts", "mts", "ctsms-mimetype-file");
	//		createMimeType(module, "model/vnd.vtu", "*.vtu", "vtu", "ctsms-mimetype-file");
	//		createMimeType(module, "model/vrml", "*.wrl;*.vrml", "wrl|vrml", "ctsms-mimetype-file");
	//		createMimeType(module, "text/calendar", "*.ics;*.ifb", "ics|ifb", "ctsms-mimetype-file");
	//		createMimeType(module, "text/css", "*.css", "css", "ctsms-mimetype-file");
	//		createMimeType(module, "text/csv", "*.csv", "csv", "ctsms-mimetype-file");
	//		createMimeType(module, "text/html", "*.html;*.htm", "html|htm", "ctsms-mimetype-file");
	//		createMimeType(module, "text/n3", "*.n3", "n3", "ctsms-mimetype-file");
	//		createMimeType(module, "text/plain", "*.txt;*.text;*.conf;*.def;*.list;*.log;*.in", "txt|text|conf|def|list|log|in", "ctsms-mimetype-file");
	//		createMimeType(module, "text/prs.lines.tag", "*.dsc", "dsc", "ctsms-mimetype-file");
	//		createMimeType(module, "text/richtext", "*.rtx", "rtx", "ctsms-mimetype-file");
	//		createMimeType(module, "text/sgml", "*.sgml;*.sgm", "sgml|sgm", "ctsms-mimetype-file");
	//		createMimeType(module, "text/tab-separated-values", "*.tsv", "tsv", "ctsms-mimetype-file");
	//		createMimeType(module, "text/troff", "*.t;*.tr;*.roff;*.man;*.me;*.ms", "t|tr|roff|man|me|ms", "ctsms-mimetype-file");
	//		createMimeType(module, "text/turtle", "*.ttl", "ttl", "ctsms-mimetype-file");
	//		createMimeType(module, "text/uri-list", "*.uri;*.uris;*.urls", "uri|uris|urls", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.curl", "*.curl", "curl", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.curl.dcurl", "*.dcurl", "dcurl", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.curl.scurl", "*.scurl", "scurl", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.curl.mcurl", "*.mcurl", "mcurl", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.fly", "*.fly", "fly", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.fmi.flexstor", "*.flx", "flx", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.graphviz", "*.gv", "gv", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.in3d.3dml", "*.3dml", "3dml", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.in3d.spot", "*.spot", "spot", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.sun.j2me.app-descriptor", "*.jad", "jad", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.wap.wml", "*.wml", "wml", "ctsms-mimetype-file");
	//		createMimeType(module, "text/vnd.wap.wmlscript", "*.wmls", "wmls", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-asm", "*.s;*.asm", "s|asm", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-c", "*.c;*.cc;*.cxx;*.cpp;*.h;*.hh;*.dic", "c|cc|cxx|cpp|h|hh|dic", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-fortran", "*.f;*.for;*.f77;*.f90", "f|for|f77|f90", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-pascal", "*.p;*.pas", "p|pas", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-java-source", "*.java", "java", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-setext", "*.etx", "etx", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-uuencode", "*.uu", "uu", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-vcalendar", "*.vcs", "vcs", "ctsms-mimetype-file");
	//		createMimeType(module, "text/x-vcard", "*.vcf", "vcf", "ctsms-mimetype-file");
	//		createMimeType(module, "video/3gpp", "*.3gp", "3gp", "ctsms-mimetype-file");
	//		createMimeType(module, "video/3gpp2", "*.3g2", "3g2", "ctsms-mimetype-file");
	//		createMimeType(module, "video/h261", "*.h261", "h261", "ctsms-mimetype-file");
	//		createMimeType(module, "video/h263", "*.h263", "h263", "ctsms-mimetype-file");
	//		createMimeType(module, "video/h264", "*.h264", "h264", "ctsms-mimetype-file");
	//		createMimeType(module, "video/jpeg", "*.jpgv", "jpgv", "ctsms-mimetype-file");
	//		createMimeType(module, "video/jpm", "*.jpm;*.jpgm", "jpm|jpgm", "ctsms-mimetype-file");
	//		createMimeType(module, "video/mj2", "*.mj2;*.mjp2", "mj2|mjp2", "ctsms-mimetype-file");
	//		createMimeType(module, "video/mp4", "*.mp4;*.mp4v;*.mpg4", "mp4|mp4v|mpg4", "ctsms-mimetype-file");
	//		createMimeType(module, "video/mpeg", "*.mpeg;*.mpg;*.mpe;*.m1v;*.m2v", "mpeg|mpg|mpe|m1v|m2v", "ctsms-mimetype-file");
	//		createMimeType(module, "video/ogg", "*.ogv", "ogv", "ctsms-mimetype-file");
	//		createMimeType(module, "video/quicktime", "*.qt;*.mov", "qt|mov", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.dece.hd", "*.uvh;*.uvvh", "uvh|uvvh", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.dece.mobile", "*.uvm;*.uvvm", "uvm|uvvm", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.dece.pd", "*.uvp;*.uvvp", "uvp|uvvp", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.dece.sd", "*.uvs;*.uvvs", "uvs|uvvs", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.dece.video", "*.uvv;*.uvvv", "uvv|uvvv", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.fvt", "*.fvt", "fvt", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.mpegurl", "*.mxu;*.m4u", "mxu|m4u", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.ms-playready.media.pyv", "*.pyv", "pyv", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.uvvu.mp4", "*.uvu;*.uvvu", "uvu|uvvu", "ctsms-mimetype-file");
	//		createMimeType(module, "video/vnd.vivo", "*.viv", "viv", "ctsms-mimetype-file");
	//		createMimeType(module, "video/webm", "*.webm", "webm", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-f4v", "*.f4v", "f4v", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-fli", "*.fli", "fli", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-flv", "*.flv", "flv", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-m4v", "*.m4v", "m4v", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-ms-asf", "*.asf;*.asx", "asf|asx", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-ms-wm", "*.wm", "wm", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-ms-wmv", "*.wmv", "wmv", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-ms-wmx", "*.wmx", "wmx", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-ms-wvx", "*.wvx", "wvx", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-msvideo", "*.avi", "avi", "ctsms-mimetype-file");
	//		createMimeType(module, "video/x-sgi-movie", "*.movie", "movie", "ctsms-mimetype-file");
	//		createMimeType(module, "x-conference/x-cooltalk", "*.ice", "ice", "ctsms-mimetype-file");
	//	}
}
