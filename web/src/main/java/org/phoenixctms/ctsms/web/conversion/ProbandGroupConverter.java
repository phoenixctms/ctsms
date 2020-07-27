package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = ProbandGroupConverter.CONVERTER_ID)
public class ProbandGroupConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.PROAND_GROUP_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		ProbandGroupOutVO group = (ProbandGroupOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (group != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, group.getTrial().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_TOKEN, group.getToken());
			details.put(MessageCodes.CRITERION_ITEM_TIP_TITLE, group.getTitle());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof ProbandGroupOutVO) {
			return ((ProbandGroupOutVO) vo).getTitle();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof ProbandGroupOutVO) {
			return ((ProbandGroupOutVO) vo).getToken();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getProbandGroup(id);
	}
}