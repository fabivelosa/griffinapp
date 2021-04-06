package com.callfailures.selenium.stepDefinitions;

import org.openqa.selenium.By;
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
	public static final String LOGIN_PAGE = "http://localhost:8080/callfailures/login.html";
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
		Hooks.driver.get(LOGIN_PAGE);
	}
	
	@Before("@NetworkEngineer")
	public void networkEngineerSetUp() {
		Hooks.driver.get(LOGIN_PAGE);
		Hooks.driver.findElement(By.id("InputUsername")).sendKeys("alex");
		Hooks.driver.findElement(By.id("InputPassword")).sendKeys("1234");
		Hooks.driver.findElement(By.id("loginBtn")).click();
	}
	
	@Before("@CustomerSupportRep")
	public void customerSupportSetUp() {
		Hooks.driver.get(LOGIN_PAGE);
		Hooks.driver.findElement(By.id("InputUsername")).sendKeys("joe");
		Hooks.driver.findElement(By.id("InputPassword")).sendKeys("1234");
		Hooks.driver.findElement(By.id("loginBtn")).click();
	}
	
	@Before("@SupportEngineer")
	public void supportEngineerSetUp() {
		Hooks.driver.get(LOGIN_PAGE);
		Hooks.driver.findElement(By.id("InputUsername")).sendKeys("bonnie");
		Hooks.driver.findElement(By.id("InputPassword")).sendKeys("1234");
		Hooks.driver.findElement(By.id("loginBtn")).click();
	}
	
	
	@After
	public void tearDown() {
		driver.quit();
	}
}
