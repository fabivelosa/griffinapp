package com.callfailures.selenium.runners;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="features", 
				 glue="com.callfailures.selenium.stepDefinitions",
				 tags= {"@CountFailuresAndDuration, @FindEventCauseCountsForAGivenPhoneModel, @CountEventFailuresByIMSI, @FindEventCauseCodeForAGivenIMSI"})
public class SeleniumTestRunner {

}
