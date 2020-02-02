package org.phoenixctms.ctsms.email;

public interface MassMailMessageTemplateParameters {

	public static final String TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR = "_";
	public static final boolean TEMPLATE_MODEL_LOWER_CASE_FIELD_NAMES = true;
	public static final String MASS_MAIL_PREFIX = "mail" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String TRIAL_PREFIX = "trial" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String PROBAND_PREFIX = "proband" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String TRIAL_TAG_VALUES = TRIAL_PREFIX + "tagvalues";
	public static final String PROBAND_LIST_ENTRY_OUT_VO_PREFIX = "listentry" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String PROBAND_LIST_ENTRY_TAG_VALUES = PROBAND_LIST_ENTRY_OUT_VO_PREFIX + "tagvalues";
	public static final Object PROBAND_LIST_ENTRY_TAG_VALUES_VALUE = "value";
	public static final String TRIAL_INVENTORY_BOOKINGS = TRIAL_PREFIX + "bookings";
	public static final String PROBAND_INVENTORY_BOOKINGS = PROBAND_PREFIX + "bookings";
	public static final String PROBAND_SALUTATION = PROBAND_PREFIX + "salutation";
	public final static String PROBAND_PHYSICIAN_PREFIX = PROBAND_PREFIX + "physician" + TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR;
	public static final String PHYSICIAN_SALUTATION = PROBAND_PHYSICIAN_PREFIX + "salutation";
	public static final String SUBJECT = MASS_MAIL_PREFIX + "subject";
	public static final String GENERATED_ON = MASS_MAIL_PREFIX + "generated_on";
	public static final String INSTANCE_NAME = "instance_name";
	public static final String HTTP_BASE_URL = "http_base_url";
	public static final String HTTP_DOMAIN_NAME = "http_domain_name";
	public static final String STRING_UTILS = "string_utils";
	public static final Object MASS_MAIL_BEACON_UNSUBSCRIBE_URL = MASS_MAIL_PREFIX + "unsubscribe_url";
	public static final Object PROBAND_BEACON_UNSUBSCRIBE_URL = PROBAND_PREFIX + "unsubscribe_url";
	public static final String PROBAND_TAG_VALUES = PROBAND_PREFIX + "tagvalues";
	public static final String PROBAND_CONTACT_DETAIL_VALUES = PROBAND_PREFIX + "contactdetailvalues";
	public static final String PROBAND_ADDRESSES = PROBAND_PREFIX + "addresses";
	public static final String DIAGNOSES = PROBAND_PREFIX + "diagnoses";
	public static final String PROCEDURES = PROBAND_PREFIX + "procedures";
	public static final String MEDICATIONS = PROBAND_PREFIX + "medications";
	public static final String BANK_ACCOUNTS = PROBAND_PREFIX + "bankaccounts";
	public static final String PROBAND_CREATED_DATE = PROBAND_PREFIX + "create_date";
}
