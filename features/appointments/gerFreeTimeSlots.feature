@TimeSlot
Feature: Time slot
  This functionality allows the systems to identify free time slots.
  The available time slot is the time slot that has appointments less than the number of available stylist at that particular slot.

  Background:
    Given Time slot system had been started and system has one day time on 2019-07-10 slots or 16 time slots and 5 customers


  @main
  Scenario: Get available time slots
    When user requested to get available time slots when one stylist is defined.
    Then returns a list of available time slots which is 16

  @main
  Scenario: Get available time slots when no stylist exist
    When time slots are available and no stylist is defined in the system
    Then system returns empty list

  @main
  Scenario: Add a stylist to system and ask for available time slots
    When all time slots are booked then a stylist had been added to the systems
    Then All time slots will be available 16

  @main
  Scenario Outline: One stylist is defined in the system and customer books different appointments
    When customer <customerId> books one appointment on <startDate>
    Then system returns a list of available time slots 15
    Examples:
      | customerId | startDate        |
      | 1          | 2019-07-10 09:00 |
      | 2          | 2019-07-10 16:30 |
      | 3          | 2019-07-10 12:00 |
      | 1          | 2019-07-10 12:30 |

  @main
  Scenario Outline: Two stylist are available and customer books different appointments
    When customer <customerId> books one appointment on <startDate> one stylist will be available
    Then system returns 16 available time slots
    Examples:
      | customerId | startDate        |
      | 1          | 2019-07-10 09:00 |
      | 2          | 2019-07-10 16:30 |
      | 3          | 2019-07-10 12:00 |
      | 1          | 2019-07-10 12:30 |

  @alternative
  Scenario Outline: customer tries to book an appointment for a time slot not available in the system
    When customer <customerId> tries books one appointment on non existing time slot <startDate>
    Then system returns non exiting time slot
    Examples:
      | customerId | startDate        |
      | 1          | 2019-07-10 08:30 |
      | 2          | 2019-07-10 17:00 |
      | 3          | 2019-08-11 09:00 |

