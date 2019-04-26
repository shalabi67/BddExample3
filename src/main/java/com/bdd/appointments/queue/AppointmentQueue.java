package com.bdd.appointments.queue;

import com.bdd.appointments.Appointment;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentQueue {
    private RabbitTemplate template;
    private Queue queue;

    public AppointmentQueue(RabbitTemplate template, Queue queue) {
        this.template = template;
        this.queue = queue;
    }

    public void send(Appointment appointment) {
        this.template.convertAndSend(queue.getName(), appointment);
    }
}
