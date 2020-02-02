package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ContactDetailType;
import org.phoenixctms.ctsms.domain.ContactDetailTypeDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValue;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueInVO;

public class ProbandContactDetailTypeTagAdapter extends TagAdapter<Proband, ContactDetailType, ProbandContactDetailValue, ProbandContactDetailValueInVO> {

	private ProbandDao probandDao;
	private ContactDetailTypeDao contactDetailTypeDao;
	private Boolean person;
	private Boolean animal;

	public ProbandContactDetailTypeTagAdapter(Boolean person, Boolean animal, ProbandDao probandDao, ContactDetailTypeDao contactDetailTypeDao) {
		this.probandDao = probandDao;
		this.contactDetailTypeDao = contactDetailTypeDao;
		this.person = person;
		this.animal = animal;
	}

	public ProbandContactDetailTypeTagAdapter(ProbandDao probandDao, ContactDetailTypeDao contactDetailTypeDao) {
		this.probandDao = probandDao;
		this.contactDetailTypeDao = contactDetailTypeDao;
	}

	@Override
	protected Proband checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkProbandId(rootId, probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Proband root, ContactDetailType tag,
			ProbandContactDetailValueInVO tagValueIn) throws ServiceException {
		if (!probandDao.toProbandOutVO(root).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(root);
		if (root.isPerson() && !tag.isProband()) {
			throw L10nUtil
					.initServiceException(ServiceExceptionCodes.CONTACT_DETAIL_NOT_FOR_PROBAND_ENTRIES, L10nUtil.getContactDetailTypeName(Locales.USER, tag.getNameL10nKey()));
		}
		if (!root.isPerson() && !tag.isAnimal()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CONTACT_DETAIL_NOT_FOR_ANIMAL_ENTRIES, L10nUtil.getContactDetailTypeName(Locales.USER, tag.getNameL10nKey()));
		}
		if (!tag.isEmail() && !tag.isPhone() && tagValueIn.isNotify()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CONTACT_NOTIFY_FLAG_ALLOWED_FOR_PHONE_OR_EMAIL_CONTACT_DETAILS_ONLY);
		}
		String value = tagValueIn.getValue();
		if (tagValueIn.getNa()) {
			if (value != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CONTACT_VALUE_NOT_NULL);
			}
			if (tagValueIn.getNotify()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CONTACT_NOTIFY_FLAG_SET);
			}
		} else {
			if (value == null || value.length() == 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CONTACT_VALUE_REQUIRED);
			}
		}
	}

	@Override
	protected ContactDetailType checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkContactDetailTypeId(tagId, contactDetailTypeDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(
			ProbandContactDetailValueInVO tagValueIn) {
		return !tagValueIn.getNa();
	}

	@Override
	protected Collection<ContactDetailType> findTagsIncludingId(Proband root, Long tagId) {
		if (root != null) {
			if (root.isPerson()) {
				return contactDetailTypeDao.findByStaffProbandAnimalId(null, true, null, tagId);
			} else {
				return contactDetailTypeDao.findByStaffProbandAnimalId(null, null, true, tagId);
			}
		} else {
			return contactDetailTypeDao.findByStaffProbandAnimalId(null, person, animal, tagId);
		}
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
	protected Long getRootId(ProbandContactDetailValueInVO tagValueIn) {
		return tagValueIn.getProbandId();
	}

	@Override
	protected ContactDetailType getTag(ProbandContactDetailValue tagValue) {
		return tagValue.getType();
	}

	@Override
	protected Long getTagId(ProbandContactDetailValueInVO tagValueIn) {
		return tagValueIn.getTypeId();
	}

	@Override
	protected Collection<ProbandContactDetailValue> getTagValues(Proband root) {
		return root.getContactDetailValues();
	}

	@Override
	protected String getValue(ProbandContactDetailValueInVO tagValueIn) {
		return tagValueIn.getValue();
	}

	@Override
	protected boolean same(ProbandContactDetailValue tagValue,
			ProbandContactDetailValueInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		contactDetailTypeDao.toContactDetailTypeVOCollection(tags);
	}
}
