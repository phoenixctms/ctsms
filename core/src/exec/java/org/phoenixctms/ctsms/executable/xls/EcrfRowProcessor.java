package org.phoenixctms.ctsms.executable.xls;

import java.util.HashMap;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ProbandListStatusType;
import org.phoenixctms.ctsms.domain.ProbandListStatusTypeDao;
import org.phoenixctms.ctsms.domain.VisitDao;
import org.phoenixctms.ctsms.domain.VisitType;
import org.phoenixctms.ctsms.domain.VisitTypeDao;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFInVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.VisitInVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EcrfRowProcessor extends RowProcessor {

	private final static String DEFAULT_VISIT_TYPE_NAME_L10N_KEY = "screening";
	private final static float DEFAULT_VISIT_REIMBURSEMENT = 0.0f;
	private final static String DEFAULT_VISIT_DESCRIPTION = "";
	private final static String SHEET_NAME = "ecrfs";
	private final static int PROBAND_GROUP_COLUMN_INDEX = 0;
	private final static int POSITION_COLUMN_INDEX = 1;
	private final static int VISIT_COLUMN_INDEX = 2;
	private final static int ACTIVE_COLUMN_INDEX = 3;
	private final static int ENABLE_BROWSER_FIELD_CALCULATION_COLUMN_INDEX = 4;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 5;
	private final static int NAME_COLUMN_INDEX = 6;
	private final static int TITLE_COLUMN_INDEX = 7;
	private final static int DESCRIPTION_COLUMN_INDEX = 8;
	private final static int ENROLLMENT_STATUS_COLUMN_INDEX = 9;
	private final static int CHARGE_COLUMN_INDEX = 10;
	private int probandGroupColumnIndex;
	private int positionColumnIndex;
	private int visitColumnIndex;
	private int activeColumnIndex;
	private int enableBrowserFieldCalculationColumnIndex;
	private int externalIdColumnIndex;
	private int nameColumnIndex;
	private int titleColumnIndex;
	private int descriptionColumnIndex;
	private int enrollmentStatusColumnIndex;
	private int chargeColumnIndex;
	private HashMap<String, Long> visitIdMap;
	private HashMap<String, Long> probandListStatusTypeIdMap;
	private VisitType defaultVisitType;
	@Autowired
	protected TrialService trialService;
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
		visitIdMap = new HashMap<String, Long>();
		probandListStatusTypeIdMap = new HashMap<String, Long>();
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

	private String getPosition(String[] values) {
		return getColumnValue(values, positionColumnIndex);
	}

	private String getProbandGroup(String[] values) {
		return getColumnValue(values, probandGroupColumnIndex);
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

	private String getVisit(String[] values) {
		return getColumnValue(values, visitColumnIndex);
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

	@Override
	public void init() throws Throwable {
		super.init();
		probandGroupColumnIndex = PROBAND_GROUP_COLUMN_INDEX;
		positionColumnIndex = POSITION_COLUMN_INDEX;
		visitColumnIndex = VISIT_COLUMN_INDEX;
		activeColumnIndex = ACTIVE_COLUMN_INDEX;
		enableBrowserFieldCalculationColumnIndex = ENABLE_BROWSER_FIELD_CALCULATION_COLUMN_INDEX;
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		nameColumnIndex = NAME_COLUMN_INDEX;
		titleColumnIndex = TITLE_COLUMN_INDEX;
		descriptionColumnIndex = DESCRIPTION_COLUMN_INDEX;
		enrollmentStatusColumnIndex = ENROLLMENT_STATUS_COLUMN_INDEX;
		chargeColumnIndex = CHARGE_COLUMN_INDEX;
		defaultVisitType = visitTypeDao.searchUniqueNameL10nKey(DEFAULT_VISIT_TYPE_NAME_L10N_KEY);
		visitIdMap.clear();
		probandListStatusTypeIdMap.clear();
		context.getImporter().loadEcrfFields(context);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getProbandGroup(values))
				.append(getPosition(values))
				.append(getVisit(values))
				.append(getActive(values))
				.append(getEnableBrowserFieldCalculation(values))
				.append(getExternalId(values))
				.append(getName(values))
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
		String probandGroupToken = getProbandGroup(values);
		Long position = Long.parseLong(getPosition(values));
		ECRF ecrf = context.getImporter().getEcrfFieldRowProcessor().getEcrf(probandGroupToken, position);
		ECRFInVO ecrfIn = new ECRFInVO();
		ecrfIn.setId(ecrf != null ? ecrf.getId() : null);
		ecrfIn.setVersion(ecrf != null ? ecrf.getVersion() : 0l);
		ecrfIn.setTrialId(context.getEntityId());
		ecrfIn.setGroupId(context.getImporter().getEcrfFieldRowProcessor().getProbandGroupId(probandGroupToken));
		ecrfIn.setPosition(position);
		ecrfIn.setVisitId(getVisitId(getVisit(values)));
		ecrfIn.setActive(Boolean.parseBoolean(getActive(values)));
		ecrfIn.setEnableBrowserFieldCalculation(Boolean.parseBoolean(getEnableBrowserFieldCalculation(values)));
		ecrfIn.setExternalId(getExternalId(values));
		ecrfIn.setName(getName(values));
		ecrfIn.setTitle(getTitle(values));
		ecrfIn.setDescription(getDescription(values));
		ecrfIn.setProbandListStatusId(getProbandListStatusTypeId(getEnrollmentStatus(values)));
		ecrfIn.setCharge(CommonUtil.isEmptyString(getCharge(values)) ? 0.0f : Float.parseFloat(getCharge(values)));
		ECRFOutVO ecrfVO = trialService.addUpdateEcrf(context.getAuth(), ecrfIn,
				context.getImporter().getEcrfFieldRowProcessor().getEcrfFields(probandGroupToken, position));
		jobOutput.println("ecrf '" + ecrfVO.getUniqueName() + "' " + (ecrfVO.getVersion() > 0l ? "updated" : "created"));
		context.getImporter().getEcrfFieldRowProcessor().clearEcrf(probandGroupToken, position);
		return 1;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (CommonUtil.isEmptyString(getPosition(values))) {
			return false;
		}
		return true;
	}
}
