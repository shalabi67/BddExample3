package com.bdd.framework;

import cucumber.api.junit.Cucumber;
import org.junit.runners.model.InitializationError;

public class UnitCucumber extends Cucumber {
    public UnitCucumber(Class clazz) throws InitializationError {
        super(clazz);
        SystemFactory.setTestingType(TestingTypeEnum.UnitTesting);
    }
}
