package com.barcampevn.data.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andranikas on 5/17/2017.
 */

public class Time {
    private String date;
    private String timezone;
    private String timezone_type;
    private int day;

    public String getHourDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("HH:mm");
        String date = format.format(newDate);

        return date;
    }

    public int getDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd");
        String day = format.format(newDate);

        return Integer.valueOf(day);
    }

    public String getDate() {
        return date;
    }
}
