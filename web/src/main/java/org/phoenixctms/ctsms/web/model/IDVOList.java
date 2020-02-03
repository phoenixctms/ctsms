package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IDVOList implements List<IDVO> {

	private static Object IDVOToVO(IDVO idVo) {
		if (idVo != null) {
			return idVo.getVo();
		} else {
			return null;
		}
	}

	private static ArrayList<Object> IDVVOCollectionToVOCollection(Collection<? extends IDVO> idVos) {
		if (idVos != null) {
			ArrayList<Object> result = new ArrayList<Object>(idVos.size());
			Iterator<? extends IDVO> it = idVos.iterator();
			while (it.hasNext()) {
				result.add(IDVOToVO(it.next()));
			}
			return result;
		} else {
			return null;
		}
	}

	private static ArrayList<IDVO> VOCollectionToIDVOCollection(Collection<Object> vos) {
		if (vos != null) {
			ArrayList<IDVO> result = new ArrayList<IDVO>(vos.size());
			Iterator<Object> it = vos.iterator();
			while (it.hasNext()) {
				result.add(VOToIDVO(it.next()));
			}
			return result;
		} else {
			return null;
		}
	}

	private static IDVO VOToIDVO(Object vo) {
		if (vo != null) {
			return new IDVO(vo);
		} else {
			return null;
		}
	}

	private List backingList;

	public IDVOList(List c) {
		if (c == null) {
			throw new NullPointerException();
		}
		this.backingList = c;
	}

	@Override
	public boolean add(IDVO e) {
		return backingList.add(IDVOToVO(e));
	}

	@Override
	public void add(int index, IDVO element) {
		backingList.add(index, IDVOToVO(element));
	}

	@Override
	public boolean addAll(Collection<? extends IDVO> c) {
		return backingList.addAll(IDVVOCollectionToVOCollection(c));
	}

	@Override
	public boolean addAll(int index, Collection<? extends IDVO> c) {
		return backingList.addAll(index, IDVVOCollectionToVOCollection(c));
	}

	@Override
	public void clear() {
		backingList.clear();
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof IDVO) {
			return backingList.contains(IDVOToVO((IDVO) o));
		}
		return backingList.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		Iterator<?> e = c.iterator();
		while (e.hasNext()) {
			if (!contains(e.next())) {
				return false;
			}
		}
		return true;
	}

	private Iterator<IDVO> createIterator() {
		final List<IDVO> thisList = this;
		return new Iterator<IDVO>() {

			private List<IDVO> list;
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
			public IDVO next() {
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

	@Override
	public IDVO get(int index) {
		return VOToIDVO(backingList.get(index));
	}

	@Override
	public int indexOf(Object o) {
		if (o instanceof IDVO) {
			return backingList.indexOf(IDVOToVO((IDVO) o));
		}
		return backingList.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return backingList.isEmpty();
	}

	@Override
	public Iterator<IDVO> iterator() {
		return createIterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		if (o instanceof IDVO) {
			return backingList.lastIndexOf(IDVOToVO((IDVO) o));
		}
		return backingList.lastIndexOf(o);
	}

	@Override
	public ListIterator<IDVO> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<IDVO> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IDVO remove(int index) {
		return VOToIDVO(backingList.remove(index));
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof IDVO) {
			return backingList.remove(IDVOToVO((IDVO) o));
		}
		return backingList.remove(o);
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
	public IDVO set(int index, IDVO element) {
		return VOToIDVO(backingList.set(index, IDVOToVO(element)));
	}

	@Override
	public int size() {
		return backingList.size();
	}

	@Override
	public List<IDVO> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		int size = this.size();
		Object[] result = new Integer[size];
		for (int i = 0; i < size; i++) {
			result[i] = get(i);
		}
		return result;
	}

	@Override
	public <E> E[] toArray(E[] a) {
		int size = this.size();
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
