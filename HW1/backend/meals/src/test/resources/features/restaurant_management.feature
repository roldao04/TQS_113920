Feature: Restaurant Management
  As a restaurant manager
  I want to manage restaurant information
  So that customers can view and book meals

  Background:
    Given the restaurant database is empty

  Scenario: List all restaurants when none exist
    When I request all restaurants
    Then I should receive an empty list
    And the response status should be 200

  Scenario: Create a new restaurant
    When I create a restaurant with the following details:
      | name           | Moliceiro Restaurant        |
      | description   | Traditional Portuguese food |
      | address       | Aveiro Center              |
      | phoneNumber   | +351123456789             |
    Then the restaurant should be created successfully
    And the response status should be 201
    And the restaurant details should match the input

  Scenario: Get restaurant by ID
    Given a restaurant exists with the following details:
      | name           | Campus Cafe         |
      | description   | Student meals       |
      | address       | University Campus   |
      | phoneNumber   | +351987654321      |
    When I request the restaurant by its ID
    Then I should receive the restaurant details
    And the response status should be 200

  Scenario: Update restaurant details
    Given a restaurant exists with the following details:
      | name           | Old Restaurant     |
      | description   | Old Description    |
      | address       | Old Address       |
      | phoneNumber   | +351111111111    |
    When I update the restaurant with the following details:
      | name           | Updated Restaurant  |
      | description   | Updated Description |
      | address       | Updated Address    |
      | phoneNumber   | +351999999999     |
    Then the restaurant should be updated successfully
    And the response status should be 200
    And the restaurant details should be updated

  Scenario: Delete a restaurant
    Given a restaurant exists with the following details:
      | name           | Restaurant to Delete |
      | description   | To be deleted       |
      | address       | Delete Address      |
      | phoneNumber   | +351777777777      |
    When I delete the restaurant
    Then the restaurant should be deleted successfully
    And the response status should be 204
    And the restaurant should not exist anymore 