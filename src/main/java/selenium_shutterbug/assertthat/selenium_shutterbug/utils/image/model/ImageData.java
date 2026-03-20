package selenium_shutterbug.assertthat.selenium_shutterbug.utils.image.model;

import selenium_shutterbug.assertthat.selenium_shutterbug.utils.file.FileUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.text.DecimalFormat;

public class ImageData {
    private final int RED_RGB = (new Color(255, 0, 0)).getRGB();
    private final BufferedImage image;
    private final int width;
    private final int height;

    public ImageData(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth((ImageObserver) null);
        this.height = image.getHeight((ImageObserver) null);
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean notEqualsDimensions(ImageData imageData) {
        return !this.equalsDimensions(imageData);
    }

    private boolean equalsDimensions(ImageData imageData) {
        return this.width == imageData.width && this.height == imageData.height;
    }

    public boolean equalsEachPixelsWithCreateDifferencesImage(ImageData imageData, double deviation, String pathDifferenceImageFileName) {
        return this.equalsEachPixelsWithCreateDifferencesImage(imageData.getImage(), deviation, pathDifferenceImageFileName);
    }

    private boolean equalsEachPixelsWithCreateDifferencesImage(BufferedImage image, double deviation, String pathDifferenceImageFileName) {
        boolean isEqual = this.equalsEachPixels(image, deviation);
        if (!isEqual) {
            this.createDifferencesImage(image, pathDifferenceImageFileName);
        }
        return isEqual;
    }

    private void createDifferencesImage(BufferedImage image, String pathDifferenceImageFileName) {
        BufferedImage output = new BufferedImage(this.width, this.height, 1);

        for (int y = 0; y < this.height; ++y) {
            for (int x = 0; x < this.width; ++x) {
                int rgb1 = this.getImage().getRGB(x, y);
                int rgb2 = image.getRGB(x, y);
                if (rgb1 != rgb2) {
                    output.setRGB(x, y, this.RED_RGB & rgb1);
                } else {
                    output.setRGB(x, y, rgb1);
                }
            }
        }

        FileUtil.writeImage(output, "png", new File(pathDifferenceImageFileName + ".png"));
    }

    public boolean equalsEachPixels(ImageData imageData, double deviation) {
        return this.equalsEachPixels(imageData.getImage(), deviation);
    }

    private boolean equalsEachPixels(BufferedImage image, double deviation) {
        double p = this.calculatePixelsDifference(image);
        boolean areEqual = p == (double) 0.0F || p <= deviation;
        if (!areEqual) {
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);
            System.out.println("[INFO] Allowed deviation: " + df.format(deviation));
            System.out.println("[INFO] Actual deviation: " + df.format(p));
        }
        return areEqual;
    }

    private double calculatePixelsDifference(BufferedImage image) {
        long diff = 0L;

        for (int y = 0; y < this.height; ++y) {
            for (int x = 0; x < this.width; ++x) {
                int rgb1 = this.getImage().getRGB(x, y);
                int rgb2 = image.getRGB(x, y);
                int r1 = rgb1 >> 16 & 255;
                int g1 = rgb1 >> 8 & 255;
                int b1 = rgb1 & 255;
                int r2 = rgb2 >> 16 & 255;
                int g2 = rgb2 >> 8 & 255;
                int b2 = rgb2 & 255;
                diff += (long) Math.abs(r1 - r2);
                diff += (long) Math.abs(g1 - g2);
                diff += (long) Math.abs(b1 - b2);
            }
        }

        double n = (double) (this.width * this.height * 3);
        return (double) diff / n / (double) 255.0F;
    }
}