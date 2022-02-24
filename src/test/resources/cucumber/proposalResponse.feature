Feature: Proposal Response
  Scenario: Clicking Availability for Event
    Given I am on the proposal response page
    And I click yes
    Then the button corresponding to yes should be clicked

  Scenario: Clicking Excitement for Event
    Given I am on the proposal response page
    And I click 1 in the excitement menu
    Then One should be selected for excitement
