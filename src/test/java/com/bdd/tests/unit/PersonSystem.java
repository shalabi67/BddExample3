package com.bdd.tests.unit;

import com.bdd.customers.Customer;
import com.bdd.customers.Person;
import com.bdd.stylists.Stylist;
import com.bdd.tests.factory.CustomerSystem;
import com.bdd.tests.factory.StylistSystem;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

public class PersonSystem {
    private Map<String, Person> emailEmployeeMap = new HashMap<>();
    private long autoGenerateId = 0;
    private Map<Long, Person> employeeMap = new HashMap<>();



    public Person addPerson(Person person) {
        if(emailEmployeeMap.containsKey(person.getEmail())) {
            throw new DataIntegrityViolationException("");
        }

        autoGenerateId++;
        Person newPerson = new Person();
        newPerson.setId(autoGenerateId);
        newPerson.setEmail(person.getEmail());
        newPerson.setFirstName(person.getFirstName());
        newPerson.setLastName(person.getLastName());

        emailEmployeeMap.put(person.getEmail(), newPerson);
        employeeMap.put(newPerson.getId(), newPerson);

        return newPerson;
    }

    public static Customer toCustomer(Person person) {
        Customer customer = CustomerSystem.createCustomer(person.getFirstName(), person.getLastName(), person.getEmail());
        customer.setId(person.getId());

        return customer;
    }

    public static Stylist toStylist(Person person) {
        Stylist stylist = StylistSystem.createStylist(person.getFirstName(), person.getLastName(), person.getEmail());
        stylist.setId(person.getId());

        return stylist;
    }
}
