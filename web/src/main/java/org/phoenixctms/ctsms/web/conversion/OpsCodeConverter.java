package org.phoenixctms.ctsms.web.conversion;

import java.util.LinkedHashMap;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.OpsCodeVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = OpsCodeConverter.CONVERTER_ID)
public class OpsCodeConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.OPS_CODE_CONVERTER_ID;

	@Override
	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
		Object vo = idvo.getVo();
		if (vo instanceof OpsCodeVO) {
			OpsCodeVO opsCode = (OpsCodeVO) vo;
			details.put(MessageCodes.CRITERION_ITEM_TIP_OPS_CODE_DIMDI_ID, opsCode.getDimdiId());
			details.put(MessageCodes.CRITERION_ITEM_TIP_OPS_CODE_FIRST_CODE, opsCode.getFirstCode());
			details.put(MessageCodes.CRITERION_ITEM_TIP_OPS_CODE_REVISION, opsCode.getRevision());
		}
		return details;
	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof OpsCodeVO) {
			return ((OpsCodeVO) vo).getText();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof OpsCodeVO) {
			return ((OpsCodeVO) vo).getFirstCode();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getOpsCode(id);
	}
}