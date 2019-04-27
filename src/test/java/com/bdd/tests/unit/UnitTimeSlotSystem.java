package com.bdd.tests.unit;

import com.bdd.slots.TimeSlotData;
import com.bdd.tests.factory.TimeSlotSystem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnitTimeSlotSystem extends TimeSlotSystem {
    @Override
    public ResponseEntity addTimeSlots(TimeSlotData timeSlotData) {
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
