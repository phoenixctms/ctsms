package org.phoenixctms.ctsms.vocycle;

import java.text.MessageFormat;
import java.util.HashMap;

import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDaoImpl;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

public class InputFieldSelectionSetValueGraph extends GraphCycle1Helper<InputFieldSelectionSetValue, InputFieldSelectionSetValueOutVO, InputField, InputFieldOutVO> {

	private static final boolean LIMIT_A_INSTANCES = false;
	private static final int MAX_A_INSTANCES = 0;

	public static String getUniqueInputFieldSelectionSetValueName(InputField field, String inputFieldSelectionSetValueName) {
		if (field != null) {
			return MessageFormat.format(UNIQUE_INPUT_FIELD_SELECTION_SET_VALUE_NAME,
					field.isLocalized() ? L10nUtil.getInputFieldName(Locales.USER, field.getNameL10nKey()) : field.getNameL10nKey(), inputFieldSelectionSetValueName);
		}
		return null;
	}

	public static String getInputFieldSelectionSetValueRow(InputFieldSelectionSetValue selectionSetValue) {
		String rowL10nKey = selectionSetValue.getRowL10nKey();
		if (rowL10nKey != null && selectionSetValue.isLocalized()) {
			return L10nUtil.getInputFieldSelectionSetValueRow(Locales.USER, rowL10nKey);
		}
		return rowL10nKey;
	}

	public static String getInputFieldSelectionSetValueColumn(InputFieldSelectionSetValue selectionSetValue) {
		String columnL10nKey = selectionSetValue.getColumnL10nKey();
		if (columnL10nKey != null && selectionSetValue.isLocalized()) {
			return L10nUtil.getInputFieldSelectionSetValueColumn(Locales.USER, columnL10nKey);
		}
		return columnL10nKey;
	}

	private InputFieldSelectionSetValueDaoImpl inputFieldSelectionSetValueDaoImpl;
	private InputFieldDao inputFieldDao;
	private UserDao userDao;
	private static final String UNIQUE_INPUT_FIELD_SELECTION_SET_VALUE_NAME = "{0}: {1}";

	public InputFieldSelectionSetValueGraph(
			InputFieldSelectionSetValueDaoImpl inputFieldSelectionSetValueDaoImpl,
			InputFieldDao inputFieldDao,
			UserDao userDao) {
		this.inputFieldSelectionSetValueDaoImpl = inputFieldSelectionSetValueDaoImpl;
		this.inputFieldDao = inputFieldDao;
		this.userDao = userDao;
	}

	@Override
	protected Long getAId(InputFieldSelectionSetValue source) {
		return source.getId();
	}

	@Override
	protected Class getAVOClass() {
		return InputFieldSelectionSetValueOutVO.class;
	}

	@Override
	protected Long getBId(InputField source) {
		return source.getId();
	}

	@Override
	protected InputField getBOfA(InputFieldSelectionSetValue source) {
		return source.getField();
	}

	@Override
	protected Class getBVOClass() {
		return InputFieldOutVO.class;
	}

	@Override
	protected int getMaxAInstances() {
		return MAX_A_INSTANCES;
	}

	@Override
	protected boolean limitAInstances() {
		return LIMIT_A_INSTANCES;
	}

	@Override
	protected InputFieldOutVO newBVO() {
		return new InputFieldOutVO();
	}

	@Override
	protected void setBVOOfAVO(InputFieldSelectionSetValueOutVO target,
			InputFieldOutVO bVO) {
		target.setField(bVO);
	}

	@Override
	protected void toBVO(InputField source, InputFieldOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		inputFieldDao.toInputFieldOutVO(source, target, voMap);
	}

	@Override
	protected void toVORemainingFields(InputFieldSelectionSetValue source,
			InputFieldSelectionSetValueOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		inputFieldSelectionSetValueDaoImpl.toInputFieldSelectionSetValueOutVOBase(source, target);
		User modifiedUser = source.getModifiedUser();
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		if (source.isLocalized()) {
			target.setName(L10nUtil.getInputFieldSelectionSetValueName(Locales.USER, source.getNameL10nKey()));
		} else {
			target.setName(source.getNameL10nKey());
		}
		target.setRow(getInputFieldSelectionSetValueRow(source));
		target.setColumn(getInputFieldSelectionSetValueColumn(source));
		InputField field = source.getField();
		target.setUniqueName(getUniqueInputFieldSelectionSetValueName(field, target.getName()));
		if (ServiceUtil.isInputFieldType(field, InputFieldType.SKETCH)) {
			target.setInkRegions(source.getInkRegion());
		} else {
			target.setInkRegions(null);
		}
	}
}
