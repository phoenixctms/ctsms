package org.phoenixctms.ctsms.executable.csv;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenixctms.ctsms.domain.Permission;
import org.phoenixctms.ctsms.domain.PermissionDao;
import org.phoenixctms.ctsms.domain.ProfilePermission;
import org.phoenixctms.ctsms.domain.ProfilePermissionDao;
import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterOverride;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterRestriction;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterTransformation;
import org.phoenixctms.ctsms.security.IPAddressValidation;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionDefinitionLineProcessor extends LineProcessor {

	private final static int SERVICE_METHOD_COLUMN_INDEX = 0;
	private final static int IP_RANGES_COLUMN_INDEX = 1;
	private final static int PARAMETER_GETTER_COLUMN_INDEX = 2;
	private final static int TRANSFORMATION_COLUMN_INDEX = 3;
	private final static int RESTRICTIONS_COLUMN_INDEX = 4;
	private final static int DISJUNCTION_GROUP_COLUMN_INDEX = 5;
	private final static int PARAMETER_SETTER_COLUMN_INDEX = 6;
	private final static int OVERRIDES_COLUMN_INDEX = 7;
	private final static int PROFILES_COLUMN_INDEX = 8;
	private final static String DEFAULT_RESTRICTION_OVERRIDE_PROFILE_SEPARATOR_REGEXP_PATTERN = " *, *";
	private final static String DEFAULT_PROFILE_SPLIT_REGEXP_PATTERN = " *: *";
	private static final String DEFAULT_EXCLUDE_RESTRICTION_OVERRIDE_PROFILE_CHAR = "/";

	private static Collection<String> SERVICE_METHOD_NAMES = getServiceMethodNames();

	private static void addServiceMethodName(Collection<String> serviceMethodNames, Class serviceClass) {
		Method[] serviceMethods = serviceClass.getMethods();
		for (int i = 0; i < serviceMethods.length; i++) {
			serviceMethodNames.add(CoreUtil.getServiceMethodName(serviceMethods[i]));
		}
	}

	private static void checkValidIpRanges(String ipRanges) {
		Iterator<String[]> it = IPAddressValidation.splitIpRanges(ipRanges).iterator();
		while (it.hasNext()) {
			String[] ips = it.next();
			if (ips.length == 1 || ips.length == 2) {
				if (!IPAddressValidation.isValidIpRange(ips)) {
					if (ips.length == 1) {
						throw new IllegalArgumentException("invalid IP: " + ips[0]);
					} else {
						throw new IllegalArgumentException("invalid IP range: " + ips[0] + "-" + ips[1]);
					}
				}
			} else {
				throw new IllegalArgumentException("invalid IP range format: " + ipRanges);
			}
		}
	}

	private static ArrayList<Permission> generatePermissionsFromOverrides(
			String ipRanges,
			String parameterSetter,
			ArrayList<ServiceMethodParameterOverride> overrides) {
		ArrayList<Permission> result = new ArrayList<Permission>(overrides.size());
		Iterator<ServiceMethodParameterOverride> overridesIt = overrides.iterator();
		while (overridesIt.hasNext()) {
			result.add(Permission.Factory.newInstance(null, ipRanges, null, null, null, null, parameterSetter, overridesIt.next(), new ArrayList<ProfilePermission>()));
		}
		return result;
	}

	private static ArrayList<Permission> generatePermissionsFromRestrictions(
			String ipRanges,
			String parameterGetter,
			ServiceMethodParameterTransformation transformation,
			ArrayList<ServiceMethodParameterRestriction> restrictions,
			String disjunctionGroup) {
		ArrayList<Permission> result = new ArrayList<Permission>(restrictions.size());
		Iterator<ServiceMethodParameterRestriction> restrictionsIt = restrictions.iterator();
		while (restrictionsIt.hasNext()) {
			result.add(Permission.Factory.newInstance(null, ipRanges, parameterGetter, transformation, restrictionsIt.next(), disjunctionGroup, null, null,
					new ArrayList<ProfilePermission>()));
		}
		return result;
	}

	private static Collection<String> getServiceMethodNames() {
		HashSet<String> serviceMethods = new HashSet<String>();
		Iterator<Class> authorizedServiceClassesIt = ExecUtil.AUTHORIZED_SERVICE_CLASSES.iterator();
		while (authorizedServiceClassesIt.hasNext()) {
			addServiceMethodName(serviceMethods, authorizedServiceClassesIt.next());
		}
		return serviceMethods;
	}
	@Autowired
	protected PermissionDao permissionDao;
	@Autowired
	protected ProfilePermissionDao profilePermissionDao;
	private TreeMap<String, ArrayList<String[]>> serviceMethodMap;
	private Pattern restrictionOverrideProfileSeparatorRegexp;
	private Pattern profileSplitRegexp;
	private String excludeRestrictionOverrideProfileChar;

	private ArrayList<Permission> allPermissions;



	public PermissionDefinitionLineProcessor() {
		super();
		serviceMethodMap = new TreeMap<String, ArrayList<String[]>>();
	}

	private ArrayList<Permission> createPermissionAndProfilePermissions(
			String serviceMethod,
			ArrayList<Permission> permissions,
			ArrayList<String> profiles,
			HashSet<PermissionProfile> profileIdentifiers) {
		ArrayList<Permission> result = new ArrayList<Permission>();
		ArrayList<ProfilePermission> profilePermissions = new ArrayList<ProfilePermission>();
		if (permissions.size() == 0) {
			Permission permission = Permission.Factory.newInstance(serviceMethod);
			if (isWrite()) {
				permission = permissionDao.create(permission);
			}
			profilePermissions.addAll(createProfilePermissions(profiles, permission, 0, profileIdentifiers));
			result.add(permission);
		} else {
			int i = 0;
			Iterator<Permission> permissionsIt = permissions.iterator();
			while (permissionsIt.hasNext()) {
				Permission permission = permissionsIt.next();
				permission.setId(null);
				permission.setServiceMethod(serviceMethod);
				if (isWrite()) {
					permission = permissionDao.create(permission);
				}
				profilePermissions.addAll(createProfilePermissions(profiles, permission, i, profileIdentifiers));
				result.add(permission);
				i++;
			}
		}
		return result;
	}

	private ArrayList<ProfilePermission> createProfilePermissions(ArrayList<String> profileNameLists, Permission permission, int i, HashSet<PermissionProfile> profiles) {
		ArrayList<ProfilePermission> result = new ArrayList<ProfilePermission>();
		if (profileNameLists.size() > i) {
			String[] profileNames = profileSplitRegexp.split(profileNameLists.get(i), -1);
			for (int j = 0; j < profileNames.length; j++) {
				if (profileNames[j].length() > 0 && !profileNames[j].startsWith(excludeRestrictionOverrideProfileChar)) {
					PermissionProfile profile = PermissionProfile.fromString(profileNames[j]);
					ProfilePermission profilePermission = ProfilePermission.Factory.newInstance();
					profilePermission.setActive(true);
					profilePermission.setProfile(profile);
					profilePermission.setPermission(permission);
					permission.addProfilePermissions(profilePermission);// XXX
					if (isWrite()) {
						profilePermission = profilePermissionDao.create(profilePermission);
					}
					result.add(profilePermission);
					profiles.add(profile);
				}
			}
		}
		return result;
	}

	private String getDisjunctionGroup(String[] values) {
		return values[DISJUNCTION_GROUP_COLUMN_INDEX];
	}

	public String getExcludeRestrictionOverrideProfileChar() {
		return excludeRestrictionOverrideProfileChar;
	}

	private String getIpRanges(String[] values) {
		return values[IP_RANGES_COLUMN_INDEX];
	}

	private String getOverrides(String[] values) {
		return values[OVERRIDES_COLUMN_INDEX];
	}

	private String getParameterGetter(String[] values) {
		return values[PARAMETER_GETTER_COLUMN_INDEX];
	}

	private String getParameterSetter(String[] values) {
		return values[PARAMETER_SETTER_COLUMN_INDEX];
	}

	public Collection<Permission> getPermissions() {
		if (allPermissions == null) {
			allPermissions = new ArrayList<Permission>();
		}
		return allPermissions;
	}

	private String getProfiles(String[] values) {
		return values[PROFILES_COLUMN_INDEX];
	}

	public String getProfileSplitRegexpPattern() {
		return profileSplitRegexp.pattern();
	}

	public String getRestrictionOverrideProfileSeparatorRegexpPattern() {
		return restrictionOverrideProfileSeparatorRegexp.pattern();
	}

	private String getRestrictions(String[] values) {
		return values[RESTRICTIONS_COLUMN_INDEX];
	}


	private String getServiceMethod(String[] values) {
		return values[SERVICE_METHOD_COLUMN_INDEX];
	}

	private String getTransformation(String[] values) {
		return values[TRANSFORMATION_COLUMN_INDEX];
	}



	@Override
	public void init() {
		super.init();
		this.setRestrictionOverrideProfileSeparatorRegexpPattern(DEFAULT_RESTRICTION_OVERRIDE_PROFILE_SEPARATOR_REGEXP_PATTERN);
		setProfileSplitRegexpPattern(DEFAULT_PROFILE_SPLIT_REGEXP_PATTERN);
		setExcludeRestrictionOverrideProfileChar(DEFAULT_EXCLUDE_RESTRICTION_OVERRIDE_PROFILE_CHAR);
		serviceMethodMap.clear();
	}

	protected void injectOverrides(String serviceMethod, ArrayList<String> profiles, ArrayList<ServiceMethodParameterOverride> overrides) {
	}

	protected void injectProfiles(String serviceMethod, ArrayList<String> profiles) {
	}

	protected void injectRestrictions(String serviceMethod, ArrayList<String> profiles, ArrayList<ServiceMethodParameterRestriction> restrictions) {
	}

	protected boolean isWrite() {
		return true;
	}

	@Override
	protected int lineHashCode(String[] values) {
		return new HashCodeBuilder(1249046965, -82296885)
				.append(getServiceMethod(values))
				.append(getParameterGetter(values))
				.append(getTransformation(values))
				.append(getRestrictions(values))
				.append(getDisjunctionGroup(values))
				.append(getParameterSetter(values))
				.append(getOverrides(values))
				.toHashCode();
	}

	@Override
	protected void postProcess() {
		HashSet<PermissionProfile> allProfileIdentifiers = new HashSet<PermissionProfile>();
		getPermissions().clear();
		Iterator<String> serviceMethodsIt = serviceMethodMap.keySet().iterator();
		while (serviceMethodsIt.hasNext()) {
			String serviceMethod = serviceMethodsIt.next();
			ArrayList<String[]> rows = serviceMethodMap.get(serviceMethod);
			HashSet<PermissionProfile> serviceMethodProfileIdentifiers = new HashSet<PermissionProfile>();
			ArrayList<Permission> serviceMethodPermissions = new ArrayList<Permission>();
			Iterator<String[]> rowsIt = rows.iterator();
			while (rowsIt.hasNext()) {
				String[] values = rowsIt.next();
				ArrayList<Permission> permissions = new ArrayList<Permission>();
				String[] profileNames = restrictionOverrideProfileSeparatorRegexp.split(getProfiles(values), -1);
				ArrayList<String> profiles = new ArrayList<String>();
				for (int i = 0; i < profileNames.length; i++) {
					profiles.add(profileNames[i]);
				}
				// injectProfiles(serviceMethod, profiles);
				String parameterGetter = getParameterGetter(values);
				String parameterSetter = getParameterSetter(values);
				if (!CommonUtil.isEmptyString(parameterGetter)) {
					String transformationName = getTransformation(values);
					ServiceMethodParameterTransformation transformation = !CommonUtil.isEmptyString(transformationName) ? ServiceMethodParameterTransformation
							.fromString(transformationName) : null;
							String[] restrictionNames = restrictionOverrideProfileSeparatorRegexp.split(getRestrictions(values), -1);
							ArrayList<ServiceMethodParameterRestriction> restrictions = new ArrayList<ServiceMethodParameterRestriction>();
							for (int i = 0; i < restrictionNames.length; i++) {
								if (restrictionNames[i].length() > 0 && !restrictionNames[i].startsWith(excludeRestrictionOverrideProfileChar)) {
									restrictions.add(ServiceMethodParameterRestriction.fromString(restrictionNames[i]));
								}
							}
							injectRestrictions(serviceMethod, profiles, restrictions);
							if (profiles.size() > restrictions.size()) {
								throw new IllegalArgumentException("more profiles than restrictions for service method " + serviceMethod + " parameter " + parameterGetter);
							}
							String disjunctionGroup = !CommonUtil.isEmptyString(getDisjunctionGroup(values)) ? getDisjunctionGroup(values) : null;
							permissions.addAll(generatePermissionsFromRestrictions(getIpRanges(values), parameterGetter, transformation, restrictions, disjunctionGroup));
				} else if (!CommonUtil.isEmptyString(parameterSetter)) {
					String[] overrideNames = restrictionOverrideProfileSeparatorRegexp.split(getOverrides(values), -1);
					ArrayList<ServiceMethodParameterOverride> overrides = new ArrayList<ServiceMethodParameterOverride>();
					for (int i = 0; i < overrideNames.length; i++) {
						if (overrideNames[i].length() > 0 && !overrideNames[i].startsWith(excludeRestrictionOverrideProfileChar)) {
							overrides.add(ServiceMethodParameterOverride.fromString(overrideNames[i]));
						}
					}
					injectOverrides(serviceMethod, profiles, overrides);
					if (profiles.size() > overrides.size()) {
						throw new IllegalArgumentException("more profiles than overrides for service method " + serviceMethod + " parameter " + parameterSetter);
					}
					permissions.addAll(generatePermissionsFromOverrides(getIpRanges(values), parameterSetter, overrides));
				} else {
					// injectNoGetterSetter(serviceMethod, profiles);
					injectProfiles(serviceMethod, profiles);
					if (profiles.size() > 1) {
						throw new IllegalArgumentException("more than one profile for service method " + serviceMethod);
					}
				}
				serviceMethodPermissions.addAll(createPermissionAndProfilePermissions(serviceMethod, permissions, profiles, serviceMethodProfileIdentifiers));
			}
			jobOutput.println(serviceMethod + ": " + serviceMethodPermissions.size() + " permissions created, used in " + serviceMethodProfileIdentifiers.size() + " profiles");
			getPermissions().addAll(serviceMethodPermissions);
			allProfileIdentifiers.addAll(serviceMethodProfileIdentifiers);
		}
		jobOutput.println(getPermissions().size() + " permissions created overall, used in " + allProfileIdentifiers.size() + " profiles");
	}

	@Override
	protected int processLine(String[] values, long lineNumber) {
		if (!SERVICE_METHOD_NAMES.contains(getServiceMethod(values))) {
			throw new IllegalArgumentException("unknown service method " + getServiceMethod(values));
		}
		checkValidIpRanges(getIpRanges(values));
		ArrayList<String[]> rows;
		if (serviceMethodMap.containsKey(getServiceMethod(values))) {
			rows = serviceMethodMap.get(getServiceMethod(values));
		} else {
			rows = new ArrayList<String[]>();
			serviceMethodMap.put(getServiceMethod(values), rows);
		}
		rows.add(values);
		return 1;
	}

	public void setExcludeRestrictionOverrideProfileChar(
			String excludeRestrictionOverrideProfileChar) {
		this.excludeRestrictionOverrideProfileChar = excludeRestrictionOverrideProfileChar;
	}

	public void setProfileSplitRegexpPattern(String profileSplitRegexpPattern) {
		this.profileSplitRegexp = Pattern.compile(profileSplitRegexpPattern);
	}

	public void setRestrictionOverrideProfileSeparatorRegexpPattern(String restrictionOverrideProfileSeparatorRegexpPattern) {
		this.restrictionOverrideProfileSeparatorRegexp = Pattern.compile(restrictionOverrideProfileSeparatorRegexpPattern);
	}

	@Override
	protected boolean testNotNullRowFields(String[] values, long lineNumber) {
		if (CommonUtil.isEmptyString(getServiceMethod(values))) {
			jobOutput.println("line " + lineNumber + ": empty service method");
			return false;
		}
		return true;
	}
}
