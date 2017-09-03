package org.phoenixctms.ctsms.executable.xls;

import java.util.HashMap;

import org.phoenixctms.ctsms.vo.AuthenticationVO;

public class XlsImporterContext {

	private XlsImporter importer;
	private String fileName;
	private AuthenticationVO auth;
	private Long entityId;

	// private HashMap<RowProcessor, String> sheetNameMap;

	private HashMap<RowProcessor, Boolean> mandatoryMap;

	public XlsImporterContext(XlsImporter importer, String fileName) {
		this.importer = importer;
		this.fileName = fileName;
		this.auth = null;
		this.entityId = null;
		mandatoryMap = new HashMap<RowProcessor, Boolean>();
		// sheetIndexMap = new HashMap<RowProcessor, Integer>();
	}

	public AuthenticationVO getAuth() {
		return auth;
	}

	public Long getEntityId() {
		return entityId;
	}

	public String getFileName() {
		return fileName;
	}

	public XlsImporter getImporter() {
		return importer;
	}

	// public String getSheetName(RowProcessor processor) {
	// return sheetNameMap.get(processor);
	// }

	// public void setSheetName(RowProcessor processor, String sheetName) {
	// sheetNameMap.put(processor, sheetName);
	// }
	public boolean isMandatory(RowProcessor processor) {
		if (mandatoryMap.containsKey(processor)) {
			return mandatoryMap.get(processor);
		}
		return false;
	}

	public void setAuth(AuthenticationVO auth) {
		this.auth = auth;
	}



	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setMandatory(RowProcessor processor, boolean mandatory) {
		mandatoryMap.put(processor, mandatory);
	}
}
