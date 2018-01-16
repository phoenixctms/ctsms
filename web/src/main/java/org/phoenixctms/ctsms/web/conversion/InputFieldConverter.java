package org.phoenixctms.ctsms.web.conversion;


import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = InputFieldConverter.CONVERTER_ID)
public class InputFieldConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.INPUT_FIELD_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		Object vo = (idvo != null ? idvo.getVo() : null);
		if (vo instanceof InputFieldOutVO) {
			InputFieldOutVO field = (InputFieldOutVO) vo;
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_TYPE, field.getFieldType().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_CATEGORY, field.getCategory());
			if (!CommonUtil.isEmptyString(field.getExternalId())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_EXTERNAL_ID, field.getExternalId());
			}
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof InputFieldOutVO) {
			return ((InputFieldOutVO) vo).getName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getInputField(id);
	}
}