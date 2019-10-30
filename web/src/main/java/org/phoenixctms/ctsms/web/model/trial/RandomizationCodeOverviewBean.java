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

	//	public String randomizationListCodeToColor(RandomizationListCodeOutVO code) {
	//		if (user != null) {
	//			if (user.getLocked()) {
	//				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.USER_LOCKED_COLOR, Bundle.SETTINGS, DefaultSettings.USER_LOCKED_COLOR));
	//			}
	//			Boolean passwordValid = getPasswordValid(user);
	//			if (passwordValid != null) {
	//				if (passwordValid) {
	//					return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.PASSWORD_VALID_COLOR, Bundle.SETTINGS, DefaultSettings.PASSWORD_VALID_COLOR));
	//				} else {
	//					return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.PASSWORD_INVALID_COLOR, Bundle.SETTINGS, DefaultSettings.PASSWORD_INVALID_COLOR));
	//				}
	//			}
	//		}
	//		return null;
	//	}
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
	//	public String timelineEventToColor(TimelineEventOutVO timelineEvent) {
	//		if (timelineEvent != null) {
	//			return WebUtil.expirationToColor(today, timelineEvent.getStart(), Settings.getTimelineEventDueInColorMap(),
	//					Settings.getColor(SettingCodes.TIMELINE_EVENT_OVERDUE_COLOR, Bundle.SETTINGS, DefaultSettings.TIMELINE_EVENT_OVERDUE_COLOR));
	//		}
	//		return "";
	//	}
}
