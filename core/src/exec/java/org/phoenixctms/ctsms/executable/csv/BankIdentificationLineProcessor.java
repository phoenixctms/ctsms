package org.phoenixctms.ctsms.executable.csv;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.BankIdentification;
import org.phoenixctms.ctsms.domain.BankIdentificationDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class BankIdentificationLineProcessor extends LineProcessor {

	private final static int BANK_NAME_COLUMN_INDEX = 6;
	private final static int BANK_CODE_NUMBER_COLUMN_INDEX = 2;
	private final static int BIC_COLUMN_INDEX = 19;
	private static final String DEFAULT_VALUE_ENCLOSING_CHAR = "\"";
	@Autowired
	protected BankIdentificationDao bankIdentificationDao;
	private int bankNameColumnIndex;
	private int bankCodeNumberColumnIndex;
	private int bicColumnIndex;

	public BankIdentificationLineProcessor() {
		super();
	}

	public BankIdentification createBankIdentification(String[] values) {
		BankIdentification bankIdentification = BankIdentification.Factory.newInstance();
		bankIdentification.setBankName(getBankName(values));
		bankIdentification.setBankCodeNumber(getBankCodeNumber(values));
		bankIdentification.setBic(getBic(values));
		bankIdentification = bankIdentificationDao.create(bankIdentification);
		return bankIdentification;
	}

	private String getBankCodeNumber(String[] values) {
		return values[bankCodeNumberColumnIndex];
	}

	public int getBankCodeNumberColumnIndex() {
		return bankCodeNumberColumnIndex;
	}

	private String getBankName(String[] values) {
		return values[bankNameColumnIndex];
	}

	public int getBankNameColumnIndex() {
		return bankNameColumnIndex;
	}

	private String getBic(String[] values) {
		return values[bicColumnIndex];
	}

	public int getBicColumnIndex() {
		return bicColumnIndex;
	}

	@Override
	public void init() {
		super.init();
		bankNameColumnIndex = BANK_NAME_COLUMN_INDEX;
		bankCodeNumberColumnIndex = BANK_CODE_NUMBER_COLUMN_INDEX;
		bicColumnIndex = BIC_COLUMN_INDEX;
		this.setValueEnclosingChar(DEFAULT_VALUE_ENCLOSING_CHAR);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
		.append(getBankName(values))
		.append(getBankCodeNumber(values))
		.toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		createBankIdentification(values);
		return 1;
	}

	public void setBankCodeNumberColumnIndex(int bankCodeNumberColumnIndex) {
		this.bankCodeNumberColumnIndex = bankCodeNumberColumnIndex;
	}

	public void setBankNameColumnIndex(int bankNameColumnIndex) {
		this.bankNameColumnIndex = bankNameColumnIndex;
	}

	public void setBicColumnIndex(int bicColumnIndex) {
		this.bicColumnIndex = bicColumnIndex;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getBankName(values))) {
			jobOutput.println("line " + lineNumber + ": empty bank name");
			return false;
		}
		if (CommonUtil.isEmptyString(getBankCodeNumber(values))) {
			jobOutput.println("line " + lineNumber + ": empty bank code number");
			return false;
		}
		return true;
	}
}
