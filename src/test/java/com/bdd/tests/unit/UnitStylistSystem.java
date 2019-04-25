package com.bdd.tests.unit;

import com.bdd.customers.Customer;
import com.bdd.customers.CustomerController;
import com.bdd.customers.CustomerRepository;
import com.bdd.customers.CustomerService;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistController;
import com.bdd.stylists.StylistRepository;
import com.bdd.stylists.StylistService;
import com.bdd.tests.factory.CustomerSystem;
import com.bdd.tests.factory.StylistSystem;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

public class UnitStylistSystem extends StylistSystem {

    private StylistController stylistController;
    private PersonSystem personSystem = new PersonSystem();

    private Answer<Stylist> stylistCreateAnswer = invocationOnMock -> {
        Stylist stylist = invocationOnMock.getArgument(0);

        return PersonSystem.toStylist(personSystem.addPerson(stylist));
    };

    @Override
    public ResponseEntity<Stylist> addStylist(Stylist stylist) {
        StylistRepository stylistRepository = Mockito.mock(StylistRepository.class);
        Mockito.when(stylistRepository.save(any())).thenAnswer(stylistCreateAnswer);

        StylistService stylistService = new StylistService(stylistRepository);
        stylistController = new StylistController(stylistService);
        return stylistController.addStylist(stylist);
    }
}
