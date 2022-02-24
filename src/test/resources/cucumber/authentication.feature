Feature: Authentication

  Scenario: Go to dashboard without login
    Given I am on the signup page for the first time
    And I visit dashboard.html
    Then I should be taken to the login page

  Scenario: Attempt to login and fail 3 times
    Given I am on the login page
    And I fill out the wrong password
    And I click on the log in button
    And I fill out the wrong password
    And I click on the log in button
    And I fill out the wrong password
    And I click on the log in button
    And I fill out the wrong password
    And I click on the log in button
    Then I should see an error at the bottom of the screen

  Scenario: Signup New Account
    Given I am on the signup page for the first time
    And I fill out my credentials
    And I click on the create account button
    Then I should be taken to the login page

  Scenario: Log In
    Given I am on the login page
    And I fill out my credentials
    And I click on the log in button
    Then I should be taken to the dashboard

  Scenario:  Blank SignUp
    Given I am on the signup page
    When I click on the create account button
    Then I should see an error at the bottom of the screen

  Scenario:  Blank Login
    Given I am on the login page
    When I click on the log in button
    Then I should see an error at the bottom of the screen

  Scenario: Invalid SignUp -- Existing Account
    Given I am on the signup page
    And I fill out my credentials
    And I click on the create account button
    Then I should see an error at the bottom of the screen

  Scenario: Invalid Login Wrong Password
    Given I am on the login page
    And I fill out the wrong password
    And I click on the log in button
    Then I should see an error at the bottom of the screen

  Scenario: Invalid Login Wrong Username
    Given I am on the login page
    And I fill out the wrong username
    And I click on the log in button
    Then I should see an error at the bottom of the screen

  Scenario: Invalid SignUp -- Mismatching Passwords
    Given I am on the signup page
    And I fill out my credentials
    And I change the confirm password field to not match my password
    And I click on the create account button
    Then I should see an error at the bottom of the screen

  Scenario: Cancel button works
    Given I am on the signup page
    And I click the cancel button
    Then I should be taken to the login page


