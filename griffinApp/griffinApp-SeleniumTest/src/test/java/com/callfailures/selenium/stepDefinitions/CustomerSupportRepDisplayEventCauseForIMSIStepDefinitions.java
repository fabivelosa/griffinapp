package com.callfailures.selenium.stepDefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CustomerSupportRepDisplayEventCauseForIMSIStepDefinitions {
	

	@When("^The Customer Support Rep selected the IMSI \"([^\"]*)\" from the dropdown$")
	public void the_Customer_Support_Rep_selected_the_IMSI_from_the_dropdown(String IMSI) throws Throwable {
		(new Select(Hooks.driver.findElement(By.id("selectUserEquipmentDropdown")))).selectByVisibleText(IMSI);
	}

	@When("^The Customer Support Rep clicked on the Call Failures Search button$")
	public void the_Customer_Support_Rep_clicked_on_the_Call_Failures_Search_button() throws Throwable {
		Hooks.driver.findElement(By.cssSelector("#userEquipmentFailures input[type='submit']")).click();
		Thread.sleep(4000);
	}
	
	@Then("^The correct value \"([^\"]*)\" is displayed in the table$")
	public void the_correct_value_is_displayed_in_the_table(String expectedMessage) throws Throwable {
		final String message = Hooks.driver.findElement(By.id("phoneFailuresTable_info")).getText();
		assertEquals(expectedMessage, message);
	}
	
}
