package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusChartEntryVO;
import org.phoenixctms.ctsms.web.model.CartesianChartModel;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DateUtil;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.ChartSeries;

public class DepartmentChartBean extends ManagedBeanBase {

	private static final Comparator<ProbandListStatusChartEntryVO> CHART_ENTRY_COMPARATOR = new Comparator<ProbandListStatusChartEntryVO>() {

		@Override
		public int compare(ProbandListStatusChartEntryVO a, ProbandListStatusChartEntryVO b) {
			int dateComparison = a.getRealTimestamp().compareTo(b.getRealTimestamp());
			if (dateComparison != 0) {
				return dateComparison;
			} else if (a.getId() < b.getId()) {
				return -1;
			} else if (a.getId() > b.getId()) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	private Long trialId;
	//private TrialOutVO trial;
	private CartesianChartModel chartModel;
	private List<Color> departmentColors;
	private ArrayList<Color> seriesColors;
	private Date minX;
	private Date maxX;
	private Integer maxY;
	private boolean stacked;

	public DepartmentChartBean() {
		super();
		chartModel = new CartesianChartModel();
		departmentColors = Settings.getColorList(SettingCodes.DEPARTMENT_CHART_DEPARTMENT_COLORS, Bundle.SETTINGS, DefaultSettings.DEPARTMENT_CHART_DEPARTMENT_COLORS);
		seriesColors = new ArrayList<Color>();
	}

	@Override
	protected String changeAction(Long id) {
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	private String dateToString(Date date) {
		return WebUtil.quoteJSString(DateUtil.getJqPlotJsDateFormat().format(date), true);
	}

	public CartesianChartModel getChartModel() {
		if (isChartEmpty()) {
			CartesianChartModel emptyChartModel = new CartesianChartModel();
			ChartSeries emptyChartSeries = new ChartSeries();
			emptyChartSeries.setLabel(Messages.getString(MessageCodes.EMPTY_CHART_LABEL));
			emptyChartSeries.set(dateToString(new Date()), 0);
			emptyChartModel.addSeries(emptyChartSeries);
			return emptyChartModel;
		}
		return chartModel;
	}

	public boolean getEnableExports() {
		return Settings.getBoolean(SettingCodes.ENABLE_PROBAND_LIST_EXPORTS, Bundle.SETTINGS, DefaultSettings.ENABLE_PROBAND_LIST_EXPORTS);
	}

	public StreamedContent getEnrollmentLogExcelStreamedContent() throws Exception {
		try {
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, null, ProbandListStatusLogLevel.ENROLLMENT);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public long getMaxX() {
		return (maxX != null ? maxX : WebUtil.subIntervals(new Date(), VariablePeriod.MONTH, null, 1)).getTime();
	}

	public Integer getMaxY() {
		return maxY;
	}

	public long getMinX() {
		return (minX != null ? minX : new Date()).getTime();
	}

	public StreamedContent getPreScreeningLogExcelStreamedContent() throws Exception {
		try {
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService()
					.exportProbandList(WebUtil.getAuthentication(), trialId, null, ProbandListStatusLogLevel.PRE_SCREENING);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getScreeningLogExcelStreamedContent() throws Exception {
		try {
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, null, ProbandListStatusLogLevel.SCREENING);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public String getSeriesColors() {
		return WebUtil.getSeriesColors(seriesColors);
	}

	public StreamedContent getSiclExcelStreamedContent() throws Exception {
		try {
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, null, ProbandListStatusLogLevel.SICL);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	public StreamedContent getProbandStatusExcelStreamedContent() throws Exception {
		try {
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, null,
					ProbandListStatusLogLevel.PROBAND_STATUS);
			return new DefaultStreamedContent(new ByteArrayInputStream(excel.getDocumentDatas()), excel.getContentType().getMimeType(), excel.getFileName());
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
			throw e;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			throw e;
		}
	}

	private void initIn() {
		stacked = true;
	}

	private void initSets() {
		chartModel.clear();
		seriesColors.clear();
		minX = null;
		maxX = null;
		maxY = null;
		//trial = WebUtil.getTrial(trialId);
		Collection<DepartmentVO> departments = null;
		try {
			departments = WebUtil.getServiceLocator().getSelectionSetService().getAllDepartments(WebUtil.getAuthentication());
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		HashMap<Long, DepartmentVO> departmentMap;
		if (departments != null) {
			departmentMap = new HashMap<Long, DepartmentVO>(departments.size());
			Iterator<Color> departmentColorsIt = CommonUtil.getCircularIterator(departmentColors);
			Iterator<DepartmentVO> departmentsIt = departments.iterator();
			while (departmentsIt.hasNext()) {
				DepartmentVO department = departmentsIt.next();
				departmentMap.put(department.getId(), department);
				seriesColors.add(departmentColorsIt.next());
			}
		} else {
			departmentMap = new HashMap<Long, DepartmentVO>();
		}
		ArrayList<ProbandListStatusChartEntryVO> initialProbandListStatus = new ArrayList<ProbandListStatusChartEntryVO>();
		if (trialId != null) {
			try {
				Collection<ProbandListStatusChartEntryVO> statusEntries = WebUtil.getServiceLocator().getTrialService()
						.getProbandListStatusChartEntries(WebUtil.getAuthentication(), trialId, Boolean.TRUE);
				if (statusEntries != null) {
					initialProbandListStatus.addAll(statusEntries);
				}
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		Collections.sort(initialProbandListStatus, CHART_ENTRY_COMPARATOR);
		HashMap<Long, TreeMap<Date, Number>> seriesMap = new HashMap<Long, TreeMap<Date, Number>>();
		LinkedHashMap<Long, ProbandListStatusChartEntryVO> probandMap = new LinkedHashMap<Long, ProbandListStatusChartEntryVO>();
		Iterator<ProbandListStatusChartEntryVO> probandListStatusIt = initialProbandListStatus.iterator();
		while (probandListStatusIt.hasNext()) {
			ProbandListStatusChartEntryVO probandListStatusEntry = probandListStatusIt.next();
			Long probandId = probandListStatusEntry.getProbandId();
			if (!probandMap.containsKey(probandId)) {
				probandMap.put(probandId, probandListStatusEntry);
			}
		}
		maxY = probandMap.size();
		TreeSet<Date> dates = new TreeSet<Date>();
		probandListStatusIt = probandMap.values().iterator();
		while (probandListStatusIt.hasNext()) {
			ProbandListStatusChartEntryVO probandListStatusEntry = probandListStatusIt.next();
			Long departmentId = probandListStatusEntry.getDepartmentId();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(probandListStatusEntry.getRealTimestamp());
			Date date = (new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)).getTime();
			dates.add(date);
			TreeMap<Date, Number> series;
			if (seriesMap.containsKey(departmentId)) {
				series = seriesMap.get(departmentId);
			} else {
				series = new TreeMap<Date, Number>();
				seriesMap.put(departmentId, series);
			}
			if (series.containsKey(date)) {
				series.put(date, series.get(date).intValue() + 1);
			} else {
				if (series.size() > 0) {
					series.put(date, series.lastEntry().getValue().intValue() + 1);
				} else {
					series.put(date, 1);
				}
			}
		}
		if (dates.size() > 0) {
			minX = dates.first();
			maxX = dates.last();
		}
		Iterator<Long> seriesMapIt = seriesMap.keySet().iterator();
		while (seriesMapIt.hasNext()) {
			Long departmentId = seriesMapIt.next();
			DepartmentVO department = departmentMap.get(departmentId);
			TreeMap<Date, Number> series = seriesMap.get(departmentId);
			if (department != null && series.size() > 0) {
				ChartSeries chartLine = new ChartSeries();
				chartLine.setLabel(department.getName());
				Number value = Integer.valueOf(0);
				Iterator<Date> datesIt = dates.iterator();
				while (datesIt.hasNext()) {
					Date date = datesIt.next();
					if (series.containsKey(date)) {
						value = series.get(date);
					}
					chartLine.set(dateToString(date), value);
				}
				chartModel.addSeries(chartLine);
			}
		}
	}

	public boolean isChartEmpty() {
		return chartModel.getSeries().isEmpty();
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public boolean isStacked() {
		return stacked;
	}

	public void setStacked(boolean stacked) {
		this.stacked = stacked;
	}

	public void updateChart() {
		initIn();
		initSets();
	}
}
