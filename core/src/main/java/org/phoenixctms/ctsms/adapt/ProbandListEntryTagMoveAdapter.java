package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTag;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
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
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;

public class ProbandListEntryTagMoveAdapter extends MoveAdapter<Trial, ProbandListEntryTag, ProbandListEntryTagOutVO> {

	private TrialDao trialDao;
	private ProbandListEntryTagDao probandListEntryTagDao;
	private JournalEntryDao journalEntryDao;

	public ProbandListEntryTagMoveAdapter(JournalEntryDao journalEntryDao, ProbandListEntryTagDao probandListEntryTagDao, TrialDao trialDao) {
		super();
		this.journalEntryDao = journalEntryDao;
		this.probandListEntryTagDao = probandListEntryTagDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkItem(ProbandListEntryTag groupItem)
			throws ServiceException {
	}

	@Override
	protected ProbandListEntryTag checkItemId(Long itemId)
			throws ServiceException {
		return CheckIDUtil.checkProbandListEntryTagId(itemId, probandListEntryTagDao);
	}

	@Override
	protected void checkRoot(Trial root) throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
	}

	@Override
	protected void daoUpdate(ProbandListEntryTag item) throws Exception {
		probandListEntryTagDao.update(item);
	}

	@Override
	protected ArrayList<ProbandListEntryTag> getItemsSorted(Long rootId, ProbandListEntryTag item)
			throws Exception {
		return new ArrayList<ProbandListEntryTag>(probandListEntryTagDao.findByTrialExcelEcrfProbandSorted(rootId, null, null, null));
	}

	@Override
	protected long getPosition(ProbandListEntryTag item) {
		return item.getPosition();
	}

	@Override
	protected Trial getRoot(ProbandListEntryTag item) throws ServiceException {
		return item.getTrial();
	}

	@Override
	protected Long getRootId(Trial root) {
		return root.getId();
	}

	@Override
	protected ProbandListEntryTagOutVO logSystemMessage(ProbandListEntryTag item, ProbandListEntryTagOutVO original, Timestamp now, User modifiedUser, PositionMovement movement)
			throws Exception {
		String systemMessageCode;
		ProbandListEntryTagOutVO result = probandListEntryTagDao.toProbandListEntryTagOutVO(item);
		switch (movement) {
			case FIRST:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_MOVED_TO_FIRST_POSITION;
				break;
			case UP:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_MOVED_UP;
				break;
			case DOWN:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_MOVED_DOWN;
				break;
			case LAST:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_MOVED_TO_LAST_POSITION;
				break;
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_POSITION_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_POSITION_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_POSITION_ROTATED_UP;
				break;
			default:
				return result;
		}
		journalEntryDao.addSystemMessage(item.getTrial(), now, modifiedUser, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(result.getTrial()) },

				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
		return result;
	}

	@Override
	protected void logUpdatedPositionsSystemMessage(Trial root, PositionMovement movement, ArrayList<ProbandListEntryTagOutVO> updated,
			Timestamp now, User modifiedUser) throws Exception {
		String systemMessageCode;
		switch (movement) {
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_POSITIONS_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_POSITIONS_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_POSITIONS_ROTATED_UP;
				break;
			default:
				return;
		}
		journalEntryDao.addSystemMessage(root, now, modifiedUser,
				systemMessageCode,
				new Object[] { CommonUtil.trialOutVOToString(trialDao.toTrialOutVO(root)), Integer.toString(updated.size()) },

				new Object[] {
				CoreUtil.getSystemMessageCommentContent(createUpdatedInfo(updated), null, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null))
		});
	}

	public ArrayList<ProbandListEntryTagOutVO> normalizePositions(Long trialId) throws Exception {
		return normalizePositions(null, aquireWriteLock(trialId));
	}

	@Override
	protected void setPosition(ProbandListEntryTag item, long position) {
		item.setPosition(position);
	}

	@Override
	protected ProbandListEntryTagOutVO toVO(ProbandListEntryTag item)
			throws Exception {
		return probandListEntryTagDao.toProbandListEntryTagOutVO(item);
	}

	@Override
	protected String voToString(ProbandListEntryTagOutVO vo) {
		return vo.getUniqueName();
	}
}
