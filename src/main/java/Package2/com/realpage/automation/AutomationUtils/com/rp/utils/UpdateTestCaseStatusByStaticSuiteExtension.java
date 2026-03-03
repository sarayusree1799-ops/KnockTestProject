package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdateTestCaseStatusByStaticSuiteExtension {
    public UpdateTestCaseStatusByStaticSuiteExtension() {
    }


    private static final Logger logger = LoggerFactory.getLogger(UpdateTestCaseStatusByStaticSuite.class);
    private static final String planId = System.getProperty("planid");
    private static final String suiteId = System.getProperty("suiteid");
    private static final String productName = "Consumer%20Solutions";
    private String testCaseIds;
    private String testResultsStatus = "";

    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logger.info("Test Disabled for test {}: ", context.getDisplayName());
        this.testResultsStatus = "failed";
        this.updateTestStatus(context, this.testResultsStatus);
    }

    public void testSuccessful(ExtensionContext context) {
        logger.info("Test Successful for test {}: ", context.getDisplayName());
        this.testResultsStatus = "passed";
        this.updateTestStatus(context, this.testResultsStatus);
    }

    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.info("Test Aborted for test {}: ", context.getDisplayName());
        this.testResultsStatus = "failed";
        this.updateTestStatus(context, this.testResultsStatus);
    }

    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.info("Test Failed for test {}: ", context.getDisplayName());
        this.testResultsStatus = "failed";
        this.updateTestStatus(context, this.testResultsStatus);
    }

    private void updateTestStatus(ExtensionContext ctx, String testStatus) {
        if (AnnotationSupport.isAnnotated(ctx.getTestMethod(), UpdateTestCaseStatusByStaticSuite.class)) {
            logger.info("Test method configured with Update Test Status - Test status will be updated in Azure");
        }

        Optional<UpdateTestCaseStatusByStaticSuite> uts = AnnotationSupport.findAnnotation(ctx.getElement(), UpdateTestCaseStatusByStaticSuite.class);
        this.testCaseIds = ((UpdateTestCaseStatusByStaticSuite) uts.get()).testCaseIds();
        logger.info("Test Status - " + testStatus);
        logger.info("Suite ID - " + suiteId);
        logger.info("Product Name - Consumer%20Solutions");
        logger.info("Test Case ids - " + this.testCaseIds);
        AzureUtils au = new AzureUtils();
        au.updateTestStatus("Consumer%20Solutions", planId, suiteId, this.testCaseIds, testStatus);
    }
}
