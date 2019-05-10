package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.AspVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = AspConverter.CONVERTER_ID)
public class AspConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.ASP_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		Object vo = (idvo != null ? idvo.getVo() : null);
		if (vo instanceof AspVO) {
			AspVO asp = (AspVO) vo;
			details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_REGISTRATION_NUMBER, asp.getRegistrationNumber());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_ATC_CODES, asp.getAtcCodesLabel());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_REVISION, asp.getRevision());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof AspVO) {
			return ((AspVO) vo).getName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getAsp(id);
	}
}