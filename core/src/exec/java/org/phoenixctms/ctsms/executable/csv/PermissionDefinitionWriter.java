package org.phoenixctms.ctsms.executable.csv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import org.phoenixctms.ctsms.domain.Permission;
import org.phoenixctms.ctsms.domain.ProfilePermission;
import org.phoenixctms.ctsms.enumeration.ServiceMethodParameterTransformation;
import org.phoenixctms.ctsms.fileprocessors.csv.LineWriter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.ExecUtil;

public class PermissionDefinitionWriter extends LineWriter {

	private Collection<Permission> allPermissions;
	private HashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>>> getterMap;
	private HashMap<String, LinkedHashMap<String, LinkedHashMap<String, ArrayList<Permission>>>> setterMap;
	private HashMap<String, Permission> noGetterSetterMap;
	private LinkedHashSet<String> serviceMethods;

	public PermissionDefinitionWriter() {
		super();
		getterMap = new HashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>>>();
		setterMap = new HashMap<String, LinkedHashMap<String, LinkedHashMap<String, ArrayList<Permission>>>>();
		noGetterSetterMap = new HashMap<String, Permission>();
		serviceMethods = new LinkedHashSet<String>();
	}

	public Collection<Permission> getPermissions() {
		if (allPermissions == null) {
			allPermissions = new ArrayList<Permission>();
		}
		return allPermissions;
	}

	@Override
	public void printLines() {
		StringBuilder headComment = new StringBuilder();
		headComment.append("SERVICE_METHOD");
		headComment.append(getFieldSeparator());
		headComment.append("IP_RANGES");
		headComment.append(getFieldSeparator());
		headComment.append("PARAMETER_GETTER");
		headComment.append(getFieldSeparator());
		headComment.append("TRANSFORMATION");
		headComment.append(getFieldSeparator());
		headComment.append("RESTRICTIONS");
		headComment.append(getFieldSeparator());
		headComment.append("DISJUNCTION_GROUP");
		headComment.append(getFieldSeparator());
		headComment.append("PARAMETER_SETTER");
		headComment.append(getFieldSeparator());
		headComment.append("OVERRIDES");
		headComment.append(getFieldSeparator());
		headComment.append("PROFILES");
		printComment(headComment.toString());
		printBlankLine();
		Iterator<Permission> it = getPermissions().iterator();
		while (it.hasNext()) {
			Permission permission = it.next();
			serviceMethods.add(permission.getServiceMethod());
			if (!CommonUtil.isEmptyString(permission.getParameterGetter())) {
				LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>> transformationMap;
				if (getterMap.containsKey(permission.getServiceMethod())) {
					transformationMap = getterMap.get(permission.getServiceMethod());
				} else {
					transformationMap = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>>();
					getterMap.put(permission.getServiceMethod(), transformationMap);
				}
				LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>> disjunctionGroupMap;
				if (transformationMap.containsKey(permission.getParameterGetter())) {
					disjunctionGroupMap = transformationMap.get(permission.getParameterGetter());
				} else {
					disjunctionGroupMap = new LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>();
					transformationMap.put(permission.getParameterGetter(), disjunctionGroupMap);
				}
				LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>> restrictionMap;
				if (disjunctionGroupMap.containsKey(permission.getDisjunctionGroup())) {
					restrictionMap = disjunctionGroupMap.get(permission.getDisjunctionGroup());
				} else {
					restrictionMap = new LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>();
					disjunctionGroupMap.put(permission.getDisjunctionGroup(), restrictionMap);
				}
				LinkedHashMap<String, ArrayList<Permission>> ipRangeMap;
				if (restrictionMap.containsKey(permission.getTransformation())) {
					ipRangeMap = restrictionMap.get(permission.getTransformation());
				} else {
					ipRangeMap = new LinkedHashMap<String, ArrayList<Permission>>();
					restrictionMap.put(permission.getTransformation(), ipRangeMap);
				}
				ArrayList<Permission> list;
				if (ipRangeMap.containsKey(permission.getIpRanges())) {
					list = ipRangeMap.get(permission.getIpRanges());
				} else {
					list = new ArrayList<Permission>();
					ipRangeMap.put(permission.getIpRanges(), list);
				}
				list.add(permission);
			} else if (!CommonUtil.isEmptyString(permission.getParameterSetter())) {
				LinkedHashMap<String, LinkedHashMap<String, ArrayList<Permission>>> overrideMap;
				if (setterMap.containsKey(permission.getServiceMethod())) {
					overrideMap = setterMap.get(permission.getServiceMethod());
				} else {
					overrideMap = new LinkedHashMap<String, LinkedHashMap<String, ArrayList<Permission>>>();
					setterMap.put(permission.getServiceMethod(), overrideMap);
				}
				LinkedHashMap<String, ArrayList<Permission>> ipRangeMap;
				if (overrideMap.containsKey(permission.getParameterSetter())) {
					ipRangeMap = overrideMap.get(permission.getParameterSetter());
				} else {
					ipRangeMap = new LinkedHashMap<String, ArrayList<Permission>>();
					overrideMap.put(permission.getParameterSetter(), ipRangeMap);
				}
				ArrayList<Permission> list;
				if (ipRangeMap.containsKey(permission.getIpRanges())) {
					list = ipRangeMap.get(permission.getIpRanges());
				} else {
					list = new ArrayList<Permission>();
					ipRangeMap.put(permission.getIpRanges(), list);
				}
				list.add(permission);
			} else {
				noGetterSetterMap.put(permission.getServiceMethod(), permission);
			}
		}
		Iterator<Class> authorizedServiceClassesIt = ExecUtil.AUTHORIZED_SERVICE_CLASSES.iterator();
		while (authorizedServiceClassesIt.hasNext()) {
			printLines(authorizedServiceClassesIt.next());
			if (authorizedServiceClassesIt.hasNext()) {
				printBlankLine();
			}
		}
	}

	private void printLines(Class serviceClass) {
		Iterator<String> serviceMethodsIt = serviceMethods.iterator();
		int definitionsCount = 0;
		int serviceMethodsCount = 0;
		while (serviceMethodsIt.hasNext()) {
			String serviceMethodName = serviceMethodsIt.next();
			if (!serviceMethodName.startsWith(serviceClass.getName())) {
				continue;
			}
			if (getterMap.containsKey(serviceMethodName)) {
				Iterator<Entry<String, LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>>> gettersIt = getterMap
						.get(serviceMethodName).entrySet()
						.iterator();
				while (gettersIt.hasNext()) {
					Entry<String, LinkedHashMap<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>> getter = gettersIt
							.next();
					Iterator<Entry<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>>> disjunctionGroupsIt = getter
							.getValue().entrySet()
							.iterator();
					while (disjunctionGroupsIt.hasNext()) {
						Entry<String, LinkedHashMap<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>> disjunctionGroup = disjunctionGroupsIt
								.next();
						Iterator<Entry<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>>> transformationsIt = disjunctionGroup.getValue()
								.entrySet().iterator();
						while (transformationsIt.hasNext()) {
							Entry<ServiceMethodParameterTransformation, LinkedHashMap<String, ArrayList<Permission>>> transformation = transformationsIt.next();
							Iterator<Entry<String, ArrayList<Permission>>> ipRangesIt = transformation.getValue().entrySet().iterator();
							while (ipRangesIt.hasNext()) {
								Entry<String, ArrayList<Permission>> ipRange = ipRangesIt.next();
								ArrayList<String> permissionLine = new ArrayList<String>();
								permissionLine.add(serviceMethodName);
								permissionLine.add(ipRange.getKey() != null ? ipRange.getKey() : "");
								permissionLine.add(getter.getKey());
								permissionLine.add(transformation.getKey() != null ? transformation.getKey().toString() : "");
								StringBuilder restrictions = new StringBuilder();
								Iterator<Permission> permissionsIt = ipRange.getValue().iterator();
								while (permissionsIt.hasNext()) {
									restrictions.append(permissionsIt.next().getRestriction().toString());
									if (permissionsIt.hasNext()) {
										restrictions.append(",");
									}
								}
								permissionLine.add(restrictions.toString());
								permissionLine.add(disjunctionGroup.getKey() != null ? disjunctionGroup.getKey() : "");
								permissionLine.add("");
								permissionLine.add("");
								StringBuilder profiles = new StringBuilder();
								permissionsIt = ipRange.getValue().iterator();
								while (permissionsIt.hasNext()) {
									StringBuilder restrictionProfiles = new StringBuilder();
									Iterator<ProfilePermission> profilesIt = permissionsIt.next().getProfilePermissions().iterator();
									while (profilesIt.hasNext()) {
										restrictionProfiles.append(profilesIt.next().getProfile().toString());
										if (profilesIt.hasNext()) {
											restrictionProfiles.append(":");
										}
									}
									profiles.append(restrictionProfiles);
									if (permissionsIt.hasNext()) {
										profiles.append(",");
									}
								}
								permissionLine.add(profiles.toString().replaceAll(",+$", ""));
								printLine(permissionLine);
								definitionsCount++;
							}
						}
					}
				}
				serviceMethodsCount++;
			}
			if (setterMap.containsKey(serviceMethodName)) {
				Iterator<Entry<String, LinkedHashMap<String, ArrayList<Permission>>>> settersIt = setterMap.get(serviceMethodName).entrySet()
						.iterator();
				while (settersIt.hasNext()) {
					Entry<String, LinkedHashMap<String, ArrayList<Permission>>> setter = settersIt.next();
					Iterator<Entry<String, ArrayList<Permission>>> ipRangesIt = setter.getValue().entrySet().iterator();
					while (ipRangesIt.hasNext()) {
						Entry<String, ArrayList<Permission>> ipRange = ipRangesIt.next();
						ArrayList<String> permissionLine = new ArrayList<String>();
						permissionLine.add(serviceMethodName);
						permissionLine.add(ipRange.getKey() != null ? ipRange.getKey() : "");
						permissionLine.add("");
						permissionLine.add("");
						permissionLine.add("");
						permissionLine.add("");
						permissionLine.add(setter.getKey());
						StringBuilder overrides = new StringBuilder();
						Iterator<Permission> overridesIt = ipRange.getValue().iterator();
						while (overridesIt.hasNext()) {
							overrides.append(overridesIt.next().getOverride().toString());
							if (overridesIt.hasNext()) {
								overrides.append(",");
							}
						}
						permissionLine.add(overrides.toString());
						StringBuilder profiles = new StringBuilder();
						Iterator<Permission> permissionsIt = ipRange.getValue().iterator();
						while (permissionsIt.hasNext()) {
							StringBuilder overrideProfiles = new StringBuilder();
							Iterator<ProfilePermission> profilesIt = permissionsIt.next().getProfilePermissions().iterator();
							while (profilesIt.hasNext()) {
								overrideProfiles.append(profilesIt.next().getProfile().toString());
								if (profilesIt.hasNext()) {
									overrideProfiles.append(":");
								}
							}
							profiles.append(overrideProfiles);
							if (permissionsIt.hasNext()) {
								profiles.append(",");
							}
						}
						permissionLine.add(profiles.toString().replaceAll(",+$", ""));
						printLine(permissionLine);
						definitionsCount++;
					}
				}
				serviceMethodsCount++;
			}
			if (noGetterSetterMap.containsKey(serviceMethodName)) {
				Permission permission = noGetterSetterMap.get(serviceMethodName);
				ArrayList<String> permissionLine = new ArrayList<String>();
				permissionLine.add(serviceMethodName);
				permissionLine.add("");
				permissionLine.add("");
				permissionLine.add("");
				permissionLine.add("");
				permissionLine.add("");
				permissionLine.add("");
				permissionLine.add("");
				StringBuilder profiles = new StringBuilder();
				Iterator<ProfilePermission> profilesIt = permission.getProfilePermissions().iterator();
				while (profilesIt.hasNext()) {
					profiles.append(profilesIt.next().getProfile().toString());
					if (profilesIt.hasNext()) {
						profiles.append(":");
					}
				}
				permissionLine.add(profiles.toString());
				printLine(permissionLine);
				definitionsCount++;
				serviceMethodsCount++;
			}
		}
		jobOutput.println(serviceClass.getName() + ": " + serviceMethodsCount + " service method(s) with " + definitionsCount + " definitions");
	}

	public void setPermissions(Collection<Permission> allPermissions) {
		this.allPermissions = allPermissions;
	}
}
