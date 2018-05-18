package org.phoenixctms.ctsms.executable;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.CriteriaDao;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.MassMailDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.service.course.CourseService;
import org.phoenixctms.ctsms.service.inventory.InventoryService;
import org.phoenixctms.ctsms.service.massmail.MassMailService;
import org.phoenixctms.ctsms.service.proband.ProbandService;
import org.phoenixctms.ctsms.service.shared.InputFieldService;
import org.phoenixctms.ctsms.service.shared.JournalService;
import org.phoenixctms.ctsms.service.shared.SearchService;
import org.phoenixctms.ctsms.service.shared.ToolsService;
import org.phoenixctms.ctsms.service.staff.StaffService;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.service.user.UserService;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.ExecDefaultSettings;
import org.phoenixctms.ctsms.util.ExecSettingCodes;
import org.phoenixctms.ctsms.util.ExecSettings;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.AuditTrailExcelVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CvPDFVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingsExcelVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.JournalExcelVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceMethodExecutor {

	private static CriteriaOutVO checkCriteriaDbModule(CriteriaOutVO criteriaOut,DBModule module) {
		if (!module.equals(criteriaOut.getModule())) {
			throw new IllegalArgumentException("criteria ID " + Long.toString(criteriaOut.getId()) + " '" + criteriaOut.getLabel() + "' is a " + criteriaOut.getModule().name()
					+ " query");
		}
		return criteriaOut;
	}

	private static <DAO, ENTITY> long performDeferredDelete(final AuthenticationVO auth, boolean remove, DAO dao, final ServiceMethodExecutor sme, String deleteMethodName,
			final String entityLabel) throws Exception {
		final Method deleteMethod = sme.getClass().getMethod(deleteMethodName, AuthenticationVO.class, Long.class);
		ChunkedDaoOperationAdapter<DAO, ENTITY> processor = new ChunkedDaoOperationAdapter<DAO, ENTITY>(dao,
				new Search(new SearchParameter[] {
						new SearchParameter("deferredDelete", true, SearchParameter.EQUAL_COMPARATOR) })
				) {

			@Override
			protected boolean isIncrementPageNumber() {
				return false;
			}

			@Override
			protected boolean process(Collection<ENTITY> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(ENTITY entity, Object passThrough) throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				if (((Long) inOut.get("i")) < ((Long) inOut.get("totalCount"))) {
					try {
						deleteMethod.invoke(sme, auth, CommonUtil.getEntityId(entity));
						inOut.put("removed", ((Long) inOut.get("removed")) + 1l);
					} catch (Exception e) {
						sme.jobOutput.println("error when removing " + entityLabel + " ID " + CommonUtil.getEntityId(entity) + ": " + CoreUtil.getStackTrace(e));
					}
				} else {
					throw new RuntimeException("some " + entityLabel + " entities could not be deleted");
				}
				inOut.put("i", ((Long) inOut.get("i")) + 1l);
				return true;
			}
		};
		long totalCount = processor.getTotalCount();
		sme.jobOutput.println(totalCount + " " + entityLabel + " entities marked for deletion");
		if (remove) {
			Map<String, Object> passThrough = new HashMap<String, Object>();
			passThrough.put("totalCount", totalCount);
			passThrough.put("removed", 0l);
			passThrough.put("i", 0l);
			processor.processEach(passThrough);
			sme.jobOutput.println(((Long) passThrough.get("removed")) + " " + entityLabel + " entities removed");
			return ((Long) passThrough.get("removed"));
		}
		return totalCount;
	}
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private ToolsService toolsService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TrialService trialService;
	@Autowired
	private ProbandService probandService;
	@Autowired
	private MassMailService massMailService;
	@Autowired
	private UserService userService;
	@Autowired
	private InputFieldService inputFieldService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private InventoryDao inventoryDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private TrialDao trialDao;
	@Autowired
	private ECRFDao eCRFDao; // varnames must match bean ids in applicationContext.xml
	@Autowired
	private ECRFFieldDao eCRFFieldDao; // varnames must match bean ids in applicationContext.xml
	@Autowired
	private InquiryDao inquiryDao;
	@Autowired
	private ProbandDao probandDao;
	@Autowired
	private MassMailDao massMailDao;
	@Autowired
	private InputFieldDao inputFieldDao;
	@Autowired
	private InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private CriteriaDao criteriaDao;
	@Autowired
	private JournalService journalService;

	private JobOutput jobOutput;

	public ServiceMethodExecutor() {
	}

	public void deleteCourse(AuthenticationVO auth, Long id) throws Exception {
		CourseOutVO course = courseService.deleteCourse(auth, id, false, true, null, null, null, null);
		jobOutput.println("course ID " + Long.toString(course.getId()) + " '" + CommonUtil.courseOutVOToString(course) + "' removed");
	}

	public void deleteCriteria(AuthenticationVO auth, Long id) throws Exception {
		CriteriaOutVO criteria = searchService.deleteCriteria(auth, id, false, true, null);
		jobOutput.println("criteria ID " + Long.toString(criteria.getId()) + " '" + CommonUtil.criteriaOutVOToString(criteria) + "' removed");
	}

	public void deleteEcrf(AuthenticationVO auth, Long id) throws Exception {
		ECRFOutVO ecrf = trialService.deleteEcrf(auth, id, false, true, null);
		jobOutput.println("eCRF ID " + Long.toString(ecrf.getId()) + " '" + ecrf.getUniqueName() + "' removed");
	}

	public void deleteEcrfField(AuthenticationVO auth, Long id) throws Exception {
		ECRFFieldOutVO ecrfField = trialService.deleteEcrfField(auth, id, false, true, null);
		jobOutput.println("eCRF field ID " + Long.toString(ecrfField.getId()) + " '" + ecrfField.getUniqueName() + "' removed");
	}

	public void deleteInputField(AuthenticationVO auth, Long id) throws Exception {
		InputFieldOutVO inputField = inputFieldService.deleteInputField(auth, id, false, true, null);
		jobOutput.println("input field ID " + Long.toString(inputField.getId()) + " '" + CommonUtil.inputFieldOutVOToString(inputField) + "' removed");
	}

	public void deleteInquiry(AuthenticationVO auth, Long id) throws Exception {
		InquiryOutVO inquiry = trialService.deleteInquiry(auth, id, false, true, null);
		jobOutput.println("inquiry ID " + Long.toString(inquiry.getId()) + " '" + inquiry.getUniqueName() + "' removed");
	}

	public void deleteInventory(AuthenticationVO auth, Long id) throws Exception {
		InventoryOutVO inventory = inventoryService.deleteInventory(auth, id, false, true, null, null, null);
		jobOutput.println("inventory ID " + Long.toString(inventory.getId()) + " '" + CommonUtil.inventoryOutVOToString(inventory) + "' removed");
	}

	public void deleteMassMail(AuthenticationVO auth, Long id) throws Exception {
		MassMailOutVO massMail = massMailService.deleteMassMail(auth, id, false, true, null);
		jobOutput.println("mass mail ID " + Long.toString(massMail.getId()) + " removed");
	}

	public void deleteProband(AuthenticationVO auth, Long id) throws Exception {
		ProbandOutVO proband = probandService.deleteProband(auth, id, false, true, null, null, null, null);
		jobOutput.println("proband ID " + Long.toString(proband.getId()) + " removed");
	}

	public long deleteProbands(String departmentL10nKey, Integer limit) throws Exception {
		jobOutput.println("department l10n key: " + departmentL10nKey);
		PSFVO psf = new PSFVO();
		psf.setSortField("id");
		psf.setSortOrder(true);
		if (limit != null && limit >= 0) {
			psf.setFirst(0);
			psf.setPageSize(limit);
			jobOutput.println("limit: " + limit);
		}
		long count = toolsService.deleteProbands(ExecUtil.departmentL10nKeyToId(departmentL10nKey, departmentDao, jobOutput), psf);
		jobOutput.println(count + " probands of " + psf.getRowCount() + " marked for auto-delete removed");
		return count;
	}

	public void deleteSelectionSetValue(AuthenticationVO auth, Long id) throws Exception {
		InputFieldSelectionSetValueOutVO selectionSetValue = inputFieldService.deleteSelectionSetValue(auth, id, false, true, null);
		jobOutput.println("selection set value ID " + Long.toString(selectionSetValue.getId()) + " '" + selectionSetValue.getUniqueName() + "' removed");
	}

	public void deleteStaff(AuthenticationVO auth, Long id) throws Exception {
		StaffOutVO staff = staffService.deleteStaff(auth, id, false, true, null, null, null);
		jobOutput.println("person/organisation ID " + Long.toString(staff.getId()) + " '" + CommonUtil.staffOutVOToString(staff) + "' removed");
	}

	public void deleteTrial(AuthenticationVO auth, Long id) throws Exception {
		TrialOutVO trial = trialService.deleteTrial(auth, id, false, true, null);
		jobOutput.println("trial ID " + Long.toString(trial.getId()) + " '" + CommonUtil.trialOutVOToString(trial) + "' removed");
	}

	public void deleteUser(AuthenticationVO auth, Long id) throws Exception {
		UserOutVO user = userService.deleteUser(auth, id, false, true, null, null);
		jobOutput.println("user ID " + Long.toString(user.getId()) + " '" + CommonUtil.userOutVOToString(user) + "' removed");
	}

	public long exportAuditTrail(AuthenticationVO auth, Long id, String fileName) throws Exception {
		// trialService.getEcrfFieldValueLog(auth, id, null, null, null);
		AuditTrailExcelVO result = trialService.exportAuditTrail(auth, id, null, null);
		if (result != null) {
			long count = 0l;
			StringBuilder sb = new StringBuilder(result.getTrial().getName() + " audit trail (" + result.getAuditTrailRowCount() + ")");
			count += result.getAuditTrailRowCount();
			Iterator<ECRFFieldStatusQueue> it = result.getEcrfFieldStatusRowCountMap().keySet().iterator();
			while (it.hasNext()) {
				ECRFFieldStatusQueue queue = it.next();
				sb.append(", " + queue.name() + " (" + result.getEcrfFieldStatusRowCountMap().get(queue) + ")");
				count += (Long) result.getEcrfFieldStatusRowCountMap().get(queue);
			}
			jobOutput.println(sb.toString());
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return count;
		} else {
			return 0l;
		}
	}

	public long exportCourseJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.COURSE_JOURNAL, id);
		if (result != null) {
			jobOutput.println("course ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportCriteriaJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.CRITERIA_JOURNAL, id);
		if (result != null) {
			jobOutput.println("criteria ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportCriteriaResults(AuthenticationVO auth, Long id, String fileName) throws Exception {
		CriteriaInVO criteria = new CriteriaInVO();
		HashSet<CriterionInVO> criterions = new HashSet<CriterionInVO>();
		ExecUtil.criteriaOutToIn(criteria, criterions, searchService.getCriteria(auth, id));
		// CriteriaInstantVO instantCriteria = searchService.parseCriteria(auth, criteria.getModule(), criterions, true);
		// jobOutput.println("criteria id " + criteria.getId() + " '" + criteria.getLabel() + "' parsed: " + instantCriteria.getCriterionExpression());
		SearchResultExcelVO result;
		String type;
		// PSFVO psf = new PSFVO();
		switch (criteria.getModule()) {
			case INVENTORY_DB:
				result = searchService.exportInventory(auth, criteria, criterions, null);
				type = "inventory";
				break;
			case STAFF_DB:
				result = searchService.exportStaff(auth, criteria, criterions, null);
				type = "staff";
				break;
			case COURSE_DB:
				result = searchService.exportCourse(auth, criteria, criterions, null);
				type = "course";
				break;
			case TRIAL_DB:
				result = searchService.exportTrial(auth, criteria, criterions, null);
				type = "trial";
				break;
			case PROBAND_DB:
				result = searchService.exportProband(auth, criteria, criterions, null);
				type = "proband";
				break;
			case INPUT_FIELD_DB:
				result = searchService.exportInputField(auth, criteria, criterions, null);
				type = "inputfield";
				break;
			case USER_DB:
				result = searchService.exportUser(auth, criteria, criterions, null);
				type = "user";
				break;
			case MASS_MAIL_DB:
				result = searchService.exportMassMail(auth, criteria, criterions, null);
				type = "massmail";
				break;
			default:
				result = null;
				type = null;
				throw new IllegalArgumentException("db module '" + criteria.getModule().toString() + "' not supported");
		}
		if (result != null) {
			printCriteriaResult(criteria, result.getCriteria(), result.getRowCount(), type);
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportEcrfJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportEcrfJournal(auth, id);
		if (result != null) {
			jobOutput.println("trial ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportInputFieldJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.INPUT_FIELD_JOURNAL, id);
		if (result != null) {
			jobOutput.println("input field ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportInventoryJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.INVENTORY_JOURNAL, id);
		if (result != null) {
			jobOutput.println("inventory ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportMassMailJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.MASS_MAIL_JOURNAL, id);
		if (result != null) {
			jobOutput.println("mass mail ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportProbandAppointments(AuthenticationVO auth, Long id, String fileName) throws Exception {
		Date now = new Date();
		// now.setDate(5);
		Date from = DateCalc.getStartOfDay(now);
		Date to = DateCalc.getEndOfDay(now);
		jobOutput
		.println("timespan: "
				+ CommonUtil.getDateStartStopString(from, to,
						new SimpleDateFormat(ExecSettings.getString(ExecSettingCodes.DATETIME_PATTERN, ExecDefaultSettings.DATETIME_PATTERN))));
		InventoryBookingsExcelVO result = inventoryService.exportInventoryBookings(auth, null, null, id, null, from, to, true, true,null, null, null, null);
		if (result != null) {
			if (id != null) {
				jobOutput.println("department ID " + Long.toString(id) + ": " + result.getRowCount() + " inventory bookings");
			} else {
				jobOutput.println( result.getRowCount() + " inventory bookings");
			}
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportProbandJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.PROBAND_JOURNAL, id);
		if (result != null) {
			jobOutput.println("proband ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportProbandList(AuthenticationVO auth, Long id, ProbandListStatusLogLevel logLevel, String fileName) throws Exception {
		// trialService.getEcrfFieldValueLog(auth, id, null, null, null);
		ProbandListExcelVO result = trialService.exportProbandList(auth, id, logLevel);
		if (result != null) {
			jobOutput.println(result.getTrial().getName() + " " + (result.getLogLevel() != null ? result.getLogLevel().name() : "[full subject list]") + ": "
					+ result.getRowCount() + " probands");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportStaffJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.STAFF_JOURNAL, id);
		if (result != null) {
			jobOutput.println("staff ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportTrialJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.TRIAL_JOURNAL, id);
		if (result != null) {
			jobOutput.println("trial ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long exportUserJournal(AuthenticationVO auth, Long id, String fileName) throws Exception {
		JournalExcelVO result = journalService.exportJournal(auth, JournalModule.USER_JOURNAL, id);
		if (result != null) {
			jobOutput.println("user ID " + Long.toString(id) + ": " + result.getRowCount() + " journal records");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return result.getRowCount();
		} else {
			return 0l;
		}
	}

	public long performAllDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performInventoryDeferredDelete(auth, remove) +
				performStaffDeferredDelete(auth, remove) +
				performCourseDeferredDelete(auth, remove) +
				performTrialDeferredDelete(auth, remove) +
				performInquiryDeferredDelete(auth, remove) +
				performEcrfDeferredDelete(auth, remove) +
				performEcrfFieldDeferredDelete(auth, remove) +
				performProbandDeferredDelete(auth, remove) +
				performMassMailDeferredDelete(auth, remove) +
				performInputFieldDeferredDelete(auth, remove) +
				performSelectionSetValueDeferredDelete(auth, remove) +
				performUserDeferredDelete(auth, remove) +
				performCriteriaDeferredDelete(auth, remove);
	}

	public long performCourseDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, courseDao, this, "deleteCourse", "course");
	}

	public long performCriteriaDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, criteriaDao, this, "deleteCriteria", "criteria");
	}

	public long performEcrfDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, eCRFDao, this, "deleteEcrf", "ecrf");
	}

	public long performEcrfFieldDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, eCRFFieldDao, this, "deleteEcrfField", "ecrf field");
	}

	public long performInputFieldDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, inputFieldDao, this, "deleteInputField", "input field");
	}

	public long performInquiryDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, inquiryDao, this, "deleteInquiry", "inquiry");
	}

	public long performInventoryDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, inventoryDao, this, "deleteInventory", "inventory");
	}

	public long performMassMailDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, massMailDao, this, "deleteMassMail", "mass mail");
	}

	public long performProbandDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, probandDao, this, "deleteProband", "proband");
	}

	public long performSelectionSetValueDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, inputFieldSelectionSetValueDao, this, "deleteSelectionSetValue", "selection set value");
	}

	public long performStaffDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, staffDao, this, "deleteStaff", "staff");
	}

	public long performTrialDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, trialDao, this, "deleteTrial", "trial");
	}

	public long performUserDeferredDelete(AuthenticationVO auth, boolean remove) throws Exception {
		return performDeferredDelete(auth, remove, userDao, this, "deleteUser", "user");
	}

	private void printCriteriaResult(CriteriaInVO criteriaIn, CriteriaInstantVO instantCriteria, long rowCount, String type) {
		jobOutput.println("criteria ID " + Long.toString(criteriaIn.getId()) + " '" + criteriaIn.getLabel() + "' returned " + rowCount + " " + type + " items:\n"
				+ (CommonUtil.isEmptyString(instantCriteria.getCriterionExpression()) ? "<no criterions>" : instantCriteria.getCriterionExpression()));
		// jobOutput.println("search returned " + result.getRowCount() + " " + type + " items"); // (psf != null ? " of " + psf.getRowCount() : "")
	}

	public long renderCourseParticipantListPDFs(AuthenticationVO auth, Long id, String fileName) throws Exception {
		CriteriaInVO criteria = new CriteriaInVO();
		HashSet<CriterionInVO> criterions = new HashSet<CriterionInVO>();
		ExecUtil.criteriaOutToIn(criteria, criterions, checkCriteriaDbModule(searchService.getCriteria(auth, id), DBModule.COURSE_DB));
		CourseParticipantListPDFVO result = searchService.renderCourseParticipantListPDFs(auth, criteria, criterions, null);
		if (result != null) {
			long rowCount = result.getCourses().size();
			printCriteriaResult(criteria, result.getCriteria(), rowCount, "course");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return rowCount;
		} else {
			return 0l;
		}
	}

	public long renderCvPDFs(AuthenticationVO auth, Long id, String fileName) throws Exception {
		CriteriaInVO criteria = new CriteriaInVO();
		HashSet<CriterionInVO> criterions = new HashSet<CriterionInVO>();
		ExecUtil.criteriaOutToIn(criteria, criterions, checkCriteriaDbModule(searchService.getCriteria(auth, id), DBModule.STAFF_DB));
		CvPDFVO result = searchService.renderCvPDFs(auth, criteria, criterions, null);
		if (result != null) {
			long rowCount = result.getStafves().size();
			printCriteriaResult(criteria, result.getCriteria(), rowCount, "staff");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return rowCount;
		} else {
			return 0l;
		}
	}

	public long renderEcrfPDFs(AuthenticationVO auth, Long id, String fileName) throws Exception {

		ECRFPDFVO result = trialService.renderEcrfs(auth, id, null, null, false);
		if (result != null) {
			long ecrfCount = result.getStatusEntries().size();
			jobOutput.println("trial ID " + Long.toString(id) + ": " + ecrfCount + " eCRF(s)");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return ecrfCount;
		} else {
			return 0l;
		}

	}

	public long renderProbandLetterPDFs(AuthenticationVO auth, Long id, String fileName) throws Exception {
		CriteriaInVO criteria = new CriteriaInVO();
		HashSet<CriterionInVO> criterions = new HashSet<CriterionInVO>();
		ExecUtil.criteriaOutToIn(criteria, criterions, checkCriteriaDbModule(searchService.getCriteria(auth, id), DBModule.STAFF_DB));
		ProbandLetterPDFVO result = searchService.renderProbandLetterPDFs(auth, criteria, criterions, null);
		if (result != null) {
			long rowCount = result.getProbands().size();
			printCriteriaResult(criteria, result.getCriteria(), rowCount, "proband");
			jobOutput.addLinkOrEmailAttachment(fileName, result.getDocumentDatas(), result.getContentType(), result.getFileName());
			return rowCount;
		} else {
			return 0l;
		}
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}

	public int validatePendingTrialEcrfs(AuthenticationVO auth, Long id) throws Exception {
		Collection<ECRFStatusEntryVO> statusEntries = trialService.validatePendingEcrfs(auth, id, null, null); // .getEcrfFieldValues(auth, 5986436l, 5991949l, false, true, null);
		// // 5994474l
		jobOutput.println("trial ID " + Long.toString(id) + ": " + statusEntries.size() + " eCRFs pending for validation");
		Iterator<ECRFStatusEntryVO> it = statusEntries.iterator();
		while (it.hasNext()) {
			ECRFStatusEntryVO statusEntry = it.next();
			jobOutput.println("proband ID " + Long.toString(statusEntry.getListEntry().getProband().getId()) + " / " + statusEntry.getEcrf().getUniqueName() + ": "
					+ statusEntry.getValidationResponseMsg());
		}
		return statusEntries.size();
	}
}
