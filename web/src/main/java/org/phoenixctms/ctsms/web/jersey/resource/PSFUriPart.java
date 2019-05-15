package org.phoenixctms.ctsms.web.jersey.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.PSFVO;

public class PSFUriPart extends PSFVO implements UriPart {

	private final static int DEFAULT_PAGE_SIZE = 10;
	private final static int DEFAULT_PAGE_NUMBER = 1;
	private static final String ASCENDING_FIELD_QUERY_PARAM = "a";
	private static final String DESCENDING_FIELD_QUERY_PARAM = "d";
	private static final String PAGE_NUMBER_QUERY_PARAM = "p";
	private static final String PAGE_SIZE_QUERY_PARAM = "s";
	private static final String UPDATE_ROW_COUNT_QUERY_PARAM = "c";
	private boolean slurp;
	private HashSet<String> slurpExcludes;
	public final static LinkedHashSet<NamedParameter> NAMED_QUERY_PARAMETERS = new LinkedHashSet<NamedParameter>();
	public final static LinkedHashSet<NamedParameter> SLURPED_NAMED_QUERY_PARAMETERS = new LinkedHashSet<NamedParameter>();
	static {
		NAMED_QUERY_PARAMETERS.add(new NamedParameter(ASCENDING_FIELD_QUERY_PARAM, String.class));
		NAMED_QUERY_PARAMETERS.add(new NamedParameter(DESCENDING_FIELD_QUERY_PARAM, String.class));
		NAMED_QUERY_PARAMETERS.add(new NamedParameter(PAGE_NUMBER_QUERY_PARAM, Integer.class));
		NAMED_QUERY_PARAMETERS.add(new NamedParameter(PAGE_SIZE_QUERY_PARAM, Integer.class));
		NAMED_QUERY_PARAMETERS.add(new NamedParameter(UPDATE_ROW_COUNT_QUERY_PARAM, Boolean.TYPE));
		SLURPED_NAMED_QUERY_PARAMETERS.addAll(NAMED_QUERY_PARAMETERS);
		SLURPED_NAMED_QUERY_PARAMETERS.add(new NamedParameter("*", String.class));
	}

	private static PSFUriPart updatePSF(PSFUriPart psf,
			MultivaluedMap<String, String> queryParameters) {
		return updatePSF(psf,
				ResourceUtils.popQueryParamValue(ASCENDING_FIELD_QUERY_PARAM, queryParameters),
				ResourceUtils.popQueryParamValue(DESCENDING_FIELD_QUERY_PARAM, queryParameters),
				ResourceUtils.popQueryParamValue(PAGE_SIZE_QUERY_PARAM, queryParameters),
				ResourceUtils.popQueryParamValue(PAGE_NUMBER_QUERY_PARAM, queryParameters),
				ResourceUtils.popQueryParamValue(UPDATE_ROW_COUNT_QUERY_PARAM, queryParameters),
				queryParameters);
	}

	private static PSFUriPart updatePSF(PSFUriPart psf, String ascendingField, String descendingField, String pageSize, String pageNumber, String updateRowCount,
			MultivaluedMap<String, String> filter) {
		if (psf != null) {
			if (!CommonUtil.isEmptyString(ascendingField)) {
				psf.setSortField(ascendingField);
				psf.setSortOrder(true);
			} else if (!CommonUtil.isEmptyString(descendingField)) {
				psf.setSortField(descendingField);
				psf.setSortOrder(false);
			}
			if (psf.slurp && filter != null && filter.size() > 0) {
				Map<String, String> filters;
				if (psf.getFilters() != null) {
					filters = psf.getFilters();
				} else {
					filters = new HashMap<String, String>(filter.size());
					psf.setFilters(filters);
				}
				Iterator<Entry<String, List<String>>> it = (new ArrayList<Entry<String, List<String>>>(filter.entrySet())).iterator();
				while (it.hasNext()) {
					Entry<String, List<String>> param = it.next();
					if (!psf.slurpExcludes.contains(param.getKey())) {
						filters.put(param.getKey(), ResourceUtils.popQueryParamValue(param.getKey(), filter));
					}
				}
			}
			if (!CommonUtil.isEmptyString(pageSize)) {
				try {
					psf.pageSize = Integer.parseInt(pageSize);
					psf.updateFirst();
				} catch (NumberFormatException e) {
				}
			}
			if (!CommonUtil.isEmptyString(pageNumber)) {
				try {
					psf.pageNumber = Integer.parseInt(pageNumber);
					psf.updateFirst();
				} catch (NumberFormatException e) {
				}
			}
			if (!CommonUtil.isEmptyString(updateRowCount)) {
				psf.setUpdateRowCount(Boolean.parseBoolean(updateRowCount));
			}
		}
		return psf;
	}

	private int pageNumber;

	public PSFUriPart() {
		super();
		pageNumber = DEFAULT_PAGE_NUMBER;
		pageSize = DEFAULT_PAGE_SIZE;
		slurp = true;
		slurpExcludes = new HashSet<String>();
		updateFirst();
	}

	private PSFUriPart(MultivaluedMap<String, String> queryParameters, String[] namedParameter) {
		this();
		if (namedParameter != null) {
			for (int i = 0; i < namedParameter.length; i++) {
				if (namedParameter[i] != null && namedParameter[i].length() > 0) {
					slurpExcludes.add(namedParameter[i]);
				}
			}
		}
		updatePSF(this, queryParameters);
	}

	public PSFUriPart(UriInfo uriInfo) {
		this(uriInfo, null);
	}

	public PSFUriPart(UriInfo uriInfo, String... namedParameter) {
		this(uriInfo.getQueryParameters(true), namedParameter);
	}

	@Override
	public Set<NamedParameter> getStaticQueryParameterNames() {
		return NAMED_QUERY_PARAMETERS;
	}

	@Override
	public boolean isSlurpQueryParameter() throws Exception {
		return slurp;
	}

	@Override
	public void setSlurpQueryParameter(boolean slurp) {
		this.slurp = slurp;
	}

	@Override
	public Object shiftParameters(MultivaluedMap<String, String> queryParameters) throws Exception {
		return updatePSF(this, queryParameters);
	}

	private void updateFirst() {
		if (pageNumber > 0 && pageSize > 0) {
			super.setFirst((pageNumber - 1) * pageSize);
		} else {
			super.setFirst(0);
		}
	}
}