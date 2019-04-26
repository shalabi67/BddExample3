package com.bdd.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {
    public static final String QUEUE_NAME = "AppointmentQueue";


    @Bean
    public Queue appointmentsQueue() {
        return new Queue(QUEUE_NAME);
    }

}
