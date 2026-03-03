package com.knock.ui.base;

import FrameWorkPackage.com.rp.automation.framework.util.WaitType;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

    protected WebDriver driver;
    public WebDriverHelper webDriverHelper;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        webDriverHelper = new WebDriverHelper(driver);
    }

    public void waitForPageElement(WebElement element, WaitType waitType, String logMsg) {
        waitForPageElement(element, waitType, logMsg, 150);
    }

    public void waitForPageElement(WebElement element, WaitType waitType, String logMsg, int timeOut) {
        switch (waitType) {
            case WAIT_FOR_ELEMENT_TO_BE_CLICKABLE:
                webDriverHelper.waitForElementToBeClickable(element, timeOut);
                break;
            case WAIT_FOR_ELEMENT_TO_BE_DISPLAYED:
                webDriverHelper.waitForElementToBeDisplayed(element, timeOut);
                break;
            default:
                throw new IllegalArgumentException("Invalid WaitType: " + waitType);
        }
    }

    public static void explicitWait(int waitTime) {
        try {
            Thread.sleep(waitTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}