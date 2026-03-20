package Reports.com.rp.reports.writers;

import Reports.com.rp.reports.enums.Colors;
import Reports.com.rp.reports.enums.ReportLabels;
import Reports.com.rp.reports.enums.ExceptionDetails;
import Reports.com.rp.reports.utils.*;
import com.realpage.com.Reports.com.rp.reports.utils.*;
import Reports.com.rp.reports.logging.LogAs;
import org.testng.ITestResult;
import org.testng.Reporter;
import selenium_shutterbug.realpage.com.Reports.com.rp.reports.utils.*;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;


public class TestCaseReportsPageWriter extends ReportsPage {
    public TestCaseReportsPageWriter() {
    }

    public static void header(PrintWriter paramPrintWriter, ITestResult paramITestResult) {
        paramPrintWriter.println("<!DOCTYPE html>\n<html>\n<head>\n<title>Pie Charts</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/CSS/design.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/CSS/jquery.qplot.css\" />\n" +
                "<script type=\"text/javascript\" src=\"" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/JS/jquery.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/JS/jquery.jqplot.min.js\"></script>\n" +
                "<!--[if lt IE 9]>\n<script language=\"javascript\" type=\"text/javascript\" src=\"" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/JS/excanvas.js\"></script>\n<![endif]-->\n" +
                "<script language=\"javascript\" type=\"text/javascript\" src=\"" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/JS/jqplot.pieRenderer.min.js\"></script>\n" +
                "<script type=\"text/javascript\">$(document).ready(function() { $('.exception').hide(); $('.showmenu').show(); $('.showmenu').click(function() { $('.exception').toggle(\"slide\"); }); });</script>\n" +
                "</head>\n<body>\n<table id=\"mainTable\">\n<tr id=\"header\">\n" +
                "<td id=\"logo\"><img src=\"../" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/IMG/" + ReportLabels.ATU_LOGO.getLabel() + "\" alt=\"Logo\" height=\"80\" width=\"140\" /> <br/>" + ReportLabels.ATU_CAPTION.getLabel() + "</td>\n" +
                "<td id=\"headertext\">\n" + ReportLabels.HEADER_TEXT.getLabel() + "\n<div style=\"padding-right:20px;float:right;\"><img src=\"../" + getTestCaseHTMLPath(paramITestResult) + "HTML_Design_Files/IMG/" + ReportLabels.PROJ_LOGO.getLabel() + "\" height=\"70\" width=\"140\" /></div>\n</td>\n</tr>");
    }

    public static String getExecutionTime(ITestResult paramITestResult) {
        long l = paramITestResult.getEndMillis() - paramITestResult.getStartMillis();
        if (l > 1000L) {
            l /= 1000L;
            return l + " Sec";
        } else {
            return l + " Milli Sec";
        }
    }

    private static String getExceptionDetails(ITestResult paramITestResult) {
        try {
            paramITestResult.getThrowable().toString();
        } catch (Throwable var4) {
            return "";
        }

        String str1 = paramITestResult.getThrowable().toString();
        String str2 = str1;
        if (str1.contains(":")) {
            str2 = str1.substring(0, str1.indexOf(":")).trim();
        } else {
            str1 = "";
        }

        try {
            str2 = getExceptionClassName(str2, str1);
            if (str2.equals("Assertion Error")) {
                if (str1.contains(">")) {
                    str2 = str2 + str1.substring(str1.indexOf(">"), str1.lastIndexOf(">") + 1).replace(">", "\"");
                    str2 = str2.replace("<", "\"");
                }

                if (paramITestResult.getThrowable().getMessage().trim().length() > 0) {
                    str2 = paramITestResult.getThrowable().getMessage().trim();
                }
            } else if (str1.contains("{")) {
                str2 = str2 + str1.substring(str1.indexOf("{"), str1.lastIndexOf("}"));
                str2 = str2.replace("\"method\":\"", " With ").replace("\"selector\":\"", " = ");
            } else if (str2.equals("Unable to Connect Browser") && str1.contains(".")) {
                str2 = str2 + ". Browser is Closed";
            } else if (str2.equals("WebDriver Exception")) {
                str2 = paramITestResult.getThrowable().getMessage();
            }
        } catch (ClassNotFoundException var5) {
        } catch (Exception var6) {
        }

        str2 = str2.replace("\"", "^");
        str2 = str2.replace("<", "^");
        return str2;
    }

    private static String getExceptionClassName(String paramString1, String paramString2) throws ClassNotFoundException {
        String str = "";

        try {
            str = ExceptionDetails.valueOf(paramString1.trim().replace(".", "_")).getExceptionInfo();
        } catch (Exception var4) {
            str = paramString1;
        }

        return str;
    }

    public static String getReqCoverageInfo(ITestResult paramITestResult) {
        try {
            return paramITestResult.getAttribute("reqCoverage") == null ? ReportLabels.TC_INFO_LABEL.getLabel() :
                    paramITestResult.getAttribute("reqCoverage").toString();
        } catch (Exception var2) {
            return ReportLabels.TC_INFO_LABEL.getLabel();
        }
    }

    public static void content(PrintWriter paramPrintWriter, ITestResult paramITestResult, int paramInt) {
        paramPrintWriter.println("<div class=\"info\"><br/><b>Requirement Coverage/ TestCase Description</b><br/><br/>" +
                getReqCoverageInfo(paramITestResult) + "</div>");

        paramPrintWriter.println("<td id=\"content\">        <div class=\"info\">\n" +
                "        the Run <br/>\nTestCase Name: <b>" + paramITestResult.getName() +
                " : Iteration " + paramITestResult.getAttribute("iteration").toString() + "</b><br/>\n" +
                "        Time Taken for Executing: <b>" + getExecutionTime(paramITestResult) + "</b> <br/>\n" +
                "        Current Run Number: <b>Run " + paramInt + "</b><br/>\n" +
                "        Method Type: <b>" + CurrentRunPageWriter.getMethodType(paramITestResult) + "</b></br>\n" +
                "        </div>");
        paramPrintWriter.println("<div class=\"chartStyle summary\" style=\"background-color:#646D7E;width: 30%; height: 200px;\">\n" +
                "<b>Execution Platform Details</b><br/><br/>\n" +
                "<table>\n" +
                "<tr>\n" +
                "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                "<td>OS</td>\n" +
                "<td>" + Platform.OS + ", " + Platform.OS_ARCH + " Bit, v" + Platform.OS_VERSION + "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                "<td>Java</td>\n" +
                "<td>" + Platform.JAVA_VERSION + "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                "<td>Hostname</td>\n" +
                "<td>" + Platform.getHostName() + "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>Selenium</td>\n" +
                "<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                "<td>" + Platform.DRIVER_VERSION + "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</div>");
        paramPrintWriter.println("<div class=\"chartStyle summary\" style=\"background-color: " + getColorBasedOnResult(paramITestResult) + ";margin-left: 20px; height: 200px;width: 30%;\">\n" +
                "<b>Summary</b><br/><br/>\n" +
                "<table>\n" +
                "<td>Status</td>\n" +
                "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                "<td>" + getResult(paramITestResult) + "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>" + Utils.getCurrentTime() + "</td>\n" +
                "<td>Execution Date</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>Browser/Device</td>\n" +
                "<td>" + getBrowserName(paramITestResult) + "," + getBrowserVersion(paramITestResult) + "</td>\n" +
                "</table>\n" +
                "</div>");
        AuthorDetails localAuthorDetails = null;
        try {
            if (paramITestResult.getAttribute("auhorInfo") == null) {
                localAuthorDetails = new AuthorDetails();
            } else {
                localAuthorDetails = (AuthorDetails) paramITestResult.getAttribute("authorInfo");
            }
        } catch (Exception var17) {
        }
        paramPrintWriter.println("<div class=\"chartStyle summary\" style=\"background-color: #7525D;margin-left: 20px; height: 200px;width: 30%;\">\n" +
                " <b>Author Info</b><br/>\n" +
                " <table>\n" +
                " <tr>\n" +
                " <td>Author Name</td>\n" +
                " <td>" + localAuthorDetails.getAuthorName() + "</td>\n" +
                " </tr>\n" +
                " <tr>\n" +
                " <td>Creation Date</td>\n" +
                " <td>" + localAuthorDetails.getCreationDate() + "</td>\n" +
                " </tr>\n" +
                " <tr>\n" +
                " <td>Version</td>\n" +
                " <td>" + localAuthorDetails.getVersion() + "</td>\n" +
                " </tr>\n" +
                " <tr>\n" +
                " <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                " <td>System User</td>\n" +
                " </tr>\n" +
                " </table>\n" +
                "</div>\n" +
                "<td>" + Platform.USER + "</td>\n                     </tr>\n                </table>\n</div>");

        if (paramITestResult.getStatus() != 3) {
            Object localObject1 = Reporter.getOutput(paramITestResult);
            paramPrintWriter.println("<div>\n" +
                    " <table class=\"chartStyle\" id=\"tableStyle\" style=\"height:50px; float: left\">\n" +
                    " <tr>\n" +
                    " <th>S.No</th>\n" +
                    " <th>Step Description</th>\n" +
                    " <th>Input Value</th>\n" +
                    " <th>Expected Value</th>\n" +
                    " <th>Actual Value</th>\n" +
                    " <th>Status</th>\n" +
                    " <th>Time</th>\n" +
                    " <th>Line No</th>\n" +
                    " <th>Screenshot</th>\n" +
                    " </tr>\n");
            int i = true;
            if (Reporter.getOutput(paramITestResult).size() <= 0) {
                paramPrintWriter.print("<tr>");
                paramPrintWriter.print("<td colspan=\"8\"><b>No Steps Available</b></td>");
                paramPrintWriter.print("</tr>");
            }
            int i = 1;
            Iterator localIterator = ((List) localObject1).iterator();
            while(localIterator.hasNext()) {
                Object localObject2 = (String) localIterator.next();
                Steps localSteps = null;
                if (localSteps == null) {
                    paramPrintWriter.print("<tr>");
                    paramPrintWriter.println("<td>" + i + "</td>");
                    paramPrintWriter.print("td style=\"text-align:left;\" colspan=\"8\">" + (String) localObject2 + "</td></tr>");
                    ++i;
                } else {
                    paramPrintWriter.print("<tr>");
                    paramPrintWriter.println("<td>" + i + "</td>");
                    paramPrintWriter.println("<td>" + localSteps.getDescription() + "</td>");
                    paramPrintWriter.println("<td>" + localSteps.getInputValue() + "</td>");
                    paramPrintWriter.println("<td>" + localSteps.getExpectedValue() + "</td>");
                    paramPrintWriter.println("<td>" + localSteps.getActualValue() + "</td>");
                    paramPrintWriter.println("<td>" + localSteps.getTime() + "</td>");
                    paramPrintWriter.println("<td>" + localSteps.getLineNum() + "</td>");
                    paramPrintWriter.println("<td>" + getLogDescription(localSteps.getLogAs(), paramITestResult) + "</td>");

                    try {
                        Integer.parseInt(localSteps.getScreenShot().trim());
                        paramPrintWriter.println("<td><a href=\"img/" + i + ".PNG\"><img alt=\"No Screenshot\" src=\"img/" + i + ".PNG\"/></a></td>");
                    } catch (Exception var16) {
                        paramPrintWriter.println("<td></td>");
                    }

                    paramPrintWriter.print("</tr");
                    ++i;
                }
            }
            paramPrintWriter.print("\n                  </table>  \n");
        }
        if (paramITestResult.getParameters().length > 0 || paramITestResult.getStatus() == 2) {
            paramPrintWriter.print("<div class=\"chartStyle summary\" style=\"color: black;width: 98%; height: 100%; padding-bottom: 30px;\"> \n");
            if (paramITestResult.getParameters().length > 0) {
                paramPrintWriter.print("<b>Parameters:</b><br/>");
                Object[] var19 = paramITestResult.getParameters();
                int var21 = var19.length;

                for (int var3 = 0; var3 < var21; ++var3) {
                    Object localObject23 = var19[var3];
                    paramPrintWriter.print("Param: " + localObject23.toString() + "<br/>");
                }
            }

            if (paramITestResult.getStatus() == 3) {
                paramPrintWriter.print("<br/><br/>");
                paramPrintWriter.println("<b>Reason for Skipping:</b><br/>\n");
                String[] localObject11 = paramITestResult.getMethod().getGroupsDependedUpon();
                String[] arrayOfString = paramITestResult.getMethod().getMethodsDependedUpon();
                String[] var11;
                int var12;
                int var13;
                String newstr;
                String str1;
                if (localObject11.length > 0) {
                    str1 = "";
                    var11 = localObject11;
                    var12 = localObject11.length;

                    for (var13 = 0; var13 < var12; ++var13) {
                        newstr = var11[var13];
                        str1 = str1 + newstr + "<br/>";
                    }

                    paramPrintWriter.print("<b>Depends on Groups: </b><br/>" + str1);
                }
                if (arrayOfString.length > 0) {
                    str1 = "";
                    var11 = arrayOfString;
                    var12 = arrayOfString.length;

                    for (var13 = 0; var13 < var12; ++var13) {
                        newstr = var11[var13];
                        str1 = str1 + newstr + "<br/>";
                    }

                    paramPrintWriter.print("<b>Depends on Methods: </b><br/>" + str1 + "<br/><br/>");
                    paramPrintWriter.print("        </div>");
                }

                if (paramITestResult.getStatus() == 2) {
                    paramPrintWriter.println("        <br/><br/><b>Reason for Failure:&nbsp;&nbsp;</b>" + getExceptionDetails(paramITestResult) + "<br/><br/>\n<b id=\"showmenu\">Click Me to Show/Hide the Full Stack Trace</b><div class=\"exception\">");

                    try {
                        paramITestResult.getThrowable().printStackTrace(paramPrintWriter);
                    } catch (Exception var15) {
                    }

                    paramPrintWriter.print("</div>");
                }

                paramPrintWriter.print("        </div>");
            }
        }
        paramPrintWriter.print("                  </div>\n\n             </td>\n                   </tr>");
    }

    public static String getLogDescription(LogAs paramLogAs, ITestResult paramITestResult) {
        switch (paramLogAs.ordinal()) {
            case 1:
                return "<img style=\"height:20px;width:20px;border:none\" alt=\"PASS\" src=\"../" + getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/logpass.png\" />";
            case 2:
                return "<img style=\"height:18px;width:18px;border:none\" alt=\"FAIL\" src=\"../" +
                        getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/logfail.png\" />";
            case 3:
                return "<img style=\"height:20px;width:20px;border:none\" alt=\"INFO\" src=\"../" +
                        getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/loginfo.png\" />";
            case 4:
                return "<img style=\"height:20px;width:20px;border:none\" alt=\"WARNING\" src=\"../" +
                        getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/logwarning.png\" />";
            default:
                return "<img style=\"height:18px;width:18px;border:none\" alt=\"FAIL\" src=\"../" +
                        getTestCaseHTMLPath(paramITestResult) + "/HTML_Design_Files/IMG/logfail.png\" />";
        }
    }

    public static String getSkippedDescription(ITestResult paramITestResult) {
        String[] arrayOfString1 = paramITestResult.getMethod().getGroupsDependedUpon();
        String[] arrayOfString2 = paramITestResult.getMethod().getMethodsDependedUpon();
        String str1 = "";
        String[] var4 = arrayOfString1;
        int var5 = arrayOfString1.length;

        int var6;
        for (var6 = 0; var6 < var5; ++var6) {
            String str2 = var4[var6];
            str1 = str1 + str2;
        }

        String str4 = "";
        String[] var10 = arrayOfString2;
        var6 = arrayOfString2.length;

        for (int var11 = 0; var11 < var6; ++var11) {
            String str3 = var10[var11];
            str4 = str4 + str3;
        }

        return "";
    }

    private static String getBrowserName(ITestResult paramITestResult) {
        try {
            return paramITestResult.getAttribute(Platform.BROWSER_NAME_PROP).toString();
        } catch (Exception var2) {
            return "";
        }
    }

    private static String getBrowserVersion(ITestResult paramITestResult) {
        try {
            return paramITestResult.getAttribute(Platform.BROWSER_VERSION_PROP).toString();
        } catch (Exception var2) {
            return "";
        }
    }

    private static String getColorBasedOnResult(ITestResult paramITestResult) {
        if (paramITestResult.getStatus() == 1) {
            return Colors.PASS.getColor();
        } else if (paramITestResult.getStatus() == 2) {
            return Colors.FAIL.getColor();
        } else {
            return paramITestResult.getStatus() == 3 ? Colors.SKIP.getColor() : Colors.PASS.getColor();
        }
    }

    private static String getResult(ITestResult paramITestResult) {
        if (paramITestResult.getStatus() == 1) {
            try {
                return paramITestResult.getAttribute("passedButFailed").equals("passedButFailed") ? "Failed" : "Passed";
            } catch (Exception var2) {
                return "Passed";
            }
        } else if (paramITestResult.getStatus() == 2) {
            return "Failed";
        } else {
            return paramITestResult.getStatus() == 3 ? "Skipped" : "Unknown";
        }
    }

    public static String getTestCaseHTMLPath(ITestResult paramITestResult) {
        String str = paramITestResult.getAttribute("reportDir").toString();
        str = str.substring(str.indexOf(Directory.RESULTSDir) + Directory.RESULTSDir.length() + 1);
        String[] arrayOfString = str.replace(Directory.SEP, "##@##@##").split("##@##@##");
        str = "";

        for (int i = 0; i < arrayOfString.length; ++i) {
            str = str + "../";
        }

        return str;
    }

    public static void menuLink(PrintWriter paramPrintWriter, ITestResult paramITestResult, int paramInt) {
        getTestCaseHTMLPath(paramITestResult);
        paramPrintWriter.println("\n <tr id=\"container\">\n <td id=\"menu\">\n <ul>\n");
        paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\"../" + getTestCaseHTMLPath(paramITestResult) + "index.html\" >Index</a></li><li style=\"padding-top: 4px;\"><a href=\"" + getTestCaseHTMLPath(paramITestResult) + "ConsolidatedPage.html\" >Consolidated Page</a></li>\n");

        if (paramInt == 1) {
            paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\"" + Directory.RUNName + paramInt + Directory.SEP + "CurrentRun.html\" >Run " + paramInt + " </a></li>\n");
        } else {
            for (int i = 1; i <= paramInt; ++i) {
                if (i == paramInt) {
                    paramPrintWriter.println("\n <li style=\"padding-top: 4px;padding-bottom: 4px;\"><a href=\"" + Directory.RUNName + i + Directory.SEP + "CurrentRun.html\" >Run " + i + " </a></li>\n");
                    break;
                }
                paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\"" + Directory.RUNName + i + Directory.SEP + "CurrentRun.html\" >Run " + i + " </a></li>\n");
            }
        }

        paramPrintWriter.println("\n </ul>\n </td>\n\n");
    }
}