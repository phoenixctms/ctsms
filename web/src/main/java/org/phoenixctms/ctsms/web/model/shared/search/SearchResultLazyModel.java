package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;

public abstract class SearchResultLazyModel extends LazyDataModelBase {

	protected CriteriaInVO criteriaIn;
	protected HashSet<CriterionInVO> criterionsIn;
	protected boolean noCriteriaSet;

	protected SearchResultLazyModel() {
		super();
		criteriaIn = new CriteriaInVO();
		criterionsIn = new HashSet<CriterionInVO>();
		noCriteriaSet = true;
	}

	@Override
	protected final Collection getLazyResult(PSFVO psf) {
		if (psf != null) {
			psf.setUpdateRowCount(false);
		}
		return getLazyResultWCount(psf);
	}

	protected abstract Collection getLazyResultWCount(PSFVO psf);

	public void setCriteriaIn(CriteriaInVO criteria) {
		if (criteria != null) {
			criteriaIn.copy(criteria);
			noCriteriaSet = false;
		} else {
			noCriteriaSet = true;
		}
	}

	public void setCriterionsIn(ArrayList<CriterionInVO> criterions) {
		this.criterionsIn.clear();
		if (criterions != null) {
			Iterator<CriterionInVO> it = criterions.iterator();
			while (it.hasNext()) {
				CriterionInVO criterion = new CriterionInVO();
				criterion.copy(it.next());
				criterionsIn.add(criterion);
			}
		}
	}

	public void setCriterionsIn(HashSet<CriterionInVO> criterions) {
		if (criterions != null) {
			this.criterionsIn = criterions;
		} else {
			this.criterionsIn.clear();
		}
	}

	@Override
	public void updateRowCount() {
		PSFVO psf = new PSFVO();
		psf.setUpdateRowCount(true);
		psf.setPageSize(0);
		getLazyResultWCount(psf);
		updateRowCount(psf);
	}
}
