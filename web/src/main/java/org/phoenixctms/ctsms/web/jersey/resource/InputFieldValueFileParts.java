package org.phoenixctms.ctsms.web.jersey.resource;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.FileContentInVO;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

/**
 * Multipart variant of the input field value list PUTs:
 * part "json" holds the value list as JSON array, parts "data_&lt;n&gt;" hold the file content
 * for the value at 0-based position n of that array.
 */
public final class InputFieldValueFileParts {

	public interface FileValueAccessor<VALUEVO> {

		public FileContentInVO getFileValue(VALUEVO value);

		public void setFileValue(VALUEVO value, FileContentInVO fileValue);

		public void setFileValueId(VALUEVO value, Long fileValueId);
	}

	public static final String JSON_PART_NAME = "json";
	private static final Pattern DATA_PART_NAME_REGEXP = Pattern.compile("^data_(\\d+)$");
	private static final String JSON_PART_REQUIRED = "multipart part ''{0}'' required";
	private static final String JSON_PART_MULTIPLE = "multipart part ''{0}'' must occur only once";
	private static final String UNKNOWN_PART = "unknown multipart part ''{0}'' (expected ''{1}'' or ''data_<n>'')";
	private static final String DATA_PART_MULTIPLE = "multipart part ''{0}'' must occur only once";
	private static final String DATA_PART_INDEX_OUT_OF_RANGE = "multipart part ''{0}'' does not refer to an element of the json array (size {1})";
	private static final String DATA_PART_INDEX_DUPLICATE = "multipart part ''{0}'' refers to the same json array element as part ''{1}''";
	private static final String JSON_FILE_VALUE_NOT_ALLOWED = "json array element {0} must not contain fileValue in multipart mode (use part ''data_{0}'')";
	private static final String DATA_PART_EMPTY = "multipart part ''{0}'' is empty";

	public static <VALUEVO> ArrayList<VALUEVO> getValues(FormDataMultiPart multiPart, Class<VALUEVO[]> arrayClass, FileValueAccessor<VALUEVO> accessor) throws Exception {
		List<FormDataBodyPart> jsonParts = multiPart.getFields(JSON_PART_NAME);
		if (jsonParts == null || jsonParts.size() == 0) {
			throw new IllegalArgumentException(MessageFormat.format(JSON_PART_REQUIRED, JSON_PART_NAME));
		} else if (jsonParts.size() > 1) {
			throw new IllegalArgumentException(MessageFormat.format(JSON_PART_MULTIPLE, JSON_PART_NAME));
		}
		FormDataBodyPart json = jsonParts.get(0);
		json.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		VALUEVO[] valueArray = json.getValueAs(arrayClass);
		ArrayList<VALUEVO> values = new ArrayList<VALUEVO>(valueArray != null ? Arrays.asList(valueArray) : new ArrayList<VALUEVO>());
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) != null && accessor.getFileValue(values.get(i)) != null) {
				throw new IllegalArgumentException(MessageFormat.format(JSON_FILE_VALUE_NOT_ALLOWED, Integer.toString(i)));
			}
		}
		HashMap<Integer, String> dataPartNames = new HashMap<Integer, String>();
		Iterator<Entry<String, List<FormDataBodyPart>>> it = multiPart.getFields().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<FormDataBodyPart>> field = it.next();
			String name = field.getKey();
			if (JSON_PART_NAME.equals(name)) {
				continue;
			}
			Matcher matcher = DATA_PART_NAME_REGEXP.matcher(name);
			if (!matcher.find()) {
				throw new IllegalArgumentException(MessageFormat.format(UNKNOWN_PART, name, JSON_PART_NAME));
			}
			if (field.getValue().size() > 1) {
				throw new IllegalArgumentException(MessageFormat.format(DATA_PART_MULTIPLE, name));
			}
			int index;
			try {
				index = Integer.parseInt(matcher.group(1));
			} catch (NumberFormatException e) {
				index = -1;
			}
			if (index < 0 || index >= values.size() || values.get(index) == null) {
				throw new IllegalArgumentException(MessageFormat.format(DATA_PART_INDEX_OUT_OF_RANGE, name, Integer.toString(values.size())));
			}
			if (dataPartNames.containsKey(index)) {
				throw new IllegalArgumentException(MessageFormat.format(DATA_PART_INDEX_DUPLICATE, name, dataPartNames.get(index)));
			}
			dataPartNames.put(index, name);
			FormDataBodyPart data = field.getValue().get(0);
			byte[] datas = CommonUtil.inputStreamToByteArray(data.getValueAs(InputStream.class));
			if (datas == null || datas.length == 0) {
				throw new IllegalArgumentException(MessageFormat.format(DATA_PART_EMPTY, name));
			}
			VALUEVO value = values.get(index);
			accessor.setFileValue(value, new FileContentInVO(
					data.getFormDataContentDisposition() != null ? data.getFormDataContentDisposition().getFileName() : null,
					data.getMediaType() != null ? data.getMediaType().getType() + "/" + data.getMediaType().getSubtype() : null,
					datas));
			accessor.setFileValueId(value, null);
		}
		return values;
	}

	private InputFieldValueFileParts() {
	}
}
