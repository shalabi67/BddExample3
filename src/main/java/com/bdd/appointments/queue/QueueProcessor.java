package com.bdd.appointments.queue;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentRepository;
import com.bdd.configuration.QueueConfiguration;
import com.bdd.convertors.DateConverter;
import com.bdd.slots.TimeSlot;
import com.bdd.slots.TimeSlotRepository;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RabbitListener(queues = QueueConfiguration.QUEUE_NAME)
public class QueueProcessor {
    private static Logger logger = LoggerFactory.getLogger(QueueProcessor.class);

    private AppointmentRepository appointmentRepository;
    private StylistRepository stylistRepository;
    private TimeSlotRepository timeSlotRepository;

    public QueueProcessor(AppointmentRepository appointmentRepository, StylistRepository stylistRepository,
                          TimeSlotRepository timeSlotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.stylistRepository = stylistRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @RabbitHandler
    public void process(Appointment appointment) {
        try {
            long stylistCount = stylistRepository.count();
            long appointmentsCount = appointmentRepository.countAppointmentsByStartDate(appointment.getStartDate());

            Date startDate = DateConverter.convert(appointment.getStartDate());
            Optional<TimeSlot> timeSlotOptional = timeSlotRepository.findByStartDateAndActiveAppointmentsLessThan(startDate, (int)stylistCount);
            if(!timeSlotOptional.isPresent()) {
                logger.info("Appointment rejected because no available time slot exists.");
                notifyFail();
                return;
            }
            if(stylistCount > appointmentsCount) {
                Set<Long> stylists = getStylists(appointment.getStartDate());
                List<Stylist> availableStylists;
                if(stylists == null || stylists.isEmpty()) {
                    availableStylists = stylistRepository.findAll();
                } else {
                    availableStylists = stylistRepository.findStylistsByIdNotIn(stylists);
                }
                appointment.setStylist(availableStylists.iterator().next());

                TimeSlot timeSlot = timeSlotOptional.get();
                timeSlot.setActiveAppointments(timeSlot.getActiveAppointments() + 1);

                save(appointment, timeSlot);

                notifySuccess();
            } else {
                logger.info("Appointment rejected because no available stylists exists.");
                notifyFail();
            }
        }catch(Exception e) {
            logger.error(e.getMessage());
            notifyFailException();
        }
    }

    @Transactional
    public void save(Appointment appointment, TimeSlot timeSlot) {
        appointmentRepository.save(appointment);
        timeSlotRepository.save(timeSlot);

    }

    private Set<Long> getStylists(String startDate) {
        Set<Appointment> appointments = appointmentRepository.findAppointmentsByStartDate(startDate);
        Set<Long> stylists = new HashSet<>();
        for(Appointment appointment :  appointments) {
            stylists.add(appointment.getStylist().getId());
        }

        return stylists;
    }

    //This will call some notification system to notify customer. right now it will be used by spy
    public boolean notifySuccess() {
        //TODO: send notification to customer that his appointment got approved.
        return true;
    }

    //to be used by spy
    public boolean notifyFail() {
        //TODO: send notification to customer that his appointment got rejected.
        return true;
    }

    //to be used by spy
    public boolean notifyFailException() {
        //TODO: send notification to customer that his appointment got rejected.
        return true;
    }
}
