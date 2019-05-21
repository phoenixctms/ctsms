package org.phoenixctms.ctsms.web.model.proband;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusTypeVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.shared.CollidingInventoryBookingEagerModel;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class ProbandStatusBean extends ManagedBeanBase {

	private static final boolean COLLIDING_VISIT_SCHEDULE_ITEMS_ALL_PROBAND_GROUPS = true;

	public static void copyStatusEntryOutToIn(ProbandStatusEntryInVO in, ProbandStatusEntryOutVO out) {
		if (in != null && out != null) {
			ProbandStatusTypeVO probandStatusTypeVO = out.getType();
			ProbandOutVO probandVO = out.getProband();
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setComment(out.getComment());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setTypeId(probandStatusTypeVO == null ? null : probandStatusTypeVO.getId());
		}
	}

	public static void initStatusEntryDefaultValues(ProbandStatusEntryInVO in, Long probandId) {
		if (in != null) {
			in.setId(null);
			in.setVersion(null);
			in.setProbandId(probandId);
			in.setComment(Messages.getString(MessageCodes.PROBAND_STATUS_ENTRY_COMMENT_PRESET));
			in.setStart(new Timestamp(System.currentTimeMillis()));
			in.setStop(null);
			in.setTypeId(null);
		}
	}

	private ProbandStatusEntryInVO in;
	private ProbandStatusEntryOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private ArrayList<SelectItem> statusTypes;
	private ProbandStatusEntryLazyModel statusEntryModel;
	private ProbandStatusTypeVO statusType;
	private HashMap<Long, CollidingInventoryBookingEagerModel> collidingInventoryBookingModelCache;
	private HashMap<Long, CollidingVisitScheduleItemEagerModel> collidingVisitScheduleItemModelCache;

	public ProbandStatusBean() {
		super();
		collidingInventoryBookingModelCache = new HashMap<Long, CollidingInventoryBookingEagerModel>();
		collidingVisitScheduleItemModelCache = new HashMap<Long, CollidingVisitScheduleItemEagerModel>();
		statusEntryModel = new ProbandStatusEntryLazyModel();
	}

	@Override
	public String addAction() {
		ProbandStatusEntryInVO backup = new ProbandStatusEntryInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		try {
			out = WebUtil.getServiceLocator().getProbandService().addProbandStatusEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (ServiceException|IllegalArgumentException|AuthorisationException e) {
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_STATUS_TAB_TITLE_BASE64,
				JSValues.AJAX_PROBAND_STATUS_ENTRY_COUNT, MessageCodes.PROBAND_STATUS_TAB_TITLE, MessageCodes.PROBAND_STATUS_TAB_TITLE_WITH_COUNT,
				new Long(statusEntryModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("probandstatus_list");
		out = null;
		this.probandId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getProbandService().deleteProbandStatusEntry(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public CollidingInventoryBookingEagerModel getCollidingInventoryBookingModel(ProbandStatusEntryOutVO statusEntry) {
		return CollidingInventoryBookingEagerModel.getCachedCollidingInventoryBookingModel(statusEntry, true, collidingInventoryBookingModelCache);
	}

	public CollidingVisitScheduleItemEagerModel getCollidingVisitScheduleItemModel(ProbandStatusEntryOutVO statusEntry) {
		return CollidingVisitScheduleItemEagerModel.getCachedCollidingVisitScheduleItemModel(statusEntry, COLLIDING_VISIT_SCHEDULE_ITEMS_ALL_PROBAND_GROUPS,
				collidingVisitScheduleItemModelCache);
	}

	public ProbandStatusEntryInVO getIn() {
		return in;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public ProbandStatusEntryOutVO getOut() {
		return out;
	}

	public IDVO getSelectedProbandStatusEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public boolean getShowProbandInActiveMessage() {
		return (statusType != null ? !statusType.isProbandActive() : false);
	}

	public ProbandStatusEntryLazyModel getStatusEntryModel() {
		return statusEntryModel;
	}

	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_STATUS_ENTRY_TITLE, Long.toString(out.getId()), out.getType().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_STATUS_ENTRY);
		}
	}

	public void handleTypeChange() {
		loadSelectedType();
	}

	@PostConstruct
	private void init() {
		// System.out.println("POSTCONSTRUCT: " + this.toString());
		Long id = WebUtil.getLongParamValue(GetParamNames.PROBAND_STATUS_ENTRY_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new ProbandStatusEntryInVO();
		}
		if (out != null) {
			copyStatusEntryOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initStatusEntryDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		collidingInventoryBookingModelCache.clear();
		collidingVisitScheduleItemModelCache.clear();
		statusEntryModel.setProbandId(in.getProbandId());
		statusEntryModel.updateRowCount();
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		Collection<ProbandStatusTypeVO> statusTypeVOs = null;
		if (proband != null) {
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
						.getProbandStatusTypes(WebUtil.getAuthentication(), proband.getPerson() ? true : null, !proband.getPerson() ? true : null, in.getTypeId());
			} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<ProbandStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				ProbandStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		loadSelectedType();
		// proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
		if (WebUtil.isProbandLocked(proband)) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
		}
	}

	@Override
	public boolean isCreateable() {
		return (in.getProbandId() == null ? false : !WebUtil.isProbandLocked(proband));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	public boolean isInputVisible() {
		return isCreated() || !WebUtil.isProbandLocked(proband);
	}

	@Override
	public boolean isRemovable() {
		return isCreated() && !WebUtil.isProbandLocked(proband);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getProbandService().getProbandStatusEntry(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_STATUS_ENTRY, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
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

	private void loadSelectedType() {
		statusType = WebUtil.getProbandStatusType(in.getTypeId());
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void setSelectedProbandStatusEntry(IDVO probandStatusEntry) {
		if (probandStatusEntry != null) {
			this.out = (ProbandStatusEntryOutVO) probandStatusEntry.getVo();
			this.initIn();
			initSets();
		}
	}

	@Override
	public String updateAction() {
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateProbandStatusEntry(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException|AuthorisationException|IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}
}
