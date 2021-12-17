package org.phoenixctms.ctsms.query;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.adapt.ExpirationEntityAdapter;
import org.phoenixctms.ctsms.adapt.ReminderEntityAdapter;
import org.phoenixctms.ctsms.enumeration.AuthenticationType;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.EventImportance;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JobStatus;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.enumeration.VisitScheduleDateMode;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.PSFVO;

public final class CriteriaUtil {

	private enum RestrictionCriterionTypes {

		GT("({0}) > ?"),
		GE("({0}) >= ?"),
		LT("({0}) < ?"),
		LE("({0}) <= ?"),
		IS_NULL("({0}) is null"),
		IS_NOT_NULL("({0}) is not null");

		private final String term;

		private RestrictionCriterionTypes(final String term) {
			this.term = term;
		}

		@Override
		public String toString() {
			return term;
		}
	}

	public final static Pattern PROPERTY_NAME_REGEXP = Pattern.compile("^[a-zA-Z0-9._]+$");
	private static HashSet<String> EXACT_STRING_FILTER_ENTITY_FIELDS = new HashSet<String>();
	static {
		EXACT_STRING_FILTER_ENTITY_FIELDS.add("logicalPath");
		EXACT_STRING_FILTER_ENTITY_FIELDS.add("code"); //randomization list code
	}
	private final static String ALIAS_PREFIX = "_";
	private final static HashMap<String, String> ALTERNATIVE_FILTER_MAP = new HashMap<String, String>();
	static {
		ALTERNATIVE_FILTER_MAP.put("ProbandContactParticulars.lastNameHash", "alias");
		ALTERNATIVE_FILTER_MAP.put("AnimalContactParticulars.animalName", "alias");
	}
	private final static String UNSUPPORTED_BINARY_RESTRICTION_CRITERION_TYPE = "unsupported binary restriction criterion type {0}";
	private final static String UNSUPPORTED_UNARY_RESTRICTION_CRITERION_TYPE = "unsupported unary restriction criterion type {0}";

	private static <T> void addReminderItem(ArrayList<T> resultSet, ReminderEntityAdapter reminderItem, Date reminderStart, Boolean notify) {
		if (!reminderItem.isDismissable() || !reminderItem.isRecurrenceDismissed(reminderStart)) {// !(reminderItem.isDismissed() && reminderStart.compareTo(new
			if (notify != null) {
				if (notify.booleanValue() == reminderItem.isNotify()) {
					resultSet.add((T) reminderItem.getItem());
				}
			} else {
				resultSet.add((T) reminderItem.getItem());
			}
		}
	}

	private static org.hibernate.criterion.Criterion applyAlternativeFilter(SubCriteriaMap criteriaMap, AssociationPath filterFieldAssociationPath, String value, String timeZone)
			throws Exception {
		Class pathClass = criteriaMap.getPropertyClassMap().get(filterFieldAssociationPath.getPathString());
		if (pathClass != null) {
			String altFilter = ALTERNATIVE_FILTER_MAP.get(pathClass.getSimpleName() + AssociationPath.ASSOCIATION_PATH_SEPARATOR + filterFieldAssociationPath.getPropertyName());
			if (!CommonUtil.isEmptyString(altFilter)) {
				AssociationPath altFilterFieldAssociationPath = new AssociationPath(
						filterFieldAssociationPath.getPathString() + AssociationPath.ASSOCIATION_PATH_SEPARATOR + altFilter);
				criteriaMap.createCriteriaForAttribute(altFilterFieldAssociationPath);
				return applyFilter(altFilterFieldAssociationPath.getPropertyName(),
						criteriaMap.getPropertyClassMap().get(altFilterFieldAssociationPath.getFullQualifiedPropertyName()), value, null, timeZone);
			}
		}
		return null;
	}

	public static void applyClosedIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		if (intervalCriteria != null) {
			org.hibernate.criterion.Criterion criterion = getClosedIntervalCriterion(from, to, or);
			if (criterion != null) {
				intervalCriteria.add(criterion);
			}
		}
	}

	private static org.hibernate.criterion.Criterion getRestrictionCriterion(RestrictionCriterionTypes restriction, String propertyName, Timestamp value) {
		return getRestrictionCriterion(restriction, propertyName, value, Hibernate.TIMESTAMP);
	}

	private static org.hibernate.criterion.Criterion getRestrictionCriterion(RestrictionCriterionTypes restriction, String propertyName, Object value,
			org.hibernate.type.NullableType type) {
		if (PROPERTY_NAME_REGEXP.matcher(propertyName).find()) {
			switch (restriction) {
				case GT:
					return Restrictions.gt(propertyName, value);
				case GE:
					return Restrictions.ge(propertyName, value);
				case LT:
					return Restrictions.lt(propertyName, value);
				case LE:
					return Restrictions.le(propertyName, value);
				default:
					throw new IllegalArgumentException(MessageFormat.format(UNSUPPORTED_BINARY_RESTRICTION_CRITERION_TYPE, restriction.toString()));
			}
		} else {
			return Restrictions.sqlRestriction(MessageFormat.format(restriction.toString(), propertyName),
					//"  "substr({alias}.logical_path, 1, length(?)) = ?",
					new Object[] { value },
					new org.hibernate.type.NullableType[] { type });
		}
	}

	private static org.hibernate.criterion.Criterion getRestrictionCriterion(RestrictionCriterionTypes restriction, String propertyName) {
		if (PROPERTY_NAME_REGEXP.matcher(propertyName).find()) {
			switch (restriction) {
				case IS_NULL:
					return Restrictions.isNull(propertyName);
				case IS_NOT_NULL:
					return Restrictions.isNotNull(propertyName);
				default:
					throw new IllegalArgumentException(MessageFormat.format(UNSUPPORTED_UNARY_RESTRICTION_CRITERION_TYPE, restriction.toString()));
			}
		} else {
			return Restrictions.sqlRestriction(MessageFormat.format(restriction.toString(), propertyName));
		}
	}

	public static org.hibernate.criterion.Criterion getClosedIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		return getClosedIntervalCriterion(from, to, or, "start", "stop");
	}

	public static org.hibernate.criterion.Criterion getClosedIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, String startPropertyName,
			String stopPropertyName) {
		if (from != null && to != null) {
			if (to.before(from)) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
			}
			return applyOr(
					Restrictions.or(
							Restrictions.or( // partial interval overlappings:
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to)),
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to))),
							Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, from),
									getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, to))),
					or);
		} else if (from != null && to == null) {
			return applyOr(
					Restrictions.or( // partial interval overlappings:
							getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
							getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from)),
					or);
		} else if (from == null && to != null) {
			return applyOr(getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to), or);
		}
		return null;
	}

	public static void applyCurrentStatusCriterion(Criteria statusEntryCriteria, Timestamp currentTimestamp, org.hibernate.criterion.Criterion or) {
		if (statusEntryCriteria != null) {
			statusEntryCriteria.add(getCurrentStatusCriterion(currentTimestamp, or));
		}
	}

	public static org.hibernate.criterion.Criterion getCurrentStatusCriterion(Timestamp currentTimestamp, org.hibernate.criterion.Criterion or) {
		return getCurrentStatusCriterion(currentTimestamp, or, "start", "stop");
	}

	public static org.hibernate.criterion.Criterion getCurrentStatusCriterion(Timestamp currentTimestamp, org.hibernate.criterion.Criterion or, String startPropertyName,
			String stopPropertyName) {
		Timestamp now;
		if (currentTimestamp == null) {
			now = new Timestamp(System.currentTimeMillis());
		} else {
			now = currentTimestamp;
		}
		return applyOr(
				Restrictions.or(
						Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, startPropertyName),
								Restrictions.and(
										getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, now),
										Restrictions.or(getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, now),
												getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, stopPropertyName)))),
						Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, stopPropertyName),
								Restrictions.and(
										Restrictions.or(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, now),
												getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, startPropertyName)),
										getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, now)))),
				or);
	}

	private static org.hibernate.criterion.Criterion applyFilter(String propertyName, Class propertyClass, String value, org.hibernate.criterion.Criterion or, String timeZone)
			throws Exception {
		if (propertyClass.equals(String.class)) {
			if (EXACT_STRING_FILTER_ENTITY_FIELDS.contains(propertyName)) {
				return applyOr(Restrictions.eq(propertyName, new String(value)), or);
			} else {
				return applyOr(Restrictions.ilike(propertyName, new String(value), MatchMode.ANYWHERE), or);
			}
		} else if (propertyClass.equals(Long.class)) {
			return applyOr(Restrictions.eq(propertyName, new Long(value)), or);
		} else if (propertyClass.equals(java.lang.Long.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Long.parseLong(value)), or);
		} else if (propertyClass.equals(Integer.class)) {
			return applyOr(Restrictions.eq(propertyName, new Integer(value)), or);
		} else if (propertyClass.equals(java.lang.Integer.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Integer.parseInt(value)), or);
		} else if (propertyClass.equals(Boolean.class)) {
			return applyOr(Restrictions.eq(propertyName, new Boolean(value)), or);
		} else if (propertyClass.equals(java.lang.Boolean.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Boolean.parseBoolean(value)), or);
		} else if (propertyClass.equals(Float.class)) {
			return applyOr(Restrictions.eq(propertyName, CommonUtil.parseFloat(value, CoreUtil.getUserContext().getDecimalSeparator())), or);
		} else if (propertyClass.equals(java.lang.Float.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, CommonUtil.parseFloat(value, CoreUtil.getUserContext().getDecimalSeparator())), or);
		} else if (propertyClass.equals(Double.class)) {
			return applyOr(Restrictions.eq(propertyName, CommonUtil.parseDouble(value, CoreUtil.getUserContext().getDecimalSeparator())), or);
		} else if (propertyClass.equals(java.lang.Double.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, CommonUtil.parseDouble(value, CoreUtil.getUserContext().getDecimalSeparator())), or);
		} else if (propertyClass.equals(Date.class)) {
			Date date;
			if (!CommonUtil.isEmptyString(timeZone)) {
				date = CommonUtil.parseDate(value, CommonUtil.getInputDatePattern(CoreUtil.getUserContext().getDateFormat()), CommonUtil.timeZoneFromString(timeZone));
			} else {
				date = CommonUtil.parseDate(value, CommonUtil.getInputDatePattern(CoreUtil.getUserContext().getDateFormat()));
			}
			return applyOr(Restrictions.eq(propertyName, date), or);
		} else if (propertyClass.equals(Timestamp.class)) {
			Date date = CommonUtil.parseDate(value, CommonUtil.getInputDatePattern(CoreUtil.getUserContext().getDateFormat()));
			Date from = DateCalc.getStartOfDay(date);
			Date to = DateCalc.getEndOfDay(date);
			if (!CommonUtil.isEmptyString(timeZone)) {
				from = DateCalc.convertTimezone(from, CommonUtil.timeZoneFromString(timeZone), TimeZone.getDefault());
				to = DateCalc.convertTimezone(to, CommonUtil.timeZoneFromString(timeZone), TimeZone.getDefault());
			}
			return applyOr(Restrictions.between(propertyName, CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to)), or);
		} else if (propertyClass.equals(VariablePeriod.class)) {
			return applyOr(Restrictions.eq(propertyName, VariablePeriod.fromString(value)), or);
		} else if (propertyClass.equals(AuthenticationType.class)) {
			return applyOr(Restrictions.eq(propertyName, AuthenticationType.fromString(value)), or);
		} else if (propertyClass.equals(Sex.class)) {
			return applyOr(Restrictions.eq(propertyName, Sex.fromString(value)), or);
		} else if (propertyClass.equals(RandomizationMode.class)) {
			return applyOr(Restrictions.eq(propertyName, RandomizationMode.fromString(value)), or);
		} else if (propertyClass.equals(DBModule.class)) {
			return applyOr(Restrictions.eq(propertyName, DBModule.fromString(value)), or);
		} else if (propertyClass.equals(HyperlinkModule.class)) {
			return applyOr(Restrictions.eq(propertyName, HyperlinkModule.fromString(value)), or);
		} else if (propertyClass.equals(JournalModule.class)) {
			return applyOr(Restrictions.eq(propertyName, JournalModule.fromString(value)), or);
		} else if (propertyClass.equals(FileModule.class)) {
			return applyOr(Restrictions.eq(propertyName, FileModule.fromString(value)), or);
		} else if (propertyClass.equals(Color.class)) {
			return applyOr(Restrictions.eq(propertyName, Color.fromString(value)), or);
		} else if (propertyClass.equals(InputFieldType.class)) {
			return applyOr(Restrictions.eq(propertyName, InputFieldType.fromString(value)), or);
		} else if (propertyClass.equals(EventImportance.class)) {
			return applyOr(Restrictions.eq(propertyName, EventImportance.fromString(value)), or);
		} else if (propertyClass.equals(JobStatus.class)) {
			return applyOr(Restrictions.eq(propertyName, JobStatus.fromString(value)), or);
		} else if (propertyClass.equals(PaymentMethod.class)) {
			return applyOr(Restrictions.eq(propertyName, PaymentMethod.fromString(value)), or);
		} else if (propertyClass.equals(VisitScheduleDateMode.class)) {
			return applyOr(Restrictions.eq(propertyName, VisitScheduleDateMode.fromString(value)), or);
		} else if (propertyClass.isArray() && propertyClass.getComponentType().equals(java.lang.Byte.TYPE)) { // only string hashes supported, no boolean, float, etc...
			return applyOr(Restrictions.eq(propertyName, CryptoUtil.hashForSearch(value)), or);
		} else {
			// illegal type...
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.CRITERIA_PROPERTY_TYPE_NOT_SUPPORTED, DefaultMessages.CRITERIA_PROPERTY_TYPE_NOT_SUPPORTED,
					new Object[] { propertyClass.toString(), propertyName }));
		}
	}

	public static void applyIdDepartmentCriterion(Criteria rootEntityCriteria, Long id, Long departmentId) {
		if (rootEntityCriteria != null) {
			if (id != null && departmentId != null) {
				rootEntityCriteria.add(Restrictions.or(Restrictions.idEq(id.longValue()), Restrictions.eq("department.id", departmentId.longValue())));
			} else if (id != null) {
				rootEntityCriteria.add(Restrictions.idEq(id.longValue()));
			} else if (departmentId != null) {
				rootEntityCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
			}
		}
	}

	public static void applyLimit(Integer limit, Integer defaultLimit, Criteria criteria) {
		if (criteria != null) {
			criteria.setFirstResult(0);
			if (limit != null) {
				if (limit >= 0) {
					criteria.setMaxResults(limit);
				}
			} else if (defaultLimit != null) {
				if (defaultLimit >= 0) {
					criteria.setMaxResults(defaultLimit);
				}
			}
		}
	}

	public static org.hibernate.criterion.Criterion applyOr(org.hibernate.criterion.Criterion criterion, org.hibernate.criterion.Criterion or) {
		org.hibernate.criterion.Criterion result;
		if (criterion != null && or != null) {
			result = Restrictions.or(criterion, or);
		} else {
			result = criterion;
		}
		return result;
	}

	public static void applyPSFVO(SubCriteriaMap criteriaMap, PSFVO psf) throws Exception {
		Criteria criteria;
		if (criteriaMap != null && criteriaMap.getEntity() != null && (criteria = criteriaMap.getCriteria()) != null) {
			criteria.setProjection(null);
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			if (psf != null) {
				Map<String, String> filters = psf.getFilters();
				if (filters != null && filters.size() > 0) {
					Iterator<Map.Entry<String, String>> filterIt = filters.entrySet().iterator();
					while (filterIt.hasNext()) {
						Map.Entry<String, String> filter = filterIt.next();
						AssociationPath filterFieldAssociationPath = new AssociationPath(filter.getKey());
						Criteria subCriteria = criteriaMap.createCriteriaForAttribute(filterFieldAssociationPath);
						subCriteria.add(applyFilter(filterFieldAssociationPath.getPropertyName(),
								criteriaMap.getPropertyClassMap().get(filterFieldAssociationPath.getFullQualifiedPropertyName()), filter.getValue(),
								applyAlternativeFilter(criteriaMap, filterFieldAssociationPath, filter.getValue(), psf.getFilterTimeZone()), psf.getFilterTimeZone()));
					}
				}
				if (psf.getUpdateRowCount()) {
					psf.setRowCount((Long) criteria.setProjection(Projections.rowCount()).uniqueResult());
					criteria.setProjection(null);
					criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				}
				if (psf.getFirst() != null) {
					criteria.setFirstResult(psf.getFirst());
				}
				if (psf.getPageSize() != null) {
					criteria.setMaxResults(psf.getPageSize());
				}
				AssociationPath sortFieldAssociationPath = new AssociationPath(psf.getSortField());
				if (sortFieldAssociationPath.isValid()) {
					Criteria subCriteria = criteriaMap.createCriteriaForAttribute(sortFieldAssociationPath, CriteriaSpecification.LEFT_JOIN);
					String sortProperty = sortFieldAssociationPath.getPropertyName();
					subCriteria.addOrder(psf.getSortOrder() ? Order.asc(sortProperty) : Order.desc(sortProperty));
				}
			}
		}
	}

	public static <T> List<T> applyPVO(List<T> resultSet, PSFVO psf, boolean distinct) {
		if (resultSet != null && psf != null) {
			psf.setRowCount((long) resultSet.size());
			int fromIndex = 0;
			int toIndex = resultSet.size();
			if (psf.getFirst() != null) {
				fromIndex = psf.getFirst();
			}
			if (psf.getPageSize() != null) {
				toIndex = Math.min(fromIndex + psf.getPageSize(), resultSet.size());
			}
			if (psf.getFirst() == null && psf.getPageSize() == null) {
				if (distinct) {
					return new ArrayList<T>(new LinkedHashSet<T>(resultSet));
				} else {
					return resultSet;
				}
			} else if ((toIndex - fromIndex) <= 0) {
				return new ArrayList<T>();
			} else {
				int size = toIndex - fromIndex;
				ArrayList<T> resultSetPage = new ArrayList<T>(size);
				if (distinct) {
					Iterator<T> it = (new LinkedHashSet<T>(resultSet)).iterator();
					int i = 0;
					while (resultSetPage.size() < size && it.hasNext()) {
						T item = it.next();
						if (i >= fromIndex) {
							resultSetPage.add(item);
						}
						i++;
					}
				} else {
					for (int i = fromIndex; i < toIndex; i++) {
						resultSetPage.add(resultSet.get(i));
					}
				}
				return resultSetPage;
			}
		} else {
			return resultSet;
		}
	}

	public static void applyStartOpenIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		if (intervalCriteria != null) {
			org.hibernate.criterion.Criterion criterion = getStartOpenIntervalCriterion(from, to, or);
			if (criterion != null) {
				intervalCriteria.add(criterion);
			}
		}
	}

	public static org.hibernate.criterion.Criterion getStartOpenIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		return getStartOpenIntervalCriterion(from, to, or, "start", "stop");
	}

	public static org.hibernate.criterion.Criterion getStartOpenIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, String startPropertyName,
			String stopPropertyName) {
		if (from != null && to != null) {
			if (to.before(from)) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
			}
			return applyOr(
					Restrictions.or(
							Restrictions.or( // partial interval overlappings:
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to)),
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to))),
							Restrictions.or( // total inclusions:
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, to)),
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, startPropertyName),
											getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, to)))),
					or);
		} else if (from != null && to == null) {
			return applyOr(getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from), or);
		} else if (from == null && to != null) {
			return applyOr(
					Restrictions.or(
							Restrictions.or( // partial interval overlappings:
									getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to),
									getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to)),
							Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, startPropertyName),
									getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, to))),
					or);
		}
		return null;
	}

	public static void applyStartOptionalIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop) {
		if (intervalCriteria != null) {
			org.hibernate.criterion.Criterion criterion = getStartOptionalIntervalCriterion(from, to, or, includeStop);
			if (criterion != null) {
				intervalCriteria.add(criterion);
			}
		}
	}

	public static org.hibernate.criterion.Criterion getStartOptionalIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop) {
		return getStartOptionalIntervalCriterion(from, to, or, includeStop, "start", "stop");
	}

	public static org.hibernate.criterion.Criterion getStartOptionalIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop,
			String startPropertyName, String stopPropertyName) {
		if (from != null && to != null) {
			if (to.before(from)) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
			}
			return applyOr(Restrictions.or(
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, startPropertyName),
							Restrictions.and(
									includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, from)
											: getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from),
									getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to))),
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, startPropertyName),
							Restrictions.or(
									// partial interval overlappings:
									Restrictions.or(
											Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
													includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, to)
															: getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to)),
											Restrictions.and(
													includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, from)
															: getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from),
													getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to))),
									// total inclusions:
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, to))))),
					or);
		} else if (from != null && to == null) {
			return applyOr(Restrictions.or(
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, startPropertyName),
							includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, from)
									: getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from)),
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, startPropertyName),
							Restrictions.or(
									getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
									includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, from)
											: getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from)))),
					or);
		} else if (from == null && to != null) {
			return applyOr(Restrictions.or(
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, startPropertyName),
							getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to)),
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, startPropertyName),
							Restrictions.or(
									includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, to)
											: getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to),
									getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to)))),
					or);
		}
		return null;
	}

	public static void applyStopOpenIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		if (intervalCriteria != null) {
			org.hibernate.criterion.Criterion criterion = getStopOpenIntervalCriterion(from, to, or);
			if (criterion != null) {
				intervalCriteria.add(criterion);
			}
		}
	}

	public static org.hibernate.criterion.Criterion getStopOpenIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		return getStopOpenIntervalCriterion(from, to, or, "start", "stop");
	}

	public static org.hibernate.criterion.Criterion getStopOpenIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, String startPropertyName,
			String stopPropertyName) {
		if (from != null && to != null) {
			if (to.before(from)) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
			}
			return applyOr(
					Restrictions.or(
							Restrictions.or( // partial interval overlappings:
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to)),
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to))),
							Restrictions.or( // total inclusions:
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, to)),
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, stopPropertyName)))),
					or);
		} else if (from != null && to == null) {
			return applyOr(
					Restrictions.or(
							Restrictions.or( // partial interval overlappings:
									getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
									getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from)),
							Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, from),
									getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, stopPropertyName))),
					or);
		} else if (from == null && to != null) {
			return applyOr(getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to), or);
		}
		return null;
	}

	public static void applyStopOptionalIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop) {
		if (intervalCriteria != null) {
			org.hibernate.criterion.Criterion criterion = getStopOptionalIntervalCriterion(from, to, or, includeStop);
			if (criterion != null) {
				intervalCriteria.add(criterion);
			}
		}
	}

	public static org.hibernate.criterion.Criterion getStopOptionalIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop) {
		return getStopOptionalIntervalCriterion(from, to, or, includeStop, "start", "stop");
	}

	public static org.hibernate.criterion.Criterion getStopOptionalIntervalCriterion(Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop,
			String startPropertyName, String stopPropertyName) {
		if (from != null && to != null) {
			if (to.before(from)) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
			}
			return applyOr(Restrictions.or(
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, stopPropertyName),
							Restrictions.and(
									getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
									includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, to)
											: getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to))),
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, stopPropertyName),
							Restrictions.or(
									// partial interval overlappings:
									Restrictions.or(
											Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
													includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, to)
															: getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to)),
											Restrictions.and(
													includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, from)
															: getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from),
													getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to))),
									// total inclusions:
									Restrictions.and(getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, from),
											getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, to))))),
					or);
		} else if (from != null && to == null) {
			return applyOr(Restrictions.or(
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, stopPropertyName),
							getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from)),
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, stopPropertyName),
							Restrictions.or(
									getRestrictionCriterion(RestrictionCriterionTypes.GE, startPropertyName, from),
									includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.GE, stopPropertyName, from)
											: getRestrictionCriterion(RestrictionCriterionTypes.GT, stopPropertyName, from)))),
					or);
		} else if (from == null && to != null) {
			return applyOr(Restrictions.or(
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NULL, stopPropertyName),
							includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, to)
									: getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to)),
					Restrictions.and(
							getRestrictionCriterion(RestrictionCriterionTypes.IS_NOT_NULL, stopPropertyName),
							Restrictions.or(
									includeStop ? getRestrictionCriterion(RestrictionCriterionTypes.LE, startPropertyName, to)
											: getRestrictionCriterion(RestrictionCriterionTypes.LT, startPropertyName, to),
									getRestrictionCriterion(RestrictionCriterionTypes.LE, stopPropertyName, to)))),
					or);
		}
		return null;
	}

	public static void applyVisibleIdCriterion(String visibleProperty, Criteria criteria, Boolean visible, Long id) {
		if (criteria != null && visibleProperty != null && visibleProperty.length() > 0) {
			if (visible != null) {
				if (id != null) {
					criteria.add(Restrictions.or(Restrictions.eq(visibleProperty, visible.booleanValue()), Restrictions.idEq(id.longValue())));
				} else {
					criteria.add(Restrictions.eq(visibleProperty, visible.booleanValue()));
				}
			} else {
				if (id != null) {
					// if visible is nullable...
					criteria.add(Restrictions.or(Restrictions.or(Restrictions.eq(visibleProperty, true), Restrictions.eq("visible", false)), Restrictions.idEq(id.longValue())));
				} else {
					// no filter...
				}
			}
		}
	}

	public final static org.hibernate.loader.criteria.CriteriaQueryTranslator getCriteriaQueryTranslator(Criteria criteria) {
		try {
			org.hibernate.impl.CriteriaImpl criteriaImpl = (org.hibernate.impl.CriteriaImpl) criteria;
			org.hibernate.engine.SessionImplementor session = criteriaImpl.getSession();
			org.hibernate.engine.SessionFactoryImplementor factory = session.getFactory();
			String[] implementors = factory.getImplementors(criteriaImpl.getEntityOrClassName());
			return new org.hibernate.loader.criteria.CriteriaQueryTranslator(factory, criteriaImpl, implementors[0],
					org.hibernate.loader.criteria.CriteriaQueryTranslator.ROOT_SQL_ALIAS);
		} catch (Exception e) {
			return null;
		}
	}

	public final static String criteriaToSql(Criteria criteria) {
		try {
			org.hibernate.impl.CriteriaImpl criteriaImpl = (org.hibernate.impl.CriteriaImpl) criteria;
			org.hibernate.engine.SessionImplementor session = criteriaImpl.getSession();
			org.hibernate.engine.SessionFactoryImplementor factory = session.getFactory();
			String[] implementors = factory.getImplementors(criteriaImpl.getEntityOrClassName());
			org.hibernate.loader.criteria.CriteriaQueryTranslator translator = new org.hibernate.loader.criteria.CriteriaQueryTranslator(factory, criteriaImpl, implementors[0],
					org.hibernate.loader.criteria.CriteriaQueryTranslator.ROOT_SQL_ALIAS);
			org.hibernate.loader.criteria.CriteriaJoinWalker walker = new org.hibernate.loader.criteria.CriteriaJoinWalker(
					(org.hibernate.persister.entity.OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
					translator,
					factory,
					criteriaImpl,
					criteriaImpl.getEntityOrClassName(),
					session.getLoadQueryInfluencers());
			return walker.getSQLString();
		} catch (Exception e) {
			return null;
		}
	}

	public static List listDistinctRoot(Criteria criteria, Object dao, String... distinctProjections) throws Exception {
		if (dao != null && criteria != null) {
			criteria.setProjection(null);
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			Method loadMethod = CoreUtil.getDaoLoadMethod(dao);
			ProjectionList projectionList = Projections.projectionList().add(Projections.id());
			boolean cast = false;
			if (distinctProjections != null && distinctProjections.length > 0) {
				SubCriteriaMap criteriaMap = new SubCriteriaMap(loadMethod.getReturnType(), criteria);
				for (int i = 0; i < distinctProjections.length; i++) {
					AssociationPath distinctProjectionAssociationPath = new AssociationPath(distinctProjections[i]);
					Criteria projectioncriteria = getProjectionCriteria(criteriaMap, distinctProjectionAssociationPath);
					if (projectioncriteria != null) {
						projectionList.add(Projections.property(distinctProjectionAssociationPath.getFullQualifiedPropertyName()));
						cast = true;
					}
				}
			}
			List items = criteria.setProjection(Projections.distinct(projectionList)).list();
			Iterator it = items.iterator();
			ArrayList result = new ArrayList(items.size());
			while (it.hasNext()) {
				result.add(loadMethod.invoke(dao, cast ? ((Object[]) it.next())[0] : it.next()));
			}
			return result;
		}
		return null;
	}

	public static List listDistinctRootPSFVO(SubCriteriaMap criteriaMap, PSFVO psf, Object dao, String... distinctProjections) throws Exception {
		Criteria criteria;
		if (dao != null && criteriaMap != null && criteriaMap.getEntity() != null && (criteria = criteriaMap.getCriteria()) != null) {
			if (psf != null) {
				criteria.setProjection(null);
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				Method loadMethod = CoreUtil.getDaoLoadMethod(dao);
				AssociationPath sortFieldAssociationPath = new AssociationPath(psf.getSortField());
				Map<String, String> filters = psf.getFilters();
				if (filters != null && filters.size() > 0) {
					Iterator<Map.Entry<String, String>> filterIt = filters.entrySet().iterator();
					while (filterIt.hasNext()) {
						Map.Entry<String, String> filter = filterIt.next();
						AssociationPath filterFieldAssociationPath = new AssociationPath(filter.getKey());
						Criteria subCriteria;
						if (sortFieldAssociationPath.isValid()
								&& sortFieldAssociationPath.getPathDepth() >= 1
								&& sortFieldAssociationPath.getPath().equals(filterFieldAssociationPath.getPath())) {
							String alias = getProjectionAlias(sortFieldAssociationPath);
							subCriteria = criteriaMap.createCriteriaForAttribute(filterFieldAssociationPath, alias);
						} else {
							subCriteria = criteriaMap.createCriteriaForAttribute(filterFieldAssociationPath);
						}
						subCriteria.add(applyFilter(filterFieldAssociationPath.getPropertyName(),
								criteriaMap.getPropertyClassMap().get(filterFieldAssociationPath.getFullQualifiedPropertyName()), filter.getValue(),
								applyAlternativeFilter(criteriaMap, filterFieldAssociationPath, filter.getValue(), psf.getFilterTimeZone()), psf.getFilterTimeZone()));
					}
				}
				Long count = null;
				if (psf.getUpdateRowCount()) {
					Iterator it = criteria.setProjection(Projections.countDistinct("id")).list().iterator();
					if (it.hasNext()) {
						count = (Long) it.next();
					}
					psf.setRowCount(count);
					criteria.setProjection(null);
					criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				}
				if (psf.getFirst() != null) {
					criteria.setFirstResult(psf.getFirst());
				}
				if (psf.getPageSize() != null) {
					count = psf.getPageSize().longValue();
					criteria.setMaxResults(CommonUtil.safeLongToInt(count));
				}
				ProjectionList projectionList = Projections.projectionList().add(Projections.id());
				boolean cast = false;
				Criteria projectioncriteria = getProjectionCriteria(criteriaMap, sortFieldAssociationPath);
				if (projectioncriteria != null) {
					projectioncriteria
							.addOrder(psf.getSortOrder() ? Order.asc(sortFieldAssociationPath.getPropertyName()) : Order.desc(sortFieldAssociationPath.getPropertyName()));
					projectionList.add(Projections.property(sortFieldAssociationPath.getFullQualifiedPropertyName()));
					cast = true;
				}
				if (distinctProjections != null && distinctProjections.length > 0) {
					for (int i = 0; i < distinctProjections.length; i++) {
						AssociationPath distinctProjectionAssociationPath = new AssociationPath(distinctProjections[i]);
						projectioncriteria = getProjectionCriteria(criteriaMap, distinctProjectionAssociationPath);
						if (projectioncriteria != null) {
							projectionList.add(Projections.property(distinctProjectionAssociationPath.getFullQualifiedPropertyName()));
							cast = true;
						}
					}
				}
				Iterator it = criteria.setProjection(Projections.distinct(projectionList)).list().iterator();
				ArrayList result = (count == null ? new ArrayList() : new ArrayList(CommonUtil.safeLongToInt(count)));
				while (it.hasNext()) {
					result.add(loadMethod.invoke(dao, cast ? ((Object[]) it.next())[0] : it.next()));
				}
				return result;
			} else {
				return listDistinctRoot(criteria, dao);
			}
		}
		return null;
	}

	public static Criteria getProjectionCriteria(SubCriteriaMap criteriaMap, AssociationPath projectionAssociationPath) {
		if (criteriaMap != null && projectionAssociationPath != null && projectionAssociationPath.isValid()) {
			Criteria subCriteria;
			String projectionProperty = projectionAssociationPath.getPropertyName();
			if (projectionAssociationPath.getPathDepth() >= 1) {
				if (projectionAssociationPath.getPathDepth() == 1 && "id".equals(projectionAssociationPath.getPropertyName())) {
					subCriteria = criteriaMap.getCriteria();
				} else {
					String alias = getProjectionAlias(projectionAssociationPath);
					subCriteria = criteriaMap.createCriteriaForAttribute(projectionAssociationPath, alias, CriteriaSpecification.LEFT_JOIN);
					alias = subCriteria.getAlias();
					projectionAssociationPath.setFullQualifiedPropertyName(alias + AssociationPath.ASSOCIATION_PATH_SEPARATOR + projectionProperty);
				}
			} else {
				subCriteria = criteriaMap.createCriteriaForAttribute(projectionAssociationPath, CriteriaSpecification.LEFT_JOIN);
				projectionAssociationPath.setFullQualifiedPropertyName(projectionProperty);
			}
			return subCriteria;
		}
		return null;
	}

	private static final String getProjectionAlias(AssociationPath projectionAssociationPath) {
		return ALIAS_PREFIX + projectionAssociationPath.getPathElement(projectionAssociationPath.getPathDepth() - 1);
	}

	public static <T> ArrayList<T> listEvents(Criteria reminderItemCriteria, Date from, Date to, Boolean notify) {
		return listEvents(reminderItemCriteria != null ? reminderItemCriteria.list() : null, from, to, notify);
	}

	public static <T> ArrayList<T> listEvents(List reminderItems, Date from, Date to, Boolean notify) {
		ArrayList<T> resultSet = new ArrayList<T>();
		if (reminderItems != null) {
			Iterator<T> it = reminderItems.iterator();
			while (it.hasNext()) {
				ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(it.next());
				if (reminderItem.isActive()) {
					Date nextRecurrence = reminderItem.getNextRecurrence(from, true);
					if (nextRecurrence.compareTo(to) <= 0) {
						addReminderItem(resultSet, reminderItem, nextRecurrence, notify);
					}
				}
			}
		}
		return resultSet;
	}

	public static <T> ArrayList<T> listExpirations(Criteria expirationItemCriteria, Date currentDate, Boolean notify, boolean includeAlreadyPassed,
			VariablePeriod consistentValidityPeriod, Long consistentValidityPeriodDays,
			VariablePeriod consistentReminderPeriod, Long consistentReminderPeriodDays, Map... caches) {
		return listExpirations(expirationItemCriteria != null ? expirationItemCriteria.list() : null, currentDate, notify, includeAlreadyPassed, consistentValidityPeriod,
				consistentValidityPeriodDays, consistentReminderPeriod, consistentReminderPeriodDays, caches);
	}

	public static <T> ArrayList<T> listExpirations(List expirationItems, Date currentDate, Boolean notify, boolean includeAlreadyPassed,
			VariablePeriod consistentValidityPeriod, Long consistentValidityPeriodDays,
			VariablePeriod consistentReminderPeriod, Long consistentReminderPeriodDays, Map... caches) {
		ArrayList<T> resultSet = new ArrayList<T>();
		if (expirationItems != null) {
			Date today;
			if (currentDate == null) {
				today = new Date();
			} else {
				today = currentDate;
			}
			Iterator<T> it = expirationItems.iterator();
			while (it.hasNext()) {
				ExpirationEntityAdapter expirationItem = ExpirationEntityAdapter.getInstance(it.next(), today, caches);
				if (expirationItem.isActive()) {
					if (today.compareTo(expirationItem.getReminderStart(consistentValidityPeriod, consistentValidityPeriodDays, consistentReminderPeriod,
							consistentReminderPeriodDays)) >= 0
							&& (includeAlreadyPassed || today.compareTo(expirationItem.getExpiry(consistentValidityPeriod, consistentValidityPeriodDays)) <= 0)) {
						if (notify != null) {
							if (notify.booleanValue() == expirationItem.isNotify()) {
								resultSet.add((T) expirationItem.getItem());
							}
						} else {
							resultSet.add((T) expirationItem.getItem());
						}
					}
				}
			}
		}
		return resultSet;
	}

	public static <T> ArrayList<T> listReminders(Criteria reminderItemCriteria, Date currentDate, Boolean notify, boolean includeAlreadyPassed,
			VariablePeriod consistentReminderPeriod, Long consistentReminderPeriodDays) {
		return listReminders(reminderItemCriteria != null ? reminderItemCriteria.list() : null, currentDate, notify, includeAlreadyPassed,
				consistentReminderPeriod, consistentReminderPeriodDays);
	}

	public static <T> ArrayList<T> listReminders(List reminderItems, Date currentDate, Boolean notify, boolean includeAlreadyPassed,
			VariablePeriod consistentReminderPeriod, Long consistentReminderPeriodDays) {
		ArrayList<T> resultSet = new ArrayList<T>();
		if (reminderItems != null) {
			Date today;
			if (currentDate == null) {
				today = new Date();
			} else {
				today = currentDate;
			}
			Iterator<T> it = reminderItems.iterator();
			while (it.hasNext()) {
				ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(it.next());
				if (reminderItem.isActive()) {
					Date reminderStart = reminderItem.getReminderStart(today, false, consistentReminderPeriod, consistentReminderPeriodDays);
					if (today.compareTo(reminderStart) >= 0
							&& (includeAlreadyPassed || today.compareTo(reminderItem.getNextRecurrence(today, false)) <= 0)) {
						addReminderItem(resultSet, reminderItem, reminderStart, notify);
					}
				}
			}
		}
		return resultSet;
	}

	private CriteriaUtil() {
	}
}
