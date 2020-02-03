package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.JobAddVO;
import org.phoenixctms.ctsms.vo.JobFileVO;
import org.phoenixctms.ctsms.vo.JobOutVO;
import org.phoenixctms.ctsms.vo.JobTypeVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class JobBean extends ManagedBeanBase {

	public static void initJobDefaultValues(JobAddVO in, Long entityId, JobModule module, Long typeId) {
		if (in != null) {
			in.setDatas(null);
			in.setFileName(null);
			in.setMimeType(null);
			in.setEmailRecipients(null);
			in.setTypeId(typeId);
			in.setTrialId(JobModule.TRIAL_JOB.equals(module) ? entityId : null);
			in.setProbandId(JobModule.PROBAND_JOB.equals(module) ? entityId : null);
			in.setInputFieldId(JobModule.INPUT_FIELD_JOB.equals(module) ? entityId : null);
			in.setCriteriaId(isCriteriaJob(module) ? entityId : null);
		}
	}

	private JobAddVO in;
	private JobOutVO out;
	private Long entityId;
	private JobModule module;
	private TrialOutVO trial;
	private ProbandOutVO proband;
	private InputFieldOutVO inputField;
	private CriteriaOutVO criteria;
	private ArrayList<SelectItem> types;
	private ArrayList<SelectItem> filterTypes;
	private JobTypeVO type;
	private JobLazyModel jobModel;
	private Integer uploadSizeLimit;
	private String allowTypes;
	private JobFileVO jobFile;
	private boolean decrypted;

	public JobBean() {
		super();
		jobModel = new JobLazyModel();
		decrypted = true;
	}

	@Override
	public String addAction() {
		JobAddVO backup = new JobAddVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getJobService().addJob(WebUtil.getAuthentication(), in);
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
			RequestContext requestContext;
			switch (module) {
				case TRIAL_JOB:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_JOB_TAB_TITLE_BASE64,
							JSValues.AJAX_TRIAL_JOB_COUNT, MessageCodes.TRIAL_JOBS_TAB_TITLE, MessageCodes.TRIAL_JOBS_TAB_TITLE_WITH_COUNT, new Long(
									jobModel.getRowCount()));
					if (operationSuccess && in.getTrialId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64,
								JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT, MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
					}
					break;
				case PROBAND_JOB:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_JOB_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOB_COUNT,
							MessageCodes.PROBAND_JOBS_TAB_TITLE, MessageCodes.PROBAND_JOBS_TAB_TITLE_WITH_COUNT, new Long(jobModel.getRowCount()));
					if (operationSuccess && in.getProbandId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
								MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
					}
					break;
				case INPUT_FIELD_JOB:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INPUT_FIELD_JOB_TAB_TITLE_BASE64, JSValues.AJAX_INPUT_FIELD_JOB_COUNT,
							MessageCodes.INPUT_FIELD_JOBS_TAB_TITLE, MessageCodes.INPUT_FIELD_JOBS_TAB_TITLE_WITH_COUNT, new Long(jobModel.getRowCount()));
					if (operationSuccess && in.getInputFieldId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INPUT_FIELD_JOURNAL_TAB_TITLE_BASE64,
								JSValues.AJAX_INPUT_FIELD_JOURNAL_ENTRY_COUNT,
								MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE, MessageCodes.INPUT_FIELD_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.INPUT_FIELD_JOURNAL, in.getInputFieldId()));
					}
					break;
				case INVENTORY_CRITERIA_JOB:
				case STAFF_CRITERIA_JOB:
				case COURSE_CRITERIA_JOB:
				case TRIAL_CRITERIA_JOB:
				case INPUT_FIELD_CRITERIA_JOB:
				case PROBAND_CRITERIA_JOB:
				case USER_CRITERIA_JOB:
				case MASS_MAIL_CRITERIA_JOB:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_CRITERIA_JOB_TAB_TITLE_BASE64, JSValues.AJAX_CRITERIA_JOB_COUNT,
							MessageCodes.CRITERIA_JOBS_TAB_TITLE, MessageCodes.CRITERIA_JOBS_TAB_TITLE_WITH_COUNT, new Long(jobModel.getRowCount()));
					if (operationSuccess && in.getCriteriaId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64,
								JSValues.AJAX_CRITERIA_JOURNAL_ENTRY_COUNT,
								MessageCodes.CRITERIA_JOURNAL_TAB_TITLE, MessageCodes.CRITERIA_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.CRITERIA_JOURNAL, in.getCriteriaId()));
					}
					break;
				default:
					break;
			}
		}
	}

	public String changeAction(String param, JobModule module) {
		DataTable.clearFilters("job_list");
		out = null;
		this.entityId = WebUtil.stringToLong(param);
		this.module = module;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeTrial(String param) {
		actionPostProcess(changeTrialAction(param));
	}

	public String changeTrialAction(String param) {
		return changeAction(param, JobModule.TRIAL_JOB);
	}

	public void changeProband(String param) {
		actionPostProcess(changeProbandAction(param));
	}

	public String changeProbandAction(String param) {
		return changeAction(param, JobModule.PROBAND_JOB);
	}

	public void changeInputField(String param) {
		actionPostProcess(changeInputFieldAction(param));
	}

	public String changeInputFieldAction(String param) {
		return changeAction(param, JobModule.INPUT_FIELD_JOB);
	}

	public void changeCriteria(String param, JobModule module) {
		actionPostProcess(changeAction(param, module));
	}

	@Override
	public String deleteAction() {
		return deleteAction(out.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getJobService().deleteJob(WebUtil.getAuthentication(), id);
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

	public JobLazyModel getJobModel() {
		return jobModel;
	}

	public JobAddVO getIn() {
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

	public JobOutVO getOut() {
		return out;
	}

	public IDVO getSelectedJob() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.JOB_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_JOB);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.JOB_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new JobAddVO();
		}
		initJobDefaultValues(in, entityId, module, out != null ? out.getType().getId() : null);
		if (out != null) {
			in.setEmailRecipients(out.getEmailRecipients());
		}
	}

	public void clearFile() {
		in.setFileName(null);
		in.setMimeType(null);
		in.setDatas(null);
		jobFile = null;
	}

	public JobFileVO getJobFile() {
		return jobFile;
	}

	public String getFileDownloadLinkLabel() {
		if (isHasFile()) {
			return in.getFileName();
		} else {
			return Messages.getString(MessageCodes.NO_FILE_LINK_LABEL);
		}
	}

	public StreamedContent getFileStreamedContent() throws Exception {
		if (isHasFile()) {
			return new DefaultStreamedContent(new ByteArrayInputStream(in.getDatas()), in.getMimeType(), in.getFileName());
		}
		return null;
	}

	public Integer getUploadSizeLimit() {
		return uploadSizeLimit;
	}

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		in.setFileName(uploadedFile.getFileName());
		in.setMimeType(uploadedFile.getContentType());
		in.setDatas(uploadedFile.getContents());
		addOperationSuccessMessage(MessageCodes.UPLOAD_OPERATION_SUCCESSFUL);
	}

	public boolean isDecrypted() {
		return decrypted;
	}

	private void initSets() {
		decrypted = true;
		jobFile = null;
		if (out != null) {
			try {
				jobFile = WebUtil.getServiceLocator().getJobService().getJobFile(WebUtil.getAuthentication(), out.getId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (jobFile != null) {
				MimeTypeVO contentTypeVO = jobFile.getContentType();
				in.setFileName(jobFile.getFileName());
				in.setMimeType(contentTypeVO == null ? null : contentTypeVO.getMimeType());
				in.setDatas(jobFile.getDatas());
				decrypted = jobFile.isDecrypted();
			}
		}
		allowTypes = WebUtil.getAllowedFileExtensionsPattern(FileModule.JOB_FILE, false);
		uploadSizeLimit = null;
		try {
			uploadSizeLimit = WebUtil.getServiceLocator().getToolsService().getJobFileUploadSizeLimit();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		trial = (JobModule.TRIAL_JOB.equals(module) ? WebUtil.getTrial(entityId) : null);
		proband = (JobModule.PROBAND_JOB.equals(module) ? WebUtil.getProband(entityId, null, null, null) : null);
		inputField = (JobModule.INPUT_FIELD_JOB.equals(module) ? WebUtil.getInputField(entityId) : null);
		criteria = (isCriteriaJob(module) ? WebUtil.getCriteria(entityId) : null);
		jobModel.setEntityId(entityId);
		jobModel.setModule(module);
		jobModel.updateRowCount();
		types = WebUtil.getAvailableJobTypes(module, in.getTypeId(), JobModule.TRIAL_JOB.equals(module) ? entityId : null);
		filterTypes = new ArrayList<SelectItem>(types);
		filterTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		loadSelectedType();
	}

	@Override
	public boolean isCreateable() {
		return module != null;
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public String loadAction() {
		return loadAction(out.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getJobService().getJob(WebUtil.getAuthentication(), id);
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

	public ArrayList<SelectItem> getTypes() {
		return types;
	}

	public ArrayList<SelectItem> getFilterTypes() {
		return filterTypes;
	}

	private void sanitizeInVals() {
		if (!isInputFile() || !decrypted) {
			clearFile();
		}
		if (type != null && !type.isEmailRecipients()) {
			in.setEmailRecipients(null);
		}
	}

	public void handleTypeChange() {
		loadSelectedType();
	}

	private void loadSelectedType() {
		type = null;
		if (in.getTypeId() != null) {
			try {
				type = WebUtil.getServiceLocator().getSelectionSetService().getJobType(WebUtil.getAuthentication(), in.getTypeId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
	}

	public void setSelectedJob(IDVO job) {
		if (job != null) {
			this.out = (JobOutVO) job.getVo();
			this.initIn();
			initSets();
		}
	}

	public boolean isHasFile() {
		byte[] data = in.getDatas();
		if (data != null && data.length > 0) {
			return true;
		}
		return false;
	}

	public boolean isInputFile() {
		return type != null ? type.getInputFile() : false;
	}

	public boolean isEmailRecipients() {
		return type != null ? type.getEmailRecipients() : false;
	}

	public JobTypeVO getType() {
		return type;
	}

	private static boolean isCriteriaJob(JobModule module) {
		if (JobModule.INVENTORY_CRITERIA_JOB.equals(module)
				|| JobModule.STAFF_CRITERIA_JOB.equals(module)
				|| JobModule.COURSE_CRITERIA_JOB.equals(module)
				|| JobModule.TRIAL_CRITERIA_JOB.equals(module)
				|| JobModule.INPUT_FIELD_CRITERIA_JOB.equals(module)
				|| JobModule.PROBAND_CRITERIA_JOB.equals(module)
				|| JobModule.USER_CRITERIA_JOB.equals(module)
				|| JobModule.MASS_MAIL_CRITERIA_JOB.equals(module)) {
			return true;
		}
		return false;
	}

	public boolean isCriteriaJob() {
		return isCriteriaJob(module);
	}

	public String getAllowTypes() {
		return allowTypes;
	}
}
