package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.enumeration.CriterionRestriction;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CriterionRestrictionVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.IntermediateSetSummaryVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.shared.search.IntermediateSetSteps;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

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
	public static final String SEARCH_PROPERTY_VALUES = "searchPropertyValues";
	public static final String COLLECT_FACET = "collectFacet";
	public static final String PARSE_QUERY = "parseQuery";
	public static final String CHECK_SETS = "checkSets";
	private static final int PAGE_SIZE = 100;
	// the lines of the result sets and of the set steps checkSets lists
	private static final int MAX_SET_LINES = 60;
	private static final int MAX_STEP_LINES = 60;
	private static final int MAX_SET_EXPRESSION_LENGTH = 160;
	private static final String STEP_REFERENCE = "step {0}";
	private static final int MAX_INPUT_FIELDS = 30;
	// each checked input field costs up to three usage count queries
	private static final int MAX_CHECKED_INPUT_FIELDS = 100;
	private static final int MAX_SELECTION_VALUES = 50;
	private static final int MAX_OPTION_MATCHES = 500;
	private static final int TEXT_VALUE_PAGES = 5;
	private static final String TEXT_VALUE_SUFFIX = "stringValue";
	private static final String SELECTION_VALUE_SUFFIX = "selectionValues.value";
	private static final String BOOLEAN_VALUE_SUFFIX = "booleanValue";
	private static final int MAX_FACET_TERMS = 20;
	private static final int MIN_FACET_TERM_LENGTH = 2;
	// the usage count queries of one facet
	private static final int MAX_FACET_USAGE_COUNTS = 600;
	// a last word up to this length must end where the term ends
	private static final int SHORT_WORD_LENGTH = 3;
	// a first word up to this length must start where the term starts
	private static final int SHORT_CODE_LENGTH = 4;
	private static final Pattern FACET_PLACEHOLDER = Pattern.compile("\\{\\s*(F\\d+)((?:\\s*-\\s*B\\d+)*)\\s*\\}", Pattern.CASE_INSENSITIVE);
	private static final Pattern BRANCH_EXCLUSION = Pattern.compile("-\\s*(B\\d+)", Pattern.CASE_INSENSITIVE);
	private static final String NO_MATCHES = "no matches";
	private static final String NO_FIELD_COLLECTIONS = "error: this search has no inquiries, eCRF fields or proband list attributes to collect from";
	private final AuthenticationVO auth;
	private final DBModule module;
	private final Integer maxCriterions;
	private final Collection<CriterionPropertyVO> properties;
	private final boolean json;
	private final ArrayList<Facet> facets = new ArrayList<Facet>();
	private int facetCount;
	private String lastValidText;
	private Collection<CriterionInVO> lastValidCriterions;
	private HashMap<Long, CriterionTieVO> ties;

	public QueryTextTools(AuthenticationVO auth, DBModule module, Integer maxCriterions, Collection<CriterionPropertyVO> properties, boolean json) {
		this.json = json;
		this.auth = auth;
		this.module = module;
		this.maxCriterions = maxCriterions;
		this.properties = properties;
	}

	/**
	 * The last query text parseQuery accepted. Models tend to retype the validated query in their answer and lose operators or parentheses on the way.
	 */
	public String getLastValidText() {
		return lastValidText;
	}

	/**
	 * The criterions parsed from the last valid text, so the view does not have to parse it again.
	 */
	public Collection<CriterionInVO> getLastValidCriterions() {
		return lastValidCriterions;
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

	/**
	 * A collection that stores input field values: the criterion property binding the input field (its value is the field name)
	 * and the property path prefix of its value properties.
	 */
	private enum FieldCollection {

		INQUIRY("proband.inquiryValues.inquiry.field.id", "proband.inquiryValues.value.", "inquiryValues.value.stringValue"),
		ECRF_FIELD("proband.trialParticipations.ecrfValues.ecrfField.field.id", "proband.trialParticipations.ecrfValues.value.", "fieldValues.value.stringValue"),
		PROBAND_LIST_ATTRIBUTE("proband.trialParticipations.tagValues.tag.field.id", "proband.trialParticipations.tagValues.value.", "tagValues.value.stringValue");

		private final String fieldProperty;
		private final String valuePrefix;
		// the filter of the collection's list on the text of its values
		private final String textValueFilter;

		private FieldCollection(String fieldProperty, String valuePrefix, String textValueFilter) {
			this.fieldProperty = fieldProperty;
			this.valuePrefix = valuePrefix;
			this.textValueFilter = textValueFilter;
		}
	}

	private long usageCount(FieldCollection collection, long fieldId) throws AuthenticationException, AuthorisationException, ServiceException {
		PSFVO psf = createCountPsf();
		return rowCount(listCollection(collection, fieldId, psf), psf);
	}

	// the inquiries, eCRF fields or proband list attributes, of one input field or of all
	private Collection<?> listCollection(FieldCollection collection, Long fieldId, PSFVO psf) throws AuthenticationException, AuthorisationException, ServiceException {
		switch (collection) {
			case INQUIRY:
				return WebUtil.getServiceLocator().getInputFieldService().getInquiryList(auth, fieldId, psf);
			case ECRF_FIELD:
				return WebUtil.getServiceLocator().getInputFieldService().getEcrfFieldList(auth, fieldId, psf);
			default:
				return WebUtil.getServiceLocator().getInputFieldService().getProbandListEntryTagList(auth, fieldId, psf);
		}
	}

	private static InputFieldOutVO collectionField(Object item) {
		if (item instanceof InquiryOutVO) {
			return ((InquiryOutVO) item).getField();
		} else if (item instanceof ECRFFieldOutVO) {
			return ((ECRFFieldOutVO) item).getField();
		} else if (item instanceof ProbandListEntryTagOutVO) {
			return ((ProbandListEntryTagOutVO) item).getField();
		}
		return null;
	}

	// the operator name of the user's locale, if the property allows the restriction
	private static String restrictionName(CriterionPropertyVO propertyVO, CriterionRestriction restriction) {
		if (propertyVO.getValidRestrictions() == null) {
			return null;
		}
		Iterator<CriterionRestrictionVO> it = propertyVO.getValidRestrictions().iterator();
		while (it.hasNext()) {
			CriterionRestrictionVO restrictionVO = it.next();
			if (restrictionVO != null && restriction.equals(restrictionVO.getRestriction()) && !CommonUtil.isEmptyString(restrictionVO.getName())) {
				return restrictionVO.getName();
			}
		}
		return null;
	}

	// the value property that holds an answer of this field type
	private static String valuePropertySuffix(InputFieldOutVO field) {
		if (field == null || field.getFieldType() == null || field.getFieldType().getType() == null) {
			return null;
		}
		switch (field.getFieldType().getType()) {
			case SINGLE_LINE_TEXT:
			case MULTI_LINE_TEXT:
			case AUTOCOMPLETE:
				// an autocomplete answer is stored as text, its suggestions are no selected items
				return "stringValue";
			case CHECKBOX:
				return "booleanValue";
			case INTEGER:
				return "longValue";
			case FLOAT:
				return "floatValue";
			case DATE:
				return "dateValue";
			case TIMESTAMP:
				return "timestampValue";
			case TIME:
				return "timeValue";
			case FILE:
				return "fileValue.id";
			default:
				return "selectionValues.value";
		}
	}

	/**
	 * Lists the criterion left-hand sides of the collections that store this field, in the property names of the user's locale, together with the value
	 * property to compare, so the agent neither guesses collections nor value properties. Returns false if no collection of this search module stores the field.
	 */
	private boolean appendCollectionBindings(StringBuilder result, InputFieldOutVO field)
			throws AuthenticationException, AuthorisationException, ServiceException {
		boolean used = false;
		String valueSuffix = valuePropertySuffix(field);
		FieldCollection[] collections = FieldCollection.values();
		for (int i = 0; i < collections.length; i++) {
			FieldCollection collection = collections[i];
			CriterionPropertyVO fieldProperty = findProperty(collection.fieldProperty);
			// only count the collections the properties of this search module can bind
			if (fieldProperty == null || usageCount(collection, field.getId()) == 0) {
				continue;
			}
			if (!used) {
				appendLine(result, field.getId() + " | " + field.getName() + " | " + field.getTitle() + " | " + fieldType(field));
				used = true;
			}
			StringBuilder line = new StringBuilder("  ");
			line.append(fieldProperty.getName()).append(" = ").append(field.getName());
			CriterionPropertyVO valueProperty = valueSuffix == null ? null : findProperty(collection.valuePrefix + valueSuffix);
			if (valueProperty != null) {
				line.append(" ∧ ").append(valueProperty.getName());
				// a plain like is case-sensitive and misses spellings, so text lines name the operator to use
				String ilike = TEXT_VALUE_SUFFIX.equals(valueSuffix) ? restrictionName(valueProperty, CriterionRestriction.ILIKE) : null;
				line.append(ilike != null ? " " + ilike + " %...%" : " ...");
			}
			appendLine(result, line.toString());
		}
		return used;
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

	@Tool(name = SEARCH_INPUT_FIELDS, value = "Search input fields whose name, title or selectable option text contains the given text. "
			+ "Returns only fields that are stored by an inquiry, eCRF field or proband list attribute. Each match is the line id | name | title | type, "
			+ "then one indented line per collection that stores the field, such as: <field property> = <field name> ∧ <value property> ..., then the selectable options as \"value\" (option text): a selection value criterion compares the value, written without the quotes. "
			+ "Each indented line is one branch: copy its two properties, then complete the value criterion with an operator and a value. "
			+ "Combine the branches of every fitting field and collection with ∪. "
			+ "Search again with other terms, such as an abbreviation or the full name in another language, to find all fitting fields.")
	public String searchInputFields(@P(name = "namePart", value = "part of the input field name, title or option text, case-insensitive") String namePart) {
		try {
			// fields name the same measure differently, so the title is searched too;
			// brand or product names often sit only in a selectable option, not in the field name
			LinkedHashMap<Long, InputFieldOutVO> fields = new LinkedHashMap<Long, InputFieldOutVO>();
			addInputFields(fields, WebUtil.getServiceLocator().getInputFieldService().getInputFieldList(auth, null, createPsf("nameL10nKey", namePart)));
			addInputFields(fields, WebUtil.getServiceLocator().getInputFieldService().getInputFieldList(auth, null, createPsf("titleL10nKey", namePart)));
			addInputFieldsBySelectionValue(fields, namePart);
			StringBuilder result = new StringBuilder();
			int count = 0;
			int checked = 0;
			Iterator<InputFieldOutVO> it = fields.values().iterator();
			while (it.hasNext()) {
				// the usage counts cost up to three queries per field, so stop at the first fields in use
				if (count >= MAX_INPUT_FIELDS || checked >= MAX_CHECKED_INPUT_FIELDS) {
					appendLine(result, "more fields match, search again with a more specific text");
					break;
				}
				InputFieldOutVO field = it.next();
				checked++;
				if (appendCollectionBindings(result, field)) {
					appendSelectionValues(result, field);
					count++;
				}
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	private void addInputFieldsBySelectionValue(LinkedHashMap<Long, InputFieldOutVO> fields, String namePart)
			throws AuthenticationException, AuthorisationException, ServiceException {
		if (CommonUtil.isEmptyString(namePart)) {
			return;
		}
		// the values come sorted by name across all fields, and a learning autocomplete can hold many spellings of the same text,
		// so take many values to still reach the other fields with a matching option
		Collection<InputFieldSelectionSetValueOutVO> values = WebUtil.getServiceLocator().getToolsService().completeInputFieldSelectionSetValue(auth, namePart.trim(), null,
				Integer.valueOf(MAX_OPTION_MATCHES));
		if (values == null) {
			return;
		}
		Iterator<InputFieldSelectionSetValueOutVO> it = values.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO value = it.next();
			if (value != null && value.getField() != null && !fields.containsKey(value.getField().getId())) {
				fields.put(value.getField().getId(), value.getField());
			}
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

	@Tool(name = SEARCH_PROPERTY_VALUES, value = "Search the values of a property marked with " + SEARCH_PROPERTY_VALUES + " under Properties. "
			+ "Use this for catalog values and for answers already entered in the text or selection value properties of inquiries, eCRF fields or proband list attributes. "
			+ "Pass the full property name as listed, including the collection in parentheses. "
			+ "A selection value property lists only options of selection fields, then the fields offering a matching option, as field: \"value\" (option text). "
			+ "A text value property lists the stored texts, then the fields storing a matching text and the autocomplete fields suggesting it; compare all of them with their text value. "
			+ "Returns one line per value: the name for the expression format, or id | name for values that JSON gives by id.")
	public String searchPropertyValues(@P(name = "property", value = "the property as listed under Properties") String property,
			@P(name = "valuePart", value = "part of the value, case-insensitive") String valuePart) {
		CriterionPropertyVO propertyVO = findProperty(property);
		if (propertyVO == null || !QueryTextValues.isLookup(propertyVO)) {
			return "error: no property " + property + " with values to look up";
		}
		try {
			FieldCollection collection = valueCollection(propertyVO);
			boolean selection = collection != null && propertyVO.getProperty().endsWith(SELECTION_VALUE_SUFFIX);
			OptionMatches options = null;
			if (collection != null) {
				try {
					options = matchOptions(collection, valuePart);
				} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
					// the hints are optional, the values are still listed
				}
			}
			StringBuilder result = new StringBuilder();
			boolean ids = QueryTextValues.isEntityLookup(propertyVO);
			Iterator<QueryTextValues.Value> it = QueryTextValues.lookup(auth, propertyVO, valuePart).iterator();
			while (it.hasNext()) {
				QueryTextValues.Value value = it.next();
				// the plain selection values also hold the learned suggestions of autocomplete fields, which are never selected
				if (!selection || options == null || !options.autocompleteOnly(value.getName())) {
					appendLine(result, ids ? value.getKey() + " | " + value.getName() : value.getName());
				}
			}
			// the hits do not name their field
			if (selection) {
				appendHints(result, "fields offering a matching option (field: \"value\" (option text)):", options == null ? null : options.selectionHints);
			} else if (collection != null) {
				appendHints(result, "fields storing a matching text:", textValueFields(collection, valuePart));
				appendHints(result, "autocomplete fields suggesting the text (compare their text value):", options == null ? null : options.autocompleteHints);
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	private static void appendHints(StringBuilder result, String header, CharSequence hints) {
		if (hints != null && hints.length() > 0) {
			appendLine(result, header);
			result.append('\n').append(hints);
		}
	}

	/**
	 * The input fields whose stored text in this collection contains the text, so the agent can bind each of them. Empty if the lookup fails.
	 */
	private StringBuilder textValueFields(FieldCollection collection, String valuePart) throws AuthenticationException {
		StringBuilder fields = new StringBuilder();
		Iterator<InputFieldOutVO> it = textValueFieldMap(collection, valuePart).values().iterator();
		while (it.hasNext()) {
			appendLine(fields, "  " + it.next().getName());
		}
		return fields;
	}

	private LinkedHashMap<Long, InputFieldOutVO> textValueFieldMap(FieldCollection collection, String valuePart) throws AuthenticationException {
		LinkedHashMap<Long, InputFieldOutVO> names = new LinkedHashMap<Long, InputFieldOutVO>();
		if (CommonUtil.isEmptyString(valuePart)) {
			return names;
		}
		try {
			// one row per matching value, so page through the rows to reach distinct fields
			for (int page = 0; page < TEXT_VALUE_PAGES && names.size() < MAX_INPUT_FIELDS; page++) {
				PSFVO psf = createPsf(collection.textValueFilter, valuePart);
				psf.setFirst(page * PAGE_SIZE);
				Collection<?> items = listCollection(collection, null, psf);
				if (items == null || items.isEmpty()) {
					break;
				}
				Iterator<?> it = items.iterator();
				while (it.hasNext()) {
					InputFieldOutVO field = collectionField(it.next());
					if (field != null && !names.containsKey(field.getId())) {
						names.put(field.getId(), field);
					}
				}
				if (items.size() < PAGE_SIZE) {
					break;
				}
			}
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			// the hints are optional
		}
		return names;
	}

	// the collection of a text or selection value property
	private static FieldCollection valueCollection(CriterionPropertyVO propertyVO) {
		if (propertyVO == null || propertyVO.getProperty() == null) {
			return null;
		}
		String path = propertyVO.getProperty();
		FieldCollection[] collections = FieldCollection.values();
		for (int i = 0; i < collections.length; i++) {
			String prefix = collections[i].valuePrefix;
			if (path.equals(prefix + TEXT_VALUE_SUFFIX) || path.equals(prefix + SELECTION_VALUE_SUFFIX)) {
				return collections[i];
			}
		}
		return null;
	}

	/**
	 * The selection set values whose name contains a text, split by the type of their field.
	 */
	private static final class OptionMatches {

		private final HashSet<String> selectionValues = new HashSet<String>();
		private final HashSet<String> autocompleteValues = new HashSet<String>();
		private final StringBuilder selectionHints = new StringBuilder();
		private final StringBuilder autocompleteHints = new StringBuilder();

		// a learned suggestion has the same name and value, so the name search finds every one of them
		private boolean autocompleteOnly(String value) {
			return autocompleteValues.contains(value) && !selectionValues.contains(value);
		}
	}

	/**
	 * Finds the selection set values containing the text, of the fields the collection stores: the options of selection fields, to compare
	 * with the selection value property, and the suggestions of autocomplete fields, whose answers are text values.
	 */
	private OptionMatches matchOptions(FieldCollection collection, String valuePart)
			throws AuthenticationException, AuthorisationException, ServiceException {
		OptionMatches matches = new OptionMatches();
		if (CommonUtil.isEmptyString(valuePart)) {
			return matches;
		}
		Collection<InputFieldSelectionSetValueOutVO> values = WebUtil.getServiceLocator().getToolsService().completeInputFieldSelectionSetValue(auth, valuePart.trim(), null,
				Integer.valueOf(MAX_OPTION_MATCHES));
		if (values == null) {
			return matches;
		}
		HashMap<Long, Boolean> stored = new HashMap<Long, Boolean>();
		int selectionHints = 0;
		int autocompleteHints = 0;
		Iterator<InputFieldSelectionSetValueOutVO> it = values.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO value = it.next();
			if (value == null || value.getField() == null || CommonUtil.isEmptyString(value.getValue())) {
				continue;
			}
			boolean autocomplete = InputFieldType.AUTOCOMPLETE.name().equals(fieldType(value.getField()));
			(autocomplete ? matches.autocompleteValues : matches.selectionValues).add(value.getValue());
			Boolean isStored = stored.get(value.getField().getId());
			if (isStored == null) {
				// one count query per field, so a short text matching many fields does not check them all
				isStored = stored.size() < MAX_CHECKED_INPUT_FIELDS && usageCount(collection, value.getField().getId()) > 0;
				stored.put(value.getField().getId(), isStored);
			}
			if (!isStored) {
				continue;
			}
			String hint = "  " + value.getField().getName() + ": " + optionText(value);
			if (autocomplete && autocompleteHints < MAX_SELECTION_VALUES) {
				appendLine(matches.autocompleteHints, hint);
				autocompleteHints++;
			} else if (!autocomplete && selectionHints < MAX_SELECTION_VALUES) {
				appendLine(matches.selectionHints, hint);
				selectionHints++;
			}
		}
		return matches;
	}

	// by the name the expression format lists, or the property JSON lists
	private CriterionPropertyVO findProperty(String property) {
		if (properties == null || CommonUtil.isEmptyString(property)) {
			return null;
		}
		String name = property.trim();
		Iterator<CriterionPropertyVO> it = properties.iterator();
		while (it.hasNext()) {
			CriterionPropertyVO propertyVO = it.next();
			if (name.equalsIgnoreCase(propertyVO.getName()) || name.equalsIgnoreCase(propertyVO.getProperty())) {
				return propertyVO;
			}
		}
		return null;
	}

	/**
	 * A branch of a facet: one input field in one collection, with every value of it that matches a term of the facet.
	 */
	private static final class FacetBranch {

		private final FieldCollection collection;
		private final InputFieldOutVO field;
		// option value -> option text, as listed
		private final LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		private final LinkedHashSet<String> patterns = new LinkedHashSet<String>();
		private boolean checked;
		private String label;
		private String text;
		// the parsed criteria of the text, which checkSets finds in the result sets
		private Collection<CriterionInVO> criterions;
		private String error;

		private FacetBranch(FieldCollection collection, InputFieldOutVO field) {
			this.collection = collection;
			this.field = field;
		}
	}

	/**
	 * The branches collected for one facet of a request, combined with ∪.
	 */
	private static final class Facet {

		private final String name;
		private final ArrayList<FacetBranch> branches = new ArrayList<FacetBranch>();
		private final LinkedHashMap<Long, InputFieldOutVO> namedFields = new LinkedHashMap<Long, InputFieldOutVO>();
		private final HashMap<String, Boolean> used = new HashMap<String, Boolean>();
		private final LinkedHashMap<String, FacetBranch> branchMap = new LinkedHashMap<String, FacetBranch>();
		private String label;
		private String text;
		private int terms;
		private boolean incomplete;
		// the branches a placeholder of the query leaves out
		private final HashSet<String> excluded = new HashSet<String>();

		private Facet(String name) {
			this.name = name;
		}
	}

	@Tool(name = COLLECT_FACET, value = "Collect every inquiry, eCRF field and proband list attribute that stores the answer of one facet, such as a condition or a device, "
			+ "by every given spelling: the matching options of selection fields, the stored texts of text fields and checkbox fields named like a term. "
			+ "Returns one line per branch, the fields named like a term that it could not compare, and the group: all branches combined with ∪, ready to copy into the query.")
	public String collectFacet(@P(name = "facet", value = "a short name of the facet, such as type 1 diabetes") String facet,
			@P(name = "terms", value = "every spelling of the requested answer: the English and German name, abbreviations, codes, Arabic and Roman numerals, brand, product and manufacturer names") List<String> terms) {
		try {
			if (!hasFieldCollections()) {
				return NO_FIELD_COLLECTIONS;
			}
			Facet result = buildFacet(facet, terms);
			// a facet collected again replaces the earlier one
			Iterator<Facet> it = facets.iterator();
			while (it.hasNext()) {
				Facet earlier = it.next();
				if (earlier.name.equalsIgnoreCase(result.name)) {
					result.label = earlier.label;
					it.remove();
				}
			}
			if (result.label == null) {
				result.label = "F" + (++facetCount);
			}
			if (result.text != null) {
				facets.add(result);
			}
			return describeFacet(result, true);
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	/**
	 * The same collection for the REST API, which keeps no facets between calls.
	 */
	public String collectFacetText(String facet, List<String> terms) throws AuthenticationException, AuthorisationException, ServiceException {
		if (!hasFieldCollections()) {
			return NO_FIELD_COLLECTIONS;
		}
		Facet result = buildFacet(facet, terms);
		result.label = "F";
		return describeFacet(result, false);
	}

	// only the proband search binds inquiries, eCRF fields and proband list attributes
	private boolean hasFieldCollections() {
		FieldCollection[] collections = FieldCollection.values();
		for (int i = 0; i < collections.length; i++) {
			if (findProperty(collections[i].fieldProperty) != null) {
				return true;
			}
		}
		return false;
	}

	private Facet buildFacet(String name, List<String> terms) throws AuthenticationException, AuthorisationException, ServiceException {
		Facet facet = new Facet(CommonUtil.isEmptyString(name) ? "facet" : name.trim());
		LinkedHashMap<String, String> spellings = new LinkedHashMap<String, String>();
		if (terms != null) {
			Iterator<String> it = terms.iterator();
			while (it.hasNext()) {
				String term = it.next();
				// a single character matches nearly everything
				if (term != null && term.trim().length() >= MIN_FACET_TERM_LENGTH && term.indexOf('\n') < 0) {
					String key = term.trim().toLowerCase(Locale.ROOT);
					if (!spellings.containsKey(key) && spellings.size() < MAX_FACET_TERMS) {
						spellings.put(key, term.trim());
					}
				}
			}
		}
		Iterator<String> it = spellings.values().iterator();
		while (it.hasNext()) {
			String term = it.next();
			collectOptions(facet, term);
			collectTexts(facet, term);
			collectNamedFields(facet, term);
		}
		facet.branches.addAll(facet.branchMap.values());
		Iterator<FacetBranch> pruneIt = facet.branches.iterator();
		while (pruneIt.hasNext()) {
			prunePatterns(pruneIt.next().patterns);
		}
		StringBuilder group = new StringBuilder();
		int count = 0;
		Iterator<FacetBranch> branchIt = facet.branches.iterator();
		while (branchIt.hasNext()) {
			FacetBranch branch = branchIt.next();
			branch.label = "B" + (++count);
			String text = branchText(branch);
			try {
				// a branch that does not parse alone, e.g. a field name that is not unique, would break the whole group
				branch.criterions = WebUtil.getServiceLocator().getSearchService().parseCriterionText(auth, module, text, true, null);
				branch.text = text;
				group.append(group.length() == 0 ? "(\n" : "\n∪\n").append(text);
			} catch (ServiceException | IllegalArgumentException e) {
				branch.error = e.getMessage();
			}
		}
		if (group.length() > 0) {
			group.append("\n)");
			Collection<CriterionInVO> parsed = WebUtil.getServiceLocator().getSearchService().parseCriterionText(auth, module, group.toString(), true, null);
			facet.terms = parsed == null ? 0 : parsed.size();
			// JSON gives the field by id, while the editor print of an expression adds position numbers that do not fit into another query
			facet.text = json ? formatCriterions(parsed) : null;
			if (CommonUtil.isEmptyString(facet.text)) {
				facet.text = group.toString();
			}
		}
		return facet;
	}

	// prints parsed criterions the way the editor shows them, in the format of the prompt
	private String formatCriterions(Collection<CriterionInVO> parsed) throws AuthenticationException, AuthorisationException, ServiceException {
		return formatCriterions(parsed, json);
	}

	private String formatCriterions(Collection<CriterionInVO> parsed, boolean asJson) throws AuthenticationException, AuthorisationException, ServiceException {
		if (parsed == null || parsed.isEmpty()) {
			return null;
		}
		ArrayList<CriterionInVO> criterions = new ArrayList<CriterionInVO>(parsed);
		for (int i = 0; i < criterions.size(); i++) {
			criterions.get(i).setPosition(CommonUtil.LIST_INITIAL_POSITION + i);
		}
		return WebUtil.getServiceLocator().getSearchService().formatCriterionText(auth, module, new HashSet<CriterionInVO>(criterions), asJson);
	}

	private FacetBranch facetBranch(Facet facet, FieldCollection collection, InputFieldOutVO field) {
		String key = collection.name() + ":" + field.getId();
		FacetBranch branch = facet.branchMap.get(key);
		if (branch == null) {
			branch = new FacetBranch(collection, field);
			facet.branchMap.put(key, branch);
		}
		return branch;
	}

	// whether the collection stores the field, counted once per field and collection
	private boolean isStored(Facet facet, FieldCollection collection, InputFieldOutVO field) throws AuthenticationException, AuthorisationException, ServiceException {
		String key = collection.name() + ":" + field.getId();
		Boolean stored = facet.used.get(key);
		if (stored == null) {
			if (facet.used.size() >= MAX_FACET_USAGE_COUNTS) {
				facet.incomplete = true;
				return false;
			}
			stored = usageCount(collection, field.getId()) > 0;
			facet.used.put(key, stored);
		}
		return stored;
	}

	// the options of selection fields whose text or value names the term
	private void collectOptions(Facet facet, String term) throws AuthenticationException, AuthorisationException, ServiceException {
		Collection<InputFieldSelectionSetValueOutVO> values = WebUtil.getServiceLocator().getToolsService().completeInputFieldSelectionSetValue(auth, term, null,
				Integer.valueOf(MAX_OPTION_MATCHES));
		if (values == null) {
			return;
		}
		if (values.size() >= MAX_OPTION_MATCHES) {
			facet.incomplete = true;
		}
		Iterator<InputFieldSelectionSetValueOutVO> it = values.iterator();
		while (it.hasNext()) {
			InputFieldSelectionSetValueOutVO value = it.next();
			// an autocomplete answer is stored as text, which collectTexts finds
			if (value == null || value.getField() == null || CommonUtil.isEmptyString(value.getValue()) || value.getValue().indexOf('\n') >= 0
					|| InputFieldType.AUTOCOMPLETE.name().equals(fieldType(value.getField()))) {
				continue;
			}
			// the name search also finds "Typ II" for "Typ I"
			if (!matchesTerm(value.getName(), term) && !matchesTerm(value.getValue(), term)) {
				continue;
			}
			FieldCollection[] collections = FieldCollection.values();
			for (int i = 0; i < collections.length; i++) {
				if (findProperty(collections[i].fieldProperty) != null && findProperty(collections[i].valuePrefix + SELECTION_VALUE_SUFFIX) != null
						&& isStored(facet, collections[i], value.getField())) {
					facetBranch(facet, collections[i], value.getField()).options.put(value.getValue(), optionText(value));
				}
			}
		}
	}

	// the text fields storing the term
	private void collectTexts(Facet facet, String term) throws AuthenticationException, AuthorisationException, ServiceException {
		FieldCollection[] collections = FieldCollection.values();
		for (int i = 0; i < collections.length; i++) {
			CriterionPropertyVO textProperty = findProperty(collections[i].valuePrefix + TEXT_VALUE_SUFFIX);
			if (findProperty(collections[i].fieldProperty) == null || textProperty == null) {
				continue;
			}
			List<String> patterns = likePatterns(textProperty, term);
			if (patterns.isEmpty()) {
				continue;
			}
			// the fields are found by the term anywhere in a text, such as E10 in the ATC code V03AE10, so when the term is not a word
			// in every text, each pattern is checked against the texts of the field
			boolean anywhere = patterns.size() == 1 && patterns.get(0).equalsIgnoreCase("%" + term + "%");
			Iterator<InputFieldOutVO> it = textValueFieldMap(collections[i], term).values().iterator();
			while (it.hasNext()) {
				InputFieldOutVO field = it.next();
				List<String> fieldPatterns = anywhere ? patterns : storedPatterns(facet, collections[i], field, patterns);
				if (!fieldPatterns.isEmpty()) {
					facetBranch(facet, collections[i], field).patterns.addAll(fieldPatterns);
				}
			}
		}
	}

	/**
	 * The patterns whose text, without the wildcards, the field stores somewhere, with one count query each.
	 */
	private List<String> storedPatterns(Facet facet, FieldCollection collection, InputFieldOutVO field, List<String> patterns) throws AuthenticationException {
		ArrayList<String> stored = new ArrayList<String>();
		Iterator<String> it = patterns.iterator();
		while (it.hasNext()) {
			String pattern = it.next();
			if (facet.used.size() >= MAX_FACET_USAGE_COUNTS) {
				// too many queries: keep the pattern unchecked, it still matches only the term as a word
				facet.incomplete = true;
				stored.add(pattern);
				continue;
			}
			PSFVO psf = createCountPsf();
			Map<String, String> filters = new HashMap<String, String>();
			filters.put(collection.textValueFilter, pattern.replace("%", ""));
			psf.setFilters(filters);
			try {
				facet.used.put("pattern:" + collection.name() + ":" + field.getId() + ":" + pattern, Boolean.TRUE);
				if (rowCount(listCollection(collection, field.getId(), psf), psf) > 0) {
					stored.add(pattern);
				}
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				stored.add(pattern);
			}
		}
		return stored;
	}

	/**
	 * The like patterns that find the term in the stored texts of a text value property. Just %term% when every stored text
	 * containing it holds the term as a word, otherwise one pattern per way the word is written, so "Typ I" does not match "Typ II".
	 */
	private List<String> likePatterns(CriterionPropertyVO textProperty, String term) throws AuthenticationException, AuthorisationException, ServiceException {
		LinkedHashSet<String> patterns = new LinkedHashSet<String>();
		boolean partial = false;
		// like is not case-sensitive, so "Libre" and "libre" are the same pattern
		HashSet<String> seen = new HashSet<String>();
		String lowerTerm = term.toLowerCase(Locale.ROOT);
		Iterator<QueryTextValues.Value> it = QueryTextValues.lookup(auth, textProperty, term).iterator();
		while (it.hasNext()) {
			String text = it.next().getName();
			String lower = text.toLowerCase(Locale.ROOT);
			if (lower.length() != text.length()) {
				continue;
			}
			int index = lower.indexOf(lowerTerm);
			while (index >= 0) {
				int end = index + term.length();
				if (isTermStart(text, index, term) && isTermEnd(text, end, term)) {
					String pattern = wordPattern(text, index, end);
					if (pattern != null && seen.add(pattern.toLowerCase(Locale.ROOT))) {
						patterns.add(pattern);
					}
				} else {
					partial = true;
				}
				index = lower.indexOf(lowerTerm, index + 1);
			}
		}
		ArrayList<String> result = new ArrayList<String>();
		if (!patterns.isEmpty()) {
			if (partial) {
				result.addAll(patterns);
			} else {
				result.add("%" + term + "%");
			}
		}
		return result;
	}

	// %ypso% already matches every text that %ypsomed% or % ypsopump % match, so these only cost query terms
	private static void prunePatterns(LinkedHashSet<String> patterns) {
		ArrayList<String> stems = new ArrayList<String>();
		Iterator<String> it = patterns.iterator();
		while (it.hasNext()) {
			String pattern = it.next();
			if (pattern.length() > 2 && pattern.startsWith("%") && pattern.endsWith("%") && pattern.indexOf('%', 1) == pattern.length() - 1) {
				stems.add(pattern.substring(1, pattern.length() - 1).toLowerCase(Locale.ROOT));
			}
		}
		it = patterns.iterator();
		while (it.hasNext()) {
			String pattern = it.next();
			String literal = pattern.replace("%", "").toLowerCase(Locale.ROOT);
			for (int i = 0; i < stems.size(); i++) {
				String stem = stems.get(i);
				if (literal.contains(stem) && !literal.equals(stem)) {
					it.remove();
					break;
				}
			}
		}
	}

	// the term with the character before and after it, which a like cannot express as a word boundary otherwise
	private static String wordPattern(String text, int index, int end) {
		StringBuilder pattern = new StringBuilder();
		if (index > 0) {
			char before = text.charAt(index - 1);
			if (!isPatternChar(before)) {
				return null;
			}
			pattern.append('%').append(before);
		}
		pattern.append(text, index, end);
		if (end < text.length()) {
			char after = text.charAt(end);
			if (!isPatternChar(after)) {
				return null;
			}
			pattern.append(after).append('%');
		}
		return pattern.toString();
	}

	// a character a pattern can hold literally on one line
	private static boolean isPatternChar(char c) {
		return c >= ' ' && c != '%' && c != '_';
	}

	// a short first word or code must start a word, so "IDDM" does not match "NIDDM"; a longer brand like "Libre" also matches "FreestyleLibre"
	private static boolean isTermStart(String text, int index, String term) {
		if (index == 0 || !Character.isLetterOrDigit(text.charAt(index - 1))) {
			return true;
		}
		String trimmed = term.trim();
		int space = trimmed.indexOf(' ');
		String firstWord = space < 0 ? trimmed : trimmed.substring(0, space);
		return firstWord.length() > SHORT_CODE_LENGTH;
	}

	// a short last word or a number must end there, so "Typ 1" does not match "Typ 10"; a longer stem like "Ypso" may go on
	private static boolean isTermEnd(String text, int end, String term) {
		if (end >= text.length() || !Character.isLetterOrDigit(text.charAt(end))) {
			return true;
		}
		String trimmed = term.trim();
		String lastWord = trimmed.substring(trimmed.lastIndexOf(' ') + 1);
		return lastWord.length() > SHORT_WORD_LENGTH && !Character.isDigit(lastWord.charAt(lastWord.length() - 1));
	}

	private static boolean matchesTerm(String text, String term) {
		if (CommonUtil.isEmptyString(text) || CommonUtil.isEmptyString(term)) {
			return false;
		}
		String lower = text.toLowerCase(Locale.ROOT);
		String lowerTerm = term.trim().toLowerCase(Locale.ROOT);
		if (lower.length() != text.length()) {
			return lower.contains(lowerTerm);
		}
		int index = lower.indexOf(lowerTerm);
		while (index >= 0) {
			if (isTermStart(text, index, term) && isTermEnd(text, index + lowerTerm.length(), term)) {
				return true;
			}
			index = lower.indexOf(lowerTerm, index + 1);
		}
		return false;
	}

	// checkbox fields named like the term are a branch with true, other fields named like it are listed to check
	private void collectNamedFields(Facet facet, String term) throws AuthenticationException, AuthorisationException, ServiceException {
		LinkedHashMap<Long, InputFieldOutVO> fields = new LinkedHashMap<Long, InputFieldOutVO>();
		addInputFields(fields, WebUtil.getServiceLocator().getInputFieldService().getInputFieldList(auth, null, createPsf("nameL10nKey", term)));
		addInputFields(fields, WebUtil.getServiceLocator().getInputFieldService().getInputFieldList(auth, null, createPsf("titleL10nKey", term)));
		Iterator<InputFieldOutVO> it = fields.values().iterator();
		while (it.hasNext()) {
			InputFieldOutVO field = it.next();
			if (!matchesTerm(field.getName(), term) && !matchesTerm(field.getTitle(), term)) {
				continue;
			}
			boolean checkbox = InputFieldType.CHECKBOX.name().equals(fieldType(field));
			FieldCollection[] collections = FieldCollection.values();
			for (int i = 0; i < collections.length; i++) {
				if (findProperty(collections[i].fieldProperty) == null || !isStored(facet, collections[i], field)) {
					continue;
				}
				if (checkbox && findProperty(collections[i].valuePrefix + BOOLEAN_VALUE_SUFFIX) != null) {
					facetBranch(facet, collections[i], field).checked = true;
				} else if (!facet.namedFields.containsKey(field.getId()) && facet.namedFields.size() < MAX_INPUT_FIELDS) {
					facet.namedFields.put(field.getId(), field);
				}
			}
		}
	}

	// field property = field name ∧ the value criteria, several of them combined with ∨ in parentheses
	private String branchText(FacetBranch branch) {
		ArrayList<String> values = new ArrayList<String>();
		String prefix = branch.collection.valuePrefix;
		if (branch.checked) {
			values.add(findProperty(prefix + BOOLEAN_VALUE_SUFFIX).getName() + " = true");
		}
		if (!branch.options.isEmpty()) {
			String property = findProperty(prefix + SELECTION_VALUE_SUFFIX).getName();
			Iterator<String> it = branch.options.keySet().iterator();
			while (it.hasNext()) {
				values.add(property + " = " + it.next());
			}
		}
		if (!branch.patterns.isEmpty()) {
			CriterionPropertyVO textProperty = findProperty(prefix + TEXT_VALUE_SUFFIX);
			String ilike = restrictionName(textProperty, CriterionRestriction.ILIKE);
			Iterator<String> it = branch.patterns.iterator();
			while (it.hasNext()) {
				values.add(textProperty.getName() + " " + ilike + " " + it.next());
			}
		}
		StringBuilder text = new StringBuilder();
		text.append(findProperty(branch.collection.fieldProperty).getName()).append(" = ").append(branch.field.getName()).append("\n∧\n");
		if (values.size() > 1) {
			text.append("(\n");
		}
		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				text.append("\n∨\n");
			}
			text.append(values.get(i));
		}
		if (values.size() > 1) {
			text.append("\n)");
		}
		return text.toString();
	}

	private String describeFacet(Facet facet, boolean placeholder) {
		StringBuilder result = new StringBuilder();
		int valid = 0;
		Iterator<FacetBranch> it = facet.branches.iterator();
		while (it.hasNext()) {
			if (it.next().text != null) {
				valid++;
			}
		}
		appendLine(result, "facet " + facet.label + " " + facet.name + ": " + valid + " branches, " + facet.terms + " query terms"
				+ (maxCriterions != null ? " (a query takes up to " + maxCriterions + ")" : ""));
		it = facet.branches.iterator();
		while (it.hasNext()) {
			FacetBranch branch = it.next();
			StringBuilder line = new StringBuilder("  ");
			line.append(branch.label).append(' ').append(findProperty(branch.collection.fieldProperty).getName()).append(" = ").append(branch.field.getName());
			if (!CommonUtil.isEmptyString(branch.field.getTitle()) && !branch.field.getTitle().equals(branch.field.getName())) {
				line.append(" (").append(branch.field.getTitle()).append(')');
			}
			line.append(':');
			if (branch.checked) {
				line.append(" checked");
			}
			if (!branch.options.isEmpty()) {
				line.append(" options ");
				Iterator<String> optionIt = branch.options.values().iterator();
				while (optionIt.hasNext()) {
					line.append(optionIt.next());
					if (optionIt.hasNext()) {
						line.append(", ");
					}
				}
			}
			if (!branch.patterns.isEmpty()) {
				line.append(" texts like ");
				Iterator<String> patternIt = branch.patterns.iterator();
				while (patternIt.hasNext()) {
					line.append(patternIt.next());
					if (patternIt.hasNext()) {
						line.append(", ");
					}
				}
			}
			if (branch.error != null) {
				line.append(" left out, does not parse: ").append(branch.error);
			}
			appendLine(result, line.toString());
		}
		if (!facet.namedFields.isEmpty()) {
			appendLine(result, "fields named like a term, not compared (add a branch if one stores the answer):");
			Iterator<InputFieldOutVO> fieldIt = facet.namedFields.values().iterator();
			while (fieldIt.hasNext()) {
				InputFieldOutVO field = fieldIt.next();
				appendLine(result, "  " + field.getName() + " | " + field.getTitle() + " | " + fieldType(field));
			}
		}
		if (facet.incomplete) {
			appendLine(result, "a term matched too many values, so the collection may be incomplete: use longer terms");
		}
		if (facet.text == null) {
			appendLine(result, NO_MATCHES);
		} else if (placeholder && json) {
			// the model writes the placeholder instead of retyping every branch, which is slow and loses branches
			appendLine(result, "write \"{" + facet.label + "}\" as a string element of the JSON array for the group of all branches, or \"{" + facet.label
					+ " -B2 -B5}\" to leave out branches that do not mean the requested answer and say so in the Note, such as [\"{F1}\", {\"tie\": \"INTERSECT\"}, \"{F2 -B4}\"]."
					+ " parseQuery and the answer expand it, and the array order counts, so give no positions.");
		} else if (placeholder) {
			appendLine(result, "write {" + facet.label + "} in the query for the group of all branches, or {" + facet.label
					+ " -B2 -B5} to leave out branches that do not mean the requested answer and say so in the Note. parseQuery and the answer expand it.");
		} else {
			appendLine(result, "group " + facet.label + ", copy it into the query as a whole; leave out a branch only if it does not mean the requested answer, and say so in the Note:");
			appendLine(result, facet.text);
		}
		return result.toString();
	}

	// the collected branches the query does not bind, by field property and field
	private String missingBranches(Collection<CriterionInVO> criterions) {
		StringBuilder missing = new StringBuilder();
		Iterator<Facet> facetIt = facets.iterator();
		while (facetIt.hasNext()) {
			Facet facet = facetIt.next();
			Iterator<FacetBranch> it = facet.branches.iterator();
			while (it.hasNext()) {
				FacetBranch branch = it.next();
				if (branch.text != null && !facet.excluded.contains(branch.label) && !bindsField(criterions, findProperty(branch.collection.fieldProperty), branch.field)) {
					appendLine(missing, "  " + facet.label + " " + branch.label + " " + findProperty(branch.collection.fieldProperty).getName() + " = " + branch.field.getName());
				}
			}
		}
		return missing.toString();
	}

	private static boolean bindsField(Collection<CriterionInVO> criterions, CriterionPropertyVO fieldProperty, InputFieldOutVO field) {
		if (criterions == null || fieldProperty == null) {
			return false;
		}
		Iterator<CriterionInVO> it = criterions.iterator();
		while (it.hasNext()) {
			CriterionInVO criterion = it.next();
			if (criterion.getPropertyId() != null && fieldProperty.getId() == criterion.getPropertyId().longValue() && Long.valueOf(field.getId()).equals(criterion.getLongValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Replaces each {F1} or {F1 -B2 -B5} in the text by the group of that collected facet, without the named branches.
	 * In JSON, a placeholder is a string element of the array.
	 */
	public String expandFacets(String text) {
		if (text == null || facets.isEmpty() || text.indexOf('{') < 0) {
			return text;
		}
		if (json) {
			return expandJsonFacets(text);
		}
		Matcher matcher = FACET_PLACEHOLDER.matcher(text);
		StringBuffer expanded = new StringBuffer();
		while (matcher.find()) {
			Facet facet = findFacet(matcher.group(1));
			String group = facet == null ? null : groupText(facet, matcher.group(2));
			matcher.appendReplacement(expanded, Matcher.quoteReplacement(group != null ? group : matcher.group()));
		}
		matcher.appendTail(expanded);
		return expanded.toString();
	}

	private Facet findFacet(String label) {
		Iterator<Facet> it = facets.iterator();
		while (it.hasNext()) {
			Facet facet = it.next();
			if (facet.label.equalsIgnoreCase(label)) {
				return facet;
			}
		}
		return null;
	}

	/**
	 * Puts the criteria of each group in place of its placeholder element. The positions are dropped, so the parser takes the array order,
	 * as the groups bring their own numbering. Returns the text unchanged if it is no JSON array or holds no placeholder.
	 */
	private String expandJsonFacets(String text) {
		try {
			JsonReader reader = new JsonReader(new StringReader(text));
			// the editor's leading comment lines
			reader.setLenient(true);
			JsonElement root = JsonParser.parseReader(reader);
			if (root == null || !root.isJsonArray()) {
				return text;
			}
			JsonArray expanded = new JsonArray();
			boolean placeholders = false;
			Iterator<JsonElement> it = root.getAsJsonArray().iterator();
			while (it.hasNext()) {
				JsonElement element = it.next();
				Matcher matcher = element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()
						? FACET_PLACEHOLDER.matcher(element.getAsString().trim())
						: null;
				Facet facet = matcher != null && matcher.matches() ? findFacet(matcher.group(1)) : null;
				String group = facet == null ? null : groupText(facet, matcher.group(2));
				String groupJson = group == null ? null
						: formatCriterions(WebUtil.getServiceLocator().getSearchService().parseCriterionText(auth, module, group, false, null), true);
				if (CommonUtil.isEmptyString(groupJson)) {
					expanded.add(element);
				} else {
					expanded.addAll(JsonParser.parseString(groupJson).getAsJsonArray());
					placeholders = true;
				}
			}
			if (!placeholders) {
				return text;
			}
			Iterator<JsonElement> objectIt = expanded.iterator();
			while (objectIt.hasNext()) {
				JsonElement element = objectIt.next();
				if (element != null && element.isJsonObject()) {
					element.getAsJsonObject().remove("position");
				}
			}
			return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(expanded);
		} catch (Exception e) {
			// not JSON the expansion understands: the parser reports what is wrong
			return text;
		}
	}

	// the group without the branches named like -B2 -B5, null if none is left
	private static String groupText(Facet facet, String exclusions) {
		facet.excluded.clear();
		Matcher matcher = BRANCH_EXCLUSION.matcher(exclusions == null ? "" : exclusions);
		while (matcher.find()) {
			facet.excluded.add(matcher.group(1).toUpperCase(Locale.ROOT));
		}
		StringBuilder group = new StringBuilder();
		Iterator<FacetBranch> it = facet.branches.iterator();
		while (it.hasNext()) {
			FacetBranch branch = it.next();
			if (branch.text != null && !facet.excluded.contains(branch.label)) {
				group.append(group.length() == 0 ? "(\n" : "\n∪\n").append(branch.text);
			}
		}
		return group.length() == 0 ? null : group.append("\n)").toString();
	}

	@Tool(name = PARSE_QUERY, value = "Parse a query. Returns ok and the number of criteria, or the parse error to fix. "
			+ "After ok, it lists the branches of collected facets that the query does not contain.")
	public String parseQuery(@P(name = "text", value = "the complete query text") String text) {
		try {
			String expanded = expandFacets(text == null ? "" : text);
			Collection<CriterionInVO> criterions = WebUtil.getServiceLocator().getSearchService().parseCriterionText(auth, module, expanded, true, maxCriterions);
			lastValidText = expanded;
			lastValidCriterions = criterions;
			String result = "ok: " + (criterions == null ? 0 : criterions.size()) + " criteria";
			String missing = missingBranches(criterions);
			if (missing.length() > 0) {
				result += "\nthese collected branches are not in the query: add them, or name each in the Note with why it does not mean the requested answer:\n" + missing;
			}
			return result;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	@Tool(name = CHECK_SETS, value = "Count the records of a query the way the Intermediate Sets tab does: each result set (A, B, ...) and each ∪, ∩ or ∖ step "
			+ "in evaluation order, with warnings for empty sets, an empty ∩, a ∪ branch that adds nothing or a ∖ that removes nothing. "
			+ "Counting is costly: call it only when the request depends on the counts, such as using another criterion when a set is empty.")
	public String checkSets(@P(name = "text", value = "the complete query text") String text) {
		try {
			String expanded = expandFacets(text == null ? "" : text);
			Collection<CriterionInVO> criterions = WebUtil.getServiceLocator().getSearchService().parseCriterionText(auth, module, expanded, true, maxCriterions);
			// the parsed criterions come in query order but without positions, which the set analysis sorts by
			if (criterions != null) {
				long position = CommonUtil.LIST_INITIAL_POSITION;
				Iterator<CriterionInVO> positionIt = criterions.iterator();
				while (positionIt.hasNext()) {
					positionIt.next().setPosition(position++);
				}
			}
			IntermediateSetSummaryVO summary = WebUtil.getServiceLocator().getSearchService().getIntermediateSets(auth, module,
					new LinkedHashSet<CriterionInVO>(criterions == null ? new ArrayList<CriterionInVO>() : criterions), null);
			IntermediateSetSteps steps = new IntermediateSetSteps(summary, getTies());
			StringBuilder result = new StringBuilder();
			appendLine(result, "ok: " + (criterions == null ? 0 : criterions.size()) + " criteria, " + steps.getSteps().size() + " set steps, result " + steps.getCount()
					+ " records");
			appendLine(result, "sets:");
			int lines = 0;
			Iterator<IntermediateSetSteps.ResultSet> setIt = steps.getSets().iterator();
			while (setIt.hasNext() && lines < MAX_SET_LINES) {
				IntermediateSetSteps.ResultSet set = setIt.next();
				StringBuilder line = new StringBuilder("  ").append(set.getLabel()).append(": ").append(set.getCount());
				String branches = facetBranches(set.getCriterions());
				if (branches.length() > 0) {
					line.append(" (").append(branches).append(')');
				}
				line.append(' ').append(abbreviateExpression(set.getCriteriaText()));
				appendLine(result, line.toString());
				lines++;
			}
			if (steps.getSets().size() > lines) {
				appendLine(result, "  ... " + (steps.getSets().size() - lines) + " more");
			}
			if (!steps.getSteps().isEmpty()) {
				appendLine(result, "steps:");
				lines = 0;
				Iterator<IntermediateSetSteps.Step> stepIt = steps.getSteps().iterator();
				while (stepIt.hasNext() && lines < MAX_STEP_LINES) {
					appendLine(result, "  " + stepText(stepIt.next()));
					lines++;
				}
				if (steps.getSteps().size() > lines) {
					appendLine(result, "  ... " + (steps.getSteps().size() - lines) + " more");
				}
			}
			return result.toString();
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
	}

	private HashMap<Long, CriterionTieVO> getTies() throws AuthenticationException, AuthorisationException, ServiceException {
		if (ties == null) {
			HashMap<Long, CriterionTieVO> map = new HashMap<Long, CriterionTieVO>();
			Collection<CriterionTieVO> tieVOs = WebUtil.getServiceLocator().getSelectionSetService().getAllCriterionTies(auth);
			if (tieVOs != null) {
				Iterator<CriterionTieVO> it = tieVOs.iterator();
				while (it.hasNext()) {
					CriterionTieVO tieVO = it.next();
					map.put(tieVO.getId(), tieVO);
				}
			}
			ties = map;
		}
		return ties;
	}

	// such as 3. step 2 ∖ D: 41 ∖ 0 = 41, warning: D removes nothing
	private static String stepText(IntermediateSetSteps.Step step) {
		String tie = step.getTieName();
		StringBuilder line = new StringBuilder();
		line.append(step.getNumber()).append(". ").append(step.getShortExpression(STEP_REFERENCE)).append(": ").append(step.getACount()).append(' ').append(tie).append(' ')
				.append(step.getBCount()).append(" = ").append(step.getCount());
		if (step.getTie() != null) {
			switch (step.getTie()) {
				case UNION:
					line.append(" (only ").append(IntermediateSetSteps.operandName(step.getA(), STEP_REFERENCE)).append(' ').append(step.getACount() - step.getIntersectionCount())
							.append(", both ").append(step.getIntersectionCount()).append(", only ").append(IntermediateSetSteps.operandName(step.getB(), STEP_REFERENCE)).append(' ')
							.append(step.getBCount() - step.getIntersectionCount()).append(')');
					break;
				case EXCEPT:
					line.append(" (removed ").append(step.getIntersectionCount()).append(')');
					break;
				default:
					break;
			}
		}
		Iterator<IntermediateSetSteps.StepWarning> it = step.getWarnings().iterator();
		while (it.hasNext()) {
			line.append(", warning: ").append(warningText(it.next()));
		}
		return line.toString();
	}

	private static String warningText(IntermediateSetSteps.StepWarning warning) {
		String operand = IntermediateSetSteps.operandName(warning.getOperand(), STEP_REFERENCE);
		switch (warning.getWarning()) {
			case EMPTY_OPERAND:
				return operand + " is empty";
			case EMPTY_RESULT:
				return "the result is empty";
			case ADDS_NOTHING:
				return operand + " adds nothing";
			case REMOVES_NOTHING:
				return operand + " removes nothing";
			case REMOVES_ALL:
				return operand + " removes everything";
			default:
				return warning.getWarning().name();
		}
	}

	// the collected facet branches a result set holds, such as F1 B2, F1 B3: all criteria of the branch, not only its field,
	// so two facets comparing different options of the same field are told apart
	private String facetBranches(Collection<CriterionInstantVO> criterions) {
		StringBuilder result = new StringBuilder();
		Iterator<Facet> facetIt = facets.iterator();
		while (facetIt.hasNext()) {
			Facet facet = facetIt.next();
			Iterator<FacetBranch> it = facet.branches.iterator();
			while (it.hasNext()) {
				FacetBranch branch = it.next();
				if (branch.text != null && containsCriterions(criterions, branch.criterions)) {
					if (result.length() > 0) {
						result.append(", ");
					}
					result.append(facet.label).append(' ').append(branch.label);
				}
			}
		}
		return result.toString();
	}

	// whether the result set has a criterion like each property criterion of the branch; ties and parentheses do not count
	private static boolean containsCriterions(Collection<CriterionInstantVO> criterions, Collection<CriterionInVO> branchCriterions) {
		if (criterions == null || branchCriterions == null) {
			return false;
		}
		boolean any = false;
		Iterator<CriterionInVO> branchIt = branchCriterions.iterator();
		while (branchIt.hasNext()) {
			CriterionInVO branchCriterion = branchIt.next();
			if (branchCriterion.getPropertyId() == null) {
				continue;
			}
			any = true;
			boolean found = false;
			Iterator<CriterionInstantVO> it = criterions.iterator();
			while (it.hasNext() && !found) {
				found = sameCriterion(it.next(), branchCriterion);
			}
			if (!found) {
				return false;
			}
		}
		return any;
	}

	private static boolean sameCriterion(CriterionInstantVO criterion, CriterionInVO branchCriterion) {
		return Objects.equals(criterion.getPropertyId(), branchCriterion.getPropertyId())
				&& Objects.equals(criterion.getRestrictionId(), branchCriterion.getRestrictionId())
				&& Objects.equals(criterion.getStringValue(), branchCriterion.getStringValue())
				&& Objects.equals(criterion.getLongValue(), branchCriterion.getLongValue())
				&& Objects.equals(criterion.getFloatValue(), branchCriterion.getFloatValue())
				&& criterion.isBooleanValue() == branchCriterion.isBooleanValue()
				&& Objects.equals(criterion.getDateValue(), branchCriterion.getDateValue())
				&& Objects.equals(criterion.getTimestampValue(), branchCriterion.getTimestampValue())
				&& Objects.equals(criterion.getTimeValue(), branchCriterion.getTimeValue());
	}

	private static String abbreviateExpression(String expression) {
		if (expression == null) {
			return "";
		}
		String line = expression.replaceAll("\\s+", " ").trim();
		return line.length() > MAX_SET_EXPRESSION_LENGTH ? line.substring(0, MAX_SET_EXPRESSION_LENGTH) + " ..." : line;
	}

	private static void appendLine(StringBuilder result, String line) {
		if (result.length() > 0) {
			result.append('\n');
		}
		result.append(line);
	}

	private static void appendSelectionValues(StringBuilder result, InputFieldOutVO field) {
		Collection<InputFieldSelectionSetValueOutVO> values = field.getSelectionSetValues();
		// autocomplete items are only suggestions, its answers are text to look up with the values tool
		if (values == null || values.isEmpty() || InputFieldType.AUTOCOMPLETE.name().equals(fieldType(field))) {
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
			options.append(optionText(value));
			count++;
		}
		if (it.hasNext()) {
			options.append(", ...");
		}
		appendLine(result, "  options: " + options);
	}

	// the value a selection value criterion compares, quoted as values can hold parentheses themselves,
	// with the option text when it says more, e.g. "1" (Diabetes Typ I) or "Ypsomed (Omnipod)"
	private static String optionText(InputFieldSelectionSetValueOutVO value) {
		String quoted = "\"" + (value.getValue() == null ? "" : value.getValue()) + "\"";
		String name = value.getName();
		if (CommonUtil.isEmptyString(name) || name.trim().equalsIgnoreCase(value.getValue() == null ? "" : value.getValue().trim())) {
			return quoted;
		}
		return quoted + " (" + name.trim() + ")";
	}

	private static String fieldName(InputFieldOutVO field) {
		return field == null ? "" : field.getName();
	}

	private static String fieldType(InputFieldOutVO field) {
		return field == null || field.getFieldType() == null || field.getFieldType().getType() == null ? "" : field.getFieldType().getType().name();
	}
}
