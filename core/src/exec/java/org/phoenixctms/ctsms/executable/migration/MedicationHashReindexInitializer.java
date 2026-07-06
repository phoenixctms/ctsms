package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.Medication;
import org.phoenixctms.ctsms.domain.MedicationDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MedicationHashReindexInitializer extends EncryptedStringHashReindexInitializer<Medication, MedicationDao> {

	@Autowired
	private MedicationDao medicationDao;

	@Override
	protected MedicationDao getDao() {
		return medicationDao;
	}

	@Override
	protected void persist(MedicationDao dao, Medication entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<Medication>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("commentHash", Medication::getCommentIv, Medication::getEncryptedComment, Medication::getCommentHash, Medication::setCommentHash));
	}
}
