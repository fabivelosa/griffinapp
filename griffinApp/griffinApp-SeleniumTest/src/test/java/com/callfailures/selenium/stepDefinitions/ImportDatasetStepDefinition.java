package com.callfailures.selenium.stepDefinitions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ImportDatasetStepDefinition {
	private static final String LOGIN_PAGE_TITLE = "Welcome Back - Login";
	private static final String username = "lisa1";
	private static final String password = "Admin123";

	@Given("^The user is in the login page$")
	public void the_user_is_in_the_login_page() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		assertEquals(LOGIN_PAGE_TITLE, Hooks.driver.getTitle());
	}

	@Given("^the User enters \"([^\"]*)\" in username field$")
	public void the_User_enters_in_username_field(String username) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Hooks.driver.findElement(By.id("InputUsername")).sendKeys(username);
	}

	@Given("^the User enters \"([^\"]*)\" in the password field$")
	public void the_User_enters_in_the_password_field(String password) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Hooks.driver.findElement(By.id("InputPassword")).sendKeys(password);
		Thread.sleep(2000);
		Hooks.driver.findElement(By.id("loginBtn")).click();
	}

	@Given("^The Choose File button is present$")
	public void the_Choose_File_button_is_present() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Thread.sleep(1000);
		assertNotNull(Hooks.driver.findElement(By.id("uploadFile")));
	}

	@When("^The user imported the file \"([^\"]*)\"$")
	public void the_user_imported_the_file(String fileLocation) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		final String filePath = Hooks.ROOT_DIRECTORY + fileLocation;
		
		Thread.sleep(1000);
		
		Hooks.driver.findElement(By.id("uploadFile")).sendKeys(filePath);
		
		Thread.sleep(2000);
	}

	@When("^The user clicked on Upload$")
	public void the_user_clicked_on_Upload() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		Hooks.driver.findElement(By.id("uploadBtn")).click();
		Thread.sleep(3000);
	}

	@Then("^The parsing message \"([^\"]*)\" is displayed$")
	public void the_parsing_message_is_displayed(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	   
	}
	
	//Old

	@Then("^The storage message \"([^\"]*)\" is displayed$")
	public void the_storage_message_is_displayed(final String expectedMessage) throws Throwable {
		final String actualMessage = Hooks.driver.findElement(By.id("storageMessage")).getAttribute("innerText");
		
		assertEquals(actualMessage, expectedMessage);
	}
}
