package org.phoenixctms.ctsms.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.spring.PaginationResult;

import org.phoenixctms.ctsms.Search;

public abstract class ChunkedDaoOperationAdapter<DAO, ENTITY> {

	public enum TableSizes {
		TINY(100),
		DEFAULT(1000),
		BIG(10000),
		HUGE(100000);

		public final int value;

		/**
		 * @param text
		 */
		private TableSizes(final int value) {
			this.value = value;
		}
	}

	protected DAO dao;
	private Method loadAllSorted;
	private Method search;
	private Search searchArg;
	private final static Search EMPTY_SEARCH = new Search();
	static {
		EMPTY_SEARCH.setUseSqlLimiting(true);
	}

	public ChunkedDaoOperationAdapter(DAO dao) throws Exception {
		this.dao = dao;
		defineLoadMethods(dao);
	}

	public ChunkedDaoOperationAdapter(DAO dao, Search search) throws Exception {
		this.dao = dao;
		defineLoadMethods(dao);
		searchArg = search;
		searchArg.setUseSqlLimiting(true);
	}

	private void defineLoadMethods(DAO dao) throws Exception {
		loadAllSorted = dao.getClass().getMethod("loadAllSorted", int.class, int.class);
		search = dao.getClass().getMethod("search", int.class, int.class, Search.class);
	}

	public final long getTotalCount() throws Exception {
		PaginationResult result;
		if (searchArg != null) {
			result = (PaginationResult) search.invoke(dao, 1, 1, searchArg);
		} else {
			result = (PaginationResult) search.invoke(dao, 1, 1, EMPTY_SEARCH);
		}
		return result.getTotalSize();
	}

	protected boolean isIncrementPageNumber() {
		return true;
	}

	protected Collection<ENTITY> load(int pageNumber, int pageSize) throws Exception {
		if (searchArg != null) {
			PaginationResult result = (PaginationResult) search.invoke(dao, pageNumber, pageSize, searchArg);
			return Arrays.<ENTITY> asList((ENTITY[]) result.getData());
		} else {
			return (Collection<ENTITY>) loadAllSorted.invoke(dao, pageNumber, pageSize);
		}
	}

	protected abstract boolean process(Collection<ENTITY> page, Object passThrough) throws Exception;

	protected abstract boolean process(ENTITY entity, Object passThrough) throws Exception;

	public final void processEach(int pageSize, Object passThrough) throws Exception {
		Collection<ENTITY> entities;
		int pageNumber = 1;
		while ((entities = load(pageNumber, pageSize)).size() > 0) {
			Iterator<ENTITY> it = entities.iterator();
			while (it.hasNext()) {
				if (!process(it.next(), passThrough)) {
					return;
				}
			}
			if (isIncrementPageNumber())
				pageNumber++;
		}
	}

	public final void processEach(Object passThrough) throws Exception {
		processEach(TableSizes.DEFAULT, passThrough);
	}

	public final void processEach(TableSizes pageSize, Object passThrough) throws Exception {
		processEach(pageSize.value, passThrough);
	}

	public final void processPages(int pageSize, Object passThrough) throws Exception {
		Collection<ENTITY> entities;
		int pageNumber = 1;
		while ((entities = load(pageNumber, pageSize)).size() > 0) {
			if (!process(entities, passThrough)) {
				return;
			}
			if (isIncrementPageNumber())
				pageNumber++;
		}
	}

	public final void processPages(Object passThrough) throws Exception {
		processPages(TableSizes.DEFAULT, passThrough);
	}

	public final void processPages(TableSizes pageSize, Object passThrough) throws Exception {
		processPages(pageSize.value, passThrough);
	}
}
