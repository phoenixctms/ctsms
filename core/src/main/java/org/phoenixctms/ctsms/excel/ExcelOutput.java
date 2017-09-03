package org.phoenixctms.ctsms.excel;

import java.io.OutputStream;

public interface ExcelOutput {

	public OutputStream getOutputStream();

	public boolean save() throws Exception;
}
