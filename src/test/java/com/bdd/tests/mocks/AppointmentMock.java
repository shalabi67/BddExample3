package com.bdd.tests.mocks;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentController;
import com.bdd.appointments.AppointmentRepository;
import com.bdd.appointments.AppointmentService;
import com.bdd.appointments.queue.AppointmentQueue;
import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.configuration.QueueConfiguration;
import com.bdd.customers.Customer;
import com.bdd.customers.CustomerRepository;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashMap;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AppointmentMock {
    QueueProcessorMock queueProcessorMock;

    public AppointmentMock(QueueProcessorMock queueProcessorMock) {
        this.queueProcessorMock = queueProcessorMock;
    }

    public AppointmentController mockAppointmentController() {
        Queue queue = Mockito.mock(Queue.class);
        Mockito.when(queue.getName()).thenReturn(QueueConfiguration.QUEUE_NAME);

        RabbitTemplate template = Mockito.mock(RabbitTemplate.class);
        Mockito.doAnswer(processorAnswer).when(template).convertAndSend(anyString(), any(Appointment.class));

        CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
        Mockito.when(customerRepository.findById(any())).thenAnswer(customerCreateAnswer);

        AppointmentRepository appointmentRepository = createAppointmentRepositoryMock();

        AppointmentQueue appointmentQueue = new AppointmentQueue(template, queue);
        AppointmentService appointmentService = new AppointmentService(appointmentQueue, customerRepository, appointmentRepository);
        AppointmentController appointmentController = new AppointmentController(appointmentService);
        return appointmentController;
    }
    private AppointmentRepository createAppointmentRepositoryMock() {
        AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
        Mockito.when(appointmentRepository.findAppointmentByCustomerAndStartDate(any(), any())).thenAnswer(invocationOnMock -> {
            Customer customer = invocationOnMock.getArgument(0);
            String startDate = invocationOnMock.getArgument(1);
            Appointment existingAppointment = queueProcessorMock.getCustomerAppointments().getOrDefault(customer.getId(), new HashMap<>())
                    .getOrDefault(startDate, null);
            if(existingAppointment == null)
                return Optional.empty();
            return Optional.ofNullable(queueProcessorMock.getAppointments().getOrDefault(existingAppointment.getId(), null));
        });
        return appointmentRepository;
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
        return queueProcessorMock.createQueueProcessorMock(appointment);
    }


}
