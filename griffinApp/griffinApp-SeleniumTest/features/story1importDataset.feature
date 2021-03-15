#Author: Wilmir Nicanor <A00278899@student.ait.ie>
@ImportDataset @SystemAdmin
Feature: As the System Administrator,
  I want to be able to receive and import Datasets
  So that the system has the data available to query and analyse.

  Scenario Outline: Upload Base Dataset Without Consistency Check
    Given The admin is in the importDataset page
    And The Choose File button is present
    When The user imported the file "<fileName>"
    And The user clicked on Upload
    Then The parsing message "<parseMessage>" is displayed
    And The storage message "<storageMessage>" is displayed

    Examples: 
      | fileName                                               | parseMessage                       | storageMessage    |
      | /resources/testdatasets/complete_dataset_zero_row.xlsx | Parsing Complete: Read 0 row.      | No new data saved |
      | /resources/testdatasets/complete_dataset_one_row.xlsx  | Parsing Complete: Read 1 row.      | Saved 1 row       |
      | /resources/testdatasets/complete_dataset.xlsx          | Parsing Complete: Read 1000 rows.  | Saved 1000 rows   |
      | /resources/testdatasets/empty_dataset.xlsx             | Parsing Failed: The file is empty. | No new data saved |
