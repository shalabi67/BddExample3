package com.bdd.appointments;

import com.bdd.customers.Customer;
import com.bdd.stylists.Stylist;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    //TODO: notice this should be of type Date, we are using string here fro simplicity, later on this should be Date.
    private String startDate;  //time slot start date and time

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;

    @OneToOne
    @JoinColumn(name = "stylist_id", referencedColumnName = "id")
    Stylist stylist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Stylist getStylist() {
        return stylist;
    }

    public void setStylist(Stylist stylist) {
        this.stylist = stylist;
    }
}
