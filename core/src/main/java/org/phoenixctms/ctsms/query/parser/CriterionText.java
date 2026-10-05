package org.phoenixctms.ctsms.query.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.compare.VOPositionComparator;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.enumeration.CriterionTie;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

/**
 * Reads and writes the criterion text formats. Expression output is produced by
 * {@link CriterionParser}; this class reads that text back and converts JSON.
 */
final class CriterionText {

	private static final Gson JSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();

	private static final class TermResult {

		private final CriterionInVO criterion;
		private final int next;

		private TermResult(CriterionInVO criterion, int next) {
			this.criterion = criterion;
			this.next = next;
		}
	}

	private static final class NamedId {

		private final String name;
		private final Long id;

		private NamedId(String name, Long id) {
			this.name = name;
			this.id = id;
		}
	}

	private static final class Catalog {

		private final HashMap<Long, CriterionProperty> properties;
		private final HashMap<Long, CriterionTie> ties;
		private final HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions;
		private final HashMap<CriterionTie, Long> tieIds;
		private final List<NamedId> propertyNames;
		private final List<NamedId> tieNames;
		private final List<NamedId> restrictionNames;
		private final String userDateFormat;
		private final String userDecimalSeparator;
		private final int maxCriterions;
		private final DBModule module;

		private Catalog(CriterionParser parser, DBModule module) {
			this.module = module;
			this.properties = parser.getPropertyMap(module);
			this.ties = parser.getTieMap();
			this.restrictions = parser.getRestrictionMap();
			this.userDateFormat = CoreUtil.getUserContext().getDateFormat();
			this.userDecimalSeparator = CoreUtil.getUserContext().getDecimalSeparator();
			this.maxCriterions = Settings.getInt(SettingCodes.MAX_CRITERIONS, Bundle.SETTINGS, DefaultSettings.MAX_CRITERIONS);
			this.tieIds = new HashMap<CriterionTie, Long>();
			this.propertyNames = new ArrayList<NamedId>();
			this.tieNames = new ArrayList<NamedId>();
			this.restrictionNames = new ArrayList<NamedId>();
			HashMap<CriterionTie, String> tieNamesByEnum = parser.getTieNameMap();
			if (ties != null) {
				Iterator<Map.Entry<Long, CriterionTie>> tieIt = ties.entrySet().iterator();
				while (tieIt.hasNext()) {
					Map.Entry<Long, CriterionTie> entry = tieIt.next();
					if (entry.getKey() == null || entry.getValue() == null) {
						continue;
					}
					tieIds.put(entry.getValue(), entry.getKey());
					addName(tieNames, entry.getValue().name(), entry.getKey());
					addName(tieNames, tieNamesByEnum.get(entry.getValue()), entry.getKey());
				}
			}
			if (properties != null) {
				Iterator<CriterionProperty> propertyIt = properties.values().iterator();
				while (propertyIt.hasNext()) {
					CriterionProperty property = propertyIt.next();
					if (property == null || property.getId() == null) {
						continue;
					}
					addName(propertyNames, property.getProperty(), property.getId());
					addName(propertyNames, L10nUtil.getCriterionPropertyName(Locales.USER, property.getNameL10nKey()), property.getId());
				}
			}
			HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String> restrictionNamesByEnum = parser.getRestrictionNameMap();
			if (restrictions != null) {
				Iterator<Map.Entry<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>> restrictionIt = restrictions.entrySet().iterator();
				while (restrictionIt.hasNext()) {
					Map.Entry<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> entry = restrictionIt.next();
					if (entry.getKey() == null || entry.getValue() == null) {
						continue;
					}
					addName(restrictionNames, entry.getValue().name(), entry.getKey());
					addName(restrictionNames, restrictionNamesByEnum.get(entry.getValue()), entry.getKey());
				}
			}
			sortByLength(propertyNames);
			sortByLength(tieNames);
			sortByLength(restrictionNames);
		}
	}

	private CriterionText() {
	}

	static String toJson(CriterionParser parser, ArrayList<CriterionInstantVO> criterions) {
		Catalog catalog = new Catalog(parser, null);
		JsonArray array = new JsonArray();
		if (criterions != null) {
			for (int i = 0; i < criterions.size(); i++) {
				CriterionInstantVO criterion = criterions.get(i);
				if (criterion == null) {
					continue;
				}
				CriterionTie tie = tieOf(criterion, catalog.ties);
				CriterionProperty property = propertyOf(criterion, catalog.properties);
				org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = restrictionOf(criterion, catalog.restrictions);
				if (tie == null && property == null) {
					continue;
				}
				JsonObject object = new JsonObject();
				if (criterion.getPosition() != null) {
					object.addProperty("position", criterion.getPosition());
				}
				if (tie != null) {
					object.addProperty("tie", tie.name());
				}
				if (property != null) {
					object.addProperty("property", property.getProperty());
					if (restriction != null) {
						object.addProperty("restriction", restriction.name());
					}
					appendJsonValue(object, criterion, property, restriction, catalog);
				}
				array.add(object);
			}
		}
		return JSON.toJson(array);
	}

	static ArrayList<CriterionInVO> parse(CriterionParser parser, DBModule module, String text) throws ServiceException {
		Catalog catalog = new Catalog(parser, module);
		String source = text == null ? "" : text.trim();
		if (source.length() == 0) {
			return new ArrayList<CriterionInVO>();
		}
		if (source.charAt(0) == '{' || source.charAt(0) == '[') {
			return parseJson(source, catalog);
		}
		return parseExpression(source, catalog);
	}

	private static void addName(List<NamedId> names, String name, Long id) {
		if (id == null || CommonUtil.isEmptyString(name)) {
			return;
		}
		String token = name.trim();
		if (token.length() == 0) {
			return;
		}
		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).name.equals(token)) {
				return;
			}
		}
		names.add(new NamedId(token, id));
	}

	private static void appendJsonValue(JsonObject object, CriterionInstantVO criterion, CriterionProperty property,
			org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction, Catalog catalog) {
		if (!hasValue(property, restriction)) {
			return;
		}
		String value = CommonUtil.getCriterionValueAsString(criterion, property.getValueType(), catalog.userDateFormat, catalog.userDecimalSeparator);
		if (CommonUtil.isEmptyString(value)) {
			return;
		}
		switch (property.getValueType()) {
			case BOOLEAN:
			case BOOLEAN_HASH:
				object.addProperty("booleanValue", criterion.getBooleanValue());
				break;
			case LONG:
			case LONG_HASH:
				object.addProperty("longValue", criterion.getLongValue());
				break;
			case FLOAT:
			case FLOAT_HASH:
				object.addProperty("floatValue", criterion.getFloatValue());
				break;
			case DATE:
			case DATE_HASH:
			case TIME:
			case TIME_HASH:
			case TIMESTAMP:
			case TIMESTAMP_HASH:
			case STRING:
			case STRING_HASH:
				object.addProperty(valueField(property.getValueType()), value);
				break;
			default:
				break;
		}
	}

	private static CriterionInVO blankCriterion() {
		CriterionInVO criterion = new CriterionInVO();
		criterion.setBooleanValue(false);
		criterion.setDateValue(null);
		criterion.setTimeValue(null);
		criterion.setFloatValue(null);
		criterion.setLongValue(null);
		criterion.setPosition(null);
		criterion.setPropertyId(null);
		criterion.setRestrictionId(null);
		criterion.setStringValue(null);
		criterion.setTieId(null);
		criterion.setTimestampValue(null);
		return criterion;
	}

	private static void checkSize(ArrayList<CriterionInVO> criterions, Catalog catalog) throws ServiceException {
		if (criterions.size() >= catalog.maxCriterions) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_TOO_MANY, Integer.toString(catalog.maxCriterions));
		}
	}

	private static Long findExact(List<NamedId> names, String token) {
		if (CommonUtil.isEmptyString(token)) {
			return null;
		}
		String trimmed = token.trim();
		NamedId match = matchLongest(trimmed, 0, names);
		if (match != null && match.name.length() == trimmed.length()) {
			return match.id;
		}
		return null;
	}

	private static boolean hasValue(CriterionProperty property, org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction) {
		return property != null && property.getValueType() != null && !CriterionValueType.NONE.equals(property.getValueType())
				&& !CommonUtil.isUnaryCriterionRestriction(restriction);
	}

	private static boolean isBoundary(String text, int index) {
		if (index >= text.length()) {
			return true;
		}
		char c = text.charAt(index);
		return Character.isWhitespace(c) || c == '<' || c == '(' || c == ')' || c == '>';
	}

	private static String jsonString(JsonElement element) {
		if (element == null || element.isJsonNull() || !element.isJsonPrimitive()) {
			return null;
		}
		return element.getAsString();
	}

	private static NamedId matchLongest(String text, int offset, List<NamedId> names) {
		for (int i = 0; i < names.size(); i++) {
			NamedId candidate = names.get(i);
			int end = offset + candidate.name.length();
			if (end <= text.length() && text.regionMatches(true, offset, candidate.name, 0, candidate.name.length()) && isBoundary(text, end)) {
				return candidate;
			}
		}
		return null;
	}

	private static ArrayList<CriterionInVO> parseExpression(String text, Catalog catalog) throws ServiceException {
		ArrayList<CriterionInVO> criterions = new ArrayList<CriterionInVO>();
		Long pendingTieId = null;
		int i = 0;
		int n = text.length();
		while (i < n) {
			i = skipSeparators(text, i);
			if (i >= n) {
				break;
			}
			if (text.charAt(i) == '(' || text.charAt(i) == ')') {
				if (pendingTieId != null) {
					checkSize(criterions, catalog);
					CriterionInVO pending = blankCriterion();
					pending.setTieId(pendingTieId);
					criterions.add(pending);
					pendingTieId = null;
				}
				CriterionTie parenthesis = text.charAt(i) == '(' ? CriterionTie.LEFT_PARENTHESIS : CriterionTie.RIGHT_PARENTHESIS;
				Long tieId = catalog.tieIds.get(parenthesis);
				if (tieId == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_UNKNOWN_TIE, String.valueOf(text.charAt(i)));
				}
				checkSize(criterions, catalog);
				CriterionInVO criterion = blankCriterion();
				criterion.setTieId(tieId);
				criterions.add(criterion);
				i++;
				continue;
			}
			if (text.charAt(i) == '<') {
				int end = text.indexOf('>', i + 1);
				if (end < 0) {
					throw invalid(text, i);
				}
				TermResult term = parseTerm(text.substring(i + 1, end).trim(), 0, catalog, true);
				if (pendingTieId != null) {
					term.criterion.setTieId(pendingTieId);
					pendingTieId = null;
				}
				checkSize(criterions, catalog);
				criterions.add(term.criterion);
				i = end + 1;
				continue;
			}
			NamedId tie = matchLongest(text, i, catalog.tieNames);
			NamedId property = matchLongest(text, i, catalog.propertyNames);
			if (property != null && (tie == null || property.name.length() > tie.name.length())) {
				TermResult term = parseTerm(text, i, catalog, false);
				if (pendingTieId != null) {
					term.criterion.setTieId(pendingTieId);
					pendingTieId = null;
				}
				checkSize(criterions, catalog);
				criterions.add(term.criterion);
				i = term.next;
				continue;
			}
			if (tie != null) {
				CriterionTie tieEnum = catalog.ties.get(tie.id);
				boolean logical = tieEnum != null && !CommonUtil.isBlankCriterionTie(tieEnum);
				if (logical) {
					if (pendingTieId != null) {
						throw invalid(text, i);
					}
					pendingTieId = tie.id;
					i += tie.name.length();
					continue;
				}
				if (pendingTieId != null) {
					checkSize(criterions, catalog);
					CriterionInVO pending = blankCriterion();
					pending.setTieId(pendingTieId);
					criterions.add(pending);
					pendingTieId = null;
				}
				checkSize(criterions, catalog);
				CriterionInVO criterion = blankCriterion();
				criterion.setTieId(tie.id);
				criterions.add(criterion);
				i += tie.name.length();
				continue;
			}
			throw invalid(text, i);
		}
		if (pendingTieId != null) {
			throw invalid(text, text.length());
		}
		return criterions;
	}

	private static ArrayList<CriterionInVO> parseJson(String text, Catalog catalog) throws ServiceException {
		JsonElement root;
		try {
			root = new JsonParser().parse(text);
		} catch (JsonSyntaxException e) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID, e.getMessage());
		}
		JsonArray criterionsArray;
		if (root.isJsonArray()) {
			criterionsArray = root.getAsJsonArray();
		} else if (root.isJsonObject()) {
			JsonObject object = root.getAsJsonObject();
			if (object.has("module") && !object.get("module").isJsonNull() && catalog.module != null) {
				String moduleName = jsonString(object.get("module"));
				if (!CommonUtil.isEmptyString(moduleName) && !catalog.module.name().equals(moduleName)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_MODULE_MISMATCH, moduleName, catalog.module.name());
				}
			}
			if (object.has("criterions") && object.get("criterions").isJsonArray()) {
				criterionsArray = object.getAsJsonArray("criterions");
			} else if (looksLikeCriterion(object)) {
				criterionsArray = new JsonArray();
				criterionsArray.add(object);
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID, "criterions");
			}
		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID, text);
		}
		ArrayList<CriterionInVO> criterions = new ArrayList<CriterionInVO>();
		for (int i = 0; i < criterionsArray.size(); i++) {
			JsonElement element = criterionsArray.get(i);
			if (element == null || !element.isJsonObject()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID, String.valueOf(i + 1));
			}
			checkSize(criterions, catalog);
			criterions.add(parseJsonCriterion(element.getAsJsonObject(), catalog));
		}
		boolean positioned = !criterions.isEmpty();
		for (int i = 0; i < criterions.size(); i++) {
			if (criterions.get(i).getPosition() == null) {
				positioned = false;
				break;
			}
		}
		if (positioned) {
			Collections.sort(criterions, new VOPositionComparator(false));
		}
		return criterions;
	}

	private static CriterionInVO parseJsonCriterion(JsonObject object, Catalog catalog) throws ServiceException {
		CriterionInVO criterion = blankCriterion();
		if (object.has("position") && !object.get("position").isJsonNull()) {
			criterion.setPosition(readLong(object.get("position")));
		}
		if (object.has("tieId") || object.has("tie")) {
			JsonElement tieElement = object.has("tie") ? object.get("tie") : object.get("tieId");
			criterion.setTieId(resolveRef(tieElement, catalog.tieNames, catalog.ties.keySet(), ServiceExceptionCodes.CRITERION_TEXT_UNKNOWN_TIE, "tie", "name"));
		}
		if (object.has("propertyId") || object.has("property")) {
			JsonElement propertyElement = object.has("property") ? object.get("property") : object.get("propertyId");
			criterion.setPropertyId(resolveRef(propertyElement, catalog.propertyNames, catalog.properties.keySet(), ServiceExceptionCodes.CRITERION_TEXT_UNKNOWN_PROPERTY,
					"property", "name"));
		}
		if (object.has("restrictionId") || object.has("restriction")) {
			JsonElement restrictionElement = object.has("restriction") ? object.get("restriction") : object.get("restrictionId");
			criterion.setRestrictionId(resolveRef(restrictionElement, catalog.restrictionNames, catalog.restrictions.keySet(),
					ServiceExceptionCodes.CRITERION_TEXT_UNKNOWN_RESTRICTION, "restriction", "name"));
		}
		CriterionProperty property = propertyOf(criterion, catalog.properties);
		org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = restrictionOf(criterion, catalog.restrictions);
		if (property != null && hasValue(property, restriction)) {
			setJsonValue(criterion, object, property, catalog);
		}
		return criterion;
	}

	private static TermResult parseTerm(String text, int offset, Catalog catalog, boolean bracket) throws ServiceException {
		int start = skipSeparators(text, offset);
		NamedId property = matchLongest(text, start, catalog.propertyNames);
		if (property == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_UNKNOWN_PROPERTY, snippet(text, start));
		}
		CriterionProperty propertyEntity = catalog.properties.get(property.id);
		int i = start + property.name.length();
		while (i < text.length() && (text.charAt(i) == ' ' || text.charAt(i) == '\t')) {
			i++;
		}
		NamedId restriction = matchLongest(text, i, catalog.restrictionNames);
		if (restriction == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_UNKNOWN_RESTRICTION, snippet(text, i));
		}
		org.phoenixctms.ctsms.enumeration.CriterionRestriction restrictionEnum = catalog.restrictions.get(restriction.id);
		i += restriction.name.length();
		while (i < text.length() && (text.charAt(i) == ' ' || text.charAt(i) == '\t')) {
			i++;
		}
		String value;
		int next;
		if (bracket) {
			value = text.substring(i).trim();
			next = text.length();
		} else {
			int end = i;
			while (end < text.length() && text.charAt(end) != '\n' && text.charAt(end) != '\r') {
				if (text.charAt(end) == ')' && (end == i || Character.isWhitespace(text.charAt(end - 1)))) {
					break;
				}
				end++;
			}
			value = text.substring(i, end).trim();
			next = end;
			if (next < text.length() && (text.charAt(next) == '\n' || text.charAt(next) == '\r')) {
				if (text.charAt(next) == '\r') {
					next++;
				}
				if (next < text.length() && text.charAt(next) == '\n') {
					next++;
				}
			}
		}
		CriterionInVO criterion = blankCriterion();
		criterion.setPropertyId(property.id);
		criterion.setRestrictionId(restriction.id);
		if (hasValue(propertyEntity, restrictionEnum) && !CommonUtil.isEmptyString(value)) {
			setStringValue(criterion, propertyEntity, value, catalog);
		}
		return new TermResult(criterion, next);
	}

	private static CriterionProperty propertyOf(CriterionInVO criterion, HashMap<Long, CriterionProperty> properties) {
		if (criterion == null || criterion.getPropertyId() == null || properties == null) {
			return null;
		}
		return properties.get(criterion.getPropertyId());
	}

	private static CriterionProperty propertyOf(CriterionInstantVO criterion, HashMap<Long, CriterionProperty> properties) {
		if (criterion == null || criterion.getPropertyId() == null || properties == null) {
			return null;
		}
		return properties.get(criterion.getPropertyId());
	}

	private static String propertyLabel(CriterionProperty property) {
		String name = L10nUtil.getCriterionPropertyName(Locales.USER, property.getNameL10nKey());
		if (!CommonUtil.isEmptyString(name)) {
			return name;
		}
		return property.getProperty();
	}

	private static org.phoenixctms.ctsms.enumeration.CriterionRestriction restrictionOf(CriterionInVO criterion,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions) {
		if (criterion == null || criterion.getRestrictionId() == null || restrictions == null) {
			return null;
		}
		return restrictions.get(criterion.getRestrictionId());
	}

	private static org.phoenixctms.ctsms.enumeration.CriterionRestriction restrictionOf(CriterionInstantVO criterion,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions) {
		if (criterion == null || criterion.getRestrictionId() == null || restrictions == null) {
			return null;
		}
		return restrictions.get(criterion.getRestrictionId());
	}

	private static Long readLong(JsonElement element) throws ServiceException {
		if (element == null || element.isJsonNull()) {
			return null;
		}
		try {
			if (element.isJsonPrimitive()) {
				JsonPrimitive primitive = element.getAsJsonPrimitive();
				if (primitive.isNumber()) {
					return primitive.getAsLong();
				}
				if (primitive.isString() && !CommonUtil.isEmptyString(primitive.getAsString())) {
					return Long.valueOf(primitive.getAsString().trim());
				}
			}
		} catch (RuntimeException e) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID, element.toString());
		}
		return null;
	}

	private static Long resolveRef(JsonElement element, List<NamedId> names, java.util.Set<Long> knownIds, String unknownCode, String... fields) throws ServiceException {
		if (element == null || element.isJsonNull()) {
			return null;
		}
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isNumber()) {
				Long id = primitive.getAsLong();
				if (knownIds != null && knownIds.contains(id)) {
					return id;
				}
				throw L10nUtil.initServiceException(unknownCode, id.toString());
			}
			String token = primitive.getAsString();
			Long id = findExact(names, token);
			if (id == null) {
				throw L10nUtil.initServiceException(unknownCode, token);
			}
			return id;
		}
		if (element.isJsonObject()) {
			JsonObject object = element.getAsJsonObject();
			if (object.has("id") && !object.get("id").isJsonNull()) {
				Long id = readLong(object.get("id"));
				if (id != null && knownIds != null && knownIds.contains(id)) {
					return id;
				}
			}
			for (int i = 0; i < fields.length; i++) {
				if (object.has(fields[i]) && object.get(fields[i]).isJsonPrimitive()) {
					Long id = findExact(names, object.get(fields[i]).getAsString());
					if (id != null) {
						return id;
					}
				}
			}
			throw L10nUtil.initServiceException(unknownCode, object.toString());
		}
		throw L10nUtil.initServiceException(unknownCode, element.toString());
	}

	private static void setJsonValue(CriterionInVO criterion, JsonObject object, CriterionProperty property, Catalog catalog) throws ServiceException {
		CriterionValueType type = property.getValueType();
		String field = valueField(type);
		if (!object.has(field) || object.get(field).isJsonNull()) {
			if (object.has("value") && !object.get("value").isJsonNull()) {
				setStringValue(criterion, property, jsonString(object.get("value")), catalog);
			}
			return;
		}
		JsonElement element = object.get(field);
		try {
			switch (type) {
				case BOOLEAN:
				case BOOLEAN_HASH:
					if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
						criterion.setBooleanValue(element.getAsBoolean());
					} else {
						setStringValue(criterion, property, jsonString(element), catalog);
					}
					break;
				case LONG:
				case LONG_HASH:
					if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
						criterion.setLongValue(element.getAsLong());
					} else {
						setStringValue(criterion, property, jsonString(element), catalog);
					}
					break;
				case FLOAT:
				case FLOAT_HASH:
					if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
						criterion.setFloatValue(element.getAsFloat());
					} else {
						setStringValue(criterion, property, jsonString(element), catalog);
					}
					break;
				default:
					setStringValue(criterion, property, jsonString(element), catalog);
					break;
			}
		} catch (ServiceException e) {
			throw e;
		} catch (RuntimeException e) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID_VALUE, propertyLabel(property), element.toString());
		}
	}

	private static void setStringValue(CriterionInVO criterion, CriterionProperty property, String value, Catalog catalog) throws ServiceException {
		if (CommonUtil.isEmptyString(value)) {
			return;
		}
		CriterionValueType type = property.getValueType();
		try {
			CommonUtil.setCriterionValueFromString(criterion, type, value.trim(), catalog.userDateFormat, catalog.userDecimalSeparator);
		} catch (RuntimeException first) {
			if (!setAlternateValue(criterion, type, value.trim())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID_VALUE, propertyLabel(property), value.trim());
			}
			return;
		}
		if ((CriterionValueType.FLOAT.equals(type) || CriterionValueType.FLOAT_HASH.equals(type)) && criterion.getFloatValue() == null) {
			Float dotted = CommonUtil.parseFloat(value.trim(), ".");
			if (dotted == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID_VALUE, propertyLabel(property), value.trim());
			}
			criterion.setFloatValue(dotted);
		}
	}

	private static boolean setAlternateValue(CriterionInVO criterion, CriterionValueType type, String value) {
		String[] patterns;
		switch (type) {
			case DATE:
			case DATE_HASH:
				patterns = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss" };
				break;
			case TIME:
			case TIME_HASH:
				patterns = new String[] { "HH:mm:ss", "HH:mm", "yyyy-MM-dd HH:mm:ss" };
				break;
			case TIMESTAMP:
			case TIMESTAMP_HASH:
				patterns = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
				break;
			case FLOAT:
			case FLOAT_HASH:
				Float dotted = CommonUtil.parseFloat(value, ".");
				if (dotted == null) {
					return false;
				}
				criterion.setFloatValue(dotted);
				return true;
			default:
				return false;
		}
		for (int i = 0; i < patterns.length; i++) {
			try {
				Date date = CommonUtil.parseDate(value, patterns[i]);
				switch (type) {
					case DATE:
					case DATE_HASH:
						criterion.setDateValue(date);
						break;
					case TIME:
					case TIME_HASH:
						criterion.setTimeValue(date);
						break;
					default:
						criterion.setTimestampValue(date);
						break;
				}
				return true;
			} catch (RuntimeException ignored) {
			}
		}
		return false;
	}

	private static boolean looksLikeCriterion(JsonObject object) {
		return object.has("property") || object.has("propertyId") || object.has("tie") || object.has("tieId") || object.has("restriction") || object.has("restrictionId")
				|| object.has("stringValue") || object.has("longValue") || object.has("booleanValue") || object.has("floatValue");
	}

	private static int skipSeparators(String text, int i) {
		int n = text.length();
		while (i < n) {
			if (Character.isWhitespace(text.charAt(i))) {
				i++;
				continue;
			}
			int j = i;
			while (j < n && Character.isDigit(text.charAt(j))) {
				j++;
			}
			if (j > i && j < n && text.charAt(j) == ':') {
				i = j + 1;
				continue;
			}
			break;
		}
		return i;
	}

	private static String snippet(String text, int offset) {
		if (offset >= text.length()) {
			return "";
		}
		int end = Math.min(text.length(), offset + 80);
		return text.substring(offset, end).replace('\n', ' ').replace('\r', ' ').trim();
	}

	private static void sortByLength(List<NamedId> names) {
		Collections.sort(names, new Comparator<NamedId>() {

			@Override
			public int compare(NamedId a, NamedId b) {
				return b.name.length() - a.name.length();
			}
		});
	}

	private static ServiceException invalid(String text, int offset) {
		return L10nUtil.initServiceException(ServiceExceptionCodes.CRITERION_TEXT_INVALID, snippet(text, offset));
	}

	private static CriterionTie tieOf(CriterionInstantVO criterion, HashMap<Long, CriterionTie> ties) {
		if (criterion == null || criterion.getTieId() == null || ties == null) {
			return null;
		}
		return ties.get(criterion.getTieId());
	}

	private static String valueField(CriterionValueType type) {
		switch (type) {
			case BOOLEAN:
			case BOOLEAN_HASH:
				return "booleanValue";
			case LONG:
			case LONG_HASH:
				return "longValue";
			case FLOAT:
			case FLOAT_HASH:
				return "floatValue";
			case DATE:
			case DATE_HASH:
				return "dateValue";
			case TIME:
			case TIME_HASH:
				return "timeValue";
			case TIMESTAMP:
			case TIMESTAMP_HASH:
				return "timestampValue";
			default:
				return "stringValue";
		}
	}
}
