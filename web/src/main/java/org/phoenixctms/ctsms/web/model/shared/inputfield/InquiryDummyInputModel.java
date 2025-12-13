package org.phoenixctms.ctsms.web.model.shared.inputfield;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

public class InquiryDummyInputModel extends InquiryInputModel {

	public InquiryDummyInputModel(InquiryValueInVO inquiryValue) {
		super(inquiryValue);
	}

	@Override
	public Color getFieldColor() {
		return Settings.getColor(SettingCodes.INPUT_MODEL_DUMMY_COLOR, Bundle.SETTINGS, DefaultSettings.INPUT_MODEL_DUMMY_COLOR);
	}

	@Override
	public String getModifiedAnnotation() {
		return "";
	}

	@Override
	public String getName() {
		return super.getName(MessageCodes.DUMMY_INQUIRY_VALUE_INPUT_TITLE);
	}

	@Override
	public boolean isCollapsed() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public boolean isDummy() {
		return true;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Object load() {
		return null;
	}

	@Override
	public Object update() {
		return null;
	}
}
