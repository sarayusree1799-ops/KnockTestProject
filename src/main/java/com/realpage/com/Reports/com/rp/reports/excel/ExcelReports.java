package com.realpage.com.Reports.com.rp.reports.excel;

import com.realpage.com.Reports.com.rp.reports.chart.PieChart;
import com.realpage.com.Reports.com.rp.reports.utils.Directory;
import com.realpage.com.Reports.com.rp.reports.utils.Steps;
import com.realpage.com.Reports.com.rp.reports.writers.CurrentRunPageWriter;

import org.testng.ITestResult;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.JFreeChart;

public class ExcelReports {
    public static final String TEST_ID_PREFIX = "ATU_TC_";

    public static int atuTC_ID = 1;

    public ExcelReports() {

    }

    public static void generateExcelReport(String paramString, List<ITestResult> paramList1, List<ITestResult> paramList2, List<ITestResult> paramList3) {
        FileOutputStream localFileOutputStream = null;
        XSSFWorkbook localXSSFWorkbook = null;
        Sheet localSheet = null;

        try {
            localFileOutputStream = new FileOutputStream(paramString);
            localXSSFWorkbook = new XSSFWorkbook();
            testSummary(localXSSFWorkbook, (Sheet) localSheet, paramList1, paramList2, paramList3);
            testCaseList(localXSSFWorkbook, (Sheet) localSheet, paramList1, paramList2, paramList3);
            testStepDescription(localXSSFWorkbook, (Sheet) localSheet, paramList1);
            testStepDescription(localXSSFWorkbook, (Sheet) localSheet, paramList2);
            localXSSFWorkbook.write(localFileOutputStream);
            localFileOutputStream.close();
        } catch (IOException var14) {
            try {
                localFileOutputStream.close();
            } catch (IOException var13) {
            }
        } finally {
            localSheet = null;
            localXSSFWorkbook = null;
            localFileOutputStream = null;
        }
    }

    private static void addPieChartToExcel(Workbook paramWorkbook, Sheet paramSheet, List<ITestResult> paramList1, List<ITestResult> paramList2, List<ITestResult> paramList3) {
        byte[] arrayOfByte = ExcelReports.ExcelChart.writeChartToExcel(paramList1.size(), paramList2.size(), paramList3.size());
        int i = paramWorkbook.addPicture(arrayOfByte, 6);
        CreationHelper localCreationHelper = paramWorkbook.getCreationHelper();
        Drawing localDrawing = paramSheet.createDrawingPatriarch();
        ClientAnchor localClientAnchor = localCreationHelper.createClientAnchor();
        localClientAnchor.setCol1(0);
        localClientAnchor.setRow1(6);
        Picture localPicture = localDrawing.createPicture(localClientAnchor, i);
        localPicture.resize();
    }

    private static void testSummary(Workbook paramWorkbook, Sheet paramSheet, List<ITestResult> paramList1, List<ITestResult> paramList2, List<ITestResult> paramList3) {
        paramSheet = paramWorkbook.createSheet("TestSummary");
        Row localRow = paramSheet.createRow(0);
        localRow.setHeight((short) 420);
        Cell localCell = localRow.createCell(0);
        localCell.setCellValue("Total Scripts/TC");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        localCell = localRow.createCell(1);
        localCell.setCellValue((double) (paramList1.size() + paramList2.size() + paramList3.size()));
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        localRow = paramSheet.createRow(1);
        localCell = localRow.createCell(0);
        localCell.setCellValue("Test Cases Passed");
        ExcelStyler.setResultCellStyle(paramWorkbook, localCell, 1);
        localCell = localRow.createCell(1);
        localCell.setCellValue((double) paramList1.size());
        ExcelStyler.setResultCellStyle(paramWorkbook, localCell, 1);
        localRow = paramSheet.createRow(2);
        localCell = localRow.createCell(0);
        localCell.setCellValue("Test Cases Failed");
        ExcelStyler.setResultCellStyle(paramWorkbook, localCell, 2);
        localCell = localRow.createCell(1);
        localCell.setCellValue((double) paramList2.size());
        ExcelStyler.setResultCellStyle(paramWorkbook, localCell, 2);

        localRow = paramSheet.createRow(3);
        localCell = localRow.createCell(0);
        localCell.setCellValue("Test Cases Skipped");
        ExcelStyler.setResultCellStyle(paramWorkbook, localCell, 3);

        localCell = localRow.createCell(1);
        localCell.setCellValue((double) paramList3.size());
        ExcelStyler.setResultCellStyle(paramWorkbook, localCell, 3);

        addPieChartToExcel(paramWorkbook, paramSheet, paramList1, paramList2, paramList3);
        paramSheet.autoSizeColumn(0);
    }

    private static void testCaseList(Workbook paramWorkbook, Sheet paramSheet, List<ITestResult> paramList1, List<ITestResult> paramList2, List<ITestResult> paramList3) {
        paramSheet = paramWorkbook.createSheet("TestSuite");
        Row localRow1 = paramSheet.createRow(0);
        localRow1.setHeight((short) 420);
        Cell localCell = localRow1.createCell(0);
        localCell.setCellValue("S.No");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);

        localCell = localRow1.createCell(1);
        localCell.setCellValue("Package");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        paramSheet.autoSizeColumn(1);

        localCell = localRow1.createCell(2);
        localCell.setCellValue("ClassName");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        paramSheet.autoSizeColumn(2);

        localCell = localRow1.createCell(3);
        localCell.setCellValue("TestCase Name");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        paramSheet.autoSizeColumn(3);

        localCell = localRow1.createCell(4);
        localCell.setCellValue("Browser");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        paramSheet.autoSizeColumn(4);

        localCell = localRow1.createCell(5);
        localCell.setCellValue("Iteration");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        paramSheet.autoSizeColumn(5);

        localCell = localRow1.createCell(6);
        localCell.setCellValue("Time Taken");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        paramSheet.autoSizeColumn(6);

        localCell = localRow1.createCell(7);
        localCell.setCellValue("Result");
        ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);

        localCell = localRow1.createCell(8);
        localCell.setCellValue("ATU_TestCase_ID");
        CellStyle localCellStyle = ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
        localCellStyle.setBorderRight((short) 1);
        localCellStyle.setRightBorderColor(ExcelStyler.HEADER_BG_COLOR);
        localCell.setCellStyle(localCellStyle);
        paramSheet.autoSizeColumn(7);
        int i = 1;
        i = writeSummaryData(paramWorkbook, paramSheet, paramList1, i);
        i = writeSummaryData(paramWorkbook, paramSheet, paramList2, i);
        i = writeSummaryData(paramWorkbook, paramSheet, paramList3, i);
        Row localRow2 = paramSheet.getRow(i - 1);
        ExcelStyler.setBorderLine(paramWorkbook, localRow2);
    }

    private static int writeSummaryData(Workbook paramWorkbook, Sheet paramSheet, List<ITestResult> paramList, int paramInt) {
        paramList.size();

        for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ++paramInt) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            Row localRow = paramSheet.createRow(paramInt);
            Cell localCell = localRow.createCell(0);
            localCell.setCellValue((double) paramInt);
            setHyperLink(localITestResult, paramWorkbook, localCell);

            localCell = localRow.createCell(1);
            localCell.setCellValue(CurrentRunPageWriter.getPackageName(localITestResult));
            setHyperLink(localITestResult, paramWorkbook, localCell);

            localCell = localRow.createCell(2);
            localCell.setCellValue(CurrentRunPageWriter.getClassName(localITestResult));
            setHyperLink(localITestResult, paramWorkbook, localCell);

            localCell = localRow.createCell(3);
            localCell.setCellValue(CurrentRunPageWriter.getTestCaseName(localITestResult));
            setHyperLink(localITestResult, paramWorkbook, localCell);

            localCell = localRow.createCell(4);
            setHyperLink(localITestResult, paramWorkbook, localCell);

            localCell = localRow.createCell(5);
            localCell.setCellValue(CurrentRunPageWriter.getIteration(localITestResult));
            setHyperLink(localITestResult, paramWorkbook, localCell);

            localCell = localRow.createCell(6);
            localCell.setCellValue(CurrentRunPageWriter.getExecutionTime(localITestResult));
            localCell = localRow.createCell(7);
            ExcelStyler.setResultCellStyle(paramWorkbook, localCell, localITestResult);
            setHyperLink(localITestResult, paramWorkbook, localCell);

            localCell = localRow.createCell(8);
            localCell.setCellValue("ATU_TC_" + atuTC_ID);
            setHyperLink(localITestResult, paramWorkbook, localCell);

            CellStyle localCellStyle = paramWorkbook.createCellStyle();
            localCellStyle.setBorderRight((short) 1);
            localCellStyle.setRightBorderColor(ExcelStyler.HEADER_BG_COLOR);
            localCell.setCellStyle(localCellStyle);

            ++atuTC_ID;
        }

        return paramInt;
    }

    private static void setHyperLink(ITestResult paramITestResult, Workbook paramWorkbook, Cell paramCell) {
        if (paramITestResult.getStatus() != 3) {
            CreationHelper localCreationHelper = paramWorkbook.getCreationHelper();
            Hyperlink localHyperlink = localCreationHelper.createHyperlink(2);
            localHyperlink.setAddress("ATU_TC_" + atuTC_ID + "'!A1");
            paramITestResult.setAttribute("testID", atuTC_ID);
            paramCell.setHyperlink(localHyperlink);
        }
    }

    private static void setHyperLink(String paramString, Workbook paramWorkbook, Cell paramCell) {
        CreationHelper localCreationHelper = paramWorkbook.getCreationHelper();
        Hyperlink localHyperlink = localCreationHelper.createHyperlink(2);
        localHyperlink.setAddress("'" + paramString + "'!A1");
        paramCell.setHyperlink(localHyperlink);
    }

    private static void testStepDescription(Workbook paramWorkbook, Sheet paramSheet, List<ITestResult> paramList) {
        Cell localCell;
        int i = 0;
        int j = 0;
        for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); localCell = paramSheet.getRow(i).getCell(j - 1)) {
            ITestResult localITestResult = (ITestResult) localIterator.next();
            paramSheet = paramWorkbook.createSheet("ATU_TC_" + localITestResult.getAttribute("testID").toString());
            ExcelStyler.setSheetTabColor(paramSheet, localITestResult);
            Row localRow1 = paramSheet.createRow(0);
            localRow1.setHeight((short) 420);
            localCell = localRow1.createCell(0);
            localCell.setCellValue("S.No");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
            localCell = localRow1.createCell(1);
            localCell.setCellValue("Step Description");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
            paramSheet.autoSizeColumn(1);
            localCell = localRow1.createCell(2);
            localCell.setCellValue("Input Value");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
            paramSheet.autoSizeColumn(2);

            localCell = localRow1.createCell(3);
            localCell.setCellValue("Expected value");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
            paramSheet.autoSizeColumn(3);

            localCell = localRow1.createCell(4);
            localCell.setCellValue("Actual Value");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
            paramSheet.autoSizeColumn(4);

            localCell = localRow1.createCell(5);
            localCell.setCellValue("Time Taken");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
            paramSheet.autoSizeColumn(5);

            localCell = localRow1.createCell(6);
            localCell.setCellValue("Line No");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);

            localCell = localRow1.createCell(7);
            localCell.setCellValue("Screenshot");
            ExcelStyler.setHeaderCellStyle(paramWorkbook, localCell);
            paramSheet.autoSizeColumn(7);

            List localList = Reporter.getOutput(localITestResult);
            i = 1;
            Object localObject = localList.iterator();

            while (((Iterator) localObject).hasNext()) {
                String str1 = (String) ((Iterator) localObject).next();
                Steps localSteps = (Steps) localITestResult.getAttribute(str1);
                Row localRow2 = paramSheet.createRow(i);
                if (localSteps == null) {
                    localCell = localRow2.createCell(0);
                    localCell.setCellValue(str1);
                } else {
                    localCell = localRow2.createCell(0);
                    localCell.setCellValue((double) i);

                    localCell = localRow2.createCell(1);
                    localCell.setCellValue(localSteps.getDescription());

                    localCell = localRow2.createCell(2);
                    localCell.setCellValue(localSteps.getInputValue());

                    localCell = localRow2.createCell(3);
                    localCell.setCellValue(localSteps.getExpectedValue());

                    localCell = localRow2.createCell(4);
                    localCell.setCellValue(localSteps.getActualValue());

                    localCell = localRow2.createCell(5);
                    localCell.setCellValue(localSteps.getTime());

                    localCell = localRow2.createCell(6);
                    localCell.setCellValue(localSteps.getLineNum());

                    localCell = localRow2.createCell(7);
                }
                try {
                    Integer.parseInt(localSteps.getScreenShot().trim());
                    CreationHelper localCreationHelper = paramWorkbook.getCreationHelper();
                    Hyperlink localHyperlink = localCreationHelper.createHyperlink(Hyperlink.LINK_FILE);
                    String str2 = localITestResult.getAttribute("relativeReportDir") + Directory.SEP + Directory.SCREENSHOT_DIRName;
                    str2 = str2.replace(" ", "%20").replace(Directory.SEP, "/");
                    str2 = str2 + "/" + i + ".PNG";
                    localHyperlink.setAddress(str2);
                    localCell.setCellValue("Screenshot");
                    localCell.setHyperlink(localHyperlink);
                } catch (Exception var16) {
                }

                ++i;
            }
        }
        Object localObject = paramSheet.createRow(i);
        localCell = ((Row) localObject).createCell(0);
        localCell.setCellValue("Go To TestSuite Sheet");
        setHyperLink("TestSuite", paramWorkbook, localCell);

        localCell = ((Row) localObject).createCell(1);
        localCell = ((Row) localObject).createCell(2);
        localCell = ((Row) localObject).createCell(3);
        localCell = ((Row) localObject).createCell(4);
        localCell = ((Row) localObject).createCell(5);
        localCell = ((Row) localObject).createCell(6);
        localCell = ((Row) localObject).createCell(7);

        j = paramSheet.addMergedRegion(new CellRangeAddress(i, i, 0, 7));
    }

    public static class ExcelChart {

        public ExcelChart() {
        }

        public static byte[] writeChartToByteStream(JFreeChart paramJFreeChart) {
            BufferedImage localBufferedImage = paramJFreeChart.createBufferedImage(450, 450);
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(localBufferedImage, "PNG", localByteArrayOutputStream);
            } catch (IOException var5) {
                var5.printStackTrace();
            }
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            return arrayOfByte;
        }

        public static byte[] writeChartToExcel(int paramInt1, int paramInt2, int paramInt3) {
            return writeChartToByteStream(PieChart.generate2DPieChart(paramInt1, paramInt2, paramInt3));
        }
    }
}