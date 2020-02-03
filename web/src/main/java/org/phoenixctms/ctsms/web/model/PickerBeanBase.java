package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

public abstract class PickerBeanBase extends ManagedBeanBase {

	private String pickTargetField;
	private String pickTargetLabel;
	private Boolean pickAjax;
	private String pickAjaxUpdate;
	private String addRemoteCommand;
	private String onclick;

	protected abstract String getCurrentPageIds();

	protected final String getSetPickerIDJSCall(Long id, String entityName) {
		StringBuilder sb = new StringBuilder();
		boolean hasUpdate = false;
		boolean multiPick = isMultiPicker();
		if (multiPick) {
			sb.append("addPickedID('");
		} else if (isPicker()) {
			if (pickAjax != null && pickAjax.booleanValue()) {
				if (pickAjaxUpdate != null && pickAjaxUpdate.length() > 0) {
					sb.append("setPickedIDAjaxUpdate('");
					hasUpdate = true;
				} else {
					sb.append("setPickedIDAjax('");
				}
			} else {
				sb.append("setPickedID('");
			}
		} else {
			sb.append("alert('");
			sb.append(Messages.getString(MessageCodes.PICKER_NOT_CONFIGURED));
			sb.append("')");
			return sb.toString();
		}
		sb.append(pickTargetField);
		sb.append("',");
		sb.append(id == null ? WebUtil.JS_NULL : id.toString());
		if (multiPick) {
			sb.append(",'");
			sb.append(addRemoteCommand);
		} else {
			sb.append(",'");
			sb.append(pickTargetLabel);
			sb.append("','");
			sb.append(entityName);
			if (hasUpdate) {
				sb.append("','");
				sb.append(pickAjaxUpdate);
			}
		}
		if (hasOnClick()) {
			sb.append("','");
			sb.append(onclick);
		}
		sb.append("')");
		return sb.toString();
	}

	public final void handlePickCurrentPage() {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.PICK_TARGET_FIELD.toString(), pickTargetField);
			requestContext.addCallbackParam(JSValues.PICK_CURRENT_PAGE_IDS.toString(), getCurrentPageIds());
			requestContext.addCallbackParam(JSValues.PICK_ADD_REMOTE_COMMAND.toString(), addRemoteCommand);
			if (hasOnClick()) {
				requestContext.addCallbackParam(JSValues.PICK_ON_CLICK.toString(), onclick);
			}
		}
	}

	private final boolean hasOnClick() {
		return (onclick != null && onclick.length() > 0);
	}

	protected final void initPickTarget() {
		pickTargetField = WebUtil.getParamValue(GetParamNames.PICK_TARGET_FIELD);
		pickTargetLabel = WebUtil.getParamValue(GetParamNames.PICK_TARGET_LABEL);
		pickAjax = WebUtil.getBooleanParamValue(GetParamNames.PICK_AJAX);
		pickAjaxUpdate = WebUtil.getParamValue(GetParamNames.PICK_AJAX_UPDATE);
		addRemoteCommand = WebUtil.getParamValue(GetParamNames.PICK_ADD_REMOTE_COMMAND);
		onclick = WebUtil.getParamValue(GetParamNames.PICK_ON_CLICK);
	}

	public final boolean isMultiPicker() {
		return isPicker() && (addRemoteCommand != null && addRemoteCommand.length() > 0);
	}

	public final boolean isPicker() {
		return (pickTargetField != null && pickTargetField.length() > 0);
	}
}
