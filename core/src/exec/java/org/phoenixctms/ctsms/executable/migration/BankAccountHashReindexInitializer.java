package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.BankAccount;
import org.phoenixctms.ctsms.domain.BankAccountDao;
import org.springframework.beans.factory.annotation.Autowired;

public class BankAccountHashReindexInitializer extends EncryptedStringHashReindexInitializer<BankAccount, BankAccountDao> {

	@Autowired
	private BankAccountDao bankAccountDao;

	@Override
	protected BankAccountDao getDao() {
		return bankAccountDao;
	}

	@Override
	protected void persist(BankAccountDao dao, BankAccount entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<BankAccount>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("accountHolderNameHash", BankAccount::getAccountHolderNameIv, BankAccount::getEncryptedAccountHolderName,
						BankAccount::getAccountHolderNameHash, BankAccount::setAccountHolderNameHash),
				encryptedStringField("accountNumberHash", BankAccount::getAccountNumberIv, BankAccount::getEncryptedAccountNumber, BankAccount::getAccountNumberHash,
						BankAccount::setAccountNumberHash),
				encryptedStringField("bankCodeNumberHash", BankAccount::getBankCodeNumberIv, BankAccount::getEncryptedBankCodeNumber,
						BankAccount::getBankCodeNumberHash, BankAccount::setBankCodeNumberHash),
				encryptedStringField("bankNameHash", BankAccount::getBankNameIv, BankAccount::getEncryptedBankName, BankAccount::getBankNameHash,
						BankAccount::setBankNameHash),
				encryptedStringField("bicHash", BankAccount::getBicIv, BankAccount::getEncryptedBic, BankAccount::getBicHash, BankAccount::setBicHash),
				encryptedStringField("ibanHash", BankAccount::getIbanIv, BankAccount::getEncryptedIban, BankAccount::getIbanHash, BankAccount::setIbanHash));
	}
}
