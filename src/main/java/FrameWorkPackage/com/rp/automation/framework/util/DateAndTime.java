package FrameWorkPackage.com.rp.automation.framework.util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {
    public static String getTime() throws Exception {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk.mm");
        String TimeNow = dateFormat.format(date);
        return TimeNow;
    }

    public static String getDate() throws Exception {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String DateNow = dateFormat.format(date);
        return DateNow;
    }

    public static int getMonth_Integer() throws Exception {
        DateTime dateTime = DateTime.now();
        int month = dateTime.getMonthOfYear();
        return month;
    }

    public static String getMonth_Full() throws Exception {
        DateTime dateTime = DateTime.now();
        String month_Full = dateTime.monthOfYear().getAsText();
        return month_Full;
    }

    public static String getMonth_Short() throws Exception {
        DateTime dateTime = DateTime.now();
        String month_Short = dateTime.monthOfYear().getAsShortText();
        return month_Short;
    }

    public static String getDayOfTheMonth() throws Exception {
        DateTime dateTime = DateTime.now();
        String dayOfTheMonth = dateTime.dayOfMonth().getAsText();
        return dayOfTheMonth;
    }

    public static String getDayCount() throws Exception {
        DateTime dateTime = DateTime.now();
        String dayCountYear = dateTime.dayOfMonth().getAsText();
        return dayCountYear;
    }

    public static String getMinuteOfTheHourAsString() throws Exception {
        DateTime dateTime = DateTime.now();
        String minuteOfTheYear = dateTime.minuteOfHour().getAsText();
        return minuteOfTheYear;
    }

    public static int getYear() throws Exception {
        DateTime dateTime = DateTime.now();
        int year = dateTime.getYear();
        return year;
    }

    public static String getHourOfTheDay() throws Exception {
        DateTime dateTime = DateTime.now();
        String hour = dateTime.hourOfDay().getAsShortText();
        return hour;
    }

    public static String getWeekCount() throws Exception {
        DateTime dateTime = DateTime.now();
        String hour = dateTime.weekOfWeekyear().getAsText();
        return hour;
    }
}