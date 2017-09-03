package org.phoenixctms.ctsms.adapt;

import org.phoenixctms.ctsms.exception.ServiceException;

public abstract class MaxCategoriesAdapter<ROOT, VO> {

	protected MaxCategoriesAdapter() {
	}

	protected abstract ROOT aquireWriteLock(VO in) throws ServiceException;

	public final boolean checkCategoryInput(VO in) throws ServiceException {
		boolean result = true;
		ServiceException exception;
		if (in != null) {
			ROOT root = aquireWriteLock(in);
			Long allowedCount = getAllowedCount();
			if (allowedCount != null && allowedCount > 0) {
				long existingCount = getExistingCount(in);
				if (existingCount > allowedCount) {
					result = false;
					exception = getException(root, allowedCount);
					if (exception != null) {
						throw exception;
					}
				} else if (existingCount == allowedCount.longValue()) {
					if (!containsNew(in)) {
						result = false;
						exception = getException(root, allowedCount);
						if (exception != null) {
							throw exception;
						}
					}
				}
			}
		}
		return result;
	}

	protected abstract boolean containsNew(VO in);

	protected abstract Long getAllowedCount();

	protected abstract ServiceException getException(ROOT root, Long allowedCount);

	protected abstract long getExistingCount(VO in);
}
