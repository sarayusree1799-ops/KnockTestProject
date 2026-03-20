package FrameWorkPackage.com.rp.automation.framework.util;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;

import FrameWorkPackage.com.rp.automation.framework.util.Reporter.TestStatus;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class CommonUtils {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    /*
    takes the input and returns the map with owner, property combination
    Param: testDataProperties
    Returns
     */
    public static Map<String, String> parseTestData(String testDataProperties) {
        Map<String, String> result = new LinkedHashMap<>();
        String propsDB[] = testDataProperties.split(";");
        for (String prop : propsDB) {
            String[] data = prop.split("=");
            result.put(data[0].trim(), data[1].trim());
        }
        return result;
    }

    /*
    Params: count
    Return: random string
     */
    public static String generateRandomString(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    /*
     Params: n -- length of digits
     Return: random number
      */
    public static String generateRandomNumber(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new Random().nextInt(9 * m) + "".trim();
    }

    /*
    generating a random number in range
    Params: n
    Return: random number
     */
    public static int generateRandomNumberInRange(int n) {
        return new Random().nextInt(n);
    }

    public static String generateRandomNumberBetween1To100() {
        Random random = new Random();
        String randomNumber = String.valueOf(random.nextInt(100) + 1);
        return randomNumber;
    }

    /*
    Returns current year month Y2019M05 format
     */
    public static String generateCurrentYearMonth() {
        LocalDate currentDate = LocalDate.now();
        String month = currentDate.getMonthValue() < 10 ? "0" + currentDate.getMonthValue() : String.valueOf(currentDate.getMonthValue());
        String yearMonth = "Y" + currentDate.getYear() + "M" + month;
        System.out.println(yearMonth);
        return yearMonth;
    }

    public static String convertIntoSpecificFormat(String date1) throws ParseException {
        DateFormat format = new SimpleDateFormat("MMMMyyyy", Locale.ENGLISH);
        Date date = format.parse(date1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("'Y'yyyy'M'MM");
        String timeNow = dateFormat.format(date);
        System.out.println(timeNow);
        return timeNow;
    }

    public static boolean isNumeric(String str) {
        boolean result = true;
        try {
            int var2 = Integer.parseInt(str);
        } catch (Exception var4) {
            result = false;
        }
        if (!result) {
            try {
                Double value = Double.parseDouble(str);
                result = true;
                ;
            } catch (Exception var3) {
                result = false;
            }
        }
        return result;
    }

    public static boolean isPositive(double d) {
        return d >= (double) 0.0F;
    }

    public static boolean isNumberInRange(double uiVal_, double dbVal_, double range_, String msg) {
        System.out.println(msg + " ui/api or ui/db " + uiVal_ + ":" + dbVal_ + ":" + range_);
        float uiVal = (float) uiVal_;
        float dbVal = (float) dbVal_;
        float range = (float) range_;

        if (!isPositive((double) dbVal)) {
            dbVal *= -1.0F;
        }

        if (!isPositive((double) uiVal)) {
            uiVal *= -1.0F;
        }

        if (uiVal == dbVal) {
            AtuReports.passResults(
                    msg + " numbers are equal " + uiVal, "--", dbVal + " should be equal", uiVal + " are equal");
            Reporter.LogEvent(TestStatus.PASS, "Verify numbers are equal " + msg + uiVal, dbVal + " should be in equal", uiVal + " are equal");
            return true;
        } else if (uiVal < dbVal) {
            if (uiVal + range >= dbVal) {
                AtuReports.warning(msg + " numbers in range " + uiVal, "--", dbVal + " should be in range", uiVal + " is in range");
                Reporter.LogEvent(TestStatus.PASS, "Verify number in range for " + msg + " : " + uiVal, dbVal + " should be in", uiVal + " is in range");

                return true;
            } else {
                AtuReports.failResults(msg + "numbers not in range" + uiVal, "--", dbVal + " should be in range", uiVal + " is not in range");
                Reporter.LogEvent(TestStatus.PASS, "Verify number in range for " + msg + "--" + uiVal, dbVal + " should be in range", uiVal + " is not in range");
                return false;
            }
        } else if (uiVal > dbVal) {
            if (uiVal <= dbVal + range) {
                AtuReports.warning(msg + "numbers in range" + uiVal, "--", dbVal + " should be in range", uiVal + " is in range");
                Reporter.LogEvent(TestStatus.PASS, "Verify number in range for " + msg + "--" + uiVal, dbVal + " should be in range", uiVal + " is in range");
                return true;
            } else {
                AtuReports.failResults(msg + "numbers not in range" + uiVal, "--", dbVal + " should be in range", uiVal + " is not in range");
                Reporter.LogEvent(TestStatus.PASS, "Verify number in range for " + msg + "--" + uiVal, dbVal + " should be in range", uiVal + " is not in range");
                return false;
            }
        } else {
            return true;
        }
    }

    public static double round(double input, int points) {
        double result = (double) 0.0F;
        String positions = "";
        for (int i = 0; i < points; ++i) {
            positions = positions + "#";
        }
        DecimalFormat df = new DecimalFormat("#." + positions);
        result = Double.valueOf(df.format(input));
        return result;
    }

    public static Double asDouble(Object o) {
        Double val = null;
        if (o instanceof Number) {
            val = ((Number) o).doubleValue();
        } else if (o instanceof Long) {
            val = ((Number) o).doubleValue();
        }
        return val;
    }

    public static String dateFormat(String format, int days) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.add(5, days);
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = cal.getTime();
        result = dateFormat.format(date);
        return result;
    }

    public static long getDateMillisec(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(5, days);
        return cal.getTimeInMillis();
    }

    public static boolean isPositive_Negative(int n) {
        boolean result;
        if (n < 0) {
            result = true;
            System.out.println("negative number");
        } else {
            result = false;
        }
        return result;
    }

    public static double roundedValue(double input) {
        double result = (double) 0.0F;
        result = (double) Math.round(input);
        return result;
    }

    public static enum API_RESPONSE_TYPE {
        MAP,
        LIST,
        BOOLEAN;
    }

    public static enum API_REQUEST_TYPE {
        GET, POST
    }
}