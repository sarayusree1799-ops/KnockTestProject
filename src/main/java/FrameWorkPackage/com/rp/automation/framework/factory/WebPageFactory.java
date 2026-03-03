package FrameWorkPackage.com.rp.automation.framework.factory;

import FrameWorkPackage.com.rp.automation.framework.webdriver.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class WebPageFactory implements BasePageFactory {
    private WebDriver driver;

    public WebPageFactory(WebDriver driver) {
        this.driver = driver;
    }

    public <T extends Page> T getPageObject(Class<T> obj) {
        return (T) PageFactory.initElements(new Page().driver, obj);
    }
}
