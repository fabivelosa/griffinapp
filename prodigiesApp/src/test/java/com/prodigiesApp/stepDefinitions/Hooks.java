package com.prodigiesApp.stepDefinitions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
	public static WebDriver driver;
	public static final String ROOT_DIRECTORY = System.getProperty("user.dir");
	public static final String TEST_UPLOAD_PAGE = "https://www.w3schools.com/howto/howto_html_file_upload_button.asp";
	
	
	@Before
	public void setup() {
		final String operatingSystem = System.getProperty("os.name");
		
		if(operatingSystem.startsWith("Windows")) {
			System.setProperty("webdriver.chrome.driver", ROOT_DIRECTORY + "/resources/chromedriver/chromedriver.exe");
		}else{
			System.setProperty("webdriver.chrome.driver", ROOT_DIRECTORY + "/resources/chromedriver/chromedriver");
		}
		driver = new ChromeDriver();
	}
	
	@Before("@SystemAdmin")
	public void loginSetup() {
		Hooks.driver.get(TEST_UPLOAD_PAGE);
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
}