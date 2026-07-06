package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.ProbandStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandStatusEntryDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandStatusEntryHashReindexInitializer extends EncryptedStringHashReindexInitializer<ProbandStatusEntry, ProbandStatusEntryDao> {

	@Autowired
	private ProbandStatusEntryDao probandStatusEntryDao;

	@Override
	protected ProbandStatusEntryDao getDao() {
		return probandStatusEntryDao;
	}

	@Override
	protected void persist(ProbandStatusEntryDao dao, ProbandStatusEntry entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<ProbandStatusEntry>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("commentHash", ProbandStatusEntry::getCommentIv, ProbandStatusEntry::getEncryptedComment, ProbandStatusEntry::getCommentHash,
						ProbandStatusEntry::setCommentHash));
	}
}
