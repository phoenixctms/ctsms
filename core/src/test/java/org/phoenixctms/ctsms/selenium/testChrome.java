package org.phoenixctms.ctsms.selenium;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

//https://tecadmin.net/setup-selenium-chromedriver-on-ubuntu/
public class testChrome {

	public static void main(String[] args) {
		//String pathToChromeDriver = "C:\\chromedriver_win32\\chromedriver.exe";
		//System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--no-sandbox");
		ChromeDriver Chrom1 = new ChromeDriver(chromeOptions);
		Chrom1.get("http://localhost:8080");
	}
}