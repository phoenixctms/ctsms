package org.phoenixctms.ctsms.web.model.course;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.HyperlinkModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CourseCategoryVO;
import org.phoenixctms.ctsms.vo.CourseCertificatePDFVO;
import org.phoenixctms.ctsms.vo.CourseInVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CvSectionVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrainingRecordSectionVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;
import org.phoenixctms.ctsms.web.model.DefaultTreeNode;
import org.phoenixctms.ctsms.web.model.IDVOTreeNode;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelector;
import org.phoenixctms.ctsms.web.model.VariablePeriodSelectorListener;
import org.phoenixctms.ctsms.web.model.shared.CourseMultiPickerModel;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class CourseBean extends ManagedBeanBase implements VariablePeriodSelectorListener {

	private static final int VALIDITY_PERIOD_PROPERTY_ID = 1;

	public static void copyCourseOutToIn(CourseInVO in, CourseOutVO out) {
		if (in != null && out != null) {
			CourseCategoryVO courseCategoryVO = out.getCategory();
			Collection<CourseOutVO> precedingCourseVOs = out.getPrecedingCourses();
			DepartmentVO departmentVO = out.getDepartment();
			CvSectionVO cvSectionVO = out.getCvSectionPreset();
			TrainingRecordSectionVO trainingRecordSectionVO = out.getTrainingRecordSectionPreset();
			StaffOutVO institution = out.getInstitution();
			TrialOutVO trial = out.getTrial();
			VariablePeriodVO validityPeriodVO = out.getValidityPeriod();
			in.setCategoryId(courseCategoryVO == null ? null : courseCategoryVO.getId());
			in.setSelfRegistration(out.getSelfRegistration());
			in.setCvCommentPreset(out.getCvCommentPreset());
			in.setCvSectionPresetId(cvSectionVO == null ? null : cvSectionVO.getId());
			in.setTrainingRecordSectionPresetId(trainingRecordSectionVO == null ? null : trainingRecordSectionVO.getId());
			in.setCvTitle(out.getCvTitle());
			in.setDepartmentId(departmentVO == null ? null : departmentVO.getId());
			in.setDescription(out.getDescription());
			in.setExpires(out.getExpires());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInstitutionId(institution == null ? null : institution.getId());
			in.setTrialId(trial == null ? null : trial.getId());
			in.setMaxNumberOfParticipants(out.getMaxNumberOfParticipants());
			in.setName(out.getName());
			in.setParticipationDeadline(out.getParticipationDeadline());
			ArrayList<Long> precedingCourseIds = new ArrayList<Long>(precedingCourseVOs.size());
			Iterator<CourseOutVO> it = precedingCourseVOs.iterator();
			while (it.hasNext()) {
				precedingCourseIds.add(it.next().getId());
			}
			in.setPrecedingCourseIds(precedingCourseIds);
			in.setShowCommentCvPreset(out.getShowCommentCvPreset());
			in.setShowCvPreset(out.getShowCvPreset());
			in.setShowCommentTrainingRecordPreset(out.getShowCommentTrainingRecordPreset());
			in.setShowTrainingRecordPreset(out.getShowTrainingRecordPreset());
			in.setCertificate(out.getCertificate());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setValidityPeriod(validityPeriodVO == null ? null : validityPeriodVO.getPeriod());
			in.setValidityPeriodDays(out.getValidityPeriodDays());
		}
	}

	private static CourseOutVO createCourseOutFromIn(CourseInVO in) {
		CourseOutVO result = new CourseOutVO();
		if (in != null) {
			VariablePeriodVO validityPeriodVO = null;
			try {
				validityPeriodVO = WebUtil.getServiceLocator().getToolsService().getLocalizedVariablePeriod(WebUtil.getAuthentication(), in.getValidityPeriod());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			result.setSelfRegistration(in.getSelfRegistration());
			result.setCvCommentPreset(in.getCvCommentPreset());
			result.setCvTitle(in.getCvTitle());
			result.setDescription(in.getDescription());
			result.setExpires(in.getExpires());
			result.setMaxNumberOfParticipants(in.getMaxNumberOfParticipants());
			result.setName(in.getName());
			result.setName(CommonUtil.getCourseName(result));
			result.setShowCommentCvPreset(in.getShowCommentCvPreset());
			result.setShowCvPreset(in.getShowCvPreset());
			result.setShowCommentTrainingRecordPreset(in.getShowCommentTrainingRecordPreset());
			result.setShowTrainingRecordPreset(in.getShowTrainingRecordPreset());
			result.setCertificate(in.getCertificate());
			result.setStart(in.getStart());
			result.setStop(in.getStop());
			result.setParticipationDeadline(in.getParticipationDeadline());
			result.setValidityPeriod(validityPeriodVO);
			result.setValidityPeriodDays(in.getValidityPeriodDays());
			result.setPrecedingCoursesCount(in.getPrecedingCourseIds().size());
		}
		return result;
	}

	public static void initCourseDefaultValues(CourseInVO in, UserOutVO user) {
		if (in != null) {
			in.setCategoryId(null);
			in.setSelfRegistration(Settings.getBoolean(SettingCodes.COURSE_SELF_REGISTRATION_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_SELF_REGISTRATION_PRESET));
			in.setCvCommentPreset(Messages.getString(MessageCodes.COURSE_CV_COMMENT_PRESET_PRESET));
			in.setCvSectionPresetId(null);
			in.setTrainingRecordSectionPresetId(null);
			in.setCvTitle(Messages.getString(MessageCodes.COURSE_CV_TITLE_PRESET));
			in.setDepartmentId(user == null ? null : user.getDepartment().getId());
			in.setDescription(Messages.getString(MessageCodes.COURSE_DESCRIPTION_PRESET));
			in.setExpires(Settings.getBoolean(SettingCodes.COURSE_EXPIRES_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_EXPIRES_PRESET));
			in.setId(null);
			in.setVersion(null);
			in.setInstitutionId(null);
			in.setTrialId(null);
			in.setMaxNumberOfParticipants(Settings.getLongNullable(SettingCodes.COURSE_MAX_NUMBER_OF_PARTICIPANTS_PRESET, Bundle.SETTINGS,
					DefaultSettings.COURSE_MAX_NUMBER_OF_PARTICIPANTS_PRESET));
			in.setName(Messages.getString(MessageCodes.COURSE_NAME_PRESET));
			in.setPrecedingCourseIds(new ArrayList<Long>());
			in.setShowCommentCvPreset(
					Settings.getBoolean(SettingCodes.COURSE_SHOW_COMMENT_CV_PRESET_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_SHOW_COMMENT_CV_PRESET_PRESET));
			in.setShowCvPreset(Settings.getBoolean(SettingCodes.COURSE_SHOW_CV_PRESET_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_SHOW_CV_PRESET_PRESET));
			in.setShowCommentTrainingRecordPreset(
					Settings.getBoolean(SettingCodes.COURSE_SHOW_COMMENT_TRAINING_RECORD_PRESET_PRESET, Bundle.SETTINGS,
							DefaultSettings.COURSE_SHOW_COMMENT_TRAINING_RECORD_PRESET_PRESET));
			in.setShowTrainingRecordPreset(
					Settings.getBoolean(SettingCodes.COURSE_SHOW_TRAINING_RECORD_PRESET_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_SHOW_TRAINING_RECORD_PRESET_PRESET));
			in.setCertificate(
					Settings.getBoolean(SettingCodes.COURSE_CERTIFICATE_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_CERTIFICATE_PRESET));
			int courseDurationDays = Settings.getInt(SettingCodes.COURSE_DURATION_DAYS_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_DURATION_DAYS_PRESET);
			if (courseDurationDays > 0) {
				in.setStart(new Date());
				in.setStop(DateUtil.addDayMinuteDelta(in.getStart(), courseDurationDays, 0));
			} else {
				in.setStart(null);
				in.setStop(new Date());
			}
			Integer courseDeadlineStartDays = Settings.getIntNullable(SettingCodes.COURSE_DEADLINE_START_DAYS_PRESET, Bundle.SETTINGS,
					DefaultSettings.COURSE_DEADLINE_START_DAYS_PRESET);
			in.setParticipationDeadline(courseDeadlineStartDays == null ? null : DateUtil.addDayMinuteDelta(in.getStart(), -1 * courseDeadlineStartDays, 0));
			in.setValidityPeriod(Settings.getVariablePeriod(SettingCodes.COURSE_VALIDITY_PERIOD_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_VALIDITY_PERIOD_PRESET));
			in.setValidityPeriodDays(
					Settings.getLongNullable(SettingCodes.COURSE_VALIDITY_PERIOD_DAYS_PRESET, Bundle.SETTINGS, DefaultSettings.COURSE_VALIDITY_PERIOD_DAYS_PRESET));
		}
	}

	private CourseInVO in;
	private CourseOutVO out;
	private ArrayList<SelectItem> categories;
	private ArrayList<SelectItem> departments;
	private ArrayList<SelectItem> cvSections;
	private ArrayList<SelectItem> trainingRecordSections;
	private CvSectionVO cvSection;
	private TrainingRecordSectionVO trainingRecordSection;
	private TreeNode renewalsRoot;
	private TreeNode precedingCoursesRoot;
	private CourseMultiPickerModel precedingCourseMultiPicker;
	private VariablePeriodSelector validity;
	private String deferredDeleteReason;
	private Date today;
	private HashMap<String, Long> tabCountMap;
	private HashMap<String, String> tabTitleMap;

	public CourseBean() {
		super();
		tabCountMap = new HashMap<String, Long>();
		tabTitleMap = new HashMap<String, String>();
		today = new Date();
		DefaultTreeNode renewalsRoot = new DefaultTreeNode("renewals_root", null);
		renewalsRoot.setExpanded(true);
		renewalsRoot.setType(WebUtil.PARENT_NODE_TYPE);
		this.renewalsRoot = renewalsRoot;
		DefaultTreeNode precedingCoursesRoot = new DefaultTreeNode("preceding_courses_root", null);
		precedingCoursesRoot.setExpanded(true);
		precedingCoursesRoot.setType(WebUtil.PARENT_NODE_TYPE);
		this.precedingCoursesRoot = precedingCoursesRoot;
		precedingCourseMultiPicker = new CourseMultiPickerModel(
				Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES));
		setValidity(new VariablePeriodSelector(this, VALIDITY_PERIOD_PROPERTY_ID));
	}

	@Override
	public String addAction() {
		CourseInVO backup = new CourseInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getCourseService().addCourse(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PRECEDING_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PRECEDING_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_RENEWALS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_RENEWALS_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public void addPrecedingCourseIds() {
		precedingCourseMultiPicker.addIds(precedingCourseMultiPicker.getIds());
		in.setPrecedingCourseIds(new ArrayList<Long>(precedingCourseMultiPicker.getSelectionIds()));
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_TITLE_BASE64.toString(), JsUtil.encodeBase64(getTitle(operationSuccess), false));
			requestContext.addCallbackParam(JSValues.AJAX_WINDOW_NAME.toString(), getWindowName(operationSuccess));
			requestContext.addCallbackParam(JSValues.AJAX_OPERATION_SUCCESS.toString(), operationSuccess);
			requestContext.addCallbackParam(JSValues.AJAX_ROOT_ENTITY_CREATED.toString(), out != null);
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_LECTURER_TAB_TITLE_BASE64, JSValues.AJAX_LECTURER_COUNT,
					MessageCodes.LECTURERS_TAB_TITLE, MessageCodes.LECTURERS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_LECTURER_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_INVENTORY_BOOKING_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_INVENTORY_BOOKING_COUNT,
					MessageCodes.COURSE_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.COURSE_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_COURSE_INVENTORY_BOOKING_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_BASE64,
					JSValues.AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT, MessageCodes.ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE,
					MessageCodes.ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_HYPERLINK_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_HYPERLINK_COUNT,
					MessageCodes.COURSE_HYPERLINKS_TAB_TITLE, MessageCodes.COURSE_HYPERLINKS_TAB_TITLE_WITH_COUNT,
					tabCountMap.get(JSValues.AJAX_COURSE_HYPERLINK_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_FILE_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_FILE_COUNT,
					MessageCodes.COURSE_FILES_TAB_TITLE, MessageCodes.COURSE_FILES_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_COURSE_FILE_COUNT.toString()));
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT,
					MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT, tabCountMap.get(JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT.toString()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getCourseService().getCourse(WebUtil.getAuthentication(), id,
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_PRECEDING_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PRECEDING_DEPTH),
						Settings.getIntNullable(SettingCodes.GRAPH_MAX_RENEWALS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_RENEWALS_DEPTH));
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			} catch (AuthenticationException e) {
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				WebUtil.publishException(e);
			}
		}
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeByNode() {
		Long courseId = WebUtil.getLongParamValue(GetParamNames.COURSE_ID);
		if (courseId != null) {
			change(courseId.toString());
		} else {
			this.out = null;
			this.initIn();
			initSets();
			appendRequestContextCallbackArgs(true);
		}
	}

	private IDVOTreeNode courseOutVOtoPrecedingCourseTreeNode(CourseOutVO course, TreeNode renewal, ArrayList<IDVOTreeNode> nodes, Integer limit, Integer maxDepth,
			ArrayList<Object[]> deferred, int depth) {
		if ((limit == null || nodes.size() < limit.intValue()) && (maxDepth == null || depth <= maxDepth.intValue())) {
			IDVOTreeNode node = new IDVOTreeNode(course, renewal);
			nodes.add(node);
			renewal.setExpanded(true);
			if (course.getPrecedingCoursesCount() > 0L) {
				node.setType(WebUtil.PARENT_NODE_TYPE);
			} else {
				node.setType(WebUtil.LEAF_NODE_TYPE);
			}
			node.setSelectable(true);
			Collection<CourseOutVO> precedingCourses = course.getPrecedingCourses();
			Iterator<CourseOutVO> it = precedingCourses.iterator();
			if (Settings.getBoolean(SettingCodes.GRAPH_COURSE_BREADTH_FIRST, Bundle.SETTINGS, DefaultSettings.GRAPH_COURSE_BREADTH_FIRST)) {
				if (deferred == null) {
					deferred = new ArrayList<Object[]>(precedingCourses.size());
					while (it.hasNext()) {
						courseOutVOtoPrecedingCourseTreeNode(it.next(), node, nodes, limit, maxDepth, deferred, depth + 1);
					}
					Iterator<Object[]> deferredIt = deferred.iterator();
					while (deferredIt.hasNext()) {
						Object[] newNode = deferredIt.next();
						courseOutVOtoPrecedingCourseTreeNode((CourseOutVO) newNode[0], (IDVOTreeNode) newNode[1], nodes, limit, maxDepth, null, (Integer) newNode[2]);
					}
				} else {
					while (it.hasNext()) {
						Object[] newNode = new Object[3];
						newNode[0] = it.next();
						newNode[1] = node;
						newNode[2] = depth + 1;
						deferred.add(newNode);
					}
				}
			} else {
				while (it.hasNext()) {
					courseOutVOtoPrecedingCourseTreeNode(it.next(), node, nodes, limit, maxDepth, null, depth + 1);
				}
			}
			return node;
		}
		return null;
	}

	private IDVOTreeNode courseOutVOtoRenewalTreeNode(CourseOutVO course, TreeNode precedingCourse, ArrayList<IDVOTreeNode> nodes,
			Integer limit, Integer maxDepth, ArrayList<Object[]> deferred, int depth) {
		if ((limit == null || nodes.size() < limit.intValue()) && (maxDepth == null || depth <= maxDepth.intValue())) {
			IDVOTreeNode node = new IDVOTreeNode(course, precedingCourse);
			nodes.add(node);
			precedingCourse.setExpanded(true);
			if (course.getRenewalsCount() > 0L) {
				node.setType(WebUtil.PARENT_NODE_TYPE);
			} else {
				node.setType(WebUtil.LEAF_NODE_TYPE);
			}
			node.setSelectable(true);
			Collection<CourseOutVO> renewals = course.getRenewals();
			Iterator<CourseOutVO> it = renewals.iterator();
			if (Settings.getBoolean(SettingCodes.GRAPH_COURSE_BREADTH_FIRST, Bundle.SETTINGS, DefaultSettings.GRAPH_COURSE_BREADTH_FIRST)) {
				if (deferred == null) {
					deferred = new ArrayList<Object[]>(renewals.size());
					while (it.hasNext()) {
						courseOutVOtoRenewalTreeNode(it.next(), node, nodes, limit, maxDepth, deferred, depth + 1);
					}
					Iterator<Object[]> deferredIt = deferred.iterator();
					while (deferredIt.hasNext()) {
						Object[] newNode = deferredIt.next();
						courseOutVOtoRenewalTreeNode((CourseOutVO) newNode[0], (IDVOTreeNode) newNode[1], nodes, limit, maxDepth, null, (Integer) newNode[2]);
					}
				} else {
					while (it.hasNext()) {
						Object[] newNode = new Object[3];
						newNode[0] = it.next();
						newNode[1] = node;
						newNode[2] = depth + 1;
						deferred.add(newNode);
					}
				}
			} else {
				while (it.hasNext()) {
					courseOutVOtoRenewalTreeNode(it.next(), node, nodes, limit, maxDepth, null, depth + 1);
				}
			}
			return node;
		}
		return null;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getCourseService().deleteCourse(WebUtil.getAuthentication(), id,
					Settings.getBoolean(SettingCodes.COURSE_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.COURSE_DEFERRED_DELETE),
					false, deferredDeleteReason,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PRECEDING_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PRECEDING_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_RENEWALS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_RENEWALS_DEPTH));
			initIn();
			initSets();
			if (!out.getDeferredDelete()) {
				addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			}
			out = null;
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public StreamedContent getBlankCourseCertificatePdfStreamedContent() throws Exception {
		if (out != null) {
			try {
				CourseCertificatePDFVO certificate = WebUtil.getServiceLocator().getCourseService().renderBlankCourseCertificate(WebUtil.getAuthentication(), out.getId());
				return new DefaultStreamedContent(new ByteArrayInputStream(certificate.getDocumentDatas()), certificate.getContentType().getMimeType(), certificate.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	public StreamedContent getBlankCourseParticipantListPdfStreamedContent() throws Exception {
		if (out != null) {
			try {
				CourseParticipantListPDFVO participantList = WebUtil.getServiceLocator().getCourseService()
						.renderCourseParticipantList(WebUtil.getAuthentication(), out.getId(), true);
				return new DefaultStreamedContent(new ByteArrayInputStream(participantList.getDocumentDatas()), participantList.getContentType().getMimeType(),
						participantList.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	public ArrayList<SelectItem> getCategories() {
		return categories;
	}

	public Boolean getCourseExpired(CourseOutVO course) {
		return WebUtil.getCourseExpired(today, course);
	}

	public String getCourseTreeLabel() {
		Integer graphMaxCourseInstances = Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES);
		if (graphMaxCourseInstances == null) {
			return Messages.getString(MessageCodes.COURSE_TREE_LABEL);
		} else {
			return Messages.getMessage(MessageCodes.COURSE_TREE_MAX_LABEL, graphMaxCourseInstances);
		}
	}

	public String getCourseTreePrecedingCoursesLabel() {
		Integer graphMaxPrecedingDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_PRECEDING_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PRECEDING_DEPTH);
		if (graphMaxPrecedingDepth == null) {
			return Messages.getString(MessageCodes.COURSE_TREE_PRECEDING_COURSES_LABEL);
		} else {
			return Messages.getMessage(MessageCodes.COURSE_TREE_PRECEDING_COURSES_LEVELS_LABEL, graphMaxPrecedingDepth);
		}
	}

	public String getCourseTreeRenewalCoursesLabel() {
		Integer graphMaxRenewalsDepth = Settings.getIntNullable(SettingCodes.GRAPH_MAX_RENEWALS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_RENEWALS_DEPTH);
		if (graphMaxRenewalsDepth == null) {
			return Messages.getString(MessageCodes.COURSE_TREE_RENEWAL_COURSES_LABEL);
		} else {
			return Messages.getMessage(MessageCodes.COURSE_TREE_RENEWAL_COURSES_LEVELS_LABEL, graphMaxRenewalsDepth);
		}
	}

	public ArrayList<SelectItem> getCvSections() {
		return cvSections;
	}

	public ArrayList<SelectItem> getTrainingRecordSections() {
		return trainingRecordSections;
	}

	public String getDeferredDeleteReason() {
		return deferredDeleteReason;
	}

	public ArrayList<SelectItem> getDepartments() {
		return departments;
	}

	public Boolean getExpired() {
		return WebUtil.getCourseExpired(today, in);
	}

	public CourseInVO getIn() {
		return in;
	}

	public String getInstitutionName() {
		return WebUtil.staffIdToName(in.getInstitutionId());
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public CourseOutVO getOut() {
		return out;
	}

	@Override
	public VariablePeriod getPeriod(int property) {
		switch (property) {
			case VALIDITY_PERIOD_PROPERTY_ID:
				return this.in.getValidityPeriod();
			default:
				return VariablePeriodSelectorListener.NO_SELECTION_VARIABLE_PERIOD;
		}
	}

	public CourseMultiPickerModel getPrecedingCourseMultiPicker() {
		return precedingCourseMultiPicker;
	}

	public TreeNode getPrecedingCoursesRoot() {
		return precedingCoursesRoot;
	}

	public TreeNode getRenewalsRoot() {
		return renewalsRoot;
	}

	public String getTabTitle(String tab) {
		return tabTitleMap.get(tab);
	}

	@Override
	public String getTitle() {
		return getTitle(WebUtil.getLongParamValue(GetParamNames.COURSE_ID) == null);
	}

	private String getTitle(boolean operationSuccess) {
		if (out != null) {
			return Messages.getMessage(out.getDeferredDelete() ? MessageCodes.DELETED_TITLE : MessageCodes.COURSE_TITLE, Long.toString(out.getId()),
					CommonUtil.courseOutVOToString(out));
		} else {
			return Messages.getString(operationSuccess ? MessageCodes.CREATE_NEW_COURSE : MessageCodes.ERROR_LOADING_COURSE);
		}
	}

	public String getTrialName() {
		return WebUtil.trialIdToName(in.getTrialId());
	}

	public VariablePeriodSelector getValidity() {
		return validity;
	}

	@Override
	public String getWindowName() {
		return getWindowName(WebUtil.getLongParamValue(GetParamNames.COURSE_ID) == null);
	}

	private String getWindowName(boolean operationSuccess) {
		if (out != null) {
			return String.format(JSValues.COURSE_ENTITY_WINDOW_NAME.toString(), Long.toString(out.getId()), WebUtil.getWindowNameUniqueToken());
		} else {
			if (operationSuccess) {
				return String.format(JSValues.COURSE_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
			} else {
				Long courseId = WebUtil.getLongParamValue(GetParamNames.COURSE_ID);
				if (courseId != null) {
					return String.format(JSValues.COURSE_ENTITY_WINDOW_NAME.toString(), courseId.toString(), WebUtil.getWindowNameUniqueToken());
				} else {
					return String.format(JSValues.COURSE_ENTITY_WINDOW_NAME.toString(), JSValues.NEW_ENTITY_WINDOW_NAME_SUFFIX.toString(), "");
				}
			}
		}
	}

	public void handleCvSectionPresetChange() {
		loadCvSelectedSection();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && cvSection != null) {
			requestContext.addCallbackParam(JSValues.AJAX_CV_SECTION_SHOW_CV_PRESET.toString(), cvSection.getShowCvPreset());
		}
	}

	public void handleTrainingRecordSectionPresetChange() {
		loadTrainingRecordSelectedSection();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null && trainingRecordSection != null) {
			requestContext.addCallbackParam(JSValues.AJAX_TRAINING_RECORD_SECTION_SHOW_TRAINING_RECORD_PRESET.toString(), trainingRecordSection.getShowTrainingRecordPreset());
		}
	}

	public void handleSelfRegistrationChange() {
	}

	public void handleShowCvPresetChange() {
		if (!in.getShowCvPreset()) {
			in.setShowCommentCvPreset(false);
		}
	}

	public void handleShowTrainingRecordPresetChange() {
		if (!in.getShowTrainingRecordPreset()) {
			in.setShowCommentTrainingRecordPreset(false);
		}
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.COURSE_ID);
		if (id != null) {
			this.load(id);
		} else {
			this.change();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new CourseInVO();
		}
		if (out != null) {
			copyCourseOutToIn(in, out);
		} else {
			initCourseDefaultValues(in, WebUtil.getUser());
		}
	}

	private void initSets() {
		tabCountMap.clear();
		tabTitleMap.clear();
		Long count = (out == null ? null : WebUtil.getLecturerCount(in.getId(), null));
		tabCountMap.put(JSValues.AJAX_LECTURER_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_LECTURER_COUNT.toString(), WebUtil.getTabTitleString(MessageCodes.LECTURERS_TAB_TITLE, MessageCodes.LECTURERS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getCourseInventoryBookingCount(in.getId()));
		tabCountMap.put(JSValues.AJAX_COURSE_INVENTORY_BOOKING_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_COURSE_INVENTORY_BOOKING_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.COURSE_INVENTORY_BOOKINGS_TAB_TITLE, MessageCodes.COURSE_INVENTORY_BOOKINGS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getCourseParticipationStatusEntryCount(null, in.getId()));
		tabCountMap.put(JSValues.AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_ADMIN_COURSE_PARTICIPATION_STATUS_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE, MessageCodes.ADMIN_COURSE_PARTICIPATION_STATUS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getHyperlinkCount(HyperlinkModule.COURSE_HYPERLINK, in.getId()));
		tabCountMap.put(JSValues.AJAX_COURSE_HYPERLINK_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_COURSE_HYPERLINK_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.COURSE_HYPERLINKS_TAB_TITLE, MessageCodes.COURSE_HYPERLINKS_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getTotalFileCount(FileModule.COURSE_DOCUMENT, in.getId()));
		tabCountMap.put(JSValues.AJAX_COURSE_FILE_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_COURSE_FILE_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.COURSE_FILES_TAB_TITLE, MessageCodes.COURSE_FILES_TAB_TITLE_WITH_COUNT, count));
		count = (out == null ? null : WebUtil.getJournalCount(JournalModule.COURSE_JOURNAL, in.getId()));
		tabCountMap.put(JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT.toString(), count);
		tabTitleMap.put(JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT.toString(),
				WebUtil.getTabTitleString(MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT, count));
		today = new Date();
		renewalsRoot.getChildren().clear();
		precedingCoursesRoot.getChildren().clear();
		if (out != null) {
			courseOutVOtoPrecedingCourseTreeNode(out, precedingCoursesRoot, new ArrayList<IDVOTreeNode>(),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PRECEDING_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PRECEDING_DEPTH),
					null, 0);
		} else {
			IDVOTreeNode loose = new IDVOTreeNode(createCourseOutFromIn(in), precedingCoursesRoot);
			loose.setType(WebUtil.LEAF_NODE_TYPE);
		}
		if (out != null) {
			courseOutVOtoRenewalTreeNode(out, renewalsRoot, new ArrayList<IDVOTreeNode>(),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_RENEWALS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_RENEWALS_DEPTH),
					null, 0);
		} else {
			IDVOTreeNode loose = new IDVOTreeNode(createCourseOutFromIn(in), renewalsRoot);
			loose.setType(WebUtil.LEAF_NODE_TYPE);
		}
		precedingCourseMultiPicker.setIds(in.getPrecedingCourseIds());
		categories = WebUtil.getVisibleCourseCategories(in.getCategoryId());
		departments = WebUtil.getVisibleDepartments(in.getDepartmentId());
		cvSections = WebUtil.getCvSections(this.in.getCvSectionPresetId());
		trainingRecordSections = WebUtil.getTrainingRecordSections(this.in.getTrainingRecordSectionPresetId());
		loadCvSelectedSection();
		loadTrainingRecordSelectedSection();
		deferredDeleteReason = (out == null ? null : out.getDeferredDeleteReason());
		if (out != null && out.isDeferredDelete()) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MARKED_FOR_DELETION, out.getDeferredDeleteReason());
		}
	}

	@Override
	public boolean isCreateable() {
		return WebUtil.getModuleEnabled(DBModule.COURSE_DB);
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDeferredDelete() {
		return Settings.getBoolean(SettingCodes.COURSE_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.COURSE_DEFERRED_DELETE);
	}

	@Override
	public boolean isEditable() {
		return WebUtil.getModuleEnabled(DBModule.COURSE_DB) && super.isEditable();
	}

	@Override
	public boolean isRemovable() {
		return WebUtil.getModuleEnabled(DBModule.COURSE_DB) && super.isRemovable();
	}

	public boolean isTabEmphasized(String tab) {
		return WebUtil.isTabCountEmphasized(tabCountMap.get(tab));
	}

	public boolean isValidityPeriodSelectorEnabled() {
		return this.in.getExpires();
	}

	public boolean isValidityPeriodSpinnerEnabled() {
		return (this.in.getExpires() && (this.in.getValidityPeriod() == null || VariablePeriod.EXPLICIT.equals(this.in.getValidityPeriod())));
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getCourseService().getCourse(WebUtil.getAuthentication(), id,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PRECEDING_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PRECEDING_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_RENEWALS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_RENEWALS_DEPTH));
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	private void loadCvSelectedSection() {
		cvSection = WebUtil.getCvSection(in.getCvSectionPresetId());
	}

	private void loadTrainingRecordSelectedSection() {
		trainingRecordSection = WebUtil.getTrainingRecordSection(in.getTrainingRecordSectionPresetId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (!in.getSelfRegistration()) {
			in.setMaxNumberOfParticipants(null);
			in.setParticipationDeadline(null);
		}
		if (!in.getShowCvPreset()) {
			in.setShowCommentCvPreset(false);
		}
		if (!in.getShowTrainingRecordPreset()) {
			in.setShowCommentTrainingRecordPreset(false);
		}
		in.setPrecedingCourseIds(new ArrayList<Long>(precedingCourseMultiPicker.getSelectionIds()));
	}

	public void setDeferredDeleteReason(String deferredDeleteReason) {
		this.deferredDeleteReason = deferredDeleteReason;
	}

	@Override
	public void setPeriod(int property, VariablePeriod period) {
		switch (property) {
			case VALIDITY_PERIOD_PROPERTY_ID:
				this.in.setValidityPeriod(period);
				break;
			default:
		}
	}

	public void setValidity(VariablePeriodSelector validity) {
		this.validity = validity;
	}

	@Override
	public String updateAction() {
		CourseInVO backup = new CourseInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getCourseService().updateCourse(WebUtil.getAuthentication(), in,
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_PRECEDING_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_PRECEDING_DEPTH),
					Settings.getIntNullable(SettingCodes.GRAPH_MAX_RENEWALS_DEPTH, Bundle.SETTINGS, DefaultSettings.GRAPH_MAX_RENEWALS_DEPTH));
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
