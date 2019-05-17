package org.phoenixctms.ctsms.web.model.shared;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.adapt.InquiryValueOutVOStringAdapter;
import org.phoenixctms.ctsms.web.adapt.ProbandListEntryTagValueOutVOStringAdapter;
import org.phoenixctms.ctsms.web.model.EagerDataModel;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class ProbandListEntryModel extends LazyDataModelBase implements EagerDataModel {

	private static final String PROBAND_LIST_ENTRY_TAG_LABEL_WITH_FIELD_NAME = "{0}. {1}";
	private static final String PROBAND_LIST_ENTRY_TAG_LABEL = "{0}.";
	private static final String CATEGORY_INQUIRY_LABEL_WITH_FIELD_NAME = "{0} - {1}. {2}";
	private static final String CATEGORY_INQUIRY_LABEL = "{0} - {1}.";
	private static final String INQUIRY_LABEL_WITH_FIELD_NAME = "{0}. {1}";
	private static final String INQUIRY_LABEL = "{0}.";
	private static final String CATEGORY_INQUIRY_LABEL_WITH_TRIAL_NAME = "{0} - {1} - {2}.";
	private static final String INQUIRY_LABEL_WITH_TRIAL_NAME = "{0} - {1}.";

	public static ProbandListEntryModel getCachedProbandListEntryModel(ProbandGroupOutVO group, HashMap<Long, ProbandListEntryModel> probandListEntryModelCache,
			boolean showProbandListEntryTagColumn, boolean showInquiryColumn, boolean total) {
		ProbandListEntryModel model;
		if (group != null && probandListEntryModelCache != null) {
			long probandGroupId = group.getId();
			if (probandListEntryModelCache.containsKey(probandGroupId)) {
				model = probandListEntryModelCache.get(probandGroupId);
			} else {
				model = new ProbandListEntryModel(group, showProbandListEntryTagColumn, showInquiryColumn, total);
				probandListEntryModelCache.put(probandGroupId, model);
			}
		} else {
			model = new ProbandListEntryModel(showProbandListEntryTagColumn, showInquiryColumn, total);
		}
		return model;
	}

	private static String getInquiryLabel(InquiryOutVO inquiry, boolean withFieldName) {
		if (inquiry != null) {
			String category = inquiry.getCategory();
			if (category != null && category.length() > 0) {
				if (withFieldName) {
					return MessageFormat.format(CATEGORY_INQUIRY_LABEL_WITH_FIELD_NAME, category, Long.toString(inquiry.getPosition()), inquiry.getField().getName());
				} else {
					return MessageFormat.format(CATEGORY_INQUIRY_LABEL, category, Long.toString(inquiry.getPosition()));
				}
			} else {
				if (withFieldName) {
					return MessageFormat.format(INQUIRY_LABEL_WITH_FIELD_NAME, Long.toString(inquiry.getPosition()), inquiry.getField().getName());
				} else {
					return MessageFormat.format(INQUIRY_LABEL, Long.toString(inquiry.getPosition()));
				}
			}
		}
		return "";
	}

	private static String getProbandListEntryTagLabel(ProbandListEntryTagOutVO listEntryTag, boolean withFieldName) {
		if (listEntryTag != null) {
			if (withFieldName) {
				return MessageFormat.format(PROBAND_LIST_ENTRY_TAG_LABEL_WITH_FIELD_NAME, Long.toString(listEntryTag.getPosition()), listEntryTag.getField().getName());
			} else {
				return MessageFormat.format(PROBAND_LIST_ENTRY_TAG_LABEL, Long.toString(listEntryTag.getPosition()));
			}
		}
		return "";
	}

	private static boolean isFieldTypeBoolean(InputFieldTypeVO fieldType) {
		if (fieldType != null) {
			return InputFieldType.CHECKBOX.equals(fieldType.getType());
		}
		return false;
	}

	private Long trialId;
	private ProbandGroupOutVO probandGroup;
	private Long probandId;
	private ArrayList<SelectItem> probandListEntryTags;
	private ArrayList<SelectItem> inquiries;
	private ArrayList<SelectItem> probandListEntryTagInputFields;
	private ArrayList<SelectItem> inquiryInputFields;
	private Long columnProbandListEntryTagAId;
	private Long columnProbandListEntryTagBId;
	private Long columnInquiryId;
	private ProbandListEntryTagOutVO columnProbandListEntryTagA;
	private ProbandListEntryTagOutVO columnProbandListEntryTagB;
	private InquiryOutVO columnInquiry;
	private Long columnProbandListEntryTagInputFieldAId;
	private Long columnProbandListEntryTagInputFieldBId;
	private Long columnInquiryInputFieldId;
	private List<IDVO> allRows;
	private HashMap<Long, MoneyTransferSummaryVO> payOffSummaryCache;
	private boolean showProbandListEntryTagColumn;
	private boolean showInquiryColumn;
	private boolean total;
	private HashMap<Long, HashMap<Long, HashMap<Long, Collection<InquiryValueOutVO>>>> inquiryValuesCache;
	private HashMap<Long, HashMap<Long, InquiryValueOutVO>> inquiryValueCache;
	private HashMap<Long, HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>> probandListEntryTagValuesCache;
	private HashMap<Long, HashMap<Long, ProbandListEntryTagValueOutVO>> probandListEntryTagValueCache;

	public ProbandListEntryModel(boolean showProbandListEntryTagColumn, boolean showInquiryColumn, boolean total) {
		super();
		this.showProbandListEntryTagColumn = showProbandListEntryTagColumn;
		this.showInquiryColumn = showInquiryColumn;
		this.total = total;
		payOffSummaryCache = new HashMap<Long, MoneyTransferSummaryVO>();
		inquiryValuesCache = new HashMap<Long, HashMap<Long, HashMap<Long, Collection<InquiryValueOutVO>>>>();
		inquiryValueCache = new HashMap<Long, HashMap<Long, InquiryValueOutVO>>();
		probandListEntryTagValuesCache = new HashMap<Long, HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>>();
		probandListEntryTagValueCache = new HashMap<Long, HashMap<Long, ProbandListEntryTagValueOutVO>>();
		resetRows();
		initSets(true);
	}

	public ProbandListEntryModel(ProbandGroupOutVO probandGroup, boolean showProbandListEntryTagColumn, boolean showInquiryColumn, boolean total) {
		super();
		this.showProbandListEntryTagColumn = showProbandListEntryTagColumn;
		this.showInquiryColumn = showInquiryColumn;
		this.total = total;
		payOffSummaryCache = new HashMap<Long, MoneyTransferSummaryVO>();
		inquiryValuesCache = new HashMap<Long, HashMap<Long, HashMap<Long, Collection<InquiryValueOutVO>>>>();
		inquiryValueCache = new HashMap<Long, HashMap<Long, InquiryValueOutVO>>();
		probandListEntryTagValuesCache = new HashMap<Long, HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>>();
		probandListEntryTagValueCache = new HashMap<Long, HashMap<Long, ProbandListEntryTagValueOutVO>>();
		resetRows();
		setProbandGroup(probandGroup);
		initSets(true);
	}

	public void clearSelectedColumns() {
		columnProbandListEntryTagAId = null;
		columnProbandListEntryTagBId = null;
		columnInquiryId = null;
		columnProbandListEntryTagA = null;
		columnProbandListEntryTagB = null;
		columnInquiry = null;
		columnProbandListEntryTagInputFieldAId = null;
		columnProbandListEntryTagInputFieldBId = null;
		columnInquiryInputFieldId = null;
	}

	@Override
	public int getAllRowCount() {
		return getAllRows().size();
	}

	@Override
	public List<IDVO> getAllRows() {
		if (allRows == null) {
			allRows = loadAll();
		}
		return allRows;
	}

	public Collection<InquiryValueOutVO> getAllTrialsInquiryInputFieldValues(ProbandListEntryOutVO listEntry) {
		return getInquiryInputFieldValuesCached(listEntry, null, columnInquiryInputFieldId);
	}

	public String getAllTrialsInquiryValueLabel(InquiryValueOutVO inquiryValue, boolean allTrials) {
		InquiryOutVO inquiry = inquiryValue == null ? null : inquiryValue.getInquiry();
		if (inquiry != null) {
			String category = inquiry.getCategory();
			if (category != null && category.length() > 0) {
				if (allTrials) {
					return MessageFormat.format(CATEGORY_INQUIRY_LABEL_WITH_TRIAL_NAME, inquiry.getTrial().getName(), category, Long.toString(inquiry.getPosition()));
				} else {
					return MessageFormat.format(CATEGORY_INQUIRY_LABEL, category, Long.toString(inquiry.getPosition()));
				}
			} else {
				if (allTrials) {
					return MessageFormat.format(INQUIRY_LABEL_WITH_TRIAL_NAME, inquiry.getTrial().getName(), Long.toString(inquiry.getPosition()));
				} else {
					return MessageFormat.format(INQUIRY_LABEL, Long.toString(inquiry.getPosition()));
				}
			}
		}
		return "";
	}

	public Long getColumnInquiryId() {
		return columnInquiryId;
	}

	public Long getColumnInquiryInputFieldId() {
		return columnInquiryInputFieldId;
	}

	public Long getColumnProbandListEntryTagAId() {
		return columnProbandListEntryTagAId;
	}

	public Long getColumnProbandListEntryTagBId() {
		return columnProbandListEntryTagBId;
	}

	public Long getColumnProbandListEntryTagInputFieldAId() {
		return columnProbandListEntryTagInputFieldAId;
	}

	public Long getColumnProbandListEntryTagInputFieldBId() {
		return columnProbandListEntryTagInputFieldBId;
	}

	public ArrayList<SelectItem> getInquiries() {
		return inquiries;
	}

	public ArrayList<SelectItem> getInquiryInputFields() {
		return inquiryInputFields;
	}

	public Collection<InquiryValueOutVO> getInquiryInputFieldValues(ProbandListEntryOutVO listEntry) {
		TrialOutVO trial = listEntry.getTrial();
		if (trial != null) {
			return getInquiryInputFieldValuesCached(listEntry, trial.getId(), columnInquiryInputFieldId);
		}
		return new ArrayList<InquiryValueOutVO>();
	}

	private Collection<InquiryValueOutVO> getInquiryInputFieldValuesCached(ProbandListEntryOutVO listEntry, Long trialId, Long columnInquiryInputFieldId) {
		ProbandOutVO proband;
		if (columnInquiryInputFieldId != null && listEntry != null && (proband = listEntry.getProband()) != null) {
			HashMap<Long, HashMap<Long, Collection<InquiryValueOutVO>>> trialMap;
			if (inquiryValuesCache.containsKey(trialId)) {
				trialMap = inquiryValuesCache.get(trialId);
			} else {
				trialMap = new HashMap<Long, HashMap<Long, Collection<InquiryValueOutVO>>>();
				inquiryValuesCache.put(trialId, trialMap);
			}
			HashMap<Long, Collection<InquiryValueOutVO>> probandMap;
			if (trialMap.containsKey(proband.getId())) {
				probandMap = trialMap.get(proband.getId());
			} else {
				probandMap = new HashMap<Long, Collection<InquiryValueOutVO>>();
				trialMap.put(proband.getId(), probandMap);
			}
			if (probandMap.containsKey(columnInquiryInputFieldId)) {
				return probandMap.get(columnInquiryInputFieldId);
			} else {
				Collection<InquiryValueOutVO> result = new ArrayList<InquiryValueOutVO>();
				try {
					// long t1 = System.currentTimeMillis();
					result = WebUtil
							.getServiceLocator()
							.getProbandService()
							.getInquiryInputFieldValues(WebUtil.getAuthentication(), trialId,
									Settings.getBoolean(SettingCodes.PROBAND_LIST_INQUIRY_ACTIVE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_INQUIRY_ACTIVE), null,
									proband.getId(),
									columnInquiryInputFieldId);
					// WebUtil.perfDebug("getInquiryInputFieldValues", t1);
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				probandMap.put(columnInquiryInputFieldId, result);
				return result;
			}
		}
		return new ArrayList<InquiryValueOutVO>();
	}

	public InquiryValueOutVO getInquiryValue(ProbandListEntryOutVO listEntry) {
		return getInquiryValueCached(listEntry, columnInquiryId);
	}

	private InquiryValueOutVO getInquiryValueCached(ProbandListEntryOutVO listEntry, Long columnInquiryId) {
		ProbandOutVO proband;
		if (columnInquiryId != null && listEntry != null && (proband = listEntry.getProband()) != null) {
			HashMap<Long, InquiryValueOutVO> inquiryMap;
			if (inquiryValueCache.containsKey(proband.getId())) {
				inquiryMap = inquiryValueCache.get(proband.getId());
			} else {
				inquiryMap = new HashMap<Long, InquiryValueOutVO>();
				inquiryValueCache.put(proband.getId(), inquiryMap);
			}
			if (inquiryMap.containsKey(columnInquiryId)) {
				return inquiryMap.get(columnInquiryId);
			} else {
				InquiryValueOutVO result = null;
				try {
					// long t1 = System.currentTimeMillis();
					result = WebUtil.getServiceLocator().getProbandService().getInquiryValue(WebUtil.getAuthentication(), proband.getId(), columnInquiryId)
							.getPageValues().iterator()
							.next();
					// WebUtil.perfDebug("getInquiryValue", t1);
					// return result;
				} catch (NoSuchElementException e) {
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				inquiryMap.put(columnInquiryId, result);
				return result;
			}
		}
		return null;
	}

	public String getInquiryValueString(ProbandListEntryOutVO listEntry) {
		return inquiryValueToString(getInquiryValueCached(listEntry, columnInquiryId));
	}

	@Override
	protected Collection<ProbandListEntryOutVO> getLazyResult(PSFVO psf) {
		if (trialId != null || probandGroup != null || probandId != null) {
			try {
				if (probandGroup != null) {
					return WebUtil.getServiceLocator().getTrialService().getProbandListEntryList(WebUtil.getAuthentication(), null, probandGroup.getId(), null, total, psf);
				} else {
					return WebUtil.getServiceLocator().getTrialService().getProbandListEntryList(WebUtil.getAuthentication(), trialId, null, probandId, total, psf);
				}
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ProbandListEntryOutVO>();
	}

	public MoneyTransferSummaryVO getPayOffSummary(ProbandListEntryOutVO listEntry) {
		if (listEntry != null) {
			MoneyTransferSummaryVO summary;
			if (payOffSummaryCache.containsKey(listEntry.getId())) {
				summary = payOffSummaryCache.get(listEntry.getId());
			} else {
				summary = WebUtil.getProbandOpenReimbursementSummary(listEntry,
						Settings.getPaymentMethod(SettingCodes.PAYOFF_PAYMENT_METHOD, Bundle.SETTINGS, DefaultSettings.PAYOFF_PAYMENT_METHOD));
				payOffSummaryCache.put(listEntry.getId(), summary);
			}
			return summary;
		}
		return null;
	}

	public ProbandGroupOutVO getProbandGroup() {
		return probandGroup;
	}

	public Long getProbandId() {
		return probandId;
	}

	public ProbandListEntryTagValueOutVO getProbandListEntryTagAValue(ProbandListEntryOutVO listEntry) {
		return getProbandListEntryTagValueCached(listEntry, columnProbandListEntryTagAId);
	}

	public String getProbandListEntryTagAValueString(ProbandListEntryOutVO listEntry) {
		return probandListEntryTagValueToString(getProbandListEntryTagValueCached(listEntry, columnProbandListEntryTagAId));
	}

	public ProbandListEntryTagValueOutVO getProbandListEntryTagBValue(ProbandListEntryOutVO listEntry) {
		return getProbandListEntryTagValueCached(listEntry, columnProbandListEntryTagBId);
	}

	public String getProbandListEntryTagBValueString(ProbandListEntryOutVO listEntry) {
		return probandListEntryTagValueToString(getProbandListEntryTagValueCached(listEntry, columnProbandListEntryTagBId));
	}

	public Collection<ProbandListEntryTagValueOutVO> getProbandListEntryTagInputFieldAValues(ProbandListEntryOutVO listEntry) {
		return getProbandListEntryTagInputFieldValuesCached(listEntry, columnProbandListEntryTagInputFieldAId);
	}

	public Collection<ProbandListEntryTagValueOutVO> getProbandListEntryTagInputFieldBValues(ProbandListEntryOutVO listEntry) {
		return getProbandListEntryTagInputFieldValuesCached(listEntry, columnProbandListEntryTagInputFieldBId);
	}

	public ArrayList<SelectItem> getProbandListEntryTagInputFields() {
		return probandListEntryTagInputFields;
	}

	private Collection<ProbandListEntryTagValueOutVO> getProbandListEntryTagInputFieldValuesCached(ProbandListEntryOutVO listEntry, Long columnProbandListEntryTagInputFieldId) {
		if (listEntry != null && columnProbandListEntryTagInputFieldId != null) {
			HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> listEntryMap;
			if (probandListEntryTagValuesCache.containsKey(listEntry.getId())) {
				listEntryMap = probandListEntryTagValuesCache.get(listEntry.getId());
			} else {
				listEntryMap = new HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>();
				probandListEntryTagValuesCache.put(listEntry.getId(), listEntryMap);
			}
			if (listEntryMap.containsKey(columnProbandListEntryTagInputFieldId)) {
				return listEntryMap.get(columnProbandListEntryTagInputFieldId);
			} else {
				Collection<ProbandListEntryTagValueOutVO> result = new ArrayList<ProbandListEntryTagValueOutVO>();
				try {
					result = WebUtil.getServiceLocator().getTrialService()
							.getProbandListEntryTagInputFieldValues(WebUtil.getAuthentication(), listEntry.getId(), columnProbandListEntryTagInputFieldId);
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				listEntryMap.put(columnProbandListEntryTagInputFieldId, result);
				return result;
			}
		}
		return new ArrayList<ProbandListEntryTagValueOutVO>();
	}

	public ArrayList<SelectItem> getProbandListEntryTags() {
		return probandListEntryTags;
	}

	private ProbandListEntryTagValueOutVO getProbandListEntryTagValueCached(ProbandListEntryOutVO listEntry, Long columnProbandListEntryTagId) {
		if (listEntry != null && columnProbandListEntryTagId != null) {
			HashMap<Long, ProbandListEntryTagValueOutVO> listEntryMap;
			if (probandListEntryTagValueCache.containsKey(listEntry.getId())) {
				listEntryMap = probandListEntryTagValueCache.get(listEntry.getId());
			} else {
				listEntryMap = new HashMap<Long, ProbandListEntryTagValueOutVO>();
				probandListEntryTagValueCache.put(listEntry.getId(), listEntryMap);
			}
			if (listEntryMap.containsKey(columnProbandListEntryTagId)) {
				return listEntryMap.get(columnProbandListEntryTagId);
			} else {
				ProbandListEntryTagValueOutVO result = null;
				try {
					result = WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagValue(WebUtil.getAuthentication(), listEntry.getId(), columnProbandListEntryTagId)
							.getPageValues().iterator().next();
				} catch (NoSuchElementException e) {
				} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				listEntryMap.put(columnProbandListEntryTagId, result);
				return result;
			}
		}
		return null;
	}

	public String getProbandListEntryTagValueLabel(ProbandListEntryTagValueOutVO listEntryTagValue) {
		return getProbandListEntryTagLabel(listEntryTagValue == null ? null : listEntryTagValue.getTag(), false);
	}

	@Override
	protected ProbandListEntryOutVO getRowElement(Long id) {
		return WebUtil.getProbandListEntry(id);
	}

	public Long getTrialId() {
		return trialId;
	}

	public void initSets(boolean reset) {
		payOffSummaryCache.clear();
		probandListEntryTagValuesCache.clear();
		probandListEntryTagValueCache.clear();
		Collection<ProbandListEntryTagOutVO> probandListEntryTagVOs = null;
		if (showProbandListEntryTagColumn && (trialId != null || probandId != null)) {
			try {
				probandListEntryTagVOs = WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagList(WebUtil.getAuthentication(), trialId, probandId, null);
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (probandListEntryTagVOs != null) {
			probandListEntryTags = new ArrayList<SelectItem>(probandListEntryTagVOs.size());
			probandListEntryTagInputFields = new ArrayList<SelectItem>(probandListEntryTagVOs.size());
			HashSet<Long> dupeCheck = new HashSet<Long>(probandListEntryTagVOs.size());
			Iterator<ProbandListEntryTagOutVO> it = probandListEntryTagVOs.iterator();
			while (it.hasNext()) {
				ProbandListEntryTagOutVO probandListEntryTagVO = it.next();
				if (dupeCheck.add(probandListEntryTagVO.getField().getId())) {
					probandListEntryTagInputFields.add(new SelectItem(Long.toString(probandListEntryTagVO.getField().getId()), probandListEntryTagVO.getField().getName()));
				}
				probandListEntryTags.add(new SelectItem(Long.toString(probandListEntryTagVO.getId()), getProbandListEntryTagLabel(probandListEntryTagVO, true)));
			}
		} else {
			probandListEntryTags = new ArrayList<SelectItem>();
			probandListEntryTagInputFields = new ArrayList<SelectItem>();
		}
		if (reset) {
			inquiryValuesCache.clear();
			inquiryValueCache.clear();
			Collection<InquiryOutVO> inquiryVOs = null;
			if (showInquiryColumn) {
				// long t1 = System.currentTimeMillis();
				if (probandId != null) {
					try {
						inquiryVOs = WebUtil
								.getServiceLocator()
								.getTrialService()
								.getInquiryList(WebUtil.getAuthentication(), trialId,
										Settings.getBoolean(SettingCodes.PROBAND_LIST_INQUIRY_ACTIVE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_INQUIRY_ACTIVE), null,
										probandId);
					} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
					} catch (AuthenticationException e) {
						WebUtil.publishException(e);
					}
					// WebUtil.perfDebug("getInquiryList", t1);
				} else if (trialId != null) {
					try {
						inquiryVOs = WebUtil
								.getServiceLocator()
								.getTrialService()
								.getInquiryList(WebUtil.getAuthentication(), trialId,
										Settings.getBoolean(SettingCodes.PROBAND_LIST_INQUIRY_ACTIVE, Bundle.SETTINGS, DefaultSettings.PROBAND_LIST_INQUIRY_ACTIVE), null);
					} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
					} catch (AuthenticationException e) {
						WebUtil.publishException(e);
					}
					// WebUtil.perfDebug("getInquiryList", t1);
				}
			}
			if (inquiryVOs != null) {
				inquiries = new ArrayList<SelectItem>(inquiryVOs.size());
				inquiryInputFields = new ArrayList<SelectItem>(inquiryVOs.size());
				HashSet<Long> dupeCheck = new HashSet<Long>(inquiryVOs.size());
				Iterator<InquiryOutVO> it = inquiryVOs.iterator();
				while (it.hasNext()) {
					InquiryOutVO inquiryVO = it.next();
					if (dupeCheck.add(inquiryVO.getField().getId())) {
						inquiryInputFields.add(new SelectItem(Long.toString(inquiryVO.getField().getId()), inquiryVO.getField().getName()));
					}
					inquiries.add(new SelectItem(Long.toString(inquiryVO.getId()), getInquiryLabel(inquiryVO, true)));
				}
			} else {
				inquiries = new ArrayList<SelectItem>();
				inquiryInputFields = new ArrayList<SelectItem>();
			}
		}
	}

	public String inquiryValueToString(InquiryValueOutVO inquiryValue) {
		if (inquiryValue != null) {
			return (new InquiryValueOutVOStringAdapter(Settings.getInt(SettingCodes.PROBAND_LIST_INQUIRY_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
					DefaultSettings.PROBAND_LIST_INQUIRY_VALUE_TEXT_CLIP_MAX_LENGTH))).toString(inquiryValue);
		}
		return null;
	}

	public boolean isInquiryBoolean(ProbandListEntryOutVO listEntry) {
		if (columnInquiry != null) {
			return isFieldTypeBoolean(columnInquiry.getField().getFieldType());
		}
		return false;
	}

	public boolean isInquiryValueBoolean(InquiryValueOutVO inquiryValue) {
		if (inquiryValue != null) {
			return isFieldTypeBoolean(inquiryValue.getInquiry().getField().getFieldType());
		}
		return false;
	}

	public boolean isProbandListEntryTagABoolean(ProbandListEntryOutVO listEntry) {
		if (columnProbandListEntryTagA != null) {
			return isFieldTypeBoolean(columnProbandListEntryTagA.getField().getFieldType());
		}
		return false;
	}

	public boolean isProbandListEntryTagBBoolean(ProbandListEntryOutVO listEntry) {
		if (columnProbandListEntryTagB != null) {
			return isFieldTypeBoolean(columnProbandListEntryTagB.getField().getFieldType());
		}
		return false;
	}

	public boolean isProbandListEntryTagValueBoolean(ProbandListEntryTagValueOutVO listEntryTagValue) {
		if (listEntryTagValue != null) {
			return isFieldTypeBoolean(listEntryTagValue.getTag().getField().getFieldType());
		}
		return false;
	}

	public boolean isShowInquiryColumn() {
		return showInquiryColumn;
	}

	public boolean isShowProbandListEntryTagColumn() {
		return showProbandListEntryTagColumn;
	}

	public boolean isUseReasonEncryption() {
		return CommonUtil.ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON;
	}

	public String probandListEntryTagValueToString(ProbandListEntryTagValueOutVO listEntryTagValue) {
		if (listEntryTagValue != null) {
			return (new ProbandListEntryTagValueOutVOStringAdapter(Settings.getInt(SettingCodes.PROBAND_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
					DefaultSettings.PROBAND_LIST_PROBAND_LIST_ENTRY_TAG_VALUE_TEXT_CLIP_MAX_LENGTH))).toString(listEntryTagValue);
		}
		return null;
	}

	@Override
	public void resetRows() {
		allRows = null;
	}

	public void setColumnInquiryId(Long inquiryId) {
		this.columnInquiryId = inquiryId;
		columnInquiry = WebUtil.getInquiry(inquiryId);
	}

	public void setColumnInquiryInputFieldId(Long inputFieldId) {
		this.columnInquiryInputFieldId = inputFieldId;
	}

	public void setColumnProbandListEntryTagAId(Long probandListEntryTagId) {
		this.columnProbandListEntryTagAId = probandListEntryTagId;
		columnProbandListEntryTagA = WebUtil.getProbandListEntryTag(probandListEntryTagId);
	}

	public void setColumnProbandListEntryTagBId(Long probandListEntryTagId) {
		this.columnProbandListEntryTagBId = probandListEntryTagId;
		columnProbandListEntryTagB = WebUtil.getProbandListEntryTag(probandListEntryTagId);
	}

	public void setColumnProbandListEntryTagInputFieldAId(Long inputFieldId) {
		this.columnProbandListEntryTagInputFieldAId = inputFieldId;
	}

	public void setColumnProbandListEntryTagInputFieldBId(Long inputFieldId) {
		this.columnProbandListEntryTagInputFieldBId = inputFieldId;
	}

	public void setProbandGroup(ProbandGroupOutVO probandGroup) {
		this.probandGroup = probandGroup;
		this.trialId = (probandGroup == null ? null : probandGroup.getTrial().getId());
		this.probandId = null;
		// initSets();
	}

	public void setProbandId(Long probandId) {
		this.probandId = probandId;
		this.trialId = null;
		this.probandGroup = null;
		// initSets();
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
		this.probandId = null;
		this.probandGroup = null;
		// initSets();
	}
}
