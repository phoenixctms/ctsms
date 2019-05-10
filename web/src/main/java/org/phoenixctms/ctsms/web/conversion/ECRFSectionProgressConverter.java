package org.phoenixctms.ctsms.web.conversion;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFSectionProgressVO;

@FacesConverter(value = ECRFSectionProgressConverter.CONVERTER_ID)
public class ECRFSectionProgressConverter implements Converter {

	public static final String CONVERTER_ID = "ctsms.ECRFSectionProgress";

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		// if (submittedValue.trim().equals(CommonUtil.NO_SELECTION_VALUE)) {
		// return null;
		// } else {
		// http://stackoverflow.com/questions/9147119/avoid-extra-db-reads-in-the-getasobject-method-of-converter-class-by-caching-dat
		Iterator<SelectItem> it = new SelectItemsIterator(facesContext, component);
		while (it.hasNext()) {
			Object value = it.next().getValue();
			if (value instanceof ECRFSectionProgressVO) {
				if (submittedValue.equals(((ECRFSectionProgressVO) value).getSection())) {
					return value;
				}
			} else if (value instanceof String) {
				if (submittedValue.equals(value)) {
					ECRFSectionProgressVO progress = new EcrfSectionProgressItemValue();
					progress.setSection((String) value);
					return progress;
				}
			}
		}
		// System.out.println("section progress for " + submittedValue + " not found");
		return null;
		// }
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return CommonUtil.NO_SELECTION_VALUE;
		} else if (value instanceof String) {
			if (CommonUtil.NO_SELECTION_VALUE.equals(value)) {
				return CommonUtil.NO_SELECTION_VALUE;
			} else {
				return (String) value;
			}
		} else if (value instanceof ECRFSectionProgressVO) {
			return ((ECRFSectionProgressVO) value).getSection();
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}
}
