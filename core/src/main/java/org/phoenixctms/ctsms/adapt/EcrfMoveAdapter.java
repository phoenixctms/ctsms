package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.vo.ECRFOutVO;

public class EcrfMoveAdapter extends MoveAdapter<Trial, ECRF, ECRFOutVO> {

	private TrialDao trialDao;
	private ECRFDao ecrfDao;
	private JournalEntryDao journalEntryDao;

	public EcrfMoveAdapter(JournalEntryDao journalEntryDao, ECRFDao ecrfDao, TrialDao trialDao) {
		super();
		this.journalEntryDao = journalEntryDao;
		this.ecrfDao = ecrfDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkItem(ECRF groupItem) throws ServiceException {
	}

	@Override
	protected ECRF checkItemId(Long itemId)
			throws ServiceException {
		return CheckIDUtil.checkEcrfId(itemId, ecrfDao);
	}

	@Override
	protected void checkRoot(Trial root) throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
	}

	@Override
	protected void daoUpdate(ECRF item) throws Exception {
		ecrfDao.update(item);
	}

	@Override
	protected ArrayList<ECRF> getItemsSorted(Long rootId, ECRF item)
			throws Exception {
		ProbandGroup group = item.getGroup();
		return new ArrayList<ECRF>(ecrfDao.findByTrialGroupSorted(rootId, group != null ? group.getId() : null));
	}

	@Override
	protected long getPosition(ECRF item) {
		return item.getPosition();
	}

	@Override
	protected Trial getRoot(ECRF item) throws ServiceException {
		return item.getTrial();
	}

	@Override
	protected Long getRootId(Trial root) {
		return root.getId();
	}

	@Override
	protected ECRFOutVO logSystemMessage(ECRF item, ECRFOutVO original, Timestamp now, User modifiedUser, PositionMovement movement) throws Exception {
		String systemMessageCode;
		ECRFOutVO result = ecrfDao.toECRFOutVO(item);
		switch (movement) {
			case FIRST:
				systemMessageCode = SystemMessageCodes.ECRF_MOVED_TO_FIRST_POSITION;
				break;
			case UP:
				systemMessageCode = SystemMessageCodes.ECRF_MOVED_UP;
				break;
			case DOWN:
				systemMessageCode = SystemMessageCodes.ECRF_MOVED_DOWN;
				break;
			case LAST:
				systemMessageCode = SystemMessageCodes.ECRF_MOVED_TO_LAST_POSITION;
				break;
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.ECRF_POSITION_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.ECRF_POSITION_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.ECRF_POSITION_ROTATED_UP;
				break;
			default:
				return result;
		}
		journalEntryDao.addSystemMessage(item.getTrial(), now, modifiedUser, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(result.getTrial()) },
				systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
		return result;
	}

	@Override
	protected void logUpdatedPositionsSystemMessage(Trial root, PositionMovement movement, ArrayList<ECRFOutVO> updated,
			Timestamp now, User modifiedUser) throws Exception {
		String systemMessageCode;
		switch (movement) {
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.ECRF_POSITIONS_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.ECRF_POSITIONS_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.ECRF_POSITIONS_ROTATED_UP;
				break;
			default:
				return;
		}
		journalEntryDao.addSystemMessage(root, now, modifiedUser,
				systemMessageCode,
				new Object[] { CommonUtil.trialOutVOToString(trialDao.toTrialOutVO(root)), Integer.toString(updated.size()) },
				systemMessageCode,
				new Object[] {
				CoreUtil.getSystemMessageCommentContent(createUpdatedInfo(updated), null, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null))
		});
	}

	public ArrayList<ECRFOutVO> normalizePositions(Long groupEcrfId) throws Exception {
		ECRF item = checkItemId(groupEcrfId);
		return normalizePositions(item, aquireWriteLock(getRootId(getRoot(item))));
	}

	@Override
	protected void setPosition(ECRF item, long position) {
		item.setPosition(position);
	}

	@Override
	protected ECRFOutVO toVO(ECRF item) throws Exception {
		return ecrfDao.toECRFOutVO(item);
	}

	@Override
	protected String voToString(ECRFOutVO vo) {
		return vo.getUniqueName();
	}
}
