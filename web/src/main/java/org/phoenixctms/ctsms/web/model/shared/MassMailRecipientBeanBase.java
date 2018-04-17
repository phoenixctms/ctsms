package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.EmailAttachmentVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientInVO;
import org.phoenixctms.ctsms.vo.MassMailRecipientOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public abstract class MassMailRecipientBeanBase extends ManagedBeanBase {

	public static void copyMassMailRecipientOutToIn(MassMailRecipientInVO in, MassMailRecipientOutVO out) {
		if (in != null && out != null) {
			MassMailOutVO massMailVO = out.getMassMail();
			ProbandOutVO probandVO = out.getProband();
			in.setId(out.getId());
			in.setMassMailId(massMailVO == null ? null : massMailVO.getId());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	protected MassMailRecipientInVO in;
	protected MassMailRecipientOutVO out;
	protected MassMailRecipientLazyModel massMailRecipientModel;

	protected MassMailRecipientBeanBase() {
		super();
		massMailRecipientModel = new MassMailRecipientLazyModel();
	}

	@Override
	public String addAction() {
		MassMailRecipientInVO backup = new MassMailRecipientInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		// sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getMassMailService().addMassMailRecipient(WebUtil.getAuthentication(), in);
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

	// private void sanitizeInVals() {
	// // if (!in.getAccess()) {
	// // in.setSign(false);
	// // in.setResolve(false);
	// // in.setVerify(false);
	// // }
	// // x;
	// }
	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getMassMailService().deleteMassMailRecipient(WebUtil.getAuthentication(), id);
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

	public StreamedContent getEmailAttachmentStreamedContent(EmailAttachmentVO attachment) throws Exception {
		if (attachment != null) {
			return new DefaultStreamedContent(new ByteArrayInputStream(attachment.getDatas()), attachment.getContentType().getMimeType(), attachment.getFileName());
		}
		return null;
	}

	public MassMailRecipientInVO getIn() {
		return in;
	}

	public MassMailRecipientLazyModel getMassMailRecipientModel() {
		return massMailRecipientModel;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public MassMailRecipientOutVO getOut() {
		return out;
	}

	public IDVO getSelectedMassMailRecipient() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	protected abstract void initIn();

	protected abstract void initSets();

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getMassMailService().getMassMailRecipient(WebUtil.getAuthentication(), id);
			// massMailRecipientDao.getEmailMessageCache().remove(id);
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
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void resetMassMailRecipient() {
		Long massMailRecipientId = WebUtil.getLongParamValue(GetParamNames.MASS_MAIL_RECIPIENT_ID);
		Long version = WebUtil.getLongParamValue(GetParamNames.VERSION);
		if (massMailRecipientId != null && version != null) {
			try {
				WebUtil.getServiceLocator().getMassMailService().resetMassMailRecipient(WebUtil.getAuthentication(), massMailRecipientId, version);
				initIn();
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
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
	}

	public void setSelectedMassMailRecipient(IDVO massMailRecipient) {
		if (massMailRecipient != null) {
			this.out = (MassMailRecipientOutVO) massMailRecipient.getVo();
			this.initIn();
			initSets();
		}
	}
}
