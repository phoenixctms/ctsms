package org.phoenixctms.ctsms.query.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.AddressTypeDao;
import org.phoenixctms.ctsms.domain.ContactDetailTypeDao;
import org.phoenixctms.ctsms.domain.CourseCategoryDao;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.domain.CvSectionDao;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InventoryCategoryDao;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.InventoryStatusTypeDao;
import org.phoenixctms.ctsms.domain.InventoryTagDao;
import org.phoenixctms.ctsms.domain.MassMailDao;
import org.phoenixctms.ctsms.domain.PrivacyConsentStatusTypeDao;
import org.phoenixctms.ctsms.domain.ProbandCategoryDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandTagDao;
import org.phoenixctms.ctsms.domain.SponsoringTypeDao;
import org.phoenixctms.ctsms.domain.StaffCategoryDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffStatusTypeDao;
import org.phoenixctms.ctsms.domain.StaffTagDao;
import org.phoenixctms.ctsms.domain.SurveyStatusTypeDao;
import org.phoenixctms.ctsms.domain.TeamMemberRoleDao;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.TrialStatusTypeDao;
import org.phoenixctms.ctsms.domain.TrialTagDao;
import org.phoenixctms.ctsms.domain.TrialTypeDao;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.CriterionValueType;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.query.QueryUtil;
import org.phoenixctms.ctsms.query.parser.OperandConfiguration.Associativity;
import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.OmittedFields;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;

public abstract class CriterionParser extends ExpressionParser<CriterionInstantVO> {

	// Operators
	private static final HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, OperandConfiguration> OPERATORS = new HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, OperandConfiguration>();
	static {
		OPERATORS.put(org.phoenixctms.ctsms.enumeration.CriterionTie.AND, new OperandConfiguration(5, Associativity.LEFT,
				new ValueType[] { WhereTermValueType.VALUE_TYPE },
				new ValueType[] { WhereTermValueType.VALUE_TYPE },
				WhereTermValueType.VALUE_TYPE));
		OPERATORS.put(org.phoenixctms.ctsms.enumeration.CriterionTie.OR, new OperandConfiguration(4, Associativity.LEFT,
				new ValueType[] { WhereTermValueType.VALUE_TYPE },
				new ValueType[] { WhereTermValueType.VALUE_TYPE },
				WhereTermValueType.VALUE_TYPE));
		OPERATORS.put(org.phoenixctms.ctsms.enumeration.CriterionTie.EXCEPT, new OperandConfiguration(0, Associativity.LEFT,
				new ValueType[] { WhereTermValueType.VALUE_TYPE, SelectValueType.VALUE_TYPE },
				new ValueType[] { WhereTermValueType.VALUE_TYPE, SelectValueType.VALUE_TYPE },
				SelectValueType.VALUE_TYPE));
		OPERATORS.put(org.phoenixctms.ctsms.enumeration.CriterionTie.INTERSECT, new OperandConfiguration(1, Associativity.LEFT,
				new ValueType[] { WhereTermValueType.VALUE_TYPE, SelectValueType.VALUE_TYPE },
				new ValueType[] { WhereTermValueType.VALUE_TYPE, SelectValueType.VALUE_TYPE },
				SelectValueType.VALUE_TYPE));
		OPERATORS.put(org.phoenixctms.ctsms.enumeration.CriterionTie.UNION, new OperandConfiguration(0, Associativity.LEFT,
				new ValueType[] { WhereTermValueType.VALUE_TYPE, SelectValueType.VALUE_TYPE },
				new ValueType[] { WhereTermValueType.VALUE_TYPE, SelectValueType.VALUE_TYPE },
				SelectValueType.VALUE_TYPE));
	}
	private final static String GET_DAO_METHOD_PREFIX = "get";
	// private PermissionProfileDao permissionProfileDao;
	// private AuthenticationTypeDao authenticationTypeDao;
	// private boolean obfuscateCriterions;
	// private boolean resolveProperties;
	private final static boolean PRETTY_PRINT_SHOW_POSITION_NUMBERS = true;
	private final static String PRETTY_PRINT_INDENTATION = "  ";
	private final static String PRETTY_PRINT_EMPTY_VALUE = "";
	private final static String PRETTY_PRINT_LINE_BREAK = "\n";

	private static String getCriterionValueString(CriterionInstantVO token, CriterionProperty property) {
		return CommonUtil.getCriterionValueAsString(token, property.getValueType(), PRETTY_PRINT_EMPTY_VALUE,
				CoreUtil.getUserContext().getDateFormat(),
				CoreUtil.getUserContext().getDecimalSeparator());
	}

	private static Method getDaoTransformMethod(String entityName, Object dao) throws Exception {
		try {
			return CoreUtil.getDaoOutVOTransformMethod(entityName, dao);
		} catch (NoSuchMethodException e) {
			return CoreUtil.getDaoVOTransformMethod(entityName, dao);
		}
	}

	private static boolean getObfuscateCriterionsArg(Object[] args) {
		try {
			return (Boolean) args[0];
		} catch (Exception e) {
			return false;
		}
	}

	protected static boolean isSetOperator(org.phoenixctms.ctsms.enumeration.CriterionTie op) {
		if (op != null) {
			return org.phoenixctms.ctsms.enumeration.CriterionTie.UNION.equals(op)
					|| org.phoenixctms.ctsms.enumeration.CriterionTie.INTERSECT.equals(op)
					|| org.phoenixctms.ctsms.enumeration.CriterionTie.EXCEPT.equals(op);
		}
		return false;
	}

	private HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> tieMap;
	private HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, String> tieNameMap;
	private HashMap<Long, CriterionProperty> propertyMap;
	private HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionMap;
	private HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String> restrictionNameMap;
	private CriterionPropertyDao criterionPropertyDao;
	private CriterionRestrictionDao criterionRestrictionDao;
	protected CriterionTieDao criterionTieDao;
	private InventoryDao inventoryDao;
	private StaffDao staffDao;
	private CourseDao courseDao;
	private TrialDao trialDao;
	private ProbandDao probandDao;
	private InputFieldDao inputFieldDao;
	private MassMailDao massMailDao;
	private UserDao userDao;
	private DepartmentDao departmentDao;
	private InventoryCategoryDao inventoryCategoryDao;
	private InventoryTagDao inventoryTagDao;
	private InventoryStatusTypeDao inventoryStatusTypeDao;
	private StaffCategoryDao staffCategoryDao;
	private CourseCategoryDao courseCategoryDao;
	private CvSectionDao cvSectionDao;
	private StaffTagDao staffTagDao;
	private ContactDetailTypeDao contactDetailTypeDao;
	private StaffStatusTypeDao staffStatusTypeDao;
	private TrialStatusTypeDao trialStatusTypeDao;
	private TrialTypeDao trialTypeDao;
	private SponsoringTypeDao sponsoringTypeDao;
	private SurveyStatusTypeDao surveyStatusTypeDao;
	private TeamMemberRoleDao teamMemberRoleDao;
	private TrialTagDao trialTagDao;
	// private InputFieldTypeDao inputFieldTypeDao;
	private ProbandCategoryDao probandCategoryDao;
	private PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao;
	private AddressTypeDao addressTypeDao;
	private InquiryDao inquiryDao;
	// private InputFieldDao inputFieldDao;
	private InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao;
	private ProbandListEntryTagDao probandListEntryTagDao;
	private ProbandTagDao probandTagDao;
	private ECRFFieldDao ecrfFieldDao;

	protected CriterionParser() {
		// this.tieMap = QueryUtil.createCriterionTieMap(criterionTieDao);
		// this.tieNameMap = QueryUtil.createCriterionTieNameMap(criterionTieDao);
		// if (criterionPropertyDao != null) {
		// this.propertyMap = QueryUtil.createCriterionPropertyMap(null, criterionPropertyDao);
		// } else {
		// this.propertyMap = new HashMap<Long, CriterionProperty>();
		// }
		// if (criterionRestrictionDao != null) {
		// this.restrictionMap = QueryUtil.createCriterionRestrictionMap(criterionRestrictionDao);
		// this.restrictionNameMap = QueryUtil.createCriterionRestrictionNameMap(criterionRestrictionDao);
		// } else {
		// this.restrictionMap = new HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>();
		// this.restrictionNameMap = new HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String>();
		// }
		// this.obfuscateCriterions = obfuscateCriterions;
	}

	public AddressTypeDao getAddressTypeDao() {
		return addressTypeDao;
	}

	public ContactDetailTypeDao getContactDetailTypeDao() {
		return contactDetailTypeDao;
	}

	public CourseCategoryDao getCourseCategoryDao() {
		return courseCategoryDao;
	}

	public CourseDao getCourseDao() {
		return courseDao;
	}

	public CvSectionDao getCvSectionDao() {
		return cvSectionDao;
	}

	private Object getDao(String entityName) throws Exception {
		return this.getClass().getMethod(GET_DAO_METHOD_PREFIX + CoreUtil.getDaoNameFromEntityName(entityName)).invoke(this);
	}

	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public ECRFFieldDao getECRFFieldDao() {
		return ecrfFieldDao;
	}

	private String getEnumerationCriterionValueString(String enumName, String value, String getNameMethodName) throws ServiceException {
		if (!CommonUtil.isEmptyString(value)) {
			Object vo;
			try {
				vo = L10nUtil.createEnumerationVO(Locales.USER, enumName, CoreUtil.getEnumerationItem(enumName, value));
			} catch (Exception e) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_ENUMERATION_OR_VALUE,
						DefaultMessages.UNSUPPORTED_ENUMERATION_OR_VALUE,
						new Object[] { enumName, value }));
			}
			try {
				return (String) AssociationPath.invoke(getNameMethodName, vo, false);
			} catch (Exception e) {
			}
		}
		return PRETTY_PRINT_EMPTY_VALUE;
	}

	protected final String getInfixStyleExpressionPrettyString(ArrayList<CriterionInstantVO> tokens, int positionDigits, boolean resolveCriterionValues,
			boolean obfuscateCriterions)
			throws ServiceException {
		StringBuilder result = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			int indent = 0;
			String positionFormat = "%0" + positionDigits + "d";
			for (int i = 0; i < tokens.size(); i++) {
				CriterionInstantVO prev = (i > 0 ? tokens.get(i - 1) : null);
				CriterionInstantVO token = tokens.get(i);
				CriterionInstantVO next = (i < tokens.size() - 1 ? tokens.get(i + 1) : null);
				if (token != null) {
					if (isLeftParenthesis(token)) {
						if (PRETTY_PRINT_SHOW_POSITION_NUMBERS && token.getPosition() != null) {
							result.append(String.format(positionFormat, token.getPosition()));
							result.append(":");
						}
						for (int j = 0; j < indent; j++) {
							result.append(PRETTY_PRINT_INDENTATION);
						}
						tokenToPrettyString(token, result, resolveCriterionValues, obfuscateCriterions);
						result.append(PRETTY_PRINT_LINE_BREAK);
						indent++;
					} else if (isRightParenthesis(token)) {
						if (token.getPosition() != null) {
							result.append(String.format(positionFormat, token.getPosition()));
							result.append(":");
						}
						indent--;
						for (int j = 0; j < indent; j++) {
							result.append(PRETTY_PRINT_INDENTATION);
						}
						tokenToPrettyString(token, result, resolveCriterionValues, obfuscateCriterions);
						if (next != null) {
							result.append(PRETTY_PRINT_LINE_BREAK);
						}
					} else if (isOperator(token)) {
						if (token.getPosition() != null) {
							result.append(String.format(positionFormat, token.getPosition()));
							result.append(":");
						}
						for (int j = 0; j < indent; j++) {
							result.append(PRETTY_PRINT_INDENTATION);
						}
						tokenToPrettyString(token, result, resolveCriterionValues, obfuscateCriterions);
						if (isSetOperator(token)) {
							result.append(PRETTY_PRINT_LINE_BREAK);
						} else if (!isValue(next)) {
							result.append(PRETTY_PRINT_LINE_BREAK);
						}
					} else {
						if (prev != null && isOperator(prev) && !isSetOperator(prev)) {
							result.append(" ");
						} else {
							if (token.getPosition() != null) {
								result.append(String.format(positionFormat, token.getPosition()));
								result.append(":");
							}
							for (int j = 0; j < indent; j++) {
								result.append(PRETTY_PRINT_INDENTATION);
							}
						}
						tokenToPrettyString(token, result, resolveCriterionValues, obfuscateCriterions);
						if (next != null) {
							result.append(PRETTY_PRINT_LINE_BREAK);
						}
					}
				}
			}
		}
		return result.toString();
	}

	public InputFieldDao getInputFieldDao() {
		return inputFieldDao;
	}

	public InputFieldSelectionSetValueDao getInputFieldSelectionSetValueDao() {
		return inputFieldSelectionSetValueDao;
	}

	public InquiryDao getInquiryDao() {
		return inquiryDao;
	}

	public InventoryCategoryDao getInventoryCategoryDao() {
		return inventoryCategoryDao;
	}

	public InventoryDao getInventoryDao() {
		return inventoryDao;
	}

	public InventoryStatusTypeDao getInventoryStatusTypeDao() {
		return inventoryStatusTypeDao;
	}

	public InventoryTagDao getInventoryTagDao() {
		return inventoryTagDao;
	}

	public MassMailDao getMassMailDao() {
		return massMailDao;
	}

	@Override
	protected OperandConfiguration getOperandConfiguration(
			CriterionInstantVO operator) {
		return (OPERATORS.get(getTieMap().get(operator.getTieId())));
	}

	private String getPickerCriterionValueString(DBModule pickerModule, Long id, boolean obfuscateCriterions) throws ServiceException {
		if (id != null) {
			switch (pickerModule) {
				case INVENTORY_DB:
					return CommonUtil.inventoryOutVOToString(inventoryDao.toInventoryOutVO(CheckIDUtil.checkInventoryId(id, inventoryDao)));
				case STAFF_DB:
					return CommonUtil.staffOutVOToString(staffDao.toStaffOutVO(CheckIDUtil.checkStaffId(id, staffDao)));
				case COURSE_DB:
					return CommonUtil.courseOutVOToString(courseDao.toCourseOutVO(CheckIDUtil.checkCourseId(id, courseDao)));
				case TRIAL_DB:
					return CommonUtil.trialOutVOToString(trialDao.toTrialOutVO(CheckIDUtil.checkTrialId(id, trialDao)));
				case PROBAND_DB:
					if (obfuscateCriterions) {
						return CheckIDUtil.checkProbandId(id, probandDao).getId().toString();
					} else {
						return CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(CheckIDUtil.checkProbandId(id, probandDao)));
					}
				case INPUT_FIELD_DB:
					return CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(CheckIDUtil.checkInputFieldId(id, inputFieldDao)));
				case MASS_MAIL_DB:
					return CommonUtil.massMailOutVOToString(massMailDao.toMassMailOutVO(CheckIDUtil.checkMassMailId(id, massMailDao)));
				case USER_DB:
					return CommonUtil.userOutVOToString(userDao.toUserOutVO(CheckIDUtil.checkUserId(id, userDao)));
				default:
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_PICKER_DB_MODULE,
							DefaultMessages.UNSUPPORTED_PICKER_DB_MODULE,
							new Object[] { pickerModule.toString() }));
			}
		}
		return PRETTY_PRINT_EMPTY_VALUE;
	}

	protected int getPositionDigits(ArrayList<CriterionInstantVO> tokens) {
		Long maxPosition = null;
		if (tokens != null && tokens.size() > 0) {
			Iterator<CriterionInstantVO> it = tokens.iterator();
			while (it.hasNext()) {
				CriterionInstantVO token = it.next();
				if (token != null && token.getPosition() != null) {
					if (maxPosition != null) {
						maxPosition = Math.max(maxPosition, token.getPosition());
					} else {
						maxPosition = token.getPosition();
					}
				}
			}
		}
		if (maxPosition != null && maxPosition > 0) {
			return maxPosition.toString().length();
			// return ((int) Math.floor(Math.log10(maxPosition))) + 1;
		}
		return 1;
	}

	public PrivacyConsentStatusTypeDao getPrivacyConsentStatusTypeDao() {
		return privacyConsentStatusTypeDao;
	}

	public ProbandCategoryDao getProbandCategoryDao() {
		return probandCategoryDao;
	}

	public ProbandDao getProbandDao() {
		return probandDao;
	}

	public ProbandListEntryTagDao getProbandListEntryTagDao() {
		return probandListEntryTagDao;
	}

	public ProbandTagDao getProbandTagDao() {
		return probandTagDao;
	}

	private HashMap<Long, CriterionProperty> getPropertyMap() {
		if (propertyMap == null) {
			// if (resolveProperties) {
			propertyMap = QueryUtil.createCriterionPropertyMap(null, criterionPropertyDao);
			// } else {
			// propertyMap = new HashMap<Long, CriterionProperty>();
			// }
		}
		return propertyMap;
	}

	private HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> getRestrictionMap() {
		if (restrictionMap == null) {
			// if (resolveProperties) {
			restrictionMap = QueryUtil.createCriterionRestrictionMap(criterionRestrictionDao);
			// } else {
			// restrictionMap = new HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>();
			// }
		}
		return restrictionMap;
	}

	private HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String> getRestrictionNameMap() {
		if (restrictionNameMap == null) {
			// if (resolveProperties) {
			restrictionNameMap = QueryUtil.createCriterionRestrictionNameMap(criterionRestrictionDao);
			// } else {
			// restrictionNameMap = new HashMap<org.phoenixctms.ctsms.enumeration.CriterionRestriction, String>();
			// }
		}
		return restrictionNameMap;
	}

	protected final String getSetOperationExpressionString(ArrayList<CriterionInstantVO> inputTokens) {
		StringBuilder result = new StringBuilder();
		if (inputTokens != null) {
			Iterator<CriterionInstantVO> it = inputTokens.iterator();
			Integer lastSelectStatementIndex = null;
			while (it.hasNext()) {
				CriterionInstantVO token = it.next();
				if (token != null) {
					if (isLeftParenthesis(token)) {
						// if (result.length() > 0) result.append(" ");
						result.append(QueryUtil.getSetExpressionTieName(
								org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS,
								getTieNameMap()));
					} else if (isRightParenthesis(token)) {
						// if (result.length() > 0) result.append(" ");
						result.append(QueryUtil.getSetExpressionTieName(
								org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS,
								getTieNameMap()));
					} else if (isOperator(token)) {
						if (isSetOperator(token)) {
							result.append(" ");
							result.append(QueryUtil.getSetExpressionTieName(
									getTieMap().get(token.getTieId()),
									getTieNameMap()));
							result.append(" ");
						}
					} else {
						if (token.getSelectStatementIndex() != null) {
							if (lastSelectStatementIndex == null || !lastSelectStatementIndex.equals(token.getSelectStatementIndex())) {
								result.append(CommonUtil.getSetOperationExpressionSelectLabel(token.getSelectStatementIndex()));
								lastSelectStatementIndex = token.getSelectStatementIndex();
							}
						}
					}
				}
			}
		}
		return result.toString();
	}

	public SponsoringTypeDao getSponsoringTypeDao() {
		return sponsoringTypeDao;
	}

	public StaffCategoryDao getStaffCategoryDao() {
		return staffCategoryDao;
	}

	public StaffDao getStaffDao() {
		return staffDao;
	}

	public StaffStatusTypeDao getStaffStatusTypeDao() {
		return staffStatusTypeDao;
	}

	public StaffTagDao getStaffTagDao() {
		return staffTagDao;
	}

	public SurveyStatusTypeDao getSurveyStatusTypeDao() {
		return surveyStatusTypeDao;
	}

	public TeamMemberRoleDao getTeamMemberRoleDao() {
		return teamMemberRoleDao;
	}

	private HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionTie> getTieMap() {
		if (tieMap == null) {
			tieMap = QueryUtil.createCriterionTieMap(criterionTieDao);
		}
		return tieMap;
	}

	private HashMap<org.phoenixctms.ctsms.enumeration.CriterionTie, String> getTieNameMap() {
		if (tieNameMap == null) {
			tieNameMap = QueryUtil.createCriterionTieNameMap(criterionTieDao);
		}
		return tieNameMap;
	}

	public TrialDao getTrialDao() {
		return trialDao;
	}

	public TrialStatusTypeDao getTrialStatusTypeDao() {
		return trialStatusTypeDao;
	}

	public TrialTagDao getTrialTagDao() {
		return trialTagDao;
	}

	public TrialTypeDao getTrialTypeDao() {
		return trialTypeDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	private String getValueObjectCriterionValueString(String entityName, Long id, String getNameMethodName) throws ServiceException {
		if (id != null) {
			Object dao;
			Object entity;
			Object vo;
			try {
				dao = getDao(entityName);
				entity = CheckIDUtil.checkEntityId(entityName, id, dao);
				vo = getDaoTransformMethod(entityName, dao).invoke(dao, entity);
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_ENTITY,
						DefaultMessages.UNSUPPORTED_ENTITY,
						new Object[] { entityName }));
			}
			try {
				return (String) AssociationPath.invoke(getNameMethodName, vo, false);
			} catch (Exception e) {
			}
		}
		return PRETTY_PRINT_EMPTY_VALUE;
	}

	@Override
	protected ValueType getValueType(CriterionInstantVO operand) {
		return WhereTermValueType.VALUE_TYPE;
	}

	@Override
	protected boolean isLeftParenthesis(CriterionInstantVO token) {
		if (token.getTieId() != null) {
			return org.phoenixctms.ctsms.enumeration.CriterionTie.LEFT_PARENTHESIS.equals(getTieMap().get(token.getTieId()));
		}
		return false;
	}

	@Override
	protected boolean isOperator(CriterionInstantVO token) {
		if (token.getTieId() != null) {
			return OPERATORS.containsKey(getTieMap().get(token.getTieId()));
		}
		return false;
	}

	@Override
	protected boolean isRightParenthesis(CriterionInstantVO token) {
		if (token.getTieId() != null) {
			return org.phoenixctms.ctsms.enumeration.CriterionTie.RIGHT_PARENTHESIS.equals(getTieMap().get(token.getTieId()));
		}
		return false;
	}

	protected boolean isSetOperator(CriterionInstantVO token) {
		if (token.getTieId() != null) {
			return isSetOperator(getTieMap().get(token.getTieId()));
		}
		return false;
	}

	public void setAddressTypeDao(AddressTypeDao addressTypeDao) {
		this.addressTypeDao = addressTypeDao;
	}

	public void setContactDetailTypeDao(ContactDetailTypeDao contactDetailTypeDao) {
		this.contactDetailTypeDao = contactDetailTypeDao;
	}

	public void setCourseCategoryDao(CourseCategoryDao courseCategoryDao) {
		this.courseCategoryDao = courseCategoryDao;
	}

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void setCriterionPropertyDao(CriterionPropertyDao criterionPropertyDao) {
		this.criterionPropertyDao = criterionPropertyDao;
	}

	public void setCriterionRestrictionDao(CriterionRestrictionDao criterionRestrictionDao) {
		this.criterionRestrictionDao = criterionRestrictionDao;
	}

	public void setCriterionTieDao(CriterionTieDao criterionTieDao) {
		this.criterionTieDao = criterionTieDao;
	}

	public void setCvSectionDao(CvSectionDao cvSectionDao) {
		this.cvSectionDao = cvSectionDao;
	}

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public void setEcrfFieldDao(ECRFFieldDao ecrfFieldDao) {
		this.ecrfFieldDao = ecrfFieldDao;
	}

	public void setInputFieldDao(InputFieldDao inputFieldDao) {
		this.inputFieldDao = inputFieldDao;
	}

	public void setInputFieldSelectionSetValueDao(InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao) {
		this.inputFieldSelectionSetValueDao = inputFieldSelectionSetValueDao;
	}

	public void setInquiryDao(InquiryDao inquiryDao) {
		this.inquiryDao = inquiryDao;
	}

	public void setInventoryCategoryDao(InventoryCategoryDao inventoryCategoryDao) {
		this.inventoryCategoryDao = inventoryCategoryDao;
	}

	public void setInventoryDao(InventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
	}

	public void setInventoryStatusTypeDao(InventoryStatusTypeDao inventoryStatusTypeDao) {
		this.inventoryStatusTypeDao = inventoryStatusTypeDao;
	}

	public void setInventoryTagDao(InventoryTagDao inventoryTagDao) {
		this.inventoryTagDao = inventoryTagDao;
	}

	public void setMassMailDao(MassMailDao massMailDao) {
		this.massMailDao = massMailDao;
	}

	public void setPrivacyConsentStatusTypeDao(PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao) {
		this.privacyConsentStatusTypeDao = privacyConsentStatusTypeDao;
	}

	public void setProbandCategoryDao(ProbandCategoryDao probandCategoryDao) {
		this.probandCategoryDao = probandCategoryDao;
	}

	public void setProbandDao(ProbandDao probandDao) {
		this.probandDao = probandDao;
	}

	public void setProbandListEntryTagDao(ProbandListEntryTagDao probandListEntryTagDao) {
		this.probandListEntryTagDao = probandListEntryTagDao;
	}

	public void setProbandTagDao(ProbandTagDao probandTagDao) {
		this.probandTagDao = probandTagDao;
	}

	public void setSponsoringTypeDao(SponsoringTypeDao sponsoringTypeDao) {
		this.sponsoringTypeDao = sponsoringTypeDao;
	}

	public void setStaffCategoryDao(StaffCategoryDao staffCategoryDao) {
		this.staffCategoryDao = staffCategoryDao;
	}

	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}

	public void setStaffStatusTypeDao(StaffStatusTypeDao staffStatusTypeDao) {
		this.staffStatusTypeDao = staffStatusTypeDao;
	}

	public void setStaffTagDao(StaffTagDao staffTagDao) {
		this.staffTagDao = staffTagDao;
	}

	public void setSurveyStatusTypeDao(SurveyStatusTypeDao surveyStatusTypeDao) {
		this.surveyStatusTypeDao = surveyStatusTypeDao;
	}

	public void setTeamMemberRoleDao(TeamMemberRoleDao teamMemberRoleDao) {
		this.teamMemberRoleDao = teamMemberRoleDao;
	}

	public void setTrialDao(TrialDao trialDao) {
		this.trialDao = trialDao;
	}

	public void setTrialStatusTypeDao(TrialStatusTypeDao trialStatusTypeDao) {
		this.trialStatusTypeDao = trialStatusTypeDao;
	}

	public void setTrialTagDao(TrialTagDao trialTagDao) {
		this.trialTagDao = trialTagDao;
	}

	public void setTrialTypeDao(TrialTypeDao trialTypeDao) {
		this.trialTypeDao = trialTypeDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	private void tokenToPrettyString(CriterionInstantVO token, StringBuilder result, boolean resolveCriterionValues, boolean obfuscateCriterions) throws ServiceException {
		if (isLeftParenthesis(token) || isRightParenthesis(token) || isOperator(token)) {
			result.append(getTieNameMap().get(getTieMap().get(token.getTieId())));
		} else {
			CriterionProperty property = getPropertyMap().get(token.getPropertyId());
			if (property != null) {
				result.append(L10nUtil.getCriterionPropertyName(Locales.USER, property.getNameL10nKey()));
			}
			org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = getRestrictionMap().get(token.getRestrictionId());
			if (restriction != null) {
				result.append(" ");
				result.append(getRestrictionNameMap().get(restriction));
			}
			if (property != null && !org.phoenixctms.ctsms.enumeration.CriterionValueType.NONE.equals(property.getValueType())) {
				result.append(" ");
				String criterionValue;
				if (obfuscateCriterions && OmittedFields.isOmitted(property.getProperty())) {
					criterionValue = CoreUtil.OBFUSCATED_STRING;
				} else {
					if (resolveCriterionValues) {
						try {
							if (property.getPicker() != null) {
								criterionValue = getPickerCriterionValueString(property.getPicker(), token.getLongValue(), obfuscateCriterions);
							} else if (!CommonUtil.isEmptyString(property.getEntityName())) {
								if (CoreUtil.isEnumerationClass(property.getEntityName())) {
									criterionValue = getEnumerationCriterionValueString(property.getEntityName(), token.getStringValue(), property.getGetNameMethodName());
								} else {
									criterionValue = getValueObjectCriterionValueString(property.getEntityName(), token.getLongValue(), property.getGetNameMethodName());
								}
							} else {
								criterionValue = getCriterionValueString(token, property);
							}
						} catch (ServiceException e) {
							e.setData(token.getPosition());
							throw e;
						}
					} else {
						// result.append("...");
						criterionValue = getCriterionValueString(token, property);
					}
				}
				result.append(criterionValue);
			}
		}
	}

	@Override
	protected String tokenToString(CriterionInstantVO token, Object... args) {
		if (isOperator(token)) {
			return getTieMap().get(token.getTieId()).name();
		} else {
			StringBuilder result = new StringBuilder();
			CriterionProperty property = getPropertyMap().get(token.getPropertyId());
			result.append("<");
			if (token.getPosition() != null) {
				result.append(token.getPosition());
				result.append(": ");
			}
			result.append(property == null ? null : property.getProperty());
			result.append(" ");
			org.phoenixctms.ctsms.enumeration.CriterionRestriction restriction = getRestrictionMap().get(token.getRestrictionId());
			result.append(restriction == null ? null : restriction.name());
			if (property != null && !CriterionValueType.NONE.equals(property.getValueType())) {
				result.append(" ");
				if (getObfuscateCriterionsArg(args) && OmittedFields.isOmitted(property.getProperty())) {
					result.append(CoreUtil.OBFUSCATED_STRING);
				} else {
					result.append(getCriterionValueString(token, property));
				}
			}
			result.append(">");
			return result.toString();
		}
	}

	@Override
	protected ArrayList<CriterionInstantVO> unFoldTokens(ArrayList<CriterionInstantVO> inputTokens) {
		ArrayList<CriterionInstantVO> unfolded;
		if (inputTokens != null && inputTokens.size() > 0) {
			unfolded = new ArrayList<CriterionInstantVO>(inputTokens.size());
			Iterator<CriterionInstantVO> it = inputTokens.iterator();
			while (it.hasNext()) {
				CriterionInstantVO criterion = it.next();
				if (criterion != null) {
					if (criterion.getTieId() != null && criterion.getPropertyId() != null) {
						CriterionInstantVO operator = new CriterionInstantVO();
						CriterionInstantVO value = new CriterionInstantVO();
						operator.setTieId(criterion.getTieId());
						operator.setPosition(criterion.getPosition());
						operator.setSelectStatementIndex(criterion.getSelectStatementIndex());
						value.copy(criterion);
						value.setTieId(null);
						unfolded.add(operator);
						unfolded.add(value);
					} else {
						unfolded.add(criterion);
					}
				}
			}
		} else {
			unfolded = new ArrayList<CriterionInstantVO>();
		}
		return unfolded;
	}
}
