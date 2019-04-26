package com.bdd.appointments;

import com.bdd.customers.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Long countAppointmentsByStartDate(String startDate);
    Set<Appointment> findAppointmentsByStartDate(String startDate);
    Optional<Appointment> findAppointmentByCustomerAndStartDate(Customer customer, String startDate);
}
