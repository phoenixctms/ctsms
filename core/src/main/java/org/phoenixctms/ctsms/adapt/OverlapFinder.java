package org.phoenixctms.ctsms.adapt;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.phoenixctms.ctsms.exception.ServiceException;

public abstract class OverlapFinder<IN, ENTITY, ROOT> extends CollisionFinder<IN, ENTITY, ROOT> {

	@Override
	protected ROOT aquireWriteLock(IN in) throws ServiceException {
		return aquireWriteLock(in, true);
	}

	protected abstract ROOT aquireWriteLock(IN in, boolean lock) throws ServiceException;

	@Override
	public boolean collides(IN in, boolean lock) throws ServiceException {
		// http://stackoverflow.com/questions/2244964/finding-number-of-overlaps-in-a-list-of-time-ranges
		if (in != null) {
			ROOT root = aquireWriteLock(in, lock);
			long maxOverlappingEvents = getMaxOverlappingEvents(root);
			ArrayList<ENTITY> collidingItems = getAllCollidingItems(in, null);
			TreeMap<Date, Long> map = new TreeMap<Date, Long>();
			if (collidingItems != null) {
				Iterator<ENTITY> it = collidingItems.iterator();
				while (it.hasNext()) {
					ENTITY collidingItem = it.next();
					if (match(in, collidingItem, root) && (isNew(in) || !equals(in, collidingItem))) {
						put(map, getStart(collidingItem), true);
						put(map, getStop(collidingItem), false);
					}
				}
			}
			put(map, getInStart(in), true);
			put(map, getInStop(in), false);
			long overlappingEventsCount = 0;
			long maxOverlappingEventsCount = 0;
			Iterator<Entry<Date, Long>> mapIt = map.entrySet().iterator();
			while (mapIt.hasNext()) {
				overlappingEventsCount += mapIt.next().getValue();
				if (overlappingEventsCount > maxOverlappingEventsCount) {
					maxOverlappingEventsCount = overlappingEventsCount;
				}
			}
			return maxOverlappingEventsCount > maxOverlappingEvents;
		}
		return false;
	}

	protected abstract Date getInStart(IN in);

	protected abstract Date getInStop(IN in);

	protected abstract long getMaxOverlappingEvents(ROOT root) throws ServiceException;

	protected abstract Date getStart(ENTITY collidingItem);

	protected abstract Date getStop(ENTITY collidingItem);

	private void put(TreeMap<Date, Long> map, Date date, boolean start) {
		Long count;
		if (map.containsKey(date)) {
			count = map.get(date);
		} else {
			count = 0l;
		}
		map.put(date, count + (start ? 1l : -1l));
	}
}
