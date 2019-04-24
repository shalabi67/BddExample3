package com.bdd.tests.unit;

import com.bdd.customers.Customer;
import com.bdd.customers.CustomerController;
import com.bdd.customers.CustomerRepository;
import com.bdd.customers.CustomerService;
import com.bdd.tests.factory.CustomerSystem;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

public class UnitCustomerSystem extends CustomerSystem {

    private CustomerController customerController;
    private PersonSystem personSystem = new PersonSystem();

    @Override
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
        Mockito.when(customerRepository.save(any())).thenAnswer(customerCreateAnswer);

        CustomerService customerService = new CustomerService(customerRepository);
        customerController = new CustomerController(customerService);
        return customerController.addCustomer(customer);
    }

    private Answer<Customer> customerCreateAnswer = new Answer<Customer>() {
        @Override
        public Customer answer(InvocationOnMock invocationOnMock) throws Throwable {
            Customer customer = invocationOnMock.getArgument(0);

            return PersonSystem.toCustomer(personSystem.addPerson(customer));
        }
    };
}
