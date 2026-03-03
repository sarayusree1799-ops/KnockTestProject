package com.realpage.com.Reports.com.rp.reports.writers;

import com.realpage.com.Reports.com.rp.reports.enums.Colors;
import com.realpage.com.Reports.com.rp.reports.enums.ReportLabels;
import com.realpage.com.Reports.com.rp.reports.utils.Directory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class HTMLDesignFilesJSWriter {

    public static int TICK_INTERVAL = 1;
    public HTMLDesignFilesJSWriter() {
    }

    private static String reduceData(String paramString, int paramInt) {
        int i = 0;
        for (int j = 0; j < paramString.length(); ++j) {
            if (paramString.charAt(j) == ',') {
                ++i;
                if (i == paramInt) {
                    paramString = paramString.substring(j + 1, paramString.length());
                }
            }
        }
        return paramString;
    }

    public static void pieChartJS(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        try {
            PrintWriter localPrintWriter = new PrintWriter(
                    Directory.RESULTSDir + Directory.SEP + Directory.RUNName + paramInt4 + Directory.SEP + "pieChart.js"
            );

            localPrintWriter.println(
                    "$(document).ready(function(){ \n" +
                            " var data = [[" + ReportLabels.PASS.getLabel() + "," + paramInt1 + "],[" +
                            ReportLabels.FAIL.getLabel() + "," + paramInt2 + "],[" +
                            ReportLabels.SKIP.getLabel() + "," + paramInt3 + "]];\n" +
                            " jQuery.jqplot('chart', [data], {\n" +
                            " seriesColors: ['" + Colors.PASS.getColor() + "','" +
                            Colors.FAIL.getColor() + "','" +
                            Colors.SKIP.getColor() + "'],\n" +
                            " seriesDefaults: {\n" +
                            "   renderer: jQuery.jqplot.PieRenderer,\n" +
                            "   rendererOptions: {\n" +
                            "     padding: 15,\n" +
                            "     sliceMargin: 1,\n" +
                            "     showDataLabels: true\n" +
                            "   }\n" +
                            " },\n" +
                            " grid: { borderColor: '#cccccc', background: '#ffffff', shadow: false },\n" +
                            " legend: { show: true, location: 'e' }\n" +
                            " });\n" +
                            "});"
            );

            localPrintWriter.close();
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        }
    }

    public static void lineChartJS(String paramString1, String paramString2,
                                   String paramString3, int paramInt) {

        paramString1 = paramString1.substring(0, paramString1.lastIndexOf(59))
                .replace(';', ',').trim();
        paramString2 = paramString2.substring(0, paramString2.lastIndexOf(59))
                .replace(';', ',').trim();
        paramString3 = paramString3.substring(0, paramString3.lastIndexOf(59))
                .replace(';', ',').trim();

        try {
            PrintWriter localPrintWriter = new PrintWriter(
                    Directory.RESULTSDir + Directory.SEP + "lineChart.js"
            );

            localPrintWriter.println(
                    "$(document).ready(function(){\n" +
                            " var line1 = [" + paramString1 + "];\n" +
                            " var line2 = [" + paramString2 + "];\n" +
                            " var line3 = [" + paramString3 + "];\n"
            );

            localPrintWriter.print("var ticks = [");

            int i = 1;
            if (paramInt == 1) {
                i = 0;
            }

            for (int j = i; j <= paramInt; j++) {
                localPrintWriter.print(j);
                if (j != paramInt) {
                    localPrintWriter.print(",");
                }
            }

            localPrintWriter.print("];");

            localPrintWriter.println(
                    "$.jqplot('line', [line1, line2, line3], {\n" +
                            " animate: true,\n" +
                            " axesDefaults: { min: 0, tickInterval: 1 },\n" +
                            " series: [\n" +
                            "   { lineWidth: 1.5, label: '" + ReportLabels.PASS.getLabel() + "' },\n" +
                            "   { lineWidth: 1.5, label: '" + ReportLabels.SKIP.getLabel() + "' },\n" +
                            "   { lineWidth: 1.5, label: '" + ReportLabels.FAIL.getLabel() + "' }\n" +
                            " ],\n" +
                            " axes: {\n" +
                            "   xaxis: { ticks: ticks, label: '" + ReportLabels.X_AXIS.getLabel() + "' },\n" +
                            "   yaxis: { label: '" + ReportLabels.Y_AXIS.getLabel() + "', tickOptions: { formatString: '%d Tc' } }\n" +
                            " },\n" +
                            " legend: { show: true, placement: 'outside', location: 'e' },\n" +
                            " grid: { background: '#ffffff', drawGridlines: true, gridLineColor: '#cccccc', borderColor: '#cccccc' }\n" +
                            "});"
            );

            localPrintWriter.close();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        }
    }

    public static void barChartJS(String paramString1, String paramString2,
                                  String paramString3, int paramInt) {

        paramString1 = paramString1.substring(0, paramString1.lastIndexOf(59))
                .replace(';', ',').trim();
        paramString2 = paramString2.substring(0, paramString2.lastIndexOf(59))
                .replace(';', ',').trim();
        paramString3 = paramString3.substring(0, paramString3.lastIndexOf(59))
                .replace(';', ',').trim();

        int i = 0;
        if (paramInt > 10) {
            i = paramInt - 10;
            paramString1 = reduceData(paramString1, i);
            paramString2 = reduceData(paramString2, i);
            paramString3 = reduceData(paramString3, i);
        }

        try {
            PrintWriter localPrintWriter = new PrintWriter(
                    Directory.RESULTSDir + Directory.SEP + "barChart.js"
            );

            localPrintWriter.println(
                    "$(document).ready(function(){\n" +
                            " var s1 = [" + paramString1 + "];\n" +
                            " var s2 = [" + paramString2 + "];\n" +
                            " var s3 = [" + paramString3 + "];\n"
            );

            localPrintWriter.print("var ticks = [");
            for (int j = i + 1; j <= paramInt; j++) {
                localPrintWriter.print(j);
                if (j != paramInt) {
                    localPrintWriter.print(",");
                }
            }
            localPrintWriter.print("];");

            localPrintWriter.println(
                    "$.jqplot('bar', [s1, s2, s3], {\n" +
                            " animate: true,\n" +
                            " axesDefaults: { min: 0, tickInterval: 1 },\n" +
                            " seriesColors: ['" + Colors.PASS.getColor() + "','" +
                            Colors.FAIL.getColor() + "','" +
                            Colors.SKIP.getColor() + "'],\n" +
                            " seriesDefaults: {\n" +
                            "   renderer: $.jqplot.BarRenderer,\n" +
                            "   rendererOptions: { barWidth: 12, barMargin: 25, fillToZero: true }\n" +
                            " },\n" +
                            " grid: { borderColor: '#ffffff', background: '#ffffff', shadow: false },\n" +
                            " legend: { show: true, placement: 'outside', location: 'e' },\n" +
                            " labels: ['" + ReportLabels.PASS.getLabel() + "','" +
                            ReportLabels.FAIL.getLabel() + "','" +
                            ReportLabels.SKIP.getLabel() + "'],\n" +
                            " axes: {\n" +
                            "   xaxis: { renderer: $.jqplot.CategoryAxisRenderer, ticks: ticks, label: '" +
                            ReportLabels.X_AXIS.getLabel() + "' },\n" +
                            "   yaxis: { label: '" + ReportLabels.Y_AXIS.getLabel() + "', tickOptions: { formatString: '%d' } }\n" +
                            " }\n" +
                            "});"
            );

            localPrintWriter.close();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        }
    }
}
