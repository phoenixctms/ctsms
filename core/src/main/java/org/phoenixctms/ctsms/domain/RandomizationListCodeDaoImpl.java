// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.compare.VOIDComparator;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.query.SubCriteriaMap;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeInVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeOutVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeValueVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListOutVO;
import org.phoenixctms.ctsms.vo.TrialRandomizationListVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

/**
 * @see RandomizationListCode
 */
public class RandomizationListCodeDaoImpl
		extends RandomizationListCodeDaoBase {

	private final static VOIDComparator RANDOMIZATION_LIST_CODE_VALUE_VO_ID_COMPARATOR = new VOIDComparator<RandomizationListCodeValueVO>(false);

	/**
	 * {@inheritDoc}
	 */
	public void toRandomizationListCodeOutVO(
			RandomizationListCode source,
			RandomizationListCodeOutVO target) {
		super.toRandomizationListCodeOutVO(source, target);
		User modifiedUser = source.getModifiedUser();
		User breakUser = source.getBreakUser();
		Trial trial = source.getTrial();
		StratificationRandomizationList stratificationRandomizationList = source.getRandomizationList();
		ProbandListEntry listEntry = this.getProbandListEntryDao().getByRandomizationListCode(source);
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
		if (breakUser != null) {
			target.setBreakUser(this.getUserDao().toUserOutVO(breakUser));
		}
		if (trial != null) {
			target.setTrialRandomizationList(this.getTrialDao().toTrialRandomizationListVO(trial));
		}
		if (stratificationRandomizationList != null) {
			target.setStratificationRandomizationList(this.getStratificationRandomizationListDao().toStratificationRandomizationListOutVO(stratificationRandomizationList));
		}
		if (listEntry != null) {
			target.setListEntry(this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry));
		}
		target.setValues(toRandomizationListCodeValueVOCollection(source.getValues()));
	}

	private ArrayList<RandomizationListCodeValueVO> toRandomizationListCodeValueVOCollection(Collection<RandomizationListCodeValue> values) { // lazyload
		RandomizationListCodeValueDao randomizationListCodeValueDao = this.getRandomizationListCodeValueDao();
		ArrayList<RandomizationListCodeValueVO> result = new ArrayList<RandomizationListCodeValueVO>(values.size());
		Iterator<RandomizationListCodeValue> it = values.iterator();
		while (it.hasNext()) {
			result.add(randomizationListCodeValueDao.toRandomizationListCodeValueVO(it.next()));
		}
		Collections.sort(result, RANDOMIZATION_LIST_CODE_VALUE_VO_ID_COMPARATOR);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public RandomizationListCodeOutVO toRandomizationListCodeOutVO(final RandomizationListCode entity) {
		return super.toRandomizationListCodeOutVO(entity);
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private RandomizationListCode loadRandomizationListCodeFromRandomizationListCodeOutVO(RandomizationListCodeOutVO randomizationListCodeOutVO) {
		RandomizationListCode randomizationListCode = this.get(randomizationListCodeOutVO.getId());
		if (randomizationListCode == null) {
			randomizationListCode = RandomizationListCode.Factory.newInstance();
		}
		return randomizationListCode;
	}

	/**
	 * {@inheritDoc}
	 */
	public RandomizationListCode randomizationListCodeOutVOToEntity(RandomizationListCodeOutVO randomizationListCodeOutVO) {
		RandomizationListCode entity = this.loadRandomizationListCodeFromRandomizationListCodeOutVO(randomizationListCodeOutVO);
		this.randomizationListCodeOutVOToEntity(randomizationListCodeOutVO, entity, true);
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void randomizationListCodeOutVOToEntity(
			RandomizationListCodeOutVO source,
			RandomizationListCode target,
			boolean copyIfNull) {
		super.randomizationListCodeOutVOToEntity(source, target, copyIfNull);
		StratificationRandomizationListOutVO stratificationRandomizationListVO = source.getStratificationRandomizationList();
		TrialRandomizationListVO trialRandomizationListVO = source.getTrialRandomizationList();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		UserOutVO breakUserVO = source.getBreakUser();
		if (stratificationRandomizationListVO != null) {
			StratificationRandomizationList randomizationList = this.getStratificationRandomizationListDao()
					.stratificationRandomizationListOutVOToEntity(stratificationRandomizationListVO);
			target.setRandomizationList(randomizationList);
			randomizationList.addRandomizationCodes(target);
		} else if (copyIfNull) {
			StratificationRandomizationList randomizationList = target.getRandomizationList();
			target.setRandomizationList(null);
			if (randomizationList != null) {
				randomizationList.removeRandomizationCodes(target);
			}
		}
		if (trialRandomizationListVO != null) {
			Trial trial = this.getTrialDao().trialRandomizationListVOToEntity(trialRandomizationListVO);
			target.setTrial(trial);
			trial.addRandomizationCodes(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeRandomizationCodes(target);
			}
		}
		Collection values = source.getValues();
		if (values.size() > 0) {
			values = new ArrayList(values); //prevent changing VO
			this.getRandomizationListCodeValueDao().randomizationListCodeValueVOToEntityCollection(values);
			target.setValues(values); // hashset-exception!!!
		} else if (copyIfNull) {
			target.getValues().clear();
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
		if (breakUserVO != null) {
			target.setBreakUser(this.getUserDao().userOutVOToEntity(breakUserVO));
		} else if (copyIfNull) {
			target.setBreakUser(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void toRandomizationListCodeInVO(
			RandomizationListCode source,
			RandomizationListCodeInVO target) {
		super.toRandomizationListCodeInVO(source, target);
		target.setValues(toRandomizationListCodeValueVOCollection(source.getValues()));
	}

	/**
	 * {@inheritDoc}
	 */
	public RandomizationListCodeInVO toRandomizationListCodeInVO(final RandomizationListCode entity) {
		return super.toRandomizationListCodeInVO(entity);
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private RandomizationListCode loadRandomizationListCodeFromRandomizationListCodeInVO(RandomizationListCodeInVO randomizationListCodeInVO) {
		return RandomizationListCode.Factory.newInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	public RandomizationListCode randomizationListCodeInVOToEntity(RandomizationListCodeInVO randomizationListCodeInVO) {
		RandomizationListCode entity = this.loadRandomizationListCodeFromRandomizationListCodeInVO(randomizationListCodeInVO);
		this.randomizationListCodeInVOToEntity(randomizationListCodeInVO, entity, true);
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void randomizationListCodeInVOToEntity(
			RandomizationListCodeInVO source,
			RandomizationListCode target,
			boolean copyIfNull) {
		super.randomizationListCodeInVOToEntity(source, target, copyIfNull);
		Collection values = source.getValues();
		if (values.size() > 0) {
			values = new ArrayList(values); //prevent changing VO
			this.getRandomizationListCodeValueDao().randomizationListCodeValueVOToEntityCollection(values);
			target.setValues(values); // hashset-exception!!!
		} else if (copyIfNull) {
			target.getValues().clear();
		}
	}

	private org.hibernate.Criteria createRandomizationListCodeCriteria(String alias) {
		org.hibernate.Criteria randomizationListCodeCriteria;
		if (alias != null && alias.length() > 0) {
			randomizationListCodeCriteria = this.getSession().createCriteria(RandomizationListCode.class, alias);
		} else {
			randomizationListCodeCriteria = this.getSession().createCriteria(RandomizationListCode.class);
		}
		return randomizationListCodeCriteria;
	}

	@Override
	protected Collection<RandomizationListCode> handleFindByTrialBroken(Long trialId, Boolean broken, PSFVO psf) throws Exception {
		org.hibernate.Criteria randomizationListCodeCriteria = createRandomizationListCodeCriteria("randomizationListCode0");
		SubCriteriaMap criteriaMap = new SubCriteriaMap(RandomizationListCode.class, randomizationListCodeCriteria);
		if (trialId != null) {
			org.hibernate.Criteria randomizationListCriteria = criteriaMap.createCriteria("randomizationList", CriteriaSpecification.LEFT_JOIN);
			randomizationListCriteria.add(Restrictions.or(
					Restrictions.eq("trial.id", trialId.longValue()),
					Restrictions.eq("randomizationListCode0.trial.id", trialId.longValue())));
		}
		if (broken != null) {
			randomizationListCodeCriteria.add(Restrictions.eq("broken", broken.booleanValue()));
		}
		return CriteriaUtil.listDistinctRootPSFVO(criteriaMap, psf, this); // support filter by selection set value
	}

	@Override
	protected Collection<RandomizationListCode> handleFindByTrialRandomizationListCode(Long trialId, Long randomizationListId, String code) throws Exception {
		org.hibernate.Criteria randomizationListCodeCriteria = createRandomizationListCodeCriteria(null);
		if (trialId != null) {
			randomizationListCodeCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (randomizationListId != null) {
			randomizationListCodeCriteria.add(Restrictions.eq("randomizationList.id", randomizationListId.longValue()));
		}
		randomizationListCodeCriteria.add(Restrictions.eq("code", code));
		return randomizationListCodeCriteria.list();
	}

	@Override
	protected long handleGetCount(Long trialId, Long randomizationListId, Boolean broken) throws Exception {
		org.hibernate.Criteria randomizationListCodeCriteria = createRandomizationListCodeCriteria(null);
		if (trialId != null) {
			randomizationListCodeCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (randomizationListId != null) {
			randomizationListCodeCriteria.add(Restrictions.eq("randomizationList.id", randomizationListId.longValue()));
		}
		if (broken != null) {
			randomizationListCodeCriteria.add(Restrictions.eq("broken", broken.booleanValue()));
		}
		return (Long) randomizationListCodeCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}
}