package org.phoenixctms.ctsms.executable.migration;

import java.util.Arrays;
import java.util.List;

import org.phoenixctms.ctsms.domain.Procedure;
import org.phoenixctms.ctsms.domain.ProcedureDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcedureHashReindexInitializer extends EncryptedStringHashReindexInitializer<Procedure, ProcedureDao> {

	@Autowired
	private ProcedureDao procedureDao;

	@Override
	protected ProcedureDao getDao() {
		return procedureDao;
	}

	@Override
	protected void persist(ProcedureDao dao, Procedure entity) throws Exception {
		dao.update(entity);
	}

	@Override
	protected List<StringHashField<Procedure>> getStringHashFields() {
		return Arrays.asList(
				encryptedStringField("commentHash", Procedure::getCommentIv, Procedure::getEncryptedComment, Procedure::getCommentHash, Procedure::setCommentHash));
	}
}
