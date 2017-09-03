package org.phoenixctms.ctsms.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;

public final class PermissionProfileGrouping {

	private static HashMap<PermissionProfileGroup, ArrayList<PermissionProfile>> permissionProfileMap = new HashMap<PermissionProfileGroup, ArrayList<PermissionProfile>>();
	private static HashMap<PermissionProfile, PermissionProfileGroup> permissionProfileGroupMap = new HashMap<PermissionProfile, PermissionProfileGroup>();
	static {
		addProfile(PermissionProfileGroup.INVENTORY, PermissionProfile.INVENTORY_MASTER_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.INVENTORY, PermissionProfile.INVENTORY_MASTER_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.INVENTORY, PermissionProfile.INVENTORY_DETAIL_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.INVENTORY, PermissionProfile.INVENTORY_DETAIL_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.INVENTORY, PermissionProfile.INVENTORY_VIEW_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.INVENTORY, PermissionProfile.INVENTORY_VIEW_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_MASTER_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_MASTER_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_MASTER_IDENTITY);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_MASTER_IDENTITY_CHILD);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_DETAIL_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_DETAIL_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_DETAIL_IDENTITY);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_DETAIL_IDENTITY_CHILD);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_VIEW_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_VIEW_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_VIEW_IDENTITY);
		addProfile(PermissionProfileGroup.STAFF, PermissionProfile.STAFF_VIEW_IDENTITY_CHILD);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_MASTER_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_MASTER_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_MASTER_LECTURER);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_DETAIL_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_DETAIL_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_DETAIL_LECTURER);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_VIEW_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_VIEW_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.COURSE, PermissionProfile.COURSE_VIEW_LECTURER);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_MASTER_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_MASTER_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_MASTER_TEAM_MEMBER);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_DETAIL_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_DETAIL_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_DETAIL_TEAM_MEMBER);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_VIEW_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_VIEW_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_VIEW_TEAM_MEMBER);
		addProfile(PermissionProfileGroup.TRIAL, PermissionProfile.TRIAL_SIGNUP);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_MASTER_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_MASTER_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_DETAIL_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_DETAIL_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_VIEW_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_VIEW_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_NO_ACCESS);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_DEVICE);
		addProfile(PermissionProfileGroup.PROBAND, PermissionProfile.PROBAND_SIGNUP);
		addProfile(PermissionProfileGroup.USER, PermissionProfile.USER_ALL_DEPARTMENTS);
		addProfile(PermissionProfileGroup.USER, PermissionProfile.USER_USER_DEPARTMENT);
		addProfile(PermissionProfileGroup.USER, PermissionProfile.USER_ACTIVE_USER);
		addProfile(PermissionProfileGroup.INPUT_FIELD, PermissionProfile.INPUT_FIELD_MASTER);
		addProfile(PermissionProfileGroup.INPUT_FIELD, PermissionProfile.INPUT_FIELD_VIEW);
		addProfile(PermissionProfileGroup.INVENTORY_SEARCH, PermissionProfile.INVENTORY_MASTER_SEARCH);
		addProfile(PermissionProfileGroup.INVENTORY_SEARCH, PermissionProfile.INVENTORY_SAVED_SEARCH);
		addProfile(PermissionProfileGroup.INVENTORY_SEARCH, PermissionProfile.INVENTORY_NO_SEARCH);
		addProfile(PermissionProfileGroup.STAFF_SEARCH, PermissionProfile.STAFF_MASTER_SEARCH);
		addProfile(PermissionProfileGroup.STAFF_SEARCH, PermissionProfile.STAFF_SAVED_SEARCH);
		addProfile(PermissionProfileGroup.STAFF_SEARCH, PermissionProfile.STAFF_NO_SEARCH);
		addProfile(PermissionProfileGroup.COURSE_SEARCH, PermissionProfile.COURSE_MASTER_SEARCH);
		addProfile(PermissionProfileGroup.COURSE_SEARCH, PermissionProfile.COURSE_SAVED_SEARCH);
		addProfile(PermissionProfileGroup.COURSE_SEARCH, PermissionProfile.COURSE_NO_SEARCH);
		addProfile(PermissionProfileGroup.TRIAL_SEARCH, PermissionProfile.TRIAL_MASTER_SEARCH);
		addProfile(PermissionProfileGroup.TRIAL_SEARCH, PermissionProfile.TRIAL_SAVED_SEARCH);
		addProfile(PermissionProfileGroup.TRIAL_SEARCH, PermissionProfile.TRIAL_NO_SEARCH);
		addProfile(PermissionProfileGroup.PROBAND_SEARCH, PermissionProfile.PROBAND_MASTER_SEARCH);
		addProfile(PermissionProfileGroup.PROBAND_SEARCH, PermissionProfile.PROBAND_SAVED_SEARCH);
		addProfile(PermissionProfileGroup.PROBAND_SEARCH, PermissionProfile.PROBAND_NO_SEARCH);
		addProfile(PermissionProfileGroup.USER_SEARCH, PermissionProfile.USER_MASTER_SEARCH);
		addProfile(PermissionProfileGroup.USER_SEARCH, PermissionProfile.USER_SAVED_SEARCH);
		addProfile(PermissionProfileGroup.USER_SEARCH, PermissionProfile.USER_NO_SEARCH);
		addProfile(PermissionProfileGroup.INPUT_FIELD_SEARCH, PermissionProfile.INPUT_FIELD_MASTER_SEARCH);
		addProfile(PermissionProfileGroup.INPUT_FIELD_SEARCH, PermissionProfile.INPUT_FIELD_SAVED_SEARCH);
		addProfile(PermissionProfileGroup.INPUT_FIELD_SEARCH, PermissionProfile.INPUT_FIELD_NO_SEARCH);
	}

	private static void addProfile(PermissionProfileGroup profileGroup, PermissionProfile profile) {
		ArrayList<PermissionProfile> profiles;
		if (permissionProfileMap.containsKey(profileGroup)) {
			profiles = permissionProfileMap.get(profileGroup);
		} else {
			profiles = new ArrayList<PermissionProfile>();
			permissionProfileMap.put(profileGroup, profiles);
		}
		profiles.add(profile);
		permissionProfileGroupMap.put(profile, profileGroup);
	}

	public static PermissionProfileGroup getGroupFromPermissionProfile(PermissionProfile profile) {
		return permissionProfileGroupMap.get(profile);
	}

	public static ArrayList<PermissionProfile> getProfilesFromPermissionProfileGroup(PermissionProfileGroup profileGroup) {
		if (permissionProfileMap.containsKey(profileGroup)) {
			return permissionProfileMap.get(profileGroup);
		} else {
			return new ArrayList<PermissionProfile>();
		}
	}

	private PermissionProfileGrouping() {
	}
}
