package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = DepartmentConverter.CONVERTER_ID)
public class DepartmentConverter extends IDVOConverter {

	public static final String CONVERTER_ID = JSFVOConverterIDs.DEPARTMENT_CONVERTER_ID;
	//	@Override
	//	public LinkedHashMap<String, String> getDetails(IDVO idvo) {
	//		LinkedHashMap<String, String> details = new LinkedHashMap<String, String>();
	//		DepartmentVO department = (DepartmentVO) (idvo != null ? getVo(idvo.getId()) : null);
	//		if (department != null) {
	//			details.put(MessageCodes.CRITERION_ITEM_TIP_TRIAL_NAME, visit.getTrial().getName());
	//			details.put(MessageCodes.CRITERION_ITEM_TIP_TOKEN, visit.getToken());
	//			details.put(MessageCodes.CRITERION_ITEM_TIP_TITLE, visit.getTitle());
	//		}
	//		return details;
	//	}

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof DepartmentVO) {
			return ((DepartmentVO) vo).getName();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof DepartmentVO) {
			return ((DepartmentVO) vo).getNameL10nKey();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getDepartment(id);
	}
}