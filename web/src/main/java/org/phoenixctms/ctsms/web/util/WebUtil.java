package org.phoenixctms.ctsms.web.util;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.ServiceLocator;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JobModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.AuthenticationExceptionCodes;
import org.phoenixctms.ctsms.util.AuthorisationExceptionCodes;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.*;
import org.phoenixctms.ctsms.web.model.ApplicationScopeBean;
import org.phoenixctms.ctsms.web.model.SessionScopeBean;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import com.sun.faces.application.view.ViewScopeManager;

public final class WebUtil {

	public enum ColorOpacity {
		ALPHA100(""), ALPHA50("-50"), ALPHA25("-25");

		private final String suffix;

		private ColorOpacity(final String suffix) {
			this.suffix = suffix;
		}

		@Override
		public String toString() {
			return suffix;
		}

		public String value() {
			return suffix;
		}
	}

	public static final String REST_API_PATH = "rest";
	public static final int IMAGE_STORE_MAX_SIZE = 2;
	private final static String COLOR_STYLECLASS_PREFIX = "ctsms-color-";
	private static final String INPUT_FIELD_TYPE_ICON_STYLECLASS_PREFIX = "ctsms-inputfieldtype-";
	public static final String SCHEDULE_EVENT_ICON_STYLECLASS = "ctsms-event-icon";
	public static final String COLLISION_ICON_STYLECLASS = "ctsms-icon-warning";
	public static final String MENUBAR_ICON_STYLECLASS = "ctsms-menubar-icon";
	public static final String MENU_BOLD_STYLECLASS = "ctsms-menu-bold";
	public final static String ID_SEPARATOR_STRING = ",";
	public final static Pattern ID_SEPARATOR_REGEXP = Pattern.compile(Pattern.quote(ID_SEPARATOR_STRING));
	public final static int FACES_INITIAL_ROW_INDEX = 0;
	private final static String REFERER_HEADER_NAME = "Referer";
	public final static String EVENT_CONTEXT_VIEW_ID = "viewId";
	public final static String FILE_TITLE_PSF_PROPERTY_NAME = "title";
	public final static String FILE_ID_PSF_PROPERTY_NAME = "id";
	public final static String FILE_TITLE_HASH_PSF_PROPERTY_NAME = "titleHash";
	public final static String FILE_NAME_PSF_PROPERTY_NAME = "fileName";
	public final static String FILE_LOGICAL_PATH_PSF_PROPERTY_NAME = "logicalPath";
	public final static String FILE_NAME_HASH_PSF_PROPERTY_NAME = "fileNameHash";
	public final static String FILE_ACTIVE_PSF_PROPERTY_NAME = "active";
	public final static String FILE_PUBLIC_PSF_PROPERTY_NAME = "publicFile";
	public final static String TRIAL_STATUS_PSF_PROPERTY_NAME = "status";
	public final static String INQUIRY_POSITION_PSF_PROPERTY_NAME = "position";
	public final static String PROBAND_LIST_ENTRY_TAG_POSITION_PSF_PROPERTY_NAME = "position";
	public final static String JOURNAL_ENTRY_ID_PSF_PROPERTY_NAME = "id";
	public final static String ECRF_FIELD_VALUE_ID_PSF_PROPERTY_NAME = "id";
	public final static String ECRF_FIELD_STATUS_ENTRY_ID_PSF_PROPERTY_NAME = "id";
	public static final String FOLDER_NODE_TYPE = "folder";
	public static final String FILE_NODE_TYPE = "file";
	public static final String PARENT_NODE_TYPE = "parent";
	public static final String LEAF_NODE_TYPE = "leaf";
	public static final String JS_NULL = "null";
	private final static Gson JSON_BEAUTIFIER = new GsonBuilder().setPrettyPrinting()
			.serializeNulls()
			.setDateFormat(JsUtil.VO_JSON_DATETIME_PATTERN)
			.create();
	private final static Gson JSON_COMPRESSOR = new GsonBuilder().serializeNulls()
			.setDateFormat(JsUtil.VO_JSON_DATETIME_PATTERN)
			.create();
	private final static JsonParser JSON_PARSER = new JsonParser();
	private final static String EL_ENUM_LIST_DEFAULT_SEPARATOR = ",";
	private final static Pattern EL_ENUM_LIST_REGEXP = Pattern.compile(Pattern.quote(EL_ENUM_LIST_DEFAULT_SEPARATOR));

	public static Date addIntervals(Date date, VariablePeriod period, Long explicitDays, int n) {
		try {
			return getServiceLocator().getToolsService().addIntervals(date, period, explicitDays, n);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return null;
	}

	public static void appendRefererParameter(StringBuilder url, HttpServletRequest request, String separator) {
		if (url != null && request != null) {
			if (separator != null && separator.length() > 0) {
				url.append(separator);
			}
			url.append(GetParamNames.REFERER);
			url.append("=");
			url.append(getRefererBase64(request));
		}
	}

	public static RequestContext appendRequestContextCallbackTabTitleArgs(RequestContext context,
			JSValues tabTitleBase64Arg,
			JSValues countJsVar,
			String tabTitleMsgCode,
			String tabTitleWithCountMsgCode,
			Long count, Object... args) {
		RequestContext requestContext = context == null ? RequestContext.getCurrentInstance() : context;
		if (requestContext != null) {
			requestContext.addCallbackParam(tabTitleBase64Arg.toString(), JsUtil.encodeBase64(getTabTitleString(tabTitleMsgCode, tabTitleWithCountMsgCode, count, args), false));
			requestContext.addCallbackParam(countJsVar.toString(), count != null ? count : -1l);
		}
		return requestContext;
	}

	public static String beautifyJson(String json) {
		try {
			JsonElement je = JSON_PARSER.parse(json);
			return JSON_BEAUTIFIER.toJson(je);
		} catch (JsonSyntaxException | NullPointerException e) {
		}
		return json;
	}

	public static String clipStringPicker(String string) {
		return CommonUtil.clipString(string, Settings.getInt(SettingCodes.PICKER_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.PICKER_CLIP_MAX_LENGTH),
				CommonUtil.DEFAULT_ELLIPSIS, EllipsisPlacement.MID);
	}

	public static String colorToStyleClass(Color color) {
		return colorToStyleClass(color, null);
	}

	public static String colorToStyleClass(Color color, ColorOpacity alpha) {
		StringBuilder result = new StringBuilder();
		if (color != null) {
			result.append(COLOR_STYLECLASS_PREFIX);
			result.append(color.name().toLowerCase(Locale.ENGLISH));
			if (alpha != null) {
				result.append(alpha.value());
			}
		}
		return result.toString();
	}

	public static List<String> completeBankCodeNumber(String query, String bicPrefix, String bankNameInfix) {
		Collection<String> bankCodeNumbers = null;
		try {
			bankCodeNumbers = getServiceLocator().getToolsService().completeBankCodeNumber(getAuthentication(), query, bicPrefix, bankNameInfix, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (bankCodeNumbers != null) {
			try {
				return ((List<String>) bankCodeNumbers);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeBankName(String bankCodeNumberPrefix, String bicPrefix, String query) {
		Collection<String> bankNames = null;
		try {
			bankNames = getServiceLocator().getToolsService().completeBankName(getAuthentication(), query, bankCodeNumberPrefix, bicPrefix, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (bankNames != null) {
			try {
				return ((List<String>) bankNames);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeBic(String bankCodeNumberPrefix, String query, String bankNameInfix) {
		Collection<String> bics = null;
		try {
			bics = getServiceLocator().getToolsService().completeBic(getAuthentication(), query, bankCodeNumberPrefix, bankNameInfix, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (bics != null) {
			try {
				return ((List<String>) bics);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeCityName(String countryName, String zipCode, String query) {
		Collection<String> cityNames = null;
		try {
			cityNames = getServiceLocator().getToolsService().completeCityName(getAuthentication(), query, countryName, zipCode, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (cityNames != null) {
			try {
				return ((List<String>) cityNames);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeCountryName(String query) {
		Collection<String> countryNames = null;
		try {
			countryNames = getServiceLocator().getToolsService().completeCountryName(getAuthentication(), query, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (countryNames != null) {
			try {
				return ((List<String>) countryNames);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeLogicalPath(FileModule module, Long entityId, String query) {
		Collection<String> folders = null;
		try {
			folders = getServiceLocator().getFileService().getFileFolders(getAuthentication(), module, entityId, query, true, null, null, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (folders != null) {
			try {
				return ((List<String>) folders);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeStreetName(String countryName, String zipCode, String cityName, String query) {
		Collection<String> streetNames = null;
		try {
			streetNames = getServiceLocator().getToolsService().completeStreetName(getAuthentication(), query, countryName, zipCode, cityName, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (streetNames != null) {
			try {
				return ((List<String>) streetNames);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeTitle(String query) {
		Collection<String> titles = null;
		try {
			titles = getServiceLocator().getToolsService().completeTitle(getAuthentication(), query, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (titles != null) {
			try {
				return ((List<String>) titles);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static List<String> completeZipCode(String countryName, String query, String cityName, String streetName) {
		Collection<String> zipCodes = null;
		try {
			if (!CommonUtil.isEmptyString(countryName) && !CommonUtil.isEmptyString(cityName) && !CommonUtil.isEmptyString(streetName)) {
				zipCodes = getServiceLocator().getToolsService().completeZipCodeByStreetName(getAuthentication(), query, countryName, cityName, streetName, null);
			} else {
				zipCodes = getServiceLocator().getToolsService().completeZipCode(getAuthentication(), query, countryName, cityName, null);
			}
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (zipCodes != null) {
			try {
				return ((List<String>) zipCodes);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public static String compressJson(String json) {
		try {
			JsonElement je = JSON_PARSER.parse(json);
			return JSON_COMPRESSOR.toJson(je);
		} catch (JsonSyntaxException | NullPointerException e) {
		}
		return json;
	}

	public static String courseIdToName(Long id) {
		if (id == null) {
			return getNoCoursePickedMessage();
		}
		try {
			CourseOutVO course = getServiceLocator().getCourseService().getCourse(getAuthentication(), id, null, null, null);
			return courseOutVOToString(course);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_COURSE_PICKED);
	}

	public static String courseOutVOToString(CourseOutVO course) {
		String result = CommonUtil.courseOutVOToString(course);
		if (result != null) {
			return result;
		} else {
			return getNoCoursePickedMessage();
		}
	}

	public static MethodExpressionActionListener createActionListenerMethodBinding(String actionListenerExpression) {
		FacesContext context = FacesContext.getCurrentInstance();
		return new MethodExpressionActionListener(context.getApplication().getExpressionFactory()
				.createMethodExpression(context.getELContext(), actionListenerExpression, null, new Class[] { ActionEvent.class }));
	}

	public static Converter createConverter(String converterId) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().createConverter(converterId);
	}

	public static String createViewUrl(Urls view, boolean relative, GetParamNames param, Long value) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		StringBuffer url = new StringBuffer();
		if (!relative) {
			url.append(getBaseUrl(request));
		}
		url.append(view.toString(request));
		if (param != null && value != null) {
			url.append('?').append(param.toString()).append("=").append(Long.toString(value));
		}
		return url.toString();
	}

	public static String escapeHtml(String string) {
		return org.apache.commons.text.StringEscapeUtils.escapeHtml4(string);
	}

	public static String escapeJSStringArray(List<String> valueList, boolean brackets, boolean quotes) {
		StringBuilder sb;
		if (brackets) {
			sb = new StringBuilder("[");
		} else {
			sb = new StringBuilder();
		}
		if (valueList != null && valueList.size() > 0) {
			Iterator<String> it = valueList.iterator();
			while (it.hasNext()) {
				sb.append(quoteJSString(it.next(), brackets || quotes));
				if (it.hasNext()) {
					sb.append(",");
				}
			}
		}
		if (brackets) {
			sb.append("]");
		}
		return sb.toString();
	}

	public static String expirationToColor(Date today, Date expiration, Map<Long, Color> dueInColorMap, Color overdueColor) {
		Boolean expired = getExpired(today, expiration);
		if (expired != null) {
			if (expired) {
				return colorToStyleClass(overdueColor);
			} else {
				long delta = CommonUtil.dateDeltaSecs(today, expiration);
				Iterator<Long> it = dueInColorMap.keySet().iterator();
				Long limitKey = null;
				while (it.hasNext()) {
					Long limit = it.next();
					if (delta < limit) {
						if (limitKey == null || limit < limitKey) {
							limitKey = limit;
						}
					}
				}
				if (limitKey != null) {
					return colorToStyleClass(dueInColorMap.get(limitKey));
				}
			}
		}
		return "";
	}

	public static UIComponent findComponentByClientId(String clientId) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			return context.getViewRoot().findComponent(clientId);
		}
		return null;
	}

	public static UIComponent findComponentById(String id) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			return findComponentById(context.getViewRoot(), id);
		}
		return null;
	}

	public static UIComponent findComponentById(UIComponent parent, String id) {
		if (id.equals(parent.getId())) {
			return parent;
		}
		Iterator<UIComponent> kids = parent.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent kid = kids.next();
			UIComponent found = findComponentById(kid, id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public static AddressTypeVO getAddressType(Long addressTypeId) {
		if (addressTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getAddressType(getAuthentication(), addressTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getAllContactDetailTypes() {
		ArrayList<SelectItem> types;
		Collection<ContactDetailTypeVO> typeVOs = null;
		try {
			typeVOs = getServiceLocator().getSelectionSetService().getAllContactDetailTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (typeVOs != null) {
			types = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<ContactDetailTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				ContactDetailTypeVO typeVO = it.next();
				types.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			types = new ArrayList<SelectItem>();
		}
		return types;
	}

	public static ArrayList<SelectItem> getAllCourseCategories() {
		ArrayList<SelectItem> categories;
		Collection<CourseCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getAllCourseCategories(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<CourseCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				CourseCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getAllCourseParticipationStatusTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<CourseParticipationStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = getServiceLocator().getSelectionSetService().getAllCourseParticipationStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<CourseParticipationStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				CourseParticipationStatusTypeVO statusTypeVO = it.next();
				statusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	public static ArrayList<SelectItem> getAllCvSections() {
		ArrayList<SelectItem> cvSections;
		Collection<CvSectionVO> sectionVOs = null;
		try {
			sectionVOs = getServiceLocator().getSelectionSetService().getAllCvSections(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (sectionVOs != null) {
			cvSections = new ArrayList<SelectItem>(sectionVOs.size());
			Iterator<CvSectionVO> it = sectionVOs.iterator();
			while (it.hasNext()) {
				CvSectionVO sectionVO = it.next();
				cvSections.add(new SelectItem(sectionVO.getId().toString(), sectionVO.getName()));
			}
		} else {
			cvSections = new ArrayList<SelectItem>();
		}
		return cvSections;
	}

	public static ArrayList<SelectItem> getAllTrainingRecordSections() {
		ArrayList<SelectItem> trainingRecordSections;
		Collection<TrainingRecordSectionVO> sectionVOs = null;
		try {
			sectionVOs = getServiceLocator().getSelectionSetService().getAllTrainingRecordSections(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (sectionVOs != null) {
			trainingRecordSections = new ArrayList<SelectItem>(sectionVOs.size());
			Iterator<TrainingRecordSectionVO> it = sectionVOs.iterator();
			while (it.hasNext()) {
				TrainingRecordSectionVO sectionVO = it.next();
				trainingRecordSections.add(new SelectItem(sectionVO.getId().toString(), sectionVO.getName()));
			}
		} else {
			trainingRecordSections = new ArrayList<SelectItem>();
		}
		return trainingRecordSections;
	}

	public static ArrayList<SelectItem> getAllDepartments() {
		ArrayList<SelectItem> departments;
		Collection<DepartmentVO> departmentVOs = null;
		try {
			departmentVOs = getServiceLocator().getSelectionSetService().getAllDepartments(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (departmentVOs != null) {
			departments = new ArrayList<SelectItem>(departmentVOs.size());
			Iterator<DepartmentVO> it = departmentVOs.iterator();
			while (it.hasNext()) {
				DepartmentVO departmentVO = it.next();
				departments.add(new SelectItem(departmentVO.getId().toString(), departmentVO.getName()));
			}
		} else {
			departments = new ArrayList<SelectItem>();
		}
		return departments;
	}

	public static ArrayList<SelectItem> getAllEcrfFieldStatusTypes() {
		ArrayList<SelectItem> types;
		Collection<ECRFFieldStatusTypeVO> typeVOs = null;
		try {
			typeVOs = getServiceLocator().getSelectionSetService().getAllEcrfFieldStatusTypes(getAuthentication(), null, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (typeVOs != null) {
			types = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<ECRFFieldStatusTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				ECRFFieldStatusTypeVO typeVO = it.next();
				types.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			types = new ArrayList<SelectItem>();
		}
		return types;
	}

	public static ArrayList<SelectItem> getAllInventoryCategories() {
		ArrayList<SelectItem> categories;
		Collection<InventoryCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getAllInventoryCategories(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<InventoryCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				InventoryCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getAllInventoryStatusTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<InventoryStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = getServiceLocator().getSelectionSetService().getAllInventoryStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<InventoryStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				InventoryStatusTypeVO statusTypeVO = it.next();
				statusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	public static ArrayList<SelectItem> getAllLecturerCompetences() {
		ArrayList<SelectItem> competences;
		Collection<LecturerCompetenceVO> competenceVOs = null;
		try {
			competenceVOs = getServiceLocator().getSelectionSetService().getAllLecturerCompetences(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (competenceVOs != null) {
			competences = new ArrayList<SelectItem>(competenceVOs.size());
			Iterator<LecturerCompetenceVO> it = competenceVOs.iterator();
			while (it.hasNext()) {
				LecturerCompetenceVO competenceVO = it.next();
				competences.add(new SelectItem(competenceVO.getId().toString(), competenceVO.getName()));
			}
		} else {
			competences = new ArrayList<SelectItem>();
		}
		return competences;
	}

	public static ArrayList<SelectItem> getAllMaintenanceTypes() {
		ArrayList<SelectItem> maintenanceTypes;
		Collection<MaintenanceTypeVO> maintenanceTypeVOs = null;
		try {
			maintenanceTypeVOs = getServiceLocator().getSelectionSetService().getAllMaintenanceTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (maintenanceTypeVOs != null) {
			maintenanceTypes = new ArrayList<SelectItem>(maintenanceTypeVOs.size());
			Iterator<MaintenanceTypeVO> it = maintenanceTypeVOs.iterator();
			while (it.hasNext()) {
				MaintenanceTypeVO maintenanceTypeVO = it.next();
				maintenanceTypes.add(new SelectItem(maintenanceTypeVO.getId().toString(), maintenanceTypeVO.getName()));
			}
		} else {
			maintenanceTypes = new ArrayList<SelectItem>();
		}
		return maintenanceTypes;
	}

	public static ArrayList<SelectItem> getAllMassMailStatusTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<MassMailStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = getServiceLocator().getSelectionSetService().getAllMassMailStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<MassMailStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				MassMailStatusTypeVO statusTypeVO = it.next();
				statusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	public static ArrayList<SelectItem> getAllMassMailTypes() {
		ArrayList<SelectItem> massMailTypes;
		Collection<MassMailTypeVO> massMailTypeVOs = null;
		try {
			massMailTypeVOs = getServiceLocator().getSelectionSetService().getAllMassMailTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (massMailTypeVOs != null) {
			massMailTypes = new ArrayList<SelectItem>(massMailTypeVOs.size());
			Iterator<MassMailTypeVO> it = massMailTypeVOs.iterator();
			while (it.hasNext()) {
				MassMailTypeVO massMailTypeVO = it.next();
				massMailTypes.add(new SelectItem(massMailTypeVO.getId().toString(), massMailTypeVO.getName()));
			}
		} else {
			massMailTypes = new ArrayList<SelectItem>();
		}
		return massMailTypes;
	}

	public static ArrayList<SelectItem> getAllNotificationTypes() {
		ArrayList<SelectItem> notificationTypes;
		Collection<NotificationTypeVO> notificationTypeVOs = null;
		try {
			notificationTypeVOs = getServiceLocator().getSelectionSetService().getAllNotificationTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (notificationTypeVOs != null) {
			notificationTypes = new ArrayList<SelectItem>(notificationTypeVOs.size());
			Iterator<NotificationTypeVO> it = notificationTypeVOs.iterator();
			while (it.hasNext()) {
				NotificationTypeVO notificationTypeVO = it.next();
				notificationTypes.add(new SelectItem(notificationTypeVO.getId().toString(), notificationTypeVO.getName()));
			}
		} else {
			notificationTypes = new ArrayList<SelectItem>();
		}
		return notificationTypes;
	}

	public static String getAllowedFileExtensionsPattern(FileModule module, Boolean image) {
		String allowTypes = null;
		if (module != null) {
			try {
				allowTypes = getServiceLocator().getToolsService().getAllowedFileExtensionsPattern(module, image);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
			if (CommonUtil.isEmptyString(allowTypes)) {
				publishException(new FacesException("Cannot find file extensions from mime-types (" + module.toString() + ")"));
			}
		} else {
			return CommonUtil.DEFAULT_FILE_EXTENSION_PATTERN;
		}
		return allowTypes;
	}

	public static ArrayList<SelectItem> getAllPrivacyConsentStatusTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<PrivacyConsentStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = getServiceLocator().getSelectionSetService().getAllPrivacyConsentStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<PrivacyConsentStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				PrivacyConsentStatusTypeVO statusTypeVO = it.next();
				statusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	public static ArrayList<SelectItem> getAllProbandCategories() {
		ArrayList<SelectItem> categories;
		Collection<ProbandCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getAllProbandCategories(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<ProbandCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				ProbandCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getAllProbandListStatusTypes(Boolean person) {
		ArrayList<SelectItem> probandListStatusTypes;
		Collection<ProbandListStatusTypeVO> probandListStatusTypeVOs = null;
		try {
			probandListStatusTypeVOs = getServiceLocator().getSelectionSetService().getAllProbandListStatusTypes(getAuthentication(), person);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (probandListStatusTypeVOs != null) {
			probandListStatusTypes = new ArrayList<SelectItem>(probandListStatusTypeVOs.size());
			Iterator<ProbandListStatusTypeVO> it = probandListStatusTypeVOs.iterator();
			while (it.hasNext()) {
				ProbandListStatusTypeVO probandListStatusTypeVO = it.next();
				probandListStatusTypes.add(new SelectItem(probandListStatusTypeVO.getId().toString(), probandListStatusTypeVO.getName()));
			}
		} else {
			probandListStatusTypes = new ArrayList<SelectItem>();
		}
		return probandListStatusTypes;
	}

	public static ArrayList<SelectItem> getAllProbandStatusTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<ProbandStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = getServiceLocator().getSelectionSetService().getAllProbandStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<ProbandStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				ProbandStatusTypeVO statusTypeVO = it.next();
				statusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	public static ArrayList<SelectItem> getAllSponsoringTypes() {
		ArrayList<SelectItem> sponsoringTypes;
		Collection<SponsoringTypeVO> sponsoringTypeVOs = null;
		try {
			sponsoringTypeVOs = getServiceLocator().getSelectionSetService().getAllSponsoringTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (sponsoringTypeVOs != null) {
			sponsoringTypes = new ArrayList<SelectItem>(sponsoringTypeVOs.size());
			Iterator<SponsoringTypeVO> it = sponsoringTypeVOs.iterator();
			while (it.hasNext()) {
				SponsoringTypeVO sponsoringTypeVO = it.next();
				sponsoringTypes.add(new SelectItem(sponsoringTypeVO.getId().toString(), sponsoringTypeVO.getName()));
			}
		} else {
			sponsoringTypes = new ArrayList<SelectItem>();
		}
		return sponsoringTypes;
	}

	public static ArrayList<SelectItem> getAllStaffCategories() {
		ArrayList<SelectItem> categories;
		Collection<StaffCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getAllStaffCategories(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<StaffCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				StaffCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getAllStaffStatusTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<StaffStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = getServiceLocator().getSelectionSetService().getAllStaffStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<StaffStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				StaffStatusTypeVO statusTypeVO = it.next();
				statusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	public static ArrayList<SelectItem> getAllSurveyStatusTypes() {
		ArrayList<SelectItem> surveyStatusTypes;
		Collection<SurveyStatusTypeVO> surveyStatusTypeVOs = null;
		try {
			surveyStatusTypeVOs = getServiceLocator().getSelectionSetService().getAllSurveyStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (surveyStatusTypeVOs != null) {
			surveyStatusTypes = new ArrayList<SelectItem>(surveyStatusTypeVOs.size());
			Iterator<SurveyStatusTypeVO> it = surveyStatusTypeVOs.iterator();
			while (it.hasNext()) {
				SurveyStatusTypeVO surveyStatusTypeVO = it.next();
				surveyStatusTypes.add(new SelectItem(surveyStatusTypeVO.getId().toString(), surveyStatusTypeVO.getName()));
			}
		} else {
			surveyStatusTypes = new ArrayList<SelectItem>();
		}
		return surveyStatusTypes;
	}

	public static ArrayList<SelectItem> getAllTeamMemberRoles() {
		ArrayList<SelectItem> roles;
		Collection<TeamMemberRoleVO> roleVOs = null;
		try {
			roleVOs = getServiceLocator().getSelectionSetService().getAllTeamMemberRoles(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (roleVOs != null) {
			roles = new ArrayList<SelectItem>(roleVOs.size());
			Iterator<TeamMemberRoleVO> it = roleVOs.iterator();
			while (it.hasNext()) {
				TeamMemberRoleVO roleVO = it.next();
				roles.add(new SelectItem(roleVO.getId().toString(), roleVO.getName()));
			}
		} else {
			roles = new ArrayList<SelectItem>();
		}
		return roles;
	}

	public static ArrayList<SelectItem> getAllTimelineEventTypes() {
		ArrayList<SelectItem> timelineEventTypes;
		Collection<TimelineEventTypeVO> eventTypeVOs = null;
		try {
			eventTypeVOs = getServiceLocator().getSelectionSetService().getAllTimelineEventTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (eventTypeVOs != null) {
			timelineEventTypes = new ArrayList<SelectItem>(eventTypeVOs.size());
			Iterator<TimelineEventTypeVO> it = eventTypeVOs.iterator();
			while (it.hasNext()) {
				TimelineEventTypeVO eventTypeVO = it.next();
				timelineEventTypes.add(new SelectItem(eventTypeVO.getId().toString(), eventTypeVO.getName()));
			}
		} else {
			timelineEventTypes = new ArrayList<SelectItem>();
		}
		return timelineEventTypes;
	}

	public static ArrayList<SelectItem> getAllTrialStatusTypes() {
		ArrayList<SelectItem> statusTypes;
		Collection<TrialStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = getServiceLocator().getSelectionSetService().getAllTrialStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<TrialStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				TrialStatusTypeVO statusTypeVO = it.next();
				statusTypes.add(new SelectItem(statusTypeVO.getId().toString(), statusTypeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		return statusTypes;
	}

	public static ArrayList<SelectItem> getAllTrialTypes() {
		ArrayList<SelectItem> trialTypes;
		Collection<TrialTypeVO> trialTypeVOs = null;
		try {
			trialTypeVOs = getServiceLocator().getSelectionSetService().getAllTrialTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (trialTypeVOs != null) {
			trialTypes = new ArrayList<SelectItem>(trialTypeVOs.size());
			Iterator<TrialTypeVO> it = trialTypeVOs.iterator();
			while (it.hasNext()) {
				TrialTypeVO trialTypeVO = it.next();
				trialTypes.add(new SelectItem(trialTypeVO.getId().toString(), trialTypeVO.getName()));
			}
		} else {
			trialTypes = new ArrayList<SelectItem>();
		}
		return trialTypes;
	}

	public static ArrayList<SelectItem> getAllVisitTypes() {
		ArrayList<SelectItem> visitTypes;
		Collection<VisitTypeVO> visitTypeVOs = null;
		try {
			visitTypeVOs = getServiceLocator().getSelectionSetService().getAllVisitTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (visitTypeVOs != null) {
			visitTypes = new ArrayList<SelectItem>(visitTypeVOs.size());
			Iterator<VisitTypeVO> it = visitTypeVOs.iterator();
			while (it.hasNext()) {
				VisitTypeVO visitTypeVO = it.next();
				visitTypes.add(new SelectItem(visitTypeVO.getId().toString(), visitTypeVO.getName()));
			}
		} else {
			visitTypes = new ArrayList<SelectItem>();
		}
		return visitTypes;
	}

	public static AlphaIdVO getAlphaId(Long alphaIdId) {
		if (alphaIdId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getAlphaId(getAuthentication(), alphaIdId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getApplicationRootUrl() {
		try {
			HttpServletRequest request = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
			URL reconstructedUrl = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "");
			return reconstructedUrl.toString();
		} catch (Exception e) {
			return getHttpBaseUrl();
		}
	}

	public static ApplicationScopeBean getApplicationScopeBean(HttpServletRequest request) {
		if (request != null) {
			return getApplicationScopeBean(request.getSession());
		}
		return null;
	}

	public static ApplicationScopeBean getApplicationScopeBean(HttpSession session) {
		if (session != null) {
			return (ApplicationScopeBean) session.getServletContext().getAttribute("applicationScopeBean");
		}
		return null;
	}

	public static AspVO getAsp(Long aspId) {
		if (aspId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getAsp(getAuthentication(), aspId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static AspAtcCodeVO getAspAtcCode(Long aspAtcCodeId) {
		if (aspAtcCodeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getAspAtcCode(getAuthentication(), aspAtcCodeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static AspSubstanceVO getAspSubstance(Long aspSubstanceId) {
		if (aspSubstanceId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getAspSubstance(getAuthentication(), aspSubstanceId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static AuthenticationVO getAuthentication() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null) {
			return sessionScopeBean.getAuthentication();
		}
		return null;
	}

	public static ArrayList<SelectItem> getAuthenticationTypes() {
		ArrayList<SelectItem> methods;
		Collection<AuthenticationTypeVO> methodVOs = null;
		try {
			methodVOs = getServiceLocator().getSelectionSetService().getAuthenticationTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (methodVOs != null) {
			methods = new ArrayList<SelectItem>(methodVOs.size());
			Iterator<AuthenticationTypeVO> it = methodVOs.iterator();
			while (it.hasNext()) {
				AuthenticationTypeVO methodVO = it.next();
				methods.add(new SelectItem(methodVO.getMethod().name(), methodVO.getName()));
			}
		} else {
			methods = new ArrayList<SelectItem>();
		}
		return methods;
	}

	public static ArrayList<SelectItem> getAvailableHyperlinkCategories(HyperlinkModule module, Long categoryId) {
		ArrayList<SelectItem> categories;
		Collection<HyperlinkCategoryVO> categoryVOs = null;
		if (module != null) {
			try {
				categoryVOs = getServiceLocator().getSelectionSetService().getHyperlinkCategories(getAuthentication(), module, categoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<HyperlinkCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				HyperlinkCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getAvailableJobTypes(JobModule module, Long typeId, Long trialId) {
		ArrayList<SelectItem> types;
		Collection<JobTypeVO> typeVOs = null;
		if (module != null) {
			try {
				typeVOs = getServiceLocator().getSelectionSetService().getJobTypes(getAuthentication(), module, typeId, trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (typeVOs != null) {
			types = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<JobTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				JobTypeVO typeVO = it.next();
				types.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			types = new ArrayList<SelectItem>();
		}
		return types;
	}

	public static ArrayList<SelectItem> getAvailableJournalCategories(JournalModule module, Long categoryId) {
		ArrayList<SelectItem> categories;
		Collection<JournalCategoryVO> categoryVOs = null;
		if (module != null) {
			try {
				categoryVOs = getServiceLocator().getSelectionSetService().getJournalCategories(getAuthentication(), module, categoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<JournalCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				JournalCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getAvailableProbandAddressTypes(Long probandId, Long typeId) {
		ArrayList<SelectItem> types;
		Collection<AddressTypeVO> typeVOs = null;
		try {
			typeVOs = getServiceLocator().getSelectionSetService().getAvailableProbandAddressTypes(getAuthentication(), null, null, probandId, typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (typeVOs != null) {
			types = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<AddressTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				AddressTypeVO typeVO = it.next();
				types.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			types = new ArrayList<SelectItem>();
		}
		return types;
	}

	public static ArrayList<SelectItem> getAvailableProbandContactDetailTypes(Long probandId, Long typeId) {
		ArrayList<SelectItem> types;
		Collection<ContactDetailTypeVO> typeVOs = null;
		try {
			typeVOs = getServiceLocator().getSelectionSetService().getAvailableProbandContactDetailTypes(getAuthentication(), null, null, probandId, typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (typeVOs != null) {
			types = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<ContactDetailTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				ContactDetailTypeVO typeVO = it.next();
				types.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			types = new ArrayList<SelectItem>();
		}
		return types;
	}

	public static ArrayList<SelectItem> getAvailableStaffAddressTypes(Long staffId, Long typeId) {
		ArrayList<SelectItem> types;
		Collection<AddressTypeVO> typeVOs = null;
		try {
			typeVOs = getServiceLocator().getSelectionSetService().getAvailableStaffAddressTypes(getAuthentication(), staffId, typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (typeVOs != null) {
			types = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<AddressTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				AddressTypeVO typeVO = it.next();
				types.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			types = new ArrayList<SelectItem>();
		}
		return types;
	}

	public static ArrayList<SelectItem> getAvailableStaffContactDetailTypes(Long staffId, Long typeId) {
		ArrayList<SelectItem> types;
		Collection<ContactDetailTypeVO> typeVOs = null;
		try {
			typeVOs = getServiceLocator().getSelectionSetService().getAvailableStaffContactDetailTypes(getAuthentication(), staffId, typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (typeVOs != null) {
			types = new ArrayList<SelectItem>(typeVOs.size());
			Iterator<ContactDetailTypeVO> it = typeVOs.iterator();
			while (it.hasNext()) {
				ContactDetailTypeVO typeVO = it.next();
				types.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			types = new ArrayList<SelectItem>();
		}
		return types;
	}

	public static Long getBankAccountCount(Long probandId) {
		if (probandId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getBankAccountCount(WebUtil.getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public static String getBaseUrl(HttpServletRequest request) {
		StringBuffer url = new StringBuffer();
		url.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort());
		return url.toString();
	}

	public static Boolean getBooleanParamValue(GetParamNames paramName) {
		return stringToBoolean(getParamValue(paramName));
	}

	public static ArrayList<SelectItem> getBooleans(boolean inverse, boolean triState) {
		ArrayList<SelectItem> booleans;
		Collection<BooleanVO> booleanVOs = null;
		try {
			booleanVOs = getServiceLocator().getSelectionSetService().getBooleans(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (booleanVOs != null) {
			booleans = new ArrayList<SelectItem>(booleanVOs.size());
			Iterator<BooleanVO> it = booleanVOs.iterator();
			while (it.hasNext()) {
				BooleanVO booleanVO = it.next();
				booleans.add(new SelectItem(((Boolean) (inverse ? !booleanVO.getValue() : booleanVO.getValue())).toString(), booleanVO.getName()));
			}
			if (triState) {
				booleans.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
			}
		} else {
			booleans = new ArrayList<SelectItem>();
		}
		return booleans;
	}

	public static CalendarWeekVO getCalendarWeek(Date date) {
		try {
			return getServiceLocator().getToolsService().getCalendarWeek(date);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return null;
	}

	public static String getCalendarWeekString(Date date) {
		if (date != null) {
			CalendarWeekVO calendarWeek = getCalendarWeek(date);
			if (calendarWeek != null) {
				return Messages.getMessage(MessageCodes.CALENDAR_WEEK, Integer.toString(calendarWeek.getWeek()), Integer.toString(calendarWeek.getYear()));
			}
		}
		return null;
	}

	public static ContactDetailTypeVO getContactDetailType(Long detailTypeId) {
		if (detailTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getContactDetailType(getAuthentication(), detailTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static CourseOutVO getCourse(Long courseId, Integer maxInstances, Integer maxPrecedingCoursesDepth, Integer maxRenewalsDepth) {
		if (courseId != null) {
			try {
				return getServiceLocator().getCourseService().getCourse(getAuthentication(), courseId, maxInstances, maxPrecedingCoursesDepth, maxRenewalsDepth);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static CourseCategoryVO getCourseCategory(Long categoryId) {
		if (categoryId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getCourseCategory(getAuthentication(), categoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Boolean getCourseExpired(Date today, CourseInVO course) {
		if (course != null && course.isExpires()) {
			return getExpired(today, course.getStop(), course.getValidityPeriod(), course.getValidityPeriodDays());
		}
		return null;
	}

	public static Boolean getCourseExpired(Date today, CourseOutVO course) {
		Date expiration;
		if (course != null && (expiration = course.getExpiration()) != null) {
			return getExpired(today, expiration);
		}
		return null;
	}

	public static Long getCourseInventoryBookingCount(Long courseId) {
		if (courseId != null) {
			try {
				return getServiceLocator().getCourseService().getCourseInventoryBookingCount(getAuthentication(), courseId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Collection<CourseParticipationStatusEntryOutVO> getCourseParticipationStatusEntries(Long staffId, Long courseId) {
		try {
			return getServiceLocator().getCourseService().getCourseParticipationStatusEntryList(getAuthentication(), staffId, courseId, null, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return new ArrayList<CourseParticipationStatusEntryOutVO>();
	}

	public static CourseParticipationStatusEntryOutVO getCourseParticipationStatusEntry(Long courseParticipationStatusEntryId) {
		if (courseParticipationStatusEntryId != null) {
			try {
				return getServiceLocator().getCourseService().getCourseParticipationStatusEntry(getAuthentication(), courseParticipationStatusEntryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static CourseParticipationStatusEntryOutVO getCourseParticipationStatusEntry(Long staffId, Long courseId) {
		try {
			return getCourseParticipationStatusEntries(staffId, courseId).iterator().next();
		} catch (NoSuchElementException e) {
		}
		return null;
	}

	public static Long getCourseParticipationStatusEntryCount(Long staffId, Long courseId) {
		try {
			return getServiceLocator().getCourseService().getCourseParticipationStatusEntryCount(getAuthentication(), staffId, courseId, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return null;
	}

	public static CriteriaOutVO getCriteria(Long criteriaId) {
		if (criteriaId != null) {
			try {
				return getServiceLocator().getSearchService().getCriteria(getAuthentication(), criteriaId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getCurrencySymbol() {
		try {
			return getServiceLocator().getToolsService().getCurrencySymbol();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return "";
	}

	public static StreamedContent getCvPdfStreamedContent(Long staffId) throws Exception {
		if (staffId != null) {
			try {
				CvPDFVO cv = getServiceLocator().getStaffService().renderCvPDF(getAuthentication(), staffId);
				return new DefaultStreamedContent(new ByteArrayInputStream(cv.getDocumentDatas()), cv.getContentType().getMimeType(), cv.getFileName());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				throw e;
			} catch (AuthenticationException e) {
				publishException(e);
				throw e;
			}
		}
		return null;
	}

	public static StreamedContent getTrainingRecordPdfStreamedContent(Long staffId) throws Exception {
		if (staffId != null) {
			try {
				TrainingRecordPDFVO cv = getServiceLocator().getStaffService().renderTrainingRecordPDF(getAuthentication(), staffId);
				return new DefaultStreamedContent(new ByteArrayInputStream(cv.getDocumentDatas()), cv.getContentType().getMimeType(), cv.getFileName());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				throw e;
			} catch (AuthenticationException e) {
				publishException(e);
				throw e;
			}
		}
		return null;
	}

	public static CvPositionOutVO getCvPosition(Long cvPositionId) {
		if (cvPositionId != null) {
			try {
				return getServiceLocator().getStaffService().getCvPosition(getAuthentication(), cvPositionId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getCvPositionListCount(Long staffId) {
		if (staffId != null) {
			try {
				return getServiceLocator().getStaffService().getCvPositionCount(getAuthentication(), staffId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static CvSectionVO getCvSection(Long sectionId) {
		if (sectionId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getCvSection(getAuthentication(), sectionId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static TrainingRecordSectionVO getTrainingRecordSection(Long sectionId) {
		if (sectionId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getTrainingRecordSection(getAuthentication(), sectionId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getCvSections(Long sectionId) {
		ArrayList<SelectItem> cvSections;
		Collection<CvSectionVO> sectionVOs = null;
		try {
			sectionVOs = getServiceLocator().getSelectionSetService().getCvSections(getAuthentication(), sectionId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (sectionVOs != null) {
			cvSections = new ArrayList<SelectItem>(sectionVOs.size());
			Iterator<CvSectionVO> it = sectionVOs.iterator();
			while (it.hasNext()) {
				CvSectionVO sectionVO = it.next();
				cvSections.add(new SelectItem(sectionVO.getId().toString(), sectionVO.getName()));
			}
		} else {
			cvSections = new ArrayList<SelectItem>();
		}
		return cvSections;
	}

	public static ArrayList<SelectItem> getTrainingRecordSections(Long sectionId) {
		ArrayList<SelectItem> trainingRecordSections;
		Collection<TrainingRecordSectionVO> sectionVOs = null;
		try {
			sectionVOs = getServiceLocator().getSelectionSetService().getTrainingRecordSections(getAuthentication(), sectionId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (sectionVOs != null) {
			trainingRecordSections = new ArrayList<SelectItem>(sectionVOs.size());
			Iterator<TrainingRecordSectionVO> it = sectionVOs.iterator();
			while (it.hasNext()) {
				TrainingRecordSectionVO sectionVO = it.next();
				trainingRecordSections.add(new SelectItem(sectionVO.getId().toString(), sectionVO.getName()));
			}
		} else {
			trainingRecordSections = new ArrayList<SelectItem>();
		}
		return trainingRecordSections;
	}

	public static String getDateFormat() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null && sessionScopeBean.getLogon() != null) {
			return sessionScopeBean.getLogon().getUser().getDateFormat();
		}
		return null;
	}

	public static ArrayList<SelectItem> getDateFormats(String existing) {
		ArrayList<SelectItem> result;
		Collection<String> dateFormats = null;
		try {
			dateFormats = new LinkedHashSet<String>(getServiceLocator().getSelectionSetService().getDateFormats(getAuthentication()));
			if (existing != null && existing.length() > 0) {
				dateFormats.add(existing);
			}
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (dateFormats != null) {
			result = new ArrayList<SelectItem>(dateFormats.size());
			Iterator<String> it = dateFormats.iterator();
			while (it.hasNext()) {
				String dateFromat = it.next();
				result.add(new SelectItem(dateFromat, dateFromat));
			}
		} else {
			result = new ArrayList<SelectItem>();
		}
		return result;
	}

	public static ArrayList<SelectItem> getDBModules() {
		ArrayList<SelectItem> dbModules;
		Collection<DBModuleVO> dbModuleVOs = null;
		try {
			dbModuleVOs = getServiceLocator().getSelectionSetService().getDBModules(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (dbModuleVOs != null) {
			dbModules = new ArrayList<SelectItem>(dbModuleVOs.size());
			Iterator<DBModuleVO> it = dbModuleVOs.iterator();
			while (it.hasNext()) {
				DBModuleVO dbModuleVO = it.next();
				dbModules.add(new SelectItem(dbModuleVO.getModule().name(), dbModuleVO.getName()));
			}
		} else {
			dbModules = new ArrayList<SelectItem>();
		}
		return dbModules;
	}

	public static DBModuleVO getDBModuleVO(DBModule module) {
		if (module != null) {
			try {
				return getServiceLocator().getToolsService().getLocalizedDBModule(getAuthentication(), module);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getDecimalSeparator() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null && sessionScopeBean.getLogon() != null) {
			return sessionScopeBean.getLogon().getUser().getDecimalSeparator();
		}
		return null;
	}

	public static ArrayList<SelectItem> getDecimalSeparators() {
		ArrayList<SelectItem> result;
		Collection<String> deciamlSeparators = null;
		try {
			deciamlSeparators = getServiceLocator().getSelectionSetService().getDecimalSeparators(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (deciamlSeparators != null) {
			result = new ArrayList<SelectItem>(deciamlSeparators.size());
			Iterator<String> it = deciamlSeparators.iterator();
			while (it.hasNext()) {
				String deciamlSeparator = it.next();
				result.add(new SelectItem(deciamlSeparator, deciamlSeparator));
			}
		} else {
			result = new ArrayList<SelectItem>();
		}
		return result;
	}

	public static Locale getDefaultLocale() {
		try {
			return CommonUtil.localeFromString(getServiceLocator().getToolsService().getDefaultLocale());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Locale.getDefault();
	}

	public static TimeZone getDefaultTimeZone() {
		try {
			return CommonUtil.timeZoneFromString(getServiceLocator().getToolsService().getDefaultTimeZone());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return TimeZone.getDefault();
	}

	public static ArrayList<SelectItem> getDiagnoses(Long probandId) {
		ArrayList<SelectItem> diagnoses;
		Collection<DiagnosisOutVO> diagnosisVOs = null;
		if (probandId != null) {
			try {
				diagnosisVOs = getServiceLocator().getProbandService().getDiagnosisList(getAuthentication(), probandId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (diagnosisVOs != null) {
			diagnoses = new ArrayList<SelectItem>(diagnosisVOs.size());
			Iterator<DiagnosisOutVO> it = diagnosisVOs.iterator();
			while (it.hasNext()) {
				DiagnosisOutVO diagnosisVO = it.next();
				diagnoses.add(new SelectItem(Long.toString(diagnosisVO.getId()), Messages.getMessage(MessageCodes.DIAGNOSIS_SELECTITEM_LABEL, diagnosisVO.getName(),
						DateUtil.getDateStartStopString(diagnosisVO.getStart(), diagnosisVO.getStop()))));
			}
		} else {
			diagnoses = new ArrayList<SelectItem>();
		}
		return diagnoses;
	}

	public static Long getDiagnosisCount(Long probandId) {
		if (probandId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getDiagnosisCount(WebUtil.getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public static DutyRosterTurnOutVO getDutyRosterTurn(Long dutyRosterTurnId) {
		if (dutyRosterTurnId != null) {
			try {
				return getServiceLocator().getStaffService().getDutyRosterTurn(getAuthentication(), dutyRosterTurnId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getDutyRosterTurnFilterCalendars(Long trialDepartmentId, Long trialId, Long staffId) {
		ArrayList<SelectItem> filterCalendars;
		Collection<String> calendarStrings = null;
		try {
			calendarStrings = getServiceLocator().getTrialService().getCalendars(getAuthentication(), trialDepartmentId, staffId, trialId, null, null);
		} catch (AuthenticationException e) {
			publishException(e);
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
		}
		if (calendarStrings != null) {
			filterCalendars = new ArrayList<SelectItem>(calendarStrings.size());
			Iterator<String> it = calendarStrings.iterator();
			while (it.hasNext()) {
				String calendar = it.next();
				filterCalendars.add(new SelectItem(calendar, calendar));
			}
		} else {
			filterCalendars = new ArrayList<SelectItem>();
		}
		filterCalendars.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		return filterCalendars;
	}

	public static ArrayList<SelectItem> getDutyRosterTurnFilterTitles(Long trialDepartmentId, Long trialId, Long staffId) {
		ArrayList<SelectItem> filterTitles;
		Collection<String> titleStrings = null;
		try {
			titleStrings = getServiceLocator().getTrialService().getDutyRosterTurnTitles(getAuthentication(), trialDepartmentId, staffId, trialId, null, null);
		} catch (AuthenticationException e) {
			publishException(e);
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
		}
		if (titleStrings != null) {
			filterTitles = new ArrayList<SelectItem>(titleStrings.size());
			Iterator<String> it = titleStrings.iterator();
			while (it.hasNext()) {
				String title = it.next();
				filterTitles.add(new SelectItem(title, title));
			}
		} else {
			filterTitles = new ArrayList<SelectItem>();
		}
		filterTitles.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		return filterTitles;
	}

	public static ECRFOutVO getEcrf(Long ecrfId) {
		if (ecrfId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrf(getAuthentication(), ecrfId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getEcrfCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfCount(getAuthentication(), trialId, null, null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ECRFFieldOutVO getEcrfField(Long ecrfFieldId) {
		if (ecrfFieldId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfField(getAuthentication(), ecrfFieldId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getEcrfFieldCount(Long trialId, Long ecrfId) {
		if (trialId != null || ecrfId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfFieldCount(getAuthentication(), trialId, ecrfId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ECRFFieldStatusEntryOutVO getEcrfFieldStatusEntry(Long id) {
		if (id != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfFieldStatusEntry(getAuthentication(), id);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getEcrfFieldStatusEntryCount(ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId, Long ecrfId, boolean last) {
		if (trialId != null || probandListEntryId != null || ecrfId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfFieldStatusEntryCount(getAuthentication(), queue, trialId, probandListEntryId, ecrfId, last);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getEcrfFieldStatusQueueName(ECRFFieldStatusQueue queue) {
		if (queue != null) {
			switch (queue) {
				case ANNOTATION:
					return Messages.getString(MessageCodes.ANNOTATION_ECRF_FIELD_STATUS_QUEUE_NAME);
				case VALIDATION:
					return Messages.getString(MessageCodes.VALIDATION_ECRF_FIELD_STATUS_QUEUE_NAME);
				case QUERY:
					return Messages.getString(MessageCodes.QUERY_ECRF_FIELD_STATUS_QUEUE_NAME);
				default:
			}
		}
		return "";
	}

	public static ECRFFieldValueOutVO getEcrfFieldValue(Long id) {
		if (id != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfFieldValueById(getAuthentication(), id);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getEcrfFieldValueCount(ECRFFieldOutVO ecrfField, boolean excludeAuditTrail) {
		if (ecrfField != null) {
			try {
				return getServiceLocator().getInputFieldService().getEcrfFieldValueCount(getAuthentication(), ecrfField.getId(), excludeAuditTrail);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ECRFProgressVO getEcrfProgress(Long ecrfId, Long listEntryId, boolean sectionDetail) {
		if (ecrfId != null && listEntryId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfProgress(getAuthentication(), listEntryId, ecrfId, sectionDetail);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ECRFProgressSummaryVO getEcrfProgressSummary(Long listEntryId, boolean ecrfDetail, boolean sectionDetail) {
		if (listEntryId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfProgressSummary(getAuthentication(), listEntryId, ecrfDetail, sectionDetail);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Integer getEcrfSectionHashCode(ECRFFieldStatusEntryOutVO ecrfFieldStatusEntry) {
		if (ecrfFieldStatusEntry != null) {
			return new HashCodeBuilder(1249046965, -82296885)
					.append(ecrfFieldStatusEntry.getListEntry().getId())
					.append(ecrfFieldStatusEntry.getEcrfField().getEcrf().getId())
					.append(ecrfFieldStatusEntry.getEcrfField().getSection())
					.append(ecrfFieldStatusEntry.getIndex())
					.toHashCode();
		}
		return null;
	}

	public static Integer getEcrfSectionHashCode(Long ecrfFieldStatusEntryId) {
		return getEcrfSectionHashCode(getEcrfFieldStatusEntry(ecrfFieldStatusEntryId));
	}

	public static ECRFStatusEntryVO getEcrfStatusEntry(Long ecrfId, Long listEntryId) {
		if (ecrfId != null && listEntryId != null) {
			try {
				return getServiceLocator().getTrialService().getEcrfStatusEntry(getAuthentication(), ecrfId, listEntryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ECRFStatusTypeVO getEcrfStatusType(Long statusTypeId) {
		if (statusTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getEcrfStatusType(getAuthentication(), statusTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Collection<ECRFStatusTypeVO> getEcrfStatusTypes() {
		try {
			return getServiceLocator().getSelectionSetService().getAllEcrfStatusTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<ECRFStatusTypeVO>();
	}

	public static String getEmailDomainName() {
		try {
			return getServiceLocator().getToolsService().getEmailDomainName();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return "";
	}

	public static EmailMessageVO getEmailMessage(Long massMailRecipientId) {
		if (massMailRecipientId != null) {
			try {
				return getServiceLocator().getMassMailService().getEmailMessage(getAuthentication(), massMailRecipientId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static <T extends Enum<T>> ArrayList<Enum<T>> getEnumList(String value, Class<T> enumeration) {
		ArrayList<Enum<T>> result;
		if (value != null && value.length() > 0) {
			String[] list = EL_ENUM_LIST_REGEXP.split(value, -1);
			result = new ArrayList<Enum<T>>(list.length);
			for (int i = 0; i < list.length; i++) {
				String name = list[i].trim();
				if (name.length() > 0) {
					result.add(Enum.valueOf(enumeration, name));
				}
			}
		} else {
			result = new ArrayList<Enum<T>>();
		}
		return result;
	}

	public static ArrayList<SelectItem> getEventImportances() {
		ArrayList<SelectItem> importances;
		Collection<EventImportanceVO> importanceVOs = null;
		try {
			importanceVOs = getServiceLocator().getSelectionSetService().getEventImportances(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (importanceVOs != null) {
			importances = new ArrayList<SelectItem>(importanceVOs.size());
			Iterator<EventImportanceVO> it = importanceVOs.iterator();
			while (it.hasNext()) {
				EventImportanceVO importanceVO = it.next();
				importances.add(new SelectItem(importanceVO.getImportance().name(), importanceVO.getName()));
			}
		} else {
			importances = new ArrayList<SelectItem>();
		}
		return importances;
	}

	public static String getExpirationDueInString(Date today, Date expiration) {
		Boolean expired = getExpired(today, expiration);
		if (expired != null) {
			if (expired) {
				return Messages.getMessage(MessageCodes.OVERDUE_LABEL, DateUtil.getExpirationDurationString(expiration, today));
			} else {
				return DateUtil.getExpirationDurationString(today, expiration);
			}
		}
		return "";
	}

	private static Boolean getExpired(Date today, Date expiration) {
		if (today != null && expiration != null) {
			if (today.compareTo(expiration) > 0) {
				return true;
			} else {
				return false;
			}
		}
		return null;
	}

	public static Boolean getExpired(Date today, Date date, VariablePeriod validityPeriod, Long validityPeriodDays) {
		if (today != null && date != null && validityPeriod != null) {
			return getExpired(today, addIntervals(date, validityPeriod, validityPeriodDays, 1));
		}
		return null;
	}

	public static String getHttpBaseUrl() {
		try {
			return getServiceLocator().getToolsService().getHttpBaseUrl();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return "";
	}

	public static String getHttpDomainName() {
		try {
			return getServiceLocator().getToolsService().getHttpDomainName();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return "";
	}

	public static String getHttpHost() {
		try {
			return getServiceLocator().getToolsService().getHttpHost();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return "";
	}

	public static String getHttpScheme() {
		try {
			return getServiceLocator().getToolsService().getHttpScheme();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return "";
	}

	public static Long getHyperlinkCount(HyperlinkModule module, Long id) {
		if (module != null && id != null) {
			try {
				return getServiceLocator().getHyperlinkService().getHyperlinkCount(getAuthentication(), module, id, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getJobCount(JobModule module, Long id) {
		if (module != null && id != null) {
			try {
				return getServiceLocator().getJobService().getJobCount(getAuthentication(), module, id);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getIdentityString(UserOutVO user) {
		if (user != null) {
			StaffOutVO identity = user.getIdentity();
			if (identity != null) {
				return CommonUtil.staffOutVOToString(identity);
			} else {
				return CommonUtil.userOutVOToString(user);
			}
		}
		return "";
	}

	public static String getIdentityUserString(UserOutVO user) {
		if (user != null) {
			StaffOutVO identity = user.getIdentity();
			if (identity != null) {
				return Messages.getMessage(MessageCodes.USER_IDENTITY_LABEL, CommonUtil.staffOutVOToString(identity), CommonUtil.userOutVOToString(user));
			} else {
				return CommonUtil.userOutVOToString(user);
			}
		}
		return "";
	}

	public static InputFieldOutVO getInputField(Long inputFieldId) {
		if (inputFieldId != null) {
			try {
				return getServiceLocator().getInputFieldService().getInputField(getAuthentication(), inputFieldId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getInputFieldIcon(InputFieldOutVO inputField) {
		if (inputField != null) {
			return INPUT_FIELD_TYPE_ICON_STYLECLASS_PREFIX + inputField.getFieldType().getType().getValue();
		}
		return "";
	}

	public static InputFieldSelectionSetValueOutVO getInputFieldSelectionSetValue(Long selectionSetValueId) {
		if (selectionSetValueId != null) {
			try {
				return getServiceLocator().getInputFieldService().getSelectionSetValue(getAuthentication(), selectionSetValueId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getInputFieldTypes() {
		ArrayList<SelectItem> fieldTypes;
		Collection<InputFieldTypeVO> fieldTypeVOs = null;
		try {
			fieldTypeVOs = getServiceLocator().getSelectionSetService().getInputFieldTypes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (fieldTypeVOs != null) {
			fieldTypes = new ArrayList<SelectItem>(fieldTypeVOs.size());
			Iterator<InputFieldTypeVO> it = fieldTypeVOs.iterator();
			while (it.hasNext()) {
				InputFieldTypeVO fieldTypeVO = it.next();
				fieldTypes.add(new SelectItem(fieldTypeVO.getType().name(), fieldTypeVO.getName()));
			}
		} else {
			fieldTypes = new ArrayList<SelectItem>();
		}
		return fieldTypes;
	}

	public static InquiryOutVO getInquiry(Long inquiryId) {
		if (inquiryId != null) {
			try {
				return getServiceLocator().getTrialService().getInquiry(getAuthentication(), inquiryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getInquiryCount(Long trialId, Boolean active, Boolean signupActive) {
		if (trialId != null) {
			try {
				return getServiceLocator().getProbandService().getInquiryCount(getAuthentication(), trialId, active, signupActive);
			} catch (AuthenticationException e) {
				publishException(e);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			}
		}
		return null;
	}

	public static InquiryValueOutVO getInquiryValue(Long id) {
		if (id != null) {
			try {
				return getServiceLocator().getProbandService().getInquiryValueById(getAuthentication(), id);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getInquiryValueCount(InquiryOutVO inquiry) {
		if (inquiry != null) {
			try {
				return getServiceLocator().getInputFieldService().getInquiryValueCount(getAuthentication(), inquiry.getId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getInstanceName() {
		try {
			return getServiceLocator().getToolsService().getInstanceName();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return "";
	}

	public static InventoryOutVO getInventory(Long inventoryId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) {
		if (inventoryId != null) {
			try {
				return getServiceLocator().getInventoryService().getInventory(getAuthentication(), inventoryId, maxInstances, maxParentDepth, maxChildrenDepth);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static InventoryBookingOutVO getInventoryBooking(Long inventoryBookingId) {
		if (inventoryBookingId != null) {
			try {
				return getServiceLocator().getInventoryService().getInventoryBooking(getAuthentication(), inventoryBookingId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getInventoryBookingCount(Long inventoryId) {
		if (inventoryId != null) {
			try {
				return getServiceLocator().getInventoryService().getInventoryBookingCount(getAuthentication(), inventoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getInventoryBookingFilterCalendars(Long inventoryDepartmentId, Long inventoryId, Long probandId, Long courseId, Long trialId) {
		ArrayList<SelectItem> filterCalendars;
		Collection<String> calendarStrings = null;
		try {
			calendarStrings = getServiceLocator().getInventoryService().getCalendars(getAuthentication(), inventoryDepartmentId, inventoryId, probandId, courseId, trialId, null,
					null);
		} catch (AuthenticationException e) {
			publishException(e);
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
		}
		if (calendarStrings != null) {
			filterCalendars = new ArrayList<SelectItem>(calendarStrings.size());
			Iterator<String> it = calendarStrings.iterator();
			while (it.hasNext()) {
				String calendar = it.next();
				filterCalendars.add(new SelectItem(calendar, calendar));
			}
		} else {
			filterCalendars = new ArrayList<SelectItem>();
		}
		filterCalendars.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		return filterCalendars;
	}

	public static InventoryCategoryVO getInventoryCategory(Long categoryId) {
		if (categoryId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getInventoryCategory(getAuthentication(), categoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static InventoryStatusEntryOutVO getInventoryStatusEntry(Long inventoryStatusEntryId) {
		if (inventoryStatusEntryId != null) {
			try {
				return getServiceLocator().getInventoryService().getInventoryStatusEntry(getAuthentication(), inventoryStatusEntryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getInventoryStatusEntryCount(Long inventoryId) {
		if (inventoryId != null) {
			try {
				return getServiceLocator().getInventoryService().getInventoryStatusEntryCount(getAuthentication(), inventoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static InventoryStatusTypeVO getInventoryStatusType(Long statusTypeId) {
		if (statusTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getInventoryStatusType(getAuthentication(), statusTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getInventoryTagValueCount(Long inventoryId) {
		if (inventoryId != null) {
			try {
				return getServiceLocator().getInventoryService().getInventoryTagValueCount(getAuthentication(), inventoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static JournalCategoryVO getJournalCategory(Long categoryId) {
		if (categoryId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getJournalCategory(getAuthentication(), categoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getJournalCount(JournalModule module, Long id) {
		if (module != null && id != null) {
			try {
				return getServiceLocator().getJournalService().getJournalCount(getAuthentication(), module, id);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static JournalEntryOutVO getJournalEntry(Long journalEntryId) {
		if (journalEntryId != null) {
			try {
				return getServiceLocator().getJournalService().getJournalEntry(getAuthentication(), journalEntryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static JournalModuleVO getJournalModule(JournalModule module) {
		if (module != null) {
			try {
				return getServiceLocator().getToolsService().getLocalizedJournalModule(getAuthentication(), module);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getJournalModules() {
		ArrayList<SelectItem> journalModules;
		Collection<JournalModuleVO> journalModuleVOs = null;
		try {
			journalModuleVOs = getServiceLocator().getSelectionSetService().getJournalModules(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (journalModuleVOs != null) {
			journalModules = new ArrayList<SelectItem>(journalModuleVOs.size());
			Iterator<JournalModuleVO> it = journalModuleVOs.iterator();
			while (it.hasNext()) {
				JournalModuleVO journalModuleVO = it.next();
				journalModules.add(new SelectItem(journalModuleVO.getModule().name(), journalModuleVO.getName()));
			}
		} else {
			journalModules = new ArrayList<SelectItem>();
		}
		return journalModules;
	}

	public static LecturerOutVO getLecturer(Long lecturerId) {
		if (lecturerId != null) {
			try {
				return getServiceLocator().getCourseService().getLecturer(getAuthentication(), lecturerId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getLecturerCount(Long courseId, Long competenceId) {
		if (courseId != null || competenceId != null) {
			try {
				return getServiceLocator().getCourseService().getLecturerCount(getAuthentication(), courseId, competenceId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Locale getLocale() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null) {
			return sessionScopeBean.getLocale();
		}
		return null;
	}

	public static ArrayList<SelectItem> getLocales() {
		Collection<LocaleVO> localeVOs = null;
		ArrayList<SelectItem> locales;
		try {
			localeVOs = getServiceLocator().getSelectionSetService().getLocales(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (localeVOs != null) {
			locales = new ArrayList<SelectItem>(localeVOs.size());
			Iterator<LocaleVO> it = localeVOs.iterator();
			while (it.hasNext()) {
				LocaleVO locale = it.next();
				locales.add(new SelectItem(locale.getLanguage(), locale.getName()));
			}
		} else {
			locales = new ArrayList<SelectItem>();
		}
		return locales;
	}

	public static Long getLongParamValue(GetParamNames paramName) {
		return stringToLong(getParamValue(paramName));
	}

	public static MaintenanceScheduleItemOutVO getMaintenanceScheduleItem(Long maintenanceScheduleItemId) {
		if (maintenanceScheduleItemId != null) {
			try {
				return getServiceLocator().getInventoryService().getMaintenanceScheduleItem(getAuthentication(), maintenanceScheduleItemId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getMaintenanceScheduleItemCount(Long inventoryId) {
		if (inventoryId != null) {
			try {
				return getServiceLocator().getInventoryService().getMaintenanceScheduleItemCount(getAuthentication(), inventoryId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static MaintenanceTypeVO getMaintenanceType(Long maintenanceTypeId) {
		if (maintenanceTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getMaintenanceType(getAuthentication(), maintenanceTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static MassMailOutVO getMassMail(Long massMailId) {
		if (massMailId != null) {
			try {
				return getServiceLocator().getMassMailService().getMassMail(getAuthentication(), massMailId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static MassMailProgressVO getMassMailProgress(Long massMailId) {
		if (massMailId != null) {
			try {
				return getServiceLocator().getMassMailService().getMassMailProgress(getAuthentication(), massMailId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getMassMailProgressLabel(MassMailOutVO massMail, MassMailProgressVO massMailProgress) {
		if (massMailProgress != null && massMailProgress.getRecipientTotalCount() > 0l) {
			return Messages.getMessage(MessageCodes.MASS_MAIL_PROGRESS_LABEL, massMailProgress.getRecipientTotalCount() - massMailProgress.getRecipientPendingCount(),
					massMailProgress.getRecipientTotalCount());
		}
		return null;
	}

	public static int getMassMailProgressValue(MassMailOutVO massMail, MassMailProgressVO massMailProgress) {
		if (massMailProgress != null && massMailProgress.getRecipientTotalCount() > 0l) {
			return Math.round(((float) Settings.getInt(SettingCodes.PROGRESS_BAR_MAX_VALUE, Bundle.SETTINGS, DefaultSettings.PROGRESS_BAR_MAX_VALUE)
					* (massMailProgress.getRecipientTotalCount() - massMailProgress.getRecipientPendingCount())) / (massMailProgress.getRecipientTotalCount()));
		}
		return 0;
	}

	public static MassMailRecipientOutVO getMassMailRecipient(Long massMailRecipientId) {
		if (massMailRecipientId != null) {
			try {
				return getServiceLocator().getMassMailService().getMassMailRecipient(getAuthentication(), massMailRecipientId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getMassMailRecipientCount(Long massMailId, Long probandId, boolean pending) {
		if (massMailId != null || probandId != null) {
			try {
				return getServiceLocator().getMassMailService().getMassMailRecipientCount(getAuthentication(), massMailId, probandId, pending);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static MassMailStatusTypeVO getMassMailStatusType(Long statusTypeId) {
		if (statusTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getMassMailStatusType(getAuthentication(), statusTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getMedicationCount(Long probandId, Long diagnosisId, Long procedureId) {
		if (probandId != null || diagnosisId != null || procedureId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getMedicationCount(WebUtil.getAuthentication(), probandId, diagnosisId, procedureId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public static String getModifiedAnnotation(long version, UserOutVO user, Date timestamp) {
		if (version == 0) {
			if (user != null) {
				StaffOutVO identity = user.getIdentity();
				if (identity != null) {
					return Messages.getMessage(MessageCodes.CREATED, CommonUtil.staffOutVOToString(identity), DateUtil.getDateTimeFormat().format(timestamp));
				} else {
					return Messages.getMessage(MessageCodes.CREATED, CommonUtil.userOutVOToString(user), DateUtil.getDateTimeFormat().format(timestamp));
				}
			} else {
				return Messages.getMessage(MessageCodes.CREATED_NO_USER, DateUtil.getDateTimeFormat().format(timestamp));
			}
		} else {
			if (user != null) {
				StaffOutVO identity = user.getIdentity();
				if (identity != null) {
					return Messages.getMessage(MessageCodes.LAST_MODIFIED, version, CommonUtil.staffOutVOToString(identity), DateUtil.getDateTimeFormat().format(timestamp));
				} else {
					return Messages.getMessage(MessageCodes.LAST_MODIFIED, version, CommonUtil.userOutVOToString(user), DateUtil.getDateTimeFormat().format(timestamp));
				}
			} else {
				return Messages.getMessage(MessageCodes.LAST_MODIFIED_NO_USER, version, DateUtil.getDateTimeFormat().format(timestamp));
			}
		}
	}

	public static boolean getModuleEnabled(DBModule module) {
		if (module != null) {
			switch (module) {
				case INVENTORY_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_INVENTORY_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_INVENTORY_MODULE) &&
							getUser().getEnableInventoryModule();
				case STAFF_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_STAFF_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_STAFF_MODULE) &&
							getUser().getEnableStaffModule();
				case COURSE_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_COURSE_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_COURSE_MODULE) &&
							getUser().getEnableCourseModule();
				case TRIAL_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_TRIAL_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_TRIAL_MODULE) &&
							getUser().getEnableTrialModule();
				case PROBAND_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_PROBAND_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_PROBAND_MODULE) &&
							getUser().getEnableProbandModule();
				case INPUT_FIELD_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_INPUT_FIELD_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_INPUT_FIELD_MODULE) &&
							getUser().getEnableInputFieldModule();
				case USER_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_USER_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_USER_MODULE) &&
							getUser().getEnableUserModule();
				case MASS_MAIL_DB:
					return Settings.getBoolean(SettingCodes.ENABLE_MASS_MAIL_MODULE, Bundle.SETTINGS, DefaultSettings.ENABLE_MASS_MAIL_MODULE) &&
							getUser().getEnableMassMailModule();
				default:
			}
		}
		return false;
	}

	public static Long getMoneyTransferCount(Long probandId, Long bankAccountId) {
		if (probandId != null || bankAccountId != null) {
			try {
				return getServiceLocator().getProbandService().getMoneyTransferCount(getAuthentication(), probandId, bankAccountId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getMoneyTransferFilterCostTypes(Long trialDepartmentId, Long trialId, Long probandDepartmentId, Long probandId) {
		ArrayList<SelectItem> filterCostTypes;
		Collection<String> costTypesStrings = null;
		try {
			costTypesStrings = getServiceLocator().getProbandService().getCostTypes(getAuthentication(), trialDepartmentId, trialId, probandDepartmentId, probandId, null);
		} catch (AuthenticationException e) {
			publishException(e);
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
		}
		if (costTypesStrings != null) {
			filterCostTypes = new ArrayList<SelectItem>(costTypesStrings.size());
			Iterator<String> it = costTypesStrings.iterator();
			while (it.hasNext()) {
				String costType = it.next();
				if (!CommonUtil.isEmptyString(costType)) {
					filterCostTypes.add(new SelectItem(costType, costType));
				}
			}
		} else {
			filterCostTypes = new ArrayList<SelectItem>();
		}
		filterCostTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		return filterCostTypes;
	}

	public static PasswordInVO getNewPassword() {
		PasswordInVO newPassword = null;
		try {
			newPassword = getServiceLocator().getToolsService().getNewPassword(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (newPassword == null) {
			newPassword = new PasswordInVO();
		}
		return newPassword;
	}

	public static String getNoCoursePickedMessage() {
		return Messages.getString(MessageCodes.NO_COURSE_PICKED);
	}

	public static String getNoInputFieldPickedMessage() {
		return Messages.getString(MessageCodes.NO_INPUT_FIELD_PICKED);
	}

	public static String getNoInventoryPickedMessage() {
		return Messages.getString(MessageCodes.NO_INVENTORY_PICKED);
	}

	public static String getNoMassMailPickedMessage() {
		return Messages.getString(MessageCodes.NO_MASS_MAIL_PICKED);
	}

	public static String getNoProbandPickedMessage() {
		return Messages.getString(MessageCodes.NO_PROBAND_PICKED);
	}

	public static String getNoStaffPickedMessage() {
		return Messages.getString(MessageCodes.NO_STAFF_PICKED);
	}

	public static String getNoTrialPickedMessage() {
		return Messages.getString(MessageCodes.NO_TRIAL_PICKED);
	}

	public static String getNoUserPickedMessage() {
		return Messages.getString(MessageCodes.NO_USER_PICKED);
	}

	public static OpsCodeVO getOpsCode(Long opsCodeId) {
		if (opsCodeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getOpsCode(getAuthentication(), opsCodeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	private static String getParamValue(FacesContext context, String paramName) {
		if (context != null && paramName != null && paramName.length() > 0) {
			return context.getExternalContext().getRequestParameterMap().get(paramName);
		}
		return null;
	}

	public static String getParamValue(GetParamNames paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return getParamValue(context, paramName.toString());
	}

	public static ArrayList<SelectItem> getParticipationTrials(Long probandId) {
		Collection<TrialOutVO> trialVOs = null;
		ArrayList<SelectItem> trials;
		if (probandId != null) {
			try {
				trialVOs = getServiceLocator().getProbandService().getParticipationTrials(getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (trialVOs != null) {
			trials = new ArrayList<SelectItem>(trialVOs.size());
			Iterator<TrialOutVO> it = trialVOs.iterator();
			while (it.hasNext()) {
				TrialOutVO trialVO = it.next();
				trials.add(new SelectItem(Long.toString(trialVO.getId()), CommonUtil.trialOutVOToString(trialVO)));
			}
		} else {
			trials = new ArrayList<SelectItem>();
		}
		return trials;
	}

	public static LinkedHashMap<PaymentMethod, PaymentMethodVO> getPaymentMethodMap() {
		LinkedHashMap<PaymentMethod, PaymentMethodVO> paymentMethods;
		Collection<PaymentMethodVO> paymentMethodVOs = null;
		try {
			paymentMethodVOs = getServiceLocator().getSelectionSetService().getPaymentMethods(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (paymentMethodVOs != null) {
			paymentMethods = new LinkedHashMap<PaymentMethod, PaymentMethodVO>(paymentMethodVOs.size());
			Iterator<PaymentMethodVO> it = paymentMethodVOs.iterator();
			while (it.hasNext()) {
				PaymentMethodVO paymentMethodVO = it.next();
				paymentMethods.put(paymentMethodVO.getPaymentMethod(), paymentMethodVO);
			}
		} else {
			paymentMethods = new LinkedHashMap<PaymentMethod, PaymentMethodVO>();
		}
		return paymentMethods;
	}

	public static ArrayList<SelectItem> getPaymentMethods() {
		ArrayList<SelectItem> paymentMethods;
		Collection<PaymentMethodVO> paymentMethodVOs = null;
		try {
			paymentMethodVOs = getServiceLocator().getSelectionSetService().getPaymentMethods(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (paymentMethodVOs != null) {
			paymentMethods = new ArrayList<SelectItem>(paymentMethodVOs.size());
			Iterator<PaymentMethodVO> it = paymentMethodVOs.iterator();
			while (it.hasNext()) {
				PaymentMethodVO paymentMethodVO = it.next();
				paymentMethods.add(new SelectItem(paymentMethodVO.getPaymentMethod().name(), paymentMethodVO.getName()));
			}
		} else {
			paymentMethods = new ArrayList<SelectItem>();
		}
		return paymentMethods;
	}

	public static ProbandOutVO getProband(Long probandId, Integer maxInstances, Integer maxParentsDepth, Integer maxChildrenDepth) {
		if (probandId != null) {
			try {
				return getServiceLocator().getProbandService().getProband(getAuthentication(), probandId, maxInstances, maxParentsDepth, maxChildrenDepth);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandAddressCount(Long probandId) {
		if (probandId != null) {
			try {
				return getServiceLocator().getProbandService().getProbandAddressCount(getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ProbandCategoryVO getProbandCategory(Long categoryId) {
		if (categoryId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getProbandCategory(getAuthentication(), categoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandContactDetailValueCount(Long probandId) {
		if (probandId != null) {
			try {
				return getServiceLocator().getProbandService().getProbandContactDetailValueCount(getAuthentication(), probandId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ProbandGroupOutVO getProbandGroup(Long probandGroupId) {
		if (probandGroupId != null) {
			try {
				return getServiceLocator().getTrialService().getProbandGroup(getAuthentication(), probandGroupId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandGroupCount(Long trialId) {
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getProbandGroupCount(WebUtil.getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getProbandGroups(Long trialId) {
		ArrayList<SelectItem> probandGroups;
		Collection<ProbandGroupOutVO> probandGroupVOs = null;
		if (trialId != null) {
			try {
				probandGroupVOs = getServiceLocator().getTrialService().getProbandGroupList(getAuthentication(), trialId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (probandGroupVOs != null) {
			probandGroups = new ArrayList<SelectItem>(probandGroupVOs.size());
			Iterator<ProbandGroupOutVO> it = probandGroupVOs.iterator();
			while (it.hasNext()) {
				ProbandGroupOutVO probandGroupVO = it.next();
				probandGroups.add(new SelectItem(Long.toString(probandGroupVO.getId()), probandGroupVO.getTitle()));
			}
		} else {
			probandGroups = new ArrayList<SelectItem>();
		}
		return probandGroups;
	}

	public static Long getProbandInventoryBookingCount(Long probandId) {
		if (probandId != null) {
			try {
				return getServiceLocator().getProbandService().getProbandInventoryBookingCount(getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static StreamedContent getProbandLettersPdfStreamedContent(Long probandId) throws Exception {
		if (probandId != null) {
			try {
				ProbandLetterPDFVO probandLetter = getServiceLocator().getProbandService().renderProbandLettersPDF(getAuthentication(), probandId);
				return new DefaultStreamedContent(new ByteArrayInputStream(probandLetter.getDocumentDatas()), probandLetter.getContentType().getMimeType(),
						probandLetter.getFileName());
			} catch (AuthenticationException e) {
				publishException(e);
				throw e;
			} catch (AuthorisationException e) {
				throw e;
			} catch (ServiceException e) {
				throw e;
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	public static ProbandListEntryOutVO getProbandListEntry(Long probandListEntryId) {
		if (probandListEntryId != null) {
			try {
				return getServiceLocator().getTrialService().getProbandListEntry(getAuthentication(), probandListEntryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandListEntryCount(Long trialId, Long probandId, boolean total) {
		if (trialId != null || probandId != null) {
			try {
				return getServiceLocator().getTrialService().getProbandListEntryCount(getAuthentication(), trialId, null, probandId, total);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ProbandListEntryTagOutVO getProbandListEntryTag(Long probandListEntryTagId) {
		if (probandListEntryTagId != null) {
			try {
				return getServiceLocator().getTrialService().getProbandListEntryTag(getAuthentication(), probandListEntryTagId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandListEntryTagCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getProbandListEntryTagCount(getAuthentication(), trialId);
			} catch (AuthenticationException e) {
				publishException(e);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			}
		}
		return null;
	}

	public static ProbandListEntryTagValueOutVO getProbandListEntryTagValue(Long id) {
		if (id != null) {
			try {
				return getServiceLocator().getTrialService().getProbandListEntryTagValueById(getAuthentication(), id);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandListEntryTagValueCount(ProbandListEntryTagOutVO probandListEntryTag) {
		if (probandListEntryTag != null) {
			try {
				return getServiceLocator().getInputFieldService().getProbandListEntryTagValueCount(getAuthentication(), probandListEntryTag.getId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static MoneyTransferSummaryVO getProbandOpenReimbursementSummary(Long trialId, Long probandId, PaymentMethod method) {
		if (probandId != null) {
			try {
				return getServiceLocator().getTrialService().getProbandMoneyTransferNoParticipationSummary(getAuthentication(), probandId, trialId, null, method, false);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static MoneyTransferSummaryVO getProbandOpenReimbursementSummary(ProbandListEntryOutVO listEntry, PaymentMethod method) {
		if (listEntry != null) {
			try {
				return getServiceLocator().getTrialService().getProbandMoneyTransferSummary(getAuthentication(), listEntry.getId(), null, method, false);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ProbandStatusEntryOutVO getProbandStatusEntry(Long probandStatusEntryId) {
		if (probandStatusEntryId != null) {
			try {
				return getServiceLocator().getProbandService().getProbandStatusEntry(getAuthentication(), probandStatusEntryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandStatusEntryCount(Long probandId) {
		if (probandId != null) {
			try {
				return getServiceLocator().getProbandService().getProbandStatusEntryCount(getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static ProbandStatusTypeVO getProbandStatusType(Long statusTypeId) {
		if (statusTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getProbandStatusType(getAuthentication(), statusTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProbandTagValueCount(Long probandId) {
		if (probandId != null) {
			try {
				return getServiceLocator().getProbandService().getProbandTagValueCount(getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getProcedureCount(Long probandId) {
		if (probandId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getProcedureCount(WebUtil.getAuthentication(), probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getProcedures(Long probandId) {
		ArrayList<SelectItem> procedures;
		Collection<ProcedureOutVO> procedureVOs = null;
		if (probandId != null) {
			try {
				procedureVOs = getServiceLocator().getProbandService().getProcedureList(getAuthentication(), probandId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (procedureVOs != null) {
			procedures = new ArrayList<SelectItem>(procedureVOs.size());
			Iterator<ProcedureOutVO> it = procedureVOs.iterator();
			while (it.hasNext()) {
				ProcedureOutVO procedureVO = it.next();
				procedures.add(new SelectItem(Long.toString(procedureVO.getId()), Messages.getMessage(MessageCodes.PROCEDURE_SELECTITEM_LABEL, procedureVO.getName(),
						DateUtil.getDateStartStopString(procedureVO.getStart(), procedureVO.getStop()))));
			}
		} else {
			procedures = new ArrayList<SelectItem>();
		}
		return procedures;
	}

	public static ArrayList<SelectItem> getRandomizationModes() {
		ArrayList<SelectItem> modes;
		Collection<RandomizationModeVO> modeVOs = null;
		try {
			modeVOs = getServiceLocator().getSelectionSetService().getRandomizationModes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (modeVOs != null) {
			modes = new ArrayList<SelectItem>(modeVOs.size());
			Iterator<RandomizationModeVO> it = modeVOs.iterator();
			while (it.hasNext()) {
				RandomizationModeVO modeVO = it.next();
				modes.add(new SelectItem(modeVO.getMode().name(), modeVO.getName()));
			}
		} else {
			modes = new ArrayList<SelectItem>();
		}
		return modes;
	}

	public static ArrayList<SelectItem> getJobStates() {
		ArrayList<SelectItem> states;
		Collection<JobStatusVO> stateVOs = null;
		try {
			stateVOs = getServiceLocator().getSelectionSetService().getJobStates(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (stateVOs != null) {
			states = new ArrayList<SelectItem>(stateVOs.size());
			Iterator<JobStatusVO> it = stateVOs.iterator();
			while (it.hasNext()) {
				JobStatusVO statusVO = it.next();
				states.add(new SelectItem(statusVO.getJobStatus().name(), statusVO.getName()));
			}
		} else {
			states = new ArrayList<SelectItem>();
		}
		return states;
	}

	public static String getRefererBase64(HttpServletRequest request) {
		if (request != null) {
			String headerValue = request.getHeader(REFERER_HEADER_NAME);
			StringBuffer url = request.getRequestURL();
			if (!CommonUtil.isEmptyString(request.getQueryString())) {
				url.append('?').append(request.getQueryString());
			}
			return JsUtil.encodeBase64(isTrustedReferer(headerValue, request) ? headerValue : url.toString(), true);
		}
		return "";
	}

	public static ArrayList<SelectItem> getReimbursementTrials(Long probandId, String costType, PaymentMethod method, Boolean paid) {
		Collection<TrialOutVO> trialVOs = null;
		ArrayList<SelectItem> trials;
		if (probandId != null) {
			try {
				trialVOs = getServiceLocator().getProbandService().getReimbursementTrials(getAuthentication(), probandId, costType, method, paid);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (trialVOs != null) {
			trials = new ArrayList<SelectItem>(trialVOs.size());
			Iterator<TrialOutVO> it = trialVOs.iterator();
			while (it.hasNext()) {
				TrialOutVO trialVO = it.next();
				trials.add(new SelectItem(Long.toString(trialVO.getId()), CommonUtil.trialOutVOToString(trialVO)));
			}
		} else {
			trials = new ArrayList<SelectItem>();
		}
		return trials;
	}

	public static String getRemoteHost() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			return getRemoteHost((HttpServletRequest) context.getExternalContext().getRequest());
		}
		return null;
	}

	public static String getRemoteHost(HttpServletRequest request) {
		if (request != null) {
			return request.getRemoteHost();
		}
		return null;
	}

	public static Long getSelectionSetValueCount(Long inputFieldId) {
		if (inputFieldId != null) {
			try {
				return getServiceLocator().getInputFieldService().getSelectionSetValueCount(getAuthentication(), inputFieldId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getSeriesColors(ArrayList<Color> colors) {
		if (colors != null && colors.size() > 0) {
			StringBuilder seriesColors = new StringBuilder();
			Iterator<Color> colorsIt = colors.iterator();
			while (colorsIt.hasNext()) {
				if (seriesColors.length() > 0) {
					seriesColors.append(",");
				}
				seriesColors.append(colorsIt.next().getValue());
			}
			return seriesColors.toString();
		}
		return Color.BLACK.getValue();
	}

	public static ServiceLocator getServiceLocator() {
		return ServiceLocator.instance();
	}

	public static SessionScopeBean getSessionScopeBean() {
		return getSessionScopeBean((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
	}

	public static SessionScopeBean getSessionScopeBean(HttpServletRequest request) {
		if (request != null) {
			return getSessionScopeBean(request.getSession());
		}
		return null;
	}

	public static SessionScopeBean getSessionScopeBean(HttpSession session) {
		if (session != null) {
			return (SessionScopeBean) session.getAttribute("sessionScopeBean");
		}
		return null;
	}

	public static ArrayList<SelectItem> getSexes() {
		ArrayList<SelectItem> sexes;
		Collection<SexVO> sexVOs = null;
		try {
			sexVOs = getServiceLocator().getSelectionSetService().getSexes(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (sexVOs != null) {
			sexes = new ArrayList<SelectItem>(sexVOs.size());
			Iterator<SexVO> it = sexVOs.iterator();
			while (it.hasNext()) {
				SexVO sexVO = it.next();
				sexes.add(new SelectItem(sexVO.getSex().name(), sexVO.getName()));
			}
		} else {
			sexes = new ArrayList<SelectItem>();
		}
		return sexes;
	}

	public static StaffOutVO getStaff(Long staffId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) {
		if (staffId != null) {
			try {
				return getServiceLocator().getStaffService().getStaff(getAuthentication(), staffId, maxInstances, maxParentDepth, maxChildrenDepth);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static DepartmentVO getDepartment(Long departmentId) {
		if (departmentId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getDepartment(getAuthentication(), departmentId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getStaffAddressCount(Long staffId) {
		if (staffId != null) {
			try {
				return getServiceLocator().getStaffService().getStaffAddressCount(getAuthentication(), staffId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static StaffCategoryVO getStaffCategory(Long categoryId) {
		if (categoryId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getStaffCategory(getAuthentication(), categoryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getStaffContactDetailValueCount(Long staffId) {
		if (staffId != null) {
			try {
				return getServiceLocator().getStaffService().getStaffContactDetailValueCount(getAuthentication(), staffId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getStaffDutyRosterCount(Long staffId) {
		if (staffId != null) {
			try {
				return getServiceLocator().getStaffService().getDutyRosterCount(getAuthentication(), staffId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static StaffStatusEntryOutVO getStaffStatusEntry(Long staffStatusEntryId) {
		if (staffStatusEntryId != null) {
			try {
				return getServiceLocator().getStaffService().getStaffStatusEntry(getAuthentication(), staffStatusEntryId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getStaffStatusEntryCount(Long staffId) {
		if (staffId != null) {
			try {
				return getServiceLocator().getStaffService().getStaffStatusEntryCount(getAuthentication(), staffId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String timelineEventToStartStopString(TimelineEventOutVO timelineEvent) {
		if (timelineEvent != null) {
			Date start = timelineEvent.getStart();
			Date stop = timelineEvent.getStop();
			if (start != null) {
				if (stop != null) {
					return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_STOP_LABEL, DateUtil.getDateFormat().format(start), DateUtil.getDateFormat().format(stop));
				} else {
					return Messages.getMessage(MessageCodes.TIMELINE_TRIAL_EVENT_START_LABEL, DateUtil.getDateFormat().format(start));
				}
			}
		}
		return "";
	}

	public static StaffStatusTypeVO getStaffStatusType(Long statusTypeId) {
		if (statusTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getStaffStatusType(getAuthentication(), statusTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getStaffTagValueCount(Long staffId) {
		if (staffId != null) {
			try {
				return getServiceLocator().getStaffService().getStaffTagValueCount(getAuthentication(), staffId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getStratificationRandomizationListCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getStratificationRandomizationListCount(getAuthentication(), trialId);
			} catch (AuthenticationException e) {
				publishException(e);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			}
		}
		return null;
	}

	public static String getTabTitleString(
			String tabTitleMsgCode,
			String tabTitleWithCountMsgCode,
			Long count, Object... countArgs) {
		Object[] args;
		String titleMsgCode;
		if (count != null && count > 0) {
			if (countArgs != null && countArgs.length > 0) {
				args = java.util.Arrays.copyOf(countArgs, countArgs.length + 1);
				System.arraycopy(args, 0, args, 1, countArgs.length);
				args[0] = count;
			} else {
				args = new Object[1];
				args[0] = count;
			}
			titleMsgCode = tabTitleWithCountMsgCode;
		} else {
			args = countArgs;
			titleMsgCode = tabTitleMsgCode;
		}
		return Messages.getMessage(titleMsgCode, args);
	}

	public static TeamMemberOutVO getTeamMember(Long teamMemberId) {
		if (teamMemberId != null) {
			try {
				return getServiceLocator().getTrialService().getTeamMember(getAuthentication(), teamMemberId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getTeamMemberCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getTeamMemberCount(getAuthentication(), trialId, null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static TimelineEventOutVO getTimelineEvent(Long timelineEventId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) {
		if (timelineEventId != null) {
			try {
				return getServiceLocator().getTrialService().getTimelineEvent(getAuthentication(), timelineEventId, maxInstances, maxParentDepth, maxChildrenDepth);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getTimelineEventCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getTimelineEventCount(getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static TimelineEventTypeVO getTimelineEventType(Long eventTypeId) {
		if (eventTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getTimelineEventType(getAuthentication(), eventTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static TimeZone getTimeZone() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null) {
			return sessionScopeBean.getTimeZone();
		}
		return null;
	}

	public static TimeZoneVO getTimeZone(String timeZoneID) {
		if (timeZoneID != null) {
			try {
				return WebUtil.getServiceLocator().getSelectionSetService().getTimeZone(WebUtil.getAuthentication(), timeZoneID);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public static Long getTotalFileCount(FileModule module, Long id) {
		if (module != null && id != null) {
			try {
				return getServiceLocator().getFileService().getFileCount(getAuthentication(), module, id, null, true, null, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static TrialOutVO getTrial(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getTrial(getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getTrialDutyRosterCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getDutyRosterCount(getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static TrialECRFProgressSummaryVO getTrialEcrfProgressSummary(Long trialId, Long probandDepartmentId, Date from, Date to) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getTrialEcrfProgressSummary(getAuthentication(), trialId, probandDepartmentId, from, to);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getTrialInventoryBookingCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getTrialInventoryBookingCount(getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static MoneyTransferSummaryVO getTrialMoneyTransferSummary(Long trialId, String costType, PaymentMethod method, Boolean paid) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getTrialMoneyTransferSummary(getAuthentication(), trialId, costType, method, paid);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getTrialsFromInquiryValues(Long probandId, Boolean active, Boolean activeSignup, ArrayList<SelectItem> trials, Object... totalCounts) {
		if (trials != null) {
			trials.clear();
		}
		Long trialCount = 0L;
		Long totalInquiryValueCount = 0L;
		Long totalInquiryCount = 0L;
		Long trialsWithoutInquiryValuesCount = null;
		Collection<TrialOutVO> trialVOs = null;
		if (probandId != null) {
			try {
				trialVOs = getServiceLocator().getProbandService().getInquiryTrials(getAuthentication(), probandId, active, activeSignup);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (trialVOs != null) {
			if (trials != null) {
				trials.ensureCapacity(trialVOs.size());
			}
			trialsWithoutInquiryValuesCount = 0l;
			Iterator<TrialOutVO> it = trialVOs.iterator();
			while (it.hasNext()) {
				TrialOutVO trialVO = it.next();
				Long inquiryValueCount = 0L;
				try {
					inquiryValueCount = getServiceLocator().getProbandService().getInquiryValueCount(getAuthentication(), trialVO.getId(), active, activeSignup, probandId);
					totalInquiryValueCount += inquiryValueCount;
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					publishException(e);
				}
				if (inquiryValueCount > 0 || (!trialVO.getStatus().getLockdown() && trialVO.getStatus().getInquiryValueInputEnabled())) {
					Long inquiryCount = 0L;
					try {
						inquiryCount = getServiceLocator().getProbandService().getInquiryCount(getAuthentication(), trialVO.getId(), active, activeSignup);
						totalInquiryCount += inquiryCount;
					} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
					} catch (AuthenticationException e) {
						publishException(e);
					}
					if (inquiryCount > 0) {
						if (trials != null) {
							trials.add(new SelectItem(Long.toString(trialVO.getId()), Messages.getMessage(MessageCodes.INQUIRIES_TRIAL_NAME,
									CommonUtil.trialOutVOToString(trialVO), inquiryValueCount, inquiryCount)));
						}
						trialCount++;
						if (!trialVO.getStatus().getLockdown() && inquiryCount > 0l && inquiryValueCount == 0l) {
							trialsWithoutInquiryValuesCount++;
						}
					}
				}
			}
		}
		if (totalCounts != null && totalCounts.length >= 3) {
			totalCounts[0] = trialCount;
			totalCounts[1] = totalInquiryValueCount;
			totalCounts[2] = totalInquiryCount;
		}
		return trialsWithoutInquiryValuesCount;
	}

	public static TrialStatusTypeVO getTrialStatusType(Long statusTypeId) {
		if (statusTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getTrialStatusType(getAuthentication(), statusTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getTrialTagValueCount(Long trialId) {
		if (trialId != null) {
			try {
				return getServiceLocator().getTrialService().getTrialTagValueCount(getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static UserOutVO getUser() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null) {
			return sessionScopeBean.getUser();
		}
		return null;
	}

	public static UserOutVO getUser(Long userId, Integer maxInstances) {
		if (userId != null) {
			try {
				return getServiceLocator().getUserService().getUser(getAuthentication(), userId, maxInstances);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static boolean getUserAuthMessages(UserOutVO user, Date now, ArrayList<String> messageCodes) {
		if (messageCodes == null) {
			messageCodes = new ArrayList<String>();
		} else {
			messageCodes.clear();
		}
		if (now == null) {
			now = new Date();
		}
		if (user == null) {
			return false;
		}
		PasswordOutVO password = null;
		Collection<UserPermissionProfileOutVO> userPermissionProfilesOut = null;
		try {
			password = getServiceLocator().getUserService().getPassword(getAuthentication(), user.getId());
			userPermissionProfilesOut = getServiceLocator().getUserService()
					.getPermissionProfiles(getAuthentication(), user.getId(), null, true);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			return false;
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			return false;
		}
		boolean warning = false;
		if (user.getLocked()) {
			messageCodes.add(AuthenticationExceptionCodes.USER_LOCKED);
			warning = true;
		}
		if (user.getDeferredDelete()) {
			messageCodes.add(AuthenticationExceptionCodes.USER_MARKED_FOR_DELETION);
			warning = true;
		}
		if (userPermissionProfilesOut == null || userPermissionProfilesOut.size() == 0) {
			warning = true;
		}
		switch (user.getAuthMethod().getMethod()) {
			case LOCAL:
				if (password == null) {
					messageCodes.add(AuthenticationExceptionCodes.NO_PASSWORD_SET);
					warning = true;
				}
				Boolean isPasswordExpired = isPasswordExpired(now, password);
				if (isPasswordExpired != null && isPasswordExpired) {
					messageCodes.add(AuthenticationExceptionCodes.PASSWORD_EXPIRED);
					warning = true;
				}
				Boolean isLogonLimitExceeded = isLogonLimitExceeded(password);
				if (isLogonLimitExceeded != null && isLogonLimitExceeded) {
					messageCodes.add(AuthenticationExceptionCodes.SUCCESSFUL_LOGON_LIMIT_EXCEEDED);
					warning = true;
				}
				Boolean isWrongPasswordAttemtpsLimitExceeded = isWrongPasswordAttemtpsLimitExceeded(password);
				if (isWrongPasswordAttemtpsLimitExceeded != null && isWrongPasswordAttemtpsLimitExceeded) {
					messageCodes.add(AuthenticationExceptionCodes.WRONG_PASSWORD_ATTEMPT_LIMIT_EXCEEDED);
					warning = true;
				}
				if (!warning) {
					messageCodes.add(MessageCodes.LOCAL_USER_CAN_LOGIN);
				}
				break;
			case LDAP1:
			case LDAP2:
				if (password == null) {
					messageCodes.add(AuthenticationExceptionCodes.NO_LOCAL_PASSWORD_SET);
					warning = true;
				}
				if (!warning) {
					messageCodes.add(MessageCodes.LDAP_USER_CAN_LOGIN);
				}
				break;
			default:
		}
		if (userPermissionProfilesOut == null || userPermissionProfilesOut.size() == 0) {
			messageCodes.add(AuthorisationExceptionCodes.NO_PERMISSIONS);
		}
		return warning;
	}

	public static StaffOutVO getUserIdentity() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null) {
			return sessionScopeBean.getUserIdentity();
		}
		return null;
	}

	public static String getUserIdentityString(UserOutVO user) {
		if (user != null) {
			StaffOutVO identity = user.getIdentity();
			if (identity != null) {
				return Messages.getMessage(MessageCodes.USER_IDENTITY_LABEL, CommonUtil.userOutVOToString(user), CommonUtil.staffOutVOToString(identity));
			} else {
				return CommonUtil.userOutVOToString(user);
			}
		}
		return "";
	}

	public static ArrayList<SelectItem> getVariablePeriods() {
		ArrayList<SelectItem> variablePeriods;
		Collection<VariablePeriodVO> periodVOs = null;
		try {
			periodVOs = getServiceLocator().getSelectionSetService().getVariablePeriods(getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (periodVOs != null) {
			variablePeriods = new ArrayList<SelectItem>(periodVOs.size());
			Iterator<VariablePeriodVO> it = periodVOs.iterator();
			while (it.hasNext()) {
				VariablePeriodVO periodVO = it.next();
				variablePeriods.add(new SelectItem(periodVO.getPeriod().name(), periodVO.getName()));
			}
		} else {
			variablePeriods = new ArrayList<SelectItem>();
		}
		return variablePeriods;
	}

	public static String getViewId() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			return context.getViewRoot().getViewId();
		}
		return null;
	}

	public static String getViewMapId() {
		return (String) FacesContext.getCurrentInstance().getViewRoot().getTransientStateHelper().getTransient(ViewScopeManager.VIEW_MAP_ID);
	}

	public static ArrayList<SelectItem> getVisibleCourseCategories(Long categoryId) {
		ArrayList<SelectItem> categories;
		Collection<CourseCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getCourseCategories(getAuthentication(), categoryId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<CourseCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				CourseCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getVisibleDepartments(Long departmentId) {
		ArrayList<SelectItem> departments;
		Collection<DepartmentVO> departmentVOs = null;
		try {
			departmentVOs = getServiceLocator().getSelectionSetService().getDepartments(getAuthentication(), departmentId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (departmentVOs != null) {
			departments = new ArrayList<SelectItem>(departmentVOs.size());
			Iterator<DepartmentVO> it = departmentVOs.iterator();
			while (it.hasNext()) {
				DepartmentVO departmentVO = it.next();
				departments.add(new SelectItem(departmentVO.getId().toString(), departmentVO.getName()));
			}
		} else {
			departments = new ArrayList<SelectItem>();
		}
		return departments;
	}

	public static ArrayList<SelectItem> getVisibleInventoryCategories(Long categoryId) {
		ArrayList<SelectItem> categories;
		Collection<InventoryCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getInventoryCategories(getAuthentication(), categoryId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<InventoryCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				InventoryCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getVisibleMassMailTypes(Long typeId) {
		ArrayList<SelectItem> massMailTypes;
		Collection<MassMailTypeVO> massMailTypeVOs = null;
		try {
			massMailTypeVOs = getServiceLocator().getSelectionSetService().getMassMailTypes(getAuthentication(), typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (massMailTypeVOs != null) {
			massMailTypes = new ArrayList<SelectItem>(massMailTypeVOs.size());
			Iterator<MassMailTypeVO> it = massMailTypeVOs.iterator();
			while (it.hasNext()) {
				MassMailTypeVO massMailTypeVO = it.next();
				massMailTypes.add(new SelectItem(massMailTypeVO.getId().toString(), massMailTypeVO.getName()));
			}
		} else {
			massMailTypes = new ArrayList<SelectItem>();
		}
		return massMailTypes;
	}

	public static ArrayList<SelectItem> getVisibleProbandCategories(Boolean person, Boolean animal, Long categoryId) {
		ArrayList<SelectItem> categories;
		Collection<ProbandCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getProbandCategories(getAuthentication(), person, animal, categoryId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<ProbandCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				ProbandCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getVisibleSponsoringTypes(Long typeId) {
		ArrayList<SelectItem> sponsoringTypes;
		Collection<SponsoringTypeVO> sponsoringTypeVOs = null;
		try {
			sponsoringTypeVOs = getServiceLocator().getSelectionSetService().getSponsoringTypes(getAuthentication(), typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (sponsoringTypeVOs != null) {
			sponsoringTypes = new ArrayList<SelectItem>(sponsoringTypeVOs.size());
			Iterator<SponsoringTypeVO> it = sponsoringTypeVOs.iterator();
			while (it.hasNext()) {
				SponsoringTypeVO sponsoringTypeVO = it.next();
				sponsoringTypes.add(new SelectItem(sponsoringTypeVO.getId().toString(), sponsoringTypeVO.getName()));
			}
		} else {
			sponsoringTypes = new ArrayList<SelectItem>();
		}
		return sponsoringTypes;
	}

	public static ArrayList<SelectItem> getVisibleStaffCategories(Boolean person, Boolean organisation, Long categoryId) {
		ArrayList<SelectItem> categories;
		Collection<StaffCategoryVO> categoryVOs = null;
		try {
			categoryVOs = getServiceLocator().getSelectionSetService().getStaffCategories(getAuthentication(), person, organisation, categoryId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (categoryVOs != null) {
			categories = new ArrayList<SelectItem>(categoryVOs.size());
			Iterator<StaffCategoryVO> it = categoryVOs.iterator();
			while (it.hasNext()) {
				StaffCategoryVO categoryVO = it.next();
				categories.add(new SelectItem(categoryVO.getId().toString(), categoryVO.getName()));
			}
		} else {
			categories = new ArrayList<SelectItem>();
		}
		return categories;
	}

	public static ArrayList<SelectItem> getVisibleSurveyStatusTypes(Long typeId) {
		ArrayList<SelectItem> surveyStatusTypes;
		Collection<SurveyStatusTypeVO> surveyStatusTypeVOs = null;
		try {
			surveyStatusTypeVOs = getServiceLocator().getSelectionSetService().getSurveyStatusTypes(getAuthentication(), typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (surveyStatusTypeVOs != null) {
			surveyStatusTypes = new ArrayList<SelectItem>(surveyStatusTypeVOs.size());
			Iterator<SurveyStatusTypeVO> it = surveyStatusTypeVOs.iterator();
			while (it.hasNext()) {
				SurveyStatusTypeVO surveyStatusTypeVO = it.next();
				surveyStatusTypes.add(new SelectItem(surveyStatusTypeVO.getId().toString(), surveyStatusTypeVO.getName()));
			}
		} else {
			surveyStatusTypes = new ArrayList<SelectItem>();
		}
		return surveyStatusTypes;
	}

	public static ArrayList<SelectItem> getVisibleTrialTypes(Long typeId) {
		ArrayList<SelectItem> trialTypes;
		Collection<TrialTypeVO> trialTypeVOs = null;
		try {
			trialTypeVOs = getServiceLocator().getSelectionSetService().getTrialTypes(getAuthentication(), typeId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		if (trialTypeVOs != null) {
			trialTypes = new ArrayList<SelectItem>(trialTypeVOs.size());
			Iterator<TrialTypeVO> it = trialTypeVOs.iterator();
			while (it.hasNext()) {
				TrialTypeVO trialTypeVO = it.next();
				trialTypes.add(new SelectItem(trialTypeVO.getId().toString(), trialTypeVO.getName()));
			}
		} else {
			trialTypes = new ArrayList<SelectItem>();
		}
		return trialTypes;
	}

	public static VisitOutVO getVisit(Long visitId) {
		if (visitId != null) {
			try {
				return getServiceLocator().getTrialService().getVisit(getAuthentication(), visitId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getVisitCount(Long trialId) {
		if (trialId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getVisitCount(WebUtil.getAuthentication(), trialId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	public static ArrayList<SelectItem> getVisits(Long trialId) {
		ArrayList<SelectItem> visits;
		Collection<VisitOutVO> visitVOs = null;
		if (trialId != null) {
			try {
				visitVOs = getServiceLocator().getTrialService().getVisitList(getAuthentication(), trialId, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		if (visitVOs != null) {
			visits = new ArrayList<SelectItem>(visitVOs.size());
			Iterator<VisitOutVO> it = visitVOs.iterator();
			while (it.hasNext()) {
				VisitOutVO visitVO = it.next();
				visits.add(new SelectItem(Long.toString(visitVO.getId()), visitVO.getTitle()));
			}
		} else {
			visits = new ArrayList<SelectItem>();
		}
		return visits;
	}

	public static VisitScheduleItemOutVO getVisitScheduleItem(Long visitScheduleItemId) {
		if (visitScheduleItemId != null) {
			try {
				return getServiceLocator().getTrialService().getVisitScheduleItem(getAuthentication(), visitScheduleItemId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static Long getVisitScheduleItemCount(Long trialId, Long probandId) {
		if (trialId != null || probandId != null) {
			try {
				return getServiceLocator().getTrialService().getVisitScheduleItemCount(getAuthentication(), trialId, null, null, probandId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static VisitTypeVO getVisitType(Long visitTypeId) {
		if (visitTypeId != null) {
			try {
				return getServiceLocator().getSelectionSetService().getVisitType(getAuthentication(), visitTypeId);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				publishException(e);
			}
		}
		return null;
	}

	public static String getWindowNameUniqueToken() {
		return "_" + Long.toString((new Date()).getTime());
	}

	public static String inputFieldIdToName(Long id) {
		if (id == null) {
			return getNoInputFieldPickedMessage();
		}
		try {
			InputFieldOutVO inputField = getServiceLocator().getInputFieldService().getInputField(getAuthentication(), id);
			return inputFieldOutVOToString(inputField);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_INPUT_FIELD_PICKED);
	}

	public static String inputFieldOutVOToString(InputFieldOutVO inputField) {
		String result = CommonUtil.inputFieldOutVOToString(inputField);
		if (result != null) {
			return result;
		} else {
			return getNoInputFieldPickedMessage();
		}
	}

	public static String inventoryIdToName(Long id) {
		if (id == null) {
			return getNoInventoryPickedMessage();
		}
		try {
			InventoryOutVO inventory = getServiceLocator().getInventoryService().getInventory(getAuthentication(), id, null, null, null);
			return inventoryOutVOToString(inventory);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_INVENTORY_PICKED);
	}

	public static String inventoryOutVOToString(InventoryOutVO inventory) {
		String result = CommonUtil.inventoryOutVOToString(inventory);
		if (result != null) {
			return result;
		} else {
			return getNoInventoryPickedMessage();
		}
	}

	public static boolean isCourseSelfRegistration(CourseOutVO course) {
		if (course != null) {
			return course.isSelfRegistration();
		}
		return false;
	}

	public static boolean isDutySelfAllocationLocked(DutyRosterTurnOutVO dutyRosterTurn) {
		return isDutySelfAllocationLocked(dutyRosterTurn.getTrial(), dutyRosterTurn.getStart(), dutyRosterTurn.getStaff() != null);
	}

	public static boolean isDutySelfAllocationLocked(TrialOutVO trial, Date dutyRosterTurnStart, boolean isStaffAssigned) {
		if (trial != null && dutyRosterTurnStart != null) {
			if (trial.isDutySelfAllocationLocked() && isStaffAssigned) {
				if (trial.getDutySelfAllocationLockedUntil() == null && trial.getDutySelfAllocationLockedFrom() == null) {
					return true;
				} else if (trial.getDutySelfAllocationLockedUntil() != null && trial.getDutySelfAllocationLockedUntil().compareTo(dutyRosterTurnStart) > 0) {
					return true;
				} else if (trial.getDutySelfAllocationLockedFrom() != null && trial.getDutySelfAllocationLockedFrom().compareTo(dutyRosterTurnStart) <= 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isInventoryBookable(InventoryOutVO inventory) {
		if (inventory != null) {
			return inventory.isBookable();
		}
		return true;
	}

	public static Boolean isLocalAuthMethod(UserOutVO user) {
		if (user != null) {
			return AuthenticationType.LOCAL.equals(user.getAuthMethod().getMethod());
		}
		return null;
	}

	public static Boolean isLogonLimitExceeded(PasswordOutVO password) {
		if (password != null && password.getLimitLogons()) {
			return password.getSuccessfulLogons() >= password.getMaxSuccessfulLogons();
		}
		return null;
	}

	public static boolean isMassMailLocked(MassMailOutVO massMail) {
		if (massMail != null) {
			return massMail.getStatus().getLocked();
		}
		return false;
	}

	public static Boolean isPasswordExpired(Date now, PasswordOutVO password) {
		if (now != null && password != null && password.getExpires()) {
			return getExpired(now, password.getExpiration());
		}
		return null;
	}

	public static boolean isProbandLocked(ProbandListEntryOutVO probandListEntry) {
		if (probandListEntry != null) {
			return isProbandLocked(probandListEntry.getProband());
		}
		return false;
	}

	public static boolean isProbandLocked(ProbandOutVO proband) {
		if (proband != null) {
			return proband.getCategory().getLocked();
		}
		return false;
	}

	public static boolean isProbandPerson(ProbandOutVO proband) {
		if (proband != null) {
			return proband.isPerson();
		}
		return false;
	}

	public static boolean isStaffAllocatable(StaffOutVO staff) {
		if (staff != null) {
			return isStaffEmployee(staff) && staff.isAllocatable();
		}
		return false;
	}

	public static boolean isStaffEmployee(StaffOutVO staff) {
		if (staff != null) {
			return isStaffPerson(staff) && staff.isEmployee();
		}
		return false;
	}

	public static boolean isStaffPerson(StaffOutVO staff) {
		if (staff != null) {
			return staff.isPerson();
		}
		return false;
	}

	public static boolean isTabCountEmphasized(Long count) {
		return isTabCountEmphasized(count, false);
	}

	public static boolean isTabCountEmphasized(Long count, boolean inverted) {
		if (count == null) {
			return false;
		} else if (count > 0l) {
			return inverted;
		} else {
			return !inverted;
		}
	}

	public static boolean isTrialLocked(ProbandListEntryOutVO probandListEntry) {
		if (probandListEntry != null) {
			return isTrialLocked(probandListEntry.getTrial());
		}
		return false;
	}

	public static boolean isTrialLocked(TrialOutVO trial) {
		if (trial != null) {
			return trial.getStatus().getLockdown();
		}
		return false;
	}

	public static boolean isTrustedHost() {
		try {
			return getServiceLocator().getToolsService().isTrustedHost(getRemoteHost());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return false;
	}

	public static boolean isTrustedHost(HttpServletRequest request) {
		try {
			return getServiceLocator().getToolsService().isTrustedHost(getRemoteHost(request));
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return false;
	}

	public static boolean isTrustedReferer(HttpServletRequest request) {
		return isTrustedReferer(request.getHeader(REFERER_HEADER_NAME), request);
	}

	public static boolean isTrustedReferer(String url, HttpServletRequest request) {
		URL refererUrl;
		try {
			refererUrl = new URL(url);
		} catch (MalformedURLException e) {
			refererUrl = null;
		}
		if (refererUrl != null && Settings.getTrustetRefererHosts(request).contains(refererUrl.getHost()) && refererUrl.getPath().startsWith(request.getContextPath())) {
			return true;
		}
		return false;
	}

	public static boolean isUserIdentityIdLoggedIn(Long staffId) {
		SessionScopeBean sessionScopeBean = null;
		if (staffId != null && (sessionScopeBean = getSessionScopeBean()) != null) {
			PasswordOutVO logon = sessionScopeBean.getLogon();
			if (logon != null) {
				StaffOutVO identity = logon.getUser().getIdentity();
				if (identity != null && staffId.longValue() == identity.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isUserIdLoggedIn(Long userId) {
		SessionScopeBean sessionScopeBean = null;
		if (userId != null && (sessionScopeBean = getSessionScopeBean()) != null) {
			PasswordOutVO logon = sessionScopeBean.getLogon();
			if (logon != null && userId.longValue() == logon.getUser().getId()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isUserLocked(Long userId) {
		UserOutVO user = getUser(userId, null);
		if (user != null) {
			return user.getLocked();
		}
		return false;
	}

	public static Boolean isWrongPasswordAttemtpsLimitExceeded(PasswordOutVO password) {
		if (password != null && password.getLimitWrongPasswordAttempts()) {
			return password.getWrongPasswordAttemptsSinceLastSuccessfulLogon() >= password.getMaxWrongPasswordAttemptsSinceLastSuccessfulLogon();
		}
		return null;
	}

	public static void logout() {
		SessionScopeBean sessionScopeBean = getSessionScopeBean();
		if (sessionScopeBean != null) {
			sessionScopeBean.logout();
		}
	}

	public static String massMailIdToName(Long id) {
		if (id == null) {
			return getNoMassMailPickedMessage();
		}
		try {
			MassMailOutVO massMail = getServiceLocator().getMassMailService().getMassMail(getAuthentication(), id);
			return massMailOutVOToString(massMail);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_MASS_MAIL_PICKED);
	}

	public static String massMailOutVOToString(MassMailOutVO massMail) {
		String result = CommonUtil.massMailOutVOToString(massMail);
		if (result != null) {
			return result;
		} else {
			return getNoMassMailPickedMessage();
		}
	}

	public static JsonElement parseJson(String json) {
		return JSON_PARSER.parse(json);
	}

	public static String probandIdToName(Long id) {
		if (id == null) {
			return getNoProbandPickedMessage();
		}
		try {
			ProbandOutVO proband = getServiceLocator().getProbandService().getProband(getAuthentication(), id, null, null, null);
			return probandOutVOToString(proband);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_PROBAND_PICKED);
	}

	public static String probandOutVOToString(ProbandOutVO proband) {
		String result = CommonUtil.probandOutVOToString(proband);
		if (result != null) {
			return result;
		} else {
			return getNoProbandPickedMessage();
		}
	}

	public static void publishException(Exception e) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExceptionQueuedEventContext eventContext = new ExceptionQueuedEventContext(context, e);
		eventContext.getAttributes().put(EVENT_CONTEXT_VIEW_ID, context.getViewRoot().getViewId());
		context.getApplication().publishEvent(context, ExceptionQueuedEvent.class, eventContext);
	}

	public static String quoteJSString(String value, boolean quotes) {
		StringBuilder sb;
		if (quotes) {
			sb = new StringBuilder("'");
		} else {
			sb = new StringBuilder();
		}
		sb.append(org.apache.commons.text.StringEscapeUtils.escapeEcmaScript(value));
		if (quotes) {
			sb.append("'");
		}
		return sb.toString();
	}

	public static boolean resourceExists(String fileName) {
		// https://stackoverflow.com/questions/19354297/a-uiinclude-that-doesnt-throw-an-error-if-file-not-found
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			java.io.File file = new java.io.File(context.getExternalContext().getRealPath(fileName));
			return file.exists();
		}
		return false;
	}

	public static void setAjaxCancelFlag(boolean value) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_CANCEL.toString(), value);
		}
	}

	public static void setSessionTimeout() {
		setSessionTimeout(null);
	}

	public static void setSessionTimeout(HttpSession session) {
		int maxInactiveInterval;
		if (isTrustedHost()) {
			maxInactiveInterval = Settings.getInt(SettingCodes.SESSION_TIMEOUT_TRUSTED, Bundle.SETTINGS, DefaultSettings.SESSION_TIMEOUT_TRUSTED);
		} else {
			maxInactiveInterval = Settings.getInt(SettingCodes.SESSION_TIMEOUT, Bundle.SETTINGS, DefaultSettings.SESSION_TIMEOUT);
		}
		maxInactiveInterval *= 60;
		if (session != null) {
			session.setMaxInactiveInterval(maxInactiveInterval);
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().setSessionMaxInactiveInterval(maxInactiveInterval);
		}
	}

	public static String staffIdToName(Long id) {
		if (id == null) {
			return getNoStaffPickedMessage();
		}
		try {
			StaffOutVO staff = getServiceLocator().getStaffService().getStaff(getAuthentication(), id, null, null, null);
			return staffOutVOToString(staff);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_STAFF_PICKED);
	}

	public static String staffOutVOToString(StaffOutVO staff) {
		String result = CommonUtil.staffOutVOToString(staff);
		if (result != null) {
			return result;
		} else {
			return getNoStaffPickedMessage();
		}
	}

	public static Boolean stringToBoolean(String bool) {
		if (!(bool == null || bool.trim().equals(CommonUtil.NO_SELECTION_VALUE))) {
			return new Boolean(Boolean.parseBoolean(bool));
		}
		return null;
	}

	public static Long stringToLong(String lng) {
		if (!(lng == null || lng.trim().equals(CommonUtil.NO_SELECTION_VALUE))) {
			try {
				return new Long(Long.parseLong(lng));
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}

	public static Date subIntervals(Date date, VariablePeriod period, Long explicitDays, int n) {
		try {
			return getServiceLocator().getToolsService().subIntervals(date, period, explicitDays, n);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return null;
	}

	public static boolean testCompleteMethodName(CriterionPropertyVO propertyVO) {
		if (propertyVO.getCompleteMethodName() != null && propertyVO.getCompleteMethodName().length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean testConverter(CriterionPropertyVO propertyVO) {
		if (propertyVO.getConverter() != null && propertyVO.getConverter().length() > 0) {
			return true;
		}
		return false;
	}

	private static boolean testParamValueExists(FacesContext context, String paramName) {
		if (context != null && paramName != null && paramName.length() > 0) {
			return context.getExternalContext().getRequestParameterMap().containsKey(paramName);
		}
		return false;
	}

	public static boolean testParamValueExists(GetParamNames paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return testParamValueExists(context, paramName.toString());
	}

	public static void testPassword(String passwordToTest) {
		if (!getSessionScopeBean().testPassword(passwordToTest)) {
			throw new IllegalArgumentException(Messages.getString(MessageCodes.WRONG_PASSWORD));
		}
	}

	public static boolean testPicker(CriterionPropertyVO propertyVO) {
		return testPicker(propertyVO, null);
	}

	public static boolean testPicker(CriterionPropertyVO propertyVO, DBModule picker) {
		if (picker == null) {
			return (propertyVO.getPicker() != null);
		} else {
			return (picker.equals(propertyVO.getPicker()));
		}
	}

	public static boolean testSelectionSetServiceMethodName(CriterionPropertyVO propertyVO) {
		if (propertyVO.getSelectionSetServiceMethodName() != null && propertyVO.getSelectionSetServiceMethodName().length() > 0 &&
				propertyVO.getGetNameMethodName() != null && propertyVO.getGetNameMethodName().length() > 0 &&
				propertyVO.getGetValueMethodName() != null && propertyVO.getGetValueMethodName().length() > 0) {
			return true;
		}
		return false;
	}

	public static String trialIdToName(Long id) {
		if (id == null) {
			return getNoTrialPickedMessage();
		}
		try {
			TrialOutVO trial = getServiceLocator().getTrialService().getTrial(getAuthentication(), id);
			return trialOutVOToString(trial);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_TRIAL_PICKED);
	}

	public static String trialOutVOToString(TrialOutVO trial) {
		String result = CommonUtil.trialOutVOToString(trial);
		if (result != null) {
			return result;
		} else {
			return getNoTrialPickedMessage();
		}
	}

	public static String userIdToName(Long id) {
		if (id == null) {
			return getNoUserPickedMessage();
		}
		try {
			UserOutVO user = getServiceLocator().getUserService().getUser(getAuthentication(), id, null);
			return userOutVOToString(user);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			publishException(e);
		}
		return Messages.getString(MessageCodes.INVALID_USER_PICKED);
	}

	public static String userOutVOToString(UserOutVO user) {
		String result = CommonUtil.userOutVOToString(user);
		if (result != null) {
			return result;
		} else {
			return getNoUserPickedMessage();
		}
	}

	public static String variablePeriodToString(VariablePeriodVO periodVO, Long days) {
		if (periodVO != null) {
			if (VariablePeriod.EXPLICIT.equals(periodVO.getPeriod())) {
				return Messages.getMessage(MessageCodes.DAYS_LABEL, days);
			} else {
				return periodVO.getName();
			}
		}
		return "";
	}

	public static TrialOutVO getRandomizationCodeTrial(RandomizationListCodeOutVO code) {
		if (code != null) {
			if (code.getStratificationRandomizationList() != null) {
				return code.getStratificationRandomizationList().getTrial();
			}
			return getTrial(code.getTrialRandomizationList().getId());
		}
		return null;
	}

	public static void clearResourceBundleCache() throws Exception {
		WebUtil.getServiceLocator().getToolsService().clearResourceBundleCache();
		//https://stackoverflow.com/questions/4325164/how-to-reload-resource-bundle-in-web-application
		ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());
		Iterator<ApplicationResourceBundle> it = ApplicationAssociate.getCurrentInstance().getResourceBundles().values().iterator();
		while (it.hasNext()) {
			ApplicationResourceBundle appBundle = it.next();
			Map<Locale, ResourceBundle> resources = CommonUtil.getDeclaredFieldValue(appBundle, "resources");
			resources.clear();
		}
	}

	private WebUtil() {
	}
}
