package org.phoenixctms.ctsms.executable.xls;

import java.util.HashMap;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.phoenixctms.ctsms.vo.AuthenticationVO;


public class XlsExporterContext {

	private HashMap<RowWriter, WritableSheet> spreadSheetMap;
	private HashMap<RowWriter, Long> entityIdMap;
	private XlsExporter exporter;
	private String fileName;
	private AuthenticationVO auth;
	private WritableWorkbook workbook;

	public XlsExporterContext(XlsExporter exporter, String fileName) {
		this.exporter = exporter;
		this.fileName = fileName;
		this.auth = null;
		this.workbook = null;
		spreadSheetMap = new HashMap<RowWriter, WritableSheet>();
		entityIdMap = new HashMap<RowWriter, Long>();
		// sheetIndexMap = new HashMap<RowProcessor, Integer>();
	}

	public AuthenticationVO getAuth() {
		return auth;
	}

	public Long getEntityId(RowWriter writer) {
		return entityIdMap.get(writer);
	}

	public XlsExporter getExporter() {
		return exporter;
	}

	public String getFileName() {
		return fileName;
	}

	public WritableSheet getSpreadSheet(RowWriter writer) {
		if (!spreadSheetMap.containsKey(writer)) {
			spreadSheetMap.put(writer,  workbook.createSheet(writer.getSheetName(), writer.getSheetNum()));
		}
		return spreadSheetMap.get(writer);
	}

	// public WritableWorkbook getWorkbook(WorkbookSettings workbookSettings) throws Exception {
	// if (workbook == null) {
	// if (workbookSettings != null) {
	// workbook = Workbook.createWorkbook(new File(fileName), workbookSettings);
	// } else {
	// workbook = Workbook.createWorkbook(new File(fileName));
	// }
	// }
	// return workbook;
	// }

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
	// public void setSpreadSheet(RowWriter writer, WritableSheet spreadSheet) {
	// spreadSheetMap.put(writer, spreadSheet);
	// }
	// public void setWorkbook(WritableWorkbook workbook) {
	// this.workbook = workbook;
	// }

}
