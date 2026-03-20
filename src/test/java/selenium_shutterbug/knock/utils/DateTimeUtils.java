package selenium_shutterbug.knock.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DateTimeUtils {
    public static final Logger logger = LogManager.getLogger(DateTimeUtils.class);

    public static final String TIME_ZONE = "GMT";

    //This method will convert the datetime in specified format and validate it.
    public static boolean verifyDateFormat(String actualDate, String dateFormat, boolean isTimeZone) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            if (isTimeZone)
                sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
            date = sdf.parse(actualDate);
            logger.info("Actual Date : " + actualDate);
            logger.info("Formatted Date : " + sdf.format(date));
            if (actualDate.equalsIgnoreCase(sdf.format(date))) {
                date = null;
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String convertAnyTimeFormatToUTCTimeStamp(String time) {
        DateTime dateTime = new DateTime(time, DateTimeZone.UTC);
        return dateTime.toString();
    }

    /**
     * Formats a date as a string according to a specified format and time zone,
     * with an offset of a specified number of days.
     *
     * @param format the format to use for the date string (e.g. "yyyy-MM-dd HH:mm:ss")
     * @param offsetInDays the number of days to add to the current date (positive or negative)
     * @param timeZone the time zone to use for the date string (e.g. "America/New_York")
     * @return the formatted date string
     */
    public static String dateFormatAsPerTimeZone(String format, int offsetInDays, String timeZone) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        cal.add(Calendar.DATE, offsetInDays);
        Random rand = new Random();
        int randomHour = 7 + rand.nextInt(9);
        cal.set(Calendar.HOUR_OF_DAY, randomHour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        return df.format(cal.getTime());
    }

    public static String addMinutesToExistingTime(String format, String dateTime, int minutesToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime.plusMinutes(minutesToAdd).format(formatter);
    }

    public static long convertToEpoch(String actualTimeStamp, String dateFormat) {
        long epochTime = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            Date startDate = simpleDateFormat.parse(actualTimeStamp);
            epochTime = startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epochTime;
    }

    public static String updateTimestamp(String actualTimeStamp, String dateFormat, long timeInMilliSeconds) {
        long getEpochTime = convertToEpoch(actualTimeStamp, dateFormat) + timeInMilliSeconds;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String updateTime = simpleDateFormat.format(getEpochTime);
        return updateTime;
    }

    public static String removeAmPmFromDate(String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a"));
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = dateTime.format(outputFormatter);
        return formattedDate;
    }

    public static boolean compareDate(String dateFormat, String startDateStr, String endDateStr, String actualDateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            Date startDate = simpleDateFormat.parse(startDateStr);
            Date endDate = simpleDateFormat.parse(endDateStr);
            Date actualDate = simpleDateFormat.parse(actualDateStr);
            return !(actualDate.before(startDate) || actualDate.after(endDate));
        } catch (ParseException e) {
            return false;
        }
    }

    public static String convertTimeToNewTimeZone(String actualTimeStamp, String actualTimeFormat, String expectedTimeFormat,
                                                  String fromTimeZone, String toTimeZone) {
        //Convert actual time to local date and time
        LocalDateTime localDateTime = LocalDateTime.parse(actualTimeStamp, DateTimeFormatter.ofPattern(actualTimeFormat));

        //Set current TimeZone, for example UTC
        ZoneId zoneId = ZoneId.of(fromTimeZone);

        ZonedDateTime zoneDateTime = localDateTime.atZone(zoneId);

        //Convert the time to new TimeZone, for example PST
        ZonedDateTime newZoneDateTime = zoneDateTime.withZoneSameInstant(ZoneId.of(toTimeZone));

        //Set the expect time format of new TimeZone
        DateTimeFormatter format = DateTimeFormatter.ofPattern(expectedTimeFormat);
        return format.format(newZoneDateTime);
    }

    public static boolean verifyDateFormat(String dateString, DateTimeFormatter formatter) {
        try {
            formatter.parse(dateString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String convertToTimezone(String dateTime, String zoneIdOf) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime);
        ZonedDateTime desiredZoneDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(zoneIdOf));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return desiredZoneDateTime.format(formatter);
    }
    /**
     * Converts a date string from one format to another.
     * @param dateValue The date string to convert
     * @param currentPattern The current date format pattern (e.g., "MM/dd/yyyy")
     * @param newPattern The desired date format pattern (e.g., "yyyy-MM-dd")
     * @return The reformatted date string
     * @throws DateTimeParseException if the date string doesn't match the current pattern
     */

    public static String changeDateFormat(String dateValue, String currentPattern, String newPattern) {
        try {
            DateTimeFormatter currentFormat = DateTimeFormatter.ofPattern(currentPattern);
            DateTimeFormatter newFormat = DateTimeFormatter.ofPattern(newPattern);
            return LocalDate.parse(dateValue, currentFormat).format(newFormat);
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date: " + dateValue + " with pattern: " + currentPattern, e);
            throw new IllegalArgumentException("Failed to parse date: " + dateValue, e);
        }
    }

    /**
     * Generate one random timestamp between 7AM–5PM for a given zone and offset day,
     * aligned to 15‑minute intervals.
     *
     * @param zoneId   Time zone string (e.g., "America/Los_Angeles")
     * @param dayOffset Number of days offset from today (negative = past, positive = future, 0 = today)
     * @return Formatted timestamp string
     */
    public static ZonedDateTime generateRandomBusinessTimeInInterval(String zoneId, int dayOffset) {
        ZoneId zone = ZoneId.of(zoneId);

        // Target date (past or future)
        LocalDate targetDate = LocalDate.now(zone).plusDays(dayOffset);

        // Define start and end bounds
        LocalTime start = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(17, 0);

        // Total number of 15‑minute slots between 7AM and 5PM
        int slots = (int) (Duration.between(start, end).toMinutes() / 15);

        // Pick a random slot
        int randomSlot = ThreadLocalRandom.current().nextInt(slots + 1);

        // Compute the time at that slot
        LocalTime randomTime = start.plusMinutes(randomSlot * 15);

        // Build ZonedDateTime
        return ZonedDateTime.of(targetDate, randomTime, zone);
    }
}