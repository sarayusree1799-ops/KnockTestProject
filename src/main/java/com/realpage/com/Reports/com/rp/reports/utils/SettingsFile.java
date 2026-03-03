package com.realpage.com.Reports.com.rp.reports.utils;

import com.realpage.com.Reports.com.rp.reports.exceptions.ATUReporterException;

import java.io.*;
import java.util.Properties;

public class SettingsFile {
    private static Properties properties;
    public SettingsFile() {
    }

    public static void initSettingsFile() throws ATUReporterException {
        create(Directory.SETTINGSFile);
        set("run", "0");
        set("passedList", "");
        set("failedList", "");
        set("skippedList", "");
    }

    public static void create(String paramString) throws ATUReporterException {
        File localFile = new File(paramString);
        try {
            if (!localFile.exists()) {
                localFile.createNewFile();
            } else {
                localFile.delete();
                localFile.createNewFile();
            }
        } catch (IOException var3) {
            throw new ATUReporterException("Unable to Create Required Files for Custom Reports");
        }
    }

    public static void open() throws ATUReporterException {
        properties = new Properties();
        try {
            properties.load(new FileInputStream((Directory.SETTINGSFile)));

        } catch (FileNotFoundException var1) {
            throw new ATUReporterException("Settings File Not Available");
        } catch (IOException var2) {
            throw new ATUReporterException("Unable to Create Required Files for Custom Reports");
        }
    }

    public static void close() throws ATUReporterException {
        try {
            properties.store(new FileOutputStream(Directory.SETTINGSFile), "");
        } catch (FileNotFoundException var5) {
            throw new ATUReporterException("Settings File Not Available");
        } catch (IOException var6) {
            throw new ATUReporterException("Unable to Create Required Files for Custom Reports");
        } finally {
            properties = null;
        }
    }

    public static int getHighestTestCaseNumber() throws ATUReporterException {
        String[] arrayOfString1 = get("passedList").split(";");
        String[] arrayOfString2 = get("failedList").split(";");
        String[] arrayOfString3 = get("skippedList").split(";");
        int[] arrayOfInt1 = getIntArrayFromStringArray(arrayOfString1);
        int[] arrayOfInt2 = getIntArrayFromStringArray(arrayOfString2);
        int[] arrayOfInt3 = getIntArrayFromStringArray(arrayOfString3);
        int i = getBiggestNumber(arrayOfInt1);
        int j = getBiggestNumber(arrayOfInt2);
        int k = getBiggestNumber(arrayOfInt3);
        int l = getBiggestNumber(new int[]{i, j, k});
        return l;
    }

    private static int[] getIntArrayFromStringArray(String[] paramArrayOfString) {
        int[] arrayOfInt = new int[paramArrayOfString.length];
        for (int i = 0; i < paramArrayOfString.length; i++) {
            arrayOfInt[i] = Integer.parseInt(paramArrayOfString[i]);
        }
        return arrayOfInt;
    }

    public static int getBiggestNumber(int[] paramArrayOfInt) {
        int i = paramArrayOfInt[0];
        for (int j = 1; j < paramArrayOfInt.length; ++j) {
            if (paramArrayOfInt[j] > i) {
                i = paramArrayOfInt[j];
            }
        }
        return i;
    }

    public static void correctErrors() throws NumberFormatException, ATUReporterException {
        int i = Integer.parseInt(get("run"));
        int j = get("passedList").split(";").length;
        int k = get("failedList").split(";").length;
        int m = get("skippedList").split(";").length;
        if (!isFirstParamBig(i, j, k, m)) {
            if (!isFirstParamBig(j, i, k, m)) {
                if (!isFirstParamBig(k, j, i, m)) {
                    if (!isFirstParamBig(m, j, k, i)) {
                    }
                }
            }
        } else {
            int n = i - j;
            String str1 = get("passedList");
            for (int i1 = 0; i1 < n; i1++) {
                str1 = str1 + 0 + ';';
            }
            set("passedList", str1);
            n = i- k;
            String str2 = get("failedList");
            for (int i2 = 0; i2 < n; ++i2) {
                str2 = str2 + 0 + ';';
            }
            set("failedList", str2);
            n = i - m;
            String str3 = get("skippedList");
            for (int i3 = 0; i3 < n; ++i3) {
                str3 = str3 + 0 + ';';
            }
            set("skippedList", str3);
        }
    }

    private static boolean isFirtParamBig(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return paramInt1 > paramInt2 && paramInt1 > paramInt3 && paramInt1 > paramInt4;
    }

    public static String get(String paramString) throws ATUReporterException {
        open();
        String str = properties.getProperty(paramString);
        close();
        return str;
    }

    public static void set(String paramString1, String paramString2) throws ATUReporterException {
        open();
        properties.setProperty(paramString1, paramString2);
        close();
    }
}