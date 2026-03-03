package FrameWorkPackage.com.rp.automation.framework.util;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ExcelService {
    /**
     * For data provider :: This method reads from excel and returns iterator
     *
     * @param sWorkBookName
     * @param sWorkbookSheetName
     * @param tableName          - testMethodName
     */
    public Iterator<Object[]> readTestDataFromExcel(String sWorkBookName, String sWorkbookSheetName, String tableName) {
        String testDataFolderPath = Paths.get(System.getProperty("user.dir"), (String) WebDriverBase.context.getBean("root_folder")).toString();
        // System.out.println(new File(testDataFolderPath).listFiles().length);
        List<Hashtable<String, String>> testData = null;
        for (File testDataFile : new File(testDataFolderPath).listFiles()) {
            if (!testDataFile.getAbsolutePath().contains("$")) {
                FileInputStream file = null;
                if (testDataFile.getPath().contains(sWorkBookName)) {
                    try {
                        file = new FileInputStream(testDataFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Workbook workBook = null;
                    try {
                        workBook = WorkbookFactory.create(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    }
                    Sheet testDataSheet = workBook.getSheet(sWorkbookSheetName);
                    testData = this.getTestDataBySheet(testDataSheet, tableName);
                }
            }
        }
        return this.getIterator(testData);
    }

    /**
     * This method is used to read full sheet content (as we are not passing method name as param)
     * Example: For reading "STANDARD REPORTS" sheet contents
     *
     * @param sWorkBookName
     * @param sWorkbookSheetName Returns
     */
    public Iterator<Object[]> readTestDataFromExcel(String sWorkBookName, String sWorkbookSheetName) {
        String testDataFolderPath = Paths.get(System.getProperty("user.dir"), (String) WebDriverBase.context.getBean("root_folder")).toString();
        // System.out.println(new File(testDataFolderPath).listFiles().length);
        List<Hashtable<String, String>> testData = null;
        for (File testDataFile : new File(testDataFolderPath).listFiles()) {
            // System.out.println("testDataFile.getAbsolutePath():::"+testDataFile.getAbsolutePath());
            if (!testDataFile.getAbsolutePath().contains("$")) {
                FileInputStream file = null;
                if (testDataFile.getPath().contains(sWorkBookName)) {
                    try {
                        file = new FileInputStream(testDataFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Workbook workBook = null;
                    try {
                        workBook = WorkbookFactory.create(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    }
                    Sheet testDataSheet = workBook.getSheet(sWorkbookSheetName);
                    testData = getTestDataBySheet(testDataSheet);
                }
            }
        }
        return getIterator(testData);
    }

    private List<Hashtable<String, String>> getTestDataBySheet(Sheet testDataSheet) {
        List<String> headers = getHeaders(testDataSheet);
        List<Hashtable<String, String>> testData = new ArrayList<Hashtable<String, String>>();
        for (int i = 1; i < testDataSheet.getPhysicalNumberOfRows(); i++) {
            Hashtable<String, String> map = new Hashtable<String, String>();
            Row dataRow = testDataSheet.getRow(i);
            Cell dataCell;
            for (int j = 0; j < headers.size(); j++) {
                try {
                    dataCell = dataRow.getCell(j);
                    dataCell.setCellType(Cell.CELL_TYPE_STRING);
                    map.put(headers.get(j), dataCell.getStringCellValue());
                } catch (Exception e) {
                }
            }
            testData.add(map);
        }
        return testData;
    }

    private Iterator<Object[]> getIterator(List<Hashtable<String, String>> testDataList) {
        List<Object[]> iteratorList = new ArrayList<Object[]>();
        for (Map map : testDataList) {
            iteratorList.add(new Object[]{map});
        }
        return iteratorList.iterator();
    }

    public List<String> getHeaders(Sheet sheet) {
        List<String> header = new ArrayList<String>();
        Row headerRow = sheet.getRow(0);
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            Cell dataCell = headerRow.getCell(i);
            dataCell.setCellType(Cell.CELL_TYPE_STRING);
            header.add(dataCell.getStringCellValue());
        }
        return header;
    }

    private List<Hashtable<String, String>> getTestDataBySheet(Sheet testDataSheet, String tableName) {
        List<Hashtable<String, String>> testData = new ArrayList<Hashtable<String, String>>();
        int startRow, endRow;
        // System.out.println("testDataSheet"+testDataSheet);
        startRow = getStartRow(testDataSheet, tableName);
        List<String> headers = getHeaders(testDataSheet, startRow + 1);
        endRow = getEndRow(testDataSheet, tableName, startRow);
        for (int i = startRow + 2; i < endRow; i++) {
            Hashtable<String, String> map = new Hashtable<String, String>();
            Row dataRow = testDataSheet.getRow(i);
            Cell dataCell;
            try {
                if (headers.contains("RunIteration") && !testDataSheet.getRow(i).getCell(1).getStringCellValue()
                        .toString().equalsIgnoreCase("Yes")) {
                    System.out.println("The current iteration :" + i + " is ignore as it set to not run");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("The current iteration :" + i + " is ignore as it set to blank ");
                continue;
            }
            for (int j = 1; j < headers.size(); j++) {
                if (!headers.get(j).equals("CELL NOT FOUND") && !headers.get(j).equals("MISSING CONTENT")) {
                    try {
                        dataCell = dataRow.getCell(j, Row.RETURN_NULL_AND_BLANK);
                        if (dataCell != null) {
                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        if (dataCell != null && !"".equals(dataCell.getStringCellValue().trim())) {
                            map.put(headers.get(j), dataCell.getStringCellValue());
                        }
                    } catch (Exception e) {
                    }
                }
            }
            testData.add(map);
        }
        return testData;
    }

    private int getEndRow(Sheet testDataSheet, String tableName, int startRow) {
        for (int i = startRow + 1; i < testDataSheet.getLastRowNum(); ++i) {
            try {
                if (testDataSheet.getRow(i).getCell(0).getStringCellValue().toString().equalsIgnoreCase(tableName)) {
                    return i;
                }
            } catch (Exception var6) {
            }
        }
        return startRow;
    }

    private int getStartRow(Sheet testDataSheet, String tableName) {
        for (int i = 0; i < testDataSheet.getLastRowNum() + 1; ++i) {
            try {
                if (testDataSheet.getRow(i).getCell(0).getStringCellValue().toString().equalsIgnoreCase(tableName)) {
                    return i;
                }
            } catch (Exception var5) {
            }
        }
        return 0;
    }

    private List<String> getHeaders(Sheet sheet, int row) {
        List<String> headers = new ArrayList<>();
        Row headerRow = sheet.getRow(row);
        for (int i = 0; i < headerRow.getLastCellNum(); ++i) {
            Cell dataCell = headerRow.getCell(i, Row.RETURN_NULL_AND_BLANK);
            if (dataCell == null) {
                headers.add("CELL NOT FOUND");
            } else if ("".equals(dataCell.getStringCellValue().trim())) {
                headers.add("MISSING CONTENT");
            } else {
                dataCell.setCellType(1);
                headers.add(dataCell.getStringCellValue());
            }
        }
        return headers;
    }

    public static Sheet getSheetData(String sWorkBookName, String sWorkbookSheetName) {
        String testDataFolderPath = Paths.get(System.getProperty("user.dir"), "temp").toString();
        System.out.println(new File(testDataFolderPath).listFiles().length);
        Sheet testDataSheet = null;
        for (File testDataFile : new File(testDataFolderPath).listFiles()) {
            System.out.println("testDataFile.getAbsolutePath():::" + testDataFile.getAbsolutePath());
            if (!testDataFile.getAbsolutePath().contains("$")) {
                FileInputStream file = null;
                String[] fileNames = sWorkBookName.split(",");
                boolean result = false;
                if (fileNames.length == 2) {
                    result = testDataFile.getPath().contains(fileNames[0])
                            && testDataFile.getPath().contains(fileNames[1]);
                } else {
                    result = testDataFile.getPath().contains(fileNames[0]);
                }
                if (result) {
                    try {
                        file = new FileInputStream(testDataFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Workbook workBook = null;
                    try {
                        workBook = WorkbookFactory.create(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    }
                    if (sWorkbookSheetName == "") {
                        testDataSheet = workBook.getSheet(sWorkBookName);
                    } else {
                        testDataSheet = workBook.getSheetAt(0);
                    }
                    break;
                }
            }
        }
        return testDataSheet;
    }

    public void isXLSFileEmpty(String fileName) {
        XSSFWorkbook wBook = null;
        try {
            wBook = new XSSFWorkbook(new FileInputStream(fileName));
            for (int i = 0; i < wBook.getNumberOfSheets(); i++) {
                System.out.println("Sheet " + i + " has data: " + isSheetEmpty(wBook.getSheetAt(i)));

                if (isSheetEmpty(wBook.getSheetAt(i))) {
                    AtuReports.passResults1("Verify the Downloaded XLSX File is empty for Sheet " + i, "--",
                            "Downloaded XLSX File", "is not empty, contains something");

                    Reporter.LogEvent(TestStatus.PASS, "Verify the Downloaded XLSX File is empty for Sheet " + i,
                            "Downloaded XLSX File", "is not empty, contains something");
                } else {
                    Reporter.LogEvent(TestStatus.FAIL, "Verify the Downloaded XLSX File is empty for Sheet " + i,
                            "Downloaded XLSX File", "is empty");

                    AtuReports.failResults("Verify the Downloaded XLSX File is empty for Sheet " + i, "--",
                            "Downloaded XLSX File", "is empty");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    boolean isSheetEmpty(XSSFSheet xssfSheet) {
        Iterator rows = xssfSheet.rowIterator();
        while (rows.hasNext()) {
            XSSFRow row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                XSSFCell cell = (XSSFCell) cells.next();
                if (!cell.getStringCellValue().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Read Excel and verify specific content
    public boolean readExcelandVerifyContent(String Path, String Content, String SheetName) {
        boolean result = true;

        File src = new File(Path);
        try {
            FileInputStream fis = new FileInputStream(src);

            try {
                XSSFWorkbook workbook = new XSSFWorkbook(fis);

                // XSSFSheet sheet=workbook.getSheet(SheetName);
                XSSFSheet sheet = workbook.getSheetAt(0);

                int rowsCount = sheet.getLastRowNum();
                System.out.println(rowsCount);
                int columnsCount = sheet.getRow(1).getLastCellNum();
                int count = 0;
                for (int i = 0; i < rowsCount; i++) {
                    for (int j = 0; j < columnsCount; j++) {
                        String cellValue = sheet.getRow(i).getCell(j).getStringCellValue();
                        if (cellValue.contains(Content)) {
                            AtuReports.passResults("Content should be available in Excel", "",
                                    "Content is available in Excel", "Content is available in Excel");
                            count++;
                            i = 50000;
                            j = 50000;
                        }
                    }
                }
                if (count == 0) {
                    result = false;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public static String getFileWithExtension(String destDirectory) {
        String filename = null;
        File file = new File(destDirectory);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.toLowerCase().endsWith(".xlsx")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (File f : files) {
            filename = f.getName();
        }
        return filename;
    }

    public void readFileWithHeader() throws IOException {
        String destDirectory = Paths.get(System.getProperty("user.dir"), "tempunzipped").toString();
        String fulldestination = Paths.get(destDirectory, getFileWithExtension(destDirectory)).toString();
        File file = new File(fulldestination);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(5);
        for (Row row1 : sheet) {
            for (Cell cell : row) {
                System.out.println("cell value " + cell.getStringCellValue());
            }
        }
    }

    public boolean readELAExcelandVerifyChildProp(String Path, String Content, String SheetName) {
        boolean result = true;
        XSSFCell cell = null;
        File src = new File(Path);
        try {
            FileInputStream fis = new FileInputStream(src);
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(fis);

                // XSSFSheet sheet=workbook.getSheet(SheetName);
                XSSFSheet sheet = workbook.getSheetAt(0);

                int rowsCount = sheet.getLastRowNum();
                System.out.println(rowsCount);

                int columnsCount = sheet.getRow(1).getLastCellNum();
                int count = 0;
                String cellvalue = "";
                for (int i = 1; i < rowsCount; i++) {
                    System.out.println("row line: " + i);
                    for (int j = 0; j < columnsCount; j++) {
                        cell = sheet.getRow(i).getCell(j);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            cellvalue = cell.getStringCellValue();
                            System.out.println(cellvalue);
                        } else if ((cell.getCellType() == Cell.CELL_TYPE_NUMERIC)) {
                            cellvalue = Double.toString(cell.getNumericCellValue());
                            System.out.println(cellvalue);
                        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                            cellvalue = Boolean.toString(cell.getBooleanCellValue());
                        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                            cellvalue = "";
                            System.out.println(cellvalue);
                        }
                        if (cellvalue == null) {
                            cellvalue = "";
                        }
                        if (cellvalue.contains(Content)) {
                            AtuReports.passResults("Content should be available in Excel", "",
                                    "Content is available in Excel", "Content is available in Excel");
                            ++count;
                            i = 50000;
                            j = 50000;
                        }
                    }
                }
                if (count == 0) {
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
}