package org.phoenixctms.ctsms.web.model.proband;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.AspSubstanceVO;
import org.phoenixctms.ctsms.vo.AspVO;
import org.phoenixctms.ctsms.vo.DiagnosisOutVO;
import org.phoenixctms.ctsms.vo.MedicationInVO;
import org.phoenixctms.ctsms.vo.MedicationOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProcedureOutVO;
import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.IDVOList;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
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

@ManagedBean
@ViewScoped
public class MedicationBean extends ManagedBeanBase {

	public static void copyMedicationOutToIn(MedicationInVO in, MedicationOutVO out) {
		if (in != null && out != null) {
			ProbandOutVO probandVO = out.getProband();
			DiagnosisOutVO diagnosisVO = out.getDiagnosis();
			ProcedureOutVO procedureVO = out.getProcedure();
			AspVO asp = out.getAsp();
			in.setComment(out.getComment());
			in.setDoseUnit(out.getDoseUnit());
			in.setDoseValue(out.getDoseValue());
			in.setDiagnosisId(diagnosisVO == null ? null : diagnosisVO.getId());
			in.setId(out.getId());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setProcedureId(procedureVO == null ? null : procedureVO.getId());
			in.setStart(out.getStart());
			in.setStop(out.getStop());
			in.setAspId(asp == null ? null : asp.getId());
			in.getSubstanceIds().clear();
			Iterator<AspSubstanceVO> it = out.getSubstances().iterator();
			while (it.hasNext()) {
				in.getSubstanceIds().add(it.next().getId());
			}
			in.setVersion(out.getVersion());
		}
	}

	public static void initMedicationDefaultValues(MedicationInVO in, Long probandId) {
		if (in != null) {
			in.setComment(Messages.getString(MessageCodes.MEDICATION_COMMENT_PRESET));
			in.setDoseUnit(Messages.getString(MessageCodes.MEDICATION_DOSE_UNIT_PRESET));
			in.setDoseValue(Settings.getFloatNullable(SettingCodes.MEDICATION_DOSE_VALUE_PRESET, Bundle.SETTINGS, DefaultSettings.MEDICATION_DOSE_VALUE_PRESET));
			in.setDiagnosisId(null);
			in.setId(null);
			in.setProbandId(probandId);
			in.setProcedureId(null);
			in.setStart(null);
			in.setStop(null);
			in.setAspId(null);
			in.getSubstanceIds().clear();
			in.setVersion(null);
		}
	}

	private MedicationInVO in;
	private MedicationOutVO out;
	private Long probandId;
	private ProbandOutVO proband;
	private ArrayList<SelectItem> diagnoses;
	private ArrayList<SelectItem> filterDiagnoses;
	private ArrayList<SelectItem> procedures;
	private ArrayList<SelectItem> filterProcedures;
	private MedicationLazyModel medicationModel;
	private AspVO asp;
	private List<AspSubstanceVO> substances;

	public MedicationBean() {
		super();
		medicationModel = new MedicationLazyModel();
		substances = new ArrayList<AspSubstanceVO>();
	}

	@Override
	public String addAction() {
		MedicationInVO backup = new MedicationInVO(in);
		in.setId(null);
		in.setVersion(null);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().addMedication(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
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
		RequestContext requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_MEDICATION_TAB_TITLE_BASE64, JSValues.AJAX_MEDICATION_COUNT,
				MessageCodes.MEDICATIONS_TAB_TITLE, MessageCodes.MEDICATIONS_TAB_TITLE_WITH_COUNT, new Long(medicationModel.getRowCount()));
		if (operationSuccess && in.getProbandId() != null) {
			WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
					MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
					WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
		}
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("medication_list");
		out = null;
		this.probandId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public List<IDVO> completeAsp(String query) {
		try {
			Collection aspVOs = WebUtil.getServiceLocator().getToolsService().completeAsp(WebUtil.getAuthentication(), query, null);
			IDVO.transformVoCollection(aspVOs);
			return (List<IDVO>) aspVOs;
		} catch (ClassCastException e) {
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<IDVO>();
	}

	public List<String> completeDoseUnit(String query) {
		this.in.setDoseUnit(query);
		Collection<String> units = null;
		try {
			units = WebUtil.getServiceLocator().getToolsService().completeMedicationDoseUnit(WebUtil.getAuthentication(), query, null);
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		if (units != null) {
			try {
				return ((List<String>) units);
			} catch (ClassCastException e) {
			}
		}
		return new ArrayList<String>();
	}

	public List<IDVO> completeSubstance(String query) {
		try {
			Collection aspSubstanceVOs = WebUtil.getServiceLocator().getToolsService().completeAspSubstance(WebUtil.getAuthentication(), query, null);
			IDVO.transformVoCollection(aspSubstanceVOs);
			return (List<IDVO>) aspSubstanceVOs;
		} catch (ClassCastException e) {
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		return new ArrayList<IDVO>();
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getProbandService().deleteMedication(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
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

	public IDVO getAsp() {
		if (asp != null) {
			return IDVO.transformVo(asp);
		}
		return null;
	}

	public ArrayList<SelectItem> getDiagnoses() {
		return diagnoses;
	}

	public ArrayList<SelectItem> getFilterDiagnoses() {
		return filterDiagnoses;
	}

	public ArrayList<SelectItem> getFilterProcedures() {
		return filterProcedures;
	}

	public MedicationInVO getIn() {
		return in;
	}

	public MedicationLazyModel getMedicationModel() {
		return medicationModel;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public MedicationOutVO getOut() {
		return out;
	}

	public ArrayList<SelectItem> getProcedures() {
		return procedures;
	}

	public IDVO getSelectedMedication() {
		if (this.out != null) {
			return IDVO.transformVo(this.out);
		} else {
			return null;
		}
	}

	public List<IDVO> getSubstances() {
		return new IDVOList(substances);
	}

	public String getSubstancesLabel(MedicationOutVO medication) {
		if (medication != null) {
			return CommonUtil.aspSubstanceVOCollectionToString(medication.getSubstances());
		}
		return "";
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.MEDICATION_TITLE, Long.toString(out.getId()), out.getName(),
					DateUtil.getDateStartStopString(out.getStart(), out.getStop()));
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_MEDICATION);
		}
	}

	public void handleAspSelect(SelectEvent event) {
		substances.clear();
		substances.addAll(this.asp.getSubstances());
	}

	public void handleAspUnselect(UnselectEvent event) {
	}

	public void handleDoseUnitSelect(SelectEvent event) {
		in.setDoseUnit((String) event.getObject());
	}

	public void handleSubstanceSelect(SelectEvent event) {
		this.asp = null;
	}

	public void handleSubstanceUnselect(UnselectEvent event) {
		this.asp = null;
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.MEDICATION_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new MedicationInVO();
		}
		if (out != null) {
			copyMedicationOutToIn(in, out);
			probandId = in.getProbandId();
		} else {
			initMedicationDefaultValues(in, probandId);
		}
	}

	private void initSets() {
		medicationModel.setProbandId(in.getProbandId());
		medicationModel.updateRowCount();
		loadAsp();
		loadSubstances();
		diagnoses = WebUtil.getDiagnoses(this.in.getProbandId());
		filterDiagnoses = new ArrayList<SelectItem>(diagnoses);
		filterDiagnoses.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		procedures = WebUtil.getProcedures(this.in.getProbandId());
		filterProcedures = new ArrayList<SelectItem>(procedures);
		filterProcedures.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		proband = WebUtil.getProband(this.in.getProbandId(), null, null, null);
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
			out = WebUtil.getServiceLocator().getProbandService().getMedication(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_MEDICATION, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
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

	private void loadAsp() {
		asp = WebUtil.getAsp(in.getAspId());
	}

	private void loadSubstances() {
		substances.clear();
		Iterator<Long> it = in.getSubstanceIds().iterator();
		while (it.hasNext()) {
			AspSubstanceVO substance = WebUtil.getAspSubstance(it.next());
			if (substance != null) {
				substances.add(substance);
			}
		}
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	private void sanitizeInVals() {
		if (asp != null) {
			in.setAspId(asp.getId());
		} else {
			in.setAspId(null);
		}
		in.getSubstanceIds().clear();
		Iterator<AspSubstanceVO> it = substances.iterator();
		while (it.hasNext()) {
			AspSubstanceVO substance = it.next();
			if (substance != null) {
				in.getSubstanceIds().add(substance.getId());
			}
		}
		if (in.getDoseValue() == null) {
			in.setDoseUnit(null);
		}
	}

	public void setAsp(IDVO asp) {
		if (asp != null) {
			this.asp = (AspVO) asp.getVo();
		} else {
			this.asp = null;
		}
	}

	public void setSelectedMedication(IDVO medication) {
		if (medication != null) {
			this.out = (MedicationOutVO) medication.getVo();
			this.initIn();
			initSets();
		}
	}

	public void setSubstances(List<IDVO> substances) {
		if (substances != null) {
			ArrayList<AspSubstanceVO> substancesCopy = new ArrayList<AspSubstanceVO>(substances.size());
			Iterator<IDVO> it = substances.iterator();
			while (it.hasNext()) {
				IDVO idVo = it.next();
				if (idVo != null) {
					substancesCopy.add((AspSubstanceVO) idVo.getVo());
				}
			}
			this.substances.clear();
			this.substances.addAll(substancesCopy);
		} else {
			this.substances.clear();
		}
	}

	@Override
	public String updateAction() {
		MedicationInVO backup = new MedicationInVO(in);
		sanitizeInVals();
		try {
			out = WebUtil.getServiceLocator().getProbandService().updateMedication(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
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
}
