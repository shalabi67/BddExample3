package com.bdd.tests.factory;

import com.bdd.slots.TimeSlot;
import com.bdd.slots.TimeSlotData;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public abstract class TimeSlotSystem {
    public static TimeSlot create(Date startDate, int availableAppointments) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartDate(startDate);
        timeSlot.setActiveAppointments(availableAppointments);

        return timeSlot;
    }
    public static TimeSlotData createTimeSlotDate(String startDate) {
        TimeSlotData timeSlotData = new TimeSlotData();
        timeSlotData.setDays(2);
        timeSlotData.setStartDate(startDate);
        timeSlotData.setDuration(30);

        return timeSlotData;
    }

    public abstract ResponseEntity addTimeSlots(TimeSlotData timeSlotData);
}
