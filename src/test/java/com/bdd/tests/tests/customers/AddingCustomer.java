package com.bdd.tests.tests.customers;

import com.bdd.customers.Customer;
import com.bdd.framework.Properties;
import com.bdd.tests.factory.CustomerSystem;
import com.bdd.tests.factory.SystemFactory;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@Ignore
public class AddingCustomer {
    @Autowired
    private MockMvc mockMvc;

    private CustomerSystem customerSystem;
    private Customer customer;



    @Given("^Customers system is started$")
    public void customers_system_is_started() throws Throwable {
        Properties properties = new Properties();
        properties.setMockMvc(mockMvc);

        customerSystem = SystemFactory.create().createCustomerSystem(properties);
    }

    @When("^user provides valid customer information to create a valid customer (.+), (.+), and (.+)$")
    public void user_provides_valid_customer_information_to_create_a_valid_customer_and(String firstname, String lastname, String email) throws Throwable {
        customer = CustomerSystem.createCustomer(firstname, lastname, email);
    }

    @When("^user provides invalid email with customer information to create a customer (.+), (.+), and (.+)$")
    public void user_provides_invalid_email_with_customer_information_to_create_a_customer_and(String firstname, String lastname, String email) throws Throwable {
        customer = CustomerSystem.createCustomer(firstname, lastname, email);
    }

    @When("^user provides exiting email with customer information to create a customer (.+), (.+), and (.+)$")
    public void user_provides_exiting_email_with_customer_information_to_create_a_customer_and(String firstname, String lastname, String email) throws Throwable {
        Customer oldCustomer = CustomerSystem.createCustomer(firstname + "1", lastname + "1", email);
        ResponseEntity<Customer> result = customerSystem.addCustomer(oldCustomer);
        Assert.assertTrue(result.getStatusCode() == HttpStatus.CREATED);

        customer = CustomerSystem.createCustomer(firstname, lastname, email);
    }

    @Then("^system creates customer$")
    public void system_creates_customer() throws Throwable {
        ResponseEntity<Customer> result = customerSystem.addCustomer(customer);

        //TODO: notice we are tersting the status but not the content, that could be added later
        Assert.assertTrue(result.getStatusCode() == HttpStatus.CREATED);

    }

    @Then("^system returns invalid email information$")
    public void system_returns_invalid_email_information() throws Throwable {
        ResponseEntity<Customer> result = customerSystem.addCustomer(customer);
        Assert.assertTrue(result.getStatusCode() == HttpStatus.NOT_ACCEPTABLE);
    }

    @Then("^system returns exiting customer email information$")
    public void system_returns_exiting_customer_email_information() throws Throwable {
        ResponseEntity<Customer> result = customerSystem.addCustomer(customer);
        Assert.assertTrue(result.getStatusCode() == HttpStatus.CONFLICT);
    }

}