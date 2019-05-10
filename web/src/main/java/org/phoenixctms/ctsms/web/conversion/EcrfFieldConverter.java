package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.LightECRFFieldOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = EcrfFieldConverter.CONVERTER_ID)
public class EcrfFieldConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.ECRF_FIELD_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		ECRFFieldOutVO ecrfField = (ECRFFieldOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (ecrfField != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, ecrfField.getTrial().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ECRF_NAME, ecrfField.getEcrf().getUniqueName());
			if (!CommonUtil.isEmptyString(ecrfField.getEcrf().getExternalId())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_ECRF_EXTERNAL_ID, ecrfField.getEcrf().getExternalId());
			}
			details.put(MessageCodes.CRITERION_ITEM_TIP_ECRF_FIELD_SECTION, ecrfField.getSection());
			if (!CommonUtil.isEmptyString(ecrfField.getExternalId())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_ECRF_FIELD_EXTERNAL_ID, ecrfField.getExternalId());
			}
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_NAME, ecrfField.getField().getName());
			if (!CommonUtil.isEmptyString(ecrfField.getTitle())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_TITLE, ecrfField.getTitle());
			}
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_TYPE, ecrfField.getField().getFieldType().getName());
			if (!CommonUtil.isEmptyString(ecrfField.getField().getExternalId())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_EXTERNAL_ID, ecrfField.getField().getExternalId());
			}
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof ECRFFieldOutVO) {
			return ((ECRFFieldOutVO) vo).getUniqueName();
		} else if (vo instanceof LightECRFFieldOutVO) {
			return ((LightECRFFieldOutVO) vo).getUniqueName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getEcrfField(id);
	}
}