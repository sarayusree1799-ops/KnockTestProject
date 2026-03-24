package FWReports.com.rp.reports.excel;

import FWReports.com.rp.reports.enums.ReportLabels;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.ITestResult;

public class ExcelStyler {
    public static final short RESULT_PASS_COLOR;
    public static final short RESULT_FAIL_COLOR;
    public static final short RESULT_SKIP_COLOR;
    public static final short FONT_COLOR;
    public static final short HEADER_BG_COLOR;
    public static final short FOOTER_BG_COLOR;
    public static final short HEADER_HEIGHT = 420;
    public static final short SHEET_COLOR;

    public ExcelStyler() {

    }

    public static void setSheetTabColor(Sheet paramSheet, ITestResult paramITestResult) {
        if (paramITestResult.getStatus() == 2) {
            ((XSSFSheet) paramSheet).setTabColor(RESULT_FAIL_COLOR);
        } else if (paramITestResult.getStatus() == 1) {
            ((XSSFSheet) paramSheet).setTabColor(RESULT_PASS_COLOR);
        } else {
            ((XSSFSheet) paramSheet).setTabColor(RESULT_SKIP_COLOR);
        }
    }

    public static void setSheetTabColor(Sheet paramSheet) {
        ((XSSFSheet) paramSheet).setTabColor(SHEET_COLOR);
    }

    public static void setBorderLine(Workbook paramWorkbook, Row paramRow) {
        for (int i = 0; i < 8; i++) {
            Cell localCell = paramRow.getCell(i);
            CellStyle localCellStyle = paramWorkbook.createCellStyle();
            if (i == 6 || i == 7) {
                localCellStyle = localCell.getCellStyle();
            }

            localCellStyle.setBorderBottom((short) 1);
            localCellStyle.setBottomBorderColor(HEADER_BG_COLOR);
            localCell.setCellStyle(localCellStyle);
        }
    }

    public static CellStyle setHeaderCellStyle(Workbook paramWorkbook, Cell paramCell) {
        Font localFont = paramWorkbook.createFont();
        localFont.setColor(FONT_COLOR);
        CellStyle localCellStyle = paramWorkbook.createCellStyle();
        localCellStyle.setFillForegroundColor(HEADER_BG_COLOR);
        localCellStyle.setFillPattern((short) 1);
        localCellStyle.setBorderRight((short) 1);
        localCellStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        localCellStyle.setFont(localFont);
        localCellStyle.setAlignment((short) 2);
        localCellStyle.setVerticalAlignment((short) 1);
        paramCell.setCellStyle(localCellStyle);
        return localCellStyle;
    }

    static CellStyle setColor(Workbook paramWorkbook, short paramShort1, short paramShort2) {
        Font localFont = paramWorkbook.createFont();
        localFont.setColor(paramShort2);
        CellStyle localCellStyle = paramWorkbook.createCellStyle();
        localCellStyle.setFillForegroundColor(paramShort1);
        localCellStyle.setFillPattern((short) 1);
        localCellStyle.setBorderRight((short) 1);
        localCellStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        localCellStyle.setFont(localFont);
        localCellStyle.setAlignment((short) 2);
        return localCellStyle;
    }

    static void setResultCellStyle(Workbook paramWorkbook, Cell paramCell, int paramInt) {
        if (paramInt == 2) {
            paramCell.setCellStyle(setColor(paramWorkbook, RESULT_FAIL_COLOR, FONT_COLOR));
        } else if (paramInt == 1) {
            paramCell.setCellStyle(setColor(paramWorkbook, RESULT_PASS_COLOR, FONT_COLOR));
        } else if (paramInt == 3) {
            paramCell.setCellStyle(setColor(paramWorkbook, RESULT_SKIP_COLOR, FONT_COLOR));
        }
    }

    static void setResultCellStyle(Workbook paramWorkbook, Cell paramCell, ITestResult paramITestResult) {
        if (paramITestResult.getStatus() == 2) {
            paramCell.setCellValue(ReportLabels.FAIL.getLabel());
            paramCell.setCellStyle(setColor(paramWorkbook, RESULT_FAIL_COLOR, FONT_COLOR));
        } else if (paramITestResult.getStatus() == 1) {
            paramCell.setCellValue(ReportLabels.PASS.getLabel());
            paramCell.setCellStyle(setColor(paramWorkbook, RESULT_PASS_COLOR, FONT_COLOR));
        } else if (paramITestResult.getStatus() == 3) {
            paramCell.setCellValue(ReportLabels.SKIP.getLabel());
            paramCell.setCellStyle(setColor(paramWorkbook, RESULT_SKIP_COLOR, FONT_COLOR));
        }
    }

    static {
        RESULT_PASS_COLOR = IndexedColors.GREEN.getIndex();
        RESULT_FAIL_COLOR = IndexedColors.RED.getIndex();
        RESULT_SKIP_COLOR = IndexedColors.ROYAL_BLUE.getIndex();
        FONT_COLOR = IndexedColors.WHITE.getIndex();
        HEADER_BG_COLOR = IndexedColors.GREY_50_PERCENT.getIndex();
        FOOTER_BG_COLOR = IndexedColors.DARK_TEAL.getIndex();
        SHEET_COLOR = IndexedColors.DARK_TEAL.getIndex();
    }
}
