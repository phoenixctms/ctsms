package org.phoenixctms.ctsms.executable.csv;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.domain.AlphaIdDao;
import org.phoenixctms.ctsms.domain.BankIdentificationDao;
import org.phoenixctms.ctsms.domain.CountryDao;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.MimeTypeDao;
import org.phoenixctms.ctsms.domain.OpsCodeDao;
import org.phoenixctms.ctsms.domain.PermissionDao;
import org.phoenixctms.ctsms.domain.ProfilePermissionDao;
import org.phoenixctms.ctsms.domain.StreetDao;
import org.phoenixctms.ctsms.domain.TitleDao;
import org.phoenixctms.ctsms.domain.ZipDao;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.TableSizes;
import org.phoenixctms.ctsms.util.ChunkedRemoveAll;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.springframework.beans.factory.annotation.Autowired;

public class CsvImporter {

	private  static Search getRevisionSearch(String revision) {
		return new Search(new SearchParameter[] {
				new SearchParameter("revision", revision, SearchParameter.EQUAL_COMPARATOR) });
	}
	@Autowired
	protected MimeTypeDao mimeTypeDao;
	@Autowired
	protected TitleDao titleDao;
	@Autowired
	protected CountryDao countryDao;
	@Autowired
	protected ZipDao zipDao;
	@Autowired
	protected BankIdentificationDao bankIdentificationDao;
	@Autowired
	protected StreetDao streetDao;
	@Autowired
	protected AlphaIdDao alphaIdDao;
	@Autowired
	protected OpsCodeDao opsCodeDao;
	@Autowired
	protected CountryLineProcessor countryProcessor;
	@Autowired
	protected MimeTypeLineProcessor mimeTypeProcessor;
	@Autowired
	protected ZipLineProcessor zipProcessor;
	@Autowired
	protected TitleLineProcessor titleProcessor;
	@Autowired
	protected BankIdentificationLineProcessor bankIdentificationProcessor;
	@Autowired
	protected StreetLineProcessor streetProcessor;
	@Autowired
	protected PermissionDefinitionLineProcessor permissionDefinitionLineProcessor;
	@Autowired
	protected ProfilePermissionDao profilePermissionDao;
	@Autowired
	protected CriterionPropertyDao criterionPropertyDao;
	@Autowired
	protected CriterionPropertyLineProcessor criterionPropertyLineProcessor;
	@Autowired
	protected AlphaIdLineProcessor alphaIdLineProcessor;
	@Autowired
	protected OpsCodeLineProcessor opsCodeLineProcessor;
	private JobOutput jobOutput;

	@Autowired
	protected PermissionDao permissionDao;

	public CsvImporter() {
	}

	public long loadAlphaIds(String fileName, String encoding, boolean removeAllBeforeInsert, String revision, String icdSystRevision) throws Exception {

		alphaIdLineProcessor.setJobOutput(jobOutput);
		if (CommonUtil.isEmptyString(revision)) {
			revision = ExecUtil.removeExtension((new File(fileName)).getName());
			jobOutput.println("no alpha id revision specified, using " + revision);
		} else {
			jobOutput.println("using alpha id revision " + revision);
		}
		alphaIdLineProcessor.setRevision(revision);
		long diagnosisCount = alphaIdDao.getDiagnosisCount(revision);
		if (diagnosisCount > 0) {
			throw new IllegalArgumentException("alpha ids of revision " + revision + " are used by " + diagnosisCount + " diagnoses");
		}
		if (!CommonUtil.isEmptyString(icdSystRevision)) {
			jobOutput.println("using icd systematics revision " + icdSystRevision);
		} else {
			// icdSystRevision = Settings.getString(SettingCodes.ICD_SYSTEMATICS_REVISION, Bundle.SETTINGS, DefaultSettings.ICD_SYSTEMATICS_REVISION);
			// jobOutput.println("no icd systematics revision specified, using " + icdSystRevision);
			throw new IllegalArgumentException("no icd systematics revision specified");
		}
		alphaIdLineProcessor.setIcdSystRevision(icdSystRevision);
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(alphaIdDao, getRevisionSearch(revision));
			jobOutput.println("alpha ids revision " + revision + " cleared");
		}
		return readLines(fileName, encoding, alphaIdLineProcessor);
	}

	public long loadBankIdentifications(String fileName, String encoding, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(bankIdentificationDao);
			jobOutput.println("bank identification suggestions cleared");
		}
		bankIdentificationProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, bankIdentificationProcessor);
	}

	public long loadCountries(String fileName, String encoding, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(countryDao);
			jobOutput.println("country name autocomplete suggestions cleared");
		}
		countryProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, countryProcessor);
	}

	public long loadCriterionProperties(String fileName, String encoding, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(criterionPropertyDao);
			jobOutput.println("criterion property definitions cleared");
		}
		criterionPropertyLineProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, criterionPropertyLineProcessor);
	}

	public long loadMimeTypes(String fileName, String encoding, FileModule module, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			mimeTypeDao.remove(mimeTypeDao.findByMimeTypeModule(null, module)); // .loadAll());
			jobOutput.println("mime types for module " + module + " cleared");
		}
		mimeTypeProcessor.setFieldSeparatorRegexpPattern("\\t+");
		mimeTypeProcessor.setModule(module);
		mimeTypeProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, mimeTypeProcessor);
	}

	public long loadOpsCodes(String fileName, String encoding, boolean removeAllBeforeInsert, String revision, String opsSystRevision) throws Exception {
		opsCodeLineProcessor.setJobOutput(jobOutput);
		if (CommonUtil.isEmptyString(revision)) {
			revision = ExecUtil.removeExtension((new File(fileName)).getName());
			jobOutput.println("no ops code revision specified, using " + revision);
		} else {
			jobOutput.println("using ops code revision " + revision);
		}
		opsCodeLineProcessor.setRevision(revision);
		long procedureCount = opsCodeDao.getProcedureCount(revision);
		if (procedureCount > 0) {
			throw new IllegalArgumentException("ops codes of revision " + revision + " are used by " + procedureCount + " procedures");
		}
		if (!CommonUtil.isEmptyString(opsSystRevision)) {
			jobOutput.println("using ops systematics revision " + opsSystRevision);
		} else {
			// opsSystRevision = Settings.getString(SettingCodes.OPS_SYSTEMATICS_REVISION, Bundle.SETTINGS, DefaultSettings.OPS_SYSTEMATICS_REVISION);
			// jobOutput.println("no ops systematics revision specified, using " + opsSystRevision);
			throw new IllegalArgumentException("no ops systematics revision specified");
		}
		opsCodeLineProcessor.setOpsSystRevision(opsSystRevision);
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(opsCodeDao, getRevisionSearch(revision));
			jobOutput.println("ops codes revision " + revision + " cleared");
		}
		return readLines(fileName, encoding, opsCodeLineProcessor);
	}

	public long loadPermissionDefinitions(String fileName, String encoding, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(profilePermissionDao);
			ChunkedRemoveAll.remove(permissionDao);
			jobOutput.println("permission definitions cleared");
		}
		permissionDefinitionLineProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, permissionDefinitionLineProcessor);
	}

	public long loadStreets(String fileName, String encoding, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(streetDao, TableSizes.BIG);
			jobOutput.println("street name autocomplete suggestions cleared");
		}
		streetProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, streetProcessor);
	}

	public long loadTitles(String fileName, String encoding, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(titleDao);
			jobOutput.println("title autocomplete suggestions cleared");
		}
		titleProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, titleProcessor);
	}

	public long loadZips(String fileName, String encoding, boolean removeAllBeforeInsert) throws Exception {
		if (removeAllBeforeInsert) {
			ChunkedRemoveAll.remove(zipDao);
			jobOutput.println("zipcode/city name autocomplete suggestions cleared");
		}
		zipProcessor.setJobOutput(jobOutput);
		return readLines(fileName, encoding, zipProcessor);
	}

	private long readLines(String fileName, String encoding, LineProcessor processor) throws Exception {

		Scanner scanner = new Scanner(new FileInputStream(fileName), ExecUtil.sanitizeEncoding(encoding, jobOutput));
		scanner.useDelimiter(processor.getLineTerminatorRegexpPattern());
		String line = null;
		processor.init();
		jobOutput.println("reading from file " + fileName);
		long rowCount = 0;
		long lineNumber = 1;
		try {
			while (scanner.hasNext()) { // .hasNextLine()) {
				line = scanner.next(); // scanner.nextLine();
				rowCount += processor.processLine(line, lineNumber);
				lineNumber++;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("line " + lineNumber + ": error reading line", e);
		} finally {
			scanner.close();
		}
		jobOutput.println(rowCount + " rows processed");
		processor.postProcess();

		return rowCount;
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
