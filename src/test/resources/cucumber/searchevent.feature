Feature: SearchEvent
  Scenario: Logout due to inactive
    Given I am logged in
    And I enter keywords
    Then After timeout I should see the alert Signed out

  Scenario: Invalid Search
    Given I am logged in
    And I click search
    Then I should see an error at the bottom of the screen

  Scenario: Search Dates
    Given I am logged in
    And I enter keywords
    And I specify a date range
    And I click search
    Then I should see results matching my query

  Scenario: Search Location
    Given I am logged in
    And I enter keywords
    And I specify a location
    And I click search
    Then I should see results matching my query

  Scenario: Empty Search Results
    Given I am logged in
    And I enter keywords
    And I specify my location as North Korea
    And I click search
    Then I should see no results