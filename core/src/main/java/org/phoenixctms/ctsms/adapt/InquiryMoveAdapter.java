package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
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
import org.phoenixctms.ctsms.vo.InquiryOutVO;

public class InquiryMoveAdapter extends MoveAdapter<Trial, Inquiry, InquiryOutVO> {

	private TrialDao trialDao;
	private InquiryDao inquiryDao;
	private JournalEntryDao journalEntryDao;

	public InquiryMoveAdapter(JournalEntryDao journalEntryDao, InquiryDao inquiryDao, TrialDao trialDao) {
		super();
		this.journalEntryDao = journalEntryDao;
		this.inquiryDao = inquiryDao;
		this.trialDao = trialDao;
	}

	@Override
	protected Trial aquireWriteLock(Long rootId) throws ServiceException {
		return CheckIDUtil.checkTrialId(rootId, trialDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkItem(Inquiry groupItem) throws ServiceException {
	}

	@Override
	protected Inquiry checkItemId(Long itemId)
			throws ServiceException {
		return CheckIDUtil.checkInquiryId(itemId, inquiryDao);
	}

	@Override
	protected void checkRoot(Trial root) throws ServiceException {
		ServiceUtil.checkTrialLocked(root);
	}

	@Override
	protected void daoUpdate(Inquiry item) throws Exception {
		inquiryDao.update(item);
	}

	@Override
	protected ArrayList<Inquiry> getItemsSorted(Long rootId, Inquiry item)
			throws Exception {
		return new ArrayList<Inquiry>(inquiryDao.findByTrialCategoryActiveJs(rootId, item.getCategory(), null, null, true, null, null));
	}

	@Override
	protected long getPosition(Inquiry item) {
		return item.getPosition();
	}

	@Override
	protected Trial getRoot(Inquiry item) throws ServiceException {
		return item.getTrial();
	}

	@Override
	protected Long getRootId(Trial root) {
		return root.getId();
	}

	@Override
	protected InquiryOutVO logSystemMessage(Inquiry item, InquiryOutVO original, Timestamp now, User modifiedUser, PositionMovement movement) throws Exception {
		String systemMessageCode;
		InquiryOutVO result = inquiryDao.toInquiryOutVO(item);
		switch (movement) {
			case FIRST:
				systemMessageCode = SystemMessageCodes.INQUIRY_MOVED_TO_FIRST_POSITION;
				break;
			case UP:
				systemMessageCode = SystemMessageCodes.INQUIRY_MOVED_UP;
				break;
			case DOWN:
				systemMessageCode = SystemMessageCodes.INQUIRY_MOVED_DOWN;
				break;
			case LAST:
				systemMessageCode = SystemMessageCodes.INQUIRY_MOVED_TO_LAST_POSITION;
				break;
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.INQUIRY_POSITION_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.INQUIRY_POSITION_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.INQUIRY_POSITION_ROTATED_UP;
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
	protected void logUpdatedPositionsSystemMessage(Trial root, PositionMovement movement, ArrayList<InquiryOutVO> updated,
			Timestamp now, User modifiedUser) throws Exception {
		String systemMessageCode;
		switch (movement) {
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.INQUIRY_POSITIONS_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.INQUIRY_POSITIONS_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.INQUIRY_POSITIONS_ROTATED_UP;
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

	public ArrayList<InquiryOutVO> normalizePositions(Long groupInquiryId) throws Exception {
		Inquiry item = checkItemId(groupInquiryId);
		return normalizePositions(item, aquireWriteLock(getRootId(getRoot(item))));
	}

	@Override
	protected void setPosition(Inquiry item, long position) {
		item.setPosition(position);
	}

	@Override
	protected InquiryOutVO toVO(Inquiry item) throws Exception {
		return inquiryDao.toInquiryOutVO(item);
	}

	@Override
	protected String voToString(InquiryOutVO vo) {
		return vo.getUniqueName();
	}
}
