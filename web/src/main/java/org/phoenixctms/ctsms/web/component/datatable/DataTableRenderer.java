package org.phoenixctms.ctsms.web.component.datatable;

import java.io.IOException;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.row.Row;

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
		} else {
			filterStyleClass = filterStyleClass == null ? DataTable.COLUMN_FILTER_CLASS
					: DataTable.COLUMN_FILTER_CLASS
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
		} else {
			return null;
		}
	}

	protected void encodeThead(FacesContext context, DataTable table) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		ColumnGroup group = table.getColumnGroup("header");
		writer.startElement("thead", null);
		encodeFacet(context, table, table.getHeader(), DataTable.HEADER_CLASS, "th");
		if (table.isPaginator() && !table.getPaginatorPosition().equalsIgnoreCase("bottom")) {
			encodePaginatorMarkup(context, table, "top", "th", org.primefaces.component.api.UIData.PAGINATOR_TOP_CONTAINER_CLASS);
		}
		if (group != null && group.isRendered()) {
			for (UIComponent child : group.getChildren()) {
				if (child.isRendered() && child instanceof Row) {
					Row headerRow = (Row) child;
					writer.startElement("tr", null);
					for (UIComponent headerRowChild : headerRow.getChildren()) {
						if (headerRowChild.isRendered()
								&& headerRowChild instanceof Column
								&& ColumnManagementBean.isVisible((Column) headerRowChild)) {
							encodeColumnHeader(context, table, (Column) headerRowChild);
						}
					}
					writer.endElement("tr");
				}
			}
		} else {
			writer.startElement("tr", null);
			writer.writeAttribute("role", "row", null);
			for (Column column : table.getColumns()) {
				if (column.isRendered()
						&& ColumnManagementBean.isVisible(column)) {
					if (column instanceof Columns) {
						encodeColumnsHeader(context, table, (Columns) column);
					} else {
						encodeColumnHeader(context, table, column);
					}
				}
			}
			writer.endElement("tr");
		}
		writer.endElement("thead");
	}

	protected boolean encodeRow(FacesContext context, org.phoenixctms.ctsms.web.component.datatable.DataTable table, String clientId, int rowIndex, String rowIndexVar)
			throws IOException {
		//Row index var
		if (rowIndexVar != null) {
			context.getExternalContext().getRequestMap().put(rowIndexVar, rowIndex);
		}
		boolean selectionEnabled = table.isSelectionEnabled();
		Object rowKey = null;
		if (selectionEnabled) {
			//try rowKey attribute
			rowKey = table.getRowKey();
			//ask selectable datamodel
			if (rowKey == null)
				rowKey = table.getRowKeyFromModel(table.getRowData());
		}
		//Preselection
		boolean selected = table.getSelectedRowKeys().contains(rowKey);
		ResponseWriter writer = context.getResponseWriter();
		String userRowStyleClass = table.getRowStyleClass();
		String rowStyleClass = rowIndex % 2 == 0 ? DataTable.ROW_CLASS + " " + DataTable.EVEN_ROW_CLASS : DataTable.ROW_CLASS + " " + DataTable.ODD_ROW_CLASS;
		if (selected) {
			rowStyleClass = rowStyleClass + " ui-state-highlight";
		}
		if (userRowStyleClass != null) {
			rowStyleClass = rowStyleClass + " " + userRowStyleClass;
		}
		writer.startElement("tr", null);
		writer.writeAttribute("data-ri", rowIndex, null);
		if (rowKey != null) {
			writer.writeAttribute("data-rk", rowKey, null);
		}
		writer.writeAttribute("class", rowStyleClass, null);
		writer.writeAttribute("role", "row", null);
		if (selectionEnabled) {
			writer.writeAttribute("aria-selected", String.valueOf(selected), null);
		}
		for (Column column : table.getColumns()) {
			if (column.isRendered()
					&& ColumnManagementBean.isVisible(column)) {
				if (column instanceof Columns) {
					encodeDynamicCell(context, table, (Columns) column);
				} else {
					encodeRegularCell(context, table, column, clientId, selected);
				}
			}
		}
		writer.endElement("tr");
		return true;
	}

	protected void encodeTFoot(FacesContext context, DataTable table) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		ColumnGroup group = table.getColumnGroup("footer");
		writer.startElement("tfoot", null);
		if (group != null && group.isRendered()) {
			for (UIComponent child : group.getChildren()) {
				if (child.isRendered() && child instanceof Row) {
					Row footerRow = (Row) child;
					writer.startElement("tr", null);
					for (UIComponent footerRowChild : footerRow.getChildren()) {
						if (footerRowChild.isRendered()
								&& footerRowChild instanceof Column
								&& ColumnManagementBean.isVisible((Column) footerRowChild)) {
							encodeColumnFooter(context, table, (Column) footerRowChild);
						}
					}
					writer.endElement("tr");
				}
			}
		} else if (table.hasFooterColumn()) {
			writer.startElement("tr", null);
			for (Column column : table.getColumns()) {
				if (column.isRendered()
						&& ColumnManagementBean.isVisible(column)) {
					if (column instanceof Columns) {
						encodeColumnsFooter(context, table, (Columns) column);
					} else {
						encodeColumnFooter(context, table, column);
					}
				}
			}
			writer.endElement("tr");
		}
		if (table.isPaginator() && !table.getPaginatorPosition().equalsIgnoreCase("top")) {
			encodePaginatorMarkup(context, table, "bottom", "td", org.primefaces.component.api.UIData.PAGINATOR_BOTTOM_CONTAINER_CLASS);
		}
		encodeFacet(context, table, table.getFooter(), DataTable.FOOTER_CLASS, "td");
		writer.endElement("tfoot");
	}
}