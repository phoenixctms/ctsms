package org.phoenixctms.ctsms.executable.csv;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.OpsCode;
import org.phoenixctms.ctsms.domain.OpsCodeDao;
import org.phoenixctms.ctsms.domain.OpsSyst;
import org.phoenixctms.ctsms.domain.OpsSystDao;
import org.phoenixctms.ctsms.enumeration.OpsCodeType;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class OpsCodeLineProcessor extends LineProcessor {

	private final static int TYPE_COLUMN_INDEX = 0;
	private final static int DIMDI_ID_COLUMN_INDEX = 1;
	private final static int FIRST_CODE_COLUMN_INDEX = 2;
	private final static int SECOND_CODE_COLUMN_INDEX = 3;
	private final static int TEXT_COLUMN_INDEX = 4;
	private final static String FIELD_SEPARATOR_PATTERN = "\\|";
	@Autowired
	protected OpsCodeDao opsCodeDao;
	@Autowired
	protected OpsSystDao opsSystDao;
	private int typeColumnIndex;
	private int dimdiIdColumnIndex;
	private int firstCodeColumnIndex;
	private int secondCodeColumnIndex;
	private int textColumnIndex;
	private String revision;
	private String opsSystRevision;

	public OpsCodeLineProcessor() {
		super();
	}

	private OpsCode createOpsCode(String[] values, int hash) {
		OpsSyst systematics = opsSystDao.findByOpsCode(getFirstCode(values), getSecondCode(values), opsSystRevision);
		if (systematics == null) {
			jobOutput.println(getDimdiId(values) + " '" + getText(values) + "' - no systematics found!");
			return null;
		} else {
			OpsCode opsCode = OpsCode.Factory.newInstance();
			opsCode.setType(OpsCodeType.fromValue(getType(values)));
			opsCode.setDimdiId(getDimdiId(values));
			opsCode.setFirstCode(getFirstCode(values));
			opsCode.setSecondCode(getSecondCode(values));
			opsCode.setText(getText(values));
			opsCode.setSystematics(systematics);
			opsCode.setHash(hash);
			opsCode.setRevision(revision);
			opsCode = opsCodeDao.create(opsCode);
			return opsCode;
		}
	}

	private String getDimdiId(String[] values) {
		return values[dimdiIdColumnIndex];
	}

	public int getDimdiIdColumnIndex() {
		return dimdiIdColumnIndex;
	}

	private String getFirstCode(String[] values) {
		return values[firstCodeColumnIndex];
	}

	public int getFirstCodeColumnIndex() {
		return firstCodeColumnIndex;
	}

	private HashCodeBuilder getHashCodeBuilder(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getType(values))
				.append(getDimdiId(values))
				.append(getFirstCode(values))
				.append(getSecondCode(values))
				.append(getText(values));
	}

	public String getOpsSystRevision() {
		return opsSystRevision;
	}

	public String getRevision() {
		return revision;
	}

	private String getSecondCode(String[] values) {
		return values[secondCodeColumnIndex];
	}

	public int getSecondCodeColumnIndex() {
		return secondCodeColumnIndex;
	}

	private String getText(String[] values) {
		return values[textColumnIndex];
	}

	public int getTextColumnIndex() {
		return textColumnIndex;
	}

	private String getType(String[] values) {
		return values[typeColumnIndex];
	}

	public int getTypeColumnIndex() {
		return typeColumnIndex;
	}

	@Override
	public void init() {
		super.init();
		typeColumnIndex = TYPE_COLUMN_INDEX;
		dimdiIdColumnIndex = DIMDI_ID_COLUMN_INDEX;
		firstCodeColumnIndex = FIRST_CODE_COLUMN_INDEX;
		secondCodeColumnIndex = SECOND_CODE_COLUMN_INDEX;
		textColumnIndex = TEXT_COLUMN_INDEX;
		this.setFieldSeparatorRegexpPattern(FIELD_SEPARATOR_PATTERN);
		this.setCommentChar(null);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return getHashCodeBuilder(values).toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		int hash = getHashCodeBuilder(values).append(revision).toHashCode();
		if (opsCodeDao.searchUniqueHash(hash) == null) {
			createOpsCode(values, hash);
			return 1;
		} else {
			return 0;
		}
	}

	public void setDimdiIdColumnIndex(int dimdiIdColumnIndex) {
		this.dimdiIdColumnIndex = dimdiIdColumnIndex;
	}

	public void setFirstCodeColumnIndex(int firstCodeColumnIndex) {
		this.firstCodeColumnIndex = firstCodeColumnIndex;
	}

	public void setOpsSystRevision(String opsSystRevision) {
		this.opsSystRevision = opsSystRevision;
	}

	public void setRevision(String revision) {
		jobOutput.println("import as revision " + revision);
		this.revision = revision;
	}

	public void setSecondCodeColumnIndex(int secondCodeColumnIndex) {
		this.secondCodeColumnIndex = secondCodeColumnIndex;
	}

	public void setTextColumnIndex(int textColumnIndex) {
		this.textColumnIndex = textColumnIndex;
	}

	public void setTypeColumnIndex(int typeColumnIndex) {
		this.typeColumnIndex = typeColumnIndex;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getType(values))) {
			jobOutput.println("line " + lineNumber + ": empty type");
			return false;
		}
		if (CommonUtil.isEmptyString(getDimdiId(values))) {
			jobOutput.println("line " + lineNumber + ": empty dimdi id");
			return false;
		}
		if (CommonUtil.isEmptyString(getFirstCode(values)) && CommonUtil.isEmptyString(getSecondCode(values))) {
			jobOutput.println("line " + lineNumber + ": empty first and second code");
			return false;
		}
		if (CommonUtil.isEmptyString(getText(values))) {
			jobOutput.println("line " + lineNumber + ": empty text");
			return false;
		}
		return true;
	}
}
