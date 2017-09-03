package org.phoenixctms.ctsms.adapt;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;

public abstract class InputFieldSelectionSetValueCollisionFinder extends CollisionFinder<InputFieldSelectionSetValueInVO, InputFieldSelectionSetValue, InputField> {

	private InputFieldDao inputFieldDao;
	protected InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;

	public InputFieldSelectionSetValueCollisionFinder(InputFieldDao inputFieldDao, InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao) {
		this.inputFieldSelectionSetValueDao = inputFieldSelectionSetValueDao;
		this.inputFieldDao = inputFieldDao;
	}

	@Override
	protected InputField aquireWriteLock(InputFieldSelectionSetValueInVO in) throws ServiceException {
		return CheckIDUtil.checkInputFieldId(in.getFieldId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected boolean equals(InputFieldSelectionSetValueInVO in,
			InputFieldSelectionSetValue existing) {
		return in.getId().equals(existing.getId());
	}

	@Override
	protected boolean isNew(InputFieldSelectionSetValueInVO in) {
		return in.getId() == null;
	}
}
