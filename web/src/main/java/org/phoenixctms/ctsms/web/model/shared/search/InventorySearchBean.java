package org.phoenixctms.ctsms.web.model.shared.search;

import java.io.ByteArrayInputStream;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class InventorySearchBean extends SearchBeanBase {

	private InventorySearchResultLazyModel inventoryResultModel;

	public InventorySearchBean() {
		super();
		inventoryResultModel = new InventorySearchResultLazyModel();
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.INVENTORY_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.INVENTORY_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.inventoryResultModel.getCurrentPageIds();
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.INVENTORY_DB;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_INVENTORY_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_INVENTORY_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportInventory(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	public InventorySearchResultLazyModel getInventoryResultModel() {
		return inventoryResultModel;
	}

	public String getMaxOverlappingBookingsString(InventoryOutVO inventory) {
		if (inventory != null && inventory.getBookable()) {
			return Messages.getMessage(MessageCodes.MAX_OVERLAPPING_LABEL, inventory.getMaxOverlappingBookings());
		}
		return "";
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(inventoryResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_INVENTORY_ITEM_LABEL);
	}

	public String getSetPickerIDJSCall(InventoryOutVO inventory) {
		return getSetPickerIDJSCall(inventory == null ? null : inventory.getId(), WebUtil.clipStringPicker(WebUtil.inventoryOutVOToString(inventory)));
	}

	@Override
	public String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.INVENTORY_CRITERIA_TITLE, MessageCodes.CREATE_NEW_INVENTORY_CRITERIA, operationSuccess);
	}

	@PostConstruct
	private void init() {
		initPickTarget();
		Long id = WebUtil.getLongParamValue(GetParamNames.CRITERIA_ID);
		if (id != null) {
			this.load(id);
		} else {
			loadDefault();
		}
	}

	@Override
	protected void initSpecificSets() {
	}

	public String inventoryToColor(InventoryOutVO inventory) {
		return (inventory != null ? WebUtil.colorToStyleClass(inventory.getCategory().getColor()) : "");
	}

	@Override
	public boolean isMarkUnEncrypted() {
		return true;
	}

	@Override
	public String searchAction() {
		inventoryResultModel.setCriteriaIn(criteriaIn);
		inventoryResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		inventoryResultModel.updateRowCount();
		LazyDataModelBase.clearFilters("inventory_result_list");
		return SEARCH_OUTCOME;
	}
}
