package selenium_shutterbug.knock.ui.base;

import selenium_shutterbug.knock.utils.SkipWithReducedStackTrace;
import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter;
import FrameWorkPackage.com.rp.automation.framework.webdriver.DriverManager;
import FrameWorkPackage.com.rp.automation.framework.webdriver.Page;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import lombok.Getter;
import net.snowflake.client.jdbc.internal.joda.time.DateTime;
import net.snowflake.client.jdbc.internal.joda.time.DateTimeZone;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class BasePage extends Page {

    public enum InputType {
        JSON,
        BeanID,
        String
    }

    String dirDownloads = System.getProperty("user.dir") + "/Downloads/";

    String leftNavigationElement = "//*[contains(@*,'nav')]//a[contains(normalize-space(.),'%s')]";
    String leftNavigationElementForClaw = "//a[(contains(normalize-space(.),'%s') or @aria-label='%s')]";

    public void findStringIn(List<WebElement> webElements, boolean expectedResult, String... textToVerify) {
        boolean textFound = false;
        try {
            for (WebElement element : webElements) {
                String elementText = element.getText();
                for (String text : textToVerify) {
                    if (elementText.contains(text) && element.isDisplayed()) {
                        textFound = true;
                        AtuReports.passResults1("Verify" + "\"" + text + "\"" + " string is found in the list of web elements provided", "--", "\"" + text + "\"" + " string should be found in the list of web elements provided", "\"" + text + "\"" + " string is found in the list of web elements provided");
                        Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify" + "\"" + text + "\"" + " string is found in the list of web elements provided", "\"" + text + "\"" + " string should be found in the list of web elements provided", "\"" + text + "\"" + " string is found in the list of web elements provided");
                        break;
                    }
                }
            }
            if (textFound == expectedResult) {
                Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify an expected string is found in the list of web elements provided", "An expected string should be found in the list of web elements provided", "An expected string is found in the list of web elements provided");
                AtuReports.passResults1("Verify an expected string is found in the list of web elements provided", "--", "An expected string should be found in the list of web elements provided", "An expected string is found in the list of web elements provided");
            } else {
                Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify an expected string is found in the list of web elements provided", "An expected string should  be found in the list of web elements provided", "An expected string is not found in the list of web elements provided");
                AtuReports.failResults("Verify an expected string is found in the list of web elements provided", "--", "An expected string should be found in the list of web elements provided", "An expected string is not found in the list of web elements provided");
            }
        } catch (Exception e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify an expected string is found in the list of web elements provided", "An expected string should be found in the list of web elements provided", this.catchException(e));
            AtuReports.failResults("Verify an expected string is found in the list of web elements provided", "--", "An expected string should be found in the list of web elements provided", this.catchException(e));
        }
    }

    public boolean isAllElementsDisplayed(List<WebElement> elements, int expectedSize) {
        if (elements.size() != expectedSize) return false;
        return waitForAllElementsToBeDisplayed(elements, "Elements", 10);
    }

    public void openBaseURL(InputType value, String urlKey, JSONObject... obj) {
        switch (value) {
            case BeanID:
                openURL(getBaseUrl(urlKey));
                break;
            case JSON:
                openURL(getJSONValue(obj[0], urlKey));
                break;
            default:
                AtuReports.failResults("Switch case is missing for the given enum value", value.toString(), "", value.toString());
        }
    }

    /* url - Should pass the exact url to render */
    public void openURL(String url) {
        try {
            driver.navigate().to(url);
            waitForPageLoad();
            AtuReports.passResults1("Navigate to " + driver.getCurrentUrl() + " application", url,
                    driver.getTitle() + " should display", driver.getTitle() + " is displayed");
            resizeWindow(1920, 1080);
        } catch (Exception exception) {
            AtuReports.failResults("Navigate to " + url + " Application", url,
                    url + " should be rendered", catchException(exception));
        }
    }

    public List<String> getMatchedText(String textToFind, String message) {
        List<String> matchedTexts = new ArrayList<>();
        Pattern pattern = Pattern.compile(textToFind);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            matchedTexts.add(matcher.group());
        }
        return matchedTexts;
    }

    /* appendString - Should pass the string to append it to the current url */
    public void renderAppendedURL(String appendString) {
        String currentURL = driver.getCurrentUrl();
        String modifiedURL = currentURL.substring(0, currentURL.lastIndexOf("/")) + "/" + appendString;
        openURL(modifiedURL);
    }

    public static String generateRandomCharsNumbers() {
        String candidateChars = "12345678901234567890123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }
        return sb.toString();
    }

    public static String generateRandomCharsAmount() {
        String candidateChars = "123456789123456789123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }
        return sb.toString();
    }

    public enum KeyAction {
        ENTER, DOWN, UP, TAB, RIGHT, LEFT, CONTROL, CTRL_A, DELETE, ESCAPE, BACK_SPACE, PAGE_DOWN, SHIFT_END, END
    }

    public void enterKeyByActions(KeyAction performKey) {
        Actions ac = new Actions(driver);
        switch (performKey) {
            case ENTER:
                ac.sendKeys(Keys.ENTER).build().perform();
                break;
            case DOWN:
                ac.sendKeys(Keys.DOWN).build().perform();
                break;
            case UP:
                ac.sendKeys(Keys.UP).build().perform();
                break;
            case TAB:
                ac.sendKeys(Keys.TAB).build().perform();
                break;
            case RIGHT:
                ac.sendKeys(Keys.RIGHT).build().perform();
                break;
            case LEFT:
                ac.sendKeys(Keys.LEFT).build().perform();
                break;
            case CTRL_A:
                ac.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).build().perform();
                break;
            case DELETE:
                ac.sendKeys(Keys.DELETE).build().perform();
                break;
            case ESCAPE:
                ac.sendKeys(Keys.ESCAPE).build().perform();
                break;
            case BACK_SPACE:
                ac.sendKeys(Keys.BACK_SPACE).build().perform();
                break;
            case PAGE_DOWN:
                ac.sendKeys(Keys.PAGE_DOWN).build().perform();
                break;
            case SHIFT_END:
                ac.keyDown(Keys.SHIFT).sendKeys(Keys.END).keyUp(Keys.SHIFT).build().perform();
                break;
            case END:
                ac.sendKeys(Keys.END).build().perform();
                break;
            default:
                AtuReports.failResults("Perform Key action", performKey.toString(), "Key should be part of KeyAction enum", "Invalid key given - " + performKey);
                break;
        }
    }

    public static String getBeanName(String url) {
        return WebDriverBase.context.getBean(url, String.class);
    }

    public <T extends Enum<T>> T getRandomEnumValueFrom(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        int length = values.length;
        if (length == 0) {
            throw new IllegalArgumentException("The provided enum has no values.");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(length);
        return values[randomIndex];
    }

    public int getRandomWithExclusion(int start, int end, int... exclude) {
        Random rnd = new Random();
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    public static String getJSONValue(JSONObject obj, String key) {
        return (String) obj.get(key);
    }

    public boolean waitForAllElementsToBeDisplayed(List<WebElement> elements, String logMessage, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            AtuReports.passResults1("Verify element is displayed " + logMessage, "--",
                    logMessage + " should be displayed ", logMessage + " is displayed");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify element is displayed " + logMessage,
                    logMessage + " should be displayed ", logMessage + " is displayed");
        } catch (Exception e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify element is displayed " + logMessage,
                    logMessage + " should be displayed ", catchException(e));
            AtuReports.failResults("Verify element is displayed " + logMessage, "--",
                    logMessage + " should be displayed ", catchException(e));
            return false;
        }
        return true;
    }

    public boolean waitForAllElementsToDisappear(List<WebElement> elements, String logMessage, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
            AtuReports.passResults1("Verify element is not displayed " + logMessage, "--",
                    logMessage + " should not be displayed ", logMessage + " is not displayed");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify element is not displayed " + logMessage,
                    logMessage + " should not be displayed ", logMessage + " is not displayed");
        } catch (Exception e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify element is not displayed " + logMessage,
                    logMessage + " should not be displayed ", catchException(e));
            AtuReports.failResults("Verify element is not displayed " + logMessage, "--",
                    logMessage + " should not be displayed ", catchException(e));
            return false;
        }
        return true;
    }

    //Method to select the value from the Dropdown list
    public void selectValueFromTheDropDownList(List<WebElement> elements, String selectedName) {
        boolean found = false;
        for (WebElement element : elements) {
            if (element.getText().equals(selectedName)) {
                compareText(element.getText(), selectedName);
                click(element, element.getText());
                explicitWait(3);
                found = true;
                break;
            }
        }
        if (!found) {
            scrollDown();
        }
    }

    //Generic Method to select One Option From The List
    public void selectOneOptionFromTheList(JSONObject fileName, String keyValue, List<WebElement> elements, int index) {
        String[] selectedFilterName = (fileName.get(keyValue)).toString().split(",");
        String selectedName = selectedFilterName[index];
        selectValueFromTheDropDownList(elements, selectedName);
    }

    public void skipInAlphaAndBeta() throws SkipWithReducedStackTrace {
        String environment = System.getProperty("env");
        if ("beta".equals(environment) || "alpha".equals(environment)) {
            throw new SkipWithReducedStackTrace(
                    "Skipping this test due to the current environment being: " + environment);
        }
    }

    //Skip the test based on given environments example skipIfEnvironment("alpha","beta");
    public void skipIfEnvironment(String... targetEnvironments) throws SkipWithReducedStackTrace {
        String currentEnvironment = System.getProperty("env");
        for (String env : targetEnvironments) {
            if (env.equals(currentEnvironment)) {
                AtuReports.info("Test skipped", env, "Test should skip, if the given environment matched", "Test is skipped for the environment - " + currentEnvironment);
                throw new SkipWithReducedStackTrace(
                        "Skipping this test due to the current environment being: " + currentEnvironment);
            }
        }
    }

    //General method for comparing the DropDown list
    public void compareListsData(List<WebElement> webElementList, List<String> expectedList) {
        List<String> actualData = getListElementsText(webElementList);
        compareList(actualData, expectedList, "compares actual data with expected data list");
    }

    public List<String> getListElementsText(List<WebElement> webElementList) {
        List<String> actualData = new ArrayList<>();
        for (WebElement element : webElementList) {
            actualData.add(element.getText());
        }
        return actualData;
    }

    public void resizeWindow(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        driver.manage().window().setSize(dimension);
        AtuReports.passResults1("Resize window", width + " x " + height,
                "Window should be resized ", "Window is resized");
    }

    public void clickOnLink(String linkText) {
        String currentAppURL = getAppCurrentUrl();
        WebElement element;
        if (currentAppURL.contains("claw")) {
            element = dynamicXpathWebElement(getLeftNavigationElementForClaw(), linkText, linkText);
        } else {
            element = dynamicXpathWebElement(getLeftNavigationElement(), linkText, linkText);
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        focus(element, "Focused" + element);
        jsClick(element, linkText + " link");
        explicitWait(3);
    }

    public void switchToIFrameXpath(WebElement pageElementXpath) {
        driver.switchTo().frame(pageElementXpath);
    }

    public void verifyHREFLink(WebElement element, String expectedLinkURL) {
        String actualURL = getText(element, "href");
        compareText(actualURL, expectedLinkURL, "URL");
        verifyURLStatus(actualURL);
    }

    public String patternMatcher(String stringPattern, String strToEvaluate) {
        String result = "";
        Matcher m = Pattern.compile(stringPattern).matcher(strToEvaluate);
        if (m.find()) {
            result = m.group(1);
        }
        return result;
    }

    public static void waitForTextToAppear(WebElement element, String textToAppear) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(60));
        wait.until(ExpectedConditions.textToBePresentInElement(element, textToAppear));
    }

    public void waitForData(WebElement element, String text, int timeout) {
        boolean isElementPresent = false;
        int count = 0;
        while (!isElementPresent) {
            explicitWait(1);
            isElementPresent = !element.getText().contains(text);
            count++;
            AtuReports.passResults1("Waiting for data to be displayed", "", "", "Waiting for 1 second");
            if (count == timeout) {
                AtuReports.failResults("Failed to verify the web element", "", "Web element data should be visible", "Waited for nearly " + timeout + " sec, WebElement data is not visible");
                break;
            }
        }
    }

    public void retrySendKeys(WebElement element, String inputText, String logMessage, Integer retryCount) {
        int count = 1;
        do {
            sendKeys(element, inputText, logMessage);
            AtuReports.passResults1("Execute sendKeys method", "", "Max retry " + retryCount, "Attempt " + count);
            count++;
        } while (!element.getText().equals(inputText) && !element.getAttribute("value").equals(inputText) && count <= retryCount);
    }

    public void enterTextCharByChar(WebElement element, String charSeq, String logMessage) {
        try {
            char[] inputText = charSeq.toCharArray();
            element.clear();
            for (char val : inputText) {
                element.sendKeys(String.valueOf(val));
            }
            AtuReports.passResults1("Entering the " + logMessage, charSeq, charSeq + " should be entered ", charSeq + " is entered successfully");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Entering the " + logMessage, charSeq + " should be entered ", charSeq + " is entered successfully");
        } catch (Exception e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Entering the " + logMessage, charSeq + " should be entered ", this.catchException(e));
            AtuReports.failResults("Failed to Enter text in " + logMessage + " text Field ", charSeq, " Text " + charSeq + " should be entered in to " + logMessage + " text Field", this.catchException(e));
        }
    }

    public void enterTextUsingJs(WebElement element, String inputText, String logMessage) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].setAttribute('value', '" + inputText + "')", element);
            AtuReports.passResults1("Entering the " + logMessage, inputText, inputText + " should be entered ", inputText + " is entered successfully");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Entering the " + logMessage, inputText + " should be entered ", inputText + " is entered successfully");
        } catch (Exception e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Entering the " + logMessage, inputText + " should be entered ", this.catchException(e));
            AtuReports.failResults("Failed to Enter text in " + logMessage + " text Field ", inputText, " Text " + inputText + " should be entered in to " + logMessage + " text Field", this.catchException(e));
        }
    }

    public void click(WebElement pageElement, String logMessage) {
        try {
            pageElement.click();
            AtuReports.passResults1("Verify click action on: " + logMessage, "--", logMessage + " Should be clicked", logMessage + " is clicked");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify click action on: " + logMessage, logMessage + " Should be clicked", logMessage + " is clicked");
        } catch (Exception e) {
            jsClick(pageElement, logMessage);
        }
    }

    public void retryClick(WebElement pageElement, String logMessage, int maxRetries) {
        int attempts = 0;
        boolean isClicked = false;
        while (!isClicked && attempts < maxRetries) {
            try {
                pageElement.click();
                AtuReports.passResults1("Verify click action on: " + logMessage, "--", logMessage + " Should be clicked", logMessage + " is clicked");
                Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify click action on: " + logMessage, logMessage + " Should be clicked", logMessage + " is clicked");
                isClicked = true;
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) {
                    click(pageElement, logMessage);
                    break;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    // Handle the InterruptedException
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (!isClicked) {
            AtuReports.failResults1("Failed to click on: " + logMessage, "--", logMessage + " Should be clicked", "Click failed after " + maxRetries + " attempts");
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Failed to click on: " + logMessage, logMessage + " Should be clicked", "Click failed after " + maxRetries + " attempts");
        }
    }

    public <T> T retryClick(WebElement pageElement, String logMessage, int maxRetries, Supplier<T> conditionSupplier) {
        int attempts = 0;
        boolean isClicked = false;
        T result = null;
        while (!isClicked && attempts < maxRetries) {
            try {
                click(pageElement, logMessage);
                result = conditionSupplier.get();
                isClicked = true;
            } catch (Exception e) {
                attempts++;
            }
        }
        if (!isClicked) {
            AtuReports.failResults1("Failed to click on: " + logMessage, "--", logMessage + " Should be clicked", "Click failed after " + maxRetries + " attempts");
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Failed to click on: " + logMessage, logMessage + " Should be clicked", "Click failed after " + maxRetries + " attempts");
        }
        return result;
    }

    //Method to enter the text in the HTML body
    public void enterTextIntoInnerHTML(WebElement element, String text, String logMessage) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].innerHTML='" + text + "'", element);
            AtuReports.passResults1("Entering the " + logMessage, text, text + " should be entered ", text + " is entered successfully");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Entering the " + logMessage, text + " should be entered ", text + " is entered successfully");
        } catch (Exception e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Entering the " + logMessage, text + " should be entered ", this.catchException(e));
            AtuReports.failResults("Failed to Enter text in " + logMessage + " text Field ", text, " Text " + text + " should be entered in to " + logMessage + " text Field", this.catchException(e));
        }
    }

    //Skip the test based on time of execution
    public static void skipTestBasedOnTime(String timeZone, int startTime, int endTime) {
        DateTimeZone zone = DateTimeZone.forID(timeZone);
        DateTime dt = new DateTime(zone);
        int hours = dt.getHourOfDay();
        if (hours >= startTime || hours <= endTime) {
            AtuReports.info("Skip test if time is within " + startTime + " PM to " + endTime + " AM at " + timeZone, timeZone, "Skipping test, time is within " + startTime + " PM to " + endTime + " AM at: " + timeZone, "Test is skipped, time is within " + startTime + " PM to " + endTime + " AM at: " + timeZone);
            throw new SkipException("Skipping test");
        }
    }

    public void selectValueFromTheDropDownListByIndex(List<WebElement> elements, int index) {
        WebElement firstValue = elements.get(index);
        jsClick(firstValue, firstValue.getText());
    }

    public WebElement dynamicXpathWebElement(String locator, String... value) {
        String element = String.format(locator, Arrays.stream(value).toArray());
        return driver.findElement(By.xpath(element));
    }

    public List<WebElement> dynamicXpathListOfWebElements(String locator, String value) {
        String element = String.format(locator, value);
        return driver.findElements(By.xpath(element));
    }

    public List<WebElement> dynamicXpathListOfWebElementsForWebElement(WebElement elementActual, String locator, String value) {
        String element = String.format(locator, value);
        return elementActual.findElements(By.xpath(element));
    }

    public boolean verifyTextPresentInPage(String value) {
        return getPageSource().contains(value);
    }

    public void uploadFile(WebElement uploadElement, String fileName) {
        String filePath = System.getProperty("user.dir") + "/TestData/" + fileName;
        uploadElement.sendKeys(filePath);
        filePath = "";
    }

    public void uploadEmailAttachmentsFile(WebElement uploadElement, String fileName, boolean isFileWithinLimit) {
        if (isFileWithinLimit) {
            String filePath = System.getProperty("user.dir") + "/TestData/emailAttachment/within limit/" + fileName;
            uploadElement.sendKeys(filePath);
        } else {
            String filePath = System.getProperty("user.dir") + "/TestData/emailAttachment/outside limit/" + fileName;
            uploadElement.sendKeys(filePath);
        }
    }

    public void openAndSwitchToNewTab() {
        driver.switchTo().newWindow(WindowType.TAB);
    }

    // Generic method to clear the text
    public void clearTextUsingKeys(WebElement element, String logMessage) {
        try {
            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(Keys.DELETE);
            AtuReports.passResults1("Verify element is cleared " + logMessage, "--", logMessage + " should be cleared ",
                    logMessage + " is cleared");
            Reporter.LogEvent(Reporter.TestStatus.PASS, "Verify element is cleared " + logMessage,
                    logMessage + " should be cleared", logMessage + " is cleared");

        } catch (Exception e) {
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify element is cleared " + logMessage,
                    logMessage + " should be cleared ", catchException(e));
            AtuReports.failResults("Verify element is cleared " + logMessage, "--", logMessage + " should be cleared ",
                    catchException(e));
        }
    }

    // This method downloads a file from the given link and saves it in the Downloads directory with the given name
    public void downloadFile(String urlLink, String fileName) {
        new File(dirDownloads).mkdir();
        String filePath = dirDownloads + fileName;
        byte[] buffer = new byte[1024];
        int readByte = 0;
        try {
            URL url = new URL(urlLink);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            BufferedInputStream input = new BufferedInputStream(http.getInputStream());
            FileOutputStream outputFile = new FileOutputStream(filePath);
            BufferedOutputStream bufferOut = new BufferedOutputStream(outputFile, 1024);
            while ((readByte = input.read(buffer, 0, 1024)) >= 0) {
                bufferOut.write(buffer, 0, readByte);
            }
            bufferOut.close();
            input.close();
            AtuReports.passResults1("Verify " + urlLink + " is downloaded as " + fileName, "--", fileName + " should be downloaded",
                    fileName + " is downloaded");
        } catch (IOException io) {
            AtuReports.failResults("Verify " + urlLink + " is downloaded as " + fileName, "--", fileName + " should be downloaded",
                    catchException(io));
        }
    }

    // This method read the Downloaded pdf file and return the string output
    public String readPDF(String fileName) {
        String filePath = dirDownloads + fileName;
        String output = null;
        try {
            URL url = new URL("file:///" + filePath);
            InputStream is = url.openStream();
            BufferedInputStream file = new BufferedInputStream(is);
            PDDocument document = null;
            try {
                document = PDDocument.load(file);
                output = new PDFTextStripper().getText(document);
                AtuReports.passResults1("Verify PDF reader " + fileName, "--", fileName + " text should be extracted ",
                        fileName + " text is extracted");
            } finally {
                if (document != null) {
                    document.close();
                }
                file.close();
                is.close();
            }
        } catch (Exception e) {
            AtuReports.failResults("Verify PDF reader " + fileName, "--", fileName + " text should be extracted ",
                    catchException(e));
        }
        return output;
    }

    // This method will delete the given file from the Downloads folder
    public void deleteDownloadedFile(String fileName) {
        String filePath = dirDownloads + fileName;
        try {
            File file = new File(filePath);
            if (file.delete()) {
                AtuReports.passResults1("Verify downloaded file is deleted " + fileName, "--", fileName + " should be deleted ",
                        fileName + " is deleted");
            }
        } catch (Exception e) {
            AtuReports.failResults("Verify downloaded file is deleted " + fileName, "--", fileName + " should be deleted ",
                    catchException(e));
        }
    }

    public void selectOptionFromDropDown(String dynamicElement, String optionName) {
        try {
            WebElement optionElement = dynamicXpathWebElement(dynamicElement, optionName);
            waitUntilClickable(optionElement, "Option is clickable");
            click(optionElement, "Click on the Option");
        } catch (Exception e) {
            AtuReports.failResults("Option not exists", "", "", "Option Name is " + optionName);
        }
    }

    public void verifyUserNotPresentInList(String userList, String deletedUserName) {
        if (!userList.contains(deletedUserName)) {
            AtuReports.passResults1("verify user status:", "", deletedUserName + " should not be present ",
                    deletedUserName + " not present is " + !userList.contains(deletedUserName));
        } else {
            AtuReports.failResults1("verify user status:", "", deletedUserName + " is present ",
                    deletedUserName + " is Available " + userList.contains(deletedUserName));
        }
    }

    public void refreshCurrentPageNTimes(int count) {
        for (int i = 0; i < count; i++) {
            explicitWait(2);
            openURL(getAppCurrentUrl());
        }
    }

    public void skipInAllEnvironment() throws SkipWithReducedStackTrace {
        throw new SkipWithReducedStackTrace("Skipping all tests in this class.");
    }


    public void pressEscape() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            AtuReports.passResults1("press the escape", "", "window should be closed", "Window is closed"
            );
        } catch (AWTException e) {
            e.printStackTrace();
            AtuReports.failResults1(" release the escape", "", " window is not closed", "Window is not closed"
            );
        }
    }

    public boolean isItemListInAlphabeticalOrder(List<String> listItems) {
        boolean isItemListInAlphabeticalOrder = false;
        for (int index = 2; index < listItems.size(); index++) {
            String currentKeyValue = listItems.get(index);
            String previousKeyValue = listItems.get(index - 1);
            int minLength = Math.min(currentKeyValue.length(), previousKeyValue.length());
            for (int charIndex = 0; charIndex < minLength; charIndex++) {
                char currentChar = Character.toLowerCase(currentKeyValue.charAt(charIndex));
                char previousChar = Character.toLowerCase(previousKeyValue.charAt(charIndex));
                if (currentChar < previousChar) {
                    break;
                }
            }
            if (index == listItems.size() - 1) {
                isItemListInAlphabeticalOrder = true;
            }
        }
        return isItemListInAlphabeticalOrder;
    }

    public static String generateRandomCharsByParam(int characters) {
        //generating random characters of x length
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < characters; ++i) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }
        return sb.toString();
    }

    public boolean isElementExists(By by) {
        boolean isExists = true;
        try {
            driver.findElement(by);
        } catch (NoSuchElementException e) {
            isExists = false;
        }
        return isExists;
    }

    // Generic Method to select one or more Names in Filter
    public void selectOneOrMoreNamesFromTheFilterList(List<WebElement> elements, String[] selectedNames) {
        for (String selectedName : selectedNames) {
            selectValueFromTheDropDownList(elements, selectedName);
        }
    }

    public void removeMouseHover(WebElement element) {
        try {
            String javaScript = "var evObj = document.createEvent('MouseEvents');" + "evObj.initMouseEvent('mouseout', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" + "arguments[0].dispatchEvent(evObj);";
            ((JavascriptExecutor) this.driver).executeScript(javaScript, new Object[]{element});
        } catch (Exception var3) {
            Exception e = var3;
            AtuReports.failResults("Verify removal of Mouse hover action", "--", "Should be able to remove hover", "Mouse hover removal is not success: " + this.catchException(e));
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify removal of Mouse hover action", "Should be able to remove hover", this.catchException(e));
        }
    }

    public int getNumericValueFromString(String string)
    {
        StringBuilder numericValue = new StringBuilder();
        boolean foundNumeric = false;
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c)) {
                numericValue.append(c);
                foundNumeric = true;
            }
            else if (foundNumeric)
            {
                break;
            }
        }
        int value = Integer.parseInt(numericValue.toString());
        return value;
    }

    /*     input is string value and format of the string eg: Sat, Nov 30, 2024, format is EEE, MMM dd, yyyy
         output format is always yyyy-mm-dd*/
    public LocalDate convertStringToLocalDate(String stringValue, String formatOfDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatOfDate);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("MM-dd-yyyy");
        LocalDate formatToLocalDate = null;
        try {
            Date date = dateFormat.parse(stringValue);
            String formattedDate = expectedFormat.format(date);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            formatToLocalDate = LocalDate.parse(formattedDate, formatter);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return formatToLocalDate;
    }

    //format of dates conversion. eg: MM-dd-yyyy to MMM dd
    public String convertDateFormats(String actualDate, String inputFormatDt, String outputFormatDt) {
        DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern(inputFormatDt);
        DateTimeFormatter outputDateFormat = DateTimeFormatter.ofPattern(outputFormatDt);
        String formattedDate = null;
        try {
            LocalDate date = LocalDate.parse(actualDate, inputDateFormat);
            formattedDate = date.format(outputDateFormat);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return formattedDate;
    }

    public int getPreviousYear() {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(1, -1);
        return prevYear.get(1);
    }

    public Map<String, String> monthFormattedToNumber() {
        Map<String, String> monthMapping = new HashMap<>();
        monthMapping.put("Jan", "01");
        monthMapping.put("Feb", "02");
        monthMapping.put("Mar", "03");
        monthMapping.put("Apr", "04");
        monthMapping.put("May", "05");
        monthMapping.put("Jun", "06");
        monthMapping.put("Jul", "07");
        monthMapping.put("Aug", "08");
        monthMapping.put("Sep", "09");
        monthMapping.put("Oct", "10");
        monthMapping.put("Nov", "11");
        monthMapping.put("Dec", "12");
        return monthMapping;
    }

    public void retrySendKeysInEmailEditor(WebElement emailIframe, WebElement editorElement, String emailBodyText, WebElement sendBtnElement) {
        int count = 0;
        do {
            switchToIFrameXpath(emailIframe);
            isElementDisplayed(editorElement, "Email text editor");
            jsClick(editorElement, "Email editor");
            sendKeys(editorElement, emailBodyText, "email body");
            count++;
            if (count == 3) {
                break;
            }
            switchToDefault();
        } while (!sendBtnElement.getAttribute("class").contains("disabled"));
    }

    public boolean checkEllipses(List<WebElement> elements) {
        // Check if the ellipsis is present using JavaScript execution
        for (WebElement element : elements) {
            // Javascript to check if text-overflow is 'ellipsis' and content overflows
            String jsScript = "return (arguments[0].offsetWidth < arguments[0].scrollWidth) && window.getComputedStyle(arguments[0]).textOverflow === 'ellipsis';";
            // Execute JavaScript and capture the result
            Boolean isEllipsesDisplayed = (Boolean) ((JavascriptExecutor) driver).executeScript(jsScript, element);
            if (isEllipsesDisplayed) {
                return true;
            }
        }
        return false;
    }

    public String formatStringToAddCommas(String strValue) {
        String reversed = new StringBuilder(strValue).reverse().toString();
        StringBuilder resultVal = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            resultVal.append(reversed.charAt(i));
            if ((i + 1) % 3 == 0 && i != reversed.length() - 1) {
                resultVal.append(',');
            }
        }
        return resultVal.reverse().toString();
    }

    /* For Computed Style elements i.e if getCSS() doesnt return CSS, get the CSS value of elements related to the locator element in rbga
    elementRelation should be nextElementSibling / previousElementSibling for siblings elements
    elementRelation should be parentElement for parent elements */
    public String fetchRelatedElementCSSValue(String elementRelation, String cssValueExpected, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String rgbaColor = (String) js.executeScript(
                "let color = window.getComputedStyle(arguments[0]." + elementRelation + ").getPropertyValue('" + cssValueExpected + "');" +
                        "if (!color.includes('rgba')) { " +
                        "   let rgbValues = color.match(/\\d+/g);" +
                        "   return `rgba(${rgbValues[0]}, ${rgbValues[1]}, ${rgbValues[2]}, 1)`;" +
                        "} return color;",
                element
        );
        return rgbaColor;
    }

    //suffix th|st|nd|rd based on dates
    public String addSuffixToDays(String formattedDate) {
        int dayToSuffix = Integer.parseInt(formattedDate);
        String suffix;
        if (dayToSuffix >= 11 && dayToSuffix <= 13) {
            suffix = "th";
        } else {
            switch (dayToSuffix % 10) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
                default:
                    suffix = "th";
            }
        }
        return dayToSuffix + suffix;
    }

    public void verticalScrollStatus() {
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        if ((Boolean) javascript.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;")) {
            AtuReports.passResults1("Verify vertical scroll bar should be displayed", "",
                    "Vertical scroll bar should be displayed",
                    "Vertical scroll bar is displayed");
        }
    }

    public void maximizeWindow() {
        driver.manage().window().fullscreen();
    }

    public void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            AtuReports.failResults("Verify element is in the view", "--", "Should be visible in the browser window", "Element is not visible in the window: " + this.catchException(e));
            Reporter.LogEvent(Reporter.TestStatus.FAIL, "Verify element is in the view", "Should be visible in the browser window", this.catchException(e));
        }
    }

    public String convertTimeBasedOnTimeZone(String date, String time, String actualTimeZone, String convertedTimeZone) {
        String formatDate = "";
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, MMM dd yyyy h:mm a", Locale.ENGLISH);
            String text = date + " " + Year.now() + " " + time;
            LocalDateTime localDateTime = LocalDateTime.parse(text, dtf);
            List<ZoneId> zoneIdVal = getZoneId(actualTimeZone);
            List<ZoneId> zoneTimeVal = getZoneId(convertedTimeZone);
            ZonedDateTime actualTime = localDateTime.atZone(zoneIdVal.get(0));
            ZonedDateTime convertedTime = actualTime.withZoneSameInstant(zoneTimeVal.get(1));
            DateTimeFormatter inputFor = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
            formatDate = convertedTime.format(inputFor);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return formatDate.toLowerCase();
    }

    public List<ZoneId> getZoneId(String text) {
        List<ZoneId> returnValues = new ArrayList<>();
        ZoneId zoneId;
        switch (text) {
            case "Eastern":
                // Set the time zone to Eastern Standard Time (EST)
                zoneId = ZoneId.of("America/New_York");
                ZoneId estTime = zoneId;
                returnValues.add(zoneId);
                returnValues.add(estTime);
                break;
            case "Pacific":
                // Set the time zone to Pacific Standard Time (PST)
                zoneId = ZoneId.of("America/Los_Angeles");
                ZoneId pstTime = zoneId;
                returnValues.add(zoneId);
                returnValues.add(pstTime);
                break;
            case "Central":
                // Set the time zone to Central Standard Time (CST)
                zoneId = ZoneId.of("America/Chicago");
                ZoneId cstTime = zoneId;
                returnValues.add(zoneId);
                returnValues.add(cstTime);
                break;
            case "Mountain":
                // Set the time zone to Mountain Standard Time (MST)
                zoneId = ZoneId.of("America/Denver");
                ZoneId mstTime = zoneId;
                returnValues.add(zoneId);
                returnValues.add(mstTime);
                break;
            default:
                // Return an empty list if the time zone is invalid
                return returnValues;
        }
        return returnValues;
    }

    public Map<String, String> timeZoneBasedOnPropertyState() {
        Map<String, String> propertyMapping = new HashMap<>();
        propertyMapping.put("New York", "Eastern");
        propertyMapping.put("Massachusetts", "Eastern");
        propertyMapping.put("Washington", "Pacific");
        propertyMapping.put("California", "Pacific");
        propertyMapping.put("Texas", "Central");
        propertyMapping.put("Illinois", "Central");
        propertyMapping.put("Denver", "Mountain");
        return propertyMapping;
    }

    public String convertStringToReqdDateFormats(String actualDate, String inputFormatDt, String outputFormatDt) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormatDt);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormatDt);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String formattedDate = null;
        try {
            Date date = inputDateFormat.parse(actualDate);
            formattedDate = outputDateFormat.format(date);
            int isYearValPresent = formattedDate.length() - formattedDate.replace("/", "").length();
            if (isYearValPresent == 2 && Integer.parseInt(formattedDate.split("/")[2]) < currentYear) {
                formattedDate = formattedDate.replace(formattedDate.split("/")[2], String.valueOf(currentYear));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return formattedDate;
    }

    //if list contains "-" or has equal values ignore those and compare next in list
    public boolean verifyListAscending(List<WebElement> list) {
        String previousEleText = null;
        boolean isAscending = true;
        for (int i = 1; i < list.size(); ++i) {
            String currentEleText = list.get(i).getText().trim();
            if (currentEleText.equals("-") || currentEleText.isEmpty()) {
                continue;
            }
            if (list.get(i - 1).getText().compareToIgnoreCase(list.get(i).getText()) > 0) {
                isAscending = false;
                break;
            }
            previousEleText = currentEleText;
        }
        return true;
    }

    public boolean verifyListDescending(List<WebElement> list) {
        String previousEleText = null;
        boolean isDescending = true;
        for (int i = 1; i < list.size(); ++i) {
            String currentEleText = list.get(i).getText().trim();
            if (currentEleText.equals("-") || currentEleText.isEmpty()) {
                continue;
            }
            if (list.get(i - 1).getText().compareToIgnoreCase(list.get(i).getText()) < 0) {
                isDescending = false;
                break;
            }
            previousEleText = currentEleText;
        }
        return true;
    }

    /*
    elementRelation should be nextElementSibling / previousElementSibling for siblings elements
    elementRelation should be parentElement for parent elements */
    public String fetchRelatedElementAttributeValue(String elementRelation, String attributeName, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String attributeVal = "";
        if (attributeName.contains("innerText") || attributeName.contains("textContent")) {
            attributeVal = (String) js.executeScript(
                    "return arguments[0]." + elementRelation + "?.[arguments[1]]", element, attributeName
            );
        } else {
            attributeVal = (String) js.executeScript(
                    "return arguments[0]." + elementRelation + "?.getAttribute(arguments[1])", element, attributeName
            );
        }
        return attributeVal;
    }

    public String getDateFormatBasedOnString(String actualDate) {
        List<String> knownFormatPatterns = new ArrayList<>();
        knownFormatPatterns.add("dd/MM/yyyy");
        knownFormatPatterns.add("MM/dd/yyyy");
        knownFormatPatterns.add("yyyy-MM-dd");
        knownFormatPatterns.add("dd-MM-yyyy");
        knownFormatPatterns.add("yyyy/MM/dd");
        knownFormatPatterns.add("dd MM yyyy");
        knownFormatPatterns.add("dd.MM.yyyy");
        knownFormatPatterns.add("MMM dd");
        knownFormatPatterns.add("MMM dd yyyy");
        for (String pattern : knownFormatPatterns) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            try {
                Date date = sdf.parse(actualDate);
                return pattern;
            } catch (Exception e) {
                //Parse exception is expected for the patterns in the list which do not match until matched pattern
                //e.printStackTrace();
            }
        }
        return null;
    }

    public String convert12HourFormatTo24HourFormat(String actualTimeVal) {
        String formattedTime = actualTimeVal.trim().replaceAll("\\s+", "");
        try {
            if (formattedTime.toLowerCase().endsWith("am") || formattedTime.toLowerCase().endsWith("pm")) {
                formattedTime = formattedTime.replaceAll("(?i)(am|pm)", " $1").toUpperCase();
                SimpleDateFormat twelveHourTime = new SimpleDateFormat("h:m a");
                SimpleDateFormat twentyFourHourTime = new SimpleDateFormat("H:mm");
                Date date = twelveHourTime.parse(formattedTime);
                return twentyFourHourTime.format(date);
            } else { // No AM/PM -> return as is
                return formattedTime;
            }
        } catch (Exception e) {
            return "Incorrect date format";
        }
    }

    //generic method to select enabled dates from calender
    public String selectFutureDate(WebElement calendarDate, List<WebElement> enabledDates) {
        int tomorrowDay = LocalDate.now().plusDays(1).getDayOfMonth();
        String selectedDateText = null;
        jsClick(calendarDate, "Calendar Date");
        explicitWait(3);
        for (WebElement date : enabledDates) {
            String dateText = date.getText().trim();
            if (!dateText.isEmpty()) {
                int day = Integer.parseInt(dateText);
                if (day >= tomorrowDay) {
                    selectedDateText = dateText;
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", date);
                    break;
                }
            }
        }
        return selectedDateText;
    }

    public String selectFirstOptionFromTheDropdown(WebElement element) {
        Select dropdown = new Select(element);
        dropdown.selectByIndex(0);
        WebElement selectedOption = dropdown.getFirstSelectedOption();
        explicitWait(1);
        return selectedOption.getText();
    }

    public String expandDayToFull(String shortDay) {
        shortDay = shortDay.length() > 3 ? shortDay.substring(0, 3) : shortDay;
        DayOfWeek day = null;
        if (shortDay.equalsIgnoreCase("MON")) {
            day = DayOfWeek.MONDAY;
        } else if (shortDay.equalsIgnoreCase("TUE")) {
            day = DayOfWeek.TUESDAY;
        } else if (shortDay.equalsIgnoreCase("WED")) {
            day = DayOfWeek.WEDNESDAY;
        } else if (shortDay.equalsIgnoreCase("THU")) {
            day = DayOfWeek.THURSDAY;
        } else if (shortDay.equalsIgnoreCase("FRI")) {
            day = DayOfWeek.FRIDAY;
        } else if (shortDay.equalsIgnoreCase("SAT")) {
            day = DayOfWeek.SATURDAY;
        } else if (shortDay.equalsIgnoreCase("SUN")) {
            day = DayOfWeek.SUNDAY;
        }
        String fullDay = day.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return fullDay;
    }

    protected boolean anyElementInListContainsText(List<WebElement> elements, String text) {
        return elements.stream()
                .anyMatch(e -> e.getText().trim().contains(text));
    }

    protected boolean allElementInListContainsText(List<WebElement> elements, String text) {
        return elements.stream()
                .allMatch(e -> e.getText().trim().contains(text));
    }
}