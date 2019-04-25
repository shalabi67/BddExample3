package com.bdd.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class PersonService {
    private static Logger logger = LoggerFactory.getLogger(PersonService.class);
    public ResponseEntity<Person> savePerson(Person person) {
        if(!Email.isValidEmail(person.getEmail())) {
            logger.warn("invalid stylist email: " + person.getEmail());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        person.setId(null);

        try {
            Person savedPerson = save(person);

            return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
        }catch(DataIntegrityViolationException integrityException) {
            logger.warn(integrityException.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    protected abstract Person save(Person person);
}
