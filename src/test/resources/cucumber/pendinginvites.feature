Feature: Responding to a finalized invite
  Scenario: Responding "Yes"
    Given I am on the Pending Invites page
    When I click the check mark
    Then I should see the alert Accepted Invite

  Scenario: Responding "No"
    Given I am on the Pending Invites page
    When I click the cross mark
    Then I should see the alert Declined Invite