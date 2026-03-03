package FrameWorkPackage.com.rp.automation.framework.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomGeneratorTestData {
    public static Random rand = new Random();

    public static String GenerateRandomNumber(int length) {
        StringBuffer sb = new StringBuffer("1");
        for(int i = 0; i < length - 1; i++) {
            sb.append("0");
        }
        long num = Long.parseLong(sb.toString());
        long maxnum = num * 10;
        Random rand = new Random();
        long randomNum = num + ((long)(rand.nextDouble() * (maxnum - num)));
        return String.valueOf(randomNum);
    }

    public static String GenerateRandomCapitalizedString(int length) {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        sb.append((char)(rand.nextInt(26) + 65));
        for(int i = 1; i < length; i++) {
            sb.append((char)(rand.nextInt(26) + 97));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static String GenerateRandomSmallLetters(int length) {
        StringBuffer output = new StringBuffer(1000);
        output.append((char)(rand.nextInt(26) + 97));
        System.out.println(output.toString());

        for(int i = 1; i < 10; ++i) {
            char c = (char)(rand.nextInt(26) + 97);
            output.append(c);
        }

        String RandSmallLetters = output.toString();
        return RandSmallLetters;
    }

    public static String GenerateRandomCapitalLetters(int length) {
        StringBuffer output = new StringBuffer(1000);
        output.append((char)(rand.nextInt(26) + 65));
        System.out.println(output.toString());

        for(int i = 1; i < 10; ++i) {
            char c = (char)(rand.nextInt(26) + 65);
            output.append(c);
        }

        String RandCapitalLetters = output.toString();
        return RandCapitalLetters;
    }

    public static String GenerateRandomAlphaNumericCharacters(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toString();
    }

    public static String GenerateRandomASCIICharacters(int length) {
        return RandomStringUtils.randomAscii(length).toString();
    }

    public static String GenerateRandomEMAILIDs() {
        String EmailID = RandomStringUtils.randomAlphabetic(15).toString();
        String Domain = RandomStringUtils.randomAlphabetic(7).toLowerCase().toString();
        return EmailID + "@" + Domain + ".com";
    }

    public static String GenerateRandomEMAILIDs(String DomainName) {
        String EmailID = RandomStringUtils.randomAlphabetic(15).toString();
        return EmailID + "@" + DomainName;
    }
}