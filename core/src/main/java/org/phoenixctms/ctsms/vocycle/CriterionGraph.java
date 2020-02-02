package org.phoenixctms.ctsms.vocycle;

import java.util.HashMap;

import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.CriteriaDao;
import org.phoenixctms.ctsms.domain.Criterion;
import org.phoenixctms.ctsms.domain.CriterionDaoImpl;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestriction;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.domain.CriterionTie;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionOutVO;

public class CriterionGraph extends GraphCycle1Helper<Criterion, CriterionOutVO, Criteria, CriteriaOutVO> {

	private static final boolean LIMIT_A_INSTANCES = false;
	private static final int MAX_A_INSTANCES = 0;
	private CriterionDaoImpl criterionDaoImpl;
	private CriteriaDao criteriaDao;
	private CriterionPropertyDao criterionPropertyDao;
	private CriterionRestrictionDao criterionRestrictionDao;
	private CriterionTieDao criterionTieDao;

	public CriterionGraph(CriterionDaoImpl criterionDaoImpl,
			CriteriaDao criteriaDao,
			CriterionPropertyDao criterionPropertyDao,
			CriterionRestrictionDao criterionRestrictionDao,
			CriterionTieDao criterionTieDao) {
		this.criterionDaoImpl = criterionDaoImpl;
		this.criteriaDao = criteriaDao;
		this.criterionPropertyDao = criterionPropertyDao;
		this.criterionRestrictionDao = criterionRestrictionDao;
		this.criterionTieDao = criterionTieDao;
	}

	@Override
	protected Long getAId(Criterion source) {
		return source.getId();
	}

	@Override
	protected Class getAVOClass() {
		return CriterionOutVO.class;
	}

	@Override
	protected Long getBId(Criteria source) {
		return source.getId();
	}

	@Override
	protected Criteria getBOfA(Criterion source) {
		return source.getCriteria();
	}

	@Override
	protected Class getBVOClass() {
		return CriteriaOutVO.class;
	}

	@Override
	protected int getMaxAInstances() {
		return MAX_A_INSTANCES;
	}

	@Override
	protected boolean limitAInstances() {
		return LIMIT_A_INSTANCES;
	}

	@Override
	protected CriteriaOutVO newBVO() {
		return new CriteriaOutVO();
	}

	@Override
	protected void setBVOOfAVO(CriterionOutVO target, CriteriaOutVO bVO) {
		target.setCriteria(bVO);
	}

	@Override
	protected void toBVO(Criteria source, CriteriaOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		criteriaDao.toCriteriaOutVO(source, target, voMap);
	}

	@Override
	protected void toVORemainingFields(Criterion source, CriterionOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		criterionDaoImpl.toCriterionOutVOBase(source, target);
		CriterionProperty property = criterionPropertyDao.findByNameL10nKey(source.getPropertyNameL10nKey());
		CriterionRestriction restriction = criterionRestrictionDao.findByRestriction(source.getRestriction());
		CriterionTie tie = criterionTieDao.findByTie(source.getTie());
		if (property != null) {
			target.setProperty(criterionPropertyDao.toCriterionPropertyVO(property));
		}
		if (restriction != null) {
			target.setRestriction(criterionRestrictionDao.toCriterionRestrictionVO(restriction));
		}
		if (tie != null) {
			target.setTie(criterionTieDao.toCriterionTieVO(tie));
		}
	}
}
