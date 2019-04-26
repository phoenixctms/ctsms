package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuditTrailExcelVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValuesOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.ECRFSectionProgressVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.Paginator;
import org.phoenixctms.ctsms.web.model.shared.inputfield.EcrfFieldInputModel;
import org.phoenixctms.ctsms.web.model.shared.inputfield.EcrfFieldInputModelList;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class EcrfFieldValueBean extends ManagedBeanBase {

	private static int MAX_INPUT_FIELDS_PER_ROW = 10;

	public static void copyEcrfFieldValueOutToIn(ECRFFieldValueInVO in, ECRFFieldValueOutVO out) {
		if (in != null && out != null) {
			ProbandListEntryOutVO listEntryVO = out.getListEntry();
			ECRFFieldOutVO ecrfFieldVO = out.getEcrfField();
			Collection<InputFieldSelectionSetValueOutVO> ecrfFieldValueVOs = out.getSelectionValues();
			in.setBooleanValue(out.getBooleanValue());
			in.setDateValue(out.getDateValue());
			in.setTimeValue(out.getTimeValue());
			in.setFloatValue(out.getFloatValue());
			in.setId(out.getId());
			in.setIndex(out.getIndex());
			in.setReasonForChange(out.getId() == null || ecrfFieldVO == null || !ecrfFieldVO.getAuditTrail() ? null
					: Messages
							.getString(MessageCodes.ECRF_FIELD_VALUE_REASON_FOR_CHANGE_PRESET));
			in.setListEntryId(listEntryVO == null ? null : listEntryVO.getId());
			in.setLongValue(out.getLongValue());
			ArrayList<Long> selectionValueIds = new ArrayList<Long>(ecrfFieldValueVOs.size());
			Iterator<InputFieldSelectionSetValueOutVO> ecrfFieldValueVOsIt = ecrfFieldValueVOs.iterator();
			while (ecrfFieldValueVOsIt.hasNext()) {
				selectionValueIds.add(ecrfFieldValueVOsIt.next().getId());
			}
			in.setSelectionValueIds(selectionValueIds);
			in.setEcrfFieldId(ecrfFieldVO == null ? null : ecrfFieldVO.getId());
			in.setTextValue(out.getTextValue());
			in.setTimestampValue(out.getTimestampValue());
			in.setInkValues(out.getInkValues());
			in.setVersion(out.getVersion());
		}
	}

	public static void copyEcrfFieldValuesInToJson(ArrayList<ECRFFieldValueJsonVO> ecrfFieldValuesOut, ArrayList<ECRFFieldValueInVO> ecrfFieldValuesIn,
			HashMap<Long, ECRFFieldOutVO> ecrfFieldVOsMap) {
		if (ecrfFieldValuesOut != null) {
			ecrfFieldValuesOut.clear();
			if (ecrfFieldValuesIn != null && ecrfFieldVOsMap != null) {
				ecrfFieldValuesOut.ensureCapacity(ecrfFieldValuesIn.size());
				Iterator<ECRFFieldValueInVO> it = ecrfFieldValuesIn.iterator();
				while (it.hasNext()) {
					ECRFFieldValueInVO in = it.next();
					if (ecrfFieldVOsMap.containsKey(in.getEcrfFieldId())) {
						ECRFFieldValueJsonVO out = new ECRFFieldValueJsonVO();
						CommonUtil.copyEcrfFieldValueInToJson(out, in, ecrfFieldVOsMap.get(in.getEcrfFieldId()));
						ecrfFieldValuesOut.add(out);
					}
				}
			}
		}
	}

	public static void copyEcrfFieldValuesOutToIn(ArrayList<ECRFFieldValueInVO> ecrfFieldValuesIn, Collection<ECRFFieldValueOutVO> ecrfFieldValuesOut,
			boolean keepEcrfFieldValuesIn) {
		if (ecrfFieldValuesIn != null) {
			HashMap<Long, HashMap<Long, ECRFFieldValueInVO>> ecrfFieldValuesInMap = new HashMap<Long, HashMap<Long, ECRFFieldValueInVO>>(ecrfFieldValuesIn.size());
			if (keepEcrfFieldValuesIn) {
				Iterator<ECRFFieldValueInVO> it = ecrfFieldValuesIn.iterator();
				while (it.hasNext()) {
					ECRFFieldValueInVO in = it.next();
					HashMap<Long, ECRFFieldValueInVO> indexMap;
					if (ecrfFieldValuesInMap.containsKey(in.getEcrfFieldId())) {
						indexMap = ecrfFieldValuesInMap.get(in.getEcrfFieldId());
					} else {
						indexMap = new HashMap<Long, ECRFFieldValueInVO>();
						ecrfFieldValuesInMap.put(in.getEcrfFieldId(), indexMap);
					}
					indexMap.put(in.getIndex(), in);
				}
			}
			ecrfFieldValuesIn.clear();
			if (ecrfFieldValuesOut != null) {
				ecrfFieldValuesIn.ensureCapacity(ecrfFieldValuesOut.size());
				Iterator<ECRFFieldValueOutVO> it = ecrfFieldValuesOut.iterator();
				while (it.hasNext()) {
					ECRFFieldValueOutVO out = it.next();
					ECRFFieldValueInVO in;
					if (ecrfFieldValuesInMap.containsKey(out.getEcrfField().getId()) && ecrfFieldValuesInMap.get(out.getEcrfField().getId()).containsKey(out.getIndex())) {
						in = ecrfFieldValuesInMap.get(out.getEcrfField().getId()).get(out.getIndex());
					} else {
						in = new ECRFFieldValueInVO();
						copyEcrfFieldValueOutToIn(in, out);
					}
					ecrfFieldValuesIn.add(in);
				}
			}
		}
	}

	// private Long probandListEntryId;
	private ProbandListEntryOutVO probandListEntry;
	// private Long ecrfId;
	private ECRFOutVO ecrf;
	private ECRFStatusEntryVO ecrfStatus;
	// private Collection<ProbandAddressOutVO> probandAddresses;
	private Collection<VisitScheduleItemOutVO> visitScheduleItems;
	private Collection<ProbandGroupOutVO> probandGroups;
	private Collection<ProbandListEntryTagValueJsonVO> probandListEntryTagValues;
	private Paginator paginator;
	private ArrayList<ECRFFieldValueInVO> ecrfFieldValuesIn;
	private Collection<ECRFFieldValueOutVO> ecrfFieldValuesOut;
	private Collection<ECRFFieldValueJsonVO> jsEcrfFieldValuesOut;
	private HashMap<EcrfFieldSection, EcrfFieldInputModelList> inputModelsMap;
	private HashMap<EcrfFieldSection, Boolean> sectionCollapsedMap;
	private HashMap<EcrfFieldSection, Boolean> sectionCreatedMap;
	private HashMap<EcrfFieldSection, Boolean> sectionUnlockValueMap;
	private Collection<EcrfFieldSection> sections;
	private HashMap<EcrfFieldSection, List<Object[]>> paddedInputModelsMap;
	private String deltaErrorMessageId;
	private String filterSection;
	private ECRFSectionProgressVO filterSectionProgress;
	private Long filterIndex;

	public EcrfFieldValueBean() {
		super();
		// probandListEntryId = null;
		probandListEntry = null;
		// ecrfId = null;
		ecrf = null;
		ecrfStatus = null;
		sections = new ArrayList<EcrfFieldSection>();
		inputModelsMap = new HashMap<EcrfFieldSection, EcrfFieldInputModelList>();
		sectionCollapsedMap = new HashMap<EcrfFieldSection, Boolean>();
		sectionCreatedMap = new HashMap<EcrfFieldSection, Boolean>();
		sectionUnlockValueMap = new HashMap<EcrfFieldSection, Boolean>();
		paddedInputModelsMap = new HashMap<EcrfFieldSection, List<Object[]>>();
		filterSection = null;
		filterSectionProgress = null;
		filterIndex = null;
		deltaErrorMessageId = null;
		paginator = new Paginator(this) {

			@Override
			protected Long getCount(Long... ids) {
				if (ids[0] != null && ids[1] != null) {
					if (CommonUtil.isEmptyString(((EcrfFieldValueBean) bean).getFilterSection())) {
						try {
							return WebUtil.getServiceLocator().getTrialService().getEcrfFieldValueCount(WebUtil.getAuthentication(), ids[0], ids[1]);
						} catch (AuthenticationException e) {
							WebUtil.publishException(e);
						} catch (AuthorisationException e) {
						} catch (ServiceException e) {
						} catch (IllegalArgumentException e) {
						}
					} else {
						try {
							return WebUtil.getServiceLocator().getTrialService()
									.getEcrfFieldValueCount(WebUtil.getAuthentication(), ids[0], ((EcrfFieldValueBean) bean).getFilterSection(),
											((EcrfFieldValueBean) bean).getFilterIndex(), ids[1]);
						} catch (AuthenticationException e) {
							WebUtil.publishException(e);
						} catch (AuthorisationException e) {
						} catch (ServiceException e) {
						} catch (IllegalArgumentException e) {
						}
					}
				}
				return null;
			}

			@Override
			protected int getDefaultFieldsPerRow() {
				return Settings.getInt(SettingCodes.ECRF_FIELD_VALUES_DEFAULT_FIELDS_PER_ROW, Bundle.SETTINGS,
						DefaultSettings.ECRF_FIELD_VALUES_DEFAULT_FIELDS_PER_ROW);
			}

			@Override
			protected int getDefaultPageSize() {
				return Settings.getInt(SettingCodes.ECRF_FIELD_VALUES_DEFAULT_PAGE_SIZE, Bundle.SETTINGS,
						DefaultSettings.ECRF_FIELD_VALUES_DEFAULT_PAGE_SIZE);
			}

			@Override
			protected String getFirstPageButtonLabel() {
				ECRFSectionProgressVO sectionProgress = ((EcrfFieldValueBean) bean).getFilterSectionProgress();
				if (sectionProgress != null && sectionProgress.getSeries()) {
					return MessageCodes.ECRF_FIELD_VALUES_FIRST_PAGE_INDEX_BUTTON_LABEL;
				}
				return MessageCodes.ECRF_FIELD_VALUES_FIRST_PAGE_BUTTON_LABEL;
			}

			@Override
			protected String getLoadLabel() {
				return MessageCodes.ECRF_FIELD_VALUES_LOAD_LABEL;
			}

			@Override
			protected int getMaxFieldsPerRow() {
				return MAX_INPUT_FIELDS_PER_ROW;
				// return Settings.getInt(SettingCodes.ECRF_FIELD_VALUES_MAX_FIELDS_PER_ROW, Bundle.SETTINGS,
				// DefaultSettings.ECRF_FIELD_VALUES_MAX_FIELDS_PER_ROW);
			}

			@Override
			protected String getPageButtonLabel() {
				ECRFSectionProgressVO sectionProgress = ((EcrfFieldValueBean) bean).getFilterSectionProgress();
				if (sectionProgress != null && sectionProgress.getSeries()) {
					return MessageCodes.ECRF_FIELD_VALUES_PAGE_INDEX_BUTTON_LABEL;
				}
				return MessageCodes.ECRF_FIELD_VALUES_PAGE_BUTTON_LABEL;
			}

			@Override
			protected String getPageButtonLabelSeriesIndex(int i, int pageSize) {
				ECRFSectionProgressVO sectionProgress = ((EcrfFieldValueBean) bean).getFilterSectionProgress();
				if (sectionProgress != null && sectionProgress.getSeries() && sectionProgress.getFieldCount() > 0l) {
					long pageStart = (long) (i * pageSize + 1);
					long pageEnd = (long) ((i + 1) * pageSize);
					StringBuilder sb = new StringBuilder();
					Long lastIndex = null;
					long maxIndex = (sectionProgress.getIndex() != null ? sectionProgress.getIndex() : 0l);
					for (long j = pageStart; j <= pageEnd; j = j + 1l) {
						long index = (long) Math.floor((double) (j - 1l) / (double) sectionProgress.getFieldCount());
						if (index > maxIndex) {
							break;
						}
						if (lastIndex == null || !lastIndex.equals(index)) {
							if (sb.length() > 0) {
								sb.append(", ");
							}
							sb.append(Long.toString(index));
							lastIndex = index;
						}
					}
					return sb.toString();
				}
				return null;
			}

			@Override
			protected ArrayList<String> getPageSizeStrings() {
				return Settings.getStringList(SettingCodes.ECRF_FIELD_VALUES_PAGE_SIZES, Bundle.SETTINGS,
						DefaultSettings.ECRF_FIELD_VALUES_PAGE_SIZES);
			}

			@Override
			public boolean isPagesEnabled() {
				return this.psf.getRowCount() != null && this.psf.getRowCount() > 0l;
			}
		};
		this.change();
	}

	@Override
	public void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			// long t = System.currentTimeMillis();
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			if (ecrfFieldValuesIn != null && ecrfFieldValuesOut != null && jsEcrfFieldValuesOut != null && jsEcrfFieldValuesOut.size() > 0) {
				ProbandListEntryOutVO listEntry = null;
				Collection<ProbandListEntryTagValueJsonVO> tagValues = null;
				Collection<VisitScheduleItemOutVO> visitSchedule = null;
				Collection<ProbandGroupOutVO> groups = null;
				ArrayList<ECRFFieldValueJsonVO> out = new ArrayList<ECRFFieldValueJsonVO>(jsEcrfFieldValuesOut.size());
				// prepare ecrfFieldVOsMap of js variables
				HashMap<Long, ECRFFieldOutVO> ecrfFieldVOsMap = new HashMap<Long, ECRFFieldOutVO>(ecrfFieldValuesOut.size());
				HashMap<Long, HashSet<Long>> visibleIndexMap = new HashMap<Long, HashSet<Long>>(ecrfFieldValuesOut.size());
				Iterator<ECRFFieldValueOutVO> ecrfFieldIt = ecrfFieldValuesOut.iterator();
				while (ecrfFieldIt.hasNext()) {
					ECRFFieldValueOutVO ecrfFieldValueVO = ecrfFieldIt.next();
					listEntry = ecrfFieldValueVO.getListEntry();
					ECRFFieldOutVO ecrfFieldVO = ecrfFieldValueVO.getEcrfField();
					if (!CommonUtil.isEmptyString(ecrfFieldVO.getJsVariableName())) {
						ecrfFieldVOsMap.put(ecrfFieldVO.getId(), ecrfFieldVO);
						if (visibleIndexMap.containsKey(ecrfFieldVO.getId())) {
							visibleIndexMap.get(ecrfFieldVO.getId()).add(ecrfFieldValueVO.getIndex());
						} else {
							HashSet<Long> visibleIndexes = new HashSet<Long>();
							visibleIndexes.add(ecrfFieldValueVO.getIndex());
							visibleIndexMap.put(ecrfFieldVO.getId(), visibleIndexes);
						}
					}
				}
				// actual user inputs to jsonVOs:
				copyEcrfFieldValuesInToJson(out, ecrfFieldValuesIn, ecrfFieldVOsMap);
				// add non visible values from jsEcrfFieldValuesOut:
				Iterator<ECRFFieldValueJsonVO> jsEcrfFieldValueIt = jsEcrfFieldValuesOut.iterator();
				while (jsEcrfFieldValueIt.hasNext()) {
					ECRFFieldValueJsonVO jsEcrfFieldValue = jsEcrfFieldValueIt.next();
					if (!visibleIndexMap.containsKey(jsEcrfFieldValue.getEcrfFieldId())
							|| !visibleIndexMap.get(jsEcrfFieldValue.getEcrfFieldId()).contains(jsEcrfFieldValue.getIndex())) {
						out.add(jsEcrfFieldValue);
					}
				}
				// if (listEntry != null) {
				// if (probandListEntry != null) {
				// if (listEntry.getProband().getId() != probandListEntry.getProband().getId()) {
				// // addresses = loadProbandAddresses(listEntry.getProband().getId());
				// tagValues = loadTagValues(listEntry);
				// visitSchedule = loadVisitScheduleItems(listEntry);
				// } else {
				// // addresses = probandAddresses;
				// visitSchedule = visitScheduleItems;
				// }
				// } else {
				// // addresses = loadProbandAddresses(listEntry.getProband().getId());
				// visitSchedule = loadVisitScheduleItems(listEntry);
				// }
				// }
				if (listEntry != null) {
					if (probandListEntry != null) {
						if (listEntry.getId() != probandListEntry.getId()) {
							// addresses = loadProbandAddresses(listEntry.getProband().getId());
							tagValues = loadProbandListEntryTagValues(listEntry);
							visitSchedule = loadVisitScheduleItems(listEntry);
							groups = loadProbandGroups(probandListEntry);
						} else {
							// addresses = probandAddresses;
							tagValues = probandListEntryTagValues;
							visitSchedule = visitScheduleItems;
							groups = probandGroups;
						}
					} else {
						// addresses = loadProbandAddresses(listEntry.getProband().getId());
						tagValues = loadProbandListEntryTagValues(listEntry);
						visitSchedule = loadVisitScheduleItems(listEntry);
						groups = loadProbandGroups(probandListEntry);
					}
				}
				if (!CommonUtil.isEmptyString(deltaErrorMessageId)) {
					requestContext.addCallbackParam(JSValues.AJAX_FIELD_DELTA_ERROR_MESSAGE_ID.toString(), deltaErrorMessageId);
				}
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64.toString(),
						JsUtil.encodeBase64(JsUtil.inputFieldVariableValueToJson(out), false));
				// requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_PROBAND_BASE64.toString(),
				// JsUtil.encodeBase64(JsUtil.voToJson(listEntry != null ? listEntry.getProband() : null), false));
				// requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_TRIAL_BASE64.toString(),
				// JsUtil.encodeBase64(JsUtil.voToJson(listEntry != null ? listEntry.getTrial() : null), false));
				// requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(addresses), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_TAG_VALUES_BASE64.toString(),
						JsUtil.encodeBase64(JsUtil.inputFieldVariableValueToJson(tagValues), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_BASE64.toString(),
						JsUtil.encodeBase64(JsUtil.voToJson(listEntry), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_VISIT_SCHEDULE_ITEMS_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(visitSchedule), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_PROBAND_GROUPS_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(groups), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_ACTIVE_USER_BASE64.toString(),
						JsUtil.encodeBase64(JsUtil.voToJson(WebUtil.getUser()), false));
			}
			// t = WebUtil.perfDebug("appendcontext valuebean: ", t);
		}
	}

	@Override
	protected String changeAction(Long id) {
		ecrfFieldValuesOut = null;
		jsEcrfFieldValuesOut = null;
		// this.ecrfId = id;
		this.ecrf = WebUtil.getEcrf(id);
		// this.probandListEntryId = id;
		initPages(true);
		initIn(true, false);
		initSets();
		return CHANGE_OUTCOME;
	}

	private ArrayList<ECRFFieldValueInVO> clearFromEcrfFieldValuesIn(EcrfFieldSection sectionToRemove) {
		ArrayList<ECRFFieldValueInVO> result = new ArrayList<ECRFFieldValueInVO>(ecrfFieldValuesIn.size());
		if (sectionToRemove != null) {
			Set<ECRFFieldValueInVO> sectionIn = inputModelsMap.get(sectionToRemove).getEcrfFieldValues();
			HashSet<Long> sectionEcrfIds = new HashSet<Long>(sectionIn.size());
			Iterator<ECRFFieldValueInVO> sectionInIt = sectionIn.iterator();
			while (sectionInIt.hasNext()) {
				sectionEcrfIds.add(sectionInIt.next().getEcrfFieldId());
			}
			Iterator<ECRFFieldValueInVO> ecrfFieldValuesInIt = ecrfFieldValuesIn.iterator();
			while (ecrfFieldValuesInIt.hasNext()) {
				ECRFFieldValueInVO in = ecrfFieldValuesInIt.next();
				if (sectionEcrfIds.contains(in.getEcrfFieldId())) {
					if (in.getIndex() == null) {
						if (sectionToRemove.getSeriesIndex() != null) {
							result.add(in);
						}
					} else if (!in.getIndex().equals(sectionToRemove.getSeriesIndex())) {
						result.add(in);
					}
				} else {
					result.add(in);
				}
			}
		} else {
			result.addAll(ecrfFieldValuesIn);
		}
		return result;
	}

	private void clearInputModelErrorMsgs() {
		Iterator<EcrfFieldInputModelList> modelListIt = inputModelsMap.values().iterator();
		while (modelListIt.hasNext()) {
			Iterator<EcrfFieldInputModel> modelIt = modelListIt.next().iterator();
			while (modelIt.hasNext()) {
				modelIt.next().setErrorMessage(null);
			}
		}
	}

	private void clearInputModelModifiedAnnotations() {
		Iterator<EcrfFieldInputModelList> modelListIt = inputModelsMap.values().iterator();
		while (modelListIt.hasNext()) {
			Iterator<EcrfFieldInputModel> modelIt = modelListIt.next().iterator();
			while (modelIt.hasNext()) {
				modelIt.next().setModifiedAnnotation(null);
			}
		}
	}

	// public void delete() {
	// actionPostProcess(deleteAction());
	// }
	@Override
	public String deleteAction() {
		try {
			// PSFVO psf = paginator.getPsfCopy(true);
			Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
			Long ecrfId = ecrf == null ? null : ecrf.getId();
			if (CommonUtil.isEmptyString(filterSection)) {
				WebUtil.getServiceLocator().getTrialService().clearEcrfFieldValues(WebUtil.getAuthentication(), ecrfId, listEntryId);
			} else {
				WebUtil.getServiceLocator().getTrialService().clearEcrfFieldValues(WebUtil.getAuthentication(), ecrfId, filterSection, listEntryId);
			}
			portionEcrfFieldValues(null);
			initPages(true);
			initIn(true, false);
			initSets();
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException e) {
			// setInputModelErrorMsgs(e.getData());
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

	public StreamedContent getAuditTrailExcelStreamedContent() throws Exception {
		try {
			Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
			Long ecrfId = ecrf == null ? null : ecrf.getId();
			AuditTrailExcelVO auditTrailExcel = WebUtil.getServiceLocator().getTrialService().exportAuditTrail(WebUtil.getAuthentication(), null, listEntryId, ecrfId);
			return new DefaultStreamedContent(new ByteArrayInputStream(auditTrailExcel.getDocumentDatas()), auditTrailExcel.getContentType().getMimeType(),
					auditTrailExcel.getFileName());
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

	public ECRFOutVO getEcrf() {
		return ecrf;
	}

	private Set<ECRFFieldValueInVO> getEcrfFieldInValues(boolean excludeNewSeries) {
		HashSet<ECRFFieldValueInVO> in;
		if (excludeNewSeries) {
			in = new HashSet<ECRFFieldValueInVO>(ecrfFieldValuesIn.size());
			Iterator<EcrfFieldSection> sectionsIt = sections.iterator();
			while (sectionsIt.hasNext()) {
				EcrfFieldSection ecrfFieldSection = sectionsIt.next();
				if (!ecrfFieldSection.isSeries()) {
					in.addAll(inputModelsMap.get(ecrfFieldSection).getEcrfFieldValues());
				} else if (sectionCreatedMap.get(ecrfFieldSection)) {
					in.addAll(inputModelsMap.get(ecrfFieldSection).getEcrfFieldValues());
				}
			}
		} else {
			in = new HashSet<ECRFFieldValueInVO>(ecrfFieldValuesIn);
		}
		return in;
	}

	private Set<ECRFFieldValueInVO> getEcrfFieldInValues(EcrfFieldSection section) {
		return inputModelsMap.get(section).getEcrfFieldValues();
	}

	public StreamedContent getEcrfPdfStreamedContent(boolean blank, boolean section) throws Exception {
		try {
			Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
			Long ecrfId = ecrf == null ? null : ecrf.getId();
			ECRFPDFVO ecrfPdf;
			if (!section || CommonUtil.isEmptyString(filterSection)) {
				ecrfPdf = WebUtil.getServiceLocator().getTrialService().renderEcrfs(WebUtil.getAuthentication(), null, listEntryId, ecrfId, blank);
			} else {
				ecrfPdf = WebUtil.getServiceLocator().getTrialService().renderEcrf(WebUtil.getAuthentication(), ecrfId, filterSection, listEntryId, blank);
			}
			return new DefaultStreamedContent(new ByteArrayInputStream(ecrfPdf.getDocumentDatas()), ecrfPdf.getContentType().getMimeType(), ecrfPdf.getFileName());
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

	public ECRFStatusEntryVO getEcrfStatus() {
		return ecrfStatus;
	}

	public boolean getEditable(boolean unlockValue) {
		if (probandListEntry == null || ecrf == null) {
			return false;
		} else if (ecrfFieldValuesIn.size() == 0) {
			return false;
		} else if (WebUtil.isTrialLocked(probandListEntry)) {
			return false;
		} else if (WebUtil.isProbandLocked(probandListEntry)) {
			return false;
		} else if (!probandListEntry.getTrial().getStatus().getEcrfValueInputEnabled()) {
			return false;
		} else if (probandListEntry.getLastStatus() != null && !probandListEntry.getLastStatus().getStatus().getEcrfValueInputEnabled()) {
			return false;
		} else if (ecrfStatus != null && ecrfStatus.getStatus().getValueLockdown()) {
			return unlockValue;
		}
		return true;
	}

	public Long getFilterIndex() {
		return filterIndex;
	}

	public String getFilterSection() {
		return filterSection;
	}

	public ECRFSectionProgressVO getFilterSectionProgress() {
		return filterSectionProgress;
	}

	public HashMap<EcrfFieldSection, List<Object[]>> getInputModelsMap() {
		return paddedInputModelsMap;
	}

	@Override
	public String getModifiedAnnotation() {
		if (ecrfFieldValuesOut != null && ecrfFieldValuesOut.size() > 0) {
			Long version = null;
			UserOutVO modifiedUser = null;
			Date modifiedTimestamp = null;
			Iterator<ECRFFieldValueOutVO> it = ecrfFieldValuesOut.iterator();
			while (it.hasNext()) {
				ECRFFieldValueOutVO ecrfFieldValueVO = it.next();
				Date ecrfFieldValueModifiedTimestamp = ecrfFieldValueVO.getModifiedTimestamp();
				if (modifiedTimestamp == null || (ecrfFieldValueModifiedTimestamp != null && modifiedTimestamp.compareTo(ecrfFieldValueModifiedTimestamp) < 0)) {
					modifiedUser = ecrfFieldValueVO.getModifiedUser();
					modifiedTimestamp = ecrfFieldValueModifiedTimestamp;
				}
				if (version == null) {
					version = ecrfFieldValueVO.getVersion();
				} else {
					version = Math.max(version.longValue(), ecrfFieldValueVO.getVersion());
				}
			}
			if (version == null || modifiedUser == null || modifiedTimestamp == null) {
				return super.getModifiedAnnotation();
			} else {
				return WebUtil.getModifiedAnnotation(version.longValue(), modifiedUser, modifiedTimestamp);
			}
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public ProbandListEntryOutVO getProbandListEntry() {
		return probandListEntry;
	}

	public HashMap<EcrfFieldSection, Boolean> getSectionCollapsedMap() {
		return sectionCollapsedMap;
	}

	public HashMap<EcrfFieldSection, Boolean> getSectionCreatedMap() {
		return sectionCreatedMap;
	}

	public Collection<EcrfFieldSection> getSections() {
		return sections;
	}

	public HashMap<EcrfFieldSection, Boolean> getSectionUnlockValueMap() {
		return sectionUnlockValueMap;
	}

	public void handleFieldsPerRowChange() {
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
		appendRequestContextCallbackArgs(true);
	}

	public void handleFirstPage() {
		paginator.setToFirstPage();
		handlePageChange();
	}

	public void handleLastPage() {
		paginator.setToLastPage();
		handlePageChange();
	}

	public void handleNextPage() {
		paginator.setToNextPage();
		handlePageChange();
	}

	public void handlePageChange() {
		ecrfFieldValuesOut = null;
		jsEcrfFieldValuesOut = null;
		// paginator.updatePSF();
		// initIn();
		// paginator.initPages(false, ecrfId, probandListEntryId);
		initPages(false);
		initIn(false, true);
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
		appendRequestContextCallbackArgs(true);
	}

	public void handlePageSizeChange() {
		ecrfFieldValuesOut = null;
		jsEcrfFieldValuesOut = null;
		// paginator.initPages(false, ecrfId, probandListEntryId);
		initPages(false);
		initIn(false, true);
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
		appendRequestContextCallbackArgs(true);
	}

	public void handlePrevPage() {
		paginator.setToPrevPage();
		handlePageChange();
	}

	protected void initIn(boolean loadAllJsValues, boolean keepEcrfFieldValuesIn) {
		if (ecrfFieldValuesIn == null) {
			ecrfFieldValuesIn = new ArrayList<ECRFFieldValueInVO>();
		}
		if (probandListEntry == null || ecrf == null) {
			ecrfFieldValuesIn.clear();
			if (ecrfFieldValuesOut != null || jsEcrfFieldValuesOut != null) {
				ecrfFieldValuesOut.clear();
				jsEcrfFieldValuesOut.clear();
			}
		} else {
			if (ecrfFieldValuesOut == null || jsEcrfFieldValuesOut == null) {
				if (CommonUtil.isEmptyString(filterSection)) {
					try {
						portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
								.getEcrfFieldValues(WebUtil.getAuthentication(), ecrf.getId(), probandListEntry.getId(), true,
										loadAllJsValues && ecrf.getEnableBrowserFieldCalculation(), paginator.getPsf()));
					} catch (ServiceException e) {
					} catch (AuthenticationException e) {
						WebUtil.publishException(e);
					} catch (AuthorisationException e) {
					} catch (IllegalArgumentException e) {
					}
				} else {
					try {
						portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
								.getEcrfFieldValues(WebUtil.getAuthentication(), ecrf.getId(), filterSection, filterIndex, probandListEntry.getId(), true,
										loadAllJsValues && ecrf.getEnableBrowserFieldCalculation(), paginator.getPsf()));
					} catch (ServiceException e) {
					} catch (AuthenticationException e) {
						WebUtil.publishException(e);
					} catch (AuthorisationException e) {
					} catch (IllegalArgumentException e) {
					}
				}
			}
			copyEcrfFieldValuesOutToIn(ecrfFieldValuesIn, ecrfFieldValuesOut, keepEcrfFieldValuesIn);
		}
	}

	private void initPages(boolean resetSelectedPage) {
		paginator.initPages(resetSelectedPage,
				ecrf == null ? null : ecrf.getId(),
				probandListEntry == null ? null : probandListEntry.getId());
	}

	private void initSets() {
		// ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, probandListEntryId);
		// probandListEntry = WebUtil.getProbandListEntry(probandListEntryId);
		// probandAddresses = loadProbandAddresses(probandListEntry == null ? null : probandListEntry.getProband().getId());
		probandListEntryTagValues = loadProbandListEntryTagValues(probandListEntry);
		visitScheduleItems = loadVisitScheduleItems(probandListEntry);
		probandGroups = loadProbandGroups(probandListEntry);
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
	}

	// public boolean isAuditTrail() {
	// return ecrfStatus == null ? false : ecrfStatus.getStatus().getAuditTrail();
	// }
	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		if (ecrfFieldValuesOut != null && ecrfFieldValuesOut.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isEditable() {
		return getEditable(false);
	}

	// public boolean isInputVisible() {
	// return isCreated() || (!WebUtil.isTrialLocked(probandListEntry) && !WebUtil.isProbandLocked(probandListEntry));
	// }
	public boolean isReasonForChangeRequired() {
		return ecrfStatus == null ? false : ecrfStatus.getStatus().getAuditTrail() && ecrfStatus.getStatus().getReasonForChangeRequired();
	}

	// private Collection<ProbandAddressOutVO> loadProbandAddresses(Long probandId) {
	// if (Settings.getBoolean(SettingCodes.ENABLE_FIELD_CALCULATION_PROBAND_ADDRESSES_RESPONSE, Bundle.SETTINGS,
	// DefaultSettings.ENABLE_FIELD_CALCULATION_PROBAND_ADDRESSES_RESPONSE) && probandId != null) {
	// try {
	// return WebUtil.getServiceLocator().getProbandService().getProbandAddressList(WebUtil.getAuthentication(), probandId, null);
	// } catch (ServiceException e) {
	// } catch (AuthenticationException e) {
	// WebUtil.publishException(e);
	// } catch (AuthorisationException e) {
	// } catch (IllegalArgumentException e) {
	// }
	// }
	// return null;
	// }
	@Override
	public boolean isRemovable() {
		return false;
	}

	@Override
	public String loadAction() {
		ecrfFieldValuesOut = null;
		jsEcrfFieldValuesOut = null;
		try {
			PSFVO psf = paginator.getPsfCopy(true);
			Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
			Long ecrfId = ecrf == null ? null : ecrf.getId();
			if (CommonUtil.isEmptyString(filterSection)) {
				portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
						.getEcrfFieldValues(WebUtil.getAuthentication(), ecrfId, listEntryId, true, true, psf));
			} else {
				portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
						.getEcrfFieldValues(WebUtil.getAuthentication(), ecrfId, filterSection, filterIndex, listEntryId, true, true,
								psf));
			}
			paginator.initPages(psf, false);
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
			initIn(true, false);
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private Collection<ProbandGroupOutVO> loadProbandGroups(ProbandListEntryOutVO listEntry) {
		if (listEntry != null) { // && listEntry.getGroup() == null) {
			try {
				return WebUtil
						.getServiceLocator()
						.getTrialService().getProbandGroupList(WebUtil.getAuthentication(), listEntry.getTrial().getId(), null);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return null;
	}

	private Collection<ProbandListEntryTagValueJsonVO> loadProbandListEntryTagValues(ProbandListEntryOutVO listEntry) {
		if (listEntry != null) {
			try {
				return WebUtil
						.getServiceLocator()
						.getTrialService()
						.getProbandListEntryTagJsonValues(WebUtil.getAuthentication(), listEntry.getId(), true, null);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return null;
	}

	private Collection<VisitScheduleItemOutVO> loadVisitScheduleItems(ProbandListEntryOutVO listEntry) {
		if (listEntry != null && listEntry.getGroup() != null) {
			try {
				return WebUtil
						.getServiceLocator()
						.getTrialService()
						.getVisitScheduleItemList(WebUtil.getAuthentication(), listEntry.getTrial().getId(), listEntry.getGroup().getId(), null, listEntry.getProband().getId(),
								null);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return null;
	}

	private void portionEcrfFieldValues(ECRFFieldValuesOutVO ecrfFieldValues) {
		if (ecrfFieldValues != null) {
			ecrfFieldValuesOut = ecrfFieldValues.getPageValues();
			jsEcrfFieldValuesOut = ecrfFieldValues.getJsValues();
		} else {
			ecrfFieldValuesOut = null;
			jsEcrfFieldValuesOut = null;
		}
	}

	@Override
	public String resetAction() {
		ecrfFieldValuesOut = null;
		jsEcrfFieldValuesOut = null;
		initIn(true, false);
		initSets();
		// Iterator<HashMap<Long, EcrfFieldInputModelList>> indexModelListIt = inputModelsMap.values().iterator();
		// while (indexModelListIt.hasNext()) {
		Iterator<EcrfFieldInputModelList> modelListIt = inputModelsMap.values().iterator();
		while (modelListIt.hasNext()) {
			Iterator<EcrfFieldInputModel> modelIt = modelListIt.next().iterator();
			while (modelIt.hasNext()) {
				modelIt.next().applyPresets();
			}
		}
		// }
		return RESET_OUTCOME;
	}

	public void setDeltaErrorMessageId(String deltaErrorMessageId) {
		this.deltaErrorMessageId = deltaErrorMessageId;
	}

	public void setEcrf(ECRFOutVO ecrf) {
		this.ecrf = ecrf;
	}

	public void setEcrfStatus(ECRFStatusEntryVO ecrfStatus) {
		this.ecrfStatus = ecrfStatus;
	}

	public void setFilterIndex(Long filterIndex) {
		this.filterIndex = filterIndex;
	}

	public void setFilterSection(String filterSection) {
		this.filterSection = filterSection;
		// this.filterSectionProgress = (filterSection != null ? new ECRFSectionProgressVO() : null);
	}

	public void setFilterSectionProgress(ECRFSectionProgressVO filterSectionProgress) {
		this.filterSectionProgress = filterSectionProgress;
		this.filterSection = (filterSectionProgress != null ? filterSectionProgress.getSection() : null);
	}

	public void setInputModelErrorMsgs(Object data) {
		HashMap<Long, HashMap<Long, String>> errorMessagesMap;
		try {
			errorMessagesMap = (HashMap<Long, HashMap<Long, String>>) data;
		} catch (ClassCastException e) {
			errorMessagesMap = null;
		}
		if (errorMessagesMap != null && errorMessagesMap.size() > 0) {
			// Iterator<HashMap<Long, EcrfFieldInputModelList>> indexModelListIt = inputModelsMap.values().iterator();
			// while (indexModelListIt.hasNext()) {
			Iterator<EcrfFieldInputModelList> modelListIt = inputModelsMap.values().iterator();
			while (modelListIt.hasNext()) {
				Iterator<EcrfFieldInputModel> modelIt = modelListIt.next().iterator();
				while (modelIt.hasNext()) {
					EcrfFieldInputModel inputModel = modelIt.next();
					HashMap<Long, String> indexErrorMessagesMap = errorMessagesMap.get(inputModel.getEcrfFieldId());
					if (indexErrorMessagesMap != null && indexErrorMessagesMap.containsKey(inputModel.getSeriesIndex())) {
						inputModel.setErrorMessage(indexErrorMessagesMap.get(inputModel.getSeriesIndex()));
						// break;
					} else {
						inputModel.setErrorMessage(null);
					}
				}
			}
			// }
		} else {
			clearInputModelErrorMsgs();
		}
	}

	public void setProbandListEntry(ProbandListEntryOutVO probandListEntry) {
		this.probandListEntry = probandListEntry;
	}

	@Override
	public String updateAction() {
		try {
			Set<ECRFFieldValueInVO> inValues = getEcrfFieldInValues(true);
			if (inValues.size() > 0) {
				PSFVO psf = paginator.getPsfCopy(true);
				if (CommonUtil.isEmptyString(filterSection)) {
					portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
							.setEcrfFieldValues(WebUtil.getAuthentication(), inValues, true, psf));
				} else {
					portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
							.setEcrfFieldValues(WebUtil.getAuthentication(), inValues, filterSection, filterIndex, true, psf));
				}
				paginator.initPages(psf, false);
				initIn(false, false);
				initSets();
				addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
				return UPDATE_OUTCOME;
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.NO_ECRF_FIELD_VALUES_TO_SAVE);
			}
		} catch (ServiceException e) {
			setInputModelErrorMsgs(e.getData());
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

	private void updateInputModelModifiedAnnotations() {
		if (ecrfFieldValuesOut != null && ecrfFieldValuesOut.size() > 0) {
			HashMap<Long, HashMap<Long, ECRFFieldValueOutVO>> ecrfFieldValuesOutMap = new HashMap<Long, HashMap<Long, ECRFFieldValueOutVO>>();
			Iterator<ECRFFieldValueOutVO> ecrfFieldValuesOutIt = ecrfFieldValuesOut.iterator();
			while (ecrfFieldValuesOutIt.hasNext()) {
				ECRFFieldValueOutVO out = ecrfFieldValuesOutIt.next();
				if (ecrfFieldValuesOutMap.containsKey(out.getEcrfField().getId())) {
					ecrfFieldValuesOutMap.get(out.getEcrfField().getId()).put(out.getIndex(), out);
				} else {
					HashMap<Long, ECRFFieldValueOutVO> indexEcrfFieldValuesOutMap = new HashMap<Long, ECRFFieldValueOutVO>();
					indexEcrfFieldValuesOutMap.put(out.getIndex(), out);
					ecrfFieldValuesOutMap.put(out.getEcrfField().getId(), indexEcrfFieldValuesOutMap);
				}
			}
			Iterator<EcrfFieldInputModelList> modelListIt = inputModelsMap.values().iterator();
			while (modelListIt.hasNext()) {
				Iterator<EcrfFieldInputModel> modelIt = modelListIt.next().iterator();
				while (modelIt.hasNext()) {
					EcrfFieldInputModel inputModel = modelIt.next();
					HashMap<Long, ECRFFieldValueOutVO> indexEcrfFieldValuesOutMap = ecrfFieldValuesOutMap.get(inputModel.getEcrfFieldId());
					if (indexEcrfFieldValuesOutMap != null && indexEcrfFieldValuesOutMap.containsKey(inputModel.getSeriesIndex())) {
						inputModel.setModifiedAnnotation(indexEcrfFieldValuesOutMap.get(inputModel.getSeriesIndex()));
						// break;
					} else {
						inputModel.setModifiedAnnotation(null);
					}
				}
			}
		} else {
			clearInputModelModifiedAnnotations();
		}
	}

	protected void updateInputModelsMap() {
		HashMap<Long, ECRFFieldOutVO> ecrfFieldVOsMap;
		HashMap<Long, HashMap<Long, ECRFFieldValueOutVO>> ecrfFieldValuesOutMap;
		if (ecrfFieldValuesOut != null) {
			ecrfFieldVOsMap = new HashMap<Long, ECRFFieldOutVO>(ecrfFieldValuesOut.size());
			ecrfFieldValuesOutMap = new HashMap<Long, HashMap<Long, ECRFFieldValueOutVO>>(ecrfFieldValuesOut.size());
			Iterator<ECRFFieldValueOutVO> it = ecrfFieldValuesOut.iterator();
			while (it.hasNext()) {
				ECRFFieldValueOutVO out = it.next();
				ECRFFieldOutVO ecrfFieldVO = out.getEcrfField();
				ecrfFieldVOsMap.put(ecrfFieldVO.getId(), ecrfFieldVO);
				HashMap<Long, ECRFFieldValueOutVO> indexMap;
				if (ecrfFieldValuesOutMap.containsKey(ecrfFieldVO.getId())) {
					indexMap = ecrfFieldValuesOutMap.get(ecrfFieldVO.getId());
				} else {
					indexMap = new HashMap<Long, ECRFFieldValueOutVO>();
					ecrfFieldValuesOutMap.put(ecrfFieldVO.getId(), indexMap);
				}
				indexMap.put(out.getIndex(), out);
			}
		} else {
			ecrfFieldVOsMap = new HashMap<Long, ECRFFieldOutVO>();
			ecrfFieldValuesOutMap = new HashMap<Long, HashMap<Long, ECRFFieldValueOutVO>>();
		}
		sections.clear();
		int sectionRowIndex = 0;
		HashMap<String, HashMap<Long, ArrayList<ECRFFieldValueInVO>>> ecrfFieldValuesMap = new HashMap<String, HashMap<Long, ArrayList<ECRFFieldValueInVO>>>();
		Iterator<ECRFFieldValueInVO> ecrfFieldValueIt = ecrfFieldValuesIn.iterator();
		while (ecrfFieldValueIt.hasNext()) {
			ECRFFieldValueInVO ecrfFieldValue = ecrfFieldValueIt.next();
			ECRFFieldOutVO ecrfFieldVO = ecrfFieldVOsMap.get(ecrfFieldValue.getEcrfFieldId());
			if (ecrfFieldVO != null) {
				String section = ecrfFieldVO.getSection();
				ArrayList<ECRFFieldValueInVO> ecrfFieldValues;
				HashMap<Long, ArrayList<ECRFFieldValueInVO>> indexEcrfFieldValuesMap;
				if (ecrfFieldValuesMap.containsKey(section)) {
					indexEcrfFieldValuesMap = ecrfFieldValuesMap.get(section);
				} else {
					indexEcrfFieldValuesMap = new HashMap<Long, ArrayList<ECRFFieldValueInVO>>();
					ecrfFieldValuesMap.put(section, indexEcrfFieldValuesMap);
				}
				if (indexEcrfFieldValuesMap.containsKey(ecrfFieldValue.getIndex())) {
					ecrfFieldValues = indexEcrfFieldValuesMap.get(ecrfFieldValue.getIndex());
				} else {
					ecrfFieldValues = new ArrayList<ECRFFieldValueInVO>();
					indexEcrfFieldValuesMap.put(ecrfFieldValue.getIndex(), ecrfFieldValues);
					sections.add(new EcrfFieldSection(section, ecrfFieldValue.getIndex(), sectionRowIndex));
					sectionRowIndex++;
				}
				ecrfFieldValues.add(ecrfFieldValue);
			}
		}
		inputModelsMap.clear();
		paddedInputModelsMap.clear();
		sectionCollapsedMap.clear();
		sectionCreatedMap.clear();
		sectionUnlockValueMap.clear();
		Iterator<Entry<String, HashMap<Long, ArrayList<ECRFFieldValueInVO>>>> indexSectionIt = ecrfFieldValuesMap.entrySet().iterator();
		while (indexSectionIt.hasNext()) {
			Entry<String, HashMap<Long, ArrayList<ECRFFieldValueInVO>>> indexSection = indexSectionIt.next();
			String section = indexSection.getKey();
			Iterator<Entry<Long, ArrayList<ECRFFieldValueInVO>>> indexValueIt = indexSection.getValue().entrySet().iterator();
			while (indexValueIt.hasNext()) {
				Entry<Long, ArrayList<ECRFFieldValueInVO>> indexValue = indexValueIt.next();
				Long index = indexValue.getKey();
				EcrfFieldInputModelList inputModelList = new EcrfFieldInputModelList(indexValue.getValue());
				EcrfFieldSection ecrfFieldSection = new EcrfFieldSection(section, index);
				inputModelsMap.put(ecrfFieldSection, inputModelList);
				sectionRowIndex++;
				Iterator<EcrfFieldInputModel> modelsIt = inputModelList.iterator();
				boolean collapsed = true;
				boolean created = true;
				boolean unlockValue = false;
				while (modelsIt.hasNext()) {
					EcrfFieldInputModel model = modelsIt.next();
					model.setLastFieldStatus(ecrfFieldValuesOutMap.get(model.getEcrfFieldValue().getEcrfFieldId()).get(index));
					collapsed = collapsed && model.isCollapsed();
					created = created && model.isCreated();
					if (!created) {
						unlockValue = unlockValue || model.isUnlockValue();
					}
				}
				sectionCollapsedMap.put(ecrfFieldSection, collapsed);
				sectionCreatedMap.put(ecrfFieldSection, created);
				sectionUnlockValueMap.put(ecrfFieldSection, unlockValue);
				paddedInputModelsMap.put(ecrfFieldSection,
						inputModelList.createPaddedList(paginator.getFieldsPerRow(), MAX_INPUT_FIELDS_PER_ROW,
								Settings.getInt(SettingCodes.ECRF_FIELD_INPUTS_GRID_COLUMNS_THRESHOLD_WIDTH, Bundle.SETTINGS,
										DefaultSettings.ECRF_FIELD_INPUTS_GRID_COLUMNS_THRESHOLD_WIDTH)));
			}
		}
	}

	public final void updateSection(EcrfFieldSection section) {
		actionPostProcess(updateSectionAction(section));
	}

	public String updateSectionAction(EcrfFieldSection section) {
		try {
			PSFVO psf = paginator.getPsfCopy(true);
			if (CommonUtil.isEmptyString(filterSection)) {
				portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
						.setEcrfFieldValues(WebUtil.getAuthentication(), getEcrfFieldInValues(section), true, psf));
			} else {
				portionEcrfFieldValues(WebUtil.getServiceLocator().getTrialService()
						.setEcrfFieldValues(WebUtil.getAuthentication(), getEcrfFieldInValues(section), filterSection, filterIndex, true, psf));
				if (filterSectionProgress != null && filterSectionProgress.getSeries()) {
					try {
						filterSectionProgress.copy(WebUtil.getServiceLocator().getTrialService()
								.getEcrfProgress(WebUtil.getAuthentication(), probandListEntry.getId(), ecrf.getId(), filterSection).getSections().iterator().next());
					} catch (Exception e) {
					}
				}
			}
			paginator.initPages(psf, false);
			ecrfFieldValuesIn = clearFromEcrfFieldValuesIn(section);
			initIn(false, true);
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			setInputModelErrorMsgs(e.getData());
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
