#Author: Monisha Mani Munusamy <A00279057@student.ait.ie>
@CountEventFailuresByIMSI @CustomerSupportRep
Feature: As a Customer Support Rep
	I want to count, for each IMSI, the number of call failures during a given time period	
	So I can confirm the determine its severity.

  Scenario Outline: Retrieval of Count of call failures and total duration in a given time period by IMSI
    Given The Customer Support Rep is on the  main page
    When The Customer Support Rep inputted the IMSI "<imsi>"
    When The Customer Support Rep selected the from datetime "<fromDate>" "<fromTime>"from the datepicker
    When The Customer Support Rep selected the to datetime "<toDate>" "<toTime>" from the datepicker
    And The Customer Support Rep clicked on the IMSI Diagnostics Summary Search button
    Then The call failure count "<count>" is displayed

    Examples: 
      | fromDate		| fromTime | toDate		  | toTime     | imsi 			     | count |
      | 11/01/2020  |    1500  | 11/01/2021 | 1630       | 344930000000011 | 486   |
      | 11/02/2020  |    1500  | 11/02/2021 | 1630       | 344930000000011 | 0     |
      
      
  Scenario Outline: Retrieval of Count of call failures and total duration in a given time period by IMSI
    Given The Customer Support Rep is on the  main page
    When The Customer Support Rep inputted the IMSI "<imsi>"
    When The Customer Support Rep selected the from datetime "<fromDate>" "<fromTime>"from the datepicker
    When The Customer Support Rep selected the to datetime "<toDate>" "<toTime>" from the datepicker
    And The Customer Support Rep clicked on the IMSI Diagnostics Summary Search button
    Then The invalid IMSI "<message>" displayed
    
       Examples: 
      | fromDate		| fromTime | toDate		  | toTime     | imsi 			     | count | message  		 |
      | 11/01/2020  |    1500  | 11/01/2021 | 1630       | A44930000000011 | 0     |  The IMSI provided is invalid     |
      
      
      
       