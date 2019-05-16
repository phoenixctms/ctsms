package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.GroupVisitMatrix;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class VisitScheduleBean extends ManagedBeanBase {

	public static void copyVisitScheduleItemOutToIn(VisitScheduleItemInVO in, VisitScheduleItemOutVO out) {
		if (in != null && out != null) {
			ProbandGroupOutVO probandGroupVO = out.getGroup();
			VisitOutVO visitVO = out.getVisit();
			TrialOutVO trialVO = out.getTrial();
			in.setGroupId(probandGroupVO == null ? null : probandGroupVO.getId());
			in.setId(out.getId());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setToken(out.getToken());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setVersion(out.getVersion());
			in.setVisitId(visitVO == null ? null : visitVO.getId());
			in.setNotify(out.getNotify());
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
		}
	}

	private VisitScheduleItemInVO in;
	private VisitScheduleItemOutVO out;
	private Long trialId;
	private TrialOutVO trial;
	private ArrayList<SelectItem> visits;
	private ArrayList<SelectItem> filterVisits;
	private ArrayList<SelectItem> probandGroups;
	private ArrayList<SelectItem> filterProbandGroups;
	private VisitScheduleItemLazyModel visitScheduleItemModel;
	private HashMap<Long, CollidingProbandStatusEntryEagerModel> collidingProbandStatusEntryModelCache;
	private HashMap<Long, CollidingStaffStatusEntryEagerModel> collidingStaffStatusEntryModelCache;
	private GroupVisitMatrix<VisitScheduleItemOutVO> matrix;

	public VisitScheduleBean() {
		super();
		collidingProbandStatusEntryModelCache = new HashMap<Long, CollidingProbandStatusEntryEagerModel>();
		collidingStaffStatusEntryModelCache = new HashMap<Long, CollidingStaffStatusEntryEagerModel>();
		visitScheduleItemModel = new VisitScheduleItemLazyModel();
		matrix = new GroupVisitMatrix<VisitScheduleItemOutVO>() {

			@Override
			protected ProbandGroupOutVO getGroupFromItem(VisitScheduleItemOutVO item) {
				return item.getGroup();
			}

			@Override
			protected Long getItemCount(Long trialId) {
				return WebUtil.getVisitScheduleItemCount(trialId, null);
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
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
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
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getTrialService().addVisitScheduleItem(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
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
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return ERROR_OUTCOME;
	}

	public CollidingProbandStatusEntryEagerModel getCollidingProbandStatusEntryModel(VisitScheduleItemOutVO visitScheduleItem) {
		CollidingProbandStatusEntryEagerModel collidingProbandStatusEntryModel = CollidingProbandStatusEntryEagerModel.getCachedCollidingProbandStatusEntryModel(visitScheduleItem,
				true, collidingProbandStatusEntryModelCache);
		collidingProbandStatusEntryModel.setProbandId(null);
		return collidingProbandStatusEntryModel;
	}

	public CollidingStaffStatusEntryEagerModel getCollidingStaffStatusEntryModel(VisitScheduleItemOutVO visitScheduleItem) {
		CollidingStaffStatusEntryEagerModel collidingStaffStatusEntryModel = CollidingStaffStatusEntryEagerModel.getCachedCollidingStaffStatusEntryModel(visitScheduleItem,
				true, collidingStaffStatusEntryModelCache);
		collidingStaffStatusEntryModel.setStaffId(null);
		return collidingStaffStatusEntryModel;
	}

	public ArrayList<SelectItem> getFilterProbandGroups() {
		return filterProbandGroups;
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

	public ArrayList<SelectItem> getProbandGroups() {
		return probandGroups;
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
			VisitScheduleExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportVisitSchedule(WebUtil.getAuthentication(), trialId, null);
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
		visitScheduleItemModel.setTrialId(in.getTrialId());
		visitScheduleItemModel.updateRowCount();
		visits = WebUtil.getVisits(in.getTrialId());
		probandGroups = WebUtil.getProbandGroups(in.getTrialId());
		filterProbandGroups = new ArrayList<SelectItem>(probandGroups);
		filterProbandGroups.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		filterVisits = new ArrayList<SelectItem>(visits);
		filterVisits.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		matrix.initPages();
		// loadMatrix();
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
		try {
			out = WebUtil.getServiceLocator().getTrialService().updateVisitScheduleItem(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			matrix.loadMatrix();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
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
}
