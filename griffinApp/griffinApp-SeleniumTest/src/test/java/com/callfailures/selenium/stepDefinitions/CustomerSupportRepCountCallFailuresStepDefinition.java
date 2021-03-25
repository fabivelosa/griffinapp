package com.callfailures.selenium.stepDefinitions;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CustomerSupportRepCountCallFailuresStepDefinition {
	@Given("^The Customer Support Rep is on the  main page$")
	public void the_customer_support_rep_is_on_the_main_page() throws Throwable {
		final String title = Hooks.driver.findElement(By.xpath("/html/body/div[1]/div/div/nav/ul/li[2]/a/span")).getText();
		assertEquals("Customer Service Rep", title);
	}
	
	@When("^The Customer Support Rep inputted the IMSI \"([^\"]*)\"$")
	public void the_customer_support_rep_inputted_the_IMSI(String imsi) throws Throwable {
		Hooks.driver.findElement(By.id("imsiOnIMSISummaryForm")).sendKeys(imsi);
	}

	@When("^The Customer Support Rep selected the from datetime \"([^\"]*)\" \"([^\"]*)\"from the datepicker$")
	public void the_customer_support_rep_selected_the_from_datetime_from_the_datepicker(String date, String time) throws Throwable {
		WebElement dateTimeInput = Hooks.driver.findElement(By.id("startDateOnIMSISummaryForm"));
		dateTimeInput.sendKeys(date);
		dateTimeInput.sendKeys(Keys.TAB);
		dateTimeInput.sendKeys(time);
	}

	@When("^The Customer Support Rep selected the to datetime \"([^\"]*)\" \"([^\"]*)\" from the datepicker$")
	public void the_customer_support_rep_selected_the_to_datetime_from_the_datepicker(String date, String time) throws Throwable {
		WebElement dateTimeInput = Hooks.driver.findElement(By.id("endDateOnIMSISummaryForm"));
		dateTimeInput.sendKeys(date);
		dateTimeInput.sendKeys(Keys.TAB);
		dateTimeInput.sendKeys(time);
	}
	
	@When("^The Customer Support Rep clicked on the IMSI Diagnostics Summary Search button$")
	public void the_customer_support_rep_clicked_on_the_IMSI_Diagnostics_Summary_Search_button() throws Throwable {
		Hooks.driver.findElement(By.cssSelector("#imsiSummaryForm input[type='submit']")).click();
		Thread.sleep(2000);
	}

	@Then("^The call failure count \"([^\"]*)\" is displayed$")
	public void the_call_failure_count_and_total_duration_is_displayed(String expectedCount) throws Throwable {
		final String count = Hooks.driver.findElement(By.id("imsiSummaryCallFailureCount")).getText();
		assertEquals(expectedCount, count);
	}
	
	@Then("^The invalid IMSI \"([^\"]*)\" displayed$")
	public void the_invalid_IMSI_displayed(String expectedMessage) throws Throwable {
		final String message = Hooks.driver.findElement(By.id("errorAlertOnSummaryForm")).getText();
		assertEquals(expectedMessage, message);
	}
	
}
