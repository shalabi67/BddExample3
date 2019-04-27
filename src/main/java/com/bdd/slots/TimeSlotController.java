package com.bdd.slots;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
