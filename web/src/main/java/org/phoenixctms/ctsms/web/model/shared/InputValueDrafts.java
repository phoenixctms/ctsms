package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.phoenixctms.ctsms.util.CommonUtil;

/**
 * Keeps the entered but not yet saved input values of a value form (tags, inquiries, eCRF fields)
 * while the form shows only a page or a Find fields result of all fields.
 *
 * @param <K> key of a value, e.g. the tag id
 * @param <V> the input value VO
 */
public abstract class InputValueDrafts<K, V> {

	protected final static HashSet<String> EQUALS_EXCLUDES = new HashSet<String>(CommonUtil.VO_EQUALS_EXCLUDES);
	static {
		// compared separately by time, as java.sql.Timestamp.equals(java.util.Date) is always false:
		EQUALS_EXCLUDES.add("*.getDateValue");
		EQUALS_EXCLUDES.add("*.getTimestampValue");
		EQUALS_EXCLUDES.add("*.getTimeValue");
	}

	protected static boolean isSameTime(Date a, Date b) {
		if (a == null) {
			return b == null;
		} else if (b == null) {
			return false;
		}
		return a.getTime() == b.getTime();
	}

	private final LinkedHashMap<K, V> drafts = new LinkedHashMap<K, V>();
	private final HashSet<K> notSaveableKeys = new HashSet<K>();
	private final HashMap<K, V> savedValues = new HashMap<K, V>();

	/**
	 * Remember the pending edits among the shown values. Values equal to their saved value are no drafts.
	 *
	 * @param values the shown input values
	 * @param notSaveable keys of shown values that a page save does not include (e.g. a new eCRF series section), or null
	 */
	public void capture(Collection<V> values, Set<K> notSaveable) {
		if (values != null) {
			Iterator<V> it = values.iterator();
			while (it.hasNext()) {
				V value = it.next();
				K key = getKey(value);
				V savedValue = savedValues.get(key);
				if (savedValue != null) {
					if (isEqual(value, savedValue)) {
						drafts.remove(key);
						notSaveableKeys.remove(key);
					} else {
						drafts.put(key, value);
						if (notSaveable != null && notSaveable.contains(key)) {
							notSaveableKeys.add(key);
						} else {
							notSaveableKeys.remove(key);
						}
					}
				}
			}
		}
	}

	public void clear() {
		drafts.clear();
		notSaveableKeys.clear();
	}

	public int getHiddenDraftCount(Collection<V> shownValues) {
		return getHiddenDrafts(shownValues).size();
	}

	/**
	 * @return the saveable drafts of values that are not shown
	 */
	public ArrayList<V> getHiddenDrafts(Collection<V> shownValues) {
		HashSet<K> shownKeys = getKeys(shownValues);
		ArrayList<V> result = new ArrayList<V>();
		Iterator<java.util.Map.Entry<K, V>> it = drafts.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry<K, V> draft = it.next();
			if (!shownKeys.contains(draft.getKey()) && !notSaveableKeys.contains(draft.getKey())) {
				result.add(draft.getValue());
			}
		}
		return result;
	}

	protected abstract K getKey(V value);

	private HashSet<K> getKeys(Collection<V> values) {
		HashSet<K> keys = new HashSet<K>();
		if (values != null) {
			Iterator<V> it = values.iterator();
			while (it.hasNext()) {
				keys.add(getKey(it.next()));
			}
		}
		return keys;
	}

	protected abstract boolean isEqual(V value, V savedValue);

	public boolean isUnsaved(V value) {
		if (value != null) {
			V savedValue = savedValues.get(getKey(value));
			return savedValue != null && !isEqual(value, savedValue);
		}
		return false;
	}

	/**
	 * Forget the drafts of the given values, e.g. after they were saved.
	 */
	public void remove(Collection<V> values) {
		Iterator<K> it = getKeys(values).iterator();
		while (it.hasNext()) {
			K key = it.next();
			drafts.remove(key);
			notSaveableKeys.remove(key);
		}
	}

	/**
	 * A single value was saved or loaded on its own: it is no draft any more.
	 *
	 * @param savedValue fresh input value copied from the saved value
	 */
	public void setSavedValue(V savedValue) {
		K key = getKey(savedValue);
		savedValues.put(key, savedValue);
		drafts.remove(key);
		notSaveableKeys.remove(key);
	}

	/**
	 * Replace shown values by their drafts.
	 */
	public void restore(List<V> values) {
		if (values != null && drafts.size() > 0) {
			ListIterator<V> it = values.listIterator();
			while (it.hasNext()) {
				V draft = drafts.get(getKey(it.next()));
				if (draft != null) {
					it.set(draft);
				}
			}
		}
	}

	/**
	 * @param savedValues fresh input values copied from the loaded (saved) values; must not be the instances shown in the form
	 */
	public void setSavedValues(Collection<V> savedValues) {
		this.savedValues.clear();
		if (savedValues != null) {
			Iterator<V> it = savedValues.iterator();
			while (it.hasNext()) {
				V savedValue = it.next();
				this.savedValues.put(getKey(savedValue), savedValue);
			}
		}
	}
}
