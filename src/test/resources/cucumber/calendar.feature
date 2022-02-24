Feature: Calendar
  Scenario: An event shows up on both calendar and list
    Given User10 has a proposal
    And I am logged in to user10
    And I am on the calendar page
    Then I should see the proposal on both the calendar and the list

  Scenario: The sort functionality of the list works
    Given User10 has two proposals
    And I am logged in to user10
    And I am on the calendar page
    And I click the switch button
    And I change the sort option
    Then The order of the list should change

  Scenario: The finalized only option hides not finalized events
    Given User10 has a not finalized proposal
    And I am logged in to user10
    And I am on the calendar page
    And I click the switch button
    And I switch to show only finalized
    Then I shouldnt see the not finalized proposals events

  Scenario: The not finalized only option hides finalized events
    Given User10 has a finalized proposal
    And I am logged in to user10
    And I am on the calendar page
    And I click the switch button
    And I switch to show only not finalized
    Then I shouldnt see the finalized proposals events

  Scenario: The responded only option hides not responded events
    Given I am logged in to user10
    And User10 has an event without a response
    And I am on the calendar page
    And I click the switch button
    And I switch to show only not responded
    Then I shouldnt see the responded event

  Scenario: The not responded only option hides responded events
    Given I am logged in to user10
    And User10 has an event with a response
    And I am on the calendar page
    And I click the switch button
    And I switch to show only responded
    Then I shouldnt see the not responded event
