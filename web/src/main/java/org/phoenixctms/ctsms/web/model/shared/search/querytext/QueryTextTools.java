package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
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
	public static final String SEARCH_PROPERTY_VALUES = "searchPropertyValues";
	public static final String PARSE_QUERY = "parseQuery";
	private static final int PAGE_SIZE = 100;
	private static final int MAX_INPUT_FIELDS = 30;
	// each checked input field costs up to three usage count queries
	private static final int MAX_CHECKED_INPUT_FIELDS = 100;
	private static final int MAX_SELECTION_VALUES = 50;
	private static final int MAX_OPTION_MATCHES = 500;
	private static final String TEXT_VALUE_SUFFIX = "stringValue";
	private static final String SELECTION_VALUE_SUFFIX = "selectionValues.value";
	private static final String NO_MATCHES = "no matches";
	private final AuthenticationVO auth;
	private final DBModule module;
	private final Integer maxCriterions;
	private final Collection<CriterionPropertyVO> properties;
	private String lastValidText;
	private Collection<CriterionInVO> lastValidCriterions;

	public QueryTextTools(AuthenticationVO auth, DBModule module, Integer maxCriterions, Collection<CriterionPropertyVO> properties) {
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

		INQUIRY("proband.inquiryValues.inquiry.field.id", "proband.inquiryValues.value."),
		ECRF_FIELD("proband.trialParticipations.ecrfValues.ecrfField.field.id", "proband.trialParticipations.ecrfValues.value."),
		PROBAND_LIST_ATTRIBUTE("proband.trialParticipations.tagValues.tag.field.id", "proband.trialParticipations.tagValues.value.");

		private final String fieldProperty;
		private final String valuePrefix;

		private FieldCollection(String fieldProperty, String valuePrefix) {
			this.fieldProperty = fieldProperty;
			this.valuePrefix = valuePrefix;
		}
	}

	private long usageCount(FieldCollection collection, long fieldId) throws AuthenticationException, AuthorisationException, ServiceException {
		PSFVO psf = createCountPsf();
		switch (collection) {
			case INQUIRY:
				return rowCount(WebUtil.getServiceLocator().getInputFieldService().getInquiryList(auth, fieldId, psf), psf);
			case ECRF_FIELD:
				return rowCount(WebUtil.getServiceLocator().getInputFieldService().getEcrfFieldList(auth, fieldId, psf), psf);
			default:
				return rowCount(WebUtil.getServiceLocator().getInputFieldService().getProbandListEntryTagList(auth, fieldId, psf), psf);
		}
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
				line.append(" ∧ ").append(valueProperty.getName()).append(" ...");
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
			+ "then one indented line per collection that stores the field, such as: <field property> = <field name> ∧ <value property> ..., then the selectable option texts. "
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
			+ "A selection value property lists only options of selection fields and then the fields offering them, as field: option. "
			+ "A text value property lists the stored texts and then the autocomplete fields suggesting the text, as field: suggestion; compare those fields with their text value. "
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
			OptionMatches options = collection != null ? matchOptions(collection, valuePart) : null;
			StringBuilder result = new StringBuilder();
			boolean ids = QueryTextValues.isEntityLookup(propertyVO);
			Iterator<QueryTextValues.Value> it = QueryTextValues.lookup(auth, propertyVO, valuePart).iterator();
			while (it.hasNext()) {
				QueryTextValues.Value value = it.next();
				// the plain selection values also hold the learned suggestions of autocomplete fields, which are never selected
				if (!selection || !options.autocompleteOnly(value.getName())) {
					appendLine(result, ids ? value.getKey() + " | " + value.getName() : value.getName());
				}
			}
			if (options != null) {
				// the hits do not name the field: options of selection fields for a selection value, suggestions of autocomplete fields for a text value
				StringBuilder hints = selection ? options.selectionHints : options.autocompleteHints;
				if (hints.length() > 0) {
					appendLine(result, selection ? "fields offering a matching option:" : "autocomplete fields suggesting this text (compare their text value):");
					result.append('\n').append(hints);
				}
			}
			return result.length() > 0 ? result.toString() : NO_MATCHES;
		} catch (AuthenticationException | AuthorisationException | ServiceException | IllegalArgumentException e) {
			return errorText(e);
		}
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
			String hint = "  " + value.getField().getName() + ": " + value.getValue();
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

	@Tool(name = PARSE_QUERY, value = "Parse a query. Returns ok and the number of criteria, or the parse error to fix.")
	public String parseQuery(@P(name = "text", value = "the complete query text") String text) {
		try {
			Collection<CriterionInVO> criterions = WebUtil.getServiceLocator().getSearchService().parseCriterionText(auth, module, text == null ? "" : text, true, maxCriterions);
			lastValidText = text;
			lastValidCriterions = criterions;
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
