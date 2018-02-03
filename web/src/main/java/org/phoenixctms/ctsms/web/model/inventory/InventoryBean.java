package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.InventoryCategoryVO;
import org.phoenixctms.ctsms.vo.InventoryInVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.model.DefaultTreeNode;
import org.phoenixctms.ctsms.web.model.IDVOTreeNode;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class InventoryBean extends ManagedBeanBase {

	public static void copyInventoryOutToIn(InventoryInVO in, InventoryOutVO out) {
		if (in != null && out != null) {
			InventoryCategoryVO inventoryCategoryVO = out.getCategory();
			InventoryOutVO parentVO = out.getParent();
			DepartmentVO departmentVO = out.getDepartment();
			StaffOutVO ownerVO = out.getOwner();
			in.setBookable(out.getBookable());
			in.setCategoryId(inventoryCategoryVO == null ? null : inventoryCategoryVO.getId());
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setName(out.getName());
			in.setOwnerId(ownerVO == null ? null : ownerVO.getId());
			in.setParentId(parentVO == null ? null : parentVO.getId());
			in.setPieces(out.getPieces());
			in.setMaxOverlappingBookings(out.getMaxOverlappingBookings());
		}
	}

	private static InventoryOutVO createInventoryOutFromIn(InventoryInVO in) {
		InventoryOutVO result = new InventoryOutVO();
		if (in != null) {
			result.setBookable(in.getBookable());
			result.setName(in.getName());
			result.setName(CommonUtil.getInventoryName(result));
			result.setPieces(in.getPieces());
			result.setMaxOverlappingBookings(in.getMaxOverlappingBookings());
		}
		return result;
	}

	public static void initInventoryDefaultValues(InventoryInVO in, UserOutVO user) {
		if (in != null) {
			in.setBookable(Settings.getBoolean(SettingCodes.INVENTORY_BOOKABLE_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_BOOKABLE_PRESET));
			in.setCategoryId(null);
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setId(null);
			in.setVersion(null);
			in.setName(Messages.getString(MessageCodes.INVENTORY_NAME_PRESET));
			in.setOwnerId(null);
			in.setParentId(null);
			in.setPieces(Settings.getLongNullable(SettingCodes.INVENTORY_PIECES_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_PIECES_PRESET));
			in.setMaxOverlappingBookings(Settings.getLongNullable(SettingCodes.INVENTORY_MAX_OVERLAPPING_PRESET, Bundle.SETTINGS, DefaultSettings.INVENTORY_MAX_OVERLAPPING_PRESET));
		}
	}

	private InventoryInVO in;
	private InventoryOutVO out;
	private ArrayList<SelectItem> categories;
	private ArrayList<SelectItem> departments;
	private TreeNode inventoryRoot;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;

	public InventoryBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		DefaultTreeNode inventoryRoot = new DefaultTreeNode("inventory_root", null);
		inventoryRoot.setExpanded(true);
		inventoryRoot.setType(WebUtil.PARENT_NODE_TYPE);
		this.inventoryRoot = inventoryRoot;
	}

	@Override
	public String addAction()
	{
		InventoryInVO backup = new InventoryInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getInventoryService().addInventory(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_PARENT_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_TAG_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_TAG_VALUE_COUNT,
					MessageCodes.INVENTORY_TAGS_TAB_TITLE, MessageCodes.INVENTORY_TAGS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_INVENTORY_TAG_VALUE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_STATUS_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_STATUS_ENTRY_COUNT,
					MessageCodes.INVENTORY_STATUS_TAB_TITLE, MessageCodes.INVENTORY_STATUS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_INVENTORY_STATUS_ENTRY_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_MAINTENANCE_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT,
					MessageCodes.MAINTENANCE_ITEMS_TAB_TITLE, MessageCodes.MAINTENANCE_ITEMS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_BOOKING_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_BOOKING_COUNT,
					MessageCodes.INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_INVENTORY_BOOKING_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_HYPERLINK_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_HYPERLINK_COUNT,
					MessageCodes.INVENTORY_HYPERLINKS_TAB_TITLE, MessageCodes.INVENTORY_HYPERLINKS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_INVENTORY_HYPERLINK_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_FILE_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_FILE_COUNT,
					MessageCodes.INVENTORY_FILES_TAB_TITLE, MessageCodes.INVENTORY_FILES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_INVENTORY_FILE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT,
					MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT.toString()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getInventoryService().getInventory(WebUtil.getAuthentication(), id,
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_PARENT_DEPTH));
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
		}
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeByNode() {
		Long inventoryId = WebUtil.getLongParamValue(GetParamNames.INVENTORY_ID);
		if (inventoryId != null) {
			change(inventoryId.toString());
		} else {
			this.out = null;
			this.initIn();
			initSets();
			appendRequestContextCallbackArgs(true);
		}
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().deleteInventory(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.INVENTORY_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.INVENTORY_DEFERRED_DELETE),
					false,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_PARENT_DEPTH));
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
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
		return ERROR_OUTCOME;
	}

	private InventoryOutVO findInventoryRoot(InventoryOutVO inventory) {
		if (inventory.getParent() == null) {
			return inventory;
		} else {
			return findInventoryRoot(inventory.getParent());
		}
	}

	public ArrayList<SelectItem> getCategories() {
		return categories;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public InventoryInVO getIn() {
		return in;
	}

	public TreeNode getInventoryRoot() {
		return inventoryRoot;
	}

	public String getInventoryTreeLabel() {
		Integer graphMaxInventoryInstances = Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES);
		Integer graphMaxInventoryParentDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_PARENT_DEPTH, Bundle.SETTINGS,
				DefaultSettings.GRAPH_MAX_INVENTORY_PARENT_DEPTH);
		if (graphMaxInventoryInstances == null && graphMaxInventoryParentDepth == null) {
			return Messages.getString(MessageCodes.INVENTORY_TREE_LABEL);
		} else if (graphMaxInventoryInstances != null && graphMaxInventoryParentDepth == null) {
			return Messages.getMessage(MessageCodes.INVENTORY_TREE_MAX_LABEL, graphMaxInventoryInstances);
		} else if (graphMaxInventoryInstances == null && graphMaxInventoryParentDepth != null) {
			return Messages.getMessage(MessageCodes.INVENTORY_TREE_LEVELS_LABEL, graphMaxInventoryParentDepth);
		} else {
			return Messages.getMessage(MessageCodes.INVENTORY_TREE_MAX_LEVELS_LABEL, graphMaxInventoryInstances, graphMaxInventoryParentDepth);
		}
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public InventoryOutVO getOut() {
		return out;
	}

	public String getOwnerName() {
		return WebUtil.staffIdToName(in.getOwnerId());
	}

	public String getParentName() {
		return WebUtil.inventoryIdToName(in.getParentId());
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.INVENTORY_ID) == null);
	}

	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : MessageCodes.INVENTORY_TITLE, Long.toString(out.getId()),
					CommonUtil.inventoryOutVOToString(out));
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_INVENTORY : MessageCodes.ERROR_LOADING_INVENTORY);
		}
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.INVENTORY_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.INVENTORY_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.INVENTORY_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long inventoryId = WebUtil.getLongParamValue(GetParamNames.INVENTORY_ID);
				if (inventoryId != null) {
					return String.format(JSValues.INVENTORY_ENTITY_WINDOW_NAME.toString(), inventoryId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.INVENTORY_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleBookableChange() {
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INVENTORY_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new InventoryInVO();
		}
		if (out != null) {
			copyInventoryOutToIn(in, out);
		} else {
			initInventoryDefaultValues(in, WebUtil.getUser());
		}
	}

	private void initSets() {
		tabCountMap.clear();
		tabTitleMap.clear();
		// PSFVO psf = new PSFVO();
		// psf.setPageSize(0);
		Long count = (out == null ? null : WebUtil.getInventoryTagValueCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_INVENTORY_TAG_VALUE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INVENTORY_TAG_VALUE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INVENTORY_TAGS_TAB_TITLE, MessageCodes.INVENTORY_TAGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getInventoryStatusEntryCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_INVENTORY_STATUS_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INVENTORY_STATUS_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INVENTORY_STATUS_TAB_TITLE, MessageCodes.INVENTORY_STATUS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getMaintenanceScheduleItemCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INVENTORY_MAINTENANCE_ITEM_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.MAINTENANCE_ITEMS_TAB_TITLE, MessageCodes.MAINTENANCE_ITEMS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getInventoryBookingCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_INVENTORY_BOOKING_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INVENTORY_BOOKING_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getHyperlinkCount(HyperlinkModule.INVENTORY_HYPERLINK, in.getId()));
		tabCountMap.put(JSValues.AJAX_INVENTORY_HYPERLINK_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INVENTORY_HYPERLINK_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INVENTORY_HYPERLINKS_TAB_TITLE, MessageCodes.INVENTORY_HYPERLINKS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTotalFileCount(FileModule.INVENTORY_DOCUMENT, in.getId()));
		tabCountMap.put(JSValues.AJAX_INVENTORY_FILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INVENTORY_FILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INVENTORY_FILES_TAB_TITLE, MessageCodes.INVENTORY_FILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.INVENTORY_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		inventoryRoot.getChildren().clear();
		if (out != null) {
			inventoryOutVOtoTreeNode(findInventoryRoot(out), inventoryRoot, out, new ArrayList<IDVOTreeNode>(),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_PARENT_DEPTH),
					null, 0);
		} else {
			IDVOTreeNode loose = new IDVOTreeNode(createInventoryOutFromIn(in), inventoryRoot);
			loose.setType(WebUtil.LEAF_NODE_TYPE);
		}
		categories = WebUtil.getVisibleInventoryCategories(in.getCategoryId());
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		if (out != null && out.isDeferredDelete()) { // && Settings.getBoolean(SettingCodes.INVENTORY_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.INVENTORY_DEFERRED_DELETE))
			// {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION);
		}
	}

	private IDVOTreeNode inventoryOutVOtoTreeNode(InventoryOutVO inventory, TreeNode parent, InventoryOutVO selected, ArrayList<IDVOTreeNode> nodes, Integer limit,
			Integer maxDepth, ArrayList<Object[]> deferred, int depth) {
		if ((limit == null || nodes.size() < limit.intValue()) && (maxDepth == null || depth <= maxDepth.intValue())) {
			IDVOTreeNode node = new IDVOTreeNode(inventory, parent);
			nodes.add(node);
			if (selected != null && inventory.getId() == selected.getId()) {
				node.setSelected(true);
				parent.setExpanded(true);
			}
			if (inventory.getChildrenCount() > 0L) {
				node.setType(WebUtil.PARENT_NODE_TYPE);
			} else {
				node.setType(WebUtil.LEAF_NODE_TYPE);
			}
			node.setSelectable(true);
			Collection<InventoryOutVO> children = inventory.getChildren();
			Iterator<InventoryOutVO> it = children.iterator();
			if (Settings.getBoolean(SettingCodes.GRAPH_INVENTORY_BREADTH_FIRST, Bundle.SETTINGS, DefaultSettings.GRAPH_INVENTORY_BREADTH_FIRST)) {
				if (deferred == null) {
					deferred = new ArrayList<Object[]>(children.size());
					while (it.hasNext()) {
						inventoryOutVOtoTreeNode(it.next(), node, selected, nodes, limit, maxDepth, deferred, depth + 1);
					}
					Iterator<Object[]> deferredIt = deferred.iterator();
					while (deferredIt.hasNext()) {
						Object[] newNode = deferredIt.next();
						inventoryOutVOtoTreeNode((InventoryOutVO) newNode[0], (IDVOTreeNode) newNode[1], selected, nodes, limit, maxDepth, null, (Integer) newNode[2]);
					}
				} else {
					while (it.hasNext()) {
						Object[] newNode = new Object[3];
						newNode[0] = it.next();
						newNode[1] = node;
						newNode[2] = depth + 1;
						deferred.add(newNode);
					}
				}
			} else {
				while (it.hasNext()) {
					inventoryOutVOtoTreeNode(it.next(), node, selected, nodes, limit, maxDepth, null, depth + 1);
				}
			}
			return node;
		}
		return null;
	}

	@Override
	public boolean isCreateable() {
		return WebUtil.getModuleEnabled(DBModule.INVENTORY_DB);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isEditable() {
		return WebUtil.getModuleEnabled(DBModule.INVENTORY_DB) && super.isEditable();
	}


	public boolean isRemovable() {
		return WebUtil.getModuleEnabled(DBModule.INVENTORY_DB) && super.isRemovable();
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab));
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getInventoryService().getInventory(WebUtil.getAuthentication(), id,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_PARENT_DEPTH));
			return LOAD_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (!in.getBookable()) {
			in.setMaxOverlappingBookings(0l);
		}
	}

	@Override
	public String updateAction() {
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getInventoryService().updateInventory(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_INVENTORY_PARENT_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_INVENTORY_PARENT_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
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
		return ERROR_OUTCOME;
	}
}
