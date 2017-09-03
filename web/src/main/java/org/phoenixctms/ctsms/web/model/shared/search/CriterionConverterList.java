package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.convert.Converter;

import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.conversion.IDVOConverter;
import org.phoenixctms.ctsms.web.model.IDVO;

public class CriterionConverterList extends CriterionListBase<Converter> {

	private final static IDVOConverter DUMMY_ID_CONVERTER = new IDVOConverter() {

		@Override
		public String getLabel(IDVO idvo) {
			return "";
		}

		@Override
		public Object getVo(Long id) {
			return null;
		}
	};
	private HashMap<Long, Converter> converterMap;

	public CriterionConverterList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, Converter> converterMap) {
		super(criterionsIn, propertyVOsMap);
		this.converterMap = (converterMap == null ? new HashMap<Long, Converter>() : converterMap);
	}

	@Override
	public Converter get(int index) {
		CriterionPropertyVO propertyVO = getPropertyVO(index);
		if (propertyVO != null && converterMap.containsKey(propertyVO.getId())) {
			return converterMap.get(propertyVO.getId());
		} else {
			return DUMMY_ID_CONVERTER;
		}
	}
}
