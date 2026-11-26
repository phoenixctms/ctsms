package org.phoenixctms.ctsms.web.model.shared.search.querytext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

/**
 * Looks up the values of a criterion property with the complete method the query builder's autocomplete uses,
 * for the query text editor and the query agent.
 */
public final class QueryTextValues {

	public static final int MAX_VALUES = 50;
	private static final String DEFAULT_GET_NAME_METHOD_NAME = "getName";
	private static final String GET_ID_METHOD_NAME = "getId";

	/**
	 * A value found by a lookup: the name an expression uses, and the key JSON uses. The key is the id of an entity, otherwise the name.
	 */
	public static final class Value {

		private final String name;
		private final String key;

		private Value(String name, String key) {
			this.name = name;
			this.key = key;
		}

		public String getName() {
			return name;
		}

		public String getKey() {
			return key;
		}
	}

	private QueryTextValues() {
	}

	// the properties with fixed options, a picker or no value are left out, like the query builder does
	public static boolean isLookup(CriterionPropertyVO propertyVO) {
		return propertyVO != null
				&& WebUtil.testCompleteMethodName(propertyVO)
				&& !WebUtil.testSelectionSetServiceMethodName(propertyVO)
				&& !WebUtil.testFilterItemsMethodName(propertyVO)
				&& !WebUtil.testPicker(propertyVO)
				&& propertyVO.getValueType() != null
				&& !CriterionValueType.NONE.equals(propertyVO.getValueType());
	}

	// entity values are ids, which the expression format takes by name and JSON by id
	public static boolean isEntityLookup(CriterionPropertyVO propertyVO) {
		return isLookup(propertyVO) && !CommonUtil.isEmptyString(propertyVO.getEntityName()) && !CommonUtil.isEnumerationClass(propertyVO.getEntityName());
	}

	public static List<Value> lookup(AuthenticationVO auth, CriterionPropertyVO propertyVO, String query)
			throws AuthenticationException, AuthorisationException, ServiceException {
		ArrayList<Value> values = new ArrayList<Value>();
		if (!isLookup(propertyVO)) {
			return values;
		}
		Collection result;
		try {
			// the arguments after the text, such as a limit or a trial id, are left empty like the query builder does
			result = (Collection) AssociationPath.invoke(propertyVO.getCompleteMethodName(), WebUtil.getServiceLocator().getToolsService(), true, auth,
					query == null ? "" : query.trim());
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof AuthenticationException) {
				throw (AuthenticationException) cause;
			} else if (cause instanceof AuthorisationException) {
				throw (AuthorisationException) cause;
			} else if (cause instanceof ServiceException) {
				throw (ServiceException) cause;
			}
			return values;
		} catch (Exception e) {
			return values;
		}
		if (result == null) {
			return values;
		}
		String getNameMethodName = CommonUtil.isEmptyString(propertyVO.getGetNameMethodName()) ? DEFAULT_GET_NAME_METHOD_NAME : propertyVO.getGetNameMethodName();
		Iterator it = result.iterator();
		while (it.hasNext() && values.size() < MAX_VALUES) {
			Object item = it.next();
			if (item instanceof String) {
				if (!CommonUtil.isEmptyString((String) item)) {
					values.add(new Value((String) item, (String) item));
				}
			} else if (item != null) {
				// the name the parser compares a value with (see CriterionParser.applyNamedCriterionValue)
				Object name = invokeQuietly(getNameMethodName, item);
				Object id = invokeQuietly(GET_ID_METHOD_NAME, item);
				if (id != null) {
					values.add(new Value(name == null || CommonUtil.isEmptyString(name.toString()) ? id.toString() : name.toString(), id.toString()));
				}
			}
		}
		return values;
	}

	private static Object invokeQuietly(String methodName, Object vo) {
		try {
			return AssociationPath.invoke(methodName, vo, false);
		} catch (Exception e) {
			return null;
		}
	}
}
