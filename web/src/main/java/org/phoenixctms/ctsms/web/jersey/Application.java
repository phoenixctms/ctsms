package org.phoenixctms.ctsms.web.jersey;

import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.api.core.PackagesResourceConfig;

import io.swagger.jaxrs.config.BeanConfig;

public class Application extends PackagesResourceConfig {

	static {
		Logger logger = Logger.getLogger("com.sun.jersey");
		logger.addHandler(new ConsoleHandler());
		logger.setLevel(Level.INFO);
	}

	public Application() {
		super(Settings.WEB_ROOT_PACKAGE_NAME + ".jersey"); //Jersey 1.19 class bytecode reader is hardcoded for 1.8 .class files
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setSchemes(new String[] { WebUtil.getHttpScheme() });
		beanConfig.setHost(WebUtil.getHttpHost());
		beanConfig.setBasePath("/" + WebUtil.REST_API_PATH);
		beanConfig.setTitle(Settings.getString(SettingCodes.API_TITLE, Bundle.SETTINGS, DefaultSettings.API_TITLE));
		beanConfig.setVersion(Settings.getString(SettingCodes.API_VERSION, Bundle.SETTINGS, DefaultSettings.API_VERSION));
		beanConfig.setContact(Settings.getContactEmail());
		beanConfig.setResourcePackage(Settings.WEB_ROOT_PACKAGE_NAME + ".jersey.resource");
		beanConfig.setScan(true);
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = super.getClasses();
		resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
		resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		return resources;
	}
}
