package org.phoenixctms.ctsms.web.component.datatable;

import org.phoenixctms.ctsms.web.util.WebUtil;

/**
 * Extending PF data table to allow for binding of the filter map.
 */
public class DataTable extends org.primefaces.component.datatable.DataTable {

	@Override
	public java.util.Map<String, String> getFilters() {
		return WebUtil.getSessionScopeBean().getFilterMap(this.getClientId());
	}

	@Override
	public void setFilters(java.util.Map<String, String> filters) {
		WebUtil.getSessionScopeBean().setFilterMap(this.getClientId(), filters);
	}
}