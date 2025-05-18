package org.phoenixctms.ctsms.web.conversion;

import java.text.MessageFormat;
import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = VisitScheduleItemConverter.CONVERTER_ID)
public class VisitScheduleItemConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.VISIT_SCHEDULE_ITEM_CONVERTER_ID;
	private static final String TRIAL_VISIT_SCHEDULE_ITEM_NAME = "{0}: {1}";

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		VisitScheduleItemOutVO visitScheduleItem = (VisitScheduleItemOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (visitScheduleItem != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, visitScheduleItem.getTrial().getName());
			if (visitScheduleItem.getGroup() != null) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_GROUP_TOKEN, visitScheduleItem.getGroup().getToken());
				details.put(MessageCodes.CRITERION_ITEM_TIP_GROUP_TITLE, visitScheduleItem.getGroup().getTitle());
			}
			if (visitScheduleItem.getVisit() != null) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_VISIT_TOKEN, visitScheduleItem.getVisit().getToken());
				details.put(MessageCodes.CRITERION_ITEM_TIP_VISIT_TITLE, visitScheduleItem.getVisit().getTitle());
			}
			details.put(MessageCodes.CRITERION_ITEM_TIP_TOKEN, visitScheduleItem.getToken());
			details.put(MessageCodes.CRITERION_ITEM_TIP_DESCRIPTION, visitScheduleItem.getDescription());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof VisitScheduleItemOutVO) {
			return MessageFormat.format(TRIAL_VISIT_SCHEDULE_ITEM_NAME,
					CommonUtil.trialOutVOToString(((VisitScheduleItemOutVO) vo).getTrial()), ((VisitScheduleItemOutVO) vo).getName());
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof VisitScheduleItemOutVO) {
			return ((VisitScheduleItemOutVO) vo).getName();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getVisitScheduleItem(id);
	}
}