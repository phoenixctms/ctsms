package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.TrialTag;
import org.phoenixctms.ctsms.domain.TrialTagDao;
import org.phoenixctms.ctsms.domain.TrialTagValue;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.TrialTagValueInVO;

public class TrialTagAdapter extends TagAdapter<Trial, TrialTag, TrialTagValue, TrialTagValueInVO> {

	private TrialDao trialDao;
	private TrialTagDao trialTagDao;

	public TrialTagAdapter(TrialDao trialDao, TrialTagDao trialTagDao) {
		this.trialDao = trialDao;
		this.trialTagDao = trialTagDao;
	}

	@Override
	protected Trial checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Trial root, TrialTag tag,
			TrialTagValueInVO tagValueIn) throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
	}

	@Override
	protected TrialTag checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkTrialTagId(tagId, trialTagDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(TrialTagValueInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<TrialTag> findTagsIncludingId(Trial root, Long tagId) {
		return trialTagDao.findByVisibleIdExcelPayoffs(true, tagId, null, null);
	}

	@Override
	protected Integer getMaxOccurrence(TrialTag tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(TrialTag tag) {
		return tag.getMismatchMsgL10nKey();
	}

	@Override
	protected String getNameL10nKey(TrialTag tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(TrialTag tag) {
		return tag.getRegExp();
	}

	@Override
	protected Long getRootId(TrialTagValueInVO tagValueIn) {
		return tagValueIn.getTrialId();
	}

	@Override
	protected TrialTag getTag(TrialTagValue tagValue) {
		return tagValue.getTag();
	}

	@Override
	protected Long getTagId(TrialTagValueInVO tagValueIn) {
		return tagValueIn.getTagId();
	}

	@Override
	protected Collection<TrialTagValue> getTagValues(Trial root) {
		return root.getTagValues();
	}

	@Override
	protected String getValue(TrialTagValueInVO tagValueIn) {
		return tagValueIn.getValue();
	}

	@Override
	protected boolean same(TrialTagValue tagValue, TrialTagValueInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		trialTagDao.toTrialTagVOCollection(tags);
	}
}
