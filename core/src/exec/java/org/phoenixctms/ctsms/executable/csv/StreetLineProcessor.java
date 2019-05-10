package org.phoenixctms.ctsms.executable.csv;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.Street;
import org.phoenixctms.ctsms.domain.StreetDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class StreetLineProcessor extends LineProcessor {

	private final static int COUNTRY_NAME_COLUMN_INDEX = 0;
	private final static int ZIP_CODE_COLUMN_INDEX = 1;
	private final static int CITY_NAME_COLUMN_INDEX = 2;
	private final static int STREET_NAME_COLUMN_INDEX = 3;
	@Autowired
	protected StreetDao streetDao;
	private int countryNameColumnIndex;
	private int zipCodeColumnIndex;
	private int cityNameColumnIndex;
	private int streetNameColumnIndex;

	public StreetLineProcessor() {
		super();
	}

	private Street createStreet(String[] values) {
		Street street = Street.Factory.newInstance();
		street.setCountryName(getCountryName(values));
		street.setCityName(getCityName(values));
		street.setZipCode(getZipCode(values));
		street.setStreetName(getStreetName(values));
		street = streetDao.create(street);
		return street;
	}

	private String getCityName(String[] values) {
		return values[cityNameColumnIndex];
	}

	public int getCityNameColumnIndex() {
		return cityNameColumnIndex;
	}

	private String getCountryName(String[] values) {
		return values[countryNameColumnIndex];
	}

	public int getCountryNameColumnIndex() {
		return countryNameColumnIndex;
	}

	private String getStreetName(String[] values) {
		return values[streetNameColumnIndex];
	}

	public int getStreetNameColumnIndex() {
		return streetNameColumnIndex;
	}

	private String getZipCode(String[] values) {
		return values[zipCodeColumnIndex];
	}

	public int getZipCodeColumnIndex() {
		return zipCodeColumnIndex;
	}

	@Override
	public void init() {
		super.init();
		countryNameColumnIndex = COUNTRY_NAME_COLUMN_INDEX;
		zipCodeColumnIndex = ZIP_CODE_COLUMN_INDEX;
		cityNameColumnIndex = CITY_NAME_COLUMN_INDEX;
		streetNameColumnIndex = STREET_NAME_COLUMN_INDEX;
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getCountryName(values))
				.append(getCityName(values))
				.append(getZipCode(values))
				.append(getStreetName(values))
				.toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		createStreet(values);
		return 1;
	}

	public void setCityNameColumnIndex(int cityNameColumnIndex) {
		this.cityNameColumnIndex = cityNameColumnIndex;
	}

	public void setCountryNameColumnIndex(int countryNameColumnIndex) {
		this.countryNameColumnIndex = countryNameColumnIndex;
	}

	public void setStreetNameColumnIndex(int streetNameColumnIndex) {
		this.streetNameColumnIndex = streetNameColumnIndex;
	}

	public void setZipCodeColumnIndex(int zipCodeColumnIndex) {
		this.zipCodeColumnIndex = zipCodeColumnIndex;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getCountryName(values))) {
			jobOutput.println("line " + lineNumber + ": empty country name");
			return false;
		}
		if (CommonUtil.isEmptyString(getCityName(values))) {
			jobOutput.println("line " + lineNumber + ": empty city name");
			return false;
		}
		if (CommonUtil.isEmptyString(getZipCode(values))) {
			jobOutput.println("line " + lineNumber + ": empty zip code");
			return false;
		}
		if (CommonUtil.isEmptyString(getStreetName(values))) {
			jobOutput.println("line " + lineNumber + ": empty street name");
			return false;
		}
		return true;
	}
}
