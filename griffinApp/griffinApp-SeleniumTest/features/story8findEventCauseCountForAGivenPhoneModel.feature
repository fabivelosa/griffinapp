#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@FindEventCauseCountsForAGivenPhoneModel @NetworkEngineer
Feature: As a Network Management Engineer  
	I want to see, for a given model of phone, all the unique failure Event Id and Cause Code combinations they have exhibited and the number of occurrences.
	So I can determine common network issues associated with a device model.

  Scenario Outline: Retrieval of Unique EventID, Cause Code and Count by Phone Model
    Given The network engineer is on the  main page
    When The network engineer selected the Phone Model "<phoneModel>" from the dropdown
    And The network engineer clicked on the User Equipment Failures Search button
		Then The correct "<Count>" is displayed in the table

    Examples: 
      | phoneModel				| Count 													 |	
      | VEA3  				    |  Showing 1 to 10 of 19 entries   | 
 
      
      
       