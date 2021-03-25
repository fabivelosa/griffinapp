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
	
	private static final String PAGE_TITLE = "System Admin";
	private static final String DOWNLOAD_NAME  ="Error Logs.txt";

	//Will need to be changed per device running test
	private static String fileDownloadpath = "C:\\Users\\Peter\\Downloads";
	
	@Given("^The Administrator is on the upload page$")
	public void the_Administrator_is_on_the_upload_page() throws Throwable {
		assertEquals(PAGE_TITLE, Hooks.driver.getTitle());
	}

	@Given("^Admin has uploads \"([^\"]*)\"$")
	public void admin_has_uploads(final String filePath) throws Throwable {
		assertNotNull(Hooks.driver.findElement(By.id("uploadFile")));
		final String fileLocation = Hooks.ROOT_DIRECTORY + filePath;
		
		Thread.sleep(500);
		Hooks.driver.findElement(By.id("uploadFile")).sendKeys(fileLocation);
		Thread.sleep(500);
		Hooks.driver.findElement(By.id("uploadBtn")).click();	
	}

	@When("^The Upload is complete$")
	public void the_Upload_is_complete() throws Throwable {
		final List<WebElement> errors = Hooks.driver.findElements(By.xpath("/html/body/main/div[2]/ul[1]"));
	    
		assertNotNull(Hooks.driver.findElement(By.id("errorBtn")));
	}

	@When("^The Admin clicks download report$")
	public void the_Admin_clicks_download_report() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
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
