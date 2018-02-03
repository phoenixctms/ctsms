package org.phoenixctms.ctsms.web.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.Urls;

public class UnsubscribeServlet extends HttpServlet {

	public static String getBeacon(HttpServletRequest request) {
		try {
			String pathInfo = request.getPathInfo();
			String path = pathInfo.substring(pathInfo.lastIndexOf('/') + 1, pathInfo.length());
			return path.substring(0);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer url = new StringBuffer();
		url.append(Urls.UNSUBSCRIBE.value());
		String beacon = getBeacon(request);
		if (beacon != null) {
			url.append('?').append(GetParamNames.BEACON.toString()).append("=").append(beacon);
		}
		request.getRequestDispatcher(url.toString()).forward(request, response);
	}
}
