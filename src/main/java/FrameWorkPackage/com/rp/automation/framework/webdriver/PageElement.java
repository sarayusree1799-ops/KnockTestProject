package FrameWorkPackage.com.rp.automation.framework.webdriver;

import FrameWorkPackage.com.rp.automation.framework.util.WaitType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PageElement {
        private By by;
        private String name;
        private boolean isSlowLoadableComponent;
        private WaitType waitType;
        private int timeOut;
        private WebElement webElement;

        public WebElement getWebElement() {
            return this.webElement = webElement;
        }

        public void setWebElement(WebElement webElement) {
            this.webElement = webElement;
        }

        public By getBy() {
            return this.by;
        }

        public void setBy(By by) {
            this.by = by;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSlowLoadableComponent() {
            return this.isSlowLoadableComponent;
        }

        public void setSlowLoadableComponent(boolean isSlowLoadableComponent) {
            this.isSlowLoadableComponent = isSlowLoadableComponent;
        }

        public WaitType getWaitType() {
            return this.waitType;
        }

        public void setWaitType(WaitType waitType) {
            this.waitType = waitType;
        }

        public int getTimeOut() {
            return this.timeOut;
        }

        public void setTimeOut(int timeOut) {
            this.timeOut = timeOut;
        }

        public  PageElement(By by, String name) {
            this.by = by;
            this.name = name;
        }

        public  PageElement(By by, String name, boolean isSlowLoadableComponent, WaitType waitType, int timeOut) {
            this.by = by;
            this.name = name;
            this.isSlowLoadableComponent = isSlowLoadableComponent;
            this.waitType = waitType;
            this.timeOut = timeOut;
        }

        public String toString() {
            return "PageElement [name=" + this.name + "]";
        }
}