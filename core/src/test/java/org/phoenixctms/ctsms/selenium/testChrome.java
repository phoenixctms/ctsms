package org.phoenixctms.ctsms.selenium;

import org.openqa.selenium.chrome.ChromeDriver;

public class testChrome {

	public static void main(String[] args) {
		//String pathToChromeDriver = "C:\\chromedriver_win32\\chromedriver.exe";
		//System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
		ChromeDriver Chrom1 = new ChromeDriver();
		Chrom1.get("http://localhost:8080");
	}
}