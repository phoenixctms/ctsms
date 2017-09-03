package org.phoenixctms.ctsms;

import org.hibernate.Hibernate;
import org.hibernate.dialect.function.SQLFunctionTemplate;

//import org.hibernate.dialect.PostgreSQLDialect;
//https://forum.hibernate.org/viewtopic.php?f=1&t=1003143&p=2426758&hilit=UPGRADE_NOWAIT#p2426758
public class PostgreSQLDialect extends org.hibernate.dialect.PostgreSQLDialect {

	public PostgreSQLDialect() {
		super();
		registerFunction("minute", new SQLFunctionTemplate(Hibernate.INTEGER, "extract(minute from ?1)"));
		registerFunction("hour", new SQLFunctionTemplate(Hibernate.INTEGER, "extract(hour from ?1)"));
	}

	@Override
	public String getForUpdateNowaitString() {
		return " for update nowait";
	}

	@Override
	public String getForUpdateNowaitString(String aliases) {
		return " for update of " + aliases + " nowait";
	}

	@Override
	public String getForUpdateString(String aliases) {
		return " for update of " + aliases;
	}
}