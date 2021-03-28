package com.callfailures.selenium.stepDefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NetworkEngineerFindUniqueEventCauseForPhoneModelStepDefinition {
	@When("^The network engineer selected the Phone Model \"([^\"]*)\" from the dropdown$")
	public void the_network_engineer_selected_the_Phone_Model_from_the_dropdown(String phoneModel) throws Throwable {
		(new Select(Hooks.driver.findElement(By.id("selectUserEquipmentDropdown")))).selectByVisibleText(phoneModel);
	}
	
	@When("^The network engineer clicked on the User Equipment Failures Search button$")
	public void the_network_engineer_clicked_on_the_User_Equipment_Failures_Search_button() throws Throwable {
		Hooks.driver.findElement(By.cssSelector("#userEquipmentFailures input[type='submit']")).click();
		Thread.sleep(4000);
	}

	@Then("^The correct \"([^\"]*)\" is displayed in the table$")
	public void the_correct_is_displayed_in_the_table(String expectedMessage) throws Throwable {
		final String message = Hooks.driver.findElement(By.id("phoneFailuresTable_info")).getText();
		assertEquals(expectedMessage, message);
	}
}
