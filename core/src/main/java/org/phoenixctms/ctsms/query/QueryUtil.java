package org.phoenixctms.ctsms.query;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.hql.QueryTranslatorFactory;
import org.hibernate.hql.ast.ASTQueryTranslatorFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.phoenixctms.ctsms.compare.JoinComparator;
import org.phoenixctms.ctsms.compare.VOPositionComparator;
import org.phoenixctms.ctsms.domain.CourseImpl;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestriction;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.domain.CriterionTie;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.domain.InputFieldImpl;
import org.phoenixctms.ctsms.domain.InventoryImpl;
import org.phoenixctms.ctsms.domain.MassMailImpl;
import org.phoenixctms.ctsms.domain.ProbandImpl;
import org.phoenixctms.ctsms.domain.StaffImpl;
import org.phoenixctms.ctsms.domain.TrialImpl;
import org.phoenixctms.ctsms.domain.UserImpl;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.ExportStatus;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.query.QueryParameterValue.NamedParameterValues;
import org.phoenixctms.ctsms.query.QueryParameterValue.QueryParameterValueType;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.PSFVO;

public final class QueryUtil {

	static final class StaticCriterionTerm {

		private String property;
		private String term;
		private ArrayList<QueryParameterValue> bindValues;

		public StaticCriterionTerm(String property, String term,
				ArrayList<QueryParameterValue> bindValues) {
			this.property = property;
			this.term = term;
			this.bindValues = bindValues;
		}

		public String getTerm(Class rootEntityClass, String rootEntityName, HashMap<String, AssociationPath> explicitJoinsMap, HashMap<String, Class> propertyClassMap) {
			AssociationPath propertyNameAssociationPath = new AssociationPath(property);
			String propertyName = aliasPropertyName(rootEntityClass, propertyNameAssociationPath, rootEntityName, explicitJoinsMap, propertyClassMap);
			return MessageFormat.format(term, propertyName);
		}

		public ArrayList<QueryParameterValue> getValues() {
			return bindValues;
		}
	}

	private final static HashMap<String, String> ALTERNATIVE_FILTER_MAP = new HashMap<String, String>();
	private final static HashMap<String, ArrayList<StaticCriterionTerm>> FIXED_CRITERION_TERMS_MAP = new HashMap<String, ArrayList<StaticCriterionTerm>>();
	static {
		ALTERNATIVE_FILTER_MAP.put("ProbandContactParticulars.lastNameHash", "alias");
		ALTERNATIVE_FILTER_MAP.put("AnimalContactParticulars.animalName", "alias");

		// addPropertyCriterionTerms("proband.diagnoses.code",
		// "proband.diagnoses.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ALPHA_ID_REVISION, Bundle.SETTINGS, DefaultSettings.ALPHA_ID_REVISION)));
		// addPropertyCriterionTerms("proband.diagnoses.code.systematics",
		// "proband.diagnoses.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ALPHA_ID_REVISION, Bundle.SETTINGS, DefaultSettings.ALPHA_ID_REVISION)));
		// addPropertyCriterionTerms("proband.diagnoses.code.systematics.blocks",
		// "proband.diagnoses.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ALPHA_ID_REVISION, Bundle.SETTINGS, DefaultSettings.ALPHA_ID_REVISION)));
		addPropertyCriterionTerms("proband.diagnoses.code.systematics.blocks",
				"proband.diagnoses.code.systematics.blocks.last", "{0} = ?",
				new QueryParameterValue(true));
		// addPropertyCriterionTerms("proband.diagnoses.code.systematics.categories",
		// "proband.diagnoses.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ALPHA_ID_REVISION, Bundle.SETTINGS, DefaultSettings.ALPHA_ID_REVISION)));
		addPropertyCriterionTerms("proband.diagnoses.code.systematics.categories",
				"proband.diagnoses.code.systematics.categories.last", "{0} = ?",
				new QueryParameterValue(true));

		// addPropertyCriterionTerms("proband.procedures.code",
		// "proband.procedures.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.OPS_CODE_REVISION, Bundle.SETTINGS, DefaultSettings.OPS_CODE_REVISION)));
		// addPropertyCriterionTerms("proband.procedures.code.systematics",
		// "proband.procedures.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.OPS_CODE_REVISION, Bundle.SETTINGS, DefaultSettings.OPS_CODE_REVISION)));
		// addPropertyCriterionTerms("proband.procedures.code.systematics.blocks",
		// "proband.procedures.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.OPS_CODE_REVISION, Bundle.SETTINGS, DefaultSettings.OPS_CODE_REVISION)));
		addPropertyCriterionTerms("proband.procedures.code.systematics.blocks",
				"proband.procedures.code.systematics.blocks.last", "{0} = ?",
				new QueryParameterValue(true));
		// addPropertyCriterionTerms("proband.procedures.code.systematics.categories",
		// "proband.procedures.code.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.OPS_CODE_REVISION, Bundle.SETTINGS, DefaultSettings.OPS_CODE_REVISION)));
		addPropertyCriterionTerms("proband.procedures.code.systematics.categories",
				"proband.procedures.code.systematics.categories.last", "{0} = ?",
				new QueryParameterValue(true));

		// addPropertyCriterionTerms("proband.medications.asp",
		// "proband.medications.asp.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ASP_REVISION, Bundle.SETTINGS, DefaultSettings.ASP_REVISION)));
		// addPropertyCriterionTerms("proband.medications.asp.substances",
		// "proband.medications.asp.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ASP_REVISION, Bundle.SETTINGS, DefaultSettings.ASP_REVISION)));
		// addPropertyCriterionTerms("proband.medications.asp.atcCodes",
		// "proband.medications.asp.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ASP_REVISION, Bundle.SETTINGS, DefaultSettings.ASP_REVISION)));
		// addPropertyCriterionTerms("proband.medications.substances",
		// "proband.medications.substances.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ASP_REVISION, Bundle.SETTINGS, DefaultSettings.ASP_REVISION)));
		// addPropertyCriterionTerms("proband.medications.substances.asp",
		// "proband.medications.substances.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ASP_REVISION, Bundle.SETTINGS, DefaultSettings.ASP_REVISION)));
		// addPropertyCriterionTerms("proband.medications.substances.asp.atcCodes",
		// "proband.medications.substances.revision", "{0} = ?",
		// new QueryParameterValue(Settings.getString(SettingCodes.ASP_REVISION, Bundle.SETTINGS, DefaultSettings.ASP_REVISION)));
	}
	private final static HashMap<DBModule, String> ENTITY_NAMES = new HashMap<DBModule, String>();
	static
	{
		ENTITY_NAMES.put(DBModule.INVENTORY_DB, "inventory");
		ENTITY_NAMES.put(DBModule.STAFF_DB, "staff");
		ENTITY_NAMES.put(DBModule.COURSE_DB, "course");
		ENTITY_NAMES.put(DBModule.USER_DB, "user");
		ENTITY_NAMES.put(DBModule.TRIAL_DB, "trial");
		ENTITY_NAMES.put(DBModule.PROBAND_DB, "proband");
		ENTITY_NAMES.put(DBModule.INPUT_FIELD_DB, "inputField");
		ENTITY_NAMES.put(DBModule.MASS_MAIL_DB, "massMail");
	}
	private final static String HQL_ALIAS_PREFIX = "_";
	private final static String HQL_ASSOCIATION_PATH_SEPARATOR = ".";
	private final static String SQL_ASSOCIATION_PATH_SEPARATOR = ".";

	private static void addPropertyCriterionTerms(String propertyPath, String termProperty, String termFormat, QueryParameterValue... bindParams) {
		ArrayList<StaticCriterionTerm> propertyCriterionTerms;
		if (FIXED_CRITERION_TERMS_MAP.containsKey(propertyPath)) {
			propertyCriterionTerms = FIXED_CRITERION_TERMS_MAP.get(propertyPath);
		} else {
			propertyCriterionTerms = new ArrayList<StaticCriterionTerm>();
			FIXED_CRITERION_TERMS_MAP.put(propertyPath, propertyCriterionTerms);
		}
		propertyCriterionTerms.add(new StaticCriterionTerm(termProperty, termFormat, new ArrayList<QueryParameterValue>(Arrays.asList(bindParams))));
	}

	private static String aliasPropertyName(Class entityClass, AssociationPath fullyQualifiedPropertyName, String rootEntityName, HashMap<String, AssociationPath> explicitJoins,
			HashMap<String, Class> propertyClassMap) {
		if (fullyQualifiedPropertyName.isValid()) {
			prependRootEntityName(fullyQualifiedPropertyName, rootEntityName);
			String path;
			String result;
			Class propertyClass = entityClass;
			if (fullyQualifiedPropertyName.getPathDepth() > 1) {
				AssociationPath join = null;
				for (int i = 1; i < fullyQualifiedPropertyName.getPathDepth(); i++) {
					path = fullyQualifiedPropertyName.getPathString(i + 1);
					if (explicitJoins.containsKey(path)) {
						join = explicitJoins.get(path);
						propertyClass = propertyClassMap.get(path);
					} else {
						if (join == null) {
							join = new AssociationPath(path, path);
						} else {
							join = new AssociationPath(path, join.getJoinAlias() + HQL_ASSOCIATION_PATH_SEPARATOR + fullyQualifiedPropertyName.getPathElement(i));
						}
						join.setJoinAlias(HQL_ALIAS_PREFIX + explicitJoins.size());
						join.setJoinOrder(explicitJoins.size());
						explicitJoins.put(path, join);
						propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPathElement(i));
						propertyClassMap.put(path, propertyClass);
					}
				}
				result = join.getJoinAlias() + HQL_ASSOCIATION_PATH_SEPARATOR + fullyQualifiedPropertyName.getPropertyName();
			} else {
				result = fullyQualifiedPropertyName.getFullQualifiedPropertyName();
			}
			path = fullyQualifiedPropertyName.getFullQualifiedPropertyName();
			if (!propertyClassMap.containsKey(path)) {
				propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPropertyName());
				propertyClassMap.put(path, propertyClass);
			}
			return result;
		} else {
			// invalid path from db property entry:
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_PROPERTY_ASSOCIATION_PATH, DefaultMessages.INVALID_PROPERTY_ASSOCIATION_PATH,
					new Object[] { fullyQualifiedPropertyName.getPathString() }));
		}
	}

	private static void appendJoins(StringBuilder statement, HashMap<String, AssociationPath> explicitJoinsMap) {
		ArrayList<AssociationPath> joins = new ArrayList<AssociationPath>(explicitJoinsMap.values());
		Collections.sort(joins, new JoinComparator());
		Iterator<AssociationPath> it = joins.iterator();
		while (it.hasNext()) {
			AssociationPath join = it.next();
			StringBuilder joinSb = new StringBuilder(" left join ");
			joinSb.append(join.getAliasedPathString());
			joinSb.append(" as ");
			joinSb.append(join.getJoinAlias());
			statement.append(joinSb);
		}
	}

	private static void appendSelectHelper(StringBuilder setOperationExpression, int selectCount) {
		setOperationExpression.append(CommonUtil.getSetOperationExpressionSelectLabel(selectCount));
	}



	private static Object appendStaticCriterionTerms(StringBuilder hqlTerm,
			AssociationPath propertyNameAssociationPath, ArrayList<QueryParameterValue> queryValues, Class rootEntityClass, String rootEntityName,
			HashMap<String, AssociationPath> explicitJoinsMap, HashMap<String, Class> propertyClassMap) {
		String propertyPath = propertyNameAssociationPath.getPathString();
		if (FIXED_CRITERION_TERMS_MAP.containsKey(propertyPath)) {
			StringBuilder newTerm = new StringBuilder("((");
			newTerm.append(hqlTerm);
			newTerm.append(") and (");
			Iterator<StaticCriterionTerm> it = FIXED_CRITERION_TERMS_MAP.get(propertyPath).iterator();
			while (it.hasNext()) {
				StaticCriterionTerm term = it.next();
				newTerm.append("(");
				newTerm.append(term.getTerm(rootEntityClass, rootEntityName, explicitJoinsMap, propertyClassMap));
				newTerm.append(")");
				queryValues.addAll(term.getValues());
				if (it.hasNext()) {
					newTerm.append(" and ");
				}
			}
			newTerm.append("))");
			return newTerm;
		}
		return hqlTerm;
	}

	private static void applyFilter(StringBuilder hqlWhereClause, ArrayList<QueryParameterValue> queryValues, String propertyName, Class propertyClass, String value,
			String orPropertyName, Class orPropertyClass) {
		if (!CommonUtil.isEmptyString(orPropertyName)) {
			hqlWhereClause.append("(");
		}
		if (propertyClass.equals(String.class)) {
			hqlWhereClause.append("lower(");
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(") like concat(concat('%',lower(?)),'%')");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Long.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(java.lang.Long.TYPE)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Integer.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(java.lang.Integer.TYPE)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Boolean.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(java.lang.Boolean.TYPE)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Float.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(java.lang.Float.TYPE)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Double.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(java.lang.Double.TYPE)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Date.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Timestamp.class)) {
			hqlWhereClause.append("(");
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" >= ? and ");
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" <= ?)");
			Date date = CommonUtil.parseDate(value, CommonUtil.getInputDatePattern(CoreUtil.getUserContext().getDateFormat()));
			queryValues.add(new QueryParameterValue(Timestamp.class, CommonUtil.inputValueToString(CommonUtil.dateToTimestamp(DateCalc.getStartOfDay(date)),
					CoreUtil.getUserContext().getDateFormat(),
					CoreUtil.getUserContext().getDecimalSeparator())));
			queryValues.add(new QueryParameterValue(Timestamp.class, CommonUtil.inputValueToString(CommonUtil.dateToTimestamp(DateCalc.getEndOfDay(date)),
					CoreUtil.getUserContext().getDateFormat(),
					CoreUtil.getUserContext().getDecimalSeparator())));
		} else if (propertyClass.equals(Time.class)) {
			hqlWhereClause.append("(hour(");
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(") >= ? and hour(");
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(") <= ?)");
			Date time = CommonUtil.parseDate(value, CommonUtil.getInputTimePattern(CoreUtil.getUserContext().getDateFormat()));
			queryValues.add(new QueryParameterValue(Integer.class, Integer.toString(DateCalc.getHour(time))));
			queryValues.add(new QueryParameterValue(Integer.class, Integer.toString(DateCalc.getHour(time))));
		} else if (propertyClass.equals(VariablePeriod.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(AuthenticationType.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Sex.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(RandomizationMode.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(DBModule.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(HyperlinkModule.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(JournalModule.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(FileModule.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(Color.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(InputFieldType.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(EventImportance.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.equals(ExportStatus.class)) {
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else if (propertyClass.isArray() && propertyClass.getComponentType().equals(java.lang.Byte.TYPE)) { // only string hashes supported, no boolean, float, etc...
			hqlWhereClause.append(propertyName);
			hqlWhereClause.append(" = ?");
			queryValues.add(new QueryParameterValue(propertyClass, value));
		} else {
			// illegal type...
			throw new IllegalArgumentException(MessageFormat.format(CommonUtil.INPUT_TYPE_NOT_SUPPORTED, propertyClass.toString()));
		}
		if (!CommonUtil.isEmptyString(orPropertyName)) {
			hqlWhereClause.append(" or ");
			applyFilter(hqlWhereClause, queryValues, orPropertyName, orPropertyClass, value, null, null);
			hqlWhereClause.append(")");
		}
	}

	public static HashMap<Long, CriterionProperty> createCriterionPropertyMap(
			DBModule module, CriterionPropertyDao criterionPropertyDao) {
		Collection<CriterionProperty> criterionProperties = criterionPropertyDao.findByModule(module);
		HashMap<Long, CriterionProperty> propertyMap = new HashMap<Long, CriterionProperty>(criterionProperties.size());
		Iterator<CriterionProperty> criterionPropertyIt = criterionProperties.iterator();
		while (criterionPropertyIt.hasNext()) {
			CriterionProperty criterionProperty = criterionPropertyIt.next();
			propertyMap.put(criterionProperty.getId(), criterionProperty);
		}
		return propertyMap;
	}

	// public static HashMap<String, String> createCriterionPropertyNameMap(
	// DBModule module, CriterionPropertyDao criterionPropertyDao) {
	// Collection<CriterionProperty> criterionProperties = criterionPropertyDao.findByModule(module);
	// HashMap<String, String> propertyMap = new HashMap<String, String>(criterionProperties.size());
	// Iterator<CriterionProperty> criterionPropertyIt = criterionProperties.iterator();
	// while (criterionPropertyIt.hasNext()) {
	// CriterionProperty criterionProperty = criterionPropertyIt.next();
	// propertyMap.put(criterionProperty.getNameL10nKey(), L10nUtil.getCriterionPropertyName(Locales.USER, criterionProperty.getNameL10nKey()));
	// }
	// return propertyMap;
	// }

	public static HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> createCriterionRestrictionMap(
			CriterionRestrictionDao criterionRestrictionDao) {
		Collection<CriterionRestriction> criterionRestrictions = criterionRestrictionDao.loadAllSorted(0, 0);
		HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionMap = new HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>(
				criterionRestrictions.size());
		Iterator<CriterionRestriction> criterionRestrictionIt = criterionRestrictions.iterator();
		while (criterionRestrictionIt.hasNext()) {
			CriterionRestriction criterionRestriction = criterionRestrictionIt.next();
			restrictionMap.put(criterionRestriction.getId(), criterionRestriction.getRestriction());
		}
		return restrictionMap;
	}

	public static HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String> createCriterionRestrictionNameMap(
			CriterionRestrictionDao criterionRestrictionDao) {
		Collection<CriterionRestriction> criterionRestrictions = criterionRestrictionDao.loadAllSorted(0, 0);
		HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String> restrictionMap = new HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String>(
				criterionRestrictions.size());
		Iterator<CriterionRestriction> criterionRestrictionIt = criterionRestrictions.iterator();
		while (criterionRestrictionIt.hasNext()) {
			CriterionRestriction criterionRestriction = criterionRestrictionIt.next();
			restrictionMap.put(criterionRestriction.getRestriction(), L10nUtil.getCriterionRestrictionName(Locales.USER, criterionRestriction.getNameL10nKey()));
		}
		return restrictionMap;
	}

	public static HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> createCriterionTieMap(
			CriterionTieDao criterionTieDao) {
		Collection<CriterionTie> criterionTies = criterionTieDao.loadAllSorted(0, 0);
		HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> tieMap = new HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie>(criterionTies.size());
		Iterator<CriterionTie> criterionTieIt = criterionTies.iterator();
		while (criterionTieIt.hasNext()) {
			CriterionTie criterionTie = criterionTieIt.next();
			tieMap.put(criterionTie.getId(), criterionTie.getTie());
		}
		return tieMap;
	}

	public static HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, String> createCriterionTieNameMap(
			CriterionTieDao criterionTieDao) {
		Collection<CriterionTie> criterionTies = criterionTieDao.loadAllSorted(0, 0);
		HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, String> tieMap = new HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, String>(criterionTies.size());
		Iterator<CriterionTie> criterionTieIt = criterionTies.iterator();
		while (criterionTieIt.hasNext()) {
			CriterionTie criterionTie = criterionTieIt.next();
			tieMap.put(criterionTie.getTie(), L10nUtil.getCriterionTieName(Locales.USER, criterionTie.getNameL10nKey()));
		}
		return tieMap;
	}

	public static Query createSearchQuery(CriteriaInstantVO criteriaInstantVO, DBModule module, PSFVO psf, SessionFactory sessionFactory,
			CriterionTieDao criterionTieDao, CriterionPropertyDao criterionPropertyDao, CriterionRestrictionDao criterionRestrictionDao) throws Exception {
		ArrayList<QueryParameterValue> queryValues = new ArrayList<QueryParameterValue>();
		HashMap<NamedParameterValues, Object> namedParameterValuesCache = new HashMap<NamedParameterValues, Object>();
		StringBuilder sqlQuery = createSQLSetStatement(criteriaInstantVO, module, psf, sessionFactory, queryValues,
				createCriterionTieMap(criterionTieDao),
				createCriterionTieNameMap(criterionTieDao),
				createCriterionPropertyMap(module, criterionPropertyDao),
				// null, // createCriterionPropertyNameMap(module, criterionPropertyDao),
				createCriterionRestrictionMap(criterionRestrictionDao),
				null); // createCriterionRestrictionNameMap(criterionRestrictionDao));
		Class rootEntityClass = getRootEntityClass(module);
		SQLQuery query;
		if (psf != null) {
			if (psf.getUpdateRowCount()) {
				StringBuilder countStatement = new StringBuilder("select count(*) from (");
				countStatement.append(sqlQuery);
				countStatement.append(") as resultset");
				Query countQuery = sessionFactory.getCurrentSession().createSQLQuery(countStatement.toString());
				setQueryValues(countQuery, queryValues, namedParameterValuesCache);
				psf.setRowCount(new Long(countQuery.uniqueResult().toString()));
			}
			String sortField = psf.getSortField();
			if (sortField != null && sortField.length() > 0) {
				// http://weblogs.sqlteam.com/jeffs/archive/2007/12/13/select-distinct-order-by-error.aspx
				sqlQuery.append(" order by ");
				sqlQuery.append(getPropertyColumnName(rootEntityClass, sortField, sessionFactory)); // sessionFactory));
				if (psf.getSortOrder()) {
					sqlQuery.append(" asc");
				} else {
					sqlQuery.append(" desc");
				}
			}
			query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery.toString());
			if (psf.getFirst() != null) {
				query.setFirstResult(psf.getFirst());
			}
			if (psf.getPageSize() != null) {
				query.setMaxResults(psf.getPageSize());
			}
		} else {
			query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery.toString());
		}
		setQueryValues(query, queryValues, namedParameterValuesCache);
		query.addEntity(rootEntityClass);
		return query;
	}

	private static String createSQLSelectStatement(ArrayList<CriterionInstantVO> sortedCriterions, DBModule module, PSFVO psf, SessionFactory sessionFactory,
			ArrayList<QueryParameterValue> queryValues,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> tieMap,
			HashMap<Long, CriterionProperty> propertyMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionMap) throws Exception {
		StringBuilder hqlStatement = new StringBuilder("select distinct "); // always distinct
		StringBuilder sqlSelectStatement = new StringBuilder();
		StringBuilder hqlWhereClause = new StringBuilder();
		StringBuilder hqlTerm;
		HashMap<String, AssociationPath> explicitJoinsMap = new HashMap<String, AssociationPath>();
		HashMap<String, Class> propertyClassMap = new HashMap<String, Class>();
		Class entityClass = getRootEntityClass(module);
		String entityClassName = entityClass.getCanonicalName();
		String entityName = ENTITY_NAMES.get(module);
		hqlStatement.append(entityName).append(" from ").append(entityClassName).append(" as ").append(entityName);
		boolean whereAppended = false;
		if (sortedCriterions != null && sortedCriterions.size() > 0) {
			hqlWhereClause.append(" where (");
			whereAppended = true;
			for (int i = 0; i < sortedCriterions.size(); i++) {
				CriterionInstantVO criterion = sortedCriterions.get(i);
				if (criterion != null) {
					if (criterion.getTieId() != null) {
						org.phoenixctms.ctsms.enumeration.CriterionTie tie = tieMap.get(criterion.getTieId());
						switch (tie) {
							case AND:
								hqlWhereClause.append(" and ");
								break;
							case OR:
								hqlWhereClause.append(" or ");
								break;
							case LEFT_PARENTHESIS:
								hqlWhereClause.append("(");
								break;
							case RIGHT_PARENTHESIS:
								hqlWhereClause.append(")");
								break;
							default:
								// unimplemented tie
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_CRITERION_TIE, DefaultMessages.UNSUPPORTED_CRITERION_TIE,
										new Object[] { tie.toString() }));
						}
					}
					CriterionProperty property = null;
					if (criterion.getPropertyId() != null) {
						property = propertyMap.get(criterion.getPropertyId());
					}
					org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = null;
					if (criterion.getRestrictionId() != null) {
						restriction = restrictionMap.get(criterion.getRestrictionId());
					}
					if (property != null && restriction != null) {
						AssociationPath propertyNameAssociationPath = new AssociationPath(property.getProperty());
						String propertyName = aliasPropertyName(entityClass, propertyNameAssociationPath, entityName, explicitJoinsMap, propertyClassMap);
						hqlTerm = new StringBuilder();
						boolean queryValueAdded = false;
						switch (restriction) {
							case ID_EQ:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id = ?");
								break;
							case ID_NE:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id != ?");
								break;
							case EQ:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								break;
							case NE:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								break;
							case GT:
								hqlTerm.append(propertyName);
								hqlTerm.append(" > ?");
								break;
							case GE:
								hqlTerm.append(propertyName);
								hqlTerm.append(" >= ?");
								break;
							case LT:
								hqlTerm.append(propertyName);
								hqlTerm.append(" < ?");
								break;
							case LE:
								hqlTerm.append(propertyName);
								hqlTerm.append(" <= ?");
								break;
							case LIKE:
								hqlTerm.append(propertyName);
								hqlTerm.append(" like ?");
								break;
							case ILIKE:
								hqlTerm.append("lower(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") like lower(?)");
								break;
							case IS_EMPTY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" is empty");
								break;
							case IS_NOT_EMPTY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" is not empty");
								break;
							case IS_NULL:
								hqlTerm.append(propertyName);
								hqlTerm.append(" is null");
								break;
							case IS_NOT_NULL:
								hqlTerm.append(propertyName);
								hqlTerm.append(" is not null");
								break;
							case SIZE_EQ:
								hqlTerm.append("size(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") = ?");
								break;
							case SIZE_NE:
								hqlTerm.append("size(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") != ?");
								break;
							case SIZE_GT:
								hqlTerm.append("size(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") > ?");
								break;
							case SIZE_GE:
								hqlTerm.append("size(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") >= ?");
								break;
							case SIZE_LT:
								hqlTerm.append("size(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") < ?");
								break;
							case SIZE_LE:
								hqlTerm.append("size(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") <= ?");
								break;
							case IS_GT_TODAY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" > ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY));
								break;
							case IS_GE_TODAY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" >= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY));
								break;
							case IS_EQ_TODAY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY));
								break;
							case IS_NE_TODAY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY));
								break;
							case IS_LT_TODAY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" < ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY));
								break;
							case IS_LE_TODAY:
								hqlTerm.append(propertyName);
								hqlTerm.append(" <= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY));
								break;
							case IS_GT_NOW:
								hqlTerm.append(propertyName);
								hqlTerm.append(" > ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW));
								break;
							case IS_GE_NOW:
								hqlTerm.append(propertyName);
								hqlTerm.append(" >= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW));
								break;
							case IS_EQ_NOW:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW));
								break;
							case IS_NE_NOW:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW));
								break;
							case IS_LT_NOW:
								hqlTerm.append(propertyName);
								hqlTerm.append(" < ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW));
								break;
							case IS_LE_NOW:
								hqlTerm.append(propertyName);
								hqlTerm.append(" <= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW));
								break;
							case IS_GT_TODAY_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" > ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_GE_TODAY_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" >= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_EQ_TODAY_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_NE_TODAY_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LT_TODAY_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" < ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LE_TODAY_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" <= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_GT_NOW_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" > ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_GE_NOW_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" >= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_EQ_NOW_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_NE_NOW_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LT_NOW_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" < ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LE_NOW_PLUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" <= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_PLUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_GT_TODAY_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" > ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_GE_TODAY_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" >= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_EQ_TODAY_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_NE_TODAY_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LT_TODAY_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" < ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LE_TODAY_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" <= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.TODAY_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_GT_NOW_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" > ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_GE_NOW_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" >= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_EQ_NOW_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_NE_NOW_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LT_NOW_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" < ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_LE_NOW_MINUS_PERIOD:
								hqlTerm.append(propertyName);
								hqlTerm.append(" <= ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.NOW_MINUS_VARIABLE_PERIOD, VariablePeriod.fromString(criterion
										.getStringValue())));
								break;
							case IS_EQ_CONTEXT_USER_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_ID));
								break;
							case IS_NE_CONTEXT_USER_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_ID));
								break;
							case IS_ID_EQ_CONTEXT_USER_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_ID));
								break;
							case IS_ID_NE_CONTEXT_USER_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_ID));
								break;
							case IS_EQ_CONTEXT_USER_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_DEPARTMENT_ID));
								break;
							case IS_NE_CONTEXT_USER_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_DEPARTMENT_ID));
								break;
							case IS_ID_EQ_CONTEXT_USER_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_DEPARTMENT_ID));
								break;
							case IS_ID_NE_CONTEXT_USER_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_USER_DEPARTMENT_ID));
								break;
							case IS_EQ_CONTEXT_IDENTITY_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_ID));
								break;
							case IS_NE_CONTEXT_IDENTITY_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_ID));
								break;
							case IS_ID_EQ_CONTEXT_IDENTITY_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_ID));
								break;
							case IS_ID_NE_CONTEXT_IDENTITY_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_ID));
								break;
							case IS_EQ_CONTEXT_IDENTITY_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_DEPARTMENT_ID));
								break;
							case IS_NE_CONTEXT_IDENTITY_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(" != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_DEPARTMENT_ID));
								break;
							case IS_ID_EQ_CONTEXT_IDENTITY_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id = ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_DEPARTMENT_ID));
								break;
							case IS_ID_NE_CONTEXT_IDENTITY_DEPARTMENT_ID:
								hqlTerm.append(propertyName);
								hqlTerm.append(".id != ?");
								queryValueAdded = queryValues.add(new QueryParameterValue(QueryParameterValueType.CONTEXT_IDENTITY_DEPARTMENT_ID));
								break;
							case TRUE:
								hqlTerm.append("1 = 1"); // https://forum.hibernate.org/viewtopic.php?f=1&t=974534
								break;
								// case IS_ID_EQ_ENTITY_ID:
								// hqlTerm.append(propertyName);
								// hqlTerm.append(".id = ");
								// hqlTerm.append(entityName);
								// hqlTerm.append(".id");
								// break;
								// case IS_ID_NE_ENTITY_ID:
								// hqlTerm.append(propertyName);
								// hqlTerm.append(".id != ");
								// hqlTerm.append(entityName);
								// hqlTerm.append(".id");
								// break;
							case HOUR_EQ:
								hqlTerm.append("hour(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") == ?");
								break;
							case HOUR_GE:
								hqlTerm.append("hour(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") >= ?");
								break;
							case HOUR_GT:
								hqlTerm.append("hour(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") > ?");
								break;
							case HOUR_LE:
								hqlTerm.append("hour(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") <= ?");
								break;
							case HOUR_LT:
								hqlTerm.append("hour(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") < ?");
								break;
							case HOUR_NE:
								hqlTerm.append("hour(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") != ?");
								break;
							case MINUTE_EQ:
								hqlTerm.append("hour(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") == ?");
								break;
							case MINUTE_GE:
								hqlTerm.append("minute(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") >= ?");
								break;
							case MINUTE_GT:
								hqlTerm.append("minute(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") > ?");
								break;
							case MINUTE_LE:
								hqlTerm.append("minute(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") <= ?");
								break;
							case MINUTE_LT:
								hqlTerm.append("minute(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") < ?");
								break;
							case MINUTE_NE:
								hqlTerm.append("minute(");
								hqlTerm.append(propertyName);
								hqlTerm.append(") != ?");
								break;
							default:
								// unimplemented restriction
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_CRITERION_RESTRICTION,
										DefaultMessages.UNSUPPORTED_CRITERION_RESTRICTION, new Object[] { restriction.toString() }));
						}
						if (!CommonUtil.isUnaryCriterionRestriction(restriction) && !queryValueAdded) {
							queryValueAdded = queryValues.add(new QueryParameterValue(propertyNameAssociationPath.getFullQualifiedPropertyName(), property.getValueType(),
									criterion));
						}
						hqlWhereClause.append(appendStaticCriterionTerms(hqlTerm, propertyNameAssociationPath, queryValues,
								entityClass, entityName, explicitJoinsMap, propertyClassMap));
					}
				}
			}
			hqlWhereClause.append(")");
		}
		if (psf != null) {
			Map<String, String> filters = psf.getFilters();
			if (filters != null && filters.size() > 0) {
				boolean firstFilter = true;
				Iterator<Map.Entry<String, String>> filterIt = filters.entrySet().iterator();
				while (filterIt.hasNext()) {
					Map.Entry<String, String> filter = (Map.Entry<String, String>) filterIt.next();
					AssociationPath filterFieldAssociationPath = new AssociationPath(filter.getKey());
					String filterField = aliasPropertyName(entityClass, filterFieldAssociationPath, entityName, explicitJoinsMap, propertyClassMap);
					AssociationPath altFilterFieldAssociationPath = null;
					String altFilterField = null;
					Class pathClass = propertyClassMap.get(filterFieldAssociationPath.getPathString());
					if (pathClass != null) {
						String altFilter = ALTERNATIVE_FILTER_MAP.get(pathClass.getSimpleName() + AssociationPath.ASSOCIATION_PATH_SEPARATOR
								+ filterFieldAssociationPath.getPropertyName());
						if (!CommonUtil.isEmptyString(altFilter)) {
							altFilterFieldAssociationPath = new AssociationPath(filterFieldAssociationPath.getPathString() + AssociationPath.ASSOCIATION_PATH_SEPARATOR + altFilter);
							altFilterField = aliasPropertyName(entityClass, altFilterFieldAssociationPath, entityName, explicitJoinsMap, propertyClassMap);
						}
					}
					if (firstFilter) {
						if (whereAppended) {
							hqlWhereClause.append(" and (");
						} else {
							hqlWhereClause.append(" where (");
							whereAppended = true;
						}
						firstFilter = false;
					} else {
						hqlWhereClause.append(" and ");
					}
					applyFilter(hqlWhereClause, queryValues, filterField, propertyClassMap.get(filterFieldAssociationPath.getFullQualifiedPropertyName()), filter.getValue(),
							altFilterField, altFilterFieldAssociationPath != null ? propertyClassMap.get(altFilterFieldAssociationPath.getFullQualifiedPropertyName()) : null);
					if (!filterIt.hasNext()) {
						hqlWhereClause.append(")");
					}
				}
			}
		}
		appendJoins(hqlStatement, explicitJoinsMap);
		hqlStatement.append(hqlWhereClause);
		sqlSelectStatement.append(hqlToSql(hqlStatement.toString(), sessionFactory));
		return sanitizeColumnProjectionList(sqlSelectStatement.toString());
	}

	private static StringBuilder createSQLSetStatement(CriteriaInstantVO criteriaInstantVO,
			DBModule module, PSFVO psf, SessionFactory sessionFactory, ArrayList<QueryParameterValue> queryValues,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> tieMap,
			HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, String> tieNameMap,
			HashMap<Long, CriterionProperty> propertyMap,
			// HashMap<String, String> propertyNameMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionMap,
			HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String> restrictionNameMap) throws Exception {
		ArrayList<CriterionInstantVO> sortedCriterions = null;
		if (criteriaInstantVO != null) {
			Collection<CriterionInstantVO> criterions = criteriaInstantVO.getCriterions();
			if (criterions != null && criterions.size() > 0) {
				sortedCriterions = new ArrayList<CriterionInstantVO>(criterions);
				Collections.sort(sortedCriterions, new VOPositionComparator(false));
			}
		}
		StringBuilder sqlSetStatement = new StringBuilder();
		StringBuilder setOperationExpression = new StringBuilder();
		boolean createSQLStatement = sessionFactory != null;
		int selectCount = CommonUtil.SET_OPERATION_EXPRESSION_FIRST_INDEX;
		int lastSetTieIndex = -1;
		if (sortedCriterions != null && sortedCriterions.size() > 0) {
			for (int i = 0; i < sortedCriterions.size(); i++) {
				CriterionInstantVO criterion = sortedCriterions.get(i);
				if (criterion != null) {
					if (criterion.getTieId() != null) {
						String sqlSetKeyWord = null;
						boolean isSetTie = false;
						org.phoenixctms.ctsms.enumeration.CriterionTie tie = tieMap.get(criterion.getTieId());
						switch (tie) {
							case EXCEPT:
							case INTERSECT:
							case UNION:
								isSetTie = true;
								sqlSetKeyWord = getSqlSetKeyWord(tie);
								break;
							case LEFT_PARENTHESIS:
							case RIGHT_PARENTHESIS:
								break;
							default:
								break;
						}
						if (isSetTie) {
							int offset = lastSetTieIndex + 1;
							int count = i - offset;
							int leftParenthesisCount = getCriterionTieCount(sortedCriterions, offset, count, org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS,
									tieMap);
							int rightParenthesisCount = getCriterionTieCount(sortedCriterions, offset, count, org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS,
									tieMap);
							if (leftParenthesisCount > rightParenthesisCount) {
								int balance = leftParenthesisCount - rightParenthesisCount;
								for (int j = 0; j < balance; j++) {
									if (createSQLStatement) {
										sqlSetStatement.append("(");
									}
									setOperationExpression.append(getSetExpressionTieName(org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS, tieNameMap));
								}
								if (createSQLStatement) {
									sqlSetStatement.append(createSQLSelectStatement(CoreUtil.getSubList(sortedCriterions, offset + balance, count - balance), module, psf,
											sessionFactory, queryValues, tieMap, propertyMap, restrictionMap));
								}
								appendSelectHelper(setOperationExpression, selectCount);
							} else if (leftParenthesisCount < rightParenthesisCount) {
								int balance = rightParenthesisCount - leftParenthesisCount;
								if (createSQLStatement) {
									sqlSetStatement.append(createSQLSelectStatement(CoreUtil.getSubList(sortedCriterions, offset, count - balance), module, psf, sessionFactory,
											queryValues, tieMap, propertyMap, restrictionMap));
								}
								appendSelectHelper(setOperationExpression, selectCount);
								for (int j = 0; j < balance; j++) {
									if (createSQLStatement) {
										sqlSetStatement.append(")");
									}
									setOperationExpression.append(getSetExpressionTieName(org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS, tieNameMap));
								}
							} else {
								if (createSQLStatement) {
									sqlSetStatement.append(createSQLSelectStatement(CoreUtil.getSubList(sortedCriterions, offset, count), module, psf, sessionFactory, queryValues,
											tieMap, propertyMap, restrictionMap));
								}
								appendSelectHelper(setOperationExpression, selectCount);
							}
							if (createSQLStatement) {
								sqlSetStatement.append(" ");
							}
							setOperationExpression.append(" ");
							if (sqlSetKeyWord != null && sqlSetKeyWord.length() > 0) {
								if (createSQLStatement) {
									sqlSetStatement.append(sqlSetKeyWord);
								}
								setOperationExpression.append(getSetExpressionTieName(tie, tieNameMap));
								// xif (tieNameMap != null && tieMap.containsKey(tie)) {
								// setOperationExpression.append(tieNameMap.get(tie));
								// } else {
								// setOperationExpression.append(sqlSetKeyWord);
								// }
							} else {
								throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.SQL_SET_OPERATION_KEYWORD_UNDEFINED,
										DefaultMessages.SQL_SET_OPERATION_KEYWORD_UNDEFINED, new Object[] { tie.toString() }));
							}
							if (createSQLStatement) {
								sqlSetStatement.append(" ");
							}
							setOperationExpression.append(" ");
							lastSetTieIndex = i;
							selectCount++;
						} else { // if (!isParenthesis) {
							if (criterion.getSelectStatementIndex() == null) {
								criterion.setSelectStatementIndex(selectCount);
							}
						}
					} else {
						if (criterion.getSelectStatementIndex() == null) {
							criterion.setSelectStatementIndex(selectCount);
						}
					}
				}
			}
		}
		if (lastSetTieIndex >= 0) {
			int offset = lastSetTieIndex + 1;
			int count = sortedCriterions.size() - offset;
			int leftParenthesisCount = getCriterionTieCount(sortedCriterions, offset, count, org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS, tieMap);
			int rightParenthesisCount = getCriterionTieCount(sortedCriterions, offset, count, org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS, tieMap);
			if (leftParenthesisCount < rightParenthesisCount) {
				int balance = rightParenthesisCount - leftParenthesisCount;
				if (createSQLStatement) {
					sqlSetStatement.append(createSQLSelectStatement(CoreUtil.getSubList(sortedCriterions, offset, count - balance), module, psf, sessionFactory, queryValues,
							tieMap, propertyMap, restrictionMap));
				}
				appendSelectHelper(setOperationExpression, selectCount);
				for (int j = 0; j < balance; j++) {
					if (createSQLStatement) {
						sqlSetStatement.append(")");
					}
					setOperationExpression.append(getSetExpressionTieName(org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS, tieNameMap));
				}
			} else if (leftParenthesisCount == rightParenthesisCount) {
				if (createSQLStatement) {
					sqlSetStatement.append(createSQLSelectStatement(CoreUtil.getSubList(sortedCriterions, offset, count), module, psf, sessionFactory, queryValues, tieMap,
							propertyMap, restrictionMap));
				}
				appendSelectHelper(setOperationExpression, selectCount);
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNBALANCED_SET_PARENTHESES, DefaultMessages.UNBALANCED_SET_PARENTHESES));
			}
		} else {
			if (createSQLStatement) {
				sqlSetStatement.append(createSQLSelectStatement(sortedCriterions, module, psf, sessionFactory, queryValues, tieMap, propertyMap, restrictionMap));
			}
			appendSelectHelper(setOperationExpression, selectCount);
		}
		if (criteriaInstantVO != null) {
			if (CommonUtil.isEmptyString(criteriaInstantVO.getSetOperationExpression())) {
				criteriaInstantVO.setSetOperationExpression(setOperationExpression.toString());
			}
			criteriaInstantVO.setSelectStatementCount(selectCount - CommonUtil.SET_OPERATION_EXPRESSION_FIRST_INDEX + 1);
		}
		return sqlSetStatement;
	}

	private static int getCriterionTieCount(
			ArrayList<CriterionInstantVO> criterions,
			int offset, int count,
			org.phoenixctms.ctsms.enumeration.CriterionTie tie,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> tieMap) {
		int tieCount = 0;
		if (criterions != null && offset >= 0 && count >= 0) {
			for (int i = offset; (i < offset + count) && (i < criterions.size()); i++) {
				CriterionInstantVO criterion = criterions.get(i);
				if (criterion.getTieId() != null
						&& tieMap.get(criterion.getTieId()).equals(tie)) {
					tieCount++;
				}
			}
		}
		return tieCount;
	}

	public static int getCriterionTieCount(
			ArrayList<CriterionInstantVO> criterions,
			org.phoenixctms.ctsms.enumeration.CriterionTie tie,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> tieMap) {
		return getCriterionTieCount(criterions, 0, criterions == null ? 0 : criterions.size(), tie, tieMap);
	}

	private static String getPropertyColumnName(Class entity, String property, SessionFactory sessionFactory) {
		ClassMetadata hibernateMetadata = sessionFactory.getClassMetadata(entity);
		AbstractEntityPersister persister = (AbstractEntityPersister) hibernateMetadata;
		String[] columnNames = persister.getPropertyColumnNames(property);
		return columnNames[0];
	}

	private static Class getRootEntityClass(DBModule module) {
		switch (module) {
			case INVENTORY_DB:
				return InventoryImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			case STAFF_DB:
				return StaffImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			case COURSE_DB:
				return CourseImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			case USER_DB:
				return UserImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			case TRIAL_DB:
				return TrialImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			case PROBAND_DB:
				return ProbandImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			case INPUT_FIELD_DB:
				return InputFieldImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			case MASS_MAIL_DB:
				return MassMailImpl.class; // "org.phoenixctms.ctsms.domain.Inventory";
			default:
				// unsupported root entity
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_DB_MODULE, DefaultMessages.UNSUPPORTED_DB_MODULE,
						new Object[] { module.toString() }));
		}
	}

	public static Long getSearchQueryResultCount(CriteriaInstantVO criteriaInstantVO, DBModule module, PSFVO psf, SessionFactory sessionFactory,
			CriterionTieDao criterionTieDao, CriterionPropertyDao criterionPropertyDao, CriterionRestrictionDao criterionRestrictionDao) throws Exception {
		ArrayList<QueryParameterValue> queryValues = new ArrayList<QueryParameterValue>();
		HashMap<NamedParameterValues, Object> namedParameterValuesCache = new HashMap<NamedParameterValues, Object>();
		StringBuilder sqlQuery = createSQLSetStatement(criteriaInstantVO, module, psf, sessionFactory, queryValues,
				createCriterionTieMap(criterionTieDao),
				createCriterionTieNameMap(criterionTieDao),
				createCriterionPropertyMap(module, criterionPropertyDao),
				// null, // createCriterionPropertyNameMap(module, criterionPropertyDao),
				createCriterionRestrictionMap(criterionRestrictionDao),
				null); // createCriterionRestrictionNameMap(criterionRestrictionDao));

		StringBuilder countStatement = new StringBuilder("select count(*) from (");
		countStatement.append(sqlQuery);
		countStatement.append(") as resultset");
		Query countQuery = sessionFactory.getCurrentSession().createSQLQuery(countStatement.toString());
		setQueryValues(countQuery, queryValues, namedParameterValuesCache);

		Long count = new Long(countQuery.uniqueResult().toString());
		if (psf != null) {
			if (psf.getUpdateRowCount()) {
				psf.setRowCount(count);
			}
		}
		return count;

	}

	public static String getSetExpressionTieName(org.phoenixctms.ctsms.enumeration.CriterionTie tie, HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, String> tieNameMap) {
		if (tieNameMap != null && tieNameMap.containsKey(tie)) {
			return tieNameMap.get(tie);
		} else {
			return getSqlSetKeyWord(tie);
		}
	}

	private static String getSqlSetKeyWord(org.phoenixctms.ctsms.enumeration.CriterionTie tie) {
		//		if (tie != null) {
		switch (tie) {
			case EXCEPT:
				return Settings.getString(SettingCodes.SQL_EXCEPT_KEYWORD, Bundle.SETTINGS, null);
			case INTERSECT:
				return Settings.getString(SettingCodes.SQL_INTERSECT_KEYWORD, Bundle.SETTINGS, null);
			case UNION:
				return Settings.getString(SettingCodes.SQL_UNION_KEYWORD, Bundle.SETTINGS, null);
			case LEFT_PARENTHESIS:
				return "(";
			case RIGHT_PARENTHESIS:
				return ")";
			default:
				break;
		}
		//		}
		return null;
	}

	// //http://narcanti.keyboardsamurais.de/hibernate-hql-to-sql-translation.html
	private static String hqlToSql(String hqlQueryText, SessionFactory sessionFactory) {
		if (hqlQueryText != null && hqlQueryText.trim().length() > 0
				&& sessionFactory != null) {
			final QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
			final SessionFactoryImplementor factory = (SessionFactoryImplementor) sessionFactory;
			final QueryTranslator translator = translatorFactory
					.createQueryTranslator(hqlQueryText, hqlQueryText,
							Collections.EMPTY_MAP, factory);
			translator.compile(Collections.EMPTY_MAP, false);
			return translator.getSQLString();
		}
		return null;
	}

	public static void parseSearchQuery(CriteriaInstantVO criteriaInstantVO, DBModule module,
			CriterionTieDao criterionTieDao) throws Exception {
		createSQLSetStatement(criteriaInstantVO, module, null, null, new ArrayList<QueryParameterValue>(),
				createCriterionTieMap(criterionTieDao),
				createCriterionTieNameMap(criterionTieDao),
				null,
				// null,
				null,
				null);
	}

	private static void prependRootEntityName(AssociationPath fullyQualifiedPropertyName, String rootEntityName) {
		if (fullyQualifiedPropertyName.getPathDepth() == 0 || !rootEntityName.equals(fullyQualifiedPropertyName.getPathElement(0))) {
			fullyQualifiedPropertyName.prependPathElement(rootEntityName);
		}
	}

	private static String sanitizeColumnProjectionList(String sqlStatement) {
		String[] columnProjectionListPatternElements = { "select distinct ", "[a-z0-9_]+", Pattern.quote(SQL_ASSOCIATION_PATH_SEPARATOR), " as ", ", ", " from " };
		StringBuilder columnProjectionListPattern = new StringBuilder("^");
		columnProjectionListPattern.append(columnProjectionListPatternElements[0]);
		columnProjectionListPattern.append("((");
		columnProjectionListPattern.append(columnProjectionListPatternElements[1]);
		columnProjectionListPattern.append(columnProjectionListPatternElements[2]);
		columnProjectionListPattern.append("(");
		columnProjectionListPattern.append(columnProjectionListPatternElements[1]);
		columnProjectionListPattern.append(")");
		columnProjectionListPattern.append(columnProjectionListPatternElements[3]);
		columnProjectionListPattern.append(columnProjectionListPatternElements[1]);
		columnProjectionListPattern.append("(");
		columnProjectionListPattern.append(columnProjectionListPatternElements[4]);
		columnProjectionListPattern.append(")?)+)");
		columnProjectionListPattern.append(columnProjectionListPatternElements[5]);
		Matcher columnProjectionListMatcher = Pattern.compile(columnProjectionListPattern.toString(), Pattern.CASE_INSENSITIVE).matcher(sqlStatement);
		StringBuilder result = new StringBuilder(columnProjectionListPatternElements[0]);
		if (columnProjectionListMatcher.lookingAt()) {
			String[] columnProjectionList = columnProjectionListMatcher.group(1).split(columnProjectionListPatternElements[4], -1);
			for (int i = 0; i < columnProjectionList.length; i++) {
				String[] columnProjection = columnProjectionList[i].trim().split(columnProjectionListPatternElements[3], 2);
				String columnIdentifier = columnProjection[0].trim();
				String[] columnName = columnIdentifier.split(columnProjectionListPatternElements[2], 2);
				if (i > 0) {
					result.append(columnProjectionListPatternElements[4]);
				}
				result.append(columnIdentifier);
				result.append(columnProjectionListPatternElements[3]);
				result.append(columnName[1].trim());
			}
			result.append(columnProjectionListPatternElements[5]);
			return columnProjectionListMatcher.replaceFirst(java.util.regex.Matcher.quoteReplacement(result.toString()));
		}
		return sqlStatement;
	}

	public static void setQueryParameterFromCriterion(String propertyName, Query query, CriterionValueType valueType, int pos, CriterionInstantVO value) throws Exception {
		switch (valueType) {
			case BOOLEAN:
				query.setBoolean(pos, value.getBooleanValue());
				break;
			case BOOLEAN_HASH:
				query.setBinary(pos, CryptoUtil.hashForSearch(value.getBooleanValue()));
				break;
			case DATE:
				query.setDate(pos, value.getDateValue());
				break;
			case DATE_HASH:
				query.setBinary(pos, CryptoUtil.hashForSearch(value.getDateValue()));
				break;
			case TIME:
				query.setTime(pos, value.getTimeValue());
				break;
			case TIME_HASH:
				query.setBinary(pos, CryptoUtil.hashForSearch(value.getTimeValue()));
				break;
			case FLOAT:
				query.setFloat(pos, value.getFloatValue().floatValue());
				break;
			case FLOAT_HASH:
				query.setBinary(pos, CryptoUtil.hashForSearch(value.getFloatValue()));
				break;
			case LONG:
				query.setBigInteger(pos, new BigInteger(value.getLongValue().toString()));
				break;
			case LONG_HASH:
				query.setBinary(pos, CryptoUtil.hashForSearch(value.getLongValue()));
				break;
			case STRING:
				query.setString(pos, value.getStringValue());
				break;
			case STRING_HASH:
				query.setBinary(pos, CryptoUtil.hashForSearch(value.getStringValue()));
				break;
			case TIMESTAMP:
				query.setTimestamp(pos, value.getTimestampValue());
				break;
			case TIMESTAMP_HASH:
				query.setBinary(pos, CryptoUtil.hashForSearch(value.getTimestampValue()));
				break;
			case NONE:
				break;
			default:
				// datatype unimplemented
				throw new IllegalArgumentException(MessageFormat.format(CommonUtil.UNSUPPORTED_CRITERION_VALUE_TYPE, valueType.toString()));
		}
	}

	public static void setQueryParameterFromString(Query query, Class propertyClass, int pos, String value) throws Exception {
		if (propertyClass.equals(String.class)) {
			query.setString(pos, value);
		} else if (propertyClass.equals(Long.class)) {
			query.setBigInteger(pos, new BigInteger(value));
		} else if (propertyClass.equals(java.lang.Long.TYPE)) {
			query.setBigInteger(pos, new BigInteger(value));
		} else if (propertyClass.equals(Integer.class)) {
			query.setInteger(pos, new Integer(value));
		} else if (propertyClass.equals(java.lang.Integer.TYPE)) {
			query.setInteger(pos, new Integer(value));
		} else if (propertyClass.equals(Boolean.class)) {
			query.setBoolean(pos, new Boolean(value));
		} else if (propertyClass.equals(java.lang.Boolean.TYPE)) {
			query.setBoolean(pos, new Boolean(value));
		} else if (propertyClass.equals(Float.class)) {
			query.setFloat(pos, CommonUtil.parseFloat(value, CoreUtil.getUserContext().getDecimalSeparator()));
		} else if (propertyClass.equals(java.lang.Float.TYPE)) {
			query.setFloat(pos, CommonUtil.parseFloat(value, CoreUtil.getUserContext().getDecimalSeparator()));
		} else if (propertyClass.equals(Double.class)) {
			query.setDouble(pos, CommonUtil.parseDouble(value, CoreUtil.getUserContext().getDecimalSeparator()));
		} else if (propertyClass.equals(java.lang.Double.TYPE)) {
			query.setDouble(pos, CommonUtil.parseDouble(value, CoreUtil.getUserContext().getDecimalSeparator()));
		} else if (propertyClass.equals(Date.class)) {
			query.setDate(pos, CommonUtil.parseDate(value, CommonUtil.getInputDatePattern(CoreUtil.getUserContext().getDateFormat())));
		} else if (propertyClass.equals(Timestamp.class)) {
			query.setTimestamp(pos, CommonUtil.dateToTimestamp(CommonUtil.parseDate(value, CommonUtil.getInputDateTimePattern(CoreUtil.getUserContext().getDateFormat()))));
		} else if (propertyClass.equals(VariablePeriod.class)) {
			query.setString(pos, VariablePeriod.fromString(value).name());
		} else if (propertyClass.equals(AuthenticationType.class)) {
			query.setString(pos, AuthenticationType.fromString(value).name());
		} else if (propertyClass.equals(Sex.class)) {
			query.setString(pos, Sex.fromString(value).name());
		} else if (propertyClass.equals(RandomizationMode.class)) {
			query.setString(pos, RandomizationMode.fromString(value).name());
		} else if (propertyClass.equals(DBModule.class)) {
			query.setString(pos, DBModule.fromString(value).name());
		} else if (propertyClass.equals(HyperlinkModule.class)) {
			query.setString(pos, HyperlinkModule.fromString(value).name());
		} else if (propertyClass.equals(JournalModule.class)) {
			query.setString(pos, JournalModule.fromString(value).name());
		} else if (propertyClass.equals(FileModule.class)) {
			query.setString(pos, FileModule.fromString(value).name());
		} else if (propertyClass.equals(Color.class)) {
			query.setString(pos, Color.fromString(value).name());
		} else if (propertyClass.equals(InputFieldType.class)) {
			query.setString(pos, InputFieldType.fromString(value).name());
		} else if (propertyClass.equals(EventImportance.class)) {
			query.setString(pos, EventImportance.fromString(value).name());
		} else if (propertyClass.equals(ExportStatus.class)) {
			query.setString(pos, ExportStatus.fromString(value).name());
		} else if (propertyClass.isArray() && propertyClass.getComponentType().equals(java.lang.Byte.TYPE)) { // only string hashes supported, no boolean, float, etc...
			query.setBinary(pos, CryptoUtil.hashForSearch(value));
		} else {
			// illegal type...
			throw new IllegalArgumentException(MessageFormat.format(CommonUtil.INPUT_TYPE_NOT_SUPPORTED, propertyClass.toString()));
		}
	}

	private static void setQueryValues(Query query, ArrayList<QueryParameterValue> queryValues, HashMap<NamedParameterValues, Object> namedParameterValuesCache) throws Exception {
		for (int i = 0; i < queryValues.size(); i++) {
			queryValues.get(i).set(query, i, namedParameterValuesCache);
		}
	}

	private QueryUtil() {
	}
}
