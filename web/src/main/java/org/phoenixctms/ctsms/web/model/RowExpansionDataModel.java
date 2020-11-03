package org.phoenixctms.ctsms.web.model;
import java.util.List;

public interface RowExpansionDataModel {

	public int getAllRowCount();

	public List<IDVO> getAllRows();

	public List<IDVO> loadAll();

	public void resetRows();
}