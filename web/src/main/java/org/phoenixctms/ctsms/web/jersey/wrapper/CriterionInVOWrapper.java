package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.util.ArrayList;
import java.util.Collection;

import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;

public class CriterionInVOWrapper extends CriteriaInVO {

	protected Collection<CriterionInVO> criterions;

	public Collection<CriterionInVO> getCriterions() {
		if (this.criterions == null) {
			this.criterions = new ArrayList<CriterionInVO>();
		}
		return this.criterions;
	}

	public void setCriterions(Collection<CriterionInVO> value) {
		this.criterions = value;
	}
}