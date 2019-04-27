package com.bdd.slots;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames="startDate"))
public class TimeSlot {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Date startDate;  //time slot start date and time
    private int duration;  // time slot duration, this is for future use.
    /*
    number of appointments on this slot. this should be always less than or equal the number of stylists
    the time slot is considered available if it is less than the number of stylists.
     */
    private int activeAppointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getActiveAppointments() {
        return activeAppointments;
    }

    public void setActiveAppointments(int activeAppointments) {
        this.activeAppointments = activeAppointments;
    }
}
