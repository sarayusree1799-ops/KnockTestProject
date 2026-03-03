package FrameWorkPackage.com.rp.automation.framework.factory;

import FrameWorkPackage.com.rp.automation.framework.webdriver.Page;

public interface BasePageFactory {
    <T extends Page> T getPageObject(Class<T> obj);
}
