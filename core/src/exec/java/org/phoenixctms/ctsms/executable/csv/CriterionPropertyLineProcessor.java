package org.phoenixctms.ctsms.executable.csv;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestriction;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.fileprocessors.csv.LineProcessor;
import org.phoenixctms.ctsms.service.shared.SelectionSetService;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.FilterItemsStore;
import org.phoenixctms.ctsms.util.JSFVOConverterIDs;
import org.springframework.beans.factory.annotation.Autowired;

public class CriterionPropertyLineProcessor extends LineProcessor {

	private final static int MODULE_INDEX = 0;
	private final static int PROPERTY_INDEX = 1;
	private final static int VALUE_TYPE_INDEX = 2;
	private final static int ENTITY_NAME_INDEX = 3;
	private final static int SELECTION_SET_SERVICE_METHOD_NAME_INDEX = 4;
	private final static int GET_NAME_METHOD_NAME_INDEX = 5;
	private final static int GET_VALUE_METHOD_NAME_INDEX = 6;
	private final static int COMPLETE_METHOD_NAME_INDEX = 7;
	private final static int CONVERTER_INDEX = 8;
	private final static int FILTER_ITEMS_METHOD_NAME_INDEX = 9;
	private final static int PICKER_INDEX = 10;
	private final static int NAME_L10N_KEY_INDEX = 11;
	private final static int VALID_RESTRICTIONS_INDEX = 12;
	private final static String DEFAULT_RESTRICTION_SEPARATOR_REGEXP_PATTERN = " *, *";
	private static final String DEFAULT_EXCLUDE_RESTRICTION_CHAR = "/";
	private final static Collection<String> SELECTION_SET_SERVICE_METHOD_NAMES = getServiceMethodNames(SelectionSetService.class);
	private final static Collection<String> TOOLS_SERVICE_METHOD_NAMES = getServiceMethodNames(ToolsService.class);
	private final static Collection<String> FILTER_ITEMS_METHOD_NAMES = getServiceMethodNames(FilterItemsStore.class);

	private static Collection<String> getServiceMethodNames(Class iface) {
		HashSet<String> serviceMethods = new HashSet<String>();
		Method[] methods = iface.getMethods();
		for (int i = 0; i < methods.length; i++) {
			serviceMethods.add(methods[i].getName());
		}
		return serviceMethods;
	}

	private static boolean isValidEntityName(String entityName) {
		return CoreUtil.isEnumerationClass(entityName) || (CoreUtil.checkClassExists(CoreUtil.getOutVOClassNameFromEntityName(entityName))
				|| CoreUtil.checkClassExists(CoreUtil.getVOClassNameFromEntityName(entityName)));
	}

	@Autowired
	protected CriterionRestrictionDao criterionRestrictionDao;
	@Autowired
	protected CriterionPropertyDao criterionPropertyDao;
	private Pattern restrictionSeparatorRegexp;
	private String excludeRestrictionChar;

	public CriterionPropertyLineProcessor() {
		super();
	}

	private CriterionProperty createCriterionProperty(DBModule module,
			String field,
			CriterionValueType type,
			DBModule picker,
			String selectionSetServiceMethodName,
			String getNameMethodName,
			String getValueMethodName,
			String completeMethodName,
			String converter,
			String filterItemsMethodName,
			String entityName,
			String nameL10nKey,
			Set<org.phoenixctms.ctsms.enumeration.CriterionRestriction> validRestrictions) {
		CriterionProperty property = CriterionProperty.Factory.newInstance();
		property.setModule(module);
		property.setNameL10nKey(nameL10nKey);
		property.setProperty(field);
		property.setValueType(type);
		property.setPicker(picker);
		property.setSelectionSetServiceMethodName(selectionSetServiceMethodName);
		property.setGetNameMethodName(getNameMethodName);
		property.setGetValueMethodName(getValueMethodName);
		property.setCompleteMethodName(completeMethodName);
		property.setConverter(converter);
		property.setFilterItemsMethodName(filterItemsMethodName);
		property.setEntityName(entityName);
		HashSet<CriterionRestriction> restrictions;
		if (validRestrictions != null) {
			restrictions = new HashSet<CriterionRestriction>(validRestrictions.size());
			Iterator<org.phoenixctms.ctsms.enumeration.CriterionRestriction> it = validRestrictions.iterator();
			while (it.hasNext()) {
				restrictions.add(criterionRestrictionDao.searchUniqueRestriction(it.next()));
			}
		} else {
			restrictions = new HashSet<CriterionRestriction>();
		}
		property.setValidRestrictions(restrictions);
		property = criterionPropertyDao.create(property);
		jobOutput.println(field + ": " + restrictions.size() + " operator(s)");
		return property;
	}

	private String getCompleteMethodName(String[] values) {
		return values[COMPLETE_METHOD_NAME_INDEX];
	}

	private String getConverter(String[] values) {
		return values[CONVERTER_INDEX];
	}

	private String getFilterItemsMethodName(String[] values) {
		return values[FILTER_ITEMS_METHOD_NAME_INDEX];
	}

	private String getEntityName(String[] values) {
		return values[ENTITY_NAME_INDEX];
	}

	public String getExcludeRestrictionChar() {
		return excludeRestrictionChar;
	}

	private String getGetNameMethodName(String[] values) {
		return values[GET_NAME_METHOD_NAME_INDEX];
	}

	private String getGetValueMethodName(String[] values) {
		return values[GET_VALUE_METHOD_NAME_INDEX];
	}

	private String getModule(String[] values) {
		return values[MODULE_INDEX];
	}

	private String getNameL10nKey(String[] values) {
		return values[NAME_L10N_KEY_INDEX];
	}

	private String getPicker(String[] values) {
		return values[PICKER_INDEX];
	}

	private String getProperty(String[] values) {
		return values[PROPERTY_INDEX];
	}

	public String getRestrictionSeparatorRegexpPattern() {
		return restrictionSeparatorRegexp.pattern();
	}

	private String getSelectionSetServiceMethodName(String[] values) {
		return values[SELECTION_SET_SERVICE_METHOD_NAME_INDEX];
	}

	private String getValidRestrictions(String[] values) {
		return values[VALID_RESTRICTIONS_INDEX];
	}

	private String getValueType(String[] values) {
		return values[VALUE_TYPE_INDEX];
	}

	@Override
	public void init() {
		super.init();
		setRestrictionSeparatorRegexpPattern(DEFAULT_RESTRICTION_SEPARATOR_REGEXP_PATTERN);
		setExcludeRestrictionChar(DEFAULT_EXCLUDE_RESTRICTION_CHAR);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getNameL10nKey(values))
				.toHashCode();
	}

	@Override
	public void postProcess() {
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		AssociationPath property = new AssociationPath(getProperty(values));
		String picker = getPicker(values);
		String selectionSetServiceMethodName = getSelectionSetServiceMethodName(values);
		if (!CommonUtil.isEmptyString(selectionSetServiceMethodName) && !SELECTION_SET_SERVICE_METHOD_NAMES.contains(selectionSetServiceMethodName)) {
			throw new IllegalArgumentException("unknown selection set service method " + selectionSetServiceMethodName);
		}
		String getNameMethodName = getGetNameMethodName(values);
		String getValueMethodName = getGetValueMethodName(values);
		String completeMethodName = getCompleteMethodName(values);
		if (!CommonUtil.isEmptyString(completeMethodName) && !TOOLS_SERVICE_METHOD_NAMES.contains(completeMethodName)) {
			throw new IllegalArgumentException("unknown tools service method " + completeMethodName);
		}
		String converter = getConverter(values);
		if (!CommonUtil.isEmptyString(converter) && !JSFVOConverterIDs.CONVERTER_IDS.contains(converter)) {
			throw new IllegalArgumentException("unknown JSF converter ID " + converter);
		}
		String filterItemsMethodName = getFilterItemsMethodName(values);
		if (!CommonUtil.isEmptyString(filterItemsMethodName) && !FILTER_ITEMS_METHOD_NAMES.contains(filterItemsMethodName)) {
			throw new IllegalArgumentException("unknown filter items method " + filterItemsMethodName);
		}
		String entityName = getEntityName(values);
		if (!CommonUtil.isEmptyString(entityName) && !isValidEntityName(entityName)) {
			throw new IllegalArgumentException("unknown entity/enumeration " + entityName);
		}
		String[] restrictionNames = restrictionSeparatorRegexp.split(getValidRestrictions(values), -1);
		HashSet<org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions = new HashSet<org.phoenixctms.ctsms.enumeration.CriterionRestriction>();
		for (int i = 0; i < restrictionNames.length; i++) {
			if (restrictionNames[i].length() > 0 && !restrictionNames[i].startsWith(excludeRestrictionChar)) {
				restrictions.add(org.phoenixctms.ctsms.enumeration.CriterionRestriction.fromString(restrictionNames[i]));
			}
		}
		createCriterionProperty(
				DBModule.fromString(getModule(values)),
				property.getFullQualifiedPropertyName(),
				CriterionValueType.fromString(getValueType(values)),
				CommonUtil.isEmptyString(picker) ? null : DBModule.fromString(picker),
				CommonUtil.isEmptyString(selectionSetServiceMethodName) ? null : selectionSetServiceMethodName,
				CommonUtil.isEmptyString(getNameMethodName) ? null : getNameMethodName,
				CommonUtil.isEmptyString(getValueMethodName) ? null : getValueMethodName,
				CommonUtil.isEmptyString(completeMethodName) ? null : completeMethodName,
				CommonUtil.isEmptyString(converter) ? null : converter,
				CommonUtil.isEmptyString(filterItemsMethodName) ? null : filterItemsMethodName,
				CommonUtil.isEmptyString(entityName) ? null : entityName,
				getNameL10nKey(values),
				restrictions);
		return 1;
	}

	public void setExcludeRestrictionChar(String excludeRestrictionChar) {
		this.excludeRestrictionChar = excludeRestrictionChar;
	}

	public void setRestrictionSeparatorRegexpPattern(String restrictionSeparatorRegexpPattern) {
		this.restrictionSeparatorRegexp = Pattern.compile(restrictionSeparatorRegexpPattern);
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getModule(values))) {
			jobOutput.println("line " + lineNumber + ": empty module");
			return false;
		}
		if (CommonUtil.isEmptyString(getProperty(values))) {
			jobOutput.println("line " + lineNumber + ": empty property");
			return false;
		}
		if (CommonUtil.isEmptyString(getValueType(values))) {
			jobOutput.println("line " + lineNumber + ": empty value type");
			return false;
		}
		if (CommonUtil.isEmptyString(getNameL10nKey(values))) {
			jobOutput.println("line " + lineNumber + ": empty nameL10nKey");
			return false;
		}
		return true;
	}
}
