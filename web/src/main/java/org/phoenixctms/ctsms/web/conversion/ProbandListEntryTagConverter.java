package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.LightProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = ProbandListEntryTagConverter.CONVERTER_ID)
public class ProbandListEntryTagConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.PROBAND_LIST_ENTRY_TAG_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		ProbandListEntryTagOutVO tag = (ProbandListEntryTagOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (tag != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, tag.getTrial().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_NAME, tag.getField().getName());
			if (!CommonUtil.isEmptyString(tag.getTitle())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_TITLE, tag.getTitle());
			}
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_TYPE, tag.getField().getFieldType().getName());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof ProbandListEntryTagOutVO) {
			return ((ProbandListEntryTagOutVO) vo).getUniqueName();
		} else if (vo instanceof LightProbandListEntryTagOutVO) {
			return ((LightProbandListEntryTagOutVO) vo).getUniqueName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getProbandListEntryTag(id);
	}
}