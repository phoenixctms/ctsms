package org.phoenixctms.ctsms.web.model.shared.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CriterionTieVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;

public abstract class CriterionListBase<T> implements List<T> {

	private HashMap<Long, CriterionPropertyVO> propertyVOsMap;
	private HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap;
	private HashMap<Long, CriterionTieVO> tieVOsMap;
	private ArrayList<CriterionInVO> criterionsIn;
	private int size;

	protected CriterionListBase(ArrayList<CriterionInVO> criterionsIn) {
		if (criterionsIn == null) {
			throw new IllegalArgumentException(Messages.getString(MessageCodes.CRITERION_LIST_NULL));
		}
		this.criterionsIn = criterionsIn;
		size = criterionsIn.size();
	}

	protected CriterionListBase(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap) {
		if (criterionsIn == null) {
			throw new IllegalArgumentException(Messages.getString(MessageCodes.CRITERION_LIST_NULL));
		}
		this.criterionsIn = criterionsIn;
		size = criterionsIn.size();
		this.propertyVOsMap = (propertyVOsMap == null ? new HashMap<Long, CriterionPropertyVO>() : propertyVOsMap);
	}

	protected CriterionListBase(ArrayList<CriterionInVO> criterionsIn, HashMap<Long, CriterionPropertyVO> propertyVOsMap,
			HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction> restrictionVOsMap) {
		if (criterionsIn == null) {
			throw new IllegalArgumentException(Messages.getString(MessageCodes.CRITERION_LIST_NULL));
		}
		this.criterionsIn = criterionsIn;
		size = criterionsIn.size();
		this.propertyVOsMap = (propertyVOsMap == null ? new HashMap<Long, CriterionPropertyVO>() : propertyVOsMap);
		this.restrictionVOsMap = (restrictionVOsMap == null ? new HashMap<Long, org.phoenixctms.ctsms.enumeration.CriterionRestriction>() : restrictionVOsMap);
	}

	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size; i++) {
			if (get(i).equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	private Iterator<T> createIterator() {
		final CriterionListBase<T> thisList = this;
		return new Iterator<T>() {

			private CriterionListBase<T> list;
			private int pos;
			{
				this.list = thisList;
				pos = 0;
			}

			@Override
			public boolean hasNext() {
				return (pos < list.size());
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				} else {
					pos++;
					return (list.get(pos - 1));
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	protected CriterionInVO getCriterionIn(int criterionIndex) {
		return (testCriterionIndex(criterionIndex) ? criterionsIn.get(criterionIndex) : null);
	}

	protected CriterionPropertyVO getPropertyVO(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : propertyVOsMap.get(criterionVO.getPropertyId()));
	}

	protected CriterionPropertyVO getPropertyVO(int criterionIndex) {
		return getPropertyVO(getCriterionIn(criterionIndex));
	}

	protected org.phoenixctms.ctsms.enumeration.CriterionRestriction getRestriction(CriterionInVO criterionVO) {
		return (criterionVO == null ? null : restrictionVOsMap.get(criterionVO.getRestrictionId()));
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return criterionsIn.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return createIterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	protected CriterionInVO setCriterionIn(int criterionIndex, CriterionInVO criterionVO) {
		return (testCriterionIndex(criterionIndex) ? criterionsIn.set(criterionIndex, criterionVO) : null);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	protected boolean testCriterionIndex(int criterionIndex) {
		return (criterionIndex >= 0 && criterionIndex < size);
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Integer[size];
		for (int i = 0; i < size; i++) {
			result[i] = get(i);
		}
		return result;
	}

	@Override
	public <E> E[] toArray(E[] a) {
		if (a.length >= size) {
			for (int i = 0; i < size; i++) {
				a[i] = (E) get(i);
			}
			if (a.length > size) {
				a[size] = null;
			}
			return a;
		} else {
			Object[] result = new Integer[size];
			for (int i = 0; i < size; i++) {
				result[i] = get(i);
			}
			return (E[]) result;
		}
	}
}
