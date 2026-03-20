package FrameWorkPackage.com.rp.automation.framework.util;

public enum BrowserType {
    FIREFOX("firefox"),
    CHROME("chrome"),
    CHROME1("chrome1"),
    CHROMEHEADLESS("chromeheadless"),
    IE("ie"),
    EDGE("edge"),
    EDGEIE("edgeie"),
    SAFARI("safari"),
    CHROMEPROXY("chromeproxy"),
    IPHONE6("iphone6"),
    IPHONE7("iphone7"),
    IPHONE8("iphone8"),
    REMOTE("remote"),
    ANDROID("android"),
    IOS("ios");

    private final String browser;
    BrowserType(String browser) {
        this.browser = browser;
    }
    public String getBrowser() {
        return browser;
    }
}
