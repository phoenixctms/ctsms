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
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
public class BeaconServlet extends FileServletBase {

	// https://stackoverflow.com/questions/31720635/creating-1x1-transparent-gif-in-a-servlet
	private final static byte[] BEACON_IMAGE = { 0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x1, 0x0, 0x1, 0x0, (byte) 0x80, 0x0, 0x0,
			(byte) 0xff, (byte) 0xff, (byte) 0xff, 0x0, 0x0, 0x0, 0x2c, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x1, 0x0,
			0x0, 0x2, 0x2, 0x44, 0x1, 0x0, 0x3b };
	private final static String DUUMY_BEACON_FILE_NAME = "beacon";

	public static String getBeacon(HttpServletRequest request) {
		try {
			String pathInfo = request.getPathInfo();
			String fileName = pathInfo.substring(pathInfo.lastIndexOf('/') + 1, pathInfo.length());
			return fileName.substring(0, fileName.lastIndexOf('.'));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected FileStream createFileStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String beacon = getBeacon(request);
		if (!WebUtil.isTrustedReferer(request)) {
			try {
				WebUtil.getServiceLocator().getToolsService().markMassMailRead(beacon);
			} catch (AuthorisationException | ServiceException | IllegalArgumentException | AuthenticationException e) {
			}
		}
		return new FileStream() {

			@Override
			public String getFileName() {
				return (CommonUtil.isEmptyString(beacon) ? DUUMY_BEACON_FILE_NAME : CommonUtil.getSafeFilename(beacon)) + "." + CommonUtil.GIF_FILENAME_EXTENSION;
			}

			@Override
			public Long getFileSize() {
				return (long) BEACON_IMAGE.length;
			}

			@Override
			public String getMimeType() {
				return CommonUtil.GIF_MIMETYPE_STRING;
			}

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(BEACON_IMAGE);
			}

			@Override
			public boolean isNotFound() {
				return false;
			}
		};
	}
}
