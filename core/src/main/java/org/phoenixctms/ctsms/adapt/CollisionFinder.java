package org.phoenixctms.ctsms.adapt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.phoenixctms.ctsms.exception.ServiceException;

public abstract class CollisionFinder<IN, ENTITY, ROOT> {

	protected abstract ROOT aquireWriteLock(IN in) throws ServiceException;

	public boolean collides(IN in) throws ServiceException {
		return collides(in, true);
	}

	public boolean collides(IN in, boolean lock) throws ServiceException {
		if (in != null) {
			ROOT root = null;
			if (lock) {
				root = aquireWriteLock(in);
			}
			ArrayList<ENTITY> collidingItems = getAllCollidingItems(in, root);
			if (collidingItems.size() > 0) {
				boolean isNew = isNew(in);
				Iterator<ENTITY> it = collidingItems.iterator();
				while (it.hasNext()) {
					ENTITY collidingItem = it.next();
					if (isNew || !equals(in, collidingItem)) {
						if (match(in, collidingItem, root)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected abstract boolean equals(IN in, ENTITY existing);

	protected ArrayList<ENTITY> getAllCollidingItems(IN in, ROOT root) throws ServiceException {
		ArrayList<ENTITY> allCollidingItems = new ArrayList<ENTITY>();
		Collection<ENTITY> collidingItems = getCollidingItems(in, root);
		if (collidingItems != null && collidingItems.size() > 0) {
			allCollidingItems.addAll(collidingItems);
		}
		ENTITY collidingItem = getCollidingItem(in, root);
		if (collidingItem != null) {
			allCollidingItems.add(collidingItem);
		}
		return allCollidingItems;
	}

	protected ENTITY getCollidingItem(IN in, ROOT root) throws ServiceException {
		return null;
	}

	protected Collection<ENTITY> getCollidingItems(IN in, ROOT root) throws ServiceException {
		return null;
	}

	protected abstract boolean isNew(IN in);

	protected boolean match(IN in, ENTITY existing, ROOT root) throws ServiceException {
		return true;
	}
}
