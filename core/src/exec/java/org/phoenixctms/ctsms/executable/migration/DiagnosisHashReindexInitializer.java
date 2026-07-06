package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.Diagnosis;
import org.phoenixctms.ctsms.domain.DiagnosisDao;
import org.springframework.beans.factory.annotation.Autowired;

public class DiagnosisHashReindexInitializer extends EncryptedStringHashReindexInitializer<Diagnosis, DiagnosisDao> {

	@Autowired
	private DiagnosisDao diagnosisDao;

	@Override
	protected DiagnosisDao getDao() {
		return diagnosisDao;
	}

	@Override
	protected void persist(DiagnosisDao dao, Diagnosis entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<Diagnosis>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("commentHash", Diagnosis::getCommentIv, Diagnosis::getEncryptedComment, Diagnosis::getCommentHash, Diagnosis::setCommentHash));
	}
}
