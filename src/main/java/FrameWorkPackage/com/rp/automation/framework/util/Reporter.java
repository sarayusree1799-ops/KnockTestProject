package FrameWorkPackage.com.rp.automation.framework.util;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reporter {
    public static String vDatetype2 = "MM/dd/yyyy";
    public static String vDatetype3 = "dd";
    public static String vDatetype4 = "HH:mm";
    public static String vDatetype5 = "HH:mm:ss";
    public static String vDatetype6 = "MM_dd_YYYY_HH_mm_a";
    public static String vDatetype7 = "E_dd_MMM_YYYY_HH_mm_a_z";
    public static String vDatetype8 = "E_MMM_dd_YYYY_HH_mm_ss_a_z";
    public static String vDatetype9;
    public static String vDatetype1;
    public static String vDatetype10;
    public static String NotePadResultFile;
    public static String NotePadResultFile1;
    public static String NotepadStepsFile;
    public static String NotepadLogFile;
    public static String outputExcelFile;
    public static String HTMLResultFile;
    public static String ScreenShotFile;
    public static String printscreen;
    public static String td_new;
    public static int stepCount;
    protected static int LogCount;
    public static int totalstepsgrand;
    public static long startTime;
    public static long endTime;
    public static long totalTime;
    public static String ScriptStartTime;
    public static String ScriptEndTime;
    public static String ScriptexecTime;
    public static int ScripttotalSteps;
    public static String FinalNotePadName;
    public static String FinalHTMLName;
    public String className = this.getClass().getSimpleName().toString().toUpperCase();

    public Reporter(String FileName, String vDateForReports) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            this.createNotepadResultTemplate(FileName, vDateForReports);
        } catch (Exception e) {
            System.out.println("ERROR OCCURED IN CLASS - " + this.className);
            System.out.println("ERROR OCCURED IN METHOD - " + methodName);
            System.out.println(methodName + " CONSTRUCTOR Error Caused By - " + e.getMessage());
        }
    }

    public void createHTMLResultTemplate(String fileName, String vGetDateForHTML) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String computername = InetAddress.getLocalHost().getHostName();
        String reportFolderName = "ReportsNP";
        createFolderIfNotExists(reportFolderName);
        HTMLResultFile = Paths.get(System.getProperty("user.dir"), reportFolderName, fileName.toUpperCase() + "_" + vGetDateForHTML + ".html").toString();
        File fHTML = new File(HTMLResultFile);
        BufferedWriter wHTML = new BufferedWriter(new FileWriter(fHTML));
        if (fHTML.exists()) {
            fHTML.delete();
        }
        wHTML.write("<!DOCTYPE html><html><head><style>div{font-family:\"Trebuchet MS\", Arial, Helvetica, sans-serif;width:750px;padding:10px;background-color:#EAF2D3;border:5px solid #98bf21;margin: auto;font-size:20px;text-align:justify;line-height:40%;}#Log {font-family:\"Trebuchet MS\", Arial, Helvetica, sans-serif;width:100%;border-collapse:collapse;}#Log th {font-size:1.1em;border:1px solid #A7C942;color:#ffffff;}#Log th {font-weight:bold;text-align:center;padding-top:5px;padding-bottom:4px;background-color: #98bf21;}div align=\"center\"><pre>Test Script Name\t: \t" + fileName.toUpperCase() + "</pre><pre>Test Environment\t: \t</pre><pre>Test Executed By\t:\t" + System.getProperty("user.name").toUpperCase() + "</pre><pre>Java Version\t:\t" + System.getProperty("java.version").toUpperCase() + "</pre><pre>OS Name / Version\t:\t" + System.getProperty("os.name").toUpperCase() + " / " + System.getProperty("os.version").toUpperCase() + "</pre><pre>Computer Name\t:\t" + computername.toUpperCase() + "</pre><pre>Browser Name / Version\t: \t</pre><pre>Test Start Time\t: \t" + ScriptStartTime + "</pre><pre>Test End Time\t: \t</pre><pre>Total Number Of Steps\t: \t</pre><pre>Script Execution Status\t: \t</pre></div><br><table id=\"Log\"><tr><th width=\"4%\">Step</th><th width=\"16%\">Execution Date-Time</th><th width=\"35%\">Step Description</th><th width=\"38%\">Actual Result</th><th width=\"7%\">Status</th></tr></table></body></html>");
        wHTML.close();
    }

    public void createNotepadResultTemplate(String fileName, String vGetDateForNotepad) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        fileName = fileName.toUpperCase();
        vGetDateForNotepad = vGetDateForNotepad.toUpperCase();
        String reportFolderName = "ReportsNP";
        createFolderIfNotExists(reportFolderName);
        String testCaseFolderName = "TestCases";
        createFolderIfNotExists(testCaseFolderName);
        NotePadResultFile = Paths.get(System.getProperty("user.dir"), reportFolderName, fileName + "_" + vGetDateForNotepad + ".txt").toString();
        NotePadResultFile1 = fileName + "_" + vGetDateForNotepad + ".txt";
        NotePadResultFile1 = fileName + "_" + vGetDateForNotepad;
        NotepadStepsFile = Paths.get(System.getProperty("user.dir"), testCaseFolderName, fileName + ".txt").toString();
        NotepadLogFile = Paths.get(System.getProperty("user.dir"), reportFolderName, fileName + "_ExecutionLOG_" + vGetDateForNotepad + ".txt").toString();
        File RF = new File(NotePadResultFile);
        File SF = new File(NotepadStepsFile);
        File LF = new File(NotepadLogFile);
        BufferedWriter wRF = new BufferedWriter(new FileWriter(RF));
        BufferedWriter wSF = new BufferedWriter(new FileWriter(SF));
        BufferedWriter wLF = new BufferedWriter(new FileWriter(LF));
        if (RF.exists()) {
            RF.delete();
        }
        if (SF.exists()) {
            SF.delete();
        }
        if (LF.exists()) {
            LF.delete();
        }
        RF.createNewFile();
        SF.createNewFile();
        LF.createNewFile();
        wSF.write("************************** - " + fileName + " - STEPS TO REPRODUCE DEFECT - **************************");
        wSF.newLine();
        wSF.newLine();
        wLF.write("************************** - " + fileName + " - EXECUTION LOG - **************************");
        wLF.newLine();
        wLF.newLine();
        String computername = InetAddress.getLocalHost().getHostName();
        ScriptStartTime = Calendar.getInstance().getTime().toString();
        wRF.write("\t\t\t***********************************************");
        wRF.newLine();
        wRF.write("\t\t\tTest Script Name\t: " + fileName.toUpperCase());
        wRF.newLine();
        wRF.write("\t\t\tTest Environment\t: ");
        wRF.newLine();
        wRF.write("\t\t\tTest Executed By\t: " + System.getProperty("user.name").toUpperCase());
        wRF.newLine();
        wRF.write("\t\t\tJava Version\t\t: " + System.getProperty("java.version").toUpperCase());
        wRF.newLine();
        wRF.write("\t\t\tOS Name / Version\t: " + System.getProperty("os.name").toUpperCase() + " / " + System.getProperty("os.version").toUpperCase());
        wRF.newLine();
        wRF.write("\t\t\tComputer Name\t\t: " + computername.toUpperCase());
        wRF.newLine();
        wRF.write("\t\t\tBrowser Name / Version\t: ");
        wRF.newLine();
        wRF.write("\t\t\tTest Start Time\t\t: " + ScriptStartTime);
        wRF.newLine();
        wRF.write("\t\t\tTest End Time\t\t: ");
        wRF.newLine();
        wRF.write("\t\t\tScript Execution Time\t: ");
        wRF.newLine();
        wRF.write("\t\t\tTotal Number Of Steps\t: ");
        wRF.newLine();
        wRF.write("\t\t\tScript Execution Status: ");
        wRF.newLine();
        wRF.write("\t\t\t\t\t\t\t\t: ");
        wRF.newLine();
        wRF.write("\t\t\t***********************************************");
        wRF.newLine();
        wRF.newLine();
        wRF.close();
        wSF.close();
        wLF.close();
    }

    public static void updateReports(updateValue value, String value1, String Result) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String newNotepadText = null;
        String newHTMLtext = null;
        String old_NotepadValue = null;
        String new_NotepadValue = null;
        String old_HTMLValue = null;
        String new_HTMLValue = null;
        File NotePadFile = new File(NotePadResultFile);
        BufferedReader NotePadFilereader = new BufferedReader(new FileReader(NotePadFile));
        String line = "";
        String oldtext;
        for (oldtext = ""; (line = NotePadFilereader.readLine()) != null; oldtext = oldtext + line + "\r\n") {
        }
        NotePadFilereader.close();
        String line1 = "";
        String oldtext1 = "";
        FileWriter NotePadFileWriter = new FileWriter(NotePadResultFile);
        switch (value) {
            case bName:
                old_NotepadValue = "\t\t\tBrowser Name / Version\t: ";
                new_NotepadValue = "\t\t\tBrowser Name / Version\t: " + value1;
                break;
            case Env:
                old_NotepadValue = "\t\t\tTest Environment\t: ";
                new_NotepadValue = "\t\t\tTest Environment\t: " + value1;
                break;
            case tEndTime:
                ScriptEndTime = Calendar.getInstance().getTime().toString();
                old_NotepadValue = "\t\t\tTest End Time\t: ";
                new_NotepadValue = "\t\t\tTest End Time\t\t: " + ScriptEndTime;
                break;
            case execTime:
                ScriptexecTime = value1;
                old_NotepadValue = "\t\t\tScript Execution Time\t: ";
                new_NotepadValue = "\t\t\tScript Execution Time\t: " + value1;
                break;
            case totalSteps:
                ScripttotalSteps = stepCount;
                old_NotepadValue = "\t\t\tTotal Number Of Steps\t: ";
                new_NotepadValue = "\t\t\tTotal Number Of Steps\t: " + stepCount;
                break;
            case execStatus:
                old_NotepadValue = "\t\t\tScript Execution Status: ";
                new_NotepadValue = "\t\t\tScript Execution Status\t: " + Result;
                break;
            case failedStepNo:
                old_NotepadValue = "\t\t\t\t\t\t\t: ";
                String var21 = null;
                old_HTMLValue = "<pre>\t\t\t\t\t\t\t: \t</pre>";
                String var24 = null;
                if (Result.equalsIgnoreCase("pass")) {
                    String var25 = "";
                    new_NotepadValue = "";
                } else {
                    new_NotepadValue = "\t\t\tFailed at Step Number\t: " + stepCount;
                }
            case ScreenPrint:
        }
        try {
            newNotepadText = oldtext.replaceAll(old_NotepadValue, new_NotepadValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotePadFileWriter.write(newNotepadText);
        NotePadFileWriter.close();
        String var19 = null;
        String var20 = null;
        String var22 = null;
    }

    public static void writeToNotepad(String status, String expected, String actual, String addInfo) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try {
            boolean append = true;
            FileWriter fout = new FileWriter(NotePadResultFile, append);
            FileWriter fout2 = new FileWriter(NotepadStepsFile, append);
            BufferedWriter fbw = new BufferedWriter(fout);
            BufferedWriter fbw2 = new BufferedWriter(fout2);
            fbw2.write(expected);
            fbw2.newLine();
            if (status.equalsIgnoreCase("pass") | status.equalsIgnoreCase("fail") | status.equalsIgnoreCase("warning")) {
                fbw.write("Step ");
                fbw.write(stepCount + "");
                fbw.write("\t\t");
                fbw.write(getDateFormat(vDatetype9));
                fbw.write("\t\t");
                fbw.write(status.toUpperCase());
                fbw.newLine();
                fbw.write("------------------------------------------------------------");
                fbw.newLine();
                fbw.write("Description\t: " + expected);
                fbw.newLine();
                fbw.write("Actual Result\t: " + actual);
                fbw.newLine();
                fbw.newLine();
                fbw.newLine();
            } else if (status.equalsIgnoreCase("msg")) {
                fbw.write("***********************************************");
                fbw.newLine();
                fbw.write("Description : " + expected);
                fbw.newLine();
                fbw.newLine();
                fbw.newLine();
            } else if (status.equalsIgnoreCase("info")) {
                fbw.newLine();
                fbw.write("\t\t" + addInfo);
                fbw.newLine();
                fbw.newLine();
                fbw.newLine();
            }

            fbw.close();
            fbw2.close();
        } catch (Exception e) {
            System.out.println("ERROR OCCURED IN METHOD - " + methodName);
            System.out.println(methodName + " Method Error Caused By - " + e.getMessage());
        }
    }

    public static void LogEvent(TestStatus Status, String Expected, String Actual, String addInfo) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        switch (Status) {
            case PASS:
                try {
                    writeToNotepad("PASS", Expected, addInfo, Actual);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ++stepCount;
                break;
            case FAIL:
                try {
                    writeToNotepad("FAIL", Expected, addInfo.toUpperCase(), Actual.toUpperCase());
                    writeToNotepad("INFO", "", "", "-------->[ Exiting Script Execution".toUpperCase() + " ]<--------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ++stepCount;
                break;
            case WARNING:
                try {
                    writeToNotepad("WARNING", Expected, addInfo, Actual);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ++stepCount;
                break;
            case MSG:
                try {
                    writeToNotepad("MSG", Expected, addInfo, Actual);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case INFO:
                try {
                    if (addInfo.equalsIgnoreCase("--------->[ End Of Script Execution ]<--------")) {
                        --stepCount;
                    }
                    writeToNotepad("INFO", Expected, addInfo, Actual);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    protected static void failurepopUp(String Expected, String Actual) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println("Inside - " + methodName);
        JFrame jframe = new JFrame();
        String sTitle = "Automation Pursuit";
        jframe.setTitle(sTitle.toUpperCase());
        jframe.setSize(600, 150);
        Toolkit kit = jframe.getToolkit();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Insets in = kit.getScreenInsets(gs[0].getDefaultConfiguration());
        Dimension d = kit.getScreenSize();
        int max_width = d.width - in.left - in.right;
        int max_height = d.height - in.top - in.bottom;
        jframe.setLocation((max_width - jframe.getWidth()) / 2, (max_height - jframe.getHeight()) / 2);
        Container pane = jframe.getContentPane();
        pane.setLayout(new GridLayout(4, 1));
        JLabel L0 = new JLabel("Test Execution Failure Notification".toString().toUpperCase());
        L0.setBackground(Color.YELLOW);
        JLabel L1 = new JLabel(" STEP NO: " + stepCount);
        L1.setForeground(Color.RED);
        JLabel L2 = new JLabel(" Description: " + Expected);
        JLabel L3 = new JLabel(" Actual Result: " + Actual.toUpperCase());
        L3.setForeground(Color.RED);
        pane.add(L0);
        pane.add(L1);
        pane.add(L2);
        pane.add(L3);
        jframe.setVisible(true);
        jframe.setAlwaysOnTop(true);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jframe.dispose();
        System.out.println("EXITING - " + methodName);
    }

    public static void writeLog(String methodName) throws IOException {
        boolean append = true;
        FileWriter fout = new FileWriter(NotepadLogFile, append);
        BufferedWriter fbw = new BufferedWriter(fout);
        fbw.write("Step ");
        fbw.write(LogCount + "\t");
        fbw.write(getDateFormat(vDatetype9) + "\t");
        fbw.write(methodName);
        fbw.newLine();
        fbw.close();
        ++LogCount;
    }

    public String getFinalStatus(int Result) {
        String TestStatus = null;
        if (Result == 1) {
            TestStatus = "PASS";
        } else if (Result == 2) {
            TestStatus = "FAIL";
        }
        return TestStatus;
    }

    public static String getDateFormat(String vDateFormat) {
        Date vDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(vDateFormat);
        return sdf.format(vDate);
    }

    protected static String formatIntoHHMMSS(long milliSeconds) {
        DateFormat f1 = new SimpleDateFormat("hh:mm:ss a");
        return f1.format(milliSeconds);
    }

    protected static String textExecutionTime(long diffSeconds) {
        diffSeconds /= 1000L;
        long hours = diffSeconds / 3600L;
        long remainder = diffSeconds % 3600L;
        long minutes = remainder / 60L;
        long seconds = remainder % 60L;
        return (hours < 10L ? "0" : "") + hours + ":" + (minutes < 10L ? "0" : "") + minutes + ":" + (seconds < 10L ? "0" : "") + seconds;
    }

    public static void RenameResultLog(String finalScriptStatus) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println("Inside - " + methodName);
        File oldNotepadfile = new File(NotePadResultFile);
        File newNotepadfile = new File(Paths.get(System.getProperty("user.dir"), "ReportsNP").toString() + NotePadResultFile1.toUpperCase() + "_" + finalScriptStatus + ".txt".toUpperCase());
        FinalNotePadName = newNotepadfile.toString();
        try {
            oldNotepadfile.renameTo(newNotepadfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createFolderIfNotExists(String folderName) {
        File folder = new File(Paths.get(System.getProperty("user.dir"), folderName).toString());
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    static {
        vDatetype9 = "MM/dd/yyyy" + vDatetype5;
        vDatetype1 = "ddMMyyyy_HHmmss";
        vDatetype10 = "hh:mm:ss a";
        stepCount = 1;
        LogCount = 1;
        ScriptStartTime = Calendar.getInstance().getTime().toString();
    }

    public static enum updateValue {
        bName,
        Env,
        tEndTime,
        execTime,
        execStatus,
        totalSteps,
        failedStepNo,
        ScreenPrint;
    }

    public static enum TestStatus {
        PASS,
        FAIL,
        WARNING,
        MSG,
        INFO
    }
}