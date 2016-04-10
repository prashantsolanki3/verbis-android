package com.blackshift.verbis.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sarneet Singh on 4/6/2016.
 */
public class AlarmTopCard {
    @SerializedName("alarmTime")
    private String alarmTime;
    @SerializedName("amPm")
    private String amPm;
    @SerializedName("days")
    private List<String> days;


    public AlarmTopCard() {

    }
    public AlarmTopCard(String alarmTime, String amPm, List<String> days) {
        this.alarmTime = alarmTime;
        this.amPm = amPm;
        this.days = days;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAmPm() {
        return amPm;
    }

    public void setAmPm(String amPm) {
        this.amPm = amPm;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }
}
