package com.callfailures.selenium.stepDefinitions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateUserStepsDefinition {
	private static final String PAGE_TITLE = "Dashboard";
	private static String id = "a1";
	private static String name = "test";
	private static String type = "Sysadmin";
	private static String passwd = "verify";
	private static String confirm = "verify";

	@Given("^The admin is in the Dashboard page$")
	public void the_admin_is_in_the_Dashboard_page() throws Throwable {	
		assertEquals(PAGE_TITLE, Hooks.driver.getTitle());
	}

	@When("^The admin enters valid credentials")
	public void admin_enters_valid_credentials() throws Throwable {
		
		Hooks.driver.findElement(By.id("userId")).sendKeys(id);
		Hooks.driver.findElement(By.id("userName")).sendKeys(name);
		Hooks.driver.findElement(By.id("userType")).sendKeys(type);
		Hooks.driver.findElement(By.id("userPassword")).sendKeys(passwd);
		Hooks.driver.findElement(By.id("confirmPassword")).sendKeys(confirm);

		
		Thread.sleep(2000);
	}

	@When("^The user clicked on Submit$")
	public void the_user_clicked_on_Submit() throws Throwable {
		Hooks.driver.findElement(By.id("createUserButton")).click();	
	}

	@Then("^The parsing message \"([^\"]*)\" is displayed$")
	public void the_parsing_message_is_displayed(final String expectedMessage) throws Throwable {
		final List<WebElement> buttons = Hooks.driver.findElements(By.xpath("/html/body/main/div[2]/ul[1]"));
		
	//	final String actualMessage = Hooks.driver.findElement(By.id("parsingMessage")).getAttribute("innerText");
		
		//assertEquals(expectedMessage, actualMessage);
	}

	@Then("^The storage message \"([^\"]*)\" is displayed$")
	public void the_storage_message_is_displayed(final String expectedMessage) throws Throwable {
		final String actualMessage = Hooks.driver.findElement(By.id("storageMessage")).getAttribute("innerText");
		
		assertEquals(actualMessage, expectedMessage);
	}
}
