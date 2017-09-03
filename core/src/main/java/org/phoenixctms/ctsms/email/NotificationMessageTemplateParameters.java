package org.phoenixctms.ctsms.email;

public interface NotificationMessageTemplateParameters {

	public static final String TEMPLATE_MODEL_FIELD_NAME_ASSOCIATION_PATH_SEPARATOR = "_";
	public static final boolean TEMPLATE_MODEL_LOWER_CASE_FIELD_NAMES = true;
	public static final String NUMBER_OF_PROBANDS_DELETED = "number_of_probands_deleted";
	public static final String NOTIFICATION = "notification";
	public static final String PROBAND_AUTO_DELETE_DAYS_LEFT = "proband_auto_delete_days_left";
	public static final String PASSWORD_EXPIRATION_DAYS_LEFT = "password_expiration_days_left";
	public static final String COURSE_EXPIRATION_DAYS_LEFT = "course_expiration_days_left";
	public static final String GENERATED_ON = "generated_on";
	public static final String NEW_USER = "new_user";
	public static final String LOCAL_AUTH_METHOD = "local_auth_method";
	public static final String LDAP_AUTH_METHOD = "ldap_auth_method";
	public static final String INSTANCE_NAME = "instance_name";
	public static final String HTTP_BASE_URL = "http_base_url";
	public static final String HTTP_DOMAIN_NAME = "http_domain_name";
	public static final String INDEX_FIELD_LOG = "index_field_log";
}
