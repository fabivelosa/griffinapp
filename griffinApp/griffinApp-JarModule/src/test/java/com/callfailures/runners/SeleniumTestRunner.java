package com.callfailures.runners;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="features", 
				 glue="com.prodigiesApp.stepDefinitions",
				 tags= {"@ImportDataset"})
public class SeleniumTestRunner {

}
