package org.phoenixctms.ctsms.query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

public class SqlOrder extends Order {

	private final String sql;

	public SqlOrder(String sql) {
		super(sql, true);
		this.sql = sql;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return sql;
	}
}
