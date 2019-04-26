package com.bdd.tests.configuration;

import com.bdd.appointments.queue.QueueProcessor;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentConfiguration {
    @SpyBean
    QueueProcessor queueProcessor;
}
