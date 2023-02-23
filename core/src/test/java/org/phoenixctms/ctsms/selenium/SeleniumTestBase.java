package org.phoenixctms.ctsms.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import java.util.logging.FileHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.phoenixctms.ctsms.TestDataProvider;
import org.phoenixctms.ctsms.test.CustomReport;
import org.phoenixctms.ctsms.test.OutputLogger;
import org.phoenixctms.ctsms.test.ReportEmailSender;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.Compress;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.FilePathSplitter;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.TestRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

public class SeleniumTestBase implements OutputLogger, ITestListener {

	private abstract class RetryEntryBase {

		protected abstract void entry();

		protected abstract void paginate();

		public void enter() {
			try {
				entry();
			} catch (org.openqa.selenium.NoSuchElementException e) {
				info("item not found, trying on next page ...");
				paginate();
				enter();
			}
		}
	}

	protected abstract class InquiryFormFieldEntry extends RetryEntryBase {

		protected void paginate() {
			clickButton("tabView:inquiryvalue_form:update");
			if (waitForUpdateOperationSuccessful("tabView:inquiryvalue_form")) {
				info("inquiry values page saved");
				createScreenshot();
				clickDatagridNextPage("tabView:inquiryvalue_form:inquiry_input_grid");
			} else {
				testFailed("saving inquiry values failed");
				//return;
			}
		}
	}

	protected abstract class EcrfFieldEntry extends RetryEntryBase {

		protected abstract String getFormId();

		protected void saveSeriesSection(String fieldId, boolean series) {
			if (series) {
				WebElement gridRow = getChromeDriver().findElement(By.id(fieldId)).findElement(By.xpath("./ancestor::tr[contains(@class, 'ui-datagrid-row')][1]"));
				Iterator<WebElement> it = getChildElements(getParentElement(gridRow)).iterator();
				WebElement lastGridRow = null;
				while (it.hasNext()) {
					lastGridRow = it.next();
				}
				if (gridRow.equals(lastGridRow)) {
					WebElement panel = gridRow.findElement(By.xpath("./ancestor::table[contains(@class, 'ctsms-content-panelgrid')][1]"));
					clickButton(getButtonIdByLabel(panel, "Save new index section"));
					if (waitForUpdateOperationSuccessful(getFormId())) {
						info("series section index saved");
					} else {
						testFailed("saving series section values failed");
						//return;
					}
				}
			}
		}

		protected void paginate() {
			clickButton(getFormId() + ":update");
			if (waitForUpdateOperationSuccessful(getFormId())) {
				info("eCRF values page saved");
				createScreenshot();
				clickDatagridNextPage(getFormId() + ":ecrffield_input_grid");
			} else {
				testFailed("saving eCRF values failed");
				//return;
			}
		}
	}

	protected abstract class TrialEcrfFieldEntry extends EcrfFieldEntry {

		protected String getFormId() {
			return "tabView:trialecrfstatusentry_form";
		}
	}

	private abstract class SelectDatatableRow extends RetryEntryBase {

		protected abstract String getDatatableId();

		protected void paginate() {
			clickDatatableNextPage(getDatatableId());
		}
	}

	protected final static Duration WEB_DRIVER_WAIT = Duration.ofSeconds(60l);
	private final static String ADD_OPERATION_SUCCESSFUL_MESSAGE = "create operation successful";
	private final static String UPDATE_OPERATION_SUCCESSFUL_MESSAGE = "update operation successful";
	private final static String UPLOAD_SUCCESSFUL_MESSAGE = "upload successful";
	private final static String PROBAND_ENTITY_WINDOW_NAME = "proband_entity_";
	private final static Pattern PROBAND_ENTITY_WINDOW_NAME_REGEXP = Pattern.compile("^" + PROBAND_ENTITY_WINDOW_NAME + "(\\d+)$");
	//private final static String SEARCH_RESULT_COUNT_MESSAGE = "Search result for query";
	private final static Pattern CRITERIA_DATATABLE_HEAD_COUNT_MESSAGE_REGEXP = Pattern.compile("^[^:]+: (\\d+(,\\d+)?) [A-Za-z()]+$");
	private final static Pattern DATATABLE_HEAD_COUNT_MESSAGE_REGEXP = Pattern.compile("^(\\d+(,\\d+)?)");
	private final static String ENTITY_WINDOW_NAME_NEW_SUFFIX = "new";
	private final static String NO_RECORDS_LABEL = "no records";
	private final static String[] HTMLTOPDF_COMMAND = new String[] { "wkhtmltopdf", "--enable-local-file-access" }; //, "--page-size", "A4", "--orientation", "Portrait", "--dpi",			"130" };
	private ChromeDriver driver;
	private Logger logger;
	private int screenshotCount = 0;
	private Date now = new Date();
	private ApplicationContext applicationContext;
	private TestDataProvider testDataProvider;
	private ReportEmailSender reportEmailSender;
	private static ArrayList<ITestContext> results;
	private static ArrayList<File> reportFiles;
	//	private ScreenRecorder screenRecorder;
	//	private int movieCount = 0;
	static {
		results = new ArrayList<ITestContext>();
		reportFiles = new ArrayList<File>();
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				//System.out.println("Shutdown Hook is running !");
				try {
					SeleniumTestBase t = (new SeleniumTestBase() {
					});
					t.sendReportEmail();
					t.saveAttachmentsToFTP();
				} catch (Throwable t) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.err.println(t);
				}
			}
		});
	}

	protected TestDataProvider getTestDataProvider() {
		if (testDataProvider == null) {
			testDataProvider = getApplicationContext().getBean(TestDataProvider.class);
			testDataProvider.setLog(this);
		}
		return testDataProvider;
	}

	protected ReportEmailSender getReportEmailSender() {
		if (reportEmailSender == null) {
			reportEmailSender = getApplicationContext().getBean(ReportEmailSender.class);
			//testDataProvider.setLog(this);
		}
		return reportEmailSender;
	}

	protected ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext(new String[] { "/applicationContextTest.xml" });
		}
		return applicationContext;
	}

	private String getTimestamp() {
		return CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN);
	}

	private Logger getLogger() {
		if (logger == null) {
			Properties props = new Properties();
			try {
				InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
				props.load(configStream);
				configStream.close();
			} catch (Exception e) {
				//e.printStackTrace();
				System.err.println(e);
			}
			props.setProperty("log4j.appender.SELENIUM.file", new File(getTestDirectory(), this.getClass().getSimpleName() + "_" + getTimestamp() + ".log").getAbsolutePath());
			PropertyConfigurator.configure(props);
			logger = LoggerFactory.getLogger("selenium");
			//			logger.ge
			//			if (!CommonUtil.isEmptyString(getTestDirectory())) {
			//				try {
			//					logger.addHandler(new FileHandler(new File(getTestDirectory(), logger.getName()).getAbsolutePath() + "_" + getTimestamp() + ".log"));
			//				} catch (Exception e) {
			//					e.printStackTrace();
			//				}
			//			}
			//logger.setLevel(Level.INFO);
		}
		return logger;
	}

	protected static String getTestDirectory() {
		return System.getProperty("ctsms.test.directory");
	}

	protected static String getUrl(String urlPath) {
		StringBuilder sb = new StringBuilder();
		String baseUrl = System.getProperty("ctsms.test.baseurl");
		if (CommonUtil.isEmptyString(baseUrl)) {
			baseUrl = "http://localhost:8080";
		}
		sb.append(baseUrl);
		if (sb.toString().endsWith("/")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		if (!CommonUtil.isEmptyString(urlPath)) {
			if (!urlPath.startsWith("/")) {
				sb.append("/");
			}
			sb.append(urlPath);
		}
		return sb.toString();
	}

	protected void load(String url) {
		info("accessing " + url);
		getChromeDriver().get(url);
	}

	protected void login(String username, String password) {
		info("logging in");
		sendKeys("login_form:username", username);
		sendKeys("login_form:password", password, Keys.ENTER);
	}

	protected AuthenticationVO createAuth(String username, String password) {
		AuthenticationVO auth = new AuthenticationVO();
		auth.setUsername(username);
		auth.setPassword(password);
		return auth;
	}

	protected void setProviderAuth(String username, String password) {
		getTestDataProvider().setAuth(createAuth(username, password));
	}

	protected void closeWindow(String windowName) {
		//String currentWindowHandle = getChromeDriver().getWindowHandle();
		getChromeDriver().close();
		info("closed browser window: " + windowName);
	}
	//	private ScreenRecorder getScreenRecorder() throws HeadlessException, IOException, AWTException {
	//		if (screenRecorder == null) {
	//			
	//			//https://medium.com/@nisal444/screen-recorder-for-selenium-webdriver-with-testng-and-java-87287230ce7b
	//			screenRecorder = new ScreenRecorder(
	//					GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(),
	//					new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height),
	//					new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.FILE, FormatKeys.MimeTypeKey, FormatKeys.MIME_AVI),
	//					new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
	//							org.monte.media.VideoFormatKeys.CompressorNameKey,
	//							org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, org.monte.media.VideoFormatKeys.DepthKey, 24, FormatKeys.FrameRateKey,
	//							Rational.valueOf(15), org.monte.media.VideoFormatKeys.QualityKey, 1.0f, FormatKeys.KeyFrameIntervalKey, 15 * 60),
	//					new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, "black", FormatKeys.FrameRateKey, Rational.valueOf(30)),
	//					(Format) null,
	//					new File(getTestDirectory())) {
	//
	//				@Override
	//				protected File createMovieFile(Format fileFormat) throws IOException {
	//					if (!movieFolder.exists()) {
	//						movieFolder.mkdirs();
	//					} else if (!movieFolder.isDirectory()) {
	//						throw new IOException("\"" + movieFolder + "\" is not a directory.");
	//					}
	//					movieCount += 1;
	//					return new File(movieFolder.getAbsolutePath(),
	//							String.format("%s_%03d_%s.%s", getTestId(), movieCount, getTimestamp(), Registry.getInstance().getExtension(fileFormat)));
	//				}
	//			};
	//		}
	//		return screenRecorder;
	//	}

	protected void finalize() throws Throwable {
		if (driver != null) {
			Iterator<String> it = driver.getWindowHandles().iterator();
			while (it.hasNext()) {
				closeWindow(it.next());
			}
			driver = null;
		}
		if (applicationContext != null) {
			((ClassPathXmlApplicationContext) applicationContext).close();
		}
		if (logger != null) {
			logger.info("finalize");
			logger = null;
		}
		super.finalize();
	}

	protected void createScreenshot() {
		createScreenshot(null);
	}

	protected void createScreenshot(String name) {
		try {
			Thread.sleep(2000);//finish UI fade effects 
		} catch (InterruptedException e) {
		}
		File screenshot = getChromeDriver().getScreenshotAs(OutputType.FILE);
		if (!CommonUtil.isEmptyString(getTestDirectory())) {
			File dest;
			if (CommonUtil.isEmptyString(name)) {
				screenshotCount += 1;
				dest = new File(getTestDirectory(), String.format("%s_%03d_%s.png", this.getClass().getSimpleName(), screenshotCount, getTimestamp()));
			} else {
				dest = new File(getTestDirectory(), String.format("%s_%s_%s.png", this.getClass().getSimpleName(), name, getTimestamp()));
			}
			if (screenshot.renameTo(dest)) {
				info("screenshot created: " + dest.getName());
				Reporter.setEscapeHtml(false);
				//Reporter.log("screenshot created: <a href=\"file:///" + dest.getName() + "\">" + dest.getName() + "</a>");
				Reporter.log("<img src=\"" + dest.getName() + "\">");
				return;
			}
		}
		info("screenshot created: " + screenshot.getAbsolutePath());
		Reporter.setEscapeHtml(false);
		//Reporter.log("screenshot created: <a href=\"file:///" + screenshot.getName() + "\">" + screenshot.getName() + "</a>");
		Reporter.log("<img src=\"" + screenshot.getName() + "\">");
	}

	protected ChromeDriver getChromeDriver() {
		if (driver == null) {
			//String pathToChromeDriver = "C:\\chromedriver_win32\\chromedriver.exe";
			//System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--headless");
			chromeOptions.addArguments("--no-sandbox");
			String windowSize = System.getProperty("ctsms.test.windowsize");
			if (!CommonUtil.isEmptyString(windowSize)) {
				chromeOptions.addArguments("--window-size=" + windowSize); //3840,2160");
			}
			//https://stackoverflow.com/questions/34515328/how-to-set-default-download-directory-in-selenium-chrome-capabilities
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", getTestDirectory());
			chromeOptions.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(chromeOptions);
			info("chrome driver created");
		}
		return driver;
	}

	protected List<String> getInfoOutcomeMessages(String formId) {
		return getOutcomeMessages(formId, "ui-messages-info-summary");
	}

	protected List<String> getErrorOutcomeMessages(String formId) {
		return getOutcomeMessages(formId, "ui-messages-error-summary");
	}

	private List<String> getOutcomeMessages(String formId, String cssClass) {
		ArrayList<String> result = new ArrayList<String>();
		WebElement element = new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='" + formId + "']//span[contains(@class, '" + cssClass + "')]")));
		if (element != null) {
			Iterator<WebElement> it = getChromeDriver().findElements(By.xpath("//form[@id='" + formId + "']//span[contains(@class, '" + cssClass + "')]")).iterator();
			while (it.hasNext()) {
				String msg = it.next().getText();
				result.add(msg);
				debug(msg);
			}
		}
		return result;
	}

	protected boolean waitForAddOperationSuccessful(String formId) {
		Iterator<String> it = getInfoOutcomeMessages(formId).iterator();
		while (it.hasNext()) {
			if (ADD_OPERATION_SUCCESSFUL_MESSAGE.equals(it.next())) {
				info(ADD_OPERATION_SUCCESSFUL_MESSAGE + ": " + formId);
				return true;
			}
		}
		return false;
	}

	protected boolean waitForUpdateOperationSuccessful(String formId) {
		Iterator<String> it = getInfoOutcomeMessages(formId).iterator();
		while (it.hasNext()) {
			if (UPDATE_OPERATION_SUCCESSFUL_MESSAGE.equals(it.next())) {
				info(UPDATE_OPERATION_SUCCESSFUL_MESSAGE + ": " + formId);
				return true;
			}
		}
		return false;
	}

	protected boolean waitForUploadSuccessful(String formId) {
		Iterator<String> it = getInfoOutcomeMessages(formId).iterator();
		while (it.hasNext()) {
			if (UPLOAD_SUCCESSFUL_MESSAGE.equals(it.next())) {
				info(UPLOAD_SUCCESSFUL_MESSAGE + ": " + formId);
				return true;
			}
		}
		return false;
	}

	protected String getTdLabelValue(String tableId, String tdLabel) {
		String value = getChromeDriver()
				.findElement(By.xpath(
						"//table[@id='" + tableId + "']//td[contains(@class, 'ctsms-label-for-column')]//label[contains(text(), '" + tdLabel + "')]/../following-sibling::td[1]"))
				.getText();
		info(tdLabel + " '" + value + "': " + tableId);
		return value;
	}

	protected boolean waitForJobSuccessful(String formId, long timeout, long sleep) throws InterruptedException {
		Long start = System.currentTimeMillis();
		boolean successful = false;
		String msg = null;
		String reloadButtonId = null;
		while ((System.currentTimeMillis() - start) < (timeout * 1000l)
				&& !(successful = getChromeDriver().findElements(By.xpath("//table[@id='" + formId + ":filelink']//span[contains(text(), 'successful')]")).size() > 0)) {
			msg = getChromeDriver().findElements(By.xpath("//table[@id='" + formId + ":filelink']//tr")).iterator().next().getText();
			debug(msg);
			if (msg.toLowerCase().contains("error occurred")) {
				break;
			}
			if (reloadButtonId == null) {
				reloadButtonId = getButtonIdByLabel(formId, "Reload");
			} else {
				Thread.sleep(sleep * 1000l);
			}
			clickButton(reloadButtonId);
		}
		try {
			String[] jobOutput = getJobOutput(formId).split("\n+");
			for (int i = 0; i < jobOutput.length; i++) {
				info("    " + jobOutput[i]);
			}
		} catch (Exception e) {
		}
		msg = getChromeDriver().findElements(By.xpath("//table[@id='" + formId + ":filelink']//tr")).iterator().next().getText();
		info(msg);
		return successful;
	}

	protected String getJobOutput(String formId) {
		return getChromeDriver().findElement(By.xpath("//form[@id='" + formId + "']//pre[contains(@class, 'ctsms-monospaced')]")).getAttribute("innerText");
	}

	protected void clickJobTypeSelectOneRadio(String formId, String itemLabel) {
		String id = formId + ":jobTypes";
		//getChildElements(getParentElement(getParentElement(new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
		//		.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "']//.//label[contains(text(), '" + itemLabel + "')]")))))).get(0)
		//				.findElement(By.xpath("//div[contains(@class, 'ui-radiobutton-box')]")).click();
		getChildElements(getParentElement(getParentElement(new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "']//.//label[contains(text(), '" + itemLabel + "')]")))))).get(0).click();
		info("jobtype selectonemenu option '" + itemLabel + "' selected: " + id);
	}

	protected void clickTab(String href) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href=\"#" + href + "\"]"))).click();
		waitForAjax();
		info("tab clicked: " + href);
	}

	protected String getTabTitle(String href) {
		return getChromeDriver().findElement(By.xpath("//a[@href=\"#" + href + "\"]")).getText();
	}

	protected void waitForAjax() {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(new ExpectedCondition<Boolean>() {

					@Override
					public Boolean apply(WebDriver driver) {
						try {
							Long result = ((Long) ((ChromeDriver) driver).executeScript("return jQuery.active"));
							debug("jQuery.active: " + result);
							return result == 0l;
						} catch (Exception e) {
							error("jQuery.active: " + e.getMessage());
							return true;
						}
					}
				});
	}

	protected void clickSelectMany(String id, String itemLabel) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@id='" + id + "']//label[contains(text(), '" + itemLabel + "')]")))
				.click();
		info("selectmany option '" + itemLabel + "' selected: " + id);
	}

	protected void clickSelectOneRadio(String id, String itemLabel) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//table[@id='" + id + "']//label[contains(text(), '" + itemLabel + "')]")))
				.click();
		info("selectonemenu option '" + itemLabel + "' selected: " + id);
	}

	protected String getFieldIdByLabel(String formId, String inputFieldLabel) {
		info("looking up input field '" + inputFieldLabel + "'");
		return new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='" + formId + "']//label[contains(text(), '" + inputFieldLabel + "')]")))
				.getAttribute("for");
	}

	protected String getFieldIdBySectionPositionName(String section, long position, String inputFieldName) {
		info("looking up input field '" + section + " - " + position + ". " + inputFieldName + "'");
		WebElement panel = getParentElement(getParentElement(new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'ui-panel')]//span[contains(text(), '" + section + "')]")))));
		WebElement fieldSet = getParentElement(
				getParentElement(panel.findElement(By.xpath(".//fieldset/legend/span[contains(text(), '" + position + ". " + inputFieldName + "')]"))));
		return fieldSet.findElement(By.xpath(".//label")).getAttribute("for");
	}

	protected String getFieldIdBySectionPositionName(String section, long position, String inputFieldName, String index) {
		if (!CommonUtil.isEmptyString(index)) {
			section += ": index " + index;
		}
		return getFieldIdBySectionPositionName(section, position, inputFieldName);
	}

	protected WebElement getParentElement(WebElement element) {
		return element.findElement(By.xpath("./.."));
	}

	protected List<WebElement> getChildElements(WebElement element) {
		return element.findElements(By.xpath("./*"));
	}

	protected String getButtonIdByLabel(String formId, String buttonLabel) {
		info("looking up button '" + buttonLabel + "'");
		return getParentElement(new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='" + formId + "']//span[contains(text(), '" + buttonLabel + "')]")))).getAttribute("id");
	}

	protected String getButtonIdByLabel(WebElement parent, String buttonLabel) {
		info("looking up button '" + buttonLabel + "'");
		return getParentElement(new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(parent.findElement(By.xpath("//span[contains(text(), '" + buttonLabel + "')]"))))).getAttribute("id");
	}

	protected String getButtonIdByTitle(String formId, String title) {
		info("looking up button '" + title + "'");
		return new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='" + formId + "']//button[contains(@title, '" + title + "')]"))).getAttribute("id");
	}

	protected String getSplitButtonIdByLabel(String formId, String buttonLabel) {
		info("looking up split button '" + buttonLabel + "'");
		return getParentElement(getParentElement(new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='" + formId + "']//span[contains(text(), '" + buttonLabel + "')]"))))).getAttribute("id");
	}

	protected void clickSplitButton(String id, String itemLabel) {
		if (itemLabel != null) {
			clickButton(id + "_menuButton");
			new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "_menu']/ul/li/a/span[contains(text(), '" + itemLabel + "')]"))).click();
			info("splitbutton menu option '" + itemLabel + "' clicked");
		} else {
			clickButton(id + "_button");
		}
	}

	protected void clickSelectOneMenu(String id, String itemLabel) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "']/div[contains(@class, 'ui-selectonemenu-trigger')]"))).click();
		//		try {
		//			((ChromeDriver) driver).executeScript(
		//					"arguments[0].scrollIntoView(true);", new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
		//							.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='" + id + "_panel']/ul/li[contains(text(), '" + itemLabel + "')]"))));
		//		} catch (Exception e) {
		//			error("scrollIntoView: " + e.getMessage());
		//		}
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "_panel']/ul/li[contains(text(), '" + itemLabel + "')]"))).click();
		info("selectonemenu option '" + itemLabel + "' selected: " + id);
	}

	protected void clickSelectOneMenu(String id, int index) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "']/div[contains(@class, 'ui-selectonemenu-trigger')]"))).click();
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "_panel']/table//tr[" + Integer.toString(index + 1) + "]"))).click();
		info("selectonemenu option index " + Integer.toString(index) + " selected: " + id);
	}

	protected void clickCheckbox(String id) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
		info("checkbox clicked: " + id);
	}

	protected void clickButton(String id) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
		waitForAjax();
		info("button clicked: " + id);
	}

	protected void clickMenuItem(String id) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
		waitForAjax();
		info("menuitem clicked: " + id);
	}

	protected void switchToWindow(String windowName) {
		getChromeDriver().switchTo().window(windowName);
		info("switched to browser window: " + windowName);
	}

	protected String getWindowName() {
		try {
			String windowName = ((String) ((ChromeDriver) driver).executeScript("return window.name"));
			debug("window.name: " + windowName);
			return windowName;
		} catch (Exception e) {
			error("window.name: " + e.getMessage());
			return null;
		}
	}

	protected String getNewProbandEntityWindowName() {
		return PROBAND_ENTITY_WINDOW_NAME + ENTITY_WINDOW_NAME_NEW_SUFFIX;
	}
	//	protected String getProbandEntityWindowName(Long probandId) {
	//		return PROBAND_ENTITY_WINDOW_NAME + probandId.toString();
	//	}

	protected static String getResourceFilePath(String fileName) throws IOException {
		return (new File(System.getProperty("ctsms.test.resourcepath"), fileName)).getCanonicalPath();
	}

	protected void uploadFile(String id, String fileName) {
		new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + id + "']")));
		getChromeDriver().findElement(By.id(id + "_input")).sendKeys(fileName);
		//new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), '" + file.getName() + "')]")));
		info("file '" + fileName + "' selected for upload: " + id);
	}

	protected void filterDatatable(String datatableId, String columnName, String filter) {
		info("filter datatable column '" + columnName + "' for '" + filter + "': " + datatableId);
		getParentElement(
				getChromeDriver().findElement(By.xpath("//div[@id='" + datatableId + "']//th[contains(@class, 'ui-filter-column')]//span[contains(text(), '" + columnName + "')]")))
						.findElement(By.xpath("./input")).sendKeys(filter);
		waitForAjax();
	}

	protected void clickDatatableRow(String datatableId, int rowNum) {
		info("click datatable row " + rowNum + ": " + datatableId);
		new SelectDatatableRow() {

			@Override
			protected String getDatatableId() {
				return datatableId;
			}

			@Override
			protected void entry() {
				getChromeDriver().findElement(By.xpath("//tbody[@id='" + datatableId + "_data']//tr[@data-ri='" + rowNum + "']")).click();
				waitForAjax();
			}
		}.enter();
	}

	protected Long getProbandIdFromProbandEntityWindowName(String windowName) {
		Matcher matcher = PROBAND_ENTITY_WINDOW_NAME_REGEXP.matcher(windowName);
		if (matcher.find()) {
			return Long.parseLong(matcher.group(1));
		}
		return null;
	}

	protected WebElement findElementByAltId(String id, String alt) {
		WebElement element;
		try {
			element = getChromeDriver().findElement(By.id(alt));
		} catch (Exception e) {
			try {
				element = getChromeDriver().findElement(By.id(id));
			} catch (Exception e1) {
				try {
					element = new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
							.until(ExpectedConditions.elementToBeClickable(By.id(alt)));
				} catch (Exception e2) {
					element = new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
							.until(ExpectedConditions.elementToBeClickable(By.id(id)));
				}
			}
		}
		return element;
	}

	protected void sendKeys(String id, CharSequence... keysToSend) {
		findElementByAltId(id, id + "_input").sendKeys(keysToSend);
		for (int i = 0; i < keysToSend.length; i++) {
			if (keysToSend[i] instanceof Keys) {
				info(((Keys) keysToSend[i]).name() + " key pressed: " + id);
			} else {
				info("text '" + StringEscapeUtils.escapeJava(new StringBuilder(keysToSend[i]).toString()) + "' entered: " + id);
			}
		}
	}

	protected void testOK(String msg) {
		info(msg);
		//Assert.assertTrue(true, msg);
	}

	protected void testFailed(String msg) {
		info(msg);
		Assert.fail(msg);
	}

	@Override
	public void debug(String msg) {
		getLogger().debug(msg);
	}

	public void info(String msg, boolean escapeHtml) {
		Reporter.setEscapeHtml(escapeHtml);
		info(msg);
	}

	@Override
	public void info(String msg) {
		Reporter.setEscapeHtml(true);
		Reporter.log(msg, 0, false);
		getLogger().info(msg);
	}

	@Override
	public void error(String msg) {
		Reporter.setEscapeHtml(true);
		Reporter.log(msg, 0, false);
		getLogger().error(msg);
		throw new Error(msg);
	}

	//private boolean onStart = true;
	private boolean onTestStart = true;
	private boolean onTestSuccess = true;
	private boolean onTestFailure = true;
	private boolean onTestSkipped = true;
	private boolean onTestFailedButWithinSuccessPercentage = true;
	//private boolean onFinish = true;
	private boolean skipScreenshot = false;

	@AfterMethod(alwaysRun = true)
	public void resetITestResultEvents() {
		onTestStart = true;
		onTestSuccess = true;
		onTestFailure = true;
		onTestSkipped = true;
		onTestFailedButWithinSuccessPercentage = true;
		skipScreenshot = false;
	}

	@Override
	public void onTestStart(ITestResult result) {
		if (((SeleniumTestBase) result.getInstance()).onTestStart) {
			((SeleniumTestBase) result.getInstance()).onTestStart = false;
			((SeleniumTestBase) result.getInstance()).info("starting test '" + result.getName() + "'");
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		if (((SeleniumTestBase) result.getInstance()).onTestSuccess) {
			((SeleniumTestBase) result.getInstance()).onTestSuccess = false;
			((SeleniumTestBase) result.getInstance()).info("test '" + result.getName() + "' OK");
			if (!((SeleniumTestBase) result.getInstance()).skipScreenshot) {
				((SeleniumTestBase) result.getInstance()).createScreenshot(result.getName());
			}
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if (((SeleniumTestBase) result.getInstance()).onTestFailure) {
			((SeleniumTestBase) result.getInstance()).onTestFailure = false;
			((SeleniumTestBase) result.getInstance()).info("test '" + result.getName() + "' FAILURE");
			if (!((SeleniumTestBase) result.getInstance()).skipScreenshot) {
				((SeleniumTestBase) result.getInstance()).createScreenshot(result.getName());
			}
		}
	}

	protected void setSkipScreenshot(boolean skipScreenshot) {
		this.skipScreenshot = skipScreenshot;
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		if (((SeleniumTestBase) result.getInstance()).onTestSkipped) {
			((SeleniumTestBase) result.getInstance()).onTestSkipped = false;
			((SeleniumTestBase) result.getInstance()).info("test '" + result.getName() + "' SKIPPED");
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		if (((SeleniumTestBase) result.getInstance()).onTestFailedButWithinSuccessPercentage) {
			((SeleniumTestBase) result.getInstance()).onTestFailedButWithinSuccessPercentage = false;
			((SeleniumTestBase) result.getInstance()).info("test '" + result.getName() + "' SKIPPED");
		}
	}

	@BeforeClass
	public void init_000() {
		info("enter test class " + this.getClass().getCanonicalName());
	}

	@Override
	public void onStart(ITestContext context) {
		info("test class '" + this.getClass().getCanonicalName() + "' instantiated");
		//TestRunner runner = (TestRunner) context;
		//info("starting test '" + context.getName() + "'");
		//		try {
		//			getScreenRecorder().start();
		//		} catch (HeadlessException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (AWTException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}

	@BeforeTest
	public void setTestName(ITestContext context) {
		String name = System.getProperty("ctsms.test.name");
		if (!CommonUtil.isEmptyString(name)) {
			TestRunner runner = (TestRunner) context;
			runner.setTestName(name);
			info("set test name '" + context.getName() + "'");
		}
	}

	@BeforeTest
	public void setTestListener(ITestContext context) {
		TestNG myTestNG = TestNG.getDefault();
		myTestNG.setUseDefaultListeners(false);
		//		myTestNG.
		//		//myTestNG.setListenerClasses(classes);
		//		//Arrays.asList(new ITestNGListener[]{CustomReport.class});
		//		myTestNG.addListener(new CustomReport()); //.setListenerClasses(new ArrayList<ITestNGListener>() { add(CustomReport.class); });
		TestRunner runner = (TestRunner) context;
		//		CustomReport report = new CustomReport();
		//		report.onStart(context);
		runner.addListener(new CustomReport());
		info("default test listeners disabled, custom test report registered");
	}

	@BeforeTest
	public void setOutputDirectory(ITestContext context) {
		TestRunner runner = (TestRunner) context;
		runner.setOutputDirectory(getTestDirectory());
		info("set test output directory: " + context.getOutputDirectory());
		//		   String path=System.getProperty("user.dir");
		//		   runner.setOutputDirectory(path+"/output-testng");
	}

	@AfterTest
	public void logReportRecipients(ITestContext context) throws Exception {
		TestRunner runner = (TestRunner) context;
		if (!CommonUtil.isEmptyString(ReportEmailSender.getEmailRecipients())) {
			info("sending test results to: " + ReportEmailSender.getEmailRecipients());
		}
		//getReportEmailSender().addEmailAttachment((new Compress()).zipDirectory(getTestDirectory()), Compress.ZIP_MIMETYPE_STRING, "test_results.zip");
		//getReportEmailSender().send("testsubject", "testmessage");
	}

	@AfterTest
	public synchronized void storeResults(ITestContext context) throws Exception {
		results.add(context);
	}

	void saveAttachmentsToFTP() throws Exception {
		String host = System.getProperty("ftp.host");
		if (!CommonUtil.isEmptyString(host)) {
			FTPClient client = new FTPClient();
			try {
				client.connect(host);
				client.setFileType(FTP.BINARY_FILE_TYPE);
				client.enterLocalPassiveMode();
				client.login(System.getProperty("ftp.username"), System.getProperty("ftp.password"));
				String path = System.getProperty("ftp.path");
				if (!CommonUtil.isEmptyString(path)) {
					path = MessageFormat.format(path, System.getProperty("ctsms.test.version"), System.getProperty("git.commit"));
					client.makeDirectory(path);
					//if (!client.makeDirectory(path)) {
					//	throw new Exception("failed to create directory " + path);
					//}
				}
				Iterator<File> it = reportFiles.iterator();
				while (it.hasNext()) {
					File reportFile = it.next();
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(reportFile);
						StringBuilder remoteFilename = new StringBuilder();
						if (!CommonUtil.isEmptyString(path)) {
							remoteFilename.append(path);
							if (!path.endsWith("/") && path.length() > 1) {
								remoteFilename.append("/");
							}
						}
						remoteFilename.append(reportFile.getName());
						if (!client.storeFile(remoteFilename.toString(), fis)) {
							throw new Exception("failed to store remote file " + remoteFilename.toString());
						}
						//} catch (IOException e) {
						//	e.printStackTrace();
					} finally {
						try {
							if (fis != null) {
								fis.close();
							}
						} catch (IOException e) {
							//e.printStackTrace();
						}
					}
				}
				client.logout();
				//} catch (IOException e) {
				//	e.printStackTrace();
			} finally {
				try {
					client.disconnect();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
	}

	void sendReportEmail() throws Throwable {
		attachReports(true);
		boolean failure = false;
		Iterator<ITestContext> it = results.iterator();
		ArrayList<String> okTests = new ArrayList<String>();
		ArrayList<String> failedTests = new ArrayList<String>();
		while (it.hasNext()) {
			ITestContext context = it.next();
			if (context.getFailedTests().size() > 0) {
				failure = true;
			}
			Iterator<ITestResult> resultsIt = context.getPassedTests().getAllResults().iterator();
			while (resultsIt.hasNext()) {
				ITestResult result = resultsIt.next();
				okTests.add("OK: " + result.getTestClass().getName().replace("org.phoenixctms.ctsms.selenium.", "") + "#" + result.getName());
			}
			resultsIt = context.getFailedTests().getAllResults().iterator();
			while (resultsIt.hasNext()) {
				ITestResult result = resultsIt.next();
				failedTests.add("FAILED: " + result.getTestClass().getName().replace("org.phoenixctms.ctsms.selenium.", "") + "#" + result.getName());
			}
		}
		Collections.sort(okTests);
		Collections.sort(failedTests);
		StringBuilder body = new StringBuilder();
		String workflowUrl = System.getProperty("github.workflow.url");
		if (CommonUtil.isEmptyString(workflowUrl)) {
			body.append("Test results from workflow run:");
		} else {
			body.append("Test results from workflow run " + workflowUrl + ":");
		}
		//if (okTests.size() > 0) {
		//	body.append("\n\n");
		//	body.append(String.join("\n", okTests));
		//}
		body.append("\n\n");
		if (failedTests.size() > 0) {
			body.append(String.join("\n", failedTests));
		} else {
			body.append("All tests passed.");
		}
		String version = System.getProperty("ctsms.test.version");
		if (!CommonUtil.isEmptyString(version)) {
			body.append("\n\n");
			body.append("Phoenix CTMS Version " + version);
		}
		//https://stackoverflow.com/questions/58886293/getting-current-branch-and-commit-hash-in-github-action
		String branch = System.getProperty("git.branch");
		String commit = System.getProperty("git.commit");
		if (!CommonUtil.isEmptyString(branch) || !CommonUtil.isEmptyString(commit)) {
			body.append("\n");
		}
		if (!CommonUtil.isEmptyString(branch)) {
			body.append(branch);
			if (!CommonUtil.isEmptyString(commit)) {
				body.append("/");
			}
		}
		if (!CommonUtil.isEmptyString(commit)) {
			body.append(commit);
		}
		StringBuilder subject = new StringBuilder("E2E usecase validation ");
		if (failure) {
			subject.append("FAILURE");
		} else {
			subject.append("SUCCESS");
		}
		String message = System.getProperty("git.message");
		if (!CommonUtil.isEmptyString(message)) {
			subject.append(": ");
			subject.append(message);
		}
		getReportEmailSender().send(subject.toString(), body.toString());
	}

	protected String getTestId() {
		return String.format("%s-%s", this.getClass().getSimpleName(), getTimestamp());
	}

	protected Long getCountFromDatatableHead(String datatableId) {
		info("parse header of datatable: " + datatableId);
		String resultCountMessage = new WebDriverWait(getChromeDriver(), WEB_DRIVER_WAIT)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='" + datatableId + "']//td[contains(@class,'ctsms-datatable-header-text-column')]")))
				.getText();
		info(resultCountMessage);
		Matcher matcher = DATATABLE_HEAD_COUNT_MESSAGE_REGEXP.matcher(resultCountMessage);
		if (matcher.find()) {
			return Long.parseLong(matcher.group(1).replaceAll("[.,]", ""));
		} else {
			matcher = CRITERIA_DATATABLE_HEAD_COUNT_MESSAGE_REGEXP.matcher(resultCountMessage);
			if (matcher.find()) {
				return Long.parseLong(matcher.group(1).replaceAll("[.,]", ""));
			}
		}
		return null;
	}

	protected boolean getDisabled(WebElement btn) {
		return btn.getAttribute("class").contains("ui-state-disabled");
	}

	protected boolean clickDatatableNextPage(String datatableId) {
		WebElement btn = getChromeDriver().findElement(By.xpath("//div[@id='" + datatableId + "']//span[contains(@class,'ui-paginator-next')]"));
		if (btn != null && !getDisabled(btn)) {
			info("loading next page of datatable: " + datatableId);
			btn.click();
			waitForAjax();
			return true;
		}
		return false;
	}

	protected boolean clickDatagridNextPage(String datagridId) {
		WebElement btn = getChromeDriver().findElement(By.xpath("//table[@id='" + datagridId + "']//span[contains(@class,'ui-icon-seek-next')]"));
		if (btn != null && !getDisabled(btn)) {
			info("loading next page of data grid: " + datagridId);
			btn.click();
			waitForAjax();
			return true;
		}
		return false;
	}

	protected Set<Long> getDatatableRowIds(String datatableId) {
		LinkedHashSet<Long> result = new LinkedHashSet<Long>();
		Iterator<String> it = getDatatableColumnValues(datatableId, "ID").iterator();
		while (it.hasNext()) {
			result.add(Long.parseLong(it.next()));
		}
		return result;
	}

	protected ArrayList<String> getDatatableColumnValues(String datatableId, String columnName) {
		ArrayList<String> result = new ArrayList<String>();
		info("fetch '" + columnName + "' column values of datatable: " + datatableId);
		Iterator<WebElement> columnIt = getChromeDriver().findElements(By.xpath("//div[@id='" + datatableId + "']//thead//tr[@role='row']//th")).iterator();
		Integer index = null;
		int i = 0;
		int j = 0;
		while (columnIt.hasNext()) {
			if (columnName.equals(columnIt.next().getText())) {
				index = i;
				debug("column '" + columnName + "' found at index " + index);
				break;
			}
			i++;
		}
		if (index != null) {
			Iterator<WebElement> it = getChromeDriver().findElements(By.xpath("//tbody[@id='" + datatableId + "_data']/tr")).iterator();
			while (it.hasNext()) {
				columnIt = it.next().findElements(By.xpath(".//td")).iterator();
				String val = null;
				for (i = 0; i <= index; i++) {
					val = columnIt.next().getText();
					if (i == 0 && j == 0 && val.toLowerCase().contains(NO_RECORDS_LABEL)) {
						return result;
					}
				}
				result.add(val);
				debug("datatable " + datatableId + " column " + index + " value: " + val);
				j++;
			}
			info("page " + getChromeDriver().findElement(By.xpath("//th[@id='" + datatableId + "_paginator_top']/span")).getText().replace("(", "").replace(")", "") + ": "
					+ result.toString());
		} else {
			error("column '" + columnName + "' not found");
		}
		return result;
	}

	@Override
	public void onFinish(ITestContext context) {
		info("finish test class '" + this.getClass().getCanonicalName() + "'");
		//		try {
		//			getScreenRecorder().stop();
		//		} catch (HeadlessException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (AWTException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}

	private void attachReports(boolean delete) throws Throwable {
		File[] files = (new File(getTestDirectory())).listFiles();
		Compress zip = null;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				if (CommonUtil.getMimeType(files[i]).equals(CommonUtil.HTML_MIMETYPE_STRING)) {
					if (!"index.html".equals(files[i].getName().toLowerCase())) {
						File pdfFile = new File((new FilePathSplitter(files[i].getCanonicalPath())).joinFilePath("{0}." + CoreUtil.PDF_FILENAME_EXTENSION));
						String[] command = new String[HTMLTOPDF_COMMAND.length + 2];
						for (int j = 0; j < HTMLTOPDF_COMMAND.length; j++) {
							command[j] = HTMLTOPDF_COMMAND[j];
						}
						command[command.length - 2] = files[i].getCanonicalPath();
						command[command.length - 1] = pdfFile.getCanonicalPath();
						info(String.join(" ", command));
						CoreUtil.runProcess(true, command);
						getReportEmailSender().addEmailAttachment(pdfFile, CoreUtil.PDF_MIMETYPE_STRING, pdfFile.getName());
						reportFiles.add(pdfFile);
						if (delete) {
							pdfFile.deleteOnExit();
						}
					}
				} else if (CommonUtil.getMimeType(files[i]).equals(CoreUtil.PDF_MIMETYPE_STRING)) {
					if (zip == null) {
						zip = new Compress();
					}
					zip.addFile(getTestDirectory(), files[i]);
					//getReportEmailSender().addEmailAttachment(files[i], CoreUtil.PDF_MIMETYPE_STRING, files[i].getName());
				}
			}
			if (delete) {
				files[i].deleteOnExit();
			}
		}
		if (zip != null) {
			String zipFilename = System.getProperty("ctsms.test.name") + ".zip";
			getReportEmailSender().addEmailAttachment(zip.zipFiles(getTestDirectory()), Compress.ZIP_MIMETYPE_STRING, zipFilename);
			File zipFile = new File(getTestDirectory(), zipFilename);
			zip.zipFiles(getTestDirectory(), zipFile.getCanonicalPath());
			reportFiles.add(zipFile);
			zipFile.deleteOnExit();
		}
		//getReportEmailSender().addEmailAttachment((new Compress()).zipDirectory(getTestDirectory()), Compress.ZIP_MIMETYPE_STRING, "test_results.zip");
	}
}
