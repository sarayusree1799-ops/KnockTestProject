package FrameWorkPackage.com.rp.automation.framework.webdriver;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    public static ThreadLocal<WebDriver> driverForThreaddr = new ThreadLocal<>();

    public static synchronized WebDriver getDriver() { return (WebDriver) driverForThreaddr.get(); }

    public static synchronized void setWebDriver(WebDriver driver) { driverForThreaddr.set(driver); }
}
