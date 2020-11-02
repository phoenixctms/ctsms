package org.phoenixctms.ctsms.web.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public abstract class LazyDataModelBase<T> extends LazyDataModel<IDVO> implements SelectableDataModel<IDVO> {

	private String currentPageIdsString;

	public String getCurrentPageIds() {
		return (currentPageIdsString != null ? currentPageIdsString : "");
	}

	protected Long getPageId(IDVO idvo) {
		return idvo.getId();
	}

	protected abstract Collection<T> getLazyResult(PSFVO psf);

	@Override
	public IDVO getRowData(String key) {
		Object vo = this.getRowElement(WebUtil.stringToLong(key));
		if (vo != null) {
			return new IDVO(vo);
		} else {
			return null;
		}
	}

	protected abstract T getRowElement(Long id);

	@Override
	public String getRowKey(IDVO element) {
		return element.getId().toString();
	}

	@Override
	public List<IDVO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		return load(createPSFVO(first, pageSize, sortField, sortOrder, filters, true));
	}

	@Override
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
}
