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
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
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
public class MassMailSearchBean extends SearchBeanBase {

	private MassMailSearchResultLazyModel massMailResultModel;

	public MassMailSearchBean() {
		super();
		massMailResultModel = new MassMailSearchResultLazyModel();
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.MASS_MAIL_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.MASS_MAIL_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.massMailResultModel.getCurrentPageIds();
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.MASS_MAIL_DB;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_MASS_MAIL_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_MASS_MAIL_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportMassMail(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
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

	public String getMassMailProgressLabel(MassMailOutVO massMail) {
		return WebUtil.getMassMailProgressLabel(massMail, massMailResultModel.getMassMailProgress(massMail));
	}

	public int getMassMailProgressValue(MassMailOutVO massMail) {
		return WebUtil.getMassMailProgressValue(massMail, massMailResultModel.getMassMailProgress(massMail));
	}

	public MassMailSearchResultLazyModel getMassMailResultModel() {
		return massMailResultModel;
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(massMailResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_MASS_MAIL_ITEM_LABEL);
	}

	public String getSetPickerIDJSCall(MassMailOutVO massMail) {
		return getSetPickerIDJSCall(massMail == null ? null : massMail.getId(), WebUtil.clipStringPicker(WebUtil.massMailOutVOToString(massMail)));
	}

	@Override
	public String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.MASS_MAIL_CRITERIA_TITLE, MessageCodes.CREATE_NEW_MASS_MAIL_CRITERIA, operationSuccess);
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

	@Override
	public boolean isMarkUnEncrypted() {
		return true;
	}

	@Override
	public String searchAction() {
		massMailResultModel.setCriteriaIn(criteriaIn);
		massMailResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		massMailResultModel.updateRowCount();
		DataTable.clearFilters(getResultListId());
		return SEARCH_OUTCOME;
	}
}
