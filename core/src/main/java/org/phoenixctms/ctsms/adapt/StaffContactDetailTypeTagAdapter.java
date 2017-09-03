package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ContactDetailType;
import org.phoenixctms.ctsms.domain.ContactDetailTypeDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffContactDetailValue;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueInVO;

public class StaffContactDetailTypeTagAdapter extends TagAdapter<Staff, ContactDetailType, StaffContactDetailValue, StaffContactDetailValueInVO> {

	private StaffDao staffDao;
	private ContactDetailTypeDao contactDetailTypeDao;

	public StaffContactDetailTypeTagAdapter(StaffDao staffDao, ContactDetailTypeDao contactDetailTypeDao) {
		this.staffDao = staffDao;
		this.contactDetailTypeDao = contactDetailTypeDao;
	}

	@Override
	protected Staff checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkStaffId(rootId, staffDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Staff root, ContactDetailType tag, StaffContactDetailValueInVO tagValueIn)
			throws ServiceException {
		if (!tag.isStaff()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CONTACT_DETAIL_NOT_FOR_STAFF_ENTRIES, L10nUtil.getContactDetailTypeName(Locales.USER, tag.getNameL10nKey()));
		}
		if (!tag.isEmail() && !tag.isPhone() && tagValueIn.isNotify()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CONTACT_NOTIFY_FLAG_ALLOWED_FOR_PHONE_OR_EMAIL_CONTACT_DETAILS_ONLY); // "notify flag allowed for phone or email contact details only");
		}
		String value = tagValueIn.getValue();
		if (tagValueIn.getNa()) {
			if (value != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CONTACT_VALUE_NOT_NULL);
			}
			if (tagValueIn.getNotify()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CONTACT_NOTIFY_FLAG_SET);
			}
		} else {
			if (value == null || value.length() == 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_CONTACT_VALUE_REQUIRED);
			}
		}
	}

	@Override
	protected ContactDetailType checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkContactDetailTypeId(tagId, contactDetailTypeDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(
			StaffContactDetailValueInVO tagValueIn) {
		return !tagValueIn.getNa();
	}

	@Override
	protected Collection<ContactDetailType> findTagsIncludingId(Staff root, Long tagId) {
		return contactDetailTypeDao.findByStaffProbandAnimalId(true, null, null, tagId);
	}

	@Override
	protected Integer getMaxOccurrence(ContactDetailType tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(ContactDetailType tag) {
		return tag.getMismatchMsgL10nKey();
	}

	@Override
	protected String getNameL10nKey(ContactDetailType tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(ContactDetailType tag) {
		return tag.getRegExp();
	}

	@Override
	protected Long getRootId(StaffContactDetailValueInVO tagValueIn) {
		return tagValueIn.getStaffId();
	}

	@Override
	protected ContactDetailType getTag(StaffContactDetailValue tagValue) {
		return tagValue.getType();
	}

	@Override
	protected Long getTagId(StaffContactDetailValueInVO tagValueIn) {
		return tagValueIn.getTypeId();
	}

	@Override
	protected Collection<StaffContactDetailValue> getTagValues(Staff root) {
		return root.getContactDetailValues();
	}

	@Override
	protected String getValue(StaffContactDetailValueInVO tagValueIn) {
		return tagValueIn.getValue();
	}

	@Override
	protected boolean same(StaffContactDetailValue tagValue,
			StaffContactDetailValueInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		contactDetailTypeDao.toContactDetailTypeVOCollection(tags);
	}
}
