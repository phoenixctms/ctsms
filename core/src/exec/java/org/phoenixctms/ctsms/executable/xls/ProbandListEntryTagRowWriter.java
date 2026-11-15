package org.phoenixctms.ctsms.executable.xls;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.phoenixctms.ctsms.fileprocessors.xls.RowWriter;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class ProbandListEntryTagRowWriter extends RowWriter {

	private final static String SHEET_NAME = "probandlistentrytags";
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
	private int maxColumnIndex;
	private HashSet<Long> inputFieldIds;
	@Autowired
	protected TrialService trialService;

	public ProbandListEntryTagRowWriter() {
		super();
		maxColumnIndex = 0;
		inputFieldIds = new HashSet<Long>();
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	@Override
	public void init() {
		super.init();
		maxColumnIndex = 0;
		refColumnIndex = REF_COLUMN_INDEX;
		maxColumnIndex = Math.max(refColumnIndex, maxColumnIndex);
		positionColumnIndex = POSITION_COLUMN_INDEX;
		maxColumnIndex = Math.max(positionColumnIndex, maxColumnIndex);
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		maxColumnIndex = Math.max(externalIdColumnIndex, maxColumnIndex);
		inputFieldNameColumnIndex = INPUT_FIELD_NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(inputFieldNameColumnIndex, maxColumnIndex);
		titleColumnIndex = TITLE_COLUMN_INDEX;
		maxColumnIndex = Math.max(titleColumnIndex, maxColumnIndex);
		commentColumnIndex = COMMENT_COLUMN_INDEX;
		maxColumnIndex = Math.max(commentColumnIndex, maxColumnIndex);
		optionalColumnIndex = OPTIONAL_COLUMN_INDEX;
		maxColumnIndex = Math.max(optionalColumnIndex, maxColumnIndex);
		disabledColumnIndex = DISABLED_COLUMN_INDEX;
		maxColumnIndex = Math.max(disabledColumnIndex, maxColumnIndex);
		excelValueColumnIndex = EXCEL_VALUE_COLUMN_INDEX;
		maxColumnIndex = Math.max(excelValueColumnIndex, maxColumnIndex);
		excelDateColumnIndex = EXCEL_DATE_COLUMN_INDEX;
		maxColumnIndex = Math.max(excelDateColumnIndex, maxColumnIndex);
		ecrfValueColumnIndex = ECRF_VALUE_COLUMN_INDEX;
		maxColumnIndex = Math.max(ecrfValueColumnIndex, maxColumnIndex);
		stratificationColumnIndex = STRATIFICATION_COLUMN_INDEX;
		maxColumnIndex = Math.max(stratificationColumnIndex, maxColumnIndex);
		randomizeColumnIndex = RANDOMIZE_COLUMN_INDEX;
		maxColumnIndex = Math.max(randomizeColumnIndex, maxColumnIndex);
		jsVariableNameColumnIndex = JS_VARIABLE_NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(jsVariableNameColumnIndex, maxColumnIndex);
		jsValueExpressionColumnIndex = JS_VALUE_EXPRESSION_COLUMN_INDEX;
		maxColumnIndex = Math.max(jsValueExpressionColumnIndex, maxColumnIndex);
		jsOutputExpressionColumnIndex = JS_OUTPUT_EXPRESSION_COLUMN_INDEX;
		maxColumnIndex = Math.max(jsOutputExpressionColumnIndex, maxColumnIndex);
		inputFieldIds.clear();
		((XlsExporter) context.getExporter()).getInputFieldRowWriter().init();
		context.getSpreadSheet(this);
	}

	@Override
	public void printRows() throws Throwable {
		printRows(trialService.getProbandListEntryTagList(context.getAuth(), context.getEntityId(this), null, null));
	}

	@Override
	public void printHeaderRow() throws Exception {
		String[] values = new String[maxColumnIndex + 1];
		values[refColumnIndex] = "ref";
		values[positionColumnIndex] = "position";
		values[externalIdColumnIndex] = "external id";
		values[inputFieldNameColumnIndex] = "input field name";
		values[titleColumnIndex] = "title";
		values[commentColumnIndex] = "comment";
		values[optionalColumnIndex] = "optional";
		values[disabledColumnIndex] = "disabled";
		values[excelValueColumnIndex] = "excel value";
		values[excelDateColumnIndex] = "excel date";
		values[ecrfValueColumnIndex] = "ecrf value";
		values[stratificationColumnIndex] = "stratification";
		values[randomizeColumnIndex] = "randomize";
		values[jsVariableNameColumnIndex] = "variable name";
		values[jsValueExpressionColumnIndex] = "value expression";
		values[jsOutputExpressionColumnIndex] = "output expression";
		printRow(values, true);
		((XlsExporter) context.getExporter()).getInputFieldRowWriter().printHeaderRow();
		((XlsExporter) context.getExporter()).getSelectionSetValueRowWriter().printHeaderRow();
	}

	public void printRows(Collection<ProbandListEntryTagOutVO> listEntryTags) throws Throwable {
		Iterator<ProbandListEntryTagOutVO> it = listEntryTags.iterator();
		while (it.hasNext()) {
			ProbandListEntryTagOutVO listEntryTag = it.next();
			if (inputFieldIds.add(listEntryTag.getField().getId())) {
				((XlsExporter) context.getExporter()).getInputFieldRowWriter().printRows(listEntryTag.getField());
			}
			String[] values = new String[maxColumnIndex + 1];
			values[refColumnIndex] = listEntryTag.getRef();
			values[positionColumnIndex] = Long.toString(listEntryTag.getPosition());
			values[externalIdColumnIndex] = listEntryTag.getExternalId();
			values[inputFieldNameColumnIndex] = listEntryTag.getField().getNameL10nKey();
			values[titleColumnIndex] = listEntryTag.getTitleL10nKey();
			values[commentColumnIndex] = listEntryTag.getComment();
			values[optionalColumnIndex] = Boolean.toString(listEntryTag.getOptional());
			values[disabledColumnIndex] = Boolean.toString(listEntryTag.getDisabled());
			values[excelValueColumnIndex] = Boolean.toString(listEntryTag.getExcelValue());
			values[excelDateColumnIndex] = Boolean.toString(listEntryTag.getExcelDate());
			values[ecrfValueColumnIndex] = Boolean.toString(listEntryTag.getEcrfValue());
			values[stratificationColumnIndex] = Boolean.toString(listEntryTag.getStratification());
			values[randomizeColumnIndex] = Boolean.toString(listEntryTag.getRandomize());
			values[jsVariableNameColumnIndex] = listEntryTag.getJsVariableName();
			values[jsValueExpressionColumnIndex] = listEntryTag.getJsValueExpression();
			values[jsOutputExpressionColumnIndex] = listEntryTag.getJsOutputExpression();
			printRow(values);
		}
	}
}
