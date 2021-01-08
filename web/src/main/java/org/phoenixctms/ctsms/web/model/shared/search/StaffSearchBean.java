package org.phoenixctms.ctsms.web.model.shared.search;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CvPDFVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordPDFVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class StaffSearchBean extends SearchBeanBase {

	private StaffSearchResultLazyModel staffResultModel;
	private HashMap<Long, Collection<IDVO>> staffTagValueCache;
	private HashMap<Long, Collection<IDVO>> staffContactDetailValueCache;
	private HashMap<Long, Collection<IDVO>> staffAddressCache;

	public StaffSearchBean() {
		super();
		staffTagValueCache = new HashMap<Long, Collection<IDVO>>();
		staffContactDetailValueCache = new HashMap<Long, Collection<IDVO>>();
		staffAddressCache = new HashMap<Long, Collection<IDVO>>();
		staffResultModel = new StaffSearchResultLazyModel();
	}

	private void clearCaches() {
		staffTagValueCache.clear();
		staffContactDetailValueCache.clear();
		staffAddressCache.clear();
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.STAFF_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.STAFF_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.staffResultModel.getCurrentPageIds();
	}

	public StreamedContent getCvPdfStreamedContent() throws Exception {
		clearCaches();
		try {
			CvPDFVO cv = WebUtil.getServiceLocator().getSearchService().renderCvPDFs(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(cv.getDocumentDatas()), cv.getContentType().getMimeType(), cv.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getTrainingRecordPdfStreamedContent(boolean allTrials) throws Exception {
		clearCaches();
		try {
			TrainingRecordPDFVO trainingRecord = WebUtil.getServiceLocator().getSearchService().renderTrainingRecordPDFs(WebUtil.getAuthentication(), criteriaIn,
					new HashSet<CriterionInVO>(criterionsIn), allTrials, null);
			return new DefaultStreamedContent(new ByteArrayInputStream(trainingRecord.getDocumentDatas()), trainingRecord.getContentType().getMimeType(),
					trainingRecord.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.STAFF_DB;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_STAFF_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_STAFF_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		clearCaches();
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportStaff(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public String getMaxOverlappingShiftsString(StaffOutVO staff) {
		if (staff != null && staff.getAllocatable()) {
			return Messages.getMessage(MessageCodes.MAX_OVERLAPPING_LABEL, staff.getMaxOverlappingShifts());
		}
		return "";
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(staffResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_STAFF_ITEM_LABEL);
	}

	public String getSetPickerIDJSCall(StaffOutVO staff) {
		return getSetPickerIDJSCall(staff == null ? null : staff.getId(), WebUtil.clipStringPicker(WebUtil.staffOutVOToString(staff)));
	}

	public Collection<IDVO> getStaffAddresses(StaffOutVO staff) {
		if (staff != null) {
			if (!staffAddressCache.containsKey(staff.getId())) {
				Collection staffAddresses = null;
				try {
					staffAddresses = WebUtil.getServiceLocator().getStaffService().getStaffAddressList(WebUtil.getAuthentication(), staff.getId(), null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (staffAddresses == null) {
					staffAddresses = new ArrayList<StaffAddressOutVO>();
				}
				IDVO.transformVoCollection(staffAddresses);
				staffAddressCache.put(staff.getId(), staffAddresses);
				return staffAddresses;
			} else {
				return staffAddressCache.get(staff.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Collection<IDVO> getStaffContactDetailValues(StaffOutVO staff) {
		if (staff != null) {
			if (!staffContactDetailValueCache.containsKey(staff.getId())) {
				Collection staffContactDetailValues = null;
				try {
					staffContactDetailValues = WebUtil.getServiceLocator().getStaffService()
							.getStaffContactDetailValueList(WebUtil.getAuthentication(), staff.getId(), false, null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (staffContactDetailValues == null) {
					staffContactDetailValues = new ArrayList<StaffContactDetailValueOutVO>();
				}
				IDVO.transformVoCollection(staffContactDetailValues);
				staffContactDetailValueCache.put(staff.getId(), staffContactDetailValues);
				return staffContactDetailValues;
			} else {
				return staffContactDetailValueCache.get(staff.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public StaffSearchResultLazyModel getStaffResultModel() {
		return staffResultModel;
	}

	public Collection<IDVO> getStaffTagValues(StaffOutVO staff) {
		if (staff != null) {
			if (!staffTagValueCache.containsKey(staff.getId())) {
				Collection staffTagValues = null;
				try {
					staffTagValues = WebUtil.getServiceLocator().getStaffService().getStaffTagValueList(WebUtil.getAuthentication(), staff.getId(), null);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				if (staffTagValues == null) {
					staffTagValues = new ArrayList<StaffTagValueOutVO>();
				}
				IDVO.transformVoCollection(staffTagValues);
				staffTagValueCache.put(staff.getId(), staffTagValues);
				return staffTagValues;
			} else {
				return staffTagValueCache.get(staff.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.STAFF_CRITERIA_TITLE, MessageCodes.CREATE_NEW_STAFF_CRITERIA, operationSuccess);
	}

	@PostConstruct
	private void init() {
		initPickTarget();
		Long id = WebUtil.getLongParamValue(GetParamNames.CRITERIA_ID);
		if (id != null) {
			this.load(id);
		} else {
			loadDefault();
		}
	}

	@Override
	protected void initSpecificSets() {
		clearCaches();
	}

	@Override
	public boolean isMarkUnEncrypted() {
		return false;
	}

	@Override
	public String searchAction() {
		clearCaches();
		staffResultModel.setCriteriaIn(criteriaIn);
		staffResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		staffResultModel.updateRowCount();
		DataTable.clearFilters(getResultListId());
		return SEARCH_OUTCOME;
	}

	@Override
	public JobModule getJobModule() {
		return JobModule.STAFF_CRITERIA_JOB;
	}
}
