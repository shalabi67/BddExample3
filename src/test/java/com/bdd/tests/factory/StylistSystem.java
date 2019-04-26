package com.bdd.tests.factory;

import com.bdd.customers.Customer;
import com.bdd.stylists.Stylist;
import org.springframework.http.ResponseEntity;

public abstract class StylistSystem {
    public abstract ResponseEntity<Stylist> addStylist(Stylist stylist);

    public static Stylist createStylist(String firstname, String lastname, String email) {
        Stylist stylist = new Stylist();
        stylist.setEmail(email);
        stylist.setFirstName(firstname);
        stylist.setLastName(lastname);

        return stylist;
    }
    public static Stylist createStylist() {
        Stylist stylist = createStylist("firstName", "lastName", "email@gmail.com");
        stylist.setId(1L);

        return stylist;
    }
}
