package com.bdd.slots;

import com.bdd.appointments.AppointmentService;
import com.bdd.convertors.DateConverter;
import com.bdd.stylists.StylistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class TimeSlotService {
    private static Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private static final int DURATION = 30;
    private static final int START_HOUR = 9;
    private static final int START_MINUTE = 0;
    private TimeSlotRepository timeSlotRepository;
    private StylistRepository stylistRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, StylistRepository stylistRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.stylistRepository = stylistRepository;
    }

    public ResponseEntity<Set<TimeSlot>> getAvailableTimeSlots() {
        Integer stylistCount = (int)stylistRepository.count();
        Set<TimeSlot> availableTimeSlots = timeSlotRepository.findAllByActiveAppointmentsIsLessThan(stylistCount);

        return new ResponseEntity<>(availableTimeSlots, HttpStatus.OK);
    }


    public ResponseEntity addTimeSlots(TimeSlotData timeSlotData) {
        if(timeSlotData == null ||
                timeSlotData.getStartDate() == null ||
                timeSlotData.getDays()<=0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            createTimeSlots(timeSlotData);
        }catch(DataIntegrityViolationException e) {
            logger.error(e.getMessage());
            //TODO: for simplicity even it violates api design(calling api multiple times will return same result).
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Transactional
    void createTimeSlots(TimeSlotData timeSlotData) {
        Calendar calender = getDate(timeSlotData);

        for(int day=0;day<timeSlotData.getDays();day++) {
            for (int i = 0; i < 16; i++) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setDuration(DURATION);
                timeSlot.setActiveAppointments(0);
                timeSlot.setStartDate(calender.getTime());

                timeSlotRepository.save(timeSlot);
                calender.add(Calendar.MINUTE, DURATION);
            }
            calender.set(Calendar.HOUR_OF_DAY, START_HOUR);
            calender.set(Calendar.MINUTE, START_MINUTE);
            calender.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private Calendar getDate(TimeSlotData timeSlotData) {
        Date startDate = DateConverter.convert(timeSlotData.getStartDate());
        Calendar calender = Calendar.getInstance();
        calender.setTime(startDate);
        calender.set(Calendar.HOUR_OF_DAY, START_HOUR);
        calender.set(Calendar.MINUTE, START_MINUTE);
        calender.set(Calendar.SECOND, 0);

        return calender;
    }
}
