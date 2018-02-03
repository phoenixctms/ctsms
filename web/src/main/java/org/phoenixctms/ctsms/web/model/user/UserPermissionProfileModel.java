package org.phoenixctms.ctsms.web.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.PermissionProfile;
import org.phoenixctms.ctsms.enumeration.PermissionProfileGroup;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.PermissionProfileGroupVO;
import org.phoenixctms.ctsms.vo.PermissionProfileVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileInVO;
import org.phoenixctms.ctsms.vo.UserPermissionProfileOutVO;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class UserPermissionProfileModel implements Map<String, String> {

	private HashMap<PermissionProfileGroup, PermissionProfile> selectedUserPermissionProfileGroupMap;
	private ArrayList<PermissionProfileGroupVO> permissionProfileGroupVOs;
	private HashMap<PermissionProfileGroup, ArrayList<UserPermissionProfileInVO>> userPermissionProfileGroupMap;
	private HashMap<PermissionProfile, UserPermissionProfileInVO> userPermissionProfileMap;
	private HashMap<PermissionProfile, PermissionProfileVO> permissionProfileVOMap;

	public UserPermissionProfileModel() {
		super();
		selectedUserPermissionProfileGroupMap = new HashMap<PermissionProfileGroup, PermissionProfile>();
		permissionProfileGroupVOs = new ArrayList<PermissionProfileGroupVO>();
		userPermissionProfileGroupMap = new HashMap<PermissionProfileGroup, ArrayList<UserPermissionProfileInVO>>();
		userPermissionProfileMap = new HashMap<PermissionProfile, UserPermissionProfileInVO>();
		permissionProfileVOMap = new HashMap<PermissionProfile, PermissionProfileVO>();
	}

	@Override
	public void clear() {
		selectedUserPermissionProfileGroupMap.clear();
		permissionProfileGroupVOs.clear();
		userPermissionProfileGroupMap.clear();
		userPermissionProfileMap.clear();
		permissionProfileVOMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return selectedUserPermissionProfileGroupMap.containsKey(PermissionProfileGroup.fromString((String) key));
	}

	@Override
	public boolean containsValue(Object value) {
		return selectedUserPermissionProfileGroupMap.containsValue(value == null ? null : PermissionProfile.fromString((String) value));
	}

	public void copyUserPermissionProfilesOutToIn(Long userId, Collection<UserPermissionProfileOutVO> userPermissionProfiles) {
		init(userId);
		if (userId != null && userPermissionProfiles != null) {
			Iterator<UserPermissionProfileOutVO> userPermissionProfilesIt = userPermissionProfiles.iterator();
			while (userPermissionProfilesIt.hasNext()) {
				UserPermissionProfileOutVO out = userPermissionProfilesIt.next();
				if (userId == out.getUser().getId()) { // unboxed, ok
					PermissionProfile profile = out.getProfile().getProfile();
					PermissionProfileGroup group = out.getProfile().getProfileGroup().getProfileGroup();
					UserPermissionProfileInVO in = userPermissionProfileMap.get(profile);
					UserPermissionProfileBean.copyUserPermissionProfileOutToIn(in, out);
					if (out.getActive()) {
						selectedUserPermissionProfileGroupMap.put(group, profile);
					}
				}
			}
		}
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get(Object key) {
		PermissionProfile value = selectedUserPermissionProfileGroupMap.get(PermissionProfileGroup.fromString((String) key));
		return value == null ? null : value.name();
	}

	public ArrayList<PermissionProfileGroupVO> getPermissionProfileGroups() {
		return permissionProfileGroupVOs;
	}

	public ArrayList<SelectItem> getPermissionProfiles(String group) {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		if (group != null) {
			ArrayList<UserPermissionProfileInVO> groupProfiles = userPermissionProfileGroupMap.get(PermissionProfileGroup.fromString(group));
			if (groupProfiles != null) {
				Iterator<UserPermissionProfileInVO> profilesIt = groupProfiles.iterator();
				while (profilesIt.hasNext()) {
					PermissionProfile profile = profilesIt.next().getProfile();
					result.add(new SelectItem(profile.name(), permissionProfileVOMap.get(profile).getProfileName()));
				}
			}
		}
		return result;
	}

	public Set<UserPermissionProfileInVO> getPermissionProfilesIn() {
		HashSet<UserPermissionProfileInVO> result = new HashSet<UserPermissionProfileInVO>(userPermissionProfileMap.size());
		Iterator<java.util.Map.Entry<PermissionProfileGroup, ArrayList<UserPermissionProfileInVO>>> groupsIt = userPermissionProfileGroupMap.entrySet().iterator();
		while (groupsIt.hasNext()) {
			java.util.Map.Entry<PermissionProfileGroup, ArrayList<UserPermissionProfileInVO>> entry = groupsIt.next();
			PermissionProfileGroup group = entry.getKey();
			PermissionProfile selectedProfile = selectedUserPermissionProfileGroupMap.get(group);
			Iterator<UserPermissionProfileInVO> userPermissionProfilesIt = entry.getValue().iterator();
			while (userPermissionProfilesIt.hasNext()) {
				UserPermissionProfileInVO userPermissionProfileIn = new UserPermissionProfileInVO();
				userPermissionProfileIn.copy(userPermissionProfilesIt.next());
				// userPermissionProfileIn.setActive(selectedProfile.equals(userPermissionProfileIn.getProfile()));
				userPermissionProfileIn.setActive(userPermissionProfileIn.getProfile().equals(selectedProfile));
				result.add(userPermissionProfileIn);
			}
		}
		return result;
	}

	private void init(Long userId) {
		clear();
		Collection<PermissionProfileVO> permissionProfileVOs = null;
		try {
			permissionProfileVOs = WebUtil.getServiceLocator().getSelectionSetService().getPermissionProfiles(WebUtil.getAuthentication(), null);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (permissionProfileVOs != null) {
			Iterator<PermissionProfileVO> permissionProfileVOsIt = permissionProfileVOs.iterator();
			while (permissionProfileVOsIt.hasNext()) {
				PermissionProfileVO permissionProfileVO = permissionProfileVOsIt.next();
				PermissionProfile profile = permissionProfileVO.getProfile();
				PermissionProfileGroup group = permissionProfileVO.getProfileGroup().getProfileGroup();
				ArrayList<UserPermissionProfileInVO> userPermissionProfiles;
				if (userPermissionProfileGroupMap.containsKey(group)) {
					userPermissionProfiles = userPermissionProfileGroupMap.get(group);
				} else {
					userPermissionProfiles = new ArrayList<UserPermissionProfileInVO>();
					userPermissionProfileGroupMap.put(group, userPermissionProfiles);
					permissionProfileGroupVOs.add(permissionProfileVO.getProfileGroup());
					selectedUserPermissionProfileGroupMap.put(group, null);
				}
				UserPermissionProfileInVO userPermissionProfileIn = new UserPermissionProfileInVO();
				UserPermissionProfileBean.initUserPermissionProfileDefaultValues(userPermissionProfileIn, userId, profile);
				userPermissionProfiles.add(userPermissionProfileIn);
				userPermissionProfileMap.put(profile, userPermissionProfileIn);
				permissionProfileVOMap.put(profile, permissionProfileVO);
			}
		}
	}

	public void initUserPermissionProfilesDefaultValues(Long userId) {
		init(userId);
	}

	@Override
	public boolean isEmpty() {
		return selectedUserPermissionProfileGroupMap.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String put(String key,
			String value) {
		PermissionProfile oldValue = selectedUserPermissionProfileGroupMap.put(PermissionProfileGroup.fromString(key), value == null ? null : PermissionProfile.fromString(value));
		return oldValue == null ? null : oldValue.name();
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return selectedUserPermissionProfileGroupMap.size();
	}

	@Override
	public Collection<String> values() {
		throw new UnsupportedOperationException();
	}
}
