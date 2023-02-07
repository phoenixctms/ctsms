package org.phoenixctms.ctsms.fileprocessors.xls;

import java.util.HashMap;

import org.phoenixctms.ctsms.vo.AuthenticationVO;

public class XlsImporterContext {

	private XlsImporterBase importer;
	private String fileName;
	private AuthenticationVO auth;
	private Long entityId;
	private HashMap<RowProcessor, Boolean> mandatoryMap;

	public XlsImporterContext(XlsImporterBase importer, String fileName) {
		this.importer = importer;
		this.fileName = fileName;
		this.auth = null;
		this.entityId = null;
		mandatoryMap = new HashMap<RowProcessor, Boolean>();
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

	public XlsImporterBase getImporter() {
		return importer;
	}

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
