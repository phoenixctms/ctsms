


package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.AspAtcCodeVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = AspAtcCodeConverter.CONVERTER_ID)
public class AspAtcCodeConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.ASP_ATC_CODE_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		Object vo = idvo.getVo();
		if (vo instanceof AspAtcCodeVO) {
			AspAtcCodeVO atcCode = (AspAtcCodeVO) vo;
			// details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_NAME, asp.getName());
			// details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_SUBSTANCE_ATC_CODE, substance.getAtcCode());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_ATC_CODE_REVISION, atcCode.getRevision());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof AspAtcCodeVO) {
			return ((AspAtcCodeVO) vo).getCode();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getAspAtcCode(id);
	}
}