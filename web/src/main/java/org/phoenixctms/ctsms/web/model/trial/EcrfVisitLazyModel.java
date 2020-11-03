package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFVisitVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.web.model.IDVO;
import org.phoenixctms.ctsms.web.model.LazyDataModel;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public class EcrfVisitLazyModel extends LazyDataModel<ECRFVisitVO> implements SelectableDataModel<ECRFVisitVO> {

	private Boolean active;
	private Long probandListEntryId;

	public Boolean getActive() {
		return active;
	}

	protected Collection<ECRFVisitVO> getLazyResult(PSFVO psf) {
		if (probandListEntryId != null) {
			try {
				return WebUtil.getServiceLocator().getTrialService().getEcrfVisitList(WebUtil.getAuthentication(), probandListEntryId, active, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		return new ArrayList<ECRFVisitVO>();
	}

	public Long getProbandListEntryId() {
		return probandListEntryId;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setProbandListEntryId(Long probandListEntryId) {
		this.probandListEntryId = probandListEntryId;
	}

	@Override
	public ECRFVisitVO getRowData(String key) {
		ECRFOutVO ecrf = null;
		VisitOutVO visit = null;
		if (key != null && key.length() > 0) {
			String[] ids = WebUtil.ID_SEPARATOR_REGEXP.split(key, 2);
			if (ids.length > 0) {
				ecrf = WebUtil.getEcrf(WebUtil.stringToLong(ids[0]));
			}
			if (ids.length > 1) {
				visit = WebUtil.getVisit(WebUtil.stringToLong(ids[1]));
			}
		}
		if (ecrf != null) {
			return new ECRFVisitVO(ecrf, visit);
		}
		return null;
	}

	@Override
	public String getRowKey(ECRFVisitVO element) {
		StringBuilder sb = new StringBuilder(Long.toString(element.getEcrf().getId()));
		sb.append(WebUtil.ID_SEPARATOR_STRING);
		if (element.getVisit() != null) {
			sb.append(Long.toString(element.getVisit().getId()));
		}
		return sb.toString();
	}

	@Override
	public List<ECRFVisitVO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		return load(createPSFVO(first, pageSize, sortField, sortOrder, filters, true));
	}

	@Override
	protected List<ECRFVisitVO> load(PSFVO psf) {
		Collection<ECRFVisitVO> lazyResult = getLazyResult(psf);
		if (psf != null && psf.getUpdateRowCount()) {
			updateRowCount(psf);
		}
		return (List<ECRFVisitVO>) lazyResult;
	}
	//avoid static method and overloads in ManagedBeans

	public IDVO ecrfToVo(ECRFOutVO ecrf) {
		if (ecrf != null) {
			return new IDVO(ecrf);
		} else {
			return null;
		}
	}

	public IDVO visitToVo(VisitOutVO visit) {
		if (visit != null) {
			return new IDVO(visit);
		} else {
			return null;
		}
	}
}