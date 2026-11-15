package org.phoenixctms.ctsms.executable.xls;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.ProbandListEntryTag;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.fileprocessors.xls.RowProcessor;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandListEntryTagRowProcessor extends RowProcessor {

	private final static String SHEET_NAME = "probandlistentrytags";
	// a skipped row removes its proband list attribute on import (it is missing in the sheet's set), so rows with data but
	// without position or input field name could be rejected instead of skipped (blank/commented rows are always ignored):
	private final static boolean REJECT_INCOMPLETE_ROWS = false;
	private final static int REF_COLUMN_INDEX = 0;
	private final static int POSITION_COLUMN_INDEX = 1;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 2;
	private final static int INPUT_FIELD_NAME_COLUMN_INDEX = 3;
	private final static int TITLE_COLUMN_INDEX = 4;
	private final static int COMMENT_COLUMN_INDEX = 5;
	private final static int OPTIONAL_COLUMN_INDEX = 6;
	private final static int DISABLED_COLUMN_INDEX = 7;
	private final static int EXCEL_VALUE_COLUMN_INDEX = 8;
	private final static int EXCEL_DATE_COLUMN_INDEX = 9;
	private final static int ECRF_VALUE_COLUMN_INDEX = 10;
	private final static int STRATIFICATION_COLUMN_INDEX = 11;
	private final static int RANDOMIZE_COLUMN_INDEX = 12;
	private final static int JS_VARIABLE_NAME_COLUMN_INDEX = 13;
	private final static int JS_VALUE_EXPRESSION_COLUMN_INDEX = 14;
	private final static int JS_OUTPUT_EXPRESSION_COLUMN_INDEX = 15;
	private int refColumnIndex;
	private int positionColumnIndex;
	private int externalIdColumnIndex;
	private int inputFieldNameColumnIndex;
	private int titleColumnIndex;
	private int commentColumnIndex;
	private int optionalColumnIndex;
	private int disabledColumnIndex;
	private int excelValueColumnIndex;
	private int excelDateColumnIndex;
	private int ecrfValueColumnIndex;
	private int stratificationColumnIndex;
	private int randomizeColumnIndex;
	private int jsVariableNameColumnIndex;
	private int jsValueExpressionColumnIndex;
	private int jsOutputExpressionColumnIndex;
	private boolean noRefs;
	private LinkedHashSet<ProbandListEntryTagInVO> listEntryTags;
	private HashSet<Long> matchedListEntryTagIds;
	@Autowired
	protected ProbandListEntryTagDao probandListEntryTagDao;
	@Autowired
	protected TrialService trialService;

	public ProbandListEntryTagRowProcessor() {
		super();
		filterDupes = false;
		acceptCommentsIndex = 0;
		noRefs = true;
		listEntryTags = new LinkedHashSet<ProbandListEntryTagInVO>();
		matchedListEntryTagIds = new HashSet<Long>();
	}

	/**
	 * Writes the proband list entry tags read from the sheet: matched tags are updated, the others created, and the
	 * trial's tags missing in the sheet are removed together with their values.
	 */
	public long addUpdateProbandListEntryTags() throws Exception {
		long removeCount = 0l;
		Iterator<ProbandListEntryTag> it = probandListEntryTagDao.findByTrialFieldJs(context.getEntityId(), null, false, null, null).iterator();
		while (it.hasNext()) {
			ProbandListEntryTag listEntryTag = it.next();
			if (!matchedListEntryTagIds.contains(listEntryTag.getId())) {
				jobOutput.println("proband list attribute position " + listEntryTag.getPosition() + ", ref " + listEntryTag.getRef() + " not in sheet, will be removed");
				removeCount++;
			}
		}
		Collection<ProbandListEntryTagOutVO> result = trialService.addUpdateProbandListEntryTags(context.getAuth(), context.getEntityId(), listEntryTags);
		jobOutput.println(matchedListEntryTagIds.size() + " proband list attributes updated, " + (result.size() - matchedListEntryTagIds.size())
				+ " proband list attributes created, " + removeCount + " proband list attributes removed");
		return result.size();
	}

	private String getComment(String[] values) {
		return getColumnValue(values, commentColumnIndex);
	}

	private String getDisabled(String[] values) {
		return getColumnValue(values, disabledColumnIndex);
	}

	private String getEcrfValue(String[] values) {
		return getColumnValue(values, ecrfValueColumnIndex);
	}

	private String getExcelDate(String[] values) {
		return getColumnValue(values, excelDateColumnIndex);
	}

	private String getExcelValue(String[] values) {
		return getColumnValue(values, excelValueColumnIndex);
	}

	private String getExternalId(String[] values) {
		return getColumnValue(values, externalIdColumnIndex);
	}

	private Long getInputFieldId(String inputFieldName) {
		InputField inputField = ((XlsImporter) context.getImporter()).getSelectionSetValueRowProcessor().getInputField(inputFieldName);
		Long fieldId = null;
		if (inputField != null) {
			fieldId = inputField.getId();
		} else {
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

	public LinkedHashSet<ProbandListEntryTagInVO> getListEntryTags() {
		return listEntryTags;
	}

	private String getOptional(String[] values) {
		return getColumnValue(values, optionalColumnIndex);
	}

	private String getPosition(String[] values) {
		return getColumnValue(values, positionColumnIndex);
	}

	private String getRandomize(String[] values) {
		return getColumnValue(values, randomizeColumnIndex);
	}

	private String getRef(String[] values) {
		return getColumnValue(values, refColumnIndex);
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	private String getStratification(String[] values) {
		return getColumnValue(values, stratificationColumnIndex);
	}

	private String getTitle(String[] values) {
		return getColumnValue(values, titleColumnIndex);
	}

	@Override
	public void init() throws Throwable {
		super.init();
		refColumnIndex = REF_COLUMN_INDEX;
		positionColumnIndex = POSITION_COLUMN_INDEX;
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		inputFieldNameColumnIndex = INPUT_FIELD_NAME_COLUMN_INDEX;
		titleColumnIndex = TITLE_COLUMN_INDEX;
		commentColumnIndex = COMMENT_COLUMN_INDEX;
		optionalColumnIndex = OPTIONAL_COLUMN_INDEX;
		disabledColumnIndex = DISABLED_COLUMN_INDEX;
		excelValueColumnIndex = EXCEL_VALUE_COLUMN_INDEX;
		excelDateColumnIndex = EXCEL_DATE_COLUMN_INDEX;
		ecrfValueColumnIndex = ECRF_VALUE_COLUMN_INDEX;
		stratificationColumnIndex = STRATIFICATION_COLUMN_INDEX;
		randomizeColumnIndex = RANDOMIZE_COLUMN_INDEX;
		jsVariableNameColumnIndex = JS_VARIABLE_NAME_COLUMN_INDEX;
		jsValueExpressionColumnIndex = JS_VALUE_EXPRESSION_COLUMN_INDEX;
		jsOutputExpressionColumnIndex = JS_OUTPUT_EXPRESSION_COLUMN_INDEX;
		noRefs = true;
		listEntryTags.clear();
		matchedListEntryTagIds.clear();
		((XlsImporter) context.getImporter()).loadInputFields(context);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getRef(values))
				.append(getPosition(values))
				.append(getExternalId(values))
				.append(getInputFieldName(values))
				.append(getTitle(values))
				.append(getComment(values))
				.append(getOptional(values))
				.append(getDisabled(values))
				.append(getExcelValue(values))
				.append(getExcelDate(values))
				.append(getEcrfValue(values))
				.append(getStratification(values))
				.append(getRandomize(values))
				.append(getJsVariableName(values))
				.append(getJsValueExpression(values))
				.append(getJsOutputExpression(values))
				.toHashCode();
	}

	@Override
	public void postProcess() {
	}

	@Override
	protected boolean preCheck(String[] values) throws Throwable {
		noRefs &= CommonUtil.isEmptyString(getRef(values));
		return noRefs;
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		Long position = Long.parseLong(getPosition(values));
		String ref = getRef(values);
		ProbandListEntryTag listEntryTag = null;
		try {
			if (noRefs) {
				listEntryTag = probandListEntryTagDao.findByTrialPosition(context.getEntityId(), position).iterator().next();
			} else if (!CommonUtil.isEmptyString(ref)) {
				listEntryTag = probandListEntryTagDao.findByTrialRef(context.getEntityId(), ref).iterator().next();
			}
		} catch (NoSuchElementException e) {
		}
		String inputFieldName = getInputFieldName(values);
		ProbandListEntryTagInVO listEntryTagIn = new ProbandListEntryTagInVO();
		listEntryTagIn.setId(listEntryTag != null ? listEntryTag.getId() : null);
		listEntryTagIn.setVersion(listEntryTag != null ? listEntryTag.getVersion() : 0l);
		listEntryTagIn.setTrialId(context.getEntityId());
		listEntryTagIn.setRef(CommonUtil.isEmptyString(ref) ? null : ref);
		listEntryTagIn.setPosition(position);
		listEntryTagIn.setExternalId(getExternalId(values));
		listEntryTagIn.setFieldId(getInputFieldId(inputFieldName));
		listEntryTagIn.setTitle(getTitle(values));
		listEntryTagIn.setComment(getComment(values));
		listEntryTagIn.setOptional(Boolean.parseBoolean(getOptional(values)));
		listEntryTagIn.setDisabled(Boolean.parseBoolean(getDisabled(values)));
		listEntryTagIn.setExcelValue(Boolean.parseBoolean(getExcelValue(values)));
		listEntryTagIn.setExcelDate(Boolean.parseBoolean(getExcelDate(values)));
		listEntryTagIn.setEcrfValue(Boolean.parseBoolean(getEcrfValue(values)));
		listEntryTagIn.setStratification(Boolean.parseBoolean(getStratification(values)));
		listEntryTagIn.setRandomize(Boolean.parseBoolean(getRandomize(values)));
		String jsVariableName = getJsVariableName(values);
		boolean clearJs = CommonUtil.isEmptyString(jsVariableName)
				|| (!CommonUtil.isEmptyString(getCommentChar()) && jsVariableName.startsWith(getCommentChar()));
		listEntryTagIn.setJsVariableName(clearJs ? null : jsVariableName);
		listEntryTagIn.setJsValueExpression(clearJs ? null : getJsValueExpression(values));
		listEntryTagIn.setJsOutputExpression(clearJs ? null : getJsOutputExpression(values));
		String label = "proband list attribute position " + position + ", field '" + inputFieldName + "'";
		if (listEntryTag != null && !matchedListEntryTagIds.add(listEntryTag.getId())) {
			throw new IllegalArgumentException(label + ": " + (noRefs ? "position" : "ref " + ref) + " matched more than once");
		}
		if (listEntryTags.add(listEntryTagIn)) {
			jobOutput.println(label + " read" + (listEntryTag != null ? " (existing proband list attribute)" : " (new proband list attribute)"));
			return 1;
		} else {
			jobOutput.println(label + " SKIPPED");
			return 0;
		}
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (REJECT_INCOMPLETE_ROWS) {
			if (isBlankRow(values)) {
				return false;
			}
			if (CommonUtil.isEmptyString(getPosition(values))) {
				throw new IllegalArgumentException("row " + rowNumber + ": empty position");
			}
			if (CommonUtil.isEmptyString(getInputFieldName(values))) {
				throw new IllegalArgumentException("row " + rowNumber + ": empty input field name");
			}
			return true;
		}
		if (CommonUtil.isEmptyString(getPosition(values))) {
			return false;
		}
		if (CommonUtil.isEmptyString(getInputFieldName(values))) {
			jobOutput.println("row " + rowNumber + ": empty input field name");
			return false;
		}
		return true;
	}
}
