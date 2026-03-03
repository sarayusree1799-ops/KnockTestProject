package FrameWorkPackage.com.rp.automation.framework.util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    int counter = 0;
    int retryLimit = 1;

    public boolean retry(ITestResult result) {
        if (this.counter < this.retryLimit) {
            ++this.counter;
            return true;
        } else {
            return false;
        }
    }
}
