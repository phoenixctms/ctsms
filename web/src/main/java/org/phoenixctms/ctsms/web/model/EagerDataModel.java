package org.phoenixctms.ctsms.web.model;

import java.util.List;

import org.phoenixctms.ctsms.vo.PSFVO;

public abstract class EagerDataModel<T> {

	protected abstract List<T> load(PSFVO psf);

	public List<T> loadAll() {
		return load(null);
	}

	public void resetRows() {
		allRows = null;
	}

	private List<T> allRows;

	public int getAllRowCount() {
		List<T> rows = getAllRows();
		if (rows != null) {
			return rows.size();
		}
		return 0;
	}

	public List<T> getAllRows() {
		if (allRows == null) {
			allRows = loadAll();
		}
		return allRows;
	}

	public boolean getHasRows() {
		return getAllRowCount() > 0;
	}
}
