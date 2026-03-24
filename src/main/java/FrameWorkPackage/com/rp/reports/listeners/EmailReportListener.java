package FrameWorkPackage.com.rp.reports.listeners;

import org.testng.*;
import org.testng.xml.XmlTest;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class EmailReportListener implements ITestListener, ISuiteListener {
    private static Instant suiteStartTime;
    private List<String> passedTestsNames = new ArrayList<>();
    private List<String> failedTestsNames = new ArrayList<>();
    private List<String> skippedTestsNames = new ArrayList<>();
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    private int totalTests = 0;

    public void onStart(ISuite suite) {
        this.passedTests = 0;
        this.failedTests = 0;
        this.skippedTests = 0;
        this.totalTests = 0;
        suiteStartTime = Instant.now();
    }

    public void onFinish(ISuite suite) {
        String suiteName = suite.getName();
        Map<String, String> testParamsMap = new HashMap<>();

        for (XmlTest test : suite.getXmlSuite().getTests()) {
            String testName = test.getName();
            Map<String, String> parameters = test.getAllParameters();
            testParamsMap.put(testName, parameters.toString());
        }

        Instant suiteEndTime = Instant.now();
        Duration duration = Duration.between(suiteStartTime, suiteEndTime);
        long totalTimeMillis = duration.toMillis();
        long totalTimeSeconds = totalTimeMillis / 1000L;
        this.sendEmailReport(suiteName, testParamsMap.toString(), this.getExecutionTime(totalTimeSeconds));
    }

    private String getExecutionTime(long durationInMillis) {
        long hours = durationInMillis / 3600000L;
        long remainingInMillis = durationInMillis % 3600000L;
        long minutes = remainingInMillis / 60000L;
        remainingInMillis %= 60000L;
        long seconds = remainingInMillis / 1000L;
        long milliseconds = remainingInMillis % 1000L;
        StringBuilder sb = new StringBuilder();
        if (hours > 0L) {
            sb.append(hours).append(" Hr ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append(" Min's ");
        }
        if (seconds > 0L) {
            sb.append(seconds).append(" Sec ");
        }
        if (milliseconds > 0L) {
            sb.append(milliseconds).append(" Milli Sec");
        }
        return sb.toString().trim();
    }

    public void onTestSuccess(ITestResult result) {
        ++this.passedTests;
        ++this.totalTests;
        this.passedTestsNames.add(result.getName());
    }

    public void onTestFailure(ITestResult result) {
        ++this.failedTests;
        ++this.totalTests;
        String errorMessage = result.getThrowable().getMessage();
        String stackTrace = getStackTrace(result.getThrowable());
        this.failedTestsNames.add(result.getName() + " - Error: " + errorMessage + "\nStack Trace:\n" + stackTrace);
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public void onTestSkipped(ITestResult result) {
        ++this.skippedTests;
        ++this.totalTests;
        this.skippedTestsNames.add(result.getName());
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }

    public List<String> getPassedTests() {
        return this.passedTestsNames;
    }

    public List<String> getFailedTests() {
        return this.failedTestsNames;
    }

    public List<String> getSkippedTests() {
        return this.skippedTestsNames;
    }

    private void sendEmailReport(String suiteName, String testDetails, String totalTimeSeconds) {
        String host = "mail.realpage.com";
        String port = "25";
        String mailFrom = "no_reply_QA@realpage.com";
        String recipientVariable = System.getProperty("email_recipients");
        if (recipientVariable != null && !recipientVariable.isEmpty()) {
            String[] recipients = recipientVariable.split(";");
            String mailSubject = System.getProperty("mailSubject", "");
            String subject = "Test Suite Report - " + suiteName + " - " + (mailSubject.isEmpty() ? "" : mailSubject) + " - " + this.getCurrentDateTime();
            Properties props = new Properties();
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.starttls.enable", "false");
            props.put("mail.smtp.host", "mail.realpage.com");
            props.put("mail.smtp.port", "25");
            Session session = Session.getInstance(props);

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailFrom));

                for (String recipient : recipients) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                }

                message.setSubject(subject);
                message.setSentDate(new Date());
                String content = "<html><body>";
                content = content + "<h2>Test Suite Report Summary</h2>";
                content = content + "<table style=\"width:50%\">";
                content = content + "<tr><th width=\"50%\" bgcolor=\"DodgerBlue\"><b style=\"color:white;\">Test Status</b></th><th width=\"50%\" bgcolor=\"DodgerBlue\"><b style=\"color:white;\">Count</b></th></tr>";
                content = content + "<tr style=\"background-color: #7dce1\"><td>Passed Tests</td><td align='center'>" + this.passedTests + "</td></tr>";
                content = content + "<tr style=\"background-color: #f78365\"><td>Failed Tests</td><td align='center'>" + this.failedTests + "</td></tr>";
                content = content + "<tr style=\"background-color: #ffb30e\"><td>Skipped Tests</td><td align='center'>" + this.skippedTests + "</td></tr>";
                content = content + "<tr style=\"background-color: #c6c4cc\"><td>Total tests</td><td align='center'>" + this.totalTests + "</td></tr>";
                content = content + "</table></body></html>";

                int totalTests = this.passedTestsNames.size() + this.failedTestsNames.size() + this.skippedTestsNames.size();
                double passPercentage = (double) this.passedTestsNames.size() / (double) totalTests * (double) 100.0F;
                double failPercentage = (double) this.failedTestsNames.size() / (double) totalTests * (double) 100.0F;
                double skippedPercentage = (double) this.skippedTestsNames.size() / (double) totalTests * (double) 100.0F;
                StringBuilder htmlContent = new StringBuilder();
                htmlContent.append("<html><head><style>table, th, td { border: 1px solid white; border-collapse: collapse; padding: 4px; }</style></head><body>");
                htmlContent.append("<h2>Suite Details:</h2>");
                htmlContent.append("<p>Suite Name: ").append(suiteName).append("</b></p>");
                htmlContent.append("<h3>Test Metrics:</h3>");
                htmlContent.append("<table style=\"width:30%\"><tr style=\"background-color: white\"><th width=\"50%\"><b style=\"color:black;\">");
                htmlContent.append("<div class=\"scalar-content\">");
                htmlContent.append("<p><div style=\"font-size: 25px; font-weight: bold; color: lightgreen;\">").append((int) passPercentage).append("%</div>");
                htmlContent.append("<div style=\"font-size: 14px\">Pass Percentage</div></p>");
                htmlContent.append("</div>");
                htmlContent.append("</th>");
                htmlContent.append("<th width=\"50%\"><b style=\"color:grey;\">");
                htmlContent.append("<div class=\"scalar-content\">");
                htmlContent.append("<p><div style=\"font-size: 25px; font-weight: bold;\">").append(totalTimeSeconds).append(" </div>");
                htmlContent.append("<div style=\"font-size: 14px\">Run Duration</div></p>");
                htmlContent.append("</div>");
                htmlContent.append("</th></tr>");
                htmlContent.append("</table>");
                htmlContent.append("<h3>Passed Tests:</h3>");
                if (!this.passedTestsNames.isEmpty()) {
                    htmlContent.append("<table style=\"border: 1px solid black\"><tr><th>Test Name</th></tr>");
                    for (String testName : this.passedTestsNames) {
                        htmlContent.append("<tr><td>").append(testName).append("</td></tr>");
                    }
                    htmlContent.append("</table>");
                } else {
                    htmlContent.append("<p>No passed tests</p>");
                }
                htmlContent.append("<h3>Failed Tests:</h3>");
                if (!this.failedTestsNames.isEmpty()) {
                    htmlContent.append("<table style=\"border: 1px solid black\"><tr><th>Test Name</th><th>Error Message</th></tr>");
                    for (String failedTest : this.failedTestsNames) {
                        String[] parts = failedTest.split(" - Error: ");
                        htmlContent.append("<tr><td>").append(parts[0]).append("</td><td>").append(parts[1]).append("</td></tr>");
                    }
                    htmlContent.append("</table>");
                } else {
                    htmlContent.append("<p>No failed tests</p>");
                }

                htmlContent.append("<h3>Skipped Tests:</h3>");
                if (!this.skippedTestsNames.isEmpty()) {
                    htmlContent.append("<table style=\"border: 1px solid black\"><tr><th>Test Name</th></tr>");

                    for (String testName : this.skippedTestsNames) {
                        htmlContent.append("<tr><td>").append(testName).append("</td></tr>");
                    }
                    htmlContent.append("</table>");
                } else {
                    htmlContent.append("<p>No skipped tests</p>");
                }

                String releaseUrl = System.getProperty("releaseUrl");
                if (releaseUrl != null && !releaseUrl.isEmpty()) {
                    htmlContent.append("<table border=\"1\">");
                    htmlContent.append("<tr><th>Azure Release URL</th><th><a href=" + releaseUrl + "><button style=\"background-color: #008CBA; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">Go to Release</button></a></th></tr>");
                    htmlContent.append("</table>");
                } else {
                    System.out.println("releaseUrl Variable not provided. releaseUrl is not added.");
                }

                String reportDownload = System.getProperty("reportDownload");
                if (reportDownload != null && !reportDownload.isEmpty()) {
                    htmlContent.append("<table border=\"1\">");
                    htmlContent.append("<tr><th>Download Report for Release</th><th><a href=" + reportDownload + "><button style=\"background-color: #008CBA; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer;\">Go to Report</button></a></th></tr>");
                    htmlContent.append("</table>");
                } else {
                    System.out.println("ReportDownload Variable not provided. reportDownload link is not added.");
                }

                String triggeredBy = System.getProperty("TriggeredBy", "");
                htmlContent.append("<br><div>Release Triggered By :" + (triggeredBy.isEmpty() ? "" : triggeredBy) + "</div>");
                htmlContent.append("<br>");
                htmlContent.append("<br><div>Thank you for your attention,</div>");
                htmlContent.append("<div>Automation Team</div>");
                htmlContent.append("</body></html>");
                htmlContent.append("</body></html>");
                message.setContent(content + "<br>" + htmlContent, "text/html");
                Transport.send(message);
                System.out.println("Email report sent successfully!");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Recipient email variable not provided. Email not sent.");
        }
    }

    private String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
