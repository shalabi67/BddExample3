package com.bdd.stylists;

import com.bdd.customers.Person;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class Stylist extends Person {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    //ToDO: find a way so inheritance will be enough and we do not need to have these getes duplicated on both customer and stylist
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
}
