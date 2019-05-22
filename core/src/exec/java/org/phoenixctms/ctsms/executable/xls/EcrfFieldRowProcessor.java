package org.phoenixctms.ctsms.executable.xls;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFField;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EcrfFieldRowProcessor extends RowProcessor {

	private final static String DEFAULT_PROBAND_GROUP_DESCRIPTION = "";
	private final static boolean DEFAULT_PROBAND_GROUP_RANDOMIZE = false;
	private final static String SHEET_NAME = "ecrffields";
	private final static int ECRF_PROBAND_GROUP_COLUMN_INDEX = 0;
	private final static int ECRF_POSITION_COLUMN_INDEX = 1;
	private final static int SECTION_COLUMN_INDEX = 2;
	private final static int POSITION_COLUMN_INDEX = 3;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 4;
	private final static int INPUT_FIELD_NAME_COLUMN_INDEX = 5;
	private final static int COMMENT_COLUMN_INDEX = 6;
	private final static int SERIES_COLUMN_INDEX = 7;
	private final static int OPTIONAL_COLUMN_INDEX = 8;
	private final static int DISABLED_COLUMN_INDEX = 9;
	private final static int AUDIT_TRAIL_COLUMN_INDEX = 10;
	private final static int REASON_FOR_CHANGE_REQUIRED_COLUMN_INDEX = 11;
	private final static int JS_VARIABLE_NAME_COLUMN_INDEX = 12;
	private final static int JS_VALUE_EXPRESSION_COLUMN_INDEX = 13;
	private final static int JS_OUTPUT_EXPRESSION_COLUMN_INDEX = 14;
	private final static int NOTIFY_COLUMN_INDEX = 15;
	private final static int TITLE_COLUMN_INDEX = 16;
	// @Autowired
	// protected InputFieldService inputFieldService;
	private int ecrfProbandGroupColumnIndex;
	private int ecrfPositionColumnIndex;
	private int sectionColumnIndex;
	private int positionColumnIndex;
	private int externalIdColumnIndex;
	private int inputFieldNameColumnIndex;
	private int titleColumnIndex;
	private int commentColumnIndex;
	private int seriesColumnIndex;
	private int optionalColumnIndex;
	private int disabledColumnIndex;
	private int auditTrailColumnIndex;
	private int reasonForChangeRequiredColumnIndex;
	private int jsVariableNameColumnIndex;
	private int jsValueExpressionColumnIndex;
	private int jsOutputExpressionColumnIndex;
	private int notifyColumnIndex;
	// private AuthenticationVO auth;
	// private Long inputFieldId;
	private HashMap<String, HashMap<Long, Set<ECRFFieldInVO>>> ecrfFieldMap;
	private HashMap<String, Long> probandGroupIdMap;
	private HashMap<String, HashMap<Long, ECRF>> ecrfMap;
	// private HashMap<String, Long> inputFieldIdMap;
	@Autowired
	protected ECRFDao eCRFDao; // varnames must match bean ids in applicationContext.xml
	@Autowired
	protected ECRFFieldDao eCRFFieldDao;
	@Autowired
	protected ProbandGroupDao probandGroupDao;
	@Autowired
	protected InputFieldDao inputFieldDao;
	@Autowired
	protected TrialService trialService;

	public EcrfFieldRowProcessor() {
		super();
		filterDupes = false;
		// this.setCommentChar(null);
		acceptCommentsIndex = 0;
		ecrfFieldMap = new HashMap<String, HashMap<Long, Set<ECRFFieldInVO>>>();
		probandGroupIdMap = new HashMap<String, Long>();
		ecrfMap = new HashMap<String, HashMap<Long, ECRF>>();
		// inputFieldIdMap = new HashMap<String, Long>();
	}

	public void clearEcrf(String probandGroupToken, Long ecrfPosition) {
		if (ecrfMap.containsKey(probandGroupToken)) {
			HashMap<Long, ECRF> positionMap = ecrfMap.get(probandGroupToken);
			positionMap.remove(ecrfPosition);
			if (positionMap.isEmpty()) {
				ecrfMap.remove(probandGroupToken);
			}
		}
	}

	private ProbandGroupOutVO createProbandGroup(String probandGroupToken) throws Exception {
		ProbandGroupInVO newProbandGroup = new ProbandGroupInVO();
		newProbandGroup.setToken(probandGroupToken);
		newProbandGroup.setTitle(probandGroupToken);
		newProbandGroup.setDescription(DEFAULT_PROBAND_GROUP_DESCRIPTION);
		newProbandGroup.setRandomize(DEFAULT_PROBAND_GROUP_RANDOMIZE);
		newProbandGroup.setTrialId(context.getEntityId());
		return trialService.addProbandGroup(context.getAuth(), newProbandGroup);
	}

	private String getAuditTrail(String[] values) {
		return getColumnValue(values, auditTrailColumnIndex);
	}

	// public HashMap<String, HashMap<Long, Set<ECRFFieldInVO>>> getEcrfFieldMap() {
	// return ecrfFieldMap;
	// }
	private String getComment(String[] values) {
		return getColumnValue(values, commentColumnIndex);
	}

	private String getTitle(String[] values) {
		return getColumnValue(values, titleColumnIndex);
	}

	private String getDisabled(String[] values) {
		return getColumnValue(values, disabledColumnIndex);
	}

	public ECRF getEcrf(String probandGroupToken, Long ecrfPosition) throws Exception {
		ECRF ecrf = null;
		HashMap<Long, ECRF> positionMap;
		if (ecrfMap.containsKey(probandGroupToken)) {
			positionMap = ecrfMap.get(probandGroupToken);
		} else {
			positionMap = new HashMap<Long, ECRF>();
			ecrfMap.put(probandGroupToken, positionMap);
		}
		if (positionMap.containsKey(ecrfPosition)) {
			ecrf = positionMap.get(ecrfPosition);
		} else {
			try {
				ecrf = eCRFDao.findCollidingTrialGroupPosition(context.getEntityId(), getProbandGroupId(probandGroupToken), ecrfPosition).iterator().next();
			} catch (NoSuchElementException e) {
			}
			positionMap.put(ecrfPosition, ecrf);
		}
		return ecrf;
	}

	public Set<ECRFFieldInVO> getEcrfFields(String probandGroupToken, Long ecrfPosition) {
		HashMap<Long, Set<ECRFFieldInVO>> positionMap = ecrfFieldMap.get(probandGroupToken);
		if (positionMap != null) {
			return positionMap.get(ecrfPosition);
		}
		return null;
	}

	private String getEcrfPosition(String[] values) {
		return getColumnValue(values, ecrfPositionColumnIndex);
	}

	private String getEcrfProbandGroup(String[] values) {
		return getColumnValue(values, ecrfProbandGroupColumnIndex);
	}

	private String getExternalId(String[] values) {
		return getColumnValue(values, externalIdColumnIndex);
	}

	private Long getInputFieldId(String inputFieldName) {
		InputField inputField = context.getImporter().getSelectionSetValueRowProcessor().getInputField(inputFieldName);
		Long fieldId = null;
		if (inputField != null) {
			fieldId = inputField.getId();
		} else {
			// fieldId = null;
			jobOutput.println("input field '" + inputFieldName + "' not found");
		}
		return fieldId;
	}

	private String getInputFieldName(String[] values) {
		return getColumnValue(values, inputFieldNameColumnIndex);
	}

	private String getJsOutputExpression(String[] values) {
		return getColumnValue(values, jsOutputExpressionColumnIndex);
	}

	private String getJsValueExpression(String[] values) {
		return getColumnValue(values, jsValueExpressionColumnIndex);
	}

	private String getJsVariableName(String[] values) {
		return getColumnValue(values, jsVariableNameColumnIndex);
	}

	private String getNotify(String[] values) {
		return getColumnValue(values, notifyColumnIndex);
	}
	// public HashMap<String, Long> getProbandGroupIdMap() {
	// return probandGroupIdMap;
	// }

	// public Long getProbandGroupId(String probandGroupToken) {
	// return probandGroupIdMap.get(probandGroupToken);
	//
	// }
	private String getOptional(String[] values) {
		return getColumnValue(values, optionalColumnIndex);
	}

	private String getPosition(String[] values) {
		return getColumnValue(values, positionColumnIndex);
	}

	public Long getProbandGroupId(String probandGroupToken) throws Exception {
		Long probandGroupId = null;
		if (!CommonUtil.isEmptyString(probandGroupToken)) {
			if (!probandGroupIdMap.containsKey(probandGroupToken)) {
				try {
					probandGroupId = probandGroupDao.findByTrialTitleToken(context.getEntityId(), null, probandGroupToken).iterator().next().getId();
				} catch (NoSuchElementException e) {
					ProbandGroupOutVO probandGroupVO = createProbandGroup(probandGroupToken);
					probandGroupId = probandGroupVO.getId();
					jobOutput.println("proband group '" + probandGroupVO.getUniqueName() + "' created");
				}
				probandGroupIdMap.put(probandGroupToken, probandGroupId);
			} else {
				probandGroupId = probandGroupIdMap.get(probandGroupToken);
			}
		}
		return probandGroupId;
	}

	private String getReasonForChangeRequired(String[] values) {
		return getColumnValue(values, reasonForChangeRequiredColumnIndex);
	}

	private String getSection(String[] values) {
		return getColumnValue(values, sectionColumnIndex);
	}

	private String getSeries(String[] values) {
		return getColumnValue(values, seriesColumnIndex);
	}

	@Override
	public String getSheetName() {
		// return context.getSheetName(this);
		return SHEET_NAME;
	}

	@Override
	public void init() throws Throwable {
		super.init();
		ecrfProbandGroupColumnIndex = ECRF_PROBAND_GROUP_COLUMN_INDEX;
		ecrfPositionColumnIndex = ECRF_POSITION_COLUMN_INDEX;
		sectionColumnIndex = SECTION_COLUMN_INDEX;
		positionColumnIndex = POSITION_COLUMN_INDEX;
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		inputFieldNameColumnIndex = INPUT_FIELD_NAME_COLUMN_INDEX;
		titleColumnIndex = TITLE_COLUMN_INDEX;
		commentColumnIndex = COMMENT_COLUMN_INDEX;
		seriesColumnIndex = SERIES_COLUMN_INDEX;
		optionalColumnIndex = OPTIONAL_COLUMN_INDEX;
		disabledColumnIndex = DISABLED_COLUMN_INDEX;
		auditTrailColumnIndex = AUDIT_TRAIL_COLUMN_INDEX;
		reasonForChangeRequiredColumnIndex = REASON_FOR_CHANGE_REQUIRED_COLUMN_INDEX;
		jsVariableNameColumnIndex = JS_VARIABLE_NAME_COLUMN_INDEX;
		jsValueExpressionColumnIndex = JS_VALUE_EXPRESSION_COLUMN_INDEX;
		jsOutputExpressionColumnIndex = JS_OUTPUT_EXPRESSION_COLUMN_INDEX;
		notifyColumnIndex = NOTIFY_COLUMN_INDEX;
		ecrfFieldMap.clear();
		probandGroupIdMap.clear();
		// inputFieldIdMap.clear();
		ecrfMap.clear();
		context.getImporter().loadInputFields(context);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				// .append(getEcrfProbandGroup(values))
				// .append(getEcrfPosition(values))
				// .append(getSection(values))
				// .append(getPosition(values))
				.append(getEcrfProbandGroup(values))
				.append(getEcrfPosition(values))
				.append(getSection(values))
				.append(getPosition(values))
				.append(getExternalId(values))
				.append(getInputFieldName(values))
				.append(getTitle(values))
				.append(getComment(values))
				.append(getSeries(values))
				.append(getOptional(values))
				.append(getDisabled(values))
				.append(getAuditTrail(values))
				.append(getReasonForChangeRequired(values))
				.append(getJsVariableName(values))
				.append(getJsValueExpression(values))
				.append(getJsOutputExpression(values))
				.append(getNotify(values))
				.toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		String probandGroupToken = getEcrfProbandGroup(values);
		Long ecrfPosition = Long.parseLong(getEcrfPosition(values));
		ECRF ecrf = getEcrf(probandGroupToken, ecrfPosition);
		Long position = Long.parseLong(getPosition(values));
		String section = getSection(values);
		ECRFField ecrfField = null;
		if (ecrf != null) {
			try {
				ecrfField = eCRFFieldDao.findByEcrfSectionPosition(ecrf.getId(), section, position).iterator().next();
			} catch (NoSuchElementException e) {
			}
		}
		String inputFieldName = getInputFieldName(values);
		ECRFFieldInVO ecrfFieldIn = new ECRFFieldInVO();
		ecrfFieldIn.setId(ecrfField != null ? ecrfField.getId() : null);
		ecrfFieldIn.setVersion(ecrfField != null ? ecrfField.getVersion() : 0l);
		ecrfFieldIn.setTrialId(context.getEntityId());
		ecrfFieldIn.setEcrfId(ecrf != null ? ecrf.getId() : null);
		ecrfFieldIn.setSection(section);
		ecrfFieldIn.setPosition(position);
		ecrfFieldIn.setExternalId(getExternalId(values));
		ecrfFieldIn.setFieldId(getInputFieldId(inputFieldName));
		ecrfFieldIn.setTitle(getTitle(values));
		ecrfFieldIn.setComment(getComment(values));
		ecrfFieldIn.setSeries(Boolean.parseBoolean(getSeries(values)));
		ecrfFieldIn.setOptional(Boolean.parseBoolean(getOptional(values)));
		ecrfFieldIn.setDisabled(Boolean.parseBoolean(getDisabled(values)));
		ecrfFieldIn.setAuditTrail(Boolean.parseBoolean(getAuditTrail(values)));
		ecrfFieldIn.setReasonForChangeRequired(Boolean.parseBoolean(getReasonForChangeRequired(values)));
		String jsVariableName = getJsVariableName(values);
		boolean clearJs = CommonUtil.isEmptyString(jsVariableName)
				|| (!CommonUtil.isEmptyString(getCommentChar()) && !CommonUtil.isEmptyString(jsVariableName) && jsVariableName.startsWith(getCommentChar()));
		ecrfFieldIn.setJsVariableName(clearJs ? null : getJsVariableName(values));
		ecrfFieldIn.setJsValueExpression(clearJs ? null : getJsValueExpression(values));
		ecrfFieldIn.setJsOutputExpression(clearJs ? null : getJsOutputExpression(values));
		ecrfFieldIn.setNotify(Boolean.parseBoolean(getNotify(values))); //clearJs ? false :
		Set<ECRFFieldInVO> ecrfFields;
		HashMap<Long, Set<ECRFFieldInVO>> positionMap;
		if (ecrfFieldMap.containsKey(probandGroupToken)) {
			positionMap = ecrfFieldMap.get(probandGroupToken);
		} else {
			positionMap = new HashMap<Long, Set<ECRFFieldInVO>>();
			ecrfFieldMap.put(probandGroupToken, positionMap);
		}
		if (positionMap.containsKey(ecrfPosition)) {
			ecrfFields = positionMap.get(ecrfPosition);
		} else {
			ecrfFields = new LinkedHashSet<ECRFFieldInVO>();
			positionMap.put(ecrfPosition, ecrfFields);
		}
		String label = "ecrf for proband group " + (CommonUtil.isEmptyString(probandGroupToken) ? "<no group>" : probandGroupToken) + ", position " + ecrfPosition
				+ ": field section " + section + ", position " + position + ", field '" + inputFieldName + "'";
		if (ecrfFields.add(ecrfFieldIn)) {
			jobOutput.println(label + " read");
			return 1;
		} else {
			jobOutput.println(label + " SKIPPED");
			return 0;
		}
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (CommonUtil.isEmptyString(getEcrfPosition(values))) {
			// jobOutput.println("row " + rowNumber + ": empty value");
			return false;
		}
		if (CommonUtil.isEmptyString(getPosition(values))) {
			// jobOutput.println("row " + rowNumber + ": empty value");
			return false;
		}
		return true;
	}
}
