package com.bdd.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private static Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        if(!Email.isValidEmail(customer.getEmail())) {
            logger.warn("invalid email: " + customer.getEmail());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        customer.setId(null);
        try {
            Customer savedCustomer = customerRepository.save(customer);

            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        }catch(DataIntegrityViolationException integrityException) {
            logger.warn(integrityException.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
