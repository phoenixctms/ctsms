package org.phoenixctms.ctsms.web.model.shared;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class EcrfSectionBean extends EcrfDataEntryBeanBase {

	private Long fieldStatusEntryId;
	private ECRFFieldStatusEntryOutVO fieldStatusEntry;

	public EcrfSectionBean() {
		super();
		fieldStatusEntryId = null;
		fieldStatusEntry = null;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(), false));
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
		}
	}

	@Override
	protected String changeAction(Long id) {
		fieldStatusEntryId = id;
		fieldStatusEntry = WebUtil.getEcrfFieldStatusEntry(fieldStatusEntryId);
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	protected String getDeltaErrorMessageId() {
		return "ecrfsection_form:delta_error_msg";
	}

	public ECRFFieldStatusEntryOutVO getFieldStatusEntry() {
		return fieldStatusEntry;
	}

	@Override
	public String getTitle() {
		if (fieldStatusEntry != null) {
			return Messages.getMessage(MessageCodes.ECRF_SECTION_TITLE, fieldStatusEntry.getEcrfField().getUniqueName(),
					CommonUtil.probandOutVOToString(fieldStatusEntry.getListEntry().getProband()));
		} else {
			return Messages.getString(MessageCodes.BLANK_ECRF_SECTION_TITLE);
		}
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.ECRF_FIELD_STATUS_ENTRY_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (fieldStatusEntry != null) {
			return String.format(JSValues.ECRF_SECTION_WINDOW_NAME.toString(), Integer.toString(WebUtil.getEcrfSectionHashCode(fieldStatusEntry)),
					WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.ECRF_SECTION_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Integer ecrfSectionHashCode = WebUtil.getEcrfSectionHashCode(fieldStatusEntryId);
				if (ecrfSectionHashCode != null) {
					return String.format(JSValues.ECRF_SECTION_WINDOW_NAME.toString(), Integer.toString(ecrfSectionHashCode), WebUtil.getWindowNameUniqueToken());
				} else if (fieldStatusEntryId != null) {
					return String.format(JSValues.ECRF_SECTION_WINDOW_NAME.toString(), fieldStatusEntryId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.ECRF_SECTION_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	@PostConstruct
	private void init() {
		fieldStatusEntryId = WebUtil.stringToLong(WebUtil.getParamValue(GetParamNames.ECRF_FIELD_STATUS_ENTRY_ID));
	}


	// protected void initIn() {
	// }

	private void initSets() {
		probandListEntry = null;
		ecrf = null;
		String section = null;
		Long index = null;
		if (fieldStatusEntry != null) {
			probandListEntry = fieldStatusEntry.getListEntry();
			ecrf = fieldStatusEntry.getEcrfField().getEcrf();
			section = fieldStatusEntry.getEcrfField().getSection();
			index = fieldStatusEntry.getIndex();
		}
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();
		ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, listEntryId);
		ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
		if (ecrfStatus != null) {
			ecrf = ecrfStatus.getEcrf();
			probandListEntry = ecrfStatus.getListEntry();
		}
		ecrfFieldValueBean.setProbandListEntry(probandListEntry);
		ecrfFieldValueBean.setFilterSection(section);
		ecrfFieldValueBean.setFilterIndex(index);
		ecrfFieldValueBean.changeRootEntity(ecrfId);
		if (fieldStatusEntry != null) {
			addMessages();
		}
	}

	@Override
	public String loadAction() {
		fieldStatusEntry = null;
		try {
			fieldStatusEntry = WebUtil.getServiceLocator().getTrialService().getEcrfFieldStatusEntry(WebUtil.getAuthentication(), fieldStatusEntryId);
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
			//initIn(true, false);
			initSets();
		}
		return ERROR_OUTCOME;
	}

	public String updateSectionAction(EcrfFieldSection section) {
		ecrfFieldValueBean.updateSection(section);
		// initSpecificSets();
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		Long ecrfId = ecrf == null ? null : ecrf.getId();

		if (ecrfStatus == null) {
			ecrfStatus = WebUtil.getEcrfStatusEntry(ecrfId, listEntryId);
			ecrfFieldValueBean.setEcrfStatus(ecrfStatus);
			if (ecrfStatus != null) {
				ecrf = ecrfStatus.getEcrf();
				ecrfFieldValueBean.setEcrf(ecrf);
				probandListEntry = ecrfStatus.getListEntry();
				ecrfFieldValueBean.setProbandListEntry(probandListEntry);
			}
		}
		addMessages();
		return UPDATE_OUTCOME;
	}
}
