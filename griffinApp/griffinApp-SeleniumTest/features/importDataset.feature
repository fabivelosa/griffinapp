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
    Then The valid rows message "<validRowCount>" is displayed
    And The invalid rows message "<invalidRowCount>" is displayed

    Examples: 
      | fileName  																				  		| userName | password | validRowCount | invalidRowCount |
      | /resources/testdatasets/mini_dataset.xlsx 							| lisa1		 | Admin123 |		505					|			715					|
      | /resources/testdatasets/mini_set_one_row.xlsx 					| lisa1		 | Admin123 |		497					|			697					|
      | /resources/testdatasets/full_dataset.xlsx 							| lisa1		 | Admin123 |			0					|				0					|
                 