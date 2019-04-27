@appointments @addAppointment
Feature: Add Appointment
  Adding Stylist. when a stylist is added to the system this means more appointments slots will be available, please see appointments time slots.

  Background:
    Given Appointment system is started and system has three stylists with ids 1,2 and 3 and five customers with id 1,2,3,4,5

  @main
  Scenario Outline: Add appointment
    When user provides valid appointment information to add a valid appointment <date>, and <customerId>
    Then system creates an appointment
    Examples:
      | date             | customerId |
      | 2019-05-01 9:0   | 1          |
      | 2019-05-01 09:30 | 1          |
      | 2019-05-01 10:00 | 1          |
      | 2019-05-01 09:0  | 2          |
      | 2019-05-01 9:00  | 3          |


  @alternative
  Scenario Outline: Add appointment using invalid date
    When user provides invalid date information to add an appointment <date>, and <customerId>
    Then system returns error invalid date
    Examples:
      | date             | customerId |
      | 2019-05-01- 9:0  | 1          |
      | 2019/05/01 09:30 | 1          |
      | dsfgdsfg         | 1          |
     # | 2019-13-01 09:0  | 2          |  //TODO: bug in java date
     # | 2019-05-32 9:00  | 3          |
     # | 2019-04-31 9:0   | 1          |

  @alternative
  Scenario Outline: Add appointment using invalid customer
    When user provides invalid customer information to add an appointment <date>, and <customerId>
    Then system returns invalid customer
    Examples:
      | date             | customerId |
      | 2019-05-01 9:0   | -1         |
      | 2019-05-01 09:30 | 30000      |

  @alternative
  Scenario Outline: Add appointment for non available stylist
    Given system has one stylist and one appointments <date>, and <oldCustomerId>
    When no available stylist, user provides valid appointment information to add a valid appointment <date>, and <customerId>
    Then system notify stylist not available
    Examples:
      | date             | customerId | oldCustomerId |
      | 2019-05-01 9:0   | 2          | 1             |

  @alternative
  Scenario Outline: Add existing appointment
    #Given customer has an appointment and wants to create another one at the same time
    When user try to create an appointment he already has by providing <date>, and <customerId>
    Then system will return customer ahd this appointment
    Examples:
      | date              | customerId |
      | 2019-05-02 10:0   | 1          |

  @alternative
  Scenario Outline: customer tries to book an appointment for a time slot not available in the system
    When time slot customer <customerId> tries books one appointment on non existing time slot <startDate>
    Then system returns non exiting time slot
    Examples:
    #No time slots will exist before 2019-04-10 09:00
      | customerId | startDate        |
      | 1          | 2018-07-10 08:30 |
      | 1          | 2018-07-10 17:00 |
      | 1          | 2018-08-11 09:00 |