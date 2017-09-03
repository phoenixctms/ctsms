package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeSet;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.compare.ProbandComparator;
import org.phoenixctms.ctsms.domain.AnimalContactParticulars;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.PrivacyConsentStatusType;
import org.phoenixctms.ctsms.domain.PrivacyConsentStatusTypeDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandCategory;
import org.phoenixctms.ctsms.domain.ProbandCategoryDao;
import org.phoenixctms.ctsms.domain.ProbandContactParticulars;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandDaoImpl;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.ProbandOutVO;

public class ProbandReflexionGraph extends ReflexionCycleHelper<Proband, ProbandOutVO> {

	private static final boolean LIMIT_INSTANCES = true;
	private static final boolean LIMIT_PARENTS_DEPTH = true;
	private static final boolean LIMIT_CHILDREN_DEPTH = true;

	// private static final String getInitials(ProbandOutVO proband) {
	// StringBuilder sb = new StringBuilder();
	// if (proband != null) {
	// if (proband.isDecrypted()) {
	// String firstName = proband.getFirstName();
	// if (firstName != null && firstName.trim().length() > 0) {
	// sb.append(firstName.trim().substring(0, 1));
	// }
	// String lastName = proband.getLastName();
	// if (lastName != null && lastName.trim().length() > 0) {
	// sb.append(lastName.trim().substring(0, 1));
	// }
	// } else {
	// sb.append(L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME));
	// }
	// }
	// return sb.toString();
	// }

	//
	//
	// private static final String getProbandName(ProbandOutVO proband, boolean withTitles) {
	// StringBuilder sb = new StringBuilder();
	// if (proband != null) {
	// if (proband.isDecrypted()) {
	// if (withTitles) {
	// CommonUtil.appendString(sb, proband.getPrefixedTitle1(), null);
	// CommonUtil.appendString(sb, proband.getPrefixedTitle2(), " ");
	// CommonUtil.appendString(sb, proband.getPrefixedTitle3(), " ");
	// CommonUtil.appendString(sb, proband.getFirstName(), " ");
	// CommonUtil.appendString(sb, proband.getLastName(), " ", "?");
	// CommonUtil.appendString(sb, proband.getPostpositionedTitle1(), ", ");
	// CommonUtil.appendString(sb, proband.getPostpositionedTitle2(), ", ");
	// CommonUtil.appendString(sb, proband.getPostpositionedTitle3(), ", ");
	// } else {
	// CommonUtil.appendString(sb, proband.getFirstName(), null);
	// CommonUtil.appendString(sb, proband.getLastName(), " ", "?");
	// }
	// } else {
	// sb.append(L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME));
	// }
	// }
	// return sb.toString();
	// }

	private int maxInstances;
	private int childrenDepth;
	private int parentsDepth;
	private ProbandDaoImpl probandDaoImpl;
	private ProbandDao probandDao;
	private StaffDao staffDao;
	private ProbandCategoryDao probandCategoryDao;
	private DepartmentDao departmentDao;
	private PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao;
	private UserDao userDao;
	private final static int DEFAULT_MAX_INSTANCES = 1;
	private final static int DEFAULT_CHILDREN_DEPTH = Integer.MAX_VALUE >> 1;
	private final static int DEFAULT_PARENTS_DEPTH = Integer.MAX_VALUE >> 1;

	public ProbandReflexionGraph(ProbandDao probandDao) {
		this.probandDao = probandDao;
	}

	public ProbandReflexionGraph(ProbandDaoImpl probandDaoImpl,
			ProbandCategoryDao probandCategoryDao, DepartmentDao departmentDao, StaffDao staffDao,
			PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao, UserDao userDao, Integer... maxInstances) {
		this.maxInstances = maxInstances != null && maxInstances.length > 0 ? (maxInstances[0] == null ? DEFAULT_MAX_INSTANCES : maxInstances[0]) : DEFAULT_MAX_INSTANCES;
		this.childrenDepth = maxInstances != null && maxInstances.length > 1 ? (maxInstances[1] == null ? DEFAULT_CHILDREN_DEPTH : maxInstances[1]) : DEFAULT_CHILDREN_DEPTH;
		this.parentsDepth = maxInstances != null && maxInstances.length > 2 ? (maxInstances[2] == null ? DEFAULT_PARENTS_DEPTH : maxInstances[2])
				: DEFAULT_PARENTS_DEPTH;
		this.probandDaoImpl = probandDaoImpl;
		this.probandCategoryDao = probandCategoryDao;
		this.departmentDao = departmentDao;
		this.staffDao = staffDao;
		this.privacyConsentStatusTypeDao = privacyConsentStatusTypeDao;
		this.userDao = userDao;
	}

	@Override
	protected Proband aquireWriteLock(Long id) throws ServiceException {
		return probandDao.load(id, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected Class getAVOClass() {
		return ProbandOutVO.class;
	}

	@Override
	protected Proband getChild(Proband source) {
		return null;
	}

	@Override
	protected Collection<Proband> getEntityChildren(Proband source) {
		Collection<Proband> children = source.getChildren();
		if (children.size() > 1) {
			TreeSet<Proband> result = new TreeSet<Proband>(new ProbandComparator());
			result.addAll(children);
			return result;
		} else {
			return children;
		}
	}

	@Override
	protected Long getEntityId(Proband source) {
		return source.getId();
	}

	@Override
	protected Collection<Proband> getEntityParents(Proband source) {
		Collection<Proband> parents = source.getParents();
		if (parents.size() > 1) {
			TreeSet<Proband> result = new TreeSet<Proband>(new ProbandComparator());
			result.addAll(parents);
			return result;
		} else {
			return parents;
		}
	}

	@Override
	protected ReflexionDepth getInitialReflexionDepth() {
		return new ReflexionDepth(parentsDepth, childrenDepth);
	}

	@Override
	protected int getMaxInstances() {
		return maxInstances; // Settings.getInt(SettingCodes.COURSEOUTVO_MAX_INSTANCES,Bundle.SETTINGS,DefaultSettings.COURSEOUTVO_MAX_INSTANCES);
	}

	@Override
	protected Proband getParent(Proband source) {
		return null;
	}

	@Override
	protected Collection<ProbandOutVO> getVOChildren(ProbandOutVO target) {
		return target.getChildren();
	}

	@Override
	protected Collection<ProbandOutVO> getVOParents(ProbandOutVO target) {
		return target.getParents();
	}

	@Override
	protected boolean limitChildrenDepth() {
		return LIMIT_CHILDREN_DEPTH;
	}

	@Override
	protected boolean limitInstances() {
		return LIMIT_INSTANCES;
	}

	@Override
	protected boolean limitParentsDepth() {
		return LIMIT_PARENTS_DEPTH;
	}

	@Override
	protected ProbandOutVO newVO() {
		return new ProbandOutVO();
	}

	@Override
	protected void setChild(ProbandOutVO target, ProbandOutVO childVO) {
	}

	@Override
	protected void setParent(ProbandOutVO target, ProbandOutVO parentVO) {
	}

	@Override
	protected void throwGraphLoopException(Proband entity) throws ServiceException {
		throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GRAPH_LOOP, entity.getId().toString());
		// ,
		// xCommonUtil.probandOutVOToString(probandDao.toProbandOutVO(entity)));
	}

	@Override
	protected void toVORemainingFields(Proband source, ProbandOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) { // , HashMap<Class,HashMap<Long,Object>> deferredVoMap) {
		probandDaoImpl.toProbandOutVOBase(source, target);
		Department department = source.getDepartment();
		ProbandCategory category = source.getCategory();
		User modifiedUser = source.getModifiedUser();
		Staff physician = source.getPhysician();
		PrivacyConsentStatusType privacyConsentStatus = source.getPrivacyConsentStatus();
		if (department != null) {
			target.setDepartment(departmentDao.toDepartmentVO(department));
		}
		if (category != null) {
			target.setCategory(probandCategoryDao.toProbandCategoryVO(category));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		if (physician != null) {
			target.setPhysician(staffDao.toStaffOutVO(physician));
		}
		if (privacyConsentStatus != null) {
			target.setPrivacyConsentStatus(privacyConsentStatusTypeDao.toPrivacyConsentStatusTypeVO(privacyConsentStatus));
		}
		target.setParentsCount(probandDaoImpl.getParentsCount(source.getId())); // source.getPrecedingCourses().size());
		target.setChildrenCount(probandDaoImpl.getChildrenCount(source.getId())); // source.getRenewals().Xsize());
		if (source.isPerson()) {
			ProbandContactParticulars personParticulars = source.getPersonParticulars();
			if (personParticulars != null) {
				try {
					if (!CoreUtil.isPassDecryption()) {
						throw new Exception();
					}
					target.setPrefixedTitle1((String) CryptoUtil.decryptValue(personParticulars.getPrefixedTitle1Iv(), personParticulars.getEncryptedPrefixedTitle1()));
					target.setPrefixedTitle2((String) CryptoUtil.decryptValue(personParticulars.getPrefixedTitle2Iv(), personParticulars.getEncryptedPrefixedTitle2()));
					target.setPrefixedTitle3((String) CryptoUtil.decryptValue(personParticulars.getPrefixedTitle3Iv(), personParticulars.getEncryptedPrefixedTitle3()));
					target.setFirstName((String) CryptoUtil.decryptValue(personParticulars.getFirstNameIv(), personParticulars.getEncryptedFirstName()));
					target.setLastName((String) CryptoUtil.decryptValue(personParticulars.getLastNameIv(), personParticulars.getEncryptedLastName()));
					target.setPostpositionedTitle1((String) CryptoUtil.decryptValue(personParticulars.getPostpositionedTitle1Iv(),
							personParticulars.getEncryptedPostpositionedTitle1()));
					target.setPostpositionedTitle2((String) CryptoUtil.decryptValue(personParticulars.getPostpositionedTitle2Iv(),
							personParticulars.getEncryptedPostpositionedTitle2()));
					target.setPostpositionedTitle3((String) CryptoUtil.decryptValue(personParticulars.getPostpositionedTitle3Iv(),
							personParticulars.getEncryptedPostpositionedTitle3()));
					target.setDateOfBirth((Date) CryptoUtil.decryptValue(personParticulars.getDateOfBirthIv(), personParticulars.getEncryptedDateOfBirth()));
					target.setCitizenship((String) CryptoUtil.decryptValue(personParticulars.getCitizenshipIv(), personParticulars.getEncryptedCitizenship()));
					target.setComment((String) CryptoUtil.decryptValue(personParticulars.getCommentIv(), personParticulars.getEncryptedComment()));
					target.setYearOfBirth(personParticulars.getYearOfBirth() != null ? CommonUtil.safeLongToInt(personParticulars.getYearOfBirth()) : null);
					target.setAge(CommonUtil.getAge(target.getDateOfBirth()));
					target.setDecrypted(true);
				} catch (Exception e) {
					target.setPrefixedTitle1(null);
					target.setPrefixedTitle2(null);
					target.setPrefixedTitle3(null);
					target.setFirstName(null);
					target.setLastName(null);
					target.setPostpositionedTitle1(null);
					target.setPostpositionedTitle2(null);
					target.setPostpositionedTitle3(null);
					target.setDateOfBirth(null);
					target.setCitizenship(null);
					target.setComment(null);
					target.setYearOfBirth(personParticulars.getYearOfBirth() != null ? CommonUtil.safeLongToInt(personParticulars.getYearOfBirth()) : null);
					target.setAge(personParticulars.getYearOfBirth() != null ? CommonUtil.getAge((new GregorianCalendar(target.getYearOfBirth(), GregorianCalendar.JULY, 1))
							.getTime()) : null);
					target.setDecrypted(false);
				}
				target.setGender(L10nUtil.createSexVO(Locales.USER, personParticulars.getGender()));
				target.setName(CommonUtil.getProbandName(target, false,
						L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)));
				target.setNameWithTitles(CommonUtil.getProbandName(target, true,
						L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)));
				target.setInitials(CommonUtil.getProbandInitials(target,
						L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)));
				target.setNameSortable(CommonUtil.getNameSortable(target));
				target.setHasImage(personParticulars.getFileSize() != null && personParticulars.getFileSize() > 0l);
			} else {
				target.setDecrypted(true);
			}
		} else {
			AnimalContactParticulars animalParticulars = source.getAnimalParticulars();
			if (animalParticulars != null) {
				target.setAnimalName(animalParticulars.getAnimalName());
				target.setDateOfBirth(animalParticulars.getDateOfBirth());
				target.setGender(L10nUtil.createSexVO(Locales.USER, animalParticulars.getGender()));
				target.setComment(animalParticulars.getComment());
				// target.setCitizenship(personParticulars.getCitizenship());
				target.setAge(CommonUtil.getAge(target.getDateOfBirth()));
				target.setYearOfBirth(CommonUtil.getYearOfBirth(target.getDateOfBirth()));
				target.setDecrypted(true);
				target.setName(CommonUtil.getProbandName(target, false,
						L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)));
				target.setNameWithTitles(CommonUtil.getProbandName(target, true,
						L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)));
				target.setInitials(CommonUtil.getProbandInitials(target,
						L10nUtil.getString(MessageCodes.ENCRYPTED_PROBAND_NAME, DefaultMessages.ENCRYPTED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.NEW_BLINDED_PROBAND_NAME, DefaultMessages.NEW_BLINDED_PROBAND_NAME),
						L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)));
				target.setNameSortable(CommonUtil.getNameSortable(target));
				target.setHasImage(animalParticulars.getFileSize() != null && animalParticulars.getFileSize() > 0l);

			} else {
				target.setDecrypted(true);
			}
		}
	}
}