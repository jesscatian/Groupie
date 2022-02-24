Feature: Sent proposals page
  Scenario: Finalizing a proposal
    Given I am on the sent proposals page
    When I click on the finalize proposals button
    Then I should see the alert is not empty