package com.bdd.tests.factory;

import com.bdd.framework.Properties;
import com.bdd.tests.integration.IntegrationCustomerSystem;
import com.bdd.tests.integration.IntegrationStylistSystem;
import com.bdd.tests.unit.UnitCustomerSystem;

public class IntegrationSystemFactory extends SystemFactory{
    @Override
    public CustomerSystem createCustomerSystem(Properties properties){
        return new IntegrationCustomerSystem(properties.getMockMvc());
    }

    @Override
    public StylistSystem createStylistSystem(Properties properties) {
        return new IntegrationStylistSystem(properties.getMockMvc());
    }
}
