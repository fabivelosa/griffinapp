#Author: Dan Fleming <A00279030@student.ait.ie>
@CreateUser @SystemAdmin
Feature: As the System Administrator, 
	I want to be able to assign credentials to each User type (i.e. Customer Service Rep., Support Engineer, Network Management Engineer)
	So that the user details are stored.

  Scenario Outline: Valid User Creation
    Given The admin is in the Create User page
    When The admin enters the "<userId>", "<userName>", "<userType>", "<userPassword>" and "<confirmPassword>"
    And The admin clicks "Submit"
    Then The new user will be stored to the database
    
	 Examples: 
      | userId  	| userName | userType   | userPassword | confirmPassword |
      | a1				|	test		 | SYSADMIN		|	verify			 | verify					 |
    


