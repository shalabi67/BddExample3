package com.bdd.appointments;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppointmentController.URL)
public class AppointmentController {
    public static final String URL = "/appointments";
    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity addAppointment(@RequestBody Appointment appointment) {
        return appointmentService.addAppointment(appointment);
    }
}
