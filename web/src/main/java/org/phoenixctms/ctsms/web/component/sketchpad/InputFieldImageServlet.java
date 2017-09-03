package org.phoenixctms.ctsms.web.component.sketchpad;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
public class InputFieldImageServlet extends HttpServlet {

	private static final int DEFAULT_BUFFER_SIZE = 96 * 1024; // 100KB.

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		Long inputFieldId = WebUtil.stringToLong(request.getParameter(GetParamNames.INPUT_FIELD_ID.toString()));
		InputFieldImageVO image = null;
		try {
			image = WebUtil.getServiceLocator().getToolsService().getInputFieldImage(inputFieldId);
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		} catch (AuthorisationException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		} catch (IllegalArgumentException e) {
		}
		MimeTypeVO contentTypeVO;
		Long fileSize;
		String fileName;
		if (image != null && (contentTypeVO = image.getContentType()) != null && (fileSize = image.getFileSize()) != null && (fileName = image.getFileName()) != null) {
			response.reset();
			response.setBufferSize(DEFAULT_BUFFER_SIZE);
			response.setContentType(contentTypeVO.getMimeType());
			response.setHeader("Content-Length", fileSize.toString());
			response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
			// Prepare streams.
			BufferedInputStream input = null;
			BufferedOutputStream output = null;
			try {
				// Open streams.
				input = new BufferedInputStream(new ByteArrayInputStream(image.getDatas()), DEFAULT_BUFFER_SIZE);
				output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
				// Write file contents to response.
				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}
			} finally {
				// Gently close streams.
				close(output);
				close(input);
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		}
	}
}