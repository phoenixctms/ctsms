package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandListStatusEntryHashReindexInitializer extends EncryptedStringHashReindexInitializer<ProbandListStatusEntry, ProbandListStatusEntryDao> {

	@Autowired
	private ProbandListStatusEntryDao probandListStatusEntryDao;

	@Override
	protected ProbandListStatusEntryDao getDao() {
		return probandListStatusEntryDao;
	}

	@Override
	protected void persist(ProbandListStatusEntryDao dao, ProbandListStatusEntry entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<ProbandListStatusEntry>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("reasonHash", ProbandListStatusEntry::getReasonIv, ProbandListStatusEntry::getEncryptedReason, ProbandListStatusEntry::getReasonHash,
						ProbandListStatusEntry::setReasonHash));
	}
}
