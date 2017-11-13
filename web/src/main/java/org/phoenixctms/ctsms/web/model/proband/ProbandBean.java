package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.ProbandCategoryVO;
import org.phoenixctms.ctsms.vo.ProbandImageOutVO;
import org.phoenixctms.ctsms.vo.ProbandInVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.SexVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.model.DefaultTreeNode;
import org.phoenixctms.ctsms.web.model.IDVOTreeNode;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.SexSelector;
import org.phoenixctms.ctsms.web.model.SexSelectorListener;
import org.phoenixctms.ctsms.web.model.shared.ProbandMultiPickerModel;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class ProbandBean extends ManagedBeanBase implements SexSelectorListener {

	public static void copyProbandOutToIn(ProbandInVO in, ProbandOutVO out) {
		if (in != null && out != null) {
			DepartmentVO departmentVO = out.getDepartment();
			ProbandCategoryVO categoryVO = out.getCategory();
			StaffOutVO physicianVO = out.getPhysician();
			SexVO genderVO = out.getGender();
			Collection<ProbandOutVO> childrenVOs = out.getChildren();
			// in.setAvailable(out.getAvailable());
			in.setPerson(out.getPerson());
			in.setBlinded(out.getBlinded());
			in.setCitizenship(out.getCitizenship());
			in.setComment(out.getComment());
			in.setDateOfBirth(out.getDateOfBirth());
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setCategoryId(categoryVO == null ? null : categoryVO.getId());
			in.setPhysicianId(physicianVO == null ? null : physicianVO.getId());
			in.setFirstName(out.getFirstName());
			in.setLastName(out.getLastName());
			in.setAnimalName(out.getAnimalName());
			in.setGender(genderVO == null ? null : genderVO.getSex());
			in.setAlias(out.getAlias());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setPostpositionedTitle1(out.getPostpositionedTitle1());
			in.setPostpositionedTitle2(out.getPostpositionedTitle2());
			in.setPostpositionedTitle3(out.getPostpositionedTitle3());
			in.setPrefixedTitle1(out.getPrefixedTitle1());
			in.setPrefixedTitle2(out.getPrefixedTitle2());
			in.setPrefixedTitle3(out.getPrefixedTitle3());
			in.setRating(out.getRating());
			in.setRatingMax(out.getRatingMax() != null ? out.getRatingMax() :
				Settings.getLongNullable(SettingCodes.PROBAND_RATING_MAX_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_RATING_MAX_PRESET));
			// in.setRecruitmentForOtherTrialsAllowed(out.getRecruitmentForOtherTrialsAllowed());
			ArrayList<Long> childIds = new ArrayList<Long>(childrenVOs.size());
			Iterator<ProbandOutVO> it = childrenVOs.iterator();
			while (it.hasNext()) {
				childIds.add(it.next().getId());
			}
			in.setChildIds(childIds);
		}
	}

	private ProbandInVO in;
	private ProbandOutVO out;
	private ArrayList<SelectItem> categories;
	private ArrayList<SelectItem> departments;
	private SexSelector gender;
	private ProbandCategoryVO category;
	private TreeNode childrenRoot;
	private TreeNode parentsRoot;
	private ProbandMultiPickerModel childrenMultiPicker;
	private static final int GENDER_PROPERTY_ID = 1;

	private static ProbandOutVO createProbandOutFromIn(ProbandInVO in) {
		ProbandOutVO result = new ProbandOutVO();
		if (in != null) {
			SexVO genderVO = null;
			try {
				genderVO = WebUtil.getServiceLocator().getToolsService().getLocalizedSex(WebUtil.getAuthentication(), in.getGender());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
			result.setPerson(in.getPerson());
			result.setBlinded(in.getBlinded());
			result.setAge(CommonUtil.getAge(in.getDateOfBirth()));
			// result.setAutoDeleteDeadline();
			// result.setAvailable(in.getAvailable());
			// result.setCategory(value)
			// result.setChildren(value);
			result.setChildrenCount(in.getChildIds().size());
			result.setCitizenship(in.getCitizenship());
			result.setComment(in.getComment());
			result.setDateOfBirth(in.getDateOfBirth());
			result.setDecrypted(true);
			// result.setDeferredDelete(value);
			// result.setDepartment(value)
			result.setFirstName(in.getFirstName());
			result.setGender(genderVO);
			result.setAlias(in.getAlias());
			// result.setHasImage(value)
			// result.setId(value)
			result.setLastName(in.getLastName());
			result.setAnimalName(in.getAnimalName());
			// result.setModifiedTimestamp(value)
			// result.setModifiedUser(value)
			// result.setParents(value)
			// result.setParentsCount(value)
			result.setPostpositionedTitle1(in.getPostpositionedTitle1());
			result.setPostpositionedTitle2(in.getPostpositionedTitle2());
			result.setPostpositionedTitle3(in.getPostpositionedTitle3());
			result.setPrefixedTitle1(in.getPrefixedTitle1());
			result.setPrefixedTitle2(in.getPrefixedTitle2());
			result.setPrefixedTitle3(in.getPrefixedTitle3());
			result.setRating(in.getRating());
			result.setRatingMax(in.getRatingMax() != null ? in.getRatingMax() :
				Settings.getLongNullable(SettingCodes.PROBAND_RATING_MAX_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_RATING_MAX_PRESET));
			// result.setPrivacyConsentStatus(value)
			// result.setRecruitmentForOtherTrialsAllowed(in.isRecruitmentForOtherTrialsAllowed());
			// result.setVersion(value);
			result.setYearOfBirth(CommonUtil.getYearOfBirth(in.getDateOfBirth()));
			result.setDecrypted(true);
			result.setInitials(CommonUtil.getProbandInitials(result, null, Messages.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME),
					Messages.getString(MessageCodes.BLINDED_PROBAND_NAME)));
			result.setName(CommonUtil.getProbandName(result, false, null, Messages.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME),
					Messages.getString(MessageCodes.BLINDED_PROBAND_NAME)));
			result.setNameWithTitles(CommonUtil.getProbandName(result, true, null, Messages.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME),
					Messages.getString(MessageCodes.BLINDED_PROBAND_NAME)));
			result.setNameSortable(CommonUtil.getNameSortable(result));
		}
		return result;
	}

	public static void initProbandDefaultValues(ProbandInVO in, UserOutVO user) {
		if (in != null) {
			ProbandCategoryVO categoryPreset = null;
			try {
				categoryPreset = WebUtil
						.getServiceLocator()
						.getSelectionSetService()
						.getProbandCategoryPreset(WebUtil.getAuthentication(), false,
								Settings.getBoolean(SettingCodes.PROBAND_PERSON_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_PERSON_PRESET));
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
			in.setPerson(Settings.getBoolean(SettingCodes.PROBAND_PERSON_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_PERSON_PRESET));
			in.setBlinded(Settings.getBoolean(SettingCodes.PROBAND_BLINDED_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_BLINDED_PRESET));
			// in.setAvailable(Settings.getBoolean(SettingCodes.PROBAND_AVAILABLE_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_AVAILABLE_PRESET));
			in.setCitizenship(Messages.getString(MessageCodes.PROBAND_CITIZENSHIP_PRESET));
			in.setComment(Messages.getString(MessageCodes.PROBAND_COMMENT_PRESET));
			in.setDateOfBirth(null);
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setCategoryId(categoryPreset == null ? null : categoryPreset.getId());
			in.setPhysicianId(null);
			in.setFirstName(Messages.getString(MessageCodes.PROBAND_FIRST_NAME_PRESET));
			in.setLastName(Messages.getString(MessageCodes.PROBAND_LAST_NAME_PRESET));
			in.setAnimalName(Messages.getString(MessageCodes.PROBAND_ANIMAL_NAME_PRESET));
			in.setGender(Settings.getSex(SettingCodes.PROBAND_GENDER_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_GENDER_PRESET));
			in.setAlias(Messages.getString(MessageCodes.PROBAND_ALIAS_PRESET));
			in.setId(null);
			in.setVersion(null);
			in.setPostpositionedTitle1(Messages.getString(MessageCodes.PROBAND_POSTPOSITIONED_TITLE1_PRESET));
			in.setPostpositionedTitle2(Messages.getString(MessageCodes.PROBAND_POSTPOSITIONED_TITLE2_PRESET));
			in.setPostpositionedTitle3(Messages.getString(MessageCodes.PROBAND_POSTPOSITIONED_TITLE3_PRESET));
			in.setPrefixedTitle1(Messages.getString(MessageCodes.PROBAND_PREFIXED_TITLE1_PRESET));
			in.setPrefixedTitle2(Messages.getString(MessageCodes.PROBAND_PREFIXED_TITLE2_PRESET));
			in.setPrefixedTitle3(Messages.getString(MessageCodes.PROBAND_PREFIXED_TITLE3_PRESET));
			in.setRating(Settings.getLongNullable(SettingCodes.PROBAND_RATING_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_RATING_PRESET));
			in.setRatingMax(Settings.getLongNullable(SettingCodes.PROBAND_RATING_MAX_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_RATING_MAX_PRESET));
			// in.setRecruitmentForOtherTrialsAllowed(Settings.getBoolean(SettingCodes.PROBAND_RECRUITMENT_FOR_OTHER_TRIALS_ALLOWED_PRESET, Bundle.SETTINGS,
			// DefaultSettings.PROBAND_RECRUITMENT_FOR_OTHER_TRIALS_ALLOWED_PRESET));
			in.setChildIds(new ArrayList<Long>());
		}
	}

	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private Object[] totalCounts;

	public ProbandBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		totalCounts = new Object[3];
		DefaultTreeNode childrenRoot = new DefaultTreeNode("children_root", null);
		childrenRoot.setExpanded(true);
		childrenRoot.setType(WebUtil.PARENT_NODE_TYPE);
		this.childrenRoot = childrenRoot;
		DefaultTreeNode parentsRoot = new DefaultTreeNode("parents_root", null);
		parentsRoot.setExpanded(true);
		parentsRoot.setType(WebUtil.PARENT_NODE_TYPE);
		this.parentsRoot = parentsRoot;
		childrenMultiPicker = new ProbandMultiPickerModel();
		setGender(new SexSelector(this, GENDER_PROPERTY_ID));
	}

	@Override
	public String addAction()
	{
		ProbandInVO backup = new ProbandInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().addProband(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_PARENTS_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	public void addChildIds() {
		childrenMultiPicker.addIds(childrenMultiPicker.getIds());
		in.setChildIds(new ArrayList<Long>(childrenMultiPicker.getSelectionIds()));
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_TAG_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_TAG_VALUE_COUNT,
					MessageCodes.PROBAND_TAGS_TAB_TITLE, MessageCodes.PROBAND_TAGS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROBAND_TAG_VALUE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_IMAGE_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_IMAGE_COUNT,
					MessageCodes.PROBAND_IMAGE_TAB_TITLE, MessageCodes.PROBAND_IMAGE_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROBAND_IMAGE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_CONTACT_DETAIL_TAB_TITLE_BASE64,
					JSValues.AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT, MessageCodes.PROBAND_CONTACT_DETAILS_TAB_TITLE, MessageCodes.PROBAND_CONTACT_DETAILS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_ADDRESS_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_ADDRESS_COUNT,
					MessageCodes.PROBAND_ADDRESSES_TAB_TITLE, MessageCodes.PROBAND_ADDRESSES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROBAND_ADDRESS_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_STATUS_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_STATUS_ENTRY_COUNT,
					MessageCodes.PROBAND_STATUS_TAB_TITLE, MessageCodes.PROBAND_STATUS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROBAND_STATUS_ENTRY_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_DIAGNOSIS_TAB_TITLE_BASE64, JSValues.AJAX_DIAGNOSIS_COUNT,
					MessageCodes.DIAGNOSES_TAB_TITLE, MessageCodes.DIAGNOSES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_DIAGNOSIS_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROCEDURE_TAB_TITLE_BASE64, JSValues.AJAX_PROCEDURE_COUNT,
					MessageCodes.PROCEDURES_TAB_TITLE, MessageCodes.PROCEDURES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROCEDURE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MEDICATION_TAB_TITLE_BASE64, JSValues.AJAX_MEDICATION_COUNT,
					MessageCodes.MEDICATIONS_TAB_TITLE, MessageCodes.MEDICATIONS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_MEDICATION_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_INVENTORY_BOOKING_TAB_TITLE_BASE64,
					JSValues.AJAX_PROBAND_INVENTORY_BOOKING_COUNT, MessageCodes.PROBAND_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.PROBAND_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_PROBAND_INVENTORY_BOOKING_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_BANK_ACCOUNT_TAB_TITLE_BASE64, JSValues.AJAX_BANK_ACCOUNT_COUNT,
					MessageCodes.BANK_ACCOUNTS_TAB_TITLE, MessageCodes.BANK_ACCOUNTS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_BANK_ACCOUNT_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MONEY_TRANSFER_TAB_TITLE_BASE64, JSValues.AJAX_MONEY_TRANSFER_COUNT,
					MessageCodes.MONEY_TRANSFERS_TAB_TITLE, MessageCodes.MONEY_TRANSFERS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_MONEY_TRANSFER_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_PARTICIPATION_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_PARTICIPATION_COUNT,
					MessageCodes.TRIAL_PARTICIPATIONS_TAB_TITLE, MessageCodes.TRIAL_PARTICIPATIONS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_TRIAL_PARTICIPATION_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_VISIT_SCHEDULE_TAB_TITLE_BASE64,
					JSValues.AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT, MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE, MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INQUIRY_VALUE_TAB_TITLE_BASE64, JSValues.AJAX_INQUIRY_VALUE_COUNT,
					MessageCodes.INQUIRY_VALUES_TAB_TITLE, MessageCodes.INQUIRY_VALUES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_INQUIRY_VALUE_COUNT.toString()),
					totalCounts);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_FILE_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_FILE_COUNT,
					MessageCodes.PROBAND_FILES_TAB_TITLE, MessageCodes.PROBAND_FILES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_PROBAND_FILE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT.toString()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getProbandService().getProband(WebUtil.getAuthentication(), id,
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_PARENTS_DEPTH),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
			} catch (ServiceException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
		}
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeByNode() {
		Long probandId = WebUtil.getLongParamValue(GetParamNames.PROBAND_ID);
		if (probandId != null) {
			change(probandId.toString());
		} else {
			this.out = null;
			this.initIn();
			initSets();
			appendRequestContextCallbackArgs(true);
		}
	}

	public List<String> completeCitizenship(String query) {
		this.in.setCitizenship(query);
		return WebUtil.completeCountryName(query);
	}

	public List<String> completePostpositionedTitle1(String query) {
		this.in.setPrefixedTitle1(query);
		return WebUtil.completeTitle(query);
	}

	public List<String> completePostpositionedTitle2(String query) {
		this.in.setPrefixedTitle2(query);
		return WebUtil.completeTitle(query);
	}

	public List<String> completePostpositionedTitle3(String query) {
		this.in.setPrefixedTitle3(query);
		return WebUtil.completeTitle(query);
	}

	public List<String> completePrefixedTitle1(String query) {
		this.in.setPrefixedTitle1(query);
		return WebUtil.completeTitle(query);
	}

	public List<String> completePrefixedTitle2(String query) {
		this.in.setPrefixedTitle2(query);
		return WebUtil.completeTitle(query);
	}

	public List<String> completePrefixedTitle3(String query) {
		this.in.setPrefixedTitle3(query);
		return WebUtil.completeTitle(query);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getProbandService().deleteProband(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.PROBAND_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.PROBAND_DEFERRED_DELETE), false,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_PARENTS_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	public Integer getAge() {
		return CommonUtil.getAge(in.getDateOfBirth());
	}

	public String getAutoDeleteMessage() {
		if (out != null && category != null) {
			if (category.isPrivacyConsentControl()) {
				if (out.getPrivacyConsentStatus().isAutoDelete()) {
					if (category.isDelete()) {
						return Messages.getMessage(MessageCodes.PRIVACY_CONSENT_STATUS_AUTODELETE_ACTIVE_MESSAGE, out.getPrivacyConsentStatus().getName(), DateUtil.getDateFormat()
								.format(out.getAutoDeleteDeadline()));
					} else {
						return Messages.getMessage(MessageCodes.PRIVACY_CONSENT_STATUS_AUTODELETE_DUE_MESSAGE, out.getPrivacyConsentStatus().getName(), DateUtil.getDateFormat()
								.format(out.getAutoDeleteDeadline()));
					}
				} else {
					return Messages.getMessage(MessageCodes.PRIVACY_CONSENT_STATUS_AUTODELETE_DISABLED_MESSAGE, out.getPrivacyConsentStatus().getName());
				}
			} else {
				return Messages.getMessage(MessageCodes.PRIVACY_CONSENT_STATUS_AUTODELETE_CONTROL_DISABLED_MESSAGE, category.getName());
			}
		}
		return "";
	}

	public ArrayList<SelectItem> getCategories() {
		return categories;
	}

	public ProbandCategoryVO getCategory() {
		return category;
	}

	public ProbandMultiPickerModel getChildrenMultiPicker() {
		return childrenMultiPicker;
	}

	public TreeNode getChildrenRoot() {
		return childrenRoot;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public SexSelector getGender() {
		return gender;
	}

	public ProbandInVO getIn() {
		return in;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public ProbandOutVO getOut() {
		return out;
	}

	public TreeNode getParentsRoot() {
		return parentsRoot;
	}

	public String getPhysicianName() {
		return WebUtil.staffIdToName(in.getPhysicianId());
	}

	public String getProbandLockedMessage() {
		if (category != null && category.getLocked()) {
			return Messages.getMessage(MessageCodes.PROBAND_LOCKED_MESSAGE);
		}
		return "";
	}

	public String getProbandTreeChildrenLabel() {
		Integer graphMaxChildrenDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_CHILDREN_DEPTH);
		if (graphMaxChildrenDepth == null) {
			return Messages.getString(MessageCodes.PROBAND_TREE_CHILDREN_LABEL);
		} else {
			return Messages.getMessage(MessageCodes.PROBAND_TREE_CHILDREN_LEVELS_LABEL, graphMaxChildrenDepth);
		}
	}

	public String getProbandTreeLabel() {
		Integer graphMaxProbandInstances = Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES);
		if (graphMaxProbandInstances == null) {
			return Messages.getString(MessageCodes.PROBAND_TREE_LABEL);
		} else {
			return Messages.getMessage(MessageCodes.PROBAND_TREE_MAX_LABEL, graphMaxProbandInstances);
		}
	}

	public String getProbandTreeParentsLabel() {
		Integer graphMaxParentsDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_PARENTS_DEPTH);
		if (graphMaxParentsDepth == null) {
			return Messages.getString(MessageCodes.PROBAND_TREE_PARENTS_LABEL);
		} else {
			return Messages.getMessage(MessageCodes.PROBAND_TREE_PARENTS_LEVELS_LABEL, graphMaxParentsDepth);
		}
	}

	@Override
	public Sex getSex(int property) {
		switch (property) {
			case GENDER_PROPERTY_ID:
				return this.in.getGender();
			default:
				return SexSelectorListener.NO_SELECTION_SEX;
		}
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.PROBAND_ID) == null);
	}

	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : (out.isPerson() ? MessageCodes.PROBAND_PERSON_TITLE
					: MessageCodes.PROBAND_ANIMAL_TITLE),
					Long.toString(out.getId()), out.getNameWithTitles());
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_PROBAND : MessageCodes.ERROR_LOADING_PROBAND);
		}
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.PROBAND_ID) == null);
	}

	// public void handleBlindedChange() {
	// loadProbandCategories();
	// }

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.PROBAND_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.PROBAND_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long probandId = WebUtil.getLongParamValue(GetParamNames.PROBAND_ID);
				if (probandId != null) {
					return String.format(JSValues.PROBAND_ENTITY_WINDOW_NAME.toString(), probandId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.PROBAND_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleCategoryChange() {
		loadSelectedCategory();
	}

	public void handleCitizenshipSelect(SelectEvent event) {
		in.setCitizenship((String) event.getObject());
	}

	public void handlePersonChange() {
		loadProbandCategories();
	}

	public void handlePostpositionedTitle1Select(SelectEvent event) {
		in.setPostpositionedTitle1((String) event.getObject());
	}

	public void handlePostpositionedTitle2Select(SelectEvent event) {
		in.setPostpositionedTitle2((String) event.getObject());
	}

	public void handlePostpositionedTitle3Select(SelectEvent event) {
		in.setPostpositionedTitle3((String) event.getObject());
	}

	public void handlePrefixedTitle1Select(SelectEvent event) {
		in.setPrefixedTitle1((String) event.getObject());
	}

	public void handlePrefixedTitle2Select(SelectEvent event) {
		in.setPrefixedTitle2((String) event.getObject());
	}

	public void handlePrefixedTitle3Select(SelectEvent event) {
		in.setPrefixedTitle3((String) event.getObject());
	}

	@PostConstruct
	private void init() {
		// System.out.println("POSTCONSTRUCT: " + this.toString());
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandInVO();
		}
		if (out != null) {
			copyProbandOutToIn(in, out);
		} else {
			initProbandDefaultValues(in, WebUtil.getUser());
		}
	}

	private void initSets() {
		tabCountMap.clear();
		tabTitleMap.clear();
		Long count = null;
		if (out != null) {
			try {
				ProbandImageOutVO probandImage = WebUtil.getServiceLocator().getProbandService().getProbandImage(WebUtil.getAuthentication(), in.getId());
				count = probandImage.getHasImage() ? 1l : 0l;
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		tabCountMap.put(JSValues.AJAX_PROBAND_IMAGE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_IMAGE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_IMAGE_TAB_TITLE, MessageCodes.PROBAND_IMAGE_TAB_TITLE_WITH_COUNT, count));
		// PSFVO psf = new PSFVO();
		// psf.setPageSize(0);
		count = (out == null ? null : WebUtil.getProbandTagValueCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_TAG_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_TAG_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_TAGS_TAB_TITLE, MessageCodes.PROBAND_TAGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandContactDetailValueCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_CONTACT_DETAIL_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_CONTACT_DETAILS_TAB_TITLE, MessageCodes.PROBAND_CONTACT_DETAILS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandAddressCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_ADDRESS_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_ADDRESS_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_ADDRESSES_TAB_TITLE, MessageCodes.PROBAND_ADDRESSES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandStatusEntryCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_STATUS_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_STATUS_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_STATUS_TAB_TITLE, MessageCodes.PROBAND_STATUS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getDiagnosisCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_DIAGNOSIS_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_DIAGNOSIS_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.DIAGNOSES_TAB_TITLE, MessageCodes.DIAGNOSES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProcedureCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROCEDURE_COUNT.toString(), count);
		tabTitleMap
		.put(JSValues.AJAX_PROCEDURE_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.PROCEDURES_TAB_TITLE, MessageCodes.PROCEDURES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getMedicationCount(in.getId(), null, null));
		tabCountMap.put(JSValues.AJAX_MEDICATION_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_MEDICATION_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.MEDICATIONS_TAB_TITLE, MessageCodes.MEDICATIONS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandInventoryBookingCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_INVENTORY_BOOKING_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_INVENTORY_BOOKING_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.PROBAND_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getBankAccountCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_BANK_ACCOUNT_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_BANK_ACCOUNT_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.BANK_ACCOUNTS_TAB_TITLE, MessageCodes.BANK_ACCOUNTS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getMoneyTransferCount(in.getId(), null));
		tabCountMap.put(JSValues.AJAX_MONEY_TRANSFER_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_MONEY_TRANSFER_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.MONEY_TRANSFERS_TAB_TITLE, MessageCodes.MONEY_TRANSFERS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getProbandListEntryCount(null, in.getId(), true));
		tabCountMap.put(JSValues.AJAX_TRIAL_PARTICIPATION_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_TRIAL_PARTICIPATION_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.TRIAL_PARTICIPATIONS_TAB_TITLE, MessageCodes.TRIAL_PARTICIPATIONS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getVisitScheduleItemCount(null, in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_VISIT_SCHEDULE_ITEM_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE, MessageCodes.PROBAND_VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT, count));
		count = WebUtil.getTrialsFromInquiryValues(in.getId(), true, null, null, totalCounts);
		tabCountMap.put(JSValues.AJAX_INQUIRY_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INQUIRY_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INQUIRY_VALUES_TAB_TITLE, MessageCodes.INQUIRY_VALUES_TAB_TITLE_WITH_COUNT, count, totalCounts));
		count = (out == null ? null : WebUtil.getFileCount(FileModule.PROBAND_DOCUMENT, in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_FILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_FILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_FILES_TAB_TITLE, MessageCodes.PROBAND_FILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT, count));


		childrenRoot.getChildren().clear();
		parentsRoot.getChildren().clear();

		if (out != null) {
			// probandOutVOtoChildTreeNode1
			probandOutVOtoParentTreeNode(out, parentsRoot, new ArrayList<IDVOTreeNode>(),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_PARENTS_DEPTH),
					null, 0);
		} else {
			IDVOTreeNode loose = new IDVOTreeNode(createProbandOutFromIn(in), parentsRoot);
			loose.setType(WebUtil.LEAF_NODE_TYPE);
		}
		if (out != null) {
			// probandOutVOtoParentTreeNode1
			probandOutVOtoChildTreeNode(out, childrenRoot, new ArrayList<IDVOTreeNode>(),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_CHILDREN_DEPTH),
					null, 0);
		} else {
			IDVOTreeNode loose = new IDVOTreeNode(createProbandOutFromIn(in), childrenRoot);
			loose.setType(WebUtil.LEAF_NODE_TYPE);
		}
		childrenMultiPicker.setIds(in.getChildIds());



		// categories = WebUtil.getVisibleProbandCategories(in.getCategoryId());
		loadProbandCategories();
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		loadSelectedCategory();
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.PROBAND_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.PROBAND_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION);
		}
	}

	public boolean isAutoDelete() {
		if (out != null && category != null) {
			return out.getPrivacyConsentStatus().isAutoDelete() && category.isPrivacyConsentControl(); // && category.isDelete();
		}
		return false;
	}

	@Override
	public boolean isCreateable() {
		return WebUtil.getModuleEnabled(DBModule.PROBAND_DB);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isEditable() {
		return WebUtil.getModuleEnabled(DBModule.PROBAND_DB) && super.isEditable();
	}

	public boolean isProbandLocked() {
		if (category != null) {
			return category.getLocked();
		}
		return false;
	}

	public boolean isRemovable() {
		return WebUtil.getModuleEnabled(DBModule.PROBAND_DB) && super.isRemovable();
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab), JSValues.AJAX_INQUIRY_VALUE_COUNT.toString().equals(tab));
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getProbandService().getProband(WebUtil.getAuthentication(), id,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_PARENTS_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadProbandCategories() {
		if (in.getPerson()) {
			categories = WebUtil.getVisibleProbandCategories(true, null, in.getCategoryId());
		} else {
			categories = WebUtil.getVisibleProbandCategories(null, true, in.getCategoryId());
		}
	}

	private void loadSelectedCategory() {
		category = WebUtil.getProbandCategory(in.getCategoryId());
	}

	private IDVOTreeNode probandOutVOtoChildTreeNode(ProbandOutVO proband, TreeNode parent, ArrayList<IDVOTreeNode> nodes,
			Integer limit, Integer maxDepth, ArrayList<Object[]> deferred, int depth) {
		if ((limit == null || nodes.size() < limit.intValue()) && (maxDepth == null || depth <= maxDepth.intValue())) {
			IDVOTreeNode node = new IDVOTreeNode(proband, parent);
			nodes.add(node);
			parent.setExpanded(true);
			if (proband.getChildrenCount() > 0L) {
				node.setType(WebUtil.PARENT_NODE_TYPE);
			} else {
				node.setType(WebUtil.LEAF_NODE_TYPE);
			}
			node.setSelectable(true);
			Collection<ProbandOutVO> children = proband.getChildren();
			Iterator<ProbandOutVO> it = children.iterator();
			if (Settings.getBoolean(SettingCodes.GRAPH_PROBAND_BREADTH_FIRST, Bundle.SETTINGS, DefaultSettings.GRAPH_PROBAND_BREADTH_FIRST)) {
				if (deferred == null) {
					deferred = new ArrayList<Object[]>(children.size());
					while (it.hasNext()) {
						probandOutVOtoChildTreeNode(it.next(), node, nodes, limit, maxDepth, deferred, depth + 1);
					}
					Iterator<Object[]> deferredIt = deferred.iterator();
					while (deferredIt.hasNext()) {
						Object[] newNode = deferredIt.next();
						probandOutVOtoChildTreeNode((ProbandOutVO) newNode[0], (IDVOTreeNode) newNode[1], nodes, limit, maxDepth, null, (Integer) newNode[2]);
					}
				} else {
					while (it.hasNext()) {
						Object[] newNode = new Object[3];
						newNode[0] = it.next();
						newNode[1] = node;
						newNode[2] = depth + 1;
						deferred.add(newNode);
					}
				}
			} else {
				while (it.hasNext()) {
					probandOutVOtoChildTreeNode(it.next(), node, nodes, limit, maxDepth, null, depth + 1);
				}
			}
			return node;
		}
		return null;
	}

	private IDVOTreeNode probandOutVOtoParentTreeNode(ProbandOutVO proband, TreeNode child, ArrayList<IDVOTreeNode> nodes, Integer limit, Integer maxDepth,
			ArrayList<Object[]> deferred, int depth) {
		if ((limit == null || nodes.size() < limit.intValue()) && (maxDepth == null || depth <= maxDepth.intValue())) {
			IDVOTreeNode node = new IDVOTreeNode(proband, child);
			nodes.add(node);
			child.setExpanded(true);
			if (proband.getParentsCount() > 0L) {
				node.setType(WebUtil.PARENT_NODE_TYPE);
			} else {
				node.setType(WebUtil.LEAF_NODE_TYPE);
			}
			node.setSelectable(true);
			Collection<ProbandOutVO> parents = proband.getParents();
			Iterator<ProbandOutVO> it = parents.iterator();
			if (Settings.getBoolean(SettingCodes.GRAPH_PROBAND_BREADTH_FIRST, Bundle.SETTINGS, DefaultSettings.GRAPH_PROBAND_BREADTH_FIRST)) {
				if (deferred == null) {
					deferred = new ArrayList<Object[]>(parents.size());
					while (it.hasNext()) {
						probandOutVOtoParentTreeNode(it.next(), node, nodes, limit, maxDepth, deferred, depth + 1);
					}
					Iterator<Object[]> deferredIt = deferred.iterator();
					while (deferredIt.hasNext()) {
						Object[] newNode = deferredIt.next();
						probandOutVOtoParentTreeNode((ProbandOutVO) newNode[0], (IDVOTreeNode) newNode[1], nodes, limit, maxDepth, null, (Integer) newNode[2]);
					}
				} else {
					while (it.hasNext()) {
						Object[] newNode = new Object[3];
						newNode[0] = it.next();
						newNode[1] = node;
						newNode[2] = depth + 1;
						deferred.add(newNode);
					}
				}
			} else {
				while (it.hasNext()) {
					probandOutVOtoParentTreeNode(it.next(), node, nodes, limit, maxDepth, null, depth + 1);
				}
			}
			return node;
		}
		return null;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (!in.getBlinded()) {
			if (in.getPerson()) {
				in.setAnimalName(null);
				// in.setCvOrganisationName(null);
			} else {
				// in.setAllocatable(false);
				in.setCitizenship(null);
				// in.setEmployee(false);
				in.setFirstName(null);
				in.setLastName(null);
				in.setPostpositionedTitle1(null);
				in.setPostpositionedTitle2(null);
				in.setPostpositionedTitle3(null);
				in.setPrefixedTitle1(null);
				in.setPrefixedTitle2(null);
				in.setPrefixedTitle3(null);
				// in.setCvAcademicTitle(null);
			}
			in.setAlias(null);
		} else {
			// if (!in.getPerson()) {
			// in.setAlias(null);
			// }
			in.setCitizenship(null);
			// in.setDateOfBirth(null);
			in.setFirstName(null);
			// in.setGender(null);
			in.setLastName(null);
			in.setAnimalName(null);
			in.setPostpositionedTitle1(null);
			in.setPostpositionedTitle2(null);
			in.setPostpositionedTitle3(null);
			in.setPrefixedTitle1(null);
			in.setPrefixedTitle2(null);
			in.setPrefixedTitle3(null);
		}
		if (in.getRatingMax() != null) {
			if (in.getRating() == null) {
				in.setRating(0l);
			}
		} else {
			in.setRating(null);
		}
		in.setChildIds(new ArrayList<Long>(childrenMultiPicker.getSelectionIds()));
	}

	public void setGender(SexSelector gender) {
		this.gender = gender;
	}

	@Override
	public void setSex(int property, Sex sex) {
		switch (property) {
			case GENDER_PROPERTY_ID:
				this.in.setGender(sex);
				break;
			default:
		}
	}

	@Override
	public String updateAction() {
		ProbandInVO backup = new ProbandInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateProband(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_PARENTS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_PARENTS_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PROBAND_CHILDREN_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PROBAND_CHILDREN_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}
}
