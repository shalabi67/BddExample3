package com.bdd.tests.integration;

import com.bdd.customers.Customer;
import com.bdd.customers.CustomerController;
import com.bdd.json.JsonMapper;
import com.bdd.tests.factory.CustomerSystem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class IntegrationCustomerSystem extends CustomerSystem {
    private MockMvc mockMvc;

    public IntegrationCustomerSystem(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Override
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        JsonMapper<Customer> jsonMapper = new JsonMapper<>();
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc
                    .perform(
                            post(CustomerController.URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonMapper.toString(customer))
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
