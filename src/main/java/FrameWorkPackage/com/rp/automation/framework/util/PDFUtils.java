package FrameWorkPackage.com.rp.automation.framework.util;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFUtils {
    static PDDocument pdfDocument;
    public static PDDocument openPdfPage(String pdfUrl) throws IOException {
        try {
            URL url = new URL(pdfUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            int statusCode = conn.getResponseCode();
            System.out.println("Status Code: " + statusCode);
            InputStream isImage = null;
            if (statusCode == 200) {
                System.out.println("Open PDF file");
                AtuReports.passResults("Open PDF file", pdfUrl, "Expected Status Code:200", "Actual Status Code:" + statusCode);
            } else {
                System.err.println("Something is wrong Openning PDF file");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return pdfDocument;
    }

    public static int getNumberOfPages() throws IOException {
        return pdfDocument.getNumberOfPages();
    }

    public static String getAllPageText() throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        textStripper.setStartPage(textStripper.getStartPage());
        textStripper.setEndPage(textStripper.getEndPage());
        return textStripper.getText(pdfDocument);
    }

    public static String getPageTextOfPage(int pageNum) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        textStripper.setStartPage(pageNum);
        textStripper.setEndPage(pageNum);
        return textStripper.getText(pdfDocument);
    }

    public static PDDocument readPdfContent(String path) throws IOException {
        pdfDocument = PDDocument.load(new File(path));
        return pdfDocument;
    }

    public static boolean validateTextPresentInPDF(String url, String text) throws IOException {
        String content = (new PDFTextStripper()).getText(readPdfContent(url));
        return content.contains(text);
    }

    public static String getPageTextOfPages(int startPage, int endPage) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        textStripper.setStartPage(startPage);
        textStripper.setEndPage(endPage);
        return textStripper.getText(pdfDocument);
    }

    public static boolean verifyTextOfPage(int startPage, String verifyText) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        textStripper.setStartPage(startPage);
        textStripper.setEndPage(startPage);
        return textStripper.getText(pdfDocument).contains(verifyText);
    }

    public static int getPDFPageImageCounts(int pageNum) throws IOException {
        int imageCount = 0;
        PDPage page = pdfDocument.getPage(pageNum - 1);
        PDResources pdResources = page.getResources();

        for(COSName c : pdResources.getXObjectNames()) {
            PDXObject o = pdResources.getXObject(c);
            if (o instanceof PDImageXObject) {
                imageCount = 1;
            }
        }

        return imageCount;
    }

    public static int getPDFImageCounts() throws IOException {
        int imageCount = 0;
        for(PDPage page : pdfDocument.getPages()) {
            PDResources pdResources = page.getResources();
            for(COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);
                if (o instanceof PDImageXObject) {
                    imageCount = 1;
                }
            }
        }
        return imageCount;
    }

    public static void renderImagesFromPDF(String pdfUrl) throws IOException {
        for(PDPage page : openPdfPage(pdfUrl).getPages()) {
            PDResources pdResources = page.getResources();
            for(COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);
                if (o instanceof PDImageXObject) {
                    File files = new File(System.getProperty("user.dir") + "\\Temp\\" + System.nanoTime() + ".png");
                    ImageIO.write(((PDImageXObject)o).getImage(), "png", files);
                }
            }
        }
    }

    public static File renderImageFromPDFPage(String pdfUrl) throws IOException {
        File files = null;
        for(PDPage page : openPdfPage(pdfUrl).getPages()) {
            PDResources pdResources = page.getResources();
            for(COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);
                if (o instanceof PDImageXObject) {
                    files = new File(System.getProperty("user.dir") + "\\Temp\\" + System.nanoTime() + ".png");
                    ImageIO.write(((PDImageXObject)o).getImage(), "png", files);
                }
            }
        }
        return files;
    }
}
