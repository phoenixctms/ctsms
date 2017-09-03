package org.phoenixctms.ctsms.adapt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;

public abstract class MoveAdapter<ROOT, LISTITEM, LISTITEMVO> {

	protected MoveAdapter() {
	}

	protected abstract ROOT aquireWriteLock(Long rootId) throws ServiceException;

	protected abstract void checkItem(LISTITEM groupItem) throws ServiceException;

	protected abstract LISTITEM checkItemId(Long itemId) throws ServiceException;

	protected abstract void checkRoot(ROOT root) throws ServiceException;

	protected Object createUpdatedInfo(final ArrayList<LISTITEMVO> updated) {
		return new Object() {

			public ArrayList<String> getUpdated() {
				ArrayList<String> result = new ArrayList<String>(updated.size());
				Iterator<LISTITEMVO> it = updated.iterator();
				while (it.hasNext()) {
					result.add(voToString(it.next()));
				}
				return result;
			}
		};
	}

	protected abstract void daoUpdate(LISTITEM item) throws Exception;

	protected abstract ArrayList<LISTITEM> getItemsSorted(Long rootId, LISTITEM item) throws Exception;

	protected abstract long getPosition(LISTITEM item);

	protected abstract ROOT getRoot(LISTITEM item) throws ServiceException;

	protected abstract Long getRootId(ROOT root);

	protected abstract LISTITEMVO logSystemMessage(LISTITEM item, LISTITEMVO original, Timestamp now, User modifiedUser, PositionMovement movement) throws Exception;

	protected abstract void logUpdatedPositionsSystemMessage(ROOT root, PositionMovement movement, ArrayList<LISTITEMVO> updated, Timestamp now, User modifiedUser)
			throws Exception;

	public LISTITEMVO move(Long itemId, PositionMovement movement) throws Exception {
		LISTITEM item = checkItemId(itemId);
		checkItem(item);
		ROOT root = aquireWriteLock(getRootId(getRoot(item)));
		checkRoot(root);
		int index = -1;
		ArrayList<LISTITEM> items = getItemsSorted(getRootId(root), item);
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).equals(item)) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MOVE_ITEM_ID_NOT_FOUND);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		long swapPosition;
		LISTITEM swapItem;
		LISTITEMVO originalItem = toVO(item);
		switch (movement) {
			case FIRST:
				if (index > 0) {
					swapPosition = getPosition(items.get(0));
					for (int i = 0; i < index; i++) {
						swapItem = items.get(i);
						checkItem(swapItem);
						setPosition(swapItem, getPosition(items.get(i + 1)));
						ServiceUtil.modifyVersion(swapItem, swapItem, now, user);
						daoUpdate(swapItem);
					}
					setPosition(item, swapPosition);
					ServiceUtil.modifyVersion(item, item, now, user);
					daoUpdate(item);
					return logSystemMessage(item, originalItem, now, user, movement);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ELEMENT_IS_AT_FIRST_POSITION_ALREADY);
				}
			case DOWN:
				if (index > 0) {
					swapItem = items.get(index - 1);
					checkItem(swapItem);
					swapPosition = getPosition(swapItem);
					setPosition(swapItem, getPosition(item));
					setPosition(item, swapPosition);
					ServiceUtil.modifyVersion(swapItem, swapItem, now, user);
					daoUpdate(swapItem);
					ServiceUtil.modifyVersion(item, item, now, user);
					daoUpdate(item);
					return logSystemMessage(item, originalItem, now, user, movement);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ELEMENT_IS_AT_LAST_POSITION_ALREADY);
				}
			case UP:
				if (index < items.size() - 1) {
					swapItem = items.get(index + 1);
					checkItem(swapItem);
					swapPosition = getPosition(swapItem);
					setPosition(swapItem, getPosition(item));
					setPosition(item, swapPosition);
					ServiceUtil.modifyVersion(swapItem, swapItem, now, user);
					daoUpdate(swapItem);
					ServiceUtil.modifyVersion(item, item, now, user);
					daoUpdate(item);
					return logSystemMessage(item, originalItem, now, user, movement);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ELEMENT_IS_AT_FIRST_POSITION_ALREADY);
				}
			case LAST:
				if (index < items.size() - 1) {
					swapPosition = getPosition(items.get(items.size() - 1));
					for (int i = (items.size() - 1); i > index; i--) {
						swapItem = items.get(i);
						checkItem(swapItem);
						setPosition(swapItem, getPosition(items.get(i - 1)));
						ServiceUtil.modifyVersion(swapItem, swapItem, now, user);
						daoUpdate(swapItem);
					}
					setPosition(item, swapPosition);
					ServiceUtil.modifyVersion(item, item, now, user);
					daoUpdate(item);
					return logSystemMessage(item, originalItem, now, user, movement);
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ELEMENT_IS_AT_LAST_POSITION_ALREADY);
				}
			default:
				throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_POSITION_MOVEMENT, movement.name());
		}
	}

	public ArrayList<LISTITEMVO> moveTo(Long itemId, long targetPosition) throws Exception {
		LISTITEM item = checkItemId(itemId);
		checkItem(item);
		ROOT root = aquireWriteLock(getRootId(getRoot(item)));
		checkRoot(root);
		int sourceIndex = -1;
		int minTargetIndex = 0;
		ArrayList<LISTITEM> items = getItemsSorted(getRootId(root), item);
		int maxTargetIndex = items.size() - 1;
		ArrayList<LISTITEMVO> updated = new ArrayList<LISTITEMVO>(items.size());
		boolean minTargetSet = false;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).equals(item)) {
				sourceIndex = i;
			}
			if (targetPosition == getPosition(items.get(i))
					|| (
							(
									i == (items.size() - 1)
									|| (i < (items.size() - 1) && targetPosition < getPosition(items.get(i + 1)))
									)
									&&
									(
											i == 0
											|| (i > 0 && targetPosition > getPosition(items.get(i - 1)))
											)
							)) {
				if (!minTargetSet) {
					minTargetIndex = i;
					minTargetSet = true;
				}
				maxTargetIndex = i;
			}
			// if (targetPosition == getPosition(items.get(i))) {
			// targetIndex = i;
			// } else if (targetPosition > getPosition(items.get(i))) {
			// if (i < (items.size() - 1) && targetPosition > getPosition(items.get(i + 1))) {
			// targetIndex = i + 1;
			// } else {
			// targetIndex = i;
			// }
			// }
		}
		if (sourceIndex < 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MOVE_ITEM_ID_NOT_FOUND);
		} else {
			LISTITEMVO originalItem = toVO(item);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			User user = CoreUtil.getUser();
			int targetIndex = (targetPosition > getPosition(item) ? minTargetIndex : maxTargetIndex);
			if (sourceIndex == targetIndex) {
				if (targetPosition != getPosition(item)) {
					setPosition(item, targetPosition);
					ServiceUtil.modifyVersion(item, item, now, user);
					daoUpdate(item);
					PositionMovement movement = getPosition(item) > targetPosition ? PositionMovement.ROTATE_DOWN : PositionMovement.ROTATE_UP;
					updated.add(logSystemMessage(item, originalItem, now, user, movement));
					logUpdatedPositionsSystemMessage(root, movement, updated, now, user);
				}
				return updated;
			} else {
				long swapPosition;
				LISTITEM swapItem;
				LISTITEMVO originalSwapItem;
				if (sourceIndex > targetIndex) {
					if (targetPosition < getPosition(items.get(0))) {
						setPosition(item, targetPosition);
						ServiceUtil.modifyVersion(item, item, now, user);
						daoUpdate(item);
						updated.add(logSystemMessage(item, originalItem, now, user, PositionMovement.ROTATE_DOWN));
						logUpdatedPositionsSystemMessage(root, PositionMovement.ROTATE_DOWN, updated, now, user);
					} else {
						for (int i = sourceIndex; i > targetIndex; i--) {
							swapItem = items.get(i - 1);
							checkItem(swapItem);
							originalSwapItem = toVO(swapItem);
							swapPosition = getPosition(swapItem);
							setPosition(swapItem, getPosition(item));
							setPosition(item, swapPosition);
							ServiceUtil.modifyVersion(swapItem, swapItem, now, user);
							daoUpdate(swapItem);
							updated.add(logSystemMessage(swapItem, originalSwapItem, now, user, PositionMovement.ROTATE_DOWN));
						}
						setPosition(item, targetPosition);
						ServiceUtil.modifyVersion(item, item, now, user);
						daoUpdate(item);
						updated.add(logSystemMessage(item, originalItem, now, user, PositionMovement.ROTATE_DOWN));
						logUpdatedPositionsSystemMessage(root, PositionMovement.ROTATE_DOWN, updated, now, user);
					}
				} else if (sourceIndex < targetIndex) {
					if (targetPosition > getPosition(items.get(items.size() - 1))) {
						setPosition(item, targetPosition);
						ServiceUtil.modifyVersion(item, item, now, user);
						daoUpdate(item);
						updated.add(logSystemMessage(item, originalItem, now, user, PositionMovement.ROTATE_UP));
						logUpdatedPositionsSystemMessage(root, PositionMovement.ROTATE_UP, updated, now, user);
					} else {
						for (int i = sourceIndex; i < targetIndex; i++) {
							swapItem = items.get(i + 1);
							checkItem(swapItem);
							originalSwapItem = toVO(swapItem);
							swapPosition = getPosition(swapItem);
							setPosition(swapItem, getPosition(item));
							setPosition(item, swapPosition);
							ServiceUtil.modifyVersion(swapItem, swapItem, now, user);
							daoUpdate(swapItem);
							updated.add(logSystemMessage(swapItem, originalSwapItem, now, user, PositionMovement.ROTATE_UP));
						}
						setPosition(item, targetPosition);
						ServiceUtil.modifyVersion(item, item, now, user);
						daoUpdate(item);
						updated.add(logSystemMessage(item, originalItem, now, user, PositionMovement.ROTATE_UP));
						logUpdatedPositionsSystemMessage(root, PositionMovement.ROTATE_UP, updated, now, user);
					}
				}
				return updated;
			}
		}

	}

	protected ArrayList<LISTITEMVO> normalizePositions(LISTITEM groupItem, ROOT root) throws Exception {
		checkRoot(root);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		long position = CommonUtil.LIST_INITIAL_POSITION;
		ArrayList<LISTITEM> items = getItemsSorted(getRootId(root), groupItem);
		ArrayList<LISTITEMVO> updated = new ArrayList<LISTITEMVO>(items.size());
		Iterator<LISTITEM> it = items.iterator();
		while (it.hasNext()) {
			LISTITEM item = it.next();
			if (getPosition(item) != position) {
				checkItem(item);
				LISTITEMVO original = toVO(item);
				setPosition(item, position);
				ServiceUtil.modifyVersion(item, item, now, user);
				daoUpdate(item);
				updated.add(logSystemMessage(item, original, now, user, PositionMovement.NORMALIZE));
			}
			position += 1L;
		}
		if (updated.size() > 0) {
			logUpdatedPositionsSystemMessage(root, PositionMovement.NORMALIZE, updated, now, user);
		}
		return updated;
	}

	protected abstract void setPosition(LISTITEM item, long position);

	protected abstract LISTITEMVO toVO(LISTITEM item) throws Exception;

	protected abstract String voToString(LISTITEMVO vo);
}
