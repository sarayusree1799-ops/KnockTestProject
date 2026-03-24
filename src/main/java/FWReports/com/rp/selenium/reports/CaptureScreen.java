




package FWReports.com.rp.selenium.reports;

import org.openqa.selenium.WebElement;

public class CaptureScreen {

    private boolean captureBrowserPage = false;
    private boolean captureDesktop = false;
    private boolean captureWebElement = false;
    private boolean captureMobilePage = false;
    private WebElement element = null;

    public CaptureScreen(WebElement paramWebElement) {
      if (paramWebElement != null) {
          this.setCaptureWebElement(true);
          this.setElement(paramWebElement);
      }
    }

    public CaptureScreen(ScreenshotOf paramScreenshotOf) {
      if (paramScreenshotOf == CaptureScreen.ScreenshotOf.BROWSER_PAGE) {
            this.setCaptureBrowserPage(true);
        } else if (paramScreenshotOf == CaptureScreen.ScreenshotOf.DESKTOP) {
            this.setCaptureDesktop(true);
        } else if (paramScreenshotOf == CaptureScreen.ScreenshotOf.MOBILE) {
            this.setCaptureMobilePage(true);
      }
    }

    public boolean isCaptureBrowserPage() {
        return this.captureBrowserPage;
    }

    public boolean isCaptureMobilePage() {
        return this.captureMobilePage;
    }

    public void setCaptureBrowserPage(boolean paramBoolean) {
        this.captureBrowserPage = paramBoolean;
    }

    public void setCaptureMobilePage(boolean paramBoolean) {
        this.captureMobilePage = paramBoolean;
    }

    public boolean isCaptureDesktop() {
        return this.captureDesktop;
    }

    public boolean setCaptureDesktop(boolean paramBoolean) {
        this.captureDesktop = paramBoolean;
        return this.captureDesktop;
    }

    public boolean isCaptureWebElement() {
        return this.captureWebElement;
    }

    public void setCaptureWebElement(boolean paramBoolean) {
        this.captureWebElement = paramBoolean;
    }

    public WebElement getElement() {
        return this.element;
    }

    public void setElement(WebElement paramWebElement) {
        this.element = paramWebElement;
    }

    public static enum ScreenshotOf {
        BROWSER_PAGE,
        DESKTOP,
        MOBILE;
    }
}
