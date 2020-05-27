package org.phoenixctms.ctsms.query;

import org.hibernate.type.Type;

public class SQLProjection extends org.hibernate.criterion.SQLProjection {

	private String sql;

	public SQLProjection(String sqlWithAlias, String groupBy, String[] columnAliases, Type[] types, String sql) {
		super(sqlWithAlias, groupBy, columnAliases, types);
		this.sql = sql;
	}

	public SQLProjection(String sqlWithAlias, String[] columnAliases, Type[] types, String sql) {
		super(sqlWithAlias, columnAliases, types);
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}
}
