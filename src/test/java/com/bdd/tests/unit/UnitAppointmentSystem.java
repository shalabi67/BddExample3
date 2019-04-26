package com.bdd.tests.unit;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentController;
import com.bdd.appointments.AppointmentRepository;
import com.bdd.appointments.AppointmentService;
import com.bdd.appointments.queue.AppointmentQueue;
import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.configuration.QueueConfiguration;
import com.bdd.customers.Customer;
import com.bdd.customers.CustomerRepository;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistRepository;
import com.bdd.tests.factory.AppointmentQueueStatus;
import com.bdd.tests.factory.AppointmentSystem;
import com.bdd.tests.factory.StylistSystem;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class UnitAppointmentSystem extends AppointmentSystem {
    private long stylistCount = 0;
    private long appointmentsCount = 0;

    private long autoIncrement;
    //appointmentId, and appointment
    HashMap<Long, Appointment> appointments = new HashMap<>();

    //customerId, start date, appointment
    HashMap<Long, HashMap<String, Appointment>> customerAppointments = new HashMap<>();

    @Override
    public ResponseEntity addAppointment(Appointment appointment) {
        Queue queue = Mockito.mock(Queue.class);
        Mockito.when(queue.getName()).thenReturn(QueueConfiguration.QUEUE_NAME);

        RabbitTemplate template = Mockito.mock(RabbitTemplate.class);
        Mockito.doAnswer(processorAnswer).when(template).convertAndSend(anyString(), any(Appointment.class));

        CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
        Mockito.when(customerRepository.findById(any())).thenAnswer(customerCreateAnswer);

        AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
        Mockito.when(appointmentRepository.findAppointmentByCustomerAndStartDate(any(), any())).thenAnswer(invocationOnMock -> {
            Customer customer = invocationOnMock.getArgument(0);
            String startDate = invocationOnMock.getArgument(1);
            Appointment existingAppointment = customerAppointments.getOrDefault(customer.getId(), new HashMap<>())
                    .getOrDefault(startDate, null);
            if(existingAppointment == null)
                return Optional.empty();
            return Optional.ofNullable(appointments.getOrDefault(existingAppointment.getId(), null));
        });

        AppointmentQueue appointmentQueue = new AppointmentQueue(template, queue);
        AppointmentService appointmentService = new AppointmentService(appointmentQueue, customerRepository, appointmentRepository);
        AppointmentController appointmentController = new AppointmentController(appointmentService);
        return appointmentController.addAppointment(appointment);
    }

    @Override
    public ResponseEntity<Stylist> addStylist(Stylist stylist) {
        stylistCount++;
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private Answer<Optional<Customer>> customerCreateAnswer = invocationOnMock -> {
        long customerId = invocationOnMock.getArgument(0);
        if(customerId < 0 || customerId > 1000) {
            return Optional.empty();
        }
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setFirstName("fake");

        Optional<Customer> optionalCustomer = Optional.of(customer);
        return optionalCustomer;
    };

    private Answer<Void> processorAnswer = invocationOnMock -> {
        Appointment appointment  = invocationOnMock.getArgument(1);
        QueueProcessor queueProcessor = createQueueProcessorMock(appointment);
        queueProcessor.process(appointment);

        return null;
    };

    private QueueProcessor createQueueProcessorMock(Appointment appointment) {
        StylistRepository stylistRepository = Mockito.mock(StylistRepository.class);
        Mockito.when(stylistRepository.count()).thenReturn(stylistCount);
        List<Stylist> stylists = new ArrayList<>();
        stylists.add(StylistSystem.createStylist());
        Mockito.when(stylistRepository.findStylistsByIdNotIn(any())).thenReturn(stylists);
        Mockito.when(stylistRepository.findAll()).thenReturn(stylists);


        AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
        Mockito.when(appointmentRepository.findAppointmentsByStartDate(any())).thenReturn(new HashSet<>());
        Mockito.when(appointmentRepository.save(any())).thenAnswer(invocationOnMock -> {
            Appointment appointmentResult  = invocationOnMock.getArgument(0);
            autoIncrement++;
            appointmentsCount = autoIncrement;
            appointmentResult.setId(autoIncrement);

            appointments.put(appointment.getId(), appointment);
            HashMap<String, Appointment> map = customerAppointments.getOrDefault(appointment.getCustomer().getId(), new HashMap<>());
            map.put(appointment.getStartDate(), appointment);
            customerAppointments.put(appointment.getCustomer().getId(), map);

            return appointmentResult;
        });

        Mockito.when(appointmentRepository.countAppointmentsByStartDate(any())).thenAnswer(invocationOnMock -> {
            return appointmentsCount;
        });


        QueueProcessor queueProcessor = new QueueProcessor(appointmentRepository, stylistRepository);

        QueueProcessor spyQueueProcessor = Mockito.spy(queueProcessor);
        Mockito.when(spyQueueProcessor.notifyFailException()).thenAnswer(invocationOnMock -> {
            appointmentQueueStatus = AppointmentQueueStatus.exceptionFail;
            return true;
        });
        Mockito.when(spyQueueProcessor.notifySuccess()).thenAnswer(invocationOnMock -> {
            appointmentQueueStatus = AppointmentQueueStatus.success;
            return true;
        });
        Mockito.when(spyQueueProcessor.notifyFail()).thenAnswer(invocationOnMock -> {
            appointmentQueueStatus = AppointmentQueueStatus.fail;
            return true;
        });

        return spyQueueProcessor;
    }
}
