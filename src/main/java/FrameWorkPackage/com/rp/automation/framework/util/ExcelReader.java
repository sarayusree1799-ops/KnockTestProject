package FrameWorkPackage.com.rp.automation.framework.util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelReader {
    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;
    private XSSFCell cell = null;

    public ExcelReader(String path) {
        this.path = path;
        try {
            this.fis = new FileInputStream(path);
            this.workbook = new XSSFWorkbook(this.fis);
            this.sheet = this.workbook.getSheetAt(0);
            this.fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRowCount(String sheetName) {
        int index = this.workbook.getSheetIndex(sheetName);
        if (index == -1) {
            return 0;
        } else {
            this.sheet = this.workbook.getSheetAt(index);
            int number = this.sheet.getLastRowNum() + 1;
            return number;
        }
    }

    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        try {
            this.fis = new FileInputStream(this.path);
            this.workbook = new XSSFWorkbook(this.fis);
            if (rowNum == 0) {
                return false;
            } else {
                int index = this.workbook.getSheetIndex(sheetName);
                int colNum = -1;
                if (index == -1) {
                    return false;
                } else {
                    this.sheet = this.workbook.getSheetAt(index);
                    this.row = this.sheet.getRow(0);

                    for (int i = 0; i < this.row.getLastCellNum(); ++i) {
                        if (this.row.getCell(i).getStringCellValue().trim().equals(colName)) {
                            colNum = i;
                        }
                    }

                    if (colNum == -1) {
                        return false;
                    } else {
                        this.sheet.autoSizeColumn(colNum);
                        this.row = this.sheet.getRow(rowNum - 1);
                        if (this.row == null) {
                            this.row = this.sheet.createRow(rowNum - 1);
                        }

                        this.cell = this.row.getCell(colNum);
                        if (this.cell == null) {
                            this.cell = this.row.createCell(colNum);
                        }

                        this.cell.setCellValue(data);
                        this.fileOut = new FileOutputStream(this.path);
                        this.workbook.write(this.fileOut);
                        this.fileOut.close();
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSheetExist(String sheetName) {
        int index = this.workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = this.workbook.getSheetIndex(sheetName.toUpperCase());
            return index != -1;
        } else {
            return true;
        }
    }

    public int getColumnCount(String sheetName) {
        if (!this.isSheetExist(sheetName)) {
            return -1;
        } else {
            this.sheet = this.workbook.getSheet(sheetName);
            this.row = this.sheet.getRow(0);
            return this.row == null ? -1 : this.row.getLastCellNum();
        }
    }

    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            if (rowNum <= 0) {
                return "";
            } else {
                int index = this.workbook.getSheetIndex(sheetName);
                int col_Num = -1;
                if (index == -1) {
                    return "";
                } else {
                    this.sheet = this.workbook.getSheetAt(index);
                    this.row = this.sheet.getRow(0);

                    for (int i = 0; i < this.row.getLastCellNum(); ++i) {
                        if (this.row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                            col_Num = i;
                        }
                    }
                    if (col_Num == -1) {
                        return "";
                    } else {
                        this.sheet = this.workbook.getSheetAt(index);
                        this.row = this.sheet.getRow(rowNum - 1);
                        if (this.row == null) {
                            return "";
                        } else {
                            this.cell = this.row.getCell(col_Num);
                            if (this.cell == null) {
                                return "";
                            } else {
                                String cellText = this.cell.getStringCellValue();
                                return cellText;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "row " + rowNum + " or column " + colName + " does not exists in xls";
        }
    }

    public String getCellDataForLeaseUpAnalysis(String sheetName, String colName, int rowNum) {
        try {
            if (rowNum <= 0) {
                return "";
            } else {
                int index = this.workbook.getSheetIndex(sheetName);
                int col_Num = -1;
                if (index == -1) {
                    return "";
                } else {
                    this.sheet = this.workbook.getSheetAt(index);
                    this.row = this.sheet.getRow(0);

                    for (int i = 0; i < this.row.getLastCellNum(); i++) {
                        if (this.row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                            col_Num = i;
                        }
                    }

                    if (col_Num == -1) {
                        return "";
                    } else {
                        this.sheet = this.workbook.getSheetAt(index);
                        this.row = this.sheet.getRow(rowNum - 1);
                        if (this.row == null) {
                            return "";
                        } else {
                            this.cell = this.row.getCell(col_Num);
                            if (this.cell == null) {
                                return "";
                            } else {
                                this.cell.getCellType();
                                if (this.cell.getCellType() == 1) {
                                    return this.cell.getStringCellValue();
                                } else if ((this.cell.getCellType() != 0 || !colName.equalsIgnoreCase("Units")) && (this.cell.getCellType() != 0 ||
                                        !colName.equalsIgnoreCase("Avg Sq Ft"))) {
                                    if (this.cell.getCellType() == 4) {
                                        return Boolean.toString(this.cell.getBooleanCellValue());
                                    } else if (this.cell.getCellType() == 3) {
                                        return "";
                                    } else {
                                        String cellText = this.cell.getStringCellValue();
                                        return cellText;
                                    }
                                } else {
                                    String s = Double.toString(this.cell.getNumericCellValue());
                                    return s.substring(0, s.length() - 2);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "row " + rowNum + " or column " + colName + " does not exist in xls";
        }
    }
}