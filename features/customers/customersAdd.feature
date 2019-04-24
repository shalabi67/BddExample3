@customer @createCustomer
Feature: Add Customer
  Adding Customer.

  Background:
    Given Customers system is started

  @main
  Scenario Outline: Create stylist
    When user provides valid customer information to create a valid customer <firstName>, <lastName>, and <email>
    Then system creates customer
    Examples:
      | firstName | lastName | email            |
      | nana      | smith    | smith@go.com     |
      | lana      | dora     | lana.dora@aa.de  |
      | kamal     | dora     | kamal.dora@aa.de |


  #TODO: add more test cases to email validation
  @alternative
  Scenario Outline: Create customer with an invalid email
    When user provides invalid email with customer information to create a customer <firstName>, <lastName>, and <email>
    Then system returns invalid email information
    Examples:
      | firstName | lastName | email            |
      | hana      | smith    | hana.go.com      |
      | lana      | dora     | lana             |
      | kamal     | dora     | kamal.dora@aa    |
      | kamal     | dora     | 123              |
      | kamal     | smith    | smith@foo_com    |
      | kamal     | smith    | smith@foo@com.de |
      | kamal     | smith    | smith@.com       |


  @alternative
  Scenario Outline: Create Customer with an exiting email
    When user provides exiting email with customer information to create a customer <firstName>, <lastName>, and <email>
    Then system returns exiting customer email information
    Examples:
      | firstName | lastName | email                  |
      | nana      | smith    | smithExisit@go.com     |
      | lana      | dora     | lana.Exist@aa.de       |
      | kamal     | dora     | kamal.Exist@aa.de      |
