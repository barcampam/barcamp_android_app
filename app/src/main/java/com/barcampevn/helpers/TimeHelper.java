package com.barcampevn.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by andranikas on 5/19/2017.
 */

public final class TimeHelper {

    public static String hourDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("HH:mm");
         date = format.format(newDate);

        return date;
    }

    public static int day(String date) {
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

    public static Date dateFromHour(String hour) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(hour);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date date(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date dt = null;
        try {
            dt = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dt;
    }

    public static Date currentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDate = df.format(Calendar.getInstance().getTime());

        Date currentDate = null;
        try {
            currentDate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return currentDate;
    }

    public static boolean isAfter(Date left, Date right) {
        return !(left == null && right == null) && left.after(right);
    }

    public static boolean isBefore(Date left, Date right) {
        return !(left == null && right == null) && left.before(right);
    }

    public static boolean isEqual(Date left, Date right) {
        return !(left == null && right == null) && left.compareTo(right) == 0;
    }

    public static long delay(String time) {
        return TimeHelper.date(time).getTime() - new Date().getTime() - 600000;
    }

    public static long triggerAtMillis(long delay) {
        return System.currentTimeMillis() + delay;
    }
}
