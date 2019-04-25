package com.bdd.tests.factory;

import com.bdd.framework.Properties;
import com.bdd.framework.TestingTypeEnum;

public abstract class SystemFactory {
    public abstract CustomerSystem createCustomerSystem(Properties properties);
    public abstract StylistSystem createStylistSystem(Properties properties);

    private static SystemFactory systemFactory = null;
    public static SystemFactory create() {
        if(systemFactory != null) {
            return systemFactory;
        }

        systemFactory = SystemFactory.createSystemFactory(com.bdd.framework.SystemFactory.getTestingType());
        return systemFactory;
    }

    private static SystemFactory createSystemFactory(TestingTypeEnum testingTypeEnum) {
        switch (testingTypeEnum) {
            case IntegrationTesting: return new IntegrationSystemFactory();
            case UnitTesting: return new UnitSystemFactory();
            default: return null;
        }

    }
}
