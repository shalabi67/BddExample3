package com.bdd.tests.tests.slots;

import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.convertors.DateConverter;
import com.bdd.customers.Customer;
import com.bdd.slots.TimeSlot;
import com.bdd.tests.factory.*;
import com.bdd.tests.integration.AppointmentProperties;
import cucumber.api.PendingException;
import cucumber.api.java.bs.A;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

@Ignore
public class GettingAvailableTimeSlots {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    QueueProcessor queueProcessor;

    private TimeSlotSystem timeSlotSystem;
    private AppointmentSystem appointmentSystem;

    @Given("^Time slot system had been started and system has one day time on 2019-07-10 slots or 16 time slots and 5 customers$")
    public void time_slot_system_had_been_started_and_system_has_one_day_time_on_20190710_slots_or_16_time_slots_and_5_customers() throws Throwable {
        AppointmentProperties properties = new AppointmentProperties();
        properties.setMockMvc(mockMvc);
        properties.setQueueProcessor(queueProcessor);
        appointmentSystem = SystemFactory.create().createAppointmentSystem(properties);
        timeSlotSystem = SystemFactory.create().createTimeSlotSystem(properties);
        if(timeSlotSystem.isInitialized()) {
            return;
        }


        timeSlotSystem.addTimeSlots(TimeSlotSystem.createTimeSlotDate("2019-07-10 09:00"));

        for(long i=0; i<5; i++) {
            Customer customer = CustomerSystem.createCustomer("customerFirstName" + i, "customerLastName" + i, "customer" + i +"@gmail.com");
            customer.setId(i);
            appointmentSystem.addCustomer(customer);
        }

        timeSlotSystem.setInitialized();
    }

    @When("^user requested to get available time slots when one stylist is defined$")
    public void user_requested_to_get_available_time_slots_when_one_stylist_is_defined() {
        appointmentSystem.addStylist(
                StylistSystem.createStylist("firstName", "lastname", "timeslot1@gmail.com"));
    }

    @When("^time slots are available and no stylist is defined in the system$")
    public void time_slots_are_available_and_no_stylist_is_defined_in_the_system() {

    }

    @When("^all time slots are bocked then a stylist had been added to the systems$")
    public void all_time_slots_are_bocked_then_a_stylist_had_been_added_to_the_systems() {
        appointmentSystem.addStylist(
                StylistSystem.createStylist("firstName", "lastname", "timeslot1@gmail.com"));
        ResponseEntity<Set<TimeSlot>> timeSlotsResponse = timeSlotSystem.getAvailableTimeSlots();
        Assert.assertEquals(HttpStatus.OK, timeSlotsResponse.getStatusCode());
        for(TimeSlot timeSlot : timeSlotsResponse.getBody()) {
            String startDate = DateConverter.toString(timeSlot.getStartDate());
            ResponseEntity responseEntity = appointmentSystem.addAppointment(AppointmentSystem.createAppointment(startDate, 1));
            Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        }
    }

    @When("^One stylist exists customer (.+) bocks one appointment on (.+)$")
    public void customer_books_one_appointment_on(String customerid, String startdate) throws  Throwable{
        throw new PendingException();
    }

    @When("^customer (.+) bocks one appointment on (.+) one stylist will be available$")
    public void customer_books_one_appointment_on_one_stylist_will_be_available(String customerid, String startdate) throws Throwable {
        throw new PendingException();
    }

    @Then("^returns a list of available time slots which is 16$")
    public void returns_a_list_of_available_time_slots_which_is_16() throws Throwable {
        ResponseEntity<Set<TimeSlot>> timeSlotsResponse = timeSlotSystem.getAvailableTimeSlots();
        Assert.assertEquals(HttpStatus.OK, timeSlotsResponse.getStatusCode());
        Assert.assertEquals(32, timeSlotsResponse.getBody().size());
    }

    @Then("^system returns empty list$")
    public void system_returns_empty_list() {
        ResponseEntity<Set<TimeSlot>> timeSlotsResponse = timeSlotSystem.getAvailableTimeSlots();
        Assert.assertEquals(HttpStatus.OK, timeSlotsResponse.getStatusCode());
        Assert.assertEquals(0, timeSlotsResponse.getBody().size());
    }

    @Then("^All time slots will be available 16$")
    public void all_time_slots_will_be_available_16() throws Throwable {
        throw new PendingException();
    }

    @Then("^system returns a list of available time slots 15$")
    public void system_returns_a_list_of_available_time_slots_15() throws Throwable {
        throw new PendingException();
    }

    @Then("^system returns 16 available time slots$")
    public void system_returns_16_available_time_slots() throws Throwable {
        throw new PendingException();
    }

}