package org.phoenixctms.ctsms.executable.xls;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.fileprocessors.xls.RowWriter;
import org.phoenixctms.ctsms.fileprocessors.xls.XlsExporterBase;
import org.phoenixctms.ctsms.fileprocessors.xls.XlsExporterContext;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class XlsExporter extends XlsExporterBase {

	@Autowired
	protected SelectionSetValueRowWriter selectionSetValueRowWriter;
	@Autowired
	protected InputFieldRowWriter inputFieldRowWriter;
	@Autowired
	protected EcrfFieldRowWriter ecrfFieldRowWriter;
	@Autowired
	protected EcrfRowWriter ecrfRowWriter;
	@Autowired
	protected InquiryRowWriter inquiryRowWriter;
	@Autowired
	protected ProbandListEntryTagRowWriter probandListEntryTagRowWriter;

	private final L10nKeyCollector l10nKeyCollector = new L10nKeyCollector();

	public XlsExporter() {
	}

	public L10nKeyCollector getL10nKeyCollector() {
		return l10nKeyCollector;
	}

	/**
	 * One sheet of localization texts per bundle used by the exported rows.
	 */
	@Override
	protected void printAdditionalSheets(XlsExporterContext context) throws Throwable {
		Iterator<Entry<InputFieldL10nBundle, TreeSet<String>>> it = l10nKeyCollector.getKeys().entrySet().iterator();
		while (it.hasNext()) {
			Entry<InputFieldL10nBundle, TreeSet<String>> bundleKeys = it.next();
			L10nTextRowWriter writer = new L10nTextRowWriter(bundleKeys.getKey(), bundleKeys.getValue());
			setContext(writer, context);
			writer.init();
			writer.printHeaderRow();
			writer.printRows();
			jobOutput.println(writer.getSheetName() + ": " + bundleKeys.getValue().size() + " keys exported");
		}
		l10nKeyCollector.clear();
	}

	public long exportEcrfs(String fileName, String encoding, AuthenticationVO auth, Long trialId) throws Throwable {
		XlsExporterContext context = new XlsExporterContext(this, fileName);
		l10nKeyCollector.clear();
		setContext(ecrfRowWriter, context);
		setContext(ecrfFieldRowWriter, context);
		setContext(inputFieldRowWriter, context);
		setContext(selectionSetValueRowWriter, context);
		context.setEntityId(ecrfRowWriter, trialId);
		context.setAuth(auth);
		return printRows(context, encoding, ecrfRowWriter);
	}

	public long exportInquiries(String fileName, String encoding, AuthenticationVO auth, Long trialId) throws Throwable {
		XlsExporterContext context = new XlsExporterContext(this, fileName);
		l10nKeyCollector.clear();
		setContext(inquiryRowWriter, context);
		setContext(inputFieldRowWriter, context);
		setContext(selectionSetValueRowWriter, context);
		context.setEntityId(inquiryRowWriter, trialId);
		context.setAuth(auth);
		return printRows(context, encoding, inquiryRowWriter);
	}

	public long exportProbandListEntryTags(String fileName, String encoding, AuthenticationVO auth, Long trialId) throws Throwable {
		XlsExporterContext context = new XlsExporterContext(this, fileName);
		l10nKeyCollector.clear();
		setContext(probandListEntryTagRowWriter, context);
		setContext(inputFieldRowWriter, context);
		setContext(selectionSetValueRowWriter, context);
		context.setEntityId(probandListEntryTagRowWriter, trialId);
		context.setAuth(auth);
		return printRows(context, encoding, probandListEntryTagRowWriter);
	}

	public long exportInputField(String fileName, String encoding, AuthenticationVO auth, Long inputFieldId) throws Throwable {
		XlsExporterContext context = new XlsExporterContext(this, fileName);
		l10nKeyCollector.clear();
		setContext(inputFieldRowWriter, context);
		setContext(selectionSetValueRowWriter, context);
		context.setEntityId(inputFieldRowWriter, inputFieldId);
		context.setAuth(auth);
		return printRows(context, encoding, inputFieldRowWriter);
	}

	public EcrfFieldRowWriter getEcrfFieldRowWriter() {
		return ecrfFieldRowWriter;
	}

	public InputFieldRowWriter getInputFieldRowWriter() {
		return inputFieldRowWriter;
	}

	public SelectionSetValueRowWriter getSelectionSetValueRowWriter() {
		return selectionSetValueRowWriter;
	}

	protected void addEmailXlsAttachment(byte[] data) {
		((JobOutput) jobOutput).addEmailXlsAttachment(data);
	}

	@Override
	protected void addEmailZipAttachment(byte[] data) {
		((JobOutput) jobOutput).addEmailZipAttachment(data);
	}

	@Override
	protected String getJobXlsFileName() {
		String fileName = ((JobOutput) jobOutput).getJobXlsFileName();
		return fileName != null ? fileName : super.getJobXlsFileName();
	}

	private void setContext(RowWriter writer, XlsExporterContext context) {
		writer.setContext(context);
		writer.setJobOutput(jobOutput);
	}
}
