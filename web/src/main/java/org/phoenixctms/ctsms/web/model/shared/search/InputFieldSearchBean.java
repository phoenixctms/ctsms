package org.phoenixctms.ctsms.web.model.shared.search;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InputFieldOutVOConfig;
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
public class InputFieldSearchBean extends SearchBeanBase {

	private InputFieldSearchResultLazyModel inputFieldResultModel;
	// private ArrayList<SelectItem> filterCategories;
	private HashMap<Long, Collection<IDVO>> inquiryCache;
	private HashMap<Long, Collection<IDVO>> listEntryTagCache;
	private HashMap<Long, Collection<IDVO>> ecrfFieldCache;
	private HashMap<Long, Map<IDVO, ArrayList<IDVO>>> inquiryTrialsCache;
	private HashMap<Long, Map<IDVO, ArrayList<IDVO>>> listEntryTagTrialsCache;
	private HashMap<Long, Map<IDVO, ArrayList<IDVO>>> ecrfFieldTrialsCache;
	private final static String TRIAL_LIST_SEPARATOR = ", ";

	public InputFieldSearchBean() {
		super();
		inputFieldResultModel = new InputFieldSearchResultLazyModel();
		inquiryCache = new HashMap<Long, Collection<IDVO>>();
		listEntryTagCache = new HashMap<Long, Collection<IDVO>>();
		ecrfFieldCache = new HashMap<Long, Collection<IDVO>>();
		inquiryTrialsCache = new HashMap<Long, Map<IDVO, ArrayList<IDVO>>>();
		listEntryTagTrialsCache = new HashMap<Long, Map<IDVO, ArrayList<IDVO>>>();
		ecrfFieldTrialsCache = new HashMap<Long, Map<IDVO, ArrayList<IDVO>>>();
	}

	private void clearCaches() {
		inquiryCache.clear();
		listEntryTagCache.clear();
		ecrfFieldCache.clear();
		inquiryTrialsCache.clear();
		listEntryTagTrialsCache.clear();
		ecrfFieldTrialsCache.clear();
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.INPUT_FIELD_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.INPUT_FIELD_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.inputFieldResultModel.getCurrentPageIds();
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.INPUT_FIELD_DB;
	}

	public Collection<IDVO> getEcrfFields(InputFieldOutVO inputField) {
		if (inputField != null) {
			if (!ecrfFieldCache.containsKey(inputField.getId())) {
				Collection ecrfFields = null;
				try {
					ecrfFields = WebUtil.getServiceLocator().getInputFieldService().getEcrfFieldList(WebUtil.getAuthentication(), inputField.getId(), null);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
				if (ecrfFields == null) {
					ecrfFields = new ArrayList<ECRFFieldOutVO>();
				}
				IDVO.transformVoCollection(ecrfFields);
				ecrfFieldCache.put(inputField.getId(), ecrfFields);
				return ecrfFields;
			} else {
				return ecrfFieldCache.get(inputField.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Map<IDVO, ArrayList<IDVO>> getEcrfFieldTrials(InputFieldOutVO inputField) {
		if (inputField != null) {
			if (!ecrfFieldTrialsCache.containsKey(inputField.getId())) {
				Iterator<IDVO> it = getEcrfFields(inputField).iterator();
				HashMap<IDVO, ArrayList<IDVO>> trialMap = new HashMap<IDVO, ArrayList<IDVO>>();
				while (it.hasNext()) {
					IDVO ecrfField = it.next();
					IDVO trial = new IDVO(((ECRFFieldOutVO) ecrfField.getVo()).getTrial());
					if (!trialMap.containsKey(trial)) {
						ArrayList<IDVO> ecrfFieldList = new ArrayList<IDVO>();
						trialMap.put(trial, ecrfFieldList);
					}
					trialMap.get(trial).add(ecrfField);
				}
				ecrfFieldTrialsCache.put(inputField.getId(), trialMap);
				return trialMap;
			} else {
				return ecrfFieldTrialsCache.get(inputField.getId());
			}
		}
		return new HashMap<IDVO, ArrayList<IDVO>>();
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_INPUT_FIELD_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_INPUT_FIELD_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		clearCaches();
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportInputField(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
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

	// public ArrayList<SelectItem> getFilterCategories() {
	// return filterCategories;
	// }
	public InputFieldSearchResultLazyModel getInputFieldResultModel() {
		return inputFieldResultModel;
	}

	public Collection<IDVO> getInquiries(InputFieldOutVO inputField) {
		if (inputField != null) {
			if (!inquiryCache.containsKey(inputField.getId())) {
				Collection inquiries = null;
				try {
					inquiries = WebUtil.getServiceLocator().getInputFieldService().getInquiryList(WebUtil.getAuthentication(), inputField.getId(), null);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
				if (inquiries == null) {
					inquiries = new ArrayList<InquiryOutVO>();
				}
				IDVO.transformVoCollection(inquiries);
				inquiryCache.put(inputField.getId(), inquiries);
				return inquiries;
			} else {
				return inquiryCache.get(inputField.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Map<IDVO, ArrayList<IDVO>> getInquiryTrials(InputFieldOutVO inputField) {
		if (inputField != null) {
			if (!inquiryTrialsCache.containsKey(inputField.getId())) {
				Iterator<IDVO> it = getInquiries(inputField).iterator();
				HashMap<IDVO, ArrayList<IDVO>> trialMap = new HashMap<IDVO, ArrayList<IDVO>>();
				while (it.hasNext()) {
					IDVO inquiry = it.next();
					IDVO trial = new IDVO(((InquiryOutVO) inquiry.getVo()).getTrial());
					if (!trialMap.containsKey(trial)) {
						ArrayList<IDVO> inquiryList = new ArrayList<IDVO>();
						trialMap.put(trial, inquiryList);
					}
					trialMap.get(trial).add(inquiry);
				}
				inquiryTrialsCache.put(inputField.getId(), trialMap);
				return trialMap;
			} else {
				return inquiryTrialsCache.get(inputField.getId());
			}
		}
		return new HashMap<IDVO, ArrayList<IDVO>>();
	}

	public Collection<IDVO> getProbandListEntryTags(InputFieldOutVO inputField) {
		if (inputField != null) {
			if (!listEntryTagCache.containsKey(inputField.getId())) {
				Collection probandListEntryTags = null;
				try {
					probandListEntryTags = WebUtil.getServiceLocator().getInputFieldService().getProbandListEntryTagList(WebUtil.getAuthentication(), inputField.getId(), null);
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
				if (probandListEntryTags == null) {
					probandListEntryTags = new ArrayList<ProbandListEntryTagOutVO>();
				}
				IDVO.transformVoCollection(probandListEntryTags);
				listEntryTagCache.put(inputField.getId(), probandListEntryTags);
				return probandListEntryTags;
			} else {
				return listEntryTagCache.get(inputField.getId());
			}
		}
		return new ArrayList<IDVO>();
	}

	public Map<IDVO, ArrayList<IDVO>> getProbandListEntryTagTrials(InputFieldOutVO inputField) {
		if (inputField != null) {
			if (!listEntryTagTrialsCache.containsKey(inputField.getId())) {
				Iterator<IDVO> it = getProbandListEntryTags(inputField).iterator();
				HashMap<IDVO, ArrayList<IDVO>> trialMap = new HashMap<IDVO, ArrayList<IDVO>>();
				while (it.hasNext()) {
					IDVO listEntryTag = it.next();
					IDVO trial = new IDVO(((ProbandListEntryTagOutVO) listEntryTag.getVo()).getTrial());
					if (!trialMap.containsKey(trial)) {
						ArrayList<IDVO> listEntryTagList = new ArrayList<IDVO>();
						trialMap.put(trial, listEntryTagList);
					}
					trialMap.get(trial).add(listEntryTag);
				}
				listEntryTagTrialsCache.put(inputField.getId(), trialMap);
				return trialMap;
			} else {
				return listEntryTagTrialsCache.get(inputField.getId());
			}
		}
		return new HashMap<IDVO, ArrayList<IDVO>>();
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(inputFieldResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_INPUT_FIELD_ITEM_LABEL);
	}

	public Long getSelectionSetValueCount(InputFieldOutVO inputField) {
		if (inputField != null) {
			InputFieldOutVOConfig config = new InputFieldOutVOConfig();
			config.setInputField(inputField);
			if (config.isSelect()) {
				return WebUtil.getSelectionSetValueCount(inputField.getId());
			}
		}
		return null;
	}

	public String getSetPickerIDJSCall(InputFieldOutVO inputField) {
		return getSetPickerIDJSCall(inputField == null ? null : inputField.getId(), WebUtil.clipStringPicker(WebUtil.inputFieldOutVOToString(inputField)));
	}

	@Override
	public String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.INPUT_FIELD_CRITERIA_TITLE, MessageCodes.CREATE_NEW_INPUT_FIELD_CRITERIA, operationSuccess);
	}

	public String getTrialsLabel(Map<IDVO, ArrayList<IDVO>> trialMap) {
		Iterator<IDVO> it = trialMap.keySet().iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(CommonUtil.trialOutVOToString((TrialOutVO) it.next().getVo()));
			if (it.hasNext()) {
				sb.append(TRIAL_LIST_SEPARATOR);
			}
		}
		return sb.toString();
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
		// Collection<String> categoryStrings = null;
		// try {
		// categoryStrings = WebUtil.getServiceLocator().getSelectionSetService().getInputFieldCategories(WebUtil.getAuthentication(), null, null, null);
		// } catch (AuthenticationException e) {
		// WebUtil.publishException(e);
		// } catch (AuthorisationException e) {
		// } catch (ServiceException e) {
		// } catch (IllegalArgumentException e) {
		// }
		// if (categoryStrings != null) {
		// filterCategories = new ArrayList<SelectItem>(categoryStrings.size());
		// Iterator<String> it = categoryStrings.iterator();
		// while (it.hasNext()) {
		// String category = it.next();
		// filterCategories.add(new SelectItem(category, category));
		// }
		// } else {
		// filterCategories = new ArrayList<SelectItem>();
		// }
		// filterCategories.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
	}

	@Override
	public boolean isMarkUnEncrypted() {
		return false;
	}

	@Override
	public String searchAction() {
		clearCaches();
		inputFieldResultModel.setCriteriaIn(criteriaIn);
		inputFieldResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		inputFieldResultModel.updateRowCount();
		LazyDataModelBase.clearFilters("input_field_result_list");
		return SEARCH_OUTCOME;
	}
}
