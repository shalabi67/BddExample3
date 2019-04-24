package com.bdd.framework;

public class SystemFactory {
    private static TestingTypeEnum testingType;
    static void setTestingType(TestingTypeEnum testingType) {
        SystemFactory.testingType = testingType;
    }

    public static TestingTypeEnum getTestingType() {
        return testingType;
    }
}
