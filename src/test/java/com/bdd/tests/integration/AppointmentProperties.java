package com.bdd.tests.integration;

import com.bdd.appointments.queue.QueueProcessor;
import com.bdd.framework.Properties;

public class AppointmentProperties extends Properties {
    private QueueProcessor queueProcessor;

    public QueueProcessor getQueueProcessor() {
        return queueProcessor;
    }

    public void setQueueProcessor(QueueProcessor queueProcessor) {
        this.queueProcessor = queueProcessor;
    }
}
