// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.util.Collection;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.query.SubCriteriaMap;
import org.phoenixctms.ctsms.vo.ContactDetailTypeVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueInVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

/**
 * @see StaffContactDetailValue
 */
public class StaffContactDetailValueDaoImpl
		extends StaffContactDetailValueDaoBase {

	private org.hibernate.Criteria createContactDetailValueCriteria() {
		org.hibernate.Criteria contactDetailValueCriteria = this.getSession().createCriteria(StaffContactDetailValue.class);
		return contactDetailValueCriteria;
	}

	@Override
	protected Collection<StaffContactDetailValue> handleFindByStaff(
			Long staffId, Boolean notify, Boolean na, Boolean email, Boolean phone,
			PSFVO psf) throws Exception {
		org.hibernate.Criteria staffContactDetailValueCriteria = createContactDetailValueCriteria();
		SubCriteriaMap criteriaMap = new SubCriteriaMap(StaffContactDetailValue.class, staffContactDetailValueCriteria);
		if (staffId != null) {
			staffContactDetailValueCriteria.add(Restrictions.eq("staff.id", staffId.longValue()));
		}
		if (notify != null) {
			staffContactDetailValueCriteria.add(Restrictions.eq("notify", notify.booleanValue()));
		}
		if (na != null) {
			staffContactDetailValueCriteria.add(Restrictions.eq("na", na.booleanValue()));
		}
		if (email != null || phone != null) {
			org.hibernate.Criteria typeCriteria = staffContactDetailValueCriteria.createCriteria("type");
			if (email != null) {
				typeCriteria.add(Restrictions.eq("email", email.booleanValue()));
			}
			if (phone != null) {
				typeCriteria.add(Restrictions.eq("phone", phone.booleanValue()));
			}
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return staffContactDetailValueCriteria.list();
	}

	@Override
	protected long handleGetCount(Long staffId, Boolean notify, Boolean na, Boolean email, Boolean phone) throws Exception {
		org.hibernate.Criteria staffContactDetailValueCriteria = createContactDetailValueCriteria();
		if (staffId != null) {
			staffContactDetailValueCriteria.add(Restrictions.eq("staff.id", staffId.longValue()));
		}
		if (notify != null) {
			staffContactDetailValueCriteria.add(Restrictions.eq("notify", notify.booleanValue()));
		}
		if (na != null) {
			staffContactDetailValueCriteria.add(Restrictions.eq("na", na.booleanValue()));
		}
		if (email != null || phone != null) {
			org.hibernate.Criteria typeCriteria = staffContactDetailValueCriteria.createCriteria("type");
			if (email != null) {
				typeCriteria.add(Restrictions.eq("email", email.booleanValue()));
			}
			if (phone != null) {
				typeCriteria.add(Restrictions.eq("phone", phone.booleanValue()));
			}
		}
		return (Long) staffContactDetailValueCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private StaffContactDetailValue loadStaffContactDetailValueFromStaffContactDetailValueInVO(StaffContactDetailValueInVO staffContactDetailValueInVO) {
		StaffContactDetailValue staffContactDetailValue = null;
		Long id = staffContactDetailValueInVO.getId();
		if (id != null) {
			staffContactDetailValue = this.load(id);
		}
		if (staffContactDetailValue == null) {
			staffContactDetailValue = StaffContactDetailValue.Factory.newInstance();
		}
		return staffContactDetailValue;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private StaffContactDetailValue loadStaffContactDetailValueFromStaffContactDetailValueOutVO(StaffContactDetailValueOutVO staffContactDetailValueOutVO) {
		StaffContactDetailValue staffContactDetailValue = this.load(staffContactDetailValueOutVO.getId());
		if (staffContactDetailValue == null) {
			staffContactDetailValue = StaffContactDetailValue.Factory.newInstance();
		}
		return staffContactDetailValue;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public StaffContactDetailValue staffContactDetailValueInVOToEntity(StaffContactDetailValueInVO staffContactDetailValueInVO) {
		StaffContactDetailValue entity = this.loadStaffContactDetailValueFromStaffContactDetailValueInVO(staffContactDetailValueInVO);
		this.staffContactDetailValueInVOToEntity(staffContactDetailValueInVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void staffContactDetailValueInVOToEntity(
			StaffContactDetailValueInVO source,
			StaffContactDetailValue target,
			boolean copyIfNull) {
		super.staffContactDetailValueInVOToEntity(source, target, copyIfNull);
		Long typeId = source.getTypeId();
		Long staffId = source.getStaffId();
		if (typeId != null) {
			target.setType(this.getContactDetailTypeDao().load(typeId));
		} else if (copyIfNull) {
			target.setType(null);
		}
		if (staffId != null) {
			Staff staff = this.getStaffDao().load(staffId);
			target.setStaff(staff);
			staff.addContactDetailValues(target);
		} else if (copyIfNull) {
			Staff staff = target.getStaff();
			target.setStaff(null);
			if (staff != null) {
				staff.removeContactDetailValues(target);
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public StaffContactDetailValue staffContactDetailValueOutVOToEntity(StaffContactDetailValueOutVO staffContactDetailValueOutVO) {
		StaffContactDetailValue entity = this.loadStaffContactDetailValueFromStaffContactDetailValueOutVO(staffContactDetailValueOutVO);
		this.staffContactDetailValueOutVOToEntity(staffContactDetailValueOutVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void staffContactDetailValueOutVOToEntity(
			StaffContactDetailValueOutVO source,
			StaffContactDetailValue target,
			boolean copyIfNull) {
		super.staffContactDetailValueOutVOToEntity(source, target, copyIfNull);
		ContactDetailTypeVO typeVO = source.getType();
		StaffOutVO staffVO = source.getStaff();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		if (typeVO != null) {
			target.setType(this.getContactDetailTypeDao().contactDetailTypeVOToEntity(typeVO));
		} else if (copyIfNull) {
			target.setType(null);
		}
		if (staffVO != null) {
			Staff staff = this.getStaffDao().staffOutVOToEntity(staffVO);
			target.setStaff(staff);
			staff.addContactDetailValues(target);
		} else if (copyIfNull) {
			Staff staff = target.getStaff();
			target.setStaff(null);
			if (staff != null) {
				staff.removeContactDetailValues(target);
			}
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public StaffContactDetailValueInVO toStaffContactDetailValueInVO(final StaffContactDetailValue entity) {
		return super.toStaffContactDetailValueInVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toStaffContactDetailValueInVO(
			StaffContactDetailValue source,
			StaffContactDetailValueInVO target) {
		super.toStaffContactDetailValueInVO(source, target);
		ContactDetailType type = source.getType();
		Staff staff = source.getStaff();
		if (type != null) {
			target.setTypeId(type.getId());
		}
		if (staff != null) {
			target.setStaffId(staff.getId());
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public StaffContactDetailValueOutVO toStaffContactDetailValueOutVO(final StaffContactDetailValue entity) {
		return super.toStaffContactDetailValueOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toStaffContactDetailValueOutVO(
			StaffContactDetailValue source,
			StaffContactDetailValueOutVO target) {
		super.toStaffContactDetailValueOutVO(source, target);
		ContactDetailType type = source.getType();
		Staff staff = source.getStaff();
		User modifiedUser = source.getModifiedUser();
		if (type != null) {
			target.setType(this.getContactDetailTypeDao().toContactDetailTypeVO(type));
		}
		if (staff != null) {
			target.setStaff(this.getStaffDao().toStaffOutVO(staff));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
	}
}