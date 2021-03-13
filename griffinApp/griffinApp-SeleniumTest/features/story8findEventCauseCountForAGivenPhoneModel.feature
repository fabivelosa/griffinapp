#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@FindEventCauseCountsForAGivenPhoneModel @NetworkEngineer
Feature: As a Network Management Engineer  
	I want to see, for a given model of phone, all the unique failure Event Id and Cause Code combinations they have exhibited and the number of occurrences.
	So I can determine common network issues associated with a device model.

  Scenario Outline: Retrieval of Unique EventID, Cause Code and Count by Phone Model
    Given The network engineer is on the  main page
    When The network engineer selected the Phone Model "<tac>" from the dropdown
    And The network engineer clicked on the Search button
		Then The correct "<EventID", "<CauseCode>" and "<Count>" are displayed in the table

    Examples: 
      | tac				| EventID  | CauseCode | Count     |
      | 21060800  |   4098   |     0 		 | 60        | 

      
      
       