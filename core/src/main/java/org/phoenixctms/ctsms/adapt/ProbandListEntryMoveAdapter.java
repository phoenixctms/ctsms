package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
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
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;

public class ProbandListEntryMoveAdapter extends MoveAdapter<Trial, ProbandListEntry, ProbandListEntryOutVO> {

	private TrialDao trialDao;
	// private ProbandDao probandDao;
	private ProbandListEntryDao probandListEntryDao;
	private JournalEntryDao journalEntryDao;
	private static final String ENUMERATED_PROBAND_LIST_ENTRY_NAME = "{0}. {1}";

	public ProbandListEntryMoveAdapter(JournalEntryDao journalEntryDao, ProbandListEntryDao probandListEntryDao, TrialDao trialDao) {
		super();
		this.journalEntryDao = journalEntryDao;
		this.probandListEntryDao = probandListEntryDao;
		this.trialDao = trialDao;
		// this.probandDao = probandDao;
	}

	@Override
	protected Trial aquireWriteLock(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkItem(ProbandListEntry groupItem) throws ServiceException {
		ServiceUtil.checkProbandLocked(groupItem.getProband());
	}

	@Override
	protected ProbandListEntry checkItemId(Long itemId)
			throws ServiceException {
		return CheckIDUtil.checkProbandListEntryId(itemId, probandListEntryDao);
	}

	@Override
	protected void checkRoot(Trial root) throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
	}

	@Override
	protected void daoUpdate(ProbandListEntry item) throws Exception {
		probandListEntryDao.update(item);
	}

	@Override
	protected ArrayList<ProbandListEntry> getItemsSorted(Long rootId, ProbandListEntry item)
			throws Exception {
		return new ArrayList<ProbandListEntry>(probandListEntryDao.findByTrialProbandSorted(rootId, null));
	}

	@Override
	protected long getPosition(ProbandListEntry item) {
		return item.getPosition();
	}

	@Override
	protected Trial getRoot(ProbandListEntry item) throws ServiceException {
		return item.getTrial();
	}

	@Override
	protected Long getRootId(Trial root) {
		return root.getId();
	}

	@Override
	protected ProbandListEntryOutVO logSystemMessage(ProbandListEntry item, ProbandListEntryOutVO original, Timestamp now, User modifiedUser, PositionMovement movement)
			throws Exception {
		String systemMessageCode;
		ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(item);
		switch (movement) {
			case FIRST:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_MOVED_TO_FIRST_POSITION;
				break;
			case UP:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_MOVED_UP;
				break;
			case DOWN:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_MOVED_DOWN;
				break;
			case LAST:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_MOVED_TO_LAST_POSITION;
				break;
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_POSITION_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_POSITION_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_POSITION_ROTATED_UP;
				break;
			default:
				return result;
		}
		journalEntryDao.addSystemMessage(item.getTrial(), now, modifiedUser, systemMessageCode, new Object[] { CommonUtil.probandOutVOToString(result.getProband()) },

				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
		journalEntryDao.addSystemMessage(item.getProband(), now, modifiedUser, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(result.getTrial()) },

				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)) });
		return result;
	}

	@Override
	protected void logUpdatedPositionsSystemMessage(Trial root, PositionMovement movement, ArrayList<ProbandListEntryOutVO> updated,
			Timestamp now, User modifiedUser) throws Exception {
		String systemMessageCode;
		switch (movement) {
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_POSITIONS_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_POSITIONS_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.PROBAND_LIST_ENTRY_POSITIONS_ROTATED_UP;
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

	public ArrayList<ProbandListEntryOutVO> normalizePositions(Long trialId) throws Exception {
		return normalizePositions(null, aquireWriteLock(trialId));
	}

	@Override
	protected void setPosition(ProbandListEntry item, long position) {
		item.setPosition(position);
	}

	@Override
	protected ProbandListEntryOutVO toVO(ProbandListEntry item)
			throws Exception {
		return probandListEntryDao.toProbandListEntryOutVO(item);
	}

	@Override
	protected String voToString(ProbandListEntryOutVO vo) {
		return MessageFormat.format(ENUMERATED_PROBAND_LIST_ENTRY_NAME, Long.toString(vo.getPosition()), vo.getProband().getName());
	}
}
