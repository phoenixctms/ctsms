package org.phoenixctms.ctsms.web.model.shared.search;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.compare.ComparatorFactory;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.CriterionOutVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CriterionRestrictionVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;
import org.phoenixctms.ctsms.vo.IntermediateSetDetailVO;
import org.phoenixctms.ctsms.vo.IntermediateSetSummaryVO;
import org.phoenixctms.ctsms.web.conversion.IDVOConverter;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.PickerBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

public abstract class SearchBeanBase extends PickerBeanBase {
	
	private static final Comparator<CriterionInVO> CRITERION_IN_VO_COMPARATOR = 
			ComparatorFactory.createNullSafe(CriterionInVO::getPosition);

	private static void copyCriteriaOutToIn(CriteriaInVO criteriaIn, ArrayList<CriterionInVO> criterionsIn, CriteriaOutVO out) {
		if (criteriaIn != null && criterionsIn != null && out != null) {
			Collection<CriterionOutVO> criterionVOs = out.getCriterions();
			criteriaIn.setCategory(out.getCategory());
			criteriaIn.setId(out.getId());
			criteriaIn.setVersion(out.getVersion());
			criteriaIn.setLabel(out.getLabel());
			criteriaIn.setComment(out.getComment());
			criteriaIn.setModule(out.getModule());
			criteriaIn.setLoadByDefault(out.getLoadByDefault());
			criterionsIn.clear();
			criterionsIn.ensureCapacity(criterionVOs.size());
			Iterator<CriterionOutVO> it = criterionVOs.iterator();
			while (it.hasNext()) {
				CriterionInVO criterionIn = new CriterionInVO();
				copyCriterionOutToIn(criterionIn, it.next());
				criterionsIn.add(criterionIn);
			}
			criterionsIn.sort(CRITERION_IN_VO_COMPARATOR);
			normalizeCriterionPositions(criterionsIn);
		}
	}

	private static void copyCriterionOutToIn(CriterionInVO criterionIn, CriterionOutVO criterionOut) {
		if (criterionIn != null && criterionOut != null) {
			CriterionPropertyVO criterionPropertyVO = criterionOut.getProperty();
			CriterionRestrictionVO criterionRestrictionVO = criterionOut.getRestriction();
			CriterionTieVO criterionTieVO = criterionOut.getTie();
			CriteriaOutVO criteriaVO = criterionOut.getCriteria();
			criterionIn.setBooleanValue(criterionOut.getBooleanValue());
			criterionIn.setDateValue(criterionOut.getDateValue());
			criterionIn.setTimeValue(criterionOut.getTimeValue());
			criterionIn.setFloatValue(criterionOut.getFloatValue());
			criterionIn.setLongValue(criterionOut.getLongValue());
			criterionIn.setPosition(criterionOut.getPosition());
			criterionIn.setPropertyId(criterionPropertyVO == null ? null : criterionPropertyVO.getId());
			criterionIn.setRestrictionId(criterionRestrictionVO == null ? null : criterionRestrictionVO.getId());
			criterionIn.setStringValue(criterionOut.getStringValue());
			criterionIn.setTieId(criterionTieVO == null ? null : criterionTieVO.getId());
			criterionIn.setTimestampValue(criterionOut.getTimestampValue());
		}
	}

	private static Integer getIndexAttribute() {
		try {
			return CommonUtil.safeLongToInt(Long.parseLong((String) UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("index")));
		} catch (Exception e) {
		}
		return null;
	}

	private static void initCriteriaDefaultValues(CriteriaInVO criteriaIn, ArrayList<CriterionInVO> criterionsIn, DBModule modulePreset, String labelPreset, String commentPreset) {
		if (criteriaIn != null && criterionsIn != null) {
			criteriaIn.setCategory(Messages.getString(MessageCodes.CRITERIA_CATEGORY_PRESET));
			criteriaIn.setId(null);
			criteriaIn.setVersion(null);
			criteriaIn.setLabel(labelPreset);
			criteriaIn.setComment(commentPreset);
			criteriaIn.setModule(modulePreset);
			criteriaIn.setLoadByDefault(false);
			criterionsIn.clear();
			CriterionInVO criterionIn = new CriterionInVO();
			initCriterionDefaultValues(criterionIn);
			criterionsIn.add(criterionIn);
			normalizeCriterionPositions(criterionsIn);
		}
	}

	private static void initCriterionDefaultValues(CriterionInVO criterionIn) {
		if (criterionIn != null) {
			criterionIn.setBooleanValue(false);
			criterionIn.setDateValue(null);
			criterionIn.setTimeValue(null);
			criterionIn.setFloatValue(null);
			criterionIn.setLongValue(null);
			criterionIn.setPosition(null);
			criterionIn.setPropertyId(null);
			criterionIn.setRestrictionId(null);
			criterionIn.setStringValue(null);
			criterionIn.setTieId(null);
			criterionIn.setTimestampValue(null);
		}
	}

	private static void normalizeCriterionPositions(ArrayList<CriterionInVO> criterionsIn) {
		updateCriterionPositions(criterionsIn, 0, criterionsIn.size() - 1);
	}

	private static void updateCriterionPositions(ArrayList<CriterionInVO> criterionsIn, int startPos, int endPos) {
		for (int i = startPos; i <= endPos; i++) {
			criterionsIn.get(i).setPosition(CommonUtil.LIST_INITIAL_POSITION + i);
		}
	}

	protected CriteriaInVO criteriaIn;
	protected ArrayList<CriterionInVO> criterionsIn;
	private CriteriaOutVO out;
	private ArrayList<SelectItem> categories;
	private CriteriaLazyModel criteriaModel;
	private ArrayList<SelectItem> ties;
	private ArrayList<SelectItem> properties;
	private ArrayList<SelectItem> booleans;
	private HashMap<Long, CriterionTieVO> tieVOsMap;
	private HashMap<Long, CriterionPropertyVO> propertyVOsMap;
	private HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap;
	private HashMap<Long, ArrayList<SelectItem>> restrictionsMap;
	private HashMap<Long, ArrayList<SelectItem>> selectionItemsMap;
	private HashMap<Long, Converter> converterMap;
	private CriteriaInstantVO instantCriteria;
	private boolean parsed;
	private HashMap<Long, CriterionInstantVO> instantCriterionsMap;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;
	private int selectionItemsNameClipMaxLength;
	private String[] criterionIndexes;
	private String deferredDeleteReason;

	protected SearchBeanBase() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		criteriaModel = new CriteriaLazyModel();
		selectionItemsNameClipMaxLength = Settings.getInt(SettingCodes.SELECTION_ITEMS_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.SELECTION_ITEMS_TEXT_CLIP_MAX_LENGTH);
		int maxCriterions = Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS);
		criterionIndexes = new String[maxCriterions];
		for (int i = 0; i < maxCriterions; i++) {
			criterionIndexes[i] = Integer.toString(i);
		}
	}

	@Override
	public String addAction() {
		CriteriaInVO backup = new CriteriaInVO(criteriaIn);
		// Long criteriaInIdBackup = criteriaIn.getId();
		// Long criteriaInVersionBackup = criteriaIn.getVersion();
		criteriaIn.setId(null);
		criteriaIn.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getSearchService().addCriteria(WebUtil.getAuthentication(),
					criteriaIn, getNewCriterions()); // ok since position differs for each element...
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			criteriaIn.copy(backup);
			Messages.addCriterionMessages(e.getData(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			criteriaIn.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException | IllegalArgumentException e) {
			criteriaIn.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_PICKER.toString(), isPicker());
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_CRITERIA_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_CRITERIA_JOURNAL_ENTRY_COUNT,
					MessageCodes.CRITERIA_JOURNAL_TAB_TITLE, MessageCodes.CRITERIA_JOURNAL_TAB_TITLE_WITH_COUNT,
					(out == null || isPicker()) ? null : WebUtil.getJournalCount(JournalModule.CRITERIA_JOURNAL, criteriaIn.getId()));
			// tabCountMap.get(JSValues.AJAX_CRITERIA_JOURNAL_ENTRY_COUNT.toString()));
		}
	}

	public void changeByRow() {
		Long criteriaId = WebUtil.getLongParamValue(GetParamNames.CRITERIA_ID);
		if (criteriaId != null) {
			load(criteriaId);
		} else {
			this.out = null;
			this.initIn();
			initSets();
			appendRequestContextCallbackArgs(true);
		}
	}

	public List<String> completeCategory(String query) {
		criteriaIn.setCategory(query);
		Collection<String> categories = null;
		try {
			categories = WebUtil.getServiceLocator().getSearchService().getCriteriaCategories(WebUtil.getAuthentication(), this.getDBModule(), query, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (categories != null) {
			try {
				return ((List<String>) categories);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public List<String> completeTextValue(String query) {
		Integer index = getIndexAttribute();
		if (index != null) {
			CriterionInVO criterionVO = getCriterionIn(index);
			criterionVO.setStringValue(query);
			Collection completeMethodResult = getCompleteMethodResult(criterionVO, query);
			if (completeMethodResult != null) {
				try {
					return ((List<String>) completeMethodResult);
				} catch (ClassCastException e) {
				}
			}
		}
		return new ArrayList<String>();
	}

	public List<IDVO> completeVo(String query) {
		Integer index = getIndexAttribute();
		if (index != null) {
			CriterionInVO criterionVO = getCriterionIn(index);
			// criterionVO.setStringValue(query);
			Collection completeMethodResult = getCompleteMethodResult(criterionVO, query);
			if (completeMethodResult != null) {
				try {
					IDVO.transformVoCollection(completeMethodResult);
					return (List<IDVO>) completeMethodResult;
				} catch (ClassCastException e) {
				}
			}
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String deleteAction() {
		return deleteAction(criteriaIn.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getSearchService().deleteCriteria(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.CRITERIA_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.CRITERIA_DEFERRED_DELETE),
					false, deferredDeleteReason);
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public void deleteCriterion(int criterionIndex) {
		if (testCriterionIndex(criterionIndex)) {
			criterionsIn.remove(criterionIndex);
			updateCriterionPositions(criterionIndex);
			updateInstantCriteria(true);
		}
	}

	public void dropCriterion() {
		Long indexSource = WebUtil.getLongParamValue(GetParamNames.SOURCE_INDEX);
		Long indexTarget = WebUtil.getLongParamValue(GetParamNames.TARGET_INDEX);
		if (indexSource != null && indexTarget != null) {
			int sourceIndex = CommonUtil.safeLongToInt(indexSource);
			int targetIndex = CommonUtil.safeLongToInt(indexTarget);
			if (testCriterionIndex(sourceIndex) && testCriterionIndex(targetIndex)) {
				if (sourceIndex > targetIndex) {
					for (int i = sourceIndex; i > targetIndex; i--) {
						moveDown(i);
					}
					updateCriterionPositions(targetIndex, sourceIndex);
					updateInstantCriteria(true);
				} else if (sourceIndex < targetIndex) {
					for (int i = sourceIndex; i < targetIndex; i++) {
						moveUp(i);
					}
					updateCriterionPositions(sourceIndex, targetIndex);
					updateInstantCriteria(true);
				}
			}
		}
	}

	public ArrayList<SelectItem> getBooleans() {
		return booleans;
	}

	public ArrayList<SelectItem> getCategories() {
		return categories;
	}

	public String getCompleteItemDetailHtml(Integer index, IDVO idvo) {
		Map<String, String> details = null;
		StringBuilder sb = new StringBuilder();
		try {
			details = ((IDVOConverter) getConverter().get(index)).getDetails(idvo);
		} catch (Exception e) {
		}
		if (details != null && details.size() > 0) {
			sb.append("<ul>");
			Iterator<Entry<String, String>> it = details.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> detail = it.next();
				sb.append("<li>");
				sb.append(CommonUtil.clipString(
						Messages.getMessage(detail.getKey(), WebUtil.escapeHtml(detail.getValue())),
						96, CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.TRAILING));
				sb.append("</li>");
			}
			sb.append("</ul>");
		}
		return sb.toString();
	}

	private Collection getCompleteMethodResult(CriterionInVO criterionVO, String query) {
		try {
			return (Collection) AssociationPath.invoke(getPropertyVO(criterionVO).getCompleteMethodName(), WebUtil.getServiceLocator().getToolsService(), true,
					WebUtil.getAuthentication(), query);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof ServiceException) {
			} else if (e.getCause() instanceof AuthenticationException) {
				WebUtil.publishException((AuthenticationException) e.getCause());
			} else if (e.getCause() instanceof AuthorisationException) {
			} else if (e.getCause() instanceof IllegalArgumentException) {
			}
		} catch (Exception e) {
		}
		return null;
	}

	public List<Converter> getConverter() {
		return new CriterionConverterList(criterionsIn, propertyVOsMap, converterMap);
	}

	public List<String> getCourseName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.COURSE_DB);
	}

	protected abstract String getCriteriaCommentPreset();

	public CriteriaInVO getCriteriaIn() {
		return criteriaIn;
	}

	protected abstract String getCriteriaLabelPreset();

	public CriteriaLazyModel getCriteriaModel() {
		return criteriaModel;
	}

	private CriterionInVO getCriterionIn(int criterionIndex) {
		return (testCriterionIndex(criterionIndex) ? criterionsIn.get(criterionIndex) : null);
	}

	public String[] getCriterionIndexes() {
		return criterionIndexes;
	}

	public List<String> getCriterionRowColor() {
		return new CriterionRowColorList(criterionsIn, instantCriteria, instantCriterionsMap, parsed);
	}

	public List<String> getCriterionRowSelectLabel() {
		return new CriterionRowSelectLabelList(criterionsIn, instantCriteria, instantCriterionsMap, parsed);
	}

	public ArrayList<CriterionInVO> getCriterionsIn() {
		return criterionsIn;
	}

	public String getCriterionsLabel() {
		Integer selectCount;
		if (parsed) {
			if (instantCriteria != null && (selectCount = instantCriteria.getSelectStatementCount()) != null) {
				return Messages.getMessage(MessageCodes.SEARCH_CRITERIONS_WITH_SET_OPS_LABEL, instantCriteria.getSetOperationExpression(), Integer.toString(selectCount));
			} else {
				return Messages.getMessage(MessageCodes.SEARCH_CRITERIONS_INVALID_LABEL);
			}
		} else {
			return Messages.getMessage(MessageCodes.SEARCH_CRITERIONS_UNCHECKED_LABEL);
		}
	}

	protected abstract DBModule getDBModule();

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public List<String> getInputFieldName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.INPUT_FIELD_DB);
	}

	public List<String> getInventoryName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.INVENTORY_DB);
	}

	public List<Boolean> getIsBoolean() {
		return new CriterionIsBooleanList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsCoursePicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.COURSE_DB);
	}

	public List<Boolean> getIsCriterionPropertyRendered() {
		return new CriterionPropertyIsRenderedList(criterionsIn, tieVOsMap);
	}

	public List<Boolean> getIsCriterionRowRendered() {
		return new CriterionRowIsRenderedList(criterionsIn);
	}

	public List<Boolean> getIsDate() {
		return new CriterionIsDateList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsFloat() {
		return new CriterionIsFloatList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsInputFieldPicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.INPUT_FIELD_DB);
	}

	public List<Boolean> getIsInsertCriterionEnabled() {
		return new CriterionIsInsertCriterionEnabledList(criterionsIn);
	}

	public List<Boolean> getIsInteger() {
		return new CriterionIsIntegerList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsInventoryPicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.INVENTORY_DB);
	}

	public List<Boolean> getIsMassMailPicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.MASS_MAIL_DB);
	}

	public List<Boolean> getIsMoveCriterionDownEnabled() {
		return new CriterionIsMoveCriterionDownEnabledList(criterionsIn);
	}

	// public List<Boolean> getIsPickerList() {
	// return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	// }
	public List<Boolean> getIsMoveCriterionUpEnabled() {
		return new CriterionIsMoveCriterionUpEnabledList(criterionsIn);
	}

	public List<Boolean> getIsProbandPicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.PROBAND_DB);
	}

	public List<Boolean> getIsSelection() {
		return new CriterionIsSelectionList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsStaffPicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.STAFF_DB);
	}

	public List<Boolean> getIsText() {
		return new CriterionIsTextList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsTextAutocomplete() {
		return new CriterionIsAutocompleteList(criterionsIn, propertyVOsMap, restrictionVOsMap, converterMap, false);
	}

	public List<Boolean> getIsTime() {
		return new CriterionIsTimeList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsTimestamp() {
		return new CriterionIsTimestampList(criterionsIn, propertyVOsMap, restrictionVOsMap);
	}

	public List<Boolean> getIsTrialPicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.TRIAL_DB);
	}

	public List<Boolean> getIsUserPicker() {
		return new CriterionIsPickerList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.USER_DB);
	}

	public List<Boolean> getIsVoAutocomplete() {
		return new CriterionIsAutocompleteList(criterionsIn, propertyVOsMap, restrictionVOsMap, converterMap, true);
	}

	public List<String> getMassMailName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.MASS_MAIL_DB);
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	protected HashSet<CriterionInVO> getNewCriterions() {
		HashSet<CriterionInVO> newCriterions = new HashSet<CriterionInVO>(criterionsIn.size());
		for (int i = 0; i < criterionsIn.size(); i++) {
			CriterionInVO newCriterionVO = new CriterionInVO(criterionsIn.get(i));
			sanitizeCriterionVals(newCriterionVO, i);
			newCriterions.add(newCriterionVO);
		}
		return newCriterions;
	}

	public CriteriaOutVO getOut() {
		return out;
	}

	public String getPositionDigitsPattern() {
		int digits = Integer.toString(Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS)).length();
		// int digits = 1;
		// try {
		// digits = ((int) Math.ceil(Math.log10(
		// Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS)
		// )));
		// } catch (Exception e) {
		// }
		return CommonUtil.repeatString("0", digits);
	}

	public List<String> getProbandName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.PROBAND_DB);
	}

	public ArrayList<SelectItem> getProperties() {
		return properties;
	}

	private CriterionPropertyVO getPropertyVO(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : propertyVOsMap.get(criterionVO.getPropertyId()));
	}

	public abstract String getQueryResultTitle();

	protected String getQueryResultTitle(long rowCount) {
		String label = criteriaIn.getLabel();
		if (label != null && label.length() > 0) {
			return Messages.getMessage(MessageCodes.SEARCH_RESULT_LIST_LABEL_HEADER, getResultItemLabel(), rowCount, label);
		}
		return Messages.getMessage(MessageCodes.SEARCH_RESULT_LIST_HEADER, getResultItemLabel(), rowCount);
	}

	private org.phoenixctms.ctsms.enumeration.CriterionRestriction getRestriction(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : restrictionVOsMap.get(criterionVO.getRestrictionId()));
	}

	public List<ArrayList<SelectItem>> getRestrictions() {
		return new CriterionRestrictionsList(criterionsIn, propertyVOsMap, restrictionsMap);
	}

	protected abstract String getResultItemLabel();

	public String getSearchTitle() {
		return Messages.getMessage(MessageCodes.SEARCH_TITLE, WebUtil.getDBModuleVO(this.getDBModule()).getName());
	}

	public List<String> getSelectedItem() {
		return new CriterionSelectedItemList(criterionsIn, propertyVOsMap);
	}

	public List<ArrayList<SelectItem>> getSelectionItems() {
		return new CriterionSelectionItemsList(criterionsIn, propertyVOsMap, selectionItemsMap);
	}

	public List<String> getStaffName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.STAFF_DB);
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	public ArrayList<SelectItem> getTies() {
		return ties;
	}

	private CriterionTieVO getTieVO(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : tieVOsMap.get(criterionVO.getTieId()));
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.CRITERIA_ID) == null);
	}

	protected abstract String getTitle(boolean operationSuccess);

	protected String getTitle(String titleMessageCode, String createNewMessageCode, boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : titleMessageCode, Long.toString(out.getId()), CommonUtil.criteriaOutVOToString(out));
		} else {
			return Messages.getString(operationSuccess ? createNewMessageCode : MessageCodes.ERROR_LOADING_CRITERIA);
		}
	}

	public List<String> getTrialName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.TRIAL_DB);
	}

	public List<String> getUserName() {
		return new CriterionEntityNameList(criterionsIn, propertyVOsMap, restrictionVOsMap, DBModule.USER_DB);
	}

	public List<IDVO> getVos() {
		return new CriterionIDVOList(criterionsIn, propertyVOsMap, converterMap);
	}

	public void handleCategoryChange() {
		criteriaModel.updateRowCount();
	}

	public void handlePropertyChange(int criterionIndex) {
		CriterionInVO newCriterionIn = getCriterionIn(criterionIndex);
		if (newCriterionIn != null) {
			newCriterionIn.setLongValue(null);
		}
		handlePropertyRestrictionTieChange(criterionIndex);
	}

	private void handlePropertyRestrictionTieChange(int criterionIndex) {
		sanitizeCriterionVals(getCriterionIn(criterionIndex), criterionIndex);
		updateInstantCriteria(false);
	}

	public void handleRestrictionChange(int criterionIndex) {
		handlePropertyRestrictionTieChange(criterionIndex);
	}

	public void handleTieChange(int criterionIndex) {
		handlePropertyRestrictionTieChange(criterionIndex);
	}

	protected void initIn() {
		if (criteriaIn == null) {
			criteriaIn = new CriteriaInVO();
		}
		if (criterionsIn == null) {
			criterionsIn = new ArrayList<CriterionInVO>();
		}
		if (out != null) {
			copyCriteriaOutToIn(criteriaIn, criterionsIn, out);
		} else {
			initCriteriaDefaultValues(criteriaIn, criterionsIn, getDBModule(), getCriteriaLabelPreset(), getCriteriaCommentPreset());
		}
	}

	private void initSets() {
		tabCountMap.clear();
		tabTitleMap.clear();
		Long count = ((out == null || isPicker()) ? null : WebUtil.getJournalCount(JournalModule.CRITERIA_JOURNAL, criteriaIn.getId()));
		tabCountMap.put(JSValues.AJAX_CRITERIA_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_CRITERIA_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.CRITERIA_JOURNAL_TAB_TITLE, MessageCodes.CRITERIA_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		criteriaModel.setCategory(criteriaIn.getCategory());
		criteriaModel.setModule(this.getDBModule());
		criteriaModel.updateRowCount();
		Collection<String> categoryStrings = null;
		try {
			categoryStrings = WebUtil.getServiceLocator().getSearchService().getCriteriaCategories(WebUtil.getAuthentication(), this.getDBModule(), null, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (categoryStrings != null) {
			categories = new ArrayList<SelectItem>(categoryStrings.size());
			Iterator<String> it = categoryStrings.iterator();
			while (it.hasNext()) {
				String category = it.next();
				categories.add(new SelectItem(category, category));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		if (ties == null || tieVOsMap == null) {
			Collection<CriterionTieVO> tieVOs = null;
			try {
				tieVOs = WebUtil.getServiceLocator().getSelectionSetService().getAllCriterionTies(WebUtil.getAuthentication());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (tieVOs != null) {
				ties = new ArrayList<SelectItem>(tieVOs.size());
				tieVOsMap = new HashMap<Long, CriterionTieVO>(tieVOs.size());
				Iterator<CriterionTieVO> it = tieVOs.iterator();
				while (it.hasNext()) {
					CriterionTieVO tieVO = it.next();
					ties.add(new SelectItem(tieVO.getId().toString(), tieVO.getName()));
					tieVOsMap.put(tieVO.getId(), tieVO);
				}
			} else {
				ties = new ArrayList<SelectItem>();
				tieVOsMap = new HashMap<Long, CriterionTieVO>();
			}
		}
		if (restrictionVOsMap == null) {
			Collection<CriterionRestrictionVO> restrictionVOs = null;
			try {
				restrictionVOs = WebUtil.getServiceLocator().getSelectionSetService().getAllCriteriaRestrictions(WebUtil.getAuthentication());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (restrictionVOs != null) {
				restrictionVOsMap = new HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>(restrictionVOs.size());
				Iterator<CriterionRestrictionVO> it = restrictionVOs.iterator();
				while (it.hasNext()) {
					CriterionRestrictionVO restrictionVO = it.next();
					restrictionVOsMap.put(restrictionVO.getId(), restrictionVO.getRestriction());
				}
			} else {
				restrictionVOsMap = new HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>();
			}
		}
		if (booleans == null) {
			booleans = WebUtil.getBooleans(false, false);
		}
		if (properties == null || propertyVOsMap == null || restrictionsMap == null || selectionItemsMap == null || converterMap == null) {
			Collection<CriterionPropertyVO> propertyVOs = null;
			try {
				propertyVOs = WebUtil.getServiceLocator().getSelectionSetService().getCriterionProperties(WebUtil.getAuthentication(), this.getDBModule());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (propertyVOs != null) {
				properties = new ArrayList<SelectItem>(propertyVOs.size());
				propertyVOsMap = new HashMap<Long, CriterionPropertyVO>(propertyVOs.size());
				restrictionsMap = new HashMap<Long, ArrayList<SelectItem>>(propertyVOs.size());
				selectionItemsMap = new HashMap<Long, ArrayList<SelectItem>>(propertyVOs.size());
				converterMap = new HashMap<Long, Converter>(propertyVOs.size());
				Iterator<CriterionPropertyVO> propertyIt = propertyVOs.iterator();
				while (propertyIt.hasNext()) {
					CriterionPropertyVO propertyVO = propertyIt.next();
					properties.add(new SelectItem(propertyVO.getId().toString(), propertyVO.getName()));
					propertyVOsMap.put(propertyVO.getId(), propertyVO);
					ArrayList<SelectItem> restrictions = new ArrayList<SelectItem>(propertyVO.getValidRestrictions().size());
					Iterator<CriterionRestrictionVO> restrictionIt = propertyVO.getValidRestrictions().iterator();
					while (restrictionIt.hasNext()) {
						CriterionRestrictionVO restrictionVO = restrictionIt.next();
						restrictions.add(new SelectItem(restrictionVO.getId().toString(), restrictionVO.getName()));
					}
					restrictionsMap.put(propertyVO.getId(), restrictions);
					ArrayList<SelectItem> selectionItems = new ArrayList<SelectItem>();
					if (WebUtil.testSelectionSetServiceMethodName(propertyVO)) {
						Collection selectionSetServiceMethodResult = null;
						try {
							selectionSetServiceMethodResult = (Collection) AssociationPath.invoke(propertyVO.getSelectionSetServiceMethodName(), WebUtil.getServiceLocator()
									.getSelectionSetService(), true, WebUtil.getAuthentication());
						} catch (InvocationTargetException e) {
							if (e.getCause() instanceof ServiceException) {
							} else if (e.getCause() instanceof AuthenticationException) {
								WebUtil.publishException((AuthenticationException) e.getCause());
							} else if (e.getCause() instanceof AuthorisationException) {
							} else if (e.getCause() instanceof IllegalArgumentException) {
							}
						} catch (Exception e) {
						}
						if (selectionSetServiceMethodResult != null) {
							Iterator serviceResultIt = selectionSetServiceMethodResult.iterator();
							while (serviceResultIt.hasNext()) {
								Object vo = serviceResultIt.next();
								String name = null;
								try {
									name = (String) AssociationPath.invoke(propertyVO.getGetNameMethodName(), vo, false);
								} catch (Exception e) {
								}
								if (name != null) {
									Object valueObject = null;
									try {
										valueObject = AssociationPath.invoke(propertyVO.getGetValueMethodName(), vo, false);
									} catch (Exception e) {
									}
									String value = null;
									if (valueObject != null) {
										value = CommonUtil.inputValueToString(valueObject, WebUtil.getDateFormat(), WebUtil.getDecimalSeparator());
									} else {
										value = CommonUtil.NO_SELECTION_VALUE;
									}
									if (value != null) {
										selectionItems.add(new SelectItem(value, CommonUtil.clipString(name, selectionItemsNameClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS,
												EllipsisPlacement.MID)));
									}
								}
							}
						}
					} else if (WebUtil.testCompleteMethodName(propertyVO) && WebUtil.testConverter(propertyVO)) {
						try {
							converterMap.put(propertyVO.getId(), WebUtil.createConverter(propertyVO.getConverter()));
						} catch (Exception e) {
							converterMap.put(propertyVO.getId(), null);
						}
					}
					selectionItemsMap.put(propertyVO.getId(), selectionItems);
				}
			} else {
				properties = new ArrayList<SelectItem>();
				propertyVOsMap = new HashMap<Long, CriterionPropertyVO>();
				restrictionsMap = new HashMap<Long, ArrayList<SelectItem>>();
				selectionItemsMap = new HashMap<Long, ArrayList<SelectItem>>();
				converterMap = new HashMap<Long, Converter>();
			}
		}
		updateInstantCriteria(true);
		initSpecificSets();
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.CRITERIA_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.CRITERIA_DEFERRED_DELETE)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, deferredDeleteReason);
		}
	}

	protected abstract void initSpecificSets();

	public void insertCriterion(int criterionIndex) {
		if (testCriterionIndex(criterionIndex)) {
			if (criterionsIn.size() < Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS)) {
				CriterionInVO criterionIn = new CriterionInVO();
				initCriterionDefaultValues(criterionIn);
				if (criterionIndex < criterionsIn.size() - 1) {
					criterionsIn.add(criterionIndex + 1, criterionIn);
				} else {
					criterionsIn.add(criterionIn);
				}
				updateCriterionPositions(criterionIndex + 1);
				updateInstantCriteria(true);
			}
		}
	}

	@Override
	public boolean isCreateable() {
		return WebUtil.getModuleEnabled(getDBModule());
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.CRITERIA_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.CRITERIA_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return WebUtil.getModuleEnabled(getDBModule()) && super.isEditable();
	}

	public abstract boolean isMarkUnEncrypted();

	@Override
	public boolean isRemovable() {
		return WebUtil.getModuleEnabled(getDBModule()) && super.isRemovable();
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab));
	}

	@Override
	public String loadAction() {
		return loadAction(criteriaIn.getId());
	}

	@Override
	public String loadAction(Long id) {
		return loadAction(id, false);
	}

	private String loadAction(Long id, boolean defaultCriteria) {
		out = null;
		int maxCriterions = Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS);
		try {
			if (defaultCriteria) {
				out = WebUtil.getServiceLocator().getSearchService().getDefaultCriteria(WebUtil.getAuthentication(), getDBModule());
				if (out == null) {
					return LOAD_OUTCOME;
				}
			} else {
				out = WebUtil.getServiceLocator().getSearchService().getCriteria(WebUtil.getAuthentication(), id);
				if (!out.getModule().equals(this.getDBModule())) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.CRITERIA_MODULE_MISMATCH, WebUtil.getDBModuleVO(out.getModule()).getName(), WebUtil
							.getDBModuleVO(this.getDBModule()).getName());
					out = null;
					return ERROR_OUTCOME;
				}
			}
			if (out.getCriterions().size() > maxCriterions) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.TOO_MANY_CRITERIONS, out.getCriterions().size(), maxCriterions);
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
			searchAction();
		}
		return ERROR_OUTCOME;
	}

	public final void loadDefault() {
		actionPostProcess(loadDefaultAction());
	}

	public final String loadDefaultAction() {
		return loadAction(null, true);
	}

	public void moveCriterionDown(int criterionIndex) {
		if (testCriterionIndex(criterionIndex) && moveDown(criterionIndex)) {
			updateCriterionPositions(criterionIndex - 1, criterionIndex);
			updateInstantCriteria(true);
		}
	}

	public void moveCriterionUp(int criterionIndex) {
		if (testCriterionIndex(criterionIndex) && moveUp(criterionIndex)) {
			updateCriterionPositions(criterionIndex, criterionIndex + 1);
			updateInstantCriteria(true);
		}
	}

	// public void onSearchTabViewChange(TabChangeEvent event) {
	// }
	private boolean moveDown(int criterionIndex) {
		if (criterionIndex > 0) {
			CriterionInVO swap = criterionsIn.set(criterionIndex - 1, criterionsIn.get(criterionIndex));
			criterionsIn.set(criterionIndex, swap);
			return true;
		}
		return false;
	}

	private boolean moveUp(int criterionIndex) {
		if (criterionIndex < criterionsIn.size() - 1) {
			CriterionInVO swap = criterionsIn.set(criterionIndex + 1, criterionsIn.get(criterionIndex));
			criterionsIn.set(criterionIndex, swap);
			return true;
		}
		return false;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		searchAction();
		return RESET_OUTCOME;
	}

	private void sanitizeCriterionVals(CriterionInVO newCriterionVO, int i) {
		CriterionPropertyVO propertyVO = getPropertyVO(newCriterionVO);
		CriterionTieVO tieVO = getTieVO(newCriterionVO);
		if (tieVO != null && CommonUtil.isBlankCriterionTie(tieVO.getTie())) {
			newCriterionVO.setFloatValue(null);
			newCriterionVO.setStringValue(null);
			newCriterionVO.setBooleanValue(false);
			newCriterionVO.setDateValue(null);
			newCriterionVO.setTimeValue(null);
			newCriterionVO.setTimestampValue(null);
			newCriterionVO.setLongValue(null);
			newCriterionVO.setPropertyId(null);
			newCriterionVO.setRestrictionId(null);
		}
		if (propertyVO != null) {
			if (CommonUtil.isUnaryCriterionRestriction(getRestriction(newCriterionVO)) || CriterionValueType.NONE.equals(propertyVO.getValueType())) {
				newCriterionVO.setFloatValue(null);
				newCriterionVO.setStringValue(null);
				newCriterionVO.setBooleanValue(false);
				newCriterionVO.setDateValue(null);
				newCriterionVO.setTimeValue(null);
				newCriterionVO.setTimestampValue(null);
				newCriterionVO.setLongValue(null);
			} else if (WebUtil.testPicker(propertyVO)) {
				newCriterionVO.setFloatValue(null);
				newCriterionVO.setStringValue(null);
				newCriterionVO.setBooleanValue(false);
				newCriterionVO.setDateValue(null);
				newCriterionVO.setTimeValue(null);
				newCriterionVO.setTimestampValue(null);
			} else {
				if (!(CriterionValueType.LONG.equals(propertyVO.getValueType()) || CriterionValueType.LONG_HASH.equals(propertyVO.getValueType()))) {
					newCriterionVO.setLongValue(null);
				}
				if (!(CriterionValueType.FLOAT.equals(propertyVO.getValueType()) || CriterionValueType.FLOAT_HASH.equals(propertyVO.getValueType()))) {
					newCriterionVO.setFloatValue(null);
				}
				if (!(CriterionValueType.STRING.equals(propertyVO.getValueType()) || CriterionValueType.STRING_HASH.equals(propertyVO.getValueType()))) {
					newCriterionVO.setStringValue(null);
				}
				if (!(CriterionValueType.BOOLEAN.equals(propertyVO.getValueType()) || CriterionValueType.BOOLEAN_HASH.equals(propertyVO.getValueType()))) {
					newCriterionVO.setBooleanValue(false);
				}
				if (!(CriterionValueType.DATE.equals(propertyVO.getValueType()) || CriterionValueType.DATE_HASH.equals(propertyVO.getValueType()))) {
					newCriterionVO.setDateValue(null);
				}
				if (!(CriterionValueType.TIME.equals(propertyVO.getValueType()) || CriterionValueType.TIME_HASH.equals(propertyVO.getValueType()))) {
					newCriterionVO.setTimeValue(null);
				}
				if (!(CriterionValueType.TIMESTAMP.equals(propertyVO.getValueType()) || CriterionValueType.TIMESTAMP_HASH.equals(propertyVO.getValueType()))) {
					newCriterionVO.setTimestampValue(null);
				}
			}
		}
	}

	public final void search() {
		actionPostProcess(searchAction());
	}

	public String searchAction() {
		return UNIMPLEMENTED_OUTCOME;
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	private boolean testCriterionIndex(int criterionIndex) {
		return (criterionIndex >= 0 && criterionIndex < criterionsIn.size());
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getSearchService().updateCriteria(WebUtil.getAuthentication(), criteriaIn, getNewCriterions());
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addCriterionMessages(e.getData(), FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	private void updateCriterionPositions(int startPos) {
		updateCriterionPositions(criterionsIn, startPos, criterionsIn.size() - 1);
	}

	private void updateCriterionPositions(int startPos, int endPos) {
		updateCriterionPositions(criterionsIn, startPos, endPos);
	}

	protected void updateInstantCriteria() {
		if (instantCriteria != null) {
			Collection<CriterionInstantVO> instantCriterionVOs = instantCriteria.getCriterions();
			instantCriterionsMap = new HashMap<Long, CriterionInstantVO>(instantCriterionVOs.size());
			Iterator<CriterionInstantVO> instantCriterionVOsIt = instantCriterionVOs.iterator();
			while (instantCriterionVOsIt.hasNext()) {
				CriterionInstantVO instantCriterionVO = instantCriterionVOsIt.next();
				instantCriterionsMap.put(instantCriterionVO.getPosition(), instantCriterionVO);
			}
		} else {
			instantCriteria = new CriteriaInstantVO();
			instantCriterionsMap = new HashMap<Long, CriterionInstantVO>();
		}
	}

	protected void updateInstantCriteria(boolean parse) {
		instantCriteria = null;
		parsed = parse;
		if (parse) {
			try {
				instantCriteria = WebUtil.getServiceLocator().getSearchService().parseCriteria(WebUtil.getAuthentication(), getDBModule(), getNewCriterions(), false);
			} catch (ServiceException e) {
				if (e.getData() != null) {
					Messages.addCriterionMessages(e.getData(), FacesMessage.SEVERITY_ERROR, e.getMessage());
				}
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException | IllegalArgumentException e) {
			}
		}
		updateInstantCriteria();
	}

	public String getResultListId() {
		return MessageFormat.format("{0}{1}_result_list", getDBModule().getValue(), isPicker() ? "_picker" : "");
	}

	public void updateIntermediateSets() {
		instantCriteria = null;
		parsed = true;
		Collection<IntermediateSetDetailVO> intermediateSets = null;
		boolean operationSuccess = false;
		try {
			IntermediateSetSummaryVO intermediateSetSummary = WebUtil.getServiceLocator().getSearchService().getIntermediateSets(WebUtil.getAuthentication(), getDBModule(),
					getNewCriterions(), null);
			operationSuccess = true;
			instantCriteria = intermediateSetSummary.getParsed();
			intermediateSets = intermediateSetSummary.getSets();
		} catch (ServiceException e) {
			if (e.getData() != null) {
				Messages.addCriterionMessages(e.getData(), FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		updateInstantCriteria();
		// if (operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_PICKER.toString(), isPicker());
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			// requestContext.addCallbackParam(JSValues.XAJAX_ROOT_ENTITY_CREATED.toString(),CommonUtil.LIST_INITIAL_POSITION);
			requestContext.addCallbackParam(JSValues.AJAX_CRITERIA_RESULT_ITEM_LABEL_BASE64.toString(), JsUtil.encodeBase64(getResultItemLabel(), false));
			requestContext.addCallbackParam(JSValues.AJAX_CRITERION_TIE_MAP_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(tieVOsMap), false));
			requestContext.addCallbackParam(JSValues.AJAX_CRITERION_ROW_COLORS_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(getCriterionRowColor()), false));
			requestContext.addCallbackParam(JSValues.AJAX_INTERMEDIATE_SETS_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(intermediateSets), false));
		}
		// }
	}
}
