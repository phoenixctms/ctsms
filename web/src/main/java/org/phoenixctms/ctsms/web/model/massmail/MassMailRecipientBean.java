package org.phoenixctms.ctsms.web.model.massmail;


import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientInVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.shared.MassMailRecipientBeanBase;
import org.phoenixctms.ctsms.web.model.shared.ProbandMultiPickerModel;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class MassMailRecipientBean extends MassMailRecipientBeanBase {



	private static void initMassMailRecipientDefaultValues(MassMailRecipientInVO in, Long massMailId) {
		if (in != null) {
			in.setProbandId(null);
			in.setMassMailId(massMailId);
			in.setId(null);
			in.setVersion(null);

		}
	}


	private Long massMailId;
	private MassMailOutVO massMail;
	private ProbandMultiPickerModel probandMultiPicker;
	// private Long bulkAddRoleId;
	// private boolean bulkAddAccess;
	// // private boolean bulkAddNotifyTimelineEvent;
	// private ArrayList<SelectItem> availableRoles;

	// private HashMap<Long, EmailMessageVO> emailMessageCache;

	public MassMailRecipientBean() {
		super();
		// emailMessageCache = new HashMap<Long, EmailMessageVO>();

		probandMultiPicker = new ProbandMultiPickerModel();
	}



	public final void addBulk() {
		actionPostProcess(addBulkAction());
	}

	public String addBulkAction() {
		try {
			// if (bulkAddRoleId != null) {
			Set<Long> ids = this.probandMultiPicker.getSelectionIds();
			Iterator<MassMailRecipientOutVO> it = WebUtil.getServiceLocator().getMassMailService()
					.addMassMailRecipients(WebUtil.getAuthentication(), massMailId, ids).iterator();
			while (it.hasNext()) {
				this.probandMultiPicker.removeId(it.next().getProband().getId());
			}
			int itemsLeft = probandMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_ADD_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_ADD_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			massMailRecipientModel.updateRowCount();
			return BULK_ADD_OUTCOME;
			// } else {
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.BULK_ADD_TEAM_MEMBER_ROLE_REQUIRED);
			// }
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

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64,
				JSValues.AJAX_MASS_MAIL_RECIPIENT_COUNT,
				MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE, MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE_WITH_COUNT, new Long(massMailRecipientModel.getRowCount()));
		if (operationSuccess && in.getMassMailId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT,
					MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE, MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.MASS_MAIL_JOURNAL, in.getMassMailId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("mass_mail_recipient_list");
		out = null;
		this.massMailId = id;
		probandMultiPicker.clear();
		// bulkAddRoleId = null;
		// bulkAddAccess = Settings.getBoolean(SettingCodes.TEAM_MEMBER_ACCESS_PRESET, Bundle.SETTINGS, DefaultSettings.TEAM_MEMBER_ACCESS_PRESET);
		// bulkAddNotifyTimelineEvent = Settings.getBoolean(SettingCodes.TEAM_MEMBER_NOTIFY_TIMELINE_EVENT_PRESET, Bundle.SETTINGS,
		// DefaultSettings.TEAM_MEMBER_NOTIFY_TIMELINE_EVENT_PRESET);
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}



	public final void deleteBulk() {
		actionPostProcess(deleteBulkAction());
	}

	public String deleteBulkAction() {
		try {
			Set<Long> ids = this.probandMultiPicker.getSelectionIds();
			Iterator<MassMailRecipientOutVO> it = WebUtil.getServiceLocator().getMassMailService()
					.deleteMassMailRecipients(WebUtil.getAuthentication(), massMailId, ids).iterator();
			while (it.hasNext()) {
				this.probandMultiPicker.removeId(it.next().getProband().getId());
			}
			int itemsLeft = probandMultiPicker.getSelection().size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_DELETE_OPERATION_INCOMPLETE, ids.size() - itemsLeft, ids.size());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_DELETE_OPERATION_SUCCESSFUL, ids.size(), ids.size());
			}
			massMailRecipientModel.updateRowCount();
			return BULK_DELETE_OUTCOME;
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









	// public ShiftDurationSummaryModel getShiftDurationModel(StaffOutVO staff) {
	// return ShiftDurationSummaryModel.getCachedShiftDurationModel(staff, now, shiftDurationModelCache);
	// }
	public ProbandMultiPickerModel getProbandMultiPicker() {
		return probandMultiPicker;
	}

	public String getProbandName() {
		return WebUtil.probandIdToName(in.getProbandId());
	}



	// public StreamedContent getTeamMembersExcelStreamedContent() throws Exception {
	// try {
	// TeamMembersExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportTeamMembers(WebUtil.getAuthentication(), trialId);
	// return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
	// } catch (AuthenticationException e) {
	// WebUtil.publishException(e);
	// throw e;
	// } catch (AuthorisationException e) {
	// throw e;
	// } catch (ServiceException e) {
	// throw e;
	// } catch (IllegalArgumentException e) {
	// throw e;
	// }
	// }
	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.MASS_MAIL_RECIPIENT_TITLE, Long.toString(out.getId()), out.getProband().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_MASS_MAIL_RECIPIENT);
		}
	}

	// public void handleAccessChange() {
	// if (!in.getAccess()) {
	// in.setSign(false);
	// in.setResolve(false);
	// in.setVerify(false);
	// }
	// }
	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.MASS_MAIL_RECIPIENT_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	protected void initIn() {
		if (in == null) {
			in = new MassMailRecipientInVO();
		}
		if (out != null) {
			copyMassMailRecipientOutToIn(in, out);
			massMailId = in.getMassMailId();
		} else {
			initMassMailRecipientDefaultValues(in, massMailId);
		}
	}

	protected void initSets() {
		// now = new Date();
		// emailMessageCache.clear();
		massMailRecipientModel.setMassMailId(in.getMassMailId());
		massMailRecipientModel.updateRowCount();
		// Collection<TeamMemberRoleVO> roleVOs = null;
		// try {
		// roleVOs = WebUtil.getServiceLocator().getSelectionSetService().getAvailableTeamMemberRoles(WebUtil.getAuthentication(), in.getTrialId(), in.getRoleId());
		// } catch (ServiceException e) {
		// } catch (AuthenticationException e) {
		// WebUtil.publishException(e);
		// } catch (AuthorisationException e) {
		// } catch (IllegalArgumentException e) {
		// }
		// if (roleVOs != null) {
		// availableRoles = new ArrayList<SelectItem>(roleVOs.size());
		// Iterator<TeamMemberRoleVO> it = roleVOs.iterator();
		// while (it.hasNext()) {
		// TeamMemberRoleVO roleVO = it.next();
		// availableRoles.add(new SelectItem(roleVO.getId().toString(), roleVO.getName()));
		// }
		// } else {
		// availableRoles = new ArrayList<SelectItem>();
		// }
		massMail = WebUtil.getMassMail(this.in.getMassMailId());
		if (WebUtil.isMassMailLocked(massMail)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MASS_MAIL_LOCKED);
		}
	}
	// public boolean isBulkAddNotifyTimelineEvent() {
	// return bulkAddNotifyTimelineEvent;
	// }
	@Override
	public boolean isCreateable() {
		return (in.getMassMailId() == null ? false : !WebUtil.isMassMailLocked(massMail));
	}

	// public boolean isBulkAddAccess() {
	// return bulkAddAccess;
	// }



	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isMassMailLocked(massMail);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isMassMailLocked(massMail);
	}

	public boolean isProbandBulkCreateable() {
		return isCreateable() && probandMultiPicker.getIsEnabled();
	}

	public boolean isProbandBulkRemovable() {
		return isCreateable() && probandMultiPicker.getIsEnabled();
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isMassMailLocked(massMail);
	}






	// public void setBulkAddAccess(boolean bulkAddAccess) {
	// this.bulkAddAccess = bulkAddAccess;
	// }
	// public void setBulkAddNotifyTimelineEvent(boolean bulkAddNotifyTimelineEvent) {
	// this.bulkAddNotifyTimelineEvent = bulkAddNotifyTimelineEvent;
	// }

	// public void setBulkAddRoleId(Long bulkAddRoleId) {
	// this.bulkAddRoleId = bulkAddRoleId;
	// }

	// @Override
	// public String updateAction() {
	// sanitizeInVals();
	// try {
	// out = WebUtil.getServiceLocator().getTrialService().updateTeamMember(WebUtil.getAuthentication(), in);
	// initIn();
	// initSets();
	// addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
	// return UPDATE_OUTCOME;
	// } catch (ServiceException e) {
	// Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
	// } catch (AuthenticationException e) {
	// Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
	// WebUtil.publishException(e);
	// } catch (AuthorisationException e) {
	// Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
	// } catch (IllegalArgumentException e) {
	// Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
	// }
	// return ERROR_OUTCOME;
	// }
}
