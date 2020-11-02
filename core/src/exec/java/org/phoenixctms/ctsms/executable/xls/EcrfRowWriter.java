package org.phoenixctms.ctsms.executable.xls;

import java.util.Collection;
import java.util.Iterator;

import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EcrfRowWriter extends RowWriter {

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
	private int maxColumnIndex;
	@Autowired
	protected TrialService trialService;

	public EcrfRowWriter() {
		super();
		maxColumnIndex = 0;
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	@Override
	public void init() {
		super.init();
		maxColumnIndex = 0;
		nameColumnIndex = NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(nameColumnIndex, maxColumnIndex);
		revisionColumnIndex = REVISION_COLUMN_INDEX;
		maxColumnIndex = Math.max(revisionColumnIndex, maxColumnIndex);
		probandGroupsColumnIndex = PROBAND_GROUPS_COLUMN_INDEX;
		maxColumnIndex = Math.max(probandGroupsColumnIndex, maxColumnIndex);
		visitsColumnIndex = VISITS_COLUMN_INDEX;
		maxColumnIndex = Math.max(visitsColumnIndex, maxColumnIndex);
		activeColumnIndex = ACTIVE_COLUMN_INDEX;
		maxColumnIndex = Math.max(activeColumnIndex, maxColumnIndex);
		enableBrowserFieldCalculationColumnIndex = ENABLE_BROWSER_FIELD_CALCULATION_COLUMN_INDEX;
		maxColumnIndex = Math.max(enableBrowserFieldCalculationColumnIndex, maxColumnIndex);
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		maxColumnIndex = Math.max(externalIdColumnIndex, maxColumnIndex);
		titleColumnIndex = TITLE_COLUMN_INDEX;
		maxColumnIndex = Math.max(titleColumnIndex, maxColumnIndex);
		descriptionColumnIndex = DESCRIPTION_COLUMN_INDEX;
		maxColumnIndex = Math.max(descriptionColumnIndex, maxColumnIndex);
		enrollmentStatusColumnIndex = ENROLLMENT_STATUS_COLUMN_INDEX;
		maxColumnIndex = Math.max(enrollmentStatusColumnIndex, maxColumnIndex);
		chargeColumnIndex = CHARGE_COLUMN_INDEX;
		maxColumnIndex = Math.max(chargeColumnIndex, maxColumnIndex);
		context.getExporter().getEcrfFieldRowWriter().init();
		context.getSpreadSheet(this);
	}

	@Override
	public void printRows() throws Throwable {
		printRows(trialService.getEcrfList(context.getAuth(), context.getEntityId(this), null, true, null));
	}

	public void printRows(Collection<ECRFOutVO> ecrfs) throws Throwable {
		Iterator<ECRFOutVO> ecrfsIt = ecrfs.iterator();
		while (ecrfsIt.hasNext()) {
			ECRFOutVO ecrf = ecrfsIt.next();
			context.setEntityId(context.getExporter().getEcrfFieldRowWriter(), ecrf.getId());
			context.getExporter().getEcrfFieldRowWriter().printRows();
			String[] values = new String[maxColumnIndex + 1];
			values[nameColumnIndex] = ecrf.getName();
			values[revisionColumnIndex] = ecrf.getRevision();
			Iterator it = ecrf.getGroups().iterator();
			StringBuilder sb = new StringBuilder();
			while (it.hasNext()) {
				if (sb.length() > 0) {
					sb.append(ServiceUtil.GROUP_VISIT_SPLIT_SEPARATOR);
				}
				sb.append(((ProbandGroupOutVO) it.next()).getToken());
			}
			values[probandGroupsColumnIndex] = sb.toString();
			it = ecrf.getVisits().iterator();
			sb = new StringBuilder();
			while (it.hasNext()) {
				if (sb.length() > 0) {
					sb.append(ServiceUtil.GROUP_VISIT_SPLIT_SEPARATOR);
				}
				sb.append(((VisitOutVO) it.next()).getToken());
			}
			values[visitsColumnIndex] = sb.toString();
			values[activeColumnIndex] = Boolean.toString(ecrf.getActive());
			values[enableBrowserFieldCalculationColumnIndex] = Boolean.toString(ecrf.getEnableBrowserFieldCalculation());
			values[externalIdColumnIndex] = ecrf.getExternalId();
			values[titleColumnIndex] = ecrf.getTitle();
			values[descriptionColumnIndex] = ecrf.getDescription();
			values[enrollmentStatusColumnIndex] = (ecrf.getProbandListStatus() != null ? ecrf.getProbandListStatus().getNameL10nKey() : null);
			values[chargeColumnIndex] = Float.toString(ecrf.getCharge());
			printRow(values);
		}
	}
}
