package org.phoenixctms.ctsms.web.model.proband;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.vo.ProbandImageInVO;
import org.phoenixctms.ctsms.vo.ProbandImageOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.shared.PhotoBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class ProbandImageBean extends PhotoBeanBase {

	public static void copyProbandImageOutToIn(ProbandImageInVO in, ProbandImageOutVO out) {
		if (in != null && out != null) {
			MimeTypeVO contentTypeVO = out.getContentType();
			in.setDatas(out.getDatas());
			in.setFileName(out.getFileName());
			in.setFileTimestamp(out.getFileTimestamp());
			in.setId(out.getId());
			in.setMimeType(contentTypeVO == null ? null : contentTypeVO.getMimeType());
			in.setVersion(out.getVersion());
		}
	}

	public static void initProbandImageDefaultValues(ProbandImageInVO in, Long probandId) {
		if (in != null) {
			in.setDatas(null);
			in.setFileName(null);
			in.setFileTimestamp(null);
			in.setId(probandId);
			in.setMimeType(null);
			in.setVersion(null);
		}
	}

	private ProbandImageInVO in;
	private ProbandImageOutVO out;
	private Long probandId;
	private ProbandOutVO proband;

	public ProbandImageBean() {
		super();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_IMAGE_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_IMAGE_COUNT,
				MessageCodes.PROBAND_IMAGE_TAB_TITLE, MessageCodes.PROBAND_IMAGE_TAB_TITLE_WITH_COUNT, isHasPhoto() ? 1l : 0l);
		if (operationSuccess) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		probandId = id;
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getProbandService().getProbandImage(WebUtil.getAuthentication(), id);
				if (!out.isDecrypted()) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_IMAGE, Long.toString(out.getId()));
					out = null;
					return ERROR_OUTCOME;
				}
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

	@Override
	protected byte[] getData() {
		return in.getDatas();
	}

	@Override
	protected FileModule getFileModule() {
		return FileModule.PROBAND_IMAGE;
	}

	@Override
	public String getFileName() {
		return in.getFileName();
	}

	public ProbandImageInVO getIn() {
		return in;
	}

	@Override
	protected String getMimeType() {
		return in.getMimeType();
	}

	public ProbandImageOutVO getOut() {
		return out;
	}

	@Override
	public Date getTimestamp() {
		return in.getFileTimestamp();
	}

	@PostConstruct
	private void init() {
		// System.out.println("POSTCONSTRUCT: " + this.toString());
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandImageInVO();
		}
		if (out != null) {
			copyProbandImageOutToIn(in, out);
			probandId = in.getId();
		} else {
			initProbandImageDefaultValues(in, probandId);
		}
	}

	@Override
	protected void initSpecificSets() {
		proband = WebUtil.getProband(in.getId(), null, null, null);
		if (WebUtil.isProbandLocked(proband)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return out != null && out.getFileSize() != null && out.getFileSize() > 0;
	}

	@Override
	public boolean isEditable() {
		return !WebUtil.isProbandLocked(proband);
	}

	@Override
	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isProbandLocked(proband);
	}

	@Override
	public boolean isRemovable() {
		return false;
	}

	public void load(ActionEvent e) {
		load();
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getProbandService().getProbandImage(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_IMAGE, Long.toString(out.getId()));
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

	@Override
	protected Integer loadUploadSizeLimit() {
		try {
			return WebUtil.getServiceLocator().getToolsService().getProbandImageUploadSizeLimit();
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
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

	@Override
	protected void setData(byte[] data) {
		in.setDatas(data);
	}

	@Override
	protected void setFileName(String fileName) {
		in.setFileName(fileName);
	}

	@Override
	protected void setMimeType(String mimeType) {
		in.setMimeType(mimeType);
	}

	@Override
	protected void setTimestamp(Date now) {
		in.setFileTimestamp(now);
	}

	public void update(ActionEvent e) {
		update();
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getProbandService().setProbandImage(WebUtil.getAuthentication(), in);
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
