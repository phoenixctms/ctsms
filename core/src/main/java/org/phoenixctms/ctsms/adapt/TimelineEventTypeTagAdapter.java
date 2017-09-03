package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.domain.TimelineEventType;
import org.phoenixctms.ctsms.domain.TimelineEventTypeDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.TimelineEventInVO;

public class TimelineEventTypeTagAdapter extends TagAdapter<Trial, TimelineEventType, TimelineEvent, TimelineEventInVO> {

	private TrialDao trialDao;
	private TimelineEventTypeDao timelineEventTypeDao;

	public TimelineEventTypeTagAdapter(TrialDao trialDao, TimelineEventTypeDao timelineEventTypeDao) {
		this.trialDao = trialDao;
		this.timelineEventTypeDao = timelineEventTypeDao;
	}

	@Override
	protected Trial checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Trial root, TimelineEventType tag, TimelineEventInVO tagValueIn)
			throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
		ServiceUtil.checkReminderPeriod(tagValueIn.getReminderPeriod(), tagValueIn.getReminderPeriodDays());
		if (tagValueIn.getStop() != null && DateCalc.getStartOfDay(tagValueIn.getStop()).compareTo(DateCalc.getStartOfDay(tagValueIn.getStart())) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.TIMELINE_EVENT_END_DATE_LESS_THAN_OR_EQUAL_TO_START_DATE);
		}
	}

	@Override
	protected TimelineEventType checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkTimelineEventTypeId(tagId, timelineEventTypeDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(TimelineEventInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<TimelineEventType> findTagsIncludingId(Trial root, Long tagId) {
		return timelineEventTypeDao.findByVisibleId(true, tagId);
	}

	@Override
	protected Integer getMaxOccurrence(TimelineEventType tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(TimelineEventType tag) {
		return null;
	}

	@Override
	protected String getNameL10nKey(TimelineEventType tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(TimelineEventType tag) {
		return null;
	}

	@Override
	protected Long getRootId(TimelineEventInVO tagValueIn) {
		return tagValueIn.getTrialId();
	}

	@Override
	protected TimelineEventType getTag(TimelineEvent tagValue) {
		return tagValue.getType();
	}

	@Override
	protected Long getTagId(TimelineEventInVO tagValueIn) {
		return tagValueIn.getTypeId();
	}

	@Override
	protected Collection<TimelineEvent> getTagValues(Trial root) {
		return root.getEvents();
	}

	@Override
	protected String getValue(TimelineEventInVO tagValueIn) {
		return null;
	}

	@Override
	protected boolean same(TimelineEvent tagValue, TimelineEventInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		timelineEventTypeDao.toTimelineEventTypeVOCollection(tags);
	}
}
