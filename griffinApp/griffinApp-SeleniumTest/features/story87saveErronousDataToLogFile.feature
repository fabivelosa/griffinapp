#Author: Peter Casey <A00278938@student.ait.ie>
@ImportDataset @SystemAdmin 
Feature: As an administrator
  I need to be able to get the erroneous data log 
  instead of it being displayed in the UI
  So, my UI view is cleaner

  
  Scenario Outline: Download of Errornous Log Data
    Given The Administrator is on the upload page
    And Admin has uploads "<fileName>"
    When The Upload is complete
    And The Admin clicks download report
    Then The error log file is downloaded

    Examples: 
      | fileName                                               | parseMessage                       | storageMessage    |
      | /resources/testdatasets/complete_dataset_zero_row.xlsx | Parsing Complete: Read 0 row.      | No new data saved |
      | /resources/testdatasets/complete_dataset_one_row.xlsx  | Parsing Complete: Read 1 row.      | Saved 1 row       |
      | /resources/testdatasets/complete_dataset.xlsx          | Parsing Complete: Read 1000 rows.  | Saved 1000 rows   |
      | /resources/testdatasets/empty_dataset.xlsx  