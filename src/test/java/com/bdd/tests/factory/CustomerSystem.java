package com.bdd.tests.factory;

import com.bdd.customers.Customer;
import org.springframework.http.ResponseEntity;

public abstract class CustomerSystem {
    public abstract ResponseEntity<Customer> addCustomer(Customer customer);

    public static Customer createCustomer(String firstname, String lastname, String email) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFirstName(firstname);
        customer.setLastName(lastname);

        return customer;
    }
}
