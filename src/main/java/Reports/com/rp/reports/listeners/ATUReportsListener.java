package Reports.com.rp.reports.listeners;

import Reports.com.rp.reports.ATUReports;
import Reports.com.rp.reports.excel.ExcelReports;
import Reports.com.rp.reports.exceptions.ATUReporterException;
import Reports.com.rp.reports.exceptions.ATUReporterStepFailedException;
import Reports.com.rp.reports.utils.SettingsFile;
import Reports.com.rp.reports.utils.Platform;
import Reports.com.rp.reports.writers.ConsolidatedReportsPageWriter;
import Reports.com.rp.reports.writers.CurrentRunPageWriter;
import Reports.com.rp.reports.writers.IndexPageWriter;
import Reports.com.rp.reports.writers.TestCaseReportsPageWriter;
import Reports.com.rp.reports.writers.HTMLDesignFilesJSWriter;
import Reports.com.rp.reports.utils.Directory;
import org.testng.*;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import Reports.com.rp.reports.utils.Attributes;

public class ATUReportsListener implements ITestListener, IExecutionListener, IReporter, ISuiteListener {
    int runCount = 1;
    List<ITestResult> passedTests = new ArrayList<>();
    List<ITestResult> failedTests = new ArrayList<>();
    List<ITestResult> skippedTests = new ArrayList<>();

    public ATUReportsListener() {
    }

    public void onStart(ITestContext paramITestContext) {
    }

    public void onFinish(ITestContext paramITestContext) {
    }

    public void onTesFailedButWithinSuccessPercentage(ITestContext paramITestContext) {
    }

    public void onTestFailure(ITestResult paramITestResult) {
        this.failedTests.add(paramITestResult);
    }

    public void onTestSkipped(ITestResult paramITestResult) {
        if (paramITestResult.getThrowable() instanceof Exception) {
            this.skippedTests.add(paramITestResult);
        } else {
            createReportDir(paramITestResult);
            this.skippedTests.add(paramITestResult);
        }
    }

    public void onTesStart(ITestResult paramITestResult) {
    }

    public void onTesSuccess(ITestResult paramITestResult) {
        try {
            if (paramITestResult.getAttribute("passedButFailed").equals("passedButFailed")) {
                paramITestResult.setStatus(2);
                paramITestResult.setThrowable(new ATUReporterStepFailedException());
                this.failedTests.add(paramITestResult);
            }
        } catch (NullPointerException var3) {
        }
        this.passedTests.add(paramITestResult);
    }

    public static void setPlatformBrowserDetails(ITestResult paramITestResult) {
        Platform.prepareDetails(ATUReports.getWebDriver());
        paramITestResult.setAttribute(Platform.BROWSER_NAME_PROP, Platform.BROWSER_NAME);
        paramITestResult.setAttribute(Platform.BROWSER_VERSION_PROP, Platform.BROWSER_VERSION);
    }

    public static void createReportDir(ITestResult paramITestResult) {
        String str = getReportDir(paramITestResult);
        Directory.mkDirs(str);
        Directory.mkDirs(str + Directory.SEP + Directory.SCREENSHOT_DIRName);
    }

    public static String getRElativePathFromSuiteLevel(ITestResult paramITestResult) {
        String str4 = paramITestResult.getMethod().getMethodName();
        str4 = str4 + "_Iter" + (paramITestResult.getMethod().getCurrentInvocationCount() + 1);
        return paramITestResult.getTestClass().getRealClass().getSimpleName() + Directory.SEP + str4;
    }

    public static String getReportDir(ITestResult paramITestResult) {
        String str1 = getRElativePathFromSuiteLevel(paramITestResult);
        paramITestResult.setAttribute("relativeReportDir", str1);
        String str2 = Directory.RUNDir + Directory.SEP + str1;
        paramITestResult.setAttribute("iteration", (paramITestResult.getMethod().getCurrentInvocationCount() + 1));
        paramITestResult.setAttribute("reportDir", str2);
        return str2;
    }

    public void setTickInterval(List<ITestResult> paramList1, List<ITestResult> paramList2, List<ITestResult> paramList3) throws ATUReporterException {
        int i = SettingsFile.getHighestTestCaseNumber();
        int j = SettingsFile.getBiggestNumber(new int[]{i, paramList1.size(), paramList2.size(), paramList3.size()});
        int k = j / 10;
        if (k > 1) {
            HTMLDesignFilesJSWriter.TICK_INTERVAL = k;
        }
    }

    public void onFinish() {
        try {
            String str1 = SettingsFile.get("passedList") + this.passedTests.size() + ';';
            String str2 = SettingsFile.get("failedList") + this.failedTests.size() + ';';
            String str3 = SettingsFile.get("skippedList") + this.skippedTests.size() + ';';
            SettingsFile.set("passedList", str1);
            SettingsFile.set("failedList", str2);
            SettingsFile.set("skippedList", str3);
            this.setTickInterval(this.passedTests, this.failedTests, this.skippedTests);
            HTMLDesignFilesJSWriter.lineChartJS(str1, str2, str3, this.runCount);
            HTMLDesignFilesJSWriter.barChartJS(str1, str2, str3, this.runCount);
            HTMLDesignFilesJSWriter.pieChartJS(this.passedTests.size(), this.failedTests.size(), this.skippedTests.size(), this.runCount);
            this.generateIndexPage();
            long l = (Long) Attributes.getAttribute("startExecutionTime");
            this.generateConsolidatedPage();
            this.generateCurrentRunPage(this.passedTests, this.failedTests, this.skippedTests, l, System.currentTimeMillis());
            this.startReportingForPassed(this.passedTests);
            this.startReportingForFailed(this.failedTests);
            this.startReportingForSkipped(this.skippedTests);
            if (Directory.generateExcelReports) {
                ExcelReports.generateExcelReport(Directory.RUNDir + Directory.SEP + "(" + Directory.REPORTSDIRName + ") " + Directory.RUNName + this.runCount + ".xlsx", this.passedTests, this.failedTests, this.skippedTests);
            }
            if (Directory.generateConfigReports) {
                ConfigurationListener.startConfigurationMethodsReporting(this.runCount);
            }
        } catch (Exception var6) {
            throw new IllegalArgumentException(var6);
        }
    }

    public void startCreatingDirs(ISuite paramISuite) {
        Iterator localIterator = paramISuite.getXmlSuite().getTests().iterator();
        while (localIterator.hasNext()) {
            XmlTest localXmlTest = (XmlTest) localIterator.next();
            Directory.mkDirs(Directory.RUNDir + Directory.SEP + paramISuite.getName() + Directory.SEP + localXmlTest.getName());
        }
    }

    public void generateIndexPage() {
        PrintWriter localPrintWriter = null;
        try {
            localPrintWriter = new PrintWriter(Directory.REPORTSDir + Directory.SEP + "index.html");
            IndexPageWriter.header(localPrintWriter);
            IndexPageWriter.content(localPrintWriter, ATUReports.indexPageDescription);
            IndexPageWriter.footer(localPrintWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                localPrintWriter.close();
            } catch (Exception e) {
                localPrintWriter = null;
            }
        }
    }

    public void generateCurrentRunPage(List<ITestResult> paramList1, List<ITestResult> paramList2, List<ITestResult> paramList3,
                                           long paramLong1, long paramLong2) {
        PrintWriter localPrintWriter = null;
        try {
            localPrintWriter = new PrintWriter(Directory.RUNDir + Directory.SEP + "CurrentRun.html");
            CurrentRunPageWriter.header(localPrintWriter);
            CurrentRunPageWriter.menuLink(localPrintWriter, 0);
            CurrentRunPageWriter.content(localPrintWriter, paramList1, paramList2, paramList3,
                    ConfigurationListener.passedConfigurations, ConfigurationListener.failedConfigurations,
                    ConfigurationListener.skippedConfigurations, this.runCount, paramLong1, paramLong2);
            CurrentRunPageWriter.footer(localPrintWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                localPrintWriter.close();
            } catch (Exception e) {
                localPrintWriter = null;
            }
        }
    }

    public void generateConsolidatedPage() {
        PrintWriter localPrintWriter = null;

        try {
            localPrintWriter = new PrintWriter(Directory.RESULTSDir + Directory.SEP + "ConsolidatedPage.html");
            ConsolidatedReportsPageWriter.header(localPrintWriter);
            ConsolidatedReportsPageWriter.menuLink(localPrintWriter, this.runCount);
            ConsolidatedReportsPageWriter.content(localPrintWriter);
            ConsolidatedReportsPageWriter.footer(localPrintWriter);
        } catch (FileNotFoundException var11) {
            var11.printStackTrace();
        } finally {
            try {
                localPrintWriter.close();
            } catch (Exception var10) {
                localPrintWriter = null;
            }
        }
    }

    public void startReportingForPassed(List<ITestResult> paramList) {
        PrintWriter localPrintWriter = null;
        Iterator localIterator = paramList.iterator();

        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            String str = localITestResult.getAttribute("reportDir").toString();

            try {
                localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
                TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
                TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
                TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
                TestCaseReportsPageWriter.footer(localPrintWriter);
            } catch (FileNotFoundException var15) {
                var15.printStackTrace();
            } finally {
                try {
                    localPrintWriter.close();
                } catch (Exception var14) {
                    localPrintWriter = null;
                }
            }
        }
    }

    public void startReportingForFailed(List<ITestResult> paramList) {
        PrintWriter localPrintWriter = null;
        Iterator localIterator = paramList.iterator();

        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            String str = localITestResult.getAttribute("reportDir").toString();

            try {
                localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
                TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
                TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
                TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
                TestCaseReportsPageWriter.footer(localPrintWriter);
            } catch (FileNotFoundException var15) {
                var15.printStackTrace();
            } finally {
                try {
                    localPrintWriter.close();
                } catch (Exception var14) {
                    localPrintWriter = null;
                }
            }
        }
    }

    public void startReportingForSkipped(List<ITestResult> paramList) {
        PrintWriter localPrintWriter = null;
        Iterator localIterator = paramList.iterator();

        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            String str = localITestResult.getAttribute("reportDir").toString();

            try {
                localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
                TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
                TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
                TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
                TestCaseReportsPageWriter.footer(localPrintWriter);
            } catch (FileNotFoundException var15) {
                var15.printStackTrace();
            } finally {
                try {
                    localPrintWriter.close();
                } catch (Exception var14) {
                    localPrintWriter = null;
                }
            }
        }
    }

    public void onExecutionFinish() {
        Attributes.setAttribute("endExecution", System.currentTimeMillis());
        if (Directory.recordSuiteExecution) {
            ;
        }
    }

    private void initChecking() {
        try {
            Directory.verifyRequiredFiles();
            SettingsFile.correctErrors();
            this.runCount = Integer.parseInt(SettingsFile.get("run").trim()) + 1;
            SettingsFile.set("run", "" + this.runCount);
            Directory.RUNDir = Directory.RUNDir + this.runCount;
            Directory.mkDirs(Directory.RUNDir);
        } catch (Exception var2) {
            throw new IllegalStateException(var2);
        }
    }

    public void onExecutionStart() {
        Attributes.setAttribute("startExecutionTime", System.currentTimeMillis());
        this.initChecking();
    }

    public void generateReport(List<XmlSuite> paramList, List<ISuite> paramList1, String paramString) {
        Iterator localIterator = paramList1.iterator();
        while (localIterator.hasNext()) {
            ISuite localISuite = (ISuite) localIterator.next();
            Attributes.setSuiteNameMapper(localISuite.getName());
            this.onFinish();
        }
    }

    public void onFinish(ISuite paramISuite) {
    }

    public void onStart(ISuite paramISuite) {
    }
}