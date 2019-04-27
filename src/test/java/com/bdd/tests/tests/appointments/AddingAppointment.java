package com.bdd.tests.tests.appointments;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.customers.Customer;
import com.bdd.stylists.Stylist;
import com.bdd.tests.factory.*;
import com.bdd.tests.integration.AppointmentProperties;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@Ignore
public class AddingAppointment {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    QueueProcessor queueProcessor;

    private AppointmentSystem appointmentSystem;
    private Appointment appointment;

    @Given("^Appointment system is started and system has three stylists with ids 1,2 and 3 and five customers with id 1,2,3,4,5$")
    public void appointment_system_is_started_and_system_has_three_stylists_with_ids_12_and_3_and_five_customers_with_id_12345() {
        AppointmentProperties properties = new AppointmentProperties();
        properties.setMockMvc(mockMvc);
        properties.setQueueProcessor(queueProcessor);
        appointmentSystem = SystemFactory.create().createAppointmentSystem(properties);
        if(appointmentSystem.isInitialized()) {
            return;
        }

        //initialize time slot system
        TimeSlotSystem timeSlotSystem= SystemFactory.create().createTimeSlotSystem(properties);
        ResponseEntity responseEntity = timeSlotSystem.addTimeSlots(TimeSlotSystem.createTimeSlotDate("2019-05-01 9:0"));
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());



        Stylist stylist = StylistSystem.createStylist("firstName","lastName", "email@test.com");
        stylist.setId(1L);
        appointmentSystem.addStylist(stylist);

        for(long i=0; i<3; i++) {
            Customer customer = CustomerSystem.createCustomer("customerFirstName" + i, "customerLastName" + i, "customer" + i +"@gmail.com");
            customer.setId(i);
            appointmentSystem.addCustomer(customer);
        }

        appointmentSystem.setInitialized();
    }

    @Given("^system has one stylist and one appointments (.+), and (.+)$")
    public void system_has_one_stylist_and_one_appointments_and(String date, String oldcustomerid) {
        Customer customer = new Customer();
        customer.setId(Long.valueOf(oldcustomerid));
        customer.setFirstName("firstName");
        customer.setLastName("lastName");
        customer.setEmail("email@test.com");

        Appointment appointment = new Appointment();
        appointment.setStartDate(date);
        appointment.setCustomer(customer);
        appointmentSystem.addAppointment(appointment);
    }

    @When("^user provides valid appointment information to add a valid appointment (.+), and (.+)$")
    public void user_provides_valid_appointment_information_to_add_a_valid_appointment_and(String date, String customerid) {
        appointment = AppointmentSystem.createAppointment(date, Long.valueOf(customerid));
    }

    @When("^user provides invalid date information to add an appointment (.+), and (.+)$")
    public void user_provides_invalid_date_information_to_add_an_appointment_and(String date, String customerid) {
        appointment = AppointmentSystem.createAppointment(date, Long.valueOf(customerid));
    }

    @When("^user provides invalid customer information to add an appointment (.+), and (.+)$")
    public void user_provides_invalid_customer_information_to_add_an_appointment_and(String date, String customerid) {
        appointment = AppointmentSystem.createAppointment(date, Long.valueOf(customerid));
    }

    @When("^no available stylist, user provides valid appointment information to add a valid appointment (.+), and (.+)$")
    public void no_available_stylist_user_provides_valid_appointment_information_to_add_a_valid_appointment_and(String date, String customerid) {
        appointment = AppointmentSystem.createAppointment(date, Long.valueOf(customerid));
    }

    @Then("^system creates an appointment$")
    public void system_creates_an_appointment() {
        ResponseEntity responseEntity = appointmentSystem.addAppointment(appointment);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Then("^system returns error invalid date$")
    public void system_returns_error_invalid_date() {
        ResponseEntity responseEntity = appointmentSystem.addAppointment(appointment);
        Assert.assertEquals(HttpStatus.EXPECTATION_FAILED, responseEntity.getStatusCode());
    }

    @Then("^system returns invalid customer$")
    public void system_returns_invalid_customer() {
        ResponseEntity responseEntity = appointmentSystem.addAppointment(appointment);
        Assert.assertEquals(HttpStatus.EXPECTATION_FAILED, responseEntity.getStatusCode());
    }

    @Then("system notify stylist not available")
    public void systemNotifyStylistNotAvailable() {
        ResponseEntity responseEntity = appointmentSystem.addAppointment(appointment);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        //TODO: this chick will fail on integration tests. for simplicity I will not fix integration test.
        Assert.assertEquals(AppointmentQueueStatus.fail, appointmentSystem.getAppointmentQueueStatus());
    }

    @When("^user try to create an appointment he already has by providing (.+), and (.+)$")
    public void user_try_to_create_an_appointment_he_already_has_by_providing_and(String date, String customerid) throws Throwable {
        appointment = AppointmentSystem.createAppointment(date, Long.valueOf(customerid));

        ResponseEntity responseEntity = appointmentSystem.addAppointment(appointment);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        //TODO: we need to remove sleep. I keep it here for simplicity
        Thread.sleep(300);  //give time for the processor to execute
    }

    @Then("^system will return customer ahd this appointment$")
    public void system_will_return_customer_ahd_this_appointment() {
        ResponseEntity responseEntity = appointmentSystem.addAppointment(appointment);
        Assert.assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
    }

    @When("^time slot customer (.+) tries books one appointment on non existing time slot (.+)$")
    public void customer_tries_books_one_appointment_on_non_existing_time_slot(String customerid, String startdate) {
        appointment = AppointmentSystem.createAppointment(startdate, Long.valueOf(customerid));
    }

    @Then("^system returns non exiting time slot$")
    public void system_returns_non_exiting_time_slot() {
        ResponseEntity responseEntity = appointmentSystem.addAppointment(appointment);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        //TODO: this chick will fail on integration tests. for simplicity I will not fix integration test.
        Assert.assertEquals(AppointmentQueueStatus.fail, appointmentSystem.getAppointmentQueueStatus());
    }
}