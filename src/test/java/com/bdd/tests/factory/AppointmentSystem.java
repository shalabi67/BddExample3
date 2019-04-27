package com.bdd.tests.factory;

import com.bdd.appointments.Appointment;
import com.bdd.customers.Customer;
import com.bdd.stylists.Stylist;
import org.springframework.http.ResponseEntity;

public abstract class AppointmentSystem {
    protected AppointmentQueueStatus appointmentQueueStatus = AppointmentQueueStatus.undefined;
    protected boolean isInitialized = false;

    public AppointmentQueueStatus getAppointmentQueueStatus() {
        return appointmentQueueStatus;
    }

    public abstract ResponseEntity addAppointment(Appointment appointment);
    public abstract ResponseEntity<Stylist> addStylist(Stylist stylist);
    public abstract ResponseEntity<Customer> addCustomer(Customer customer);
    public abstract void setInitialized();

    public boolean isInitialized() {
        return isInitialized;
    }


    public static Appointment createAppointment(String startDate, long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setStartDate(startDate);

        return appointment;
    }
}
