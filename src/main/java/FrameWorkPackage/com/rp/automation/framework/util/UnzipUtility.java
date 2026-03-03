package FrameWorkPackage.com.rp.automation.framework.util;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.SheetBuilder;
import org.testng.annotations.AfterClass;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipUtility {
    private static final int BUFFER_SIZE = 4096;

    public void unzip(String zipFilePath, String startsWith) {
        String fullPath = "";
        File directoryPath = new File(zipFilePath);
        String[] contents = directoryPath.list();
        System.out.println("List of files and directories in the specified directory:");
        for (int i = 0; i < contents.length; ++i) {
            System.out.println(contents[i]);
            if (contents[i].contains(".zip") || contents[i].startsWith(startsWith)) {
                fullPath = contents[i];
            }
        }
        String destDirectory = zipFilePath + File.separator + fullPath.substring(0, fullPath.indexOf("."));
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath + File.separator + fullPath));
            for (ZipEntry entry = zipIn.getNextEntry(); entry != null; entry = zipIn.getNextEntry()) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    this.extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
            }
            zipIn.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public static String getMeFullFilePath(String path, String startsWith, String fileExtension) {
        String result = "";
        File tempDir = new File(path);
        String[] tempFolderContent = tempDir.list();
        System.out.println("List of files and directories in the specified directory:");
        String unzippedFolderPath = "";
        for (int i = 0; i < tempFolderContent.length; ++i) {
            System.out.println(tempFolderContent[i]);
            if (!tempFolderContent[i].contains(".zip") || tempFolderContent[i].startsWith(startsWith)) {
                unzippedFolderPath = path + File.separator + tempFolderContent[i];
                break;
            }
        }
        File unzippedFolder = new File(unzippedFolderPath);
        String[] unzippedFolderContents = unzippedFolder.list();
        for (int j = 0; j < unzippedFolderContents.length; ++j) {
            if (unzippedFolderContents[j].contains(fileExtension)) {
                result = unzippedFolderPath + File.separator + unzippedFolderContents[j];
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        UnzipUtility unzipUtil = new UnzipUtility();
        String path = System.getProperty("user.dir") + "/temp";
        String startsWith = "Report_";
        String fileExtension = "HTML";
        try {
            unzipUtil.unzip(path, startsWith);
        } catch (Exception var6) {
        }
        String tempp = getMeFullFilePath(path, startsWith, fileExtension);
        System.out.println(tempp);
    }

    public static SheetBuilder getFileNamesFromZip(String path, String extension, String sWorkbookSheetName) {
        Sheet testDataSheet = null;
        try {
            ZipFile zipFile = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String actualExtension = entry.getName().split("\\.")[1];
                InputStream file = zipFile.getInputStream(entry);
                if (!actualExtension.equalsIgnoreCase("csv") && actualExtension.equalsIgnoreCase("XLSX")) {
                    Workbook workbook = WorkbookFactory.create(file);
                    testDataSheet = workbook.getSheet(sWorkbookSheetName);
                }
            }
        } catch (InvalidFormatException | IOException var10) {
            System.out.println("Exception at getFileNamesFromZip - Path" + path);
        }
        return (SheetBuilder) testDataSheet;
    }

    public void deleteZipFile(File directory, String fileNameStartwith) {
        File[] fList = directory.listFiles();
        if (fList.length != 0) {
            for (File file : fList) {
                if (file.getName().contains(fileNameStartwith)) {
                    file.delete();
                }
            }
        }
    }

    public void deleteUnZippedfolder(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public void cleanDirectory(String folder) {
        File directory = new File(System.getProperty("user.dir") + "\\temp\\" + folder);
        if (directory.exists()) {
            try {
                FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "\\tenp\\" + folder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMeSpecificFileinUnzippedFile(String path, String startsWith, String fileExtension, String fileName) {
        String result = "";
        File tempDir = new File(path);
        String[] tempFolderContent = tempDir.list();
        System.out.println("List of files and directories in the specified directory:");
        String unzippedFolderPath = "";
        for (int i = 0; i < tempFolderContent.length; ++i) {
            System.out.println(tempFolderContent[i]);
            if (!tempFolderContent[i].contains(".zip") || tempFolderContent[i].startsWith(startsWith)) {
                unzippedFolderPath = path + File.separator + tempFolderContent[i];
                break;
            }
        }
        File unzippedFolder = new File(unzippedFolderPath);
        String[] unzippedFolderContents = unzippedFolder.list();
        for (int j = 0; j < unzippedFolderContents.length; ++j) {
            if (unzippedFolderContents[j].contains(fileExtension) && unzippedFolderContents[j].contains(fileName)) {
                result = unzippedFolderPath + File.separator + unzippedFolderContents[j];
                break;
            }
        }
        return result;
    }

    public String getFullFilePath(String path, String startsWith, String fileExtension) {
        File tempDir = new File(path);
        String[] tempFolderContent = tempDir.list();
        String filePath = "";
        for (int i = 0; i < tempFolderContent.length; ++i) {
            System.out.println(tempFolderContent[i]);
            if (tempFolderContent[i].startsWith(startsWith) || tempFolderContent[i].contains(fileExtension)) {
                filePath = path + File.separator + tempFolderContent[i];
                break;
            }
        }
        return filePath;
    }
}