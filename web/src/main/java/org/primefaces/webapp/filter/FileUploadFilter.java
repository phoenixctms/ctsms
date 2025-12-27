package org.primefaces.webapp.filter;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.primefaces.webapp.MultipartRequest;

public class FileUploadFilter implements Filter {

	private static final Logger logger = Logger.getLogger(FileUploadFilter.class.getName());
	private static final String THRESHOLD_SIZE_PARAM = "thresholdSize";
	private static final String UPLOAD_DIRECTORY_PARAM = "uploadDirectory";
	private String thresholdSize;
	private String uploadDir;

	public void init(FilterConfig filterConfig) throws ServletException {
		thresholdSize = filterConfig.getInitParameter(THRESHOLD_SIZE_PARAM);
		uploadDir = filterConfig.getInitParameter(UPLOAD_DIRECTORY_PARAM);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("FileUploadFilter initiated successfully");
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);
		if (isMultipart) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Parsing file upload request");
			}
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			if (thresholdSize != null) {
				diskFileItemFactory.setSizeThreshold(Integer.valueOf(thresholdSize));
			}
			if (uploadDir != null) {
				diskFileItemFactory.setRepository(new File(uploadDir));
			}
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			try {
				MultipartRequest multipartRequest = new MultipartRequest(httpServletRequest, servletFileUpload);
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("File upload request parsed successfully");
				}
				filterChain.doFilter(multipartRequest, response);
				return;
			} catch (IOException e) {
				// 🔧 commons-io 2.15+ can throw EOFException here
				Throwable cause = e.getCause();
				if (e instanceof EOFException || cause instanceof EOFException) {
					if (logger.isLoggable(Level.WARNING)) {
						logger.log(Level.WARNING,
								"Ignoring EOFException while parsing multipart request " +
										"(known issue with newer commons-io)",
								e);
					}
					// fall through → continue with original request
				} else {
					throw e;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Destroying FileUploadFilter");
		}
	}
}
