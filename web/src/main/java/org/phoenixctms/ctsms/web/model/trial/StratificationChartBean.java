package org.phoenixctms.ctsms.web.model.trial;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.StratificationPermutationVO;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.DonutChartModel;

public class StratificationChartBean extends ManagedBeanBase {

	private Long trialId;
	//private TrialOutVO trial;
	private DonutChartModel chartModel;
	private List<Color> selectionValueColors;
	private ArrayList<Color> seriesColors;

	public StratificationChartBean() {
		super();
		chartModel = new DonutChartModel();
		selectionValueColors = Settings.getColorList(SettingCodes.STRATIFICATION_CHART_SELECTION_VALUE_COLORS, Bundle.SETTINGS,
				DefaultSettings.STRATIFICATION_CHART_SELECTION_VALUE_COLORS);
		seriesColors = new ArrayList<Color>();
	}

	@Override
	protected String changeAction(Long id) {
		this.trialId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public DonutChartModel getChartModel() {
		if (isChartEmpty()) {
			DonutChartModel emptyChartModel = new DonutChartModel();
			Map<String, Number> circle = new LinkedHashMap<String, Number>();
			circle.put(Messages.getString(MessageCodes.EMPTY_CHART_LABEL), 0);
			emptyChartModel.addCircle(circle);
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

	public StreamedContent getProbandStatusExcelStreamedContent() throws Exception {
		try {
			ProbandListExcelVO excel = WebUtil.getServiceLocator().getTrialService().exportProbandList(WebUtil.getAuthentication(), trialId,
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
	}

	private void initSets() {
		chartModel.clear();
		seriesColors.clear();
		Collection<StratificationPermutationVO> permutations = null;
		try {
			permutations = WebUtil.getServiceLocator().getTrialService().getStratificationPermutations(WebUtil.getAuthentication(), trialId);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (permutations != null && !permutations.isEmpty()) {
			Iterator<Color> colorIt = CommonUtil.getCircularIterator(selectionValueColors);
			int depth = permutations.iterator().next().getValues().size();
			// REVERSED LOOP: depth-1 is Outermost (per your jqPlot configuration)
			for (int i = depth - 1; i >= 0; i--) {
				Map<String, Number> circle = new LinkedHashMap<String, Number>();
				Iterator<StratificationPermutationVO> it = permutations.iterator();
				while (it.hasNext()) {
					StratificationPermutationVO p = it.next();
					List<InputFieldSelectionSetValueOutVO> values = (List<InputFieldSelectionSetValueOutVO>) p.getValues();
					// Construct hierarchical label to ensure slices split correctly
					StringBuilder hierarchyLabel = new StringBuilder();
					for (int j = 0; j <= i; j++) {
						hierarchyLabel.append(values.get(j).getName());
						if (j < i) {
							hierarchyLabel.append(" | ");
						}
					}
					String key = hierarchyLabel.toString();
					Number currentCount = circle.get(key);
					// Only add a color if this is a NEW slice in this specific ring
					if (!circle.containsKey(key)) {
						if (colorIt.hasNext()) {
							seriesColors.add(colorIt.next());
						}
					}
					circle.put(key, (currentCount == null ? 0L : currentCount.longValue()) + p.getCount());
				}
				chartModel.addCircle(circle);
			}
		}
	}

	public int getLegendCols(int maxRows) {
		if (maxRows <= 0) {
			return 1;
		}
		// Step 1: Calculate the total number of unique legend items
		// Since initSets() uses hierarchical labels (e.g., "Graz | Male"), 
		// we count every entry in every circle added to the model.
		int totalItems = 0;
		List<Map<String, Number>> data = chartModel.getData();
		if (data != null) {
			// We use a Set to ensure we only count unique labels across all rings
			Set<String> uniqueLabels = new HashSet<String>();
			Iterator<Map<String, Number>> ringsIt = data.iterator();
			while (ringsIt.hasNext()) {
				Map<String, Number> ring = ringsIt.next();
				if (ring != null) {
					uniqueLabels.addAll(ring.keySet());
				}
			}
			totalItems = uniqueLabels.size();
		}
		if (totalItems == 0) {
			return 1;
		}
		// Step 2: Calculate columns needed to stay under maxRows
		// Formula: ceil(totalItems / maxRows)
		int cols = (int) Math.ceil((double) totalItems / maxRows);
		return Math.max(1, cols);
	}

	public boolean isChartEmpty() {
		return chartModel.getData().isEmpty();
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public void updateChart() {
		initIn();
		initSets();
	}
}
