package com.bdd.tests.factory;

import com.bdd.framework.Properties;
import com.bdd.tests.unit.UnitCustomerSystem;

public class UnitSystemFactory extends SystemFactory{
    public CustomerSystem createCustomerSystem(Properties properties){
        return new UnitCustomerSystem();
    }
}
