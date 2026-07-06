package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.ProbandContactDetailValue;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandContactDetailValueHashReindexInitializer extends EncryptedStringHashReindexInitializer<ProbandContactDetailValue, ProbandContactDetailValueDao> {

	@Autowired
	private ProbandContactDetailValueDao probandContactDetailValueDao;

	@Override
	protected ProbandContactDetailValueDao getDao() {
		return probandContactDetailValueDao;
	}

	@Override
	protected void persist(ProbandContactDetailValueDao dao, ProbandContactDetailValue entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<ProbandContactDetailValue>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("valueHash", ProbandContactDetailValue::getValueIv, ProbandContactDetailValue::getEncryptedValue,
						ProbandContactDetailValue::getValueHash, ProbandContactDetailValue::setValueHash),
				encryptedStringField("commentHash", ProbandContactDetailValue::getCommentIv, ProbandContactDetailValue::getEncryptedComment,
						ProbandContactDetailValue::getCommentHash, ProbandContactDetailValue::setCommentHash));
	}
}
