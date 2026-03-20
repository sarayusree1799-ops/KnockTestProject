package selenium_shutterbug.assertthat.selenium_shutterbug.utils.web;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class Coordinates {
    private final int width;
    private final int scrollWidth;
    private final int height;
    private final int scrollHeight;
    private final int x;
    private final int y;
    private final int absoluteX;
    private final int absoluteY;

    public Coordinates(WebElement element, Double devicePixelRatio) {
        Point point = element.getLocation();
        Dimension size = element.getSize();
        this.width = (int) ((double) size.getWidth() * devicePixelRatio);
        this.height = (int) ((double) size.getHeight() * devicePixelRatio);
        this.x = (int) ((double) point.getX() * devicePixelRatio);
        this.y = (int) ((double) point.getY() * devicePixelRatio);
        this.scrollWidth = (int) ((double) size.getWidth() * devicePixelRatio);
        this.scrollHeight = (int) ((double) size.getHeight() * devicePixelRatio);
        this.absoluteX = (int) ((double) point.getX() * devicePixelRatio);
        this.absoluteY = (int) ((double) point.getY() * devicePixelRatio);
    }

    public Coordinates(Point absoluteLocation, Point currentLocation, Dimension size, Dimension scrollableSize, Double devicePixelRatio) {
        this.width = (int) ((double) size.getWidth() * devicePixelRatio);
        this.height = (int) ((double) size.getHeight() * devicePixelRatio);
        this.absoluteX = (int) ((double) absoluteLocation.getX() * devicePixelRatio);
        this.absoluteY = (int) ((double) absoluteLocation.getY() * devicePixelRatio);
        this.x = (int) ((double) currentLocation.getX() * devicePixelRatio);
        this.y = (int) ((double) currentLocation.getY() * devicePixelRatio);
        this.scrollWidth = (int) ((double) scrollableSize.getWidth() * devicePixelRatio);
        this.scrollHeight = (int) ((double) scrollableSize.getHeight() * devicePixelRatio);
    }

    public int getAbsoluteX() {
        return this.absoluteX;
    }

    public int getAbsoluteY() {
        return this.absoluteY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getScrollHeight() {
        return this.scrollHeight;
    }

    public int getScrollWidth() {
        return this.scrollWidth;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
