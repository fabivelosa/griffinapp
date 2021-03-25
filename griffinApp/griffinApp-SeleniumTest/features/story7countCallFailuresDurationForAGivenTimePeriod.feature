#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@CountFailuresAndDuration @NetworkEngineer
Feature: As a Network Engineer
	I want to count, for each IMSI, the number of call failures and their total duration during a given time period	
	So I can confirm the call failure issue escalated and determine its severity.

  Scenario Outline: Retrieval of Count of call failures and total duration in a given time period by IMSI
    Given The network engineer is on the  main page
    When The network engineer inputted the IMSI "<imsi>"
    When The network engineer selected the from datetime "<fromDate>" "<fromTime>"from the datepicker
    When The network engineer selected the to datetime "<toDate>" "<toTime>" from the datepicker
    And The network engineer clicked on the IMSI Diagnostics Summary Search button
    Then The call failure count "<count>" and total duration "<totalDuration>" is displayed

    Examples: 
      | fromDate		| fromTime | toDate		  | toTime     | imsi 			     | count | totalDuration  		 |
      | 11/01/2020  |    1500  | 11/01/2021 | 1630       | 344930000000011 | 486   |  486 s     		 |
      | 11/02/2020  |    1500  | 11/02/2021 | 1630       | 344930000000011 | 0     |  0 s     		 |
      
      
   Scenario Outline: Retrieval of Count of call failures and total duration in a given time period by IMSI
    Given The network engineer is on the  main page
    When The network engineer inputted the IMSI "<imsi>"
    When The network engineer selected the from datetime "<fromDate>" "<fromTime>"from the datepicker
    When The network engineer selected the to datetime "<toDate>" "<toTime>" from the datepicker
    And The network engineer clicked on the IMSI Diagnostics Summary Search button
    Then The invalid IMSI "<message>" is displayed
    
       Examples: 
      | fromDate		| fromTime | toDate		  | toTime     | imsi 			     | count | message  		 |
      | 11/01/2020  |    1500  | 11/01/2021 | 1630       | A44930000000011 | 0     |  The IMSI provided is invalid     |
      
      
      
       