package org.phoenixctms.ctsms.js;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CriterionOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public final class JsUtil {

	public final static String INPUT_JSON_DATETIME_PATTERN = "yyyy-MM-dd " + CommonUtil.DEFAULT_INPUT_TIME_PATTERN;
	public final static String JSON_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private final static String BASE64_CHARSET = "UTF8";
	public static final GsonExclusionStrategy[] GSON_EXCLUSION_STRATEGIES = new GsonExclusionStrategy[] {
			new GsonExclusionStrategy(UserOutVO.class, "modifiedUser"),
			new GsonExclusionStrategy(StaffOutVO.class, "modifiedUser"),
			new GsonExclusionStrategy(InventoryOutVO.class, "children"),
			new GsonExclusionStrategy(StaffOutVO.class, "children"),
			new GsonExclusionStrategy(CourseOutVO.class, "renewals"),
			new GsonExclusionStrategy(CourseOutVO.class, "precedingCourses"),
			// new GsonExclusionStrategy(InputFieldOutVO.class, "selectionSetValues"),
			new GsonExclusionStrategy(InputFieldSelectionSetValueOutVO.class, "field"),
			// new GsonExclusionStrategy(CriteriaOutVO.class, "criterions"),
			new GsonExclusionStrategy(CriterionOutVO.class, "criteria"),
			// new GsonExclusionStrategy(ProbandListEntryOutVO.class, "lastStatus"),
			new GsonExclusionStrategy(ProbandListStatusEntryOutVO.class, "listEntry"),
			new GsonExclusionStrategy(ProbandOutVO.class, "children"),
			new GsonExclusionStrategy(ProbandOutVO.class, "parents"),
	};
	public static final HashMap<Class, JsonSerializer> GSON_SHORTCUT_SERIALISATIONS = new HashMap<Class, JsonSerializer>();
	static {
		GSON_SHORTCUT_SERIALISATIONS.put(UserOutVO.class, new JsonSerializer<UserOutVO>() {

			@Override
			public JsonElement serialize(UserOutVO src, Type typeOfSrc, JsonSerializationContext context) {
				JsonObject object = new JsonObject();
				object.addProperty("id", src.getId());
				object.addProperty("userName", src.getName());
				object.addProperty("locale", src.getLocale());
				object.addProperty("staffName", src.getIdentity() != null ? src.getIdentity().getName() : null);
				object.addProperty("staffInitials", src.getIdentity() != null ? src.getIdentity().getInitials() : null);
				return object;
			}
		});
	}
	private final static Gson INPUT_FIELD_VARIABLE_VALUE_JSON_SERIALIZER = registerGsonTypeAdapters(new GsonBuilder(),
			GSON_SHORTCUT_SERIALISATIONS).setExclusionStrategies(GSON_EXCLUSION_STRATEGIES)
					.serializeNulls()
					.setDateFormat(INPUT_JSON_DATETIME_PATTERN) // CommonUtil.INPUT_DATETIME_PATTERN)
					.create();
	private final static Gson VO_JSON_SERIALIZER = registerGsonTypeAdapters(new GsonBuilder(),
			JsUtil.GSON_SHORTCUT_SERIALISATIONS).setExclusionStrategies(JsUtil.GSON_EXCLUSION_STRATEGIES)
					.serializeNulls()
					.setDateFormat(JSON_DATETIME_PATTERN)
					.create();

	public static String decodeBase64(String base64String) {
		if (base64String != null && base64String.length() > 0 && Base64.isBase64(base64String)) {
			try {
				return new String(Base64.decodeBase64(base64String), BASE64_CHARSET);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return "";
	}

	public static String encodeBase64(String string, boolean urlSafe) { // url safe
		if (string != null && string.length() > 0) {
			try {
				return new String(Base64.encodeBase64(string.getBytes(BASE64_CHARSET), false, urlSafe, Integer.MAX_VALUE));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return "";
	}

	public static String inputFieldVariableValueToJson(Object src) { // , final TimeZone timeZone) {
		return JsUtil.INPUT_FIELD_VARIABLE_VALUE_JSON_SERIALIZER.toJson(src);
		// final DateFormat dateFormat = new SimpleDateFormat(INPUT_JSON_DATETIME_PATTERN);
		// if (timeZone != null) {
		// dateFormat.setTimeZone(timeZone);
		// }
		// return registerGsonTypeAdapters(new GsonBuilder(),
		// GSON_SHORTCUT_SERIALISATIONS)
		// .registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
		//
		// @Override
		// public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
		// return new JsonPrimitive(dateFormat.format(date));
		// }
		// })
		// .setExclusionStrategies(GSON_EXCLUSION_STRATEGIES)
		// .serializeNulls()
		// // .setDateFormat(JSON_DATETIME_PATTERN) // CommonUtil.INPUT_DATETIME_PATTERN)
		// .create().toJson(src);
	}

	public static GsonBuilder registerGsonTypeAdapters(GsonBuilder builder, HashMap<Class, JsonSerializer> serialisations) {
		Iterator<Entry<Class, JsonSerializer>> it = serialisations.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Class, JsonSerializer> shortcut = it.next();
			builder.registerTypeAdapter(shortcut.getKey(), shortcut.getValue());
		}
		return builder;
	}

	public static String voToJson(Object src) {
		return VO_JSON_SERIALIZER.toJson(src);
	}

	private JsUtil() {
	}
}
