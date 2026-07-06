package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.ProbandAddress;
import org.phoenixctms.ctsms.domain.ProbandAddressDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandAddressHashReindexInitializer extends EncryptedStringHashReindexInitializer<ProbandAddress, ProbandAddressDao> {

	@Autowired
	private ProbandAddressDao probandAddressDao;

	@Override
	protected ProbandAddressDao getDao() {
		return probandAddressDao;
	}

	@Override
	protected void persist(ProbandAddressDao dao, ProbandAddress entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<ProbandAddress>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("countryNameHash", ProbandAddress::getCountryNameIv, ProbandAddress::getEncryptedCountryName,
						ProbandAddress::getCountryNameHash, ProbandAddress::setCountryNameHash),
				encryptedStringField("provinceHash", ProbandAddress::getProvinceIv, ProbandAddress::getEncryptedProvince, ProbandAddress::getProvinceHash,
						ProbandAddress::setProvinceHash),
				encryptedStringField("zipCodeHash", ProbandAddress::getZipCodeIv, ProbandAddress::getEncryptedZipCode, ProbandAddress::getZipCodeHash,
						ProbandAddress::setZipCodeHash),
				encryptedStringField("cityNameHash", ProbandAddress::getCityNameIv, ProbandAddress::getEncryptedCityName, ProbandAddress::getCityNameHash,
						ProbandAddress::setCityNameHash),
				encryptedStringField("streetNameHash", ProbandAddress::getStreetNameIv, ProbandAddress::getEncryptedStreetName, ProbandAddress::getStreetNameHash,
						ProbandAddress::setStreetNameHash),
				encryptedStringField("houseNumberHash", ProbandAddress::getHouseNumberIv, ProbandAddress::getEncryptedHouseNumber, ProbandAddress::getHouseNumberHash,
						ProbandAddress::setHouseNumberHash),
				encryptedStringField("entranceHash", ProbandAddress::getEntranceIv, ProbandAddress::getEncryptedEntrance, ProbandAddress::getEntranceHash,
						ProbandAddress::setEntranceHash),
				encryptedStringField("doorNumberHash", ProbandAddress::getDoorNumberIv, ProbandAddress::getEncryptedDoorNumber, ProbandAddress::getDoorNumberHash,
						ProbandAddress::setDoorNumberHash),
				encryptedStringField("careOfHash", ProbandAddress::getCareOfIv, ProbandAddress::getEncryptedCareOf, ProbandAddress::getCareOfHash,
						ProbandAddress::setCareOfHash));
	}
}
