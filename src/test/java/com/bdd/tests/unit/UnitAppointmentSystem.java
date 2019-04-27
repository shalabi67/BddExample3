package com.bdd.tests.unit;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentController;
import com.bdd.appointments.AppointmentRepository;
import com.bdd.appointments.AppointmentService;
import com.bdd.appointments.queue.AppointmentQueue;
import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.configuration.QueueConfiguration;
import com.bdd.customers.Customer;
import com.bdd.customers.CustomerRepository;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistRepository;
import com.bdd.tests.factory.AppointmentQueueStatus;
import com.bdd.tests.factory.AppointmentSystem;
import com.bdd.tests.factory.StylistSystem;
import com.bdd.tests.mocks.AppointmentMock;
import com.bdd.tests.mocks.QueueProcessorMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class UnitAppointmentSystem extends AppointmentSystem {
    private QueueProcessorMock queueProcessorMock = QueueProcessorMock.reset();

    @Override
    public ResponseEntity addAppointment(Appointment appointment) {
        AppointmentMock appointmentMock = new AppointmentMock(queueProcessorMock);
        AppointmentController appointmentController = appointmentMock.mockAppointmentController();
        return appointmentController.addAppointment(appointment);
    }

    @Override
    public ResponseEntity<Stylist> addStylist(Stylist stylist) {
        queueProcessorMock.incrementStylistCount();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public int getStylistCount() {
        return 0;
    }

    @Override
    public void setInitialized() {
        isInitialized = false;
    }

    @Override
    public AppointmentQueueStatus getAppointmentQueueStatus() {
        return queueProcessorMock.getAppointmentQueueStatus();
    }
}
