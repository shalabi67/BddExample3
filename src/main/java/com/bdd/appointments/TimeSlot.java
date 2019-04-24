package com.bdd.appointments;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class TimeSlot {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Date startDate;  //time slot start date and time
    private short duration;  // time slot duration
    /*
    number of appointments on this slot. this should be always less than or equal the number of stylists
    the time slot is considered available if it is less than the number of stylists.
     */
    private int activeAppointments;
}
