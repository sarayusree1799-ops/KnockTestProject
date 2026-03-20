package FrameWorkPackage.com.rp.reports.listeners;

import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class CustomRetryListener extends TestListenerAdapter implements IRetryAnalyzer, IAnnotationTransformer {
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1;
    private List<ITestResult> initialTestResults = new ArrayList<>();
    private List<ITestResult> retryTestResults = new ArrayList<>();
    private Map<String, Integer> initialTestCounts = new HashMap<>();
    private Map<String, Integer> retryTestCounts = new HashMap<>();
    private int totalExecutedTests = 0;

    public void onStart(ITestContext context) {
        super.onStart(context);
        this.initializeTestCounts(context.getAllTestMethods());
    }

    private void initializeTestCounts(ITestNGMethod[] iTestNGMethods) {
        for (ITestNGMethod method : iTestNGMethods) {
            this.initialTestCounts.put(method.getMethodName(), 0);
            this.retryTestCounts.put(method.getMethodName(), 0);
        }
    }

    public boolean retry(ITestResult result) {
        ++this.retryCount;
        if (this.retryCount <= 1) {
            this.retryTestResults.add(result);
            this.updateRetryCounts(result);
            ++this.totalExecutedTests;
            return true;
        } else {
            return false;
        }
    }

    private void updateRetryCounts(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        this.retryTestCounts.put(methodName, (Integer)this.retryTestCounts.getOrDefault(methodName, 0) + 1);
    }

    public void onTestFailure(ITestResult result) {
        this.initialTestResults.add(result);
        this.updateInitialCounts(result);
    }

    public void onTestSuccess(ITestResult result) {
        this.initialTestResults.add(result);
        this.updateInitialCounts(result);
    }

    private void updateInitialCounts(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        this.initialTestCounts.put(methodName, (Integer)this.initialTestCounts.getOrDefault(methodName, 0) + 1);
    }

    public void onFinish(ITestContext context) {
        super.onFinish(context);
        this.updateSummaryReport(context);
    }

    private void updateSummaryReport1gsd(ITestContext context) {
        System.out.println("Initial Test Results:");
        this.printTestResults(this.initialTestCounts);
        System.out.println("\nRetry Test Results:");
        this.printTestResults(this.retryTestCounts);
    }

    private void updateSummaryReport(ITestContext context) {
        int totalTests = context.getAllTestMethods().length;
        System.out.println("** Overall Test Summary (Including Retries): **");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Executed Tests: " + this.totalExecutedTests);
        int initialPassedTests = this.getPassedTests(this.initialTestResults);
        int retryPassedTests = this.getPassedTests(this.retryTestResults);
        int totalPassedTests = initialPassedTests + retryPassedTests;
        System.out.println("Passed Tests (Initial): " + initialPassedTests);
        System.out.println("Passed Tests (Retry): " + retryPassedTests);
        System.out.println("Total Passed Tests: " + totalPassedTests);
        int initialFailedTests = this.getFailedTests(this.initialTestResults);
        int retryFailedTests = this.getFailedTests(this.retryTestResults);
        int totalFailedTests = initialFailedTests + retryFailedTests;
        System.out.println("Failed Tests (Initial): " + initialFailedTests);
        System.out.println("Failed Tests (Retry): " + retryFailedTests);
        System.out.println("Total Failed Tests: " + totalFailedTests);
        double passPercentage = (double)totalPassedTests / (double)this.totalExecutedTests * (double)100.0F;
        System.out.println("Pass Percentage: " + passPercentage + "%");
    }

    private int getPassedTests(List<ITestResult> testResults) {
        int passedTests = 0;
        for (ITestResult result : testResults) {
            if (result.getStatus() == 1) {
                ++passedTests;
            }
        }
        return passedTests;
    }

    private int getFailedTests(List<ITestResult> testResults) {
        int failedTests = 0;
        for (ITestResult result : testResults) {
            if (result.getStatus() == 2) {
                ++failedTests;
            }
        }
        return failedTests;
    }

    private void printTestResults(Map<String, Integer> testCounts) {
        for(Map.Entry<String, Integer> entry : testCounts.entrySet()) {
            System.out.println((String)entry.getKey() + ": " + entry.getValue());
        }
    }

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(this.getClass());
    }
}
