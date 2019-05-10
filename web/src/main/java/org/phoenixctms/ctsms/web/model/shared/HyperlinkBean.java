package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.HyperlinkCategoryVO;
import org.phoenixctms.ctsms.vo.HyperlinkInVO;
import org.phoenixctms.ctsms.vo.HyperlinkOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
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

@ManagedBean
@ViewScoped
public class HyperlinkBean extends ManagedBeanBase {

	public static void copyHyperlinkOutToIn(HyperlinkInVO in, HyperlinkOutVO out) {
		if (in != null && out != null) {
			HyperlinkCategoryVO hyperlinkCategoryVO = out.getCategory();
			InventoryOutVO inventoryVO = out.getInventory();
			StaffOutVO staffVO = out.getStaff();
			CourseOutVO courseVO = out.getCourse();
			TrialOutVO trialVO = out.getTrial();
			in.setActive(out.getActive());
			in.setCategoryId(hyperlinkCategoryVO == null ? null : hyperlinkCategoryVO.getId());
			in.setCourseId(courseVO == null ? null : courseVO.getId());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInventoryId(inventoryVO == null ? null : inventoryVO.getId());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setTitle(out.getTitle());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setUrl(out.getUrl());
		}
	}

	public static void initHyperlinkDefaultValues(HyperlinkInVO in, Long entityId, HyperlinkModule module) {
		if (in != null) {
			in.setActive(Settings.getBoolean(SettingCodes.HYPERLINK_ACTIVE_PRESET, Bundle.SETTINGS, DefaultSettings.HYPERLINK_ACTIVE_PRESET));
			in.setCategoryId(null);
			in.setCourseId(HyperlinkModule.COURSE_HYPERLINK.equals(module) ? entityId : null);
			in.setId(null);
			in.setVersion(null);
			in.setInventoryId(HyperlinkModule.INVENTORY_HYPERLINK.equals(module) ? entityId : null);
			in.setStaffId(HyperlinkModule.STAFF_HYPERLINK.equals(module) ? entityId : null);
			in.setTitle(Messages.getString(MessageCodes.HYPERLINK_TITLE_PRESET));
			in.setTrialId(HyperlinkModule.TRIAL_HYPERLINK.equals(module) ? entityId : null);
			in.setUrl(Settings.getString(SettingCodes.HYPERLINK_URL_PRESET, Bundle.SETTINGS, DefaultSettings.HYPERLINK_URL_PRESET));
		}
	}

	private HyperlinkInVO in;
	private HyperlinkOutVO out;
	private Long entityId;
	private HyperlinkModule module;
	private InventoryOutVO inventory;
	private StaffOutVO staff;
	private CourseOutVO course;
	private TrialOutVO trial;
	private ArrayList<SelectItem> categories;
	private ArrayList<SelectItem> filterCategories;
	private HyperlinkLazyModel hyperlinkModel;
	private HyperlinkCategoryVO category;

	public HyperlinkBean() {
		super();
		hyperlinkModel = new HyperlinkLazyModel();
	}

	@Override
	public String addAction() {
		HyperlinkInVO backup = new HyperlinkInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getHyperlinkService().addHyperlink(WebUtil.getAuthentication(), in);
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
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		if (module != null) {
			RequestContext requestContext;
			switch (module) {
				case INVENTORY_HYPERLINK:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INVENTORY_HYPERLINK_TAB_TITLE_BASE64,
							JSValues.AJAX_INVENTORY_HYPERLINK_COUNT, MessageCodes.INVENTORY_HYPERLINKS_TAB_TITLE, MessageCodes.INVENTORY_HYPERLINKS_TAB_TITLE_WITH_COUNT, new Long(
									hyperlinkModel.getRowCount()));
					if (operationSuccess && in.getInventoryId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64,
								JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.INVENTORY_JOURNAL, in.getInventoryId()));
					}
					break;
				case STAFF_HYPERLINK:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_HYPERLINK_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_HYPERLINK_COUNT,
							MessageCodes.STAFF_HYPERLINKS_TAB_TITLE, MessageCodes.STAFF_HYPERLINKS_TAB_TITLE_WITH_COUNT, new Long(hyperlinkModel.getRowCount()));
					if (operationSuccess && in.getStaffId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
								MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
					}
					break;
				case COURSE_HYPERLINK:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_COURSE_HYPERLINK_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_HYPERLINK_COUNT,
							MessageCodes.COURSE_HYPERLINKS_TAB_TITLE, MessageCodes.COURSE_HYPERLINKS_TAB_TITLE_WITH_COUNT, new Long(hyperlinkModel.getRowCount()));
					if (operationSuccess && in.getCourseId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT,
								MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.COURSE_JOURNAL, in.getCourseId()));
					}
					break;
				case TRIAL_HYPERLINK:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_HYPERLINK_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_HYPERLINK_COUNT,
							MessageCodes.TRIAL_HYPERLINKS_TAB_TITLE, MessageCodes.TRIAL_HYPERLINKS_TAB_TITLE_WITH_COUNT, new Long(hyperlinkModel.getRowCount()));
					if (operationSuccess && in.getTrialId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
								MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
					}
					break;
				default:
					break;
			}
		}
	}

	private String changeAction(String param, HyperlinkModule module) {
		LazyDataModelBase.clearFilters("hyperlink_list");
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
		return changeAction(param, HyperlinkModule.COURSE_HYPERLINK);
	}

	public void changeInventory(String param) {
		changeInventoryAction(param);
	}

	public String changeInventoryAction(String param) {
		return changeAction(param, HyperlinkModule.INVENTORY_HYPERLINK);
	}

	public void changeStaff(String param) {
		changeStaffAction(param);
	}

	public String changeStaffAction(String param) {
		return changeAction(param, HyperlinkModule.STAFF_HYPERLINK);
	}

	public void changeTrial(String param) {
		changeTrialAction(param);
	}

	public String changeTrialAction(String param) {
		return changeAction(param, HyperlinkModule.TRIAL_HYPERLINK);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getHyperlinkService().deleteHyperlink(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
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

	public ArrayList<SelectItem> getCategories() {
		return categories;
	}

	public ArrayList<SelectItem> getFilterCategories() {
		return filterCategories;
	}

	public HyperlinkLazyModel getHyperlinkModel() {
		return hyperlinkModel;
	}

	public HyperlinkInVO getIn() {
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

	public HyperlinkOutVO getOut() {
		return out;
	}

	public IDVO getSelectedHyperlink() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.HYPERLINK_TITLE, Long.toString(out.getId()), out.getTitle());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_HYPERLINK);
		}
	}

	public void handleCategoryChange() {
		loadSelectedCategory();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && category != null) {
			requestContext.addCallbackParam(JSValues.AJAX_HYPERLINK_CATEGORY_TITLE_PRESET_BASE64.toString(), JsUtil.encodeBase64(category.getTitlePreset(), false));
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.HYPERLINK_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new HyperlinkInVO();
		}
		if (out != null) {
			copyHyperlinkOutToIn(in, out);
			if (in.getInventoryId() != null) {
				entityId = in.getInventoryId();
				module = HyperlinkModule.INVENTORY_HYPERLINK;
			} else if (in.getStaffId() != null) {
				entityId = in.getStaffId();
				module = HyperlinkModule.STAFF_HYPERLINK;
			} else if (in.getCourseId() != null) {
				entityId = in.getCourseId();
				module = HyperlinkModule.COURSE_HYPERLINK;
			} else if (in.getTrialId() != null) {
				entityId = in.getTrialId();
				module = HyperlinkModule.TRIAL_HYPERLINK;
			} else {
				entityId = null;
				module = null;
			}
		} else {
			initHyperlinkDefaultValues(in, entityId, module);
		}
	}

	private void initSets() {
		inventory = (HyperlinkModule.INVENTORY_HYPERLINK.equals(module) ? WebUtil.getInventory(entityId, null, null, null) : null);
		staff = (HyperlinkModule.STAFF_HYPERLINK.equals(module) ? WebUtil.getStaff(entityId, null, null, null) : null);
		course = (HyperlinkModule.COURSE_HYPERLINK.equals(module) ? WebUtil.getCourse(entityId, null, null, null) : null);
		trial = (HyperlinkModule.TRIAL_HYPERLINK.equals(module) ? WebUtil.getTrial(entityId) : null);
		hyperlinkModel.setEntityId(entityId);
		hyperlinkModel.setModule(module);
		hyperlinkModel.updateRowCount();
		categories = WebUtil.getAvailableHyperlinkCategories(module, in.getCategoryId());
		filterCategories = new ArrayList<SelectItem>(categories);
		filterCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		loadSelectedCategory();
		if (module != null) {
			switch (module) {
				case INVENTORY_HYPERLINK:
					break;
				case STAFF_HYPERLINK:
					break;
				case COURSE_HYPERLINK:
					break;
				case TRIAL_HYPERLINK:
					if (WebUtil.isTrialLocked(trial)) {
						Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
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
				case INVENTORY_HYPERLINK:
					return (in.getInventoryId() == null ? false : true);
				case STAFF_HYPERLINK:
					return (in.getStaffId() == null ? false : true);
				case COURSE_HYPERLINK:
					return (in.getCourseId() == null ? false : true);
				case TRIAL_HYPERLINK:
					return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
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

	@Override
	public boolean isEditable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_HYPERLINK:
					return super.isEditable();
				case STAFF_HYPERLINK:
					return super.isEditable();
				case COURSE_HYPERLINK:
					return super.isEditable();
				case TRIAL_HYPERLINK:
					return isCreated() && !WebUtil.isTrialLocked(trial);
				default:
					break;
			}
		}
		return super.isEditable();
	}

	public boolean isInputVisible() {
		if (module != null) {
			switch (module) {
				case INVENTORY_HYPERLINK:
					return true;
				case STAFF_HYPERLINK:
					return true;
				case COURSE_HYPERLINK:
					return true;
				case TRIAL_HYPERLINK:
					return isCreated() || !WebUtil.isTrialLocked(trial);
				default:
					break;
			}
		}
		return true;
	}

	@Override
	public boolean isRemovable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_HYPERLINK:
					return super.isRemovable();
				case STAFF_HYPERLINK:
					return super.isRemovable();
				case COURSE_HYPERLINK:
					return super.isRemovable();
				case TRIAL_HYPERLINK:
					return isCreated() && !WebUtil.isTrialLocked(trial);
				default:
					break;
			}
		}
		return super.isRemovable();
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getHyperlinkService().getHyperlink(WebUtil.getAuthentication(), id);
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

	private void loadSelectedCategory() {
		category = WebUtil.getHyperlinkCategory(in.getCategoryId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedHyperlink(IDVO hyperlink) {
		if (hyperlink != null) {
			this.out = (HyperlinkOutVO) hyperlink.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getHyperlinkService().updateHyperlink(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
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
}
