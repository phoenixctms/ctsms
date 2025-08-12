package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.IDVOList;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

public class ProbandListStatusEntryBean extends ManagedBeanBase {

	public static void copyProbandListStatusEntryOutToIn(ProbandListStatusEntryInVO in, ProbandListStatusEntryOutVO out) {
		if (in != null && out != null) {
			ProbandListEntryOutVO listEntryVO = out.getListEntry();
			ProbandListStatusTypeVO statusVO = out.getStatus();
			in.setId(out.getId());
			in.setListEntryId(listEntryVO == null ? null : listEntryVO.getId());
			in.getVisitScheduleItemIds().clear();
			Iterator it = out.getVisitScheduleItems().iterator();
			while (it.hasNext()) {
				in.getVisitScheduleItemIds().add(((VisitScheduleItemOutVO) it.next()).getId());
			}
			in.setReason(out.getReason());
			in.setRealTimestamp(out.getRealTimestamp());
			in.setStatusId(statusVO == null ? null : statusVO.getId());
			in.setVersion(out.getVersion());
		}
	}

	public static void initProbandListStatusEntryDefaultValues(ProbandListStatusEntryInVO in, Long probandListEntryId) {
		if (in != null) {
			in.setId(null);
			in.setListEntryId(probandListEntryId);
			in.getVisitScheduleItemIds().clear();
			in.setReason(Messages.getString(MessageCodes.PROBAND_LIST_STATUS_ENTRY_REASON_PRESET));
			in.setRealTimestamp(new Date());
			in.setStatusId(null);
			in.setVersion(null);
		}
	}

	private ProbandListStatusEntryInVO in;
	private ProbandListStatusEntryOutVO out;
	private Long probandListEntryId;
	private ProbandListEntryOutVO probandListEntry;
	private ProbandListStatusEntryOutVO lastStatus;
	private boolean isLastStatus;
	private ArrayList<SelectItem> statusTypes;
	private ProbandListStatusEntryLazyModel probandListStatusEntryModel;
	private List<VisitScheduleItemOutVO> visitScheduleItems;

	public ProbandListStatusEntryBean() {
		super();
		probandListStatusEntryModel = new ProbandListStatusEntryLazyModel();
		visitScheduleItems = new ArrayList<VisitScheduleItemOutVO>();
		this.change();
	}

	@Override
	public String addAction() {
		ProbandListStatusEntryInVO backup = new ProbandListStatusEntryInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getTrialService().addProbandListStatusEntry(WebUtil.getAuthentication(), false, in);
			initIn();
			initSets();
			addOperationSuccessMessage("probandListStatusEntryMessages", MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("probandliststatus_list");
		out = null;
		this.probandListEntryId = id;
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
			out = WebUtil.getServiceLocator().getTrialService().deleteProbandListStatusEntry(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage("probandListStatusEntryMessages", MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public ProbandListStatusEntryInVO getIn() {
		return in;
	}

	public long getMassMailCount() {
		if (probandListEntry != null && in.getStatusId() != null) {
			HashSet<Long> visitScheduleItemIds = new HashSet<Long>(visitScheduleItems.size()); //force unique items to prevent confusion when unselecting a duplicate item
			Iterator<VisitScheduleItemOutVO> it = visitScheduleItems.iterator();
			while (it.hasNext()) {
				VisitScheduleItemOutVO visitScheduleItem = (VisitScheduleItemOutVO) it.next();
				if (visitScheduleItem != null) {
					visitScheduleItemIds.add(visitScheduleItem.getId());
				}
			}
			try {
				long count = WebUtil.getServiceLocator().getMassMailService().getMassMailCount(WebUtil.getAuthentication(), probandListEntry.getTrial().getId(), in.getStatusId(),
						visitScheduleItemIds, false,
						probandListEntry.getProband().getId());
				if (count == 0l) {
					count = WebUtil.getServiceLocator().getMassMailService().getMassMailCount(WebUtil.getAuthentication(), probandListEntry.getTrial().getId(), in.getStatusId(),
							new HashSet<Long>(), false,
							probandListEntry.getProband().getId());
				}
				return count;
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return 0l;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public ProbandListStatusEntryOutVO getOut() {
		return out;
	}

	public ProbandListEntryOutVO getProbandListEntry() {
		return probandListEntry;
	}

	public ProbandListStatusEntryLazyModel getProbandListStatusEntryModel() {
		return probandListStatusEntryModel;
	}

	public IDVO getSelectedProbandListStatusEntry() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public boolean getShowTerminalStateMessage() {
		if (in.getStatusId() != null) {
			Collection<ProbandListStatusTypeVO> statusTypeVOs = null;
			try {
				statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService().getProbandListStatusTypeTransitions(WebUtil.getAuthentication(), in.getStatusId());
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (statusTypeVOs != null) {
				if (statusTypeVOs.size() > 1 || (statusTypeVOs.size() == 1 && !statusTypeVOs.iterator().next().getId().equals(in.getStatusId()))) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<SelectItem> getStatusTypes() {
		return statusTypes;
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.PROBAND_LIST_STATUS_ENTRY_TITLE, Long.toString(out.getId()), out.getStatus().getName());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_PROBAND_LIST_STATUS_ENTRY);
		}
	}

	protected void initIn() {
		if (in == null) {
			in = new ProbandListStatusEntryInVO();
		}
		if (out != null) {
			copyProbandListStatusEntryOutToIn(in, out);
			probandListEntryId = in.getListEntryId();
		} else {
			initProbandListStatusEntryDefaultValues(in, probandListEntryId);
		}
	}

	private void initSets() {
		probandListStatusEntryModel.setProbandListEntryId(in.getListEntryId());
		probandListStatusEntryModel.updateRowCount();
		probandListEntry = WebUtil.getProbandListEntry(probandListEntryId);
		isLastStatus = false;
		lastStatus = null;
		if (in.getListEntryId() != null) {
			try {
				lastStatus = WebUtil.getServiceLocator().getTrialService().getProbandListEntry(WebUtil.getAuthentication(), probandListEntryId).getLastStatus();
				if (out != null && lastStatus != null) {
					isLastStatus = (out.getId() == lastStatus.getId());
				}
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		Collection<ProbandListStatusTypeVO> statusTypeVOs = null;
		if (out != null) {
			statusTypeVOs = new ArrayList<ProbandListStatusTypeVO>(1);
			statusTypeVOs.add(out.getStatus());
		} else {
			try {
				if (lastStatus != null) {
					statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
							.getProbandListStatusTypeTransitions(WebUtil.getAuthentication(), lastStatus.getStatus().getId());
				} else if (probandListEntry != null) {
					statusTypeVOs = WebUtil.getServiceLocator().getSelectionSetService()
							.getInitialProbandListStatusTypes(WebUtil.getAuthentication(), false, probandListEntry.getProband().getPerson());
				}
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (statusTypeVOs != null) {
			statusTypes = new ArrayList<SelectItem>(statusTypeVOs.size());
			Iterator<ProbandListStatusTypeVO> it = statusTypeVOs.iterator();
			while (it.hasNext()) {
				ProbandListStatusTypeVO typeVO = it.next();
				statusTypes.add(new SelectItem(typeVO.getId().toString(), typeVO.getName()));
			}
		} else {
			statusTypes = new ArrayList<SelectItem>();
		}
		loadVisitScheduleItems();
	}

	@Override
	public boolean isCreateable() {
		return (in.getListEntryId() == null ? false : !isCreated() && !WebUtil.isTrialLocked(probandListEntry) && !WebUtil.isProbandLocked(probandListEntry));
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	public boolean isInputVisible() {
		return isCreated() || (!WebUtil.isTrialLocked(probandListEntry) && !WebUtil.isProbandLocked(probandListEntry));
	}

	@Override
	public boolean isRemovable() {
		return isLastStatus && !WebUtil.isTrialLocked(probandListEntry) && !WebUtil.isProbandLocked(probandListEntry);
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getTrialService().getProbandListStatusEntry(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_LIST_STATUS_ENTRY,
						Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			Messages.addMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (lastStatus != null && in.getRealTimestamp().getSeconds() == 0) {
			long delta = CommonUtil.dateDeltaSecs(in.getRealTimestamp(), lastStatus.getRealTimestamp());
			if (delta > 0 && delta < 60) {
				in.setRealTimestamp(lastStatus.getRealTimestamp());
			}
		}
		LinkedHashSet<Long> visitScheduleItemIds = new LinkedHashSet<Long>(visitScheduleItems.size()); //force unique items to prevent confusion when unselecting a duplicate item
		Iterator<VisitScheduleItemOutVO> it = visitScheduleItems.iterator();
		while (it.hasNext()) {
			VisitScheduleItemOutVO visitScheduleItem = (VisitScheduleItemOutVO) it.next();
			if (visitScheduleItem != null) {
				visitScheduleItemIds.add(visitScheduleItem.getId());
			}
		}
		in.setVisitScheduleItemIds(new ArrayList<Long>(visitScheduleItemIds));
	}

	public void setSelectedProbandListStatusEntry(IDVO probandListStatusEntry) {
		if (probandListStatusEntry != null) {
			this.out = (ProbandListStatusEntryOutVO) probandListStatusEntry.getVo();
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessageClientId("probandListStatusEntryMessages", FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_PROBAND_LIST_STATUS_ENTRY,
						Long.toString(out.getId()));
				out = null;
			}
			this.initIn();
			initSets();
		}
	}

	public List<IDVO> completeVisitScheduleItem(String query) {
		if (probandListEntry != null) {
			try {
				Collection visitScheduleItemVOs = WebUtil.getServiceLocator().getToolsService().completeVisitScheduleItem(WebUtil.getAuthentication(), query,
						probandListEntry.getTrial().getId(), null);
				visitScheduleItemVOs.removeAll(visitScheduleItems);
				IDVO.transformVoCollection(visitScheduleItemVOs);
				return (List<IDVO>) visitScheduleItemVOs;
			} catch (ClassCastException e) {
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<IDVO>();
	}

	public List<IDVO> getVisitScheduleItems() {
		return new IDVOList(visitScheduleItems);
	}

	public void handleVisitScheduleItemSelect(SelectEvent event) {
	}

	public void handleVisitScheduleItemUnselect(UnselectEvent event) {
	}

	private void loadVisitScheduleItems() {
		visitScheduleItems.clear();
		Iterator<Long> it = in.getVisitScheduleItemIds().iterator();
		while (it.hasNext()) {
			VisitScheduleItemOutVO visitScheduleItem = WebUtil.getVisitScheduleItem(it.next());
			if (visitScheduleItem != null) {
				visitScheduleItems.add(visitScheduleItem);
			}
		}
	}

	public void setVisitScheduleItems(List<IDVO> visitScheduleItems) {
		if (visitScheduleItems != null) {
			ArrayList<VisitScheduleItemOutVO> visitScheduleItemsCopy = new ArrayList<VisitScheduleItemOutVO>(visitScheduleItems.size());
			Iterator<IDVO> it = visitScheduleItems.iterator();
			while (it.hasNext()) {
				IDVO idVo = it.next();
				if (idVo != null) {
					visitScheduleItemsCopy.add((VisitScheduleItemOutVO) idVo.getVo());
				}
			}
			this.visitScheduleItems.clear();
			this.visitScheduleItems.addAll(visitScheduleItemsCopy);
		} else {
			this.visitScheduleItems.clear();
		}
	}
}
