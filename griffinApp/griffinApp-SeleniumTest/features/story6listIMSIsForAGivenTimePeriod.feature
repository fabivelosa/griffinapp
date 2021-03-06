#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@ListIMSIBYDate @SupportEngineer
Feature: As a Support Engineer 
	I want to count, for a given model of phone, the number of call failures it has had during a given time period.
	So I can determine common network issues associated with a device model.

  Scenario Outline: Retrieval of IMSIs by Date
    Given The support engineer is on the  main page
    And The date dropdown is visible
    When The support engineer selected the date "<date>" from the dropdown
    And The support engineer clicked on the Search button
    Then The message for IMSI count "<message>" is displayed
    And The table listing the call failures is displayed

    Examples: 
      | Date 						| message                |
      | 11/01/2020    	| 6 distinct IMSIs found |
      | 11/02/2020      | 0 IMSI found           |
      
       