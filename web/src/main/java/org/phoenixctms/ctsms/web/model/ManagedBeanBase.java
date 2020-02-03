package org.phoenixctms.ctsms.web.model;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class ManagedBeanBase {

	protected final static String UNIMPLEMENTED_OUTCOME = "unimplemented";
	protected final static String RESET_OUTCOME = "reset";
	protected final static String LOAD_OUTCOME = "load";
	protected final static String CHANGE_OUTCOME = "change";
	protected final static String ADD_OUTCOME = "add";
	protected final static String UPDATE_OUTCOME = "update";
	protected final static String DELETE_OUTCOME = "delete";
	protected final static String ERROR_OUTCOME = "error";
	protected final static String SEARCH_OUTCOME = "search";
	protected final static String BULK_ADD_OUTCOME = "bulkadd";
	protected final static String CLONE_ADD_OUTCOME = "cloneadd";
	protected final static String BULK_DELETE_OUTCOME = "bulkdelete";
	protected final static String BULK_UPDATE_OUTCOME = "bulkupdate";
	protected final static String VALID_OUTCOME = "valid";

	protected void actionPostProcess(String outcome) {
		boolean success = true;
		if (UNIMPLEMENTED_OUTCOME.equals(outcome)) {
			addUnimplementedMessage();
			success = false;
		} else if (ERROR_OUTCOME.equals(outcome)) {
			success = false;
		}
		appendRequestContextCallbackArgs(success, outcome);
	}

	public final void add() {
		actionPostProcess(addAction());
	}

	public String addAction() {
		return UNIMPLEMENTED_OUTCOME;
	}

	protected final void addOperationSuccessMessage(String facesMessageCode) {
		Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, facesMessageCode);
	}

	protected final void addOperationSuccessMessage(String clientId, String facesMessageCode) {
		Messages.addLocalizedMessageClientId(clientId, FacesMessage.SEVERITY_INFO, facesMessageCode);
	}

	protected void addUnimplementedMessage() {
		Messages.addLocalizedMessage(FacesMessage.SEVERITY_FATAL, MessageCodes.NOT_IMPLEMENTED);
	}

	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
	}

	protected void appendRequestContextCallbackArgs(boolean operationSuccess, String outcome) {
		appendRequestContextCallbackArgs(operationSuccess);
	}

	public void change() {
		actionPostProcess(changeAction((Long) null));
	}

	public final void change(String param) { // force remoteCommand to use this method...
		changeRootEntity(WebUtil.stringToLong(param));
	}

	protected String changeAction(Long id) {
		return UNIMPLEMENTED_OUTCOME;
	}

	public final String changeAction(String param) {
		return changeAction(WebUtil.stringToLong(param));
	}

	public final void changeRootEntity(Long id) {
		actionPostProcess(changeAction(id));
	}

	public final void delete() {
		actionPostProcess(deleteAction());
	}

	public final void delete(Long id) {
		actionPostProcess(deleteAction(id));
	}

	public String deleteAction() {
		return UNIMPLEMENTED_OUTCOME;
	}

	public String deleteAction(Long id) {
		return UNIMPLEMENTED_OUTCOME;
	}

	public String getModifiedAnnotation() {
		return "";
	}

	public String getTitle() {
		return "";
	}

	public String getWindowName() {
		return "";
	}

	public abstract boolean isCreateable();

	public abstract boolean isCreated();

	public boolean isEditable() {
		return isCreated();
	}

	public boolean isRemovable() {
		return isCreated();
	}

	public final void load() {
		actionPostProcess(loadAction());
	}

	public final void load(Long id) {
		actionPostProcess(loadAction(id));
	}

	public String loadAction() {
		return UNIMPLEMENTED_OUTCOME;
	}

	public String loadAction(Long id) {
		return UNIMPLEMENTED_OUTCOME;
	}

	public final void reset() {
		actionPostProcess(resetAction());
	}

	public String resetAction() {
		return UNIMPLEMENTED_OUTCOME;
	}

	public final void update() {
		actionPostProcess(updateAction());
	}

	public String updateAction() {
		return UNIMPLEMENTED_OUTCOME;
	}
}
