package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingInVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.SelectEvent;

public abstract class InventoryBookingBeanBase extends ManagedBeanBase {

	public static void copyBookingOutToIn(InventoryBookingInVO in, InventoryBookingOutVO out) {
		if (in != null && out != null) {
			CourseOutVO courseVO = out.getCourse();
			InventoryOutVO inventoryVO = out.getInventory();
			StaffOutVO onBehalfOfVO = out.getOnBehalfOf();
			ProbandOutVO probandVO = out.getProband();
			TrialOutVO trialVO = out.getTrial();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setComment(out.getComment());
			in.setCourseId(courseVO == null ? null : courseVO.getId());
			in.setInventoryId(inventoryVO == null ? null : inventoryVO.getId());
			in.setOnBehalfOfId(onBehalfOfVO == null ? null : onBehalfOfVO.getId());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setCalendar(out.getCalendar());
		}
	}

	protected InventoryBookingInVO in;
	protected InventoryBookingOutVO out;
	protected HashMap<Long, CollidingInventoryStatusEntryEagerModel> collidingInventoryStatusEntryModelCache;
	protected ArrayList<SelectItem> filterCalendars;

	public InventoryBookingBeanBase() {
		super();
		collidingInventoryStatusEntryModelCache = new HashMap<Long, CollidingInventoryStatusEntryEagerModel>();
	}

	@Override
	public String addAction() {
		InventoryBookingInVO backup = new InventoryBookingInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getInventoryService().addInventoryBooking(WebUtil.getAuthentication(), in, null);
			initIn();
			initSets();
			addEvent();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	protected void addEvent() {
	}

	public void bookingStartAddOneDay(ActionEvent e) {
		if (this.in.getStart() != null) {
			this.in.setStart(DateUtil.addDayMinuteDelta(this.in.getStart(), 1, 0));
		}
	}

	public void bookingStartSubOneDay(ActionEvent e) {
		if (this.in.getStart() != null) {
			this.in.setStart(DateUtil.addDayMinuteDelta(this.in.getStart(), -1, 0));
		}
	}

	public void bookingStopAddOneDay(ActionEvent e) {
		if (this.in.getStop() != null) {
			this.in.setStop(DateUtil.addDayMinuteDelta(this.in.getStop(), 1, 0));
		}
	}

	public void bookingStopSubOneDay(ActionEvent e) {
		if (this.in.getStop() != null) {
			this.in.setStop(DateUtil.addDayMinuteDelta(this.in.getStop(), -1, 0));
		}
	}

	public List<String> completeCalendar(String query) {
		in.setCalendar(query);
		return getCompleteCalendarList(query);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().deleteInventoryBooking(WebUtil.getAuthentication(), id, null);
			initIn();
			initSets();
			deleteEvent();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	protected boolean deleteEvent() {
		return false;
	}

	public CollidingInventoryStatusEntryEagerModel getCollidingInventoryStatusEntryModel(InventoryBookingOutVO booking) {
		return CollidingInventoryStatusEntryEagerModel.getCachedCollidingInventoryStatusEntryModel(booking, true, collidingInventoryStatusEntryModelCache);
	}

	private List<String> getCompleteCalendarList(String query) {
		Collection<String> calendars = null;
		try {
			calendars = WebUtil.getServiceLocator().getInventoryService().getCalendars(WebUtil.getAuthentication(),
					null, null, null, null, null, query, null); // let permission argument override decide...
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (calendars != null) {
			try {
				return ((List<String>) calendars);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public String getCourseName() {
		return WebUtil.courseIdToName(in.getCourseId());
	}

	public ArrayList<SelectItem> getFilterCalendars() {
		return filterCalendars;
	}

	public InventoryBookingInVO getIn() {
		return in;
	}

	public String getInventoryName() {
		return WebUtil.inventoryIdToName(in.getInventoryId());
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public String getOnBehalfOfName() {
		return WebUtil.staffIdToName(in.getOnBehalfOfId());
	}

	public InventoryBookingOutVO getOut() {
		return out;
	}

	public String getProbandName() {
		return WebUtil.probandIdToName(in.getProbandId());
	}

	public IDVO getSelectedInventoryBooking() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public String getTrialName() {
		return WebUtil.trialIdToName(in.getTrialId());
	}

	public void handleCalendarSelect(SelectEvent event) {
		in.setCalendar((String) event.getObject());
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
			out = WebUtil.getServiceLocator().getInventoryService().getInventoryBooking(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
			loadEvent();
		}
		return ERROR_OUTCOME;
	}

	protected void loadEvent() {
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedInventoryBooking(IDVO inventoryBooking) {
		if (inventoryBooking != null) {
			this.out = (InventoryBookingOutVO) inventoryBooking.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().updateInventoryBooking(WebUtil.getAuthentication(), in, null);
			initIn();
			initSets();
			updateEvent();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	protected void updateEvent() {
	}
}
