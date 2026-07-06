package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.ProbandTagValue;
import org.phoenixctms.ctsms.domain.ProbandTagValueDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandTagValueHashReindexInitializer extends EncryptedStringHashReindexInitializer<ProbandTagValue, ProbandTagValueDao> {

	@Autowired
	private ProbandTagValueDao probandTagValueDao;

	@Override
	protected ProbandTagValueDao getDao() {
		return probandTagValueDao;
	}

	@Override
	protected void persist(ProbandTagValueDao dao, ProbandTagValue entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<ProbandTagValue>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("valueHash", ProbandTagValue::getValueIv, ProbandTagValue::getEncryptedValue, ProbandTagValue::getValueHash,
						ProbandTagValue::setValueHash));
	}
}
