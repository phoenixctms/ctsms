package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.convert.Converter;

import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.conversion.IDVOConverter;
import org.phoenixctms.ctsms.web.model.IDVO;

public class CriterionIDVOList extends CriterionListBase<IDVO> {

	private HashMap<Long, Converter> converterMap;

	public CriterionIDVOList(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap, HashMap<Long, Converter> converterMap) {
		super(criterionsIn, propertyVOsMap);
		this.converterMap = (converterMap == null ? new HashMap<Long, Converter>() : converterMap);
	}

	@Override
	public IDVO get(int index) {
		CriterionInVO criterionIn = this.getCriterionIn(index);
		CriterionPropertyVO propertyVO = this.getPropertyVO(criterionIn);
		return getIDVO(criterionIn, propertyVO);
	}

	private IDVO getIDVO(CriterionInVO criterionIn, CriterionPropertyVO propertyVO) {
		if (criterionIn != null && propertyVO != null && criterionIn.getLongValue() != null) {
			IDVOConverter converter = null;
			try {
				converter = (IDVOConverter) converterMap.get(propertyVO.getId());
			} catch (Exception e) {
			}
			if (converter != null) {
				Object vo = converter.getVo(criterionIn.getLongValue());
				if (vo != null) {
					return IDVO.transformVo(vo);
				}
			}
		}
		return null;
	}

	@Override
	public IDVO set(int index, IDVO value) {
		CriterionInVO criterionIn = this.getCriterionIn(index);
		CriterionPropertyVO propertyVO = this.getPropertyVO(criterionIn);
		if (criterionIn != null && propertyVO != null) {
			IDVO old = getIDVO(criterionIn, propertyVO);
			if (value != null) {
				criterionIn.setLongValue(value.getId());
			}
			return old;
		}
		return null;
	}
}
