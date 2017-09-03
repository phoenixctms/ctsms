package org.phoenixctms.ctsms.util;

import java.util.Collection;
import java.util.HashSet;


public final class JSFVOConverterIDs {

	public final static String ALPHA_ID_CONVERTER_ID = "ctsms.AlphaId";
	public final static String OPS_CODE_CONVERTER_ID = "ctsms.OpsCode";
	public final static String ASP_CONVERTER_ID = "ctsms.Asp";
	public final static String ASP_SUBSTANCE_CONVERTER_ID = "ctsms.AspSubstance";
	public final static String ASP_ATC_CODE_CONVERTER_ID = "ctsms.AspAtcCode";
	public final static String INQUIRY_CONVERTER_ID = "ctsms.Inquiry";
	public final static String PROBAND_LIST_ENTRY_TAG_CONVERTER_ID = "ctsms.ProbandListEntryTag";
	public final static String ECRF_FIELD_CONVERTER_ID = "ctsms.EcrfField";
	public final static String INPUT_FIELD_CONVERTER_ID = "ctsms.InputField";
	public final static String INPUT_FIELD_SELECTION_SET_VALUE_CONVERTER_ID = "ctsms.InputFieldSelectionSetValue";
	public final static Collection<String> CONVERTER_IDS = new HashSet<String>();
	static {
		CONVERTER_IDS.add(ALPHA_ID_CONVERTER_ID);
		CONVERTER_IDS.add(OPS_CODE_CONVERTER_ID);
		CONVERTER_IDS.add(ASP_CONVERTER_ID);
		CONVERTER_IDS.add(ASP_SUBSTANCE_CONVERTER_ID);
		CONVERTER_IDS.add(ASP_ATC_CODE_CONVERTER_ID);
		CONVERTER_IDS.add(INQUIRY_CONVERTER_ID);
		CONVERTER_IDS.add(PROBAND_LIST_ENTRY_TAG_CONVERTER_ID);
		CONVERTER_IDS.add(ECRF_FIELD_CONVERTER_ID);
		CONVERTER_IDS.add(INPUT_FIELD_CONVERTER_ID);
		CONVERTER_IDS.add(INPUT_FIELD_SELECTION_SET_VALUE_CONVERTER_ID);
	}

	private JSFVOConverterIDs() {
	}
}
