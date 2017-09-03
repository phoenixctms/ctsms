package org.phoenixctms.ctsms.web.adapt;

import java.util.Collection;
import java.util.Date;

import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;

public class ProbandListEntryTagValueInVOStringAdapter extends InputFieldValueStringAdapter<ProbandListEntryTagValueInVO> {

	public ProbandListEntryTagValueInVOStringAdapter(int textClipMaxLength) {
		super(textClipMaxLength);
	}

	@Override
	protected boolean getBooleanValue(ProbandListEntryTagValueInVO value) {
		return value.getBooleanValue();
	}

	@Override
	protected Date getDateValue(ProbandListEntryTagValueInVO value) {
		return value.getDateValue();
	}

	@Override
	protected Float getFloatValue(ProbandListEntryTagValueInVO value) {
		return value.getFloatValue();
	}

	@Override
	protected Long getLongValue(ProbandListEntryTagValueInVO value) {
		return value.getLongValue();
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(
			ProbandListEntryTagValueInVO value) {
		return getSelectionSetValuesFromIds(value.getSelectionValueIds());
	}

	@Override
	protected String getTextValue(ProbandListEntryTagValueInVO value) {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue(ProbandListEntryTagValueInVO value) {
		return value.getTimestampValue();
	}

	@Override
	protected Date getTimeValue(ProbandListEntryTagValueInVO value) {
		return value.getTimeValue();
	}
}
