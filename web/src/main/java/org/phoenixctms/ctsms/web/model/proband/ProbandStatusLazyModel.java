package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandStatusLazyModel extends LazyDataModelBase {

	private Long probandId;
	private Long departmentId;
	private Long probandCategoryId;
	private Boolean probandActive;
	private Boolean hideAvailability;

	public ProbandStatusLazyModel() {
		super();
		setShowInActiveOnly(Settings.getBoolean(SettingCodes.PROBAND_STATUS_SHOW_INACTIVE_ONLY_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_STATUS_SHOW_INACTIVE_ONLY_PRESET));
		setIgnoreObsolete(Settings.getBoolean(SettingCodes.PROBAND_STATUS_IGNORE_OBSOLETE_PRESET, Bundle.SETTINGS, DefaultSettings.PROBAND_STATUS_IGNORE_OBSOLETE_PRESET));
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public boolean getIgnoreObsolete() {
		return hideAvailability != null;
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> getLazyResult(PSFVO psf) {
		try {
			return WebUtil.getServiceLocator().getProbandService()
					.getProbandStatus(WebUtil.getAuthentication(), null, probandId, departmentId, probandCategoryId, probandActive, hideAvailability, psf);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		return new ArrayList<ProbandStatusEntryOutVO>();
	}

	public Long getProbandCategoryId() {
		return probandCategoryId;
	}

	public Long getProbandId() {
		return probandId;
	}

	@Override
	protected ProbandStatusEntryOutVO getRowElement(Long id) {
		return WebUtil.getProbandStatusEntry(id);
	}

	public boolean getShowInActiveOnly() {
		return probandActive != null;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public void setIgnoreObsolete(boolean ignoreObsolete) {
		this.hideAvailability = ignoreObsolete ? false : null;
	}

	public void setProbandCategoryId(Long probandCategoryId) {
		this.probandCategoryId = probandCategoryId;
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
	}

	public void setShowInActiveOnly(boolean active) {
		this.probandActive = active ? false : null;
	}
}
