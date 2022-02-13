package org.phoenixctms.ctsms.web.model.massmail;

import java.sql.Timestamp;
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
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.MassMailInVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailStatusTypeVO;
import org.phoenixctms.ctsms.vo.MassMailTypeVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserInheritedVO;
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
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class MassMailBean extends ManagedBeanBase {

	public static void copyMassMailOutToIn(MassMailInVO in, MassMailOutVO out) {
		if (in != null && out != null) {
			DepartmentVO departmentVO = out.getDepartment();
			MassMailStatusTypeVO statusVO = out.getStatus();
			MassMailTypeVO typeVO = out.getType();
			ProbandListStatusTypeVO probandListStatus = out.getProbandListStatus();
			TrialOutVO trial = out.getTrial();
			in.setId(out.getId());
			in.setName(out.getName());
			in.setDescription(out.getDescription());
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setStart(out.getStart());
			in.setStatusId(statusVO == null ? null : statusVO.getId());
			in.setLockAfterSending(out.getLockAfterSending());
			in.setTypeId(typeVO == null ? null : typeVO.getId());
			in.setProbandListStatusId(probandListStatus == null ? null : probandListStatus.getId());
			in.setProbandListStatusResend(out.getProbandListStatusResend());
			in.setTrialId(trial == null ? null : trial.getId());
			in.setFromAddress(out.getFromAddress());
			in.setFromName(out.getFromName());
			in.setLocale(out.getLocale());
			in.setMaleSalutation(out.getMaleSalutation());
			in.setFemaleSalutation(out.getFemaleSalutation());
			in.setSubjectFormat(out.getSubjectFormat());
			in.setTextTemplate(out.getTextTemplate());
			in.setReplyToAddress(out.getReplyToAddress());
			in.setReplyToName(out.getReplyToName());
			in.setProbandTo(out.getProbandTo());
			in.setPhysicianTo(out.getPhysicianTo());
			in.setTrialTeamTo(out.getTrialTeamTo());
			in.setOtherTo(out.getOtherTo());
			in.setCc(out.getCc());
			in.setBcc(out.getBcc());
			in.setUseBeacon(out.getUseBeacon());
			in.setAttachMassMailFiles(out.getAttachMassMailFiles());
			in.setMassMailFilesLogicalPath(out.getMassMailFilesLogicalPath());
			in.setAttachTrialFiles(out.getAttachTrialFiles());
			in.setTrialFilesLogicalPath(out.getTrialFilesLogicalPath());
			in.setAttachProbandFiles(out.getAttachProbandFiles());
			in.setProbandFilesLogicalPath(out.getProbandFilesLogicalPath());
			in.setAttachInquiries(out.getAttachInquiries());
			in.setAttachProbandListEntryTags(out.getAttachProbandListEntryTags());
			in.setAttachEcrfs(out.getAttachEcrfs());
			in.setAttachProbandLetter(out.getAttachProbandLetter());
			in.setAttachReimbursementsPdf(out.getAttachReimbursementsPdf());
			in.setVersion(out.getVersion());
		}
	}

	public static void initMassMailDefaultValues(MassMailInVO in, UserInheritedVO user) {
		if (in != null) {
			in.setId(null);
			in.setName(Messages.getString(MessageCodes.MASS_MAIL_NAME_PRESET));
			in.setDescription(Messages.getString(MessageCodes.MASS_MAIL_DESCRIPTION_PRESET));
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setStart(new Timestamp(System.currentTimeMillis()));
			in.setStatusId(null);
			in.setLockAfterSending(Settings.getBoolean(SettingCodes.MASS_MAIL_LOCK_AFTER_SENDING_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_LOCK_AFTER_SENDING_PRESET));
			in.setTypeId(null);
			in.setProbandListStatusId(null);
			in.setProbandListStatusResend(
					Settings.getBoolean(SettingCodes.MASS_MAIL_PROBAND_LIST_STATUS_RESEND_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_PROBAND_LIST_STATUS_RESEND_PRESET));
			in.setTrialId(null);
			in.setFromAddress(Settings.getString(SettingCodes.MASS_MAIL_FROM_ADDRESS_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_FROM_ADDRESS_PRESET));
			in.setFromName(Settings.getString(SettingCodes.MASS_MAIL_FROM_NAME_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_FROM_NAME_PRESET));
			in.setLocale(Settings.getString(SettingCodes.MASS_MAIL_LOCALE_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_LOCALE_PRESET));
			in.setMaleSalutation(Messages.getString(MessageCodes.MASS_MAIL_MALE_SALUTATION_PRESET));
			in.setFemaleSalutation(Messages.getString(MessageCodes.MASS_MAIL_FEMALE_SALUTATION_PRESET));
			in.setSubjectFormat(Messages.getString(MessageCodes.MASS_MAIL_SUBJECT_FORMAT_PRESET));
			in.setTextTemplate(Messages.getString(MessageCodes.MASS_MAIL_TEXT_TEMPLATE_PRESET));
			in.setReplyToAddress(Settings.getString(SettingCodes.MASS_MAIL_REPLY_TO_ADDRESS_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_REPLY_TO_ADDRESS_PRESET));
			in.setReplyToName(Settings.getString(SettingCodes.MASS_MAIL_REPLY_TO_NAME_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_REPLY_TO_NAME_PRESET));
			in.setProbandTo(Settings.getBoolean(SettingCodes.MASS_MAIL_PROBAND_TO_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_PROBAND_TO_PRESET));
			in.setPhysicianTo(Settings.getBoolean(SettingCodes.MASS_MAIL_PHYSICIAN_TO_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_PHYSICIAN_TO_PRESET));
			in.setTrialTeamTo(Settings.getBoolean(SettingCodes.MASS_MAIL_TRIAL_TEAM_TO_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_TRIAL_TEAM_TO_PRESET));
			in.setOtherTo(Settings.getString(SettingCodes.MASS_MAIL_OTHER_TO_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_OTHER_TO_PRESET));
			in.setCc(Settings.getString(SettingCodes.MASS_MAIL_CC_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_CC_PRESET));
			in.setBcc(Settings.getString(SettingCodes.MASS_MAIL_BCC_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_BCC_PRESET));
			in.setUseBeacon(Settings.getBoolean(SettingCodes.MASS_MAIL_USE_BEACON_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_USE_BEACON_PRESET));
			in.setAttachMassMailFiles(
					Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_MASS_MAIL_FILES_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_ATTACH_MASS_MAIL_FILES_PRESET));
			in.setMassMailFilesLogicalPath(
					Settings.getString(SettingCodes.MASS_MAIL_MASS_MAIL_FILES_LOGICAL_PATH_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_MASS_MAIL_FILES_LOGICAL_PATH_PRESET));
			in.setAttachTrialFiles(Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_TRIAL_FILES_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_ATTACH_TRIAL_FILES_PRESET));
			in.setTrialFilesLogicalPath(
					Settings.getString(SettingCodes.MASS_MAIL_TRIAL_FILES_LOGICAL_PATH_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_TRIAL_FILES_LOGICAL_PATH_PRESET));
			in.setAttachProbandFiles(
					Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_PROBAND_FILES_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_ATTACH_PROBAND_FILES_PRESET));
			in.setProbandFilesLogicalPath(
					Settings.getString(SettingCodes.MASS_MAIL_PROBAND_LOGICAL_PATH_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_PROBAND_LOGICAL_PATH_PRESET));
			in.setAttachInquiries(Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_INQUIRIES_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_ATTACH_INQUIRIES_PRESET));
			in.setAttachProbandListEntryTags(Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_PROBAND_LIST_ENTRY_TAGS_PRESET, Bundle.SETTINGS,
					DefaultSettings.MASS_MAIL_ATTACH_PROBAND_LIST_ENTRY_TAGS_PRESET));
			in.setAttachEcrfs(Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_ECRFS_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_ATTACH_ECRFS_PRESET));
			in.setAttachProbandLetter(
					Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_PROBAND_LETTER_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_ATTACH_PROBAND_LETTER_PRESET));
			in.setAttachReimbursementsPdf(
					Settings.getBoolean(SettingCodes.MASS_MAIL_ATTACH_REIMBURSEMENTS_PRESET, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_ATTACH_REIMBURSEMENTS_PRESET));
			in.setVersion(null);
		}
	}

	private MassMailInVO in;
	private MassMailOutVO out;
	private ArrayList<SelectItem> statusTypes;
	private ArrayList<SelectItem> departments;
	private ArrayList<SelectItem> massMailTypes;
	private MassMailStatusTypeVO massMailStatusType;
	private ArrayList<SelectItem> probandListStatusTypes;
	private ArrayList<SelectItem> locales;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private Long previewProbandId;
	private String previewSubject;
	private String previewText;
	private String deferredDeleteReason;

	public MassMailBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
	}

	@Override
	public String addAction() {
		MassMailInVO backup = new MassMailInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getMassMailService().addMassMail(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage("inputMessages", MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MASS_MAIL_RECIPIENT_TAB_TITLE_BASE64, JSValues.AJAX_MASS_MAIL_RECIPIENT_COUNT,
					MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE, MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_MASS_MAIL_RECIPIENT_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MASS_MAIL_FILE_TAB_TITLE_BASE64, JSValues.AJAX_MASS_MAIL_FILE_COUNT,
					MessageCodes.MASS_MAIL_FILES_TAB_TITLE, MessageCodes.MASS_MAIL_FILES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_MASS_MAIL_FILE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT,
					MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE, MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT.toString()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		previewProbandId = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getMassMailService().getMassMail(WebUtil.getAuthentication(), id);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<String> completeMassMailFilesLogicalPath(String query) {
		this.in.setMassMailFilesLogicalPath(query);
		return WebUtil.completeLogicalPath(FileModule.MASS_MAIL_DOCUMENT, this.in.getId(), query);
	}

	public List<String> completeProbandFilesLogicalPath(String query) {
		this.in.setProbandFilesLogicalPath(query);
		return WebUtil.completeLogicalPath(FileModule.PROBAND_DOCUMENT, null, query);
	}

	public List<String> completeTrialFilesLogicalPath(String query) {
		this.in.setTrialFilesLogicalPath(query);
		return WebUtil.completeLogicalPath(FileModule.TRIAL_DOCUMENT, this.in.getTrialId(), query);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getMassMailService().deleteMassMail(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.MASS_MAIL_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_DEFERRED_DELETE), false, deferredDeleteReason);
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage("inputMessages", MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public MassMailInVO getIn() {
		return in;
	}

	public ArrayList<SelectItem> getLocales() {
		return locales;
	}

	public String getMassMailAttachmentFileSystemStats() {
		if (in.getId() != null && in.getAttachMassMailFiles()) {
			long totalFileCount = 0l;
			long totalSize = 0l;
			try {
				totalFileCount = WebUtil.getServiceLocator().getFileService().getFileCount(WebUtil.getAuthentication(), FileModule.MASS_MAIL_DOCUMENT,
						in.getId(), in.getMassMailFilesLogicalPath(), true, null, null);
				totalSize = WebUtil.getServiceLocator().getFileService().getFolderSize(WebUtil.getAuthentication(), FileModule.MASS_MAIL_DOCUMENT,
						in.getId(), in.getMassMailFilesLogicalPath(), true, null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			return Messages.getMessage(MessageCodes.LOGICAL_FILE_SYSTEM_STATS_LABEL, CommonUtil.humanReadableByteCount(totalSize, WebUtil.getDecimalSeparator()), totalFileCount);
		}
		return null;
	}

	public MassMailStatusTypeVO getMassMailStatusType() {
		return massMailStatusType;
	}

	public ArrayList<SelectItem> getMassMailTypes() {
		return massMailTypes;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public MassMailOutVO getOut() {
		return out;
	}

	public Long getPreviewProbandId() {
		return previewProbandId;
	}

	public String getPreviewProbandName() {
		return WebUtil.probandIdToName(previewProbandId);
	}

	public String getPreviewSubject() {
		return previewSubject;
	}

	public String getPreviewText() {
		return previewText;
	}

	public String getProbandAttachmentFileSystemStats() {
		if (previewProbandId != null && in.getAttachProbandFiles()) {
			long totalFileCount = 0l;
			long totalSize = 0l;
			try {
				totalFileCount = WebUtil.getServiceLocator().getFileService().getFileCount(WebUtil.getAuthentication(), FileModule.PROBAND_DOCUMENT,
						previewProbandId, in.getProbandFilesLogicalPath(), true, null, null);
				totalSize = WebUtil.getServiceLocator().getFileService().getFolderSize(WebUtil.getAuthentication(), FileModule.PROBAND_DOCUMENT,
						previewProbandId, in.getProbandFilesLogicalPath(), true, null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			return Messages.getMessage(MessageCodes.LOGICAL_FILE_SYSTEM_STATS_LABEL, CommonUtil.humanReadableByteCount(totalSize, WebUtil.getDecimalSeparator()), totalFileCount);
		}
		return null;
	}

	public ArrayList<SelectItem> getProbandListStatusTypes() {
		return probandListStatusTypes;
	}

	public boolean getShowTerminalStateMessage() {
		if (in.getStatusId() != null) {
			Collection<MassMailStatusTypeVO> statusTypeVOs = null;
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getMassMailStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (statusTypeVOs != null) {
				if (statusTypeVOs.size() > 1 || (statusTypeVOs.size() == 1 && !statusTypeVOs.iterator().next().getId().equals(in.getStatusId()))) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.MASS_MAIL_ID) == null);
	}

	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : MessageCodes.MASS_MAIL_TITLE, Long.toString(out.getId()),
					CommonUtil.massMailOutVOToString(out));
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_MASS_MAIL : MessageCodes.ERROR_LOADING_MASS_MAIL);
		}
	}

	public String getTrialAttachmentFileSystemStats() {
		if (in.getTrialId() != null && in.getAttachTrialFiles()) {
			long totalFileCount = 0l;
			long totalSize = 0l;
			try {
				totalFileCount = WebUtil.getServiceLocator().getFileService().getFileCount(WebUtil.getAuthentication(), FileModule.TRIAL_DOCUMENT,
						in.getTrialId(), in.getTrialFilesLogicalPath(), true, null, null);
				totalSize = WebUtil.getServiceLocator().getFileService().getFolderSize(WebUtil.getAuthentication(), FileModule.TRIAL_DOCUMENT,
						in.getTrialId(), in.getTrialFilesLogicalPath(), true, null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			return Messages.getMessage(MessageCodes.LOGICAL_FILE_SYSTEM_STATS_LABEL, CommonUtil.humanReadableByteCount(totalSize, WebUtil.getDecimalSeparator()), totalFileCount);
		}
		return null;
	}

	public String getTrialName() {
		return WebUtil.trialIdToName(in.getTrialId());
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.MASS_MAIL_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.MASS_MAIL_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.MASS_MAIL_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long massMailId = WebUtil.getLongParamValue(GetParamNames.MASS_MAIL_ID);
				if (massMailId != null) {
					return String.format(JSValues.MASS_MAIL_ENTITY_WINDOW_NAME.toString(), massMailId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.MASS_MAIL_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleMassMailFilesLogicalPathSelect(SelectEvent event) {
		in.setMassMailFilesLogicalPath((String) event.getObject());
	}

	public void handleProbandFilesLogicalPathSelect(SelectEvent event) {
		in.setProbandFilesLogicalPath((String) event.getObject());
	}

	public void handleStatusTypeChange() {
		loadMassMailStatusType();
	}

	public void handleTrialFilesLogicalPathSelect(SelectEvent event) {
		in.setTrialFilesLogicalPath((String) event.getObject());
	}

	public void handleUpdatePreview() {
		updatePreview(true);
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.MASS_MAIL_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new MassMailInVO();
		}
		if (out != null) {
			copyMassMailOutToIn(in, out);
		} else {
			initMassMailDefaultValues(in, WebUtil.getUser());
		}
	}

	private void initSets() {
		tabCountMap.clear();
		tabTitleMap.clear();
		Long count = null;
		count = (out == null ? null : WebUtil.getMassMailRecipientCount(in.getId(), null, false));
		tabCountMap.put(JSValues.AJAX_MASS_MAIL_RECIPIENT_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_MASS_MAIL_RECIPIENT_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE, MessageCodes.MASS_MAIL_RECIPIENTS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTotalFileCount(FileModule.MASS_MAIL_DOCUMENT, in.getId()));
		tabCountMap.put(JSValues.AJAX_MASS_MAIL_FILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_MASS_MAIL_FILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.MASS_MAIL_FILES_TAB_TITLE, MessageCodes.MASS_MAIL_FILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.MASS_MAIL_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE, MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		massMailTypes = WebUtil.getVisibleMassMailTypes(in.getTypeId());
		Collection<MassMailStatusTypeVO> statusTypeVOs = null;
		if (out != null) {
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getMassMailStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		} else {
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getInitialMassMailStatusTypes(WebUtil.getAuthentication());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<MassMailStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				MassMailStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		loadMassMailStatusType();
		probandListStatusTypes = WebUtil.getAllProbandListStatusTypes(null);
		if (this.locales == null) {
			this.locales = WebUtil.getLocales();
		}
		updatePreview(false);
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) {
			Messages.addLocalizedMessageClientId("inputMessages", FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
	}

	@Override
	public boolean isCreateable() {
		return WebUtil.getModuleEnabled(DBModule.MASS_MAIL_DB);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.MASS_MAIL_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return WebUtil.getModuleEnabled(DBModule.MASS_MAIL_DB) && super.isEditable();
	}

	@Override
	public boolean isRemovable() {
		return WebUtil.getModuleEnabled(DBModule.MASS_MAIL_DB) && super.isRemovable();
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
			out = WebUtil.getServiceLocator().getMassMailService().getMassMail(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadMassMailStatusType() {
		massMailStatusType = WebUtil.getMassMailStatusType(in.getStatusId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (in.getAttachMassMailFiles()) {
			in.setMassMailFilesLogicalPath(CommonUtil.fixLogicalPathFolderName(in.getMassMailFilesLogicalPath()));
		} else {
			in.setMassMailFilesLogicalPath(null);
		}
		if (in.getAttachTrialFiles()) {
			in.setTrialFilesLogicalPath(CommonUtil.fixLogicalPathFolderName(in.getTrialFilesLogicalPath()));
		} else {
			in.setTrialFilesLogicalPath(null);
		}
		if (in.getAttachProbandFiles()) {
			in.setProbandFilesLogicalPath(CommonUtil.fixLogicalPathFolderName(in.getProbandFilesLogicalPath()));
		} else {
			in.setProbandFilesLogicalPath(null);
		}
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	public void setPreviewProbandId(Long previewProbandId) {
		this.previewProbandId = previewProbandId;
	}

	@Override
	public String updateAction() {
		MassMailInVO backup = new MassMailInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getMassMailService().updateMassMail(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage("inputMessages", MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("inputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	private void updatePreview(boolean addMessages) {
		previewSubject = null;
		try {
			previewSubject = WebUtil.getServiceLocator().getMassMailService().getSubject(WebUtil.getAuthentication(), in, previewProbandId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			if (addMessages) {
				Messages.addMessageClientId("previewMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
		} catch (AuthenticationException e) {
			if (addMessages) {
				Messages.addMessageClientId("previewMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
		previewText = null;
		try {
			previewText = WebUtil.getServiceLocator().getMassMailService().getText(WebUtil.getAuthentication(), in, previewProbandId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			if (addMessages) {
				Messages.addMessageClientId("previewMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
		} catch (AuthenticationException e) {
			if (addMessages) {
				Messages.addMessageClientId("previewMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
	}
}
