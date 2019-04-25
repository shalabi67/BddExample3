package com.bdd.stylists;

import com.bdd.customers.Person;
import com.bdd.customers.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StylistService extends PersonService {
    private StylistRepository stylistRepository;

    public StylistService(StylistRepository stylistRepository) {
        this.stylistRepository = stylistRepository;
    }
    ResponseEntity addStylist(Stylist stylist) {
        return savePerson(stylist);
    }

    @Override
    protected Person save(Person stylist) {
        return stylistRepository.save((Stylist)stylist);
    }
}
