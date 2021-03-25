#Author: Peter Casey <A00278938@student.ait.ie>
@ImportDataset @SystemAdmin 
Feature: As an administrator
  I need to be able to get the erroneous data log 
  instead of it being displayed in the UI
  So, my UI view is cleaner

  
  Scenario Outline: Download of Errornous Log Data
    Given The Administrator is on the login page
    And Admin enters "<username>" in usernameInput
    And Admin enters "<password>" in passwordInput
    When The Admins uploads "<fileName>"
    And The Admin clicks download report
    Then The error log file is downloaded

    Examples: 
      | fileName             											| username | password|
      | /resources/testdatasets/full_dataset.xlsx | lisa	 | 1234  |
			| /resources/testdatasets/mini_dataset.xlsx |	lisa	 | 1234  |
      