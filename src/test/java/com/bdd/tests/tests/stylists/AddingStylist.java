package com.bdd.tests.tests.stylists;

import com.bdd.customers.Customer;
import com.bdd.framework.Properties;
import com.bdd.stylists.Stylist;
import com.bdd.tests.factory.CustomerSystem;
import com.bdd.tests.factory.StylistSystem;
import com.bdd.tests.factory.SystemFactory;
import com.bdd.tests.integration.IntegrationStylistSystem;
import cucumber.api.PendingException;
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
public class AddingStylist {
    @Autowired
    private MockMvc mockMvc;

    private StylistSystem stylistSystem;
    private Stylist stylist;

    @Given("^Stylists system is started$")
    public void stylists_system_is_started() throws Throwable {
        Properties properties = new Properties();
        properties.setMockMvc(mockMvc);

        stylistSystem = SystemFactory.create().createStylistSystem(properties);
    }

    @When("^user provides valid stylists information to create a valid stylist (.+), (.+), and (.+)$")
    public void user_provides_valid_stylists_information_to_create_a_valid_stylist_and(String firstname, String lastname, String email) throws Throwable {
        stylist = StylistSystem.createStylist(firstname, lastname, email);
    }

    @When("^user provides invalid email with stylist information to create a stylist (.+), (.+), and (.+)$")
    public void user_provides_invalid_email_with_stylist_information_to_create_a_stylist_and(String firstname, String lastname, String email) throws Throwable {
        stylist = StylistSystem.createStylist(firstname, lastname, email);
    }

    @When("^user provides exiting email with stylist information to create a stylist (.+), (.+), and (.+)$")
    public void user_provides_exiting_email_with_stylist_information_to_create_a_stylist_and(String firstname, String lastname, String email) throws Throwable {
        Stylist oldStylist = StylistSystem.createStylist(firstname + "1", lastname + "1", email);
        ResponseEntity<Stylist> result = stylistSystem.addStylist(oldStylist);
        Assert.assertTrue(result.getStatusCode() == HttpStatus.CREATED);

        stylist = StylistSystem.createStylist(firstname, lastname, email);
    }

    @Then("^system creates stylists$")
    public void system_creates_stylists() throws Throwable {
        ResponseEntity<Stylist> result = stylistSystem.addStylist(stylist);
        Assert.assertTrue(result.getStatusCode() == HttpStatus.CREATED);
    }

    @Then("^system returns invalid stylist email information$")
    public void system_returns_invalid_stylist_email_information() throws Throwable {
        ResponseEntity<Stylist> result = stylistSystem.addStylist(stylist);
        Assert.assertTrue(result.getStatusCode() == HttpStatus.NOT_ACCEPTABLE);
    }

    @Then("^system returns exiting stylist email information$")
    public void system_returns_exiting_stylist_email_information() throws Throwable {
        ResponseEntity<Stylist> result = stylistSystem.addStylist(stylist);
        Assert.assertTrue(result.getStatusCode() == HttpStatus.CONFLICT);
    }

}
