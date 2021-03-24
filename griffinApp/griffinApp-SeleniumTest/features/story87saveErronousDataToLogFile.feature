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
      | fileName             																	 |
      | /resources/testdatasets/full_dataset.xlsx 						 |
			| /resources/testdatasets/mini_dataset.xlsx 						 |	
      