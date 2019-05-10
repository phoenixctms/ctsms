package org.phoenixctms.ctsms.executable.migration;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterOverride;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterRestriction;
import org.phoenixctms.ctsms.executable.csv.PermissionDefinitionLineProcessor;
import org.phoenixctms.ctsms.util.CommonUtil;

public class UserIdentityDepartmentPermissionDefinitionLineProcessor extends PermissionDefinitionLineProcessor {

	public UserIdentityDepartmentPermissionDefinitionLineProcessor() {
		super();
	}

	private boolean addProfiles(String serviceMethod, ArrayList<String> profiles, int i) {
		boolean result = false;
		if (i < profiles.size()) {
			String newProfile = buildNewProfile(profiles.get(i), false);
			if (!CommonUtil.isEmptyString(newProfile)) {
				profiles.add(i, profiles.get(i));
				profiles.set(i + 1, newProfile);
				result = true;
			}
		}
		return result;
	}

	private String buildNewProfile(String profiles, boolean addExisitng) {
		StringBuilder newProfile = new StringBuilder();
		String[] profileNames = Pattern.compile(getProfileSplitRegexpPattern()).split(profiles, -1);
		for (int j = 0; j < profileNames.length; j++) {
			if (profileNames[j].length() > 0 && !profileNames[j].startsWith(getExcludeRestrictionOverrideProfileChar())) {
				PermissionProfile profile = PermissionProfile.fromString(profileNames[j]);
				if (addExisitng) {
					if (newProfile.length() > 0) {
						newProfile.append(":");
					}
					newProfile.append(profile.toString());
				}
				switch (profile) {
					case INVENTORY_MASTER_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.INVENTORY_MASTER_IDENTITY_DEPARTMENT.toString());
						break;
					case INVENTORY_DETAIL_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.INVENTORY_DETAIL_IDENTITY_DEPARTMENT.toString());
						break;
					case INVENTORY_VIEW_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.INVENTORY_VIEW_IDENTITY_DEPARTMENT.toString());
						break;
					case STAFF_MASTER_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.STAFF_MASTER_IDENTITY_DEPARTMENT.toString());
						break;
					case STAFF_DETAIL_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.STAFF_DETAIL_IDENTITY_DEPARTMENT.toString());
						break;
					case STAFF_VIEW_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.STAFF_VIEW_IDENTITY_DEPARTMENT.toString());
						break;
					case COURSE_MASTER_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.COURSE_MASTER_IDENTITY_DEPARTMENT.toString());
						break;
					case COURSE_DETAIL_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.COURSE_DETAIL_IDENTITY_DEPARTMENT.toString());
						break;
					case COURSE_VIEW_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.COURSE_VIEW_IDENTITY_DEPARTMENT.toString());
						break;
					case TRIAL_MASTER_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.TRIAL_MASTER_IDENTITY_DEPARTMENT.toString());
						break;
					case TRIAL_DETAIL_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.TRIAL_DETAIL_IDENTITY_DEPARTMENT.toString());
						break;
					case TRIAL_VIEW_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.TRIAL_VIEW_IDENTITY_DEPARTMENT.toString());
						break;
					case MASS_MAIL_MASTER_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.MASS_MAIL_MASTER_IDENTITY_DEPARTMENT.toString());
						break;
					case MASS_MAIL_DETAIL_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.MASS_MAIL_DETAIL_IDENTITY_DEPARTMENT.toString());
						break;
					case MASS_MAIL_VIEW_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.MASS_MAIL_VIEW_IDENTITY_DEPARTMENT.toString());
						break;
					case USER_USER_DEPARTMENT:
						if (newProfile.length() > 0) {
							newProfile.append(":");
						}
						newProfile.append(PermissionProfile.USER_IDENTITY_DEPARTMENT.toString());
						break;
					default:
				}
			}
		}
		return newProfile.toString();
	}

	@Override
	protected void injectOverrides(String serviceMethod, ArrayList<String> profiles, ArrayList<ServiceMethodParameterOverride> overrides) {
		ArrayList<String> newProfiles = new ArrayList<String>(profiles);
		ArrayList<ServiceMethodParameterOverride> newOverrides = new ArrayList<ServiceMethodParameterOverride>();
		int j = 0;
		for (int i = 0; i < overrides.size(); i++) {
			switch (overrides.get(i)) {
				case USER_DEPARTMENT_ID:
					newOverrides.add(overrides.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newOverrides.add(ServiceMethodParameterOverride.IDENTITY_DEPARTMENT_ID);
						j++;
					}
					break;
				case USER_DEPARTMENT_ID_FILTER:
					newOverrides.add(overrides.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newOverrides.add(ServiceMethodParameterOverride.IDENTITY_DEPARTMENT_ID_FILTER);
						j++;
					}
					break;
				default:
					newOverrides.add(overrides.get(i));
			}
			j++;
		}
		profiles.clear();
		profiles.addAll(newProfiles);
		overrides.clear();
		overrides.addAll(newOverrides);
	}

	@Override
	protected void injectProfiles(String serviceMethod, ArrayList<String> profiles) {
		// ArrayList<String> newProfiles = new ArrayList<String>(profiles);
		profiles.set(0, buildNewProfile(profiles.get(0), true));
		// addProfiles(serviceMethod, newProfiles, 0);
		// profiles.clear();
		// profiles.addAll(newProfiles);
	}

	@Override
	protected void injectRestrictions(String serviceMethod, ArrayList<String> profiles, ArrayList<ServiceMethodParameterRestriction> restrictions) {
		ArrayList<String> newProfiles = new ArrayList<String>(profiles);
		ArrayList<ServiceMethodParameterRestriction> newRestrictions = new ArrayList<ServiceMethodParameterRestriction>();
		int j = 0;
		for (int i = 0; i < restrictions.size(); i++) {
			switch (restrictions.get(i)) {
				case USER_DEPARTMENT:
					newRestrictions.add(restrictions.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newRestrictions.add(ServiceMethodParameterRestriction.IDENTITY_DEPARTMENT);
						j++;
					}
					break;
				case INVENTORY_USER_DEPARTMENT:
					newRestrictions.add(restrictions.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newRestrictions.add(ServiceMethodParameterRestriction.INVENTORY_IDENTITY_DEPARTMENT);
						j++;
					}
					break;
				case STAFF_USER_DEPARTMENT:
					newRestrictions.add(restrictions.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newRestrictions.add(ServiceMethodParameterRestriction.STAFF_IDENTITY_DEPARTMENT);
						j++;
					}
					break;
				case COURSE_USER_DEPARTMENT:
					newRestrictions.add(restrictions.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newRestrictions.add(ServiceMethodParameterRestriction.COURSE_IDENTITY_DEPARTMENT);
						j++;
					}
					break;
				case TRIAL_USER_DEPARTMENT:
					newRestrictions.add(restrictions.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newRestrictions.add(ServiceMethodParameterRestriction.TRIAL_IDENTITY_DEPARTMENT);
						j++;
					}
					break;
				case USER_USER_DEPARTMENT:
					newRestrictions.add(restrictions.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newRestrictions.add(ServiceMethodParameterRestriction.USER_IDENTITY_DEPARTMENT);
						j++;
					}
					break;
				case MASS_MAIL_USER_DEPARTMENT:
					newRestrictions.add(restrictions.get(i));
					if (addProfiles(serviceMethod, newProfiles, j)) {
						newRestrictions.add(ServiceMethodParameterRestriction.MASS_MAIL_IDENTITY_DEPARTMENT);
						j++;
					}
					break;
				default:
					newRestrictions.add(restrictions.get(i));
			}
			j++;
		}
		profiles.clear();
		profiles.addAll(newProfiles);
		restrictions.clear();
		restrictions.addAll(newRestrictions);
	}

	@Override
	protected boolean isWrite() {
		return false;
	}
}
