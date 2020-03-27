package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffTag;
import org.phoenixctms.ctsms.domain.StaffTagDao;
import org.phoenixctms.ctsms.domain.StaffTagValue;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.StaffTagValueInVO;

public class StaffTagAdapter extends TagAdapter<Staff, StaffTag, StaffTagValue, StaffTagValueInVO> {

	private StaffDao staffDao;
	private StaffTagDao staffTagDao;
	private Boolean person;
	private Boolean organisation;

	public StaffTagAdapter(Boolean person, Boolean organisation, StaffDao staffDao, StaffTagDao staffTagDao) {
		this.staffDao = staffDao;
		this.staffTagDao = staffTagDao;
		this.person = person;
		this.organisation = organisation;
	}

	public StaffTagAdapter(StaffDao staffDao, StaffTagDao staffTagDao) {
		this.staffDao = staffDao;
		this.staffTagDao = staffTagDao;
	}

	@Override
	protected Staff checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkStaffId(rootId, staffDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Staff root, StaffTag tag,
			StaffTagValueInVO tagValueIn) throws ServiceException {
		if (root.isPerson() && !tag.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_TAG_NOT_FOR_PERSON_ENTRIES, L10nUtil.getStaffTagName(Locales.USER, tag.getNameL10nKey()));
		}
		if (!root.isPerson() && !tag.isOrganisation()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STAFF_TAG_NOT_FOR_ORGANISATION_ENTRIES, L10nUtil.getStaffTagName(Locales.USER, tag.getNameL10nKey()));
		}
	}

	@Override
	protected StaffTag checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkStaffTagId(tagId, staffTagDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(StaffTagValueInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<StaffTag> findTagsIncludingId(Staff root, Long tagId) {
		if (root != null) {
			if (root.isPerson()) {
				return staffTagDao.findByPersonOrganisationIdExcelTrainingRecord(true, null, tagId, null, null);
			} else {
				return staffTagDao.findByPersonOrganisationIdExcelTrainingRecord(null, true, tagId, null, null);
			}
		} else {
			return staffTagDao.findByPersonOrganisationIdExcelTrainingRecord(person, organisation, tagId, null, null);
		}
	}

	@Override
	protected Integer getMaxOccurrence(StaffTag tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(StaffTag tag) {
		return tag.getMismatchMsgL10nKey();
	}

	@Override
	protected String getNameL10nKey(StaffTag tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(StaffTag tag) {
		return tag.getRegExp();
	}

	@Override
	protected Long getRootId(StaffTagValueInVO tagValueIn) {
		return tagValueIn.getStaffId();
	}

	@Override
	protected StaffTag getTag(StaffTagValue tagValue) {
		return tagValue.getTag();
	}

	@Override
	protected Long getTagId(StaffTagValueInVO tagValueIn) {
		return tagValueIn.getTagId();
	}

	@Override
	protected Collection<StaffTagValue> getTagValues(Staff root) {
		return root.getTagValues();
	}

	@Override
	protected String getValue(StaffTagValueInVO tagValueIn) {
		return tagValueIn.getValue();
	}

	@Override
	protected boolean same(StaffTagValue tagValue, StaffTagValueInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		staffTagDao.toStaffTagVOCollection(tags);
	}
}
