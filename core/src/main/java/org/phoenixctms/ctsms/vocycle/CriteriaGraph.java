package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.HashMap;

import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.CriteriaDaoImpl;
import org.phoenixctms.ctsms.domain.Criterion;
import org.phoenixctms.ctsms.domain.CriterionDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionOutVO;

public class CriteriaGraph extends GraphCycleNHelper<Criteria, CriteriaOutVO, Criterion, CriterionOutVO> {

	private static final boolean LIMIT_A_INSTANCES = true;
	private static final int MAX_A_INSTANCES = 1;
	private CriteriaDaoImpl criteriaDaoImpl;
	private CriterionDao criterionDao;
	private UserDao userDao;

	public CriteriaGraph(CriteriaDaoImpl criteriaDaoImpl, CriterionDao criterionDao,
			UserDao userDao) {
		this.criteriaDaoImpl = criteriaDaoImpl;
		this.criterionDao = criterionDao;
		this.userDao = userDao;
	}

	@Override
	protected Long getAId(Criteria source) {
		return source.getId();
	}

	@Override
	protected Criteria getAOfB(Criterion source) {
		return source.getCriteria();
	}

	@Override
	protected Class getAVOClass() {
		return CriteriaOutVO.class;
	}

	@Override
	protected Long getBId(Criterion source) {
		return source.getId();
	}

	@Override
	protected Collection<Criterion> getBsOfA(Criteria source) {
		return source.getCriterions();
	}

	@Override
	protected Class getBVOClass() {
		return CriterionOutVO.class;
	}

	@Override
	protected Collection<CriterionOutVO> getBVOsOfAVO(CriteriaOutVO target) {
		return target.getCriterions();
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
	protected CriterionOutVO newBVO() {
		return new CriterionOutVO();
	}

	@Override
	protected void setAVOOfBVO(CriterionOutVO bVO, CriteriaOutVO aVO) {
		bVO.setCriteria(aVO);
	}

	@Override
	protected void toBVO(Criterion source, CriterionOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		criterionDao.toCriterionOutVO(source, target, voMap);
	}

	@Override
	protected void toVORemainingFields(Criteria source, CriteriaOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) { // , HashMap<Class,HashMap<Long,Object>> deferredVoMap)
																																// {
		criteriaDaoImpl.toCriteriaOutVOBase(source, target);
		User modifiedUser = source.getModifiedUser();
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
	}
}
