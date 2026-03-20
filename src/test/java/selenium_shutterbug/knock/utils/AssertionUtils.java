package selenium_shutterbug.knock.utils;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter;
import org.testng.Assert;

public class AssertionUtils {

    public static void assertTrue(String message, boolean condition) {
        try {
            Assert.assertTrue(condition, message);
            AtuReports.passResults1("Verified the true condition " + message, "", "The expected value is " + true,
                    "The actual value is " + condition);
            Reporter.LogEvent(Reporter.TestStatus.PASS, "The expected value is " + true,
                    "The actual value is " + condition, message);
        } catch (Throwable e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "The expected value is " + true,
                    "The actual value is " + condition, message);
            AtuReports.failResults("Failed to verify the true condition " + message, "",
                    "The expected value is " + true, "The actual value is " + condition);
            throw new AssertionError(e);
        }
    }

    public static void assertFalse(String message, boolean condition) {
        try {
            Assert.assertFalse(condition, message);
            AtuReports.passResults1("Verified the false condition " + message, "", "The expected value is " + false,
                    "The actual value is " + condition);
            Reporter.LogEvent(Reporter.TestStatus.PASS, "The expected value is " + false,
                    "The actual value is " + condition, message);
        } catch (Throwable e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "The expected value is " + false,
                    "The actual value is " + condition, message);
            AtuReports.failResults("Failed to verify the false condition " + message, "",
                    "The expected value is " + false, "The actual value is " + condition);
            throw new AssertionError(e);
        }
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        try {
            Assert.assertEquals(expected, actual, message);
            AtuReports.passResults1("Verified the equals condition " + message, "",
                    "The expected value is " + expected, "The actual value is " + actual);
            Reporter.LogEvent(Reporter.TestStatus.PASS, "The expected value is " + expected,
                    "The actual value is " + actual, message);
        } catch (Throwable e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "The expected value is " + expected,
                    "The actual value is " + actual, message);
            AtuReports.failResults("Failed to verify the equals condition " + message, "",
                    "The expected value is " + expected, "The actual value is " + actual);
            throw new AssertionError(e);
        }
    }

    public static void assertNotEquals(String message, Object expected, Object actual) {
        try {
            Assert.assertNotSame(expected, actual, message);
            AtuReports.passResults1("Verified the not equals condition " + message, "",
                    "The expected value is " + expected, "The actual value is " + actual);
            Reporter.LogEvent(Reporter.TestStatus.PASS, "The expected value is " + expected,
                    "The actual value is " + actual, message);
        } catch (Throwable e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "The expected value is " + expected,
                    "The actual value is " + actual, message);
            AtuReports.failResults("Failed to verify the not equals condition " + message, "", "The expected value is" +
                    " '" + expected, "The actual value is " + actual);
            throw new AssertionError(e);
        }
    }

    public static void assertNotNull(String message, Object object) {
        try {
            Assert.assertNotNull(object, message);
            AtuReports.passResults1("Verified the not null condition", "", "", "The actual value is " + object);
            Reporter.LogEvent(Reporter.TestStatus.PASS, "", "The actual value is " + object, "Verified the not null " +
                    "condition");
        } catch (Throwable e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "", "The actual value is " + object, "Failed to verify the " +
                    "not null condition");
            AtuReports.failResults("Failed to verify the not null condition", "", "", "The actual value is " + object);
            throw new AssertionError(e);
        }
    }
}