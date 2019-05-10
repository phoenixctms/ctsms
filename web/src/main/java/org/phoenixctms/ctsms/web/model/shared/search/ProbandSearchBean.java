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
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
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
public class ProbandSearchBean extends SearchBeanBase {

	private ProbandSearchResultLazyModel probandResultModel;
	private HashMap<Long, Collection<IDVO>> probandTagValueCache;
	private HashMap<Long, Collection<IDVO>> probandContactDetailValueCache;
	private HashMap<Long, Collection<IDVO>> probandAddressCache;
	private HashMap<Long, Collection<IDVO>> probandListStatusCache;

	public ProbandSearchBean() {
		super();
		probandTagValueCache = new HashMap<Long, Collection<IDVO>>();
		probandContactDetailValueCache = new HashMap<Long, Collection<IDVO>>();
		probandAddressCache = new HashMap<Long, Collection<IDVO>>();
		probandListStatusCache = new HashMap<Long, Collection<IDVO>>();
		probandResultModel = new ProbandSearchResultLazyModel();
	}

	private void clearCaches() {
		probandTagValueCache.clear();
		probandContactDetailValueCache.clear();
		probandAddressCache.clear();
		probandListStatusCache.clear();
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.PROBAND_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.PROBAND_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.probandResultModel.getCurrentPageIds();
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.PROBAND_DB;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_PROBAND_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_PROBAND_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		clearCaches();
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportProband(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	public Collection<IDVO> getProbandAddresses(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandAddressCache.containsKey(proband.getId())) {
				Collection probandAddresses = null;
				try {
					probandAddresses = WebUtil.getServiceLocator().getProbandService().getProbandAddressList(WebUtil.getAuthentication(), proband.getId(), null);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
				if (probandAddresses == null) {
					probandAddresses = new ArrayList<IDVO>();
				}
				IDVO.transformVoCollection(probandAddresses);
				probandAddressCache.put(proband.getId(), probandAddresses);
				return probandAddresses;
			} else {
				return probandAddressCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Collection<IDVO> getProbandContactDetailValues(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandContactDetailValueCache.containsKey(proband.getId())) {
				Collection probandContactDetailValues = null;
				try {
					probandContactDetailValues = WebUtil.getServiceLocator().getProbandService()
							.getProbandContactDetailValueList(WebUtil.getAuthentication(), proband.getId(), false, null);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
				if (probandContactDetailValues == null) {
					probandContactDetailValues = new ArrayList<ProbandContactDetailValueOutVO>();
				}
				IDVO.transformVoCollection(probandContactDetailValues);
				probandContactDetailValueCache.put(proband.getId(), probandContactDetailValues);
				return probandContactDetailValues;
			} else {
				return probandContactDetailValueCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public StreamedContent getProbandLetterPdfStreamedContent() throws Exception {
		clearCaches();
		try {
			ProbandLetterPDFVO probandLetter = WebUtil.getServiceLocator().getSearchService()
					.renderProbandLetterPDFs(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(probandLetter.getDocumentDatas()), probandLetter.getContentType().getMimeType(),
					probandLetter.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	public Collection<IDVO> getProbandListStatus(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandListStatusCache.containsKey(proband.getId())) {
				Collection probandListStatus = null;
				try {
					probandListStatus = WebUtil.getServiceLocator().getTrialService().getProbandListStatus(WebUtil.getAuthentication(), null, proband.getId(), true, null);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
				if (probandListStatus == null) {
					probandListStatus = new ArrayList<ProbandListStatusEntryOutVO>();
				}
				IDVO.transformVoCollection(probandListStatus);
				probandListStatusCache.put(proband.getId(), probandListStatus);
				return probandListStatus;
			} else {
				return probandListStatusCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public ProbandSearchResultLazyModel getProbandResultModel() {
		return probandResultModel;
	}

	public Collection<IDVO> getProbandTagValues(ProbandOutVO proband) {
		if (proband != null) {
			if (!probandTagValueCache.containsKey(proband.getId())) {
				Collection probandTagValues = null;
				try {
					probandTagValues = WebUtil.getServiceLocator().getProbandService().getProbandTagValueList(WebUtil.getAuthentication(), proband.getId(), null);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
				if (probandTagValues == null) {
					probandTagValues = new ArrayList<ProbandTagValueOutVO>();
				}
				IDVO.transformVoCollection(probandTagValues);
				probandTagValueCache.put(proband.getId(), probandTagValues);
				return probandTagValues;
			} else {
				return probandTagValueCache.get(proband.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(probandResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_PROBAND_ITEM_LABEL);
	}

	public String getSetPickerIDJSCall(ProbandOutVO proband) {
		return getSetPickerIDJSCall(proband == null ? null : proband.getId(), WebUtil.clipStringPicker(WebUtil.probandOutVOToString(proband)));
	}

	@Override
	protected String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.PROBAND_CRITERIA_TITLE, MessageCodes.CREATE_NEW_PROBAND_CRITERIA, operationSuccess);
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
		return true;
	}

	public String probandToColor(ProbandOutVO proband) {
		return (proband != null ? WebUtil.colorToStyleClass(proband.getCategory().getColor()) : "");
	}

	@Override
	public String searchAction() {
		clearCaches();
		probandResultModel.setCriteriaIn(criteriaIn);
		probandResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		probandResultModel.updateRowCount();
		LazyDataModelBase.clearFilters("proband_result_list");
		return SEARCH_OUTCOME;
	}
}
