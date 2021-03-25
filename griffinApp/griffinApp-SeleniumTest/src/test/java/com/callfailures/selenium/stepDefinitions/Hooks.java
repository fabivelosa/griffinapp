package com.callfailures.selenium.stepDefinitions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
	public static WebDriver driver;
	public static final String ROOT_DIRECTORY = System.getProperty("user.dir");
	public static final String TEST_UPLOAD_PAGE = "http://localhost:8080/callfailures/upload.html";
	public static final String NETWORK_ENGINEER_PAGE = "http://localhost:8080/callfailures/networkEngineer.html";
	public static final String CUSTOMER_SUPPORT_REP_PAGE = "http://localhost:8080/callfailures/customerSupport.html";
	public static final String SYSTEM_ADMIN_PAGE = "http://localhost:8080/callfailures/sysadmin.html";
	
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
		Hooks.driver.get(SYSTEM_ADMIN_PAGE);
	}
	
	@Before("@NetworkEngineer")
	public void loginSetupForNetworkEngineer() {
		Hooks.driver.get(NETWORK_ENGINEER_PAGE);
	}
	
	@Before("@CustomerSupportRep")
	public void loginSetupForCustomerSupportRep() {
		Hooks.driver.get(CUSTOMER_SUPPORT_REP_PAGE);
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
}
