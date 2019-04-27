package com.bdd.tests.mocks;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentRepository;
import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.convertors.DateConverter;
import com.bdd.slots.TimeSlot;
import com.bdd.slots.TimeSlotRepository;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistRepository;
import com.bdd.tests.factory.AppointmentQueueStatus;
import com.bdd.tests.factory.StylistSystem;
import com.bdd.tests.factory.TimeSlotSystem;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.*;

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
    }

    public  QueueProcessor createQueueProcessorMock(Appointment appointment) {
        StylistRepository stylistRepository = createStylistRepositoryMock();

        AppointmentRepository appointmentRepository = createAppointmentRepositoryMock(appointment);

        TimeSlotRepository timeSlotRepository = createTimeSlotRepositoryMock();

        QueueProcessor spyQueueProcessor = createQueueProcessor(stylistRepository, appointmentRepository, timeSlotRepository);

        return spyQueueProcessor;
    }
    private TimeSlotRepository createTimeSlotRepositoryMock() {
        TimeSlotRepository timeSlotRepository = Mockito.mock(TimeSlotRepository.class);
        Mockito.when(timeSlotRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        Mockito.when(timeSlotRepository.findByStartDateAndActiveAppointmentsLessThan(any(), any()))
                .thenAnswer(invocationOnMock -> {
            Date appointmentDate = invocationOnMock.getArgument(0);
            Date noTimeSlotDate = DateConverter.convert("2019-04-10 09:00");
            if(noTimeSlotDate == null) {
                return Optional.empty();
            }
            if(appointmentDate.compareTo(noTimeSlotDate) > 0) {
                TimeSlot timeSlot = TimeSlotSystem.create(appointmentDate, 0);
                return Optional.ofNullable(timeSlot);
            }
            return Optional.empty();
        });


        return timeSlotRepository;
    }
    private AppointmentRepository createAppointmentRepositoryMock(Appointment appointment) {
        AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);
        Mockito.when(appointmentRepository.findAppointmentsByStartDate(any())).thenReturn(new HashSet<>());
        Mockito.when(appointmentRepository.save(any())).thenAnswer(saveAppointmentAnswer(appointment));
        Mockito.when(appointmentRepository.countAppointmentsByStartDate(any())).thenAnswer(invocationOnMock -> appointmentsCount);
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
    private QueueProcessor createQueueProcessor(StylistRepository stylistRepository,
                                AppointmentRepository appointmentRepository, TimeSlotRepository timeSlotRepository) {
        QueueProcessor queueProcessor = new QueueProcessor(appointmentRepository, stylistRepository, timeSlotRepository);

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
