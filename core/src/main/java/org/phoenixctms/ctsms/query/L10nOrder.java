package org.phoenixctms.ctsms.query;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * Sorts a property that holds localization keys by their texts (e.g. eCRF sections, inquiry categories),
 * rendered as "case column when 'key' then 'text' ... else column end". Keys without a text sort by the key itself.
 */
public class L10nOrder extends Order {

	private static String quote(String value) {
		return "'" + value.replace("'", "''") + "'";
	}

	private final String propertyName;
	private final boolean ascending;
	private final Map<String, String> texts; // key -> text, only keys with a text that differs from the key

	public L10nOrder(String propertyName, boolean ascending, Map<String, String> texts) {
		super(propertyName, ascending);
		this.propertyName = propertyName;
		this.ascending = ascending;
		this.texts = texts;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		if (texts == null || texts.isEmpty()) {
			return super.toSqlString(criteria, criteriaQuery);
		}
		String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
		StringBuilder fragment = new StringBuilder();
		for (int i = 0; i < columns.length; i++) {
			fragment.append("case ").append(columns[i]);
			Iterator<Map.Entry<String, String>> it = texts.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> text = it.next();
				fragment.append(" when ").append(quote(text.getKey())).append(" then ").append(quote(text.getValue()));
			}
			fragment.append(" else ").append(columns[i]).append(" end").append(ascending ? " asc" : " desc");
			if (i < columns.length - 1) {
				fragment.append(", ");
			}
		}
		return fragment.toString();
	}

	@Override
	public String toString() {
		return propertyName + " (localized) " + (ascending ? "asc" : "desc");
	}
}
