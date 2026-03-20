package Reports.com.rp.reports.utils;

import Reports.com.rp.reports.ATUReports;
import Reports.com.rp.reports.enums.RecordingFor;
import Reports.com.rp.reports.enums.ReportLabels;
import Reports.com.rp.reports.exceptions.ATUReporterException;
import Reports.com.rp.reports.writers.HTMLDesignFilesWriter;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Directory {
    public static final String ATU_VERSION = "v5.1.1";
    public static final String CurrentDir = System.getProperty("user.dir");
    public static final String SEP = System.getProperty("file.separator");
    public static String REPORTSDIRName = "ATU Reports";

    public static String REPORTSDir;
    public static String ExcelDIRName;
    public static String RESULTSDir;
    public static String HTMLDESIGNDIRName;
    public static String HTMLDESIGNDir;
    public static String CSSDIRName;
    public static String CSSDir;
    public static String IMGDIRName;
    public static String IMGDir;
    public static String JSDIRName;
    public static String JSDir;
    public static String RUNName;
    public static String RUNDir;
    public static String SETTINGSFile;
    public static final char JS_SETTINGS_DELIM = ';';
    public static final String REPO_DELIM = "##@##@##";
    public static final char JS_FILE_DELIM = ',';

    public static final String MENU_LINK_NAME = "Run ";
    public static final String SCREENSHOT_TYPE = "PNG";
    public static final String SCREENSHOT_EXTENSION = ".PNG";

    private static String logo;
    public static String SCREENSHOT_DIRName;
    public static boolean generateConfigReports;
    public static boolean generateExcelReports;
    public static boolean takeScreenshot;
    public static boolean continueExecutionAfterStepFailed;
    public static boolean recordExecutionAvailable;
    public static boolean recordSuiteExecution;
    public static boolean recordTestMethodExecution;
    public static final String MAIN_RECORD_FILE_NAME = "ATU_CompleteSuiteRecording";

    public Directory() {
    }

    public static void init() throws ATUReporterException {
        if (getCustomProperties() != null) {
            Properties localProperties = new Properties();

            try {
                localProperties.load(new FileInputStream(getCustomProperties()));
                String str1 = localProperties.getProperty("atu.reports.dir").trim();
                String str2 = localProperties.getProperty("atu.proj.header.text").trim();
                logo = localProperties.getProperty("atu.proj.header.logo").trim();
                String str3 = localProperties.getProperty("atu.proj.description").trim();
                String str4 = localProperties.getProperty("atu.reports.takescreenshot").trim();
                String str5 = localProperties.getProperty("atu.reports.configurationreports").trim();
                String str6 = localProperties.getProperty("atu.reports.excel").trim();
                String str7 = localProperties.getProperty("atu.reports.continueExecutionAfterStepFailed").trim();
                String str8 = localProperties.getProperty("atu.reports.recordExecution").trim();

                try {
                    if (str2 != null && str2.length() > 0) {
                        ReportLabels.HEADER_TEXT.setLabel(str2);
                    }

                    if (str4 != null && str4.length() > 0) {
                        try {
                            takeScreenshot = Boolean.parseBoolean(str4);
                        } catch (Exception var14) {
                        }
                    }

                    if (str5 != null && str5.length() > 0) {
                        try {
                            generateConfigReports = Boolean.parseBoolean(str5);
                        } catch (Exception var13) {
                        }
                    }

                    if (str6 != null && str6.length() > 0) {
                        try {
                            generateExcelReports = Boolean.parseBoolean(str6);
                        } catch (Exception var12) {
                        }
                    }

                    if (str7 != null && str7.length() > 0) {
                        try {
                            continueExecutionAfterStepFailed = Boolean.parseBoolean(str7);
                        } catch (Exception var11) {
                        }
                    }

                    if (str8 != null && str8.length() > 0) {
                        try {
                            RecordingFor localRecordingFor = RecordingFor.valueOf(str8.toUpperCase());
                            if (localRecordingFor == RecordingFor.SUITE) {
                                recordSuiteExecution = true;
                            } else if (localRecordingFor == RecordingFor.TESTMETHOD) {
                                recordTestMethodExecution = true;
                            }
                        } catch (Exception var10) {
                        }
                    }

                    if (str3 != null && str3.length() > 0) {
                        ATUReports.indexPageDescription = str3;
                    }

                    if (str1 != null && str1.length() > 0) {
                        REPORTSDir = str1;
                        REPORTSDIRName = (new File(str1)).getName();
                        RESULTSDir = REPORTSDir + SEP + "Results";
                        HTMLDESIGNDIRName = "HTML_Design_Files";
                        HTMLDESIGNDir = REPORTSDir + SEP + HTMLDESIGNDIRName;
                        CSSDIRName = "CSS";
                        CSSDir = HTMLDESIGNDir + SEP + CSSDIRName;
                        IMGDIRName = "IMG";
                        IMGDir = HTMLDESIGNDir + SEP + IMGDIRName;
                        JSDIRName = "JS";
                        JSDir = HTMLDESIGNDir + SEP + JSDIRName;
                        RUNName = "Run_";
                        RUNDir = RESULTSDir + SEP + RUNName;
                        SETTINGSFile = RESULTSDir + SEP + "Settings.properties";
                    }

                } catch (Exception var15) {
                    throw new ATUReporterException(var15.toString());
                }
            } catch (FileNotFoundException var16) {
                throw new ATUReporterException("Custom Properties File not found at " + getCustomProperties());
            } catch (IOException var17) {
                throw new ATUReporterException("Custom Properties File could not be loaded from " + getCustomProperties());
            }
        }
    }

    public static void mkDirs(String paramString) {
        File localFile = new File(paramString);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
    }

    public static boolean exists(String paramString) {
        File localFile = new File(paramString);
        return localFile.exists();
    }

    public static void verifyRequiredFiles() throws ATUReporterException {
        if (!exists(REPORTSDir)) {
            mkDirs(REPORTSDir);
        }

        if (!exists(RESULTSDir)) {
            mkDirs(RESULTSDir);
        }

        if (!exists(HTMLDESIGNDir)) {
            mkDirs(HTMLDESIGNDir);
            HTMLDesignFilesWriter.writeCSS();
            HTMLDesignFilesWriter.writeIMG();
            HTMLDesignFilesWriter.writeJS();
        }

        if (logo != null && logo.length() > 0) {
            String str = (new File(logo)).getName();
            if (!(new File(IMGDir + SEP + str)).exists()) {
                copyImage(logo);
            }
            ReportLabels.PROJ_LOGO.setLabel(str);
        }
    }

    private static void copyImage(String paramString) throws ATUReporterException {
        File localFile = new File(paramString);
        if (localFile.exists()) {
            FileImageInputStream localFileImageInputStream = null;
            FileImageOutputStream localFileImageOutputStream = null;

            try {
                localFileImageInputStream = new FileImageInputStream(new File(paramString));
                localFileImageOutputStream = new FileImageOutputStream(new File(IMGDir + SEP + localFile.getName()));
                int i = false;

                while ((i = localFileImageInputStream.read()) >= 0) {
                    localFileImageOutputStream.write(i);
                }

                localFileImageOutputStream.close();
                return;
            } catch (Exception var14) {
            } finally {
                try {

                    localFileImageOutputStream.close();
                } catch (Exception var13) {
                    localFileImageInputStream = null;
                    localFileImageOutputStream = null;
                    localFile = null;
                }
            }
        }
    }

    private static String getCustomProperties() {
        return System.getProperty("atu.reporter.config");
    }

    static {
        REPORTSDir = CurrentDir + SEP + REPORTSDIRName;
        ExcelDIRName = "";
        RESULTSDir = REPORTSDir + SEP + "Results";
        HTMLDESIGNDIRName = "HTML_Design_Files";
        HTMLDESIGNDir = REPORTSDir + SEP + HTMLDESIGNDIRName;
        CSSDIRName = "CSS";
        CSSDir = HTMLDESIGNDir + SEP + CSSDIRName;
        IMGDIRName = "IMG";
        IMGDir = HTMLDESIGNDir + SEP + IMGDIRName;
        JSDIRName = "JS";
        JSDir = HTMLDESIGNDir + SEP + JSDIRName;
        RUNName = "Run_";
        RUNDir = RESULTSDir + SEP + RUNName;
        SETTINGSFile = RESULTSDir + SEP + "Settings.properties";
        logo = null;
        SCREENSHOT_DIRName = "img";
        generateConfigReports = true;
        generateExcelReports = false;
        takeScreenshot = false;
        continueExecutionAfterStepFailed = false;
        recordExecutionAvailable = false;
        recordSuiteExecution = false;
        recordTestMethodExecution = false;
    }
}