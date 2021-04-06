#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@ImportDataset @SystemAdmin
Feature: As the System Administrator, 
	I want to be able to receive and import Datasets
	So that the system has the data available to query and analyse.

  Scenario Outline: Upload Base Dataset Without Consistency Check
    Given The user is in the login page
    And the User enters "<userName>" in username field
    And the User enters "<password>" in the password field
    And The Choose File button is present
    When The user imported the file "<fileName>"
    And The user clicked on Upload
    Then The parsing message "<parseMessage>" is displayed

    Examples: 
      | fileName  																				  		| userName | password |
      | /resources/testdatasets/complete_dataset_zero_row.xlsx 	| lisa1		 | Admin123 |
      | /resources/testdatasets/complete_dataset_one_row.xlsx 	| lisa1		 | Admin123 |
      | /resources/testdatasets/complete_dataset.xlsx 					| lisa1		 | Admin123 |
                 