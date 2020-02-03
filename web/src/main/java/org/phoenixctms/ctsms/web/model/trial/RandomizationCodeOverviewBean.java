package org.phoenixctms.ctsms.web.model.trial;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.vo.RandomizationListCodeOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class RandomizationCodeOverviewBean extends ManagedBeanBase {

	private CodeBreakBean codeBreakBean;
	private RandomizationListCodeLazyModel randomizationListCodeModel;

	public RandomizationCodeOverviewBean() {
		super();
		randomizationListCodeModel = new RandomizationListCodeLazyModel();
		codeBreakBean = new CodeBreakBean();
	}

	public RandomizationListCodeLazyModel getRandomizationListCodeModel() {
		return randomizationListCodeModel;
	}

	public CodeBreakBean getCodeBreakBean() {
		return codeBreakBean;
	}

	public TrialOutVO getTrial(RandomizationListCodeOutVO code) {
		return WebUtil.getRandomizationCodeTrial(code);
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	public void handlTrialChange() {
		initSets();
	}

	private void initSets() {
		randomizationListCodeModel.updateRowCount();
		DataTable.clearFilters("randomizationlistcodes_list");
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}
}
