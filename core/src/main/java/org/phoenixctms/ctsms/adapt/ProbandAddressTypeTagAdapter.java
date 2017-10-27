package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.AddressType;
import org.phoenixctms.ctsms.domain.AddressTypeDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandAddress;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ProbandAddressInVO;

public class ProbandAddressTypeTagAdapter extends TagAdapter<Proband, AddressType, ProbandAddress, ProbandAddressInVO> {

	private ProbandDao probandDao;
	private AddressTypeDao addressTypeDao;
	private Boolean person;
	private Boolean animal;

	public ProbandAddressTypeTagAdapter(Boolean person, Boolean animal, ProbandDao probandDao, AddressTypeDao addressTypeDao) {
		this.probandDao = probandDao;
		this.addressTypeDao = addressTypeDao;
		this.person = person;
		this.animal = animal;
	}

	public ProbandAddressTypeTagAdapter(ProbandDao probandDao, AddressTypeDao addressTypeDao) {
		this.probandDao = probandDao;
		this.addressTypeDao = addressTypeDao;
	}
	@Override
	protected Proband checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkProbandId(rootId, probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Proband root, AddressType tag,
			ProbandAddressInVO tagValueIn) throws ServiceException {
		if (!probandDao.toProbandOutVO(root).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(root);
		Pattern zipCodeRegExp = Settings.getRegexp(SettingCodes.ZIP_CODE_REGEXP, Bundle.SETTINGS, DefaultSettings.ZIP_CODE_REGEXP);
		if (zipCodeRegExp != null && !zipCodeRegExp.matcher(tagValueIn.getZipCode()).find()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ZIP_CODE, tagValueIn.getZipCode(), zipCodeRegExp.pattern());
		}
		if (root.isPerson() && !tag.isProband()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ADDRESS_TYPE_NOT_FOR_PERSON_ENTRIES, L10nUtil.getAddressTypeName(Locales.USER, tag.getNameL10nKey()));
		}
		if (!root.isPerson() && !tag.isAnimal()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ADDRESS_TYPE_NOT_FOR_ANIMAL_ENTRIES, L10nUtil.getAddressTypeName(Locales.USER, tag.getNameL10nKey()));
		}
	}

	@Override
	protected AddressType checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkAddressTypeId(tagId, addressTypeDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(ProbandAddressInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<AddressType> findTagsIncludingId(Proband root, Long tagId) {
		// return addressTypeDao.findByStaffProbandId(null, true, tagId);
		if (root != null) {
			if (root.isPerson()) {
				return addressTypeDao.findByStaffProbandAnimalId(null, true, null, tagId);
			} else {
				return addressTypeDao.findByStaffProbandAnimalId(null, null, true, tagId);
			}
		} else {
			return addressTypeDao.findByStaffProbandAnimalId(null, person, animal, tagId);
		}
	}

	@Override
	protected Integer getMaxOccurrence(AddressType tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(AddressType tag) {
		return null;
	}

	@Override
	protected String getNameL10nKey(AddressType tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(AddressType tag) {
		return null;
	}

	@Override
	protected Long getRootId(ProbandAddressInVO tagValueIn) {
		return tagValueIn.getProbandId();
	}

	@Override
	protected AddressType getTag(ProbandAddress tagValue) {
		return tagValue.getType();
	}

	@Override
	protected Long getTagId(ProbandAddressInVO tagValueIn) {
		return tagValueIn.getTypeId();
	}

	@Override
	protected Collection<ProbandAddress> getTagValues(Proband root) {
		return root.getAddresses();
	}

	@Override
	protected String getValue(ProbandAddressInVO tagValueIn) {
		return null;
	}

	@Override
	protected boolean same(ProbandAddress tagValue, ProbandAddressInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		addressTypeDao.toAddressTypeVOCollection(tags);
	}
}
