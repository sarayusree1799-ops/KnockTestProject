package FrameWorkPackage.com.rp.automation.framework.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;


public class ExcelReadwithheader {
    public static String getFileWithExtension(String destDirectory) {
        String filename = null;
        File file = new File(destDirectory);
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xlsx");
            }
        });
        for (File f : files) {
            filename = f.getName();
        }
        return filename;
    }

    public void readFilewithHeader() throws IOException {
        String destDirectory = Paths.get(System.getProperty("user.dir"), "tempunzipped").toString();
        String fulldestination = Paths.get(destDirectory, getFileWithExtension(destDirectory)).toString();
        File file = new File(fulldestination);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(5);
        for (Row row1 : sheet) {
            for (Cell cell : row) {
                System.out.print("cell value" + cell.getStringCellValue());
            }
        }
    }
}
