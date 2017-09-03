package org.phoenixctms.ctsms.web.model.inputfield;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;

@ManagedBean
@ViewScoped
public class InputFieldAssociationBean extends ManagedBeanBase {

	private Long inputFieldId;
	private InquiryUsageLazyModel inquiryUsageModel;
	private ProbandListEntryTagUsageLazyModel probandListEntryTagUsageModel;
	private EcrfFieldUsageLazyModel ecrfFieldUsageModel;

	public InputFieldAssociationBean() {
		super();
		inquiryUsageModel = new InquiryUsageLazyModel();
		probandListEntryTagUsageModel = new ProbandListEntryTagUsageLazyModel();
		ecrfFieldUsageModel = new EcrfFieldUsageLazyModel();
	}

	@Override
	protected String changeAction(Long id) {
		this.inputFieldId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public EcrfFieldUsageLazyModel getEcrfFieldUsageModel() {
		return ecrfFieldUsageModel;
	}

	public InquiryUsageLazyModel getInquiryUsageModel() {
		return inquiryUsageModel;
	}

	public ProbandListEntryTagUsageLazyModel getProbandListEntryTagUsageModel() {
		return probandListEntryTagUsageModel;
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		inquiryUsageModel.setInputFieldId(inputFieldId);
		inquiryUsageModel.updateRowCount();
		probandListEntryTagUsageModel.setInputFieldId(inputFieldId);
		probandListEntryTagUsageModel.updateRowCount();
		ecrfFieldUsageModel.setInputFieldId(inputFieldId);
		ecrfFieldUsageModel.updateRowCount();
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public void refreshEcrfFieldUsage() {
		ecrfFieldUsageModel.updateRowCount();
		LazyDataModelBase.clearFilters("ecrffield_list");
	}

	public void refreshInquiryUsage() {
		inquiryUsageModel.updateRowCount();
		LazyDataModelBase.clearFilters("inquiry_list");
	}

	public void refreshProbandListEntryTagUsage() {
		probandListEntryTagUsageModel.updateRowCount();
		LazyDataModelBase.clearFilters("probandlistentrytag_list");
	}
}
