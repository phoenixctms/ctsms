package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.util.ArrayList;

import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;

public class ProbandListEntryTagInputModelList extends InputModelListBase<ProbandListEntryTagValueInVO, ProbandListEntryTagInputModel> {

	public ProbandListEntryTagInputModelList(ArrayList<ProbandListEntryTagValueInVO> valuesIn) {
		super(valuesIn);
	}

	@Override
	protected ProbandListEntryTagInputModel createModel(
			ProbandListEntryTagValueInVO value) {
		return new ProbandListEntryTagInputModel(value);
	}

	@Override
	protected int getWidth(ProbandListEntryTagInputModel model) {
		return model.getWidth();
	}

	@Override
	protected void setRowIndex(ProbandListEntryTagInputModel model, int index) {
		// TODO Auto-generated method stub
		model.setRowIndex(index);
	}
	// @Override
	// public ProbandListEntryTagInputModel get(int index) {
	// return new ProbandListEntryTagInputModel(valuesIn.get(index));
	// }
}
