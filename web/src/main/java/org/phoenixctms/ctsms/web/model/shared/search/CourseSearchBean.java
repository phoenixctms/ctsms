package org.phoenixctms.ctsms.web.model.shared.search;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
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
public class CourseSearchBean extends SearchBeanBase {

	private CourseSearchResultLazyModel courseResultModel;
	private Date today;

	public CourseSearchBean() {
		super();
		today = new Date();
		courseResultModel = new CourseSearchResultLazyModel();
	}

	public String courseToColor(CourseOutVO course) {
		return (course != null ? WebUtil.colorToStyleClass(course.getCategory().getColor()) : "");
	}

	public Boolean getCourseExpired(CourseOutVO course) {
		return WebUtil.getCourseExpired(today, course);
	}

	public StreamedContent getCourseParticipantListPdfStreamedContent() throws Exception {
		try {
			CourseParticipantListPDFVO participantList = WebUtil.getServiceLocator().getSearchService()
					.renderCourseParticipantListPDFs(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(participantList.getDocumentDatas()), participantList.getContentType().getMimeType(),
					participantList.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
			throw e;
		}
	}

	public CourseSearchResultLazyModel getCourseResultModel() {
		return courseResultModel;
	}

	public Boolean getCourseValid(CourseOutVO course) {
		Boolean courseExpired = WebUtil.getCourseExpired(today, course);
		if (courseExpired != null) {
			return !courseExpired;
		}
		return null;
	}

	@Override
	protected String getCriteriaCommentPreset() {
		return Messages.getString(MessageCodes.COURSE_CRITERIA_COMMENT_PRESET);
	}

	@Override
	protected String getCriteriaLabelPreset() {
		return Messages.getString(MessageCodes.COURSE_CRITERIA_LABEL_PRESET);
	}

	@Override
	protected String getCurrentPageIds() {
		return this.courseResultModel.getCurrentPageIds();
	}

	@Override
	protected DBModule getDBModule() {
		return DBModule.COURSE_DB;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_COURSE_SEARCH_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_COURSE_SEARCH_EXPORTS);
	}

	public StreamedContent getExcelStreamedContent() throws Exception {
		try {
			SearchResultExcelVO excel = WebUtil.getServiceLocator().getSearchService()
					.exportCourse(WebUtil.getAuthentication(), criteriaIn, new HashSet<CriterionInVO>(criterionsIn), null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException|ServiceException|IllegalArgumentException e) {
			throw e;
		}
	}

	@Override
	public String getQueryResultTitle() {
		return getQueryResultTitle(courseResultModel.getRowCount());
	}

	@Override
	protected String getResultItemLabel() {
		return Messages.getString(MessageCodes.SEARCH_RESULT_COURSE_ITEM_LABEL);
	}

	public String getSetPickerIDJSCall(CourseOutVO course) {
		return getSetPickerIDJSCall(course == null ? null : course.getId(), WebUtil.clipStringPicker(WebUtil.courseOutVOToString(course)));
	}

	@Override
	public String getTitle(boolean operationSuccess) {
		return getTitle(MessageCodes.COURSE_CRITERIA_TITLE, MessageCodes.CREATE_NEW_COURSE_CRITERIA, operationSuccess);
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
		today = new Date();
	}

	@Override
	public boolean isMarkUnEncrypted() {
		return false;
	}

	@Override
	public String searchAction() {
		courseResultModel.setCriteriaIn(criteriaIn);
		courseResultModel.setCriterionsIn(getNewCriterions());
		updateInstantCriteria(true);
		courseResultModel.updateRowCount();
		DataTable.clearFilters(getResultListId());
		return SEARCH_OUTCOME;
	}
}
