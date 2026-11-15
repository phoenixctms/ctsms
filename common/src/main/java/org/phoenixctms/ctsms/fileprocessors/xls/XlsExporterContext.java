package org.phoenixctms.ctsms.fileprocessors.xls;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import org.phoenixctms.ctsms.util.CommonUtil;
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
	private LinkedHashMap<String, byte[]> files;

	public XlsExporterContext(XlsExporterBase exporter, String fileName) {
		this.exporter = exporter;
		this.fileName = fileName;
		this.auth = null;
		this.workbook = null;
		spreadSheetMap = new HashMap<RowWriter, WritableSheet>();
		entityIdMap = new HashMap<RowWriter, Long>();
		files = new LinkedHashMap<String, byte[]>();
	}

	/**
	 * Adds a file to bundle with the spreadsheet in a zip archive (see {@link XlsArchive}). The file is put into a
	 * subfolder named by the MD5 of its content, so it keeps its original name (restored on import), files of same name
	 * but different content do not collide, and a file used by several rows (same content and name) is added only once.
	 *
	 * @return the path of the file within the archive (&lt;folder&gt;/&lt;md5&gt;/&lt;file name&gt;), for rows to reference
	 */
	public String addFile(String folder, String fileName, byte[] data) throws NoSuchAlgorithmException {
		String name = XlsArchive.normalizePath(fileName == null ? "" : fileName);
		name = name.substring(name.lastIndexOf('/') + 1);
		if (name.length() == 0) {
			name = "file";
		}
		String md5 = CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(data != null ? data : new byte[0])).toLowerCase(Locale.ENGLISH);
		String path = folder + "/" + md5 + "/" + name;
		if (!files.containsKey(path)) {
			files.put(path, data);
		}
		return path;
	}

	public LinkedHashMap<String, byte[]> getFiles() {
		return files;
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
