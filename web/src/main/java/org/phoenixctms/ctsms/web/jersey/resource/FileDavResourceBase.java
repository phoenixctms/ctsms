package org.phoenixctms.ctsms.web.jersey.resource;

import static java.util.Collections.singletonList;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.OK;
import static net.java.dev.webdav.jaxrs.Headers.DAV;
import static net.java.dev.webdav.jaxrs.Headers.DEPTH_0;
import static net.java.dev.webdav.jaxrs.ResponseStatus.MULTI_STATUS;
import static net.java.dev.webdav.jaxrs.xml.elements.Depth.ZERO;
import static net.java.dev.webdav.jaxrs.xml.properties.ResourceType.COLLECTION;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.FilePathSplitter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FileStreamInVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.model.shared.FileBean;
import org.phoenixctms.ctsms.web.util.WebUtil;

import net.java.dev.webdav.jaxrs.xml.elements.ActiveLock;
import net.java.dev.webdav.jaxrs.xml.elements.HRef;
import net.java.dev.webdav.jaxrs.xml.elements.LockInfo;
import net.java.dev.webdav.jaxrs.xml.elements.LockRoot;
import net.java.dev.webdav.jaxrs.xml.elements.LockToken;
import net.java.dev.webdav.jaxrs.xml.elements.MultiStatus;
import net.java.dev.webdav.jaxrs.xml.elements.Prop;
import net.java.dev.webdav.jaxrs.xml.elements.PropStat;
import net.java.dev.webdav.jaxrs.xml.elements.PropertyUpdate;
import net.java.dev.webdav.jaxrs.xml.elements.RemoveOrSet;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import net.java.dev.webdav.jaxrs.xml.elements.Status;
import net.java.dev.webdav.jaxrs.xml.elements.TimeOut;
import net.java.dev.webdav.jaxrs.xml.properties.CreationDate;
import net.java.dev.webdav.jaxrs.xml.properties.DisplayName;
import net.java.dev.webdav.jaxrs.xml.properties.GetContentLength;
import net.java.dev.webdav.jaxrs.xml.properties.GetContentType;
import net.java.dev.webdav.jaxrs.xml.properties.GetETag;
import net.java.dev.webdav.jaxrs.xml.properties.GetLastModified;
import net.java.dev.webdav.jaxrs.xml.properties.LockDiscovery;

/**
 * Sole JAX-RS Resource of JPA Address Book Sample.
 *
 * @author Markus KARG (mkarg@java.net)
 */
public abstract class FileDavResourceBase {

	private final static String[] TEMP_FILE_EXTENSIONS = new String[] { "tmp", "asd", "wbk" };
	private final static String[] EDITABLE_FILE_EXTENSIONS = new String[] { "xls", "xlsx", "csv", "doc", "docx", "ppt", "pptx" };
	private final static String TMP_FILE_LOGICAL_PATH = CommonUtil.fixLogicalPathFolderName("tmp");
	private final static String DAV_URL_FILE = "%s/" + WebUtil.REST_API_PATH + "/%s/%s/files/dav/%s";

	private static final URI buildOpaqueLockToken() throws URISyntaxException {
		return new URI("opaquelocktoken", CommonUtil.generateUUID(), null);
	}

	private static final Prop buildProp(final FileOutVO f, final Providers providers) {
		return new Prop(new GetETag(String.valueOf(f.getVersion())),
				new DisplayName(f.getFileName()), // format("%s %s", c.getLastName(), c.getFirstName())),
				new GetLastModified(f.getModifiedTimestamp()),
				new GetContentLength(f.getSize()),
				new GetContentType(f.getContentType().getMimeType()));
	}

	private static Response buildResponse(final FileOutVO file, final URI path, final Providers providers) {
		return new Response(new HRef(path), null, null, null, new PropStat(buildProp(file, providers), new Status((StatusType) OK)));
	}

	private static final String buildUriHeader(final URI uri) {
		return String.format("<%s>", uri);
	}

	private static String getDavFileName(FileOutVO f) {
		FilePathSplitter filePath = new FilePathSplitter(f.getFileName());
		String extension = filePath.getExtension();
		StringBuilder sb;
		if (isTempFile(extension)) {
			sb = new StringBuilder(f.getFileName());
		} else {
			sb = new StringBuilder(Long.toString(f.getId()));
			if (extension != null && extension.length() > 0) {
				sb.append(".");
				sb.append(extension);
			}
		}
		return sb.toString();
	}

	public static String getDavUrlFile(String contextPath, FileOutVO f) {
		if (f.getInventory() != null) {
			return String.format(DAV_URL_FILE, contextPath, "inventory", Long.toString(f.getInventory().getId()), getDavFileName(f));
		} else if (f.getStaff() != null) {
			return String.format(DAV_URL_FILE, contextPath, "staff", Long.toString(f.getStaff().getId()), getDavFileName(f));
		} else if (f.getCourse() != null) {
			return String.format(DAV_URL_FILE, contextPath, "course", Long.toString(f.getCourse().getId()), getDavFileName(f));
		} else if (f.getTrial() != null) {
			return String.format(DAV_URL_FILE, contextPath, "trial", Long.toString(f.getTrial().getId()), getDavFileName(f));
		} else if (f.getProband() != null) {
			return String.format(DAV_URL_FILE, contextPath, "proband", Long.toString(f.getProband().getId()), getDavFileName(f));
		} else if (f.getMassMail() != null) {
			return String.format(DAV_URL_FILE, contextPath, "massmail", Long.toString(f.getMassMail().getId()), getDavFileName(f));
		}
		return "";
	}

	public static boolean isEditableFile(String extension) {
		return Arrays.asList(EDITABLE_FILE_EXTENSIONS).contains(extension);
	}

	public static boolean isTempFile(String extension) {
		return Arrays.asList(TEMP_FILE_EXTENSIONS).contains(extension);
	}

	protected javax.ws.rs.core.Response davDelete(Long id, String fileName) throws AuthenticationException, AuthorisationException, ServiceException {
		FileOutVO out = getFileFromDavFileName(id, fileName);
		WebUtil.getServiceLocator().getFileService().deleteFile(getAuth(), out.getId());
		return javax.ws.rs.core.Response.ok().build();
	}

	protected javax.ws.rs.core.Response davGet(Long id, String fileName) throws AuthenticationException, AuthorisationException, ServiceException {
		FileOutVO out = getFileFromDavFileName(id, fileName);
		FileStreamOutVO fs = WebUtil.getServiceLocator().getFileService().getFileStream(getAuth(), out.getId());
		ResponseBuilder response = javax.ws.rs.core.Response.ok(fs.getStream(), fs.getContentType().getMimeType());
		response.header(HttpHeaders.CONTENT_LENGTH, out.getSize());
		return response.build();
		// return (Contact) this.em().createNamedQuery("FindContactByMatchCode").setParameter(1, matchCode).getSingleResult();
	}

	protected javax.ws.rs.core.Response davHead(Long id, String fileName) throws AuthenticationException, AuthorisationException, ServiceException {
		FileOutVO out = getFileFromDavFileName(id, fileName);
		ResponseBuilder response = javax.ws.rs.core.Response.ok();
		response.header(HttpHeaders.CONTENT_LENGTH, out.getSize());
		response.type(out.getContentType().getMimeType());
		return response.build();
		// return (Contact) this.em().createNamedQuery("FindContactByMatchCode").setParameter(1, matchCode).getSingleResult();
	}

	protected Prop davLock(LockInfo lockInfo, UriInfo uriInfo, String depth, TimeOut timeout) throws URISyntaxException, IOException {
		/* TODO Lock the resource here. */
		return new Prop(new LockDiscovery(new ActiveLock(lockInfo.getLockScope(), lockInfo.getLockType(), ZERO, lockInfo.getOwner(), new TimeOut(3600),
				new LockToken(new HRef("opaquelocktoken:" + CommonUtil.generateUUID())), new LockRoot(new HRef(uriInfo.getAbsolutePath())))));
	}

	protected javax.ws.rs.core.Response davOptions() {
		return javax.ws.rs.core.Response.noContent().header(DAV, "1,2")
				.header("MS-Author-Via", "DAV")
				.header("Allow", "GET,HEAD,PROPFIND,OPTIONS,PUT,DELETE,LOCK,UNLOCK").build();
		// .header("Allow", "GET,OPTIONS,PROPFIND,PROPPATCH,PUT,LOCK,UNLOCK").build(); DELETE,MOVE,COPY,PROPPATCH,
	}

	protected javax.ws.rs.core.Response davPropFind(Long id, String fileName, UriInfo uriInfo, Providers providers) throws AuthenticationException, AuthorisationException,
	ServiceException {
		FileOutVO out = getFileFromDavFileName(id, fileName);
		return javax.ws.rs.core.Response.status(MULTI_STATUS)
				.entity(new MultiStatus(buildResponse(out, uriInfo.getAbsolutePathBuilder().build(), providers))).build();
	}

	protected javax.ws.rs.core.Response davPropFind(Long id, UriInfo uriInfo, String depth, Providers providers) throws AuthenticationException, AuthorisationException,
	ServiceException {
		final Response folder = new Response(new HRef(uriInfo.getRequestUri()), null, null, null, new PropStat(new Prop(new DisplayName("My Collection"),
				new CreationDate(new Date()), new GetLastModified(new Date()), COLLECTION), new Status((StatusType) OK)));
		if (depth.equals(DEPTH_0) || id == null) {
			return javax.ws.rs.core.Response.status(MULTI_STATUS).entity(new MultiStatus(folder)).build();
		}
		final Collection<Response> responses = new LinkedList<Response>(singletonList(folder));
		Iterator<FileOutVO> it = WebUtil.getServiceLocator().getFileService().getFiles(getAuth(), getFileModule(), id, null, false, null, null, null).iterator();
		if (it != null) {
			while (it.hasNext()) {
				FileOutVO out = it.next();
				// responses.add(buildResponse(f, uriInfo.getAbsolutePathBuilder().path(format("%s", c.getMatchCode())).build(), providers));
				FilePathSplitter filePath = new FilePathSplitter(out.getFileName());
				String extension = filePath.getExtension();
				if (isTempFile(extension) || isEditableFile(extension)) {
					responses.add(buildResponse(out, uriInfo.getAbsolutePathBuilder().path(getDavFileName(out)).build(), providers));
				}
			}
		}
		return javax.ws.rs.core.Response.status(MULTI_STATUS).entity(new MultiStatus(responses.toArray(new Response[0]))).build();
	}

	protected javax.ws.rs.core.Response davPropPatch(InputStream body, UriInfo uriInfo, Providers providers, HttpHeaders httpHeaders) throws IOException {
		final PropertyUpdate propertyUpdate = providers.getMessageBodyReader(PropertyUpdate.class, PropertyUpdate.class, new Annotation[0],
				APPLICATION_XML_TYPE).readFrom(PropertyUpdate.class, PropertyUpdate.class, new Annotation[0], APPLICATION_XML_TYPE,
						httpHeaders.getRequestHeaders(), body);
		// System.out.println("PATCH PROPERTIES: " + propertyUpdate.list());
		/* TODO Patch properties in database. */
		final Collection<PropStat> propstats = new LinkedList<PropStat>();
		for (final RemoveOrSet removeOrSet : propertyUpdate.list()) {
			propstats.add(new PropStat(removeOrSet.getProp(), new Status((StatusType) FORBIDDEN)));
		}
		return javax.ws.rs.core.Response.status(MULTI_STATUS)
				.entity(new MultiStatus(new Response(new HRef(uriInfo.getRequestUri()), null, null, null, propstats))).build();
	}

	// @DELETE
	// @Path("{filename}.adr")
	// public final void delete(@PathParam("filename") final String matchCode) {
	// final EntityManager em = this.em();
	// final EntityTransaction t = em.getTransaction();
	// t.begin();
	// em.createNamedQuery("DeleteContactByMatchCode").setParameter(1, matchCode).executeUpdate();
	// t.commit();
	// }
	//
	// @MOVE
	// @Path("{filename}.adr")
	// public final void move(@PathParam("filename") final String sourceMatchCode, @HeaderParam(DESTINATION) final URI destination,
	// @HeaderParam(OVERWRITE) final String overwrite) {
	// final EntityManager em = this.em();
	// final EntityTransaction t = em.getTransaction();
	// t.begin();
	//
	// final Contact source;
	// try {
	// source = (Contact) em.createNamedQuery("FindContactByMatchCode").setParameter(1, sourceMatchCode).getSingleResult();
	// } catch (final NoResultException e) {
	// t.rollback();
	// throw new WebApplicationException(NOT_FOUND);
	// }
	//
	// final String[] destinationPathSegments = destination.getPath().split("/");
	// final String lastDestinationPathSegment = destinationPathSegments[destinationPathSegments.length - 1];
	//
	// if (!lastDestinationPathSegment.endsWith(".adr")) {
	// t.rollback();
	// throw new WebApplicationException(FORBIDDEN);
	// }
	//
	// final String destinationMatchCode = lastDestinationPathSegment.split("\\.")[0];
	//
	// Contact target;
	// try {
	// target = (Contact) em.createNamedQuery("FindContactByMatchCode").setParameter(1, destinationMatchCode).getSingleResult();
	// } catch (final NoResultException e) {
	// target = null;
	// }
	//
	// if (target != null) {
	// if (overwrite.equals(OVERWRITE_FALSE)) {
	// t.rollback();
	// throw new WebApplicationException(PRECONDITION_FAILED);
	// }
	//
	// em.remove(target);
	// }
	//
	// em.remove(source);
	//
	// em.persist(new Contact(destinationMatchCode, source));
	//
	// t.commit();
	// }
	//
	// @COPY
	// @Path("{filename}.adr")
	// public final void copy(@PathParam("filename") final String sourceMatchCode, @HeaderParam(DESTINATION) final URI destination,
	// @HeaderParam(OVERWRITE) final String overwrite) {
	//
	// final EntityManager em = this.em();
	// final EntityTransaction t = em.getTransaction();
	// t.begin();
	//
	// final Contact source;
	// try {
	// source = (Contact) em.createNamedQuery("FindContactByMatchCode").setParameter(1, sourceMatchCode).getSingleResult();
	// } catch (final NoResultException e) {
	// t.rollback();
	// throw new WebApplicationException(NOT_FOUND);
	// }
	//
	// final String[] destinationPathSegments = destination.getPath().split("/");
	// final String lastDestinationPathSegment = destinationPathSegments[destinationPathSegments.length - 1];
	//
	// if (!lastDestinationPathSegment.endsWith(".adr")) {
	// t.rollback();
	// throw new WebApplicationException(FORBIDDEN);
	// }
	//
	// final String destinationMatchCode = lastDestinationPathSegment.split("\\.")[0];
	//
	// Contact target;
	// try {
	// target = (Contact) em.createNamedQuery("FindContactByMatchCode").setParameter(1, destinationMatchCode).getSingleResult();
	// } catch (final NoResultException e) {
	// target = null;
	// }
	//
	// if (target != null) {
	// if (overwrite.equals(OVERWRITE_FALSE)) {
	// t.rollback();
	// throw new WebApplicationException(PRECONDITION_FAILED);
	// }
	//
	// em.remove(target);
	// }
	//
	// em.persist(new Contact(destinationMatchCode, source));
	//
	// t.commit();
	// }
	protected javax.ws.rs.core.Response davPut(InputStream input, Long id, String fileName,
			long contentLength, HttpHeaders httpHeaders)
					throws AuthenticationException, AuthorisationException, ServiceException {
		// @Context final UriInfo uriInfo,
		// @Context final Providers providers,
		/* Workaround for Jersey issue #154 (see https://jersey.dev.java.net/issues/show_bug.cgi?id=154): Jersey will throw an exception and abstain from calling a method if the
		 * method expects a JAXB element body while the actual Content-Length is zero. */
		// final Contact entity = contentLength == 0 ? new Contact(matchCode, null, null, null) : providers.getMessageBodyReader(Contact.class, Contact.class,
		// new Annotation[0], new MediaType("application", "address+xml")).readFrom(Contact.class, Contact.class, new Annotation[0],
		// new MediaType("application", "address+xml"), httpHeaders.getRequestHeaders(), entityStream);
		/* End of #154 workaround */
		FileOutVO out = getFileFromDavFileName(id, fileName);
		FileInVO in = new FileInVO();
		FileStreamInVO stream = new FileStreamInVO();
		stream.setStream(input);
		stream.setMimeType(httpHeaders.getMediaType().toString());
		stream.setSize(contentLength);
		if (out != null) {
			FileBean.copyFileOutToIn(in, out);
			stream.setFileName(out.getFileName());
			WebUtil.getServiceLocator().getFileService().updateFile(getAuth(), in, stream);
		} else {
			FileBean.initFileDefaultValues(in, id, getFileModule());
			in.setTitle(fileName);
			// in.setComment(value);
			in.setLogicalPath(TMP_FILE_LOGICAL_PATH);
			stream.setFileName(fileName);
			WebUtil.getServiceLocator().getFileService().addFile(getAuth(), in, stream);
		}
		return javax.ws.rs.core.Response.noContent().build();
	}

	protected javax.ws.rs.core.Response davUnlock() {
		/* TODO Unlock the resource here. */
		return javax.ws.rs.core.Response.ok().build();
	}

	protected abstract AuthenticationVO getAuth();

	private FileOutVO getFileFromDavFileName(Long id, String fileName) throws AuthenticationException, AuthorisationException, ServiceException {
		FilePathSplitter filePath = new FilePathSplitter(fileName);
		Long fileId;
		try {
			fileId = Long.parseLong(filePath.getName());
		} catch (NumberFormatException e) {
			fileId = null;
		}
		// String extension = filePath.getExtension();
		if (fileId != null && !isTempFile(filePath.getExtension())) {
			return WebUtil.getServiceLocator().getFileService().getFile(getAuth(), fileId);
		} else {
			PSFVO sf = new PSFVO();
			Map<String, String> fileFilters = new HashMap<String, String>();
			fileFilters.put(CommonUtil.getUseFileEncryption(getFileModule()) ? WebUtil.FILE_NAME_HASH_PSF_PROPERTY_NAME : WebUtil.FILE_NAME_PSF_PROPERTY_NAME, fileName);
			sf.setFilters(fileFilters);
			sf.setPageSize(1);
			Collection<FileOutVO> files = WebUtil.getServiceLocator().getFileService().getFiles(getAuth(), getFileModule(), id, null, false, null, null, sf);
			if (files != null && files.iterator().hasNext()) {
				return files.iterator().next();
			}
			return null;
		}
	}

	protected abstract FileModule getFileModule();
}
