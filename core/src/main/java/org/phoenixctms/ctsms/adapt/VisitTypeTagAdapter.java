package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.Visit;
import org.phoenixctms.ctsms.domain.VisitType;
import org.phoenixctms.ctsms.domain.VisitTypeDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.VisitInVO;

public class VisitTypeTagAdapter extends TagAdapter<Trial, VisitType, Visit, VisitInVO> {

	private TrialDao trialDao;
	private VisitTypeDao visitTypeDao;

	public VisitTypeTagAdapter(TrialDao trialDao, VisitTypeDao visitTypeDao) {
		this.trialDao = trialDao;
		this.visitTypeDao = visitTypeDao;
	}

	@Override
	protected Trial checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Trial root, VisitType tag, VisitInVO tagValueIn)
			throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
	}

	@Override
	protected VisitType checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkVisitTypeId(tagId, visitTypeDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(VisitInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<VisitType> findTagsIncludingId(Trial root, Long tagId) {
		return visitTypeDao.findByVisibleId(true, tagId);
	}

	@Override
	protected Integer getMaxOccurrence(VisitType tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(VisitType tag) {
		return null;
	}

	@Override
	protected String getNameL10nKey(VisitType tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(VisitType tag) {
		return null;
	}

	@Override
	protected Long getRootId(VisitInVO tagValueIn) {
		return tagValueIn.getTrialId();
	}

	@Override
	protected VisitType getTag(Visit tagValue) {
		return tagValue.getType();
	}

	@Override
	protected Long getTagId(VisitInVO tagValueIn) {
		return tagValueIn.getTypeId();
	}

	@Override
	protected Collection<Visit> getTagValues(Trial root) {
		return root.getVisits();
	}

	@Override
	protected String getValue(VisitInVO tagValueIn) {
		return null;
	}

	@Override
	protected boolean same(Visit tagValue, VisitInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		visitTypeDao.toVisitTypeVOCollection(tags);
	}
}
