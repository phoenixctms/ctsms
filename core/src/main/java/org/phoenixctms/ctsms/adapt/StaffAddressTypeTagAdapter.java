package org.phoenixctms.ctsms.adapt;

import java.util.Collection;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.AddressType;
import org.phoenixctms.ctsms.domain.AddressTypeDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffAddress;
import org.phoenixctms.ctsms.domain.StaffAddressDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.StaffAddressInVO;

public class StaffAddressTypeTagAdapter extends TagAdapter<Staff, AddressType, StaffAddress, StaffAddressInVO> {

	private StaffDao staffDao;
	private AddressTypeDao addressTypeDao;
	private StaffAddressDao staffAddressDao;

	public StaffAddressTypeTagAdapter(StaffDao staffDao, AddressTypeDao addressTypeDao) {
		this.staffDao = staffDao;
		this.addressTypeDao = addressTypeDao;
	}

	public StaffAddressTypeTagAdapter(StaffDao staffDao, AddressTypeDao addressTypeDao, StaffAddressDao staffAddressDao) {
		this.staffDao = staffDao;
		this.addressTypeDao = addressTypeDao;
		this.staffAddressDao = staffAddressDao;
	}

	@Override
	protected Staff checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkStaffId(rootId, staffDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Staff root, AddressType tag,
			StaffAddressInVO tagValueIn) throws ServiceException {
		if (!tag.isStaff()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ADDRESS_TYPE_NOT_FOR_STAFF_ENTRIES, L10nUtil.getAddressTypeName(Locales.USER, tag.getNameL10nKey()));
		}
		Pattern zipCodeRegExp = Settings.getRegexp(SettingCodes.ZIP_CODE_REGEXP, Bundle.SETTINGS, DefaultSettings.ZIP_CODE_REGEXP);
		if (zipCodeRegExp != null && !zipCodeRegExp.matcher(tagValueIn.getZipCode()).find()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_ZIP_CODE, tagValueIn.getZipCode(), zipCodeRegExp.pattern());
		}
		if (tagValueIn.getId() == null) {
			if (tagValueIn.isCv() && staffAddressDao.getCount(tagValueIn.getStaffId(), true, null, null) > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_ADDRESS_ALREADY_ANOTHER_ADDRESS_MARKED_FOR_CV);
			}
		} else {
			if (tagValueIn.isCv() && !CheckIDUtil.checkStaffAddressId(tagValueIn.getId(), staffAddressDao).isCv() &&
					staffAddressDao.getCount(tagValueIn.getStaffId(), true, null, null) > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_ADDRESS_ALREADY_ANOTHER_ADDRESS_MARKED_FOR_CV);
			}
		}
	}

	@Override
	protected AddressType checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkAddressTypeId(tagId, addressTypeDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(StaffAddressInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<AddressType> findTagsIncludingId(Staff root, Long tagId) {
		return addressTypeDao.findByStaffProbandAnimalId(true, null, null, tagId);
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
	protected Long getRootId(StaffAddressInVO tagValueIn) {
		return tagValueIn.getStaffId();
	}

	@Override
	protected AddressType getTag(StaffAddress tagValue) {
		return tagValue.getType();
	}

	@Override
	protected Long getTagId(StaffAddressInVO tagValueIn) {
		return tagValueIn.getTypeId();
	}

	@Override
	protected Collection<StaffAddress> getTagValues(Staff root) {
		return root.getAddresses();
	}

	@Override
	protected String getValue(StaffAddressInVO tagValueIn) {
		return null;
	}

	@Override
	protected boolean same(StaffAddress tagValue, StaffAddressInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		addressTypeDao.toAddressTypeVOCollection(tags);
	}
}
