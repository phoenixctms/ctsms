package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.util.Map;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;

public class PSFVOWrapper {

	private Integer pageSize;
	private Integer from;
	private Integer to;
	private Long totalCount;
	private Map filters;
	private String sortBy;
	private boolean ascending;

	public PSFVOWrapper(PSFVO psf) {
		pageSize = psf.getPageSize();
		from = psf.getFirst() + 1;
		to = psf.getFirst() + psf.getPageSize();
		if (psf.getRowCount() != null && to > psf.getRowCount()) {
			to = CommonUtil.safeLongToInt(psf.getRowCount());
		}
		totalCount = psf.getRowCount();
		filters = psf.getFilters();
		sortBy = psf.getSortField();
		ascending = psf.getSortOrder();
	}
}