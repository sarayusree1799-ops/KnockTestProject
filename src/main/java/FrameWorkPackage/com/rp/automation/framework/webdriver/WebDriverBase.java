package FrameWorkPackage.com.rp.automation.framework.webdriver;

import FrameWorkPackage.com.rp.automation.framework.factory.BasePageFactory;
import FrameWorkPackage.com.rp.automation.framework.factory.WebPageFactory;
import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter;
import FrameWorkPackage.com.rp.automation.framework.util.BrowserType;
import Reports.com.rp.reports.ATUReports;
import Reports.com.rp.reports.utils.Platform;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.ITestContext;
import org.testng.ITestResult;

import org.testng.annotations.*;
import org.testng.annotations.Optional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.ResourceBundle;


public class WebDriverBase {
    private static String BROWSER_NAME = "browsername";
    protected static BrowserType browserType;
    public static ResourceBundle _prop1 = ResourceBundle.getBundle("atu");
    public static BrowserMobProxyServer server;
    public BasePageFactory pageFactory;
    private WebDriver driver;
    public static AbstractApplicationContext context = null;
    public static File folder;
    public static int counter;
    public static boolean api_db_testingtype_enabler = false;
    public static String planId;
    public static String suiteId;
    public boolean grid = Boolean.valueOf(System.getProperty("grid", "false"));
    public boolean browserstack_hub = Boolean.valueOf(System.getProperty("browserstack_hub", "false"));
    public static final String USERNAME = String.valueOf(System.getProperty("b_username", "abc"));
    public static final String AUTOMATE_KEY = String.valueOf(System.getProperty("b_key", "XXX"));
    public static final String browserstack_URL;

    public AbstractApplicationContext getContext() {
        return context;
    }

    public JdbcTemplate getJdbcTemplate(String connString) {
        return (JdbcTemplate) context.getBean(connString, JdbcTemplate.class);
    }

    public String getBeanName(String sWorkBookName) {
        return (String) context.getBean(sWorkBookName, String.class);
    }

    public BasePageFactory getPageFactory() {
        this.pageFactory = new WebPageFactory(getDriver());
        return this.pageFactory;
    }

    @BeforeSuite(
            alwaysRun = true
    )
    public void beforeSuite() throws Exception {
        context = new ClassPathXmlApplicationContext("/env/spring-beans.xml");
    }

    @BeforeSuite(
            alwaysRun = true
    )
    @Parameters({"planid", "suiteid"})
    public void setSuitePlanID(@Optional("NoParameters") String planid, @Optional("NoParameters") String suiteid) throws Exception {
        planId = planid;
        suiteId = suiteid;
    }

    @AfterSuite(
            alwaysRun = true
    )
    @Parameters({"browsername"})
    public void afterSuite(String browserName) throws Exception {
        context.close();
        if (System.getProperty("os.name").toLowerCase().contains("win") && !browserName.toLowerCase().contains("chrome")) {
            this.AbolishDriverInstProcess("IEDriverServer");
            this.AbolishDriverInstProcess("geckodriver");
            this.AbolishDriverInstProcess("msedgedriver");
        }
    }

    @BeforeClass(
            alwaysRun = true
    )
    public void indexDescription() {
        AtuReports.setIndexPageDescription();
    }

    @BeforeTest(
            alwaysRun = true
    )
    @Parameters({"testingtype", "browsername", "browserversion", "platform"})
    public static synchronized void setUpTest(@Optional("ui") String testingtype, @Optional("chrome") String browserName, String browserVersion, String platform) {
        String browserValue = browserName;
        try {
            if (!System.getProperty("browser", browserName).isEmpty()) {
                browserValue = System.getProperty("browser", browserName);
            }
        } catch (Exception var9) {
            browserValue = browserName;
        }

        if (!testingtype.trim().equalsIgnoreCase("") && !testingtype.trim().equalsIgnoreCase("ui")) {
            api_db_testingtype_enabler = true;
        } else {
            for (BrowserType browser : BrowserType.values()) {
                if (browser.toString().toLowerCase().equals(browserValue.toLowerCase())) {
                    browserType = browser;
                    break;
                }
            }
        }
    }

    @BeforeMethod(
            alwaysRun = true
    )
    public synchronized void setUp(ITestContext ic, Method method) throws IOException {
        if (!api_db_testingtype_enabler) {
            DriverManager.setWebDriver(this.loadWebDriver());
            ATUReports.setWebDriver(DriverManager.getDriver());
            DriverManager.getDriver().manage().window().maximize();
            DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
            System.out.println("method.getName() - " + method.getName());
            System.out.println("ic.getClass().getName() - " + this.getClass().getName());
        }

        new Reporter(method.getName(), Reporter.getDateFormat(Reporter.vDatetype8));
    }

    @AfterMethod(
            alwaysRun = true
    )
    public synchronized void tearDown(ITestResult paramITestResult) throws IOException {
        System.out.println("@AfterMethod");
        String browserName = paramITestResult.getAttribute(Platform.BROWSER_NAME_PROP).toString();
        if (!api_db_testingtype_enabler) {
            if (browserName != null && browserName.equalsIgnoreCase("Unknown")) {
                paramITestResult.setAttribute("BrowserName", "Internet explorer");
                paramITestResult.setAttribute("BrowserVersion", "v11");
            }
        } else {
            paramITestResult.setAttribute("BrowserName", "- -");
            paramITestResult.setAttribute("BrowserVersion", "- -");
        }
        if (DriverManager.getDriver() != null) {
            try {
                DriverManager.getDriver().quit();
                System.out.println("removed driver");
            } catch (WebDriverException var4) {
                if (DriverManager.getDriver() != null) {
                    DriverManager.getDriver().quit();
                }
            }
        }
    }

    public static WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    private DesiredCapabilities generateDesiredCapabilities(BrowserType capabilityType) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        switch (capabilityType) {
            case IE:
                capabilities.setCapability("ignoreProtectedModeSettings", true);
                capabilities.setCapability("nativeEvents", true);
                capabilities.setCapability("ignoreProtectedModeSettings", true);
                capabilities.setCapability("requireWindowFocus", true);
                BROWSER_NAME = capabilities.getBrowserName();
                Platform.BROWSER_VERSION = capabilities.getBrowserVersion();
                break;
            case EDGE:
                folder = new File(Paths.get(System.getProperty("user.dir"), "temp").toString());
                if (!folder.exists()) {
                    folder.mkdir();
                }

                new EdgeOptions();
                capabilities.setCapability("profile.default_content_settings.popups", 0);
                capabilities.setCapability("download.default_directory", Paths.get(System.getProperty("user.dir"), "temp").toString());
                capabilities.setCapability("browser.setDownloadBehavior", "allow");
                break;
            case SAFARI:
                capabilities.setCapability("safari.cleanSession", true);
                break;
            case CHROME:
                folder = new File(Paths.get(System.getProperty("user.dir"), "temp").toString());
                if (!folder.exists()) {
                    folder.mkdir();
                }

                ChromeOptions options = new ChromeOptions();
                options.addArguments(new String[]{"--remote-allow-origins=*"});
                options.setPlatformName("windows");
                options.addArguments(new String[]{"networkConnectionEnabled", "false"});
                capabilities.setCapability("acceptInsecureCerts", true);
                capabilities.setCapability("goog:chromeOptions", options);
                break;

            case CHROME1:
                folder = new File(Paths.get(System.getProperty("user.dir"), "temp").toString());
                if (!folder.exists()) {
                    folder.mkdir();
                }

                capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
                HashMap<String, Object> chromePreferences3 = new HashMap<>();
                chromePreferences3.put("profile.default_content_setting_values.automatic_downloads", 1);
                chromePreferences3.put("profile.password_manager_enabled", "false");
                chromePreferences3.put("download.prompt_for_download", 0);
                chromePreferences3.put("download.default_directory", Paths.get(System.getProperty("user.dir"), "temp").toString());

                ChromeOptions options3 = new ChromeOptions();
                List<String> experimentalFlags3 = new ArrayList<>();

                experimentalFlags3.add("same-site-by-default-cookies@2");
                experimentalFlags3.add("cookies-without-same-site-must-be-secure@2");
                chromePreferences3.put("browser.enabled_labs_experiments", experimentalFlags3);
                options3.setExperimentalOption("prefs", chromePreferences3);
                options3.addArguments(new String[]{"--start-maximized"});

                options3.addArguments(new String[]{"--remote-allow-origins=*"});
                options3.addArguments(new String[]{"--use-fake-ui-for-media-stream"});
                options3.addArguments(new String[]{"--auto-grant-permissions"});
                options3.addArguments(new String[]{"--window-size=1980,960"});
                options3.addArguments(new String[]{"--start-fullscreen"});
                options3.addArguments(new String[]{"--unsafely-treat-insecure-origin-as-secure=http://bicasat.realpage.com,http://bicaorc.realpage.com,http://bicausat.realpage.com,https://bica.realpage.com,http://rcqbicappor01.realpage.com,http://rcsbicappor01.realpage.com"});
                options3.addArguments(new String[]{"test-type"});
                options3.addArguments(new String[]{"disable-popup-blocking"});
                capabilities.setCapability("chrome.prefs", chromePreferences3);
                capabilities.setCapability("acceptInsecureCerts", true);
                capabilities.setCapability("goog:chromeOptions", options3);
                break;
            case CHROMEPROXY:
                folder = new File(Paths.get(System.getProperty("user.dir"), "temp").toString());
                if (!folder.exists()) {
                    folder.mkdir();
                }
                server = new BrowserMobProxyServer();
                server.start(0);
                server.enableHarCaptureTypes(new CaptureType[]{CaptureType.REQUEST_HEADERS, CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_HEADERS, CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_CONTENT});
                Proxy proxy = ClientUtil.createSeleniumProxy(server);
                ChromeOptions options4 = new ChromeOptions();
                options4.setProxy(proxy);
                options4.setAcceptInsecureCerts(true);
                capabilities.setCapability("proxy", proxy);
                capabilities.setCapability("goog:chromeOptions", options4);
                break;
            case CHROMEHEADLESS:
                folder = new File(Paths.get(System.getProperty("user.dir"), "temp").toString());
                if (!folder.exists()) {
                    folder.mkdir();
                }

                capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
                HashMap<String, Object> chromePreferences1 = new HashMap<>();
                chromePreferences1.put("profile.default_content_settings.popups", 0);
                chromePreferences1.put("download.prompt_for_download", 0);
                chromePreferences1.put("download.default_directory", Paths.get(System.getProperty("user.dir"), "temp").toString());
                chromePreferences1.put("profile.password_manager_enabled", "false");
                ChromeOptions options1 = new ChromeOptions();
                options1.addArguments(new String[]{"--headless"});
                options1.addArguments(new String[]{"test-type"});
                options1.addArguments(new String[]{"--remote-allow-origins=*"});
                options1.addArguments(new String[]{"--use-fake-ui-for-media-stream"});
                options1.addArguments(new String[]{"--auto-grant-permissions"});
                options1.addArguments(new String[]{"disable-popup-blocking"});
                capabilities.setCapability("chrome.prefs", chromePreferences1);
                capabilities.setCapability("acceptInsecureCerts", true);
                capabilities.setCapability("goog:chromeOptions", options1);
                break;

            case FIREFOX:
                folder = new File(Paths.get(System.getProperty("user.dir"), "temp").toString());
                if (!folder.exists()) {
                    folder.mkdir();
                }

                capabilities.setBrowserName("firefox");
                capabilities.setCapability("marionette", false);
                break;

            case IPHONE6:
            case IPHONE7:
            case IPHONE8:
                Map<String, String> iphone_6_7_8 = new HashMap<>();
                iphone_6_7_8.put("deviceName", "iphone 6/7/8");
                Map<String, Object> chromeOptions = new HashMap<>();
                chromeOptions.put("mobileEmulation", iphone_6_7_8);
                break;
            default:
                capabilities.setCapability("javaScriptEnabled", true);
        }
        return capabilities;
    }

    private DesiredCapabilities browserStackCap() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (this.browserstack_hub) {
            capabilities.setCapability("browser", System.getProperty("browser", browserType.toString()));
            System.out.println("browserstackOptions_browser" + System.getProperty("browser"));
            capabilities.setCapability("browserVersion", "latest");
            HashMap<String, Object> browserstackOptions = new HashMap<>();
            browserstackOptions.put("buildName", System.getProperty("suiteName", this.getClass().getSimpleName().toString().toUpperCase()));
            browserstackOptions.put("os", System.getProperty("os", "Windows"));
            System.out.println("browserstackOptions_os" + System.getProperty("os", "Windows"));
            browserstackOptions.put("osVersion", System.getProperty("osVersion", "11"));
            System.out.println("browserstackOptions_osVersion" + System.getProperty("osVersion", "11"));
            capabilities.setCapability("bstack:options", browserstackOptions);
        }
        return capabilities;
    }

    private synchronized WebDriver loadWebDriver() {
        System.out.println("Current Browser Selection: " + browserType);
        String osName = this.operatingSystem();
        if (this.grid) {
            try {
                RemoteWebDriver driver = new RemoteWebDriver(new URL(this.getBeanName("grid_url")), this.generateDesiredCapabilities(browserType));
                return driver;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to create driver");
            }
        } else if (this.browserstack_hub) {
            try {
                RemoteWebDriver driver = new RemoteWebDriver(new URL(browserstack_URL), this.browserStackCap());
                return driver;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to create driver");
            }
        } else {
            folder = new File(Paths.get(System.getProperty("user.dir"), "temp").toString());
            if (!folder.exists()) {
                folder.mkdir();
            }

            if (browserType.getBrowser().equals("chrome")) {
                System.out.println("Launching : " + browserType);
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
                HashMap<String, Object> chromePreferences = new HashMap<>();
                chromePreferences.put("profile.default_content_settings.popups", 0);
                chromePreferences.put("download.default_directory", Paths.get(System.getProperty("user.dir"), "temp").toString());
                chromePreferences.put("profile.password_manager_enabled", "false");
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", chromePreferences);
                options.addArguments(new String[]{"test-type"});
                options.addArguments(new String[]{"--remote-allow-origins=*"});
                options.addArguments(new String[]{"--use-fake-ui-for-media-stream"});
                options.addArguments(new String[]{"--auto-grant-permissions"});
                options.addArguments(new String[]{"disable-popup-blocking"});
                options.addArguments(new String[]{"safebrowsing.enabled", "true"});
                capabilities.setCapability("prefs", chromePreferences);
                capabilities.setCapability("acceptInsecureCerts", true);
                capabilities.setCapability("goog:chromeOptions", options);
                options.merge(capabilities);
                this.driver = new ChromeDriver(options);
            } else if (browserType.getBrowser().equals("chromeheadless")) {
                System.out.println("Launching : " + browserType);
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
                HashMap<String, Object> chromePreferences = new HashMap<>();
                chromePreferences.put("download.prompt_for_download", 0);
                chromePreferences.put("download.default_directory", Paths.get(System.getProperty("user.dir"), "temp").toString());
                chromePreferences.put("profile.password_manager_enabled", "false");
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", chromePreferences);
                options.addArguments(new String[]{"test-type"});
                options.addArguments(new String[]{"--headless"});
                options.addArguments(new String[]{"--remote-allow-origins=*"});
                options.addArguments(new String[]{"--use-fake-ui-for-media-stream"});
                options.addArguments(new String[]{"--auto-grant-permissions"});
                options.addArguments(new String[]{"disable-popup-blocking"});
                options.addArguments(new String[]{"safebrowsing.enabled", "true"});
                capabilities.setCapability("chrome.prefs", chromePreferences);
                capabilities.setCapability("acceptInsecureCerts", true);
                capabilities.setCapability("goog:chromeOptions", options);
                options.merge(capabilities);
                this.driver = new ChromeDriver(options);
            } else if (browserType.getBrowser().equals("chrome1")) {
                System.out.println("Launching " + browserType);
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
                HashMap<String, Object> chromePreferences3 = new HashMap<>();
                chromePreferences3.put("download.prompt_for_download", 0);
                chromePreferences3.put("download.default_directory", Paths.get(System.getProperty("user.dir"), "temp").toString());
                chromePreferences3.put("profile.default_content_setting_values.automatic_downloads", 1);
                chromePreferences3.put("profile.password_manager_enabled", "false");
                ChromeOptions options3 = new ChromeOptions();
                List<String> experimentalFlags3 = new ArrayList<>();
                experimentalFlags3.add("same-site@2");
                chromePreferences3.put("browser.enabled_labs_experiments", experimentalFlags3);
                options3.setExperimentalOption("prefs", chromePreferences3);
                options3.addArguments(new String[]{"--start-maximized"});
                options3.addArguments(new String[]{"--remote-allow-origins=*"});
                options3.addArguments(new String[]{"--use-fake-ui-for-media-stream"});
                options3.addArguments(new String[]{"--auto-grant-permissions"});
                options3.addArguments(new String[]{"--window-size=1980,960"});
                options3.addArguments(new String[]{"--start-fullscreen"});
                options3.addArguments(new String[]{"--unsafely-treat-insecure-origin-as-secure=http://bicasat.realpage.com,http://bicaga.realpage.com,http://bicadev.realpage.com,http://bicaorc.realpage.com,https://bica.realpage.com,http://rcgbicappor101.realpage.com,http://rcsbicappor101.realpage.com"});
                options3.addArguments(new String[]{"test-type"});
                options3.addArguments(new String[]{"disable-popup-blocking"});
                capabilities.setCapability("chrome.prefs", chromePreferences3);
                capabilities.setCapability("acceptInsecureCerts", true);
                capabilities.setCapability("goog:chromeOptions", options3);
                options3.merge(capabilities);
                this.driver = new ChromeDriver(options3);
            } else if (browserType.getBrowser().equals("chromeproxy")) {
                System.out.println("Launching : " + browserType);
                DesiredCapabilities capabilities = new DesiredCapabilities();
                server = new BrowserMobProxyServer();
                server.start(0);
                server.enableHarCaptureTypes(new CaptureType[]{CaptureType.REQUEST_HEADERS, CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_HEADERS, CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_COOKIES, CaptureType.RESPONSE_CONTENT});
                Proxy seleniumProxy = ClientUtil.createSeleniumProxy(server);
                String hostIp = "localhost";
                seleniumProxy.setHttpProxy(hostIp + ":" + server.getPort());
                seleniumProxy.setSslProxy(hostIp + ":" + server.getPort());
                seleniumProxy.setFtpProxy(hostIp + ":" + server.getPort());
                ChromeOptions options4 = new ChromeOptions();
                options4.addArguments(new String[]{"--ignore-certificate-errors"});
                options4.addArguments(new String[]{"--use-fake-ui-for-media-stream"});
                options4.addArguments(new String[]{"--auto-grant-permissions"});
                options4.setProxy(seleniumProxy);
                options4.setAcceptInsecureCerts(true);
                capabilities.setCapability("proxy", seleniumProxy);
                capabilities.setCapability("goog:chromeOptions", options4);
                options4.merge(capabilities);
                this.driver = new ChromeDriver(options4);
            } else if (browserType.getBrowser().equals("chromeheadless")) {
                System.out.println("Launching : " + browserType);
                this.driver = new ChromeDriver();
            } else if (browserType.getBrowser().equals("firefox")) {
                this.driver = new FirefoxDriver();
            } else if (browserType.getBrowser().equals("ie")) {
                System.out.println("Launching : " + browserType);
                WebDriverManager.iedriver().arch32().setup();
                this.driver = new InternetExplorerDriver();

            } else if (browserType.getBrowser().equals("edge")) {
                System.out.println("Launching : " + browserType);
                EdgeOptions edgeOptions = new EdgeOptions();
                HashMap<String, Object> edgePreferences = new HashMap<>();
                edgePreferences.put("download.default_directory", Paths.get(System.getProperty("user.dir"), "temp").toString());
                edgePreferences.put("download.prompt_for_download", false);
                edgePreferences.put("plugins.always_open_pdf_externally", true);
                edgePreferences.put("profile.default_content_setting_values.automatic_downloads", 1);
                edgeOptions.addArguments(new String[]{"--remote-allow-origins=*"});
                edgeOptions.addArguments(new String[]{"--use-fake-ui-for-media-stream"});
                edgeOptions.addArguments(new String[]{"--auto-grant-permissions"});
                edgeOptions.setExperimentalOption("prefs", edgePreferences);
                this.driver = new EdgeDriver(edgeOptions);
            } else if (browserType.getBrowser().equals("iphone7")) {
                System.out.println("Launching : " + browserType);
                System.setProperty("webdriver.chrome.silentOutput", "true");
                this.driver = new ChromeDriver();
            } else if (browserType.getBrowser().equals("safari")) {
                System.out.println("Launching : " + browserType);
                this.driver = new SafariDriver();
            } else if (browserType.getBrowser().equals("edgeie")) {
                System.out.println("Launching : " + browserType);
                WebDriverManager.iedriver().arch32().setup();
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.attachToEdgeChrome();
                ieOptions.withEdgeExecutablePath("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
                ieOptions.setCapability("ignoreProtectedModeSettings", true);
                this.driver = new InternetExplorerDriver(ieOptions);
            }

            return this.driver;
        }
    }

    public String operatingSystem() {
        return System.getProperty("os.name");
    }

    private void AbolishDriverInstProcess(String strDriverInstName) {
        try {
            Runtime.getRuntime().exec(new String[]{"cmd", "/k", "start", "taskkill.exe", "/F", "/IM", strDriverInstName + ".exe", "/T"});
            System.out.println("Kill all the Driver Instances of " + strDriverInstName);
        } catch (Exception ex) {
            System.out.println("error desc : " + ex.getMessage());
        }
    }

    protected static String formatIntoHHMMSS(long diffSeconds) {
        diffSeconds /= 1000L;
        long hours = diffSeconds / 3600L;
        long remainder = diffSeconds % 3600L;
        long minutes = remainder / 60L;
        long seconds = remainder % 60L;
        return (hours < 10L ? "0" : "") + hours + ":" + (minutes < 10L ? "0" : "") + minutes + ":" + (seconds < 10L ? "0" : "") + seconds;
    }

    static {
        browserstack_URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    }
}
