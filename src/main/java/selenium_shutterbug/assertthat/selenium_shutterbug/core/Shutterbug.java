package selenium_shutterbug.assertthat.selenium_shutterbug.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.Browser;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.Coordinates;
import java.util.function.Function;

public class Shutterbug {

    private static final int DEFAULT_SCROLL_TIMEOUT = 100;
    private static Function<WebDriver, ?> beforeShootCondition;
    private static int beforeShootTimeout;

    private Shutterbug() {
    }

    public static PageSnapshot shootPage(WebDriver driver) {
        return shootPage(driver, true);
    }

    public static PageSnapshot shootPage(WebDriver driver, boolean useDevicePixelRatio) {
        Browser browser = new Browser(driver, useDevicePixelRatio);
        PageSnapshot pageScreenshot = new PageSnapshot(driver, browser.getDevicePixelRatio());
        pageScreenshot.setImage(browser.takeScreenshot());
        return pageScreenshot;
    }

    public static PageSnapshot shootPage(WebDriver driver, Capture capture) {
        return shootPage(driver, capture, 100);
    }

    public static PageSnapshot shootPage(WebDriver driver, Capture capture, int betweenScrollTimeout) {
        return shootPage(driver, capture, betweenScrollTimeout, true);
    }

    public static Shutterbug wait(ExpectedCondition<?> cond, int timeout) {
        beforeShootCondition = cond;
        beforeShootTimeout = timeout;
        return null;
    }

    public static Shutterbug wait(int timeout) {
        beforeShootTimeout = timeout;
        return null;
    }

    public static PageSnapshot shootPage(WebDriver driver, Capture capture, boolean useDevicePixelRatio) {
        return shootPage(driver, capture, 0, useDevicePixelRatio);
    }

    public static PageSnapshot shootPage(WebDriver driver, Capture capture, int betweenScrollTimeout, boolean useDevicePixelRatio) {
        Browser browser = new Browser(driver, useDevicePixelRatio);
        browser.setBetweenScrollTimeout(betweenScrollTimeout);
        if (beforeShootCondition != null) {
            browser.setBeforeShootTimeout(beforeShootTimeout);
            browser.setBeforeShootCondition(beforeShootCondition);
        } else if (beforeShootTimeout != 0) {
            browser.setBeforeShootTimeout(beforeShootTimeout);
        }

        PageSnapshot pageScreenshot = new PageSnapshot(driver, browser.getDevicePixelRatio());
        switch (capture) {
            case VIEWPORT:
                pageScreenshot.setImage(browser.takeScreenshot());
                break;
            case FULL:
                pageScreenshot.setImage(browser.takeFullPageScreenshot());
                break;
            case VERTICAL_SCROLL:
                pageScreenshot.setImage(browser.takeFullPageVerticalScreenshotScroll((Coordinates) null));
                break;
            case HORIZONTAL_SCROLL:
                pageScreenshot.setImage(browser.takeFullPageHorizontalScreenshotScroll((Coordinates) null));
                break;
            case FULL_SCROLL:
                pageScreenshot.setImage(browser.takeFullPageScreenshotScroll((Coordinates) null));
                break;
        }

        return pageScreenshot;
    }

    public static ElementSnapshot shootElementVerticallyCentered(WebDriver driver, WebElement element) {
        return shootElementVerticallyCentered(driver, element, true);
    }

    public static ElementSnapshot shootElement(WebDriver driver, WebElement element) {
        return shootElement(driver, element, true);
    }

    public static ElementSnapshot shootElement(WebDriver driver, WebElement element, CaptureElement capture) {
        return shootElement(driver, element, capture, true);
    }

    public static ElementSnapshot shootElement(WebDriver driver, By element, CaptureElement capture) {
        return shootElement(driver, element, capture, true);
    }

    public static ElementSnapshot shootElement(WebDriver driver, WebElement element, boolean useDevicePixelRatio) {
        Browser browser = new Browser(driver, useDevicePixelRatio);
        ElementSnapshot elementSnapshot = new ElementSnapshot(driver, browser.getDevicePixelRatio());
        browser.scrollToElement(element);
        elementSnapshot.setImage(browser.takeScreenshot(), browser.getCoordinates(element));
        return elementSnapshot;
    }

    public static ElementSnapshot shootElement(WebDriver driver, WebElement element, CaptureElement capture, boolean useDevicePixelRatio) {
        Browser browser = new Browser(driver, useDevicePixelRatio);
        ElementSnapshot elementSnapshot = new ElementSnapshot(driver, browser.getDevicePixelRatio());
        browser.scrollToElement(element);
        switch (capture) {
            case VERTICAL_SCROLL:
                elementSnapshot.setImage(browser.takeFullPageVerticalScreenshotScroll(browser.getCoordinates(element)), browser.getCoordinates(element));
                break;
            case HORIZONTAL_SCROLL:
                elementSnapshot.setImage(browser.takeFullPageHorizontalScreenshotScroll(browser.getCoordinates(element)), browser.getCoordinates(element));
                break;
            case FULL_SCROLL:
                elementSnapshot.setImage(browser.takeFullPageScreenshotScroll(browser.getCoordinates(element)), browser.getCoordinates(element));
                break;
            default:
                elementSnapshot.setImage(browser.takeScreenshot(), browser.getCoordinates(element));
        }
        return elementSnapshot;
    }

    public static ElementSnapshot shootElement(WebDriver driver, By by, CaptureElement capture, boolean useDevicePixelRatio) {
        Browser browser = new Browser(driver, useDevicePixelRatio);
        ElementSnapshot elementSnapshot = new ElementSnapshot(driver, browser.getDevicePixelRatio());
        browser.scrollToElement(driver.findElement(by));
        switch (capture) {
            case VERTICAL_SCROLL:
                elementSnapshot.setImage(browser.takeFullPageVerticalScreenshotScroll(browser.getCoordinates(driver.findElement(by))), browser.getCoordinates(driver.findElement(by)));
                break;
            case HORIZONTAL_SCROLL:
                elementSnapshot.setImage(browser.takeFullPageHorizontalScreenshotScroll(browser.getCoordinates(driver.findElement(by))), browser.getCoordinates(driver.findElement(by)));
                break;
            case FULL_SCROLL:
                elementSnapshot.setImage(browser.takeFullPageScreenshotScroll(browser.getCoordinates(driver.findElement(by))), browser.getCoordinates(driver.findElement(by)));
                break;
            default:
                elementSnapshot.setImage(browser.takeElementViewportScreenshot(by));
        }
        return elementSnapshot;
    }

    public static ElementSnapshot shootElementVerticallyCentered(WebDriver driver, WebElement element, boolean useDevicePixelRatio) {
        Browser browser = new Browser(driver, useDevicePixelRatio);
        ElementSnapshot elementSnapshot = new ElementSnapshot(driver, browser.getDevicePixelRatio());
        browser.scrollToElementVerticalCentered(element);
        elementSnapshot.setImage(browser.takeScreenshot(), browser.getCoordinates(element));
        return elementSnapshot;
    }

    public static PageSnapshot shootFrame(WebDriver driver, String frameId, CaptureElement capture, boolean useDevicePixelRatio) {
        WebElement frame = driver.findElement(By.id(frameId));
        return shootFrame(driver, frame, capture, 0, useDevicePixelRatio);
    }

    public static PageSnapshot shootFrame(WebDriver driver, WebElement frame, CaptureElement capture, boolean useDevicePixelRatio) {
        return shootFrame(driver, frame, capture, 0, useDevicePixelRatio);
    }

    public static PageSnapshot shootFrame(WebDriver driver, WebElement frame, CaptureElement capture) {
        return shootFrame(driver, frame, capture, 0, true);
    }

    public static PageSnapshot shootFrame(WebDriver driver, WebElement frame, CaptureElement capture, int betweenScrollTimeout, boolean useDevicePixelRatio) {
        Browser browser = new Browser(driver, useDevicePixelRatio);
        browser.setBetweenScrollTimeout(betweenScrollTimeout);
        browser.scrollToElement(frame);
        Coordinates coordinates = browser.getCoordinates(frame);
        Browser browserParent = new Browser(driver, useDevicePixelRatio);
        if (capture == CaptureElement.VIEWPORT || coordinates.getWidth() <= browserParent.getViewportWidth() && coordinates.getHeight() <= browserParent.getViewportHeight()) {
            driver.switchTo().frame(frame);
            if (beforeShootCondition != null) {
                browser.setBeforeShootTimeout(beforeShootTimeout);
                browser.setBeforeShootCondition(beforeShootCondition);
            } else if (beforeShootTimeout != 0) {
                browser.setBeforeShootTimeout(beforeShootTimeout);
            }
            PageSnapshot pageScreenshot = new PageSnapshot(driver, browser.getDevicePixelRatio());
            switch (capture) {
                case VERTICAL_SCROLL:
                    pageScreenshot.setImage(browser.takeFullPageVerticalScreenshotScroll(coordinates));
                    break;
                case HORIZONTAL_SCROLL:
                    pageScreenshot.setImage(browser.takeFullPageHorizontalScreenshotScroll(coordinates));
                    break;
                case FULL_SCROLL:
                    pageScreenshot.setImage(browser.takeFullPageScreenshotScroll(coordinates));
                    break;
                default:
                    pageScreenshot.setImage(browser.takeFrameViewportScreenshot(coordinates));
            }

            return pageScreenshot;
        } else {
            throw new UnsupportedOperationException("Full frame screenshot is only available if WHOLE frame is fully visible in the viewport. Use CaptureElement.VIEWPORT in case frame is outside of visible viewport.");
        }
    }

    public static PageSnapshot shootFrame(WebDriver driver, String frameId, CaptureElement capture) {
        WebElement frame = driver.findElement(By.id(frameId));
        return shootFrame(driver, frame, capture, true);
    }

    public static PageSnapshot shootFrame(WebDriver driver, String frameId) {
        WebElement frame = driver.findElement(By.id(frameId));
        return shootFrame(driver, frame, CaptureElement.VIEWPORT, true);
    }
}