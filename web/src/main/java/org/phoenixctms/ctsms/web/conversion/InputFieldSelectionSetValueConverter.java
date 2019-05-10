package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.LightInputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = InputFieldSelectionSetValueConverter.CONVERTER_ID)
public class InputFieldSelectionSetValueConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.INPUT_FIELD_SELECTION_SET_VALUE_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		InputFieldSelectionSetValueOutVO value = (InputFieldSelectionSetValueOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (value != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_NAME, value.getField().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_TYPE, value.getField().getFieldType().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_CATEGORY, value.getField().getCategory());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_SELECTION_SET_VALUE_NAME, value.getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_SELECTION_SET_VALUE_VALUE, value.getValue());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof InputFieldSelectionSetValueOutVO) {
			return ((InputFieldSelectionSetValueOutVO) vo).getUniqueName();
		} else if (vo instanceof LightInputFieldSelectionSetValueOutVO) {
			return ((LightInputFieldSelectionSetValueOutVO) vo).getUniqueName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof InputFieldSelectionSetValueOutVO) {
			return ((InputFieldSelectionSetValueOutVO) vo).getName();
		} else if (vo instanceof LightInputFieldSelectionSetValueOutVO) {
			return ((LightInputFieldSelectionSetValueOutVO) vo).getName();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getInputFieldSelectionSetValue(id);
	}
}