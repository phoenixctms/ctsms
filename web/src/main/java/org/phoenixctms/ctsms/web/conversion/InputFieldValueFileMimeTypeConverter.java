package org.phoenixctms.ctsms.web.conversion;

import javax.faces.convert.FacesConverter;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

@FacesConverter(value = InputFieldValueFileMimeTypeConverter.CONVERTER_ID)
public class InputFieldValueFileMimeTypeConverter extends IDVOConverter {

	public static final String CONVERTER_ID = "ctsms.InputFieldValueFileMimeType";

	@Override
	public String getLabel(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof MimeTypeVO) {
			return ((MimeTypeVO) vo).getMimeType();
		}
		return "";
	}

	@Override
	public String getName(IDVO idvo) {
		Object vo = idvo.getVo();
		if (vo instanceof MimeTypeVO) {
			return ((MimeTypeVO) vo).getFileNameExtensions();
		}
		return "";
	}

	@Override
	public Object getVo(Long id) {
		return WebUtil.getMimeType(FileModule.INPUT_FIELD_VALUE_FILE, id);
	}
}
