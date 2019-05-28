package org.phoenixctms.ctsms.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.Search;

public class ChunkedRemoveAll<DAO> extends ChunkedDaoOperationAdapter<DAO, Object> {

	public static <DAO> long remove(DAO dao) throws Exception {
		return remove(dao, PageSizes.DEFAULT);
	}

	public static <DAO> long remove(DAO dao, int pageSize) throws Exception {
		return (new ChunkedRemoveAll(dao)).remove(pageSize);
	}

	public static <DAO> long remove(DAO dao, Search search) throws Exception {
		return remove(dao, search, PageSizes.DEFAULT);
	}

	public static <DAO> long remove(DAO dao, Search search, int pageSize) throws Exception {
		return (new ChunkedRemoveAll(dao, search)).remove(pageSize);
	}

	public static <DAO> long remove(DAO dao, Search search, PageSizes pageSize) throws Exception {
		return remove(dao, search, pageSize.value);
	}

	public static <DAO> long remove(DAO dao, PageSizes pageSize) throws Exception {
		return remove(dao, pageSize.value);
	}

	private Method removePage;
	private Method remove;

	public ChunkedRemoveAll(DAO dao) throws Exception {
		super(dao);
		defineRemoveMethods(dao);
	}

	public ChunkedRemoveAll(DAO dao, Search search) throws Exception {
		super(dao, search);
		defineRemoveMethods(dao);
	}

	protected Collection<Object> convertPage(Collection<Object> page) {
		return page;
	}

	private void defineRemoveMethods(DAO dao) throws Exception {
		removePage = getRemovePageMethod(dao.getClass());
		remove = getRemoveMethod(dao.getClass());
	}

	protected Method getRemoveMethod(Class dao) throws NoSuchMethodException, SecurityException {
		return dao.getMethod("remove", Long.class);
	}

	protected Method getRemovePageMethod(Class dao) throws NoSuchMethodException, SecurityException {
		return dao.getMethod("remove", Collection.class);
	}

	@Override
	protected boolean isIncrementPageNumber() {
		return false;
	}

	@Override
	protected boolean process(Collection<Object> page, Object passThrough)
			throws Exception {
		Map<String, Object> inOut = (Map<String, Object>) passThrough;
		if (removePageDone(page.size(), removePage.invoke(dao, convertPage(page)))) {
			inOut.put("count", ((Long) inOut.get("count")) + page.size());
			return true;
		}
		return false;
	}

	@Override
	protected boolean process(Object entity, Object passThrough) throws Exception {
		Map<String, Object> inOut = (Map<String, Object>) passThrough;
		if (removeDone(remove.invoke(dao, CommonUtil.reflectiveGetIdCall(entity)))) {
			inOut.put("count", ((Long) inOut.get("count")) + 1l);
			return true;
		}
		return false;
	}

	public long remove() throws Exception {
		return remove(PageSizes.DEFAULT);
	}

	public long remove(int pageSize)
			throws Exception {
		Map<String, Object> passThrough = new HashMap<String, Object>();
		passThrough.put("count", 0l);
		processPages(pageSize, passThrough);
		return (Long) passThrough.get("count");
	}

	public long remove(PageSizes pageSize) throws Exception {
		return remove(pageSize.value);
	}

	protected boolean removeDone(Object removeResult) {
		return true;
	}

	protected boolean removePageDone(int pageSize, Object removePageResult) {
		return true;
	}
}
