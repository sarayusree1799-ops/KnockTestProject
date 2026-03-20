package selenium_shutterbug.assertthat.selenium_shutterbug.core;

import org.openqa.selenium.WebDriver;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.image.ImageProcessor;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.Coordinates;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.ElementOutsideViewportException;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

public class ElementSnapshot extends Snapshot {
    ElementSnapshot(WebDriver driver, Double devicePixelRatio) {
        this.driver = driver;
        this.devicePixelRatio = devicePixelRatio;
    }

    protected void setImage(BufferedImage image, Coordinates coords) {
        try {
            this.self().image = ImageProcessor.getElement(image, coords);
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    protected ElementSnapshot self() {
        return this;
    }
}