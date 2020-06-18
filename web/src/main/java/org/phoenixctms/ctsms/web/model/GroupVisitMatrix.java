package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.phoenixctms.ctsms.compare.ProbandGroupOutVOTokenComparator;
import org.phoenixctms.ctsms.compare.VisitOutVOTokenComparator;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public abstract class GroupVisitMatrix<ITEM> {

	private Long trialId;
	private static final String MATRIX_ITEMS_SEPARATOR = ", ";
	private HashMap<Long, ProbandGroupOutVO> matrixGroups;
	private HashMap<Long, VisitOutVO> matrixVisits;
	private HashMap<Long, HashMap<Long, ArrayList<ITEM>>> matrixItemMap;
	private Paginator paginator;
	private boolean matrixVisitsVertical;
	private final static Comparator GROUP_COMPARATOR = new ProbandGroupOutVOTokenComparator();
	private final static Comparator VISIT_COMPARATOR = new VisitOutVOTokenComparator();

	public GroupVisitMatrix() {
		matrixVisitsVertical = Settings.getBoolean(SettingCodes.GROUP_VISIT_MATRIX_VISITS_VERTICAL_DEFAULT, Bundle.SETTINGS,
				DefaultSettings.GROUP_VISIT_MATRIX_VISITS_VERTICAL_DEFAULT);
		matrixGroups = new HashMap<Long, ProbandGroupOutVO>();
		matrixVisits = new HashMap<Long, VisitOutVO>();
		matrixItemMap = new HashMap<Long, HashMap<Long, ArrayList<ITEM>>>();
		paginator = new Paginator() {

			@Override
			protected Long getCount(Long... ids) {
				if (ids[0] != null) {
					return getItemCount(ids[0]);
				}
				return null;
			}

			@Override
			protected int getDefaultPageSize() {
				return Settings.getInt(SettingCodes.GROUP_VISIT_MATRIX_DEFAULT_PAGE_SIZE, Bundle.SETTINGS, DefaultSettings.GROUP_VISIT_MATRIX_DEFAULT_PAGE_SIZE);
			}

			@Override
			protected String getFirstPageButtonLabel() {
				return getPaginatorFirstPageButtonLabel();
			}

			@Override
			protected String getLoadLabel() {
				return getPaginatorLoadLabel();
			}

			@Override
			protected String getPageButtonLabel() {
				return getPaginatorPageButtonLabel();
			}

			@Override
			protected ArrayList<String> getPageSizeStrings() {
				return Settings.getStringList(SettingCodes.GROUP_VISIT_MATRIX_PAGE_SIZES, Bundle.SETTINGS, DefaultSettings.GROUP_VISIT_MATRIX_PAGE_SIZES);
			}

			@Override
			public boolean isPagesEnabled() {
				return this.psf.getRowCount() != null && this.psf.getRowCount() > 0l;
			}
		};
		paginator.setShowPagesMessage(false);
		paginator.setInputFieldForm(false);
		paginator.setShowNavigatorButtons(false);
	}

	public void change(Long trialId) {
		this.trialId = trialId;
		paginator.initPages(true, trialId);
	}

	private void clearMatrix() {
		matrixGroups.clear();
		matrixVisits.clear();
		matrixItemMap.clear();
	}

	protected abstract ProbandGroupOutVO getGroupFromItem(ITEM item);

	protected abstract Long getItemCount(Long trialId);

	protected abstract String getItemLabel(ITEM item);

	protected abstract Collection<ITEM> getItemsPage(Long trialId, PSFVO psf);

	public ProbandGroupOutVO[] getMatrixGroups() {
		ArrayList<ProbandGroupOutVO> groups = new ArrayList<ProbandGroupOutVO>(matrixGroups.values());
		Collections.sort(groups, GROUP_COMPARATOR);
		return groups.toArray(new ProbandGroupOutVO[0]);
	}

	public int getMatrixGroupsCount() {
		return matrixGroups.size();
	}

	public String getMatrixItemColor(VisitOutVO visit, ProbandGroupOutVO group, int visitIndex, int groupIndex) {
		ArrayList<ITEM> items = getMatrixItems(visit, group);
		if (items != null && items.size() > 0) {
			return WebUtil.colorToStyleClass(visit != null ? visit.getType().getColor()
					: Settings.getColor(
							SettingCodes.GROUP_VISIT_MATRIX_NO_VISIT_COLOR, Bundle.SETTINGS,
							DefaultSettings.GROUP_VISIT_MATRIX_NO_VISIT_COLOR));
		} else {
			if (paginator.getPsf().getPageSize() < paginator.getPsf().getRowCount()) {
				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.GROUP_VISIT_MATRIX_NOT_LOADED_COLOR, Bundle.SETTINGS,
						DefaultSettings.GROUP_VISIT_MATRIX_NOT_LOADED_COLOR));
			} else {
				return WebUtil.colorToStyleClass(Settings.getColor(SettingCodes.GROUP_VISIT_MATRIX_GAP_COLOR, Bundle.SETTINGS,
						DefaultSettings.GROUP_VISIT_MATRIX_GAP_COLOR));
			}
		}
	}

	public int getMatrixItemCount(VisitOutVO visit, ProbandGroupOutVO group) {
		try {
			return matrixItemMap.get(visit != null ? visit.getId() : null).get(group != null ? group.getId() : null).size();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	private ArrayList<ITEM> getMatrixItems(VisitOutVO visit, ProbandGroupOutVO group) {
		try {
			return matrixItemMap.get(visit != null ? visit.getId() : null).get(group != null ? group.getId() : null);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getMatrixItemsString(VisitOutVO visit, ProbandGroupOutVO group) {
		StringBuilder sb = new StringBuilder();
		ArrayList<ITEM> items = getMatrixItems(visit, group);
		if (items != null && items.size() > 0) {
			Iterator<ITEM> it = items.iterator();
			while (it.hasNext()) {
				String label = getItemLabel(it.next());
				if (!CommonUtil.isEmptyString(label)) {
					if (sb.length() > 0) {
						sb.append(MATRIX_ITEMS_SEPARATOR);
					}
					sb.append(label);
				}
			}
		}
		return sb.toString();
	}

	public VisitOutVO[] getMatrixVisits() {
		ArrayList<VisitOutVO> visits = new ArrayList<VisitOutVO>(matrixVisits.values());
		Collections.sort(visits, VISIT_COMPARATOR);
		return visits.toArray(new VisitOutVO[0]);
	}

	public int getMatrixVisitsCount() {
		return matrixVisits.size();
	}

	public Paginator getPaginator() {
		return paginator;
	}

	protected abstract String getPaginatorFirstPageButtonLabel();

	protected abstract String getPaginatorLoadLabel();

	protected abstract String getPaginatorPageButtonLabel();

	protected abstract VisitOutVO getVisitFromItem(ITEM item);

	public void handleFieldsPerRowChange() {
	}

	public void handleFirstPage() {
		paginator.setToFirstPage();
		handlePageChange();
	}

	public void handleLastPage() {
		paginator.setToLastPage();
		handlePageChange();
	}

	public void handleNextPage() {
		paginator.setToNextPage();
		handlePageChange();
	}

	public void handlePageChange() {
		paginator.updatePSF();
		loadMatrix();
	}

	public void handlePageSizeChange() {
		paginator.initPages(false, trialId);
		loadMatrix();
	}

	public void handlePrevPage() {
		paginator.setToPrevPage();
		handlePageChange();
	}

	public void initPages() {
		paginator.initPages(false, trialId);
	}

	public boolean isCreated() { // required by paginator
		return false;
	}

	public boolean isMatrixVisitsVertical() {
		return matrixVisitsVertical;
	}

	public void loadMatrix() {
		clearMatrix();
		Collection<ITEM> items = null;
		if (trialId != null) {
			items = getItemsPage(trialId, paginator.getPsf());
		}
		if (items != null) {
			Iterator<ITEM> it = items.iterator();
			while (it.hasNext()) {
				ITEM item = it.next();
				VisitOutVO visit = getVisitFromItem(item);
				ProbandGroupOutVO group = getGroupFromItem(item);
				Long visitId = (visit != null ? visit.getId() : null);
				Long groupId = (group != null ? group.getId() : null);
				if (!matrixVisits.containsKey(visitId)) {
					matrixVisits.put(visitId, visit);
				}
				if (!matrixGroups.containsKey(groupId)) {
					matrixGroups.put(groupId, group);
				}
				HashMap<Long, ArrayList<ITEM>> groupMap;
				if (!matrixItemMap.containsKey(visitId)) {
					groupMap = new HashMap<Long, ArrayList<ITEM>>();
					matrixItemMap.put(visitId, groupMap);
				} else {
					groupMap = matrixItemMap.get(visitId);
				}
				ArrayList<ITEM> itemList;
				if (!groupMap.containsKey(groupId)) {
					itemList = new ArrayList<ITEM>();
					groupMap.put(groupId, itemList);
				} else {
					itemList = groupMap.get(groupId);
				}
				itemList.add(item);
			}
		}
	}

	public void selectVisitGroup() {
		setVisitId(WebUtil.getLongParamValue(GetParamNames.VISIT_ID));
		setGroupId(WebUtil.getLongParamValue(GetParamNames.PROBAND_GROUP_ID));
	}

	protected abstract void setGroupId(Long groupId);

	public void setMatrixVisitsVertical(boolean matrixVisitsVertical) {
		this.matrixVisitsVertical = matrixVisitsVertical;
	}

	protected abstract void setVisitId(Long visitId);
}
