package org.phoenixctms.ctsms.vocycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.phoenixctms.ctsms.compare.Comparators;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDaoImpl;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

public class InputFieldGraph extends GraphCycleNHelper<InputField, InputFieldOutVO, InputFieldSelectionSetValue, InputFieldSelectionSetValueOutVO> {

	private static final boolean LIMIT_A_INSTANCES = true;
	private static final int MAX_A_INSTANCES = 1;
	private InputFieldDaoImpl inputFieldDaoImpl;
	private InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	private UserDao userDao;

	public InputFieldGraph(
			InputFieldDaoImpl inputFieldDaoImpl,
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao,
			UserDao userDao) {
		this.inputFieldDaoImpl = inputFieldDaoImpl;
		this.inputFieldSelectionSetValueDao = inputFieldSelectionSetValueDao;
		this.userDao = userDao;
	}

	@Override
	protected Long getAId(InputField source) {
		return source.getId();
	}

	@Override
	protected InputField getAOfB(InputFieldSelectionSetValue source) {
		return source.getField();
	}

	@Override
	protected Class getAVOClass() {
		return InputFieldOutVO.class;
	}

	@Override
	protected Long getBId(InputFieldSelectionSetValue source) {
		return source.getId();
	}

	@Override
	protected Collection<InputFieldSelectionSetValue> getBsOfA(InputField source) {
		if (ServiceUtil.isLoadSelectionSet(source.getFieldType())) {
			return source.getSelectionSetValues();
		} else {
			return new ArrayList<InputFieldSelectionSetValue>();
		}
	}

	@Override
	protected Class getBVOClass() {
		return InputFieldSelectionSetValueOutVO.class;
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getBVOsOfAVO(
			InputFieldOutVO target) {
		return target.getSelectionSetValues();
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
	protected InputFieldSelectionSetValueOutVO newBVO() {
		return new InputFieldSelectionSetValueOutVO();
	}

	@Override
	protected void setAVOOfBVO(InputFieldSelectionSetValueOutVO bVO,
			InputFieldOutVO aVO) {
		bVO.setField(aVO);
	}

	@Override
	protected void toBVO(InputFieldSelectionSetValue source,
			InputFieldSelectionSetValueOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		inputFieldSelectionSetValueDao.toInputFieldSelectionSetValueOutVO(source, target, voMap);
	}

	@Override
	protected void toVORemainingFields(InputField source,
			InputFieldOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		inputFieldDaoImpl.toInputFieldOutVOBase(source, target);
		Collections.sort((ArrayList<InputFieldSelectionSetValueOutVO>) target.getSelectionSetValues(), Comparators.INPUT_FIELD_SELECTION_SET_VALUE_OUT_VO);
		User modifiedUser = source.getModifiedUser();
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		target.setFieldType(L10nUtil.createInputFieldTypeVO(Locales.USER, source.getFieldType()));
		target.setHasImage(source.getFileSize() != null && source.getFileSize() > 0l);
		if (source.isLocalized()) {
			target.setTextPreset(L10nUtil.getInputFieldTextPreset(Locales.USER, source.getTextPresetL10nKey()));
			target.setName(L10nUtil.getInputFieldName(Locales.USER, source.getNameL10nKey()));
			target.setTitle(L10nUtil.getInputFieldTitle(Locales.USER, source.getTitleL10nKey()));
			target.setComment(L10nUtil.getInputFieldComment(Locales.USER, source.getCommentL10nKey()));
			target.setValidationErrorMsg(L10nUtil.getInputFieldValidationErrorMsg(Locales.USER, source.getValidationErrorMsgL10nKey()));
		} else {
			target.setTextPreset(source.getTextPresetL10nKey());
			target.setName(source.getNameL10nKey());
			target.setTitle(source.getTitleL10nKey());
			target.setComment(source.getCommentL10nKey());
			target.setValidationErrorMsg(source.getValidationErrorMsgL10nKey());
		}
		if (InputFieldType.AUTOCOMPLETE.equals(source.getFieldType())) {
			target.setTextPreset(ServiceUtil.getAutocompletePresetValue(source.getId(), inputFieldSelectionSetValueDao));
		}
	}
}
