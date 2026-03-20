package selenium_shutterbug.assertthat.selenium_shutterbug.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.image.ImageProcessor;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.ElementOutsideViewportException;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.Coordinates;

import java.awt.*;
import java.awt.image.RasterFormatException;

public class PageSnapshot extends Snapshot {

    PageSnapshot(WebDriver driver, Double devicePixelRatio) {
        this.driver = driver;
        this.devicePixelRatio = devicePixelRatio;
    }

    public PageSnapshot highlight(WebElement element) {
        try {
            this.highlight(element, Color.red, 3);
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot highlight(WebElement element, Color color, int lineWidth) {
        try {
            this.image = ImageProcessor.highlight(this.image, new Coordinates(element, this.devicePixelRatio), color, lineWidth);
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot highlightWithText(WebElement element, String text) {
        try {
            this.highlightWithText(element, Color.red, 3, text, Color.red, new Font("Serif", 1, 20));
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot highlightWithText(WebElement element, Color elementColor, int lineWidth, String text, Color textColor, Font textFont) {
        try {
            this.highlight(element, elementColor, 0);
            Coordinates coords = new Coordinates(element, this.devicePixelRatio);
            this.image = ImageProcessor.addText(this.image, coords.getX(), coords.getY() - textFont.getSize() / 2, text, textColor, textFont);
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot blur() {
        this.image = ImageProcessor.blur(this.image);
        return this;
    }

    public PageSnapshot blur(WebElement element) {
        try {
            this.image = ImageProcessor.blurArea(this.image, new Coordinates(element, this.devicePixelRatio));
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot monochrome(WebElement element) {
        try {
            this.image = ImageProcessor.monochromeArea(this.image, new Coordinates(element, this.devicePixelRatio));
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot blurExcept(WebElement element) {
        try {
            this.image = ImageProcessor.blurExceptArea(this.image, new Coordinates(element, this.devicePixelRatio));
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot cropAround(WebElement element, int offsetX, int offsetY) {
        try {
            this.image = ImageProcessor.cropAround(this.image, new Coordinates(element, this.devicePixelRatio), offsetX, offsetY);
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot cutOut(int offsetX, int offsetY, WebElement... elements) {
        try {
            for(WebElement element : elements) {
                ImageProcessor.cutOut(this.image, new Coordinates(element, this.devicePixelRatio), offsetX, offsetY);
            }
            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    public PageSnapshot cutOut(WebElement... elements) {
        try {
            for(WebElement element : elements) {
                ImageProcessor.cutOut(this.image, new Coordinates(element, this.devicePixelRatio), 0, 0);
            }

            return this;
        } catch (RasterFormatException rfe) {
            throw new ElementOutsideViewportException("Requested element is outside the viewport", rfe);
        }
    }

    protected PageSnapshot self() {
        return this;
    }
}
