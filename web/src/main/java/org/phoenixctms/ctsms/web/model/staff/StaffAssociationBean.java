package org.phoenixctms.ctsms.web.model.staff;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.web.component.datatable.DataTable;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel;
import org.phoenixctms.ctsms.web.model.ShiftDurationSummaryModel.ShiftDurationSummaryType;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class StaffAssociationBean extends ManagedBeanBase {

	private Date now;
	private Long staffId;
	private AccountLazyModel accountModel;
	private OwnershipInventoryLazyModel ownershipInventoryModel;
	private OnBehalfOfInventoryBookingLazyModel onBehalfOfInventoryBookingModel;
	private ResponsiblePersonMaintenanceLazyModel responsiblePersonMaintenanceModel;
	private CompanyContactMaintenanceLazyModel companyContactMaintenanceModel;
	private OriginatorInventoryStatusLazyModel originatorInventoryStatusModel;
	private AddresseeInventoryStatusLazyModel addresseeInventoryStatusModel;
	private InstitutionCourseLazyModel institutionCourseModel;
	private InstitutionCvPositionLazyModel institutionCvPositionModel;
	private LectureLazyModel lectureModel;
	private TrialMembershipLazyModel trialMembershipModel;
	private ShiftDurationSummaryModel shiftDurationModel;
	private PatientLazyModel patientModel;
	private ArrayList<SelectItem> filterCalendars;

	public StaffAssociationBean() {
		super();
		now = new Date();
		accountModel = new AccountLazyModel();
		ownershipInventoryModel = new OwnershipInventoryLazyModel();
		onBehalfOfInventoryBookingModel = new OnBehalfOfInventoryBookingLazyModel();
		responsiblePersonMaintenanceModel = new ResponsiblePersonMaintenanceLazyModel();
		companyContactMaintenanceModel = new CompanyContactMaintenanceLazyModel();
		originatorInventoryStatusModel = new OriginatorInventoryStatusLazyModel();
		addresseeInventoryStatusModel = new AddresseeInventoryStatusLazyModel();
		institutionCourseModel = new InstitutionCourseLazyModel();
		patientModel = new PatientLazyModel();
		institutionCvPositionModel = new InstitutionCvPositionLazyModel();
		lectureModel = new LectureLazyModel();
		trialMembershipModel = new TrialMembershipLazyModel();
		shiftDurationModel = new ShiftDurationSummaryModel(ShiftDurationSummaryType.STAFF);
	}

	@Override
	protected String changeAction(Long id) {
		DataTable.clearFilters("account_list");
		DataTable.clearFilters("ownershipinventory_list");
		DataTable.clearFilters("inventorybooking_list");
		DataTable.clearFilters("responsiblepersonmaintenanceitems_list");
		DataTable.clearFilters("companycontactmaintenanceitems_list");
		DataTable.clearFilters("originatorinventorystatus_list");
		DataTable.clearFilters("addresseeinventorystatus_list");
		DataTable.clearFilters("institutioncourses_list");
		DataTable.clearFilters("institutioncvpositions_list");
		DataTable.clearFilters("lectures_list");
		DataTable.clearFilters("trial_membership_list");
		this.staffId = id;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public AccountLazyModel getAccountModel() {
		return accountModel;
	}

	public AddresseeInventoryStatusLazyModel getAddresseeInventoryStatusModel() {
		return addresseeInventoryStatusModel;
	}

	public CompanyContactMaintenanceLazyModel getCompanyContactMaintenanceModel() {
		return companyContactMaintenanceModel;
	}

	public ArrayList<SelectItem> getFilterCalendars() {
		return filterCalendars;
	}

	public InstitutionCourseLazyModel getInstitutionCourseModel() {
		return institutionCourseModel;
	}

	public InstitutionCvPositionLazyModel getInstitutionCvPositionModel() {
		return institutionCvPositionModel;
	}

	public LectureLazyModel getLectureModel() {
		return lectureModel;
	}

	public OnBehalfOfInventoryBookingLazyModel getOnBehalfOfInventoryBookingModel() {
		return onBehalfOfInventoryBookingModel;
	}

	public OriginatorInventoryStatusLazyModel getOriginatorInventoryStatusModel() {
		return originatorInventoryStatusModel;
	}

	public OwnershipInventoryLazyModel getOwnershipInventoryModel() {
		return ownershipInventoryModel;
	}

	public PatientLazyModel getPatientModel() {
		return patientModel;
	}

	public ResponsiblePersonMaintenanceLazyModel getResponsiblePersonMaintenanceModel() {
		return responsiblePersonMaintenanceModel;
	}

	public ShiftDurationSummaryModel getShiftDurationModel() {
		return shiftDurationModel;
	}

	public TrialMembershipLazyModel getTrialMembershipModel() {
		return trialMembershipModel;
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		now = new Date();
		accountModel.setStaffId(staffId);
		accountModel.updateRowCount();
		ownershipInventoryModel.setStaffId(staffId);
		ownershipInventoryModel.updateRowCount();
		onBehalfOfInventoryBookingModel.setStaffId(staffId);
		onBehalfOfInventoryBookingModel.updateRowCount();
		responsiblePersonMaintenanceModel.setStaffId(staffId);
		responsiblePersonMaintenanceModel.updateRowCount();
		companyContactMaintenanceModel.setStaffId(staffId);
		companyContactMaintenanceModel.updateRowCount();
		originatorInventoryStatusModel.setStaffId(staffId);
		originatorInventoryStatusModel.updateRowCount();
		addresseeInventoryStatusModel.setStaffId(staffId);
		addresseeInventoryStatusModel.updateRowCount();
		institutionCourseModel.setStaffId(staffId);
		institutionCourseModel.updateRowCount();
		patientModel.setStaffId(staffId);
		patientModel.updateRowCount();
		institutionCvPositionModel.setStaffId(staffId);
		institutionCvPositionModel.updateRowCount();
		lectureModel.setStaffId(staffId);
		lectureModel.updateRowCount();
		trialMembershipModel.setStaffId(staffId);
		trialMembershipModel.updateRowCount();
		shiftDurationModel.reset(now, ShiftDurationSummaryType.STAFF, staffId);
		filterCalendars = WebUtil.getInventoryBookingFilterCalendars(null, null, null, null, null);
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public void refreshAccount() {
		accountModel.updateRowCount();
		DataTable.clearFilters("account_list");
	}

	public void refreshAddresseeInventoryStatus() {
		addresseeInventoryStatusModel.updateRowCount();
		DataTable.clearFilters("addresseeinventorystatus_list");
	}

	public void refreshCompanyContactMaintenance() {
		companyContactMaintenanceModel.updateRowCount();
		DataTable.clearFilters("companycontactmaintenanceitems_list");
	}

	public void refreshInstitutionCourse() {
		institutionCourseModel.updateRowCount();
		DataTable.clearFilters("institutioncourses_list");
	}

	public void refreshInstitutionCvPosition() {
		institutionCvPositionModel.updateRowCount();
		DataTable.clearFilters("institutioncvpositions_list");
	}

	public void refreshLecture() {
		lectureModel.updateRowCount();
		DataTable.clearFilters("lectures_list");
	}

	public void refreshOnBehalfOfInventoryBooking() {
		onBehalfOfInventoryBookingModel.updateRowCount();
		DataTable.clearFilters("inventorybooking_list");
	}

	public void refreshOriginatorInventoryStatus() {
		originatorInventoryStatusModel.updateRowCount();
		DataTable.clearFilters("originatorinventorystatus_list");
	}

	public void refreshownershipInventory() {
		ownershipInventoryModel.updateRowCount();
		DataTable.clearFilters("ownershipinventory_list");
	}

	public void refreshPatient() {
		patientModel.updateRowCount();
		DataTable.clearFilters("patients_list");
	}

	public void refreshResponsiblePersonMaintenance() {
		responsiblePersonMaintenanceModel.updateRowCount();
		DataTable.clearFilters("responsiblepersonmaintenanceitems_list");
	}

	public void refreshTrialMembership() {
		trialMembershipModel.updateRowCount();
		DataTable.clearFilters("trial_membership_list");
	}
}
