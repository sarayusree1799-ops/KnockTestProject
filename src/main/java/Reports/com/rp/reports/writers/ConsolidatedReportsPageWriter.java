package Reports.com.rp.reports.writers;

import Reports.com.rp.reports.enums.ReportLabels;
import Reports.com.rp.reports.utils.Directory;

import java.io.PrintWriter;

public class ConsolidatedReportsPageWriter extends ReportsPage {
    public ConsolidatedReportsPageWriter() {
    }

    public static void menuLink(PrintWriter paramPrintWriter, int paramInt) {
        paramPrintWriter.println("\n <tr id=\"container\">\n <td id=\"menu\">\n <ul> \n");
        paramPrintWriter.println(" <li class=\"menuStyle\"><a href=\"../index.html\" >Index</a></li>\n");
        if (paramInt == 1) {
            paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\""
                    + Directory.RUNName + paramInt + Directory.SEP + "CurrentRun.html\" >Run "
                    + paramInt + " </a></li>\n");
        } else {
            for(int i = 1; i <= paramInt; ++i) {
                if (i == paramInt) {
                    paramPrintWriter.println("\n <li style=\"padding-top: 4px;padding-bottom: 4px;\"><a href=\""
                            + Directory.RUNName + i + Directory.SEP + "CurrentRun.html\">Run "
                            + i + " </a></li>\n");
                    break;
                }
                paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\""
                        + Directory.RUNName + i + Directory.SEP + "CurrentRun.html\" >Run "
                        + i + " </a></li>\n");
            }
        }
        paramPrintWriter.println("\n </ul>\n </td>\n\n");
    }

    public static void header(PrintWriter paramPrintWriter) {
        paramPrintWriter.println("<!DOCTYPE html>\n<html>\n<head>\n<title>Execution Summary</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../HTML_Design_Files/CSS/design.css\">\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../HTML_Design_Files/CSS/jquery.jqplot.css\">\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jquery.min.js\"></script>\n" +
                "<!--[if lt IE 9]>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/excanvas.js\"></script>\n" +
                "<![endif]-->\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jquery.jqplot.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.barRenderer.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.categoryAxisRenderer.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.pointLabels.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.highlighter.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jqplot.cursor.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/barChart.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"../HTML_Design_Files/JS/jquery-ui.min.js\"></script>\n" +
                "$(function() {\n" +
                "$(\"#tabs\").tabs();\n\n" +
                "plot1.drawCount == 0) {\n" +
                "plot1.replot();\n" +
                "}\n" +
                "plot2.drawCount == 0) {\n" +
                "plot2.replot();\n" +
                "}\n" +
                "$(\"#tabs\").bind(\"tabsshow\", function(event, ui) {\n" +
                "if (ui.index === 1 && plot1.drawCount == 0) {\n" +
                "plot1.replot();\n" +
                "} else if (ui.index == 2 && plot2.drawCount == 0) {\n" +
                "plot2.replot();\n" +
                "}\n" +
                "});\n" +
                "$(document).ready(function() {\n" +
                "});\n" +
                "</script>\n</head>\n<body>\n" +
                "<table id=\"mainTable\">\n<tr id=\"header\">\n" +
                "<td id=\"logo\"><img src=\"../HTML_Design_Files/IMG/" + ReportLabels.ATU_LOGO.getLabel() + "\" alt=\"logo\" height=\"70\" width=\"140\" /> <br/>" +
                "</td>\n<td id=\"headertext\">\n" +
                "<div style=\"padding-right:20px;float:right;\"><img src=\"../HTML_Design_Files/IMG/" + ReportLabels.PROJ_LOGO.getLabel() + "\" height=\"70\" width=\"140\" /></div>\n" +
                "</td>\n</tr>");
    }

    public static void content(PrintWriter paramPrintWriter) {
        paramPrintWriter.println("<td id=\"content\">\n\n" +
                "<div id=\"tabs\">\n" +
                "<ul>\n" +
                "<li><a href=\"#tabs-1\">Line Chart</a></li>\n" +
                "<li><a href=\"#tabs-2\">Bar Chart</a></li>\n" +
                "</ul>\n" +
                "<div id=\"tabs-1\" style=\"text-align: left;\">\n" +
                "<p class=\"info\" style=\"text-align: center;color: black;font-size: 14px\">\n" +
                "The following Line chart demonstrates the number of Passed, Failed and Skipped Test Cases\n" +
                "in last 10 Runs\n" +
                "</p>\n" +
                "<div id=\"line\" style=\"height: 270px; width: 85%; margin-top: 20px;color:black;\"></div>\n" +
                "</div>\n" +
                "<div id=\"tabs-2\" style=\"text-align: left;\">\n" +
                "<p class=\"info\" style=\"text-align: center;color: black;font-size: 14px\">\n" +
                "The following Bar chart demonstrates the number of Passed, Failed and Skipped Test Cases\n" +
                "in last 10 Runs\n" +
                "</p>\n" +
                "<div id=\"bar\" style=\"margin-top:20px; margin-left:20px; width:85%; height:300px;color:black;\"></div>\n" +
                "</div>\n" +
                "</td>\n");
    }
}
