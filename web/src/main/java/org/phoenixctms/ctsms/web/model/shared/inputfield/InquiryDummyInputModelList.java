package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.util.ArrayList;

import org.phoenixctms.ctsms.vo.InquiryValueInVO;

public class InquiryDummyInputModelList extends InquiryInputModelList {

	public InquiryDummyInputModelList(ArrayList<InquiryValueInVO> valuesIn) {
		super(valuesIn);
	}

	@Override
	protected InquiryDummyInputModel createModel(
			InquiryValueInVO value) {
		return new InquiryDummyInputModel(value);
	}
}
