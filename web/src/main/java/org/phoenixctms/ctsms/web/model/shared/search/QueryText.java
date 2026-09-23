package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.phoenixctms.ctsms.compare.VOPositionComparator;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

/**
 * Converts query terms to and from a pretty-printed expression or JSON.
 * The expression uses the same layout as the criterion pretty-printer
 * (position, indentation, localized conjunction / property / operator, stored value).
 * JSON accepts either criterion input objects or the REST criteria/criterion documents.
 */
public final class QueryText {

	public static final String FORMAT_EXPRESSION = "EXPRESSION";
	public static final String FORMAT_JSON = "JSON";
	private static final String INDENT = "  ";
	private static final Gson JSON = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();

	public static final class ParseException extends Exception {

		private static final long serialVersionUID = 1L;

		public ParseException(String message) {
			super(message);
		}
	}

	public static final class Parsed {

		private final ArrayList<CriterionInVO> criterions;
		private final String label;
		private final boolean labelSet;
		private final String category;
		private final boolean categorySet;
		private final String comment;
		private final boolean commentSet;
		private final Boolean loadByDefault;

		private Parsed(ArrayList<CriterionInVO> criterions, String label, boolean labelSet, String category, boolean categorySet, String comment,
				boolean commentSet, Boolean loadByDefault) {
			this.criterions = criterions;
			this.label = label;
			this.labelSet = labelSet;
			this.category = category;
			this.categorySet = categorySet;
			this.comment = comment;
			this.commentSet = commentSet;
			this.loadByDefault = loadByDefault;
		}

		public ArrayList<CriterionInVO> getCriterions() {
			return criterions;
		}

		public String getLabel() {
			return label;
		}

		public boolean isLabelSet() {
			return labelSet;
		}

		public String getCategory() {
			return category;
		}

		public boolean isCategorySet() {
			return categorySet;
		}

		public String getComment() {
			return comment;
		}

		public boolean isCommentSet() {
			return commentSet;
		}

		public Boolean getLoadByDefault() {
			return loadByDefault;
		}
	}

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

		private final HashMap<Long, CriterionPropertyVO> properties;
		private final HashMap<Long, CriterionTieVO> ties;
		private final HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions;
		private final HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, Long> tieIds;
		private final List<NamedId> propertyNames;
		private final List<NamedId> tieNames;
		private final List<NamedId> restrictionNames;
		private final String userDateFormat;
		private final String userDecimalSeparator;
		private final int maxCriterions;
		private final DBModule module;

		private Catalog(DBModule module, HashMap<Long, CriterionPropertyVO> properties, HashMap<Long, CriterionTieVO> ties,
				HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions, String userDateFormat, String userDecimalSeparator,
				int maxCriterions) {
			this.module = module;
			this.properties = properties;
			this.ties = ties;
			this.restrictions = restrictions;
			this.userDateFormat = userDateFormat;
			this.userDecimalSeparator = userDecimalSeparator;
			this.maxCriterions = maxCriterions;
			this.tieIds = new HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, Long>();
			this.propertyNames = new ArrayList<NamedId>();
			this.tieNames = new ArrayList<NamedId>();
			this.restrictionNames = new ArrayList<NamedId>();
			if (ties != null) {
				Iterator<CriterionTieVO> tieIt = ties.values().iterator();
				while (tieIt.hasNext()) {
					CriterionTieVO tie = tieIt.next();
					if (tie == null || tie.getId() == null || tie.getTie() == null) {
						continue;
					}
					tieIds.put(tie.getTie(), tie.getId());
					addName(tieNames, tie.getTie().name(), tie.getId());
					addName(tieNames, tie.getName(), tie.getId());
				}
			}
			if (properties != null) {
				Iterator<CriterionPropertyVO> propertyIt = properties.values().iterator();
				while (propertyIt.hasNext()) {
					CriterionPropertyVO property = propertyIt.next();
					if (property == null || property.getId() == null) {
						continue;
					}
					addName(propertyNames, property.getProperty(), property.getId());
					addName(propertyNames, property.getName(), property.getId());
				}
			}
			if (restrictions != null) {
				Iterator<java.util.Map.Entry<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>> restrictionIt = restrictions.entrySet().iterator();
				while (restrictionIt.hasNext()) {
					java.util.Map.Entry<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> entry = restrictionIt.next();
					if (entry.getKey() == null || entry.getValue() == null) {
						continue;
					}
					addName(restrictionNames, entry.getValue().name(), entry.getKey());
				}
			}
			if (properties != null) {
				Iterator<CriterionPropertyVO> propertyIt = properties.values().iterator();
				while (propertyIt.hasNext()) {
					CriterionPropertyVO property = propertyIt.next();
					if (property == null || property.getValidRestrictions() == null) {
						continue;
					}
					Iterator<org.phoenixctms.ctsms.vo.CriterionRestrictionVO> restrictionIt = property.getValidRestrictions().iterator();
					while (restrictionIt.hasNext()) {
						org.phoenixctms.ctsms.vo.CriterionRestrictionVO restriction = restrictionIt.next();
						if (restriction == null || restriction.getId() == null) {
							continue;
						}
						addName(restrictionNames, restriction.getName(), restriction.getId());
						if (restriction.getRestriction() != null) {
							addName(restrictionNames, restriction.getRestriction().name(), restriction.getId());
						}
					}
				}
			}
			sortByLength(propertyNames);
			sortByLength(tieNames);
			sortByLength(restrictionNames);
		}
	}

	private QueryText() {
	}

	public static String toExpression(ArrayList<CriterionInVO> criterions, HashMap<Long, CriterionPropertyVO> properties, HashMap<Long, CriterionTieVO> ties,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions, String userDateFormat, String userDecimalSeparator) {
		if (criterions == null || criterions.isEmpty()) {
			return "";
		}
		int digits = 1;
		for (int i = 0; i < criterions.size(); i++) {
			CriterionInVO criterion = criterions.get(i);
			if (criterion != null && criterion.getPosition() != null) {
				digits = Math.max(digits, Long.toString(criterion.getPosition()).length());
			}
		}
		String positionFormat = "%0" + digits + "d";
		StringBuilder result = new StringBuilder();
		int indent = 0;
		boolean any = false;
		for (int i = 0; i < criterions.size(); i++) {
			CriterionInVO criterion = criterions.get(i);
			if (criterion == null) {
				continue;
			}
			CriterionTieVO tie = tieOf(criterion, ties);
			CriterionPropertyVO property = propertyOf(criterion, properties);
			org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = restrictionOf(criterion, restrictions);
			boolean left = tie != null && org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS.equals(tie.getTie());
			boolean right = tie != null && org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS.equals(tie.getTie());
			boolean blank = tie != null && CommonUtil.isBlankCriterionTie(tie.getTie());
			if (tie == null && property == null) {
				continue;
			}
			if (right) {
				indent = Math.max(0, indent - 1);
			}
			if (any) {
				result.append('\n');
			}
			any = true;
			if (criterion.getPosition() != null) {
				result.append(String.format(positionFormat, criterion.getPosition()));
				result.append(':');
			}
			for (int j = 0; j < indent; j++) {
				result.append(INDENT);
			}
			if (blank || (tie != null && property == null)) {
				result.append(tieLabel(tie));
			} else {
				if (tie != null) {
					result.append(tieLabel(tie));
					result.append(' ');
				}
				if (property != null) {
					result.append(propertyLabel(property));
					if (restriction != null) {
						result.append(' ');
						result.append(restrictionLabel(restriction, property));
						if (hasValue(property, restriction)) {
							String value = CommonUtil.getCriterionValueAsString(criterion, property.getValueType(), userDateFormat, userDecimalSeparator);
							if (!CommonUtil.isEmptyString(value)) {
								result.append(' ');
								result.append(value.replace('\r', ' ').replace('\n', ' '));
							}
						}
					}
				}
			}
			if (left) {
				indent++;
			}
		}
		return result.toString();
	}

	public static String toJson(ArrayList<CriterionInVO> criterions, HashMap<Long, CriterionPropertyVO> properties, HashMap<Long, CriterionTieVO> ties,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions, String userDateFormat, String userDecimalSeparator) {
		JsonArray array = new JsonArray();
		if (criterions != null) {
			for (int i = 0; i < criterions.size(); i++) {
				CriterionInVO criterion = criterions.get(i);
				if (criterion == null) {
					continue;
				}
				CriterionTieVO tie = tieOf(criterion, ties);
				CriterionPropertyVO property = propertyOf(criterion, properties);
				org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = restrictionOf(criterion, restrictions);
				if (tie == null && property == null) {
					continue;
				}
				JsonObject object = new JsonObject();
				if (criterion.getPosition() != null) {
					object.addProperty("position", criterion.getPosition());
				}
				if (tie != null && tie.getTie() != null) {
					object.addProperty("tie", tie.getTie().name());
				}
				if (property != null) {
					object.addProperty("property", property.getProperty());
					if (restriction != null) {
						object.addProperty("restriction", restriction.name());
					}
					appendJsonValue(object, criterion, property, restriction, userDateFormat, userDecimalSeparator);
				}
				array.add(object);
			}
		}
		return JSON.toJson(array);
	}

	public static Parsed parse(String text, DBModule module, HashMap<Long, CriterionPropertyVO> properties, HashMap<Long, CriterionTieVO> ties,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictions, String userDateFormat, String userDecimalSeparator, int maxCriterions)
			throws ParseException {
		Catalog catalog = new Catalog(module, properties, ties, restrictions, userDateFormat, userDecimalSeparator, maxCriterions);
		String source = text == null ? "" : text.trim();
		if (source.length() == 0) {
			return new Parsed(new ArrayList<CriterionInVO>(), null, false, null, false, null, false, null);
		}
		if (source.charAt(0) == '{' || source.charAt(0) == '[') {
			return parseJson(source, catalog);
		}
		return new Parsed(parseExpression(source, catalog), null, false, null, false, null, false, null);
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

	private static void appendJsonValue(JsonObject object, CriterionInVO criterion, CriterionPropertyVO property,
			org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction, String userDateFormat, String userDecimalSeparator) {
		if (!hasValue(property, restriction)) {
			return;
		}
		String value = CommonUtil.getCriterionValueAsString(criterion, property.getValueType(), userDateFormat, userDecimalSeparator);
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

	private static void checkSize(ArrayList<CriterionInVO> criterions, Catalog catalog) throws ParseException {
		if (criterions.size() >= catalog.maxCriterions) {
			throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_TOO_MANY, Integer.toString(catalog.maxCriterions)));
		}
	}

	private static Long findExact(List<NamedId> names, String token) throws ParseException {
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

	private static boolean hasValue(CriterionPropertyVO property, org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction) {
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

	private static ArrayList<CriterionInVO> parseExpression(String text, Catalog catalog) throws ParseException {
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
				org.phoenixctms.ctsms.enumeration.CriterionTie parenthesis = text.charAt(i) == '(' ? org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS
						: org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS;
				Long tieId = catalog.tieIds.get(parenthesis);
				if (tieId == null) {
					throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_UNKNOWN_TIE, String.valueOf(text.charAt(i))));
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
				CriterionTieVO tieVO = catalog.ties.get(tie.id);
				boolean logical = tieVO != null && !CommonUtil.isBlankCriterionTie(tieVO.getTie());
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

	private static Parsed parseJson(String text, Catalog catalog) throws ParseException {
		JsonElement root;
		try {
			root = new JsonParser().parse(text);
		} catch (JsonSyntaxException e) {
			throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID, e.getMessage()));
		}
		String label = null;
		boolean labelSet = false;
		String category = null;
		boolean categorySet = false;
		String comment = null;
		boolean commentSet = false;
		Boolean loadByDefault = null;
		JsonArray criterionsArray;
		if (root.isJsonArray()) {
			criterionsArray = root.getAsJsonArray();
		} else if (root.isJsonObject()) {
			JsonObject object = root.getAsJsonObject();
			if (object.has("module") && !object.get("module").isJsonNull() && catalog.module != null) {
				String moduleName = jsonString(object.get("module"));
				if (!CommonUtil.isEmptyString(moduleName) && !catalog.module.name().equals(moduleName)) {
					throw new ParseException(Messages.getMessage(MessageCodes.CRITERIA_MODULE_MISMATCH, moduleName, catalog.module.name()));
				}
			}
			if (object.has("label")) {
				labelSet = true;
				label = object.get("label").isJsonNull() ? null : jsonString(object.get("label"));
			}
			if (object.has("category")) {
				categorySet = true;
				category = object.get("category").isJsonNull() ? null : jsonString(object.get("category"));
			}
			if (object.has("comment")) {
				commentSet = true;
				comment = object.get("comment").isJsonNull() ? null : jsonString(object.get("comment"));
			}
			if (object.has("loadByDefault") && object.get("loadByDefault").isJsonPrimitive() && object.get("loadByDefault").getAsJsonPrimitive().isBoolean()) {
				loadByDefault = object.get("loadByDefault").getAsBoolean();
			}
			if (object.has("criterions") && object.get("criterions").isJsonArray()) {
				criterionsArray = object.getAsJsonArray("criterions");
			} else if (looksLikeCriterion(object)) {
				criterionsArray = new JsonArray();
				criterionsArray.add(object);
			} else {
				throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID, "criterions"));
			}
		} else {
			throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID, text));
		}
		ArrayList<CriterionInVO> criterions = new ArrayList<CriterionInVO>();
		for (int i = 0; i < criterionsArray.size(); i++) {
			JsonElement element = criterionsArray.get(i);
			if (element == null || !element.isJsonObject()) {
				throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID, String.valueOf(i + 1)));
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
		return new Parsed(criterions, label, labelSet, category, categorySet, comment, commentSet, loadByDefault);
	}

	private static CriterionInVO parseJsonCriterion(JsonObject object, Catalog catalog) throws ParseException {
		CriterionInVO criterion = blankCriterion();
		if (object.has("position") && !object.get("position").isJsonNull()) {
			Long position = readLong(object.get("position"));
			criterion.setPosition(position);
		}
		if (object.has("tieId") || object.has("tie")) {
			JsonElement tieElement = object.has("tie") ? object.get("tie") : object.get("tieId");
			Long tieId = resolveRef(tieElement, catalog.tieNames, catalog.ties.keySet(), MessageCodes.SEARCH_QUERY_TEXT_UNKNOWN_TIE, "tie", "name");
			criterion.setTieId(tieId);
		}
		if (object.has("propertyId") || object.has("property")) {
			JsonElement propertyElement = object.has("property") ? object.get("property") : object.get("propertyId");
			Long propertyId = resolveRef(propertyElement, catalog.propertyNames, catalog.properties.keySet(), MessageCodes.SEARCH_QUERY_TEXT_UNKNOWN_PROPERTY, "property",
					"name");
			criterion.setPropertyId(propertyId);
		}
		if (object.has("restrictionId") || object.has("restriction")) {
			JsonElement restrictionElement = object.has("restriction") ? object.get("restriction") : object.get("restrictionId");
			Long restrictionId = resolveRef(restrictionElement, catalog.restrictionNames, catalog.restrictions.keySet(), MessageCodes.SEARCH_QUERY_TEXT_UNKNOWN_RESTRICTION,
					"restriction", "name");
			criterion.setRestrictionId(restrictionId);
		}
		CriterionPropertyVO property = propertyOf(criterion, catalog.properties);
		org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = restrictionOf(criterion, catalog.restrictions);
		if (property != null && hasValue(property, restriction)) {
			setJsonValue(criterion, object, property, catalog);
		}
		return criterion;
	}

	private static TermResult parseTerm(String text, int offset, Catalog catalog, boolean bracket) throws ParseException {
		int start = skipSeparators(text, offset);
		NamedId property = matchLongest(text, start, catalog.propertyNames);
		if (property == null) {
			throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_UNKNOWN_PROPERTY, snippet(text, start)));
		}
		CriterionPropertyVO propertyVO = catalog.properties.get(property.id);
		int i = start + property.name.length();
		while (i < text.length() && (text.charAt(i) == ' ' || text.charAt(i) == '\t')) {
			i++;
		}
		NamedId restriction = matchLongest(text, i, catalog.restrictionNames);
		if (restriction == null) {
			throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_UNKNOWN_RESTRICTION, snippet(text, i)));
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
		if (hasValue(propertyVO, restrictionEnum) && !CommonUtil.isEmptyString(value)) {
			setStringValue(criterion, propertyVO, value, catalog);
		}
		return new TermResult(criterion, next);
	}

	private static CriterionPropertyVO propertyOf(CriterionInVO criterion, HashMap<Long, CriterionPropertyVO> properties) {
		if (criterion == null || criterion.getPropertyId() == null || properties == null) {
			return null;
		}
		return properties.get(criterion.getPropertyId());
	}

	private static String propertyLabel(CriterionPropertyVO property) {
		if (!CommonUtil.isEmptyString(property.getName())) {
			return property.getName();
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

	private static String restrictionLabel(org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction, CriterionPropertyVO property) {
		if (property.getValidRestrictions() != null) {
			Iterator<org.phoenixctms.ctsms.vo.CriterionRestrictionVO> it = property.getValidRestrictions().iterator();
			while (it.hasNext()) {
				org.phoenixctms.ctsms.vo.CriterionRestrictionVO restrictionVO = it.next();
				if (restrictionVO != null && restriction.equals(restrictionVO.getRestriction()) && !CommonUtil.isEmptyString(restrictionVO.getName())) {
					return restrictionVO.getName();
				}
			}
		}
		return restriction.name();
	}

	private static Long readLong(JsonElement element) throws ParseException {
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
			throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID, element.toString()));
		}
		return null;
	}

	private static Long resolveRef(JsonElement element, List<NamedId> names, java.util.Set<Long> knownIds, String unknownCode, String... fields) throws ParseException {
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
				throw new ParseException(Messages.getMessage(unknownCode, id.toString()));
			}
			String token = primitive.getAsString();
			Long id = findExact(names, token);
			if (id == null) {
				throw new ParseException(Messages.getMessage(unknownCode, token));
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
			throw new ParseException(Messages.getMessage(unknownCode, object.toString()));
		}
		throw new ParseException(Messages.getMessage(unknownCode, element.toString()));
	}

	private static void setJsonValue(CriterionInVO criterion, JsonObject object, CriterionPropertyVO property, Catalog catalog) throws ParseException {
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
		} catch (ParseException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID_VALUE, propertyLabel(property), element.toString()));
		}
	}

	private static void setStringValue(CriterionInVO criterion, CriterionPropertyVO property, String value, Catalog catalog) throws ParseException {
		if (CommonUtil.isEmptyString(value)) {
			return;
		}
		CriterionValueType type = property.getValueType();
		try {
			CommonUtil.setCriterionValueFromString(criterion, type, value.trim(), catalog.userDateFormat, catalog.userDecimalSeparator);
		} catch (RuntimeException first) {
			if (!setAlternateValue(criterion, type, value.trim())) {
				throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID_VALUE, propertyLabel(property), value.trim()));
			}
			return;
		}
		if ((CriterionValueType.FLOAT.equals(type) || CriterionValueType.FLOAT_HASH.equals(type)) && criterion.getFloatValue() == null) {
			Float dotted = CommonUtil.parseFloat(value.trim(), ".");
			if (dotted == null) {
				throw new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID_VALUE, propertyLabel(property), value.trim()));
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

	private static ParseException invalid(String text, int offset) {
		return new ParseException(Messages.getMessage(MessageCodes.SEARCH_QUERY_TEXT_INVALID, snippet(text, offset)));
	}

	private static CriterionTieVO tieOf(CriterionInVO criterion, HashMap<Long, CriterionTieVO> ties) {
		if (criterion == null || criterion.getTieId() == null || ties == null) {
			return null;
		}
		return ties.get(criterion.getTieId());
	}

	private static String tieLabel(CriterionTieVO tie) {
		if (!CommonUtil.isEmptyString(tie.getName())) {
			return tie.getName();
		}
		return tie.getTie().name();
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
