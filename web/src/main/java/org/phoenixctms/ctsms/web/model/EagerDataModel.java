package org.phoenixctms.ctsms.web.model;

import java.util.List;

public interface EagerDataModel {

	public int getAllRowCount();

	public List<IDVO> getAllRows();

	public List<IDVO> loadAll();

	public void resetRows();
}
