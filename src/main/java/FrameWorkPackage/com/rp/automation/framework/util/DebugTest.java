package FrameWorkPackage.com.rp.automation.framework.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DebugTest {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions cp = new ChromeOptions();
        cp.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        ChromeDriver driver = new ChromeDriver(cp);
        driver.findElement(By.name("q")).sendKeys(new CharSequence[] {"selenium"});
    }
}
