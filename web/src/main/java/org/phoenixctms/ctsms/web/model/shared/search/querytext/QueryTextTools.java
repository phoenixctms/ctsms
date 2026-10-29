package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

/**
 * Read-only lookups and the parse check the query agent calls in-process, with the session auth of the user who clicked Generate.
 */
public class QueryTextTools {

	public static final String SEARCH_TRIALS = "searchTrials";
	public static final String SEARCH_INPUT_FIELDS = "searchInputFields";
	public static final String SEARCH_TRIAL_INQUIRIES = "searchTrialInquiries";
	public static final String SEARCH_TRIAL_PROBAND_LIST_ATTRIBUTES = "searchTrialProbandListAttributes";
	public static final String SEARCH_TRIAL_ECRF_FIELDS = "searchTrialEcrfFields";
	public static final String PARSE_QUERY = "parseQuery";
	private static final int PAGE_SIZE = 100;
	private static final int MAX_SELECTION_VALUES = 50;
	private static final String NO_MATCHES = "no matches";
	private final AuthenticationVO auth;
	private final DBModule module;
	private final Integer maxCriterions;
	private String lastValidText;

	public QueryTextTools(AuthenticationVO auth, DBModule module, Integer maxCriterions) {
		this.auth = auth;
		this.module = module;
		this.maxCriterions = maxCriterions;
	}

	/**
	 * The last query text parseQuery accepted. Models tend to retype the validated query in their answer and lose operators or parentheses on the way.
	 */
	public String getLastValidText() {
		return lastValidText;
	}

	private static PSFVO createPsf(String filterField, String namePart) {
		PSFVO psf = new PSFVO();
		psf.setFirst(0);
		psf.setPageSize(PAGE_SIZE);
		if (!CommonUtil.isEmptyString(namePart)) {
			Map<String, String> filters = new HashMap<String, String>();
			filters.put(filterField, namePart.trim());
			psf.setFilters(filters);
		}
		return psf;
	}

	private static PSFVO createCountPsf() {
		PSFVO psf = new PSFVO();
		psf.setFirst(0);
		psf.setPageSize(1);
		psf.setUpdateRowCount(true);
		return psf;
	}

	private static long rowCount(Collection<?> rows, PSFVO psf) {
		return psf.getRowCount() != null ? psf.getRowCount() : (rows == null ? 0 : rows.size());
	}

	private static void addInputFields(LinkedHashMap<Long, InputFieldOutVO> fields, Collection<InputFieldOutVO> found) {
		if (found != null) {
			Iterator<InputFieldOutVO> it = found.iterator();
			while (it.hasNext()) {
				InputFieldOutVO field = it.next();
				if (!fields.containsKey(field.getId())) {
					fields.put(field.getId(), field);
				}
			}
		}
	}

	private String inputFieldUsage(long fieldId) throws AuthenticationException, AuthorisationException, ServiceException {
		PSFVO inquiryPsf = createCountPsf();
		long inquiries = rowCount(WebUtil.getServiceLocator().getInputFieldService().getInquiryList(auth, fieldId, inquiryPsf), inquiryPsf);
		PSFVO ecrfFieldPsf = createCountPsf();
		long ecrfFields = rowCount(WebUtil.getServiceLocator().getInputFieldService().getEcrfFieldList(auth, fieldId, ecrfFieldPsf), ecrfFieldPsf);
		PSFVO tagPsf = createCountPsf();
		long tags = rowCount(WebUtil.getServiceLocator().getInputFieldService().getProbandListEntryTagList(auth, fieldId, tagPsf), tagPsf);
		return "used by " + inquiries + " inquiries, " + ecrfFields + " eCRF fields, " + tags + " proband list attributes";
	}

	private static String errorText(Exception e) {
		return "error: " + e.getMessage();
	}

	@Tool(name = SEARCH_TRIALS, value = "Search trials whose name contains the given text. Returns one line per trial: id | name | title.")
	public String searchTrials(@P(name = "namePart", value = "part of the trial name, case-insensitive") String namePart) {
		try {
			Collection<TrialOutVO> trials = WebUtil.getServiceLocator().getTrialService().getTrialList(auth, null, null, createPsf("name", namePart));
			StringBuilder result = new StringBuilder();
			if (trials != null) {
				Iterator<TrialOutVO> it = trials.iterator();
				while (it.hasNext()) {
					TrialOutVO trial = it.next();
					appendLine(result, trial.getId() + " | " + trial.getName() + " | " + trial.getTitle());
				}
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	@Tool(name = SEARCH_INPUT_FIELDS, value = "Search input fields whose name or title contains the given text. Returns one line per field: "
			+ "id | name | title | type | how many inquiries, eCRF fields and proband list attributes use it, followed by the selectable option texts for selection fields. "
			+ "A field has data only in the collections that use it. Search again with other terms, such as an abbreviation or the full name in another language, to find all fitting fields.")
	public String searchInputFields(@P(name = "namePart", value = "part of the input field name or title, case-insensitive") String namePart) {
		try {
			// fields name the same measure differently, so the title is searched too
			LinkedHashMap<Long, InputFieldOutVO> fields = new LinkedHashMap<Long, InputFieldOutVO>();
			addInputFields(fields, WebUtil.getServiceLocator().getInputFieldService().getInputFieldList(auth, null, createPsf("nameL10nKey", namePart)));
			addInputFields(fields, WebUtil.getServiceLocator().getInputFieldService().getInputFieldList(auth, null, createPsf("titleL10nKey", namePart)));
			StringBuilder result = new StringBuilder();
			Iterator<InputFieldOutVO> it = fields.values().iterator();
			while (it.hasNext()) {
				InputFieldOutVO field = it.next();
				appendLine(result, field.getId() + " | " + field.getName() + " | " + field.getTitle() + " | " + fieldType(field) + " | " + inputFieldUsage(field.getId()));
				appendSelectionValues(result, field);
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	@Tool(name = SEARCH_TRIAL_INQUIRIES, value = "Search the inquiries of a trial whose input field name contains the given text. Returns one line per inquiry: id | uniqueName | field name | type.")
	public String searchTrialInquiries(@P(name = "trialId", value = "trial id") long trialId, @P(name = "namePart", value = "part of the input field name, case-insensitive") String namePart) {
		try {
			Collection<InquiryOutVO> inquiries = WebUtil.getServiceLocator().getTrialService().getInquiryList(auth, trialId, null, null, false,
					createPsf("field.nameL10nKey", namePart));
			StringBuilder result = new StringBuilder();
			if (inquiries != null) {
				Iterator<InquiryOutVO> it = inquiries.iterator();
				while (it.hasNext()) {
					InquiryOutVO inquiry = it.next();
					appendLine(result, inquiry.getId() + " | " + inquiry.getUniqueName() + " | " + fieldName(inquiry.getField()) + " | " + fieldType(inquiry.getField()));
				}
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	@Tool(name = SEARCH_TRIAL_PROBAND_LIST_ATTRIBUTES, value = "Search the proband list attributes of a trial whose input field name contains the given text. "
			+ "Returns one line per attribute: id | uniqueName | field name | type.")
	public String searchTrialProbandListAttributes(@P(name = "trialId", value = "trial id") long trialId, @P(name = "namePart", value = "part of the input field name, case-insensitive") String namePart) {
		try {
			Collection<ProbandListEntryTagOutVO> tags = WebUtil.getServiceLocator().getTrialService().getProbandListEntryTagList(auth, trialId,
					createPsf("field.nameL10nKey", namePart));
			StringBuilder result = new StringBuilder();
			if (tags != null) {
				Iterator<ProbandListEntryTagOutVO> it = tags.iterator();
				while (it.hasNext()) {
					ProbandListEntryTagOutVO tag = it.next();
					appendLine(result, tag.getId() + " | " + tag.getUniqueName() + " | " + fieldName(tag.getField()) + " | " + fieldType(tag.getField()));
				}
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	@Tool(name = SEARCH_TRIAL_ECRF_FIELDS, value = "Search the eCRF fields of a trial whose input field name contains the given text. Returns one line per eCRF field: id | uniqueName | field name | type.")
	public String searchTrialEcrfFields(@P(name = "trialId", value = "trial id") long trialId, @P(name = "namePart", value = "part of the input field name, case-insensitive") String namePart) {
		try {
			Collection<ECRFFieldOutVO> ecrfFields = WebUtil.getServiceLocator().getTrialService().getEcrfFieldList(auth, trialId, null, false,
					createPsf("field.nameL10nKey", namePart));
			StringBuilder result = new StringBuilder();
			if (ecrfFields != null) {
				Iterator<ECRFFieldOutVO> it = ecrfFields.iterator();
				while (it.hasNext()) {
					ECRFFieldOutVO ecrfField = it.next();
					appendLine(result, ecrfField.getId() + " | " + ecrfField.getUniqueName() + " | " + fieldName(ecrfField.getField()) + " | " + fieldType(ecrfField.getField()));
				}
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	@Tool(name = PARSE_QUERY, value = "Parse a query. Returns ok and the number of criteria, or the parse error to fix.")
	public String parseQuery(@P(name = "text", value = "the complete query text") String text) {
		try {
			Collection<CriterionInVO> criterions = WebUtil.getServiceLocator().getSearchService().parseCriterionText(auth, module, text == null ? "" : text, maxCriterions);
			lastValidText = text;
			return "ok: " + (criterions == null ? 0 : criterions.size()) + " criteria";
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	private static void appendLine(StringBuilder result, String line) {
		if (result.length() > 0) {
			result.append('\n');
		}
		result.append(line);
	}

	private static void appendSelectionValues(StringBuilder result, InputFieldOutVO field) {
		Collection<InputFieldSelectionSetValueOutVO> values = field.getSelectionSetValues();
		if (values == null || values.isEmpty()) {
			return;
		}
		StringBuilder options = new StringBuilder();
		int count = 0;
		Iterator<InputFieldSelectionSetValueOutVO> it = values.iterator();
		while (it.hasNext() && count < MAX_SELECTION_VALUES) {
			InputFieldSelectionSetValueOutVO value = it.next();
			if (options.length() > 0) {
				options.append(", ");
			}
			options.append(value.getValue());
			count++;
		}
		if (it.hasNext()) {
			options.append(", ...");
		}
		appendLine(result, "  options: " + options);
	}

	private static String fieldName(InputFieldOutVO field) {
		return field == null ? "" : field.getName();
	}

	private static String fieldType(InputFieldOutVO field) {
		return field == null || field.getFieldType() == null || field.getFieldType().getType() == null ? "" : field.getFieldType().getType().name();
	}
}
