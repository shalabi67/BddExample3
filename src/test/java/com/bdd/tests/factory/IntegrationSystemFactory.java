package com.bdd.tests.factory;

import com.bdd.framework.Properties;
import com.bdd.tests.integration.*;

public class IntegrationSystemFactory extends SystemFactory{
    private static IntegrationAppointmentSystem appointmentSystem = null;
    private static IntegrationTimeSlotSystem timeSlotSystem = null;
    @Override
    public CustomerSystem createCustomerSystem(Properties properties){
        return new IntegrationCustomerSystem(properties.getMockMvc());
    }

    @Override
    public StylistSystem createStylistSystem(Properties properties) {
        return new IntegrationStylistSystem(properties.getMockMvc());
    }

    @Override
    public AppointmentSystem createAppointmentSystem(AppointmentProperties properties) {
        if(appointmentSystem == null) {
            appointmentSystem = new IntegrationAppointmentSystem(properties.getMockMvc(), properties.getQueueProcessor());
        }
        return appointmentSystem;
    }

    @Override
    public TimeSlotSystem createTimeSlotSystem(Properties properties) {
        if(timeSlotSystem == null) {
            timeSlotSystem = new IntegrationTimeSlotSystem(properties.getMockMvc());
        }
        return timeSlotSystem;
    }
}
