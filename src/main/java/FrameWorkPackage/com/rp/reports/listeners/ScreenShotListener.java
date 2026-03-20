package FrameWorkPackage.com.rp.reports.listeners;

import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotListener extends TestListenerAdapter {
    public void onTestFailure(org.testng.ITestResult tr) {
        if (WebDriverBase.getDriver() != null) {
            Reporter.setCurrentTestResult(tr);
            File screenshot = new File("ScreenShots" + File.separator + System.currentTimeMillis() + ".png");
            if (!screenshot.exists()) {
                (new File(screenshot.getParent())).mkdirs();
                try {
                    screenshot.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                WebDriver driver = (new Augmenter()).augment(WebDriverBase.getDriver());
                (new FileOutputStream(screenshot)).write((byte[]) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}