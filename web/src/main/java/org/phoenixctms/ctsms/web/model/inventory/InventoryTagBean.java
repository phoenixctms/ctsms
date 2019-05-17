package org.phoenixctms.ctsms.web.model.inventory;

import java.util.ArrayList;
import java.util.Collection;
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
import org.phoenixctms.ctsms.vo.InventoryTagVO;
import org.phoenixctms.ctsms.vo.InventoryTagValueInVO;
import org.phoenixctms.ctsms.vo.InventoryTagValueOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class InventoryTagBean extends ManagedBeanBase {

	public static void copyTagValueOutToIn(InventoryTagValueInVO in, InventoryTagValueOutVO out) {
		if (in != null && out != null) {
			InventoryTagVO inventoryTagVO = out.getTag();
			InventoryOutVO inventoryVO = out.getInventory();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInventoryId(inventoryVO == null ? null : inventoryVO.getId());
			in.setTagId(inventoryTagVO == null ? null : inventoryTagVO.getId());
			in.setValue(out.getValue());
		}
	}

	public static void initTagValueDefaultValues(InventoryTagValueInVO in, Long inventoryId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setInventoryId(inventoryId);
			in.setTagId(null);
			in.setValue(Messages.getString(MessageCodes.INVENTORY_TAG_VALUE_PRESET));
		}
	}

	private InventoryTagValueInVO in;
	private InventoryTagValueOutVO out;
	private Long inventoryId;
	private ArrayList<SelectItem> availableTags;
	private InventoryTagValueLazyModel tagValueModel;

	public InventoryTagBean() {
		super();
		tagValueModel = new InventoryTagValueLazyModel();
	}

	@Override
	public String addAction() {
		InventoryTagValueInVO backup = new InventoryTagValueInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getInventoryService().addInventoryTagValue(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INVENTORY_TAG_TAB_TITLE_BASE64,
				JSValues.AJAX_INVENTORY_TAG_VALUE_COUNT, MessageCodes.INVENTORY_TAGS_TAB_TITLE, MessageCodes.INVENTORY_TAGS_TAB_TITLE_WITH_COUNT,
				new Long(tagValueModel.getRowCount()));
		if (operationSuccess && in.getInventoryId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT,
					MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.INVENTORY_JOURNAL, in.getInventoryId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		LazyDataModelBase.clearFilters("inventorytag_list");
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
			out = WebUtil.getServiceLocator().getInventoryService().deleteInventoryTagValue(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public ArrayList<SelectItem> getAvailableTags() {
		return availableTags;
	}

	public InventoryTagValueInVO getIn() {
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

	public InventoryTagValueOutVO getOut() {
		return out;
	}

	public IDVO getSelectedInventoryTagValue() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public InventoryTagValueLazyModel getTagValueModel() {
		return tagValueModel;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.INVENTORY_TAG_VALUE_TITLE, Long.toString(out.getId()), out.getTag().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_INVENTORY_TAG_VALUE);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.INVENTORY_TAG_VALUE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new InventoryTagValueInVO();
		}
		if (out != null) {
			copyTagValueOutToIn(in, out);
			inventoryId = in.getInventoryId();
		} else {
			initTagValueDefaultValues(in, inventoryId);
		}
	}

	private void initSets() {
		tagValueModel.setInventoryId(in.getInventoryId());
		tagValueModel.updateRowCount();
		Collection<InventoryTagVO> tagVOs = null;
		try {
			tagVOs = WebUtil.getServiceLocator().getSelectionSetService().getAvailableInventoryTags(WebUtil.getAuthentication(), this.in.getInventoryId(), this.in.getTagId());
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (tagVOs != null) {
			availableTags = new ArrayList<SelectItem>(tagVOs.size());
			Iterator<InventoryTagVO> it = tagVOs.iterator();
			while (it.hasNext()) {
				InventoryTagVO tagVO = it.next();
				availableTags.add(new SelectItem(tagVO.getId().toString(), tagVO.getName()));
			}
		} else {
			availableTags = new ArrayList<SelectItem>();
		}
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
			out = WebUtil.getServiceLocator().getInventoryService().getInventoryTagValue(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedInventoryTagValue(IDVO inventoryTagValue) {
		if (inventoryTagValue != null) {
			this.out = (InventoryTagValueOutVO) inventoryTagValue.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getInventoryService().updateInventoryTagValue(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
