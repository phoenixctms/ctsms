package org.phoenixctms.ctsms.web.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.SortOrder;

public abstract class LazyDataModel<T> extends org.primefaces.model.LazyDataModel<T> {

	private int page = 0;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isStorePage() {
		return true; //store page by default
	}

	public void handlePageChanged(PageEvent event) {
		page = event.getPage();
	}

	protected abstract List<T> load(PSFVO psf);

	protected abstract Collection getLazyResult(PSFVO psf);

	public void updateRowCount() {
		PSFVO psf = new PSFVO();
		psf.setPageSize(0);
		getLazyResult(psf);
		updateRowCount(psf);
	}

	protected void updateRowCount(PSFVO psf) {
		int rowCount = 0;
		Long psfRowCount;
		if (psf != null && (psfRowCount = psf.getRowCount()) != null) {
			rowCount = CommonUtil.safeLongToInt(psfRowCount);
		}
		setRowCount(rowCount);
	}

	@Override
	public void setRowIndex(int rowIndex) {
		/*
		 * the following is a workaround to avoid div by zero in case of pageSize=0
		 * (possible bug in pf)
		 * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
		 */
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else {
			super.setRowIndex(rowIndex);
		}
	}

	@Override
	public T getRowData() {
		// http://primefaces.prime.com.tr/forum/viewtopic.php?f=3&t=11353
		if (isRowAvailable()) {
			return super.getRowData();
		} else {
			return null;
		}
	}

	protected static PSFVO createPSFVO(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters, boolean dropFirstPathElement) {
		PSFVO psf = new PSFVO();
		psf.setFirst(first);
		psf.setPageSize(pageSize);
		if (sortField != null && sortField.length() > 0) {
			AssociationPath sortFieldAssociationPath = new AssociationPath(sortField);
			if (dropFirstPathElement) {
				sortFieldAssociationPath.dropFirstPathElement();
			}
			switch (sortOrder) {
				case ASCENDING:
					psf.setSortField(sortFieldAssociationPath.getFullQualifiedPropertyName());
					psf.setSortOrder(true);
					break;
				case DESCENDING:
					psf.setSortField(sortFieldAssociationPath.getFullQualifiedPropertyName());
					psf.setSortOrder(false);
					break;
				case UNSORTED:
					psf.setSortField(null);
					break;
				default:
					psf.setSortField(null);
					break;
			}
		} else {
			psf.setSortField(null);
		}
		Map<String, String> preparedFilters;
		if (filters != null && filters.size() > 0) {
			preparedFilters = new HashMap<String, String>(filters.size());
			Iterator<Map.Entry<String, String>> filterIt = filters.entrySet().iterator();
			while (filterIt.hasNext()) {
				Map.Entry<String, String> filter = filterIt.next();
				AssociationPath filterFieldAssociationPath = new AssociationPath(filter.getKey());
				if (dropFirstPathElement) {
					filterFieldAssociationPath.dropFirstPathElement();
				}
				preparedFilters.put(filterFieldAssociationPath.getFullQualifiedPropertyName(), filter.getValue());
			}
		} else {
			preparedFilters = new HashMap<String, String>();
		}
		psf.setFilters(preparedFilters);
		if (Settings.getBoolean(SettingCodes.FILTER_USER_TIME_ZONE, Bundle.SETTINGS, DefaultSettings.FILTER_USER_TIME_ZONE)) {
			psf.setFilterTimeZone(WebUtil.getTimeZone().getID());
		}
		return psf;
	}

	public List<T> loadAll() {
		return load(null);
	}
}
