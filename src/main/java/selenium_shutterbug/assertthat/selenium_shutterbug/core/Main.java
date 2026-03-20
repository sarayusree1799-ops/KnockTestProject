package selenium_shutterbug.assertthat.selenium_shutterbug.core;

import java.io.IOException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

    public static void main(String... args) throws IOException, InterruptedException {
        String input = "And I type \"ttt\" in \"sss\"";
        System.out.println("And I type \"ttt\" in \"sss\"".replaceAll("<.+>", "< >").replaceAll("\".+^\"", "\" \""));
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.pearson.com/en-us/search.html?q=edition");
        WebElement el = driver.findElement(By.cssSelector(".col-12 .title h2"));
        driver.manage().window().maximize();
        PageSnapshot snapshot = Shutterbug.shootPage(driver, Capture.FULL, true);
        snapshot.cutOut(new WebElement[]{el});
        snapshot.withName("dasdsa").save("./dasad/");
        driver.quit();
        System.exit(0);
        driver.manage().window().maximize();
        Shutterbug.shootPage(driver, Capture.VERTICAL_SCROLL)
                .cutOut(5, 5, new WebElement[]{driver.findElement(By.cssSelector("[alt='adidas-solid-clx-swim-shorts-blue']"))})
                .save();
        driver.quit();
    }
}
