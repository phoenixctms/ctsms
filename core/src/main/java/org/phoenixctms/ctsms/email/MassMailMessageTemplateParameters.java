package org.phoenixctms.ctsms.email;

public interface MassMailMessageTemplateParameters {

	public static final String TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR = "_";
	public static final boolean TEMPLATE_MODEL_LOWER_CASE_FIELD_NAMES = true;
	public static final String MASS_MAIL_PREFIX = "mail" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String TRIAL_PREFIX = "trial" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String PROBAND_PREFIX = ""; // "trial" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	// public static final String TRIAL = "trial";
	// public static final String PROBAND = "proband";
	public static final String TRIAL_TAG_VALUES = "trial_tagvalues";
	public static final String PROBAND_LIST_ENTRY_OUT_VO_PREFIX = "trial_listentry" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String PROBAND_LIST_ENTRY_TAG_VALUES = "trial_listentry_tagvalues";
	public static final Object PROBAND_LIST_ENTRY_TAG_VALUES_VALUE = "value";
	public static final String TRIAL_INVENTORY_BOOKINGS = "trial_bookings";
	public static final String PROBAND_INVENTORY_BOOKINGS = "proband_bookings";
	public static final String PROBAND_SALUTATION = "proband_salutation";
	public static final String PHYSICIAN_SALUTATION = "physician_salutation";
	public static final String SUBJECT = "subject";
	public static final String GENERATED_ON = "generated_on";
	public static final String INSTANCE_NAME = "instance_name";
	public static final String HTTP_BASE_URL = "http_base_url";
	public static final String HTTP_DOMAIN_NAME = "http_domain_name";
	public static final String STRING_UTILS = "string_utils";
	public static final Object MASS_MAIL_BEACON_UNSUBSCRIBE_URL = "mail_unsubscribe_url";
	public static final Object PROBAND_BEACON_UNSUBSCRIBE_URL = "proband_unsubscribe_url";
	public static final String PROBAND_TAG_VALUES = "proband_tagvalues";
	public static final String PROBAND_CONTACT_DETAIL_VALUES = "proband_contactdetailvalues";
	public static final String PROBAND_ADDRESSES = "proband_addresses";
	public static final String DIAGNOSES = "diagnoses";
	public static final String PROCEDURES = "procedures";
	public static final String MEDICATIONS = "medications";
	public static final String BANK_ACCOUNTS = "bankaccounts";


}
