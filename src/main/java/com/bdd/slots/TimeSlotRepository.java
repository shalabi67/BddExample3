package com.bdd.slots;

import com.bdd.appointments.Appointment;
import com.bdd.customers.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    Set<TimeSlot> findAllByActiveAppointmentsIsLessThan(Integer stylistsCount);
    Optional<TimeSlot> findByStartDateAndActiveAppointmentsLessThan(Date startDate, Integer stylistCount);

}
