package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandTag;
import org.phoenixctms.ctsms.domain.ProbandTagDao;
import org.phoenixctms.ctsms.domain.ProbandTagValue;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.ProbandTagValueInVO;

public class ProbandTagAdapter extends TagAdapter<Proband, ProbandTag, ProbandTagValue, ProbandTagValueInVO> {

	private ProbandDao probandDao;
	private ProbandTagDao probandTagDao;
	private Boolean person;
	private Boolean animal;

	public ProbandTagAdapter(Boolean person, Boolean animal, ProbandDao probandDao, ProbandTagDao probandTagDao) {
		this.probandDao = probandDao;
		this.probandTagDao = probandTagDao;
		this.person = person;
		this.animal = animal;
	}

	public ProbandTagAdapter(ProbandDao probandDao, ProbandTagDao probandTagDao) {
		this.probandDao = probandDao;
		this.probandTagDao = probandTagDao;
	}

	@Override
	protected Proband checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkProbandId(rootId, probandDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Proband root, ProbandTag tag,
			ProbandTagValueInVO tagValueIn) throws ServiceException {
		if (!probandDao.toProbandOutVO(root).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(root);
		if (root.isPerson() && !tag.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_TAG_NOT_FOR_PERSON_ENTRIES, L10nUtil.getProbandTagName(Locales.USER, tag.getNameL10nKey()));
		}
		if (!root.isPerson() && !tag.isAnimal()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_TAG_NOT_FOR_ANIMAL_ENTRIES, L10nUtil.getProbandTagName(Locales.USER, tag.getNameL10nKey()));
		}
	}

	@Override
	protected ProbandTag checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkProbandTagId(tagId, probandTagDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(ProbandTagValueInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<ProbandTag> findTagsIncludingId(Proband root, Long tagId) {
		if (root != null) {
			if (root.isPerson()) {
				return probandTagDao.findByPersonAnimalIdExcel(true, null, tagId, null);
			} else {
				return probandTagDao.findByPersonAnimalIdExcel(null, true, tagId, null);
			}
		} else {
			return probandTagDao.findByPersonAnimalIdExcel(person, animal, tagId, null);
		}
	}

	@Override
	protected Integer getMaxOccurrence(ProbandTag tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(ProbandTag tag) {
		return tag.getMismatchMsgL10nKey();
	}

	@Override
	protected String getNameL10nKey(ProbandTag tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(ProbandTag tag) {
		return tag.getRegExp();
	}

	@Override
	protected Long getRootId(ProbandTagValueInVO tagValueIn) {
		return tagValueIn.getProbandId();
	}

	@Override
	protected ProbandTag getTag(ProbandTagValue tagValue) {
		return tagValue.getTag();
	}

	@Override
	protected Long getTagId(ProbandTagValueInVO tagValueIn) {
		return tagValueIn.getTagId();
	}

	@Override
	protected Collection<ProbandTagValue> getTagValues(Proband root) {
		return root.getTagValues();
	}

	@Override
	protected String getValue(ProbandTagValueInVO tagValueIn) {
		return tagValueIn.getValue();
	}

	@Override
	protected boolean same(ProbandTagValue tagValue, ProbandTagValueInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		probandTagDao.toProbandTagVOCollection(tags);
	}
}
