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

	private static final String SECTION_KEY_PREFIX = "s:";
	private static final String INDEX_KEY_PREFIX = "i:";

	static String toConverterKey(ECRFSectionProgressVO value) {
		if (value == null) {
			return CommonUtil.NO_SELECTION_VALUE;
		}
		String section = value.getSection() != null ? value.getSection() : CommonUtil.NO_SELECTION_VALUE;
		Long selectionIndex = (value instanceof EcrfSectionProgressItemValue) ? ((EcrfSectionProgressItemValue) value).getSelectionIndex() : null;
		if (selectionIndex == null) {
			if (section.length() == 0) {
				return CommonUtil.NO_SELECTION_VALUE;
			}
			StringBuilder key = new StringBuilder(SECTION_KEY_PREFIX);
			key.append(escapeConverterKey(section));
			return key.toString();
		}
		StringBuilder key = new StringBuilder(INDEX_KEY_PREFIX);
		key.append(escapeConverterKey(section));
		key.append(':');
		key.append(selectionIndex);
		return key.toString();
	}

	private static String escapeConverterKey(String section) {
		return section.replace(":", "::");
	}

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		// http://stackoverflow.com/questions/9147119/avoid-extra-db-reads-in-the-getasobject-method-of-converter-class-by-caching-dat
		Iterator<SelectItem> it = new SelectItemsIterator(facesContext, component);
		while (it.hasNext()) {
			Object value = it.next().getValue();
			if (value instanceof ECRFSectionProgressVO) {
				if (submittedValue.equals(toConverterKey((ECRFSectionProgressVO) value))) {
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
		return null;
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
			return toConverterKey((ECRFSectionProgressVO) value);
		} else {
			return CommonUtil.NO_SELECTION_VALUE;
		}
	}
}
