package org.phoenixctms.ctsms.js;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.springframework.core.io.ClassPathResource;

import jdk.nashorn.api.scripting.JSObject;

@SuppressWarnings("restriction")
public class FieldCalculation {

	private final static String ENV_JS_FILE_NAME = "env.js";
	private final static String FIELD_CALCULATION_JS_FILE_NAME = "fieldCalculation.js";
	private final static String SPRINTF_JS_FILE_NAME = "sprintf.js";
	private final static String JS_JODA_JS_FILE_NAME = "js-joda.min.js";
	private final static String JS_JODA_TIMEZONE_JS_FILE_NAME = "js-joda-timezone.min.js";
	private final static String STRIP_COMMENTS_JS_FILE_NAME = "strip-comments.js";
	private final static String JQUERY_BASE64_JS_FILE_NAME = "jquery.base64.js";
	private final static String REST_API_JS_FILE_NAME = "restApi.js";
	private final static String JSON2_JS_FILE_NAME = "json2.min.js";
	private final static String LOCATION_DISTANCE_JS_FILE_NAME = "locationDistance.js";
	private final static String AJAX_OPERATION_SUCCESS = "operationSuccess";
	private final static String AJAX_FIELD_DELTA_ERROR_MESSAGE_ID = "fieldDeltaErrorMessageId";
	private final static String AJAX_INPUT_FIELD_PROBAND_BASE64 = "inputFieldProbandBase64";
	private final static String AJAX_INPUT_FIELD_TRIAL_BASE64 = "inputFieldTrialBase64";
	private final static String AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64 = "inputFieldProbandAddressesBase64";
	private final static String AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_BASE64 = "inputFieldProbandListEntryBase64";
	private final static String AJAX_INPUT_FIELD_VISIT_SCHEDULE_ITEMS_BASE64 = "inputFieldVisitScheduleItemsBase64";
	private final static String AJAX_INPUT_FIELD_PROBAND_GROUPS_BASE64 = "inputFieldProbandGroupsBase64";
	private final static String AJAX_INPUT_FIELD_ACTIVE_USER_BASE64 = "activeUserBase64";
	private final static String AJAX_INPUT_FIELD_LOCALE = "inputFieldLocale";
	private final static String AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64 = "inputFieldVariableValuesBase64";
	private final static String AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_TAG_VALUES_BASE64 = "probandListEntryTagValuesBase64";
	private final static String DUMMY_ERROR_MESSAGE_ID = "dummy_error_message_id";
	private final static boolean FIELD_CALCULATION_ENCODE_BASE64 = false;
	private final static String INIT_INPUT_FIELD_VARIABLES = "initInputFieldVariables";
	private final static String UPDATE_INPUT_FIELD_VARIABLES = "updateInputFieldVariables";

	private static Invocable createEngine() throws ScriptException, IOException {
		ScriptEngine engine = CoreUtil.getJsEngine();
		engine.eval(getJsFile(ENV_JS_FILE_NAME));
		updateEnv(engine);
		engine.eval(getJsFile(SPRINTF_JS_FILE_NAME));
		engine.eval(getJsFile(JSON2_JS_FILE_NAME));
		engine.eval(getJsFile(JS_JODA_JS_FILE_NAME));
		engine.eval(getJsFile(JS_JODA_TIMEZONE_JS_FILE_NAME));
		engine.eval(getJsFile(STRIP_COMMENTS_JS_FILE_NAME));
		engine.eval(getJsFile(JQUERY_BASE64_JS_FILE_NAME));
		engine.eval(getJsFile(REST_API_JS_FILE_NAME));
		engine.eval(getJsFile(LOCATION_DISTANCE_JS_FILE_NAME));
		engine.eval(getJsFile(FIELD_CALCULATION_JS_FILE_NAME));
		return (Invocable) engine;
	}

	private static String encode(Object src) {
		String json = JsUtil.inputFieldVariableValueToJson(src);
		if (FIELD_CALCULATION_ENCODE_BASE64) {
			return JsUtil.encodeBase64(json, false);
		}
		return json;
	}

	private static String encodeVO(Object src) {
		String json = JsUtil.voToJson(src);
		if (FIELD_CALCULATION_ENCODE_BASE64) {
			return JsUtil.encodeBase64(json, false);
		}
		return json;
	}

	private static InputStreamReader getJsFile(String fileName) throws IOException {
		ClassPathResource resource = new ClassPathResource("/" + fileName);
		return new InputStreamReader(resource.getInputStream());
	}

	private final static void updateEnv(ScriptEngine engine) throws ScriptException {
		StringBuilder sb = new StringBuilder();
		sb.append("INPUT_JSON_DATETIME_PATTERN = '" + JsUtil.INPUT_JSON_DATETIME_PATTERN + "';");
		sb.append("VO_JSON_DATETIME_PATTERN = '" + JsUtil.VO_JSON_DATETIME_PATTERN + "';");
		sb.append("INPUT_DATE_PATTERN = '" + CommonUtil.getInputDatePattern(CoreUtil.getUserContext().getDateFormat()) + "';");
		sb.append("INPUT_TIME_PATTERN = '" + CommonUtil.getInputTimePattern(CoreUtil.getUserContext().getDateFormat()) + "';");
		sb.append("INPUT_DATETIME_PATTERN = '" + CommonUtil.getInputDateTimePattern(CoreUtil.getUserContext().getDateFormat()) + "';");
		if (CoreUtil.getUserContext().getDecimalSeparator() != null) {
			sb.append("INPUT_DECIMAL_SEPARATOR = '" + CoreUtil.getUserContext().getDecimalSeparator() + "';");
		}
		sb.append("INPUT_TIMEZONE_ID = '" + CommonUtil.timeZoneToString(CoreUtil.getUserContext().getTimeZone()) + "';");
		sb.append("SYSTEM_TIMEZONE_ID = '" + CommonUtil.timeZoneToString(TimeZone.getDefault()) + "';");
		sb.append("FIELD_CALCULATION_DEBUG_LEVEL = "
				+ Integer.toString(Settings.getInt(SettingCodes.FIELD_CALCULATION_DEBUG_LEVEL, Bundle.SETTINGS, DefaultSettings.FIELD_CALCULATION_DEBUG_LEVEL)) + ";");
		Integer inputFieldDeltaSummaryMax = Settings.getIntNullable(SettingCodes.INPUT_FIELD_DELTA_SUMMARY_MAX, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_DELTA_SUMMARY_MAX);
		if (inputFieldDeltaSummaryMax != null) {
			sb.append("INPUT_FIELD_DELTA_SUMMARY_MAX = " + Integer.toString(inputFieldDeltaSummaryMax) + ";");
		}
		engine.eval(sb.toString());
	}

	private Invocable invocable;
	private HashMap<String, Object> args;

	public FieldCalculation() throws ScriptException, IOException {
		args = new HashMap<String, Object>();
		invocable = createEngine();
		reset();
	}

	public ArrayList<ValidationError> initInputFieldVariables() throws ScriptException, NoSuchMethodException {
		return invoke(INIT_INPUT_FIELD_VARIABLES);
	}

	private ArrayList<ValidationError> invoke(String function) throws ScriptException, NoSuchMethodException {
		JSObject msgs = (JSObject) invocable.invokeFunction(function, new JSObjectMap(args));
		ArrayList<ValidationError> result = new ArrayList<ValidationError>();
		if (msgs != null) {
			Iterator<String> it = msgs.keySet().iterator();
			while (it.hasNext()) {
				int index = Integer.parseInt(it.next());
				result.add(new ValidationError((JSObject) msgs.getSlot(index)));
			}
		}
		return result;
	}

	public void reset() throws ScriptException {
		args.clear();
		args.put(AJAX_OPERATION_SUCCESS, true);
		args.put(AJAX_FIELD_DELTA_ERROR_MESSAGE_ID, DUMMY_ERROR_MESSAGE_ID);
	}

	public void setActiveUser(UserOutVO user) {
		args.put(AJAX_INPUT_FIELD_ACTIVE_USER_BASE64, encodeVO(user));
	}

	public void setECRFFieldInputFieldVariableValues(Collection<ECRFFieldValueJsonVO> jsValues) {
		args.put(AJAX_INPUT_FIELD_VARIABLE_VALUES_BASE64, encode(jsValues));
	}

	public void setLocale(Locales locale) {
		args.put(AJAX_INPUT_FIELD_LOCALE, CommonUtil.localeToString(L10nUtil.getLocale(locale)));
	}

	public void setProband(ProbandOutVO proband) {
		args.put(AJAX_INPUT_FIELD_PROBAND_BASE64, encodeVO(proband));
	}

	public void setProbandAddresses(Collection<ProbandAddressOutVO> addresses) {
		args.put(AJAX_INPUT_FIELD_PROBAND_ADDRESSES_BASE64, encodeVO(addresses));
	}

	public void setProbandGroups(Collection<ProbandGroupOutVO> probandGroups) {
		args.put(AJAX_INPUT_FIELD_PROBAND_GROUPS_BASE64, encodeVO(probandGroups));
	}

	public void setProbandListEntry(ProbandListEntryOutVO listEntry) {
		args.put(AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_BASE64, encodeVO(listEntry));
	}

	public void setProbandListEntryTagValues(Collection<ProbandListEntryTagValueJsonVO> probandListEntryTagValues) {
		args.put(AJAX_INPUT_FIELD_PROBAND_LIST_ENTRY_TAG_VALUES_BASE64, encode(probandListEntryTagValues));
	}

	public void setTrial(TrialOutVO trial) {
		args.put(AJAX_INPUT_FIELD_TRIAL_BASE64, encodeVO(trial));
	}

	public void setVisitScheduleItems(Collection<VisitScheduleItemOutVO> visitSchedule) {
		args.put(AJAX_INPUT_FIELD_VISIT_SCHEDULE_ITEMS_BASE64, encodeVO(visitSchedule));
	}

	public ArrayList<ValidationError> updateInputFieldVariables() throws ScriptException, NoSuchMethodException {
		return invoke(UPDATE_INPUT_FIELD_VARIABLES);
	}
}
