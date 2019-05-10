package org.phoenixctms.ctsms.web.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
public class PublicFileServlet extends FileServletBase {

	@Override
	protected FileStream createFileStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Long fileId = WebUtil.stringToLong(request.getParameter(GetParamNames.FILE_ID.toString()));
		FileStreamOutVO stream = null;
		try {
			stream = WebUtil.getServiceLocator().getToolsService().getPublicFileStream(fileId);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (AuthorisationException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (IllegalArgumentException e) {
		}
		final MimeTypeVO contentTypeVO = (stream != null ? stream.getContentType() : null);
		final Long fileSize = (stream != null ? stream.getSize() : null);
		final String fileName = (stream != null ? stream.getFileName() : null);
		final InputStream inputStream = (stream != null ? stream.getStream() : null);
		final boolean found = (contentTypeVO != null && fileSize != null && fileName != null && inputStream != null);
		return new FileStream() {

			@Override
			public String getFileName() {
				return fileName;
			}

			@Override
			public Long getFileSize() {
				return fileSize;
			}

			@Override
			public String getMimeType() {
				return contentTypeVO.getMimeType();
			}

			@Override
			public InputStream getStream() {
				return inputStream;
			}

			@Override
			public boolean isNotFound() {
				// TODO Auto-generated method stub
				return !found;
			}
		};
	}
}
