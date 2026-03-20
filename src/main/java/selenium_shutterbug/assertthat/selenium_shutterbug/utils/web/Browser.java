package selenium_shutterbug.assertthat.selenium_shutterbug.utils.web;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.support.ui.FluentWait;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.file.FileUtil;

import com.github.zafarkhaja.semver.Version;
import com.google.common.collect.ImmutableMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.lang.reflect.*;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

import javax.imageio.ImageIO;
import org.openqa.selenium.*;


public class Browser {
    private static final String RELATIVE_COORDS_JS = "js/relative-element-coords.js";
    private static final String MAX_DOC_WIDTH_JS = "js/max-document-width.js";
    private static final String MAX_DOC_HEIGHT_JS = "js/max-document-height.js";
    private static final String VIEWPORT_HEIGHT_JS = "js/viewport-height.js";
    private static final String VIEWPORT_WIDTH_JS = "js/viewport-width.js";
    private static final String SCROLL_TO_JS = "js/scroll-to.js";
    private static final String SCROLL_BY_JS = "js/scroll-by.js";
    private static final String SCROLL_ELEMENT = "js/scroll-element.js";
    private static final String SCROLL_INTO_VIEW_JS = "js/scroll-element-into-view.js";
    private static final String SCROLL_INTO_VIEW_VERTICAL_CENTERED_JS = "js/scroll-element-into-view-vertical-centered.js";
    private static final String CURRENT_SCROLL_Y_JS = "js/get-current-scrollY.js";
    private static final String CURRENT_SCROLL_X_JS = "js/get-current-scrollX.js";
    private static final String DEVICE_PIXEL_RATIO = "js/get-device-pixel-ratio.js";
    private static final String DOC_SCROLL_BAR_WIDTH = "js/doc-scrollbar-width.js";
    private static final String ELEMENT_SCROLL_BAR_HEIGHT = "js/element-scrollbar-height.js";
    private static final String ELEMENT_SCROLL_BAR_WIDTH = "js/element-scrollbar-width.js";
    private static final String ALL_METRICS = "js/all-metrics.js";
    private static final String ELEMENT_CURRENT_SCROLL_X_JS = "js/get-current-element-scrollX.js";
    private static final String ELEMENT_CURRENT_SCROLL_Y_JS = "js/get-current-element-scrollY.js";

    private WebDriver driver;
    private int docHeight = -1;
    private int docWidth = -1;
    private int viewportWidth = -1;
    private int viewportHeight = -1;
    private int betweenScrollTimeout;
    private Function<WebDriver, ?> beforeShootCondition;
    private int beforeShootTimeout;
    private Double devicePixelRatio = (double) 1.0F;

    public Browser(WebDriver driver, boolean useDevicePixelRatio) {
        this.driver = driver;
        if (useDevicePixelRatio) {
            Object devicePixelRatio = this.executeJsScript("js/get-device-pixel-ratio.js");
            this.devicePixelRatio = devicePixelRatio instanceof Double ? (Double) devicePixelRatio : (double) (Long) devicePixelRatio * (double) 1.0F;
        }
    }

    public static void wait(int ms) {
        try {
            Thread.sleep((long) ms);
        } catch (InterruptedException e) {
            throw new UnableTakeSnapshotException(e);
        }
    }

    public Double getDevicePixelRatio() {
        return this.devicePixelRatio;
    }

    public void wait(Function<WebDriver, ?> condition, int timeout) {
        if (condition != null) {
            (new FluentWait(this.driver)).withTimeout(Duration.ofSeconds((long) timeout)).ignoring(StaleElementReferenceException.class,
                    NoSuchMethodException.class).until(condition);
        } else if (timeout > 0) {
            wait(timeout);
        }
    }

    public void setBetweenScrollTimeout(int betweenScrollTimeout) {
        this.betweenScrollTimeout = betweenScrollTimeout;
    }

    public void setBeforeShootTimeout(int beforeShootTimeout) {
        this.beforeShootTimeout = beforeShootTimeout;
    }

    public void setBeforeShootCondition(Function<WebDriver, ?> beforeShootCondition) {
        this.beforeShootCondition = beforeShootCondition;
    }

    public BufferedImage takeScreenshot() {
        this.wait(this.beforeShootCondition, this.beforeShootTimeout);
        File srcFile = (File) ((TakesScreenshot) this.getUnderlyingDriver()).getScreenshotAs(OutputType.FILE);

        BufferedImage var2;
        try {
            var2 = ImageIO.read(srcFile);
        } catch (IOException e) {
            throw new UnableTakeSnapshotException(e);
        } finally {
            if (srcFile.exists()) {
                srcFile.delete();
            }
        }
        return var2;
    }

    public BufferedImage takeFullPageScreenshot() {
        this.driver = this.unwrapDriver();
        if (!(this.driver instanceof ChromeDriver) && !(this.driver instanceof EdgeDriver)) {
            if (this.driver instanceof FirefoxDriver) {
                return this.takeFullPageScreenshotGeckoDriver();
            } else {
                if (this.driver instanceof RemoteWebDriver) {
                    if (((RemoteWebDriver) this.driver).getCapabilities().getBrowserName().equals("chrome") ||
                            ((RemoteWebDriver) this.driver).getCapabilities().getBrowserName().equals("MicrosoftEdge") ||
                            ((RemoteWebDriver) this.driver).getCapabilities().getBrowserName().equals("msedge")) {
                        return this.takeFullPageScreenshotChromeCommand();
                    }

                    if (((RemoteWebDriver) this.driver).getCapabilities().getBrowserName().equals("firefox")) {
                        return this.takeFullPageScreenshotGeckoDriver();
                    }
                }
                return this.takeFullPageScreenshotScroll((Coordinates) null);
            }
        } else {
            return this.takeFullPageScreenshotChromeCommand();
        }
    }

    public BufferedImage takeFullPageElementScreenshot() {
        this.driver = this.unwrapDriver();
        if (!(this.driver instanceof ChromeDriver) && !(this.driver instanceof EdgeDriver)) {
            if (this.driver instanceof FirefoxDriver) {
                return this.takeFullPageScreenshotGeckoDriver();
            } else {
                if (this.driver instanceof RemoteWebDriver) {
                    if (((RemoteWebDriver)this.driver).getCapabilities().getBrowserName().equals("chrome") ||
                            ((RemoteWebDriver)this.driver).getCapabilities().getBrowserName().equals("MicrosoftEdge")) {
                        return this.takeFullPageScreenshotChromeCommand();
                    }

                    if (((RemoteWebDriver)this.driver).getCapabilities().getBrowserName().equals("firefox")) {
                        return this.takeFullPageScreenshotGeckoDriver();
                    }
                }
                throw new UnsupportedOperationException("Full scrollable element screenshot is supported in Chrome, Firefox and MicrosoftEdge browsers only.");
            }
        } else {
            return this.takeFullPageScreenshotChromeCommand();
        }
    }

    private WebDriver unwrapDriver() {
        String[] wrapperClassNames = new String[]{"org.openqa.selenium.WrapsDriver", "org.openqa.selenium.internal.WrapsDriver"};

        for (String wrapperClassName : wrapperClassNames) {
            try {
                Class<?> clazz = Class.forName(wrapperClassName);
                if (clazz.isInstance(this.driver)) {
                    return (WebDriver) clazz.getMethod("getWrappedDriver").invoke(this.driver);
                }
            } catch (ReflectiveOperationException var7) {
            }
        }
        return this.driver;
    }

    public BufferedImage takeFullPageScreenshotScroll(Coordinates coordinates) {
        int docWidth = this.getDocWidth();
        int docHeight = this.getDocHeight();
        BufferedImage combinedImage = new BufferedImage(docWidth, docHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int viewportWidth = this.getViewportWidth();
        int viewportHeight = this.getViewportHeight();
        int scrollBarMaxWidth = this.getDocScrollBarWidth();
        if (viewportWidth < docWidth || viewportHeight < docHeight && viewportWidth - scrollBarMaxWidth < docWidth) {
            viewportHeight -= scrollBarMaxWidth;
        }

        if (viewportHeight < docHeight) {
            viewportWidth -= scrollBarMaxWidth;
        }

        int horizontalIterations = (int)Math.ceil((double)docWidth / (double)viewportWidth);
        int verticalIterations = (int)Math.ceil((double)docHeight / (double)viewportHeight);
        this.wait(this.beforeShootCondition, this.beforeShootTimeout);

        label38:
        for(int j = 0; j < verticalIterations; ++j) {
            this.scrollTo(0, j * viewportHeight);

            for(int i = 0; i < horizontalIterations; ++i) {
                this.scrollTo(i * viewportWidth, viewportHeight * j);
                wait(this.betweenScrollTimeout);
                BufferedImage image = this.takeScreenshot();
                if (coordinates != null) {
                    image = image.getSubimage(coordinates.getX(), coordinates.getY(), coordinates.getWidth(), coordinates.getHeight());
                }

                g.drawImage(image, this.getCurrentScrollX(), this.getCurrentScrollY(), (ImageObserver)null);
                if (docWidth == image.getWidth((ImageObserver)null) && docHeight == image.getHeight((ImageObserver)null)) {
                    break label38;
                }
            }
        }

        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullPageHorizontalScreenshotScroll(Coordinates coordinates) {
        int docWidth = this.getDocWidth();
        int docHeight = this.getDocHeight();
        int viewportWidth = this.getViewportWidth();
        int viewportHeight = this.getViewportHeight();
        int scrollBarMaxWidth = this.getDocScrollBarWidth();
        if (viewportWidth < docWidth || viewportHeight < docHeight && viewportWidth - scrollBarMaxWidth < docWidth) {
            viewportHeight -= scrollBarMaxWidth;
        }

        if (viewportHeight < docHeight) {
            viewportWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(docWidth, viewportHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int horizontalIterations = (int)Math.ceil((double)docWidth / (double)viewportWidth);

        for(int i = 0; i < horizontalIterations; ++i) {
            this.scrollTo(i * viewportWidth, this.getCurrentScrollY());
            wait(this.betweenScrollTimeout);
            BufferedImage image = this.takeScreenshot();
            if (coordinates != null) {
                image = image.getSubimage(coordinates.getX(), coordinates.getY(), coordinates.getWidth(), coordinates.getHeight());
            }

            g.drawImage(image, i * viewportWidth, 0, (ImageObserver)null);
            if (this.getDocWidth() == image.getWidth((ImageObserver)null)) {
                break;
            }
        }

        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullPageVerticalScreenshotScroll(Coordinates coordinates) {
        int docWidth = this.getDocWidth();
        int docHeight = this.getDocHeight();
        int viewportWidth = this.getViewportWidth();
        int viewportHeight = this.getViewportHeight();
        int scrollBarMaxWidth = this.getDocScrollBarWidth();
        if (viewportWidth < docWidth || viewportHeight < docHeight && viewportWidth - scrollBarMaxWidth < docWidth) {
            viewportHeight -= scrollBarMaxWidth;
        }

        if (viewportHeight < docHeight) {
            viewportWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(viewportWidth, docHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int verticalIterations = (int)Math.ceil((double)docHeight / (double)viewportHeight);

        for(int j = 0; j < verticalIterations; ++j) {
            this.scrollTo(this.getCurrentScrollY(), j * viewportHeight);
            wait(this.betweenScrollTimeout);
            BufferedImage image = this.takeScreenshot();
            if (coordinates != null) {
                image = image.getSubimage(coordinates.getX(), coordinates.getY(), coordinates.getWidth(), coordinates.getHeight());
            }

            g.drawImage(image, 0, j * viewportHeight, (ImageObserver)null);
            if (this.getDocHeight() == image.getHeight((ImageObserver)null)) {
                break;
            }
        }

        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullElementScreenshot(WebElement element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }

        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(scrollableWidth, scrollableHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int horizontalIterations = (int)Math.ceil((double)scrollableWidth / (double)elementWidth);
        int verticalIterations = (int)Math.ceil((double)scrollableHeight / (double)elementHeight);
        this.wait(this.beforeShootCondition, this.beforeShootTimeout);

        label32:
        for(int j = 0; j < verticalIterations; ++j) {
            this.scrollElement((WebElement)element, 0, j * elementHeight);

            for(int i = 0; i < horizontalIterations; ++i) {
                this.scrollElement(element, i * elementWidth, j * elementHeight);
                wait(this.betweenScrollTimeout);
                BufferedImage image = this.takeFullPageElementScreenshot();
                image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
                g.drawImage(image, this.getElementCurrentScrollX(element), this.getElementCurrentScrollY(element), (ImageObserver)null);
                if (scrollableWidth == image.getWidth((ImageObserver)null) && scrollableHeight == image.getHeight((ImageObserver)null)) {
                    break label32;
                }
            }
        }
        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullElementScreenshotScroll(By element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }

        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(scrollableWidth, scrollableHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int horizontalIterations = (int)Math.ceil((double)scrollableWidth / (double)elementWidth);
        int verticalIterations = (int)Math.ceil((double)scrollableHeight / (double)elementHeight);
        this.wait(this.beforeShootCondition, this.beforeShootTimeout);

        label32:
        for(int j = 0; j < verticalIterations; ++j) {
            this.scrollElement((By)element, 0, j * elementHeight);

            for(int i = 0; i < horizontalIterations; ++i) {
                this.scrollElement(element, i * elementWidth, j * elementHeight);
                wait(this.betweenScrollTimeout);
                BufferedImage image = this.takeFullPageElementScreenshot();
                image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
                g.drawImage(image, this.getElementCurrentScrollX(element), this.getElementCurrentScrollY(element), (ImageObserver)null);
                if (scrollableWidth == image.getWidth((ImageObserver)null) && scrollableHeight == image.getHeight((ImageObserver)null)) {
                    break label32;
                }
            }
        }
        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullElementVerticalScreenshotScroll(WebElement element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }

        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(elementWidth, scrollableHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int verticalIterations = (int)Math.ceil((double)scrollableHeight / (double)elementHeight);

        for(int j = 0; j < verticalIterations; ++j) {
            this.scrollElement(element, this.getElementCurrentScrollX(element), j * elementHeight);
            wait(this.betweenScrollTimeout);
            BufferedImage image = this.takeFullPageElementScreenshot();
            image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
            g.drawImage(image, 0, this.getElementCurrentScrollY(element), (ImageObserver)null);
            if (scrollableHeight == image.getHeight((ImageObserver)null)) {
                break;
            }
        }

        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullElementVerticalScreenshotScroll(By element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }

        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(elementWidth, scrollableHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int verticalIterations = (int)Math.ceil((double)scrollableHeight / (double)elementHeight);

        for(int j = 0; j < verticalIterations; ++j) {
            this.scrollElement(element, this.getElementCurrentScrollX(element), j * elementHeight);
            wait(this.betweenScrollTimeout);
            BufferedImage image = this.takeFullPageElementScreenshot();
            image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
            g.drawImage(image, 0, this.getElementCurrentScrollY(element), (ImageObserver)null);
            if (scrollableHeight == image.getHeight((ImageObserver)null)) {
                break;
            }
        }

        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullElementHorizontalScreenshotScroll(WebElement element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }

        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(scrollableWidth, elementHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int horizontalIterations = (int)Math.ceil((double)scrollableWidth / (double)elementWidth);

        for(int j = 0; j < horizontalIterations; ++j) {
            this.scrollElement(element, j * elementWidth, this.getElementCurrentScrollY(element));
            wait(this.betweenScrollTimeout);
            BufferedImage image = this.takeFullPageElementScreenshot();
            image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
            g.drawImage(image, this.getElementCurrentScrollX(element), 0, (ImageObserver)null);
            if (scrollableWidth == image.getWidth((ImageObserver)null)) {
                break;
            }
        }

        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullElementHorizontalScreenshotScroll(By element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }

        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(scrollableWidth, elementHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        int horizontalIterations = (int)Math.ceil((double)scrollableWidth / (double)elementWidth);

        for(int j = 0; j < horizontalIterations; ++j) {
            this.scrollElement(element, j * elementWidth, this.getElementCurrentScrollY(element));
            wait(this.betweenScrollTimeout);
            BufferedImage image = this.takeFullPageElementScreenshot();
            image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
            g.drawImage(image, this.getElementCurrentScrollX(element), 0, (ImageObserver)null);
            if (scrollableWidth == image.getWidth((ImageObserver)null)) {
                break;
            }
        }

        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeElementViewportScreenshot(WebElement element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }
        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(elementWidth, elementHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        wait(this.betweenScrollTimeout);
        BufferedImage image = this.takeFullPageElementScreenshot();
        image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
        g.drawImage(image, 0, 0, (ImageObserver)null);
        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeElementViewportScreenshot(By element) {
        Coordinates coordinates = this.getCoordinates(element);
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getElementScrollBarWidth(element);
        int scrollBarMaxHeight = this.getElementScrollBarHeight(element);
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxHeight < scrollableWidth) {
            elementHeight -= scrollBarMaxHeight;
        }
        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(elementWidth, elementHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        wait(this.betweenScrollTimeout);
        BufferedImage image = this.takeFullPageElementScreenshot();
        image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
        g.drawImage(image, 0, 0, (ImageObserver)null);
        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFrameViewportScreenshot(Coordinates coordinates) {
        int scrollableHeight = coordinates.getScrollHeight();
        int scrollableWidth = coordinates.getScrollWidth();
        int elementWidth = coordinates.getWidth();
        int elementHeight = coordinates.getHeight();
        int scrollBarMaxWidth = this.getDocScrollBarWidth();
        if (elementWidth < scrollableWidth || elementHeight < scrollableHeight && elementWidth - scrollBarMaxWidth < scrollableWidth) {
            elementHeight -= scrollBarMaxWidth;
        }

        if (elementHeight < scrollableHeight) {
            elementWidth -= scrollBarMaxWidth;
        }

        BufferedImage combinedImage = new BufferedImage(elementWidth, elementHeight, 2);
        Graphics2D g = combinedImage.createGraphics();
        wait(this.betweenScrollTimeout);
        BufferedImage image = this.takeFullPageElementScreenshot();
        image = image.getSubimage(coordinates.getAbsoluteX(), coordinates.getAbsoluteY(), elementWidth, elementHeight);
        g.drawImage(image, 0, 0, (ImageObserver)null);
        g.dispose();
        return combinedImage;
    }

    public BufferedImage takeFullPageScreenshotChromeCommand() {
        Object devicePixelRatio = this.executeJsScript("js/get-device-pixel-ratio.js");
        this.devicePixelRatio = devicePixelRatio instanceof Double ? (Double)devicePixelRatio : (double)(Long)devicePixelRatio * (double)1.0F;
        this.defineCustomCommand("sendCommand", new CommandInfo("/session/:sessionId/chromium/send_command_and_get_result", HttpMethod.POST));
        int verticalIterations = (int)Math.ceil((double)this.getDocHeight() / (double)this.getViewportHeight());

        for(int j = 0; j < verticalIterations; ++j) {
            this.scrollTo(0, j * this.getViewportHeight());
            wait(this.betweenScrollTimeout);
        }

        Object metrics = this.evaluate(FileUtil.getJsScript("js/all-metrics.js"));
        this.sendCommand("Emulation.setDeviceMetricsOverride", metrics);
        this.wait(this.beforeShootCondition, this.beforeShootTimeout);
        Object result = this.sendCommand("Page.captureScreenshot", ImmutableMap.of("format", "png", "fromSurface", true));
        this.sendCommand("Emulation.clearDeviceMetricsOverride", ImmutableMap.of());
        return this.decodeBase64EncodedPng((String)((Map)result).get("data"));
    }

    public BufferedImage takeFullPageScreenshotGeckoDriver() {
        String version = (String) ((RemoteWebDriver) this.driver).getCapabilities().getCapability("moz:geckodriverVersion");
        if (version != null && !Version.valueOf(version).satisfies("<0.24.0")) {
            this.defineCustomCommand("mozFullPageScreenshot", new CommandInfo("/session/:sessionId/moz/screenshot/full", HttpMethod.GET));
            Object result = this.executeCustomCommand("mozFullPageScreenshot");
            String base64EncodedPng;
            if (result instanceof String) {
                base64EncodedPng = (String) result;
            } else {
                if (!(result instanceof byte[])) {
                    throw new RuntimeException(String.format("Unexpected result for /moz/screenshot/full command: %s", result == null ? "null" : result.getClass().getName() + " instance"));
                }

                base64EncodedPng = new String((byte[]) result);
            }

            return this.decodeBase64EncodedPng(base64EncodedPng);
        } else {
            return this.takeFullPageScreenshotScroll((Coordinates) null);
        }
    }

    public WebDriver getUnderlyingDriver() {
        return this.driver;
    }

    public int getCurrentScrollX() {
        return (int)(Double.parseDouble(this.executeJsScript("js/get-current-scrollX.js").toString()) * this.devicePixelRatio);
    }

    public int getDocScrollBarWidth() {
        return Math.max((int)(Double.parseDouble(this.executeJsScript("js/doc-scrollbar-width.js").toString()) * this.devicePixelRatio), 40);
    }

    public int getElementScrollBarWidth(WebElement element) {
        return (int)(Double.parseDouble(this.executeJsScript("js/element-scrollbar-width.js", element).toString()) * this.devicePixelRatio);
    }

    public int getElementScrollBarHeight(WebElement element) {
        return (int)(Double.parseDouble(this.executeJsScript("js/element-scrollbar-height.js", element).toString()) * this.devicePixelRatio);
    }

    public int getElementScrollBarWidth(By by) {
        return (int)(Double.parseDouble(this.executeJsScript("js/element-scrollbar-width.js", this.driver.findElement(by)).toString()) * this.devicePixelRatio);
    }

    public int getElementScrollBarHeight(By by) {
        return (int)(Double.parseDouble(this.executeJsScript("js/element-scrollbar-height.js", this.driver.findElement(by)).toString()) * this.devicePixelRatio);
    }

    public int getCurrentScrollY() {
        return (int)(Double.parseDouble(this.executeJsScript("js/get-current-scrollY.js").toString()) * this.devicePixelRatio);
    }

    public int getElementCurrentScrollX(WebElement element) {
        return (int)(Double.parseDouble(this.executeJsScript("js/get-current-element-scrollX.js", element).toString()) * this.devicePixelRatio);
    }

    public int getElementCurrentScrollY(WebElement element) {
        return (int)(Double.parseDouble(this.executeJsScript("js/get-current-element-scrollY.js", element).toString()) * this.devicePixelRatio);
    }

    public int getElementCurrentScrollX(By by) {
        return (int)(Double.parseDouble(this.executeJsScript("js/get-current-element-scrollX.js", this.driver.findElement(by)).toString()) * this.devicePixelRatio);
    }

    public int getElementCurrentScrollY(By by) {
        return (int)(Double.parseDouble(this.executeJsScript("js/get-current-element-scrollY.js", this.driver.findElement(by)).toString()) * this.devicePixelRatio);
    }

    public int getDocWidth() {
        if (this.docWidth == -1) {
            this.docWidth = (int)(Double.parseDouble(this.executeJsScript("js/max-document-width.js").toString()) * this.devicePixelRatio);
        }
        return this.docWidth;
    }

    public int getDocHeight() {
        if (this.docHeight == -1) {
            this.docHeight = (int)(Double.parseDouble(this.executeJsScript("js/max-document-height.js").toString()) * this.devicePixelRatio);
        }
        return this.docHeight;
    }

    public int getViewportWidth() {
        if (this.viewportWidth == -1) {
            this.viewportWidth = (int)(Double.parseDouble(this.executeJsScript("js/viewport-width.js").toString()) * this.devicePixelRatio);
        }
        return this.viewportWidth;
    }

    public int getViewportHeight() {
        if (this.viewportHeight == -1) {
            this.viewportHeight = (int)(Double.parseDouble(this.executeJsScript("js/viewport-height.js").toString()) * this.devicePixelRatio);
        }
        return this.viewportHeight;
    }

    public Coordinates getCoordinates(WebElement element) {
        ArrayList<String> list = (ArrayList)this.executeJsScript("js/relative-element-coords.js", element);
        Point currentLocation = new Point(Integer.parseInt((String)list.get(0)), Integer.parseInt((String)list.get(1)));
        Dimension size = new Dimension(Integer.parseInt((String)list.get(2)), Integer.parseInt((String)list.get(3)));
        Dimension scrollableSize = new Dimension(Integer.parseInt((String)list.get(4)), Integer.parseInt((String)list.get(5)));
        return new Coordinates(element.getLocation(), currentLocation, size, scrollableSize, this.devicePixelRatio);
    }

    public Coordinates getCoordinates(By by) {
        return this.getCoordinates(this.driver.findElement(by));
    }

    public void scrollToElement(WebElement element) {
        this.executeJsScript("js/scroll-element-into-view.js", element);
    }

    public void scrollToElement(By by) {
        this.executeJsScript("js/scroll-element-into-view.js", this.driver.findElement(by));
    }

    public void scrollToElementVerticalCentered(WebElement element) {
        this.executeJsScript("js/scroll-into-view-vertical-centered.js", element);
    }

    public void scrollTo(int x, int y) {
        this.executeJsScript("js/scroll-to.js", (double)x / this.devicePixelRatio, (double)y / this.devicePixelRatio);
    }

    public void scrollBy(int x, int y) {
        this.executeJsScript("js/scroll-by.js", (double)x / this.devicePixelRatio, (double)y / this.devicePixelRatio);
    }

    public void scrollElement(WebElement element, int x, int y) {
        this.executeJsScript("js/scroll-element.js", element, (double)x / this.devicePixelRatio, (double)y / this.devicePixelRatio);
    }

    public void scrollElement(By by, int x, int y) {
        this.executeJsScript("js/scroll-element.js", this.driver.findElement(by), (double)x / this.devicePixelRatio, (double)y / this.devicePixelRatio);
    }

    public Object executeJsScript(String filePath, Object... arg) {
        String script = FileUtil.getJsScript(filePath);
        JavascriptExecutor js = (JavascriptExecutor)this.driver;
        return js.executeScript(script, arg);
    }

    public Object sendCommand(String cmd, Object params) {
        try {
            Method execute = RemoteWebDriver.class.getDeclaredMethod("execute", String.class, Map.class);
            execute.setAccessible(true);
            Response res = (Response)execute.invoke(this.driver, "sendCommand", ImmutableMap.of("cmd", cmd, "params", params));
            return res.getValue();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object evaluate(String script) {
        Object response = this.sendCommand("Runtime.evaluate", ImmutableMap.of("returnByValue", true, "expression", script));
        Object result = ((Map)response).get("result");
        return ((Map)result).get("value");
    }

    public Object executeCustomCommand(String commandName) {
        try {
            Method execute = RemoteWebDriver.class.getDeclaredMethod("execute", String.class);
            execute.setAccessible(true);
            Response res = (Response)execute.invoke(this.driver, commandName);
            return res.getValue();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void defineCustomCommand(String name, CommandInfo info) {
        try {
            Method defineCommand = HttpCommandExecutor.class.getDeclaredMethod("defineCommand", String.class, CommandInfo.class);
            defineCommand.setAccessible(true);
            CommandExecutor commandExecutor = ((RemoteWebDriver)this.driver).getCommandExecutor();

            try {
                Class.forName("org.openqa.selenium.remote.TracedCommandExecutor");
                if (commandExecutor instanceof TracedCommandExecutor) {
                    Field delegateField = TracedCommandExecutor.class.getDeclaredField("delegate");
                    delegateField.setAccessible(true);
                    commandExecutor = (CommandExecutor)delegateField.get(commandExecutor);
                }
            } catch (ClassNotFoundException var6) {
            }

            defineCommand.invoke(commandExecutor, name, info);
        } catch (IllegalAccessException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage decodeBase64EncodedPng(String base64EncodedPng) {
        InputStream in = new ByteArrayInputStream((byte[]) OutputType.BYTES.convertFromBase64Png(base64EncodedPng));

        try {
            BufferedImage bImageFromConvert = ImageIO.read(in);
            return bImageFromConvert;
        } catch (IOException var5) {
            throw new RuntimeException("Error while converting results from bytes to BufferedImage");
        }
    }
}
