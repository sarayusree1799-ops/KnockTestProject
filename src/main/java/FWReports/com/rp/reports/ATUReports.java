package FWReports.com.rp.reports;


import com.google.common.io.Files;
import FWReports.com.rp.reports.exceptions.ATUReporterStepFailedException;
import FWReports.com.rp.reports.logging.LogAs;
import FWReports.com.rp.reports.utils.AuthorDetails;
import FWReports.com.rp.reports.utils.Directory;
import FWReports.com.rp.reports.utils.Platform;
import FWReports.com.rp.reports.utils.Steps;
import FWReports.com.rp.selenium.reports.CaptureScreen;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.testng.Reporter.getCurrentTestResult;

public class ATUReports {
    private static WebDriver driver;
    public static final int MAX_BAR_REPORTS = 10;
    public static final String MESSAGE = "ATU Reporter: Preparing Reports";
    public static String indexPageDescription = "Reports Description";
    public static String currentRunDescription = "Current Run Description";
    public static String screenShotNumber;
    public static long lastExecutionTime;
    public static long currentExecutionTime;
    public static final String STEP_NUM = "STEP";
    public static final String PASSED_BUT_FAILED = "passedButFailed";

    public ATUReports() {
    }

    public static WebDriver getWebDriver() {
        return driver;
    }

    public static void setWebDriver(WebDriver paramWebDriver) {
        driver = paramWebDriver;
        Platform.prepareDetails(driver);
    }

    public static void setAuthorInfo(String paramString1, String paramString2, String paramString3) {
        AuthorDetails localAuthorDetails = new AuthorDetails();
        localAuthorDetails.setAuthorName(paramString1);
        localAuthorDetails.setCreationDate(paramString2);
        localAuthorDetails.setVersion(paramString3);
        getCurrentTestResult().setAttribute("authorInfo", localAuthorDetails);
    }

    public static void setTestCaseReqCoverage(String paramString) {
        getCurrentTestResult().setAttribute("reqCoverage", paramString);
    }

    public static void stepFailureHandler(ITestResult paramITestResult, Steps paramSteps, LogAs paramLogAs) {
        if (paramLogAs == LogAs.FAILED) {
            buildReportData(paramSteps);
            if (Directory.continueExecutionAfterStepFailed) {
                paramITestResult.setAttribute("passedButFailed", "passedButFailed");
            } else {
                Throwable cause = paramITestResult.getThrowable();
                throw new ATUReporterStepFailedException("Step failed with log as FAILED", cause);
            }
        } else {
            buildReportData(paramSteps);
        }
    }

    public static void add(String paramString, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null) {
            if (paramCaptureScreen.isCaptureBrowserPage()) {
                takeBrowserPageScreenShot();
            } else if (paramCaptureScreen.isCaptureDesktop()) {
                takeDesktopScreenShot();
            } else if (paramCaptureScreen.isCaptureWebElement()) {
                takeWebElementScreenShot(paramCaptureScreen.getElement());
            }
        }

        Steps localSteps = new Steps();
        localSteps.setDescription(paramString);
        localSteps.setInputValue("");
        localSteps.setExpectedValue("");
        localSteps.setActualValue("");
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void add(String paramString1, String paramString2, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null) {
            if (paramCaptureScreen.isCaptureBrowserPage()) {
                takeBrowserPageScreenShot();
            } else if (paramCaptureScreen.isCaptureDesktop()) {
                takeDesktopScreenShot();
            } else if (paramCaptureScreen.isCaptureWebElement()) {
                takeWebElementScreenShot(paramCaptureScreen.getElement());
            }
        }

        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue("");
        localSteps.setActualValue("");
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void add(String paramString1, String paramString2, String paramString3, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null) {
            if (paramCaptureScreen.isCaptureBrowserPage()) {
                takeBrowserPageScreenShot();
            } else if (paramCaptureScreen.isCaptureDesktop()) {
                takeDesktopScreenShot();
            } else if (paramCaptureScreen.isCaptureWebElement()) {
                takeWebElementScreenShot(paramCaptureScreen.getElement());
            }
        }

        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue("");
        localSteps.setExpectedValue(paramString2);
        localSteps.setActualValue(paramString3);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        stepFailureHandler(getCurrentTestResult(), localSteps, paramLogAs);
    }

    public static void add(String paramString1, String paramString2, String paramString3, String paramString4, LogAs paramLogAs, CaptureScreen paramCaptureScreen) {
        if (paramCaptureScreen != null) {
            if (paramCaptureScreen.isCaptureBrowserPage()) {
                takeBrowserPageScreenShot();
            } else if (paramCaptureScreen.isCaptureDesktop()) {
                takeDesktopScreenShot();
            } else if (paramCaptureScreen.isCaptureWebElement()) {
                takeWebElementScreenShot(paramCaptureScreen.getElement());
            } else if (paramCaptureScreen.isCaptureMobilePage()) {
                takeMobilePageScreenShot();
            }
        }

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
        getCurrentTestResult().setAttribute("STEP" + i, paramSteps);
        Reporter.log("STEP" + i);
    }

    private static String getExecutionTime() {
        currentExecutionTime = System.currentTimeMillis();
        long l = currentExecutionTime - lastExecutionTime;
        if (l > 1000L) {
            l /= 1000L;
            lastExecutionTime = currentExecutionTime;
            return l + " Sec";
        } else {
            lastExecutionTime = currentExecutionTime;
            return l + " Milli Sec";
        }
    }

    private static String getLineNumDesc() {
        String str = "" + Thread.currentThread().getStackTrace()[3].getLineNumber();
        return str;
    }

    private static void takeDesktopScreenShot() {
        if (Directory.takeScreenshot) {
            ITestResult localITestResult = getCurrentTestResult();
            String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
            screenShotNumber = Reporter.getOutput(getCurrentTestResult()).size() + 1 + "";
            File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");
            try {
                Rectangle localRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage localBufferedImage = (new Robot()).createScreenCapture(localRectangle);
                ImageIO.write(localBufferedImage, "PNG", localFile);
            } catch (Exception var5) {
                screenShotNumber = null;
            }
        }
    }
    private static void takeBrowserPageScreenShot() {
        if (Directory.takeScreenshot) {
            if (getWebDriver() == null) {
                screenShotNumber = null;
            } else {
                ITestResult localITestResult = getCurrentTestResult();
                String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
                screenShotNumber = Reporter.getOutput(getCurrentTestResult()).size() + 1 + "";
                File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");

                try {
                    WebDriver localWebDriver;
                    if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
                        localWebDriver = (new Augmenter()).augment(getWebDriver());
                    } else {
                        localWebDriver = getWebDriver();
                    }

                    if (localWebDriver instanceof TakesScreenshot) {
                        byte[] arrayOfByte = (byte[])((TakesScreenshot)localWebDriver).getScreenshotAs(OutputType.BYTES);
                        Files.write(arrayOfByte, localFile);
                    } else {
                        screenShotNumber = null;
                    }
                } catch (Exception var5) {
                    screenShotNumber = null;
                }
            }
        }
    }

    private static void takeMobilePageScreenShot() {
        if (Directory.takeScreenshot) {
            if (getWebDriver() == null) {
                screenShotNumber = null;
            } else {
                ITestResult localTestResult = getCurrentTestResult();
                String str = localTestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
                screenShotNumber = Reporter.getOutput(getCurrentTestResult()).size() + 1 + "";
                File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");

                try {
                    WebDriver localWebDriver;
                    if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
                        localWebDriver = (new Augmenter()).augment(getWebDriver());
                    } else {
                        localWebDriver = getWebDriver();
                    }

                    if (localWebDriver instanceof TakesScreenshot) {
                        byte[] arrayOfByte = (byte[])((TakesScreenshot)localWebDriver).getScreenshotAs(OutputType.BYTES);
                        Files.write(arrayOfByte, localFile);
                    } else {
                        screenShotNumber = null;
                    }
                } catch (Exception var5) {
                    screenShotNumber = null;
                }
            }
        }
    }

    private static void takeWebElementScreenShot(WebElement paramWebElement) {
        if (Directory.takeScreenshot) {
            if (getWebDriver() == null) {
                screenShotNumber = null;
            } else {
                ITestResult localTestResult = getCurrentTestResult();
                String str = localTestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
                screenShotNumber = Reporter.getOutput(getCurrentTestResult()).size() + 1 + "";
                File localFile1 = new File(str + Directory.SEP + screenShotNumber + ".PNG");

                try {
                    WebDriver localWebDriver;
                    if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
                        localWebDriver = (new Augmenter()).augment(getWebDriver());
                    } else {
                        localWebDriver = getWebDriver();
                    }

                    if (localWebDriver instanceof TakesScreenshot) {
                        File localFile2 = (File)((TakesScreenshot)localWebDriver).getScreenshotAs(OutputType.FILE);
                        BufferedImage localBufferedImage1 = ImageIO.read(localFile2);
                        org.openqa.selenium.Point localPoint = paramWebElement.getLocation();
                        int i = paramWebElement.getSize().getWidth();
                        int j = paramWebElement.getSize().getHeight();
                        BufferedImage localBufferedImage2 = localBufferedImage1.getSubimage(localPoint.getX(), localPoint.getY(), i, j);
                        ImageIO.write(localBufferedImage2, "PNG", localFile1);
                    } else {
                        screenShotNumber = null;
                    }
                } catch (Exception var11) {
                    screenShotNumber = null;
                }
            }
        }
    }

    @Deprecated
    public static void add(String paramString, boolean paramBoolean) {
        if (paramBoolean) {
            takeScreenShot();
        }
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
    public static void add(String paramString1, String paramString2, boolean paramBoolean) {
        if (paramBoolean) {
            takeScreenShot();
        }
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
    public static void add(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
        if (paramBoolean) {
            takeScreenShot();
        }
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

    public static void add(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) {
        if (paramBoolean) {
            takeScreenShot();
        }
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

    @Deprecated
    public static void add(String paramString1, String paramString2, String paramString3, String paramString4, LogAs paramLogAs, boolean paramBoolean) {
        if (paramBoolean) {
            takeScreenShot();
        }
        Steps localSteps = new Steps();
        localSteps.setDescription(paramString1);
        localSteps.setInputValue(paramString2);
        localSteps.setExpectedValue(paramString3);
        localSteps.setActualValue(paramString4);
        localSteps.setTime(getExecutionTime());
        localSteps.setLineNum(getLineNumDesc());
        localSteps.setScreenShot(screenShotNumber);
        localSteps.setLogAs(paramLogAs);
        localSteps.setLogAs(paramLogAs);
        buildReportData(localSteps);
    }


    private static void takeScreenShot() {
        if (Directory.takeScreenshot) {
            ITestResult localITestResult = getCurrentTestResult();
            String str = localITestResult.getAttribute("reportDir").toString() + Directory.SEP + Directory.IMGDIRName;
            screenShotNumber = Reporter.getOutput(getCurrentTestResult()).size() + 1 + "";
            File localFile = new File(str + Directory.SEP + screenShotNumber + ".PNG");

            try {
                WebDriver localWebDriver;
                if (getWebDriver().getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
                    localWebDriver = (new Augmenter()).augment(getWebDriver());
                } else {
                    localWebDriver = getWebDriver();
                }

                if (localWebDriver instanceof TakesScreenshot) {
                    byte[] arrayOfByte = (byte[])((TakesScreenshot)localWebDriver).getScreenshotAs(OutputType.BYTES);
                    Files.write(arrayOfByte, localFile);
                } else {
                    screenShotNumber = null;
                }
            } catch (Exception var5) {
                screenShotNumber = null;
            }
        }
    }

    static {
        try {
            lastExecutionTime = getCurrentTestResult().getStartMillis();
        } catch (Exception var1) {
        }
    }
}