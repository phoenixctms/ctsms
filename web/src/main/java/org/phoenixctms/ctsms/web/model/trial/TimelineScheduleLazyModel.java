package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class TimelineScheduleLazyModel extends LazyDataModelBase {

	private Long trialId;
	private Long departmentId;
	private Long teamMemberStaffId;
	private Boolean notify;
	private Boolean ignoreTimelineEvents;

	public TimelineScheduleLazyModel() {
		super();
		setIdentityTeamMember(Settings.getBoolean(SettingCodes.TIMELINE_SCHEDULE_IDENTITY_TEAM_MEMBER_PRESET, Bundle.SETTINGS,
				DefaultSettings.TIMELINE_SCHEDULE_IDENTITY_TEAM_MEMBER_PRESET));
		setShowNotifyOnly(Settings.getBoolean(SettingCodes.TIMELINE_SCHEDULE_SHOW_NOTIFY_ONLY_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_SCHEDULE_SHOW_NOTIFY_ONLY_PRESET));
		setIgnoreObsolete(Settings.getBoolean(SettingCodes.TIMELINE_SCHEDULE_IGNORE_OBSOLETE_PRESET, Bundle.SETTINGS, DefaultSettings.TIMELINE_SCHEDULE_IGNORE_OBSOLETE_PRESET));
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public boolean getIgnoreObsolete() {
		return ignoreTimelineEvents != null;
	}

	@Override
	protected Collection<TimelineEventOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getTrialService()
					.getTimelineSchedule(WebUtil.getAuthentication(), null, trialId, departmentId, teamMemberStaffId, notify, ignoreTimelineEvents, psf);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<TimelineEventOutVO>();
	}

	@Override
	protected TimelineEventOutVO getRowElement(Long id) {
		return WebUtil.getTimelineEvent(id);
	}

	public boolean getShowNotifyOnly() {
		return notify != null;
	}

	public Long getTeamMemberStaffId() {
		return teamMemberStaffId;
	}

	public Long getTrialId() {
		return trialId;
	}

	public boolean isIdentityTeamMember() {
		if (teamMemberStaffId != null) {
			StaffOutVO identity = WebUtil.getUserIdentity();
			if (identity != null && teamMemberStaffId.longValue() == identity.getId()) {
				return true;
			}
		}
		return false;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setIdentityTeamMember(boolean identityResponsiblePerson) {
		if (identityResponsiblePerson) {
			StaffOutVO identity = WebUtil.getUserIdentity();
			if (identity != null) {
				teamMemberStaffId = identity.getId();
			} else {
				teamMemberStaffId = null;
			}
		} else {
			teamMemberStaffId = null;
		}
	}

	public void setIgnoreObsolete(boolean ignoreObsolete) {
		this.ignoreTimelineEvents = ignoreObsolete ? false : null;
	}

	public void setShowNotifyOnly(boolean notify) {
		this.notify = notify ? true : null;
	}

	public void setTeamMemberStaffId(Long teamMemberStaffId) {
		this.teamMemberStaffId = teamMemberStaffId;
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
	}
}
