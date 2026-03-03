package com.realpage.com.Reports.com.rp.reports.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Platform {
    private static BuildInfo driverInfo = new BuildInfo();
    public static final String DRIVER_VERSION;
    public static final String DRIVER_REVISION;
    public static final String USER;
    public static final String OS;
    public static final String OS_ARCH;
    public static final String OS_VERSION;
    public static final String JAVA_VERSION;
    public static String BROWSER_NAME;
    public static String BROWSER_VERSION;
    public static String BROWSER_NAME_PROP;
    public static String BROWSER_VERSION_PROP;

    public Platform() {
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var1) {
            return "Unknown";
        }
    }

    public static void prepareDetails(WebDriver paramWebDriver) {
        BROWSER_NAME = "Unknown";
        BROWSER_VERSION = "";
        if (paramWebDriver == null) {
            BROWSER_VERSION = "";
            BROWSER_NAME = "";
        } else {
            try {
                String str = (String) ((JavascriptExecutor) paramWebDriver).executeScript("return navigator.userAgent;", new Object[0]);
                if (str.contains("MSIE")) {
                    BROWSER_VERSION = str.substring(str.indexOf("MSIE") + 5, str.indexOf("Windows NT") - 2);
                    BROWSER_NAME = "Internet Explorer";
                } else if (str.contains("Firefox/")) {
                    BROWSER_VERSION = str.substring(str.indexOf("Firefox/") + 8);
                    BROWSER_NAME = "Mozilla Firefox";
                } else if (str.contains("Chrome/")) {
                    BROWSER_VERSION = str.substring(str.indexOf("Chrome") + 7, str.lastIndexOf("Safari/"));
                    BROWSER_NAME = "Google Chrome";
                } else if (str.contains("AppleWebKit") && str.contains("Version/")) {
                    BROWSER_VERSION = str.substring(str.indexOf("Version/") + 8, str.lastIndexOf("Safari/"));
                    BROWSER_NAME = "Apple Safari";
                } else {
                    if (str.contains("Opera/")) {
                        return;
                    }
                    BROWSER_VERSION = str.substring(str.indexOf("Version/") + 8);
                    BROWSER_NAME = "Opera";
                }
                getCapabilitiesDetails(paramWebDriver);
            } catch (Exception var4) {
                try {
                    getCapabilitiesDetails(paramWebDriver);
                    return;
                } catch (Exception var3) {
                    return;
                }
            }
            BROWSER_VERSION = "version " + BROWSER_VERSION;
        }
    }

    private static void getCapabilitiesDetails(WebDriver paramWebDriver) {
        Capabilities localCapabilities = ((RemoteWebDriver) paramWebDriver).getCapabilities();
        BROWSER_NAME_PROP = localCapabilities.getBrowserName();
        BROWSER_VERSION_PROP = localCapabilities.getBrowserVersion();
    }

    static {
        DRIVER_VERSION = driverInfo.getReleaseLabel();
        DRIVER_REVISION = driverInfo.getBuildRevision();
        USER = System.getProperty("user.name");
        OS = System.getProperty("os.name");
        OS_ARCH = System.getProperty("os.arch");
        OS_VERSION = System.getProperty("os.version");
        JAVA_VERSION = System.getProperty("java.version");
        BROWSER_NAME = "Unknown";
        BROWSER_VERSION = "";
        BROWSER_NAME_PROP = "BrowserName";
        BROWSER_VERSION_PROP = "BrowserVersion";
    }
}