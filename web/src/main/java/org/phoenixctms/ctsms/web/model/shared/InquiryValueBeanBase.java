package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValuesOutVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.Paginator;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InquiryInputModel;
import org.phoenixctms.ctsms.web.model.shared.inputfield.InquiryInputModelList;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

public abstract class InquiryValueBeanBase extends ManagedBeanBase {

	private static int MAX_INPUT_FIELDS_PER_ROW = 10;

	public static void copyInquiryValueOutToIn(InquiryValueInVO in, InquiryValueOutVO out) {
		if (in != null && out != null) {
			ProbandOutVO probandVO = out.getProband();
			InquiryOutVO inquiryVO = out.getInquiry();
			Collection<InputFieldSelectionSetValueOutVO> inquiryValueVOs = out.getSelectionValues();
			in.setBooleanValue(out.getBooleanValue());
			in.setDateValue(out.getDateValue());
			in.setTimeValue(out.getTimeValue());
			in.setFloatValue(out.getFloatValue());
			in.setId(out.getId());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setLongValue(out.getLongValue());
			ArrayList<Long> selectionValueIds = new ArrayList<Long>(inquiryValueVOs.size());
			Iterator<InputFieldSelectionSetValueOutVO> inquiryValueVOsIt = inquiryValueVOs.iterator();
			while (inquiryValueVOsIt.hasNext()) {
				selectionValueIds.add(inquiryValueVOsIt.next().getId());
			}
			in.setSelectionValueIds(selectionValueIds);
			in.setInquiryId(inquiryVO == null ? null : inquiryVO.getId());
			in.setTextValue(out.getTextValue());
			in.setTimestampValue(out.getTimestampValue());
			in.setInkValues(out.getInkValues());
			in.setVersion(out.getVersion());
		}
	}

	public static void copyInquiryValuesInToJson(ArrayList<InquiryValueJsonVO> inquiryValuesOut, ArrayList<InquiryValueInVO> inquiryValuesIn,
			HashMap<Long, InquiryOutVO> inquiryVOsMap) {
		if (inquiryValuesOut != null) {
			inquiryValuesOut.clear();
			if (inquiryValuesIn != null && inquiryVOsMap != null) {
				inquiryValuesOut.ensureCapacity(inquiryValuesIn.size());
				Iterator<InquiryValueInVO> it = inquiryValuesIn.iterator();
				while (it.hasNext()) {
					InquiryValueInVO in = it.next();
					if (inquiryVOsMap.containsKey(in.getInquiryId())) {
						InquiryValueJsonVO out = new InquiryValueJsonVO();
						CommonUtil.copyInquiryValueInToJson(out, in, inquiryVOsMap.get(in.getInquiryId()));
						inquiryValuesOut.add(out);
					}
				}
			}
		}
	}

	public static void copyInquiryValuesOutToIn(ArrayList<InquiryValueInVO> inquiryValuesIn, Collection<InquiryValueOutVO> inquiryValuesOut, boolean keepInquiryValuesIn) {
		if (inquiryValuesIn != null) {
			HashMap<Long, InquiryValueInVO> inquiryValuesInMap = new HashMap<Long, InquiryValueInVO>(inquiryValuesIn.size());
			if (keepInquiryValuesIn) {
				Iterator<InquiryValueInVO> it = inquiryValuesIn.iterator();
				while (it.hasNext()) {
					InquiryValueInVO in = it.next();
					inquiryValuesInMap.put(in.getInquiryId(), in);
				}
			}
			inquiryValuesIn.clear();
			if (inquiryValuesOut != null) {
				inquiryValuesIn.ensureCapacity(inquiryValuesOut.size());
				Iterator<InquiryValueOutVO> it = inquiryValuesOut.iterator();
				while (it.hasNext()) {
					InquiryValueOutVO out = it.next();
					InquiryValueInVO in;
					if (inquiryValuesInMap.containsKey(out.getInquiry().getId())) {
						in = inquiryValuesInMap.get(out.getInquiry().getId());
					} else {
						in = new InquiryValueInVO();
						copyInquiryValueOutToIn(in, out);
					}
					inquiryValuesIn.add(in);

				}
			}
		}
	}

	protected Long trialId;
	protected TrialOutVO trial;
	protected Paginator paginator;
	protected ArrayList<InquiryValueInVO> inquiryValuesIn;
	protected Collection<InquiryValueOutVO> inquiryValuesOut;
	protected Collection<InquiryValueJsonVO> jsInquiryValuesOut;
	private HashMap<String, InquiryInputModelList> inputModelsMap;
	private HashMap<String, Boolean> categoryCollapsedMap;
	private Collection<InquiryCategory> categories;
	private HashMap<String, List<Object[]>> paddedInputModelsMap;

	public InquiryValueBeanBase() {
		super();
		trialId = null;
		trial = null;
		categories = new ArrayList<InquiryCategory>();
		inputModelsMap = new HashMap<String, InquiryInputModelList>();
		categoryCollapsedMap = new HashMap<String, Boolean>();
		paddedInputModelsMap = new HashMap<String, List<Object[]>>();
		paginator = new Paginator(this) {

			@Override
			protected Long getCount(Long... ids) {
				if (isSignup()) {
					return WebUtil.getInquiryCount(ids[0], null, true);
				} else {
					return WebUtil.getInquiryCount(ids[0], true, null);
				}
			}

			@Override
			protected int getDefaultFieldsPerRow() {
				return ((InquiryValueBeanBase) bean).getDefaultFieldsPerRow();
				// return Settings.getInt(SettingCodes.INQUIRY_VALUES_DEFAULT_FIELDS_PER_ROW, Bundle.SETTINGS,DefaultSettings.INQUIRY_VALUES_DEFAULT_FIELDS_PER_ROW);
			}

			@Override
			protected int getDefaultPageSize() {
				return ((InquiryValueBeanBase) bean).getDefaultPageSize();
				// return Settings.getInt(SettingCodes.INQUIRY_VALUES_DEFAULT_PAGE_SIZE, Bundle.SETTINGS, DefaultSettings.INQUIRY_VALUES_DEFAULT_PAGE_SIZE);
			}

			@Override
			protected String getFirstPageButtonLabel() {
				return MessageCodes.INQUIRY_VALUES_FIRST_PAGE_BUTTON_LABEL;
			}

			@Override
			protected String getLoadLabel() {
				return MessageCodes.INQUIRY_VALUES_LOAD_LABEL;
			}

			@Override
			protected int getMaxFieldsPerRow() {
				return MAX_INPUT_FIELDS_PER_ROW;
				// return Settings.getInt(SettingCodes.INQUIRY_VALUES_MAX_FIELDS_PER_ROW, Bundle.SETTINGS,
				// DefaultSettings.INQUIRY_VALUES_MAX_FIELDS_PER_ROW);
			}

			@Override
			protected String getPageButtonLabel() {
				return MessageCodes.INQUIRY_VALUES_PAGE_BUTTON_LABEL;
			}

			@Override
			protected ArrayList<String> getPageSizeStrings() {
				return ((InquiryValueBeanBase) bean).getPageSizeStrings();
				// return Settings.getStringList(SettingCodes.INQUIRY_VALUES_PAGE_SIZES, Bundle.SETTINGS, DefaultSettings.INQUIRY_VALUES_PAGE_SIZES);
			}
		};
	}

	protected void appendRequestContextCallbackInputModelValuesOutArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			if (inquiryValuesIn != null && inquiryValuesOut != null && jsInquiryValuesOut != null && jsInquiryValuesOut.size() > 0) {
				ProbandOutVO proband = getProband();
				ArrayList<InquiryValueJsonVO> out = new ArrayList<InquiryValueJsonVO>(jsInquiryValuesOut.size());
				HashMap<Long, InquiryOutVO> inquiryVOsMap = new HashMap<Long, InquiryOutVO>(inquiryValuesOut.size());
				Iterator<InquiryValueOutVO> inquiryIt = inquiryValuesOut.iterator();
				while (inquiryIt.hasNext()) {
					InquiryValueOutVO inquiryValueVO = inquiryIt.next();
					InquiryOutVO inquiryVO = inquiryValueVO.getInquiry();
					if (!CommonUtil.isEmptyString(inquiryVO.getJsVariableName())) {
						inquiryVOsMap.put(inquiryVO.getId(), inquiryVO);
					}
				}
				copyInquiryValuesInToJson(out, inquiryValuesIn, inquiryVOsMap);
				Iterator<InquiryValueJsonVO> jsInquiryValueIt = jsInquiryValuesOut.iterator();
				while (jsInquiryValueIt.hasNext()) {
					InquiryValueJsonVO jsInquiryValue = jsInquiryValueIt.next();
					if (!inquiryVOsMap.containsKey(jsInquiryValue.getInquiryId())) {
						out.add(jsInquiryValue);
					}
				}
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64.toString(), JsUtil.encodeBase64(JsUtil.inputFieldVariableValueToJson(out), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_PROBAND_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(proband), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_TRIAL_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(trial), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64.toString(), JsUtil.encodeBase64(JsUtil.voToJson(getProbandAddresses()), false));
				requestContext.addCallbackParam(JSValues.AJAX_INPUT_FIELD_ACTIVE_USER_BASE64.toString(),
						JsUtil.encodeBase64(JsUtil.voToJson(WebUtil.getUser()), false));
			}
		}
	}

	private void clearInputModelErrorMsgs() {
		Iterator<InquiryInputModelList> modelListIt = inputModelsMap.values().iterator();
		while (modelListIt.hasNext()) {
			Iterator<InquiryInputModel> modelIt = modelListIt.next().iterator();
			while (modelIt.hasNext()) {
				modelIt.next().setErrorMessage(null);
			}
		}
	}

	private void clearInputModelModifiedAnnotations() {
		Iterator<InquiryInputModelList> modelListIt = inputModelsMap.values().iterator();
		while (modelListIt.hasNext()) {
			Iterator<InquiryInputModel> modelIt = modelListIt.next().iterator();
			while (modelIt.hasNext()) {
				modelIt.next().setModifiedAnnotation(null);
			}
		}
	}

	public Collection<InquiryCategory> getCategories() {
		return categories;
	}

	public Map<String, Boolean> getCategoryCollapsedMap() {
		return categoryCollapsedMap;
	}

	protected abstract int getDefaultFieldsPerRow();

	protected abstract int getDefaultPageSize();

	public Map<String, List<Object[]>> getInputModelsMap() {
		return paddedInputModelsMap;
	}

	protected abstract InquiryInputModelList getInquiryInputModelList(ArrayList<InquiryValueInVO> values);

	protected abstract ArrayList<String> getPageSizeStrings();

	public Paginator getPaginator() {
		return paginator;
	}

	protected abstract ProbandOutVO getProband();

	protected abstract Collection<ProbandAddressOutVO> getProbandAddresses();

	public TrialOutVO getTrial() {
		return trial;
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
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		paginator.updatePSF();
		initIn(false, true);
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
		appendRequestContextCallbackInputModelValuesOutArgs(true);
	}

	public void handlePageSizeChange() {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		paginator.initPages(false, trialId);
		initIn(false, true);
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
		appendRequestContextCallbackInputModelValuesOutArgs(true);
	}

	public void handlePrevPage() {
		paginator.setToPrevPage();
		handlePageChange();
	}

	protected abstract void initIn(boolean loadAllJsValues, boolean keepInquiryValuesIn);

	protected void initSets() {
		initSpecificSets();
		loadTrial();
		updateInputModelsMap();
		updateInputModelModifiedAnnotations();
	}

	protected abstract void initSpecificSets();

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isRemovable() {
		return false;
	}

	protected abstract boolean isSignup();

	protected void loadTrial() {
		trial = WebUtil.getTrial(trialId);
	}

	protected void portionInquiryValues(InquiryValuesOutVO inquiryValues) {
		if (inquiryValues != null) {
			inquiryValuesOut = inquiryValues.getPageValues();
			jsInquiryValuesOut = inquiryValues.getJsValues();
		} else {
			inquiryValuesOut = null;
			jsInquiryValuesOut = null;
		}
	}

	@Override
	public String resetAction() {
		inquiryValuesOut = null;
		jsInquiryValuesOut = null;
		initIn(true, false);
		initSets();
		Iterator<InquiryInputModelList> modelListIt = inputModelsMap.values().iterator();
		while (modelListIt.hasNext()) {
			Iterator<InquiryInputModel> it = modelListIt.next().iterator();
			while (it.hasNext()) {
				it.next().applyPresets();
			}
		}
		return RESET_OUTCOME;
	}

	protected void setInputModelErrorMsgs(Object data) {
		HashMap<Long, String> errorMessagesMap;
		try {
			errorMessagesMap = (HashMap<Long, String>) data;
		} catch (ClassCastException e) {
			errorMessagesMap = null;
		}
		if (errorMessagesMap != null && errorMessagesMap.size() > 0) {
			Iterator<InquiryInputModelList> modelListIt = inputModelsMap.values().iterator();
			while (modelListIt.hasNext()) {
				Iterator<InquiryInputModel> modelIt = modelListIt.next().iterator();
				while (modelIt.hasNext()) {
					InquiryInputModel inputModel = modelIt.next();
					if (errorMessagesMap.containsKey(inputModel.getInquiryId())) {
						inputModel.setErrorMessage(errorMessagesMap.get(inputModel.getInquiryId()));
					} else {
						inputModel.setErrorMessage(null);
					}
				}
			}
		} else {
			clearInputModelErrorMsgs();
		}
	}

	protected void updateInputModelModifiedAnnotations() {
		if (inquiryValuesOut != null && inquiryValuesOut.size() > 0) {
			HashMap<Long, InquiryValueOutVO> inquiryValuesOutMap = new HashMap<Long, InquiryValueOutVO>(inquiryValuesOut.size());
			Iterator<InquiryValueOutVO> inquiryValuesOutIt = inquiryValuesOut.iterator();
			while (inquiryValuesOutIt.hasNext()) {
				InquiryValueOutVO out = inquiryValuesOutIt.next();
				inquiryValuesOutMap.put(out.getInquiry().getId(), out);
			}
			Iterator<InquiryInputModelList> modelListIt = inputModelsMap.values().iterator();
			while (modelListIt.hasNext()) {
				Iterator<InquiryInputModel> modelIt = modelListIt.next().iterator();
				while (modelIt.hasNext()) {
					InquiryInputModel inputModel = modelIt.next();
					if (inquiryValuesOutMap.containsKey(inputModel.getInquiryId())) {
						inputModel.setModifiedAnnotation(inquiryValuesOutMap.get(inputModel.getInquiryId()));
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
		HashMap<Long, InquiryOutVO> inquiryVOsMap;
		if (inquiryValuesOut != null) {
			inquiryVOsMap = new HashMap<Long, InquiryOutVO>(inquiryValuesOut.size());
			Iterator<InquiryValueOutVO> it = inquiryValuesOut.iterator();
			while (it.hasNext()) {
				InquiryOutVO inquiryVO = it.next().getInquiry();
				inquiryVOsMap.put(inquiryVO.getId(), inquiryVO);
			}
		} else {
			inquiryVOsMap = new HashMap<Long, InquiryOutVO>();
		}
		categories.clear();
		int categoryRowIndex = 0;
		HashMap<String, ArrayList<InquiryValueInVO>> inquiryValuesMap = new HashMap<String, ArrayList<InquiryValueInVO>>();
		Iterator<InquiryValueInVO> inquiryValueIt = inquiryValuesIn.iterator();
		while (inquiryValueIt.hasNext()) {
			InquiryValueInVO inquiryValue = inquiryValueIt.next();
			InquiryOutVO inquiryVO = inquiryVOsMap.get(inquiryValue.getInquiryId());
			if (inquiryVO != null) {
				String category = inquiryVO.getCategory();
				ArrayList<InquiryValueInVO> inquiryValues;
				if (inquiryValuesMap.containsKey(category)) {
					inquiryValues = inquiryValuesMap.get(category);
				} else {
					inquiryValues = new ArrayList<InquiryValueInVO>();
					inquiryValuesMap.put(category, inquiryValues);
					categories.add(new InquiryCategory(category, categoryRowIndex));
					categoryRowIndex++;
				}
				inquiryValues.add(inquiryValue);
			}
		}
		inputModelsMap.clear();
		paddedInputModelsMap.clear();
		categoryCollapsedMap.clear();
		Iterator<String> categoryIt = inquiryValuesMap.keySet().iterator();
		while (categoryIt.hasNext()) {
			String category = categoryIt.next();
			InquiryInputModelList inputModelList = getInquiryInputModelList(inquiryValuesMap.get(category));
			inputModelsMap.put(category, inputModelList);
			Iterator<InquiryInputModel> modelsIt = inputModelList.iterator();
			boolean collapsed = true;
			while (collapsed && modelsIt.hasNext()) {
				collapsed = modelsIt.next().isCollapsed();
			}
			categoryCollapsedMap.put(category, collapsed);
			paddedInputModelsMap.put(category,
					inputModelList.createPaddedList(paginator.getFieldsPerRow(), MAX_INPUT_FIELDS_PER_ROW,
							Settings.getInt(SettingCodes.INQUIRY_INPUTS_GRID_COLUMNS_THRESHOLD_WIDTH, Bundle.SETTINGS,
									DefaultSettings.INQUIRY_INPUTS_GRID_COLUMNS_THRESHOLD_WIDTH)));
		}
	}
}
