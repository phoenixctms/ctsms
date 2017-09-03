package org.phoenixctms.ctsms.adapt;

import java.util.Collection;

import org.hibernate.LockMode;

import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.InventoryTag;
import org.phoenixctms.ctsms.domain.InventoryTagDao;
import org.phoenixctms.ctsms.domain.InventoryTagValue;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.vo.InventoryTagValueInVO;

public class InventoryTagAdapter extends TagAdapter<Inventory, InventoryTag, InventoryTagValue, InventoryTagValueInVO> {

	private InventoryDao inventoryDao;
	private InventoryTagDao inventoryTagDao;

	public InventoryTagAdapter(InventoryDao inventoryDao, InventoryTagDao inventoryTagDao) {
		this.inventoryDao = inventoryDao;
		this.inventoryTagDao = inventoryTagDao;
	}

	@Override
	protected Inventory checkRootId(Long rootId) throws ServiceException {
		return CheckIDUtil.checkInventoryId(rootId, inventoryDao, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected void checkRootTagConstraints(Inventory root, InventoryTag tag, InventoryTagValueInVO tagValueIn)
			throws ServiceException {
	}

	@Override
	protected InventoryTag checkTagId(Long tagId) throws ServiceException {
		return CheckIDUtil.checkInventoryTagId(tagId, inventoryTagDao);
	}

	@Override
	protected boolean checkValueAgainstRegExp(InventoryTagValueInVO tagValueIn) {
		return true;
	}

	@Override
	protected Collection<InventoryTag> findTagsIncludingId(Inventory root, Long tagId) {
		return inventoryTagDao.findByVisibleIdExcel(true, tagId, null);
	}

	@Override
	protected Integer getMaxOccurrence(InventoryTag tag) {
		return tag.getMaxOccurrence();
	}

	@Override
	protected String getMismatchL10nKey(InventoryTag tag) {
		return tag.getMismatchMsgL10nKey();
	}

	@Override
	protected String getNameL10nKey(InventoryTag tag) {
		return tag.getNameL10nKey();
	}

	@Override
	protected String getRegExp(InventoryTag tag) {
		return tag.getRegExp();
	}

	@Override
	protected Long getRootId(InventoryTagValueInVO tagValueIn) {
		return tagValueIn.getInventoryId();
	}

	@Override
	protected InventoryTag getTag(InventoryTagValue tagValue) {
		return tagValue.getTag();
	}

	@Override
	protected Long getTagId(InventoryTagValueInVO tagValueIn) {
		return tagValueIn.getTagId();
	}

	@Override
	protected Collection<InventoryTagValue> getTagValues(Inventory root) {
		return root.getTagValues();
	}

	@Override
	protected String getValue(InventoryTagValueInVO tagValueIn) {
		return tagValueIn.getValue();
	}

	@Override
	protected boolean same(InventoryTagValue tagValue,
			InventoryTagValueInVO tagValueIn) {
		return tagValue.getId().equals(tagValueIn.getId());
	}

	@Override
	protected void toTagVOCollection(Collection<?> tags) {
		inventoryTagDao.toInventoryTagVOCollection(tags);
	}
}
