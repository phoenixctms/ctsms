package org.phoenixctms.ctsms.web.model.shared;

import javax.faces.application.FacesMessage;

import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.ECRFStatusTypeVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class EcrfDataEntryBeanBase extends ManagedBeanBase {

	protected ECRFOutVO ecrf;
	protected ProbandListEntryOutVO probandListEntry;
	protected ECRFStatusEntryVO ecrfStatus;
	protected EcrfFieldValueBean ecrfFieldValueBean;
	private EcrfFieldValueAuditTrailLazyModel ecrfFieldValueAuditTrailModel;
	private EcrfFieldStatusEntryBean ecrfFieldAnnotationStatusEntryBean;
	private EcrfFieldStatusEntryBean ecrfFieldValidationStatusEntryBean;
	private EcrfFieldStatusEntryBean ecrfFieldQueryStatusEntryBean;
	private ECRFFieldOutVO auditTrialEcrfField;

	public EcrfDataEntryBeanBase() {
		super();
		ecrfFieldValueBean = new EcrfFieldValueBean();
		ecrfFieldValueBean.setDeltaErrorMessageId(getDeltaErrorMessageId());
		ecrfFieldValueAuditTrailModel = new EcrfFieldValueAuditTrailLazyModel();
		ecrfFieldAnnotationStatusEntryBean = new EcrfFieldStatusEntryBean(ECRFFieldStatusQueue.ANNOTATION);
		ecrfFieldValidationStatusEntryBean = new EcrfFieldStatusEntryBean(ECRFFieldStatusQueue.VALIDATION);
		ecrfFieldQueryStatusEntryBean = new EcrfFieldStatusEntryBean(ECRFFieldStatusQueue.QUERY);
		ecrfStatus = null;
	}

	protected void addMessages() {
		// sync with EcrfFieldValueBean.editable:
		if (WebUtil.isProbandLocked(probandListEntry)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		} else if (WebUtil.isTrialLocked(probandListEntry)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		} else if (probandListEntry != null && !probandListEntry.getTrial().getStatus().getEcrfValueInputEnabled()) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.ECRF_VALUE_INPUT_DISABLED_FOR_TRIAL, probandListEntry.getTrial().getStatus().getName());
		} else if (probandListEntry != null && probandListEntry.getLastStatus() != null && !probandListEntry.getLastStatus().getStatus().getEcrfValueInputEnabled()) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.ECRF_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_ENTRY, probandListEntry.getLastStatus().getStatus()
					.getName());
		} else if (ecrfStatus != null && ecrfStatus.getStatus().getValueLockdown()) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.ECRF_FIELD_VALUES_LOCKED_STATUS, ecrfStatus.getStatus().getName());
		}
	}

	public void changeAuditTrail() {
		Long listEntryId = WebUtil.getLongParamValue(GetParamNames.PROBAND_LIST_ENTRY_ID);
		Long ecrfFieldId = WebUtil.getLongParamValue(GetParamNames.ECRF_FIELD_ID);
		Long seriesIndex = WebUtil.getLongParamValue(GetParamNames.SERIES_INDEX);
		ecrfFieldValueAuditTrailModel.setListEntryId(listEntryId);
		ecrfFieldValueAuditTrailModel.setEcrfFieldId(ecrfFieldId);
		ecrfFieldValueAuditTrailModel.setIndex(seriesIndex);
		ecrfFieldValueAuditTrailModel.updateRowCount();
		ecrfFieldAnnotationStatusEntryBean.setListEntryId(listEntryId);
		ecrfFieldAnnotationStatusEntryBean.setIndex(seriesIndex);
		ecrfFieldAnnotationStatusEntryBean.changeRootEntity(ecrfFieldId);
		ecrfFieldValidationStatusEntryBean.setListEntryId(listEntryId);
		ecrfFieldValidationStatusEntryBean.setIndex(seriesIndex);
		ecrfFieldValidationStatusEntryBean.changeRootEntity(ecrfFieldId);
		ecrfFieldQueryStatusEntryBean.setListEntryId(listEntryId);
		ecrfFieldQueryStatusEntryBean.setIndex(seriesIndex);
		ecrfFieldQueryStatusEntryBean.changeRootEntity(ecrfFieldId);
		auditTrialEcrfField = WebUtil.getEcrfField(ecrfFieldId);
	}

	public ECRFFieldStatusQueue convertToQueue(ECRFFieldStatusQueue queue) {
		return queue;
	}

	public String getAuditTrailFieldLabel() {
		if (auditTrialEcrfField != null) {
			return getAuditTrailFieldLabel(auditTrialEcrfField.getUniqueName());
		}
		return null;
	}

	private String getAuditTrailFieldLabel(String fieldName) {
		if (auditTrialEcrfField != null && probandListEntry != null) {
			if (ecrfFieldValueAuditTrailModel.getIndex() != null) {
				return Messages.getMessage(MessageCodes.ECRF_FIELD_SERIES_INDEX_LABEL, ecrfFieldValueAuditTrailModel.getIndex(), fieldName);
			} else {
				return Messages.getMessage(MessageCodes.ECRF_FIELD_LABEL, fieldName);
			}
		}
		return null;
	}

	public String getAuditTrailFieldShortLabel() {
		if (auditTrialEcrfField != null) {
			return getAuditTrailFieldLabel(auditTrialEcrfField.getField().getName());
		}
		return null;
	}

	public EcrfFieldValueAuditTrailLazyModel getAuditTrailModel() {
		return ecrfFieldValueAuditTrailModel;
	}

	public String getAuditTrailProbandName() {
		return probandListEntry == null ? WebUtil.getNoProbandPickedMessage() : WebUtil.probandOutVOToString(probandListEntry.getProband());
	}

	public ECRFFieldOutVO getAuditTrialEcrfField() {
		return auditTrialEcrfField;
	}

	protected abstract String getDeltaErrorMessageId();

	public ECRFOutVO getEcrf() {
		return ecrf;
	}

	public EcrfFieldStatusEntryBean getEcrfFieldAnnotationStatusEntryBean() {
		return ecrfFieldAnnotationStatusEntryBean;
	}

	public EcrfFieldStatusEntryBean getEcrfFieldQueryStatusEntryBean() {
		return ecrfFieldQueryStatusEntryBean;
	}

	public EcrfFieldStatusEntryBean getEcrfFieldValidationStatusEntryBean() {
		return ecrfFieldValidationStatusEntryBean;
	}

	public EcrfFieldValueBean getEcrfFieldValueBean() {
		return ecrfFieldValueBean;
	}

	public ECRFStatusEntryVO getEcrfStatus() {
		return ecrfStatus;
	}

	public String getEcrfStatusTypeLabel(ECRFStatusTypeVO statusType) {
		if (statusType != null) {
			return statusType.getName();
		} else {
			return Messages.getString(MessageCodes.NO_ECRF_STATUS_TYPE_LABEL);
		}
	}

	public ProbandListEntryOutVO getProbandListEntry() {
		return probandListEntry;
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return ecrfStatus != null;
	}

	@Override
	public boolean isEditable() {
		if (probandListEntry == null || ecrf == null) {
			return false;
		} else if (WebUtil.isTrialLocked(probandListEntry)) {
			return false;
		} else if (WebUtil.isProbandLocked(probandListEntry)) {
			return false;
		} else if (probandListEntry != null && !probandListEntry.getTrial().getStatus().getEcrfValueInputEnabled()) {
			return false;
		} else if (probandListEntry.getLastStatus() != null && !probandListEntry.getLastStatus().getStatus().getEcrfValueInputEnabled()) {
			return false;
		}
		return true;
	}

	protected void resetAuditTrailModel() {
		Long listEntryId = probandListEntry == null ? null : probandListEntry.getId();
		ecrfFieldValueAuditTrailModel.setListEntryId(listEntryId);
		ecrfFieldValueAuditTrailModel.setEcrfFieldId(null);
		ecrfFieldValueAuditTrailModel.setIndex(null);
		ecrfFieldValueAuditTrailModel.updateRowCount();
		ecrfFieldAnnotationStatusEntryBean.setListEntryId(listEntryId);
		ecrfFieldAnnotationStatusEntryBean.setIndex(null);
		ecrfFieldAnnotationStatusEntryBean.changeRootEntity(null);
		ecrfFieldValidationStatusEntryBean.setListEntryId(listEntryId);
		ecrfFieldValidationStatusEntryBean.setIndex(null);
		ecrfFieldValidationStatusEntryBean.changeRootEntity(null);
		ecrfFieldQueryStatusEntryBean.setListEntryId(listEntryId);
		ecrfFieldQueryStatusEntryBean.setIndex(null);
		ecrfFieldQueryStatusEntryBean.changeRootEntity(null);
		auditTrialEcrfField = null;
	}

	public final void updateSection(EcrfFieldSection section) {
		actionPostProcess(updateSectionAction(section));
	}

	public abstract String updateSectionAction(EcrfFieldSection section);
}
