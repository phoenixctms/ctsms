package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.JournalCategoryVO;
import org.phoenixctms.ctsms.vo.JournalEntryInVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.JournalExcelVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.Urls;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class JournalBean extends ManagedBeanBase {

	public static void copyJournalEntryOutToIn(JournalEntryInVO in, JournalEntryOutVO out) {
		if (in != null && out != null) {
			JournalCategoryVO journalCategoryVO = out.getCategory();
			InventoryOutVO inventoryVO = out.getInventory();
			StaffOutVO staffVO = out.getStaff();
			CourseOutVO courseVO = out.getCourse();
			UserOutVO userVO = out.getUser();
			TrialOutVO trialVO = out.getTrial();
			ProbandOutVO probandVO = out.getProband();
			CriteriaOutVO criteriaVO = out.getCriteria();
			InputFieldOutVO inputFieldVO = out.getInputField();
			MassMailOutVO massMailVO = out.getMassMail();
			in.setCategoryId(journalCategoryVO == null ? null : journalCategoryVO.getId());
			in.setComment(out.getComment());
			in.setCourseId(courseVO == null ? null : courseVO.getId());
			in.setCriteriaId(criteriaVO == null ? null : criteriaVO.getId());
			in.setInputFieldId(inputFieldVO == null ? null : inputFieldVO.getId());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInventoryId(inventoryVO == null ? null : inventoryVO.getId());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setRealTimestamp(out.getRealTimestamp());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setTitle(out.getTitle());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setUserId(userVO == null ? null : userVO.getId());
			in.setMassMailId(massMailVO == null ? null : massMailVO.getId());
		}
	}

	public static void initJournalEntryDefaultValues(JournalEntryInVO in, Long entityId, JournalModule module) {
		if (in != null) {
			JournalCategoryVO categoryPreset = null;
			try {
				categoryPreset = WebUtil.getServiceLocator().getSelectionSetService().getJournalCategoryPreset(WebUtil.getAuthentication(), module);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			in.setCategoryId(categoryPreset == null ? null : categoryPreset.getId());
			in.setComment(Messages.getString(MessageCodes.JOURNAL_ENTRY_COMMENT_PRESET));
			in.setCourseId(JournalModule.COURSE_JOURNAL.equals(module) ? entityId : null);
			in.setCriteriaId(JournalModule.CRITERIA_JOURNAL.equals(module) ? entityId : null);
			in.setInputFieldId(JournalModule.INPUT_FIELD_JOURNAL.equals(module) ? entityId : null);
			in.setId(null);
			in.setVersion(null);
			in.setInventoryId(JournalModule.INVENTORY_JOURNAL.equals(module) ? entityId : null);
			in.setProbandId(JournalModule.PROBAND_JOURNAL.equals(module) ? entityId : null);
			in.setRealTimestamp(new Date());
			in.setStaffId(JournalModule.STAFF_JOURNAL.equals(module) ? entityId : null);
			in.setTitle(categoryPreset == null ? Messages.getString(MessageCodes.JOURNAL_ENTRY_TITLE_PRESET) : categoryPreset.getTitlePreset());
			in.setTrialId(JournalModule.TRIAL_JOURNAL.equals(module) ? entityId : null);
			in.setUserId(JournalModule.USER_JOURNAL.equals(module) ? entityId : null);
			in.setMassMailId(JournalModule.MASS_MAIL_JOURNAL.equals(module) ? entityId : null);
		}
	}

	private JournalEntryInVO in;
	private JournalEntryOutVO out;
	private Long entityId;
	private JournalModule module;
	private InventoryOutVO inventory;
	private StaffOutVO staff;
	private CourseOutVO course;
	private TrialOutVO trial;
	private ProbandOutVO proband;
	private CriteriaOutVO criteria;
	private InputFieldOutVO inputField;
	private UserOutVO user;
	private MassMailOutVO massMail;
	private ArrayList<SelectItem> categories;
	private ArrayList<SelectItem> filterCategories;
	private ArrayList<SelectItem> filterUsers;
	private JournalEntryLazyModel journalEntryModel;
	private JournalCategoryVO category;
	private boolean useJournalEncryption;

	public JournalBean() {
		super();
		useJournalEncryption = false;
		journalEntryModel = new JournalEntryLazyModel();
	}

	@Override
	public String addAction() {
		JournalEntryInVO backup = new JournalEntryInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getJournalService().addJournalEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT,
							MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				case STAFF_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
							MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				case COURSE_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT,
							MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				case TRIAL_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
							MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				case PROBAND_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
							MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				case USER_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_USER_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_USER_JOURNAL_ENTRY_COUNT,
							MessageCodes.USER_JOURNAL_TAB_TITLE, MessageCodes.USER_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				case CRITERIA_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_CRITERIA_JOURNAL_ENTRY_COUNT,
							MessageCodes.CRITERIA_JOURNAL_TAB_TITLE, MessageCodes.CRITERIA_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				case INPUT_FIELD_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT,
							MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE, MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
				case MASS_MAIL_JOURNAL:
					WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT,
							MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE, MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE_WITH_COUNT, new Long(journalEntryModel.getRowCount()));
					break;
				default:
					break;
			}
		}
	}

	private String changeAction(String param, JournalModule module) {
		DataTable.clearFilters("journal_list");
		out = null;
		this.entityId = WebUtil.stringToLong(param);
		this.module = module;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeCourse(String param) {
		changeCourseAction(param);
	}

	public String changeCourseAction(String param) {
		return changeAction(param, JournalModule.COURSE_JOURNAL);
	}

	public void changeCriteria(String param) {
		changeCriteriaAction(param);
	}

	public String changeCriteriaAction(String param) {
		return changeAction(param, JournalModule.CRITERIA_JOURNAL);
	}

	public void changeInputField(String param) {
		changeInputFieldAction(param);
	}

	public String changeInputFieldAction(String param) {
		return changeAction(param, JournalModule.INPUT_FIELD_JOURNAL);
	}

	public void changeInventory(String param) {
		changeInventoryAction(param);
	}

	public String changeInventoryAction(String param) {
		return changeAction(param, JournalModule.INVENTORY_JOURNAL);
	}

	public void changeMassMail(String param) {
		changeMassMailAction(param);
	}

	public String changeMassMailAction(String param) {
		return changeAction(param, JournalModule.MASS_MAIL_JOURNAL);
	}

	public void changeProband(String param) {
		changeProbandAction(param);
	}

	public String changeProbandAction(String param) {
		return changeAction(param, JournalModule.PROBAND_JOURNAL);
	}

	public void changeStaff(String param) {
		changeStaffAction(param);
	}

	public String changeStaffAction(String param) {
		return changeAction(param, JournalModule.STAFF_JOURNAL);
	}

	public void changeTrial(String param) {
		changeTrialAction(param);
	}

	public String changeTrialAction(String param) {
		return changeAction(param, JournalModule.TRIAL_JOURNAL);
	}

	public void changeUser(String param) {
		changeUserAction(param);
	}

	public String changeUserAction(String param) {
		return changeAction(param, JournalModule.USER_JOURNAL);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getJournalService().deleteJournalEntry(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public ArrayList<SelectItem> getCategories() {
		return categories;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_JOURNAL_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_JOURNAL_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		try {
			JournalExcelVO excel = WebUtil.getServiceLocator().getJournalService().exportJournal(WebUtil.getAuthentication(), module, entityId);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public ArrayList<SelectItem> getFilterCategories() {
		return filterCategories;
	}

	public ArrayList<SelectItem> getFilterUsers() {
		return filterUsers;
	}

	public JournalEntryInVO getIn() {
		return in;
	}

	public JournalEntryLazyModel getJournalEntryModel() {
		return journalEntryModel;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public JournalEntryOutVO getOut() {
		return out;
	}

	public IDVO getSelectedJournalEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			if (out.isSystemMessage()) {
				return Messages.getMessage(MessageCodes.SYSTEM_MESSAGE_TITLE, Long.toString(out.getId()), out.getTitle());
			} else {
				return Messages.getMessage(MessageCodes.JOURNAL_ENTRY_TITLE, Long.toString(out.getId()), out.getCategory().getName(), out.getTitle());
			}
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_JOURNAL_ENTRY);
		}
	}

	public void handleCategoryChange() {
		loadSelectedCategory();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && category != null) {
			requestContext.addCallbackParam(JSValues.AJAX_JOURNAL_CATEGORY_TITLE_PRESET_BASE64.toString(), JsUtil.encodeBase64(category.getTitlePreset(), false));
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.JOURNAL_ENTRY_ID);
		if (id != null) {
			this.load(id);
		} else {
			if (WebUtil.testParamValueExists(GetParamNames.CRITERIA_ID)) { // special treatment for js openXYCriteria() ...
				this.changeAction(WebUtil.getParamValue(GetParamNames.CRITERIA_ID), JournalModule.CRITERIA_JOURNAL);
			} else {
				DBModule criteriaModule = Urls.getCurrentSearchModule();
				if (criteriaModule != null) {
					CriteriaOutVO defaultCriteria = null;
					try {
						defaultCriteria = WebUtil.getServiceLocator().getSearchService().getDefaultCriteria(WebUtil.getAuthentication(), criteriaModule);
					} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
					} catch (AuthenticationException e) {
						WebUtil.publishException(e);
					}
					if (defaultCriteria != null) {
						this.changeAction(Long.toString(defaultCriteria.getId()), JournalModule.CRITERIA_JOURNAL);
					} else {
						this.changeAction(null, JournalModule.CRITERIA_JOURNAL);
					}
				} else {
					initIn();
					initSets();
				}
			}
		}
	}

	private void initIn() {
		if (in == null) {
			in = new JournalEntryInVO();
		}
		if (out != null) {
			copyJournalEntryOutToIn(in, out);
			if (in.getInventoryId() != null) {
				entityId = in.getInventoryId();
				module = JournalModule.INVENTORY_JOURNAL;
			} else if (in.getStaffId() != null) {
				entityId = in.getStaffId();
				module = JournalModule.STAFF_JOURNAL;
			} else if (in.getCourseId() != null) {
				entityId = in.getCourseId();
				module = JournalModule.COURSE_JOURNAL;
			} else if (in.getUserId() != null) {
				entityId = in.getUserId();
				module = JournalModule.USER_JOURNAL;
			} else if (in.getTrialId() != null) {
				entityId = in.getTrialId();
				module = JournalModule.TRIAL_JOURNAL;
			} else if (in.getProbandId() != null) {
				entityId = in.getProbandId();
				module = JournalModule.PROBAND_JOURNAL;
			} else if (in.getCriteriaId() != null) {
				entityId = in.getCriteriaId();
				module = JournalModule.CRITERIA_JOURNAL;
			} else if (in.getInputFieldId() != null) {
				entityId = in.getInputFieldId();
				module = JournalModule.INPUT_FIELD_JOURNAL;
			} else if (in.getMassMailId() != null) {
				entityId = in.getMassMailId();
				module = JournalModule.MASS_MAIL_JOURNAL;
			} else {
				entityId = null;
				module = null;
			}
		} else {
			initJournalEntryDefaultValues(in, entityId, module);
		}
	}

	private void initSets() {
		inventory = (JournalModule.INVENTORY_JOURNAL.equals(module) ? WebUtil.getInventory(entityId, null, null, null) : null);
		staff = (JournalModule.STAFF_JOURNAL.equals(module) ? WebUtil.getStaff(entityId, null, null, null) : null);
		course = (JournalModule.COURSE_JOURNAL.equals(module) ? WebUtil.getCourse(entityId, null, null, null) : null);
		trial = (JournalModule.TRIAL_JOURNAL.equals(module) ? WebUtil.getTrial(entityId) : null);
		proband = (JournalModule.PROBAND_JOURNAL.equals(module) ? WebUtil.getProband(entityId, null, null, null) : null);
		criteria = (JournalModule.CRITERIA_JOURNAL.equals(module) ? WebUtil.getCriteria(entityId) : null);
		user = (JournalModule.USER_JOURNAL.equals(module) ? WebUtil.getUser(entityId, null) : null);
		inputField = (JournalModule.INPUT_FIELD_JOURNAL.equals(module) ? WebUtil.getInputField(entityId) : null);
		massMail = (JournalModule.MASS_MAIL_JOURNAL.equals(module) ? WebUtil.getMassMail(entityId) : null);
		journalEntryModel.setEntityId(entityId);
		journalEntryModel.setModule(module);
		journalEntryModel.updateRowCount();
		if (Settings.getBoolean(SettingCodes.LOAD_JOURNAL_ENTRY_USERS, Bundle.SETTINGS, DefaultSettings.LOAD_JOURNAL_ENTRY_USERS) && module != null && entityId != null) {
			Collection<UserOutVO> userVOs = null;
			if (module != null) {
				try {
					userVOs = WebUtil.getServiceLocator().getJournalService().getJournalUsers(WebUtil.getAuthentication(), module, entityId,
							Settings.getBoolean(SettingCodes.LIMIT_JOURNAL_ENTRY_USERS, Bundle.SETTINGS, DefaultSettings.LIMIT_JOURNAL_ENTRY_USERS));
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
			}
			if (userVOs != null) {
				filterUsers = new ArrayList<SelectItem>(userVOs.size());
				Iterator<UserOutVO> it = userVOs.iterator();
				while (it.hasNext()) {
					UserOutVO userVO = it.next();
					if (userVO != null) {
						filterUsers.add(new SelectItem(Long.toString(userVO.getId()), WebUtil.getUserIdentityString(userVO)));
					}
				}
			} else {
				filterUsers = new ArrayList<SelectItem>();
			}
		} else {
			filterUsers = new ArrayList<SelectItem>();
		}
		filterUsers.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		useJournalEncryption = CommonUtil.getUseJournalEncryption(null, module);
		categories = WebUtil.getAvailableJournalCategories(module, in.getCategoryId());
		filterCategories = new ArrayList<SelectItem>(categories);
		filterCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		loadSelectedCategory();
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					break;
				case STAFF_JOURNAL:
					break;
				case COURSE_JOURNAL:
					break;
				case TRIAL_JOURNAL:
					if (WebUtil.isTrialLocked(trial)) {
						Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
					}
					break;
				case PROBAND_JOURNAL:
					if (WebUtil.isProbandLocked(proband)) {
						Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
					}
					break;
				case USER_JOURNAL:
					break;
				case CRITERIA_JOURNAL:
					break;
				case INPUT_FIELD_JOURNAL:
					break;
				case MASS_MAIL_JOURNAL:
					if (WebUtil.isMassMailLocked(massMail)) {
						Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MASS_MAIL_LOCKED);
					}
					break;
				default:
					break;
			}
		}
	}

	@Override
	public boolean isCreateable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					return (in.getInventoryId() == null ? false : !isSystemMessage());
				case STAFF_JOURNAL:
					return (in.getStaffId() == null ? false : !isSystemMessage());
				case COURSE_JOURNAL:
					return (in.getCourseId() == null ? false : !isSystemMessage());
				case USER_JOURNAL:
					return (in.getUserId() == null ? false : !isSystemMessage());
				case TRIAL_JOURNAL:
					return (in.getTrialId() == null ? false : (!WebUtil.isTrialLocked(trial) && !isSystemMessage()));
				case PROBAND_JOURNAL:
					return (in.getProbandId() == null ? false : (!WebUtil.isProbandLocked(proband) && !isSystemMessage()));
				case CRITERIA_JOURNAL:
					return (in.getCriteriaId() == null ? false : !isSystemMessage());
				case INPUT_FIELD_JOURNAL:
					return (in.getInputFieldId() == null ? false : !isSystemMessage());
				case MASS_MAIL_JOURNAL:
					return (in.getMassMailId() == null ? false : (!WebUtil.isMassMailLocked(massMail) && !isSystemMessage()));
				default:
					break;
			}
		}
		return false;
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isCriteriaJournal() {
		return JournalModule.CRITERIA_JOURNAL.equals(module);
	}

	@Override
	public boolean isEditable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					return isCreated() && !isSystemMessage();
				case STAFF_JOURNAL:
					return isCreated() && !isSystemMessage();
				case COURSE_JOURNAL:
					return isCreated() && !isSystemMessage();
				case TRIAL_JOURNAL:
					return isCreated() && !isSystemMessage() && !WebUtil.isTrialLocked(trial);
				case PROBAND_JOURNAL:
					return isCreated() && !isSystemMessage() && !WebUtil.isProbandLocked(proband);
				case USER_JOURNAL:
					return isCreated() && !isSystemMessage();
				case CRITERIA_JOURNAL:
					return isCreated() && !isSystemMessage();
				case INPUT_FIELD_JOURNAL:
					return isCreated() && !isSystemMessage();
				case MASS_MAIL_JOURNAL:
					return isCreated() && !isSystemMessage() && !WebUtil.isMassMailLocked(massMail);
				default:
					break;
			}
		}
		return isCreated() && !isSystemMessage();
	}

	public boolean isInputVisible() {
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					return true;
				case STAFF_JOURNAL:
					return true;
				case COURSE_JOURNAL:
					return true;
				case TRIAL_JOURNAL:
					return isCreated() || !WebUtil.isTrialLocked(trial);
				case PROBAND_JOURNAL:
					return isCreated() || !WebUtil.isProbandLocked(proband);
				case USER_JOURNAL:
					return true;
				case CRITERIA_JOURNAL:
					return true;
				case INPUT_FIELD_JOURNAL:
					return true;
				case MASS_MAIL_JOURNAL:
					return isCreated() || !WebUtil.isMassMailLocked(massMail);
				default:
					return true;
			}
		}
		return true;
	}

	public boolean isLoadFilterUsers() {
		return Settings.getBoolean(SettingCodes.LOAD_JOURNAL_ENTRY_USERS, Bundle.SETTINGS, DefaultSettings.LOAD_JOURNAL_ENTRY_USERS);
	}

	@Override
	public boolean isRemovable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_JOURNAL:
					return isCreated() && !isSystemMessage();
				case STAFF_JOURNAL:
					return isCreated() && !isSystemMessage();
				case COURSE_JOURNAL:
					return isCreated() && !isSystemMessage();
				case TRIAL_JOURNAL:
					return isCreated() && !isSystemMessage() && !WebUtil.isTrialLocked(trial);
				case PROBAND_JOURNAL:
					return isCreated() && !isSystemMessage() && !WebUtil.isProbandLocked(proband);
				case USER_JOURNAL:
					return isCreated() && !isSystemMessage();
				case CRITERIA_JOURNAL:
					return isCreated() && !isSystemMessage();
				case INPUT_FIELD_JOURNAL:
					return isCreated() && !isSystemMessage();
				case MASS_MAIL_JOURNAL:
					return isCreated() && !isSystemMessage() && !WebUtil.isMassMailLocked(massMail);
				default:
					break;
			}
		}
		return isCreated() && !isSystemMessage();
	}

	public boolean isSystemMessage() {
		if (out != null) {
			return out.isSystemMessage();
		}
		return false;
	}

	public boolean isUseJournalEncryption() {
		return useJournalEncryption;
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getJournalService().getJournalEntry(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_JOURNAL_ENTRY, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadSelectedCategory() {
		category = WebUtil.getJournalCategory(in.getCategoryId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedJournalEntry(IDVO journalEntry) {
		if (journalEntry != null) {
			this.out = (JournalEntryOutVO) journalEntry.getVo();
			if (out != null && !out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_JOURNAL_ENTRY, Long.toString(out.getId()));
				out = null;
			}
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getJournalService().updateJournalEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
