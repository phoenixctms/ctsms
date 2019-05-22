package org.phoenixctms.ctsms.executable.csv;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.AlphaId;
import org.phoenixctms.ctsms.domain.AlphaIdDao;
import org.phoenixctms.ctsms.domain.IcdSyst;
import org.phoenixctms.ctsms.domain.IcdSystDao;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class AlphaIdLineProcessor extends LineProcessor {

	private final static int VALID_COLUMN_INDEX = 0;
	private final static int ALPHA_ID_COLUMN_INDEX = 1;
	private final static int PRIMARY_CODE_COLUMN_INDEX = 2;
	private final static int ASTERISK_CODE_COLUMN_INDEX = 3;
	private final static int OPTIONAL_CODE_COLUMN_INDEX = 4;
	private final static int TEXT_COLUMN_INDEX = 5;
	private final static String FIELD_SEPARATOR_PATTERN = "\\|";

	private static boolean parseBoolean(String value) {
		if ("1".equals(value)) {
			return true;
		} else if ("0".equals(value)) {
			return false;
		} else {
			throw new IllegalArgumentException("cannot parse boolean value " + value);
		}
	}

	@Autowired
	protected AlphaIdDao alphaIdDao;
	@Autowired
	protected IcdSystDao icdSystDao;
	private int alphaIdColumnIndex;
	private int validColumnIndex;
	private int primaryCodeColumnIndex;
	private int asteriskCodeColumnIndex;
	private int optionalCodeColumnIndex;
	private int textColumnIndex;
	private String revision;
	private String icdSystRevision;

	public AlphaIdLineProcessor() {
		super();
	}

	private AlphaId createAlphaId(String[] values, int hash) {
		IcdSyst systematics = icdSystDao.findByIcdCode(getPrimaryCode(values), getAsteriskCode(values), getOptionalCode(values), icdSystRevision);
		if (systematics == null) {
			jobOutput.println(getAlphaId(values) + " '" + getText(values) + "' - no systematics found!");
			return null;
		} else {
			AlphaId alphaId = AlphaId.Factory.newInstance();
			alphaId.setAlphaId(getAlphaId(values));
			alphaId.setValid(parseBoolean(getValid(values)));
			alphaId.setPrimaryCode(getPrimaryCode(values));
			alphaId.setAsteriskCode(getAsteriskCode(values));
			alphaId.setOptionalCode(getOptionalCode(values));
			alphaId.setText(getText(values));
			alphaId.setSystematics(systematics);
			alphaId.setHash(hash);
			alphaId.setRevision(revision);
			alphaId = alphaIdDao.create(alphaId);
			return alphaId;
		}
	}

	private String getAlphaId(String[] values) {
		return values[alphaIdColumnIndex];
	}

	public int getAlphaIdColumnIndex() {
		return alphaIdColumnIndex;
	}

	private String getAsteriskCode(String[] values) {
		return values[asteriskCodeColumnIndex];
	}

	public int getAsteriskCodeColumnIndex() {
		return asteriskCodeColumnIndex;
	}

	private HashCodeBuilder getHashCodeBuilder(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getValid(values))
				.append(getAlphaId(values))
				.append(getPrimaryCode(values))
				.append(getAsteriskCode(values))
				.append(getOptionalCode(values))
				.append(getText(values));
	}

	public String getIcdSystRevision() {
		return icdSystRevision;
	}

	private String getOptionalCode(String[] values) {
		return values[optionalCodeColumnIndex];
	}

	public int getOptionalCodeColumnIndex() {
		return optionalCodeColumnIndex;
	}

	private String getPrimaryCode(String[] values) {
		return values[primaryCodeColumnIndex];
	}

	public int getPrimaryCodeColumnIndex() {
		return primaryCodeColumnIndex;
	}

	public String getRevision() {
		return revision;
	}

	private String getText(String[] values) {
		return values[textColumnIndex];
	}

	public int getTextColumnIndex() {
		return textColumnIndex;
	}

	private String getValid(String[] values) {
		return values[validColumnIndex];
	}

	public int getValidColumnIndex() {
		return validColumnIndex;
	}

	@Override
	public void init() {
		super.init();
		alphaIdColumnIndex = ALPHA_ID_COLUMN_INDEX;
		validColumnIndex = VALID_COLUMN_INDEX;
		primaryCodeColumnIndex = PRIMARY_CODE_COLUMN_INDEX;
		asteriskCodeColumnIndex = ASTERISK_CODE_COLUMN_INDEX;
		optionalCodeColumnIndex = OPTIONAL_CODE_COLUMN_INDEX;
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
		if (alphaIdDao.searchUniqueHash(hash) == null) {
			createAlphaId(values, hash);
			return 1;
		} else {
			return 0;
		}
	}

	public void setAlphaIdColumnIndex(int alphaIdColumnIndex) {
		this.alphaIdColumnIndex = alphaIdColumnIndex;
	}

	public void setAsteriskCodeColumnIndex(int asteriskCodeColumnIndex) {
		this.asteriskCodeColumnIndex = asteriskCodeColumnIndex;
	}

	public void setIcdSystRevision(String icdSystRevision) {
		this.icdSystRevision = icdSystRevision;
	}

	public void setOptionalCodeColumnIndex(int optionalCodeColumnIndex) {
		this.optionalCodeColumnIndex = optionalCodeColumnIndex;
	}

	public void setPrimaryCodeColumnIndex(int primaryCodeColumnIndex) {
		this.primaryCodeColumnIndex = primaryCodeColumnIndex;
	}

	public void setRevision(String revision) {
		jobOutput.println("import as revision " + revision);
		this.revision = revision;
	}

	public void setTextColumnIndex(int textColumnIndex) {
		this.textColumnIndex = textColumnIndex;
	}

	public void setValidColumnIndex(int validColumnIndex) {
		this.validColumnIndex = validColumnIndex;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getAlphaId(values))) {
			jobOutput.println("line " + lineNumber + ": empty alpha id");
			return false;
		}
		if (CommonUtil.isEmptyString(getValid(values))) {
			jobOutput.println("line " + lineNumber + ": empty valid");
			return false;
		}
		if (CommonUtil.isEmptyString(getPrimaryCode(values)) && CommonUtil.isEmptyString(getAsteriskCode(values)) && CommonUtil.isEmptyString(getOptionalCode(values))) {
			jobOutput.println("line " + lineNumber + ": empty primary, asterisk and optional code");
			return false;
		}
		if (CommonUtil.isEmptyString(getText(values))) {
			jobOutput.println("line " + lineNumber + ": empty text");
			return false;
		}
		return true;
	}
}
