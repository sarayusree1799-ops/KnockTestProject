package selenium_shutterbug.knock.ui.lma.scripts;

import selenium_shutterbug.knock.ui.base.BaseTest;
import selenium_shutterbug.knock.ui.lma.pages.PasswordCheck;
import org.testng.annotations.Test;

import java.awt.*;

public class RobotClassPractiseTest extends BaseTest {
    
    @Test
    public void testRobotFunctionality() throws AWTException {
        PasswordCheck passwordCheck = new PasswordCheck(driver);
        passwordCheck.openURL("https://ccnp-app.knocktest.com/");
        passwordCheck.login();
        passwordCheck.googlePasswordManager();
    }
}
