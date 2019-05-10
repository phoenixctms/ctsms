package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.LightInquiryOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = InquiryConverter.CONVERTER_ID)
public class InquiryConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.INQUIRY_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		InquiryOutVO inquiry = (InquiryOutVO) (idvo != null ? getVo(idvo.getId()) : null);
		if (inquiry != null) {
			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, inquiry.getTrial().getName());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INQUIRY_CATEGORY, inquiry.getCategory());
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_NAME, inquiry.getField().getName());
			if (!CommonUtil.isEmptyString(inquiry.getTitle())) {
				details.put(MessageCodes.CRITERION_ITEM_TIP_TITLE, inquiry.getTitle());
			}
			details.put(MessageCodes.CRITERION_ITEM_TIP_INPUT_FIELD_TYPE, inquiry.getField().getFieldType().getName());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof InquiryOutVO) {
			return ((InquiryOutVO) vo).getUniqueName();
		} else if (vo instanceof LightInquiryOutVO) {
			return ((LightInquiryOutVO) vo).getUniqueName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		return getLabel(idvo);
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getInquiry(id);
	}
}