package com.bdd.tests.mocks;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentRepository;
import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistRepository;
import com.bdd.tests.factory.AppointmentQueueStatus;
import com.bdd.tests.factory.StylistSystem;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class QueueProcessorMock {
    private long stylistCount = 0;
    private long appointmentsCount = 0;

    private long autoIncrement;
    private AppointmentQueueStatus appointmentQueueStatus;
    public AppointmentQueueStatus getAppointmentQueueStatus() {
        return appointmentQueueStatus;
    }


    //appointmentId, and appointment
    private HashMap<Long, Appointment> appointments = new HashMap<>();

    //customerId, start date, appointment
    private HashMap<Long, HashMap<String, Appointment>> customerAppointments = new HashMap<>();

    public HashMap<Long, Appointment> getAppointments() {
        return appointments;
    }

    public HashMap<Long, HashMap<String, Appointment>> getCustomerAppointments() {
        return customerAppointments;
    }

    public void incrementStylistCount() {
        stylistCount++;
    }

    public QueueProcessorMock(AppointmentQueueStatus appointmentQueueStatus) {
        this.appointmentQueueStatus = appointmentQueueStatus;
        this.appointments = appointments;
        this.customerAppointments = customerAppointments;
    }

    public  QueueProcessor createQueueProcessorMock(Appointment appointment) {
        StylistRepository stylistRepository = createStylistRepositoryMock();

        AppointmentRepository appointmentRepository = createAppointmentRepositoryMock(appointment);

        QueueProcessor spyQueueProcessor = createQueueProcessor(stylistRepository, appointmentRepository);

        return spyQueueProcessor;
    }
    private AppointmentRepository createAppointmentRepositoryMock(Appointment appointment) {
        AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
        Mockito.when(appointmentRepository.findAppointmentsByStartDate(any())).thenReturn(new HashSet<>());
        Mockito.when(appointmentRepository.save(any())).thenAnswer(saveAppointmentAnswer(appointment));
        Mockito.when(appointmentRepository.countAppointmentsByStartDate(any())).thenAnswer(invocationOnMock -> {
            return appointmentsCount;
        });
        return appointmentRepository;
    }
    private StylistRepository createStylistRepositoryMock() {
        StylistRepository stylistRepository = Mockito.mock(StylistRepository.class);
        Mockito.when(stylistRepository.count()).thenReturn(stylistCount);
        List<Stylist> stylists = new ArrayList<>();
        stylists.add(StylistSystem.createStylist());
        Mockito.when(stylistRepository.findStylistsByIdNotIn(any())).thenReturn(stylists);
        Mockito.when(stylistRepository.findAll()).thenReturn(stylists);
        return stylistRepository;
    }
    private QueueProcessor createQueueProcessor(StylistRepository stylistRepository, AppointmentRepository appointmentRepository) {
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
    private Answer<Object> saveAppointmentAnswer(Appointment appointment) {
        return invocationOnMock -> {
            Appointment appointmentResult  = invocationOnMock.getArgument(0);
            autoIncrement++;
            appointmentsCount = autoIncrement;
            appointmentResult.setId(autoIncrement);

            appointments.put(appointment.getId(), appointment);
            HashMap<String, Appointment> map = customerAppointments.getOrDefault(appointment.getCustomer().getId(), new HashMap<>());
            map.put(appointment.getStartDate(), appointment);
            customerAppointments.put(appointment.getCustomer().getId(), map);

            return appointmentResult;
        };
    }
}
