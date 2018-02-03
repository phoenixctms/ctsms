package org.phoenixctms.ctsms.web.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
public class InputFieldImageServlet extends FileServletBase {

	@Override
	protected FileStream createFileStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Long inputFieldId = WebUtil.stringToLong(request.getParameter(GetParamNames.INPUT_FIELD_ID.toString()));
		InputFieldImageVO image = null;
		try {
			image = WebUtil.getServiceLocator().getToolsService().getInputFieldImage(inputFieldId);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (AuthorisationException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (IllegalArgumentException e) {
		}
		final MimeTypeVO contentTypeVO;
		final Long fileSize;
		final String fileName;
		final byte[] data;
		if (image != null && (contentTypeVO = image.getContentType()) != null && (fileSize = image.getFileSize()) != null && (fileName = image.getFileName()) != null
				&& (data = image.getDatas()) != null) {
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
					return new ByteArrayInputStream(data);
				}


			};
		}
		return null;
	}
}