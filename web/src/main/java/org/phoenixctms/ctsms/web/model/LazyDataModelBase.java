package org.phoenixctms.ctsms.web.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.enumeration.InputFieldL10nBundle;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public abstract class LazyDataModelBase<T> extends LazyDataModel<IDVO> implements SelectableDataModel<IDVO> {

	private String currentPageIdsString;
	// per bundle, the languages with a text of their own for the localization keys of the current page, by key (see getL10nKeyLocales):
	private HashMap<InputFieldL10nBundle, HashMap<String, String>> l10nKeyLocales = new HashMap<InputFieldL10nBundle, HashMap<String, String>>();

	public String getCurrentPageIds() {
		return (currentPageIdsString != null ? currentPageIdsString : "");
	}

	protected Long getPageId(IDVO idvo) {
		return idvo.getId();
	}

	protected abstract Collection<T> getLazyResult(PSFVO psf);

	@Override
	public IDVO getRowData(String key) {
		Object vo = this.getRowElement(WebUtil.stringToLong(key));
		if (vo != null) {
			return new IDVO(vo);
		} else {
			return null;
		}
	}

	protected abstract T getRowElement(Long id);

	/**
	 * The localization key a row holds for a bundle (e.g. the section of an eCRF field), to look up the languages with a text of
	 * their own (see getL10nKeyLocales); none by default.
	 */
	protected String getL10nKey(Object vo, InputFieldL10nBundle bundle) {
		return null;
	}

	/**
	 * The languages with a text of their own for the localization key a row holds for a bundle (see getL10nKey), as a list of
	 * language names. The first lookup of a bundle fetches the languages of the keys of all rows of the current page with a single
	 * service call.
	 */
	public String getL10nKeyLocales(InputFieldL10nBundle bundle, Object vo) {
		String key = bundle != null ? getL10nKey(vo instanceof IDVO ? ((IDVO) vo).getVo() : vo, bundle) : null;
		if (key == null || key.length() == 0) {
			return "";
		}
		HashMap<String, String> locales = l10nKeyLocales.get(bundle);
		if (locales == null || !locales.containsKey(key)) {
			HashSet<String> keys = new HashSet<String>();
			keys.add(key);
			if (locales == null) {
				locales = new HashMap<String, String>();
				l10nKeyLocales.put(bundle, locales);
				Object page = getWrappedData();
				if (page instanceof Collection) {
					Iterator<?> it = ((Collection<?>) page).iterator();
					while (it.hasNext()) {
						Object row = it.next();
						String rowKey = getL10nKey(row instanceof IDVO ? ((IDVO) row).getVo() : row, bundle);
						if (rowKey != null && rowKey.length() > 0) {
							keys.add(rowKey);
						}
					}
				}
			}
			locales.putAll(WebUtil.getInputFieldL10nKeyLocales(bundle, keys));
		}
		String result = locales.get(key);
		return result != null ? result : "";
	}

	@Override
	public String getRowKey(IDVO element) {
		return element.getId().toString();
	}

	@Override
	public List<IDVO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		return load(createPSFVO(first, pageSize, sortField, sortOrder, filters, true));
	}

	@Override
	protected List<IDVO> load(PSFVO psf) {
		l10nKeyLocales.clear();
		Collection<?> lazyResult = getLazyResult(psf);
		IDVO.transformVoCollection(lazyResult);
		PSFVO rowPsf;
		if (psf != null) {
			rowPsf = psf;
		} else {
			rowPsf = new PSFVO();
		}
		if (lazyResult != null && lazyResult.size() > 0) {
			StringBuilder sb = new StringBuilder();
			Iterator idIt = lazyResult.iterator();
			int rowIndex = 0;
			while (idIt.hasNext()) {
				Object item = idIt.next();
				if (item != null && item instanceof IDVO) {
					IDVO idvo = (IDVO) item;
					idvo.setRowIndex(WebUtil.FACES_INITIAL_ROW_INDEX + rowIndex);
					idvo.setPsf(rowPsf);
					rowIndex++;
					if (idvo.getId() != null) {
						if (sb.length() > 0) {
							sb.append(WebUtil.ID_SEPARATOR_STRING);
						}
						sb.append(getPageId(idvo).toString());
					}
				}
			}
			currentPageIdsString = sb.toString();
		} else {
			currentPageIdsString = null;
		}
		if (psf != null && psf.getUpdateRowCount()) {
			updateRowCount(psf);
		}
		return (List<IDVO>) lazyResult;
	}
}
