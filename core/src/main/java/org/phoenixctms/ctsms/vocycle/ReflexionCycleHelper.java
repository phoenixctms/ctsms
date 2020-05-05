package org.phoenixctms.ctsms.vocycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.phoenixctms.ctsms.compare.EntityIDComparator;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;

public abstract class ReflexionCycleHelper<A, AVO> extends CycleHelperBase<A, AVO> {

	private EntityIDComparator<A> idComparator;

	protected abstract A aquireWriteLock(Long id) throws ServiceException;

	protected void appendLoopPath(StringBuilder sb, String entityLabel) {
		if (sb.length() > 0) {
			sb.append(L10nUtil.getString(MessageCodes.LOOP_PATH_SEPARATOR, DefaultMessages.LOOP_PATH_SEPARATOR));
		}
		sb.append(entityLabel);
	}

	private final void checkChildren(A entity, A root, HashSet<Long> checked, Set<A> path) throws ServiceException {
		Long id;
		if (entity != null && !checked.contains(id = getEntityId(entity))) {
			// lock
			aquireWriteLock(id);
			A child = getChild(entity);
			if (checkEntity(child, entity, root, path)) {
				checkChildren(child, root, checked, path);
			}
			path.remove(child);
			Collection<A> children = getEntityChildren(entity);
			if (children != null && children.size() > 0) {
				ArrayList<A> childrenSorted = new ArrayList<A>(children);
				Collections.sort(childrenSorted, getIdComparator());
				Iterator<A> it = childrenSorted.iterator();
				while (it.hasNext()) {
					child = it.next();
					LinkedHashSet<A> lastPath = new LinkedHashSet<A>(path);
					if (checkEntity(child, entity, root, lastPath)) {
						checkChildren(child, root, checked, lastPath);
					}
					path.remove(child);
				}
			}
			checked.add(id);
		}
	}

	private final boolean checkEntity(A parentChild, A entity, A root, Set<A> path) throws ServiceException {
		if (parentChild != null && root != null) {
			//lock
			Long id = getEntityId(parentChild);
			aquireWriteLock(id);
			path.add(parentChild);
			if (id.equals(getEntityId(root))) {
				ArrayList<A> loopPath = new ArrayList<A>(path.size() + 1);
				loopPath.add(root);
				loopPath.addAll(path);
				throwGraphLoopException(loopPath);
			} else {
				return true;
			}
		}
		return false;
	}

	public final void checkGraphLoop(A entity, boolean parents, boolean children) throws ServiceException {
		HashSet<Long> checked = new HashSet<Long>();
		LinkedHashSet<A> path = new LinkedHashSet<A>();
		if (parents) {
			checkParents(entity, entity, checked, path);
		}
		checked.clear();
		path.clear();
		if (children) {
			checkChildren(entity, entity, checked, path);
		}
	}

	private final void checkParents(A entity, A root, HashSet<Long> checked, Set<A> path) throws ServiceException {
		Long id;
		if (entity != null && !checked.contains(id = getEntityId(entity))) {
			//lock
			aquireWriteLock(id);
			A parent = getParent(entity);
			if (checkEntity(parent, entity, root, path)) {
				checkParents(parent, root, checked, path);
			}
			path.remove(parent);
			Collection<A> parents = getEntityParents(entity);
			if (parents != null && parents.size() > 0) {
				ArrayList<A> parentsSorted = new ArrayList<A>(parents);
				Collections.sort(parentsSorted, getIdComparator());
				Iterator<A> it = parentsSorted.iterator();
				while (it.hasNext()) {
					parent = it.next();
					if (checkEntity(parent, entity, root, path)) {
						checkParents(parent, root, checked, path);
					}
					path.remove(parent);
				}
			}
			checked.add(id);
		}
	}

	protected abstract A getChild(A source);

	protected abstract Collection<A> getEntityChildren(A source);

	protected abstract Long getEntityId(A source);

	protected abstract Collection<A> getEntityParents(A source);

	private EntityIDComparator<A> getIdComparator() {
		if (idComparator == null) {
			idComparator = new EntityIDComparator<A>(false);
		}
		return idComparator;
	}

	protected abstract ReflexionDepth getInitialReflexionDepth();

	protected abstract int getMaxInstances();

	protected abstract A getParent(A source);

	protected abstract Collection<AVO> getVOChildren(AVO target);

	protected abstract Collection<AVO> getVOParents(AVO target);

	protected abstract boolean limitChildrenDepth();

	protected abstract boolean limitInstances();

	protected abstract boolean limitParentsDepth();

	protected abstract AVO newVO();

	protected abstract void setChild(AVO target, AVO childVO);

	protected abstract void setParent(AVO target, AVO parentVO);

	protected void throwGraphLoopException(List<A> path) throws ServiceException {
	}

	@Override
	public void toVOHelper(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap) {
		toVOHelper(source, target, voMap, getInitialReflexionDepth());
	}

	protected final void toVOHelper(A source, AVO target, HashMap<Class, HashMap<Long, Object>> voMap, ReflexionDepth reflexionDepth) {
		if (!voMapContainsKey(getEntityId(source), voMap)) {
			// debug(source,"NOT in map");
			if (!limitInstances() || voMapSize(voMap) < getMaxInstances()) {
				// debug(source,"put into map");
				voMapPut(getEntityId(source), target, voMap);
			} else {
				// debug(source,"instance limit of " + getMaxInstances() + " exceeded");
				voMapPut(getEntityId(source), null, voMap);
				return;
			}
			// } else {
			// debug(source,"FOUND in map");
		}
		// if (limitParentsDepth()) {
		// debug(source,"parent depth: " + treeRecursionDepth.getParentDepth());
		// }
		// http://stackoverflow.com/questions/1792501/does-java-serialization-work-for-cyclic-references
		if (!limitParentsDepth() || reflexionDepth.getParentDepth() > 0) {
			Collection<A> parents = getEntityParents(source);
			if (parents != null && parents.size() > 0) {
				// debug(source,"processing " + parents.size() + " parents");
				Iterator<A> it = parents.iterator();
				while (it.hasNext()) {
					A parent = it.next();
					if (!voMapContainsKey(getEntityId(parent), voMap)) {
						this.toVOHelper(parent, newVO(), voMap, reflexionDepth == null ? null : reflexionDepth.towardsParent());
					}
					AVO parentVO = voMapGet(getEntityId(parent), voMap);
					if (parentVO != null) { // symmetric (copyifnull) behavior to getBVO of graphcycle1helper
						getVOParents(target).add(parentVO);
					} else {
						break;
					}
				}
				// } else {
				// debug(source,"no parents");
			}
			A parent = getParent(source);
			AVO parentVO;
			if (parent != null) {
				// debug(source,"processing parent");
				if (!voMapContainsKey(getEntityId(parent), voMap)) {
					this.toVOHelper(parent, newVO(), voMap, reflexionDepth == null ? null : reflexionDepth.towardsParent());
				}
				parentVO = voMapGet(getEntityId(parent), voMap);
			} else {
				// debug(source,"no parent");
				parentVO = null;
			}
			setParent(target, parentVO); // symmetric copyifnull behavior to getBVO of graphcycle1helper
			// } else {
			// debug(source,"parent depth exceeded");
		}
		// if (limitChildrenDepth()) {
		// debug(source,"child depth: " + treeRecursionDepth.getChildrenDepth());
		// }
		if (!limitChildrenDepth() || reflexionDepth.getChildrenDepth() > 0) {
			Collection<A> children = getEntityChildren(source);
			if (children != null && children.size() > 0) {
				// debug(source,"processing " + children.size() + " children");
				Iterator<A> it = children.iterator();
				while (it.hasNext()) {
					A child = it.next();
					if (!voMapContainsKey(getEntityId(child), voMap)) {
						this.toVOHelper(child, newVO(), voMap, reflexionDepth == null ? null : reflexionDepth.towardsChildren());
					}
					AVO childVO = voMapGet(getEntityId(child), voMap);
					if (childVO != null) { // symmetric (copyifnull) behavior to getBVO of graphcycle1helper
						getVOChildren(target).add(childVO);
					} else {
						break;
					}
				}
				// } else {
				// debug(source,"no children");
			}
			A child = getChild(source);
			AVO childVO;
			if (child != null) {
				// debug(source,"processing child");
				if (!voMapContainsKey(getEntityId(child), voMap)) {
					this.toVOHelper(child, newVO(), voMap, reflexionDepth == null ? null : reflexionDepth.towardsChildren());
				}
				childVO = voMapGet(getEntityId(child), voMap);
			} else {
				// debug(source,"no child");
				childVO = null;
			}
			setChild(target, childVO); // symmetric copyifnull behavior to getBVO of graphcycle1helper
			// } else {
			// debug(source,"child depth exceeded");
		}
		toVORemainingFields(source, target, voMap);
	}
}
