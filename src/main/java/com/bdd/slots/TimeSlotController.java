package com.bdd.slots;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(TimeSlotController.URL)
public class TimeSlotController {
    public static final String URL = "/slots";

    private TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PostMapping
    public ResponseEntity addTimeSlots(@RequestBody TimeSlotData timeSlotData) {
        return timeSlotService.addTimeSlots(timeSlotData);
    }

    //TODO: notice paging is not implemented for simplicity
    @GetMapping
    public ResponseEntity<Set<TimeSlot>> getAvailableTimeSlots() {
        return timeSlotService.getAvailableTimeSlots();
    }

}
