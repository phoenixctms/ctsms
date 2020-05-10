package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.trial.TimelineEventBeanBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = TimelineEventConverter.CONVERTER_ID)
public class TimelineEventConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.TIMELINE_EVENT_CONVERTER_ID;

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof TimelineEventOutVO) {
			return ((TimelineEventOutVO) vo).getTitle();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getTimelineEvent(id, TimelineEventBeanBase.GRAPH_MAX_TIMElINE_EVENT_INSTANCES, TimelineEventBeanBase.GRAPH_MAX_TIMElINE_EVENT_PARENT_DEPTH,
				TimelineEventBeanBase.GRAPH_MAX_TIMElINE_EVENT_CHILDREN_DEPTH);
	}
}