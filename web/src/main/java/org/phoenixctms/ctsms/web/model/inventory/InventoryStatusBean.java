package org.phoenixctms.ctsms.web.model.inventory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryInVO;
import org.phoenixctms.ctsms.vo.InventoryStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.InventoryStatusTypeVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.CollidingInventoryBookingEagerModel;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class InventoryStatusBean extends ManagedBeanBase {

	public static void copyStatusEntryOutToIn(InventoryStatusEntryInVO in, InventoryStatusEntryOutVO out) {
		if (in != null && out != null) {
			InventoryStatusTypeVO inventoryStatusTypeVO = out.getType();
			InventoryOutVO inventoryVO = out.getInventory();
			StaffOutVO originatorVO = out.getOriginator();
			StaffOutVO addresseeVO = out.getAddressee();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInventoryId(inventoryVO == null ? null : inventoryVO.getId());
			in.setComment(out.getComment());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setTypeId(inventoryStatusTypeVO == null ? null : inventoryStatusTypeVO.getId());
			in.setOriginatorId(originatorVO == null ? null : originatorVO.getId());
			in.setAddresseeId(addresseeVO == null ? null : addresseeVO.getId());
		}
	}

	public static void initStatusEntryDefaultValues(InventoryStatusEntryInVO in, Long inventoryId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setInventoryId(inventoryId);
			in.setComment(Messages.getString(MessageCodes.INVENTORY_STATUS_ENTRY_COMMENT_PRESET));
			in.setStart(new Timestamp(System.currentTimeMillis()));
			in.setStop(null);
			in.setTypeId(null);
			in.setOriginatorId(null);
			in.setAddresseeId(null);
		}
	}

	private InventoryStatusEntryInVO in;
	private InventoryStatusEntryOutVO out;
	private Long inventoryId;
	private ArrayList<SelectItem> statusTypes;
	private InventoryStatusEntryLazyModel statusEntryModel;
	private InventoryStatusTypeVO statusType;
	private HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache;

	public InventoryStatusBean() {
		super();
		collidingInventoryBookingModelCache = new HashMap<Long, CollidingInventoryBookingEagerModel>();
		statusEntryModel = new InventoryStatusEntryLazyModel();
	}

	@Override
	public String addAction() {
		InventoryStatusEntryInVO backup = new InventoryStatusEntryInVO(in);
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getInventoryService().addInventoryStatusEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INVENTORY_STATUS_TAB_TITLE_BASE64,
				JSValues.AJAX_INVENTORY_STATUS_ENTRY_COUNT, MessageCodes.INVENTORY_STATUS_TAB_TITLE, MessageCodes.INVENTORY_STATUS_TAB_TITLE_WITH_COUNT,
				new Long(statusEntryModel.getRowCount()));
		if (operationSuccess && in.getInventoryId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT,
					MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.INVENTORY_JOURNAL, in.getInventoryId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("inventorystatus_list");
		out = null;
		this.inventoryId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().deleteInventoryStatusEntry(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public String getAddresseeName() {
		return WebUtil.staffIdToName(in.getAddresseeId());
	}

	public CollidingInventoryBookingEagerModel getCollidingInventoryBookingModel(InventoryStatusEntryOutVO statusEntry) {
		return CollidingInventoryBookingEagerModel.getCachedCollidingInventoryBookingModel(statusEntry, true, collidingInventoryBookingModelCache);
	}

	public InventoryStatusEntryInVO getIn() {
		return in;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public String getOriginatorName() {
		return WebUtil.staffIdToName(in.getOriginatorId());
	}

	public InventoryStatusEntryOutVO getOut() {
		return out;
	}

	public IDVO getSelectedInventoryStatusEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public boolean getShowInventoryInActiveMessage() {
		return (statusType != null ? !statusType.isInventoryActive() : false);
	}

	public InventoryStatusEntryLazyModel getStatusEntryModel() {
		return statusEntryModel;
	}

	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.INVENTORY_STATUS_ENTRY_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_INVENTORY_STATUS_ENTRY);
		}
	}

	public void handleTypeChange() {
		loadSelectedType();
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INVENTORY_STATUS_ENTRY_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new InventoryStatusEntryInVO();
		}
		if (out != null) {
			copyStatusEntryOutToIn(in, out);
			inventoryId = in.getInventoryId();
		} else {
			initStatusEntryDefaultValues(in, inventoryId);
		}
	}

	private void initSets() {
		collidingInventoryBookingModelCache.clear();
		statusEntryModel.setInventoryId(in.getInventoryId());
		statusEntryModel.updateRowCount();
		Collection<InventoryStatusTypeVO> statusTypeVOs = null;
		try {
			statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getInventoryStatusTypes(WebUtil.getAuthentication(), in.getTypeId());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<InventoryStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				InventoryStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		loadSelectedType();
	}

	@Override
	public boolean isCreateable() {
		return (in.getInventoryId() == null ? false : true);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getInventoryService().getInventoryStatusEntry(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadSelectedType() {
		statusType = WebUtil.getInventoryStatusType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedInventoryStatusEntry(IDVO inventoryStatusEntry) {
		if (inventoryStatusEntry != null) {
			this.out = (InventoryStatusEntryOutVO) inventoryStatusEntry.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().updateInventoryStatusEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
