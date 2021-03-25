#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@CountFailuresAndDuration @NetworkEngineer
Feature: As a Support Engineer 
	I want to count, for a given model of phone, the number of call failures it has had during a given time period.
	So I can determine common network issues associated with a device model.

  Scenario Outline: Retrieval of Count of call failures and total duration in a given time period by IMSI
    Given The network engineer is on the  main page
    And The date dropdown is visible
    And The IMSI dropdown is visible
    When The network engineer selected the date "<date>" from the dropdown
    When The network engineer selected the IMSI "<imsi>" from the dropdown
    And The network engineer clicked on the Search button
    Then The message for call failure count "<count>" and total duration "<totalDuration>" is displayed
    And The table listing the affected IMSIs is displayed

    Examples: 
      | Date 						| IMSI 									| count                | totalDuration  |
      | 11/01/2020    	| 344930000000011  			| 240 events found     | 4 minutes      | 
      | 11/01/2020      | A44930000000011    		| 0 event found        | Not applicable |
      | 11/02/2020      | 34493000000001        | 0 event found        | Not applicable |
      
       