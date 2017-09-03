package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;

public class EcrfFieldInputModelList extends InputModelListBase<ECRFFieldValueInVO, EcrfFieldInputModel> {

	public EcrfFieldInputModelList(ArrayList<ECRFFieldValueInVO> valuesIn) {
		super(valuesIn);
	}

	@Override
	protected EcrfFieldInputModel createModel(
			ECRFFieldValueInVO value) {
		return new EcrfFieldInputModel(value);
	}

	public Set<ECRFFieldValueInVO> getEcrfFieldValues() {
		HashSet<ECRFFieldValueInVO> values = new HashSet<ECRFFieldValueInVO>(size());
		Iterator<EcrfFieldInputModel> it = this.iterator();
		while (it.hasNext()) {
			EcrfFieldInputModel model = it.next();
			model.sanitizeReasonForChange();
			values.add(model.getEcrfFieldValue());
		}
		return values;
	}

	@Override
	protected int getWidth(EcrfFieldInputModel model) {
		return model.getWidth();
	}

	@Override
	protected void setRowIndex(EcrfFieldInputModel model, int index) {
		model.setRowIndex(index);
	}
}
