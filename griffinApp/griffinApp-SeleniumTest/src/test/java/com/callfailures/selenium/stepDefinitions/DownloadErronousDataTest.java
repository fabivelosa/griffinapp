package com.callfailures.selenium.stepDefinitions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DownloadErronousDataTest {
	
	private static final String LOGIN_PAGE_TITLE = "Welcome Back - Login";
	private static final String ADMIN_PAGE_TITLE = "System Admin";
	private static final String DOWNLOAD_NAME  ="Error Logs.txt";
	private static final String USER_NAME  ="alex";
	private static final String PASSWORD  ="1234";

	//Will need to be changed per device running test
	private static String fileDownloadpath = "C:\\Users\\Peter\\Downloads";
	
	@Given("^The Administrator is on the login page$")
	public void the_Administrator_is_on_the_login_page() throws Throwable {
	    
		assertEquals(LOGIN_PAGE_TITLE, Hooks.driver.getTitle());
	}

	@Given("^Admin enters \"([^\"]*)\" in usernameInput$")
	public void admin_enters_in_usernameInput(String userName) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Hooks.driver.findElement(By.id("InputUsername")).sendKeys(userName);
	}

	@Given("^Admin enters \"([^\"]*)\" in passwordInput$")
	public void admin_enters_in_passwordInput(String password) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Hooks.driver.findElement(By.id("InputPassword")).sendKeys(password);
		Hooks.driver.findElement(By.id("loginBtn")).click();
	}

	@When("^The Admins uploads \"([^\"]*)\"$")
	public void the_Admins_uploads(String filePath) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Thread.sleep(1000);
		assertNotNull(Hooks.driver.findElement(By.id("uploadFile")));
		final String fileLocation = Hooks.ROOT_DIRECTORY + filePath;
		
		Thread.sleep(500);
		Hooks.driver.findElement(By.id("uploadFile")).sendKeys(fileLocation);
		Thread.sleep(500);
		Hooks.driver.findElement(By.id("uploadBtn")).click();	
	}

	@When("^The Admin clicks download report$")
	public void the_Admin_clicks_download_report() throws Throwable {
		assertNotNull(Hooks.driver.findElement(By.id("errorBtn")));
		Thread.sleep(10000);
		WebElement errorDownloadBtn =  Hooks.driver.findElement(By.id("errorBtn"));
		Thread.sleep(500);
		errorDownloadBtn.click();
		Thread.sleep(5000);
		
	}

	@Then("^The error log file is downloaded$")
	public void the_error_log_file_is_downloaded() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		assertTrue(isFileDownloaded(fileDownloadpath, DOWNLOAD_NAME), "Failed to download Expected document");
	}

	@After
	public void clean_up_file() {
		File testErrorLogs = new File(fileDownloadpath + "/" +DOWNLOAD_NAME);
		testErrorLogs.delete();
	}
	
	// Check the downloaded file in the download folder
	public boolean isFileDownloaded(String fileDownloadpath, String fileName) {
	boolean flag = false;
	File directory = new File(fileDownloadpath);
	File[] content = directory.listFiles();
	 for (int i = 0; i < content.length; i++) {
		 if (content[i].getName().equals(fileName))
		 return flag=true;
	  	}
	return flag;
	 }
	
	 

}
