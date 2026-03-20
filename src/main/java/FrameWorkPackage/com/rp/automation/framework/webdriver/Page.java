package FrameWorkPackage.com.rp.automation.framework.webdriver;

import FrameWorkPackage.com.rp.automation.framework.factory.BasePageFactory;
import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter.TestStatus;
import FrameWorkPackage.com.rp.automation.framework.util.WaitType;

import org.openqa.selenium.support.ui.ExpectedConditions;
import selenium_shutterbug.assertthat.selenium_shutterbug.core.Capture;
import selenium_shutterbug.assertthat.selenium_shutterbug.core.Shutterbug;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


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
            Reporter.LogEvent(TestStatus.PASS, "Verify click action on: " + logMessage, logMessage + " Should be clicked", logMessage + " is clicked");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify click action on: " + logMessage, logMessage + " Should be clicked", this.catchException(exception));
        }
    }

    public void jsClick(WebElement pageElement, String logMessage) {
        try {
            ((JavascriptExecutor) this.driver).executeScript("arguments[0].click();", new Object[]{pageElement});
            System.out.println("clicked");
            AtuReports.passResults1("Verify click action on: " + logMessage, "--", logMessage + " Should be clicked", logMessage + " is clicked");
            Reporter.LogEvent(TestStatus.PASS, "Verify click action on: " + logMessage, logMessage + " Should be clicked", logMessage + " is clicked");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify click action on: " + logMessage, logMessage + " Should be clicked", this.catchException(exception));
            AtuReports.failResults("Verify click action on: " + logMessage, "--", logMessage + " Should be clicked", this.catchException(exception));
        }
    }

    public void sendKeys(WebElement pageElement, String value, String logMessage) {
        try {
            pageElement.sendKeys(new CharSequence[]{value});
            if (logMessage != null && logMessage.equalsIgnoreCase("IaPassWord")) {
                logMessage = "XXXXXXX";
            }
            AtuReports.passResults1("Verify text is entered to " + logMessage + " textbox", value, "Text '" + value + "' should be entered in to " + logMessage + " textbox", "Text '" + value + "' is entered in to " + logMessage + " textbox");
            Reporter.LogEvent(TestStatus.PASS, "Verify text is entered to " + logMessage + " textbox", "Text '" + value + "' should be entered in to " + logMessage + " textbox", "Text '" + value + "' is entered in to " + logMessage + " textbox");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text is entered to " + logMessage + " textbox", "Text '" + value + "' should be entered in to " + logMessage + " textbox", this.catchException(exception));
            AtuReports.failResults("Failed to enter text in " + logMessage + " text Field", value, "Text '" + value + "' should be entered in to " + logMessage + " text Field", this.catchException(exception));
        }
    }

    public void sendKeysJs(WebElement pageElement, String value, String logMessage) {
        try {
            JavascriptExecutor myExecutor = (JavascriptExecutor) this.driver;
            myExecutor.executeScript("arguments[0].value='" + value + "';", new Object[]{pageElement});
            if (logMessage != null && logMessage.equalsIgnoreCase("IaPassWord")) {
                logMessage = "XXXXXXX";
            }
            AtuReports.passResults1("Verify text is entered to " + logMessage + " textbox", value, "Text '" + value + "' should be entered in to " + logMessage + " textbox", "Text '" + value + "' is entered in to " + logMessage + " textbox");
            Reporter.LogEvent(TestStatus.PASS, "Verify text is entered to " + logMessage + " textbox", "Text '" + value + "' is entered in to " + logMessage + " textbox", "Text '" + value + "' is entered in to " + logMessage + " textbox");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text is entered to " + logMessage + " textbox", "Text '" + value + "' should be entered in to " + logMessage + " textbox", this.catchException(exception));
            AtuReports.failResults("Failed to Enter text in " + logMessage + " text Field", value, "Text " + value + " should be entered in to " + logMessage + " text Field", this.catchException(exception));
        }
    }

    protected void clear(WebElement pageElement, String logMessage) {
        try {
            pageElement.clear();
            AtuReports.passResults1("Clear text in: " + logMessage, "--", logMessage + " text should be cleared", logMessage + " text is be cleared");
            Reporter.LogEvent(TestStatus.PASS, "Clear test in: " + logMessage, logMessage + " text should be cleared", logMessage + " text is be cleared");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Clear test in: " + logMessage, logMessage + " text should be cleared", this.catchException(exception));
            AtuReports.failResults("Clear text in: " + logMessage, "--", logMessage + " text should be cleared", this.catchException(exception));
        }
    }

    public boolean isElementDisplayed(WebElement pageElement, String logMessage) {
        boolean isElementDisplayed = false;
        try {
            isElementDisplayed = pageElement.isDisplayed() || pageElement.isEnabled();
            AtuReports.passResults1("Verify element " + logMessage + " is Displayed", "--", logMessage + " Should be Displayed ", logMessage + " is Displayed");
            Reporter.LogEvent(TestStatus.PASS, "Verify element " + logMessage + " is Displayed", logMessage + " Should be Displayed ", logMessage + " is Displayed");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify element " + logMessage + " is Displayed", logMessage + " Should be Displayed ", this.catchException(exception));
            AtuReports.failResults(logMessage + " is not Displayed in " + this.getClass().getSimpleName(), "--", logMessage + " Should be Displayed ", this.catchException(exception));
        }
        return isElementDisplayed;
    }

    public boolean isElementDisplayed(String locator, String objectIdentifier, String logMessage) {
        boolean isElementNotDisplayed = false;
        int size = 0;
        if (objectIdentifier.equalsIgnoreCase("xpath")) {
            size = this.driver.findElements(By.xpath(locator)).size();
        } else if (objectIdentifier.equalsIgnoreCase("linkText")) {
            size = this.driver.findElements(By.linkText(locator)).size();
        } else if (objectIdentifier.equalsIgnoreCase("Id")) {
            size = this.driver.findElements(By.id(locator)).size();
        } else if (objectIdentifier.equalsIgnoreCase("Css")) {
            size = this.driver.findElements(By.cssSelector(locator)).size();
        }
        System.out.println(size + "size");
        if (size > 0) {
            isElementNotDisplayed = true;
            AtuReports.passResults1("Verify element" + logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ",
                    logMessage + " is not Displayed");
            Reporter.LogEvent(TestStatus.PASS, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ",
                    logMessage + " is not Displayed");
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ",
                    logMessage + " is Displayed");
            AtuReports.warning(logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ", logMessage + " is Displayed");
        }
        return isElementNotDisplayed;
    }

    public boolean isElementNotDisplayed(WebElement element, String logMessage) {
        boolean isElementNotDisplayed = false;
        int height = element.getSize().getHeight();
        int width = element.getSize().getWidth();
        if (height <= 0 && width <= 0) {
            isElementNotDisplayed = true;
            AtuReports.passResults1("Verify element" + logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ",
                    logMessage + " is not Displayed");
            Reporter.LogEvent(TestStatus.PASS, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ",
                    logMessage + " is not Displayed");
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ",
                    logMessage + " is Displayed");
            AtuReports.failResults("Verify element" + logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ",
                    logMessage + " is Displayed");
        }
        return isElementNotDisplayed;
    }

    public int isElementDisplayed(String locator) {
        int size = this.driver.findElements(By.xpath(locator)).size();
        return size;
    }

    public boolean isElementDisplayed(String locator, String logMessage) {
        boolean isElementNotDisplayed = false;
        int size = 0;
        size = this.driver.findElements(By.xpath(locator)).size();
        System.out.println(size + "size");
        if (size > 0) {
            isElementNotDisplayed = true;
            AtuReports.passResults1("Verify element" + logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ",
                    logMessage + " is not Displayed");
            Reporter.LogEvent(TestStatus.PASS, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ",
                    logMessage + " is not Displayed");
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ",
                    logMessage + " is Displayed");
            AtuReports.warning(logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ", logMessage + " is Displayed");
        }
        return isElementNotDisplayed;
    }

    public boolean isElementNotDisplayed(String locator, String objectIdentifier, String logMessage) {
        boolean isElementNotDisplayed = false;
        int size = 0;
        if (objectIdentifier.equalsIgnoreCase("xpath")) {
            size = this.driver.findElements(By.xpath(locator)).size();
        } else if (objectIdentifier.equalsIgnoreCase("linkText")) {
            size = this.driver.findElements(By.linkText(locator)).size();
        } else if (objectIdentifier.equalsIgnoreCase("Id")) {
            size = this.driver.findElements(By.id(locator)).size();
        } else if (objectIdentifier.equalsIgnoreCase("Css")) {
            size = this.driver.findElements(By.cssSelector(locator)).size();
        }
        System.out.println(size + "size");
        if (size > 0) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ", logMessage + " is Displayed");
            AtuReports.failResults(logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ", logMessage + " is Displayed");
        } else {
            isElementNotDisplayed = true;
            AtuReports.passResults1("Verify element" + logMessage + " is not Displayed", "--", logMessage + " Should not be Displayed ", logMessage + " is not Displayed");
            Reporter.LogEvent(TestStatus.PASS, "Verify element" + logMessage + " is not Displayed", logMessage + " Should not be Displayed ", logMessage + " is not Displayed");
        }
        return isElementNotDisplayed;
    }

    public boolean isElementClickable(WebElement pageElement, String logMessage) {
        boolean isElementSelected = false;

        try {
            pageElement.click();
            AtuReports.passResults1("Verify element " + logMessage + " is Selected", "--", logMessage + " Should be Selected ", logMessage + " is Selected");
            Reporter.LogEvent(TestStatus.PASS, "Verify element " + logMessage + " is Selected", logMessage + " Should be Selected ", logMessage + " is Selected");
            isElementSelected = true;
        } catch (Exception exception) {
            AtuReports.warning(logMessage + " is not Selected in" + this.getClass().getSimpleName(), "--", logMessage + " Should be Selected ", this.catchException(exception));
            isElementSelected = false;
        }
        return isElementSelected;
    }

    public boolean isElementPresent(WebElement pageElement, String logMessage) {
        boolean isElementDisplayed = true;
        try {
            if (!pageElement.isDisplayed() && !pageElement.isEnabled()) {
                boolean var7 = false;
            } else {
                boolean var10000 = true;
            }
            AtuReports.passResults1("Verify element " + logMessage + " is Displayed", "--", logMessage + " Should be Displayed ", logMessage + " is Displayed");
            Reporter.LogEvent(TestStatus.PASS, "Verify element " + logMessage + " is Displayed", logMessage + " Should be Displayed ", logMessage + " is Displayed");
            isElementDisplayed = true;
        } catch (Exception exception) {
            AtuReports.warning(logMessage + " is not Displayed in " + this.getClass().getSimpleName(), "--", logMessage + " Should be Displayed ", this.catchException(exception));
            isElementDisplayed = false;
        }
        return isElementDisplayed;
    }

    public void verifyElementNotPresent(WebElement pageElement, String logMessage) {
        try {
            if (pageElement.isDisplayed() || pageElement.isEnabled()) {
                Reporter.LogEvent(TestStatus.FAIL, "Verify element is displayed: " + logMessage, "" + logMessage + " Should not be Displayed", "");
                AtuReports.failResults("Verify " + logMessage + " is not present", "" + logMessage + " should not be present", "", "" + logMessage + " is Displayed");
            }
        } catch (Exception exception) {
            if (!exception.getLocalizedMessage().contains("no such element") && !exception.getLocalizedMessage().contains("no_such_element")) {
                Reporter.LogEvent(TestStatus.FAIL, "Verify element is displayed: " + logMessage, "" + logMessage + " Should not be Displayed", "");
                AtuReports.failResults("Verify " + logMessage + " is not present", "--" + logMessage + " should not be present", "", "" + logMessage + " is Displayed");
            } else {
                Reporter.LogEvent(TestStatus.PASS, "Verify element is displayed: " + logMessage, "" + logMessage + " Should not be Displayed", "" + logMessage + " is not Displayed");
                AtuReports.passResults1("Verify " + logMessage + " is not present", "--" + logMessage + " should not be present", "", "" + logMessage + " is not Displayed");
            }
        }
    }

    public void switchToFrameByName(String frameName) {
        try {
            this.webDriverHelper.WAIT_FOR_FRAME_TO_BE_DISPLAYED(this.driver, 180, frameName);
            AtuReports.passResults1("Verify frame is: " + frameName, "", "Frame Should be Switched to: " + frameName, "Frame is be Switched to: " + frameName);
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is displayed: " + frameName, "Frame Should be Switched to: " + frameName, "Frame is Switched to: " + frameName);
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify frame is displayed: " + frameName, "Frame Should be Switched to: " + frameName, this.catchException(exception));
            AtuReports.failResults("Verify " + frameName + " is selected", "--", "Frame Should be Switched to :" + frameName, this.catchException(exception));
        }
    }

    public void switchToFrameByIndex(int frameNo, String frameName) {
        try {
            this.webDriverHelper.WaitForFrametoBeDisplayedByIndex(this.driver, 180, frameNo);
            AtuReports.passResults1("Verify frame is: " + frameName, "", "Frame Should be Switched to: " + frameName, "Frame is be Switched to: " + frameName);
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is displayed: " + frameName, "Frame Should be Switched to: " + frameName, "Frame is Switched to: " + frameName);
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify frame is displayed: " + frameName, "Frame Should be Switched to: " + frameName, this.catchException(exception));
            AtuReports.failResults("Verify " + frameName + " is selected", "--", "Frame Should be Switched to :" + frameName, this.catchException(exception));
        }
    }

    public boolean switchToFrameByIndex(int frameNo, String frameName, int timeOut) {
        try {
            this.webDriverHelper.WaitForFrametoBeDisplayedByIndex(this.driver, timeOut, frameNo);
            AtuReports.passResults1("Verify frame is: " + frameName, "", "Frame Should be Switched to: " + frameName, "Frame is be Switched to: " + frameName);
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is displayed: " + frameName, "Frame Should be Switched to: " + frameName, "Frame is Switched to: " + frameName);
            return true;
        } catch (Exception exception) {
            AtuReports.info("Verify " + frameName + " is selected", "--", "Frame Should be Switched to :" + frameName, "Frame is be Switched to :" + frameName);
            this.catchException(exception);
            return false;
        }
    }

    public void switchToFrameById(String frameId) {
        try {
            this.webDriverHelper.WAIT_FOR_FRAME_TO_BE_DISPLAYED(this.driver, 180, By.id(frameId));
            AtuReports.passResults1("Verify frame is: " + frameId, "", "Frame Should be Switched to: " + frameId, "Frame Should be Switched to :" + frameId);
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is displayed: " + frameId, "Frame Should be Switched to: " + frameId, "Frame is Switched to: " + frameId);
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify frame is displayed: " + frameId, "Frame Should be Switched to: " + frameId, this.catchException(exception));
            AtuReports.failResults("Verify " + frameId + " is selected", "--", "Frame Should be Switched to :" + frameId, this.catchException(exception));
        }
    }

    public void switchToFrameByXpath(String frame) {
        try {
            this.webDriverHelper.WAIT_FOR_FRAME_TO_BE_DISPLAYED(this.driver, 180, By.xpath("//iframe[contains(@id,'" + frame + "')]"));
            AtuReports.passResults1("Verify frame is: " + frame, "", "Frame Should be Switched to: " + frame, "Frame is  Switched to: " + frame);
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is displayed: " + frame, "Frame Should be Switched to: " + frame, "Frame is  Switched to: " + frame);
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify frame is displayed: " + frame, "Frame Should be Switched to: " + frame, this.catchException(exception));
            AtuReports.failResults("Verify " + frame + " is selected", "--", "Frame Should be Switched to :" + frame, this.catchException(exception));
        }
    }

    public void switchToFrameBy(String frameId) {
        try {
            this.webDriverHelper.WAIT_FOR_FRAME_TO_BE_DISPLAYED(this.driver, 180, By.id(frameId));
            AtuReports.passResults1("Verify frame is: " + frameId, "", "Frame Should be Switched to: " + frameId, "Frame is  Switched to: " + frameId);
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is displayed: " + frameId, "Frame Should be Switched to: " + frameId, "Frame is  Switched to: " + frameId);
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify frame is displayed: " + frameId, "Frame Should be Switched to: " + frameId, this.catchException(exception));
            AtuReports.failResults("Verify " + frameId + " is selected", "--", "Frame Should be Switched to :" + frameId, this.catchException(exception));
        }
    }

    public void switchToDefault() {
        try {
            this.driver.switchTo().defaultContent();
            AtuReports.passResults1("Verify frame is switched to default content", "", "Frame should be switched to default content", "Frame is switched to default content");
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is switched to default content", "Frame should be switched to default content", "Frame is switched to default content");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify frame is switched to default content", "Frame should be switched to default content", this.catchException(exception));
            AtuReports.failResults("Verify frame is switched to default content", "", "Frame should be switched to default content", "frame is not switched to default content");
        }
    }

    public String switchToWindow() {
        String windowname = null;
        try {
            Set<String> WindowHandles = this.driver.getWindowHandles();
            Iterator<String> LoopIterator = WindowHandles.iterator();
            System.out.println("WindowHandles====" + WindowHandles);

            while (LoopIterator.hasNext()) {
                windowname = this.driver.switchTo().window((String) LoopIterator.next()).getTitle();
                System.out.println("windowname====" + windowname);
            }
            AtuReports.passResults1("Switch to : " + windowname, "", "Window Should be Switched to " + windowname, "Window is Switched to " + windowname);
            Reporter.LogEvent(TestStatus.PASS, "Switch to : " + windowname, "Window Should be Switched to " + windowname, "Window is Switched to " + windowname);
            System.out.println("Window name is " + windowname);
            return windowname;
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Switch to : " + windowname, "Window Should be Switched to " + windowname, this.catchException(exception));
            AtuReports.failResults("Switch to : " + windowname, "", "Window Should be Switched to " + windowname, this.catchException(exception));
            return windowname;
        }
    }

    public void switchToChildWindow() {
        String pWindow = this.driver.getWindowHandle();
        Date date1 = new Date();
        Long l1 = date1.getTime();
        int i = 0;
        while (true) {
            Set<String> WindowHandles = this.driver.getWindowHandles();
            if (WindowHandles.size() == 2) {
                for (String cWindow : WindowHandles) {
                    if (!pWindow.equals(cWindow)) {
                        this.driver.switchTo().window(cWindow);
                        AtuReports.passResults1("Switch to Report", "--", "Window should be switched to Report", "Switched to Report");
                    }
                }
                return;
            }
            Date date2 = new Date();
            Long l2 = date2.getTime();
            Long l3 = l2 - l1;
            Long timeDiff = l3 / 1000L;
            if (timeDiff > 180L) {
                AtuReports.failResults("Switch to Report", "--", "Window should be switched to Report", "Error in Report");
                return;
            }
            ++i;
        }
    }

    public boolean closeWindowByTitle(String windowTitle) {
        boolean isWindowSelected = false;
        try {
            explicitWait(10);
            for (String windowname : this.driver.getWindowHandles()) {
                this.driver.switchTo().window(windowname);
                this.waitForPageLoad();
                String wTitleEncoded = this.driver.getTitle();
                System.out.println(wTitleEncoded);
                String wTitleEncoded1 = this.driver.getCurrentUrl();
                String wTitle = URLDecoder.decode(wTitleEncoded1, "UTF-8");
                if (wTitle.contains(windowTitle) || wTitleEncoded.contains(windowTitle)) {
                    isWindowSelected = true;
                    this.driver.close();
                    break;
                }
            }
        } catch (Exception exception) {
            AtuReports.failResults("Failed to switch to window : " + windowTitle, windowTitle, " window " + windowTitle + " should be selected", this.catchException(exception));
        }

        if (isWindowSelected) {
            AtuReports.passResults1("Switch to window : " + windowTitle, windowTitle, " window " + windowTitle + " should be selected", windowTitle + " is selected");
        } else {
            AtuReports.failResults("Failed to switch to window : " + windowTitle, windowTitle, " window " + windowTitle + " should be selected", windowTitle + " is not selected");
        }

        return isWindowSelected;
    }

    public void selectListBox(WebElement pageElement, String value, String by, String logMessage) {
        try {
            Select select = new Select(pageElement);
            if (by.equalsIgnoreCase("ByVisibleText")) {
                select.selectByVisibleText(value);
            } else if (by.equalsIgnoreCase("Index")) {
                select.selectByIndex(Integer.parseInt(value));
            } else if (by.equalsIgnoreCase("value")) {
                select.selectByValue(value);
            } else {
                select.selectByVisibleText(value);
            }

            AtuReports.passResults1("Select: " + logMessage, value, logMessage + " Should be selected", logMessage + " is selected");
            Reporter.LogEvent(TestStatus.PASS, "Select: " + logMessage, logMessage + " Should be selected", logMessage + " is selected");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Select: " + logMessage, logMessage + " Should be selected", this.catchException(exception));
            AtuReports.failResults("Failed to select : " + logMessage, value, logMessage + " Should be selected", this.catchException(exception));
        }
    }

    public void verifySelectedValue(WebElement pageElement, String expectedValue) {
        String actualValue = this.getSelectedValueFromListBox(pageElement, expectedValue);
        actualValue = actualValue.replaceAll("\n", "").replaceAll(" +", " ");
        if (actualValue.trim().equals(expectedValue.trim())) {
            AtuReports.passResults1("Verify Selected Report ", "--", expectedValue, actualValue);
            Reporter.LogEvent(TestStatus.PASS, "Verify Selected Report ", expectedValue, actualValue);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify Selected Report ", expectedValue, actualValue);
            AtuReports.failResults("Verify Selected Report ", "--", expectedValue, actualValue);
        }
    }

    public String getSelectedValueFromListBox(WebElement pageElement, String logMessage) {
        String selectedValue = null;

        try {
            Select select = new Select(pageElement);
            selectedValue = select.getFirstSelectedOption().getText();
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Get selected option: " + logMessage, "Selected option should be returned",
                    this.catchException(exception));
            AtuReports.failResults("Get selected option: " + logMessage, "--", "Selected option should be returned",
                    this.catchException(exception));
        }

        return selectedValue;
    }

    public ArrayList<String> getAllValuesFromListBox(WebElement pageElement, String logMessage) {
        ArrayList<String> optionList = new ArrayList<>();

        try {
            Select select = new Select(pageElement);

            for (WebElement options : select.getOptions()) {
                optionList.add(options.getText().trim());
            }
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Get all values from the: " + logMessage, "All values should be retuned form the " + logMessage,
                    this.catchException(exception));
            AtuReports.failResults("Get all values from the: " + logMessage, "--", "All values should be retuned form the " + logMessage,
                    this.catchException(exception));
        }

        return optionList;
    }

    public boolean switchToWindow(String windowTitle) {
        boolean isWindowSelected = false;

        try {
            explicitWait(10);

            for (String windowname : this.driver.getWindowHandles()) {
                this.driver.switchTo().window(windowname);
                this.waitForPageLoad();
                String wTitleEncoded = this.driver.getTitle();
                System.out.println(wTitleEncoded);
                String wTitleEncoded1 = this.driver.getCurrentUrl();
                String wTitle = URLDecoder.decode(wTitleEncoded1, "UTF-8");
                if (wTitle.contains(windowTitle) || wTitleEncoded.contains(windowTitle)) {
                    isWindowSelected = true;
                    break;
                }
            }
        } catch (Exception exception) {
            AtuReports.failResults("Failed to switch to window : " + windowTitle, windowTitle, " window " + windowTitle + " should be selected", this.catchException(exception));
        }

        if (isWindowSelected) {
            AtuReports.passResults1("Switch to window : " + windowTitle, windowTitle, " window " + windowTitle + " should be selected", windowTitle + " is selected");
        } else {
            AtuReports.failResults("Failed to switch to window : " + windowTitle, windowTitle, " window " + windowTitle + " should be selected", windowTitle + " is not selected");
        }

        return isWindowSelected;
    }

    public boolean switchToNewlyOpenedWindow(Set<String> previousWindows, Set<String> currentWindows) {
        boolean isWindowSelected = false;
        try {
            currentWindows.removeAll(previousWindows);
            this.driver.switchTo().window(currentWindows.toArray()[0].toString());
            isWindowSelected = true;
        } catch (Exception exception) {
            AtuReports.failResults("Failed to switch to new window : ", "", "New window should be selected", this.catchException(exception));
        }
        if (isWindowSelected) {
            AtuReports.passResults1("Switch to window : " + this.driver.getTitle(), this.driver.getTitle(), " window " + this.driver.getTitle() + " should be selected", this.driver.getTitle() + " is selected");
        } else {
            AtuReports.failResults("Failed to switch to window : " + this.driver.getTitle(), this.driver.getTitle(), " window " + this.driver.getTitle() + " should be selected", this.driver.getTitle() + " is not selected");
        }
        return isWindowSelected;
    }

    public String switchToWindows(String windowTitle) {
        String windowname = "";
        String wTitle = null;
        boolean isWindowSelected = false;
        int count = 0;
        String mainWindow = null;
        try {
            int i = 0;
            while (true) {
                Set<String> WindowHandles = this.driver.getWindowHandles();
                System.out.println("WindowHandles==" + WindowHandles);
                for (String windowname1: WindowHandles) {
                    System.out.println("windowName==" + windowname);
                    this.driver.switchTo().window(windowname1);
                    wTitle = this.driver.getTitle();
                    System.out.println("wTitle===" + wTitle);
                    if (wTitle.contains(windowTitle)) {
                        isWindowSelected = true;
                        windowname = windowname1;
                        break;
                    }

                    ++count;
                    if (count == 40) {
                        break;
                    }
                }
                System.out.println("count==" + count);
                if (isWindowSelected) {
                    AtuReports.passResults1("Switch to: " + wTitle, "--", "Window Should switch to " + wTitle, "Window is Switched to " + wTitle);
                    Reporter.LogEvent(TestStatus.PASS, "Switch to :" + wTitle, "Window Should Switch to " + wTitle, "Window Should Switch to " + wTitle);
                } else {
                    AtuReports.info("Parent window is selected: " + windowname, windowname, " window " + windowTitle + " should be selected", windowTitle + " is selected");
                    Reporter.LogEvent(TestStatus.INFO, "Parent window is selected: " + windowname, " window " + windowTitle + " should be selected", windowTitle + " is selected");
                }

                ++i;
            }
        } catch (NoSuchWindowException exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Switch to: " + wTitle, "Window Should be Switched to " + wTitle, this.catchException(exception));
            AtuReports.failResults("Switch to: " + wTitle, "--", "Window Should be Switched to " + wTitle, this.catchException(exception));
            return wTitle;
        }
    }

    public void waitForPageLoad() {
        this.webDriverHelper.WaitForPageLoad(180);
    }

    public void waitForPageElement(By by, String waitType, String logMessage) {
        WaitType waitTypes = WaitType.valueOf(waitType);
        switch (waitTypes) {
            case WAIT_FOR_TEXT_TO_BE_ABSENT_IN_ELEMENT:
                try {
                    System.out.println("text: " + this.driver.findElement(by).getText());
                    this.webDriverHelper.waitForTextToBeAbsentInElement(by, logMessage, 180);
                    AtuReports.passResults("Verify element text is disappeared " + logMessage, "--", logMessage + " should be disappeared ", logMessage + " is disappeared");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element text is disappeared " + logMessage, logMessage + " should be disappeared ",
                            logMessage + " is disappeared");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element text is disappeared " + logMessage, logMessage + " should be disappeared ",
                            this.catchException(e));
                    AtuReports.failResults("Verify element is disappeared " + logMessage, "--", logMessage + " should be disappeared ",
                            this.catchException(e));
                }
            default:
        }
    }

    public void waitForPageElement(By by, WaitType waitType, String logMessage) {
        switch (waitType) {
            case WAIT_FOR_TEXT_TO_BE_ABSENT_IN_ELEMENT:
                try {
                    System.out.println("text: " + this.driver.findElement(by).getText());
                    this.webDriverHelper.waitForTextToBeAbsentInElement(by, logMessage, 180);
                    AtuReports.passResults("Verify element text is disappeared " + logMessage, "--", logMessage + " should be disappeared ", logMessage + " is disappeared");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element text is disappeared " + logMessage, logMessage + " should be disappeared ",
                            logMessage + " is disappeared");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element text is disappeared " + logMessage, logMessage + " should be disappeared ",
                            this.catchException(e));
                    AtuReports.failResults("Verify element is disappeared " + logMessage, "--", logMessage + " should be disappeared ",
                            this.catchException(e));
                }
            default:
        }
    }

    public void waitForPageElement(WebElement element, WaitType waitType, String logMessage) {
        this.waitForPageElement(element, (WaitType) waitType, logMessage, 180);
    }

    public void waitForPageElement(WebElement pageElement, WaitType waitType, String logMessage, int timeOut) {
        switch (waitType) {
            case WAIT_FOR_ELEMENT_TO_BE_CLICKABLE:
                try {
                    this.webDriverHelper.waitForElementToBeClickable(pageElement, timeOut);
                    AtuReports.passResults("Verify element is clickable " + logMessage, "--", logMessage + " should be clickable ", logMessage + " is displayed and clickable ");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is clickable " + logMessage, logMessage + " should be clickable ", logMessage + " is clickable");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is clickable " + logMessage, logMessage + " should be clickable ", this.catchException(e));
                    AtuReports.failResults("Verify element is clickable " + logMessage, "--", logMessage + " should be clickable ", this.catchException(e));
                }
                break;

            case WAIT_FOR_ELEMENT_TO_BE_ENABLED:
                try {
                    this.webDriverHelper.waitForElementToBeEnabled(pageElement, timeOut);
                    AtuReports.passResults("Verify element is enabled " + logMessage, "--", logMessage + " should be enabled ", logMessage + " is displayed and enabled ");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is enabled " + logMessage, logMessage + " should be enabled ", logMessage + " is enabled");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is enabled " + logMessage, logMessage + " should be enabled ", this.catchException(e));
                    AtuReports.failResults("Verify element is enabled " + logMessage, "--", logMessage + " should be enabled ", this.catchException(e));
                }
                break;
            case WAIT_FOR_ELEMENT_TO_BE_DISPLAYED:
                try {
                    this.webDriverHelper.waitForElementToBeDisplayed(pageElement, timeOut, logMessage);
                    AtuReports.passResults1("Verify element is displayed " + logMessage, "--", logMessage + " should be displayed ", logMessage + " is displayed");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is displayed " + logMessage, logMessage + " should be displayed ", logMessage + " is displayed");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is displayed " + logMessage, logMessage + " should be displayed ", this.catchException(e));
                    AtuReports.failResults("Verify element is displayed " + logMessage, "--", logMessage + " should be displayed ", this.catchException(e));
                }
                break;
            case WAIT_FOR_ELEMENT_TO_DISAPPEAR:
                try {
                    this.webDriverHelper.waitForElementToDisappear(pageElement, timeOut);
                    AtuReports.passResults1("Verify element is disappeared " + logMessage, "--", logMessage + " should be disappeared ", logMessage + " is disappeared");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is disappeared " + logMessage, logMessage + " should be disappeared ", logMessage + " is disappeared");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is disappeared " + logMessage, logMessage + " should be disappeared ", this.catchException(e));
                    AtuReports.failResults("Verify element is disappeared " + logMessage, "--", logMessage + " should be disappeared ", this.catchException(e));
                }
                break;
            case WAIT_FOR_TEXT_TO_BE_PRESENT_IN_ELEMENT:
                try {
                    System.out.println("text: " + pageElement.getText());
                    this.webDriverHelper.waitForTextToBePresentInElement(pageElement, logMessage, 180);
                    AtuReports.passResults1("Verify element text is appeared " + logMessage, "--", logMessage + " should be appeared ", logMessage + " is appeared");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element text is appeared " + logMessage + " should be appeared ", logMessage + " is appeared", logMessage + " is appeared");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element text is appeared " + logMessage, logMessage + " should be appeared", this.catchException(e));
                    AtuReports.failResults("Verify element is appeared " + logMessage, "--", logMessage + " should be appeared ", this.catchException(e));
                }
        }
    }

    public void waitForPageElement(WebElement pageElement, String waitType, String logMessage) {
        this.waitForPageElement(pageElement, (String) waitType, logMessage, 180);
    }

    public void waitForPageElement(WebElement pageElement, String waitType, String logMessage, int timeOut) {
        WebElement webElement = null;
        WaitType waitTypes = WaitType.valueOf(waitType);
        switch (waitTypes) {
            case WAIT_FOR_ELEMENT_TO_BE_CLICKABLE:
                try {
                    this.webDriverHelper.waitForElementToBeClickable(pageElement, timeOut);
                    AtuReports.passResults("Verify element is clickable " + logMessage, "--", logMessage + " should be clickable ", logMessage + " is displayed and clickable");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is clickable " + logMessage, logMessage + " should be clickable ", logMessage + " is displayed and clickable");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is clickable " + logMessage, logMessage + " should be clickable ", this.catchException(e));
                    AtuReports.failResults("Verify element is clickable " + logMessage, "--", logMessage + " should be clickable ", this.catchException(e));
                }
                break;
            case WAIT_FOR_ELEMENT_TO_BE_ENABLED:
                try {
                    this.webDriverHelper.waitForElementToBeEnabled(pageElement, timeOut);
                    AtuReports.passResults("Verify element is enabled " + logMessage, "--", logMessage + " should be enabled ", logMessage + " is displayed and enabled");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is enabled " + logMessage, logMessage + " should be enabled ", logMessage + " is displayed and enabled");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is enabled " + logMessage, logMessage + " should be enabled ", this.catchException(e));
                    AtuReports.failResults("Verify element is enabled " + logMessage, "--", logMessage + " should be enabled ", this.catchException(e));
                }
                break;
            case WAIT_FOR_ELEMENT_TO_BE_DISPLAYED:
                try {
                    this.webDriverHelper.waitForElementToBeDisplayed(pageElement, timeOut, logMessage);
                    AtuReports.passResults1("Verify element is displayed " + logMessage, "--", logMessage + " should be displayed ", logMessage + " is displayed");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is displayed " + logMessage, logMessage + " should be displayed ", logMessage + " is displayed");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is displayed " + logMessage, logMessage + " should be displayed ", this.catchException(e));
                    AtuReports.failResults("Verify element is displayed " + logMessage, "--", logMessage + " should be displayed ", this.catchException(e));
                }
                break;

            case WAIT_FOR_ELEMENT_TO_DISAPPEAR:
                try {
                    this.webDriverHelper.waitForElementToDisappear(pageElement, timeOut);
                    AtuReports.passResults1("Verify element is disappeared " + logMessage, "--", logMessage + " should be disappeared ", logMessage + " is disappeared");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element is disappeared " + logMessage, logMessage + " should be disappeared ", logMessage + " is disappeared");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element is disappeared " + logMessage, logMessage + " should be disappeared ", this.catchException(e));
                    AtuReports.failResults("Verify element is disappeared " + logMessage, "--", logMessage + " should be disappeared ", this.catchException(e));
                }
                break;
            case WAIT_FOR_TEXT_TO_BE_PRESENT_IN_ELEMENT:
                try {
                    System.out.println("text: " + pageElement.getText());
                    this.webDriverHelper.waitForTextToBePresentInElement(pageElement, logMessage, 180);
                    AtuReports.passResults("Verify element text is appeared " + logMessage, "--", " should be appeared ", logMessage + " is appeared");
                    Reporter.LogEvent(TestStatus.PASS, "Verify element text is appeared " + logMessage, logMessage + " should be appeared ", logMessage + " is appeared");
                } catch (Exception e) {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify element text is appeared " + logMessage, logMessage + " should be appeared ", this.catchException(e));
                    AtuReports.failResults("Verify element is appeared " + logMessage + " should be appeared ", logMessage, "--", this.catchException(e));
                }
        }
    }

    public void isTextPresent(WebElement pageElement, String logMessage) {
        boolean isTextPresent = false;
        try {
            String text = pageElement.getText();
            isTextPresent = text.contains(logMessage);
            System.out.println("pageElement.getText() " + text);
            if (isTextPresent) {
                AtuReports.passResults("Verify text " + logMessage, "--", logMessage, text);
                Reporter.LogEvent(TestStatus.PASS, "Verify text " + logMessage, logMessage, text);
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, logMessage, text);
                AtuReports.failResults("Verify text " + logMessage, "--", logMessage, text);
            }
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, logMessage, this.catchException(e));
            AtuReports.failResults("Verify text " + logMessage, "--", logMessage, this.catchException(e));
        }
    }

    public void visibilityOfText(String searchText) {
        boolean findText = this.driver.findElement(By.cssSelector("body")).getText().contains(searchText);
        if (findText) {
            AtuReports.passResults1("Verify text " + searchText, "--", searchText, searchText);
            Reporter.LogEvent(TestStatus.PASS, "Verify text " + searchText, searchText, searchText);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text " + searchText, searchText, searchText);
            AtuReports.failResults("Verify text " + searchText, "--", searchText, searchText);
        }
    }

    public void isTextPresent(WebElement pageElement, String attribute, String logMessage) {
        boolean isTextPresent = false;

        try {
            String text = this.getText(pageElement, attribute);
            isTextPresent = text.contains(logMessage);
            if (isTextPresent) {
                AtuReports.passResults1("Verify text " + logMessage, "--", logMessage, text);
                Reporter.LogEvent(TestStatus.PASS, "Verify text " + logMessage, logMessage, text);
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, logMessage, text);
                AtuReports.failResults("Verify text " + logMessage, "--", logMessage, text);
            }
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, logMessage, this.catchException(e));
            AtuReports.failResults("Verify text " + logMessage, "--", logMessage, this.catchException(e));
        }
    }

    public void check(WebElement pageElement, String action, String logMessage) {
        try {
            boolean isChecked = pageElement.isEnabled() && pageElement.isSelected();
            if (action.equalsIgnoreCase("Check") && !isChecked) {
                this.jsClick(pageElement, logMessage);
            }
            if (action.equalsIgnoreCase("UnCheck") && isChecked) {
                this.jsClick(pageElement, logMessage);
            }
            AtuReports.passResults1("Verify " + logMessage + " is " + action + "ed", logMessage, logMessage + " should be " + action + "ed", logMessage + " is " + action + "ed");
            Reporter.LogEvent(TestStatus.PASS, "Verify " + logMessage + " is " + action + "ed", logMessage + " should be " + action + "ed", logMessage + " is " + action + "ed");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage + " is " + action + "ed", logMessage + " should be " + action + "ed",
                    this.catchException(exception));
            AtuReports.failResults("Verify " + logMessage + " is " + action + "ed", "--", logMessage + " should be " + action + "ed",
                    this.catchException(exception));
        }
    }

    public void radioBtnSelect(WebElement pageElement, String logMessage) {
        try {
            pageElement.click();
            AtuReports.passResults1("Verify " + logMessage + " is selected", "--", logMessage + " should be selected", logMessage + " is selected");
            Reporter.LogEvent(TestStatus.PASS, "Verify " + logMessage + " is selected", logMessage + " should be selected", logMessage + " is selected");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage + " is selected", logMessage + " should be selected",
                    this.catchException(exception));
            AtuReports.failResults("Verify " + logMessage + " is selected", "--", logMessage + " should be selected",
                    this.catchException(exception));
        }
    }

    public void isRadioBtnSelected(WebElement pageElement, String logMessage) {
        try {
            if (pageElement.isSelected()) {
                AtuReports.passResults1("Verify " + logMessage + " is selected", "--", logMessage + " should be selected", logMessage + " is selected");
                Reporter.LogEvent(TestStatus.PASS, "Verify " + logMessage + " is checked", logMessage + " should be selected", logMessage + " is selected");
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage + " is checked", logMessage + " should be selected", logMessage + "is not selected");
                AtuReports.failResults("Verify text " + logMessage, "--", logMessage + " should be selected", logMessage + "is not selected");
            }
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage + " is selected", logMessage + " should be selected", this.catchException(exception));
            AtuReports.failResults("Verify " + logMessage + " is selected", "--", logMessage + " should be selected", this.catchException(exception));
        }
    }

    public void isRadioBtnNotSelected(WebElement pageElement, String logMessage) {
        try {
            if (!pageElement.isSelected()) {
                AtuReports.passResults1("Verify " + logMessage + " is not selected", "--", logMessage + " should not be selected", logMessage + " is not selected");
                Reporter.LogEvent(TestStatus.PASS, "Verify " + logMessage + " is not checked", logMessage + " should not be selected", logMessage + " is not selected");
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage + " is not checked", logMessage + " should not be selected", logMessage + "is selected");
                AtuReports.failResults("Verify " + logMessage + " is not selected", "--", logMessage + " should not be selected", logMessage + "is selected");
            }
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage + " is not selected", logMessage + " should not be selected", this.catchException(exception));
            AtuReports.failResults("Verify " + logMessage + " is not selected", "--", logMessage + " should not be selected", this.catchException(exception));
        }
    }

    public String catchException(Exception exception) {
        if (this.driver.getPageSource().contains("HTTP Status")) {
            return "";
        } else if (this.driver.getTitle().equalsIgnoreCase("This page can't be displayed") | this.driver.getTitle().equalsIgnoreCase("Problem loading page")) {
            return "CHECK THE INTERNET CONNECTION";
        } else {
            String sTemp1 = exception.getMessage();
            String[] sTemp2;
            if (sTemp1 != null && sTemp1.contains("WARN")) {
                sTemp2 = sTemp1.split("WARN");
                if (sTemp2.length > 0) {
                    sTemp2[0] = new String(sTemp2[0].substring(0, sTemp2[0].length() - 1));
                }
            } else {
                if (sTemp1 == null || !sTemp1.contains("Timed out")) {
                    String sTemp3 = exception.toString();
                    return sTemp3;
                }

                sTemp2 = sTemp1.split(":");
                if (sTemp2.length > 0) {
                    String replaceStr = sTemp2[1].replace("Build info", "");
                    sTemp2[0] = new String(sTemp2[0] + " " + replaceStr);
                }
            }

            String var5 = null;
            return sTemp2[0];
        }
    }

    public WebElement getWebElement(String locator, String objectIdentifier, String logMessage) {
        WebElement element = null;
        try {
            if (objectIdentifier.equalsIgnoreCase("xpath")) {
                element = this.driver.findElement(By.xpath(locator));
            } else if (objectIdentifier.equalsIgnoreCase("linkText")) {
                element = this.driver.findElement(By.linkText(locator));
            } else if (objectIdentifier.equalsIgnoreCase("Id")) {
                element = this.driver.findElement(By.id(locator));
            }
            AtuReports.passResults1("Verify" + logMessage + " Webelement is displayed", "--", logMessage + " Webelement should be Displayed ", logMessage + " Webelement is Displayed");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify webelement " + logMessage + " is present", logMessage + " webelement should be present", this.catchException(exception));
            AtuReports.failResults("Verify webelement " + logMessage + " is present", "--", logMessage + " webelement should be present", this.catchException(exception));
        }

        return element;
    }

    public List<WebElement> getWebElements(String locator, String objectIdentifier, String logMessage) {
        List<WebElement> element = null;
        try {
            if (objectIdentifier.equalsIgnoreCase("xpath")) {
                element = this.driver.findElements(By.xpath(locator));
            } else if (objectIdentifier.equalsIgnoreCase("linkText")) {
                element = this.driver.findElements(By.linkText(locator));
            } else if (objectIdentifier.equalsIgnoreCase("Id")) {
                element = this.driver.findElements(By.id(locator));
            }
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify webelement " + logMessage + " is present", logMessage + " webelement should be present", this.catchException(exception));
            AtuReports.failResults("Verify webelement " + logMessage + " is present", "--", logMessage + " webelement should be present", this.catchException(exception));
        }
        return element;
    }

    public String getText(WebElement pageElement) {
        String test = "";
        try {
            test = pageElement.getText();
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text is present", "Text should be present", this.catchException(exception));
            AtuReports.failResults("Verify text is present", "--", "Text should be present", this.catchException(exception));
        }
        return test;
    }

    public String getText(WebElement pageElement, String attribute) {
        String text = "";
        try {
            text = pageElement.getAttribute(attribute);
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Get text", "Get text", this.catchException(exception));
            AtuReports.failResults("Get text", "--", "get text", this.catchException(exception));
        }
        return text;
    }

    public WebElement waitForTextToLoad(final WebElement pageElement, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds((long) timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long) timeOutPeriod));
        return (WebElement) webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    if (pageElement.getText() != null && !pageElement.getText().trim().isEmpty()) {
                        AtuReports.passResults("Verify text is displayed", "--", "Text should be Displayed", pageElement.getText() + " is Displayed");
                        Reporter.LogEvent(TestStatus.PASS, "Verify text is displayed", pageElement.getText() + " text should be Displayed", "Text is Displayed");
                        return pageElement;
                    } else {
                        return null;
                    }
                } catch (Exception exception) {
                    return pageElement;
                }
            }
        });
    }

    public void waitForTextToDisappear(final WebElement element, int timeOutPeriod, final String text, String attribute) {
        WebDriverWait webDriverWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds((long) timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long) timeOutPeriod));
        try {
            if (attribute.equalsIgnoreCase("value")) {
                webDriverWait.until(new ExpectedCondition<WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        System.out.println("element.getText()==" + element.getAttribute("value"));
                        if (element.isEnabled() && element.isDisplayed() && !element.getAttribute("value").equalsIgnoreCase(text)) {
                            AtuReports.passResults("Verify text should disappear", "--", text + " text should disappear ", text + " is disappeared");
                            Reporter.LogEvent(TestStatus.PASS, "Verify text should disappear", text + " text should disappear ", text + " is disappeared");
                            return element;
                        } else {
                            return null;
                        }
                    }
                });
            } else {
                webDriverWait.until(new ExpectedCondition<WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        System.out.println("element.getText()==" + element.getText());
                        if (element.isEnabled() && element.isDisplayed() && !element.getText().equalsIgnoreCase(text)) {
                            AtuReports.passResults("Verify text should disappear", "--", text + " text should disappear ", text + " is disappeared");
                            Reporter.LogEvent(TestStatus.PASS, "Verify text should disappear", text + " text should disappear ", text + " is disappeared");
                            return element;
                        } else {
                            return null;
                        }
                    }
                });
            }

            AtuReports.passResults1("Verify text is disappeared" + text, "--", text + " text should be disappeared ", text + " is disappeared");
            Reporter.LogEvent(TestStatus.PASS, "Verify text is disappeared" + text, text + " text should be disappeared ", text + " is disappeared");
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text is disappeared" + text, text + " text should be disappeared ", this.catchException(e));
            AtuReports.failResults("Verify text is disappeared" + text, "--", text + " text should be disappeared ", this.catchException(e));
        }
    }

    public Long JsExecutor(String attributeIdentifier, String attributeName, String value, String logMessage) {
        Long val = 0L;
        try {
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            val = (Long) js.executeScript("document.getElementById('" + attributeIdentifier + "').setAttribute('" + attributeName + "','" + value + "')", new Object[0]);
            AtuReports.passResults1("Verify " + logMessage + " is draged", "--", logMessage + " should be draged", logMessage + " is draged");
            Reporter.LogEvent(TestStatus.PASS, "Verify " + logMessage + " is draged", logMessage + " should be draged", logMessage + " is draged");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage + " is draged", logMessage + " should be draged", this.catchException(exception));
            AtuReports.failResults("Verify " + logMessage + " is draged", logMessage, logMessage + " should be draged", this.catchException(exception));
        }
        return val;
    }

    public void JsExecutorClick(String attributeIdentifier, String action, String logMessage) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            if (action != null && action.equalsIgnoreCase("Id")) {
                js.executeScript("document.getElementById('" + attributeIdentifier + "').click()", new Object[0]);
            } else if (action != null && action.equalsIgnoreCase("Name")) {
                js.executeScript("var objArr=document.getElementsByName('" + attributeIdentifier + "');objArr[0].click()", new Object[0]);
            }
            AtuReports.passResults1("Verify click action on: " + logMessage, "--", logMessage + " Should be clicked", logMessage + " is clicked");
            Reporter.LogEvent(TestStatus.PASS, "Verify click action on: " + logMessage, logMessage + " Should be clicked", logMessage + " is clicked");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify click action on: " + logMessage, logMessage + " Should be clicked", this.catchException(exception));
            AtuReports.failResults("Verify click action on: " + logMessage, "--", logMessage + " Should be clicked", this.catchException(exception));
            this.catchException(exception);
        }
    }

    public void JsExecutorSelect(String attributeIdentifier, String selectValue, String logMessage) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            js.executeScript("document.getElementById('" + attributeIdentifier + "').value='" + selectValue + "'", new Object[0]);
            AtuReports.passResults1("Select: " + logMessage, selectValue, logMessage + " should be selected", logMessage + " is selected");
            Reporter.LogEvent(TestStatus.PASS, "Select: " + logMessage, logMessage + " Should be selected", logMessage + " is selected");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Select: " + logMessage, logMessage + " Should be selected", this.catchException(exception));
            AtuReports.failResults("Failed to select: " + logMessage, selectValue, logMessage + " Should be selected", this.catchException(exception));
        }
    }

    public void closeModalDialog() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            js.executeScript("var o1=top.getActiveDialog(); o1.$modal.children('.CloseModal').click()", new Object[0]);
            AtuReports.passResults1("Verify modal dialog is closed", "--", "Modal dialog should be closed", "Modal dialog is closed");
            Reporter.LogEvent(TestStatus.PASS, "Verify modal dialog is closed", "Modal dialog should be closed", "Modal dialog is closed");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify modal dialog is closed", "Modal dialog should be closed", this.catchException(exception));
            AtuReports.failResults("Verify modal dialog is closed", "--", "should be draged", this.catchException(exception));
        }
    }

    public void mutipleSelect(String[] locators, String logMessage) {
        try {
            Actions ac = (new Actions(this.driver)).keyDown(Keys.CONTROL);
            for (int i = 0; i < locators.length; ++i) {
                ac.click(this.getWebElement("//span[text()='" + locators[i] + "']", "xpath", locators[i]));
            }
            ac.keyUp(Keys.CONTROL);
            ac.build().perform();
            AtuReports.passResults1("Select " + logMessage, "--", logMessage + " should be Select", logMessage + " is Selected");
            Reporter.LogEvent(TestStatus.PASS, "Select " + logMessage, logMessage + " should be Select", logMessage + " is Selected");
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Select " + logMessage, logMessage + " should be Select", this.catchException(exception));
            AtuReports.failResults("Select " + logMessage, "--", logMessage + " should be Select", this.catchException(exception));
        }
    }

    public String getPageSource() {
        String pageSource = null;
        try {
            pageSource = this.driver.getPageSource();
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify PageSource", "Get the page source", this.catchException(exception));
            AtuReports.failResults("Verify PageSource", "--", "Get the page source", this.catchException(exception));
        }
        return pageSource;
    }

    public static void explicitWait(int waitTime) {
        try {
            Thread.sleep((long) (waitTime * 1000));
        } catch (InterruptedException var2) {
        }
    }

    public boolean isAlertPresent() {
        try {
            explicitWait(2);
            Alert alert = this.driver.switchTo().alert();
            alert.accept();
            return true;
        } catch (NoAlertPresentException var2) {
            return false;
        }
    }

    public String getTextFromAlert() {
        String text = "";
        try {
            explicitWait(2);
            text = this.driver.switchTo().alert().getText();
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "verify alert is present", "Get the text from alert", this.catchException(exception));
            AtuReports.failResults("verify alert is present", "--", "Get the text from alert", this.catchException(exception));
        }
        return text;
    }

    public void acceptDismissAlert(String action) {
        try {
            if (action != null && action.equalsIgnoreCase("Accept")) {
                this.driver.switchTo().alert().accept();
            } else if (action != null && action.equalsIgnoreCase("Dismiss")) {
                this.driver.switchTo().alert().dismiss();
            }
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify alert is " + action, "Alert should be " + action, this.catchException(exception));
            AtuReports.failResults("Verify alert is " + action, "--", "Alert should be " + action, this.catchException(exception));
        }
    }

    public void closeChildWindow() {
        try {
            this.driver.close();
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify child window is closed", "Child window should be closed", this.catchException(exception));
            AtuReports.failResults("Verify child window is closed", "--", "Child window should be closed", this.catchException(exception));
        }
    }

    public void verifyTextInAlert(String testData) {
        String text = this.getTextFromAlert();
        if (text.contains(testData)) {
            AtuReports.passResults1("Verify text in alert", "--", testData, text);
            Reporter.LogEvent(TestStatus.PASS, "Verify text in alert", testData, text);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text in alert", testData, text);
            AtuReports.failResults("Verify text in alert", "--", testData, text);
        }
        this.acceptDismissAlert("Accept");
    }

    public void isDisabled(WebElement pageElement, String logMessage) {
        try {
            if (!pageElement.isEnabled()) {
                AtuReports.passResults1("Verify" + logMessage + " is disabled", "--", logMessage + " should be disabled", logMessage + " is disabled");
                Reporter.LogEvent(TestStatus.PASS, "Verify" + logMessage + " is disabled", logMessage + " should be disabled", logMessage + " is disabled");
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify" + logMessage + " is disabled", logMessage + " should be disabled", logMessage + " is Enabled");
                AtuReports.failResults("Verify" + logMessage + " is disabled", "--", logMessage + " should be disabled", logMessage + " is Enabled");
            }
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify" + logMessage + " is disabled", logMessage + " should be disabled", this.catchException(e));
            AtuReports.failResults("Verify" + logMessage + " is disabled", "--", logMessage + " should be disabled", this.catchException(e));
        }
    }

    public boolean isEnabled(WebElement pageElement, String logMessage) {
        boolean result = false;
        try {
            if (pageElement.isEnabled()) {
                result = true;
                AtuReports.passResults("Verify" + logMessage + " is enabled", "--", logMessage + " should be enabled", logMessage + " is enabled");
                Reporter.LogEvent(TestStatus.PASS, "Verify" + logMessage + " is enabled", logMessage + " should be enabled", logMessage + " is enabled");
            } else {
                result = false;
                AtuReports.failResults("Verify" + logMessage + " is enabled", "--", logMessage + " should be enabled", logMessage + " is Disabled");
                Reporter.LogEvent(TestStatus.FAIL, "Verify" + logMessage + " is enabled", logMessage + " should be enabled", logMessage + " is disabled");
            }
        } catch (Exception e) {
            AtuReports.failResults("Verify" + logMessage + " is enabled", "--", logMessage + " should be enabled", logMessage + " is Disabled");
            Reporter.LogEvent(TestStatus.FAIL, "Verify" + logMessage + " is enabled", logMessage + " should be enabled", logMessage + " is disabled");
            result = false;
        }
        return result;
    }

    public void waitWhileElementHasAttributeValue(WebElement pageElement, String attribute, String value) {
        while (!pageElement.getAttribute(attribute).contains(value)) {
            int timeout = 10;
            if (timeout > 0) {
                timeout--;
                try {
                    System.out.println(attribute + "\t" + value);
                    Thread.sleep(1000L);
                } catch (Exception var6) {
                }
            }
        }
    }

    public void focus(WebElement pageElement, String logMessage) {
        try {
            Actions builder = new Actions(this.driver);
            builder.moveToElement(pageElement).perform();
        } catch (Exception var4) {
        }
    }

    public void mouseClick(WebElement pageElement, String logMessage) {
        try {
            Actions builder = new Actions(this.driver);
            builder.moveToElement(pageElement).click().perform();
        } catch (Exception var4) {
        }
    }

    public String getCurrentWindowHandle() {
        return this.driver.getWindowHandle();
    }

    public void switchToWindowHandle(String windowHandle) {
        this.driver.switchTo().window(windowHandle);
    }

    public String getPageTitle() {
        return this.driver.getTitle();
    }

    public void verifyPageTitle(String expectedTitle) {
        try {
            String actualTitle = this.getPageTitle();
            if (actualTitle.contains(expectedTitle)) {
                AtuReports.passResults1("Verify title " + expectedTitle, "--", expectedTitle, actualTitle);
                Reporter.LogEvent(TestStatus.PASS, "Verify title " + expectedTitle, expectedTitle, actualTitle);
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify title " + expectedTitle, expectedTitle, actualTitle);
                AtuReports.failResults("Verify title " + expectedTitle, "--", expectedTitle, actualTitle);
            }
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify title " + expectedTitle, expectedTitle, this.catchException(e));
            AtuReports.failResults("Verify title " + expectedTitle, "--", expectedTitle, this.catchException(e));
        }
    }

    public void keyBoardEvents(String eventType) {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            AtuReports.failResults("Robot class initialization Failed", "--", eventType, this.catchException(e));
        }

        if (eventType.equalsIgnoreCase("Escape")) {
            robot.keyPress(27);
            robot.keyRelease(27);
        }

        if (eventType.equalsIgnoreCase("TAB")) {
            robot.keyPress(9);
            robot.keyRelease(9);
        }

        if (eventType.equalsIgnoreCase("ENTER")) {
            robot.keyPress(10);
            robot.keyRelease(10);
        }
    }

    public static String getBackgroundColor(WebElement element) {
        System.out.println(element.getCssValue("background-color") + " bg color");
        return element.getCssValue("background-color");
    }

    public void closeAllTabsExceptParent() {
        String originalHandle = this.driver.getWindowHandle();
        Set<String> windowHandles = this.driver.getWindowHandles();
        System.out.println(windowHandles.size() + "size");
        if (windowHandles.size() > 1) {
            for (String handle : windowHandles) {
                if (!handle.equals(originalHandle)) {
                    System.out.println(originalHandle + "originalHandle");
                    this.driver.switchTo().window(handle);
                    this.driver.close();
                }
            }
            this.driver.switchTo().window(originalHandle);
        }
    }

    public boolean isListAscending(List<WebElement> list) {
        for (int i = 1; i < list.size(); ++i) {
            if (((WebElement) list.get(i - 1)).getText().compareToIgnoreCase(((WebElement) list.get(i)).getText()) > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isListDescending(List<WebElement> list) {
        for (int i = 1; i < list.size(); ++i) {
            if (((WebElement) list.get(i - 1)).getText().compareToIgnoreCase(((WebElement) list.get(i)).getText()) < 0) {
                return false;
            }
        }
        return true;
    }

    public void dragAndDrop(WebElement sourceElement, WebElement destinationElement) {
        try {
            Actions builder = new Actions(this.driver);
            Action dragAndDrop = builder.clickAndHold(sourceElement).moveToElement(destinationElement).release(destinationElement).build();
            dragAndDrop.perform();
            AtuReports.passResults("Verify drag and drop action", "--", "Should be able to drag and drop", "Drag and drop is success");
            Reporter.LogEvent(TestStatus.PASS, "Verify drag and drop action", "Should be able to drag and drop", "Drag and drop is success");
        } catch (Exception e) {
            AtuReports.failResults("Verify drag and drop action", "--", "Should be able to drag and drop", "Drag and drop is not success");
            Reporter.LogEvent(TestStatus.FAIL, "Verify drag and drop action", "Should be able to drag and drop", this.catchException(e));
        }
    }

    public void moveToElement(WebElement element) {
        try {
            Actions action = new Actions(this.driver);
            action.moveToElement(element).build().perform();
        } catch (Exception e) {
            AtuReports.failResults("Verify Mouse hover action", "--", "Should be able to hover", "Mouse hover is not success: " + this.catchException(e));
            Reporter.LogEvent(TestStatus.FAIL, "Verify Mouse hover action", "Should be able to hover", this.catchException(e));
        }
    }

    public void mouseHover(WebElement element) {
        try {
            String javaScript = "var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);arguments[0].dispatchEvent(evObj);";
            ((JavascriptExecutor)this.driver).executeScript(javaScript, new Object[]{element});
        } catch (Exception e) {
            AtuReports.failResults("Verify Mouse hover action", "--", "Should be able to hover", "Mouse hover is not success: " + this.catchException(e));
            Reporter.LogEvent(TestStatus.FAIL, "Verify Mouse hover action", "Should be able to hover", this.catchException(e));
        }
    }

    public String getFormattedDate(String formatType, Calendar cal) {
        DateFormat sdf = new SimpleDateFormat(formatType);
        String fromatedDateone = sdf.format(cal.getTime());
        return fromatedDateone;
    }

    public Calendar getMonth(int monthNumber) {
        Calendar cal = Calendar.getInstance();
        cal.add(2, monthNumber);
        return cal;
    }

    public Calendar getDate(int dateRange) {
        Calendar cal = Calendar.getInstance();
        cal.add(5, dateRange);
        return cal;
    }

    public Calendar getDateFromCurrentDate(int yearRange, int monthRange, int dateRange) {
        Calendar cal = Calendar.getInstance();
        cal.add(5, dateRange);
        cal.add(2, monthRange);
        cal.add(1, yearRange);
        return cal;
    }

    public Calendar getDateFromGivenDate(int yearRange, int monthRange, int dateRange, Calendar cal) {
        cal.add(5, dateRange);
        cal.add(2, monthRange);
        cal.add(1, yearRange);
        return cal;
    }

    public void verifyPageUrl(String url) {
        String currentURL = this.driver.getCurrentUrl();
        System.out.println("currentURL " + currentURL);
        if (currentURL.equals(url)) {
            AtuReports.passResults("Verify Url " + url, "--", url, currentURL);
            Reporter.LogEvent(TestStatus.PASS, "Verify Url " + url, url, currentURL);
        } else if (currentURL.contains(url)) {
            AtuReports.passResults("Verify Url contains " + url, "--", url, currentURL);
            Reporter.LogEvent(TestStatus.PASS, "Verify Url contains" + url, url, currentURL);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify Url " + url, url, currentURL);
            AtuReports.failResults("Verify Url " + url, "--", url, currentURL);
        }
    }

    public void refresh() {
        this.driver.navigate().refresh();
        this.waitForPageLoad();
    }

    public void switchToWindowUrl(String url) {
        explicitWait(10);
        Set<String> windowHandles = this.driver.getWindowHandles();
        boolean flag = false;

        try {
            for(String windowHandle : windowHandles) {
                this.driver.switchTo().window(windowHandle);
                explicitWait(10);
                this.waitForPageLoad();
                if (this.driver.getCurrentUrl().contains(url)) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify switch window to Url " + url, url, this.catchException(e));
            AtuReports.failResults("Verify switch window to Url " + url, "--", url, this.catchException(e));
        }

        if (flag) {
            AtuReports.passResults("Verify switch window to Url " + url, "--", url, "Switched to url");
            Reporter.LogEvent(TestStatus.PASS, "Verify switch window to Url " + url, url, "Switched to url");
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify switch window to Url " + url, url, "Unable to Switch to url");
            AtuReports.failResults("Verify switch window to Url " + url, "--", url, "Unable to Switch to url");
        }
    }

    public void invisibleOfText(String searchText) {
        boolean findText = this.driver.findElement(By.cssSelector("body")).getText().contains(searchText);
        if (findText) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify invisible text " + searchText, searchText, searchText);
            AtuReports.failResults("Verify invisible text " + searchText, "--", searchText, searchText);
        } else {
            AtuReports.passResults1("Verify invisible text " + searchText, "--", searchText, searchText);
            Reporter.LogEvent(TestStatus.PASS, "Verify invisible text " + searchText, searchText, searchText);
        }
    }

    public List<WebElement> getSelectOptions(WebElement selectElement) {
        Select select = new Select(selectElement);
        return select.getOptions();
    }

    public String getQuarter(String month) {
        int value = (Integer.parseInt(month) - 1) / 3 + 1;
        return "Q" + value;
    }

    public String getQuarterAndYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(1);
        int month = cal.get(2);
        String quarter = this.getQuarter(String.valueOf(month));
        System.out.println(quarter + " " + year + " quarter year;");
        return quarter + " " + year;
    }

    public WebElement getLink(String linkText) {
        return this.driver.findElement(By.linkText(linkText));
    }

    public WebElement getSubLink(String linkText) {
        return this.driver.findElement(By.xpath("//span[@class='raul-left-navigation-item-display' and text()='" + linkText + "']"));
    }

    public List<WebElement> getLinks(String linkText) {
        return this.driver.findElements(By.linkText(linkText));
    }

    public void compareText(String actual, String expected, String logMessage) {
        if (actual.equals(expected)) {
            AtuReports.passResults1("Verify text " + logMessage, "--", expected, actual);
            Reporter.LogEvent(TestStatus.PASS, "Verify text " + logMessage, expected, actual);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, expected, actual);
            AtuReports.failResults("Verify text " + logMessage, "--", expected, actual);
        }
    }

    public void compareText(String actual, String expected) {
        this.compareText(actual, expected, expected);
    }

    public List<String> getOptionsList(WebElement selectElement) {
        Select select = new Select(selectElement);
        List<WebElement> options = select.getOptions();
        List<String> optionValues = new ArrayList<>();
        for(WebElement option : options) {
            optionValues.add(option.getText());
        }
        return optionValues;
    }

    public void compareList(List<String> listString1, List<String> listString2, String logmessage) {
        boolean flag = false;
        if (listString1 != null && listString2 != null && listString1.size() == listString2.size()) {
            listString1.removeAll(listString2);
            if (listString1.isEmpty()) {
                flag = true;
            }
        }

        if (flag) {
            AtuReports.passResults1("Compare Lists", "--", logmessage, logmessage + " are same");
            Reporter.LogEvent(TestStatus.PASS, "Compare Lists", logmessage, logmessage + " are same");
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Compare Lists", logmessage, logmessage + " are not same");
            AtuReports.failResults("Compare Lists", "--", logmessage, logmessage + " are not same");
        }
    }

    public String getAppCurrentUrl() {
        String currentURL = this.driver.getCurrentUrl();
        if (!currentURL.isEmpty()) {
            currentURL = this.driver.getCurrentUrl();
            AtuReports.passResults1("Get Current Application Url ", "--", "shoud get Live Application Url", currentURL);
        } else {
            currentURL = "Url";
            AtuReports.failResults("Get Current Application Url", "--", "Shoud get Live Application Url", currentURL + " is Empty");
        }

        return currentURL;
    }

    public void acceptAlert() {
        this.webDriverHelper.acceptAlert(180);
    }

    public void validateText(WebElement pageElement, String value, String logMessage) {
        boolean isTextPresent = false;
        try {
            String text = pageElement.getText();
            isTextPresent = text.contains(value);
            System.out.println("pageElement.getText() " + text);
            if (isTextPresent) {
                AtuReports.passResults1("Verify text " + logMessage, "--", value, text);
                Reporter.LogEvent(TestStatus.PASS, "Verify text " + logMessage, value, text);
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, value, text);
                AtuReports.failResults("Verify text " + logMessage, "--", value, text);
            }
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, value, this.catchException(e));
            AtuReports.failResults("Verify text " + logMessage, "--", value, this.catchException(e));
        }
    }

    public boolean isValidDate(String inDate, String format, String logMessage) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.parse(inDate);
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, logMessage + ": Date Format " + inDate, logMessage + ": " + inDate + " Date Format should be valid "
                    + format, this.catchException(e) + " Date Format is not valid " + format);
            AtuReports.failResults("Date Format " + inDate, "--", logMessage + ": " + inDate + " Date Format should be valid " + format,
                    this.catchException(e) + " Date Format is not valid " + format);
            return false;
        }
        AtuReports.passResults1(logMessage + ": Date Format " + inDate, "--", logMessage + ": " + inDate + " Date Format is valid " + format,
                inDate + " Date Format is valid " + format);
        Reporter.LogEvent(TestStatus.PASS, "Date Format is valid " + format, inDate + " Date Format is valid " + format, logMessage + ": " + inDate + " Date Format is valid " + format);
        return true;
    }

    public void validateHelpLink(WebElement link, String title) {
        String parenthandle = this.getCurrentWindowHandle();
        this.click(link, "Help Link");
        this.switchToWindow(title);
        this.switchToWindowHandle(parenthandle);
        this.closeAllTabsExceptParent();
    }

    public void isTextAbsent(WebElement pageElement, String logMessage) {
        boolean isTextPresent = false;

        try {
            String text = this.getText(pageElement);
            isTextPresent = !text.contains(logMessage);
            if (isTextPresent) {
                AtuReports.passResults("Verify text is not present" + logMessage, "--", logMessage, text);
                Reporter.LogEvent(TestStatus.PASS, "Verify text is not present" + logMessage, logMessage, text);
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Verify text is not present" + logMessage, logMessage, text);
                AtuReports.failResults("Verify text is not present" + logMessage, "--", logMessage, text);
            }
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text is not present" + logMessage, logMessage, this.catchException(e));
            AtuReports.failResults("Verify text is not present" + logMessage, "--", logMessage, this.catchException(e));
        }
    }

    public String hexToRgbA(String hex) {
        return "rgb (" + Color.decode(hex).getRed() + "," + Color.decode(hex).getGreen() + "," + Color.decode(hex).getRed() + ")";
    }

    public String rgbToHex(String font_color) {
        System.out.println(font_color + " font_color");
        String[] color1;
        if (font_color.contains("rgba(")) {
            color1 = font_color.replace("rgba(", "").replace(")", "").split(",");
        } else {
            color1 = font_color.replace("rgb(", "").replace(")", "").split(",");
        }

        String hex = String.format("#%02x%02x%02x", Integer.parseInt(color1[0].trim()), Integer.parseInt(color1[1].trim()), Integer.parseInt(color1[2].trim()));
        return hex;
    }

    public Date convertStringToDate(String inDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;

        try {
            date = dateFormat.parse(inDate);
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, "Date Format " + inDate, inDate + " Date Format should be valid " + format, this.catchException(e) + " Date Format is not valid " + format);
            AtuReports.failResults("Date Format " + inDate, "--", inDate + " Date Format should be valid " + format, this.catchException(e) + " Date Format is not valid " + format);
        }

        return date;
    }

    public Calendar convertStringToCalender(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(dateFormat.parse(date));
        } catch (Exception e) {
            Reporter.LogEvent(TestStatus.FAIL, " Date Format " + date, date + " Date Format should be valid " + format, this.catchException(e) + " Date Format is not valid " + format);
            AtuReports.failResults("Date Format " + date, "--", date + " Date Format should be valid " + format, this.catchException(e) + " Date Format is not valid " + format);
        }

        return cal;
    }

    public String convertCalenderToString(Calendar cal, String format) {
        DateFormat sdf = new SimpleDateFormat(format);
        String fromatedDateone = sdf.format(cal.getTime());
        return fromatedDateone;
    }

    public static long daysBetween(Date date1, Date date2) {
        long difference = (date1.getTime() - date2.getTime()) / 86400000L;
        return Math.abs(difference);
    }

    public void validateBackgroundColor(WebElement element, String color, String logMessage) {
        String actualColor = element.getCssValue("background-color");
        if (actualColor.equals(color)) {
            AtuReports.passResults1("Verify Background color " + logMessage, "--", "Background Color Should be " + color, "Background Color is " + actualColor);
            Reporter.LogEvent(TestStatus.PASS, "Verify Background color " + logMessage, "Background Color Should be " + color, "Background Color is " + actualColor);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify Background color " + logMessage, "Background Color Should be " + color, "Color is " + actualColor);
            AtuReports.failResults("Verify Background color " + logMessage, "--", "Background Color Should be " + color, "Background Color is " + actualColor);
        }
    }

    public void validateColor(WebElement element, String color, String logMessage) {
        String actualColor = element.getCssValue("color");
        if (actualColor.equals(color)) {
            AtuReports.passResults1("Verify color " + logMessage, "--", "Color Should be " + color, "Color is " + actualColor);
            Reporter.LogEvent(TestStatus.PASS, "Verify color " + logMessage, "Color Should be " + color, "Color is " + actualColor);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify color " + logMessage, "Color Should be " + color, "Color is " + actualColor);
            AtuReports.failResults("Verify color " + logMessage, "--", "Color Should be " + color, "Color is " + actualColor);
        }
    }

    public static void validateFont(WebElement element, String format, String logMessage) {
        String actualFont = element.getCssValue("font-family").toLowerCase();
        if (actualFont.contains(format.toLowerCase())) {
            AtuReports.passResults1("Verify font " + logMessage, "--", " Font Should be " + format, " Font is " + actualFont);
            Reporter.LogEvent(TestStatus.PASS, "Verify  Font " + logMessage, " Font Should be " + format, " Font is " + actualFont);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify  Font " + logMessage, " Font Should be " + format, "Font is " + actualFont);
            AtuReports.failResults("Verify  Font " + logMessage, "--", " Font Should be " + format, " Font is " + actualFont);
        }
    }

    public String convertDatetoString(String date, String initialFormat, String finalFormat) {
        return this.convertCalenderToString(this.convertStringToCalender(date, initialFormat), finalFormat);
    }

    public JSONObject convertStingToJsonObj(String jsonstring) {
        String jsonString = jsonstring;
        JSONParser parser = new JSONParser();
        JSONObject json = null;

        try {
            json = (JSONObject)parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray convertStingToJSONArray(String jsonstring) {
        String jsonString = jsonstring;
        JSONParser parser = new JSONParser();
        JSONArray array = null;

        try {
            array = (JSONArray)parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return array;
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp((new Date()).getTime());
    }

    public static long duration(Timestamp end_time, Timestamp start_time) {
        return end_time.getTime() - start_time.getTime();
    }

    public void switchToFrameByAbsoluteXpath(String frame) {
        try {
            this.webDriverHelper.WAIT_FOR_FRAME_TO_BE_DISPLAYED(this.driver, 180, By.xpath(frame));
            AtuReports.passResults1("Verify frame is: " + frame, "", "Frame Should be Switched to: " + frame, "Frame is Switched to: " + frame);
            Reporter.LogEvent(TestStatus.PASS, "Verify frame is displayed: " + frame, "Frame Should be Switched to: " + frame, "Frame is Switched to: " + frame);
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify frame is displayed: " + frame, "Frame Should be Switched to: " + frame, this.catchException(exception));
            AtuReports.failResults("Verify " + frame + " is selected", "--", "Frame Should be Switched to :" + frame, this.catchException(exception));
        }
    }

    public static int daysInMonth(GregorianCalendar c) {
        int[] daysInMonths = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        daysInMonths[1] += c.isLeapYear(c.get(1)) ? 1 : 0;
        return daysInMonths[c.get(2)];
    }

    public String getLastDate(String format, Calendar cal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        cal.set(5, cal.getActualMaximum(5));
        return dateFormat.format(cal.getTime());
    }

    public void bringElementInView(WebElement element) {
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].scrollIntoView(true);", new Object[]{element});
    }

    public void bringElementInView(WebElement element, int offSet) {
        Point p = element.getLocation();
        String script = "window.scrollTo(" + p.getX() + ", " + (p.x - offSet) + ");";
        ((JavascriptExecutor)this.driver).executeScript(script, new Object[0]);
    }

    public void scrollUp() {
        ((JavascriptExecutor)this.driver).executeScript("scroll(0, -250);", new Object[0]);
    }

    public void scrollDown() {
        ((JavascriptExecutor)this.driver).executeScript("scroll(0, 250);", new Object[0]);
    }

    public void scrollToBottomOfPage() {
        JavascriptExecutor js = (JavascriptExecutor)this.driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)", new Object[0]);
    }

    public void scrollToTopOfPage() {
        JavascriptExecutor js = (JavascriptExecutor)this.driver;
        js.executeScript("window.scrollTo(0, 0)", new Object[0]);
    }

    public void scrollToElement(WebElement element) {
        Actions scrollAction = new Actions(this.driver);
        scrollAction.keyDown(Keys.CONTROL).sendKeys(new CharSequence[]{Keys.END}).perform();
        explicitWait(3);
        Actions clickAction = new Actions(this.driver);
        clickAction.moveToElement(element).build().perform();
    }

    public void validateElementsFontAndColor(String FontFamily, String htmlTagNames, String colors) {
        AtuReports.passResults1("Page name of RAUL Changes :", this.driver.getTitle(), "", "");

        for (WebElement element : this.driver.findElements(By.cssSelector("*"))) {
            if (element.getText().trim().length() != 0) {
                if (element.getCssValue("font-family").contains(FontFamily)) {
                    AtuReports.passResults1("Verify RAUL Font Changes for Html TagName : " + element.getTagName(), "Font Name :" + FontFamily,
                            "Expected Font name is '" + FontFamily + "' Text from UI is : " + element.getText() + " and its CLASS name is :" +
                                    element.getAttribute("class"), "Actual Font values are : " + element.getCssValue("font-family"));
                } else {
                    AtuReports.notice("Verify RAUL Font Changes for Html TagName : " + element.getTagName(), "Font Name :" + FontFamily,
                            "Expected Font name is '" + FontFamily + "' Text from UI is : " + element.getText() + " and its CLASS name is :" +
                                    element.getAttribute("class"), "Actual Font values are : " + element.getCssValue("font-family"));
                }
            }

            if (htmlTagNames.trim().length() != 0 && htmlTagNames.contains(element.getTagName())) {
                String actualColor = element.getCssValue("color");
                if (actualColor.equals(colors)) {
                    AtuReports.notice("Verify RAUL Color Changes for Html TagName : " + element.getTagName(), "Element colors :" + colors,
                            "Expected colors name is '" + colors + "' Element TEXT from UI is : " + element.getText() + " and its CLASS name is :" +
                                    element.getAttribute("class"), "Actual color values are : " + actualColor);
                } else {
                    AtuReports.notice("Verify RAUL Color Changes for Html TagName : " + element.getTagName(), "Element color :" + colors,
                            "Expected color name is '" + colors + "' Element TEXT from UI is : " + element.getText() + " and its CLASS name is :" +
                                    element.getAttribute("class"), "Actual color values are : " + actualColor);
                }
            }
        }
    }

    public void validateInvalidLinks(String tagName) {
        int numberOfElementsFound = this.getNumberOfElementsFound(By.tagName(tagName));
        System.out.println(numberOfElementsFound);

        for(int pos = 0; pos < numberOfElementsFound - 1; ++pos) {
            url = this.getElementWithIndex(By.tagName(tagName), pos).getAttribute("href");
            if (url != null && !url.contains("javascript") && !url.isEmpty() && !url.startsWith("/") && !url.startsWith("tel")) {
                verifyURLStatus(url);
            }
        }

        AtuReports.passResults1("Valid Links Count", "", "Valid details ", "NO: of Valid Links::" + validLinksCount);
        AtuReports.passResults1("Invalid Links Count", "", "InValid details ", "NO: of InValid Links::" + invalidLinksCount);
        AtuReports.passResults1("Connection Error Url", "", "connection details ", "NO: of connection Links::" + connectionErrorUrl);
    }

    public static void verifyURLStatus(String URL) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                AtuReports.notice("InValid link url", URL, "200" + URL, "::" + response.getStatusLine().getStatusCode());
                ++invalidLinksCount;
            } else {
                AtuReports.passResults1("Valid link url", URL, "200" + URL, "::" + response.getStatusLine().getStatusCode());
                ++validLinksCount;
            }
        } catch (Exception e) {
            AtuReports.notice("Connection Error url", URL, "Connection Error message for :" + URL, "Error details ::" + e.getMessage());
            ++connectionErrorUrl;
        }
    }

    public int getNumberOfElementsFound(By by) {
        return this.driver.findElements(by).size();
    }

    public WebElement getElementWithIndex(By by, int pos) {
        return (WebElement)this.driver.findElements(by).get(pos);
    }

    public void jsSendKeys(WebElement element, String value, String logMessage) {
        try {
            String id = element.getAttribute("id");
            String script = "$('#" + id + "').val('" + value + "')";
            ((JavascriptExecutor)this.driver).executeScript(script, new Object[0]);
            element.sendKeys(new CharSequence[]{Keys.BACK_SPACE});
        } catch (Exception exception) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text is entered to " + logMessage + " textbox", "Text '" + value + "' should be entered in to " + logMessage + " textbox", this.catchException(exception));
        }
    }

    public String jsExecutorGetText(String script, String logMessage) {
        String str = "";

        try {
            JavascriptExecutor js = (JavascriptExecutor)this.driver;
            str = (String)js.executeScript(script, new Object[0]);
        } catch (Exception var5) {
        }

        return str;
    }

    public void JsExecutor(String script, String logMessage) {
        try {
            JavascriptExecutor js = (JavascriptExecutor)this.driver;
            js.executeScript(script, new Object[0]);
        } catch (Exception var4) {
        }
    }

    public void notEqualsToText(String actual, String expected, String logMessage) {
        if (!actual.equals(expected)) {
            AtuReports.passResults1("Verify text " + logMessage, "--", expected, actual);
            Reporter.LogEvent(TestStatus.PASS, "Verify text " + logMessage, expected, actual);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, expected, actual);
            AtuReports.failResults("Verify text " + logMessage, "--", expected, actual);
        }
    }

    public void isTextPresent(String actual, String expected, String logMessage) {
        if (actual.contains(expected)) {
            AtuReports.passResults1("Verify text " + logMessage, "--", expected, actual);
            Reporter.LogEvent(TestStatus.PASS, "Verify text " + logMessage, expected, actual);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text " + logMessage, expected, actual);
            AtuReports.failResults("Verify text " + logMessage, "--", expected, actual);
        }
    }

    public void isTrue(boolean condition, String logMessage) {
        if (condition) {
            AtuReports.passResults1("Verify text is present " + logMessage, "--", logMessage, logMessage);
            Reporter.LogEvent(TestStatus.PASS, "Verify text is present " + logMessage, logMessage, logMessage);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify text is not present " + logMessage, logMessage, logMessage);
            AtuReports.failResults("Verify text is not present " + logMessage, "--", logMessage, logMessage);
        }
    }

    public static int getPercentage(int amount, int percentage) {
        int result = 0;
        result = amount * percentage / 100;
        return result;
    }

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(5, days);
        return cal.getTime();
    }

    public boolean isValidFormat(String inputDate, String format) {
        boolean result = false;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(inputDate);
            System.out.println(date);
            result = true;
        } catch (Exception var6) {
        }
        return result;
    }

    public static String getTimeDifference(long from) {
        long to = (new Date()).getTime();
        long diff = to - from;
        long diffSeconds = diff / 1000L % 60L;
        long diffMinutes = diff / 60000L % 60L;
        long diffHours = diff / 3600000L % 24L;
        long diffDays = diff / 86400000L;
        return diffDays + ":" + diffHours + ":" + diffMinutes + ":" + diffSeconds;
    }

    public Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        Date date = new Date();
        System.out.println(new Timestamp(date.getTime()));
        return sdf.format(timestamp);
    }

    public static String getCurrentTimeStampIncludingSS() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        Date date = new Date();
        System.out.println(new Timestamp(date.getTime()));
        return sdf.format(timestamp);
    }

    public void validateInvalidImages(String tagName) {
        int numberOfElementsFound = this.getNumberOfElementsFound(By.tagName(tagName));
        System.out.println(numberOfElementsFound);

        for(int pos = 0; pos < numberOfElementsFound - 1; ++pos) {
            url = this.getElementWithIndex(By.tagName(tagName), pos).getAttribute("src");
            if (url != null && !url.contains("javascript") && !url.isEmpty() && !url.startsWith("/") && !url.startsWith("tel")) {
                verifyURLStatus(url);
            }
        }

        AtuReports.passResults1("Valid Images Count", "", "Valid details ", "NO: of Valid Images::" + validLinksCount);
        AtuReports.passResults1("Invalid Images Count", "", "InValid details ", "NO: of InValid Images::" + invalidLinksCount);
        AtuReports.passResults1("Connection Error Url", "", "connection details ", "NO: of connection Images::" + connectionErrorUrl);
    }

    public static String generateRandomChars() {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < 8; ++i) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }

        return sb.toString();
    }

    public void removeAttribute(WebElement element, String attr) {
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].removeAttribute('" + attr + "')", new Object[]{element});
    }

    public void sendEmail(Map<String, String> data, String methodName) throws MessagingException, AddressException {
        String[] mailTo = ((String)data.get("EmailTo")).toString().split(";");
        String[] mailCc;
        try {
            mailCc = ((String)data.get("EmailCc")).toString().split(";");
        } catch (Exception var10) {
            mailCc = null;
        }

        String subject = ((String)data.get("EmailSubject")).toString() + " Execution is completed for " + (String)data.get("PmcName") + " -" + (new SimpleDateFormat("MM/dd/yyyy")).format(new Date());
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "mail.realpage.com");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "false");
        properties.put("mail.smtp.starttls.enable", "false");
        Session session = Session.getInstance(properties);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("no_reply_QA@realpage.com"));
        for(int i = 0; i < mailTo.length; ++i) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo[i]));
        }

        if (mailCc != null) {
            for(int i = 0; i < mailCc.length; ++i) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(mailCc[i]));
            }
        }
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        if (methodName.equalsIgnoreCase("validateBIDesignerTest")) {
            msg.setContent("<div>Hi Team,</div><br> <div>" + ((String)data.get("EmailSubject")).toString() + " Generation of excel reports task is completed for PMC " + ((String)data.get("PmcName")) + ".</br> <div>Thanks,<div>AOS Automation Team</div>", "text/html");
        } else if (methodName.equalsIgnoreCase("widgetDataExportToExcel")) {
            msg.setContent("<div>Hi Team,</div><br> <div>" + ((String)data.get("EmailSubject")).toString() + " Generation of excel reports task is completed for PMC " + ((String)data.get("PmcName")) + ".</br> <div>Thanks,<div>AOS Automation Team</div>", "text/html");
        } else {
            msg.setContent("<div>Hi Team,</div><br> <div>" + ((String)data.get("EmailSubject")).toString() + " Generation of excel reports task is completed for PMC " + ((String)data.get("PmcName")) + ".</div><div>Please execute the R Script.</div><br> <br><br><div><b>NOTE:</b> Please Check Both BI Designer Expense Per Unit and Revenue Per Unit emails for above mentioned PMC has been received successfully before executing R Script..</div><br><div>Thanks,<div>AOS Automation Team</div>", "text/html");
        }
        Transport.send(msg);
    }

    public BufferedImage getPageImage() {
        explicitWait(4);
        BufferedImage pageImage = Shutterbug.shootPage(this.driver, Capture.FULL_SCROLL).getImage();
        return pageImage;
    }

    public BufferedImage getFullPageImage() {
        explicitWait(4);
        BufferedImage pageImage = Shutterbug.shootPage(this.driver, Capture.FULL_SCROLL).getImage();
        return pageImage;
    }

    public BufferedImage getPageImage(WebElement header, WebElement footer) {
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.position='absolute'", new Object[]{header});
        explicitWait(4);
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.position='absolute'", new Object[]{footer});
        explicitWait(4);
        BufferedImage pageImage = Shutterbug.shootPage(this.driver, Capture.FULL_SCROLL).getImage();
        return pageImage;
    }

    public BufferedImage getPageImage(WebElement header, WebElement footer, WebElement leftNavigation) {
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.position='absolute'", new Object[]{header});
        explicitWait(4);
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.position='absolute'", new Object[]{footer});
        explicitWait(4);
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.visibility='hidden'", new Object[]{leftNavigation});
        explicitWait(4);
        BufferedImage pageImage = Shutterbug.shootPage(this.driver, Capture.FULL_SCROLL).getImage();
        return pageImage;
    }

    public BufferedImage getPageImageHiddenElements(WebElement header, WebElement footer, WebElement leftNavigation) {
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.visibility='hidden'", new Object[]{header});
        explicitWait(4);
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.visibility='hidden'", new Object[]{footer});
        explicitWait(4);
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.visibility='hidden'", new Object[]{leftNavigation});
        explicitWait(4);
        BufferedImage pageImage = Shutterbug.shootPage(this.driver, Capture.FULL_SCROLL).getImage();
        return pageImage;
    }

    public void webElementHide(WebElement element) {
        try {
            ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.position='absolute'", new Object[]{element});
            explicitWait(2);
            AtuReports.passResults1("Verify is element Hiden " + element.getText(), "--", element.getText(), element.getText() + " Element Hide ");
            Reporter.LogEvent(TestStatus.PASS, "Verify is element Hiden " + element.getText(), element.getText(), element.getText() + " Element Hide ");
        } catch (Exception var3) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify is element Hiden " + element.getText(), element.getText(), element.getText() + " Element Not Hide ");
            AtuReports.failResults("Verify is element Hiden " + element.getText(), "--", element.getText(),  element.getText() + " Element Not Hide ");
        }
    }

    public void webElementShow(WebElement element) {
        try {
            ((JavascriptExecutor)this.driver).executeScript("arguments[0].style.position='relative'", new Object[]{element});
            explicitWait(2);
            AtuReports.passResults1("Verify is element Visible " + element.getText(), "--", element.getText(), element.getText() + " Element Visible ");
            Reporter.LogEvent(TestStatus.PASS, "is element Visible " + element.getText(), element.getText(),element.getText() + " Element Visible ");
        } catch (Exception var3) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify is element Visible " + element.getText(), element.getText(),  element.getText() + " Element Not Visible ");
            AtuReports.failResults("Verify is element Visible " + element.getText(), "--", element.getText(), element.getText() + " Element Not Visible ");
        }
    }

    public void copy(String text) {
        Clipboard clipboard = this.getSystemClipboard();
        clipboard.setContents(new StringSelection(text), (ClipboardOwner)null);
    }

    public void paste() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(17);
        robot.keyPress(86);
        robot.keyRelease(17);
        robot.keyRelease(86);
        robot.keyRelease(10);
    }

    public String get() throws Exception {
        Clipboard systemClipboard = this.getSystemClipboard();
        DataFlavor dataFlavor = DataFlavor.stringFlavor;
        if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
            Object text = systemClipboard.getData(dataFlavor);
            return (String)text;
        } else {
            return null;
        }
    }

    private Clipboard getSystemClipboard() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
        return systemClipboard;
    }

    public void sendKeyActions(Keys sendKey) {
        try {
            Actions action = new Actions(this.driver);
            action.sendKeys(new CharSequence[]{sendKey}).perform();
        } catch (Exception e) {
            AtuReports.failResults("Verify Sendkey action", "--", "Should be perform sendKeys action", "sendKeys is not perform: " + this.catchException(e));
            Reporter.LogEvent(TestStatus.FAIL, "Should be perform sendKeys action", "sendKeys is not perform", this.catchException(e));
        }
    }

    public void useEscapeElement() {
        try {
            Actions action = new Actions(this.driver);
            action.sendKeys(new CharSequence[]{Keys.ESCAPE}).perform();
            explicitWait(3);
        } catch (Exception e) {
            AtuReports.failResults("Verify close dialog using escape action", "--", "Should be able to hover", "Mouse hover is not success: " + this.catchException(e));
            Reporter.LogEvent(TestStatus.FAIL, "Verify Mouse hover action", "Should be able to hover", this.catchException(e));
        }
    }

    public void dynamicTextCompare(String actual, String pattenString) {
        if (actual.matches(pattenString)) {
            AtuReports.passResults1("Verify patten String ", "--", pattenString, actual + "<b> is matched");
            Reporter.LogEvent(TestStatus.PASS, "patten String ", pattenString, actual);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "patten String ", pattenString, actual + "<b> is not matched");
            AtuReports.failResults("Verify patten String", "--", pattenString, actual);
        }
    }

    public void userActBeforeTimeOut(int sessionTimeOut) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("started time " + dtf.format(LocalDateTime.now()));
        AtuReports.passResults("Verify timeOut start time ", "--", "started time ", "capture time :" + dtf.format(LocalDateTime.now()).toString());
        for(int i = 0; i < sessionTimeOut * 60 / 300; ++i) {
            explicitWait(300);
            this.driver.navigate().refresh();
            explicitWait(3);
            System.out.println(dtf.format(LocalDateTime.now()));
            if (this.driver.getCurrentUrl().contains("app/login")) {
                AtuReports.failResults("Verify Session time-out idleTime < 30hrs ", "--", "application Session time-out before idleTime @ time: ", "capture time :" + dtf.format(LocalDateTime.now()).toString());
                System.out.println("application Session time-out before idleTime @ time: " + dtf.format(LocalDateTime.now()).toString());
                break;
            }
        }
        AtuReports.passResults("Verify Session time-out idleTime < 30hrs ", "--", "user performing some activities on application @ time: ", "capture time :" + dtf.format(LocalDateTime.now()).toString());
    }

    public void sessionTimeOut(int sessionTimeOut, Boolean sessionActive) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("started time " + dtf.format(LocalDateTime.now()));
        AtuReports.passResults("Verify timeOut start time ", "--", "started time ", "capture time :" + dtf.format(LocalDateTime.now()).toString());
        this.driver.navigate().refresh();
        explicitWait(3);
        if (sessionActive) {
            if (this.driver.getCurrentUrl().contains("app/login")) {
                AtuReports.failResults1("Verify Session time-out idleTime < 30hrs ", "--", "application Session time-out before idleTime @ time: ", "capture time :" + dtf.format(LocalDateTime.now()).toString());
                System.out.println("application Session time-out before idleTime @ time: " + dtf.format(LocalDateTime.now()).toString());
            } else {
                AtuReports.passResults("Verify Session time-out idleTime < 30hrs ", "--", "user performing some activities on application @ time: ", "capture time :" + dtf.format(LocalDateTime.now()).toString());
                System.out.println("user performing some activities on application @ time: " + dtf.format(LocalDateTime.now()).toString());
            }
        }
    }

    public void deleteFilesinTempFolder() {
        String reportFolderPath = System.getProperty("user.dir") + "\\temp";
        File directory = new File(reportFolderPath);
        File[] files = directory.listFiles();
        for(int i = 0; i < files.length; ++i) {
            files[i].delete();
        }
    }

    public WebElement waitUntilClickable(WebElement element, String logMessage) {
        this.waitForPageElement(element, WaitType.WAIT_FOR_ELEMENT_TO_BE_CLICKABLE, logMessage);
        return element;
    }

    public WebElement waitUntilPresent(By locator) {
        return this.waitUntilPresent(locator, 10);
    }

    public WebElement waitUntilPresent(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeout));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        try {
            return this.driver.findElement(locator);
        } catch (Exception var5) {
            return null;
        }
    }

    public List<WebElement> waitUntilAllElementsPresent(By locator) {
        return this.waitUntilAllElementsPresent(locator, 10);
    }

    public List<WebElement> waitUntilAllElementsPresent(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeout));
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (Exception var5) {
            return new ArrayList();
        }
        return this.driver.findElements(locator);
    }

    public void setImplicitWait(int seconds) {
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds((long)seconds));
    }

    public void setImplicitWait(Duration duration) {
        this.driver.manage().timeouts().implicitlyWait(duration);
    }

    public Duration getImplicitWait() {
        return this.driver.manage().timeouts().getImplicitWaitTimeout();
    }

    public void waitForNumberOfWindowsToBePresent(int numberofWindows, int timeOut) {
        try {
            this.webDriverHelper.waitForNumberWindowsToBe(numberofWindows, timeOut);
            AtuReports.passResults1("Verify Number Of Windows ", "--", numberofWindows + " should be appeared ",
                    this.driver.getWindowHandles().size() + " is appeared");
            Reporter.LogEvent(TestStatus.PASS, "Verify Number Of Windows ", numberofWindows + " should be appeared ",
                    this.driver.getWindowHandles().size() + " is appeared");
        } catch (Exception var4) {
            Reporter.LogEvent(TestStatus.FAIL, "Verify Number Of Windows ", numberofWindows + " should be appeared ",
                    this.driver.getWindowHandles().size() + " is appeared");
            AtuReports.failResults("Verify Number Of Windows ", "--", numberofWindows + " should be appeared ",
                    this.driver.getWindowHandles().size() + " is appeared");
        }
    }
}
