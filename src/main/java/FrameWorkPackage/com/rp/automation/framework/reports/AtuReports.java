package FrameWorkPackage.com.rp.automation.framework.reports;

import FrameWorkPackage.com.rp.automation.framework.annotations.AzureSuiteIDTestCaseId;
import FrameWorkPackage.com.rp.automation.framework.annotations.AzureTestCaseId;
import FrameWorkPackage.com.rp.automation.framework.util.AzureUtils;
import FrameWorkPackage.com.rp.automation.framework.util.ImageComparisonsUtil;
import FrameWorkPackage.com.rp.automation.framework.webdriver.DriverManager;
import FWReports.com.rp.reports.ATUReports;
import FWReports.com.rp.reports.logging.LogAs;
import FWReports.com.rp.reports.utils.Utils;
import FWReports.com.rp.selenium.reports.CaptureScreen;
import FWReports.com.rp.selenium.reports.CaptureScreen.ScreenshotOf;
import org.testng.ITestResult;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class AtuReports {

    static AzureUtils azureUtils = new AzureUtils();
    public static String testCaseId;
    public static String projectName;

    public AtuReports() {
        System.setProperty("atu.reporter.config", System.getProperty("user.dir") + "\\src\\test\\resources\\atu.properties");
        setIndexPageDescription();
    }

    public synchronized static void setAuthorInfoForReports(String author, String version) {
        ATUReports.setAuthorInfo(author, Utils.getCurrentTime(), version);
    }

    public synchronized static void setTestCaseId(String jiraId) {
        String[] jiraIds = jiraId.split(",");
        String finalJiraId = "";
        if (jiraIds.length > 1) {
            for (String jId : jiraIds) {
                String temp = "<i><mark><a href=http://jira.realpage.com/browse/"
                        + jId + ">TestCase ID:- " + jId + "</a><mark></i><br>";
                finalJiraId = finalJiraId.concat(temp);
            }
            ATUReports.setTestCaseReqCoverage(finalJiraId);
        } else {
            ATUReports
                    .setTestCaseReqCoverage("<i><mark><a href=http://jira.realpage.com/browse/"
                            + jiraId
                            + ">TestCase ID:- "
                            + jiraId
                            + "</a><mark></i>");
        }
    }

    public synchronized static void setAzureTestCaseId(String productName, String azureId) {
        testCaseId = azureId;
        projectName = productName;

        String[] azureIds = azureId.split(";");
        String finalAzureId = "";
        if (azureIds.length > 1) {
            for (String jId : azureIds) {
                String temp = "<i><mark><a href=https://tfs.realpage.com/tfs/Realpage/" + productName + "/_workitems/edit/"
                        + jId + ">TestCase ID: " + jId + "</a></mark></i><br>";
                finalAzureId = finalAzureId.concat(temp);
            }
            ATUReports.setTestCaseReqCoverage(finalAzureId);
        } else {
            ATUReports
                    .setTestCaseReqCoverage("<i><mark><a href=https://tfs.realpage.com/tfs/Realpage/" + productName + "/_workitems/edit/"
                            + azureId
                            + ">TestCase ID: "
                            + azureId
                            + "</a></mark></i>");
        }
    }

    public synchronized static void setAzureTestCaseId(ITestResult results) {
        String productName = null;
        String[] azureIds = null;

        if (results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class) != null) {
            productName = results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class).productName();
            azureIds = results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class).testCaseIds();
        }

        if (results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureTestCaseId.class) != null) {
            productName = results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureTestCaseId.class).productName();
            azureIds = results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureTestCaseId.class).testCaseIds();
        }

        if (productName != null && azureIds != null) {
            System.out.println("productName :" + productName);
            System.out.println("azureIds :" + azureIds[0]);
            String finalAzureId = "";
            if (azureIds.length > 1) {
                for (String jId : azureIds) {
                    String temp = "<i><mark><a href=https://tfs.realpage.com/tfs/Realpage/" + productName + "/_workitems/edit/"
                            + jId + ">TestCase ID: " + jId + "</a></mark></i><br>";
                    finalAzureId = finalAzureId.concat(temp);
                }
                ATUReports.setTestCaseReqCoverage(finalAzureId);
            } else {
                ATUReports
                        .setTestCaseReqCoverage("<mark><a href=https://tfs.realpage.com/tfs/Realpage/" + productName + "/_workitems/edit/"
                                + azureIds[0] + ">TestCase ID:- " + azureIds[0] + "</a></mark>");
            }
        } else {
            System.out.println("productName :" + productName);
            System.out.println("azureIds :" + azureIds);
        }
    }

    public static void setIndexPageDescription() {
        ATUReports.indexPageDescription = "Selenium Project Description <br/> <b> Selenium application of HTML tags </b>";
    }

    public synchronized static void passResults(String Desc, String InputValue, String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, InputValue, ExpectedValue, ActualValue, LogAs.PASSED, new CaptureScreen(CaptureScreen.ScreenshotOf.BROWSER_PAGE));
    }

    public synchronized static void failResults(String Desc, String InputValue, String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, InputValue, ExpectedValue, ActualValue, LogAs.FAILED, new CaptureScreen(CaptureScreen.ScreenshotOf.BROWSER_PAGE));
    }

    public synchronized static void passResults1(String Desc, String InputValue, String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, InputValue, ExpectedValue, ActualValue, LogAs.PASSED, false);
    }

    public synchronized static void failResults1(String Desc, String InputValue, String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, InputValue, ExpectedValue, ActualValue, LogAs.FAILED, true);
    }

    public synchronized static void info(String Desc, String InputValue,
                                         String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, InputValue, ExpectedValue, ActualValue,
                LogAs.INFO, new CaptureScreen(CaptureScreen.ScreenshotOf.BROWSER_PAGE));
        // ATUReports.add(Desc,InputValue,ExpectedValue,ActualValue, false)
    }

    public synchronized static void warning(String Desc, String InputValue,
                                            String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, InputValue, ExpectedValue, ActualValue,
                LogAs.WARNING, new CaptureScreen(CaptureScreen.ScreenshotOf.BROWSER_PAGE));
        // ATUReports.add(Desc,InputValue,ExpectedValue,ActualValue, false)
    }

    public synchronized static void notice(String Desc, String InputValue,
                                           String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, InputValue, ExpectedValue, ActualValue, LogAs.WARNING, null);
    }

    public synchronized static void failResults2(String Desc, String InputValue,
                                                 String ExpectedValue, String ActualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ATUReports.add(Desc, ExpectedValue, ActualValue, false);
        // ATUReports.add(Desc,InputValue,ExpectedValue,ActualValue, false)
    }

    public synchronized static void compareImageResults(String Desc, String InputValue, String ExpectedValue, String ActualValue, BufferedImage resultImage, BufferedImage resultDestination) throws IOException {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ImageComparisonsUtil.diffImage(Desc, InputValue, ExpectedValue, ActualValue, LogAs.PASSED, new CaptureScreen(
                ScreenshotOf.DESKTOP), resultImage, resultDestination);
    }

    public synchronized static void captureImageScreen(String Desc, String InputValue, String ExpectedValue, String ActualValue, BufferedImage CaptureImage) throws IOException {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ImageComparisonsUtil.addImage(Desc, InputValue, ExpectedValue, ActualValue,
                LogAs.PASSED, new CaptureScreen(CaptureScreen.ScreenshotOf.DESKTOP), CaptureImage);
    }

    public synchronized static void passResultsWithAzureStatus(String desc, String inputValue,
                                                               String expectedValue, String actualValue) {
        ATUReports.setWebDriver(DriverManager.getDriver());
        // ATUReports.add(desc,inputValue,expectedValue,actualValue,
        // LogAs.PASSED, new CaptureScreen(ScreenshotOf.DESKTOP));
        ATUReports.add(desc, inputValue, expectedValue, actualValue, false);
        ATUReports.add(desc, inputValue, expectedValue, actualValue,
                LogAs.PASSED, new CaptureScreen(CaptureScreen.ScreenshotOf.BROWSER_PAGE));
        try {
            azureUtils.updateTestCaseExecution(projectName,testCaseId,"passed");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public synchronized static void failResultWithAzureStatus(String desc, String inputValue, String expectedValue, String actualValue) {
        try {
            azureUtils.updateTestCaseExecution(projectName, testCaseId, "failed");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ATUReports.setWebDriver(DriverManager.getDriver());
        //ATUReports.getWebDriver().manage().window().maximize();
        ATUReports.add(desc, inputValue, expectedValue, actualValue,
                LogAs.FAILED, new CaptureScreen(CaptureScreen.ScreenshotOf.BROWSER_PAGE));
        // ATUReports.add(desc, inputValue, expectedValue, actualValue, false)
    }

    public synchronized static void captureFullImageScreen(String Desc, String InputValue,
                                                           String ExpectedValue, String ActualValue, BufferedImage pageImage) throws IOException {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ImageComparisonsUtil.addFullImage(Desc, InputValue, ExpectedValue, ActualValue,
                LogAs.PASSED, new CaptureScreen(CaptureScreen.ScreenshotOf.DESKTOP), pageImage);
    }

    public synchronized static void captureErrorImageScreen(String Desc, String InputValue,
                                                            String ExpectedValue, String ActualValue, BufferedImage CaptureImage) throws IOException {
        ATUReports.setWebDriver(DriverManager.getDriver());
        ImageComparisonsUtil.addImage(Desc, InputValue, ExpectedValue, ActualValue,
                LogAs.WARNING, new CaptureScreen(CaptureScreen.ScreenshotOf.DESKTOP), CaptureImage);
    }
}