
package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.AspSubstanceVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = AspSubstanceConverter.CONVERTER_ID)
public class AspSubstanceConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.ASP_SUBSTANCE_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		Object vo = (idvo != null ? idvo.getVo() : null);
		if (vo instanceof AspSubstanceVO) {
			AspSubstanceVO substance = (AspSubstanceVO) vo;
			// details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_NAME, asp.getName());
			// details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_SUBSTANCE_ATC_CODE, substance.getAtcCode());
			details.put(MessageCodes.CRITERION_ITEM_TIP_ASP_SUBSTANCE_REVISION, substance.getRevision());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof AspSubstanceVO) {
			return ((AspSubstanceVO) vo).getName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getAspSubstance(id);
	}
}