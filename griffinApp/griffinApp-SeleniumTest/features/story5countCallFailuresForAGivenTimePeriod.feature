#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@DisplayEventsByPhoneModelAndDate @SupportEngineer
Feature: As a Support Engineer 
	I want to count, for a given model of phone, the number of call failures it has had during a given time period.
	So I can determine common network issues associated with a device model.

  Scenario Outline: Retrieval of Events with Date and Phone Model
    Given The support engineer is on the  main page
    And The phone model name dropdown is visible
    And The date dropdown is visible
    When The support engineer selected the date "<date>" from the dropdown
    And The support engineer selected the phone model "<phonemodel>" from the dropdown
    And The support engineer clicked on the Search button
    Then The message for events count "<message>" is displayed
    And The table listing the call failures is displayed

    Examples: 
      | Phone Model 		| date                  | message                       |
      | VEA3    	      | 11/01/2020            | 303 call failure events found |
      | VEA3            | 11/01/2020            | 0 call failure events found   |
      | Dolphin 10K     | 11/01/2020            | 0 call failure events found   |