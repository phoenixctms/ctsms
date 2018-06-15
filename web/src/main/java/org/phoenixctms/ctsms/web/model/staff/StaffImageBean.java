package org.phoenixctms.ctsms.web.model.staff;

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
import org.phoenixctms.ctsms.vo.StaffImageInVO;
import org.phoenixctms.ctsms.vo.StaffImageOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.model.shared.PhotoBeanBase;
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
public class StaffImageBean extends PhotoBeanBase {

	public static void copyStaffImageOutToIn(StaffImageInVO in, StaffImageOutVO out) {
		if (in != null && out != null) {
			MimeTypeVO contentTypeVO = out.getContentType();
			in.setDatas(out.getDatas());
			in.setFileName(out.getFileName());
			in.setFileTimestamp(out.getFileTimestamp());
			in.setId(out.getId());
			in.setMimeType(contentTypeVO == null ? null : contentTypeVO.getMimeType());
			in.setShowCv(out.getHasImage() ? out.getShowCv() : Settings.getBoolean(SettingCodes.STAFF_IMAGE_SHOW_CV_PRESET, Bundle.SETTINGS,
					DefaultSettings.STAFF_IMAGE_SHOW_CV_PRESET));
			in.setVersion(out.getVersion());
		}
	}

	public static void initStaffImageDefaultValues(StaffImageInVO in, Long staffId) {
		if (in != null) {
			in.setDatas(null);
			in.setFileName(null);
			in.setFileTimestamp(null);
			in.setId(staffId);
			in.setMimeType(null);
			in.setShowCv(Settings.getBoolean(SettingCodes.STAFF_IMAGE_SHOW_CV_PRESET, Bundle.SETTINGS, DefaultSettings.STAFF_IMAGE_SHOW_CV_PRESET));
			in.setVersion(null);
		}
	}

	private StaffImageInVO in;
	private StaffImageOutVO out;
	private Long staffId;
	private StaffOutVO staff;

	public StaffImageBean() {
		super();
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_IMAGE_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_IMAGE_COUNT,
				MessageCodes.STAFF_IMAGE_TAB_TITLE, MessageCodes.STAFF_IMAGE_TAB_TITLE_WITH_COUNT, isHasPhoto() ? 1l : 0l);
		if (operationSuccess) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		staffId = id;
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getStaffService().getStaffImage(WebUtil.getAuthentication(), id);
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
		return FileModule.STAFF_IMAGE;
	}

	@Override
	public String getFileName() {
		return in.getFileName();
	}

	public StaffImageInVO getIn() {
		return in;
	}

	@Override
	protected String getMimeType() {
		return in.getMimeType();
	}

	public StaffImageOutVO getOut() {
		return out;
	}

	@Override
	public Date getTimestamp() {
		return in.getFileTimestamp();
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.STAFF_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new StaffImageInVO();
		}
		if (out != null) {
			copyStaffImageOutToIn(in, out);
			staffId = in.getId();
		} else {
			initStaffImageDefaultValues(in, staffId);
		}
	}

	@Override
	protected void initSpecificSets() {
		staff = WebUtil.getStaff(in.getId(), null, null);
		if (staff != null) {
			if (!WebUtil.isStaffPerson(staff)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.STAFF_NOT_PERSON);
			}
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
		return WebUtil.isStaffPerson(staff);
	}

	@Override
	public boolean isInputVisible() {
		return isCreated() || WebUtil.isStaffPerson(staff);
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
			out = WebUtil.getServiceLocator().getStaffService().getStaffImage(WebUtil.getAuthentication(), id);
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
			return WebUtil.getServiceLocator().getToolsService().getStaffImageUploadSizeLimit();
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
			out = WebUtil.getServiceLocator().getStaffService().setStaffImage(WebUtil.getAuthentication(), in);
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
