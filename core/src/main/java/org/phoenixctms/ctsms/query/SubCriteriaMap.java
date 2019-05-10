package org.phoenixctms.ctsms.query;

import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;

public class SubCriteriaMap {

	private final static int DEFAULT_JOIN_TYPE = CriteriaSpecification.INNER_JOIN;

	private static int getJoinType(int[] joinTypes, int i) {
		if (joinTypes != null && joinTypes.length > 0) {
			if (i >= 0 && i < joinTypes.length) {
				return joinTypes[i];
			}
			return joinTypes[joinTypes.length - 1];
		} else {
			return DEFAULT_JOIN_TYPE;
		}
	}

	private Class entity;
	private Criteria criteria;
	private HashMap<String, Criteria> propertyPathMap;
	private HashMap<String, Class> propertyClassMap;

	private SubCriteriaMap() {
		propertyPathMap = new HashMap<String, Criteria>();
		propertyClassMap = new HashMap<String, Class>();
	}

	public SubCriteriaMap(Class entity, Criteria criteria) {
		propertyPathMap = new HashMap<String, Criteria>();
		propertyClassMap = new HashMap<String, Class>();
		this.entity = entity;
		this.criteria = criteria;
	}

	private Criteria createCriteria(AssociationPath fullyQualifiedPropertyName, boolean isPropertyAssociation, int... joinTypes) {
		return createCriteria(fullyQualifiedPropertyName, isPropertyAssociation, null, joinTypes);
	}

	private Criteria createCriteria(AssociationPath fullyQualifiedPropertyName, boolean isPropertyAssociation, String alias, int... joinTypes) {
		if (fullyQualifiedPropertyName.isValid()) {
			String path;
			org.hibernate.Criteria subCriteria = criteria;
			Class propertyClass = entity;
			for (int i = 0; i < fullyQualifiedPropertyName.getPathDepth(); i++) {
				path = fullyQualifiedPropertyName.getPathString(i + 1);
				if (propertyPathMap.containsKey(path)) {
					subCriteria = propertyPathMap.get(path);
					propertyClass = propertyClassMap.get(path);
				} else {
					if (!isPropertyAssociation && alias != null && alias.length() > 0 && i == fullyQualifiedPropertyName.getPathDepth() - 1) {
						subCriteria = subCriteria.createCriteria(fullyQualifiedPropertyName.getPathElement(i), alias, getJoinType(joinTypes, i));
					} else {
						subCriteria = subCriteria.createCriteria(fullyQualifiedPropertyName.getPathElement(i), getJoinType(joinTypes, i));
					}
					propertyPathMap.put(path, subCriteria);
					propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPathElement(i));
					propertyClassMap.put(path, propertyClass);
				}
			}
			path = fullyQualifiedPropertyName.getFullQualifiedPropertyName();
			if (isPropertyAssociation) {
				if (!propertyPathMap.containsKey(path)) {
					if (alias != null && alias.length() > 0) {
						subCriteria = subCriteria.createCriteria(fullyQualifiedPropertyName.getPropertyName(), alias, getJoinType(joinTypes, -1));
					} else {
						subCriteria = subCriteria.createCriteria(fullyQualifiedPropertyName.getPropertyName(), getJoinType(joinTypes, -1));
					}
					propertyPathMap.put(path, subCriteria);
					propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPropertyName());
					propertyClassMap.put(path, propertyClass);
				} else {
					subCriteria = propertyPathMap.get(path);
					propertyClass = propertyClassMap.get(path);
				}
			} else {
				if (!propertyClassMap.containsKey(path)) {
					propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPropertyName());
					propertyClassMap.put(path, propertyClass);
				}
			}
			return subCriteria;
		} else {
			// invalid path from db property entry:
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_PROPERTY_ASSOCIATION_PATH, DefaultMessages.INVALID_PROPERTY_ASSOCIATION_PATH,
					new Object[] { fullyQualifiedPropertyName.getPathString() }));
		}
	}

	public Criteria createCriteria(String fullyQualifiedPropertyName, int... joinTypes) {
		return createCriteria(new AssociationPath(fullyQualifiedPropertyName), true, joinTypes);
	}

	public Criteria createCriteriaForAttribute(AssociationPath fullyQualifiedPropertyName, int... joinTypes) {
		return createCriteria(fullyQualifiedPropertyName, false, joinTypes);
	}

	public Criteria createCriteria(String fullyQualifiedPropertyName, String alias, int... joinTypes) {
		return createCriteria(new AssociationPath(fullyQualifiedPropertyName), true, alias, joinTypes);
	}

	public Criteria createCriteriaForAttribute(AssociationPath fullyQualifiedPropertyName, String alias, int... joinTypes) {
		return createCriteria(fullyQualifiedPropertyName, false, alias, joinTypes);
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public Class getEntity() {
		return entity;
	}

	public HashMap<String, Class> getPropertyClassMap() {
		return propertyClassMap;
	}

	public HashMap<String, org.hibernate.Criteria> getPropertyPathMap() {
		return propertyPathMap;
	}

	public void registerCriteria(AssociationPath fullyQualifiedPropertyName, Criteria... criterias) {
		if (fullyQualifiedPropertyName.isValid()) {
			String path;
			org.hibernate.Criteria subCriteria = criteria;
			Class propertyClass = entity;
			for (int i = 0; i < fullyQualifiedPropertyName.getPathDepth(); i++) {
				path = fullyQualifiedPropertyName.getPathString(i + 1);
				if (propertyPathMap.containsKey(path)) {
					subCriteria = propertyPathMap.get(path);
					propertyClass = propertyClassMap.get(path);
				} else {
					try {
						subCriteria = criterias[i];
					} catch (Exception e) {
						subCriteria = null;
					}
					if (subCriteria == null) {
						throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NO_CRITERIA_FOR_ASSOCIATION_PATH, DefaultMessages.NO_CRITERIA_FOR_ASSOCIATION_PATH,
								new Object[] { path }));
					}
					propertyPathMap.put(path, subCriteria);
					propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPathElement(i));
					propertyClassMap.put(path, propertyClass);
				}
			}
			path = fullyQualifiedPropertyName.getFullQualifiedPropertyName();
			// if (isPropertyAssociation) {
			if (!propertyPathMap.containsKey(path)) {
				try {
					subCriteria = criterias[criterias.length - 1];
				} catch (Exception e) {
					subCriteria = null;
				}
				if (subCriteria == null) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NO_CRITERIA_FOR_ASSOCIATION_PATH, DefaultMessages.NO_CRITERIA_FOR_ASSOCIATION_PATH,
							new Object[] { path }));
				}
				propertyPathMap.put(path, subCriteria);
				propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPropertyName());
				propertyClassMap.put(path, propertyClass);
			} else {
				subCriteria = propertyPathMap.get(path);
				propertyClass = propertyClassMap.get(path);
			}
			// } else {
			// if (!propertyClassMap.containsKey(path)) {
			// propertyClass = CoreUtil.getPropertyClass(propertyClass, fullyQualifiedPropertyName.getPropertyName());
			// propertyClassMap.put(path, propertyClass);
			// }
			// }
			// return subCriteria;
		} else {
			// invalid path from db property entry:
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.INVALID_PROPERTY_ASSOCIATION_PATH, DefaultMessages.INVALID_PROPERTY_ASSOCIATION_PATH,
					new Object[] { fullyQualifiedPropertyName.getPathString() }));
		}
	}

	public void registerCriteria(String fullyQualifiedPropertyName, Criteria... criterias) {
		registerCriteria(new AssociationPath(fullyQualifiedPropertyName), criterias);
	}
}
