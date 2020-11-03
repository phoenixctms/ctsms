package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = VisitConverter.CONVERTER_ID)
public class VisitConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.VISIT_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		VisitOutVO visit = (VisitOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (visit != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, visit.getTrial().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_TOKEN, visit.getToken());
			details.put(MessageCodes.CRITERION_ITEM_TIP_TITLE, visit.getTitle());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof VisitOutVO) {
			return ((VisitOutVO) vo).getTitle();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof VisitOutVO) {
			return ((VisitOutVO) vo).getToken();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getVisit(id);
	}
}