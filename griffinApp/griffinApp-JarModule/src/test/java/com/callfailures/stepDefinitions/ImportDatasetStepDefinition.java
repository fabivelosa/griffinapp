package com.callfailures.stepDefinitions;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ImportDatasetStepDefinition {
	private static final String PAGE_TITLE = "Import Dataset";

	@Given("^The admin is in the importDataset page$")
	public void the_admin_is_in_the_importDataset_page() throws Throwable {	
		assertEquals(PAGE_TITLE, Hooks.driver.getTitle());
	}

	@Given("^The Choose File button is present$")
	public void the_Choose_File_button_is_present() throws Throwable {
		assertNotNull(Hooks.driver.findElement(By.id("myFile")));
	}

	@When("^The user imported the file \"([^\"]*)\"$")
	public void the_user_imported_the_file(final String fileLocation) throws Throwable {
		final String filePath = Hooks.ROOT_DIRECTORY + fileLocation;
				
		Thread.sleep(1000);
		
		Hooks.driver.findElement(By.id("myFile")).sendKeys(filePath);
		
		Thread.sleep(2000);
	}

	@When("^The user clicked on Upload$")
	public void the_user_clicked_on_Upload() throws Throwable {
		Hooks.driver.findElement(By.id("uploadBtn")).click();	
	}

	@Then("^The parsing message \"([^\"]*)\" is displayed$")
	public void the_parsing_message_is_displayed(final String expectedMessage) throws Throwable {
		final String actualMessage = Hooks.driver.findElement(By.id("parsingMessage")).getAttribute("innerText");
		
		assertEquals(expectedMessage, actualMessage);
	}

	@Then("^The storage message \"([^\"]*)\" is displayed$")
	public void the_storage_message_is_displayed(final String expectedMessage) throws Throwable {
		final String actualMessage = Hooks.driver.findElement(By.id("storageMessage")).getAttribute("innerText");
		
		assertEquals(actualMessage, expectedMessage);
	}
}
