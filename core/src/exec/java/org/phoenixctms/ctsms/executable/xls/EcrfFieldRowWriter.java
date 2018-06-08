package org.phoenixctms.ctsms.executable.xls;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EcrfFieldRowWriter extends RowWriter {

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

	private int ecrfProbandGroupColumnIndex;
	private int ecrfPositionColumnIndex;
	private int sectionColumnIndex;
	private int positionColumnIndex;
	private int externalIdColumnIndex;
	private int inputFieldNameColumnIndex;
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
	private int maxColumnIndex;
	private HashSet<Long> inputFieldIds;
	@Autowired
	protected TrialService trialService;

	public EcrfFieldRowWriter() {
		super();
		maxColumnIndex = 0;
		inputFieldIds = new HashSet<Long>();
	}

	public String getSheetName() {
		// return context.getSheetName(this);
		return SHEET_NAME;
	}

	@Override
	public void init() {
		super.init();

		maxColumnIndex = 0;
		ecrfProbandGroupColumnIndex = ECRF_PROBAND_GROUP_COLUMN_INDEX;
		maxColumnIndex = Math.max(ecrfProbandGroupColumnIndex, maxColumnIndex);
		ecrfPositionColumnIndex = ECRF_POSITION_COLUMN_INDEX;
		maxColumnIndex = Math.max(ecrfPositionColumnIndex, maxColumnIndex);
		sectionColumnIndex = SECTION_COLUMN_INDEX;
		maxColumnIndex = Math.max(sectionColumnIndex, maxColumnIndex);
		positionColumnIndex = POSITION_COLUMN_INDEX;
		maxColumnIndex = Math.max(positionColumnIndex, maxColumnIndex);
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		maxColumnIndex = Math.max(externalIdColumnIndex, maxColumnIndex);
		inputFieldNameColumnIndex = INPUT_FIELD_NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(inputFieldNameColumnIndex, maxColumnIndex);
		commentColumnIndex = COMMENT_COLUMN_INDEX;
		maxColumnIndex = Math.max(commentColumnIndex, maxColumnIndex);
		seriesColumnIndex = SERIES_COLUMN_INDEX;
		maxColumnIndex = Math.max(seriesColumnIndex, maxColumnIndex);
		optionalColumnIndex = OPTIONAL_COLUMN_INDEX;
		maxColumnIndex = Math.max(optionalColumnIndex, maxColumnIndex);
		disabledColumnIndex = DISABLED_COLUMN_INDEX;
		maxColumnIndex = Math.max(disabledColumnIndex, maxColumnIndex);
		auditTrailColumnIndex = AUDIT_TRAIL_COLUMN_INDEX;
		maxColumnIndex = Math.max(auditTrailColumnIndex, maxColumnIndex);
		reasonForChangeRequiredColumnIndex = REASON_FOR_CHANGE_REQUIRED_COLUMN_INDEX;
		maxColumnIndex = Math.max(reasonForChangeRequiredColumnIndex, maxColumnIndex);
		jsVariableNameColumnIndex = JS_VARIABLE_NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(jsVariableNameColumnIndex, maxColumnIndex);
		jsValueExpressionColumnIndex = JS_VALUE_EXPRESSION_COLUMN_INDEX;
		maxColumnIndex = Math.max(jsValueExpressionColumnIndex, maxColumnIndex);
		jsOutputExpressionColumnIndex = JS_OUTPUT_EXPRESSION_COLUMN_INDEX;
		maxColumnIndex = Math.max(jsOutputExpressionColumnIndex, maxColumnIndex);
		notifyColumnIndex = NOTIFY_COLUMN_INDEX;
		maxColumnIndex = Math.max(notifyColumnIndex, maxColumnIndex);
		inputFieldIds.clear();

		context.getExporter().getInputFieldRowWriter().init();
		context.getSpreadSheet(this);
	}

	@Override
	public void printRows() throws Throwable {
		printRows(trialService.getEcrfFieldList(context.getAuth(), null, context.getEntityId(this), true, null));
	}

	public void printRows(Collection<ECRFFieldOutVO> ecrfFields) throws Throwable {
		Iterator<ECRFFieldOutVO> it = ecrfFields.iterator();
		while (it.hasNext()) {
			ECRFFieldOutVO ecrfField = it.next();
			if (inputFieldIds.add(ecrfField.getField().getId())) {
				context.getExporter().getInputFieldRowWriter().printRows(ecrfField.getField());
			}
			String[] values = new String[maxColumnIndex + 1];
			values[ecrfProbandGroupColumnIndex] = (ecrfField.getEcrf().getGroup() != null ?
					ecrfField.getEcrf().getGroup().getToken() : null);
			values[ecrfPositionColumnIndex] = Long.toString(ecrfField.getEcrf().getPosition());
			values[sectionColumnIndex] = ecrfField.getSection();
			values[positionColumnIndex] = Long.toString(ecrfField.getPosition());
			values[externalIdColumnIndex] = ecrfField.getExternalId();
			values[inputFieldNameColumnIndex] = ecrfField.getField().getNameL10nKey();
			values[commentColumnIndex] = ecrfField.getComment();
			values[seriesColumnIndex] = Boolean.toString(ecrfField.getSeries());
			values[optionalColumnIndex] = Boolean.toString(ecrfField.getOptional());
			values[disabledColumnIndex] = Boolean.toString(ecrfField.getDisabled());
			values[auditTrailColumnIndex] = Boolean.toString(ecrfField.getAuditTrail());
			values[reasonForChangeRequiredColumnIndex] = Boolean.toString(ecrfField.getReasonForChangeRequired());
			values[jsVariableNameColumnIndex] = ecrfField.getJsVariableName();
			values[jsValueExpressionColumnIndex] = ecrfField.getJsValueExpression();
			values[jsOutputExpressionColumnIndex] = ecrfField.getJsOutputExpression();
			values[notifyColumnIndex] = Boolean.toString(ecrfField.getNotify());
			printRow(values);
		}
	}
}
