package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.ProbandContactParticulars;
import org.phoenixctms.ctsms.domain.ProbandContactParticularsDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandContactParticularsHashReindexInitializer extends EncryptedStringHashReindexInitializer<ProbandContactParticulars, ProbandContactParticularsDao> {

	@Autowired
	private ProbandContactParticularsDao probandContactParticularsDao;

	public ProbandContactParticularsHashReindexInitializer() {
	}

	@Override
	protected ProbandContactParticularsDao getDao() {
		return probandContactParticularsDao;
	}

	@Override
	protected void persist(ProbandContactParticularsDao dao, ProbandContactParticulars entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<ProbandContactParticulars>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("firstNameHash", ProbandContactParticulars::getFirstNameIv, ProbandContactParticulars::getEncryptedFirstName,
						ProbandContactParticulars::getFirstNameHash, ProbandContactParticulars::setFirstNameHash),
				encryptedStringField("lastNameHash", ProbandContactParticulars::getLastNameIv, ProbandContactParticulars::getEncryptedLastName,
						ProbandContactParticulars::getLastNameHash, ProbandContactParticulars::setLastNameHash),
				encryptedStringField("firstNameNormalizedHash", ProbandContactParticulars::getFirstNameIv, ProbandContactParticulars::getEncryptedFirstName,
						ProbandContactParticulars::getFirstNameNormalizedHash, ProbandContactParticulars::setFirstNameNormalizedHash,
						CommonUtil::normalizeFirstName),
				encryptedStringField("lastNameNormalizedHash", ProbandContactParticulars::getLastNameIv, ProbandContactParticulars::getEncryptedLastName,
						ProbandContactParticulars::getLastNameNormalizedHash, ProbandContactParticulars::setLastNameNormalizedHash,
						CommonUtil::normalizeLastName),
				encryptedStringField("citizenshipHash", ProbandContactParticulars::getCitizenshipIv, ProbandContactParticulars::getEncryptedCitizenship,
						ProbandContactParticulars::getCitizenshipHash, ProbandContactParticulars::setCitizenshipHash),
				encryptedStringField("commentHash", ProbandContactParticulars::getCommentIv, ProbandContactParticulars::getEncryptedComment,
						ProbandContactParticulars::getCommentHash, ProbandContactParticulars::setCommentHash));
	}
}
