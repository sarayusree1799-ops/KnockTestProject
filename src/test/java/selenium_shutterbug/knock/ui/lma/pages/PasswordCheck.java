package selenium_shutterbug.knock.ui.lma.pages;

import selenium_shutterbug.knock.ui.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.KeyEvent;

public class PasswordCheck extends BasePage {
    //WebDriver driver = new ChromeDriver();

    @FindBy(xpath = "//input[@name='username']")
    private WebElement loginUserName;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement loginPassword;

    @FindBy(xpath = "//button[text()='Sign in']")
    private WebElement signInButton;

    @FindBy(xpath = "/images/nav-bar-icons/knock-logo.png")
    private WebElement knockLogo;

    public PasswordCheck(WebDriver driver) {
        super(driver);
    }

    public void openURL(String url) {
        driver.get(url);
    }

    public void login() {
        loginUserName.click();
        loginUserName.sendKeys("qa_user");
        loginPassword.click();
        loginPassword.sendKeys("qa_user123!");
        signInButton.click();
        //waitForPageElement(knockLogo, WaitType.WAIT_FOR_ELEMENT_TO_BE_DISPLAYED,"", 20);
        explicitWait(5);
    }

    public void googlePasswordManager() throws AWTException {
        Robot robot = new Robot();
        // Focus OK button
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        // Press OK
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}
