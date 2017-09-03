package org.phoenixctms.ctsms.web.model.user;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.JournalCategoryVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class UserActivityBean extends ManagedBeanBase {

	private Long userId;
	private UserActivityLazyModel userActivityModel;
	private ArrayList<SelectItem> filterDbModules;
	private ArrayList<SelectItem> filterJournalModules;
	private ArrayList<SelectItem> filterCategories;
	private final static String JOURNAL_CATEGORY_NAME = "{0} - {1}";

	public UserActivityBean() {
		super();
		userActivityModel = new UserActivityLazyModel();
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("useractivity_list");
		this.userId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public String getCriteriaName(JournalEntryOutVO journalEntry) {
		if (journalEntry != null) {
			if (journalEntry.getCriteria() != null) {
				return CommonUtil.criteriaOutVOToString(journalEntry.getCriteria());
			}
		}
		return "";
	}

	public String getEntityName(JournalEntryOutVO journalEntry) {
		if (journalEntry != null) {
			if (journalEntry.getInventory() != null) {
				return CommonUtil.inventoryOutVOToString(journalEntry.getInventory());
			} else if (journalEntry.getStaff() != null) {
				return CommonUtil.staffOutVOToString(journalEntry.getStaff());
			} else if (journalEntry.getCourse() != null) {
				return CommonUtil.courseOutVOToString(journalEntry.getCourse());
			} else if (journalEntry.getUser() != null) {
				return CommonUtil.userOutVOToString(journalEntry.getUser());
			} else if (journalEntry.getTrial() != null) {
				return CommonUtil.trialOutVOToString(journalEntry.getTrial());
			} else if (journalEntry.getProband() != null) {
				return CommonUtil.probandOutVOToString(journalEntry.getProband());
			} else if (journalEntry.getInputField() != null) {
				return CommonUtil.inputFieldOutVOToString(journalEntry.getInputField());
			}
		}
		return "";
	}

	public String getEntityNodeStyleClass(JournalEntryOutVO journalEntry) {
		if (journalEntry != null) {
			if (journalEntry.getInventory() != null) {
				return journalEntry.getInventory().getCategory().getNodeStyleClass();
			} else if (journalEntry.getStaff() != null) {
				return journalEntry.getStaff().getCategory().getNodeStyleClass();
			} else if (journalEntry.getCourse() != null) {
				return journalEntry.getCourse().getCategory().getNodeStyleClass();
			} else if (journalEntry.getTrial() != null) {
				return journalEntry.getTrial().getStatus().getNodeStyleClass();
			} else if (journalEntry.getProband() != null) {
				return journalEntry.getProband().isDecrypted() ? journalEntry.getProband().getCategory().getNodeStyleClass() : "ctsms-icon-encrypted";
			} else if (journalEntry.getCriteria() != null) {
				return ""; // "ctsms-icon-search";
			} else if (journalEntry.getInputField() != null) {
				return WebUtil.getInputFieldIcon(journalEntry.getInputField());
			}
		}
		return "";
	}

	public ArrayList<SelectItem> getFilterCategories() {
		return filterCategories;
	}

	public ArrayList<SelectItem> getFilterDbModules() {
		return filterDbModules;
	}

	public ArrayList<SelectItem> getFilterJournalModules() {
		return filterJournalModules;
	}

	public String getOpenEntityJSCall(JournalEntryOutVO journalEntry) {
		if (journalEntry != null) {
			if (journalEntry.getInventory() != null) {
				return "openInventory(" + Long.toString(journalEntry.getInventory().getId()) + ")";
			} else if (journalEntry.getStaff() != null) {
				return "openStaff(" + Long.toString(journalEntry.getStaff().getId()) + ")";
			} else if (journalEntry.getCourse() != null) {
				return "openCourse(" + Long.toString(journalEntry.getCourse().getId()) + ")";
			} else if (journalEntry.getUser() != null) {
				return "openUser(" + Long.toString(journalEntry.getUser().getId()) + ")";
			} else if (journalEntry.getTrial() != null) {
				return "openTrial(" + Long.toString(journalEntry.getTrial().getId()) + ")";
			} else if (journalEntry.getProband() != null) {
				return "openProband(" + Long.toString(journalEntry.getProband().getId()) + ")";
			} else if (journalEntry.getCriteria() != null) {
				StringBuilder sb = new StringBuilder();
				switch (journalEntry.getCriteria().getModule()) {
					case INVENTORY_DB:
						sb.append("openInventorySearch(");
						break;
					case STAFF_DB:
						sb.append("openStaffSearch(");
						break;
					case COURSE_DB:
						sb.append("openCourseSearch(");
						break;
					case TRIAL_DB:
						sb.append("openTrialSearch(");
						break;
					case PROBAND_DB:
						sb.append("openProbandSearch(");
						break;
					case INPUT_FIELD_DB:
						sb.append("openInputFieldSearch(");
						break;
					case USER_DB:
						sb.append("openUserSearch(");
						break;
					default:
						return "";
				}
				sb.append(Long.toString(journalEntry.getCriteria().getId()));
				sb.append(")");
				return sb.toString();
			} else if (journalEntry.getInputField() != null) {
				return "openInputField(" + Long.toString(journalEntry.getInputField().getId()) + ")";
			}
		}
		return "";
	}

	public UserActivityLazyModel getUserActivityModel() {
		return userActivityModel;
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		userActivityModel.setModifiedUserId(userId);
		userActivityModel.updateRowCount();
		if (filterDbModules == null) {
			filterDbModules = WebUtil.getDBModules();
			filterDbModules.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		if (filterJournalModules == null) {
			filterJournalModules = WebUtil.getJournalModules();
			filterJournalModules.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		if (filterCategories == null) {
			Collection<JournalCategoryVO> categoryVOs = null;
			try {
				categoryVOs = WebUtil.getServiceLocator().getSelectionSetService().getJournalCategories(WebUtil.getAuthentication(), null, null);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
			if (categoryVOs != null) {
				filterCategories = new ArrayList<SelectItem>(categoryVOs.size());
				Iterator<JournalCategoryVO> it = categoryVOs.iterator();
				while (it.hasNext()) {
					JournalCategoryVO categoryVO = it.next();
					filterCategories.add(new SelectItem(categoryVO.getId().toString(), MessageFormat.format(JOURNAL_CATEGORY_NAME, WebUtil.getJournalModule(categoryVO.getModule())
							.getName(), categoryVO.getName())));
				}
			} else {
				filterCategories = new ArrayList<SelectItem>();
			}
			filterCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public boolean isJournalEntryEncrypted(JournalEntryOutVO journalEntry) {
		if (journalEntry != null) {
			return CommonUtil.getUseJournalEncryption(journalEntry.getSystemMessageModule(), journalEntry.getCategory() == null ? null : journalEntry.getCategory().getModule());
		}
		return false;
	}

	public void refresh() {
		userActivityModel.updateRowCount();
		LazyDataModelBase.clearFilters("useractivity_list");
	}
}
