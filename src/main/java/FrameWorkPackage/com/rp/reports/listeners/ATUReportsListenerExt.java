package FrameWorkPackage.com.rp.reports.listeners;

import FrameWorkPackage.com.rp.automation.framework.annotations.AzureSuiteIDTestCaseId;
import FrameWorkPackage.com.rp.automation.framework.annotations.AzureTestCaseId;
import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.AzureUtils;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter;
import Reports.com.rp.reports.excel.ExcelReports;
import Reports.com.rp.reports.exceptions.ATUReporterStepFailedException;
import Reports.com.rp.reports.listeners.ATUReportsListener;
import Reports.com.rp.reports.listeners.ConfigurationListener;
import Reports.com.rp.reports.utils.Attributes;
import Reports.com.rp.reports.utils.Directory;
import Reports.com.rp.reports.utils.SettingsFile;
import Reports.com.rp.reports.writers.ConsolidatedReportsPageWriter;
import Reports.com.rp.reports.writers.HTMLDesignFilesJSWriter;
import org.testng.ITestResult;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.testng.*;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class ATUReportsListenerExt extends ATUReportsListener {
    public ATUReportsListenerExt() {
        super.runCount = 1;
    }

    public void generateReport(List<XmlSuite> paramList, List<ISuite> paramList1, String paramString) {
        for (ISuite localISuite : paramList1) {
            Attributes.setSuiteNameMapper(localISuite.getName());
            this.startCreatingDirs(localISuite);
            this.onFinish();
        }
    }

    public void onTestStart(ITestResult paramITestResult) {
        try {
            AtuReports.setAzureTestCaseId(paramITestResult);
        } catch (Exception var3) {
        }
    }

    public void onTestFailure(ITestResult paramITestResult) {
        this.failedTests.add(paramITestResult);

        try {
            if (paramITestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureTestCaseId.class) != null) {
                (new AzureUtils()).updateTestCaseExecution(paramITestResult);
            }

            if (paramITestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class) != null &&
                    ((AzureSuiteIDTestCaseId) paramITestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class)).suiteId() != null) {
                (new AzureUtils()).updateTestCaseExecution(paramITestResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTestSuccess(ITestResult paramITestResult) {
        try {
            if (paramITestResult.getAttribute("passedButFailed").equals("passedButFailed")) {
                paramITestResult.setStatus(2);
                paramITestResult.setThrowable(new ATUReporterStepFailedException());
                this.failedTests.add(paramITestResult);
                return;
            }
        } catch (NullPointerException var4) {
        }

        this.passedTests.add(paramITestResult);

        try {
            if (paramITestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureTestCaseId.class) != null) {
                System.out.println("AzureTestCaseId");
                (new AzureUtils()).updateTestCaseExecution(paramITestResult);
            }

            if (paramITestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class) != null &&
                    ((AzureSuiteIDTestCaseId) paramITestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class)).suiteId() != null) {
                System.out.println("AzureSuiteIDTestCaseId");
                (new AzureUtils()).updateTestCaseExecution(paramITestResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            HTMLDesignFilesJSWriter.barChartJS(str1, str2, str3, 1);
            HTMLDesignFilesJSWriter.pieChartJS(this.passedTests.size(), this.failedTests.size(), this.skippedTests.size(), 1);
            this.generateIndexPage();
            long l = (Long) Attributes.getAttribute("startExecution");
            this.generateConsolidatedPage();
            this.generateCurrentRunPage(this.passedTests, this.failedTests, this.skippedTests, l, System.currentTimeMillis());
            this.startReportingForPassed(this.passedTests);
            this.startReportingForFailed(this.failedTests);
            this.startReportingForSkipped(this.skippedTests);
            if (Directory.generateExcelReports) {
                ExcelReports.generateExcelReport(Directory.RUNDir + Directory.SEP + "(" + Directory.REPORTSDIRName + ") " + Directory.RUNName + 1 + ".xlsx", this.passedTests, this.failedTests, this.skippedTests);
            }

            if (Directory.generateConfigReports) {
                ConfigurationListener.startConfigurationMethodsReporting(1);
            }
        } catch (NullPointerException var6) {
        } catch (
                Exception localException) {
            throw new IllegalStateException(localException);
        }
    }

    public void onExecutionStart() {
        try {
            File reportsFolder = new File(System.getProperty("user.dir") + "//Reports//" + Reporter.getDateFormat(Reporter.vDatetype1));
            System.out.println("@@@@@@==" + reportsFolder);
            System.out.println("@@@@@@11111==" + reportsFolder.exists());
            reportsFolder.mkdir();
            this.createAtuConfigPropFile(reportsFolder.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.initChecking();
    }

    private void createAtuConfigPropFile1(String filePath) {
        try {
            Properties properties = new Properties();
            properties.setProperty("atu.reports.dir", filePath.toString());
            properties.setProperty("atu.proj.header.text", "Automation Project Reports");
            properties.setProperty("atu.proj.header.logo", System.getProperty("user.dir") + "\\ProjectLogos\\RealpageLogo.png");
            properties.setProperty("atu.proj.description", "Project Testing Reports");
            properties.setProperty("atu.reports.takescreenshot", "true");
            properties.setProperty("atu.reports.configurationreports", "false");
            properties.setProperty("atu.reports.excel", "false");
            properties.setProperty("atu.reports.continueExecutionAfterStepFailed", "false");
            properties.setProperty("atu.reports.recordExecution", "none");
            properties.setProperty("atu.reports.setMaxRuns", "100");
            properties.setProperty("atu.reports.pdf", "false");
            File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\atu.properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "config properties ");
            fileOut.close();
            System.setProperty("atu.reports.config", file.getAbsolutePath());
            Attributes.setAttribute("startExecution", System.currentTimeMillis());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAtuConfigPropFile(String filePath) {
        try {
            Properties properties = new Properties();
            properties.setProperty("atu.reports.dir", filePath);
            properties.setProperty("atu.proj.header.text", "Automation Project Reports");
            properties.setProperty("atu.proj.header.logo", Paths.get(System.getProperty("user.dir"), "ProjectLogos", "RealpageLogo.png").toString());
            properties.setProperty("atu.proj.description", "Project Testing Reports");
            properties.setProperty("atu.reports.takescreenshot", "true");
            properties.setProperty("atu.reports.configurationreports", "false");
            properties.setProperty("atu.reports.excel", "false");
            properties.setProperty("atu.reports.continueExecutionAfterStepFailed", "false");
            properties.setProperty("atu.reports.recordExecution", "none");
            properties.setProperty("atu.reports.setMaxRuns", "100");
            properties.setProperty("atu.reports.pdf", "true");
            String propfilePath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "atu.properties").toString();
            File file = new File(propfilePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fileOut = new FileOutputStream(file);

            try {
                properties.store(fileOut, "config properties");
                System.setProperty("atu.reporter.config", file.getAbsolutePath());
                Attributes.setAttribute("startExecution", System.currentTimeMillis());
            } catch (Throwable var9) {
                try {
                    fileOut.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
                throw var9;
            }

            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) throws Exception {
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for(int i = 0; i < children.length; ++i) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public void startCreatingDirs(ISuite paramISuite) {
        Directory.mkDirs(Directory.RUNDir + Directory.SEP + paramISuite.getName());

        for(XmlTest localXmlTest : paramISuite.getXmlSuite().getTests()) {
            Directory.mkDirs(Directory.RUNDir + Directory.SEP + paramISuite.getName() + Directory.SEP + localXmlTest.getName());
        }
    }

    public void initChecking() {
        try {
            Directory.verifyRequiredFiles();
            SettingsFile.correctErrors();
            this.runCount = 1;
            SettingsFile.set("run", "" + this.runCount);
            Directory.RUNDir = Directory.RUNDir + this.runCount;
            Directory.mkDirs(Directory.RUNDir);
            if (Directory.recordSuiteExecution) {
            }
        } catch (Exception localException) {
            throw new IllegalStateException(localException);
        }
    }

    public void generateConsolidatedPage() {
        PrintWriter localPrintWriter = null;

        try {
            localPrintWriter = new PrintWriter(Directory.RESULTSDir + Directory.SEP + "ConsolidatedPage.html");
            ConsolidatedReportsPageWriter.header(localPrintWriter);
            ConsolidatedReportsPageWriter.menuLink(localPrintWriter, this.runCount);
            content(localPrintWriter);
            ConsolidatedReportsPageWriter.footer(localPrintWriter);
            return;
        } catch (FileNotFoundException localFileNotFoundException) {
            localFileNotFoundException.printStackTrace();
        } finally {
            try {
                localPrintWriter.close();
            } catch (Exception var10) {
                PrintWriter var13 = null;
            }
        }
    }

    public static void content(PrintWriter paramPrintWriter) {
        paramPrintWriter.println("<td id=\"content\">\n\n <div id=\"tabs\">\n<ul>\n <li><a href=\"#tabs-2\">Bar Chart</a></li> \n</ul>\n <div id=\"tabs-2\" style=\"text-align: left; color: black; font-size: 14px\">\n <p class=\"info\" style=\"text-align: center; color: black;\"> \n The following Bar chart demonstrates the number of Passed, Failed and Skipped Test Cases\n </p>\n <div id=\"bar\" style=\"margin-top: 20px; margin-left: 20px; width: 85%; height: 300px; color: black;\"></div>\n </div> \n</td>\n</tr>");
    }
}
