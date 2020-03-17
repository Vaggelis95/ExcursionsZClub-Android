package com.zeustech.excursions.customViews;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class DateManager {

    public enum DateOfMonth {
        MAX,
        MIN
    }

    public DateManager() { }

    public static String convertDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(date);
    }

    public static String convertDate(String date, String oldFormat, String newFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(oldFormat, Locale.US);
        try {
            Date d = formatter.parse(date);
            formatter.applyPattern(newFormat);
            if (d != null) return formatter.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static CalendarDay dateToCalendarDay(@NonNull Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return CalendarDay.from(year, month, day);
    }

    public static int getValueFrom(@NonNull Date date, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(value);
    }

    public static CalendarDay textToCalendarDay(@NonNull String textDate, @NonNull String format) {
        Date date;
        try {
            date = new SimpleDateFormat(format, Locale.US).parse(textDate);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return dateToCalendarDay(date != null ? date : new Date());
    }

    @NonNull
    private static String dateToText(@NonNull Date date, @NonNull String format) {
        return new SimpleDateFormat(format, Locale.US).format(date);
    }

    private static Date getFirstDateOfMonth(int month) {
        return dateOfMonth(month, DateOfMonth.MIN);
    }

    public static String getFirstDateOfMonth(int month, @NonNull String format) {
        return dateToText(getFirstDateOfMonth(month), format);
    }

    private static Date getLastDateOfMonth(int month) {
        return dateOfMonth(month, DateOfMonth.MAX);
    }

    public static String getLastDateOfMonth(int month, @NonNull String format) {
        return dateToText(getLastDateOfMonth(month), format);
    }

    public static String getLastDateOfMonth(@NonNull Date date, @NonNull String format) {
        return getLastDateOfMonth(getValueFrom(date, Calendar.MONTH) + 1, format);
    }

    private static Date dateOfMonth(int month, @NonNull DateOfMonth date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        int d = (date == DateOfMonth.MAX) ?
                calendar.getActualMaximum(Calendar.DATE) : calendar.getActualMinimum(Calendar.DATE);
        calendar.set(Calendar.DATE, d);
        return calendar.getTime();
    }

    @NonNull
    private static Date getLastDateOfCurrentYear() {
        // Get a calendar instance
        Calendar calendar = Calendar.getInstance();
        // Get the last date of the current month. To get the last date for a
        // specific month you can set the calendar month using calendar object
        // calendar.set(Calendar.MONTH, theMonth) method.
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        int lastDate = calendar.getActualMaximum(Calendar.DATE);
        // Set the calendar date to the last date of the month so then we can
        // get the last day of the month
        calendar.set(Calendar.DATE, lastDate);
        // Print the current date and the last date of the month
        return calendar.getTime();
    }

    @NonNull
    private static Date getFirstDateOfCurrentMonth() {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static CalendarDay getLastDateOfYear() {
        return dateToCalendarDay(getLastDateOfCurrentYear());
    }

    public static CalendarDay getFirstDateOfMonth() {
        return dateToCalendarDay(getFirstDateOfCurrentMonth());
    }
}
