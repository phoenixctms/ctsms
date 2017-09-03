package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

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

	private LinkedHashMap<Long, ProbandGroupOutVO> matrixGroups;
	private LinkedHashMap<Long, VisitOutVO> matrixVisits;
	private HashMap<Long, HashMap<Long, ArrayList<ITEM>>> matrixItemMap;
	private Paginator paginator;

	private boolean matrixVisitsVertical;

	public GroupVisitMatrix() {
		matrixVisitsVertical = Settings.getBoolean(SettingCodes.GROUP_VISIT_MATRIX_VISITS_VERTICAL_DEFAULT, Bundle.SETTINGS, DefaultSettings.GROUP_VISIT_MATRIX_VISITS_VERTICAL_DEFAULT);
		matrixGroups = new LinkedHashMap<Long, ProbandGroupOutVO>();
		matrixVisits = new LinkedHashMap<Long, VisitOutVO>();
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
				// return this.getPages().size() > 0;
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
		// paginator.setPagesEnabled(paginator.getPages().size() > 0);
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
		return matrixGroups.values().toArray(new ProbandGroupOutVO[0]);
	}

	public int getMatrixGroupsCount() {
		return matrixGroups.size();
	}

	public String getMatrixItemColor(VisitOutVO visit, ProbandGroupOutVO group, int visitIndex, int groupIndex) {
		ArrayList<ITEM> items = getMatrixItems(visit, group);
		if (items != null && items.size() > 0) {
			return WebUtil.colorToStyleClass(visit != null ? visit.getType().getColor() : Settings.getColor(
					SettingCodes.GROUP_VISIT_MATRIX_NO_VISIT_COLOR, Bundle.SETTINGS,
					DefaultSettings.GROUP_VISIT_MATRIX_NO_VISIT_COLOR));
		} else {
			// if (paginator.getItemsOnPage() < getMatrixItemsSum(visitIndex,groupIndex)) {
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

	// private long getMatrixItemsSum(int visitIndex, int groupIndex) {
	// VisitOutVO[] visits = getMatrixVisits();
	// ProbandGroupOutVO[] groups = getMatrixGroups();
	// long count = 0l;
	// if (visits != null && groups != null) {
	// for (int i = 0; i < groups.length; i++) {
	// for (int j = 0; j < visits.length; j++) {
	// if (i < groupIndex) {
	// count += getMatrixItemCount(visits[j], groups[i]);
	// } else {
	// if (j <= visitIndex) {
	// count += getMatrixItemCount(visits[j], groups[i]);
	// } else {
	// return count;
	// }
	// }
	//
	// }
	// }
	// }
	// return count;
	// }

	public VisitOutVO[] getMatrixVisits() {
		return matrixVisits.values().toArray(new VisitOutVO[0]);
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

	// private void loadMatrix() {
	// clearMatrix();
	//
	// HashMap<String,Object> passThrough = new HashMap<String, Object>();
	// passThrough.put("itemCount", 0l);
	// passThrough.put("limit", false);
	// passThrough.put("error", false);
	// PSFVO psf = paginator.getPsfCopy();
	// psf.setSortField(WebUtil.VISIT_TOKEN_PSF_PROPERTY_NAME);
	// psf.setSortOrder(true);
	// Collection<VisitOutVO> visitVOs = null;
	// if (trialId != null) {
	// try {
	// visitVOs = WebUtil.getServiceLocator().getTrialService().getVisitList(WebUtil.getAuthentication(), trialId, psf);
	// } catch (ServiceException e) {
	// } catch (AuthenticationException e) {
	// WebUtil.publishException(e);
	// } catch (AuthorisationException e) {
	// } catch (IllegalArgumentException e) {
	// }
	// }
	// if (visitVOs != null && visitVOs.size() > 0) {
	// Iterator<VisitOutVO> visitIt = visitVOs.iterator();
	// while (visitIt.hasNext() && !((Boolean)passThrough.get("error")) && !((Boolean)passThrough.get("limit"))) {
	// loadMatrixChunked(passThrough, visitIt.next());
	// }
	// } else {
	// loadMatrixChunked(passThrough, null);
	// }
	//
	// }
	//
	// private void loadMatrixChunked( HashMap<String,Object> passThrough, VisitOutVO visitVO) {
	// PSFVO psf = new PSFVO();
	// psf.setUpdateRowCount(true);
	// psf.setFirst(0);
	// psf.setPageSize(2);
	// psf.setSortField(WebUtil.VISIT_SCHEDULE_ITEM_GROUP_TOKEN_PSF_PROPERTY_NAME);
	// psf.setSortOrder(true);
	// Long visitLimit = Settings.getLongNullable(SettingCodes.VISIT_SCHEDULE_ITEM_MATRIX_VISIT_LIMIT, Bundle.SETTINGS, DefaultSettings.VISIT_SCHEDULE_ITEM_MATRIX_VISIT_LIMIT);
	// Long groupLimit = Settings.getLongNullable(SettingCodes.VISIT_SCHEDULE_ITEM_MATRIX_GROUP_LIMIT, Bundle.SETTINGS, DefaultSettings.VISIT_SCHEDULE_ITEM_MATRIX_GROUP_LIMIT);
	// Long itemLimit = Settings.getLongNullable(SettingCodes.VISIT_SCHEDULE_ITEM_MATRIX_ITEM_LIMIT, Bundle.SETTINGS, DefaultSettings.VISIT_SCHEDULE_ITEM_MATRIX_ITEM_LIMIT);
	// long itemCount = (Long) passThrough.get("itemCount");
	// boolean limit = (Boolean) passThrough.get("limit");
	// boolean error = (Boolean) passThrough.get("error");
	// while (!limit && !error && (psf.getRowCount() == null || psf.getRowCount() > itemCount)) {
	// Collection<VisitScheduleItemOutVO> visitScheduleItemsChunk = null;
	// error = true;
	// if (trialId != null) {
	// try {
	// visitScheduleItemsChunk = WebUtil.getServiceLocator().getTrialService().getVisitScheduleItemList(WebUtil.getAuthentication(), trialId, null, (visitVO != null ?
	// visitVO.getId() : null) , null, psf);
	// error = false;
	// psf.setUpdateRowCount(false);
	// psf.setFirst(psf.getFirst() + psf.getPageSize());
	// } catch (ServiceException e) {
	// } catch (AuthenticationException e) {
	// WebUtil.publishException(e);
	// } catch (AuthorisationException e) {
	// } catch (IllegalArgumentException e) {
	// }
	// }
	// if (visitScheduleItemsChunk != null) {
	// Iterator<VisitScheduleItemOutVO> it = visitScheduleItemsChunk.iterator();
	// while (it.hasNext()) {
	// VisitScheduleItemOutVO item = it.next();
	// Long visitId = (item.getVisit() != null ? item.getVisit().getId() : null);
	// Long groupId = (item.getGroup() != null ? item.getGroup().getId() : null);
	// if (itemLimit == null || itemCount < itemLimit) {
	// if (!matrixVisits.containsKey(visitId)) {
	// if (visitLimit == null || matrixVisits.keySet().size() < visitLimit) {
	// matrixVisits.put(visitId, item.getVisit());
	// } else {
	// limit = true;
	// break;
	// }
	// }
	// if (!matrixGroups.containsKey(groupId)) {
	// if (groupLimit == null || matrixGroups.keySet().size() < groupLimit) {
	// matrixGroups.put(groupId, item.getGroup());
	// } else {
	// limit = true;
	// break;
	// }
	// }
	// LinkedHashMap<Long, ArrayList<VisitScheduleItemOutVO>> groupMap;
	// if (!matrixItemMap.containsKey(visitId)) {
	// groupMap = new LinkedHashMap<Long,ArrayList<VisitScheduleItemOutVO>>();
	// matrixItemMap.put(visitId, groupMap);
	// } else {
	// groupMap = matrixItemMap.get(visitId);
	// }
	// ArrayList<VisitScheduleItemOutVO> itemList;
	// if (!groupMap.containsKey(groupId)) {
	// itemList = new ArrayList<VisitScheduleItemOutVO>();
	// groupMap.put(groupId, itemList);
	// } else {
	// itemList = groupMap.get(groupId);
	// }
	// itemList.add(item);
	// itemCount++;
	// } else {
	// limit = true;
	// break;
	// }
	// }
	// }
	// }
	// passThrough.put("itemCount", itemCount);
	// passThrough.put("limit", limit);
	// passThrough.put("error", error);
	// //return limit || error;
	// }

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
		// PSFVO psf = paginator.getPsfCopy();
		// psf.setSortField(WebUtil.VISIT_SCHEDULE_ITEM_VISIT_TOKEN_PSF_PROPERTY_NAME);
		// psf.setSortOrder(true);
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
