Feature: Money Transfer
  As a banking system user
  I want to transfer money between accounts
  So that I can manage my finances effectively

  Background:
    Given the banking system is running
    And there are existing customers and accounts

  Scenario: Successful transfer between two accounts
    Given customer "Rajesh Kumar" has account "ACC123456" with balance "₹50000"
    And customer "Priya Sharma" has account "ACC123457" with balance "₹30000"
    When I transfer "₹10000" from "ACC123456" to "ACC123457"
    Then the transfer should be successful
    And account "ACC123456" should have balance "₹40000"
    And account "ACC123457" should have balance "₹40000"
    And the transaction should be recorded in audit logs

  Scenario: Transfer with insufficient funds
    Given customer "Amit Patel" has account "ACC123458" with balance "₹5000"
    And customer "Sneha Singh" has account "ACC123459" with balance "₹20000"
    When I transfer "₹10000" from "ACC123458" to "ACC123459"
    Then the transfer should fail with "Insufficient balance" error
    And account "ACC123458" should have balance "₹5000"
    And account "ACC123459" should have balance "₹20000"

  Scenario: Transfer to same account
    Given customer "Vikram Reddy" has account "ACC123460" with balance "₹25000"
    When I transfer "₹5000" from "ACC123460" to "ACC123460"
    Then the transfer should fail with "Cannot transfer to the same account" error
    And account "ACC123460" should have balance "₹25000"

  Scenario: Transfer with invalid account numbers
    Given customer "Anita Desai" has account "ACC123461" with balance "₹15000"
    When I transfer "₹5000" from "ACC123461" to "INVALID_ACC"
    Then the transfer should fail with "Destination account not found" error
    And account "ACC123461" should have balance "₹15000"

  Scenario: Transfer with zero amount
    Given customer "Ravi Joshi" has account "ACC123462" with balance "₹30000"
    And customer "Meera Iyer" has account "ACC123463" with balance "₹20000"
    When I transfer "₹0" from "ACC123462" to "ACC123463"
    Then the transfer should fail with "Transfer amount must be greater than zero" error
    And account "ACC123462" should have balance "₹30000"
    And account "ACC123463" should have balance "₹20000"

  Scenario: Transfer with negative amount
    Given customer "Kiran Nair" has account "ACC123464" with balance "₹40000"
    And customer "Arjun Menon" has account "ACC123465" with balance "₹25000"
    When I transfer "₹-1000" from "ACC123464" to "ACC123465"
    Then the transfer should fail with "Transfer amount must be greater than zero" error
    And account "ACC123464" should have balance "₹40000"
    And account "ACC123465" should have balance "₹25000"

  Scenario: Transfer with inactive account
    Given customer "Deepak Gupta" has account "ACC123466" with balance "₹35000" and status "INACTIVE"
    And customer "Sunita Agarwal" has account "ACC123467" with balance "₹18000"
    When I transfer "₹5000" from "ACC123466" to "ACC123467"
    Then the transfer should fail with "Source account is not active" error
    And account "ACC123466" should have balance "₹35000"
    And account "ACC123467" should have balance "₹18000"

  Scenario: Large amount transfer
    Given customer "Rajesh Kumar" has account "ACC123456" with balance "₹1000000"
    And customer "Priya Sharma" has account "ACC123457" with balance "₹500000"
    When I transfer "₹500000" from "ACC123456" to "ACC123457"
    Then the transfer should be successful
    And account "ACC123456" should have balance "₹500000"
    And account "ACC123457" should have balance "₹1000000"
    And the transaction should be recorded in audit logs

  Scenario: Multiple transfers in sequence
    Given customer "Amit Patel" has account "ACC123458" with balance "₹100000"
    And customer "Sneha Singh" has account "ACC123459" with balance "₹50000"
    When I transfer "₹20000" from "ACC123458" to "ACC123459"
    And I transfer "₹10000" from "ACC123459" to "ACC123458"
    Then both transfers should be successful
    And account "ACC123458" should have balance "₹90000"
    And account "ACC123459" should have balance "₹60000"
    And both transactions should be recorded in audit logs
