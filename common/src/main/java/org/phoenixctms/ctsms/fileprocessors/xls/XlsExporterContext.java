package org.phoenixctms.ctsms.fileprocessors.xls;

import java.util.HashMap;

import org.phoenixctms.ctsms.vo.AuthenticationVO;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class XlsExporterContext {

	private HashMap<RowWriter, WritableSheet> spreadSheetMap;
	private HashMap<RowWriter, Long> entityIdMap;
	private XlsExporterBase exporter;
	private String fileName;
	private AuthenticationVO auth;
	private WritableWorkbook workbook;

	public XlsExporterContext(XlsExporterBase exporter, String fileName) {
		this.exporter = exporter;
		this.fileName = fileName;
		this.auth = null;
		this.workbook = null;
		spreadSheetMap = new HashMap<RowWriter, WritableSheet>();
		entityIdMap = new HashMap<RowWriter, Long>();
	}

	public AuthenticationVO getAuth() {
		return auth;
	}

	public Long getEntityId(RowWriter writer) {
		return entityIdMap.get(writer);
	}

	public XlsExporterBase getExporter() {
		return exporter;
	}

	public String getFileName() {
		return fileName;
	}

	public WritableSheet getSpreadSheet(RowWriter writer) {
		if (!spreadSheetMap.containsKey(writer)) {
			spreadSheetMap.put(writer, workbook.createSheet(writer.getSheetName(), writer.getSheetNum()));
		}
		return spreadSheetMap.get(writer);
	}

	public WritableWorkbook getWorkbook() {
		return workbook;
	}

	public void setAuth(AuthenticationVO auth) {
		this.auth = auth;
	}

	public void setEntityId(RowWriter writer, Long entityId) {
		entityIdMap.put(writer, entityId);
	}

	public void setWorkbook(WritableWorkbook workbook) {
		this.workbook = workbook;
	}
}
