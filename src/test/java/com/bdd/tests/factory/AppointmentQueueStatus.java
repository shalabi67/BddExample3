package com.bdd.tests.factory;
/*
this is identifies the status of an appointment after it processes by QueueProcesses
it is mapped to the three methods define in QueueProcessor: notifyFailException, notifyFail, notifySuccess
 */
public enum AppointmentQueueStatus {
    undefined,
    success,
    fail,
    exceptionFail
}
