package org.phoenixctms.ctsms.executable.xls;

import java.util.Collection;
import java.util.Iterator;

import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EcrfRowWriter extends RowWriter {

	private final static String SHEET_NAME = "ecrfs";
	private final static int PROBAND_GROUP_COLUMN_INDEX = 0;
	private final static int POSITION_COLUMN_INDEX = 1;
	private final static int VISIT_COLUMN_INDEX = 2;
	private final static int ACTIVE_COLUMN_INDEX = 3;
	private final static int ENABLE_BROWSER_FIELD_CALCULATION_COLUMN_INDEX = 4;
	private final static int EXTERNAL_ID_COLUMN_INDEX = 5; // 4;
	private final static int NAME_COLUMN_INDEX = 6; // 5;
	private final static int TITLE_COLUMN_INDEX = 7; // 6;
	private final static int DESCRIPTION_COLUMN_INDEX = 8; // 7;
	private final static int ENROLLMENT_STATUS_COLUMN_INDEX = 9; // 8;
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
	private int maxColumnIndex;
	@Autowired
	protected TrialService trialService;

	public EcrfRowWriter() {
		super();
		maxColumnIndex = 0;
	}

	public String getSheetName() {
		// return context.getSheetName(this);
		return SHEET_NAME;
	}

	@Override
	public void init() {
		super.init();

		maxColumnIndex = 0;
		probandGroupColumnIndex = PROBAND_GROUP_COLUMN_INDEX;
		maxColumnIndex = Math.max(probandGroupColumnIndex, maxColumnIndex);
		positionColumnIndex = POSITION_COLUMN_INDEX;
		maxColumnIndex = Math.max(positionColumnIndex, maxColumnIndex);
		visitColumnIndex = VISIT_COLUMN_INDEX;
		maxColumnIndex = Math.max(visitColumnIndex, maxColumnIndex);
		activeColumnIndex = ACTIVE_COLUMN_INDEX;
		maxColumnIndex = Math.max(activeColumnIndex, maxColumnIndex);
		enableBrowserFieldCalculationColumnIndex = ENABLE_BROWSER_FIELD_CALCULATION_COLUMN_INDEX;
		maxColumnIndex = Math.max(enableBrowserFieldCalculationColumnIndex, maxColumnIndex);
		externalIdColumnIndex = EXTERNAL_ID_COLUMN_INDEX;
		maxColumnIndex = Math.max(externalIdColumnIndex, maxColumnIndex);
		nameColumnIndex = NAME_COLUMN_INDEX;
		maxColumnIndex = Math.max(nameColumnIndex, maxColumnIndex);
		titleColumnIndex = TITLE_COLUMN_INDEX;
		maxColumnIndex = Math.max(titleColumnIndex, maxColumnIndex);
		descriptionColumnIndex = DESCRIPTION_COLUMN_INDEX;
		maxColumnIndex = Math.max(descriptionColumnIndex, maxColumnIndex);
		enrollmentStatusColumnIndex = ENROLLMENT_STATUS_COLUMN_INDEX;
		maxColumnIndex = Math.max(enrollmentStatusColumnIndex, maxColumnIndex);

		context.getExporter().getEcrfFieldRowWriter().init();
		context.getSpreadSheet(this);
	}

	@Override
	public void printRows() throws Throwable {
		printRows(trialService.getEcrfList(context.getAuth(), context.getEntityId(this), null, true, null));
	}

	public void printRows(Collection<ECRFOutVO> ecrfs) throws Throwable {
		Iterator<ECRFOutVO> it = ecrfs.iterator();
		while (it.hasNext()) {
			ECRFOutVO ecrf = it.next();
			context.setEntityId(context.getExporter().getEcrfFieldRowWriter(), ecrf.getId());
			context.getExporter().getEcrfFieldRowWriter().printRows();
			String[] values = new String[maxColumnIndex + 1];
			values[probandGroupColumnIndex] = (ecrf.getGroup() != null ? ecrf.getGroup().getToken() : null);
			values[positionColumnIndex] = Long.toString(ecrf.getPosition());
			values[visitColumnIndex] = (ecrf.getVisit() != null ? ecrf.getVisit().getToken() : null);
			values[activeColumnIndex] = Boolean.toString(ecrf.getActive());
			values[enableBrowserFieldCalculationColumnIndex] = Boolean.toString(ecrf.getEnableBrowserFieldCalculation());
			values[externalIdColumnIndex] = ecrf.getExternalId();
			values[nameColumnIndex] = ecrf.getName();
			values[titleColumnIndex] = ecrf.getTitle();
			values[descriptionColumnIndex] = ecrf.getDescription();
			values[enrollmentStatusColumnIndex] = (ecrf.getProbandListStatus() != null ? ecrf.getProbandListStatus().getNameL10nKey() : null);
			printRow(values);
		}
	}
}
