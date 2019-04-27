package com.bdd.tests.integration;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentController;
import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.customers.Customer;
import com.bdd.json.JsonMapper;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistController;
import com.bdd.tests.factory.AppointmentQueueStatus;
import com.bdd.tests.factory.AppointmentSystem;
import com.bdd.tests.factory.StylistSystem;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class IntegrationAppointmentSystem extends AppointmentSystem {
    private MockMvc mockMvc;
    private QueueProcessor queueProcessor;
    private IntegrationCustomerSystem customerSystem;
    private IntegrationStylistSystem stylistSystem;

    public IntegrationAppointmentSystem(MockMvc mockMvc, QueueProcessor queueProcessor) {
        this.mockMvc = mockMvc;
        this.queueProcessor = queueProcessor;
        customerSystem = new IntegrationCustomerSystem(mockMvc);
        stylistSystem = new IntegrationStylistSystem(mockMvc);
    }

    @Override
    public ResponseEntity<Stylist> addStylist(Stylist stylist) {
        return stylistSystem.addStylist(stylist);
    }

    @Override
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        return customerSystem.addCustomer(customer);
    }

    @Override
    public void setInitialized() {
        isInitialized = true;
    }

    @Override
    public ResponseEntity addAppointment(Appointment appointment) {
        JsonMapper<Appointment> jsonMapper = new JsonMapper<>();
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc
                    .perform(
                            post(AppointmentController.URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonMapper.toString(appointment))
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andReturn();
            //TODO: String content = mvcResult.getResponse().getContentAsString();
            //TODO: notice we are returning the content, because we are not testing it. this part is ignored, IK can do it later if needed.
            return new ResponseEntity<>(HttpStatus.valueOf(mvcResult.getResponse().getStatus()));
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
