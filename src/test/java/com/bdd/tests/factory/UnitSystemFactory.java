package com.bdd.tests.factory;

import com.bdd.framework.Properties;
import com.bdd.tests.integration.AppointmentProperties;
import com.bdd.tests.unit.UnitAppointmentSystem;
import com.bdd.tests.unit.UnitCustomerSystem;
import com.bdd.tests.unit.UnitStylistSystem;

public class UnitSystemFactory extends SystemFactory{
    @Override
    public CustomerSystem createCustomerSystem(Properties properties){
        return new UnitCustomerSystem();
    }

    @Override
    public StylistSystem createStylistSystem(Properties properties) {
        return new UnitStylistSystem();
    }

    @Override
    public AppointmentSystem createAppointmentSystem(AppointmentProperties properties) {
        return new UnitAppointmentSystem();
    }
}
