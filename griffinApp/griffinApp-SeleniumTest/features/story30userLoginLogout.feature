#Author: Fabiane Velosa <A00267345@student.ait.ie>
@PermitAll
Feature: As a system user, I want to login into the system 
				 So that I can use the application and have access to 
				 the functionalities associated with my profile.

Scenario Outline: Login Successfull
    Given User is on Login Page
    When User enters "<username>" and "<password>" 
    And credentials entered are valid
    Then User category landing "<page>" is rendered.

Examples: 
        |username|password|page      |
        |sa0001|12345|sysadmin.html  |
        |se0001|12345|supporteng.html|
        |cs0001|12345|custserv.html  |
        |nm0001|12345|networkmng.html|

        
Scenario Outline: Logout Successfull
    Given User is logged in the system
    And user is on your role related page
    When user clicks LogOut button
    Then user is logged out succesfully
     And user login page is rendenred.


    