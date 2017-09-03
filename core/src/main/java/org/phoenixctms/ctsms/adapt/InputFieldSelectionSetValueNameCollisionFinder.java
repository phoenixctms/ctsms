package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;

public class InputFieldSelectionSetValueNameCollisionFinder extends InputFieldSelectionSetValueCollisionFinder {

	public InputFieldSelectionSetValueNameCollisionFinder(InputFieldDao inputFieldDao, InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao) {
		super(inputFieldDao, inputFieldSelectionSetValueDao);
	}

	@Override
	protected Collection<InputFieldSelectionSetValue> getCollidingItems(
			InputFieldSelectionSetValueInVO in, InputField root) {
		return inputFieldSelectionSetValueDao.findByFieldNameL10nKeyLocalized(in.getFieldId(), in.getName(), false);
	}
}
