package org.phoenixctms.ctsms.web.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.SelectableDataModel;

public abstract class EagerDataModelBase<T> extends EagerDataModel<IDVO> implements SelectableDataModel<IDVO>, RowExpansionDataModel {

	private String currentPageIdsString;

	public String getCurrentPageIds() {
		return (currentPageIdsString != null ? currentPageIdsString : "");
	}

	protected abstract Collection<T> getEagerResult(PSFVO psf);

	protected Long getPageId(IDVO idvo) {
		return idvo.getId();
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

	protected abstract T getRowElement(Long id);

	@Override
	public String getRowKey(IDVO element) {
		return element.getId().toString();
	}

	@Override
	protected List<IDVO> load(PSFVO psf) {
		Collection<T> eagerResult = getEagerResult(psf);
		IDVO.transformVoCollection(eagerResult);
		PSFVO rowPsf;
		if (psf != null) {
			rowPsf = psf;
		} else {
			rowPsf = new PSFVO();
		}
		if (eagerResult != null && eagerResult.size() > 0) {
			StringBuilder sb = new StringBuilder();
			Iterator idIt = eagerResult.iterator();
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
		return (List<IDVO>) eagerResult;
	}
}
