package org.phoenixctms.ctsms.util;

public interface AuthorisationExceptionCodes {

	public final static String CRITERIA_MODIFIED_DIFFERENT_NUMBER_OF_CRITERIONS = "criteria_modified_different_number_of_criterions";
	public final static String CRITERIA_MODIFIED_DIFFERENT_PROPERTY = "criteria_modified_different_property";
	public final static String CRITERIA_MODIFIED_DIFFERENT_TIE = "criteria_modified_different_tie";
	public final static String CRITERIA_NOT_SAVED = "criteria_not_saved";
	public final static String HOST_NOT_ALLOWED_OR_UNKNOWN_HOST = "host_not_allowed_or_unknown_host";
	public final static String NO_HOST = "no_host";
	public static final String NO_IDENTITY = "no_identity";
	public static final String NO_PERMISSIONS = "no_permissions";
	public final static String NOT_AUTHENTICATED = "not_authenticated";
	public static final String PARAMETER_DISJUNCTIVE_RESTRICTION_NOT_SATISFIED = "parameter_disjunctive_restriction_not_satisfied";
	public static final String PARAMETER_RESTRICTION_VIOLATED = "parameter_restriction_violated";
	public final static String FILE_NOT_PUBLIC = "file_not_public";
	public final static String ENCRYPTED_FILE = "encrypted_file";
}
