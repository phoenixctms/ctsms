package org.phoenixctms.ctsms.test.xls;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.fileprocessors.xls.RowProcessor;
import org.phoenixctms.ctsms.fileprocessors.xls.XlsImporterBase;
import org.phoenixctms.ctsms.fileprocessors.xls.XlsImporterContext;
import org.phoenixctms.ctsms.test.EcrfValidationTestVector;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.springframework.beans.factory.annotation.Autowired;

public class XlsImporter extends XlsImporterBase {

	private AuthenticationVO auth;
	@Autowired
	private EcrfValidationRowProcessor ecrfValidationRowProcessor;

	public XlsImporter() {
	}

	private XlsImporterContext createContext(RowProcessor processor, String fileName, boolean mandatory) {
		XlsImporterContext context = new XlsImporterContext(this, fileName);
		setContext(processor, context, mandatory);
		return context;
	}

	public long loadEcrfValidationVectors(String fileName, Long trialId) throws Throwable {
		XlsImporterContext context = createContext(ecrfValidationRowProcessor, fileName, true);
		context.setEntityId(trialId);
		return readRows(context, ecrfValidationRowProcessor);
	}

	public List<EcrfValidationTestVector> getEcrfValidationTestVectors(String ecrfName, String ecrfRevision) {
		return ecrfValidationRowProcessor.getVectors(ecrfName, ecrfRevision);
	}

	public List<EcrfValidationTestVector> getEcrfValidationTestVectors() {
		return ecrfValidationRowProcessor.getVectors();
	}

	private void setContext(RowProcessor processor, XlsImporterContext context, boolean mandatory) {
		context.setMandatory(processor, mandatory);
		processor.setContext(context);
		processor.setJobOutput(jobOutput);
		context.setAuth(auth);
	}

	protected InputStream getInputStream(String fileName, AuthenticationVO auth) throws AuthenticationException,
			AuthorisationException, ServiceException, FileNotFoundException {
		return new FileInputStream(fileName);
	}

	public void setAuth(AuthenticationVO auth) {
		this.auth = auth;
	}
}