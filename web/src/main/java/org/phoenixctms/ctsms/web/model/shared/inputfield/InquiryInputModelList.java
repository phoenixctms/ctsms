package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.util.ArrayList;

import org.phoenixctms.ctsms.vo.InquiryValueInVO;

public class InquiryInputModelList extends InputModelListBase<InquiryValueInVO, InquiryInputModel> {

	public InquiryInputModelList(ArrayList<InquiryValueInVO> valuesIn) {
		super(valuesIn);
	}

	@Override
	protected InquiryInputModel createModel(
			InquiryValueInVO value) {
		return new InquiryInputModel(value);
	}

	@Override
	protected int getWidth(InquiryInputModel model) {
		return model.getWidth();
	}

	@Override
	protected void setRowIndex(InquiryInputModel model, int index) {
		model.setRowIndex(index);
	}
}
