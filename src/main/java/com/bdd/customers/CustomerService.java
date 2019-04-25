package com.bdd.customers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends PersonService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    ResponseEntity addCustomer(Customer customer) {
        return savePerson(customer);
    }

    @Override
    protected Person save(Person person) {
        return customerRepository.save((Customer)person);
    }
}
