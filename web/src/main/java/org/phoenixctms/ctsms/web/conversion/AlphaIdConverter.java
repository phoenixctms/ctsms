package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.AlphaIdVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = AlphaIdConverter.CONVERTER_ID)
public class AlphaIdConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.ALPHA_ID_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		Object vo = (idvo != null ? idvo.getVo() : null);
		if (vo instanceof AlphaIdVO) {
			AlphaIdVO alphaId = (AlphaIdVO) vo;
			details.put(MessageCodes.CRITERION_ITEM_TIP_ALPHA_ID_ALPHA_ID, alphaId.getAlphaId());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ALPHA_ID_PRIMARY_CODE, alphaId.getPrimaryCode());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ALPHA_ID_REVISION, alphaId.getRevision());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof AlphaIdVO) {
			return ((AlphaIdVO) vo).getText();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof AlphaIdVO) {
			return ((AlphaIdVO) vo).getPrimaryCode();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getAlphaId(id);
	}
}