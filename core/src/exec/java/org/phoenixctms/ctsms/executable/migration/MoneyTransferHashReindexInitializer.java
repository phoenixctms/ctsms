package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.MoneyTransfer;
import org.phoenixctms.ctsms.domain.MoneyTransferDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MoneyTransferHashReindexInitializer extends EncryptedStringHashReindexInitializer<MoneyTransfer, MoneyTransferDao> {

	@Autowired
	private MoneyTransferDao moneyTransferDao;

	@Override
	protected MoneyTransferDao getDao() {
		return moneyTransferDao;
	}

	@Override
	protected void persist(MoneyTransferDao dao, MoneyTransfer entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<MoneyTransfer>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("commentHash", MoneyTransfer::getCommentIv, MoneyTransfer::getEncryptedComment, MoneyTransfer::getCommentHash,
						MoneyTransfer::setCommentHash));
	}
}
