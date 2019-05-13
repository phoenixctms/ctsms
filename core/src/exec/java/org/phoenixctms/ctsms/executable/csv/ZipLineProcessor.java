package org.phoenixctms.ctsms.executable.csv;

import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.Zip;
import org.phoenixctms.ctsms.domain.ZipDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class ZipLineProcessor extends LineProcessor {

	private final static int COUNTRY_NAME_COLUMN_INDEX = 0;
	private final static int CITY_NAME_COLUMN_INDEX = 2;
	private final static int ZIP_CODE_COLUMN_INDEX = 1;
	private final static String DEFAULT_ZIP_CODE_SEPARATOR_PATTERN = " ";
	@Autowired
	protected ZipDao zipDao;
	private int countryNameColumnIndex;
	private int cityNameColumnIndex;
	private int zipCodeColumnIndex;
	private Pattern zipCodeSeparatorRegexp;

	public ZipLineProcessor() {
		super();
	}

	private Zip createZip(String countryName, String cityName, String zipCode) {
		Zip zip = Zip.Factory.newInstance();
		zip.setCountryName(countryName);
		zip.setCityName(cityName);
		zip.setZipCode(zipCode);
		zip = zipDao.create(zip);
		return zip;
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

	private String getZipCode(String[] values) {
		return values[zipCodeColumnIndex];
	}

	public int getZipCodeColumnIndex() {
		return zipCodeColumnIndex;
	}

	public String getZipCodeSeparatorRegexpPattern() {
		return zipCodeSeparatorRegexp.pattern();
	}

	@Override
	public void init() {
		super.init();
		countryNameColumnIndex = COUNTRY_NAME_COLUMN_INDEX;
		cityNameColumnIndex = CITY_NAME_COLUMN_INDEX;
		zipCodeColumnIndex = ZIP_CODE_COLUMN_INDEX;
		this.setZipCodeSeparatorRegexpPattern(DEFAULT_ZIP_CODE_SEPARATOR_PATTERN);
	}

	private int lineHashCode(String countryName, String cityName, String zipCode) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(countryName)
				.append(zipCode)
				.append(cityName)
				.toHashCode();
	}

	@Override
	protected int lineHashCode(String[] values) {
		return lineHashCode(getCountryName(values), getCityName(values), getZipCode(values));
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		int rowCount = 0;
		if (multipleInsert) {
			String[] zipCodes = zipCodeSeparatorRegexp.split(getZipCode(values), -1);
			for (int i = 0; i < zipCodes.length; i++) {
				if (zipCodes[i].length() > 0) {
					if (!filterDupes || dupeCheck.add(lineHashCode(getCountryName(values), getCityName(values), zipCodes[i]))) {
						createZip(getCountryName(values), getCityName(values), zipCodes[i]);
						rowCount++;
					} else {
						jobOutput.println("line " + lineNumber + ": dupe ignored - zip code: " + zipCodes[i]);
					}
				} else {
					jobOutput.println("line " + lineNumber + ": zero-length zip code");
				}
			}
		} else {
			createZip(getCountryName(values), getCityName(values), getZipCode(values));
			rowCount++;
		}
		return rowCount;
	}

	public void setCityNameColumnIndex(int cityNameColumnIndex) {
		this.cityNameColumnIndex = cityNameColumnIndex;
	}

	public void setCountryNameColumnIndex(int countryNameColumnIndex) {
		this.countryNameColumnIndex = countryNameColumnIndex;
	}

	public void setZipCodeColumnIndex(int zipCodeColumnIndex) {
		this.zipCodeColumnIndex = zipCodeColumnIndex;
	}

	public void setZipCodeSeparatorRegexpPattern(String zipCodeSeparatorRegexpPattern) {
		if (zipCodeSeparatorRegexpPattern != null && zipCodeSeparatorRegexpPattern.length() > 0) {
			this.zipCodeSeparatorRegexp = Pattern.compile(zipCodeSeparatorRegexpPattern);
			multipleInsert = true;
		} else {
			multipleInsert = false;
			this.zipCodeSeparatorRegexp = null;
		}
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
			jobOutput.println("line " + lineNumber + ": empty zip codes");
			return false;
		}
		return true;
	}
}
