package org.phoenixctms.ctsms.test;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;

public class EcrfValidationTestVector {

	private String inputValue;
	private String exportedValue;
	private String expectedOutput;
	private String index;
	private ECRFFieldOutVO ecrfField;

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public String getExportedValue() {
		return exportedValue;
	}

	public void setExportedValue(String exportedValue) {
		this.exportedValue = exportedValue;
	}

	public String getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public ECRFFieldOutVO getEcrfField() {
		return ecrfField;
	}

	public void setEcrfField(ECRFFieldOutVO ecrfField) {
		this.ecrfField = ecrfField;
	}

	public boolean hasIndex() {
		return !CommonUtil.isEmptyString(index);
	}

	public String toString() {
		String label = ecrfField.getUniqueName().replace(ecrfField.getTrial().getName() + " - ", "");
		if (hasIndex()) {
			label += " (index " + index + ")";
		}
		return label;
	}
}