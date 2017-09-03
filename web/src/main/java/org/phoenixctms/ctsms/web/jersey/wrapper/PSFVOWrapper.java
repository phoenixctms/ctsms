package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.util.Map;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;

public class PSFVOWrapper {

	// @SerializedName("pageSize")
	private Integer pageSize;
	// @SerializedName("itemFrom")
	private Integer from;
	// @SerializedName("itemTo")
	private Integer to;
	// @SerializedName("totalCount")
	private Long totalCount;
	// @SerializedName("filters")
	private Map filters;
	// @SerializedName("sortBy")
	private String sortBy;
	// @SerializedName("ascending")
	private boolean ascending;

	public PSFVOWrapper(PSFVO psf) {
		pageSize = psf.getPageSize();
		from = psf.getFirst() + 1;
		to = psf.getFirst() + psf.getPageSize();
		if (psf.getRowCount() != null && to > psf.getRowCount()) {
			to = CommonUtil.safeLongToInt(psf.getRowCount());
		}
		// if (psf.getRowCount() != null) {
		// if (psf.getRowCount() > 0l) {
		// from = psf.getFirst() + 1;
		// to = psf.getFirst() + psf.getPageSize();
		// if (to > psf.getRowCount()) {
		// to = CommonUtil.safeLongToInt(psf.getRowCount());
		// }
		// } else {
		// from = 0;
		// to = 0;
		// }
		// totalCount = psf.getRowCount();
		// } else {
		// from = 0;
		// to = 0;
		// totalCount = -1l;
		// }
		totalCount = psf.getRowCount();
		filters = psf.getFilters();
		sortBy = psf.getSortField();
		ascending = psf.getSortOrder();
	}
}