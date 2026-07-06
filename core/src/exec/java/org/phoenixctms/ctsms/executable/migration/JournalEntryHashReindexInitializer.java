package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.springframework.beans.factory.annotation.Autowired;

public class JournalEntryHashReindexInitializer extends EncryptedStringHashReindexInitializer<JournalEntry, JournalEntryDao> {

	@Autowired
	private JournalEntryDao journalEntryDao;

	@Override
	protected JournalEntryDao getDao() {
		return journalEntryDao;
	}

	@Override
	protected void persist(JournalEntryDao dao, JournalEntry entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<JournalEntry>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("titleHash", JournalEntry::getTitleIv, JournalEntry::getEncryptedTitle, JournalEntry::getTitleHash, JournalEntry::setTitleHash),
				encryptedStringField("commentHash", JournalEntry::getCommentIv, JournalEntry::getEncryptedComment, JournalEntry::getCommentHash,
						JournalEntry::setCommentHash));
	}
}
