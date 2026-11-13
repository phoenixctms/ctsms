package org.phoenixctms.ctsms.adapt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;

public class InputFieldSelectionSetValueMatrixRowPresetCollisionFinder extends InputFieldSelectionSetValueCollisionFinder {

	public InputFieldSelectionSetValueMatrixRowPresetCollisionFinder(InputFieldDao inputFieldDao, InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao) {
		super(inputFieldDao, inputFieldSelectionSetValueDao);
	}

	@Override
	protected Collection<InputFieldSelectionSetValue> getCollidingItems(
			InputFieldSelectionSetValueInVO in, InputField root) {
		if (in.getFieldId() == null) {
			return new ArrayList<InputFieldSelectionSetValue>();
		}
		return inputFieldSelectionSetValueDao.findByFieldPreset(in.getFieldId(), true, null);
	}

	@Override
	protected boolean match(InputFieldSelectionSetValueInVO in, InputFieldSelectionSetValue existing, InputField root) {
		return Objects.equals(in.getRow(), existing.getRowL10nKey());
	}
}
