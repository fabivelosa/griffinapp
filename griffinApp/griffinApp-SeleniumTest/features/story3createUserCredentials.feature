@SystemAdmin
Feature: As the System Administrator, 
	I want to be able to assign credentials to each User type (i.e. Customer Service Rep., Support Engineer, Network Management Engineer)

	So that the user details are stored.

  Scenario Outline: Create valid User
    Given The admin is in the Add users page
    When The admin enters the user id, name, type and password to be added
    And The admin clicks "Save New User"
    Then The new user will be stored to the database
    

	Examples:
			|userId		|userName  		|userType				|userPassword		|confirmPassword
			|userA1		|named user		|System Admin		|verify					|verify

    
       