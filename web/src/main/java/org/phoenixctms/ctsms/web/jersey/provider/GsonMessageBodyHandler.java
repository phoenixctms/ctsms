package org.phoenixctms.ctsms.web.jersey.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.web.jersey.wrapper.NoShortcutSerializationWrapper;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GsonMessageBodyHandler implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

	private final static boolean ENABLE_TIMEZONE_CONVERSION = true;
	private final static String API_JSON_SERIALIZE_VALUE_CHARSET = "UTF8";
	private final static String API_JSON_DESERIALIZE_VALUE_CHARSET = "UTF8";
	private final static String API_JSON_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMEZONE_QUERY_PARAM = "tz";
	private final static String FIELD_VALUE_VO_TIMESTAMP_FIELD_NAME = "timestampValue";
	private final static String FIELD_VALUE_VO_DATE_FIELD_NAME = "dateValue";
	private final static String FIELD_VALUE_VO_TIME_FIELD_NAME = "timeValue";
	private final Gson serializer = buildSerializer();
	private final Gson shortcutSerializer = buildShortcutSerializer();
	private final Gson deserializer = buildDeserializer();
	@Context
	UriInfo uriInfo;

	private TimeZone getTimezone() {
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		String timeZoneId = queryParams.getFirst(TIMEZONE_QUERY_PARAM);
		if (!CommonUtil.isEmptyString(timeZoneId)) {
			TimeZone timeZone = CommonUtil.timeZoneFromString(timeZoneId);
			if (timeZoneId.equals(timeZone.getID())) {
				return timeZone;
			} else {
				throw new IllegalArgumentException(
						Messages.getMessage(MessageCodes.UNKNOWN_TIMEZONE, timeZoneId));
			}
		}
		return null;
	}

	//https://stackoverflow.com/questions/6873020/gson-date-format
	private JsonSerializer getDateSerializer() {
		return new JsonSerializer<Date>() {

			@Override
			public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
				if (src != null) {
					DateFormat dateFormat = new SimpleDateFormat(API_JSON_DATETIME_PATTERN);
					if (ENABLE_TIMEZONE_CONVERSION) {
						TimeZone timeZone = getTimezone();
						if (timeZone != null) {
							dateFormat.setTimeZone(timeZone);
						}
					}
					return new JsonPrimitive(dateFormat.format(src));
				}
				return null;
			}
		};
	}

	private JsonDeserializer getDateDeserializer() {
		return new JsonDeserializer<Date>() {

			@Override
			public Date deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context) throws JsonParseException {
				if (json != null) {
					TimeZone timeZone = null;
					if (ENABLE_TIMEZONE_CONVERSION) {
						timeZone = getTimezone();
					}
					if (timeZone != null) {
						return CommonUtil.parseDate(json.getAsString(), API_JSON_DATETIME_PATTERN, timeZone);
					} else {
						return CommonUtil.parseDate(json.getAsString(), API_JSON_DATETIME_PATTERN);
					}
				}
				return null;
			}
		};
	}

	private static void updateFieldValueVODateValue(JsonObject json, String fieldName, Date timestampValue) {
		if (json != null && timestampValue != null) {
			//TimeZone timeZone = getTimezone();
			//if (timeZone != null) {
			DateFormat dateFormat = new SimpleDateFormat(API_JSON_DATETIME_PATTERN);
			//	dateFormat.setTimeZone(timeZone);
			json.remove(fieldName);
			json.add(fieldName, new JsonPrimitive(dateFormat.format(timestampValue)));
			//}
		}
	}

	private static HashMap<Class, JsonSerializer> getFieldValueVOSerializers(final Gson gson) {
		HashMap<Class, JsonSerializer> serializations = new HashMap<Class, JsonSerializer>();
		serializations.put(ECRFFieldValueOutVO.class, new JsonSerializer<ECRFFieldValueOutVO>() {

			@Override
			public JsonElement serialize(ECRFFieldValueOutVO src, Type typeOfSrc, JsonSerializationContext context) {
				JsonObject json = (JsonObject) gson.toJsonTree(src);
				if (src != null) {
					if (!src.getEcrfField().getField().getUserTimeZone()) {
						updateFieldValueVODateValue(json, FIELD_VALUE_VO_TIMESTAMP_FIELD_NAME, src.getTimestampValue());
					}
					updateFieldValueVODateValue(json, FIELD_VALUE_VO_DATE_FIELD_NAME, src.getDateValue());
					updateFieldValueVODateValue(json, FIELD_VALUE_VO_TIME_FIELD_NAME, src.getTimeValue());
				}
				return json;
			}
		});
		serializations.put(ProbandListEntryTagValueOutVO.class, new JsonSerializer<ProbandListEntryTagValueOutVO>() {

			@Override
			public JsonElement serialize(ProbandListEntryTagValueOutVO src, Type typeOfSrc, JsonSerializationContext context) {
				JsonObject json = (JsonObject) gson.toJsonTree(src);
				if (src != null) {
					if (!src.getTag().getField().getUserTimeZone()) {
						updateFieldValueVODateValue(json, FIELD_VALUE_VO_TIMESTAMP_FIELD_NAME, src.getTimestampValue());
					}
					updateFieldValueVODateValue(json, FIELD_VALUE_VO_DATE_FIELD_NAME, src.getDateValue());
					updateFieldValueVODateValue(json, FIELD_VALUE_VO_TIME_FIELD_NAME, src.getTimeValue());
				}
				return json;
			}
		});
		serializations.put(InquiryValueOutVO.class, new JsonSerializer<InquiryValueOutVO>() {

			@Override
			public JsonElement serialize(InquiryValueOutVO src, Type typeOfSrc, JsonSerializationContext context) {
				JsonObject json = (JsonObject) gson.toJsonTree(src);
				if (src != null) {
					if (!src.getInquiry().getField().getUserTimeZone()) {
						updateFieldValueVODateValue(json, FIELD_VALUE_VO_TIMESTAMP_FIELD_NAME, src.getTimestampValue());
					}
					updateFieldValueVODateValue(json, FIELD_VALUE_VO_DATE_FIELD_NAME, src.getDateValue());
					updateFieldValueVODateValue(json, FIELD_VALUE_VO_TIME_FIELD_NAME, src.getTimeValue());
				}
				return json;
			}
		});
		return serializations;
	}

	private static HashMap<Class, JsonDeserializer> getFieldValueVODeserializers(final Gson gson) {
		HashMap<Class, JsonDeserializer> deserializations = new HashMap<Class, JsonDeserializer>();
		deserializations.put(ECRFFieldValueInVO.class, new JsonDeserializer<ECRFFieldValueInVO>() {

			@Override
			public ECRFFieldValueInVO deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context) throws JsonParseException {
				ECRFFieldValueInVO in = gson.fromJson(json, ECRFFieldValueInVO.class);
				if (in != null && json != null) {
					if (in.getTimestampValue() != null) {
						boolean isUserTimeZone = false;
						try {
							isUserTimeZone = WebUtil.getServiceLocator().getToolsService().isEcrfFieldUserTimeZone(in.getEcrfFieldId());
						} catch (AuthenticationException | AuthorisationException | ServiceException e) {
						}
						if (!isUserTimeZone) {
							in.setTimestampValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_TIMESTAMP_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
						}
					}
					in.setDateValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_DATE_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
					in.setTimeValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_TIME_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
				}
				return null;
			}
		});
		deserializations.put(ProbandListEntryTagValueInVO.class, new JsonDeserializer<ProbandListEntryTagValueInVO>() {

			@Override
			public ProbandListEntryTagValueInVO deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context) throws JsonParseException {
				ProbandListEntryTagValueInVO in = gson.fromJson(json, ProbandListEntryTagValueInVO.class);
				if (in != null && json != null) {
					if (in.getTimestampValue() != null) {
						boolean isUserTimeZone = false;
						try {
							isUserTimeZone = WebUtil.getServiceLocator().getToolsService().isProbandListEntryTagUserTimeZone(in.getTagId());
						} catch (AuthenticationException | AuthorisationException | ServiceException e) {
						}
						if (!isUserTimeZone) {
							in.setTimestampValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_TIMESTAMP_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
						}
					}
					in.setDateValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_DATE_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
					in.setTimeValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_TIME_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
				}
				return null;
			}
		});
		deserializations.put(InquiryValueInVO.class, new JsonDeserializer<InquiryValueInVO>() {

			@Override
			public InquiryValueInVO deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context) throws JsonParseException {
				InquiryValueInVO in = gson.fromJson(json, InquiryValueInVO.class);
				if (in != null && json != null) {
					if (in.getTimestampValue() != null) {
						boolean isUserTimeZone = false;
						try {
							isUserTimeZone = WebUtil.getServiceLocator().getToolsService().isInquiryUserTimeZone(in.getInquiryId());
						} catch (AuthenticationException | AuthorisationException | ServiceException e) {
						}
						if (!isUserTimeZone) {
							in.setTimestampValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_TIMESTAMP_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
						}
					}
					in.setDateValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_DATE_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
					in.setTimeValue(CommonUtil.parseDate(json.getAsJsonObject().get(FIELD_VALUE_VO_TIME_FIELD_NAME).getAsString(), API_JSON_DATETIME_PATTERN));
				}
				return null;
			}
		});
		return deserializations;
	}

	private Gson buildDeserializer() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, getDateDeserializer());
		// https://gist.github.com/miere/3202425
		builder.registerTypeAdapter(Collection.class, new JsonDeserializer<Collection<?>>() {

			@Override
			public Collection<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
				Type collectionType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
				return parseAsArrayList(json, collectionType, context);
			}

			@SuppressWarnings("unchecked")
			public <T> ArrayList<T> parseAsArrayList(JsonElement collection, T type, JsonDeserializationContext context) {
				ArrayList<T> result = new ArrayList<T>();
				Iterator<JsonElement> it = collection.getAsJsonArray().iterator();
				while (it.hasNext()) {
					result.add((T) context.deserialize(it.next(), (Class<?>) type));
				}
				return result;
			}
		});
		JsUtil.registerTypeAdapters(builder, getFieldValueVODeserializers(builder.create()));
		return builder.create();
	}

	private Gson buildSerializer() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, getDateSerializer());
		builder.serializeNulls();
		builder.setExclusionStrategies(JsUtil.GSON_EXCLUSION_STRATEGIES);
		builder.registerTypeAdapter(NoShortcutSerializationWrapper.class, new JsonSerializer<NoShortcutSerializationWrapper<?>>() {

			@Override
			public JsonElement serialize(NoShortcutSerializationWrapper<?> src, Type typeOfSrc, JsonSerializationContext context) {
				return context.serialize(src.getVo());
			}
		});
		JsUtil.registerTypeAdapters(builder, getFieldValueVOSerializers(builder.create()));
		return builder.create();
	}

	private Gson buildShortcutSerializer() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, getDateSerializer());
		builder.serializeNulls();
		builder.setExclusionStrategies(JsUtil.GSON_EXCLUSION_STRATEGIES);
		builder.registerTypeAdapter(NoShortcutSerializationWrapper.class, new JsonSerializer<NoShortcutSerializationWrapper<?>>() {

			@Override
			public JsonElement serialize(NoShortcutSerializationWrapper<?> src, Type typeOfSrc, JsonSerializationContext context) {
				return serializer.toJsonTree(src.getVo()); // use regular serializer
			}
		});
		JsUtil.registerTypeAdapters(builder, JsUtil.GSON_SHORTCUT_SERIALIZATIONS);
		JsUtil.registerTypeAdapters(builder, getFieldValueVOSerializers(builder.create()));
		return builder.create();
	}

	@Override
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException {
		try {
			InputStreamReader r = new InputStreamReader(entityStream, API_JSON_DESERIALIZE_VALUE_CHARSET);
			return deserializer.fromJson(r, genericType);
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(e);
		}
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException {
		try {
			final Writer w = new OutputStreamWriter(entityStream, API_JSON_SERIALIZE_VALUE_CHARSET);
			final JsonWriter jsw = new JsonWriter(w);
			shortcutSerializer.toJson(t, type, jsw);
			jsw.close();
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(e);
		}
	}
}