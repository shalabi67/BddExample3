package com.bdd.tests.unit;

import com.bdd.slots.TimeSlot;
import com.bdd.slots.TimeSlotController;
import com.bdd.slots.TimeSlotData;
import com.bdd.tests.factory.TimeSlotSystem;
import com.bdd.tests.mocks.QueueProcessorMock;
import com.bdd.tests.mocks.TimeSlotMock;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public class UnitTimeSlotSystem extends TimeSlotSystem {
    private TimeSlotMock timeSlotMock = new TimeSlotMock();

    public UnitTimeSlotSystem() {
        QueueProcessorMock.create(timeSlotMock);
    }

    @Override
    public ResponseEntity addTimeSlots(TimeSlotData timeSlotData) {
        TimeSlotController timeSlotController = timeSlotMock.addTimeSlotsMock();
        return timeSlotController.addTimeSlots(timeSlotData);
    }

    @Override
    public ResponseEntity<Set<TimeSlot>> getAvailableTimeSlots() {
        TimeSlotController timeSlotController = timeSlotMock.createGetAvailableTimeSlots();
        return timeSlotController.getAvailableTimeSlots();
    }

    @Override
    public void setInitialized() {
        isInitialized = false;
    }
}
