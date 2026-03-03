package Package2.com.realpage.automation.AutomationUtils.com.rp.listeners;

import Package2.com.realpage.automation.AutomationUtils.com.rp.utils.AzureUtils;
import Package2.com.realpage.automation.AutomationUtils.com.rp.utils.UpdateTestStatusTFS;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.IOException;

public class TFSListener {

    String testCaseStatusUpdate = "";

    public TFSListener() {
    }

    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {
        this.testCaseStatusUpdate = result.getTestContext().getSuite().getParameter("testCaseStatusUpdate");
        if (this.testCaseStatusUpdate.equalsIgnoreCase("true")) {
             try {
                 if (result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(UpdateTestStatusTFS.class) != null) {
                     (new AzureUtils()).updateTestCaseExecution(result);
                 }
             } catch (IOException var3) {
                 var3.printStackTrace();
             }
        }
    }

    public void onTestFailure(ITestResult result) {
        this.testCaseStatusUpdate = result.getTestContext().getSuite().getParameter("testCaseStatusUpdate");
        if (this.testCaseStatusUpdate.equalsIgnoreCase("true")) {
            try {
                if (result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(UpdateTestStatusTFS.class) != null) {
                    (new AzureUtils()).updateTestCaseExecution(result);
                }
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }
    }

    public void onTestSkipped(ITestResult result) {
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }
}
