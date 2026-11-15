package org.phoenixctms.ctsms.executable.xls;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.fileprocessors.xls.RowProcessor;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InquiryInVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class InquiryRowProcessor extends RowProcessor {

	private final static String SHEET_NAME = "inquiries";
	private final static int REF_COLUMN_INDEX = 0;
	private final static int CATEGORY_COLUMN_INDEX = 1;
	private final static int POSITION_COLUMN_INDEX = 2;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 3;
	private final static int INPUT_FIELD_NAME_COLUMN_INDEX = 4;
	private final static int TITLE_COLUMN_INDEX = 5;
	private final static int COMMENT_COLUMN_INDEX = 6;
	private final static int ACTIVE_COLUMN_INDEX = 7;
	private final static int ACTIVE_SIGNUP_COLUMN_INDEX = 8;
	private final static int OPTIONAL_COLUMN_INDEX = 9;
	private final static int DISABLED_COLUMN_INDEX = 10;
	private final static int EXCEL_VALUE_COLUMN_INDEX = 11;
	private final static int EXCEL_DATE_COLUMN_INDEX = 12;
	private final static int JS_VARIABLE_NAME_COLUMN_INDEX = 13;
	private final static int JS_VALUE_EXPRESSION_COLUMN_INDEX = 14;
	private final static int JS_OUTPUT_EXPRESSION_COLUMN_INDEX = 15;
	private int refColumnIndex;
	private int categoryColumnIndex;
	private int positionColumnIndex;
	private int externalIdColumnIndex;
	private int inputFieldNameColumnIndex;
	private int titleColumnIndex;
	private int commentColumnIndex;
	private int activeColumnIndex;
	private int activeSignupColumnIndex;
	private int optionalColumnIndex;
	private int disabledColumnIndex;
	private int excelValueColumnIndex;
	private int excelDateColumnIndex;
	private int jsVariableNameColumnIndex;
	private int jsValueExpressionColumnIndex;
	private int jsOutputExpressionColumnIndex;
	private boolean noRefs;
	private LinkedHashSet<InquiryInVO> inquiries;
	private HashSet<Long> matchedInquiryIds;
	@Autowired
	protected InquiryDao inquiryDao;
	@Autowired
	protected TrialService trialService;

	public InquiryRowProcessor() {
		super();
		filterDupes = false;
		acceptCommentsIndex = 0;
		noRefs = true;
		inquiries = new LinkedHashSet<InquiryInVO>();
		matchedInquiryIds = new HashSet<Long>();
	}

	/**
	 * Writes the inquiries read from the sheet: matched inquiries are updated, the others created, and the
	 * trial's inquiries missing in the sheet are removed (marked for deferred deletion if there are values).
	 */
	public long addUpdateInquiries() throws Exception {
		long removeCount = 0l;
		Iterator<Inquiry> it = inquiryDao.findByTrialActiveJs(context.getEntityId(), null, null, false, null, null).iterator();
		while (it.hasNext()) {
			Inquiry inquiry = it.next();
			if (!inquiry.isDeferredDelete() && !matchedInquiryIds.contains(inquiry.getId())) {
				jobOutput.println("inquiry category " + inquiry.getCategory() + ", position " + inquiry.getPosition() + ", ref " + inquiry.getRef()
						+ " not in sheet, will be removed");
				removeCount++;
			}
		}
		Collection<InquiryOutVO> result = trialService.addUpdateInquiries(context.getAuth(), context.getEntityId(), inquiries);
		jobOutput.println(matchedInquiryIds.size() + " inquiries updated, " + (result.size() - matchedInquiryIds.size()) + " inquiries created, " + removeCount
				+ " inquiries removed");
		return result.size();
	}

	private String getActive(String[] values) {
		return getColumnValue(values, activeColumnIndex);
	}

	private String getActiveSignup(String[] values) {
		return getColumnValue(values, activeSignupColumnIndex);
	}

	private String getCategory(String[] values) {
		return getColumnValue(values, categoryColumnIndex);
	}

	private String getComment(String[] values) {
		return getColumnValue(values, commentColumnIndex);
	}

	private String getDisabled(String[] values) {
		return getColumnValue(values, disabledColumnIndex);
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

	public LinkedHashSet<InquiryInVO> getInquiries() {
		return inquiries;
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

	private String getOptional(String[] values) {
		return getColumnValue(values, optionalColumnIndex);
	}

	private String getPosition(String[] values) {
		return getColumnValue(values, positionColumnIndex);
	}

	private String getRef(String[] values) {
		return getColumnValue(values, refColumnIndex);
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	private String getTitle(String[] values) {
		return getColumnValue(values, titleColumnIndex);
	}

	@Override
	public void init() throws Throwable {
		super.init();
		refColumnIndex = REF_COLUMN_INDEX;
		categoryColumnIndex = CATEGORY_COLUMN_INDEX;
		positionColumnIndex = POSITION_COLUMN_INDEX;
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		inputFieldNameColumnIndex = INPUT_FIELD_NAME_COLUMN_INDEX;
		titleColumnIndex = TITLE_COLUMN_INDEX;
		commentColumnIndex = COMMENT_COLUMN_INDEX;
		activeColumnIndex = ACTIVE_COLUMN_INDEX;
		activeSignupColumnIndex = ACTIVE_SIGNUP_COLUMN_INDEX;
		optionalColumnIndex = OPTIONAL_COLUMN_INDEX;
		disabledColumnIndex = DISABLED_COLUMN_INDEX;
		excelValueColumnIndex = EXCEL_VALUE_COLUMN_INDEX;
		excelDateColumnIndex = EXCEL_DATE_COLUMN_INDEX;
		jsVariableNameColumnIndex = JS_VARIABLE_NAME_COLUMN_INDEX;
		jsValueExpressionColumnIndex = JS_VALUE_EXPRESSION_COLUMN_INDEX;
		jsOutputExpressionColumnIndex = JS_OUTPUT_EXPRESSION_COLUMN_INDEX;
		noRefs = true;
		inquiries.clear();
		matchedInquiryIds.clear();
		((XlsImporter) context.getImporter()).loadInputFields(context);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getRef(values))
				.append(getCategory(values))
				.append(getPosition(values))
				.append(getExternalId(values))
				.append(getInputFieldName(values))
				.append(getTitle(values))
				.append(getComment(values))
				.append(getActive(values))
				.append(getActiveSignup(values))
				.append(getOptional(values))
				.append(getDisabled(values))
				.append(getExcelValue(values))
				.append(getExcelDate(values))
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
		String category = getCategory(values);
		if (CommonUtil.isEmptyString(category)) {
			category = null;
		}
		Long position = Long.parseLong(getPosition(values));
		String ref = getRef(values);
		Inquiry inquiry = null;
		try {
			if (noRefs) {
				inquiry = inquiryDao.findByTrialCategoryPosition(context.getEntityId(), category, position).iterator().next();
			} else if (!CommonUtil.isEmptyString(ref)) {
				inquiry = inquiryDao.findByTrialRef(context.getEntityId(), ref).iterator().next();
			}
		} catch (NoSuchElementException e) {
		}
		String inputFieldName = getInputFieldName(values);
		InquiryInVO inquiryIn = new InquiryInVO();
		inquiryIn.setId(inquiry != null ? inquiry.getId() : null);
		inquiryIn.setVersion(inquiry != null ? inquiry.getVersion() : 0l);
		inquiryIn.setTrialId(context.getEntityId());
		inquiryIn.setRef(CommonUtil.isEmptyString(ref) ? null : ref);
		inquiryIn.setCategory(category);
		inquiryIn.setPosition(position);
		inquiryIn.setExternalId(getExternalId(values));
		inquiryIn.setFieldId(getInputFieldId(inputFieldName));
		inquiryIn.setTitle(getTitle(values));
		inquiryIn.setComment(getComment(values));
		inquiryIn.setActive(Boolean.parseBoolean(getActive(values)));
		inquiryIn.setActiveSignup(Boolean.parseBoolean(getActiveSignup(values)));
		inquiryIn.setOptional(Boolean.parseBoolean(getOptional(values)));
		inquiryIn.setDisabled(Boolean.parseBoolean(getDisabled(values)));
		inquiryIn.setExcelValue(Boolean.parseBoolean(getExcelValue(values)));
		inquiryIn.setExcelDate(Boolean.parseBoolean(getExcelDate(values)));
		String jsVariableName = getJsVariableName(values);
		boolean clearJs = CommonUtil.isEmptyString(jsVariableName)
				|| (!CommonUtil.isEmptyString(getCommentChar()) && jsVariableName.startsWith(getCommentChar()));
		inquiryIn.setJsVariableName(clearJs ? null : jsVariableName);
		inquiryIn.setJsValueExpression(clearJs ? null : getJsValueExpression(values));
		inquiryIn.setJsOutputExpression(clearJs ? null : getJsOutputExpression(values));
		String label = "inquiry category " + category + ", position " + position + ", field '" + inputFieldName + "'";
		if (inquiry != null && !matchedInquiryIds.add(inquiry.getId())) {
			throw new IllegalArgumentException(label + ": " + (noRefs ? "category and position" : "ref " + ref) + " matched more than once");
		}
		if (inquiries.add(inquiryIn)) {
			jobOutput.println(label + " read" + (inquiry != null ? " (existing inquiry)" : " (new inquiry)"));
			return 1;
		} else {
			jobOutput.println(label + " SKIPPED");
			return 0;
		}
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
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
