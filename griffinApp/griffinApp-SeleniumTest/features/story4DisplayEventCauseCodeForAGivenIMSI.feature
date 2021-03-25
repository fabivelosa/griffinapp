#Author: Monisha Mani Munusamy <A00279057@student.ait.ie>
@FindEventCauseCodeForAGivenIMSI @CustomerSupportRep
Feature: As a Customer Support Rep  
	I want to see, for a given IMSI, all the unique failure Event Id and Cause Code combinations they have exhibited.
	So I can determine common network issues associated with a IMSI.

  Scenario Outline: Retrieval of Unique EventID, Cause Code and Count by IMSI
    Given The Customer Support Rep is on the  main page
    When The Customer Support Rep selected the IMSI "<IMSI>" from the dropdown
    And The Customer Support Rep clicked on the Call Failures Search button
		Then The correct value "<Cause Code>" is displayed in the table

    Examples: 
      |IMSI	            |Cause Code|
			|344930000000011	|Showing 1 to 10 of 13 entries |

 
      
      
       