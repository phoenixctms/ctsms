package org.phoenixctms.ctsms.executable.xls;

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

	public XlsExporter() {
	}

	public long exportEcrfs(String fileName, AuthenticationVO auth, Long trialId) throws Throwable {
		XlsExporterContext context = new XlsExporterContext(this, fileName);
		setContext(ecrfRowWriter, context);
		setContext(ecrfFieldRowWriter, context);
		setContext(inputFieldRowWriter, context);
		setContext(selectionSetValueRowWriter, context);
		context.setEntityId(ecrfRowWriter, trialId);
		context.setAuth(auth);
		return printRows(context, ecrfRowWriter);
	}

	public long exportInputField(String fileName, AuthenticationVO auth, Long inputFieldId) throws Throwable {
		XlsExporterContext context = new XlsExporterContext(this, fileName);
		setContext(inputFieldRowWriter, context);
		setContext(selectionSetValueRowWriter, context);
		context.setEntityId(inputFieldRowWriter, inputFieldId);
		context.setAuth(auth);
		return printRows(context, inputFieldRowWriter);
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

	private void setContext(RowWriter writer, XlsExporterContext context) {
		writer.setContext(context);
		writer.setJobOutput(jobOutput);
	}
}
