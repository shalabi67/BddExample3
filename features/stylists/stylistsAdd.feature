@stylists @createStylist
Feature: Add Stylist
  Adding Stylist. when a stylist is added to the system this means more appointments slots will be available, please see appointments time slots.

  Background:
    Given Stylists system is started

  @main
  Scenario Outline: Create stylist
    When user provides valid stylists information to create a valid stylist <firstName>, <lastName>, and <email>
    Then system creates stylists
    Examples:
      | firstName | lastName | email            |
      | nana      | smith    | smith@go.com     |
      | lana      | dora     | lana.dora@aa.de  |
      | kamal     | dora     | kamal.dora@aa.de |


  #TODO: add more test cases to email validation
  @alternative
  Scenario Outline: Create stylist with an invalid email
    When user provides invalid email with stylist information to create a stylist <firstName>, <lastName>, and <email>
    Then system returns invalid stylist email information
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
  Scenario Outline: Create Stylist with an exiting email
    When user provides exiting email with stylist information to create a stylist <firstName>, <lastName>, and <email>
    Then system returns exiting stylist email information
    Examples:
      | firstName | lastName | email                  |
      | nana      | smith    | smithExisit@go.com     |
      | lana      | dora     | lana.Exist@aa.de       |
      | kamal     | dora     | kamal.Exist@aa.de      |
