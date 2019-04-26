package com.bdd.tests.factory;

import com.bdd.appointments.Appointment;
import com.bdd.customers.Customer;
import com.bdd.stylists.Stylist;
import org.springframework.http.ResponseEntity;

public abstract class AppointmentSystem {
    public static final String FAIL_EXCEPTION = "Fail Exception";

    protected AppointmentQueueStatus appointmentQueueStatus = AppointmentQueueStatus.undefined;
    public AppointmentQueueStatus getAppointmentQueueStatus() {
        return appointmentQueueStatus;
    }

    public abstract ResponseEntity addAppointment(Appointment appointment);
    public abstract ResponseEntity<Stylist> addStylist(Stylist stylist);
    public abstract ResponseEntity<Customer> addCustomer(Customer customer);


    public static Appointment createAppointment(String startDate, long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setStartDate(startDate);

        return appointment;
    }
}
