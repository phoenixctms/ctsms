package org.phoenixctms.ctsms.web.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public abstract class LazyDataModelBase extends LazyDataModel<IDVO> implements SelectableDataModel<IDVO> {

	private String currentPageIdsString;

	public String getCurrentPageIds() {
		return (currentPageIdsString != null ? currentPageIdsString : "");
	}

	protected abstract Collection<?> getLazyResult(PSFVO psf);

	protected Long getPageId(IDVO idvo) {
		return idvo.getId();
	}

	@Override
	public IDVO getRowData() {
		// http://primefaces.prime.com.tr/forum/viewtopic.php?f=3&t=11353
		if (isRowAvailable()) {
			return super.getRowData();
		} else {
			return null;
		}
	}

	@Override
	public IDVO getRowData(String key) {
		Object vo = this.getRowElement(WebUtil.stringToLong(key));
		if (vo != null) {
			return new IDVO(vo);
		} else {
			return null;
		}
	}

	protected abstract <T> T getRowElement(Long id);

	@Override
	public String getRowKey(IDVO element) {
		return element.getId().toString();
	}

	@Override
	public List<IDVO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		PSFVO psf = new PSFVO();
		psf.setFirst(first);
		psf.setPageSize(pageSize);
		if (sortField != null && sortField.length() > 0) {
			AssociationPath sortFieldAssociationPath = new AssociationPath(sortField);
			sortFieldAssociationPath.dropFirstPathElement();
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
				filterFieldAssociationPath.dropFirstPathElement();
				preparedFilters.put(filterFieldAssociationPath.getFullQualifiedPropertyName(), filter.getValue());
			}
		} else {
			preparedFilters = new HashMap<String, String>();
		}
		psf.setFilters(preparedFilters);
		return load(psf);
	}

	protected List<IDVO> load(PSFVO psf) {
		Collection<?> lazyResult = getLazyResult(psf);
		IDVO.transformVoCollection(lazyResult);
		PSFVO rowPsf;
		if (psf != null) {
			rowPsf = psf;
		} else {
			rowPsf = new PSFVO();
		}
		if (lazyResult != null && lazyResult.size() > 0) {
			StringBuilder sb = new StringBuilder();
			Iterator idIt = lazyResult.iterator();
			int rowIndex = 0;
			while (idIt.hasNext()) {
				Object item = idIt.next();
				if (item != null && item instanceof IDVO) {
					IDVO idvo = (IDVO) item;
					idvo.setRowIndex(WebUtil.FACES_INITIAL_ROW_INDEX + rowIndex);
					idvo.setPsf(rowPsf);
					rowIndex++;
					if (idvo.getId() != null) {
						if (sb.length() > 0) {
							sb.append(WebUtil.ID_SEPARATOR_STRING);
						}
						sb.append(getPageId(idvo).toString());
					}
				}
			}
			currentPageIdsString = sb.toString();
		} else {
			currentPageIdsString = null;
		}
		if (psf != null && psf.getUpdateRowCount()) {
			updateRowCount(psf);
		}
		return (List<IDVO>) lazyResult;
	}

	public List<IDVO> loadAll() {
		return load(null);
	}

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
}
