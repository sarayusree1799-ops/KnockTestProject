package FrameWorkPackage.com.rp.automation.framework.api;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.FlatMapUtil;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ApiCommonUtils {
    public static boolean isTrue(Boolean condition, String logMessage) {
        if (condition) {
            AtuReports.passResults("Verify Condition: ", logMessage, "T", "T");
            return true;
        } else {
            AtuReports.failResults("Verify Condition: ", logMessage, "F", "F");
            return false;
        }
    }

    public static boolean isTrue(Boolean condition, String uri, String resp, String schemaErr) {
        if (condition) {
            AtuReports.passResults("Verify schema: ", uri, resp, schemaErr);
            return true;
        } else {
            AtuReports.failResults("Verify schema: ", uri, resp, schemaErr);
            return false;
        }
    }

    public static boolean isTrue(String expected, String actual, String logMessage) {
        if (expected.contentEquals(actual)) {
            AtuReports.passResults("Verify Condition: ", logMessage, actual.toString(), expected.toString());
            return true;
        } else {
            AtuReports.failResults("Verify Condition: ", logMessage, actual.toString(), expected.toString());
            return false;
        }
    }

    public static boolean isTrue(List<String> expected, List<String> actual, String logMessage) {
        if (expected.equals(actual)) {
            AtuReports.passResults("Verify Condition: ", logMessage, actual.toString(), expected.toString());
            return true;
        } else {
            AtuReports.failResults("Verify Condition: ", logMessage, actual.toString(), expected.toString());
            return false;
        }
    }

    public static boolean isTrue(Object expected, Object actual, String logMessage) {
        boolean result = false;
        if (expected instanceof String) {
            if (expected.toString().trim().contentEquals(actual.toString().trim()))
                result = true;
        } else if (expected instanceof Double || expected instanceof Long) {
            if (expected.equals(actual))
                result = true;
        } else {
            if (expected == actual)
                result = true;
        }

        if (result) {
            AtuReports.passResults("Comparing two objects: ", logMessage, actual.toString(), expected.toString());
        } else {
            AtuReports.failResults("Comparing two objects: ", logMessage, actual.toString(), expected.toString());
        }

        return result;
    }

    public static boolean compareText(String actual, String expected, String logMessage) {
        boolean result = false;
        if (actual.equals(expected)) {
            result = true;
            AtuReports.passResults("Compare two texts: " + logMessage, "--", expected, actual);
        } else {
            AtuReports.failResults("Compare two texts: " + logMessage, "--", expected, actual);
        }
        return result;
    }

    public static boolean compareTextWithContains(String actual, String expected, String logMessage) {
        boolean result = false;
        if (actual.contains(expected)) {
            result = true;
            AtuReports.passResults("Compare two texts: " + logMessage, "--", expected, actual);
        } else {
            AtuReports.failResults("Compare two texts: " + logMessage, "--", expected, actual);
        }
        return result;
    }

    public static void verifyAllResults(List<Boolean> results, String msg) {
        System.out.println(results);
        if (results.size() < 0) {
            AtuReports.passResults(msg, "input:" + results.toString(),
                    "Expected:Must be all verification points are true", "all verification points are true");
        } else {
            boolean r = true;
            for (Boolean b : results) {
                if (!b)
                    r = false;
            }
            if (r)
                AtuReports.passResults(msg, "input:" + results.toString(),
                        "Expected:Must be all verification points are true", "all verification points are true");
            else
                AtuReports.failResults(msg, "input:" + results.toString(),
                        "Expected:Must be all verification points are true", "one or more verification points are false");
        }
    }

    public static void compareJsonStrObj(String biMetricsCodes, String apiResponseMetricsCodes) {
        Gson g = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> mapExpected = g.fromJson(biMetricsCodes, mapType);
        Map<String, Object> mapResponse = g.fromJson(apiResponseMetricsCodes.toString(), mapType);

        Map<String, Object> leftFlatMap = FlatMapUtil.flatten(mapExpected);
        Map<String, Object> rightFlatMap = FlatMapUtil.flatten(mapResponse);

        MapDifference<String, Object> difference = Maps.difference(leftFlatMap, rightFlatMap);

        if (difference.entriesDiffering().isEmpty()) {
            AtuReports.passResults("Verify the metrics/BI Entries differing", "",
                    " Expected metrics count difference are Zero :",
                    "Actual API call  metrics count difference are Zero :" + difference.entriesDiffering().isEmpty());
        } else {
            difference.entriesDiffering().forEach((key, value) -> System.out.println(key + ": " + value));
            difference.entriesDiffering()
                    .forEach((key, value) -> AtuReports.warning("Verify the metrics/BI Entries differing", "",
                            " Expected no difference :",
                            "Actual API call response has differences from database value as :" + key + ": " + value));
            AtuReports.failResults("over all the metrics/BI status ", "", " Expected metrics values are ",
                    " not matching with Actual API metrics lists");
        }
    }

    private String getConnectionUrl(JdbcTemplate jdbcTemplate, String portfolio) {
        String currentUrl = ((DriverManagerDataSource) jdbcTemplate.getDataSource()).getUrl();
        currentUrl = currentUrl.substring(0, currentUrl.lastIndexOf("/") + 1);
        currentUrl = currentUrl + portfolio;
        return currentUrl;
    }
}