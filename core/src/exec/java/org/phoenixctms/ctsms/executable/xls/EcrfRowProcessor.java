package org.phoenixctms.ctsms.executable.xls;

import java.util.HashMap;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusType;
import org.phoenixctms.ctsms.domain.ProbandListStatusTypeDao;
import org.phoenixctms.ctsms.domain.VisitDao;
import org.phoenixctms.ctsms.domain.VisitType;
import org.phoenixctms.ctsms.domain.VisitTypeDao;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.ECRFInVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.VisitInVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EcrfRowProcessor extends RowProcessor {

	private final static String DEFAULT_PROBAND_GROUP_DESCRIPTION = "";
	private final static boolean DEFAULT_PROBAND_GROUP_RANDOMIZE = false;
	private final static String DEFAULT_VISIT_TYPE_NAME_L10N_KEY = "screening";
	private final static float DEFAULT_VISIT_REIMBURSEMENT = 0.0f;
	private final static String DEFAULT_VISIT_DESCRIPTION = "";
	private final static String SHEET_NAME = "ecrfs";
	private final static int NAME_COLUMN_INDEX = 0;
	private final static int REVISION_COLUMN_INDEX = 1;
	private final static int PROBAND_GROUPS_COLUMN_INDEX = 2;
	private final static int VISITS_COLUMN_INDEX = 3;
	private final static int ACTIVE_COLUMN_INDEX = 4;
	private final static int ENABLE_BROWSER_FIELD_CALCULATION_COLUMN_INDEX = 5;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 6;
	private final static int TITLE_COLUMN_INDEX = 7;
	private final static int DESCRIPTION_COLUMN_INDEX = 8;
	private final static int ENROLLMENT_STATUS_COLUMN_INDEX = 9;
	private final static int CHARGE_COLUMN_INDEX = 10;
	private int nameColumnIndex;
	private int revisionColumnIndex;
	private int probandGroupsColumnIndex;
	private int visitsColumnIndex;
	private int activeColumnIndex;
	private int enableBrowserFieldCalculationColumnIndex;
	private int externalIdColumnIndex;
	private int titleColumnIndex;
	private int descriptionColumnIndex;
	private int enrollmentStatusColumnIndex;
	private int chargeColumnIndex;
	private HashMap<String, Long> probandGroupIdMap;
	private HashMap<String, Long> visitIdMap;
	private HashMap<String, Long> probandListStatusTypeIdMap;
	private VisitType defaultVisitType;
	@Autowired
	protected TrialService trialService;
	@Autowired
	protected ProbandGroupDao probandGroupDao;
	@Autowired
	protected VisitDao visitDao;
	@Autowired
	protected VisitTypeDao visitTypeDao;
	@Autowired
	protected ProbandListStatusTypeDao probandListStatusTypeDao;

	public EcrfRowProcessor() {
		super();
		filterDupes = false;
		acceptCommentsIndex = 0;
		probandGroupIdMap = new HashMap<String, Long>();
		visitIdMap = new HashMap<String, Long>();
		probandListStatusTypeIdMap = new HashMap<String, Long>();
	}

	private String getActive(String[] values) {
		return getColumnValue(values, activeColumnIndex);
	}

	private String getCharge(String[] values) {
		return getColumnValue(values, chargeColumnIndex);
	}

	private String getDescription(String[] values) {
		return getColumnValue(values, descriptionColumnIndex);
	}

	private String getEnableBrowserFieldCalculation(String[] values) {
		return getColumnValue(values, enableBrowserFieldCalculationColumnIndex);
	}

	private String getEnrollmentStatus(String[] values) {
		return getColumnValue(values, enrollmentStatusColumnIndex);
	}

	private String getExternalId(String[] values) {
		return getColumnValue(values, externalIdColumnIndex);
	}

	private String getName(String[] values) {
		return getColumnValue(values, nameColumnIndex);
	}

	private String getRevision(String[] values) {
		return getColumnValue(values, revisionColumnIndex);
	}

	private String getProbandGroups(String[] values) {
		return getColumnValue(values, probandGroupsColumnIndex);
	}

	private Long getProbandListStatusTypeId(String probandListStatusTypeNameL10nKey) {
		Long probandListStatusTypeId = null;
		if (!CommonUtil.isEmptyString(probandListStatusTypeNameL10nKey)) {
			if (!probandListStatusTypeIdMap.containsKey(probandListStatusTypeNameL10nKey)) {
				ProbandListStatusType probandListStatusType = probandListStatusTypeDao.searchUniqueNameL10nKey(probandListStatusTypeNameL10nKey);
				if (probandListStatusType != null) {
					probandListStatusTypeId = probandListStatusType.getId();
				} else {
					jobOutput.println("enrollment status '" + probandListStatusTypeNameL10nKey + "' not found");
				}
				probandListStatusTypeIdMap.put(probandListStatusTypeNameL10nKey, probandListStatusTypeId);
			} else {
				probandListStatusTypeId = probandListStatusTypeIdMap.get(probandListStatusTypeNameL10nKey);
			}
		}
		return probandListStatusTypeId;
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	private String getTitle(String[] values) {
		return getColumnValue(values, titleColumnIndex);
	}

	private String getVisits(String[] values) {
		return getColumnValue(values, visitsColumnIndex);
	}

	private Long getVisitId(String visitToken) throws Exception {
		Long visitId = null;
		if (!CommonUtil.isEmptyString(visitToken)) {
			if (!visitIdMap.containsKey(visitToken)) {
				try {
					visitId = visitDao.findByTrialTitleToken(context.getEntityId(), null, visitToken).iterator().next().getId();
				} catch (NoSuchElementException e) {
					VisitOutVO visitVO = createVisit(visitToken);
					visitId = visitVO.getId();
					jobOutput.println("visit '" + visitVO.getUniqueName() + "' created");
				}
				visitIdMap.put(visitToken, visitId);
			} else {
				visitId = visitIdMap.get(visitToken);
			}
		}
		return visitId;
	}

	private VisitOutVO createVisit(String visitToken) throws Exception {
		VisitInVO newVisit = new VisitInVO();
		newVisit.setToken(visitToken);
		newVisit.setTitle(visitToken);
		newVisit.setTrialId(context.getEntityId());
		newVisit.setTypeId(defaultVisitType != null ? defaultVisitType.getId() : null);
		newVisit.setReimbursement(DEFAULT_VISIT_REIMBURSEMENT);
		newVisit.setDescription(DEFAULT_VISIT_DESCRIPTION);
		return trialService.addVisit(context.getAuth(), newVisit);
	}

	private Long getProbandGroupId(String probandGroupToken) throws Exception {
		Long probandGroupId = null;
		if (!CommonUtil.isEmptyString(probandGroupToken)) {
			if (!probandGroupIdMap.containsKey(probandGroupToken)) {
				try {
					probandGroupId = probandGroupDao.findByTrialTitleToken(context.getEntityId(), null, probandGroupToken).iterator().next().getId();
				} catch (NoSuchElementException e) {
					ProbandGroupOutVO probandGroupVO = createProbandGroup(probandGroupToken);
					probandGroupId = probandGroupVO.getId();
					jobOutput.println("proband group '" + probandGroupVO.getUniqueName() + "' created");
				}
				probandGroupIdMap.put(probandGroupToken, probandGroupId);
			} else {
				probandGroupId = probandGroupIdMap.get(probandGroupToken);
			}
		}
		return probandGroupId;
	}

	private ProbandGroupOutVO createProbandGroup(String probandGroupToken) throws Exception {
		ProbandGroupInVO newProbandGroup = new ProbandGroupInVO();
		newProbandGroup.setToken(probandGroupToken);
		newProbandGroup.setTitle(probandGroupToken);
		newProbandGroup.setDescription(DEFAULT_PROBAND_GROUP_DESCRIPTION);
		newProbandGroup.setRandomize(DEFAULT_PROBAND_GROUP_RANDOMIZE);
		newProbandGroup.setTrialId(context.getEntityId());
		return trialService.addProbandGroup(context.getAuth(), newProbandGroup);
	}

	@Override
	public void init() throws Throwable {
		super.init();
		nameColumnIndex = NAME_COLUMN_INDEX;
		revisionColumnIndex = REVISION_COLUMN_INDEX;
		probandGroupsColumnIndex = PROBAND_GROUPS_COLUMN_INDEX;
		visitsColumnIndex = VISITS_COLUMN_INDEX;
		activeColumnIndex = ACTIVE_COLUMN_INDEX;
		enableBrowserFieldCalculationColumnIndex = ENABLE_BROWSER_FIELD_CALCULATION_COLUMN_INDEX;
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		titleColumnIndex = TITLE_COLUMN_INDEX;
		descriptionColumnIndex = DESCRIPTION_COLUMN_INDEX;
		enrollmentStatusColumnIndex = ENROLLMENT_STATUS_COLUMN_INDEX;
		chargeColumnIndex = CHARGE_COLUMN_INDEX;
		defaultVisitType = visitTypeDao.searchUniqueNameL10nKey(DEFAULT_VISIT_TYPE_NAME_L10N_KEY);
		probandGroupIdMap.clear();
		visitIdMap.clear();
		probandListStatusTypeIdMap.clear();
		context.getImporter().loadEcrfFields(context);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getName(values))
				.append(getRevision(values))
				.append(getProbandGroups(values))
				.append(getVisits(values))
				.append(getActive(values))
				.append(getEnableBrowserFieldCalculation(values))
				.append(getExternalId(values))
				.append(getTitle(values))
				.append(getDescription(values))
				.append(getEnrollmentStatus(values))
				.append(getCharge(values))
				.toHashCode();
	}

	@Override
	protected void postProcess() {
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		String name = getName(values);
		String revision = getRevision(values);
		ECRF ecrf = context.getImporter().getEcrfFieldRowProcessor().getEcrf(name, revision);
		ECRFInVO ecrfIn = new ECRFInVO();
		ecrfIn.setId(ecrf != null ? ecrf.getId() : null);
		ecrfIn.setVersion(ecrf != null ? ecrf.getVersion() : 0l);
		ecrfIn.setName(name);
		ecrfIn.setRevision(getRevision(values));
		ecrfIn.setTrialId(context.getEntityId());
		String[] tokens;
		try {
			tokens = getProbandGroups(values).split(ServiceUtil.GROUP_VISIT_SPLIT_REGEX_PATTERN);
		} catch (Exception e) {
			tokens = new String[] {};
		}
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].trim();
			if (token != null && token.length() > 0) {
				ecrfIn.getGroupIds().add(getProbandGroupId(token));
			}
		}
		try {
			tokens = getVisits(values).split(ServiceUtil.GROUP_VISIT_SPLIT_REGEX_PATTERN);
		} catch (Exception e) {
			tokens = new String[] {};
		}
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i].trim();
			if (token != null && token.length() > 0) {
				ecrfIn.getVisitIds().add(getVisitId(token));
			}
		}
		ecrfIn.setActive(Boolean.parseBoolean(getActive(values)));
		ecrfIn.setEnableBrowserFieldCalculation(Boolean.parseBoolean(getEnableBrowserFieldCalculation(values)));
		ecrfIn.setExternalId(getExternalId(values));
		ecrfIn.setTitle(getTitle(values));
		ecrfIn.setDescription(getDescription(values));
		ecrfIn.setProbandListStatusId(getProbandListStatusTypeId(getEnrollmentStatus(values)));
		ecrfIn.setCharge(CommonUtil.isEmptyString(getCharge(values)) ? 0.0f : Float.parseFloat(getCharge(values)));
		ECRFOutVO ecrfVO = trialService.addUpdateEcrf(context.getAuth(), ecrfIn,
				context.getImporter().getEcrfFieldRowProcessor().getEcrfFields(name, revision));
		jobOutput.println("ecrf '" + ecrfVO.getUniqueName() + "' " + (ecrfVO.getVersion() > 0l ? "updated" : "created"));
		context.getImporter().getEcrfFieldRowProcessor().clearEcrf(name, revision);
		return 1;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (CommonUtil.isEmptyString(getName(values))) {
			return false;
		}
		return true;
	}
}
