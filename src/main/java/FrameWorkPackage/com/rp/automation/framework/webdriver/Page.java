package FrameWorkPackage.com.rp.automation.framework.webdriver;

import FrameWorkPackage.com.rp.automation.framework.factory.BasePageFactory;
import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import javax.swing.*;
import java.util.HashSet;

public class Page {
    public WebDriver driver;
    public WebDriverHelper webDriverHelper;
    private static int invalidLinksCount;
    private static int validLinksCount;
    private static int connectionErrorUrl;
    public static String url = "";
    BasePageFactory pageFactory;
    public static String pageName;

    public static String getBaseUrl(String url) {
        return (String) WebDriverBase.context.getBean(url, String.class);
    }

    public Page() {
        if (this.driver == null) {
            this.driver = DriverManager.getDriver();
            this.webDriverHelper = new WebDriverHelper(this.driver);
            this.pageFactory = (new WebDriverBase()).getPageFactory();
        }
    }

    public HashSet<Cookie> getCookie() {
        return (HashSet) this.driver.manage().getCookies();
    }

    public String getCookie_() {
        String cookievalue = "";
        for (Cookie cook : this.driver.manage().getCookies()) {
            cookievalue = cookievalue + cook.getName() + "=" + cook.getValue() + ";";
        }
        return cookievalue;
    }

    public void click(WebElement pageElement, String logMessage) {
        try {
            pageElement.click();
            System.out.println("clicked");
            AtuReports.passResults1("Verify click action on: " + logMessage, "--", logMessage + " Should be clicked", logMessage + " is clicked");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify click action on: " + logMessage, logMessage + " Should be clicked", logMessage + " is clicked");
        } catch (Exception exception) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify click action on: " + logMessage, logMessage + " Should be clicked", this.catchException(exception));
        }

    }
}
