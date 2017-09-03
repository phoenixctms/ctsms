package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.web.model.EagerDataModelBase;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class CollidingVisitScheduleItemEagerModel extends EagerDataModelBase {

	public static CollidingVisitScheduleItemEagerModel getCachedCollidingVisitScheduleItemModel(ProbandStatusEntryOutVO statusEntry, boolean allProbandGroups,
			HashMap<Long, CollidingVisitScheduleItemEagerModel> collidingVisitScheduleItemModelCache) {
		CollidingVisitScheduleItemEagerModel model;
		if (statusEntry != null && collidingVisitScheduleItemModelCache != null) {
			long probandStatusEntryId = statusEntry.getId();
			if (collidingVisitScheduleItemModelCache.containsKey(probandStatusEntryId)) {
				model = collidingVisitScheduleItemModelCache.get(probandStatusEntryId);
			} else {
				model = new CollidingVisitScheduleItemEagerModel(probandStatusEntryId, allProbandGroups);
				collidingVisitScheduleItemModelCache.put(probandStatusEntryId, model);
			}
		} else {
			model = new CollidingVisitScheduleItemEagerModel(allProbandGroups);
		}
		return model;
	}

	private Long probandStatusEntryId;
	private ArrayList<SelectItem> filterTrials;
	private boolean allProbandGroups;

	public CollidingVisitScheduleItemEagerModel(boolean allProbandGroups) {
		super();
		this.allProbandGroups = allProbandGroups;
		initSets();
	}

	public CollidingVisitScheduleItemEagerModel(Long probandStatusEntryId, boolean allProbandGroups) {
		super();
		resetRows();
		this.probandStatusEntryId = probandStatusEntryId;
		this.allProbandGroups = allProbandGroups;
		initSets();
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> getEagerResult(PSFVO psf) {
		if (probandStatusEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getProbandService().getCollidingVisitScheduleItems(WebUtil.getAuthentication(), probandStatusEntryId, allProbandGroups);
			} catch (ServiceException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			} catch (AuthorisationException e) {
			} catch (IllegalArgumentException e) {
			}
		}
		return new ArrayList<VisitScheduleItemOutVO>();
	}

	public ArrayList<SelectItem> getFilterTrials() {
		return filterTrials;
	}

	public Long getProbandStatusEntryId() {
		return probandStatusEntryId;
	}

	@Override
	protected VisitScheduleItemOutVO getRowElement(Long id) {
		return WebUtil.getVisitScheduleItem(id);
	}

	private void initSets() {
		ProbandStatusEntryOutVO probandStatusEntryVO = WebUtil.getProbandStatusEntry(probandStatusEntryId);
		if (probandStatusEntryVO != null) {
			filterTrials = WebUtil.getParticipationTrials(probandStatusEntryVO.getProband().getId());
		} else {
			filterTrials = new ArrayList<SelectItem>();
		}
		filterTrials.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
	}

	public void setProbandStatusEntryId(Long probandStatusEntryId) {
		this.probandStatusEntryId = probandStatusEntryId;
		initSets();
	}
}
