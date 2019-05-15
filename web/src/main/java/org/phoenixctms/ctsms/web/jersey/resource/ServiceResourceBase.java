package org.phoenixctms.ctsms.web.jersey.resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.phoenixctms.ctsms.util.AssociationPath;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.jersey.index.IndexBase;
import org.phoenixctms.ctsms.web.jersey.resource.shared.IndexResource;

import com.google.gson.JsonElement;
import com.sun.jersey.api.NotFoundException;

public abstract class ServiceResourceBase extends FileDavResourceBase {

	private final static Pattern GET_LIST_METHOD_NAME_REGEXP = Pattern.compile("^get(.+)List$");

	protected final static ArgsUriPart getArgsUriPart(Class<?> serviceInterface, String resource, AuthenticationVO auth, String rootEntityIdMethodParamName,
			MethodTransfilter getListMethodNameTransformer, Long id, PSFUriPart psf) {
		ArgsUriPart args = new ArgsUriPart(serviceInterface, resource, getListMethodNameTransformer);
		args.getOverrides().put("auth", auth);
		args.getOverrides().put(rootEntityIdMethodParamName, id);
		args.getOverrides().put("psf", psf);
		return args;
	}

	protected static MethodTransfilter getGetListMethodNameTransformer(final String rootEntityIdMethodParamName, final Class<?> rootOutVo) {
		return new MethodTransfilter() {

			@Override
			public boolean exclude(Method method) {
				Class<?>[] paramTypes = method.getParameterTypes();
				if (paramTypes == null) {
					return true;
				}
				if (!PSFVO.class.equals(paramTypes[paramTypes.length - 1])) {
					return true;
				}
				// Integer x = AssociationPath.getArgumentIndexMap(method).get(rootEntityIdMethodParamName);
				if (!(new Integer(1)).equals(AssociationPath.getArgumentIndexMap(method).get(rootEntityIdMethodParamName))) {
					return true;
				}
				if (Collection.class.isAssignableFrom(method.getReturnType())
						&& rootOutVo.isAssignableFrom((Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0])) {
					return true;
				}
				return false;
			};

			@Override
			public boolean include(Method method) {
				return GET_LIST_METHOD_NAME_REGEXP.matcher(method.getName()).find();
			}

			@Override
			public String transform(String methodName) {
				if (methodName != null && methodName.length() > 0) {
					Matcher matcher = GET_LIST_METHOD_NAME_REGEXP.matcher(methodName);
					if (matcher.find()) {
						return matcher.group(1).toLowerCase();
					}
				}
				return methodName;
			}
		};
	}

	protected static JsonElement getListIndexNode(String pathPrefix, Class<?> serviceInterface, MethodTransfilter getListMethodNameTransformer, ArgsUriPart args) {
		try {
			return IndexResource.getResourceMethodIndexNode(pathPrefix, AssociationPath.listMethods(serviceInterface, getListMethodNameTransformer),
					args, true);
		} catch (Exception e) {
			return IndexBase.EMPTY_INDEX_NODE;
		}
	}

	protected abstract MethodTransfilter getGetListMethodNameTransformer();

	protected abstract String getRootEntityIdMethodParamName();

	protected abstract Object getService();

	protected abstract Class<?> getServiceInterface();

	protected Page list(AuthenticationVO auth, Long id, String resource, UriInfo uriInfo) throws Throwable {
		if (AssociationPath.methodExists(getServiceInterface(), resource, getGetListMethodNameTransformer())) {
			MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters(true);
			PSFUriPart psf = new PSFUriPart();
			ArgsUriPart args = getArgsUriPart(getServiceInterface(), resource, auth, getRootEntityIdMethodParamName(), getGetListMethodNameTransformer(), id, psf);
			try {
				Object[] arguments = (Object[]) args.shiftParameters(queryParameters);
				psf.shiftParameters(queryParameters);
				return new Page((Collection) AssociationPath.invoke(resource,
						getService(),
						getServiceInterface(),
						getGetListMethodNameTransformer(), true, arguments), psf);
			} catch (InvocationTargetException e) {
				throw e.getCause();
			} catch (Exception e) {
				throw e;
			}
		}
		throw new NotFoundException();
	}
}
