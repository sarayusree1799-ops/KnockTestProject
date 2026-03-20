package Reports.com.rp.reports.writers;

import Reports.com.rp.reports.utils.Directory;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HTMLDesignFilesWriter {
    public HTMLDesignFilesWriter() {
    }

    public static void writeCSS() {
        copyFile("style/design.css", Directory.CSSDir);
        copyFile("style/jquery.jqplot.css", Directory.CSSDir);
        copyFile("style/jquery-ui.min.css", Directory.CSSDir);
    }

    public static void writeJS() {
        copyFile("js/excanvas.js", Directory.JSDir);
        copyFile("js/jqplot.barRenderer.min.js", Directory.JSDir);
        copyFile("js/jqplot.categoryAxisRenderer.min.js", Directory.JSDir);
        copyFile("js/jqplot.highlighter.min.js", Directory.JSDir);
        copyFile("js/jqplot.pieRenderer.min.js", Directory.JSDir);
        copyFile("js/jqplot.pointLabels.min.js", Directory.JSDir);
        copyFile("js/jquery.jqplot.min.js", Directory.JSDir);
        copyFile("js/jquery.min.js", Directory.JSDir);
        copyFile("js/jquery-ui.min.js", Directory.JSDir);
    }

    public static void writeIMG() {
        copyFile("images/fail.png", Directory.IMGDir);
        copyFile("images/pass.png", Directory.IMGDir);
        copyFile("images/skip.png", Directory.IMGDir);
        copyFile("images/rp-logo-2020.png", Directory.IMGDir);
        copyFile("images/loginfo.png", Directory.IMGDir);
        copyFile("images/logpass.png", Directory.IMGDir);
        copyFile("images/logfail.png", Directory.IMGDir);
        copyFile("images/logwarning.png", Directory.IMGDir);
    }

    private static void copyImage(String paramString1, String paramString2) {
        File localFile = new File(paramString1);
        InputStream localInputStream = HTMLDesignFilesWriter.class.getClassLoader().getResourceAsStream(paramString1);
        FileImageOutputStream localFileImageOutputStream = null;
        try {
            localFileImageOutputStream = new FileImageOutputStream(new File(paramString2 + Directory.SEP + localFile.getName()));
            int i = false;
            int i;
            while ((i = localInputStream.read()) >= 0) {
                localFileImageOutputStream.write(i);
            }
            localFileImageOutputStream.close();
            return;
        } catch (Exception var15) {
        } finally {
            try {
                localInputStream.close();
                localFileImageOutputStream.close();
                localFile = null;
            } catch (Exception var14) {
                localInputStream = null;
                localFileImageOutputStream = null;
                localFile = null;
            }
        }
    }

    private static void copyFile(String paramString1, String paramString2) {
        File localFile = new File(paramString1);
        InputStream localInputStream = HTMLDesignFilesWriter.class.getClassLoader().getResourceAsStream(paramString1);
        FileOutputStream localFileOutputStream = null;
        try {
            localFileOutputStream = new FileOutputStream(paramString2 + Directory.SEP + localFile.getName());
            int i = false;
            int i;
            while ((i = localInputStream.read()) >= 0) {
                localFileOutputStream.write(i);
            }
            return;
        } catch (FileNotFoundException var17) {
        } catch (Exception var15) {
        } finally {
            try {
                localInputStream.close();
                localFileOutputStream.close();
                localFile = null;
            } catch (Exception var14) {
                localInputStream = null;
                localFileOutputStream = null;
                localFile = null;
            }
        }
    }
}