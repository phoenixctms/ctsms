package org.phoenixctms.ctsms.security;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.phoenixctms.ctsms.compare.AlphanumStringComparator;
import org.phoenixctms.ctsms.compare.EntityIDComparator;
import org.phoenixctms.ctsms.domain.AnimalContactParticulars;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.OrganisationContactParticulars;
import org.phoenixctms.ctsms.domain.PersonContactParticulars;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandContactParticulars;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.util.Accessor;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.GraphEnumerator;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

public abstract class EntitySignature extends GraphEnumerator {

	private final static String PROPERTY_ASSOCIATION_PATH_SEPARATOR = AssociationPath.ASSOCIATION_PATH_SEPARATOR;
	private final static boolean PROPERTY_LOWER_CASE_FIELD_NAMES = false;
	private final static MethodTransfilter PROPERTY_METHOD_TRANSFILTER = MethodTransfilter.getVoMethodTransfilter(PROPERTY_LOWER_CASE_FIELD_NAMES);
	private final static ToStringStyle COMMENT_VALUE_TO_STRING_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;
	private final static Comparator PROPERTY_COLLECTION_VALUES_COMPARATOR = new EntityIDComparator(false);

	protected static void addFieldToMap(HashMap<Class, HashSet<String>> fieldMap, Class entity, String fieldName) {
		if (fieldMap.containsKey(entity)) {
			if (fieldName == null) {
				fieldMap.put(entity, null);
			} else {
				if (fieldMap.get(entity) != null) {
					fieldMap.get(entity).add(fieldName);
				}
			}
		} else {
			if (fieldName == null) {
				fieldMap.put(entity, null);
			} else {
				HashSet<String> fieldNames = new HashSet<String>();
				fieldMap.put(entity, fieldNames);
				fieldNames.add(fieldName);
			}
		}
	}

	private static Serializable entityToSerializable(Class entityClass, Object entity) {
		if (entityClass != null && entity != null) {
			if (Inventory.class.isAssignableFrom(entityClass)) {
				Inventory inventory = ((Inventory) entity);
				return new Serializable[] { inventory.getId(), inventory.getName() };
			} else if (Staff.class.isAssignableFrom(entityClass)) {
				Staff staff = ((Staff) entity);
				if (staff.isPerson()) {
					PersonContactParticulars personparticulars = staff.getPersonParticulars();
					return new Serializable[] { staff.getId(), personparticulars.getFirstName(), personparticulars.getLastName(), personparticulars.getDateOfBirth() };
				} else {
					OrganisationContactParticulars organisationParticulars = staff.getOrganisationParticulars();
					return new Serializable[] { staff.getId(), organisationParticulars.getOrganisationName() };
				}
			} else if (Course.class.isAssignableFrom(entityClass)) {
				Course course = ((Course) entity);
				return new Serializable[] { course.getId(), course.getName() };
			} else if (Trial.class.isAssignableFrom(entityClass)) {
				Trial trial = ((Trial) entity);
				return new Serializable[] { trial.getId(), trial.getName() };
			} else if (Proband.class.isAssignableFrom(entityClass)) {
				Proband proband = ((Proband) entity);
				if (proband.isPerson()) {
					ProbandContactParticulars personParticulars = proband.getPersonParticulars();
					if (personParticulars != null) {
						return new Serializable[] { proband.getId(), personParticulars.getEncryptedFirstName(), personParticulars.getEncryptedLastName(),
								personParticulars.getEncryptedDateOfBirth() };
					} else {
						return new Serializable[] { proband.getId() };
					}
				} else {
					AnimalContactParticulars animalParticulars = proband.getAnimalParticulars();
					if (animalParticulars != null) {
						return new Serializable[] { proband.getId(), animalParticulars.getAnimalName(), animalParticulars.getDateOfBirth() };
					} else {
						return new Serializable[] { proband.getId() };
					}
				}
			} else if (User.class.isAssignableFrom(entityClass)) {
				User user = ((User) entity);
				return new Serializable[] { user.getId(), user.getName() };
			} else if (InputField.class.isAssignableFrom(entityClass)) {
				InputField inputField = ((InputField) entity);
				return new Serializable[] { inputField.getId(), inputField.getNameL10nKey(), inputField.getTitleL10nKey(), inputField.getFieldType() };
			} else if (File.class.isAssignableFrom(entityClass)) {
				File file = ((File) entity);
				return new Serializable[] {
						file.getId(),
						file.getTitle(),
						file.getEncryptedTitle(),
						file.getFileName(),
						file.getEncryptedFileName(),
						file.getLogicalPath(),
						file.getMd5() };
			} else if (Department.class.isAssignableFrom(entityClass)) {
				Department department = ((Department) entity);
				return new Serializable[] { department.getId(), department.getNameL10nKey() };
			} else if (Serializable.class.isAssignableFrom(entityClass)) {
				return (Serializable) entity;
			} else if (isTerminal(entityClass)) {
				try {
					return (Serializable) entity;
				} catch (ClassCastException e) {
					// System.out.println("ERROR casting: " + entityClass.toString());
				}
			}
		}
		// if (entity != null) {
		// System.out.println("ignored: " + entityClass.toString());
		// }
		return null;
	}

	protected static boolean fieldMapContains(HashMap<Class, HashSet<String>> fieldMap, Class entity, String fieldName) {
		if (fieldMap.containsKey(entity)) {
			if (fieldMap.get(entity) == null) {
				return true;
			} else {
				return fieldMap.get(entity).contains(fieldName);
			}
		} else {
			return false;
		}
	}

	public static String getDescription(SignatureVO signature) {
		return getDescription(signature, Locales.USER, null);
	}

	public static String getDescription(SignatureVO signature, Locales locale, Integer signatureDataBase64Length) {
		if (signature != null) {
			UserOutVO signee = signature.getSignee();
			StaffOutVO signeeIdentity = signee.getIdentity();
			String signeeString;
			if (signeeIdentity != null) {
				signeeString = L10nUtil.getMessage(MessageCodes.SIGNEE_IDENTITY_LABEL, DefaultMessages.SIGNEE_IDENTITY_LABEL, CommonUtil.userOutVOToString(signee),
						CommonUtil.staffOutVOToString(signeeIdentity));
			} else {
				signeeString = CommonUtil.userOutVOToString(signee);
			}
			SimpleDateFormat dateTimeFormat = Settings.getSimpleDateFormat(SettingCodes.SIGNATURE_DESCRIPTION_DATETIME_PATTERN, Bundle.SETTINGS, DefaultSettings.SIGNATURE_DESCRIPTION_DATETIME_PATTERN, locale);
			String signatureDataBase64String = signature.getSignatureDataBase64();
			if (signatureDataBase64Length != null) {
				signatureDataBase64String = CommonUtil.clipString(signatureDataBase64String, signatureDataBase64Length);
			}
			switch (signature.getModule()) {
				case TRIAL_SIGNATURE:
					if (signature.getVerified()) {
						if (signature.getValid()) {
							return L10nUtil.getMessage(MessageCodes.TRIAL_SIGNATURE_VALID_DESCRIPTION, DefaultMessages.TRIAL_SIGNATURE_VALID_DESCRIPTION,
									CommonUtil.trialOutVOToString(signature.getTrial()), signeeString, dateTimeFormat.format(signature.getTimestamp()),
									dateTimeFormat.format(signature.getVerificationTimestamp()), signatureDataBase64String);
						} else {
							return L10nUtil.getMessage(MessageCodes.TRIAL_SIGNATURE_INVALID_DESCRIPTION, DefaultMessages.TRIAL_SIGNATURE_INVALID_DESCRIPTION,
									CommonUtil.trialOutVOToString(signature.getTrial()), signeeString,
									dateTimeFormat.format(signature.getTimestamp()), dateTimeFormat.format(signature.getVerificationTimestamp()));
						}
					} else {
						return L10nUtil.getMessage(MessageCodes.TRIAL_SIGNATURE_AVAILABLE, DefaultMessages.TRIAL_SIGNATURE_AVAILABLE,
								CommonUtil.trialOutVOToString(signature.getTrial()), dateTimeFormat.format(signature.getTimestamp()));
					}
				case ECRF_SIGNATURE:
					if (signature.getVerified()) {
						if (signature.getValid()) {
							return L10nUtil.getMessage(MessageCodes.ECRF_SIGNATURE_VALID_DESCRIPTION, DefaultMessages.ECRF_SIGNATURE_VALID_DESCRIPTION, signature
									.getEcrfStatusEntry().getEcrf().getUniqueName(),
									CommonUtil.probandOutVOToString(signature.getEcrfStatusEntry().getListEntry().getProband()), signeeString, dateTimeFormat.format(signature.getTimestamp()), dateTimeFormat.format(signature.getVerificationTimestamp()),
									signatureDataBase64String);
						} else {
							return L10nUtil.getMessage(MessageCodes.ECRF_SIGNATURE_INVALID_DESCRIPTION, DefaultMessages.ECRF_SIGNATURE_INVALID_DESCRIPTION, signature
									.getEcrfStatusEntry().getEcrf().getUniqueName(),
									CommonUtil.probandOutVOToString(signature.getEcrfStatusEntry().getListEntry().getProband()), signeeString,
									dateTimeFormat.format(signature.getTimestamp()), dateTimeFormat.format(signature.getVerificationTimestamp()));
						}
					} else {
						return L10nUtil.getMessage(MessageCodes.ECRF_SIGNATURE_AVAILABLE, DefaultMessages.ECRF_SIGNATURE_AVAILABLE, signature.getEcrfStatusEntry().getEcrf()
								.getUniqueName(),
								CommonUtil.probandOutVOToString(signature.getEcrfStatusEntry().getListEntry().getProband()), dateTimeFormat.format(signature.getTimestamp()));
					}
				default:
			}
		}
		return null;
	}

	private static ArrayList<EntitySignature> getEntitySignatures(Class signatureClass, Class entity, int depth) throws Exception {
		ArrayList<EntitySignature> entitySignatures = new ArrayList<EntitySignature>();
		ArrayList<Accessor> getterChain = new ArrayList<Accessor>();
		appendProperties(signatureClass, entitySignatures, entity, getterChain, null, depth,
				PROPERTY_METHOD_TRANSFILTER,
				PROPERTY_COLLECTION_VALUES_COMPARATOR,
				//ENTITY_GETTER_METHOD_NAME_REGEXP,
				//ENTITY_GETTER_METHOD_NAME_EXCLUSION_REGEXP,
				true, null, true, true, true, PROPERTY_ASSOCIATION_PATH_SEPARATOR);
		return entitySignatures;
	}

	private final static Signature getSignature() throws NoSuchAlgorithmException {
		return Signature.getInstance(CryptoUtil.SIGNATURE_ALGORITHM);
	}

	private static boolean isTerminal(Class clazz) {
		if (clazz.equals(String.class) ||
				clazz.equals(Long.class) ||
				clazz.equals(Integer.class) ||
				clazz.equals(Boolean.class) ||
				clazz.equals(Float.class) ||
				clazz.equals(Double.class) ||
				clazz.equals(Date.class) ||
				clazz.equals(Timestamp.class) ||
				Inventory.class.isAssignableFrom(clazz) ||
				Staff.class.isAssignableFrom(clazz) ||
				Course.class.isAssignableFrom(clazz) ||
				Trial.class.isAssignableFrom(clazz) ||
				Proband.class.isAssignableFrom(clazz) ||
				User.class.isAssignableFrom(clazz) ||
				InputField.class.isAssignableFrom(clazz) ||
				File.class.isAssignableFrom(clazz) ||
				Department.class.isAssignableFrom(clazz) ||
				clazz.isPrimitive() ||
				clazz.isEnum() ||
				clazz.isArray()) {
			return true;
		}
		return false;
	}

	protected static byte[] sign(Class signatureClass, Object entity, Timestamp now, int depth, StringBuilder comment) throws Exception {
		Signature signature = getSignature();
		signature.initSign(CoreUtil.getUserContext().getPrivateKey());
		updateSignature(entity, getEntitySignatures(signatureClass, entity.getClass(), depth), signature, CoreUtil.getUser(), now, comment);
		//FileUtils.writeStringToFile(new java.io.File("d:\\ctsms\\sign_" + now.getTime() + ".txt"),comment.toString());
		return signature.sign();
	}

	private static void updateSignature(Object entity, ArrayList<EntitySignature> entitySignatures, Signature signature, User signee, Timestamp timestamp, StringBuilder comment)
			throws Exception {
		signature.update(CryptoUtil.serialize(entityToSerializable(User.class, signee)));
		signature.update(CryptoUtil.serialize(entityToSerializable(Staff.class, signee.getIdentity())));
		signature.update(CryptoUtil.serialize(timestamp));
		AlphanumStringComparator stringComparator = new AlphanumStringComparator(true);
		TreeMap<String, Serializable> referenceFieldMap = new TreeMap<String, Serializable>(stringComparator);
		TreeMap<String, Serializable> deferredCollectionMapFieldMap = new TreeMap<String, Serializable>(stringComparator);
		Iterator<EntitySignature> entitySignatureIt = entitySignatures.iterator();
		while (entitySignatureIt.hasNext()) {
			EntitySignature entitySignature = entitySignatureIt.next();
			Iterator<ArrayList<Object>> indexesKeysIt = entitySignature.getIndexesKeys(entity).iterator();
			while (indexesKeysIt.hasNext()) {
				ArrayList<Object> indexesKeys = indexesKeysIt.next();
				String key = entitySignature.getKey(indexesKeys);
				//System.out.println(key);
				Serializable value = entitySignature.getValue(entity, indexesKeys);
				if (indexesKeys.size() > 0) {
					deferredCollectionMapFieldMap.put(key, value);
				} else {
					referenceFieldMap.put(key, value);
				}
			}
		}
		String lineSeparator;
		String keyValueSeparator;
		if (comment != null) {
			lineSeparator = Settings.getString(SettingCodes.SIGNATURE_COMMENT_LINE_SEPARATOR, Bundle.SETTINGS, DefaultSettings.SIGNATURE_COMMENT_LINE_SEPARATOR);
			keyValueSeparator = Settings.getString(SettingCodes.SIGNATURE_COMMENT_KEY_VALUE_SEPARATOR, Bundle.SETTINGS, DefaultSettings.SIGNATURE_COMMENT_KEY_VALUE_SEPARATOR);
		} else {
			lineSeparator = null;
			keyValueSeparator = null;
		}
		Iterator<String> referenceFieldsIt = referenceFieldMap.keySet().iterator();
		while (referenceFieldsIt.hasNext()) {
			String key = referenceFieldsIt.next();
			Serializable value = referenceFieldMap.get(key);
			if (comment != null) {
				if (comment.length() > 0) {
					comment.append(lineSeparator);
				}
				comment.append(key);
				comment.append(keyValueSeparator);
				comment.append(ReflectionToStringBuilder.toString(value,COMMENT_VALUE_TO_STRING_STYLE));
			}
			signature.update(CryptoUtil.serialize(key));
			signature.update(CryptoUtil.serialize(value));
		}
		Iterator<String> deferredCollectionMapFieldsIt = deferredCollectionMapFieldMap.keySet().iterator();
		while (deferredCollectionMapFieldsIt.hasNext()) {
			String key = deferredCollectionMapFieldsIt.next();
			Serializable value = deferredCollectionMapFieldMap.get(key);
			if (comment != null) {
				if (comment.length() > 0) {
					comment.append(lineSeparator);
				}
				comment.append(key);
				comment.append(keyValueSeparator);
				comment.append(ReflectionToStringBuilder.toString(value,COMMENT_VALUE_TO_STRING_STYLE));
			}
			signature.update(CryptoUtil.serialize(key));
			signature.update(CryptoUtil.serialize(value));
		}
	}

	protected static boolean verify(Class signatureClass, byte[] signatureData, Object entity, User signee, Timestamp timestamp, int depth, StringBuilder comment) throws Exception {
		Signature signature = getSignature();
		signature.initVerify(CryptoUtil.getPublicKey(signee.getKeyPair().getPublicKey()));
		updateSignature(entity, getEntitySignatures(signatureClass, entity.getClass(), depth), signature, signee, timestamp, comment);
		//FileUtils.writeStringToFile(new java.io.File("d:\\ctsms\\verify_" + timestamp.getTime() + ".txt"),comment.toString());
		return signature.verify(signatureData);
	}

	private final ArrayList<ArrayList<Object>> getIndexesKeys(Object entity) throws Exception {
		return getIndexKeyChains(entity);
	}

	private final String getKey(ArrayList<Object> indexesKeys) throws Exception {
		return getAssociationPath(indexesKeys);
	}

	private final Serializable getValue(Object entity, ArrayList<Object> indexesKeys) throws Exception {
		return entityToSerializable(returnType, getValueOf(entity, indexesKeys));
	}

	@Override
	protected boolean isTerminalType(Object passThrough) {
		return isTerminal(returnType);
	}
}
