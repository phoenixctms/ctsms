package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.VisitScheduleDateMode;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.LightProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.vo.VisitScheduleDateModeVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.GroupVisitMatrix;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.IDVO.VOTransformation;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.VisitScheduleDateModeSelector;
import org.phoenixctms.ctsms.web.model.VisitScheduleDateModeSelectorListener;
import org.phoenixctms.ctsms.web.model.shared.CollidingProbandStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.CollidingStaffStatusEntryEagerModel;
import org.phoenixctms.ctsms.web.model.shared.VisitScheduleItemLazyModel;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class VisitScheduleBean extends ManagedBeanBase implements VisitScheduleDateModeSelectorListener {

	private final static VOTransformation<LightProbandListEntryTagOutVO, ProbandListEntryTagOutVO> PROBAND_LIST_ENTRY_TAG_VO_TRANSFORMATION = new IDVO.VOTransformation<LightProbandListEntryTagOutVO, ProbandListEntryTagOutVO>() {

		@Override
		public ProbandListEntryTagOutVO transform(LightProbandListEntryTagOutVO vo) {
			return vo != null ? WebUtil.getProbandListEntryTag(vo.getId()) : null;
		}
	};
	private static final String UNIQUE_PROBAND_LIST_ENTRY_TAG_NAME = "{0}. {1}";

	public String getProbandListEntryTagName(ProbandListEntryTagOutVO probandListEntryTagVO) {
		if (probandListEntryTagVO != null && probandListEntryTagVO.getField() != null) {
			return MessageFormat.format(UNIQUE_PROBAND_LIST_ENTRY_TAG_NAME, Long.toString(probandListEntryTagVO.getPosition()),
					probandListEntryTagVO.getField().getName());
		}
		return null;
	}

	private static final int MODE_PROPERTY_ID = 1;
	private ProbandListEntryTagOutVO startTag;
	private ProbandListEntryTagOutVO stopTag;
	private VisitScheduleDateModeSelector mode;

	public List<IDVO> completeProbandListEntryTag(String query) {
		if (in.getTrialId() != null) {
			try {
				Collection tagVOs = WebUtil.getServiceLocator().getToolsService().completeProbandListEntryTag(WebUtil.getAuthentication(), query, in.getTrialId(), null);
				IDVO.transformVoCollection(tagVOs, PROBAND_LIST_ENTRY_TAG_VO_TRANSFORMATION);
				return (List<IDVO>) tagVOs;
			} catch (ClassCastException e) {
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<IDVO>();
	}

	public IDVO getStartTag() {
		if (startTag != null) {
			return IDVO.transformVo(startTag);
		}
		return null;
	}

	public void setStartTag(IDVO startTag) {
		if (startTag != null) {
			this.startTag = (ProbandListEntryTagOutVO) startTag.getVo();
		} else {
			this.startTag = null;
		}
	}

	public void handleStartTagSelect(SelectEvent event) {
	}

	public void handleStartTagUnselect(UnselectEvent event) {
	}

	private void loadStartTag() {
		startTag = WebUtil.getProbandListEntryTag(in.getStartTagId());
	}

	public IDVO getStopTag() {
		if (stopTag != null) {
			return IDVO.transformVo(stopTag);
		}
		return null;
	}

	public void setStopTag(IDVO stopTag) {
		if (stopTag != null) {
			this.stopTag = (ProbandListEntryTagOutVO) stopTag.getVo();
		} else {
			this.stopTag = null;
		}
	}

	public void handleStopTagSelect(SelectEvent event) {
	}

	public void handleStopTagUnselect(UnselectEvent event) {
	}

	private void loadStopTag() {
		stopTag = WebUtil.getProbandListEntryTag(in.getStopTagId());
	}

	public static void copyVisitScheduleItemOutToIn(VisitScheduleItemInVO in, VisitScheduleItemOutVO out) {
		if (in != null && out != null) {
			ProbandGroupOutVO probandGroupVO = out.getGroup();
			VisitOutVO visitVO = out.getVisit();
			TrialOutVO trialVO = out.getTrial();
			VisitScheduleDateModeVO modeVO = out.getMode();
			ProbandListEntryTagOutVO startTagVO = out.getStartTag();
			ProbandListEntryTagOutVO stopTagVO = out.getStopTag();
			in.setGroupId(probandGroupVO == null ? null : probandGroupVO.getId());
			in.setId(out.getId());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setToken(out.getToken());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setVersion(out.getVersion());
			in.setVisitId(visitVO == null ? null : visitVO.getId());
			in.setNotify(out.getNotify());
			in.setOffsetSeconds(out.getOffsetSeconds());
			in.setDuration(out.getDuration());
			in.setMode(modeVO == null ? null : modeVO.getDateMode());
			in.setStartTagId(startTagVO == null ? null : startTagVO.getId());
			in.setStopTagId(stopTagVO == null ? null : stopTagVO.getId());
		}
	}

	public static void copyVisitScheduleAppointmentToIn(VisitScheduleItemInVO in, VisitScheduleAppointmentVO out) {
		if (in != null && out != null) {
			ProbandGroupOutVO probandGroupVO = out.getGroup();
			VisitOutVO visitVO = out.getVisit();
			TrialOutVO trialVO = out.getTrial();
			VisitScheduleDateModeVO modeVO = out.getMode();
			LightProbandListEntryTagOutVO startTagVO = out.getStartTag();
			LightProbandListEntryTagOutVO stopTagVO = out.getStopTag();
			in.setGroupId(probandGroupVO == null ? null : probandGroupVO.getId());
			in.setId(out.getId());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setToken(out.getToken());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setVersion(out.getVersion());
			in.setVisitId(visitVO == null ? null : visitVO.getId());
			in.setNotify(out.getNotify());
			in.setOffsetSeconds(out.getOffsetSeconds());
			in.setDuration(out.getDuration());
			in.setMode(modeVO == null ? null : modeVO.getDateMode());
			in.setStartTagId(startTagVO == null ? null : startTagVO.getId());
			in.setStopTagId(stopTagVO == null ? null : stopTagVO.getId());
		}
	}

	public static void initVisitScheduleItemDefaultValues(VisitScheduleItemInVO in, Long trialId) {
		if (in != null) {
			in.setGroupId(null);
			in.setId(null);
			in.setStart(null);
			in.setStop(null);
			in.setToken(Messages.getString(MessageCodes.VISIT_SCHEDULE_ITEM_TOKEN_PRESET));
			in.setTrialId(trialId);
			in.setVersion(null);
			in.setVisitId(null);
			in.setNotify(Settings.getBoolean(SettingCodes.VISIT_SCHEDULE_ITEM_NOTIFY_PRESET, Bundle.SETTINGS, DefaultSettings.VISIT_SCHEDULE_ITEM_NOTIFY_PRESET));
			in.setOffsetSeconds(
					Settings.getIntNullable(SettingCodes.VISIT_SCHEDULE_ITEM_OFFSET_SECONDS_PRESET, Bundle.SETTINGS, DefaultSettings.VISIT_SCHEDULE_ITEM_OFFSET_SECONDS_PRESET));
			in.setDuration(Settings.getIntNullable(SettingCodes.VISIT_SCHEDULE_ITEM_DURATION_PRESET, Bundle.SETTINGS, DefaultSettings.VISIT_SCHEDULE_ITEM_DURATION_PRESET));
			in.setMode(Settings.getVisitScheduleDateMode(SettingCodes.VISIT_SCHEDULE_ITEM_MODE_PRESET, Bundle.SETTINGS, DefaultSettings.VISIT_SCHEDULE_ITEM_MODE_PRESET));
			in.setStartTagId(null);
			in.setStopTagId(null);
		}
	}

	private VisitScheduleItemInVO in;
	private VisitScheduleItemOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ArrayList<SelectItem> visits;
	private ArrayList<SelectItem> filterVisits;
	private ArrayList<SelectItem> durations;
	private ArrayList<SelectItem> offsets;
	private VisitScheduleItemLazyModel visitScheduleItemModel;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;
	private HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	private HashMap<Long, VisitScheduleAppointmentEagerModel> visitScheduleAppointmentModelCache;
	private GroupVisitMatrix<VisitScheduleItemOutVO> matrix;
	private ProbandGroupOutVO group;

	public VisitScheduleBean() {
		super();
		collidingProbandStatusEntryModelCache = new HashMap<Long, CollidingProbandStatusEntryEagerModel>();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		visitScheduleAppointmentModelCache = new HashMap<Long, VisitScheduleAppointmentEagerModel>();
		visitScheduleItemModel = new VisitScheduleItemLazyModel();
		setMode(new VisitScheduleDateModeSelector(this, MODE_PROPERTY_ID));
		matrix = new GroupVisitMatrix<VisitScheduleItemOutVO>() {

			@Override
			protected ProbandGroupOutVO getGroupFromItem(VisitScheduleItemOutVO item) {
				return item.getGroup();
			}

			@Override
			protected Long getItemCount(Long trialId) {
				return WebUtil.getVisitScheduleItemCount(trialId, null, false);
			}

			@Override
			protected String getItemLabel(VisitScheduleItemOutVO item) {
				return item.getToken();
			}

			@Override
			protected Collection<VisitScheduleItemOutVO> getItemsPage(Long trialId, PSFVO psf) {
				try {
					return WebUtil.getServiceLocator().getTrialService()
							.getVisitScheduleItemList(WebUtil.getAuthentication(), trialId, true, psf);
				} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				}
				return null;
			}

			@Override
			protected String getPaginatorFirstPageButtonLabel() {
				return MessageCodes.VISIT_SCHEDULE_ITEM_MATRIX_FIRST_PAGE_BUTTON_LABEL;
			}

			@Override
			protected String getPaginatorLoadLabel() {
				return MessageCodes.VISIT_SCHEDULE_ITEM_MATRIX_LOAD_LABEL;
			}

			@Override
			protected String getPaginatorPageButtonLabel() {
				return MessageCodes.VISIT_SCHEDULE_ITEM_MATRIX_PAGE_BUTTON_LABEL;
			}

			@Override
			protected VisitOutVO getVisitFromItem(VisitScheduleItemOutVO item) {
				return item.getVisit();
			}

			@Override
			protected void setGroupId(Long groupId) {
				in.setGroupId(groupId);
			}

			@Override
			protected void setVisitId(Long visitId) {
				in.setVisitId(visitId);
			}
		};
	}

	@Override
	public String addAction() {
		VisitScheduleItemInVO backup = new VisitScheduleItemInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().addVisitScheduleItem(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException | IllegalArgumentException | AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_VISIT_SCHEDULE_TAB_TITLE_BASE64,
				JSValues.AJAX_VISIT_SCHEDULE_ITEM_COUNT, MessageCodes.VISIT_SCHEDULE_TAB_TITLE, MessageCodes.VISIT_SCHEDULE_TAB_TITLE_WITH_COUNT,
				new Long(visitScheduleItemModel.getRowCount()));
		if (operationSuccess && in.getTrialId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
					MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("visitschedule_list");
		out = null;
		this.trialId = id;
		matrix.change(id);
		initIn();
		initSets();
		matrix.loadMatrix();
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getTrialService().deleteVisitScheduleItem(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			matrix.loadMatrix();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public CollidingProbandStatusEntryEagerModel getCollidingProbandStatusEntryModel(VisitScheduleItemOutVO visitScheduleItem) {
		CollidingProbandStatusEntryEagerModel collidingProbandStatusEntryModel = CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(visitScheduleItem,
				true, collidingProbandStatusEntryModelCache);
		collidingProbandStatusEntryModel.setProbandId(null);
		return collidingProbandStatusEntryModel;
	}

	public VisitScheduleAppointmentEagerModel getVisitScheduleAppointmentModel(VisitScheduleItemOutVO visitScheduleItem) {
		VisitScheduleAppointmentEagerModel visitScheduleAppointmentModel = VisitScheduleAppointmentEagerModel.getCachedVisitScheduleAppointmentModel(visitScheduleItem,
				true, visitScheduleAppointmentModelCache);
		visitScheduleAppointmentModel.setProbandId(null);
		return visitScheduleAppointmentModel;
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(VisitScheduleItemOutVO visitScheduleItem) {
		CollidingStaffStatusEntryEagerModel collidingStaffStatusEntryModel = CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(visitScheduleItem,
				true, collidingStaffStatusEntryModelCache);
		collidingStaffStatusEntryModel.setStaffId(null);
		return collidingStaffStatusEntryModel;
	}

	public ArrayList<SelectItem> getFilterVisits() {
		return filterVisits;
	}

	public VisitScheduleItemInVO getIn() {
		return in;
	}

	public GroupVisitMatrix getMatrix() {
		return matrix;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public VisitScheduleItemOutVO getOut() {
		return out;
	}

	public IDVO getSelectedVisitScheduleItem() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.VISIT_SCHEDULE_ITEM_TITLE, Long.toString(out.getId()), out.getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_VISIT_SCHEDULE_ITEM);
		}
	}

	public ArrayList<SelectItem> getVisits() {
		return visits;
	}

	public StreamedContent getVisitScheduleExcelStreamedContent() throws Exception {
		try {
			VisitScheduleExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportVisitSchedule(WebUtil.getAuthentication(), trialId, null, null, null, null);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public VisitScheduleItemLazyModel getVisitScheduleItemModel() {
		return visitScheduleItemModel;
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.VISIT_SCHEDULE_ITEM_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new VisitScheduleItemInVO();
		}
		if (out != null) {
			copyVisitScheduleItemOutToIn(in, out);
			trialId = in.getTrialId();
		} else {
			initVisitScheduleItemDefaultValues(in, trialId);
		}
	}

	private void initSets() {
		collidingProbandStatusEntryModelCache.clear();
		collidingStaffStatusEntryModelCache.clear();
		visitScheduleAppointmentModelCache.clear();
		visitScheduleItemModel.setTrialId(in.getTrialId());
		visitScheduleItemModel.setExpand(false);
		visitScheduleItemModel.updateRowCount();
		visits = WebUtil.getVisits(in.getTrialId());
		durations = WebUtil.getVisitScheduleDurations();
		offsets = WebUtil.getVisitScheduleOffsets();
		filterVisits = new ArrayList<SelectItem>(visits);
		filterVisits.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		loadStopTag();
		loadStartTag();
		loadProbandGroup();
		matrix.initPages();
		trial = WebUtil.getTrial(this.in.getTrialId());
		if (WebUtil.isTrialLocked(trial)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getTrialId() == null ? false : !WebUtil.isTrialLocked(trial));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isTrialLocked(trial);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isTrialLocked(trial);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getVisitScheduleItem(WebUtil.getAuthentication(), id);
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
			matrix.loadMatrix();
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		matrix.loadMatrix();
		return RESET_OUTCOME;
	}

	public void setSelectedVisitScheduleItem(IDVO visitScheduleItem) {
		if (visitScheduleItem != null) {
			this.out = (VisitScheduleItemOutVO) visitScheduleItem.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		VisitScheduleItemInVO backup = new VisitScheduleItemInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateVisitScheduleItem(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
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

	public void visitScheduleItemStartAddOneDay(ActionEvent e) {
		if (this.in.getStart() != null) {
			this.in.setStart(DateUtil.addDayMinuteDelta(this.in.getStart(), 1, 0));
		}
	}

	public void visitScheduleItemStartSubOneDay(ActionEvent e) {
		if (this.in.getStart() != null) {
			this.in.setStart(DateUtil.addDayMinuteDelta(this.in.getStart(), -1, 0));
		}
	}

	public void visitScheduleItemStopAddOneDay(ActionEvent e) {
		if (this.in.getStop() != null) {
			this.in.setStop(DateUtil.addDayMinuteDelta(this.in.getStop(), 1, 0));
		}
	}

	public void visitScheduleItemStopSubOneDay(ActionEvent e) {
		if (this.in.getStop() != null) {
			this.in.setStop(DateUtil.addDayMinuteDelta(this.in.getStop(), -1, 0));
		}
	}

	@Override
	public void setVisitScheduleDateMode(int property, VisitScheduleDateMode visitScheduleDateMode) {
		switch (property) {
			case MODE_PROPERTY_ID:
				this.in.setMode(visitScheduleDateMode);
				break;
			default:
		}
	}

	public void setMode(VisitScheduleDateModeSelector mode) {
		this.mode = mode;
	}

	public VisitScheduleDateModeSelector getMode() {
		return mode;
	}

	@Override
	public VisitScheduleDateMode getVisitScheduleDateMode(int property) {
		switch (property) {
			case MODE_PROPERTY_ID:
				return this.in.getMode();
			default:
				return VisitScheduleDateModeSelectorListener.NO_SELECTION_VISIT_SCHEDULE_DATE_MODE;
		}
	}

	protected void sanitizeInVals() {
		if (group != null) {
			in.setGroupId(group.getId());
		} else {
			in.setGroupId(null);
		}
		if (!isStartVisible()) {
			in.setStart(null);
		}
		if (!isStopVisible()) {
			in.setStop(null);
		}
		if (isStartTagVisible()) {
			if (startTag != null) {
				in.setStartTagId(startTag.getId());
			} else {
				in.setStartTagId(null);
			}
		} else {
			in.setStartTagId(null);
		}
		if (isStopTagVisible()) {
			if (stopTag != null) {
				in.setStopTagId(stopTag.getId());
			} else {
				in.setStopTagId(null);
			}
		} else {
			in.setStopTagId(null);
		}
		if (!isDurationVisible()) {
			in.setDuration(null);
		}
		if (!isOffsetVisible()) {
			in.setOffsetSeconds(null);
		}
	}

	public boolean isStale() {
		return VisitScheduleDateMode.STALE.equals(in.getMode());
	}

	public boolean isStartVisible() {
		return VisitScheduleDateMode.STATIC.equals(in.getMode()) || isStale();
	}

	public boolean isStopVisible() {
		return isStartVisible();
	}

	public boolean isStartTagVisible() {
		return isStopTagVisible() || isDurationVisible();
	}

	public boolean isStopTagVisible() {
		return VisitScheduleDateMode.TAGS.equals(in.getMode()) || isStale();
	}

	public boolean isDurationVisible() {
		return VisitScheduleDateMode.TAG_DURATION.equals(in.getMode()) || isStale();
	}

	public boolean isOffsetVisible() {
		return isStartTagVisible();
	}

	public void handleModeChange() {
		if (!isStartVisible()) {
			in.setStart(null);
		}
		if (!isStopVisible()) {
			in.setStop(null);
		}
		if (!isStartTagVisible()) {
			in.setStartTagId(null);
			startTag = null;
		}
		if (!isStopTagVisible()) {
			in.setStopTagId(null);
			stopTag = null;
		}
		if (!isDurationVisible()) {
			in.setDuration(null);
		}
		if (!isOffsetVisible()) {
			in.setOffsetSeconds(null);
		}
	}

	public ArrayList<SelectItem> getDurations() {
		return durations;
	}

	public ArrayList<SelectItem> getOffsets() {
		return offsets;
	}

	public List<IDVO> completeGroup(String query) {
		try {
			Collection probandGroupVOs = WebUtil.getServiceLocator().getToolsService().completeProbandGroup(WebUtil.getAuthentication(), query, query, trialId, null);
			IDVO.transformVoCollection(probandGroupVOs);
			return (List<IDVO>) probandGroupVOs;
		} catch (ClassCastException e) {
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<IDVO>();
	}

	public IDVO getGroup() {
		if (group != null) {
			return IDVO.transformVo(group);
		}
		return null;
	}

	public void setGroup(IDVO group) {
		if (group != null) {
			this.group = (ProbandGroupOutVO) group.getVo();
		} else {
			this.group = null;
		}
	}

	public void handleGroupSelect(SelectEvent event) {
	}

	public void handleGroupUnselect(UnselectEvent event) {
	}

	private void loadProbandGroup() {
		group = WebUtil.getProbandGroup(in.getGroupId());
	}
}
