package org.phoenixctms.ctsms.web.model.staff;

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

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.SexVO;
import org.phoenixctms.ctsms.vo.StaffCategoryVO;
import org.phoenixctms.ctsms.vo.StaffImageOutVO;
import org.phoenixctms.ctsms.vo.StaffInVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.model.DefaultTreeNode;
import org.phoenixctms.ctsms.web.model.IDVOTreeNode;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.SexSelector;
import org.phoenixctms.ctsms.web.model.SexSelectorListener;
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
public class StaffBean extends ManagedBeanBase implements SexSelectorListener {

	public static void copyStaffOutToIn(StaffInVO in, StaffOutVO out) {
		if (in != null && out != null) {
			StaffCategoryVO staffCategoryVO = out.getCategory();
			StaffOutVO parentVO = out.getParent();
			DepartmentVO departmentVO = out.getDepartment();
			SexVO genderVO = out.getGender();
			in.setAllocatable(out.getAllocatable());
			in.setCategoryId(staffCategoryVO == null ? null : staffCategoryVO.getId());
			in.setCitizenship(out.getCitizenship());
			in.setDateOfBirth(out.getDateOfBirth());
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setEmployee(out.getEmployee());
			in.setFirstName(out.getFirstName());
			in.setLastName(out.getLastName());
			in.setGender(genderVO == null ? null : genderVO.getSex());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setOrganisationName(out.getOrganisationName());
			in.setCvOrganisationName(out.getCvOrganisationName());
			in.setParentId(parentVO == null ? null : parentVO.getId());
			in.setPerson(out.getPerson());
			in.setPostpositionedTitle1(out.getPostpositionedTitle1());
			in.setPostpositionedTitle2(out.getPostpositionedTitle2());
			in.setPostpositionedTitle3(out.getPostpositionedTitle3());
			in.setPrefixedTitle1(out.getPrefixedTitle1());
			in.setPrefixedTitle2(out.getPrefixedTitle2());
			in.setPrefixedTitle3(out.getPrefixedTitle3());
			in.setCvAcademicTitle(out.getCvAcademicTitle());
			in.setMaxOverlappingShifts(out.getMaxOverlappingShifts());
		}
	}

	private static StaffOutVO createStaffOutFromIn(StaffInVO in) {
		StaffOutVO result = new StaffOutVO();
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
			result.setAllocatable(in.getAllocatable());
			result.setCitizenship(in.getCitizenship());
			result.setEmployee(in.getEmployee());
			result.setFirstName(in.getFirstName());
			result.setLastName(in.getLastName());
			result.setGender(genderVO);
			result.setOrganisationName(in.getOrganisationName());
			result.setCvOrganisationName(in.getCvOrganisationName());
			result.setPerson(in.getPerson());
			result.setPostpositionedTitle1(in.getPostpositionedTitle1());
			result.setPostpositionedTitle2(in.getPostpositionedTitle2());
			result.setPostpositionedTitle3(in.getPostpositionedTitle3());
			result.setPrefixedTitle1(in.getPrefixedTitle1());
			result.setPrefixedTitle2(in.getPrefixedTitle2());
			result.setPrefixedTitle3(in.getPrefixedTitle3());
			result.setCvAcademicTitle(in.getCvAcademicTitle());
			result.setInitials(CommonUtil.getStaffInitials(result));
			result.setName(CommonUtil.getStaffName(result, false));
			result.setNameWithTitles(CommonUtil.getStaffName(result, true));
			result.setNameSortable(CommonUtil.getNameSortable(result));
			result.setAge(CommonUtil.getAge(in.getDateOfBirth()));
			result.setMaxOverlappingShifts(in.getMaxOverlappingShifts());
		}
		return result;
	}

	public static void initStaffDefaultValues(StaffInVO in, UserOutVO user) {
		if (in != null) {
			in.setAllocatable(Settings.getBoolean(SettingCodes.STAFF_ALLOCATABLE_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_ALLOCATABLE_PRESET));
			in.setCategoryId(null);
			in.setCitizenship(Messages.getString(MessageCodes.STAFF_CITIZENSHIP_PRESET));
			in.setDateOfBirth(null);
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setEmployee(Settings.getBoolean(SettingCodes.STAFF_EMPLOYEE_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_EMPLOYEE_PRESET));
			in.setFirstName(Messages.getString(MessageCodes.STAFF_FIRST_NAME_PRESET));
			in.setLastName(Messages.getString(MessageCodes.STAFF_LAST_NAME_PRESET));
			in.setGender(Settings.getSex(SettingCodes.STAFF_GENDER_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_GENDER_PRESET));
			in.setId(null);
			in.setVersion(null);
			in.setOrganisationName(Messages.getString(MessageCodes.STAFF_ORGANISATION_NAME_PRESET));
			in.setCvOrganisationName(Messages.getString(MessageCodes.STAFF_CV_ORGANISATION_NAME_PRESET));
			in.setParentId(null);
			in.setPerson(Settings.getBoolean(SettingCodes.STAFF_PERSON_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_PERSON_PRESET));
			in.setPostpositionedTitle1(Messages.getString(MessageCodes.STAFF_POSTPOSITIONED_TITLE1_PRESET));
			in.setPostpositionedTitle2(Messages.getString(MessageCodes.STAFF_POSTPOSITIONED_TITLE2_PRESET));
			in.setPostpositionedTitle3(Messages.getString(MessageCodes.STAFF_POSTPOSITIONED_TITLE3_PRESET));
			in.setPrefixedTitle1(Messages.getString(MessageCodes.STAFF_PREFIXED_TITLE1_PRESET));
			in.setPrefixedTitle2(Messages.getString(MessageCodes.STAFF_PREFIXED_TITLE2_PRESET));
			in.setPrefixedTitle3(Messages.getString(MessageCodes.STAFF_PREFIXED_TITLE3_PRESET));
			in.setCvAcademicTitle(Messages.getString(MessageCodes.STAFF_CV_ACADEMIC_TITLE_PRESET));
			in.setMaxOverlappingShifts(Settings.getLongNullable(SettingCodes.STAFF_MAX_OVERLAPPING_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_MAX_OVERLAPPING_PRESET));
		}
	}

	private StaffInVO in;
	private StaffOutVO out;
	private ArrayList<SelectItem> categories;
	private ArrayList<SelectItem> departments;
	private TreeNode staffRoot;
	private SexSelector gender;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private static final int GENDER_PROPERTY_ID = 1;

	public StaffBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		DefaultTreeNode staffRoot = new DefaultTreeNode("staff_root", null);
		staffRoot.setExpanded(true);
		staffRoot.setType(WebUtil.PARENT_NODE_TYPE);
		this.staffRoot = staffRoot;
		setGender(new SexSelector(this, GENDER_PROPERTY_ID));
	}

	@Override
	public String addAction()
	{
		StaffInVO backup = new StaffInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getStaffService().addStaff(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_PARENT_DEPTH));
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

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		if ((UPDATE_OUTCOME.equals(outcome) || DELETE_OUTCOME.equals(outcome)) && WebUtil.isUserIdentityIdLoggedIn(in.getId())) {
			WebUtil.logout();
		} else {
			RequestContext requestContext = RequestContext.getCurrentInstance();
			if (requestContext != null) {
				requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
				requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
				requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
				requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_TAG_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_TAG_VALUE_COUNT,
						MessageCodes.STAFF_TAGS_TAB_TITLE, MessageCodes.STAFF_TAGS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_STAFF_TAG_VALUE_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_IMAGE_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_IMAGE_COUNT,
						MessageCodes.STAFF_IMAGE_TAB_TITLE, MessageCodes.STAFF_IMAGE_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_STAFF_IMAGE_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_CONTACT_DETAIL_TAB_TITLE_BASE64,
						JSValues.AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT, MessageCodes.STAFF_CONTACT_DETAILS_TAB_TITLE, MessageCodes.STAFF_CONTACT_DETAILS_TAB_TITLE_WITH_COUNT,
						tabCountMap.get(JSValues.AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_ADDRESS_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_ADDRESS_COUNT,
						MessageCodes.STAFF_ADDRESSES_TAB_TITLE, MessageCodes.STAFF_ADDRESSES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_STAFF_ADDRESS_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_STATUS_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_STATUS_ENTRY_COUNT,
						MessageCodes.STAFF_STATUS_TAB_TITLE, MessageCodes.STAFF_STATUS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_STAFF_STATUS_ENTRY_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_DUTY_ROSTER_TURN_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_DUTY_ROSTER_TURN_COUNT,
						MessageCodes.STAFF_DUTY_ROSTER_TURNS_TAB_TITLE, MessageCodes.STAFF_DUTY_ROSTER_TURNS_TAB_TITLE_WITH_COUNT,
						tabCountMap.get(JSValues.AJAX_STAFF_DUTY_ROSTER_TURN_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_CV_POSITION_TAB_TITLE_BASE64, JSValues.AJAX_CV_POSITION_COUNT,
						MessageCodes.CV_POSITIONS_TAB_TITLE, MessageCodes.CV_POSITIONS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_CV_POSITION_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64,
						JSValues.AJAX_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT, MessageCodes.COURSE_PARTICIPATION_STATUS_TAB_TITLE,
						MessageCodes.COURSE_PARTICIPATION_STATUS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_HYPERLINK_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_HYPERLINK_COUNT,
						MessageCodes.STAFF_HYPERLINKS_TAB_TITLE, MessageCodes.STAFF_HYPERLINKS_TAB_TITLE_WITH_COUNT,
						tabCountMap.get(JSValues.AJAX_STAFF_HYPERLINK_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_FILE_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_FILE_COUNT,
						MessageCodes.STAFF_FILES_TAB_TITLE, MessageCodes.STAFF_FILES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_STAFF_FILE_COUNT.toString()));
				WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
						MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT.toString()));
			}
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getStaffService().getStaff(WebUtil.getAuthentication(), id,
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_PARENT_DEPTH));
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
		Long staffId = WebUtil.getLongParamValue(GetParamNames.STAFF_ID);
		if (staffId != null) {
			change(staffId.toString());
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

	public List<String> completeCvAcademicTitle(String query) {
		this.in.setCvAcademicTitle(query);
		return WebUtil.completeTitle(query);
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
			out = WebUtil.getServiceLocator().getStaffService().deleteStaff(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.STAFF_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.STAFF_DEFERRED_DELETE),
					false,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_PARENT_DEPTH));
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

	private StaffOutVO findStaffRoot(StaffOutVO staff) {
		if (staff.getParent() == null) {
			return staff;
		} else {
			return findStaffRoot(staff.getParent());
		}
	}

	public ArrayList<SelectItem> getCategories() {
		return categories;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public SexSelector getGender() {
		return gender;
	}

	public StaffInVO getIn() {
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

	public StaffOutVO getOut() {
		return out;
	}

	public String getParentName() {
		return WebUtil.staffIdToName(in.getParentId());
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

	public TreeNode getStaffRoot() {
		return staffRoot;
	}

	public String getStaffTreeLabel() {
		Integer graphMaxStaffInstances = Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_INSTANCES);
		Integer graphMaxStaffParentDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_PARENT_DEPTH);
		if (graphMaxStaffInstances == null && graphMaxStaffParentDepth == null) {
			return Messages.getString(MessageCodes.STAFF_TREE_LABEL);
		} else if (graphMaxStaffInstances != null && graphMaxStaffParentDepth == null) {
			return Messages.getMessage(MessageCodes.STAFF_TREE_MAX_LABEL, graphMaxStaffInstances);
		} else if (graphMaxStaffInstances == null && graphMaxStaffParentDepth != null) {
			return Messages.getMessage(MessageCodes.STAFF_TREE_LEVELS_LABEL, graphMaxStaffParentDepth);
		} else {
			return Messages.getMessage(MessageCodes.STAFF_TREE_MAX_LEVELS_LABEL, graphMaxStaffInstances, graphMaxStaffParentDepth);
		}
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.STAFF_ID) == null);
	}

	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : (out.isPerson() ? MessageCodes.STAFF_PERSON_TITLE
					: MessageCodes.STAFF_ORGANISATION_TITLE), Long.toString(out.getId()), out.getNameWithTitles());
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_STAFF : MessageCodes.ERROR_LOADING_STAFF);
		}
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.STAFF_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.STAFF_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.STAFF_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long staffId = WebUtil.getLongParamValue(GetParamNames.STAFF_ID);
				if (staffId != null) {
					return String.format(JSValues.STAFF_ENTITY_WINDOW_NAME.toString(), staffId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.STAFF_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleAllocatableChange() {
	}

	public void handleCitizenshipSelect(SelectEvent event) {
		in.setCitizenship((String) event.getObject());
	}

	public void handleCvAcademicTitleSelect(SelectEvent event) {
		in.setCvAcademicTitle((String) event.getObject());
	}

	public void handleEmployeeChange() {
		if (!in.getEmployee()) {
			in.setAllocatable(false);
		}
		if (!in.getAllocatable()) {
			in.setMaxOverlappingShifts(0l);
		}
	}

	public void handlePersonChange() {
		loadStaffCategories();
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
		Long id = WebUtil.getLongParamValue(GetParamNames.STAFF_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new StaffInVO();
		}
		if (out != null) {
			copyStaffOutToIn(in, out);
		} else {
			initStaffDefaultValues(in, WebUtil.getUser());
		}
	}

	private void initSets() {
		tabCountMap.clear();
		tabTitleMap.clear();
		Long count = null;
		if (out != null) {
			try {
				StaffImageOutVO staffImage = WebUtil.getServiceLocator().getStaffService().getStaffImage(WebUtil.getAuthentication(), in.getId());
				count = staffImage.getHasImage() ? 1l : 0l;
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		tabCountMap.put(JSValues.AJAX_STAFF_IMAGE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_IMAGE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_IMAGE_TAB_TITLE, MessageCodes.STAFF_IMAGE_TAB_TITLE_WITH_COUNT, count));
		// PSFVO psf = new PSFVO();
		// psf.setPageSize(0);
		count = (out == null ? null : WebUtil.getStaffTagValueCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_TAG_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_TAG_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_TAGS_TAB_TITLE, MessageCodes.STAFF_TAGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getStaffContactDetailValueCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_CONTACT_DETAIL_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_CONTACT_DETAILS_TAB_TITLE, MessageCodes.STAFF_CONTACT_DETAILS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getStaffAddressCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_ADDRESS_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_ADDRESS_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_ADDRESSES_TAB_TITLE, MessageCodes.STAFF_ADDRESSES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getStaffStatusEntryCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_STATUS_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_STATUS_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_STATUS_TAB_TITLE, MessageCodes.STAFF_STATUS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getStaffDutyRosterCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_DUTY_ROSTER_TURN_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_DUTY_ROSTER_TURN_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_DUTY_ROSTER_TURNS_TAB_TITLE, MessageCodes.STAFF_DUTY_ROSTER_TURNS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getCvPositionListCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_CV_POSITION_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_CV_POSITION_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.CV_POSITIONS_TAB_TITLE, MessageCodes.CV_POSITIONS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getCourseParticipationStatusEntryCount(in.getId(), null));
		tabCountMap.put(JSValues.AJAX_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.COURSE_PARTICIPATION_STATUS_TAB_TITLE, MessageCodes.COURSE_PARTICIPATION_STATUS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getHyperlinkCount(HyperlinkModule.STAFF_HYPERLINK, in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_HYPERLINK_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_HYPERLINK_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_HYPERLINKS_TAB_TITLE, MessageCodes.STAFF_HYPERLINKS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getFileCount(FileModule.STAFF_DOCUMENT, in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_FILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_FILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_FILES_TAB_TITLE, MessageCodes.STAFF_FILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		staffRoot.getChildren().clear();
		if (out != null) {
			staffOutVOtoTreeNode(findStaffRoot(out), staffRoot, out, new ArrayList<IDVOTreeNode>(),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_PARENT_DEPTH),
					null, 0);
		} else {
			IDVOTreeNode loose = new IDVOTreeNode(createStaffOutFromIn(in), staffRoot);
			loose.setType(WebUtil.LEAF_NODE_TYPE);
		}
		loadStaffCategories();
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		if (WebUtil.isUserIdentityIdLoggedIn(in.getId())) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.EDITING_ACTIVE_USER_IDENTITY);
		}
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.STAFF_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.STAFF_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION);
		}
	}

	@Override
	public boolean isCreateable() {
		return true;
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isUserIdentityIdLoggedIn(in.getId());
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab));
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getStaffService().getStaff(WebUtil.getAuthentication(), id,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_PARENT_DEPTH));
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

	private void loadStaffCategories() {
		if (in.getPerson()) {
			categories = WebUtil.getVisibleStaffCategories(true, null, in.getCategoryId());
		} else {
			categories = WebUtil.getVisibleStaffCategories(null, true, in.getCategoryId());
		}
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (in.getPerson()) {
			in.setOrganisationName(null);
			in.setCvOrganisationName(null);
		} else {
			in.setAllocatable(false);
			in.setCitizenship(null);
			in.setDateOfBirth(null);
			in.setEmployee(false);
			in.setFirstName(null);
			in.setGender(null);
			in.setLastName(null);
			in.setPostpositionedTitle1(null);
			in.setPostpositionedTitle2(null);
			in.setPostpositionedTitle3(null);
			in.setPrefixedTitle1(null);
			in.setPrefixedTitle2(null);
			in.setPrefixedTitle3(null);
			in.setCvAcademicTitle(null);
		}
		if (!in.getEmployee()) {
			in.setAllocatable(false);
		}
		if (!in.getAllocatable()) {
			in.setMaxOverlappingShifts(0l);
		}
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

	private IDVOTreeNode staffOutVOtoTreeNode(StaffOutVO staff, TreeNode parent, StaffOutVO selected, ArrayList<IDVOTreeNode> nodes, Integer limit, Integer maxDepth,
			ArrayList<Object[]> deferred, int depth) {
		if ((limit == null || nodes.size() < limit.intValue()) && (maxDepth == null || depth <= maxDepth.intValue())) {
			IDVOTreeNode node = new IDVOTreeNode(staff, parent);
			nodes.add(node);
			if (selected != null && staff.getId() == selected.getId()) {
				node.setSelected(true);
				parent.setExpanded(true);
			}
			if (staff.getChildrenCount() > 0L) {
				node.setType(WebUtil.PARENT_NODE_TYPE);
			} else {
				node.setType(WebUtil.LEAF_NODE_TYPE);
			}
			node.setSelectable(true);
			Collection<StaffOutVO> children = staff.getChildren();
			Iterator<StaffOutVO> it = children.iterator();
			if (Settings.getBoolean(SettingCodes.GRAPH_STAFF_BREADTH_FIRST, Bundle.SETTINGS, DefaultSettings.GRAPH_STAFF_BREADTH_FIRST)) {
				if (deferred == null) {
					deferred = new ArrayList<Object[]>(children.size());
					while (it.hasNext()) {
						staffOutVOtoTreeNode(it.next(), node, selected, nodes, limit, maxDepth, deferred, depth + 1);
					}
					Iterator<Object[]> deferredIt = deferred.iterator();
					while (deferredIt.hasNext()) {
						Object[] newNode = deferredIt.next();
						staffOutVOtoTreeNode((StaffOutVO) newNode[0], (IDVOTreeNode) newNode[1], selected, nodes, limit, maxDepth, null, (Integer) newNode[2]);
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
					staffOutVOtoTreeNode(it.next(), node, selected, nodes, limit, maxDepth, null, depth + 1);
				}
			}
			return node;
		}
		return null;
	}

	@Override
	public String updateAction() {
		StaffInVO backup = new StaffInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getStaffService().updateStaff(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_STAFF_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_STAFF_PARENT_DEPTH));
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
