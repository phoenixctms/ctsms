package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.ReminderEntityAdapter;
import org.phoenixctms.ctsms.compare.TimelineEventComparator;
import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.domain.TimelineEventDao;
import org.phoenixctms.ctsms.domain.TimelineEventDaoImpl;
import org.phoenixctms.ctsms.domain.TimelineEventType;
import org.phoenixctms.ctsms.domain.TimelineEventTypeDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;

public class TimelineEventReflexionGraph extends ReflexionCycleHelper<TimelineEvent, TimelineEventOutVO> {

	private static final boolean LIMIT_INSTANCES = true;
	private static final boolean LIMIT_PARENTS_DEPTH = true;
	private static final boolean LIMIT_CHILDREN_DEPTH = true;
	private TimelineEventDaoImpl timelineEventDaoImpl;
	private TimelineEventDao timelineEventDao;
	private TimelineEventTypeDao timelineEventTypeDao;
	private TrialDao trialDao;
	private UserDao userDao;
	private int maxInstances;
	private int parentDepth;
	private int childrenDepth;
	private final static int DEFAULT_MAX_INSTANCES = 1;
	private final static int DEFAULT_PARENT_DEPTH = Integer.MAX_VALUE >> 1;
	private final static int DEFAULT_CHILDREN_DEPTH = Integer.MAX_VALUE >> 1;

	public TimelineEventReflexionGraph(TimelineEventDao timelineEventDao) {
		this.timelineEventDao = timelineEventDao;
	}

	public TimelineEventReflexionGraph(TimelineEventDaoImpl timelineEventDaoImpl,
			TimelineEventTypeDao timelineEventTypeDao,
			TrialDao trialDao, UserDao userDao, Integer... maxInstances) {
		this.maxInstances = maxInstances != null && maxInstances.length > 0 ? (maxInstances[0] == null ? DEFAULT_MAX_INSTANCES : maxInstances[0]) : DEFAULT_MAX_INSTANCES;
		this.parentDepth = maxInstances != null && maxInstances.length > 1 ? (maxInstances[1] == null ? DEFAULT_PARENT_DEPTH : maxInstances[1]) : DEFAULT_PARENT_DEPTH;
		this.childrenDepth = maxInstances != null && maxInstances.length > 2 ? (maxInstances[2] == null ? DEFAULT_CHILDREN_DEPTH : maxInstances[2]) : DEFAULT_CHILDREN_DEPTH;
		this.timelineEventDaoImpl = timelineEventDaoImpl;
		this.timelineEventTypeDao = timelineEventTypeDao;
		this.trialDao = trialDao;
		this.userDao = userDao;
	}

	@Override
	protected TimelineEvent aquireWriteLock(Long id) throws ServiceException {
		return timelineEventDao.load(id, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected Class getAVOClass() {
		return TimelineEventOutVO.class;
	}

	@Override
	protected TimelineEvent getChild(TimelineEvent source) {
		return null;
	}

	@Override
	protected Collection<TimelineEvent> getEntityChildren(TimelineEvent source) {
		Collection<TimelineEvent> children = source.getChildren();
		if (children.size() > 1) {
			TreeSet<TimelineEvent> result = new TreeSet<TimelineEvent>(new TimelineEventComparator(true));
			result.addAll(children);
			return result;
		} else {
			return children;
		}
	}

	@Override
	protected Long getEntityId(TimelineEvent source) {
		return source.getId();
	}

	@Override
	protected Collection<TimelineEvent> getEntityParents(TimelineEvent source) {
		return null;
	}

	@Override
	protected ReflexionDepth getInitialReflexionDepth() {
		return new ReflexionDepth(parentDepth, childrenDepth);
	}

	@Override
	protected int getMaxInstances() {
		return maxInstances;
	}

	@Override
	protected TimelineEvent getParent(TimelineEvent source) {
		return source.getParent();
	}

	@Override
	protected Collection<TimelineEventOutVO> getVOChildren(TimelineEventOutVO target) {
		return target.getChildren();
	}

	@Override
	protected Collection<TimelineEventOutVO> getVOParents(TimelineEventOutVO target) {
		return null;
	}

	@Override
	protected boolean limitChildrenDepth() {
		return LIMIT_CHILDREN_DEPTH;
	}

	@Override
	protected boolean limitInstances() {
		return LIMIT_INSTANCES;
	}

	@Override
	protected boolean limitParentsDepth() {
		return LIMIT_PARENTS_DEPTH;
	}

	@Override
	protected TimelineEventOutVO newVO() {
		return new TimelineEventOutVO();
	}

	@Override
	protected void setChild(TimelineEventOutVO target, TimelineEventOutVO childVO) {
	}

	@Override
	protected void setParent(TimelineEventOutVO target, TimelineEventOutVO parentVO) {
		target.setParent(parentVO);
	}

	@Override
	protected void throwGraphLoopException(List<TimelineEvent> path) throws ServiceException {
		Iterator<TimelineEvent> it = path.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			appendLoopPath(sb,L10nUtil.getMessage(MessageCodes.LOOP_PATH_TIMELINE_EVENT_LABEL,DefaultMessages.LOOP_PATH_TIMELINE_EVENT_LABEL, it.next().getTitle()));
		}
		throw L10nUtil.initServiceException(ServiceExceptionCodes.TIMELINE_EVENT_GRAPH_LOOP, sb.toString());
	}

	@Override
	protected void toVORemainingFields(TimelineEvent source, TimelineEventOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		timelineEventDaoImpl.toTimelineEventOutVOBase(source, target);
		TimelineEventType type = source.getType();
		Trial trial = source.getTrial();
		User modifiedUser = source.getModifiedUser();
		if (type != null) {
			target.setType(timelineEventTypeDao.toTimelineEventTypeVO(type));
		}
		if (trial != null) {
			target.setTrial(trialDao.toTrialOutVO(trial));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		target.setReminderPeriod(L10nUtil.createVariablePeriodVO(Locales.USER, source.getReminderPeriod()));
		target.setImportance(L10nUtil.createEventImportanceVO(Locales.USER, source.getImportance()));
		ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(source);
		Date today = new Date();
		if (target.getReminderStart() == null) {
			target.setReminderStart(reminderItem.getReminderStart(today, false, null, null));
		}
		target.setChildrenCount(timelineEventDaoImpl.getChildrenCount(source.getId()));
	}
}