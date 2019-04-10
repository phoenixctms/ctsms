package org.phoenixctms.ctsms.web.component.datatable;

import java.io.IOException;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;

/**
 * Extends the Primefaces DataTableRenderer to address issus with the
 * persistence of the filter values.
 */
public class DataTableRenderer extends org.primefaces.component.datatable.DataTableRenderer {

	// http://stickysession.com/2013/02/extending-primefaces-datatable-to-enable-persistent-filter-values/
	@Override
	protected void encodeFilter(FacesContext context, DataTable table, Column column) throws IOException {
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		ResponseWriter writer = context.getResponseWriter();
		ValueExpression filterBy = column.getValueExpression("filterBy");
		String filterField = null;
		if (filterBy != null) {
			filterField = resolveStaticField(filterBy);
		}
		String filterId = column.getClientId(context) + "_filter";
		String filterValue = params.containsKey(filterId) ? params.get(filterId) : table.getFilters().get(filterField);
		String filterStyleClass = column.getFilterStyleClass();
		if (column.getValueExpression("filterOptions") == null) {
			filterStyleClass = filterStyleClass == null ? DataTable.COLUMN_INPUT_FILTER_CLASS
					: DataTable.COLUMN_INPUT_FILTER_CLASS + " " + filterStyleClass;
			writer.startElement("input", null);
			writer.writeAttribute("id", filterId, null);
			writer.writeAttribute("name", filterId, null);
			writer.writeAttribute("class", filterStyleClass, null);
			writer.writeAttribute("value", filterValue, null);
			writer.writeAttribute("autocomplete", "off", null);
			if (column.getFilterStyle() != null) {
				writer.writeAttribute("style", column.getFilterStyle(), null);
			}
			if (column.getFilterMaxLength() != Integer.MAX_VALUE) {
				writer.writeAttribute("maxlength", column.getFilterMaxLength(), null);
			}
			writer.endElement("input");
		}
		else {
			filterStyleClass = filterStyleClass == null ? DataTable.COLUMN_FILTER_CLASS : DataTable.COLUMN_FILTER_CLASS
					+ " " + filterStyleClass;
			writer.startElement("select", null);
			writer.writeAttribute("id", filterId, null);
			writer.writeAttribute("name", filterId, null);
			writer.writeAttribute("class", filterStyleClass, null);
			SelectItem[] itemsArray = getFilterOptions(column);
			for (SelectItem item : itemsArray) {
				Object itemValue = item.getValue();
				writer.startElement("option", null);
				writer.writeAttribute("value", item.getValue(), null);
				if (itemValue != null && String.valueOf(itemValue).equals(filterValue)) {
					writer.writeAttribute("selected", "selected", null);
				}
				writer.writeText(item.getLabel(), null);
				writer.endElement("option");
			}
			writer.endElement("select");
		}
	}

	public String resolveStaticField(ValueExpression expression) {
		if (expression != null) {
			String expressionString = expression.getExpressionString();
			expressionString = expressionString.substring(2, expressionString.length() - 1); // Remove #{}
			return expressionString.substring(expressionString.indexOf(".") + 1); // Remove var
		}
		else {
			return null;
		}
	}
}