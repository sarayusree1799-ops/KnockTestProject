package FrameWorkPackage.com.rp.automation.framework.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebDriverHelper {
    private WebDriver driver;

    public WebDriverHelper(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForElementToBeDisplayed(final WebElement element, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis(timeOutPeriod));
        return webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

    public WebElement waitForElementToBeClickable(final WebElement element, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis(timeOutPeriod));
        return webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }
}
