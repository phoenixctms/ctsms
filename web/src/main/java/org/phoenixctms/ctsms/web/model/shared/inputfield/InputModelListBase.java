package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;

public abstract class InputModelListBase<T, MODEL> implements List<MODEL> {

	private ArrayList<T> valuesIn;
	private ArrayList<MODEL> models;
	private int size;

	protected InputModelListBase(ArrayList<T> valuesIn) {
		if (valuesIn == null) {
			throw new IllegalArgumentException(Messages.getString(MessageCodes.INPUT_FIELD_VALUE_LIST_NULL));
		}
		this.valuesIn = valuesIn;
		size = valuesIn.size();
		models = new ArrayList<MODEL>(size);
		Iterator<T> it = valuesIn.iterator();
		while (it.hasNext()) {
			models.add(null);
			it.next();
		}
	}

	@Override
	public void add(int index, MODEL element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(MODEL e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends MODEL> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends MODEL> c) {
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

	private Iterator<MODEL> createIterator() {
		final InputModelListBase<T, MODEL> thisList = this;
		return new Iterator<MODEL>() {

			private InputModelListBase<T, MODEL> list;
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
			public MODEL next() {
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

	protected abstract MODEL createModel(T value);

	public List<Object[]> createPaddedList(int fieldsPerRow, int rowSize, int widthThreshold) {
		ArrayList<Object[]> result;
		if (fieldsPerRow >= 1 && fieldsPerRow <= rowSize) {
			if (fieldsPerRow == 1) {
				result = new ArrayList<Object[]>(size);
				for (int i = 0; i < size; i = i + 1) {
					Object[] row = new Object[rowSize + 1];
					row[0] = get(i);
					for (int k = 1; k < rowSize; k++) {
						row[k] = null;
					}
					row[rowSize] = result.size();
					result.add(row);
				}
			} else {
				ArrayList<MODEL> paddedList = new ArrayList<MODEL>(size);
				MODEL model;
				int j = 0;
				for (int i = 0; i < size; i++) {
					model = get(i);
					if (j == 0) {
						paddedList.add(model);
						j = 1;
					} else {
						int width = getWidth(model);
						for (int k = j; k > 0; k--) {
							width += getWidth(get(i - k));
						}
						if (width > widthThreshold) {
							for (int k = 0; k < fieldsPerRow; k++) {
								paddedList.add(null);
							}
							paddedList.add(model);
							j = 1;
						} else {
							paddedList.add(model);
							if (j < fieldsPerRow - 1) {
								j++;
							} else {
								j = 0;
							}
						}
					}
				}
				result = new ArrayList<Object[]>((int) Math.ceil((double) paddedList.size() / (double) fieldsPerRow));
				for (int i = 0; i < paddedList.size(); i = i + fieldsPerRow) {
					Object[] row = new Object[rowSize + 1];
					row[0] = paddedList.get(i);
					for (int k = 1; k < fieldsPerRow; k++) {
						row[k] = ((i + k) < paddedList.size() ? paddedList.get(i + k) : null);
					}
					for (int k = fieldsPerRow; k < rowSize; k++) {
						row[k] = null;
					}
					row[rowSize] = result.size();
					result.add(row);
				}
			}
		} else {
			throw new IllegalArgumentException(Messages.getMessage(MessageCodes.UNSUPPORTED_FIELDS_PER_ROW, fieldsPerRow));
		}
		return result;
	}

	@Override
	public MODEL get(int index) {
		MODEL model = models.get(index);
		if (model == null) {
			model = createModel(valuesIn.get(index));
			setRowIndex(model, index);
			models.set(index, model);
		}
		return model;
	}

	protected abstract int getWidth(MODEL model);

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return valuesIn.isEmpty();
	}

	@Override
	public Iterator<MODEL> iterator() {
		return createIterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<MODEL> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<MODEL> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MODEL remove(int index) {
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
	public MODEL set(int index, MODEL element) {
		throw new UnsupportedOperationException();
	}

	protected abstract void setRowIndex(MODEL model, int index);

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<MODEL> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
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
