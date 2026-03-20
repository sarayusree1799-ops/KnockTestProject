package com.knock.utils;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.RandomGeneratorTestData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;

public class CommonUtils {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static boolean areListItemsExistInArray(HashSet<String> stringSet, List<String> list) {
        HashSet<String> flattenedSet = uniquelyFlatten(list);
        for (String item : flattenedSet) {
            if (!stringSet.contains(item)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areArrayEqualsToList(String[] array, List<String> list) {
        return array.length == list.size() && Arrays.equals(array, list.stream().sorted().toArray(String[]::new));
    }

    public static String generatePhoneNumber(String... obj) {
        if (obj.length > 0) {
            return "8055" + RandomGeneratorTestData.GenerateRandomNumber(6);
        } else {
            return "1234" + RandomGeneratorTestData.GenerateRandomNumber(6);
        }
    }

    public static String generatePhoneNumber() {
        return "1234" + RandomGeneratorTestData.GenerateRandomNumber(6);
    }

    public static String formatUSPhoneNumber(String phoneNumber) {
        String cleanedPhoneNumber = phoneNumber.replaceAll("[^0-9+]", "");
        if (cleanedPhoneNumber.startsWith("+1")) {
            cleanedPhoneNumber = cleanedPhoneNumber.substring(2);
        } else if (cleanedPhoneNumber.length() == 11 && cleanedPhoneNumber.startsWith("1")) {
            cleanedPhoneNumber = cleanedPhoneNumber.substring(1);
        }
        if (cleanedPhoneNumber.length() != 10) {
            return "Invalid US phone number";
        }
        return "(" + cleanedPhoneNumber.substring(0, 3) + ") " + cleanedPhoneNumber.substring(3, 6) + "-" +
                cleanedPhoneNumber.substring(6);
    }

    public static String generateRandomValue() {
        return "abcdd" + RandomGeneratorTestData.GenerateRandomNumber(10);
    }

    public static String generateRandomAlphabet(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public static String currentRandomEpoch() {
        return System.currentTimeMillis() / 1000L + RandomGeneratorTestData.GenerateRandomNumber(4);
    }

    public static HashSet<String> uniquelyFlatten(List<?> list) {
        HashSet<String> uniqueStrings = new HashSet<>();
        for (Object element : list) {
            if (element instanceof List<?>) {
                uniqueStrings.addAll(uniquelyFlatten((List<?>) element));
            } else if (element instanceof String) {
                uniqueStrings.add(((String) element).toLowerCase());
            }
        }
        return uniqueStrings;
    }

    public static boolean hashMapComparator(Map expectedMap, Map actualMap) {
        if (expectedMap != null && actualMap != null) {
            List<Boolean> resultList = new ArrayList<>();
            Set<String> mapKey = expectedMap.keySet();
            if (expectedMap.keySet().size() != actualMap.keySet().size()) {
                return false;
            }
            for (String key : mapKey) {
                if (expectedMap.get(key).equals(actualMap.get(key))) {
                    resultList.add(true);
                } else {
                    resultList.add(false);
                    AtuReports.failResults("Hashmap does not match", actualMap.get(key).toString(),
                            expectedMap.get(key).toString(), actualMap.get(key).toString());
                    throw new RuntimeException("\nMismatch in : \n " + expectedMap + "\n For Key => " + key
                            + "\nExpected Value: " + expectedMap.get(key) + "\nActual Value: " + actualMap.get(key));
                }
            }
            return !resultList.contains(false);
        }
        return false;
    }

    /**
     * PURPOSE: To Convert Milliseconds to HHMMSS format RETURN VALUE: HHMMSS format
     * INPUT(s): Milli Seconds
     *
     * @param diffSeconds
     **/
    public static String formatIntoHHMMSS(long diffSeconds) {
        // Convert Milli Seconds into Seconds
        diffSeconds = diffSeconds / 1000;

        long hours = diffSeconds / 3600;
        long remainder = diffSeconds % 3600;
        long minutes = remainder / 60;
        long seconds = remainder % 60;

        return (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
                + (seconds < 10 ? "0" : "") + seconds;
    }

    /**
     * Convert the integer to decimal in String format
     * For example: Input-> stringToConvert = 1 and numberOfDecimal = 3
     * Output-> 1.000
     * @param stringToConvert
     * @param numberOfDecimal
     * @return
     */
    public static String convertStringToDecimal(String stringToConvert, int numberOfDecimal) {
        Float stringToFloat = Float.parseFloat(stringToConvert);
        DecimalFormat df = new DecimalFormat("0.0000");
        df.setMaximumFractionDigits(numberOfDecimal);
        return df.format(stringToFloat);
    }

    public static boolean compareTextAndSkipFail(String actualText, String expectedText, String logMessage) {
        if (actualText.equals(expectedText)) {
            AtuReports.passResults("Compare " + logMessage, " -- ", actualText, expectedText);
            return true;
        } else {
            return false;
        }
    }

    public static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}