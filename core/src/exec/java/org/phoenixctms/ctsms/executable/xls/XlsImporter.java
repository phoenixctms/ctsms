package org.phoenixctms.ctsms.executable.xls;


import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.domain.AspDao;
import org.phoenixctms.ctsms.domain.AspSubstanceDao;
import org.phoenixctms.ctsms.service.shared.FileService;
import org.phoenixctms.ctsms.util.ChunkedRemoveAll;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class XlsImporter {

	private static Search getRevisionSearch(String revision) {
		return new Search(new SearchParameter[] {
				new SearchParameter("revision", revision, SearchParameter.EQUAL_COMPARATOR) });
	}


	@Autowired
	protected SelectionSetValueRowProcessor selectionSetValueRowProcessor;
	@Autowired
	protected InputFieldRowProcessor inputFieldRowProcessor;
	@Autowired
	protected EcrfFieldRowProcessor ecrfFieldRowProcessor;
	@Autowired
	protected EcrfRowProcessor ecrfRowProcessor;
	@Autowired
	protected AspRowProcessor aspRowProcessor;
	@Autowired
	protected AspDao aspDao;
	@Autowired
	protected AspSubstanceDao aspSubstanceDao;
	@Autowired
	private FileService fileService;
	private JobOutput jobOutput;

	public XlsImporter() {
	}

	private XlsImporterContext createContext(RowProcessor processor, String fileName, boolean mandatory) {// , String sheetName) {
		XlsImporterContext context = new XlsImporterContext(this,fileName);
		// context.setSheetName(processor, sheetName);
		setContext(processor, context, mandatory);
		// processor.setContext(context);
		// processor.setJobOutput(jobOutput);
		return context;
	}

	public EcrfFieldRowProcessor getEcrfFieldRowProcessor() {
		return ecrfFieldRowProcessor;
	}

	public SelectionSetValueRowProcessor getSelectionSetValueRowProcessor() {
		return selectionSetValueRowProcessor;
	}

	public long loadAsps(String fileName,  boolean removeAllBeforeInsert, String revision) throws Throwable {
		XlsImporterContext context = createContext(aspRowProcessor, fileName, true);
		if (CommonUtil.isEmptyString(revision)) {
			revision = ExecUtil.removeExtension((new File(fileName)).getName());
			jobOutput.println("no asp revision specified, using " + revision);
		} else {
			jobOutput.println("using asp revision " + revision);
		}
		aspRowProcessor.setRevision(revision);
		long medicationCount = aspDao.getMedicationCount(revision);
		if (medicationCount > 0) {
			throw new IllegalArgumentException("asps of revision " + revision + " are used by " + medicationCount + " medications");
		}
		medicationCount = aspSubstanceDao.getMedicationCount(revision);
		if (medicationCount > 0) {
			throw new IllegalArgumentException("asp substances of revision " + revision + " are used by medications " + medicationCount + " times");
		}

		if (removeAllBeforeInsert) {
			removeAspRecords(revision);
			jobOutput.println("asp revision " + revision + " cleared");
		}
		return readRows(context, aspRowProcessor);
	}

	protected long loadEcrfFields(XlsImporterContext context) throws Throwable {
		setContext(ecrfFieldRowProcessor, context, true);
		return readRows(context, ecrfFieldRowProcessor);
	}

	public long loadEcrfs(String fileName, AuthenticationVO auth, Long trialId) throws Throwable {
		XlsImporterContext context = createContext(ecrfRowProcessor, fileName, true);
		context.setAuth(auth);
		context.setEntityId(trialId);
		return readRows(context, ecrfRowProcessor);
	}

	public long loadInputFields(String fileName, AuthenticationVO auth) throws Throwable {
		XlsImporterContext context = createContext(inputFieldRowProcessor, fileName, true);
		context.setAuth(auth);
		return readRows(context, inputFieldRowProcessor);
	}

	protected long loadInputFields(XlsImporterContext context) throws Throwable {
		setContext(inputFieldRowProcessor, context, false);
		return readRows(context, inputFieldRowProcessor);
	}

	protected long loadSelectionSetValues(XlsImporterContext context) throws Throwable {
		setContext(selectionSetValueRowProcessor, context, context.isMandatory(inputFieldRowProcessor));
		return readRows(context, selectionSetValueRowProcessor);
	}

	private long readRows(XlsImporterContext context, RowProcessor processor) throws Throwable {
		processor.init();
		jobOutput.println("reading from file " + context.getFileName());
		long rowCount = 0;
		long lineNumber = 1;
		Workbook workbook = null;
		try {
			// Create a workbook object from the file at specified location.
			// Change the path of the file as per the location on your computer.
			InputStream inputStream = ExecUtil.getInputStream(context.getFileName(), context.getAuth(), fileService, jobOutput); // new FileInputStream(context.getFileName());
			WorkbookSettings workbookSettings = processor.getWorkbookSettings();
			if (workbookSettings != null) {
				workbook = Workbook.getWorkbook(inputStream, workbookSettings);
			} else {
				workbook = Workbook.getWorkbook(inputStream);
			}
			Sheet sheet;
			String sheetName = processor.getSheetName();
			if (sheetName != null) {
				sheet = workbook.getSheet(sheetName);
			} else {
				try {
					sheet = workbook.getSheet(processor.getSheetNum());
				} catch (IndexOutOfBoundsException e) {
					sheet = null;
				}
			}
			if (sheet != null) {
				sheetName = sheet.getName();
				if (!CommonUtil.isEmptyString(sheetName)) {
					jobOutput.println("processing sheet '" + sheetName + "'");
				}
				int sheetRowCount = sheet.getRows();
				for (int i = 0; i < sheetRowCount; i++) {
					rowCount += processor.processRow(sheet.getRow(i), lineNumber);
					lineNumber++;
				}
			} else {
				if (context.isMandatory(processor)) {
					throw new IllegalArgumentException("spreadsheet not found");
				} else {
					jobOutput.println("spreadsheet not found, skipping");
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("line " + lineNumber + ": error reading line", e);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		jobOutput.println(rowCount + " rows processed");
		processor.postProcess();
		return rowCount;
	}

	private void removeAspRecords(String revision) throws Exception {
		ChunkedRemoveAll<AspDao> aspRemover = new ChunkedRemoveAll<AspDao>(
				aspDao, getRevisionSearch(revision)) {

			protected Collection<Object> convertPage(Collection<Object> page) {
				return new HashSet(page);
			}

			protected Method getRemoveMethod(Class dao) throws NoSuchMethodException, SecurityException {
				return dao.getMethod("removeTxn", Long.class);
			}

			protected Method getRemovePageMethod(Class dao) throws NoSuchMethodException, SecurityException {
				return dao.getMethod("removeAllTxn", Set.class);
			}

			protected boolean removePageDone(int pageSize, Object removePageResult) {
				jobOutput.println(pageSize + " asp records removed");
				return true;
			}
		};
		aspRemover.remove();
	}

	private void setContext(RowProcessor processor, XlsImporterContext context, boolean mandatory) { // , String sheetName) {
		context.setMandatory(processor, mandatory);
		processor.setContext(context);
		processor.setJobOutput(jobOutput);
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
