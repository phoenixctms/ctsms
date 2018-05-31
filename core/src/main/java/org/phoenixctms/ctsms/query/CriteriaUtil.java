package org.phoenixctms.ctsms.query;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
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
import org.phoenixctms.ctsms.enumeration.ExportStatus;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
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

	private static HashSet<String> EXACT_STRING_FILTER_ENTITY_FIELDS = new HashSet<String>();
	static {
		EXACT_STRING_FILTER_ENTITY_FIELDS.add("logicalPath");
	}

	private final static HashMap<String, String> ALTERNATIVE_FILTER_MAP = new HashMap<String, String>();

	static {
		ALTERNATIVE_FILTER_MAP.put("ProbandContactParticulars.lastNameHash", "alias");
		ALTERNATIVE_FILTER_MAP.put("AnimalContactParticulars.animalName", "alias");
	}

	private static <T> void addReminderItem(ArrayList<T> resultSet, ReminderEntityAdapter reminderItem, Date reminderStart, Boolean notify) { // , boolean checkDismissed) {
		if (!reminderItem.isDismissable() || !reminderItem.isRecurrenceDismissed(reminderStart)) {// !(reminderItem.isDismissed() && reminderStart.compareTo(new
			// Date(reminderItem.getDismissedTimestamp().getTime())) <= 0)) {
			if (notify != null) {
				if (notify.booleanValue() == reminderItem.isNotify()) {
					resultSet.add((T) reminderItem.getItem());
				}
			} else {
				resultSet.add((T) reminderItem.getItem());
			}
		}
	}

	private static org.hibernate.criterion.Criterion applyAlternativeFilter(SubCriteriaMap criteriaMap, AssociationPath filterFieldAssociationPath, String value) throws Exception {

		Class pathClass = criteriaMap.getPropertyClassMap().get(filterFieldAssociationPath.getPathString());
		if (pathClass != null) {
			String altFilter = ALTERNATIVE_FILTER_MAP.get(pathClass.getSimpleName() + AssociationPath.ASSOCIATION_PATH_SEPARATOR + filterFieldAssociationPath.getPropertyName());
			if (!CommonUtil.isEmptyString(altFilter)) {
				AssociationPath altFilterFieldAssociationPath = new AssociationPath(filterFieldAssociationPath.getPathString() + AssociationPath.ASSOCIATION_PATH_SEPARATOR + altFilter);
				criteriaMap.createCriteriaForAttribute(altFilterFieldAssociationPath);
				return applyFilter(altFilterFieldAssociationPath.getPropertyName(),
						criteriaMap.getPropertyClassMap().get(altFilterFieldAssociationPath.getFullQualifiedPropertyName()), value, null);
			}
		}
		return null;
	}

	public static void applyClosedIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		if (intervalCriteria != null) {
			if (from != null && to != null) {
				if (to.before(from)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
				}
				intervalCriteria.add(applyOr(
						Restrictions.or(
								Restrictions.or( // partial interval overlappings:
										Restrictions.and(Restrictions.ge("start", from), Restrictions.lt("start", to)),
										Restrictions.and(Restrictions.gt("stop", from), Restrictions.le("stop", to))
										),
								Restrictions.and(Restrictions.le("start", from), Restrictions.ge("stop", to))
								)
						, or));
			} else if (from != null && to == null) {
				intervalCriteria.add(applyOr(
						Restrictions.or( // partial interval overlappings:
								Restrictions.ge("start", from),
								Restrictions.gt("stop", from)
								)
						, or));
			} else if (from == null && to != null) {
				intervalCriteria.add(applyOr(Restrictions.lt("start", to), or));
			}
		}
	}

	public static void applyCurrentStatusCriterion(Criteria statusEntryCriteria, Timestamp currentTimestamp, org.hibernate.criterion.Criterion or) {
		if (statusEntryCriteria != null) {
			Timestamp now;
			if (currentTimestamp == null) {
				now = new Timestamp(System.currentTimeMillis());
			} else {
				now = currentTimestamp;
			}
			statusEntryCriteria.add(applyOr(
					Restrictions.or(
							Restrictions.and(Restrictions.isNotNull("start"),
									Restrictions.and(
											Restrictions.le("start", now),
											Restrictions.or(Restrictions.gt("stop", now), Restrictions.isNull("stop"))
											))
							,
							Restrictions.and(Restrictions.isNotNull("stop"),
									Restrictions.and(
											Restrictions.or(Restrictions.le("start", now), Restrictions.isNull("start")),
											Restrictions.gt("stop", now)
											))
							)
					, or));
		}
	}

	private static org.hibernate.criterion.Criterion applyFilter(String propertyName, Class propertyClass, String value, org.hibernate.criterion.Criterion or) throws Exception {
		if (propertyClass.equals(String.class)) {
			if (EXACT_STRING_FILTER_ENTITY_FIELDS.contains(propertyName)) {
				return applyOr(Restrictions.eq(propertyName, new String(value)),or);
			} else {
				return applyOr(Restrictions.ilike(propertyName, new String(value), MatchMode.ANYWHERE),or);
			}
		} else if (propertyClass.equals(Long.class)) {
			return applyOr(Restrictions.eq(propertyName, new Long(value)),or);
		} else if (propertyClass.equals(java.lang.Long.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Long.parseLong(value)),or);
		} else if (propertyClass.equals(Integer.class)) {
			return applyOr(Restrictions.eq(propertyName, new Integer(value)),or);
		} else if (propertyClass.equals(java.lang.Integer.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Integer.parseInt(value)),or);
		} else if (propertyClass.equals(Boolean.class)) {
			return applyOr(Restrictions.eq(propertyName, new Boolean(value)),or);
		} else if (propertyClass.equals(java.lang.Boolean.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Boolean.parseBoolean(value)),or);
		} else if (propertyClass.equals(Float.class)) {
			return applyOr(Restrictions.eq(propertyName, new Float(value)),or);
		} else if (propertyClass.equals(java.lang.Float.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Float.parseFloat(value)),or);
		} else if (propertyClass.equals(Double.class)) {
			return applyOr(Restrictions.eq(propertyName, new Double(value)),or);
		} else if (propertyClass.equals(java.lang.Double.TYPE)) {
			return applyOr(Restrictions.eq(propertyName, Double.parseDouble(value)),or);
		} else if (propertyClass.equals(Date.class)) {
			return applyOr(Restrictions.eq(propertyName, CommonUtil.parseDate(value, CommonUtil.INPUT_DATE_PATTERN)),or);
		} else if (propertyClass.equals(Timestamp.class)) {
			Date date = CommonUtil.parseDate(value, CommonUtil.INPUT_DATE_PATTERN);
			return applyOr(Restrictions.between(propertyName, CommonUtil.dateToTimestamp(DateCalc.getStartOfDay(date)), CommonUtil.dateToTimestamp(DateCalc.getEndOfDay(date))),or);
		} else if (propertyClass.equals(VariablePeriod.class)) {
			return applyOr(Restrictions.eq(propertyName, VariablePeriod.fromString(value)),or);
		} else if (propertyClass.equals(AuthenticationType.class)) {
			return applyOr(Restrictions.eq(propertyName, AuthenticationType.fromString(value)),or);
		} else if (propertyClass.equals(Sex.class)) {
			return applyOr(Restrictions.eq(propertyName, Sex.fromString(value)),or);
		} else if (propertyClass.equals(DBModule.class)) {
			return applyOr(Restrictions.eq(propertyName, DBModule.fromString(value)),or);
		} else if (propertyClass.equals(HyperlinkModule.class)) {
			return applyOr(Restrictions.eq(propertyName, HyperlinkModule.fromString(value)),or);
		} else if (propertyClass.equals(JournalModule.class)) {
			return applyOr(Restrictions.eq(propertyName, JournalModule.fromString(value)),or);
		} else if (propertyClass.equals(FileModule.class)) {
			return applyOr(Restrictions.eq(propertyName, FileModule.fromString(value)),or);
		} else if (propertyClass.equals(Color.class)) {
			return applyOr(Restrictions.eq(propertyName, Color.fromString(value)),or);
		} else if (propertyClass.equals(InputFieldType.class)) {
			return applyOr(Restrictions.eq(propertyName, InputFieldType.fromString(value)),or);
		} else if (propertyClass.equals(EventImportance.class)) {
			return applyOr(Restrictions.eq(propertyName, EventImportance.fromString(value)), or);
		} else if (propertyClass.equals(ExportStatus.class)) {
			return applyOr(Restrictions.eq(propertyName, ExportStatus.fromString(value)),or);
		} else if (propertyClass.isArray() && propertyClass.getComponentType().equals(java.lang.Byte.TYPE)) { // only string hashes supported, no boolean, float, etc...
			return applyOr(Restrictions.eq(propertyName, CryptoUtil.hashForSearch(value)),or);
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
			criteria.setResultTransformer(Criteria.ROOT_ENTITY);
			if (psf != null) {
				Map<String, String> filters = psf.getFilters();
				if (filters != null && filters.size() > 0) {
					Iterator<Map.Entry<String, String>> filterIt = filters.entrySet().iterator();
					while (filterIt.hasNext()) {
						Map.Entry<String, String> filter = (Map.Entry<String, String>) filterIt.next();
						AssociationPath filterFieldAssociationPath = new AssociationPath(filter.getKey());
						Criteria subCriteria = criteriaMap.createCriteriaForAttribute(filterFieldAssociationPath);
						subCriteria.add(applyFilter(filterFieldAssociationPath.getPropertyName(),
								criteriaMap.getPropertyClassMap().get(filterFieldAssociationPath.getFullQualifiedPropertyName()), filter.getValue(),
								applyAlternativeFilter(criteriaMap, filterFieldAssociationPath, filter.getValue())
								));
					}
				}
				if (psf.getUpdateRowCount()) {
					psf.setRowCount((Long) criteria.setProjection(Projections.rowCount()).uniqueResult());
					criteria.setProjection(null);
					criteria.setResultTransformer(Criteria.ROOT_ENTITY);
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
			if (from != null && to != null) {
				if (to.before(from)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
				}
				intervalCriteria.add(applyOr(
						Restrictions.or(
								Restrictions.or( // partial interval overlappings:
										Restrictions.and(Restrictions.ge("start", from), Restrictions.lt("start", to)),
										Restrictions.and(Restrictions.gt("stop", from), Restrictions.le("stop", to))
										),
								Restrictions.or( // total inclusions:
										Restrictions.and(Restrictions.le("start", from), Restrictions.ge("stop", to)),
										Restrictions.and(Restrictions.isNull("start"), Restrictions.ge("stop", to))
										)
								)
						, or));
			} else if (from != null && to == null) {
				intervalCriteria.add(applyOr(Restrictions.gt("stop", from), or));
			} else if (from == null && to != null) {
				intervalCriteria.add(applyOr(
						Restrictions.or(
								Restrictions.or( // partial interval overlappings:
										Restrictions.lt("start", to),
										Restrictions.le("stop", to)
										),
								Restrictions.and(Restrictions.isNull("start"), Restrictions.ge("stop", to))
								)
						, or));
			}
		}
	}

	public static void applyStartOptionalIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop) {
		if (intervalCriteria != null) {
			if (from != null && to != null) {
				if (to.before(from)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
				}
				intervalCriteria.add(applyOr(Restrictions.or(
						Restrictions.and(
								Restrictions.isNull("start"),
								Restrictions.and(
										includeStop ? Restrictions.ge("stop", from) : Restrictions.gt("stop", from),
												Restrictions.le("stop", to)
										)
								),
						Restrictions.and(
								Restrictions.isNotNull("start"),
								Restrictions.or(
										// partial interval overlappings:
										Restrictions.or(
												Restrictions.and(Restrictions.ge("start", from), includeStop ? Restrictions.le("start", to) : Restrictions.lt("start", to)),
												Restrictions.and(includeStop ? Restrictions.ge("stop", from) : Restrictions.gt("stop", from), Restrictions.le("stop", to))
												),
										// total inclusions:
										Restrictions.and(Restrictions.le("start", from), Restrictions.ge("stop", to))
										)
								)
						), or));
			} else if (from != null && to == null) {
				intervalCriteria.add(applyOr(Restrictions.or(
						Restrictions.and(
								Restrictions.isNull("start"),
								includeStop ? Restrictions.ge("stop", from) : Restrictions.gt("stop", from)
								),
						Restrictions.and(
								Restrictions.isNotNull("start"),
								Restrictions.or(
										Restrictions.ge("start", from),
										includeStop ? Restrictions.ge("stop", from) : Restrictions.gt("stop", from)
										)
								)
						), or));
			} else if (from == null && to != null) {
				intervalCriteria.add(applyOr(Restrictions.or(
						Restrictions.and(
								Restrictions.isNull("start"),
								Restrictions.le("stop", to)
								),
						Restrictions.and(
								Restrictions.isNotNull("start"),
								Restrictions.or(
										includeStop ? Restrictions.le("start", to) : Restrictions.lt("start", to),
												Restrictions.le("stop", to)
										)
								)
						), or));
			}
		}
	}

	public static void applyStopOpenIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or) {
		if (intervalCriteria != null) {
			if (from != null && to != null) {
				if (to.before(from)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
				}
				intervalCriteria.add(applyOr(
						Restrictions.or(
								Restrictions.or( // partial interval overlappings:
										Restrictions.and(Restrictions.ge("start", from), Restrictions.lt("start", to)),
										Restrictions.and(Restrictions.gt("stop", from), Restrictions.le("stop", to))
										),
								Restrictions.or( // total inclusions:
										Restrictions.and(Restrictions.le("start", from), Restrictions.ge("stop", to)),
										Restrictions.and(Restrictions.le("start", from), Restrictions.isNull("stop"))
										)
								)
						, or));
			} else if (from != null && to == null) {
				intervalCriteria.add(applyOr(
						Restrictions.or(
								Restrictions.or( // partial interval overlappings:
										Restrictions.ge("start", from),
										Restrictions.gt("stop", from)
										),
								Restrictions.and(Restrictions.le("start", from), Restrictions.isNull("stop"))
								)
						, or));
			} else if (from == null && to != null) {
				intervalCriteria.add(applyOr(Restrictions.lt("start", to), or));
			}
		}
	}

	public static void applyStopOptionalIntervalCriterion(Criteria intervalCriteria, Timestamp from, Timestamp to, org.hibernate.criterion.Criterion or, boolean includeStop) {
		if (intervalCriteria != null) {
			if (from != null && to != null) {
				if (to.before(from)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INTERVAL_STOP_BEFORE_START, DefaultMessages.INTERVAL_STOP_BEFORE_START));
				}
				intervalCriteria.add(applyOr(Restrictions.or(
						Restrictions.and(
								Restrictions.isNull("stop"),
								Restrictions.and(
										Restrictions.ge("start", from),
										includeStop ? Restrictions.le("start", to) : Restrictions.lt("start", to)
										)
								),
						Restrictions.and(
								Restrictions.isNotNull("stop"),
								Restrictions.or(
										// partial interval overlappings:
										Restrictions.or(
												Restrictions.and(Restrictions.ge("start", from), includeStop ? Restrictions.le("start", to) : Restrictions.lt("start", to)),
												Restrictions.and(includeStop ? Restrictions.ge("stop", from) : Restrictions.gt("stop", from), Restrictions.le("stop", to))
												),
										// total inclusions:
										Restrictions.and(Restrictions.le("start", from), Restrictions.ge("stop", to))
										)
								)
						), or));
			} else if (from != null && to == null) {
				intervalCriteria.add(applyOr(Restrictions.or(
						Restrictions.and(
								Restrictions.isNull("stop"),
								Restrictions.ge("start", from)
								),
						Restrictions.and(
								Restrictions.isNotNull("stop"),
								Restrictions.or(
										Restrictions.ge("start", from),
										includeStop ? Restrictions.ge("stop", from) : Restrictions.gt("stop", from)
										)
								)
						), or));
			} else if (from == null && to != null) {
				intervalCriteria.add(applyOr(Restrictions.or(
						Restrictions.and(
								Restrictions.isNull("stop"),
								includeStop ? Restrictions.le("start", to) : Restrictions.lt("start", to)
								),
						Restrictions.and(
								Restrictions.isNotNull("stop"),
								Restrictions.or(
										includeStop ? Restrictions.le("start", to) : Restrictions.lt("start", to),
												Restrictions.le("stop", to)
										)
								)
						), or));
			}
		}
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

	public static List listDistinctRoot(Criteria criteria,Object dao,String... fields) throws Exception {
		if (dao != null && criteria != null) {
			criteria.setProjection(null);
			criteria.setResultTransformer(Criteria.ROOT_ENTITY);
			Method loadMethod = CoreUtil.getDaoLoadMethod(dao);
			ProjectionList projectionList = Projections.projectionList().add(Projections.id());
			boolean cast = false;
			if (fields != null && fields.length > 0) {
				for (int i = 0; i< fields.length; i++) {
					projectionList.add(Projections.property(fields[i]));
				}
				cast = true;
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

	public static List listDistinctRootPSFVO(SubCriteriaMap criteriaMap, PSFVO psf, Object dao) throws Exception {
		Criteria criteria;
		if (dao != null && criteriaMap != null && criteriaMap.getEntity() != null && (criteria = criteriaMap.getCriteria()) != null) {
			if (psf != null) {
				criteria.setProjection(null);
				criteria.setResultTransformer(Criteria.ROOT_ENTITY);
				Method loadMethod = CoreUtil.getDaoLoadMethod(dao);
				Map<String, String> filters = psf.getFilters();
				if (filters != null && filters.size() > 0) {
					Iterator<Map.Entry<String, String>> filterIt = filters.entrySet().iterator();
					while (filterIt.hasNext()) {
						Map.Entry<String, String> filter = (Map.Entry<String, String>) filterIt.next();
						AssociationPath filterFieldAssociationPath = new AssociationPath(filter.getKey());
						Criteria subCriteria = criteriaMap.createCriteriaForAttribute(filterFieldAssociationPath);
						subCriteria.add(applyFilter(filterFieldAssociationPath.getPropertyName(),
								criteriaMap.getPropertyClassMap().get(filterFieldAssociationPath.getFullQualifiedPropertyName()), filter.getValue(),
								applyAlternativeFilter(criteriaMap, filterFieldAssociationPath, filter.getValue())
								));
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
					criteria.setResultTransformer(Criteria.ROOT_ENTITY);
				}
				if (psf.getFirst() != null) {
					criteria.setFirstResult(psf.getFirst());
				}
				if (psf.getPageSize() != null) {
					count = psf.getPageSize().longValue();
					criteria.setMaxResults(CommonUtil.safeLongToInt(count));
				}
				ProjectionList projectionList = Projections.projectionList().add(Projections.id());
				AssociationPath sortFieldAssociationPath = new AssociationPath(psf.getSortField());
				boolean cast = false;
				if (sortFieldAssociationPath.isValid()) {
					Criteria subCriteria = criteriaMap.createCriteriaForAttribute(sortFieldAssociationPath, CriteriaSpecification.LEFT_JOIN);
					String sortProperty = sortFieldAssociationPath.getPropertyName();
					subCriteria.addOrder(psf.getSortOrder() ? Order.asc(sortProperty) : Order.desc(sortProperty));
					projectionList.add(Projections.property(sortProperty));
					cast = true;
				}
				Iterator it = criteria.setProjection(Projections.distinct(projectionList)).list().iterator();
				ArrayList result = (count == null ? new ArrayList() : new ArrayList(CommonUtil.safeLongToInt(count)));
				while (it.hasNext()) {
					result.add(loadMethod.invoke(dao, cast ? ((Object[]) it.next())[0] : it.next()));
				}
				return result;
			} else {
				// criteria.setProjection(null);
				// return new ArrayList(new LinkedHashSet(criteria.list()));
				// sorting via psf mandatory...
				// criteria.setProjection(null);
				// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				// return criteria.list();
				return listDistinctRoot(criteria, dao);
			}
		}
		return null;
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
						addReminderItem(resultSet, reminderItem, nextRecurrence, notify); // , true);
					}
				}
			}
		}
		return resultSet;
	}

	public static <T> ArrayList<T> listExpirations(Criteria expirationItemCriteria, Date currentDate, Boolean notify, boolean includeAlreadyPassed,
			VariablePeriod consistentValidityPeriod, Long consistentValidityPeriodDays,
			VariablePeriod consistentReminderPeriod, Long consistentReminderPeriodDays, Map... caches) {
		ArrayList<T> resultSet = new ArrayList<T>();
		if (expirationItemCriteria != null) {
			Date today;
			if (currentDate == null) {
				today = new Date();
			} else {
				today = currentDate;
			}
			Iterator<T> it = expirationItemCriteria.list().iterator();
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
		ArrayList<T> resultSet = new ArrayList<T>();
		if (reminderItemCriteria != null) {
			Date today;
			if (currentDate == null) {
				today = new Date();
			} else {
				today = currentDate;
			}
			Iterator<T> it = reminderItemCriteria.list().iterator();
			while (it.hasNext()) {
				ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(it.next());
				if (reminderItem.isActive()) {
					Date reminderStart = reminderItem.getReminderStart(today, false, consistentReminderPeriod, consistentReminderPeriodDays);
					if (today.compareTo(reminderStart) >= 0
							&& (includeAlreadyPassed || today.compareTo(reminderItem.getNextRecurrence(today, false)) <= 0)) {
						addReminderItem(resultSet, reminderItem, reminderStart, notify); // , true);
					}
				}
			}
		}
		return resultSet;
	}

	private CriteriaUtil() {
	}
}
