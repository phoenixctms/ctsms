/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.jersey.spi.container.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URI;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.container.MappableContainerException;
import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.jersey.api.core.ClasspathResourceConfig;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.core.TraceInformation;
import com.sun.jersey.api.core.servlet.WebAppResourceConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.core.header.MediaTypes;
import com.sun.jersey.core.reflection.ReflectionHelper;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.server.impl.InitialContextHelper;
import com.sun.jersey.server.impl.ThreadLocalInvoker;
import com.sun.jersey.server.impl.application.DeferredResourceConfig;
import com.sun.jersey.server.impl.cdi.CDIComponentProviderFactoryInitializer;
import com.sun.jersey.server.impl.container.servlet.JSPTemplateProcessor;
import com.sun.jersey.server.impl.ejb.EJBComponentProviderFactoryInitilizer;
import com.sun.jersey.server.impl.managedbeans.ManagedBeanComponentProviderFactoryInitilizer;
import com.sun.jersey.server.impl.model.method.dispatch.FormDispatchProvider;
import com.sun.jersey.server.impl.monitoring.GlassFishMonitoringInitializer;
import com.sun.jersey.server.probes.UriRuleProbeProvider;
import com.sun.jersey.spi.container.ContainerListener;
import com.sun.jersey.spi.container.ContainerNotifier;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseWriter;
import com.sun.jersey.spi.container.ReloadListener;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.WebApplicationFactory;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

/**
 * An abstract Web component that may be extended a Servlet and/or
 * Filter implementation, or encapsulated by a Servlet or Filter implementation.
 *
 * @author Paul Sandoz (paul.sandoz at oracle.com)
 */
public class WebComponent implements ContainerListener {

	/**
	 * The servlet initialization property whose value is a fully qualified
	 * class name of a class that implements {@link ResourceConfig} or
	 * {@link Application}.
	 */
	public static final String APPLICATION_CONFIG_CLASS = "javax.ws.rs.Application";
	/**
	 * The servlet initialization property whose value is a fully qualified
	 * class name of a class that implements {@link ResourceConfig} or
	 * {@link Application}.
	 */
	public static final String RESOURCE_CONFIG_CLASS = "com.sun.jersey.config.property.resourceConfigClass";
	/**
	 * The base path in the Web Pages where JSP templates, associated with
	 * viewables of resource classes, are located.
	 * <p/>
	 * If this property is not set then the base path will be the root path
	 * of the Web Pages.
	 */
	public static final String JSP_TEMPLATES_BASE_PATH = "com.sun.jersey.config.property.JSPTemplatesBasePath";
	private static final Logger LOGGER = Logger.getLogger(WebComponent.class.getName());
	private final ThreadLocalInvoker<HttpServletRequest> requestInvoker = new ThreadLocalInvoker<HttpServletRequest>();
	private final ThreadLocalInvoker<HttpServletResponse> responseInvoker = new ThreadLocalInvoker<HttpServletResponse>();
	private WebConfig config;
	private ResourceConfig resourceConfig;
	private WebApplication application;

	public WebComponent() {
	}

	public WebComponent(Application app) {
		if (app == null)
			throw new IllegalArgumentException();
		if (app instanceof ResourceConfig) {
			resourceConfig = (ResourceConfig) app;
		} else {
			resourceConfig = new ApplicationAdapter(app);
		}
	}

	/**
	 * Get the Web configuration.
	 *
	 * @return the Web configuration.
	 */
	public WebConfig getWebConfig() {
		return config;
	}

	/**
	 * Get the resource configuration.
	 *
	 * @return the resource configuration.
	 */
	public ResourceConfig getResourceConfig() {
		return resourceConfig;
	}

	/**
	 * Initiate the Web component.
	 *
	 * @param webConfig the Web configuration.
	 * @throws javax.servlet.ServletException in case of any initialization error
	 */
	public void init(WebConfig webConfig) throws ServletException {
		config = webConfig;
		if (resourceConfig == null)
			resourceConfig = createResourceConfig(config);
		load();
		Object o = resourceConfig.getProperties().get(
				ResourceConfig.PROPERTY_CONTAINER_NOTIFIER);
		if (o instanceof List) {
			List list = (List) o;
			for (Object elem : list) {
				if (elem instanceof ContainerNotifier) {
					ContainerNotifier crf = (ContainerNotifier) elem;
					crf.addListener(this);
				}
			}
		} else if (o instanceof ContainerNotifier) {
			ContainerNotifier crf = (ContainerNotifier) o;
			crf.addListener(this);
		}
	}

	/**
	 * Destroy this Web component.
	 * <p/>
	 * This will destroy the Web application created by this this Web component.
	 */
	public void destroy() {
		if (application != null)
			application.destroy();
	}

	private final static class Writer extends OutputStream implements ContainerResponseWriter {

		final HttpServletResponse response;
		ContainerResponse cResponse;
		long contentLength;
		OutputStream out;
		boolean statusAndHeadersWritten = false;

		Writer(HttpServletResponse response) {
			this.response = response;
		}

		public OutputStream writeStatusAndHeaders(long contentLength,
				ContainerResponse cResponse) throws IOException {
			this.contentLength = contentLength;
			this.cResponse = cResponse;
			this.statusAndHeadersWritten = false;
			return this;
		}

		public void finish() throws IOException {
			if (statusAndHeadersWritten)
				return;
			// Note that the writing of headers MUST be performed before
			// the invocation of sendError as on some Servlet implementations
			// modification of the response headers will have no effect
			// after the invocation of sendError.
			writeHeaders();
			writeStatus();
		}

		private void writeStatus() {
			Response.StatusType statusType = cResponse.getStatusType();
			final String reasonPhrase = statusType.getReasonPhrase();
			if (reasonPhrase != null) {
				try {
					//javax.servlet.http.HttpServletResponse:
					response.setStatus(statusType.getStatusCode(), reasonPhrase);
				} catch (NoSuchMethodError e1) {
					try {
						//jakarta.servlet.http.HttpServletResponse:
						Method sendError = response.getClass().getMethod("sendError", int.class, String.class);
						sendError.invoke(response, statusType.getStatusCode(), reasonPhrase);
					} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
						response.setStatus(statusType.getStatusCode());
					}
				}
			} else {
				response.setStatus(statusType.getStatusCode());
			}
		}

		public void write(int b) throws IOException {
			initiate();
			out.write(b);
		}

		@Override
		public void write(byte b[]) throws IOException {
			if (b.length > 0) {
				initiate();
				out.write(b);
			}
		}

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			if (len > 0) {
				initiate();
				out.write(b, off, len);
			}
		}

		@Override
		public void flush() throws IOException {
			writeStatusAndHeaders();
			if (out != null)
				out.flush();
		}

		@Override
		public void close() throws IOException {
			initiate();
			out.close();
		}

		void initiate() throws IOException {
			if (out == null) {
				writeStatusAndHeaders();
				out = response.getOutputStream();
			}
		}

		void writeStatusAndHeaders() {
			if (statusAndHeadersWritten)
				return;
			writeHeaders();
			writeStatus();
			statusAndHeadersWritten = true;
		}

		void writeHeaders() {
			if (contentLength != -1 && contentLength < Integer.MAX_VALUE)
				response.setContentLength((int) contentLength);
			MultivaluedMap<String, Object> headers = cResponse.getHttpHeaders();
			for (Map.Entry<String, List<Object>> e : headers.entrySet()) {
				for (Object v : e.getValue()) {
					response.addHeader(e.getKey(), ContainerResponse.getHeaderValue(v));
				}
			}
		}
	}

	/**
	 * Dispatch client requests to a resource class.
	 *
	 * @param baseUri    the base URI of the request.
	 * @param requestUri the URI of the request.
	 * @param request    the {@link HttpServletRequest} object that
	 *                   contains the request the client made to
	 *                   the Web component.
	 * @param response   the {@link HttpServletResponse} object that
	 *                   contains the response the Web component returns
	 *                   to the client.
	 * @return the status code of the response.
	 * @throws IOException      if an input or output error occurs
	 *                          while the Web component is handling the
	 *                          HTTP request.
	 * @throws ServletException if the HTTP request cannot
	 *                          be handled.
	 */
	public int service(URI baseUri, URI requestUri,
			final HttpServletRequest request,
			final HttpServletResponse response)
			throws ServletException, IOException {
		// Copy the application field to local instance to ensure that the
		// currently loaded web application is used to process
		// request
		final WebApplication _application = application;
		final ContainerRequest cRequest = createRequest(
				_application,
				request,
				baseUri,
				requestUri);
		cRequest.setSecurityContext(new SecurityContext() {

			public Principal getUserPrincipal() {
				return request.getUserPrincipal();
			}

			public boolean isUserInRole(String role) {
				return request.isUserInRole(role);
			}

			public boolean isSecure() {
				return request.isSecure();
			}

			public String getAuthenticationScheme() {
				return request.getAuthType();
			}
		});
		try {
			// Check if any servlet filters have consumed a request entity
			// of the media type application/x-www-form-urlencoded
			// This can happen if a filter calls request.getParameter(...)
			filterFormParameters(request, cRequest);
			UriRuleProbeProvider.requestStart(requestUri);
			requestInvoker.set(request);
			responseInvoker.set(response);
			final Writer w = new Writer(response);
			_application.handleRequest(cRequest, w);
			return w.cResponse.getStatus();
		} catch (WebApplicationException ex) {
			final Response exResponse = ex.getResponse();
			final String entity = exResponse.getEntity() != null ? exResponse.getEntity().toString() : null;
			response.sendError(exResponse.getStatus(), entity);
			return exResponse.getStatus();
		} catch (MappableContainerException ex) {
			traceOnException(cRequest, response);
			throw new ServletException(ex.getCause());
		} catch (ContainerException ex) {
			traceOnException(cRequest, response);
			throw new ServletException(ex);
		} catch (RuntimeException ex) {
			traceOnException(cRequest, response);
			throw ex;
		} finally {
			UriRuleProbeProvider.requestEnd();
			requestInvoker.set(null);
			responseInvoker.set(null);
		}
	}

	/**
	 * Extension point for creating your custom container request.
	 *
	 * @param app        the web app
	 * @param request    the current servlet api request
	 * @param baseUri    the base uri
	 * @param requestUri the request uri
	 * @return the request container
	 * @throws IOException if any error occurs when getting the input stream
	 */
	protected ContainerRequest createRequest(WebApplication app,
			HttpServletRequest request, URI baseUri, URI requestUri) throws IOException {
		return new ContainerRequest(
				app,
				request.getMethod(),
				baseUri,
				requestUri,
				getHeaders(request),
				request.getInputStream());
	}

	private void traceOnException(final ContainerRequest cRequest, final HttpServletResponse response) {
		if (cRequest.isTracingEnabled()) {
			final TraceInformation ti = (TraceInformation) cRequest.getProperties().get(TraceInformation.class.getName());
			ti.addTraceHeaders(new TraceInformation.TraceHeaderListener() {

				public void onHeader(String name, String value) {
					response.addHeader(name, value);
				}
			});
		}
	}

	/**
	 * Create a new instance of a {@link WebApplication}.
	 *
	 * @return the {@link WebApplication} instance.
	 */
	protected WebApplication create() {
		return WebApplicationFactory.createWebApplication();
	}

	/**
	 * A helper class for creating an injectable provider that supports
	 * {@link Context} with a type and constant value.
	 *
	 * @param <T> the type of the constant value.
	 */
	protected static class ContextInjectableProvider<T> extends
			SingletonTypeInjectableProvider<Context, T> {

		/**
		 * Create a new instance.
		 *
		 * @param type     the type of the constant value.
		 * @param instance the constant value.
		 */
		protected ContextInjectableProvider(Type type, T instance) {
			super(type, instance);
		}
	}

	/**
	 * Configure the {@link ResourceConfig}.
	 * <p/>
	 * The {@link ResourceConfig} is configured such that the following classes
	 * may be injected onto the field of a root resource class or a parameter
	 * of a method of root resource class that is annotated with
	 * {@link javax.ws.rs.core.Context}: {@link HttpServletRequest}, {@link HttpServletResponse}
	 * , {@link ServletContext} and {@link WebConfig}.
	 * <p/>
	 * Any root resource class in registered in the resource configuration
	 * that is an interface is processed as follows.
	 * If the class is an interface and there exists a JNDI named object
	 * with the fully qualified class name as the JNDI name then that named
	 * object is added as a singleton root resource and the class is removed
	 * from the set of root resource classes.
	 * <p/>
	 * An inheriting class may override this method to configure the
	 * {@link ResourceConfig} to provide alternative or additional instances
	 * that are resource or provider classes or instances, and may modify the
	 * features and properties of the {@link ResourceConfig}. For an inheriting
	 * class to extend configuration behaviour the overriding method MUST call
	 * <code>super.configure(servletConfig, rc, wa)</code> as the first statement
	 * of that method.
	 * <p/>
	 * This method will be called only once at initiation. Subsequent
	 * reloads of the Web application will not result in subsequence calls to
	 * this method.
	 *
	 * @param wc the Web configuration
	 * @param rc the Resource configuration
	 * @param wa the Web application
	 */
	protected void configure(WebConfig wc, ResourceConfig rc, WebApplication wa) {
		configureJndiResources(rc);
		rc.getSingletons().add(new ContextInjectableProvider<HttpServletRequest>(
				HttpServletRequest.class,
				(HttpServletRequest) Proxy.newProxyInstance(
						this.getClass().getClassLoader(),
						new Class[] { HttpServletRequest.class },
						requestInvoker)));
		rc.getSingletons().add(new ContextInjectableProvider<HttpServletResponse>(
				HttpServletResponse.class,
				(HttpServletResponse) Proxy.newProxyInstance(
						this.getClass().getClassLoader(),
						new Class[] { HttpServletResponse.class },
						responseInvoker)));
		GenericEntity<ThreadLocal<HttpServletRequest>> requestThreadLocal = new GenericEntity<ThreadLocal<HttpServletRequest>>(requestInvoker.getImmutableThreadLocal()) {
		};
		rc.getSingletons().add(new ContextInjectableProvider<ThreadLocal<HttpServletRequest>>(
				requestThreadLocal.getType(), requestThreadLocal.getEntity()));
		GenericEntity<ThreadLocal<HttpServletResponse>> responseThreadLocal = new GenericEntity<ThreadLocal<HttpServletResponse>>(responseInvoker.getImmutableThreadLocal()) {
		};
		rc.getSingletons().add(new ContextInjectableProvider<ThreadLocal<HttpServletResponse>>(
				responseThreadLocal.getType(), responseThreadLocal.getEntity()));
		rc.getSingletons().add(new ContextInjectableProvider<ServletContext>(
				ServletContext.class,
				wc.getServletContext()));
		rc.getSingletons().add(new ContextInjectableProvider<WebConfig>(
				WebConfig.class,
				wc));
		rc.getClasses().add(JSPTemplateProcessor.class);
		// TODO
		// If CDI is enabled should EJB injection be disabled?
		EJBComponentProviderFactoryInitilizer.initialize(rc);
		CDIComponentProviderFactoryInitializer.initialize(wc, rc, wa);
		// TODO
		// If CDI is enabled then no need to initialize managed beans
		ManagedBeanComponentProviderFactoryInitilizer.initialize(rc);
		GlassFishMonitoringInitializer.initialize();
	}

	/**
	 * Initiate the {@link WebApplication}.
	 * <p/>
	 * This method will be called once at initiation and for
	 * each reload of the Web application.
	 * <p/>
	 * An inheriting class may override this method to initiate the
	 * Web application with different parameters.
	 *
	 * @param rc the Resource configuration
	 * @param wa the Web application
	 */
	protected void initiate(ResourceConfig rc, WebApplication wa) {
		wa.initiate(rc);
	}

	/**
	 * Load the Web application. This will create, configure and initiate
	 * the web application.
	 */
	public void load() {
		WebApplication _application = create();
		configure(config, resourceConfig, _application);
		initiate(resourceConfig, _application);
		application = _application;
	}

	/**
	 * Get the default resource configuration if one is not declared in the
	 * web.xml.
	 * <p/>
	 * This implementation returns an instance of {@link WebAppResourceConfig}
	 * that scans in files and directories as declared by the
	 * {@link ClasspathResourceConfig#PROPERTY_CLASSPATH} if present, otherwise
	 * in the "WEB-INF/lib" and "WEB-INF/classes" directories.
	 * <p/>
	 * An inheriting class may override this method to supply a different
	 * default resource configuration implementation.
	 *
	 * @param props the properties to pass to the resource configuration.
	 * @param wc    the web configuration.
	 * @return the default resource configuration.
	 * @throws javax.servlet.ServletException in case of any issues with providing \
	 *                                        the default resource configuration
	 */
	protected ResourceConfig getDefaultResourceConfig(Map<String, Object> props,
			WebConfig wc) throws ServletException {
		return getWebAppResourceConfig(props, wc);
	}
	// ContainerListener

	/**
	 * Reload the Web application. This will create and initiate the web
	 * application using the same {@link ResourceConfig} implementation
	 * that was used to load the Web application.
	 * <p/>
	 * This method may be called at runtime, more than once, to reload the
	 * Web application. For example, if a {@link ResourceConfig} implementation
	 * is capable of detecting changes to resource classes (addition or removal)
	 * or providers then this method may be invoked to reload the web
	 * application for such changes to take effect.
	 * <p/>
	 * If this method is called when there are pending requests then such
	 * requests will be processed using the previously loaded web application.
	 */
	public void onReload() {
		WebApplication oldApplication = application;
		WebApplication newApplication = create();
		initiate(resourceConfig, newApplication);
		application = newApplication;
		if (resourceConfig instanceof ReloadListener)
			((ReloadListener) resourceConfig).onReload();
		oldApplication.destroy();
	}
	//

	/* package */ ResourceConfig getWebAppResourceConfig(Map<String, Object> props,
			WebConfig webConfig) throws ServletException {
		// Default to using Web app resource config
		return new WebAppResourceConfig(props, webConfig.getServletContext());
	}

	private ResourceConfig createResourceConfig(WebConfig webConfig)
			throws ServletException {
		final Map<String, Object> props = getInitParams(webConfig);
		final ResourceConfig rc = createResourceConfig(webConfig, props);
		rc.setPropertiesAndFeatures(props);
		return rc;
	}

	private ResourceConfig createResourceConfig(WebConfig webConfig, Map<String, Object> props)
			throws ServletException {
		// Check if the resource config class property is present
		String resourceConfigClassName = webConfig.getInitParameter(RESOURCE_CONFIG_CLASS);
		// Otherwise check if the JAX-RS application config class property is
		// present
		if (resourceConfigClassName == null)
			resourceConfigClassName = webConfig.getInitParameter(APPLICATION_CONFIG_CLASS);
		// If no resource config class property is present
		if (resourceConfigClassName == null) {
			// If the packages property is present then
			// use the packages resource config
			String packages = webConfig.getInitParameter(
					PackagesResourceConfig.PROPERTY_PACKAGES);
			if (packages != null) {
				props.put(PackagesResourceConfig.PROPERTY_PACKAGES, packages);
				return new PackagesResourceConfig(props);
			}
			ResourceConfig defaultConfig = webConfig.getDefaultResourceConfig(props);
			if (defaultConfig != null)
				return defaultConfig;
			return getDefaultResourceConfig(props, webConfig);
		}
		try {
			Class<?> resourceConfigClass = AccessController.doPrivileged(ReflectionHelper.classForNameWithExceptionPEA(resourceConfigClassName));
			// TODO add support for WebAppResourceConfig
			if (resourceConfigClass == ClasspathResourceConfig.class) {
				String[] paths = getPaths(webConfig.getInitParameter(
						ClasspathResourceConfig.PROPERTY_CLASSPATH));
				props.put(ClasspathResourceConfig.PROPERTY_CLASSPATH, paths);
				return new ClasspathResourceConfig(props);
			} else if (ResourceConfig.class.isAssignableFrom(resourceConfigClass)) {
				try {
					Constructor constructor = resourceConfigClass.getConstructor(Map.class);
					if (ClasspathResourceConfig.class.isAssignableFrom(resourceConfigClass)) {
						String[] paths = getPaths(webConfig.getInitParameter(
								ClasspathResourceConfig.PROPERTY_CLASSPATH));
						props.put(ClasspathResourceConfig.PROPERTY_CLASSPATH, paths);
					}
					return (ResourceConfig) constructor.newInstance(props);
				} catch (NoSuchMethodException ex) {
					// Pass through and try the default constructor
				} catch (Exception e) {
					throw new ServletException(e);
				}
				return new DeferredResourceConfig(resourceConfigClass.asSubclass(ResourceConfig.class));
			} else if (Application.class.isAssignableFrom(resourceConfigClass)) {
				return new DeferredResourceConfig(resourceConfigClass.asSubclass(Application.class));
			} else {
				String message = "Resource configuration class, " + resourceConfigClassName +
						", is not a super class of " + Application.class;
				throw new ServletException(message);
			}
		} catch (ClassNotFoundException e) {
			String message = "Resource configuration class, " + resourceConfigClassName +
					", could not be loaded";
			throw new ServletException(message, e);
		} catch (PrivilegedActionException e) {
			String message = "Resource configuration class, " + resourceConfigClassName +
					", could not be loaded";
			throw new ServletException(message, e.getCause());
		}
	}

	private Map<String, Object> getInitParams(WebConfig webConfig) {
		Map<String, Object> props = new HashMap<String, Object>();
		Enumeration names = webConfig.getInitParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			props.put(name, webConfig.getInitParameter(name));
		}
		return props;
	}

	private String[] getPaths(String classpath) throws ServletException {
		final ServletContext context = config.getServletContext();
		if (classpath == null) {
			String[] paths = {
					context.getRealPath("/WEB-INF/lib"),
					context.getRealPath("/WEB-INF/classes")
			};
			if (paths[0] == null && paths[1] == null) {
				String message = "The default deployment configuration that scans for " +
						"classes in /WEB-INF/lib and /WEB-INF/classes is not supported " +
						"for the application server." +
						"Try using the package scanning configuration, see the JavaDoc for " +
						PackagesResourceConfig.class.getName() + " and the property " +
						PackagesResourceConfig.PROPERTY_PACKAGES + ".";
				throw new ServletException(message);
			}
			return paths;
		} else {
			String[] virtualPaths = classpath.split(";");
			List<String> resourcePaths = new ArrayList<String>();
			for (String virtualPath : virtualPaths) {
				virtualPath = virtualPath.trim();
				if (virtualPath.length() == 0)
					continue;
				String path = context.getRealPath(virtualPath);
				if (path != null)
					resourcePaths.add(path);
			}
			if (resourcePaths.isEmpty()) {
				String message = "None of the declared classpath locations, " +
						classpath +
						", could be resolved. " +
						"This could be because the default deployment configuration that scans for " +
						"classes in classpath locations is not supported. " +
						"Try using the package scanning configuration, see the JavaDoc for " +
						PackagesResourceConfig.class.getName() + " and the property " +
						PackagesResourceConfig.PROPERTY_PACKAGES + ".";
				throw new ServletException(message);
			}
			return resourcePaths.toArray(new String[resourcePaths.size()]);
		}
	}

	private void configureJndiResources(ResourceConfig rc) {
		// Obtain any instances that are registered in JNDI
		// Assumes such instances are singletons
		// Registered classes have to be interfaces
		javax.naming.Context x = InitialContextHelper.getInitialContext();
		if (x != null) {
			Iterator<Class<?>> i = rc.getClasses().iterator();
			while (i.hasNext()) {
				Class<?> c = i.next();
				if (!c.isInterface())
					continue;
				try {
					Object o = x.lookup(c.getName());
					if (o != null) {
						i.remove();
						rc.getSingletons().add(o);
						LOGGER.log(Level.INFO,
								"An instance of the class " + c.getName() +
										" is found by JNDI look up using the class name as the JNDI name. " +
										"The instance will be registered as a singleton.");
					}
				} catch (NamingException ex) {
					LOGGER.log(Level.CONFIG,
							"JNDI lookup failed for Jersey application resource " + c.getName(),
							ex);
				}
			}
		}
	}

	private void filterFormParameters(HttpServletRequest servletRequest, ContainerRequest containerRequet) throws IOException {
		if (MediaTypes.typeEquals(MediaType.APPLICATION_FORM_URLENCODED_TYPE, containerRequet.getMediaType())
				&& !isEntityPresent(containerRequet)) {
			Form f = new Form();
			Enumeration e = servletRequest.getParameterNames();
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String[] values = servletRequest.getParameterValues(name);
				f.put(name, Arrays.asList(values));
			}
			if (!f.isEmpty()) {
				containerRequet.getProperties().put(FormDispatchProvider.FORM_PROPERTY, f);
				if (LOGGER.isLoggable(Level.WARNING)) {
					LOGGER.log(Level.WARNING,
							"A servlet request, to the URI " + containerRequet.getRequestUri() + ", " +
									"contains form parameters in " +
									"the request body but the request body has been consumed " +
									"by the servlet or a servlet filter accessing the request " +
									"parameters. Only resource methods using @FormParam " +
									"will work as expected. Resource methods consuming the " +
									"request body by other means will not work as expected.");
				}
			}
		}
	}

	private boolean isEntityPresent(ContainerRequest cr) throws IOException {
		InputStream in = cr.getEntityInputStream();
		if (!in.markSupported()) {
			in = new BufferedInputStream(in, ReaderWriter.BUFFER_SIZE);
			cr.setEntityInputStream(in);
		}
		in.mark(1);
		if (in.read() == -1)
			return false;
		else {
			in.reset();
			return true;
		}
	}

	private InBoundHeaders getHeaders(HttpServletRequest request) {
		InBoundHeaders rh = new InBoundHeaders();
		for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements();) {
			String name = names.nextElement();
			List<String> valueList = new LinkedList<String>();
			for (Enumeration<String> values = request.getHeaders(name); values.hasMoreElements();) {
				valueList.add(values.nextElement());
			}
			rh.put(name, valueList);
		}
		return rh;
	}
}
