package com.bdd.slots;

import com.bdd.convertors.DateConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TimeSlotService {
    private static final int DURATION = 30;
    private TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public ResponseEntity addTimeSlots(TimeSlotData timeSlotData) {
        if(timeSlotData == null ||
                timeSlotData.getStartDate() == null ||
                timeSlotData.getDays()<=0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
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
            calender.set(Calendar.HOUR_OF_DAY, 9);
            calender.set(Calendar.MINUTE, 0);
            calender.add(Calendar.DAY_OF_MONTH, 1);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    private Calendar getDate(TimeSlotData timeSlotData) {
        Date startDate = DateConverter.convert(timeSlotData.getStartDate());
        Calendar calender = Calendar.getInstance();
        calender.setTime(startDate);
        calender.set(Calendar.HOUR_OF_DAY, 9);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);

        return calender;
    }
}
