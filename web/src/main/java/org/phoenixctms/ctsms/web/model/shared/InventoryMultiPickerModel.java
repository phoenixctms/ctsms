package org.phoenixctms.ctsms.web.model.shared;

import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.web.model.MultiPickerModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InventoryMultiPickerModel extends MultiPickerModelBase<InventoryOutVO> {

	public InventoryMultiPickerModel() {
		super(null);
	}

	@Override
	protected InventoryOutVO loadSelectionElement(Long id) {
		return WebUtil.getInventory(id, null, null);
	}
}
