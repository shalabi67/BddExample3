package com.bdd.appointments.queue;

import com.bdd.appointments.Appointment;
import com.bdd.appointments.AppointmentRepository;
import com.bdd.configuration.QueueConfiguration;
import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RabbitListener(queues = QueueConfiguration.QUEUE_NAME)
public class QueueProcessor {
    private static Logger logger = LoggerFactory.getLogger(QueueProcessor.class);

    AppointmentRepository appointmentRepository;
    StylistRepository stylistRepository;

    public QueueProcessor(AppointmentRepository appointmentRepository, StylistRepository stylistRepository) {
        this.appointmentRepository = appointmentRepository;
        this.stylistRepository = stylistRepository;
    }

    @RabbitHandler
    public void process(Appointment appointment) {
        Boolean hasError = false;
        try {
            long stylistCount = stylistRepository.count();
            long appointmentsCount = appointmentRepository.countAppointmentsByStartDate(appointment.getStartDate());
            if(stylistCount > appointmentsCount) {
                Set<Long> stylists = getStylists(appointment.getStartDate());
                List<Stylist> availableStylists;
                if(stylists == null || stylists.isEmpty()) {
                    availableStylists = stylistRepository.findAll();
                } else {
                    availableStylists = stylistRepository.findStylistsByIdNotIn(stylists);
                }
                appointment.setStylist(availableStylists.iterator().next());
                appointmentRepository.save(appointment);
                notifySuccess();
                return;
            } else {
                logger.info("Appointment rejected because no available stylists exists.");
                hasError = notifyFail();
                return;
            }

        }catch(Exception e) {
            logger.error(e.getMessage());
            notifyFailException();
        }finally {
            // just for testing since no notification system exist.
            if(hasError) {
                throw new RuntimeException("Fail");
            }
        }
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
