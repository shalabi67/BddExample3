package com.bdd.tests.mocks;

import com.bdd.slots.TimeSlot;
import com.bdd.slots.TimeSlotController;
import com.bdd.slots.TimeSlotRepository;
import com.bdd.slots.TimeSlotService;
import com.bdd.stylists.StylistRepository;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

public class TimeSlotMock {
    private Set<TimeSlot> timeSlots = new HashSet<>();
    private HashMap<Date, TimeSlot> timeSlotsMap = new HashMap<>();
    private Long autoIncrement = 0L;

    public TimeSlotController addTimeSlotsMock() {
        TimeSlotRepository timeSlotRepository = Mockito.mock(TimeSlotRepository.class);
        Mockito.when(timeSlotRepository.save(any())).thenAnswer((Answer<TimeSlot>) invocationOnMock -> {
            TimeSlot timeSlot = invocationOnMock.getArgument(0);
            autoIncrement++;
            timeSlot.setId(autoIncrement);

            timeSlots.add(timeSlot);
            timeSlotsMap.put(timeSlot.getStartDate(), timeSlot);

            return timeSlot;
        });

        TimeSlotService timeSlotService = new TimeSlotService(timeSlotRepository, null);
        return new TimeSlotController(timeSlotService);
    }

    public TimeSlotController createGetAvailableTimeSlots() {
        StylistRepository stylistRepository = Mockito.mock(StylistRepository.class);
        Mockito.when(stylistRepository.count()).thenAnswer(invocationOnMock -> (long)QueueProcessorMock.create().getStylistCount());

        TimeSlotRepository timeSlotRepository = Mockito.mock(TimeSlotRepository.class);
        Mockito.when(timeSlotRepository.findAllByActiveAppointmentsIsLessThan(any())).thenAnswer((Answer<Set<TimeSlot>>) invocationOnMock -> {
            Integer count = invocationOnMock.getArgument(0);
            return timeSlots.stream()
                    .filter(timeSlot->timeSlot.getActiveAppointments()<count)
                    .collect(Collectors.toSet());

        });

        TimeSlotService timeSlotService = new TimeSlotService(timeSlotRepository, stylistRepository);
        return new TimeSlotController(timeSlotService);
    }

    public void save(TimeSlot timeSlot) {
        timeSlotsMap.put(timeSlot.getStartDate(), timeSlot);

    }
}
