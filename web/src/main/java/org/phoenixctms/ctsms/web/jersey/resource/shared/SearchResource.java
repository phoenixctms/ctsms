package org.phoenixctms.ctsms.web.jersey.resource.shared;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.IntermediateSetSummaryVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.jersey.index.ResourceIndex;
import org.phoenixctms.ctsms.web.jersey.resource.PSFUriPart;
import org.phoenixctms.ctsms.web.jersey.resource.Page;
import org.phoenixctms.ctsms.web.jersey.resource.ResourceUtils;
import org.phoenixctms.ctsms.web.jersey.wrapper.CriterionInVOWrapper;
import org.phoenixctms.ctsms.web.jersey.wrapper.NoShortcutSerializationWrapper;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import io.swagger.annotations.Api;

@Api(value = "shared")
@Path("/search")
public final class SearchResource {

	@Context
	AuthenticationVO auth;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public CriteriaOutVO addCriteria(CriterionInVOWrapper criteria) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getSearchService().addCriteria(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()));
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public CriteriaOutVO deleteCriteria(@PathParam("id") Long id, @QueryParam("reason") String reason) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getSearchService()
				.deleteCriteria(auth, id, Settings.getBoolean(SettingCodes.CRITERIA_DEFERRED_DELETE, Bundle.SETTINGS, DefaultSettings.CRITERIA_DEFERRED_DELETE), false, reason);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("course")
	public Page<CriteriaOutVO> getCourseCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.COURSE_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}")
	public CriteriaOutVO getCriteria(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getSearchService().getCriteria(auth, id);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/criterions")
	public Collection<CriterionOutVO> getCriterions(@PathParam("id") Long id) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getSearchService().getCriteria(auth, id).getCriterions();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inputfield")
	public Page<CriteriaOutVO> getInputFieldCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.INPUT_FIELD_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{id}/intermediatesets")
	public IntermediateSetSummaryVO getIntermediateSetsByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo) throws AuthenticationException, AuthorisationException,
			ServiceException {
		return WebUtil.getServiceLocator().getSearchService().getIntermediateSetsByCriteria(auth, id, new PSFUriPart(uriInfo));
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inventory")
	public Page<CriteriaOutVO> getInventoryCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.INVENTORY_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("massmail")
	public Page<CriteriaOutVO> getMassMailCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.MASS_MAIL_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("proband")
	public Page<CriteriaOutVO> getProbandCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.PROBAND_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("staff")
	public Page<CriteriaOutVO> getStaffCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.STAFF_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("trial")
	public Page<CriteriaOutVO> getTrialCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.TRIAL_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("user")
	public Page<CriteriaOutVO> getUserCriteriaList(@Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CriteriaOutVO>(WebUtil.getServiceLocator().getSearchService().getCriteriaList(auth, DBModule.USER_DB, psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceIndex index(@Context Application application,
			@Context HttpServletRequest request) throws Exception {
		return new ResourceIndex(IndexResource.getResourceIndexNode(SearchResource.class, request));
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("course/search")
	public Page<CourseOutVO> searchCourse(CriterionInVOWrapper criteria, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CourseOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchCourse(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()), ResourceUtils.LIST_GRAPH_MAX_COURSE_INSTANCES,
						psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("course/{id}/search")
	public Page<CourseOutVO> searchCourseByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<CourseOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchCourseByCriteria(auth, id, ResourceUtils.LIST_GRAPH_MAX_COURSE_INSTANCES, psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inputfield/search")
	public Page<InputFieldOutVO> searchInputField(CriterionInVOWrapper criteria, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<InputFieldOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchInputField(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()), psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inputfield/{id}/search")
	public Page<InputFieldOutVO> searchInputFieldByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<InputFieldOutVO>(WebUtil.getServiceLocator().getSearchService().searchInputFieldByCriteria(auth, id, psf = new PSFUriPart(uriInfo)), psf);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inventory/search")
	public Page<InventoryOutVO> searchInventory(CriterionInVOWrapper criteria, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<InventoryOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchInventory(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()),
						ResourceUtils.LIST_GRAPH_MAX_INVENTORY_INSTANCES, psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("inventory/{id}/search")
	public Page<InventoryOutVO> searchInventoryByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<InventoryOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchInventoryByCriteria(auth, id, ResourceUtils.LIST_GRAPH_MAX_INVENTORY_INSTANCES, psf = new PSFUriPart(uriInfo)), psf);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("proband/search")
	public Page<ProbandOutVO> searchProband(CriterionInVOWrapper criteria, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<ProbandOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchProband(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()),
						ResourceUtils.LIST_GRAPH_MAX_PROBAND_INSTANCES, psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("proband/{id}/search")
	public Page<ProbandOutVO> searchProbandByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<ProbandOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchProbandByCriteria(auth, id, ResourceUtils.LIST_GRAPH_MAX_PROBAND_INSTANCES, psf = new PSFUriPart(uriInfo)), psf);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("staff/search")
	public Page<StaffOutVO> searchStaff(CriterionInVOWrapper criteria, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<StaffOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchStaff(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()), ResourceUtils.LIST_GRAPH_MAX_STAFF_INSTANCES,
						psf = new PSFUriPart(uriInfo)),
				psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("staff/{id}/search")
	public Page<StaffOutVO> searchStaffByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<StaffOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchStaffByCriteria(auth, id, ResourceUtils.LIST_GRAPH_MAX_STAFF_INSTANCES, psf = new PSFUriPart(uriInfo)), psf);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("trial/search")
	public Page<TrialOutVO> searchTrial(CriterionInVOWrapper criteria, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<TrialOutVO>(WebUtil.getServiceLocator().getSearchService()
				.searchTrial(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()), psf = new PSFUriPart(uriInfo)), psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("trial/{id}/search")
	public Page<TrialOutVO> searchTrialByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf;
		return new Page<TrialOutVO>(WebUtil.getServiceLocator().getSearchService().searchTrialByCriteria(auth, id, psf = new PSFUriPart(uriInfo)), psf);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("user/search")
	public Page<NoShortcutSerializationWrapper<UserOutVO>> searchUser(CriterionInVOWrapper criteria, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf = new PSFUriPart(uriInfo);
		Collection result = WebUtil.getServiceLocator().getSearchService()
				.searchUser(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()), ResourceUtils.LIST_GRAPH_MAX_USER_INSTANCES, psf);
		NoShortcutSerializationWrapper.transformVoCollection(result);
		return new Page<NoShortcutSerializationWrapper<UserOutVO>>(result, psf);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("user/{id}/search")
	public Page<NoShortcutSerializationWrapper<UserOutVO>> searchUserByCriteria(@PathParam("id") Long id, @Context UriInfo uriInfo)
			throws AuthenticationException, AuthorisationException, ServiceException {
		PSFUriPart psf = new PSFUriPart(uriInfo);
		Collection result = WebUtil.getServiceLocator().getSearchService().searchUserByCriteria(auth, id, ResourceUtils.LIST_GRAPH_MAX_USER_INSTANCES, psf);
		NoShortcutSerializationWrapper.transformVoCollection(result);
		return new Page<NoShortcutSerializationWrapper<UserOutVO>>(result, psf);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public CriteriaOutVO updateCriteria(CriterionInVOWrapper criteria) throws AuthenticationException, AuthorisationException, ServiceException {
		return WebUtil.getServiceLocator().getSearchService().updateCriteria(auth, criteria, new LinkedHashSet<CriterionInVO>(criteria.getCriterions()));
	}
}
