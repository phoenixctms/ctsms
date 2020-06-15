package org.phoenixctms.ctsms.web.model.shared;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnInVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.SelectEvent;

public abstract class DutyRosterTurnBeanBase extends ManagedBeanBase {

	protected static String getTitle(DutyRosterTurnOutVO dutyRosterTurn) {
		if (dutyRosterTurn != null) {
			String title = dutyRosterTurn.getTitle();
			String comment = dutyRosterTurn.getComment();
			VisitScheduleItemOutVO visitScheduleItem = dutyRosterTurn.getVisitScheduleItem();
			TrialOutVO trial = dutyRosterTurn.getTrial();
			StaffOutVO staff = dutyRosterTurn.getStaff();
			if (visitScheduleItem != null) {
				return visitScheduleItem.getName();
			} else if (trial != null) {
				return CommonUtil.trialOutVOToString(trial);
			} else if (title != null && title.length() > 0) {
				return title;
			} else if (staff != null) {
				return CommonUtil.staffOutVOToString(staff);
			} else if (!CommonUtil.isEmptyString(comment)) {
				return CommonUtil.clipString(comment, Settings.getInt(SettingCodes.COMMENT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.COMMENT_CLIP_MAX_LENGTH),
						CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING);
			}
		}
		return Messages.getString(MessageCodes.EMPTY_DUTY_ROSTER_TURN_LABEL);
	}

	protected DutyRosterTurnInVO in;
	protected DutyRosterTurnOutVO out;
	protected ArrayList<SelectItem> filterCalendars;
	protected ArrayList<SelectItem> filterTitles;
	protected HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	protected HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache;
	private static final String VISIT_SCHEDULE_ITEM_NAME = "{0}";
	private static final String TRIAL_VISIT_SCHEDULE_ITEM_NAME = "{0}: {1}";

	public static void copyDutyRosterTurnOutToIn(DutyRosterTurnInVO in, DutyRosterTurnOutVO out) {
		if (in != null && out != null) {
			StaffOutVO staffVO = out.getStaff();
			TrialOutVO trialVO = out.getTrial();
			VisitScheduleItemOutVO visitScheduleItemVO = out.getVisitScheduleItem();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setComment(out.getComment());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setSelfAllocatable(out.getSelfAllocatable());
			in.setTitle(out.getTitle());
			in.setCalendar(out.getCalendar());
			in.setVisitScheduleItemId(visitScheduleItemVO == null ? null : visitScheduleItemVO.getId());
		}
	}

	public DutyRosterTurnBeanBase() {
		super();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		collidingInventoryBookingModelCache = new HashMap<Long, CollidingInventoryBookingEagerModel>();
	}

	@Override
	public String addAction() {
		DutyRosterTurnInVO backup = new DutyRosterTurnInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeTrial();
		try {
			out = WebUtil.getServiceLocator().getStaffService().addDutyRosterTurn(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addEvent();
			addOperationSuccessMessage("dutyRosterScheduleInputMessages", MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	protected void addEvent() {
	}

	public List<String> completeCalendar(String query) {
		in.setCalendar(query);
		return getCompleteCalendarList(query);
	}

	public List<String> completeTitle(String query) {
		in.setTitle(query);
		return getCompleteTitleList(query);
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getStaffService().deleteDutyRosterTurn(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			deleteEvent();
			out = null;
			addOperationSuccessMessage("dutyRosterScheduleInputMessages", MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	protected boolean deleteEvent() {
		return false;
	}

	public void dutyRosterTurnStartAddOneDay(ActionEvent e) {
		if (this.in.getStart() != null) {
			this.in.setStart(DateUtil.addDayMinuteDelta(this.in.getStart(), 1, 0));
		}
	}

	public void dutyRosterTurnStartSubOneDay(ActionEvent e) {
		if (this.in.getStart() != null) {
			this.in.setStart(DateUtil.addDayMinuteDelta(this.in.getStart(), -1, 0));
		}
	}

	public void dutyRosterTurnStopAddOneDay(ActionEvent e) {
		if (this.in.getStop() != null) {
			this.in.setStop(DateUtil.addDayMinuteDelta(this.in.getStop(), 1, 0));
		}
	}

	public void dutyRosterTurnStopSubOneDay(ActionEvent e) {
		if (this.in.getStop() != null) {
			this.in.setStop(DateUtil.addDayMinuteDelta(this.in.getStop(), -1, 0));
		}
	}

	public CollidingInventoryBookingEagerModel getCollidingInventoryBookingModel(DutyRosterTurnOutVO dutyRosterTurn) {
		return CollidingInventoryBookingEagerModel.getCachedCollidingInventoryBookingModel(dutyRosterTurn, true, collidingInventoryBookingModelCache);
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(DutyRosterTurnOutVO dutyRosterTurn) {
		return CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(dutyRosterTurn, true, collidingStaffStatusEntryModelCache);
	}

	private List<String> getCompleteCalendarList(String query) {
		Collection<String> calendars = null;
		try {
			calendars = WebUtil.getServiceLocator().getTrialService().getCalendars(WebUtil.getAuthentication(),
					null, null, null, query, null); // let permission argument override decide...
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
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

	private List<String> getCompleteTitleList(String query) {
		Collection<String> titles = null;
		try {
			titles = WebUtil.getServiceLocator().getTrialService().getDutyRosterTurnTitles(WebUtil.getAuthentication(),
					null, null, null, query, null); // let permission argument override decide...
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (titles != null) {
			try {
				return ((List<String>) titles);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public ArrayList<SelectItem> getFilterCalendars() {
		return filterCalendars;
	}

	public ArrayList<SelectItem> getFilterTitles() {
		return filterTitles;
	}

	public DutyRosterTurnInVO getIn() {
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

	public DutyRosterTurnOutVO getOut() {
		return out;
	}

	public IDVO getSelectedDutyRosterTurn() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public String getStaffName() {
		return WebUtil.staffIdToName(in.getStaffId());
	}

	public String getTrialName() {
		return WebUtil.trialIdToName(in.getTrialId());
	}

	protected ArrayList<SelectItem> getTrialVisitScheduleItems() {
		TrialOutVO trial = WebUtil.getTrial(in.getTrialId());
		if (trial != null) {
			DepartmentVO department = trial.getDepartment();
			return getVisitScheduleItems(department == null ? null : department.getId());
		}
		return new ArrayList<SelectItem>();
	}

	public String getVisitScheduleItemName(VisitScheduleItemOutVO visitScheduleItemVO) {
		if (visitScheduleItemVO != null) {
			TrialOutVO trialVO = in.getTrialId() == null ? visitScheduleItemVO.getTrial() : null;
			return trialVO == null ? MessageFormat.format(VISIT_SCHEDULE_ITEM_NAME, visitScheduleItemVO.getName())
					: MessageFormat.format(TRIAL_VISIT_SCHEDULE_ITEM_NAME,
							CommonUtil.trialOutVOToString(visitScheduleItemVO.getTrial()), visitScheduleItemVO.getName());
		}
		return "";
	}

	public abstract ArrayList<SelectItem> getVisitScheduleItems();

	protected ArrayList<SelectItem> getVisitScheduleItems(Long departmentId) {
		ArrayList<SelectItem> visitScheduleItems;
		Collection<VisitScheduleItemOutVO> visitScheduleItemVOs = null;
		if (in.getStart() != null && in.getStop() != null) {
			try {
				visitScheduleItemVOs = WebUtil
						.getServiceLocator()
						.getTrialService()
						.getVisitScheduleItems(WebUtil.getAuthentication(), in.getTrialId(), departmentId, in.getStart(), in.getStop(),
								in.getVisitScheduleItemId(), true);
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			}
		}
		if (visitScheduleItemVOs != null) {
			visitScheduleItems = new ArrayList<SelectItem>(visitScheduleItemVOs.size());
			Iterator<VisitScheduleItemOutVO> it = visitScheduleItemVOs.iterator();
			while (it.hasNext()) {
				VisitScheduleItemOutVO visitScheduleItemVO = it.next();
				visitScheduleItems.add(new SelectItem(Long.toString(visitScheduleItemVO.getId()), getVisitScheduleItemName(visitScheduleItemVO)));
			}
		} else {
			visitScheduleItems = new ArrayList<SelectItem>();
		}
		return visitScheduleItems;
	}

	public void handleCalendarSelect(SelectEvent event) {
		in.setCalendar((String) event.getObject());
	}

	public void handleTitleSelect(SelectEvent event) {
		in.setTitle((String) event.getObject());
	}

	public void handleVisitScheduleItemChange() {
		VisitScheduleItemOutVO visitScheduleItem = WebUtil.getVisitScheduleItem(in.getVisitScheduleItemId());
		if (visitScheduleItem != null) {
			in.setTrialId(visitScheduleItem.getTrial().getId());
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
			out = WebUtil.getServiceLocator().getStaffService().getDutyRosterTurn(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
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

	private void sanitizeTrial() {
		if (in.getVisitScheduleItemId() != null && in.getTrialId() == null) {
			VisitScheduleItemOutVO visitScheduleItem = WebUtil.getVisitScheduleItem(in.getVisitScheduleItemId());
			if (visitScheduleItem != null) {
				in.setTrialId(visitScheduleItem.getTrial().getId());
			}
		}
	}

	public void setSelectedDutyRosterTurn(IDVO dutyRosterTurn) {
		if (dutyRosterTurn != null) {
			this.out = (DutyRosterTurnOutVO) dutyRosterTurn.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		Long trialIdBackup = in.getTrialId();
		sanitizeTrial();
		try {
			out = WebUtil.getServiceLocator().getStaffService().updateDutyRosterTurn(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			updateEvent();
			addOperationSuccessMessage("dutyRosterScheduleInputMessages", MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.setTrialId(trialIdBackup);
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.setTrialId(trialIdBackup);
			Messages.addMessageClientId("dutyRosterScheduleInputMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	protected void updateEvent() {
	}
}
