package org.phoenixctms.ctsms.test.xls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFField;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.fileprocessors.xls.RowProcessor;
import org.phoenixctms.ctsms.service.trial.TrialService;
import org.phoenixctms.ctsms.test.EcrfValidationTestVector;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.springframework.beans.factory.annotation.Autowired;

public class EcrfValidationRowProcessor extends RowProcessor {

	private final static String SHEET_NAME = "validation";
	private final static int ECRF_NAME_COLUMN_INDEX = 0;
	private final static int ECRF_REVISION_COLUMN_INDEX = 1;
	private final static int SECTION_COLUMN_INDEX = 2;
	private final static int POSITION_COLUMN_INDEX = 3;
	private final static int INDEX_COLUMN_INDEX = 4;
	private final static int INPUT_VALUE_COLUMN_INDEX = 5;
	private final static int EXPORTED_VALUE_COLUMN_INDEX = 6;
	private final static int EXPECTED_OUTPUT_COLUMN_INDEX = 7;
	private int ecrfNameColumnIndex;
	private int ecrfRevisionColumnIndex;
	private int sectionColumnIndex;
	private int positionColumnIndex;
	private int indexColumnIndex;
	private int inputValueColumnIndex;
	private int exportedValueColumnIndex;
	private int expectedOutputColumnIndex;
	private HashMap<String, HashMap<String, List<EcrfValidationTestVector>>> vectorMap;
	private HashMap<String, HashMap<String, ECRF>> ecrfMap;
	private ArrayList<EcrfValidationTestVector> vectors;
	@Autowired
	protected ECRFDao eCRFDao; // varnames must match bean ids in applicationContext.xml
	@Autowired
	protected ECRFFieldDao eCRFFieldDao;
	//@Autowired
	//protected InputFieldDao inputFieldDao;
	@Autowired
	protected TrialService trialService;

	public EcrfValidationRowProcessor() {
		super();
		filterDupes = false;
		acceptCommentsIndex = 0;
		vectorMap = new HashMap<String, HashMap<String, List<EcrfValidationTestVector>>>();
		ecrfMap = new HashMap<String, HashMap<String, ECRF>>();
		vectors = new ArrayList<EcrfValidationTestVector>();
	}

	public void clearEcrf(String ecrfName, String ecrfRevision) {
		if (ecrfMap.containsKey(ecrfName)) {
			HashMap<String, ECRF> revisionMap = ecrfMap.get(ecrfName);
			revisionMap.remove(ecrfRevision);
			if (revisionMap.isEmpty()) {
				ecrfMap.remove(ecrfName);
			}
		}
	}

	public ECRF getEcrf(String name, String revision) throws Exception {
		ECRF ecrf = null;
		HashMap<String, ECRF> revisionMap;
		if (ecrfMap.containsKey(name)) {
			revisionMap = ecrfMap.get(name);
		} else {
			revisionMap = new HashMap<String, ECRF>();
			ecrfMap.put(name, revisionMap);
		}
		if (revisionMap.containsKey(revision)) {
			ecrf = revisionMap.get(revision);
		} else {
			try {
				ecrf = eCRFDao.findCollidingTrialNameRevision(context.getEntityId(), name, revision).iterator().next();
			} catch (NoSuchElementException e) {
			}
			revisionMap.put(revision, ecrf);
		}
		return ecrf;
	}

	public List<EcrfValidationTestVector> getVectors(String ecrfName, String ecrfRevision) {
		HashMap<String, List<EcrfValidationTestVector>> revisionMap = vectorMap.get(ecrfName);
		if (revisionMap != null) {
			return revisionMap.get(ecrfRevision);
		}
		return null;
	}

	public List<EcrfValidationTestVector> getVectors() {
		return vectors;
	}

	private String getEcrfRevision(String[] values) {
		return getColumnValue(values, ecrfRevisionColumnIndex);
	}

	private String getEcrfName(String[] values) {
		return getColumnValue(values, ecrfNameColumnIndex);
	}
	//	private Long getInputFieldId(String inputFieldName) {
	//		InputField inputField = ((XlsImporter) context.getImporter()).getSelectionSetValueRowProcessor().getInputField(inputFieldName);
	//		Long fieldId = null;
	//		if (inputField != null) {
	//			fieldId = inputField.getId();
	//		} else {
	//			jobOutput.println("input field '" + inputFieldName + "' not found");
	//		}
	//		return fieldId;
	//	}

	private String getInputValue(String[] values) {
		return getColumnValue(values, inputValueColumnIndex);
	}

	private String getExportedValue(String[] values) {
		return getColumnValue(values, exportedValueColumnIndex);
	}

	private String getExpectedOutput(String[] values) {
		return getColumnValue(values, expectedOutputColumnIndex);
	}

	private String getPosition(String[] values) {
		return getColumnValue(values, positionColumnIndex);
	}

	private String getIndex(String[] values) {
		return getColumnValue(values, indexColumnIndex);
	}

	private String getSection(String[] values) {
		return getColumnValue(values, sectionColumnIndex);
	}

	@Override
	public String getSheetName() {
		return SHEET_NAME;
	}

	@Override
	public void init() throws Throwable {
		super.init();
		ecrfNameColumnIndex = ECRF_NAME_COLUMN_INDEX;
		ecrfRevisionColumnIndex = ECRF_REVISION_COLUMN_INDEX;
		sectionColumnIndex = SECTION_COLUMN_INDEX;
		positionColumnIndex = POSITION_COLUMN_INDEX;
		indexColumnIndex = INDEX_COLUMN_INDEX;
		inputValueColumnIndex = INPUT_VALUE_COLUMN_INDEX;
		exportedValueColumnIndex = EXPORTED_VALUE_COLUMN_INDEX;
		expectedOutputColumnIndex = EXPECTED_OUTPUT_COLUMN_INDEX;
		vectorMap.clear();
		ecrfMap.clear();
		vectors.clear();
		//		((XlsImporter) context.getImporter()).loadInputFields(context);
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getEcrfName(values))
				.append(getEcrfRevision(values))
				.append(getSection(values))
				.append(getPosition(values))
				.append(getIndex(values))
				.append(getInputValue(values))
				.append(getExportedValue(values))
				.append(getExpectedOutput(values))
				.toHashCode();
	}

	@Override
	public void postProcess() {
	}

	@Override
	protected int processRow(String[] values, long rowNumber) throws Throwable {
		String ecrfName = getEcrfName(values);
		String ecrfRevision = getEcrfRevision(values);
		ECRF ecrf = getEcrf(ecrfName, ecrfRevision);
		Long position = Long.parseLong(getPosition(values));
		String section = getSection(values);
		ECRFField ecrfField = eCRFFieldDao.findByEcrfSectionPosition(ecrf.getId(), section, position).iterator().next();
		ECRFFieldOutVO ecrfFieldVO = trialService.getEcrfField(context.getAuth(), ecrfField.getId());
		List<EcrfValidationTestVector> ecrfVectors;
		HashMap<String, List<EcrfValidationTestVector>> revisionMap;
		if (vectorMap.containsKey(ecrfName)) {
			revisionMap = vectorMap.get(ecrfName);
		} else {
			revisionMap = new HashMap<String, List<EcrfValidationTestVector>>();
			vectorMap.put(ecrfName, revisionMap);
		}
		if (revisionMap.containsKey(ecrfRevision)) {
			ecrfVectors = revisionMap.get(ecrfRevision);
		} else {
			ecrfVectors = new ArrayList<EcrfValidationTestVector>();
			revisionMap.put(ecrfRevision, ecrfVectors);
		}
		String label = "ecrf " + ecrfName + ", revision " + ecrfRevision
				+ ": field section " + section + ", position " + position + ", field '" + ecrfFieldVO.getField().getNameL10nKey() + "'";
		EcrfValidationTestVector v = new EcrfValidationTestVector();
		v.setInputValue(getInputValue(values) != null ? getInputValue(values) : "");
		v.setExportedValue(getExportedValue(values) != null ? getExportedValue(values) : "");
		v.setExpectedOutput(getExpectedOutput(values) != null ? getExpectedOutput(values) : "");
		v.setIndex(getIndex(values));
		v.setEcrfField(ecrfFieldVO);
		ecrfVectors.add(v);
		vectors.add(v);
		jobOutput.println(label + " input " + v.toString());
		return 1;
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long rowNumber) {
		if (CommonUtil.isEmptyString(getEcrfName(values))) {
			return false;
		}
		if (CommonUtil.isEmptyString(getPosition(values))) {
			jobOutput.println("row " + rowNumber + ": empty position");
			return false;
		}
		return true;
	}
}