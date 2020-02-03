package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.phoenixctms.ctsms.compare.ProbandListStatusEntryOutVOComparator;
import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
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

public class EnrollmentChartBean extends ManagedBeanBase {

	private Long trialId;
	private TrialOutVO trial;
	private CartesianChartModel chartModel;
	private ArrayList<Color> seriesColors;
	private Date minX;
	private Date maxX;
	private Integer maxY;
	private boolean stacked;

	public EnrollmentChartBean() {
		super();
		chartModel = new CartesianChartModel();
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
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, ProbandListStatusLogLevel.ENROLLMENT);
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
					.exportProbandList(WebUtil.getAuthentication(), trialId, ProbandListStatusLogLevel.PRE_SCREENING);
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
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, ProbandListStatusLogLevel.SCREENING);
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
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId, ProbandListStatusLogLevel.SICL);
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
		trial = WebUtil.getTrial(trialId);
		Collection<ProbandListStatusTypeVO> statusTypes = null;
		try {
			statusTypes = WebUtil.getServiceLocator().getSelectionSetService()
					.getAllProbandListStatusTypes(WebUtil.getAuthentication(), trial != null ? trial.getType().getPerson() : null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		HashMap<Long, ProbandListStatusTypeVO> statusTypeMap;
		if (statusTypes != null) {
			statusTypeMap = new HashMap<Long, ProbandListStatusTypeVO>(statusTypes.size());
			Iterator<ProbandListStatusTypeVO> statusTypeIt = statusTypes.iterator();
			while (statusTypeIt.hasNext()) {
				ProbandListStatusTypeVO statusType = statusTypeIt.next();
				statusTypeMap.put(statusType.getId(), statusType);
			}
		} else {
			statusTypeMap = new HashMap<Long, ProbandListStatusTypeVO>();
		}
		Collection<ProbandListStatusEntryOutVO> probandListStatus = null;
		if (trialId != null) {
			try {
				probandListStatus = WebUtil.getServiceLocator().getTrialService().getProbandListStatus(WebUtil.getAuthentication(), trialId, null, false, null);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		if (probandListStatus == null) {
			probandListStatus = new ArrayList<ProbandListStatusEntryOutVO>();
		} else {
			probandListStatus = new ArrayList<ProbandListStatusEntryOutVO>(probandListStatus);
			Collections.sort((ArrayList<ProbandListStatusEntryOutVO>) probandListStatus, new ProbandListStatusEntryOutVOComparator());
		}
		HashMap<Long, TreeMap<Date, Number>> seriesMap = new HashMap<Long, TreeMap<Date, Number>>();
		HashMap<Long, TreeMap<Long, ProbandListStatusEntryOutVO>> probandMap = new HashMap<Long, TreeMap<Long, ProbandListStatusEntryOutVO>>();
		Iterator<ProbandListStatusEntryOutVO> probandListStatusIt = probandListStatus.iterator();
		while (probandListStatusIt.hasNext()) {
			ProbandListStatusEntryOutVO probandListStatusEntry = probandListStatusIt.next();
			Long probandId = probandListStatusEntry.getListEntry().getProband().getId();
			TreeMap<Long, ProbandListStatusEntryOutVO> enrollmentStatusMap;
			if (probandMap.containsKey(probandId)) {
				enrollmentStatusMap = probandMap.get(probandId);
			} else {
				enrollmentStatusMap = new TreeMap<Long, ProbandListStatusEntryOutVO>();
				probandMap.put(probandId, enrollmentStatusMap);
			}
			enrollmentStatusMap.put(probandListStatusEntry.getId(), probandListStatusEntry);
		}
		maxY = probandMap.size();
		TreeSet<Date> dates = new TreeSet<Date>();
		probandListStatusIt = probandListStatus.iterator();
		while (probandListStatusIt.hasNext()) {
			ProbandListStatusEntryOutVO probandListStatusEntry = probandListStatusIt.next();
			Long statusId = probandListStatusEntry.getStatus().getId();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(probandListStatusEntry.getRealTimestamp());
			Date date = (new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)).getTime();
			dates.add(date);
			TreeMap<Long, ProbandListStatusEntryOutVO> enrollmentStatusMap = probandMap.get(probandListStatusEntry.getListEntry().getProband().getId());
			Entry<Long, ProbandListStatusEntryOutVO> previousStatus = enrollmentStatusMap.lowerEntry(probandListStatusEntry.getId());
			TreeMap<Date, Number> series;
			if (seriesMap.containsKey(statusId)) {
				series = seriesMap.get(statusId);
			} else {
				series = new TreeMap<Date, Number>();
				seriesMap.put(statusId, series);
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
			if (previousStatus != null) {
				series = seriesMap.get(previousStatus.getValue().getStatus().getId());
				series.put(date, series.lastEntry().getValue().intValue() - 1);
			}
		}
		if (dates.size() > 0) {
			minX = dates.first();
			maxX = dates.last();
		}
		Iterator<Long> seriesMapIt = seriesMap.keySet().iterator();
		while (seriesMapIt.hasNext()) {
			Long statusId = seriesMapIt.next();
			ProbandListStatusTypeVO statusType = statusTypeMap.get(statusId);
			TreeMap<Date, Number> series = seriesMap.get(statusId);
			if (statusType != null && series.size() > 0) {
				ChartSeries chartLine = new ChartSeries();
				chartLine.setLabel(statusType.getName());
				Iterator<Date> datesIt = dates.iterator();
				while (datesIt.hasNext()) {
					Date date = datesIt.next();
					if (series.containsKey(date)) {
						chartLine.set(dateToString(date), series.get(date));
					} else {
						Entry<Date, Number> previousValue = series.lowerEntry(date);
						chartLine.set(dateToString(date), previousValue == null ? 0 : previousValue.getValue());
					}
				}
				chartModel.addSeries(chartLine);
				seriesColors.add(statusType.getColor());
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
