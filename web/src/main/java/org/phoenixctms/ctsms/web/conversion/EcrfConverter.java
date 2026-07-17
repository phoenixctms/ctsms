package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = EcrfConverter.CONVERTER_ID)
public class EcrfConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.ECRF_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		ECRFOutVO ecrf = (ECRFOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (ecrf != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, ecrf.getTrial().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ECRF_NAME, ecrf.getUniqueName());
			if (!CommonUtil.isEmptyString(ecrf.getExternalId())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_ECRF_EXTERNAL_ID, ecrf.getExternalId());
			}
			if (!CommonUtil.isEmptyString(ecrf.getTitle())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_TITLE, ecrf.getTitle());
			}
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof ECRFOutVO) {
			return ((ECRFOutVO) vo).getUniqueName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getEcrf(id);
	}
}
