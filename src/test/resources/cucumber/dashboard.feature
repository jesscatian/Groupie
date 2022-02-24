Feature: Dashboard
  Scenario: Sending a proposal with no events
    Given I am logged in
    And I click next
    Then I should see a no events alert

  Scenario: Sending a proposal with no users
    Given I am logged in
    And I select an event
    And I click next
    And I click submit
    Then I should see a no users alert

  Scenario: Sending a proposal with no proposal name
    Given I am logged in
    And I select an event
    And I click next
    And I select an user
    And I click submit
    Then I should see a no proposal name alert

  Scenario: Sending a proposal
    Given I am logged in
    And I select an event
    And I click next
    And I select an user
    And I write a proposal name
    And I click submit
    Then I should see a success alert

#  Scenario: Sending a proposal to someone who has blocked me
#    Given I am logged in
#    And I select an event
#    And I click next
#    And I select an user that has blocked me
#    Then I should see a error alert
