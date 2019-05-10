package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFField;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
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
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;

public class EcrfFieldMoveAdapter extends MoveAdapter<ECRF, ECRFField, ECRFFieldOutVO> {

	private ECRFDao ecrfDao;
	private TrialDao trialDao;
	private ECRFFieldDao ecrfFieldDao;
	private JournalEntryDao journalEntryDao;

	public EcrfFieldMoveAdapter(JournalEntryDao journalEntryDao, ECRFFieldDao ecrfFieldDao, ECRFDao ecrfDao, TrialDao trialDao) {
		super();
		this.journalEntryDao = journalEntryDao;
		this.ecrfFieldDao = ecrfFieldDao;
		this.ecrfDao = ecrfDao;
		this.trialDao = trialDao;
	}

	@Override
	protected ECRF aquireWriteLock(Long rootId) throws ServiceException {
		return CheckIDUtil.checkEcrfId(rootId, ecrfDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkItem(ECRFField groupItem) throws ServiceException {
	}

	@Override
	protected ECRFField checkItemId(Long itemId)
			throws ServiceException {
		return CheckIDUtil.checkEcrfFieldId(itemId, ecrfFieldDao);
	}

	@Override
	protected void checkRoot(ECRF root) throws ServiceException {
		ServiceUtil.checkTrialLocked(root.getTrial());
	}

	@Override
	protected void daoUpdate(ECRFField item) throws Exception {
		ecrfFieldDao.update(item);
	}

	@Override
	protected ArrayList<ECRFField> getItemsSorted(Long rootId, ECRFField item)
			throws Exception {
		return new ArrayList<ECRFField>(ecrfFieldDao.findByTrialEcrfSectionSeriesJs(null, rootId, item.getSection(), true, null, null, null));
	}

	@Override
	protected long getPosition(ECRFField item) {
		return item.getPosition();
	}

	@Override
	protected ECRF getRoot(ECRFField item) throws ServiceException {
		return item.getEcrf();
	}

	@Override
	protected Long getRootId(ECRF root) {
		return root.getId();
	}

	@Override
	protected ECRFFieldOutVO logSystemMessage(ECRFField item, ECRFFieldOutVO original, Timestamp now, User modifiedUser, PositionMovement movement) throws Exception {
		String systemMessageCode;
		ECRFFieldOutVO result = ecrfFieldDao.toECRFFieldOutVO(item);
		switch (movement) {
			case FIRST:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_MOVED_TO_FIRST_POSITION;
				break;
			case UP:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_MOVED_UP;
				break;
			case DOWN:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_MOVED_DOWN;
				break;
			case LAST:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_MOVED_TO_LAST_POSITION;
				break;
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_POSITION_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_POSITION_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_POSITION_ROTATED_UP;
				break;
			default:
				return result;
		}
		journalEntryDao.addSystemMessage(item.getTrial(), now, modifiedUser, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(result.getTrial()) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
		return result;
	}

	@Override
	protected void logUpdatedPositionsSystemMessage(ECRF root, PositionMovement movement, ArrayList<ECRFFieldOutVO> updated,
			Timestamp now, User modifiedUser) throws Exception {
		String systemMessageCode;
		switch (movement) {
			case NORMALIZE:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_POSITIONS_NORMALIZED;
				break;
			case ROTATE_DOWN:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_POSITIONS_ROTATED_DOWN;
				break;
			case ROTATE_UP:
				systemMessageCode = SystemMessageCodes.ECRF_FIELD_POSITIONS_ROTATED_UP;
				break;
			default:
				return;
		}
		journalEntryDao.addSystemMessage(root.getTrial(), now, modifiedUser,
				systemMessageCode,
				new Object[] { CommonUtil.trialOutVOToString(trialDao.toTrialOutVO(root.getTrial())), Integer.toString(updated.size()) },
				new Object[] {
						CoreUtil.getSystemMessageCommentContent(createUpdatedInfo(updated), null, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null))
				});
	}

	public ArrayList<ECRFFieldOutVO> normalizePositions(Long groupEcrfFieldId) throws Exception {
		ECRFField item = checkItemId(groupEcrfFieldId);
		return normalizePositions(item, aquireWriteLock(getRootId(getRoot(item))));
	}

	@Override
	protected void setPosition(ECRFField item, long position) {
		item.setPosition(position);
	}

	@Override
	protected ECRFFieldOutVO toVO(ECRFField item) throws Exception {
		return ecrfFieldDao.toECRFFieldOutVO(item);
	}

	@Override
	protected String voToString(ECRFFieldOutVO vo) {
		return vo.getUniqueName();
	}
}
