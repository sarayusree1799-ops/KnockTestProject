package FrameWorkPackage.com.rp.automation.framework.webdriver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class WebDriverHelper {
    private WebDriver driver;

    public WebDriverHelper(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForElementToBeDisplayed(final WebElement element, int timeOutPeriod, String logMessage) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        return (WebElement)webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    return element.isDisplayed() ? element : null;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public WebElement waitForElementToBeDisplayed(final By by, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis(1L));
        return (WebElement)webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(by);
                    return element.isDisplayed() ? element : null;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public WebDriver WAIT_FOR_FRAME_TO_BE_DISPLAYED(WebDriver driver, int timeOutPeriod, String svalue) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(svalue));
        return (WebDriver)webDriverWait.until(new ExpectedCondition<WebDriver>() {
            public WebDriver apply(WebDriver driver) {
                try {
                    return driver;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public void WAIT_FOR_FRAME_TO_BE_DISPLAYED(WebDriver driver, int timeOutPeriod, By id) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis(1L));
        webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(id));
    }

    public WebDriver WaitForFrametoBeDisplayedByIndex(WebDriver driver, int timeOutPeriod, final int index) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        return (WebDriver)webDriverWait.until(new ExpectedCondition<WebDriver>() {
            public WebDriver apply(WebDriver driver) {
                WebDriver TLocator = null;
                TLocator = driver.switchTo().frame(index);
                return TLocator != null ? TLocator : driver;
            }
        });
    }


    public void waitForElementToBeClickable(WebElement pageElement, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(pageElement));
    }

    public WebElement waitForElementToBeClickable(final By by, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        return (WebElement)webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(by);
                    return element.isEnabled() && element.isDisplayed() ? element : null;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public WebElement waitForElementToBeClickable(final WebElement element, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        return (WebElement)webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    return element.isEnabled() && element.isDisplayed() ? element : null;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public WebElement waitForElementToBeEnabled(final WebElement element, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        return (WebElement)webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    return element.isEnabled() ? element : null;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public WebElement waitForElementToBeEnabled(final By by, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        return (WebElement)webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(by);
                    return element.isEnabled() ? element : null;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public WebElement waitForOptionToBePopulatedInList(final WebElement dropdownList, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis(1L));
        return (WebElement)webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                try {
                    List<WebElement> options = dropdownList.findElements(By.tagName("option"));
                    return options.size() > 1 ? dropdownList : null;
                } catch (NoSuchElementException var3) {
                    return null;
                } catch (StaleElementReferenceException var4) {
                    return null;
                } catch (NullPointerException var5) {
                    return null;
                }
            }
        });
    }

    public void waitForElementToDisappear(By by, int timeOutPeriod) {
        FluentWait<By> fluentWait = new FluentWait(by);
        fluentWait.pollingEvery(Duration.ofMillis((long) timeOutPeriod));
        fluentWait.withTimeout(Duration.ofSeconds((long) timeOutPeriod));
        fluentWait.until(new Function<By, Boolean>() {
            Boolean isElementFound;

            {
                this.isElementFound = Boolean.FALSE;
            }

            public Boolean apply(By by) {
                try {
                    this.isElementFound = !WebDriverHelper.this.driver.findElement(by).isDisplayed();
                    return this.isElementFound;
                } catch (NoSuchElementException var3) {
                    return this.isElementFound;
                }
            }
        });
    }


    public void waitForElementToDisappear(WebElement element, int timeOutPeriod) {
        FluentWait<WebElement> fluentWait = new FluentWait(element);
        fluentWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        fluentWait.withTimeout(Duration.ofSeconds((long)timeOutPeriod));
        fluentWait.until(new Function<WebElement, Boolean>() {
            public Boolean apply(WebElement element) {
                try {
                    return !element.isDisplayed();
                } catch (NoSuchElementException var3) {
                    return true;
                } catch (StaleElementReferenceException var4) {
                    return true;
                }
            }
        });
    }

    public void waitForAlert(int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.ignoring(NoSuchElementException.class,
                StaleElementReferenceException.class).pollingEvery(Duration.ofSeconds(10L)).until(ExpectedConditions.alertIsPresent());
    }

    public String acceptAlert(int timeOutPeriod) {
        this.waitForAlert(timeOutPeriod);
        Alert alert = this.driver.switchTo().alert();
        String AlertMessage = alert.getText();
        alert.accept();
        return AlertMessage;
    }

    public String dismissAlert(int timeOutPeriod) {
        this.waitForAlert(timeOutPeriod);
        Alert alert = this.driver.switchTo().alert();
        String AlertMessage = alert.getText();
        alert.dismiss();
        return alert.getText();
    }

    public void explicitWait(int waitTime) {
        try {
            Thread.sleep((long)(waitTime * 1000));
        } catch (InterruptedException var3) {
        }
    }

    public void waitForTextToBePresentInElement(WebElement pageElement, String text, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(pageElement, text));
    }

    public void waitForTextToBeAbsentInElement(By by, String text, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        webDriverWait.until(ExpectedConditions.invisibilityOfElementWithText(by, text));
    }

    public void WaitForPageLoad(int timeOutPeriod) {
        try {
            FluentWait<WebDriver> fluentWait = new FluentWait(this.driver);
            fluentWait.pollingEvery(Duration.ofSeconds((long)timeOutPeriod));
            fluentWait.withTimeout(Duration.ofSeconds((long)timeOutPeriod));
            fluentWait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver1) {
                    System.out.println("State " + ((JavascriptExecutor)WebDriverHelper.this.driver).executeScript("return document.readyState", new Object[0]));
                    return ((JavascriptExecutor)WebDriverHelper.this.driver).executeScript("return document.readyState", new Object[0]).equals("complete");
                }
            });
        } catch (Exception e) {
            System.out.println(e + " Exception");
        }
    }

    public void waitForUrlContains(String expectedString, int timeOutPeriod) {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        ExpectedCondition<Boolean> urlIsCorrect = (arg0) -> this.driver.getCurrentUrl().contains(expectedString);
        wait.until(urlIsCorrect);
        this.WaitForPageLoad(timeOutPeriod);
    }

    public void waitForNumberWindowsToBe(int numberOfWindows, int timeOutPeriod) {
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds((long)timeOutPeriod));
        webDriverWait.pollingEvery(Duration.ofMillis((long)timeOutPeriod));
        webDriverWait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
    }
}