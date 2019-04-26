package com.bdd.appointments;

import com.bdd.appointments.queue.AppointmentQueue;
import com.bdd.customers.Customer;
import com.bdd.customers.CustomerRepository;
import com.bdd.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class AppointmentService {
    private static Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private AppointmentQueue appointmentQueue;
    private CustomerRepository customerRepository;
    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentQueue appointmentQueue,
                              CustomerRepository customerRepository,
                              AppointmentRepository appointmentRepository) {
        this.appointmentQueue = appointmentQueue;
        this.customerRepository = customerRepository;
        this.appointmentRepository = appointmentRepository;
    }

    ResponseEntity addAppointment(Appointment appointment) {
        //validate date
        String startDate = getValidDate(appointment.getStartDate());
        if(startDate == null) {
            logger.error("invalid date for appointment add " + appointment.getStartDate());
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }
        appointment.setStartDate(startDate);

        //check if customer exists
        if(appointment.getCustomer() == null) {
            logger.error("Customer is required while adding an appointment.");
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }
        Optional<Customer> customer = customerRepository.findById(appointment.getCustomer().getId());
        if(!customer.isPresent()) {
            logger.error("Customer not found while adding an appointment " + appointment.getCustomer().getId());
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }

        //check if customer has an appointment at that time
        Optional<Appointment> optional = appointmentRepository.findAppointmentByCustomerAndStartDate(customer.get(), appointment.getStartDate());
        if(optional.isPresent()) {
            return new ResponseEntity(HttpStatus.FOUND);
        }

        appointment.setCustomer(customer.get());

        appointment.setStylist(null);

        appointmentQueue.send(appointment);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    private String getValidDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date expectedDate = format.parse(date);
            String newDate = format.format(expectedDate);
            Date actualDate =  format.parse(newDate);
            if(actualDate.equals(expectedDate)) {
                return newDate;
            }
            return null;
        } catch (ParseException e) {
            return null;
        }
    }
}
