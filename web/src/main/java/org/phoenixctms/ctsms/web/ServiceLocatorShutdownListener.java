package org.phoenixctms.ctsms.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.phoenixctms.ctsms.ServiceLocator;

/**
 * Closes the Spring application context of the core when the web application stops (undeploy, redeploy, Tomcat
 * shutdown), so the destroy methods of its beans run: e.g. the input field l10n overlay stops its reload thread, and
 * Hibernate and the connection pool are closed, instead of staying alive with the stopped web application's classes.
 */
public class ServiceLocatorShutdownListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			ServiceLocator.instance().shutdown();
		} catch (Throwable t) {
			event.getServletContext().log("closing the application context failed", t);
		}
	}
}
