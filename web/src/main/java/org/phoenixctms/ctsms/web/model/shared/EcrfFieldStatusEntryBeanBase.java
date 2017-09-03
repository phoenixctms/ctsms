
package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusTypeVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class EcrfFieldStatusEntryBeanBase extends ManagedBeanBase {

	protected static void copyEcrfFieldStatusEntryOutToIn(ECRFFieldStatusEntryInVO in, ECRFFieldStatusEntryOutVO out) {
		if (in != null && out != null) {
			ProbandListEntryOutVO listEntryVO = out.getListEntry();
			ECRFFieldOutVO ecrfFieldVO = out.getEcrfField();
			ECRFFieldStatusTypeVO statusVO = out.getStatus();
			in.setId(out.getId());
			in.setListEntryId(listEntryVO == null ? null : listEntryVO.getId());
			in.setEcrfFieldId(ecrfFieldVO == null ? null : ecrfFieldVO.getId());
			in.setIndex(out.getIndex());
			in.setComment(out.getComment());
			in.setStatusId(statusVO == null ? null : statusVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	protected static void initEcrfFieldStatusEntryDefaultValues(ECRFFieldStatusEntryInVO in, Long probandListEntryId, Long ecrfFieldId, Long index) {
		if (in != null) {
			in.setId(null);
			in.setListEntryId(probandListEntryId);
			in.setEcrfFieldId(ecrfFieldId);
			in.setIndex(index);
			in.setComment(Messages.getString(MessageCodes.ECRF_FIELD_STATUS_ENTRY_COMMENT_PRESET));
			in.setStatusId(null);
			in.setVersion(null);
		}
	}

	protected ECRFFieldStatusEntryInVO in;
	protected ECRFFieldStatusEntryOutVO out;
	protected Long listEntryId;
	private ProbandListEntryOutVO probandListEntry;
	private ECRFStatusEntryVO ecrfStatus;
	protected Long ecrfFieldId;
	protected ECRFFieldOutVO ecrfField;
	protected Long index;
	// private ECRFFieldStatusQueue queue;
	// private Long probandListEntryId;
	// private ProbandListEntryOutVO probandListEntry;
	protected ECRFFieldStatusEntryOutVO lastStatus;
	private ECRFFieldStatusTypeVO statusType;
	protected boolean isLastStatus;
	private ArrayList<SelectItem> statusTypes;
	private ECRFFieldValueOutVO value;

	public EcrfFieldStatusEntryBeanBase() {
		super();
	}

	@Override
	public String addAction()
	{
		ECRFFieldStatusEntryInVO backup = new ECRFFieldStatusEntryInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		// sanitizeInVals();
		clearFromCache(out);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addEcrfFieldStatusEntry(WebUtil.getAuthentication(), getQueue(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(getMessagesId(), MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}



	protected void addMessages() {
		if (WebUtil.isProbandLocked(probandListEntry)) {
			Messages.addLocalizedMessageClientId(getMessagesId(), FacesMessage.SEVERITY_WARN,
					MessageCodes.PROBAND_LOCKED);
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		} else if (WebUtil.isTrialLocked(probandListEntry)) {
			Messages.addLocalizedMessageClientId(getMessagesId(), FacesMessage.SEVERITY_WARN,
					MessageCodes.TRIAL_LOCKED);
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		} else if (probandListEntry != null && probandListEntry.getLastStatus() != null && !probandListEntry.getLastStatus().getStatus().getEcrfValueInputEnabled()) {
			Messages.addLocalizedMessageClientId(getMessagesId(), FacesMessage.SEVERITY_INFO,
					MessageCodes.ECRF_VALUE_INPUT_DISABLED, probandListEntry.getLastStatus().getStatus().getName());
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.ECRF_VALUE_INPUT_DISABLED, probandListEntry.getLastStatus().getStatus().getName());
		} else if (ecrfStatus != null && ecrfStatus.getStatus().getFieldStatusLockdown()) { // .getValueLockdown()) {
			Messages.addLocalizedMessageClientId(getMessagesId(), FacesMessage.SEVERITY_WARN,
					MessageCodes.ECRF_FIELD_STATUS_LOCKED_STATUS, ecrfStatus.getStatus().getName());
			// Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.ECRF_LOCKED_STATUS, ecrfStatus.getStatus().getName());
		} else if (getQueue() != null && statusTypes.size() == 0) {
			Messages.addLocalizedMessageClientId(getMessagesId(), FacesMessage.SEVERITY_WARN,
					MessageCodes.NO_NEW_ECRF_FIELD_STATUS, getQueueName());
		}
	}

	protected void clearFromCache(ECRFFieldStatusEntryOutVO status) {

	}

	// protected abstract String getDataTableId(); // {
	// return "ecrffieldstatus_" + ecrfFieldStatusEntryModel.getQueue().getValue() + "_list";
	// }

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		clearFromCache(out);
		try {

			out = WebUtil.getServiceLocator().getTrialService().deleteEcrfFieldStatusEntry(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(getMessagesId(), MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	// @Override
	// protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
	// RequestContext requestContext = RequestContext.getCurrentInstance();
	// if (requestContext != null) {
	// requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
	// WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext,
	// JSValues.valueOf("AJAX_" + ecrfFieldStatusEntryModel.getQueue().name() + "_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64"),
	// JSValues.valueOf("AJAX_" + ecrfFieldStatusEntryModel.getQueue().name() + "_ECRF_FIELD_STATUS_ENTRY_COUNT"),
	// MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_WITH_COUNT, new Long(ecrfFieldStatusEntryModel.getRowCount()),getQueueName());
	// }
	// //
	// //
	// // WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_BASE64, JSValues.AJAX_ECRF_FIELD_STATUS_ENTRY_COUNT,
	// // MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
	// // WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
	// // if (operationSuccess && in.getTrialId() != null) {
	// // WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
	// // MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
	// // WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
	// // }
	// }
	public ECRFFieldStatusEntryInVO getIn() {
		return in;
	}

	protected abstract String getMessagesId();// {
	// return "ecrffieldstatus_" + ecrfFieldStatusEntryModel.getQueue().getValue() + "_messages"
	// }

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	// public EcrfFieldStatusEntryLazyModel getEcrfFieldStatusEntryModel() {
	// return ecrfFieldStatusEntryModel;
	// }
	public ECRFFieldStatusEntryOutVO getOut() {
		return out;
	}

	public abstract ECRFFieldStatusQueue getQueue(); // {
	// return ecrfFieldStatusEntryModel.getQueue();
	// }

	public String getQueueName() {
		return WebUtil.getEcrfFieldStatusQueueName(getQueue());
	}

	public EcrfFieldSection getSection() {
		if (ecrfField != null && value != null) {
			return new EcrfFieldSection(ecrfField.getSection(), value.getIndex());
		}
		return null;
	}

	public IDVO getSelectedEcrfFieldStatusEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public boolean getShowResolvedStateMessage() {
		if (statusType != null) {
			return statusType.getResolved();
		}
		return false;
	}



	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	public ECRFFieldValueOutVO getValue() {
		return value;
	}

	// public String getTabTitle() {
	// return WebUtil.getTabTitleString(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE_WITH_COUNT, new
	// Long(ecrfFieldStatusEntryModel.getRowCount()), getQueueName());
	// //return Messages.getMessage(MessageCodes.ECRF_FIELD_STATUS_ENTRY_TAB_TITLE, getQueueName(), ecrfFieldStatusEntryModel.getRowCount());
	// }
	public void handleStatusTypeChange() {
		statusType = null;
		if (in.getStatusId() != null) {
			try {
				statusType = WebUtil.getServiceLocator().getSelectionSetService().getEcrfFieldStatusType(WebUtil.getAuthentication(), in.getStatusId());
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		// // if (statusType != null && statusType.isProposed() && CommonUtil.isEmptyString(in.getComment())) { // && in.getListEntryId() != null && in.getEcrfFieldId() != null) {
		// // ECRFFieldValueOutVO value = null;
		// // try {
		// // value = WebUtil.getServiceLocator().getTrialService()
		// // .getEcrfFieldValue(WebUtil.getAuthentication(), in.getListEntryId(), in.getEcrfFieldId(), in.getIndex()).getPageValues().iterator().next();
		// // } catch (NoSuchElementException e) {
		// // } catch (ServiceException e) {
		// // } catch (AuthenticationException e) {
		// // WebUtil.publishException(e);
		// // } catch (AuthorisationException e) {
		// // } catch (IllegalArgumentException e) {
		// // }
		// // if (value != null && !CommonUtil.isEmptyString(value.getReasonForChange())) {
		// if (statusType != null && statusType.isProposed() && CommonUtil.isEmptyString(in.getComment()) && value != null && !CommonUtil.isEmptyString(value.getReasonForChange()))
		// {
		// in.setComment(Messages.getMessage(MessageCodes.ECRF_FIELD_STATUS_ENTRY_RESPONSE_COMMENT_PRESET, value.getReasonForChange()));
		// }
		// // }
	}

	protected void initIn() {
		if (in == null) {
			in = new ECRFFieldStatusEntryInVO();
		}
		if (out != null) {
			copyEcrfFieldStatusEntryOutToIn(in, out);
			listEntryId = in.getListEntryId();
			// probandListEntry = out.getListEntry();
			ecrfFieldId = in.getEcrfFieldId();
			index = in.getIndex();
		} else {
			initEcrfFieldStatusEntryDefaultValues(in, listEntryId, ecrfFieldId, index);
		}
	}

	protected void initSets() {
		initSpecificSets();
		isLastStatus = false;
		lastStatus = null;
		if (getQueue() != null && in.getListEntryId() != null && in.getEcrfFieldId() != null) {
			try {
				lastStatus = WebUtil.getServiceLocator().getTrialService()
						.getLastEcrfFieldStatusEntry(WebUtil.getAuthentication(), getQueue(), in.getListEntryId(), in.getEcrfFieldId(), in.getIndex());
				if (out != null && lastStatus != null) {
					isLastStatus = (out.getId() == lastStatus.getId());
				}
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		Collection<ECRFFieldStatusTypeVO> statusTypeVOs = null;
		if (out != null) {
			statusType = out.getStatus();
			statusTypeVOs = new ArrayList<ECRFFieldStatusTypeVO>(1);
			statusTypeVOs.add(out.getStatus());
		} else {
			statusType = null;
			try {
				if (lastStatus != null) {
					statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
							.getEcrfFieldStatusTypeTransitions(WebUtil.getAuthentication(), lastStatus.getStatus().getId(), false);
				} else if (getQueue() != null) {
					statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
							.getInitialEcrfFieldStatusTypes(WebUtil.getAuthentication(), getQueue(), false);
				}
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<ECRFFieldStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				ECRFFieldStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		value = null;
		if (in.getListEntryId() != null && in.getEcrfFieldId() != null) {
			try {
				value = WebUtil.getServiceLocator().getTrialService()
						.getEcrfFieldValue(WebUtil.getAuthentication(), in.getListEntryId(), in.getEcrfFieldId(), in.getIndex()).getPageValues().iterator().next();
			} catch (NoSuchElementException e) {
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		if (value != null) {
			probandListEntry = value.getListEntry();
			ecrfField = value.getEcrfField();
		} else {
			probandListEntry = WebUtil.getProbandListEntry(in.getListEntryId());
			ecrfField = WebUtil.getEcrfField(in.getEcrfFieldId());
		}
		ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfField != null ? ecrfField.getEcrf().getId() : null, in.getListEntryId());
		// if (statusTypes.size() == 0) {
		// Messages.addLocalizedMessageClientId("ecrffieldstatus_" + ecrfFieldStatusEntryModel.getQueue().getValue() + "_messages", FacesMessage.SEVERITY_WARN,
		// MessageCodes.PROBAND_LOCKED);
		// } else
		addMessages();
	}

	protected abstract void initSpecificSets(); // {
	// ecrfFieldStatusEntryModel.setListEntryId(in.getListEntryId());
	// ecrfFieldStatusEntryModel.setEcrfFieldId(in.getEcrfFieldId());
	// ecrfFieldStatusEntryModel.setIndex(in.getIndex());
	// ecrfFieldStatusEntryModel.updateRowCount();
	// }

	@Override
	public boolean isCreateable() {
		if (getQueue() == null || in.getListEntryId() == null || in.getEcrfFieldId() == null) {
			return false;
		} else if (!isEnabled()) {
			return false;
		}
		return !isCreated();
		// return (in.getListEntryId() == null || in.getEcrfFieldId() == null ? false : !isCreated() && !WebUtil.isTrialLocked(probandListEntry) &&
		// !WebUtil.isProbandLocked(probandListEntry));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return false;
	}



	protected boolean isEnabled() {
		if (out == null && statusTypes != null && statusTypes.size() == 0) {
			return false;
		} else if (WebUtil.isTrialLocked(probandListEntry)) {
			return false;
		} else if (WebUtil.isProbandLocked(probandListEntry)) {
			return false;
		} else if (probandListEntry != null && probandListEntry.getLastStatus() != null && !probandListEntry.getLastStatus().getStatus().getEcrfValueInputEnabled()) {
			return false;
			// } else if (ecrfStatus != null && ecrfStatus.getStatus().getValueLockdown()) {
		} else if (ecrfStatus != null && ecrfStatus.getStatus().getFieldStatusLockdown()) {
			return false;
		}
		return true;
	}

	public boolean isInputVisible() {
		return getQueue() != null && (isCreated() || isEnabled());
		// if (!isCreated()) {
		// return false;
		// } else if (!isEnabled()) {
		// return false;
		// }
		// return true;
		// return isCreated() || (!WebUtil.isTrialLocked(probandListEntry) && !WebUtil.isProbandLocked(probandListEntry));
	}

	@Override
	public boolean isRemovable() {
		// return isLastStatus && !WebUtil.isTrialLocked(probandListEntry) && !WebUtil.isProbandLocked(probandListEntry);
		return isLastStatus && isEnabled();
	}



	public boolean isUpdateCommentEnabled() {
		//&& CommonUtil.isEmptyString(in.getComment())
		return statusType != null && statusType.isProposed()  && value != null && !CommonUtil.isEmptyString(value.getReasonForChange());
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		clearFromCache(out);
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getEcrfFieldStatusEntry(WebUtil.getAuthentication(), id);
			// if (!out.isDecrypted()) {
			// Messages.addLocalizedMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_LIST_STATUS_ENTRY,
			// Long.toString(out.getId()));
			// out = null;
			// return ERROR_OUTCOME;
			// }
			return LOAD_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessageClientId(getMessagesId(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	// private void sanitizeInVals() {
	// // if (lastStatus != null && in.getRealTimestamp().getSeconds() == 0) {
	// // long delta = CommonUtil.dateDeltaSecs(in.getRealTimestamp(), lastStatus.getRealTimestamp());
	// // if (delta > 0 && delta < 60) {
	// // in.setRealTimestamp(lastStatus.getRealTimestamp());
	// // }
	// // }
	// }
	@Override
	public String resetAction() {
		clearFromCache(out);
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}



	// public void setProbandListEntry(ProbandListEntryOutVO probandListEntry) {
	// this.probandListEntry = probandListEntry;
	// }
	//
	//
	//
	//
	// public void setEcrfStatus(ECRFStatusEntryVO ecrfStatus) {
	// this.ecrfStatus = ecrfStatus;
	// }

	public void setSelectedEcrfFieldStatusEntry(IDVO ecrfFieldStatusEntry) {
		if (ecrfFieldStatusEntry != null) {
			this.out = (ECRFFieldStatusEntryOutVO) ecrfFieldStatusEntry.getVo();
			// if (!out.isDecrypted()) {
			// Messages.addLocalizedMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_LIST_STATUS_ENTRY,
			// Long.toString(out.getId()));
			// out = null;
			// }
			this.initIn();
			initSets();
		}
	}

	public void updateComment() {
		if (isUpdateCommentEnabled()) {
			in.setComment(Messages.getMessage(MessageCodes.ECRF_FIELD_STATUS_ENTRY_RESPONSE_COMMENT_PRESET, value.getReasonForChange()));
		}
	}
}
