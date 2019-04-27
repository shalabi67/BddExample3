package com.bdd.slots;

public class TimeSlotData {
    private int days;  // number of days to create
    private String startDate;
    private int duration;  //for future use, will be neglected

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
