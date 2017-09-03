package org.phoenixctms.ctsms.web.jersey;

import io.swagger.jaxrs.config.BeanConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import net.java.dev.webdav.jaxrs.xml.WebDavContextResolver;

import org.phoenixctms.ctsms.web.jersey.resource.Win32LastAccessTime;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

import com.sun.jersey.api.core.PackagesResourceConfig;

public class Application extends PackagesResourceConfig {

	static {
		// Handler fh = new ConsoleHandler(); // FileHandler("/tmp/jersey_test.log");
		// Logger.getLogger("").addHandler(fh);
		Logger logger = Logger.getLogger("com.sun.jersey");
		logger.addHandler(new ConsoleHandler());
		logger.setLevel(Level.INFO);
	}

	public Application() {
		super(Settings.WEB_ROOT_PACKAGE_NAME + ".jersey");
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setSchemes(new String[] { WebUtil.getHttpScheme() });
		beanConfig.setHost(WebUtil.getHttpHost());
		beanConfig.setBasePath("/" + WebUtil.REST_API_PATH);
		beanConfig.setTitle(Settings.getString(SettingCodes.API_TITLE, Bundle.SETTINGS, DefaultSettings.API_TITLE));
		// beanConfig.setDescription(Settings.getString(SettingCodes.API_INSTANCE, Bundle.SETTINGS, DefaultSettings.API_INSTANCE));
		beanConfig.setVersion(Settings.getString(SettingCodes.API_VERSION, Bundle.SETTINGS, DefaultSettings.API_VERSION));
		beanConfig.setContact(Settings.getContactEmail());
		// beanConfig.setResourcePackage("io.swagger.resources");
		beanConfig.setResourcePackage(Settings.WEB_ROOT_PACKAGE_NAME + ".jersey.resource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.shared.HyperlinkResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.course.CourseResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.inventory.InventoryResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.staff.StaffResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.shared.SearchResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.VisitResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.proband.ProbandResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.inputfield.InputFieldResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.VisitScheduleItemResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.user.UserResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.InquiryResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.shared.FileResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.ProbandListEntryResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.ProbandListEntryTagResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.ProbandListEntryTagValuesResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.ProbandListStatusEntryResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.shared.JournalResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.proband.InquiryValuesResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.shared.IndexResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.shared.SelectionSetResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.shared.ToolsResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.inputfield.SelectionSetValueResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.ProbandGroupResource");
		// beanConfig.setResourcePackage("org.phoenixctms.ctsms.web.jersey.resource.trial.TrialResource");
		beanConfig.setScan(true);
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources =super.getClasses();

		//resources.add(FirstResource.class);
		//resources.add(SecondResource.class);
		//...

		resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
		resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

		return resources;
	}

	@Override
	public Set<Object> getSingletons() {
		try {
			return new HashSet<Object>(Arrays.asList(new WebDavContextResolver(Win32LastAccessTime.class)));
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
}
