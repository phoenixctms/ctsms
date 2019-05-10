package org.phoenixctms.ctsms.query;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.Query;
import org.phoenixctms.ctsms.UserContext;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;

public class QueryParameterValue {

	public enum NamedParameterValues {
		TIME, USER, USER_DEPARTMENT, IDENTITY, IDENTITY_DEPARTMENT
	}

	public enum QueryParameterValueType {
		FILTER_VALUE, CRITERION_VALUE, STATIC_TERM_VALUE, TODAY, NOW, TODAY_PLUS_VARIABLE_PERIOD, TODAY_MINUS_VARIABLE_PERIOD, NOW_PLUS_VARIABLE_PERIOD, NOW_MINUS_VARIABLE_PERIOD, CONTEXT_USER_ID, CONTEXT_USER_DEPARTMENT_ID, CONTEXT_IDENTITY_ID, CONTEXT_IDENTITY_DEPARTMENT_ID
	}

	private static Staff getCachedIdentity(HashMap<NamedParameterValues, Object> namedParameterValuesCache) throws Exception {
		Staff identity;
		if (namedParameterValuesCache.containsKey(NamedParameterValues.IDENTITY)) {
			identity = (Staff) namedParameterValuesCache.get(NamedParameterValues.IDENTITY);
		} else {
			User user = getCachedUser(namedParameterValuesCache);
			if (user != null) {
				identity = user.getIdentity();
			} else {
				identity = null;
			}
			if (identity != null) {
				namedParameterValuesCache.put(NamedParameterValues.IDENTITY, identity);
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_UNKNOWN_IDENTITY, DefaultMessages.NAMED_PARAMETER_UNKNOWN_IDENTITY));
			}
		}
		return identity;
	}

	private static Department getCachedIdentityDepartment(HashMap<NamedParameterValues, Object> namedParameterValuesCache) throws Exception {
		Department department;
		if (namedParameterValuesCache.containsKey(NamedParameterValues.IDENTITY_DEPARTMENT)) {
			department = (Department) namedParameterValuesCache.get(NamedParameterValues.IDENTITY_DEPARTMENT);
		} else {
			Staff identity = getCachedIdentity(namedParameterValuesCache);
			if (identity != null) {
				department = identity.getDepartment();
			} else {
				department = null;
			}
			if (department != null) {
				namedParameterValuesCache.put(NamedParameterValues.IDENTITY_DEPARTMENT, department);
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_UNKNOWN_IDENTITY_DEPARTMENT,
						DefaultMessages.NAMED_PARAMETER_UNKNOWN_IDENTITY_DEPARTMENT));
			}
		}
		return department;
	}

	private static Date getCachedTime(HashMap<NamedParameterValues, Object> namedParameterValuesCache) {
		Date now;
		if (namedParameterValuesCache.containsKey(NamedParameterValues.TIME)) {
			now = (Date) namedParameterValuesCache.get(NamedParameterValues.TIME);
		} else {
			now = new Date();
			namedParameterValuesCache.put(NamedParameterValues.TIME, now);
		}
		return now;
	}

	private static User getCachedUser(HashMap<NamedParameterValues, Object> namedParameterValuesCache) throws Exception {
		User user;
		if (namedParameterValuesCache.containsKey(NamedParameterValues.USER)) {
			user = (User) namedParameterValuesCache.get(NamedParameterValues.USER);
		} else {
			UserContext context = CoreUtil.getUserContext();
			if (context != null) {
				user = context.getUser();
			} else {
				user = null;
			}
			if (user != null) {
				namedParameterValuesCache.put(NamedParameterValues.USER, user);
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_UNKNOWN_USER, DefaultMessages.NAMED_PARAMETER_UNKNOWN_USER));
			}
		}
		return user;
	}

	private static Department getCachedUserDepartment(HashMap<NamedParameterValues, Object> namedParameterValuesCache) throws Exception {
		Department department;
		if (namedParameterValuesCache.containsKey(NamedParameterValues.USER_DEPARTMENT)) {
			department = (Department) namedParameterValuesCache.get(NamedParameterValues.USER_DEPARTMENT);
		} else {
			User user = getCachedUser(namedParameterValuesCache);
			if (user != null) {
				department = user.getDepartment();
			} else {
				department = null;
			}
			if (department != null) {
				namedParameterValuesCache.put(NamedParameterValues.USER_DEPARTMENT, department);
			} else {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_UNKNOWN_USER_DEPARTMENT,
						DefaultMessages.NAMED_PARAMETER_UNKNOWN_USER_DEPARTMENT));
			}
		}
		return department;
	}

	private CriterionInstantVO criterion;
	private Class propertyClass;
	private CriterionValueType valueType;
	private String value;
	private String propertyName;
	private VariablePeriod period;
	private QueryParameterValueType type;

	private QueryParameterValue() {
	}

	public QueryParameterValue(boolean value) {
		this.valueType = CriterionValueType.BOOLEAN;
		this.criterion = new CriterionInstantVO();
		this.criterion.setBooleanValue(value);
		this.type = QueryParameterValueType.STATIC_TERM_VALUE;
	}

	public QueryParameterValue(Class propertyClass, String value) {
		this.propertyClass = propertyClass;
		this.value = value;
		this.type = QueryParameterValueType.FILTER_VALUE;
	}

	public QueryParameterValue(QueryParameterValueType namedParameter) {
		this.type = namedParameter;
	}

	public QueryParameterValue(QueryParameterValueType namedParameter, VariablePeriod period) {
		this.period = period;
		this.type = namedParameter;
	}

	public QueryParameterValue(String value) {
		this.valueType = CriterionValueType.STRING;
		this.criterion = new CriterionInstantVO();
		this.criterion.setStringValue(value);
		this.type = QueryParameterValueType.STATIC_TERM_VALUE;
	}

	public QueryParameterValue(String propertyName, CriterionValueType valueType, CriterionInstantVO value) {
		this.propertyName = propertyName;
		this.valueType = valueType;
		this.criterion = value;
		this.type = QueryParameterValueType.CRITERION_VALUE;
	}

	public void set(Query query, int pos, HashMap<NamedParameterValues, Object> namedParameterValuesCache) throws Exception {
		switch (type) {
			case CRITERION_VALUE:
				QueryUtil.setQueryParameterFromCriterion(propertyName, query, valueType, pos, criterion);
				break;
			case FILTER_VALUE:
				QueryUtil.setQueryParameterFromString(query, propertyClass, pos, value);
				break;
			case STATIC_TERM_VALUE:
				QueryUtil.setQueryParameterFromCriterion(null, query, valueType, pos, criterion);
				break;
			case TODAY:
				query.setDate(pos, getCachedTime(namedParameterValuesCache));
				break;
			case NOW:
				query.setTimestamp(pos, CommonUtil.dateToTimestamp(getCachedTime(namedParameterValuesCache)));
				break;
			case TODAY_PLUS_VARIABLE_PERIOD:
				if (VariablePeriod.EXPLICIT.equals(period)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD,
							DefaultMessages.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD));
				}
				query.setDate(pos, DateCalc.addInterval(getCachedTime(namedParameterValuesCache), period, null));
				break;
			case TODAY_MINUS_VARIABLE_PERIOD:
				if (VariablePeriod.EXPLICIT.equals(period)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD,
							DefaultMessages.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD));
				}
				query.setDate(pos, DateCalc.subInterval(getCachedTime(namedParameterValuesCache), period, null));
				break;
			case NOW_PLUS_VARIABLE_PERIOD:
				if (VariablePeriod.EXPLICIT.equals(period)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD,
							DefaultMessages.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD));
				}
				query.setTimestamp(pos, CommonUtil.dateToTimestamp(DateCalc.addInterval(getCachedTime(namedParameterValuesCache), period, null)));
				break;
			case NOW_MINUS_VARIABLE_PERIOD:
				if (VariablePeriod.EXPLICIT.equals(period)) {
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD,
							DefaultMessages.NAMED_PARAMETER_EXPLICIT_VARIABLE_PERIOD));
				}
				query.setTimestamp(pos, CommonUtil.dateToTimestamp(DateCalc.subInterval(getCachedTime(namedParameterValuesCache), period, null)));
				break;
			case CONTEXT_USER_ID:
				query.setBigInteger(pos, new BigInteger(getCachedUser(namedParameterValuesCache).getId().toString()));
				break;
			case CONTEXT_USER_DEPARTMENT_ID:
				query.setBigInteger(pos, new BigInteger(getCachedUserDepartment(namedParameterValuesCache).getId().toString()));
				break;
			case CONTEXT_IDENTITY_ID:
				query.setBigInteger(pos, new BigInteger(getCachedIdentity(namedParameterValuesCache).getId().toString()));
				break;
			case CONTEXT_IDENTITY_DEPARTMENT_ID:
				query.setBigInteger(pos, new BigInteger(getCachedIdentityDepartment(namedParameterValuesCache).getId().toString()));
				break;
			default:
		}
	}
}
