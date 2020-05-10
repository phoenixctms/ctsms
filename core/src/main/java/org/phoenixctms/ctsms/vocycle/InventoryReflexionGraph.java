package org.phoenixctms.ctsms.vocycle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.compare.InventoryComparator;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.DepartmentDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryCategory;
import org.phoenixctms.ctsms.domain.InventoryCategoryDao;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.InventoryDaoImpl;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.vo.InventoryOutVO;

public class InventoryReflexionGraph extends ReflexionCycleHelper<Inventory, InventoryOutVO> {

	private static final boolean LIMIT_INSTANCES = true;
	private static final boolean LIMIT_PARENTS_DEPTH = true;
	private static final boolean LIMIT_CHILDREN_DEPTH = true;
	private InventoryDaoImpl inventoryDaoImpl;
	private InventoryDao inventoryDao;
	private InventoryCategoryDao inventoryCategoryDao;
	private DepartmentDao departmentDao;
	private StaffDao staffDao;
	private UserDao userDao;
	private int maxInstances;
	private int parentDepth;
	private int childrenDepth;
	private final static int DEFAULT_MAX_INSTANCES = 1;
	private final static int DEFAULT_PARENT_DEPTH = Integer.MAX_VALUE >> 1;
	private final static int DEFAULT_CHILDREN_DEPTH = Integer.MAX_VALUE >> 1;

	public InventoryReflexionGraph(InventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
	}

	public InventoryReflexionGraph(InventoryDaoImpl inventoryDaoImpl,
			InventoryCategoryDao inventoryCategoryDao,
			DepartmentDao departmentDao, StaffDao staffDao, UserDao userDao, Integer... maxInstances) {
		this.maxInstances = maxInstances != null && maxInstances.length > 0 ? (maxInstances[0] == null ? DEFAULT_MAX_INSTANCES : maxInstances[0]) : DEFAULT_MAX_INSTANCES;
		this.parentDepth = maxInstances != null && maxInstances.length > 1 ? (maxInstances[1] == null ? DEFAULT_PARENT_DEPTH : maxInstances[1]) : DEFAULT_PARENT_DEPTH;
		this.childrenDepth = maxInstances != null && maxInstances.length > 2 ? (maxInstances[2] == null ? DEFAULT_CHILDREN_DEPTH : maxInstances[2]) : DEFAULT_CHILDREN_DEPTH;
		this.inventoryDaoImpl = inventoryDaoImpl;
		this.inventoryCategoryDao = inventoryCategoryDao;
		this.departmentDao = departmentDao;
		this.staffDao = staffDao;
		this.userDao = userDao;
	}

	@Override
	protected Inventory aquireWriteLock(Long id) throws ServiceException {
		return inventoryDao.load(id, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	protected Class getAVOClass() {
		return InventoryOutVO.class;
	}

	@Override
	protected Inventory getChild(Inventory source) {
		return null;
	}

	@Override
	protected Collection<Inventory> getEntityChildren(Inventory source) {
		Collection<Inventory> children = source.getChildren();
		if (children.size() > 1) {
			TreeSet<Inventory> result = new TreeSet<Inventory>(new InventoryComparator());
			result.addAll(children);
			return result;
		} else {
			return children;
		}
	}

	@Override
	protected Long getEntityId(Inventory source) {
		return source.getId();
	}

	@Override
	protected Collection<Inventory> getEntityParents(Inventory source) {
		return null;
	}

	@Override
	protected ReflexionDepth getInitialReflexionDepth() {
		return new ReflexionDepth(parentDepth, childrenDepth);
	}

	@Override
	protected int getMaxInstances() {
		return maxInstances;
	}

	@Override
	protected Inventory getParent(Inventory source) {
		return source.getParent();
	}

	@Override
	protected Collection<InventoryOutVO> getVOChildren(InventoryOutVO target) {
		return target.getChildren();
	}

	@Override
	protected Collection<InventoryOutVO> getVOParents(InventoryOutVO target) {
		return null;
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
	protected InventoryOutVO newVO() {
		return new InventoryOutVO();
	}

	@Override
	protected void setChild(InventoryOutVO target, InventoryOutVO childVO) {
	}

	@Override
	protected void setParent(InventoryOutVO target, InventoryOutVO parentVO) {
		target.setParent(parentVO);
	}

	@Override
	protected void throwGraphLoopException(List<Inventory> path) throws ServiceException {
		Iterator<Inventory> it = path.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			appendLoopPath(sb, L10nUtil.getMessage(MessageCodes.LOOP_PATH_INVENTORY_LABEL,DefaultMessages.LOOP_PATH_INVENTORY_LABEL,CommonUtil.inventoryOutVOToString(inventoryDao.toInventoryOutVO(it.next()))));
		}
		throw L10nUtil.initServiceException(ServiceExceptionCodes.INVENTORY_GRAPH_LOOP, sb.toString());
	}

	@Override
	protected void toVORemainingFields(Inventory source, InventoryOutVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		inventoryDaoImpl.toInventoryOutVOBase(source, target);
		InventoryCategory category = source.getCategory();
		Department department = source.getDepartment();
		User modifiedUser = source.getModifiedUser();
		Staff owner = source.getOwner();
		if (category != null) {
			target.setCategory(inventoryCategoryDao.toInventoryCategoryVO(category));
		}
		if (department != null) {
			target.setDepartment(departmentDao.toDepartmentVO(department));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(userDao.toUserOutVO(modifiedUser));
		}
		if (owner != null) {
			target.setOwner(staffDao.toStaffOutVO(owner));
		}
		target.setChildrenCount(inventoryDaoImpl.getChildrenCount(source.getId()));
	}
}