package org.phoenixctms.ctsms.web.model.staff;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.phoenixctms.ctsms.vo.StaffStatusEntryInVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusTypeVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.proband.CollidingVisitScheduleItemEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingDutyRosterTurnEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingInventoryBookingEagerModel;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class StaffStatusBean extends ManagedBeanBase {

	public static void copyStatusEntryOutToIn(StaffStatusEntryInVO in, StaffStatusEntryOutVO out) {
		if (in != null && out != null) {
			StaffStatusTypeVO staffStatusTypeVO = out.getType();
			StaffOutVO staffVO = out.getStaff();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setComment(out.getComment());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setTypeId(staffStatusTypeVO == null ? null : staffStatusTypeVO.getId());
		}
	}

	public static void initStatusEntryDefaultValues(StaffStatusEntryInVO in, Long staffId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setStaffId(staffId);
			in.setComment(Messages.getString(MessageCodes.STAFF_STATUS_ENTRY_COMMENT_PRESET));
			in.setStart(new Timestamp(System.currentTimeMillis()));
			in.setStop(null);
			in.setTypeId(null);
		}
	}

	private StaffStatusEntryInVO in;
	private StaffStatusEntryOutVO out;
	private Long staffId;
	private StaffOutVO staff;
	private ArrayList<SelectItem> statusTypes;
	private StaffStatusEntryLazyModel statusEntryModel;
	private StaffStatusTypeVO statusType;
	private HashMap<Long, CollidingDutyRosterTurnEagerModel> collidingDutyRosterTurnModelCache;
	private HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache;
	private HashMap<Long, CollidingVisitScheduleItemEagerModel> collidingVisitScheduleItemModelCache;

	public StaffStatusBean() {
		super();
		collidingDutyRosterTurnModelCache = new HashMap<Long, CollidingDutyRosterTurnEagerModel>();
		collidingInventoryBookingModelCache = new HashMap<Long, CollidingInventoryBookingEagerModel>();
		collidingVisitScheduleItemModelCache = new HashMap<Long, CollidingVisitScheduleItemEagerModel>();
		statusEntryModel = new StaffStatusEntryLazyModel();
	}

	@Override
	public String addAction() {
		StaffStatusEntryInVO backup = new StaffStatusEntryInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getStaffService().addStaffStatusEntry(WebUtil.getAuthentication(), in);
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_STATUS_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_STATUS_ENTRY_COUNT,
				MessageCodes.STAFF_STATUS_TAB_TITLE, MessageCodes.STAFF_STATUS_TAB_TITLE_WITH_COUNT, new Long(statusEntryModel.getRowCount()));
		if (operationSuccess && in.getStaffId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
					MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("staffstatus_list");
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
			out = WebUtil.getServiceLocator().getStaffService().deleteStaffStatusEntry(WebUtil.getAuthentication(), id);
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

	public CollidingDutyRosterTurnEagerModel getCollidingDutyRosterTurnModel(StaffStatusEntryOutVO statusEntry) {
		return CollidingDutyRosterTurnEagerModel.getCachedCollidingDutyRosterTurnModel(statusEntry, true, collidingDutyRosterTurnModelCache);
	}

	public CollidingInventoryBookingEagerModel getCollidingInventoryBookingModel(StaffStatusEntryOutVO statusEntry) {
		return CollidingInventoryBookingEagerModel.getCachedCollidingInventoryBookingModel(statusEntry, true, collidingInventoryBookingModelCache);
	}

	public CollidingVisitScheduleItemEagerModel getCollidingVisitScheduleItemModel(StaffStatusEntryOutVO statusEntry) {
		return CollidingVisitScheduleItemEagerModel.getCachedCollidingVisitScheduleItemModel(statusEntry, collidingVisitScheduleItemModelCache);
	}

	public StaffStatusEntryInVO getIn() {
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

	public StaffStatusEntryOutVO getOut() {
		return out;
	}

	public IDVO getSelectedStaffStatusEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public boolean getShowStaffInActiveMessage() {
		return (statusType != null ? !statusType.isStaffActive() : false);
	}

	public StaffStatusEntryLazyModel getStatusEntryModel() {
		return statusEntryModel;
	}

	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.STAFF_STATUS_ENTRY_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_STAFF_STATUS_ENTRY);
		}
	}

	public void handleTypeChange() {
		loadSelectedType();
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.STAFF_STATUS_ENTRY_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new StaffStatusEntryInVO();
		}
		if (out != null) {
			copyStatusEntryOutToIn(in, out);
			staffId = in.getStaffId();
		} else {
			initStatusEntryDefaultValues(in, staffId);
		}
	}

	private void initSets() {
		collidingDutyRosterTurnModelCache.clear();
		collidingInventoryBookingModelCache.clear();
		collidingVisitScheduleItemModelCache.clear();
		statusEntryModel.setStaffId(in.getStaffId());
		statusEntryModel.updateRowCount();
		Collection<StaffStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getStaffStatusTypes(WebUtil.getAuthentication(), in.getTypeId());
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<StaffStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				StaffStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		staff = WebUtil.getStaff(in.getStaffId(), null, null, null);
		loadSelectedType();
		if (staff != null) {
			if (!WebUtil.isStaffPerson(staff)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.STAFF_NOT_PERSON);
			} else if (!WebUtil.isStaffEmployee(staff)) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.STAFF_NOT_EMPLOYEE);
			}
		}
	}

	@Override
	public boolean isCreateable() {
		return this.in.getStaffId() == null ? false : WebUtil.isStaffEmployee(staff);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && WebUtil.isStaffEmployee(staff);
	}

	public boolean isInputVisible() {
		return isCreated() || WebUtil.isStaffEmployee(staff);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && WebUtil.isStaffEmployee(staff);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getStaffService().getStaffStatusEntry(WebUtil.getAuthentication(), id);
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

	private void loadSelectedType() {
		statusType = WebUtil.getStaffStatusType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedStaffStatusEntry(IDVO staffStatusEntry) {
		if (staffStatusEntry != null) {
			this.out = (StaffStatusEntryOutVO) staffStatusEntry.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getStaffService().updateStaffStatusEntry(WebUtil.getAuthentication(), in);
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
