package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InputFieldInVO;

public class InputFieldCollisionFinder extends CollisionFinder<InputFieldInVO, InputField, Object> {

	private InputFieldDao inputFieldDao;

	public InputFieldCollisionFinder(InputFieldDao inputFieldDao) {
		this.inputFieldDao = inputFieldDao;
	}

	@Override
	protected InputField aquireWriteLock(InputFieldInVO in)
			throws ServiceException {
		return null; // table level lock needed. name column made unique instead
	}

	@Override
	protected boolean equals(InputFieldInVO in,
			InputField existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected Collection<InputField> getCollidingItems(
			InputFieldInVO in, Object root) {
		return inputFieldDao.findByNameL10nKeyLocalized(in.getName(), null);
	}

	@Override
	protected boolean isNew(InputFieldInVO in) {
		return in.getId() == null;
	}
}
