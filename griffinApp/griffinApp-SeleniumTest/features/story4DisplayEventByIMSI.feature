#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@DisplayEventsByImsi @SupportEngineer
Feature: As Customer Service Rep. , 
	I want to display, for a given affected IMSI, the Event ID and Cause Code for any / all failures affecting that IMSI
	So that I can confirm the failure reported by the customer

  Scenario Outline: Retrieval of Events with Valid and Invalid IMSI 
    Given The customer service representative is on the  main page
    When The customer service representative typed in the "<IMSI>" in the search field
    And The customer service representative clicked on the Search button
    Then The message for event count "<message>" is displayed
    And The table listing the events affecting the IMSI is displayed

    Examples: 
      | IMSI 						| message          |
      | 344930000000011	| 240 events found |
      | 344930000000011 | 0 event found    |
      | A44930000000011 | 0 event found    |
     
       