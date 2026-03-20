package Reports.com.rp.reports.writers;

import Reports.com.rp.reports.ATUReports;
import Reports.com.rp.reports.enums.ReportLabels;
import Reports.com.rp.reports.utils.Attributes;
import Reports.com.rp.reports.utils.Directory;
import Reports.com.rp.reports.utils.Utils;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestNGMethod;

public class CurrentRunPageWriter extends ReportsPage {
    public CurrentRunPageWriter() {
    }

    public static void menuLink(PrintWriter paramPrintWriter, int paramInt) {
        paramPrintWriter.println("\n <td id=\"container\">\n <td id=\"menu\">\n <ul> \n");
        paramPrintWriter.println(" <li class=\"menuStyle\"><a href=\"../index.html\" style=\"padding-top: 4px;\"><a href=\"../ConsolidatedPage.html\">Consolidated Page</a></li>\n");
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

    public static String getExecutionTime(ITestResult paramITestResult) {
        long l = paramITestResult.getEndMillis() - paramITestResult.getStartMillis();
        if (l > 1000L) {
            l /= 1000L;
            return l + " Sec";
        } else {
            return l + " Milli Sec";
        }
    }

    public static String getExecutionTime(long paramLong1, long paramLong2) {
        long l = paramLong1 - paramLong2;
        if (l > 1000L) {
            l /= 1000L;
            return l + " Sec";
        } else {
            return l + " Milli Sec";
        }
    }

    public static void content(PrintWriter paramPrintWriter, List<ITestResult> paramList1, List<ITestResult> paramList2, List<ITestResult> paramList3, List<ITestResult> paramList4, List<ITestResult> paramList5, List<ITestResult> paramList6, int paramInt, long paramLong1, long paramLong2) {
        int i = paramList1.size() + paramList2.size() + paramList3.size();
        paramPrintWriter.println("<td id=\"content\">");

        paramPrintWriter.println("<div class=\"info\">\n" +
                "The following pie chart demonstrates the percentage of Passed, Failed and Skipped Test Cases<br/>\n" +
                getExecutionTime(paramLong1, paramLong2) + "<br/>\n" +
                "</div>\n<div class=\"info\"><b>Run Description</b><br/><br/>" + ATUReports.currentRunDescription + "</div>\n");

        paramPrintWriter.println("<div class=\"chartStyle summary\" style=\"width: 32%;background-color: #3B9C9C;\">\n" +
                "<table>\n" +
                "<tr><td>Date</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>\n" +
                "<tr><td>Scripts/TC</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>\n" +
                "<tr><td>Passed</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>\n" +
                "<tr><td>Failed</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>\n" +
                "<tr><td>Skipped</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>\n" +
                "<tr><td>Execution</td><td>" + Utils.getCurrentTime() + "</td></tr>\n" +
                "<tr><td>Total</td><td>" + i + "</td></tr>\n" +
                "<tr><td>" + paramList1.size() + "</td></tr>\n" +
                "<tr><td>" + paramList2.size() + "</td></tr>\n" +
                "<tr><td>" + paramList3.size() + "</td></tr>\n" +
                "</table>\n</div>");

        paramPrintWriter.println("<div class=\"chartStyle\" style=\"text-align: left;margin-left: 30px;float: left;width: 60%;\">\n" +
                "<div id=\"chart\" style=\"height:300px;color:position:relative;\"></div>\n");

        if (Directory.recordSuiteExecution) {
            paramPrintWriter.println("<p id=\"show\"><object id=\"video\" class=\"video\">" +
                    "<object classid=\"clsid:9BE31822-FDAD-461B-AD51-5E4E2A3C4C50\" codebase=\"http://downloads.videolan.org/pub/videolan/vlc/latest/win32/axvlc.cab\" width=\"400\" height=\"300\" id=\"vlc\" events=\"True\">" +
                    "<param name=\"Src\" value=\"Recording" + Directory.SEP +
                    "ATU_CompleteSuiteRecording.mov\"></param>  <param name=\"ShowDisplay\" value=\"True\"></param> <param name=\"AutoLoop\" value=\"no\"></param> <param name=\"AutoPlay\" value=\"no\"></param> <embed type=\"application/x-google-vlc-plugin\" name=\"vlcfirefox\" autoplay=\"no\" loop=\"no\" width=\"999\" height=\"100%\" target=\"Recording" + Directory.SEP + "ATU_CompleteSuiteRecording.mov\"></embed></object></div>");
        } else {
            paramPrintWriter.println("<p id=\"showmenu\">No Video Recording Available</p>");
        }

        paramPrintWriter.println("<div style=\"float:left; color: #585858; font-size: 14px;\"><select id=\"tcFilter\" class=\"filter\">\n" +
                "<option class=\"filterOption\" value=\"all\">All Methods</option>\n" +
                "<option class=\"filterOption\" value=\"test\">Test Methods</option>\n" +
                "<option class=\"filterOption\" value=\"pass\">Passed Test Cases</option>\n" +
                "<option class=\"filterOption\" value=\"fail\">Failed Test Cases</option>\n" +
                "<option class=\"filterOption\" value=\"skip\">Skipped Test Cases</option>\n" +
                "<option class=\"filterOption\" value=\"config\">Configuration Methods</option>\n" +
                "</select>Filter Methods &nbsp;</div>");

        paramPrintWriter.println("<div style=\"float:left; color: #585858; font-size: 14px;\"><select id=\"suiteFilter\" class=\"filter\">\n" +
                "<option class=\"filterOption\" value=\"all\">All Suites</option>\n");

        Iterator localIterator = Attributes.getSuiteNameMapperMap().keySet().iterator();

        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            paramPrintWriter.println("<option class=\"filterOption\" value=\"" + Attributes.getSuiteNameMapperMap().get(str) + "\">" + str + "</option>\n");
        }

        paramPrintWriter.println("</select>Filter Suites&nbsp;&nbsp;</div>");

        paramPrintWriter.println("\n<table id=\"tableStyle\" class=\"chartStyle\" style=\"height:50px; float: left\">\n" +
                "<tr>\n" +
                "<th>Class Name</th>\n" +
                "<th>Suite Name</th>\n" +
                "<th>Package</th>\n" +
                "<th>Test Case Name</th>\n" +
                "<th>Iteration</th>\n" +
                "<th>Time</th>\n" +
                "<th>Method Type</th>\n" +
                "<th style=\"width: 7%\">Status</th>\n" +
                "</tr>\n");

        writePassedData(paramPrintWriter, paramList1, paramInt);
        writeFailedData(paramPrintWriter, paramList2, paramInt);
        writeSkippedData(paramPrintWriter, paramList3, paramInt);
        writePassedData(paramPrintWriter, paramList4, paramInt);
        writeFailedData(paramPrintWriter, paramList5, paramInt);
        writeSkippedData(paramPrintWriter, paramList6, paramInt);

        paramPrintWriter.println("                          </table>\n                    </div>\n                </td>\n            </tr>");
    }

    private static void writePassedData(PrintWriter paramPrintWriter, List<ITestResult> paramList, int paramInt) {
        String str = "pass";
        ITestResult localITestResult;
        for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); paramPrintWriter.print(
                "<tr class=\"all " + str + "\">" +
                        "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getSuiteName(localITestResult) + "</a></td>\n" +
                        "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" +
                        "<td>" + getPackageName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getClassName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getMethodType(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getTestCaseName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getIteration(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getExecutionTime(localITestResult) + "</td>\n" +
                        "<td><img style=\"zoom: 80%; width: 25px\" src=\"./html/Design_Files/IMG/pass.png\"></td>\n" +
                        "</tr>\n")) {
            localITestResult = (ITestResult)localIterator.next();
            if (!localITestResult.getMethod().isTest()) {
                str = "config" + getSuiteNameMapper(localITestResult);
            }
        }
    }

    private static void writeFailedData(PrintWriter paramPrintWriter, List<ITestResult> paramList, int paramInt) {
        String str = "fail";
        ITestResult localITestResult;
        for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); paramPrintWriter.print(
                "<tr class=\"all " + str + "\">" +
                        "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getSuiteName(localITestResult) + "</a></td>\n" +
                        "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getPackageName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getClassName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getMethodType(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getTestCaseName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getIteration(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getExecutionTime(localITestResult) + "</td>\n" +
                        "<td><img style=\"zoom: 80%; width: 25px\" src=\"./html/Design_Files/IMG/fail.png\"></td>\n" +
                        "</tr>\n")) {
            localITestResult = (ITestResult)localIterator.next();
            if (!localITestResult.getMethod().isTest()) {
                str = "config" + getSuiteNameMapper(localITestResult);
            }
        }
    }

    private static void writeSkippedData(PrintWriter paramPrintWriter, List<ITestResult> paramList, int paramInt) {
        String str = "skip";
        ITestResult localITestResult;
        for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); paramPrintWriter.print(
                "<tr class=\"all " + str + "\">" +
                        "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getSuiteName(localITestResult) + "</a></td>\n" +
                        "<td><a href=\"" + getTestCaseHTMLPath(localITestResult, paramInt) + "\">" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getPackageName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getClassName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getMethodType(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getTestCaseName(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getIteration(localITestResult) + "</td>\n" + getTestCaseHTMLPath(localITestResult, paramInt) + "</a></td>\n" +
                        "<td>" + getExecutionTime(localITestResult) + "</td>\n" +
                        "<td><img style=\"zoom: 80%; width: 25px\" src=\"./html/Design_Files/IMG/skip.png\"></td>\n" +
                        "</tr>\n")) {
            localITestResult = (ITestResult)localIterator.next();
            if (!localITestResult.getMethod().isTest()) {
                str = "config" + getSuiteNameMapper(localITestResult);
            }
        }
    }

    public static String getSuiteName(ITestResult paramITestResult) {
        return paramITestResult.getTestContext().getSuite().getName();
    }

    public static String getSuiteNameMapper(ITestResult paramITestResult) {
        return Attributes.getSuiteNameMapper(paramITestResult.getTestContext().getSuite().getName());
    }

    public static String getTestCaseHTMLPath(ITestResult paramITestResult, int paramInt) {
        String str1 = paramITestResult.getAttribute("reportDirectorySuiteName").toString();
        int i = (Directory.RUNName + paramInt).length();
        String str2 = str1.substring(str1.indexOf(Directory.RUNName + paramInt) + i + 1);
        return str2 + Directory.SEP + getTestCaseName(paramITestResult) + ".html";
    }

    public static String getPackageName(ITestResult paramITestResult) {
        try {
            return paramITestResult.getTestClass().getRealClass().getPackage().getName();
        } catch (NullPointerException var2) {
            return "";
        }
    }

    public static String getClassName(ITestResult paramITestResult) {
        return paramITestResult.getTestClass().getRealClass().getSimpleName();
    }

    public static String getIteration(ITestResult paramITestResult) {
        return paramITestResult.getAttribute("iteration").toString();
    }

    public static String getTestCaseName(ITestResult paramITestResult) {
        return paramITestResult.getName();
    }

    public static String getMethodType(ITestResult paramITestResult) {
        ITestNGMethod localTestNGMethod = paramITestResult.getMethod();
        if (localTestNGMethod.isAfterClassConfiguration()) {
            return "After Class";
        } else if (localTestNGMethod.isAfterGroupsConfiguration()) {
            return "After Groups";
        } else if (localTestNGMethod.isAfterMethodConfiguration()) {
            return "After Method";
        } else if (localTestNGMethod.isAfterSuiteConfiguration()) {
            return "After Suite";
        } else if (localTestNGMethod.isAfterTestConfiguration()) {
            return "After Test";
        } else if (localTestNGMethod.isBeforeClassConfiguration()) {
            return "Before Class";
        } else if (localTestNGMethod.isBeforeGroupsConfiguration()) {
            return "Before Groups";
        } else if (localTestNGMethod.isBeforeMethodConfiguration()) {
            return "Before Method";
        } else if (localTestNGMethod.isBeforeSuiteConfiguration()) {
            return "Before Suite";
        } else if (localTestNGMethod.isBeforeTestConfiguration()) {
            return "Before Test";
        } else {
            return localTestNGMethod.isTest() ? "Test Method" : "Unknown";
        }
    }

    public static void header(PrintWriter paramPrintWriter) {
        paramPrintWriter.println("<!DOCTYPE html>\n <head>\n <title>Current Run Report</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../HTML Design Files/CSS/design.css\" />\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../../HTML Design Files/CSS/jquery.jqplot.css\" />\n" +
                "<script type=\"text/javascript\" src=\"../../HTML Design Files/JS/jquery.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../../HTML Design Files/JS/jquery.jqplot.min.js\"></script>\n" +
                "<!--[if lt IE 9]>\n" +
                "<script language=\"javascript\" type=\"text/javascript\" src=\"../../HTML Design Files/JS/excanvas.js\"></script>\n" +
                "<![endif]-->\n" +
                "<script language=\"javascript\" type=\"text/javascript\" src=\"pieChart.js\"></script>\n</head>\n");

        paramPrintWriter.print("<script language=\"javascript\" type=\"text/javascript\">" +
                "$(document).ready(function() {" +
                "$(\".video\").hide();" +
                "$(\"#showmenu\").click(function() {" +
                "$(\".video\").toggle(\"slide\"); }); });</script>" +
                "<style>#showmenu{text-align:center; padding-top:350px;color: #585858; font-size: 14px; width: 97%; border-style: solid; border-width: 1px; border-color: #21ABCD; margin-top: 5px;}" +
                "#video{height: 550px; -moz-box-shadow: 0 0 10px #CCCCCC; -webkit-box-shadow: 0 0 10px #CCCCCC; box-shadow: 0 0 10px #CCCCCC; zoom: 1;" +
                "filter: progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=90)," +
                "progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=0)," +
                "progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=180)," +
                "progid:DXImageTransform.Microsoft.Shadow(Color=#cccccc, Strength=2, Direction=270);" +
                "background-color: white;}</style>");

        paramPrintWriter.println("<script language=\"javascript\" type=\"text/javascript\">\n" +
                "$(document).ready(function() {\n" +
                "\t$(\"#tcFilter\").on('change', function() {\n" +
                "\t\tif($(this).val()=='pass'){\n\t\t\t$('.pass').show();\n\t\t\t$('.fail').hide();\n\t\t\t$('.skip').hide();\n\t\t\t$('.config').hide();\n\t\t}\n" +
                "\t\telse if($(this).val()=='fail'){\n\t\t\t$('.pass').hide();\n\t\t\t$('.fail').show();\n\t\t\t$('.skip').hide();\n\t\t\t$('.config').hide();\n\t\t}\n" +
                "\t\telse if($(this).val()=='skip'){\n\t\t\t$('.pass').hide();\n\t\t\t$('.fail').hide();\n\t\t\t$('.skip').show();\n\t\t\t$('.config').hide();\n\t\t}\n" +
                "\t\telse if($(this).val()=='config'){\n\t\t\t$('.pass').hide();\n\t\t\t$('.fail').hide();\n\t\t\t$('.skip').hide();\n\t\t\t$('.config').show();\n\t\t}\n" +
                "\t\telse if($(this).val()=='all'){\n\t\t\t$('.all').show();\n\t\t}\n\t});\n</script>");

        paramPrintWriter.print("<script language=\"javascript\" type=\"text/javascript\">" +
                "$(document).ready(function() {\n" +
                "\t$(\"#tcFilter\").on('change', function() {\n" +
                "\t\tif($(this).val()=='all') {\n\t\t\t$('.all').show();\n\t\t}\n\t});\n");

        Iterator localIterator = Attributes.getSuiteNameMapperMap().keySet().iterator();
        while(localIterator.hasNext()) {
            String str = (String)localIterator.next();
            paramPrintWriter.print("if($(this).val()=='" + Attributes.getSuiteNameMapperMap().get(str) + "') {" +
                    " $('.all').hide();" +
                    "$('." + Attributes.getSuiteNameMapperMap().get(str) + "').show(); }");
        }

        paramPrintWriter.println(" }); });</script>");
        paramPrintWriter.println(" </head>\n <body>\n <table id=\"mainTable\">\n <tr id=\"header\" >\n" +
                "<td id=\"logo\"><img src=\"../../HTML_Design_Files/IMG/" + ReportLabels.ATU_LOGO.getLabel() +
                "\" alt=\"Logo\" height=\"70\" width=\"140\" /><br/>" + ReportLabels.ATU_CAPTION.getLabel() + "</td>\n" +
                "<td id=\"headertext\">\n" +
                "<div style=\"padding-right:20px;float:right;\"><img src=\"../../HTML_Design_Files/IMG/" +
                ReportLabels.PROJ_LOGO.getLabel() + "\" height=\"70\" width=\"140\" /></div></td>\n</tr>");
    }
}