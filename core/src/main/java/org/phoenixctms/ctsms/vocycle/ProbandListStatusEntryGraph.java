package org.phoenixctms.ctsms.vocycle;

import java.util.HashMap;

import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDaoImpl;
import org.phoenixctms.ctsms.domain.ProbandListStatusType;
import org.phoenixctms.ctsms.domain.ProbandListStatusTypeDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;

public class ProbandListStatusEntryGraph extends GraphCycle1Helper<ProbandListStatusEntry, ProbandListStatusEntryOutVO, ProbandListEntry, ProbandListEntryOutVO> {

	private static final boolean LIMIT_A_INSTANCES = false;
	private static final int MAX_A_INSTANCES = 0;
	private ProbandListStatusEntryDaoImpl probandListStatusEntryDaoImpl;
	private ProbandListEntryDao probandListEntryDao;
	private ProbandListStatusTypeDao probandListStatusTypeDao;
	private UserDao userDao;

	public ProbandListStatusEntryGraph(
			ProbandListStatusEntryDaoImpl probandListStatusEntryDaoImpl,
			ProbandListEntryDao probandListEntryDao,
			ProbandListStatusTypeDao probandListStatusTypeDao, UserDao userDao) {
		this.probandListStatusEntryDaoImpl = probandListStatusEntryDaoImpl;
		this.probandListEntryDao = probandListEntryDao;
		this.probandListStatusTypeDao = probandListStatusTypeDao;
		this.userDao = userDao;
	}

	@Override
	protected Long getAId(ProbandListStatusEntry source) {
		return source.getId();
	}

	@Override
	protected Class getAVOClass() {
		return ProbandListStatusEntryOutVO.class;
	}

	@Override
	protected Long getBId(ProbandListEntry source) {
		return source.getId();
	}

	@Override
	protected ProbandListEntry getBOfA(ProbandListStatusEntry source) {
		return source.getListEntry();
	}

	@Override
	protected Class getBVOClass() {
		return ProbandListEntryOutVO.class;
	}

	@Override
	protected int getMaxAInstances() {
		return MAX_A_INSTANCES;
	}

	@Override
	protected boolean limitAInstances() {
		return LIMIT_A_INSTANCES;
	}

	@Override
	protected ProbandListEntryOutVO newBVO() {
		return new ProbandListEntryOutVO();
	}

	@Override
	protected void setBVOOfAVO(ProbandListStatusEntryOutVO target,
			ProbandListEntryOutVO bVO) {
		target.setListEntry(bVO);
	}

	@Override
	protected void toBVO(ProbandListEntry source, ProbandListEntryOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		probandListEntryDao.toProbandListEntryOutVO(source, target, voMap);
	}

	@Override
	protected void toVORemainingFields(ProbandListStatusEntry source,
			ProbandListStatusEntryOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		probandListStatusEntryDaoImpl.toProbandListStatusEntryOutVOBase(source, target);
		ProbandListStatusType status = source.getStatus();
		User modifiedUser = source.getModifiedUser();
		if (status != null) {
			target.setStatus(probandListStatusTypeDao.toProbandListStatusTypeVO(status));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		if (CommonUtil.ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON) {
			try {
				if (!CoreUtil.isPassDecryption()) {
					throw new Exception();
				}
				target.setReason((String) CryptoUtil.decryptValue(source.getReasonIv(), source.getEncryptedReason()));
				target.setDecrypted(true);
			} catch (Exception e) {
				target.setReason(null);
				target.setDecrypted(false);
			}
		} else {
			target.setDecrypted(true);
		}
	}
}
