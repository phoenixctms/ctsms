package org.phoenixctms.ctsms.executable.csv;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.Country;
import org.phoenixctms.ctsms.domain.CountryDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class CountryLineProcessor extends LineProcessor {

	private final static int COUNTRY_NAME_COLUMN_INDEX = 0;
	private final static int COUNTRY_CODE_COLUMN_INDEX = 1;
	@Autowired
	protected CountryDao countryDao;
	private int countryNameColumnIndex;
	private int countryCodeColumnIndex;

	public CountryLineProcessor() {
		super();
	}

	public Country createCountry(String[] values) {
		Country country = Country.Factory.newInstance();
		country.setCountryName(getCountryName(values));
		country.setCountryCode(getCountryCode(values));
		country = countryDao.create(country);
		return country;
	}

	private String getCountryCode(String[] values) {
		return values[countryCodeColumnIndex];
	}

	public int getCountryCodeColumnIndex() {
		return countryCodeColumnIndex;
	}

	private String getCountryName(String[] values) {
		return values[countryNameColumnIndex];
	}

	public int getCountryNameColumnIndex() {
		return countryNameColumnIndex;
	}

	@Override
	public void init() {
		super.init();
		countryNameColumnIndex = COUNTRY_NAME_COLUMN_INDEX;
		countryCodeColumnIndex = COUNTRY_CODE_COLUMN_INDEX;
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getCountryName(values))
				.toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		createCountry(values);
		return 1;
	}

	public void setCountryCodeColumnIndex(int countryCodeColumnIndex) {
		this.countryCodeColumnIndex = countryCodeColumnIndex;
	}

	public void setCountryNameColumnIndex(int countryNameColumnIndex) {
		this.countryNameColumnIndex = countryNameColumnIndex;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getCountryName(values))) {
			jobOutput.println("line " + lineNumber + ": empty country name");
			return false;
		}
		return true;
	}
}
