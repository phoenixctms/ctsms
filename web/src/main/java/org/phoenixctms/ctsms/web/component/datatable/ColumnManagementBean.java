package org.phoenixctms.ctsms.web.component.datatable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.event.ActionEvent;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.DataTableColumnVO;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.component.column.Column;
import org.primefaces.context.RequestContext;

public class ColumnManagementBean {

	//keep filters when hiding columns:
	public final static boolean FILTER_INVISIBLE = false;
	private ConcurrentHashMap<String, Map<String, String>> cache;

	public ColumnManagementBean() {
		cache = new ConcurrentHashMap<String, Map<String, String>>();
	}

	public Map<String, String> getVisibleMap(String dataTableClientId) {
		return getVisibleMap(dataTableClientId, null);
	}

	private void clearVisibleMap(String dataTableClientId) {
		getVisibleMap(dataTableClientId, new ArrayList<DataTableColumnVO>());
	}

	private void reloadVisibleMap(String dataTableClientId) {
		cache.remove(dataTableClientId);
		getVisibleMap(dataTableClientId, null);
	}

	public static void resetVisibleMap(String dataTableId) {
		DataTable dataTable = (DataTable) WebUtil.findComponentById(dataTableId);
		if (dataTable != null) {
			WebUtil.getSessionScopeBean().getColumnManager().clearVisibleMap(":" + dataTable.getClientId());
		}
	}

	private void updateVisibleMap(String dataTableClientId, Collection<DataTableColumnVO> tableColumnVOs) {
		getVisibleMap(dataTableClientId, tableColumnVOs);
	}

	private Map<String, String> getVisibleMap(String dataTableClientId, Collection<DataTableColumnVO> tableColumnVOs) {
		if (tableColumnVOs == null && cache.containsKey(dataTableClientId)) {
			return cache.get(dataTableClientId);
		} else {
			if (tableColumnVOs == null) {
				try {
					tableColumnVOs = WebUtil.getServiceLocator().getUserService().getDataTableColumns(WebUtil.getAuthentication(), WebUtil.getUserId(), dataTableClientId,
							null).getColumns();
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			HashMap<String, Boolean> savedTableColumns;
			LinkedHashMap<String, String> tableColumns;
			if (tableColumnVOs != null) {
				savedTableColumns = new HashMap<String, Boolean>(tableColumnVOs.size());
				Iterator<DataTableColumnVO> it = tableColumnVOs.iterator();
				while (it.hasNext()) {
					DataTableColumnVO tableColumnVO = it.next();
					savedTableColumns.put(tableColumnVO.getColumnName(), tableColumnVO.getVisible());
				}
				tableColumns = new LinkedHashMap<String, String>(savedTableColumns.size());
			} else {
				savedTableColumns = new HashMap<String, Boolean>();
				tableColumns = new LinkedHashMap<String, String>();
			}
			DataTable dataTable = (DataTable) WebUtil.findComponentByClientId(dataTableClientId);
			if (dataTable != null) {
				Iterator<Column> it = dataTable.getAllColumns().iterator();
				while (it.hasNext()) {
					Column column = it.next();
					if (column.isRendered()) {
						String headerTextEl = null;
						try {
							headerTextEl = getColumnName(column);
						} catch (Exception e) {
						}
						if (headerTextEl != null) {
							if (savedTableColumns.containsKey(headerTextEl)) {
								tableColumns.put(headerTextEl, Boolean.toString(savedTableColumns.get(headerTextEl)));
							} else {
								tableColumns.put(headerTextEl, getColumnVisibleDefault(column));
							}
						}
					}
				}
			}
			cache.put(dataTableClientId, tableColumns); //thread-safe enough.
			return tableColumns;
		}
	}

	private static String getColumnName(Column column) {
		String headerTextEl;
		UIComponent headerFacet = column.getFacet("header");
		try {
			//in general, columns have a header facet with p:outputText
			headerTextEl = ((HtmlOutputText) headerFacet).getValueExpression("value").getExpressionString();
		} catch (ClassCastException e) {
			//and there are columns with a dropdown in the header
			headerTextEl = ((HtmlPanelGroup) headerFacet).getChildren().get(0).getValueExpression("label").getExpressionString();
		}
		return headerTextEl.substring(2, headerTextEl.length() - 1); // Remove #{}
	}

	private static String getColumnVisibleDefault(Column column) {
		Object visibleDefault = column.getAttributes().get("visibleDefault");
		if (visibleDefault != null) {
			if (visibleDefault instanceof Boolean) {
				return Boolean.toString((boolean) visibleDefault);
			} else if (visibleDefault instanceof String) {
				return (String) visibleDefault;
			}
		}
		return Boolean.toString(true);
	}

	public static boolean isVisible(Column column) {
		String dataTableClientId = null;
		String headerTextEl = null;
		try {
			dataTableClientId = column.getNamingContainer().getClientId();
			headerTextEl = getColumnName(column);
		} catch (Exception e) {
		}
		if (dataTableClientId != null && dataTableClientId.length() > 0 &&
				headerTextEl != null && headerTextEl.length() > 0) {
			Map visibleMap = WebUtil.getSessionScopeBean().getColumnManager().getVisibleMap(":" + dataTableClientId);
			if (visibleMap == null || visibleMap.size() == 0) {
				return true; //table not found/not supported (eg. nested)/no visibility saved in db
			} else if (!visibleMap.containsKey(headerTextEl)) {
				return true; //column not found
			} else {
				return Boolean.parseBoolean((String) visibleMap.get(headerTextEl));
			}
		}
		return true; //rowtoggler, buttoncolumn, dropdown column, ...
	}

	private void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
		}
	}

	public void update(ActionEvent ae) {
		String dataTableClientId = (String) ae.getComponent().getAttributes().get("datatable_id");
		Iterator<Entry<String, String>> it = getVisibleMap(dataTableClientId).entrySet().iterator();
		LinkedHashSet<DataTableColumnVO> in = new LinkedHashSet<DataTableColumnVO>();
		int visibleCount = 0;
		while (it.hasNext()) {
			Entry<String, String> value = it.next();
			DataTableColumnVO tableColumnVO = new DataTableColumnVO();
			tableColumnVO.setTableName(dataTableClientId);
			tableColumnVO.setColumnName(value.getKey());
			tableColumnVO.setVisible(Boolean.parseBoolean(value.getValue()));
			if (tableColumnVO.getVisible()) {
				visibleCount++;
			}
			in.add(tableColumnVO);
		}
		if (visibleCount > 0) {
			try {
				updateVisibleMap(dataTableClientId,
						WebUtil.getServiceLocator().getUserService().setDataTableColumns(WebUtil.getAuthentication(), WebUtil.getUserId(), in).getColumns());
				if (!FILTER_INVISIBLE) {
					((DataTable) WebUtil.findComponentByClientId(dataTableClientId)).clearFilters();
				}
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
				appendRequestContextCallbackArgs(true);
				return;
			} catch (ServiceException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
		} else {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.NO_VISIBLE_COLUMNS_SELECTED);
		}
		reloadVisibleMap(dataTableClientId);
		appendRequestContextCallbackArgs(false);
	}

	public void clear(ActionEvent ae) {
		String dataTableClientId = (String) ae.getComponent().getAttributes().get("datatable_id");
		try {
			WebUtil.getServiceLocator().getUserService().clearDataTableColumns(WebUtil.getAuthentication(), WebUtil.getUserId(), dataTableClientId, null);
			clearVisibleMap(dataTableClientId);
			if (!FILTER_INVISIBLE) {
				((DataTable) WebUtil.findComponentByClientId(dataTableClientId)).clearFilters();
			}
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			appendRequestContextCallbackArgs(true);
			return;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		reloadVisibleMap(dataTableClientId);
		appendRequestContextCallbackArgs(false);
	}
}
