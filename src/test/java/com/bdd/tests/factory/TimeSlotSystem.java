package com.bdd.tests.factory;

import com.bdd.slots.TimeSlot;
import com.bdd.slots.TimeSlotData;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Set;

public abstract class TimeSlotSystem {
    protected boolean isInitialized = false;

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
    public abstract ResponseEntity<Set<TimeSlot>> getAvailableTimeSlots();
    public abstract void setInitialized();

    public boolean isInitialized() {
        return isInitialized;
    }
}
