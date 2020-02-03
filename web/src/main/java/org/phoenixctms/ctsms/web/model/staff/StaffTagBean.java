package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffTagVO;
import org.phoenixctms.ctsms.vo.StaffTagValueInVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class StaffTagBean extends ManagedBeanBase {

	public static void copyTagValueOutToIn(StaffTagValueInVO in, StaffTagValueOutVO out) {
		if (in != null && out != null) {
			StaffTagVO staffTagVO = out.getTag();
			StaffOutVO staffVO = out.getStaff();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setTagId(staffTagVO == null ? null : staffTagVO.getId());
			in.setValue(out.getValue());
		}
	}

	public static void initTagValueDefaultValues(StaffTagValueInVO in, Long staffId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setStaffId(staffId);
			in.setTagId(null);
			in.setValue(Messages.getString(MessageCodes.STAFF_TAG_VALUE_PRESET));
		}
	}

	private StaffTagValueInVO in;
	private StaffTagValueOutVO out;
	private Long staffId;
	private ArrayList<SelectItem> availableTags;
	private StaffTagValueLazyModel tagValueModel;

	public StaffTagBean() {
		super();
		tagValueModel = new StaffTagValueLazyModel();
	}

	@Override
	public String addAction() {
		StaffTagValueInVO backup = new StaffTagValueInVO(in);
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getStaffService().addStaffTagValue(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_TAG_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_TAG_VALUE_COUNT,
				MessageCodes.STAFF_TAGS_TAB_TITLE, MessageCodes.STAFF_TAGS_TAB_TITLE_WITH_COUNT, new Long(tagValueModel.getRowCount()));
		if (operationSuccess && in.getStaffId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("stafftag_list");
		out = null;
		this.staffId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getStaffService().deleteStaffTagValue(WebUtil.getAuthentication(), id);
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

	public ArrayList<SelectItem> getAvailableTags() {
		return availableTags;
	}

	public StaffTagValueInVO getIn() {
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

	public StaffTagValueOutVO getOut() {
		return out;
	}

	public IDVO getSelectedStaffTagValue() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public StaffTagValueLazyModel getTagValueModel() {
		return tagValueModel;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.STAFF_TAG_VALUE_TITLE, Long.toString(out.getId()), out.getTag().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_STAFF_TAG_VALUE);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.STAFF_TAG_VALUE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new StaffTagValueInVO();
		}
		if (out != null) {
			copyTagValueOutToIn(in, out);
			staffId = in.getStaffId();
		} else {
			initTagValueDefaultValues(in, staffId);
		}
	}

	private void initSets() {
		tagValueModel.setStaffId(in.getStaffId());
		tagValueModel.updateRowCount();
		Collection<StaffTagVO> tagVOs = null;
		try {
			tagVOs = WebUtil.getServiceLocator().getSelectionSetService().getAvailableStaffTags(WebUtil.getAuthentication(), null, null, this.in.getStaffId(), this.in.getTagId());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (tagVOs != null) {
			availableTags = new ArrayList<SelectItem>(tagVOs.size());
			Iterator<StaffTagVO> it = tagVOs.iterator();
			while (it.hasNext()) {
				StaffTagVO tagVO = it.next();
				availableTags.add(new SelectItem(tagVO.getId().toString(), tagVO.getName()));
			}
		} else {
			availableTags = new ArrayList<SelectItem>();
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getStaffId() == null ? false : true);
	}

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
			out = WebUtil.getServiceLocator().getStaffService().getStaffTagValue(WebUtil.getAuthentication(), id);
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedStaffTagValue(IDVO staffTagValue) {
		if (staffTagValue != null) {
			this.out = (StaffTagValueOutVO) staffTagValue.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getStaffService().updateStaffTagValue(WebUtil.getAuthentication(), in);
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
