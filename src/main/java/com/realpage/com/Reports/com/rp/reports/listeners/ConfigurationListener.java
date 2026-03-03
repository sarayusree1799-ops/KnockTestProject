package com.realpage.com.Reports.com.rp.reports.listeners;

import com.realpage.com.Reports.com.rp.reports.writers.TestCaseReportsPageWriter;
import com.realpage.com.Reports.com.rp.reports.utils.Directory;
import org.testng.IConfigurationListener;
import org.testng.ITestResult;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigurationListener implements IConfigurationListener {
    static List<ITestResult> passedConfigurations = new ArrayList<>();
    static List<ITestResult> failedConfigurations = new ArrayList<>();

    static List<ITestResult> skippedConfigurations = new ArrayList<>();

    public static int methodInvocationCount;

    public ConfigurationListener() {
    }

    public void onConfigurationFailure(ITestResult paramITestResult) {
        if (Directory.generateConfigReports) {
            failedConfigurations.add(paramITestResult);
        }
    }

    public void onConfigurationSkip(ITestResult paramITestResult) {
        if (Directory.generateConfigReports) {
            skippedConfigurations.add(paramITestResult);
        }
    }

    public void onConfigurationSuccess(ITestResult paramITestResult) {
        if (Directory.generateConfigReports) {
            passedConfigurations.add(paramITestResult);
        }
    }

    public static void startConfigurationMethodsReporting(int paramInt) {
        startReportingForPassedConfigurations(passedConfigurations, paramInt);
        startReportingForFailedConfigurations(failedConfigurations, paramInt);
        startReportingForSkippedConfigurations(skippedConfigurations, paramInt);
    }

    public static void testMethodInvocationCount(ITestResult result) {
        methodInvocationCount = result.getMethod().getCurrentInvocationCount() + 1;
        System.out.println("methodInvocationCount" + methodInvocationCount);
    }

    private static void startReportingForPassedConfigurations(List<ITestResult> paramList, int paramInt) {
        PrintWriter localPrintWriter = null;
        Iterator localIterator = paramList.iterator();

        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            String str = localITestResult.getAttribute("reportDir").toString();
            try {
                localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + methodInvocationCount + ".html");
                TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
                TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
                TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, paramInt);
                TestCaseReportsPageWriter.footer(localPrintWriter);
                try {
                    localPrintWriter.close();
                } catch (Exception var16) {
                    localPrintWriter = null;
                }
            } catch (FileNotFoundException var17) {
                var17.printStackTrace();
            } finally {
                try {
                    localPrintWriter.close();
                } catch (Exception var15) {
                    localPrintWriter = null;
                }
            }
        }
    }

    private static void startReportingForFailedConfigurations(List<ITestResult> paramList, int paramInt) {
        PrintWriter localPrintWriter = null;
        Iterator localIterator = paramList.iterator();

        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            String str = localITestResult.getAttribute("reportDir").toString();
            try {
                localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + methodInvocationCount + ".html");
                TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
                TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
                TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, paramInt);
                TestCaseReportsPageWriter.footer(localPrintWriter);
                try {
                    localPrintWriter.close();
                } catch (Exception var17) {
                    localPrintWriter = null;
                }
            } catch (FileNotFoundException var18) {
                var18.printStackTrace();
            } finally {
                try {
                    localPrintWriter.close();
                } catch (Exception var16) {
                    localPrintWriter = null;
                }
            }
        }
    }

    private static void startReportingForSkippedConfigurations(List<ITestResult> paramList, int paramInt) {
        PrintWriter localPrintWriter = null;
        Iterator localIterator = paramList.iterator();

        while (localIterator.hasNext()) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            String str = localITestResult.getAttribute("reportDir").toString();
            try {
                localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + methodInvocationCount + ".html");
                TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
                TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
                TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, paramInt);
                TestCaseReportsPageWriter.footer(localPrintWriter);
                try {
                    localPrintWriter.close();
                } catch (Exception var16) {
                    localPrintWriter = null;
                }
            } catch (FileNotFoundException var17) {
                var17.printStackTrace();
            } finally {
                try {
                    localPrintWriter.close();
                } catch (Exception var15) {
                    localPrintWriter = null;
                }
            }
        }
    }

    public void beforeConfiguration(ITestResult paramITestResult) {
    }
}