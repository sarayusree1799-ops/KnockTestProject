package selenium_shutterbug.assertthat.selenium_shutterbug.utils.image;

import selenium_shutterbug.assertthat.selenium_shutterbug.utils.image.model.ImageData;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.Coordinates;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageProcessor {
    private static final int ARCH_SIZE = 10;
    private static final float[] matrix = new float[49];
    private static double pixelError = Double.MAX_VALUE;

    public static BufferedImage blur(BufferedImage sourceImage) {
        BufferedImageOp options = new ConvolveOp(new Kernel(7, 7, matrix), 1, (RenderingHints) null);
        return options.filter(sourceImage, (BufferedImage) null);
    }

    public static BufferedImage highlight(BufferedImage sourceImage, Coordinates coords, Color color, int lineWidth) {
        byte defaultLineWidth = 3;
        Graphics2D g = sourceImage.createGraphics();
        g.setPaint(color);
        g.setStroke(new BasicStroke(lineWidth == 0 ? (float) defaultLineWidth : (float) lineWidth));
        g.drawRoundRect(coords.getX(), coords.getY(), coords.getWidth(), coords.getHeight(), 10, 10);
        g.dispose();
        return sourceImage;
    }

    public static BufferedImage addText(BufferedImage sourceImage, int x, int y, String text, Color color, Font font) {
        Graphics2D g = sourceImage.createGraphics();
        g.setPaint(color);
        g.setFont(font);
        g.drawString(text, x, y);
        g.dispose();
        return sourceImage;
    }

    public static BufferedImage getElement(BufferedImage sourceImage, Coordinates coords) {
        return sourceImage.getSubimage(coords.getX(), coords.getY(), coords.getWidth(), coords.getHeight());
    }

    public static BufferedImage blurArea(BufferedImage sourceImage, Coordinates coords) {
        BufferedImage blurredImage = blur(sourceImage.getSubimage(coords.getX(), coords.getY(), coords.getWidth(), coords.getHeight()));
        return getBufferedImage(sourceImage, coords, blurredImage, sourceImage);
    }

    public static BufferedImage monochromeArea(BufferedImage sourceImage, Coordinates coords) {
        BufferedImage monochromedImage = convertToGrayAndWhite(sourceImage.getSubimage(coords.getX(), coords.getY(), coords.getWidth(), coords.getHeight()));
        return getBufferedImage(sourceImage, coords, monochromedImage, sourceImage);
    }

    public static BufferedImage blurExceptArea(BufferedImage sourceImage, Coordinates coords) {
        BufferedImage subImage = sourceImage.getSubimage(coords.getX(), coords.getY(), coords.getWidth(), coords.getHeight());
        BufferedImage blurredImage = blur(sourceImage);
        return getBufferedImage(sourceImage, coords, subImage, blurredImage);
    }

    private static BufferedImage getBufferedImage(BufferedImage sourceImage, Coordinates coords, BufferedImage subImage, BufferedImage blurredImage) {
        BufferedImage combined = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), 2);
        Graphics2D g = combined.createGraphics();
        g.drawImage(blurredImage, 0, 0, (ImageObserver) null);
        g.drawImage(subImage, coords.getX(), coords.getY(), (ImageObserver) null);
        g.dispose();
        return combined;
    }

    public static BufferedImage cropAround(BufferedImage sourceImage, Coordinates coords, int offsetX, int offsetY) {
        return sourceImage.getSubimage(coords.getX() - offsetX, coords.getY() - offsetY, coords.getScrollWidth() + offsetX * 2, coords.getHeight() + offsetY * 2);
    }

    public static BufferedImage cutOut(BufferedImage sourceImage, Coordinates coords, int offsetX, int offsetY) {
        Graphics bg = sourceImage.getGraphics();
        bg.setColor(Color.white);
        bg.fillRect(coords.getX() - offsetX, coords.getY() - offsetY, coords.getScrollWidth() + offsetX * 2, coords.getScrollHeight() + offsetY * 2);
        return sourceImage;
    }

    public static BufferedImage addTitle(BufferedImage sourceImage, String title, Color color, Font textFont) {
        int textOffset = 5;
        BufferedImage combined = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight() + textFont.getSize() + textOffset, 2);
        Graphics2D g = combined.createGraphics();
        g.drawImage(sourceImage, 0, textFont.getSize() + textOffset, (ImageObserver) null);
        addText(combined, 0, textFont.getSize(), title, color, textFont);
        g.dispose();
        return combined;
    }

    public static BufferedImage convertToGrayAndWhite(BufferedImage sourceImage) {
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(1003), (RenderingHints) null);
        op.filter(sourceImage, sourceImage);
        return sourceImage;
    }

    public static boolean imagesAreEquals(BufferedImage image1, BufferedImage image2, double deviation) {
        ImageData image1Data = new ImageData(image1);
        ImageData image2Data = new ImageData(image2);
        if (image1Data.notEqualsDimensions(image2Data)) {
            throw new UnableToCompareImagesException("Images dimensions mismatch: image1 - " + image1Data.getWidth()
                    + "x" + image1Data.getHeight()
                    + "; image2 - " + image2Data.getWidth() + "x" + image2Data.getHeight());
        } else {
            return image1Data.equalsEachPixels(image2Data, deviation);
        }
    }

    public static boolean imagesAreEqualsWithDiff(BufferedImage image1, BufferedImage image2, String pathFileName, double deviation) {
        ImageData image1Data = new ImageData(image1);
        ImageData image2Data = new ImageData(image2);
        if (image1Data.notEqualsDimensions(image2Data)) {
            throw new UnableToCompareImagesException("Images dimensions mismatch: image1 - " + image1Data.getWidth()
                    + "x" + image1Data.getHeight()
                    + "; image2 - " + image2Data.getWidth() + "x" + image2Data.getHeight());
        } else {
            return image1Data.equalsEachPixelsWithCreateDifferencesImage(image2Data, deviation, pathFileName);
        }
    }

    public static BufferedImage scale(BufferedImage source, double ratio) {
        return cropAndScale(source, ratio, (double) 1.0F, (double) 1.0F);
    }

    public static BufferedImage cropAndScale(BufferedImage source, double ratio, double cropWidth, double cropHeight) {
        int w = (int) ((double) source.getWidth() * ratio);
        int h = (int) ((double) source.getHeight() * ratio);
        BufferedImage scaledImage = createAndDrawImage(source, w, h);
        return scaledImage.getSubimage(0, 0, (int) ((double) w * cropWidth), (int) ((double) h * cropHeight));
    }

    public static BufferedImage cropAndScale(BufferedImage source, double ratio, int maxWidth, int maxHeight) {
        int w = (int) ((double) source.getWidth() * ratio);
        int h = (int) ((double) source.getHeight() * ratio);
        BufferedImage scaledImage = createAndDrawImage(source, w, h);

        if (maxWidth != -1 && w > maxWidth) {
            w = maxWidth;
        }

        if (maxHeight != -1 && h > maxHeight) {
            h = maxHeight;
        }

        return scaledImage.getSubimage(0, 0, w, h);
    }

    private static BufferedImage createAndDrawImage(BufferedImage source, int w, int h) {
        BufferedImage scaledImage = getCompatibleImage(w, h, source);
        Graphics2D resultGraphics = scaledImage.createGraphics();
        resultGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        resultGraphics.drawImage(source, 0, 0, w, h, (ImageObserver) null);
        resultGraphics.dispose();
        return scaledImage;
    }

    private static BufferedImage getCompatibleImage(int w, int h, BufferedImage source) {
        BufferedImage bImage = null;

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            bImage = gc.createCompatibleImage(w, h);
        } catch (HeadlessException var7) {
        }
        if (bImage == null) {
            boolean hasAlpha = hasAlpha(source);
            int type = 1;
            if (hasAlpha) {
                type = 2;
            }
            bImage = new BufferedImage(w, h, type);
        }
        return bImage;
    }

    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bImage = (BufferedImage) image;
            return bImage.getColorModel().hasAlpha();
        } else {
            PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);

            try {
                pg.grabPixels();
            } catch (InterruptedException var3) {
            }

            ColorModel cm = pg.getColorModel();
            return cm.hasAlpha();
        }
    }

    public static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);

        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        for (int i = 0; i < 49; ++i) {
            matrix[i] = 0.020408163F;
        }
    }
}