package FrameWorkPackage.com.rp.automation.framework.util;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ComparisonResult;
import com.github.romankh3.image.comparison.model.ComparisonState;
import com.google.common.io.Files;
import com.realpage.com.Reports.com.rp.reports.exceptions.ATUReporterStepFailedException;
import com.realpage.com.Reports.com.rp.reports.logging.LogAs;
import com.realpage.com.Reports.com.rp.reports.utils.Attributes;
import com.realpage.com.Reports.com.rp.reports.utils.AuthorDetails;
import com.realpage.com.Reports.com.rp.reports.utils.Directory;
import com.realpage.com.Reports.com.rp.reports.utils.Steps;
import com.realpage.com.Reports.com.rp.reports.utils.Platform;
import com.realpage.com.Reports.com.rp.selenium.reports.CaptureScreen;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageComparisonsUtil {
    private static WebDriver driver;
    public static final int MAX_BAR_REPORTS = 10;
    public static final String MESSAGE = "ATU Reporter: Preparing Reports";
    public static String indexPageDescription = "Reports Description";
    public static String currentRunDescription = "Here you can give description about the current Run";
    private static String screenShotNumber;
    private static long lastExecutionTime = 0L;
    private static long currentExecutionTime;
    public static final String EMPTY = "";
    public static final String STEP_NUM = "STEP";
    public static final String PASSED_BUT_FAILED = "passedButFailed";

    public static void setWebDriver(WebDriver paramWebDriver) {
        driver = paramWebDriver;
        Platform.prepareDetails(driver);
    }

    public static WebDriver getWebDriver() {
        return driver;
    }

    public static void setAuthorInfo(String paramString1, String paramString2,
                                     String paramString3) {
        AuthorDetails localAuthorDetails = new AuthorDetails();
        localAuthorDetails.setAuthorName(paramString1);
        localAuthorDetails.setCreationDate(paramString2);
        localAuthorDetails.setVersion(paramString3);
        Reporter.getCurrentTestResult().setAttribute("authorInfo",
                localAuthorDetails);
    }

    public static void setAuthorInfoAtClassLevel(String paramString1,
                                                 String paramString2, String paramString3) {
        String str = java.lang.Thread.currentThread().getStackTrace()[2]
                .getClassName();
        AuthorDetails localAuthorDetails = new AuthorDetails();
        localAuthorDetails.setAuthorName(paramString1);
        localAuthorDetails.setCreationDate(paramString2);
        localAuthorDetails.setVersion(paramString3);
        Attributes.setClassLevelAuthors(str, localAuthorDetails);
    }

    public static void setTestCaseReqCoverage(String paramString) {
        Reporter.getCurrentTestResult()
                .setAttribute("reqCoverage", paramString);
    }

    public static void stepFailureHandler(ITestResult paramITestResult, Steps paramStep, LogAs paramLogAs) {
        if (paramLogAs == LogAs.FAILED) {
            buildReportData(paramStep);
            if (Directory.continueExecutionAfterStepFailed)
                paramITestResult.setAttribute("passedButFailed", "passedButFailed");
            else
                throw new ATUReporterStepFailedException();
            return;
        }
        buildReportData(paramStep);
    }

    public static void add(String paramString, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null)
            if (paramCaptureScreen.isCaptureBrowserPage())
                takeBrowserPageScreenShot();
            else if (paramCaptureScreen.isCaptureDesktop())
                takeDesktopScreenshot();
            else if (paramCaptureScreen.isCaptureWebElement())
                takeWebElementScreenshot(paramCaptureScreen.getElement());
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString);
        localSteps.setInputValue("");
        localSteps.setExpectedValue("");
        localSteps.setActualValue("");
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void add(String paramString1, String paramString2, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null)
            if (paramCaptureScreen.isCaptureBrowserPage())
                takeBrowserPageScreenShot();
            else if (paramCaptureScreen.isCaptureDesktop())
                takeDesktopScreenshot();
            else if (paramCaptureScreen.isCaptureWebElement())
                takeWebElementScreenshot(paramCaptureScreen.getElement());
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue("");
        localSteps.setActualValue("");
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void add(String paramString1, String paramString2, String paramString3, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null)
            if (paramCaptureScreen.isCaptureBrowserPage())
                takeBrowserPageScreenShot();
            else if (paramCaptureScreen.isCaptureDesktop())
                takeDesktopScreenshot();
            else if (paramCaptureScreen.isCaptureWebElement())
                takeWebElementScreenshot(paramCaptureScreen.getElement());
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue("");
        localSteps.setExpectedValue(paramString2);
        localSteps.setActualValue(paramString3);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void add(String paramString1, String paramString2, String paramString3, String paramString4, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null)
            if (paramCaptureScreen.isCaptureBrowserPage())
                takeBrowserPageScreenShot();
            else if (paramCaptureScreen.isCaptureDesktop())
                takeDesktopScreenshot();
            else if (paramCaptureScreen.isCaptureWebElement())
                takeWebElementScreenshot(paramCaptureScreen.getElement());
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue(paramString3);
        localSteps.setActualValue(paramString4);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void diffImage(String paramString1, String paramString2,
                                 String paramString3, String paramString4, LogAs paramLogAs,
                                 CaptureScreen paramCaptureScreen, BufferedImage expectedImageResults, BufferedImage ActualImageResults) throws IOException {
        if (paramCaptureScreen != null)
            if (paramCaptureScreen.isCaptureBrowserPage())
                takeBrowserPageScreenShot();
            else if (paramCaptureScreen.isCaptureDesktop())
                takeDesktopScreenshot(expectedImageResults, ActualImageResults);
            else if (paramCaptureScreen.isCaptureWebElement())
                takeWebElementScreenshot(paramCaptureScreen.getElement());
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue(paramString3);
        localSteps.setActualValue(paramString4);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);

        ImageComparison imageComparison = new ImageComparison(
                ImageComparisonUtil.toBufferedImage(expectedImageResults),
                ImageComparisonUtil.toBufferedImage(ActualImageResults)
        );
        // Threshold - it's the max distance between non-equal pixels. By default it's 5.
        imageComparison.setThreshold(7);
        imageComparison.getThreshold();
        // RectangleListWidth - Width of the line that is drawn in the rectangle. By default it's 1.
        imageComparison.setRectangleLineWidth(2);
        imageComparison.getRectangleLineWidth();

        // Destination. Before comparing also can be added destination file for result image.
        // imageComparison.setDestination(resultDestination);
        // imageComparison.getDestination();
        // MaximalRectangleCount - It means that would get first x biggest rectangles for drawing.
// by default all the rectangles would be drawn.
// imageComparison.setMaximalRectangleCount(10);
// imageComparison.getMaximalRectangleCount();

// MinimalRectangleSize - The number of the minimal rectangle size. Count as (width x height).
// by default it's 1.
// imageComparison.setMinimalRectangleSize(100);
// imageComparison.getMinimalRectangleSize();

// Change the level of the pixel tolerance:
        imageComparison.setPixelToleranceLevel(0.2);
        imageComparison.getPixelToleranceLevel();
        ComparisonResult comparisonResult = imageComparison.compareImages();

// Can be found ComparisonState.
        ComparisonState comparisonState = comparisonResult.getComparisonState();
        if (comparisonState.toString() != "MATCH") {
            paramLogAs = LogAs.FAILED;
        }
        System.out.println("comparisonState " + comparisonState.name());
        System.out.println("comparisonState " + comparisonState);

        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void addImage(String paramString1, String paramString2, String paramString3, String paramString4, LogAs paramLogAs,
                                CaptureScreen paramCaptureScreen, BufferedImage imageResults) throws IOException {
        if (paramCaptureScreen != null)
            if (paramCaptureScreen.isCaptureBrowserPage())
                takeBrowserPageScreenShot();
            else if (paramCaptureScreen.isCaptureDesktop())
                takeDesktopScreenshot(imageResults);
            else if (paramCaptureScreen.isCaptureWebElement())
                takeWebElementScreenshot(paramCaptureScreen.getElement());
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue(paramString3);
        localSteps.setActualValue(paramString4);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void addFullImage(String paramString1, String paramString2, String paramString3, String paramString4, LogAs paramLogAs,
                                    CaptureScreen paramCaptureScreen, BufferedImage imageResults) throws IOException {
        if (paramCaptureScreen != null)
            if (paramCaptureScreen.isCaptureBrowserPage())
                takeBrowserPageScreenShot();
            else if (paramCaptureScreen.isCaptureDesktop())
                takeDesktopScreenshot(imageResults);
            else if (paramCaptureScreen.isCaptureWebElement())
                takeWebElementScreenshot(paramCaptureScreen.getElement());
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue(paramString3);
        localSteps.setActualValue(paramString4);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(Reporter.getCurrentTestResult(), localSteps, paramLogAs);
    }

    private static void buildReportData(Steps paramSteps) {
        screenShotNumber = null;
        int i = Reporter.getOutput().size() + 1;
        Reporter.getCurrentTestResult().setAttribute("STEP" + i, paramSteps);
        Reporter.log("STEP" + i);
    }

    private static String getExecutionTime() {
        currentExecutionTime = System.currentTimeMillis();
        long l = currentExecutionTime - lastExecutionTime;
        if (l > 1000L) {
            l /= 1000L;
            lastExecutionTime = currentExecutionTime;
            return l + " Sec";
        }
        lastExecutionTime = currentExecutionTime;
        return l + " Milli Sec";
    }

    private static String getLineNumDesc() {
        String str = ""
                + java.lang.Thread.currentThread().getStackTrace()[3]
                .getLineNumber();
        return str;
    }

    private static void takeDesktopScreenshot(BufferedImage expectedImage, BufferedImage actualImage) throws IOException {
        if (!((Directory.takeScreenshot)))
            return;
        ITestResult localITestResult = Reporter.getCurrentTestResult();
        String str = localITestResult.getAttribute("reportDir").toString()
                + Directory.SEP + Directory.IMGDIRName;
        screenShotNumber = (Reporter.getOutput(Reporter.getCurrentTestResult())
                .size() + 1) + "";
        File localFile = new File(str + Directory.SEP + screenShotNumber
                + ".PNG");
        ImageComparison imageComparison = new ImageComparison(
                ImageComparisonUtil.toBufferedImage(expectedImage),
                ImageComparisonUtil.toBufferedImage(actualImage),
                localFile
        );

        // Threshold - it's the max distance between non-equal pixels. By default it's 5.
        imageComparison.setThreshold(7);
        imageComparison.getThreshold();

        // RectangleListWidth - Width of the line that is drawn in the rectangle. By default it's 1.
        imageComparison.setRectangleLineWidth(2);
        imageComparison.getRectangleLineWidth();
        imageComparison.setPixelToleranceLevel(0.2);
        imageComparison.getPixelToleranceLevel();
        ComparisonResult comparisonResult = imageComparison.compareImages();
        ComparisonState comparisonState = comparisonResult.getComparisonState();

        System.out.println("comparisonState " + comparisonState.name());
        System.out.println("comparisonState " + comparisonState);
        BufferedImage resultImage = comparisonResult.getResult();

        System.out.println("resultImage " + resultImage);

// Image can be saved after comparison, using ImageComparisonUtil.
        ImageComparisonUtil.saveImage(localFile, resultImage);
        try {
            ImageIO.write(resultImage, "PNG", localFile);
        } catch (Exception localException) {
            screenShotNumber = null;
        }
    }

    private static void takeDesktopScreenshot(BufferedImage pageImage) throws IOException {
        if (!((Directory.takeScreenshot)))
            return;
        ITestResult localITestResult = Reporter.getCurrentTestResult();
        String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
        screenShotNumber = (Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1) + "";
        File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
        try {
            ImageIO.write(ImageComparisonUtil.toBufferedImage(pageImage), "PNG", localFile);
        } catch (Exception localException) {
            screenShotNumber = null;
        }
    }

    private static void takeDesktopScreenshot() {
        if (!((Directory.takeScreenshot)))
            return;
        ITestResult localITestResult = Reporter.getCurrentTestResult();
        String str = localITestResult.getAttribute("reportDir").toString()
                + Directory.SEP + Directory.IMGDIRName;
        screenShotNumber = (Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1) + "";
        File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
        try {
            Rectangle localRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage localBufferedImage = (new Robot()).createScreenCapture(localRectangle);
            ImageIO.write(localBufferedImage, "PNG", localFile);
        } catch (Exception localException) {
            screenShotNumber = null;
        }
    }

    private static void takeBrowserPageScreenShot() {
        if (!(Directory.takeScreenshot))
            return;
        if (getWebDriver() == null) {
            screenShotNumber = null;
            return;
        }
        ITestResult localITestResult = Reporter.getCurrentTestResult();
        String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
        screenShotNumber = (Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1) + "";
        File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
        try {
            WebDriver localWebDriver;
            if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver"))
                localWebDriver = new Augmenter().augment(getWebDriver());
            else
                localWebDriver = getWebDriver();
            if (localWebDriver instanceof TakesScreenshot) {
                byte[] arrayOfByte = (byte[]) ((TakesScreenshot) localWebDriver)
                        .getScreenshotAs(OutputType.BYTES);
                Files.write(arrayOfByte, localFile);
            } else {
                screenShotNumber = null;
            }
        } catch (Exception localException) {
            screenShotNumber = null;
        }
    }

    private static void takeWebElementScreenshot(WebElement paramWebElement) {
        if (!(Directory.takeScreenshot))
            return;
        if (getWebDriver() == null) {
            screenShotNumber = null;
            return;
        }
        ITestResult localITestResult = Reporter.getCurrentTestResult();
        String str = localITestResult.getAttribute("reportDir").toString()
                + Directory.IMGDIRName;
        screenShotNumber = (Reporter.getOutput(Reporter.getCurrentTestResult())
                .size() + 1) + "";
        File localFile1 = new File(str + Directory.SEP + screenShotNumber
                + ".PNG");
        try {
            WebDriver localWebDriver;
            if (getWebDriver().getClass().getName()
                    .equals("org.openqa.selenium.remote.RemoteWebDriver"))
                localWebDriver = new Augmenter().augment(getWebDriver());
            else
                localWebDriver = getWebDriver();
            if (localWebDriver instanceof TakesScreenshot) {
                File localFile2 = (File) ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.FILE);
                BufferedImage localBufferedImage1 = ImageIO.read(localFile2);
                Point localPoint = paramWebElement.getLocation();
                int i = paramWebElement.getSize().getWidth();
                int j = paramWebElement.getSize().getHeight();
                BufferedImage localBufferedImage2 = localBufferedImage1
                        .getSubimage(localPoint.getX(), localPoint.getY(), i, j);
                ImageIO.write(localBufferedImage2, "PNG", localFile1);
            } else {
                screenShotNumber = null;
            }
        } catch (Exception localException) {
            screenShotNumber = null;
        }
    }

    @Deprecated
    public static void add(String paramString, boolean paramBoolean) {
        if (paramBoolean)
            takeScreenShot();
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString);
        localSteps.setInputValue("");
        localSteps.setExpectedValue("");
        localSteps.setActualValue("");
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(LogAs.PASSED);
        buildReportData(localSteps);
    }

    @Deprecated
    public static void add(String paramString1, String paramString2,
                           boolean paramBoolean) {
        if (paramBoolean)
            takeScreenShot();
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue("");
        localSteps.setActualValue("");
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(LogAs.PASSED);
        buildReportData(localSteps);
    }

    @Deprecated
    public static void add(String paramString1, String paramString2,
                           String paramString3, boolean paramBoolean) {
        if (paramBoolean)
            takeScreenShot();
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue("");
        localSteps.setExpectedValue(paramString2);
        localSteps.setActualValue(paramString3);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(LogAs.PASSED);
        buildReportData(localSteps);
    }

    @Deprecated
    public static void add(String paramString1, String paramString2,
                           String paramString3, String paramString4, boolean paramBoolean) {
        if (paramBoolean)
            takeScreenShot();
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue(paramString3);
        localSteps.setActualValue(paramString4);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(LogAs.PASSED);
        buildReportData(localSteps);
    }

    private static void takeScreenShot() {
        if (!(Directory.takeScreenshot)) {
            return;
        }
        if (getWebDriver() == null) {
            screenShotNumber = null;
            return;
        }
        ITestResult localITestResult = Reporter.getCurrentTestResult();
        String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
        screenShotNumber = (Reporter.getOutput(Reporter.getCurrentTestResult()).size() + 1) + "";
        File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
        try {
            WebDriver localWebDriver;
            if ((getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver"))
                    || (getWebDriver() instanceof RemoteWebDriver)) {
                localWebDriver = new Augmenter().augment(getWebDriver());
            } else {
                localWebDriver = getWebDriver();
            }
            if (localWebDriver instanceof TakesScreenshot) {
                byte[] arrayOfByte = (byte[]) ((TakesScreenshot) localWebDriver).getScreenshotAs(OutputType.BYTES);
                Files.write(arrayOfByte, localFile);
            } else {
                screenShotNumber = null;
            }
        } catch (Exception localException) {
            screenShotNumber = null;
        }
    }

    static {
        try {
            lastExecutionTime = Reporter.getCurrentTestResult().getStartMillis();
        } catch (Exception localException) {
            lastExecutionTime = ((Long) Attributes.getAttribute("startExecution")).longValue();
        }
    }
}