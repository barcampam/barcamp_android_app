package com.barcampevn.data.models;

/**
 * Created by andranikas on 5/17/2017.
 */

public class Schedule {
    int id;
    String room;
    Time time_to;
    Time time_from;
    Speaker en;
    Speaker hy;
    String bg_image_url;
    boolean isTime;
    boolean isDefault;

    public String getRoom() {
        return room;
    }

    public Time getTimeTo() {
        return time_to;
    }

    public Time getTimeFrom() {
        return time_from;
    }

    public Speaker getEnSpeaker() {
        return en;
    }

    public Speaker getHySpeaker() {
        return hy;
    }

    public String getBgImageUrl() {
        return bg_image_url;
    }

    public boolean isTime() {
        return isTime;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTime(boolean time) {
        isTime = time;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
