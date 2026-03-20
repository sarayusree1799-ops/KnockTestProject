package selenium_shutterbug.assertthat.selenium_shutterbug.core;

import org.openqa.selenium.WebDriver;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.file.FileUtil;
import selenium_shutterbug.assertthat.selenium_shutterbug.utils.image.ImageProcessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Snapshot<T extends Snapshot> {

    static final String ELEMENT_OUT_OF_VIEWPORT_EX_MESSAGE = "Requested element is outside the viewport";
    private static final String EXTENSION = "PNG";
    protected BufferedImage image;
    private BufferedImage thumbnailImage;
    WebDriver driver;
    Double devicePixelRatio = (double)1.0F;
    private String fileName = (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS")).format(new Date()) + "." + "PNG".toLowerCase();
    private Path location = Paths.get("./screenshots/");
    private String title;

    protected abstract T self();

    public T withName(String name) {
        if (name != null) {
            this.fileName = name + "." + "PNG".toLowerCase();
        }
        return (T)this.self();
    }

    public T withTitle(String title) {
        this.title = title;
        this.image = ImageProcessor.addTitle(this.image, title, Color.red, new Font("Serif", 1, 20));
        return (T)this.self();
    }

    public T withThumbnail(String path, String name, double scale) {
        File thumbnailFile = new File(path, name);
        if (!Files.exists(Paths.get(path), new LinkOption[0])) {
            thumbnailFile.mkdirs();
        }
        this.thumbnailImage = ImageProcessor.scale(this.image, scale);
        FileUtil.writeImage(this.thumbnailImage, "PNG", thumbnailFile);
        return (T)this.self();
    }

    public T withCroppedThumbnail(String path, String name, double scale, double cropWidth, double cropHeight) {
        File thumbnailFile = this.getFile(path, name);
        this.thumbnailImage = ImageProcessor.cropAndScale(this.image, scale, cropWidth, cropHeight);
        FileUtil.writeImage(this.thumbnailImage, "PNG", thumbnailFile);
        return (T)this.self();
    }

    public T withCroppedThumbnail(String path, String name, double scale, int maxWidth, int maxHeight) {
        File thumbnailFile = this.getFile(path, name);
        this.thumbnailImage = ImageProcessor.cropAndScale(this.image, scale, maxWidth, maxHeight);
        FileUtil.writeImage(this.thumbnailImage, "PNG", thumbnailFile);
        return (T)this.self();
    }

    private File getFile(String path, String name) {
        File thumbnailFile = new File(path, name);
        if (!Files.exists(Paths.get(path), new LinkOption[0])) {
            thumbnailFile.mkdirs();
        }
        return thumbnailFile;
    }

    public T withCroppedThumbnail(double scale, double cropWidth, double cropHeight) {
        return (T)this.withCroppedThumbnail(Paths.get(this.location.toString(), "./thumbnails").toString(), "thumb_" + this.fileName, scale, cropWidth, cropHeight);
    }

    public T withCroppedThumbnail(double scale, int maxWidth, int maxHeight) {
        return (T)this.withCroppedThumbnail(Paths.get(this.location.toString(), "./thumbnails").toString(), "thumb_" + this.fileName, scale, maxWidth, maxHeight);
    }

    public T withThumbnail(Path path, String name, double scale) {
        return (T)this.withThumbnail(path.toString(), name, scale);
    }

    public T withThumbnail(double scale) {
        return (T)this.withThumbnail(Paths.get(this.location.toString(), "./thumbnails").toString(), "thumb_" + this.fileName, scale);
    }

    public T monochrome() {
        this.image = ImageProcessor.convertToGrayAndWhite(this.image);
        return (T)this.self();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(this.image, "png", outputStream);
        return outputStream.toByteArray();
    }

    protected void setImage(BufferedImage image) {
        this.self().image = image;
    }

    public void save() {
        File screenshotFile = new File(this.location.toString(), this.fileName);
        if (!Files.exists(this.location, new LinkOption[0])) {
            screenshotFile.mkdirs();
        }
        FileUtil.writeImage(this.image, "PNG", screenshotFile);
    }

    public void save(String path) {
        this.location = Paths.get(path);
        this.save();
    }

    public boolean equals(Snapshot other, double deviation) {
        if (this == other) {
            return true;
        } else {
            return this.getImage() != null ? ImageProcessor.imagesAreEquals(this.getImage(), other.getImage(), deviation) : other.getImage() == null;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Snapshot)) {
            return false;
        } else {
            Snapshot that = (Snapshot)o;
            return this.getImage() != null ? ImageProcessor.imagesAreEquals(this.getImage(), that.getImage(), (double)0.0F) : that.getImage() == null;
        }
    }

    public boolean equals(BufferedImage image) {
        return this.equals(image, (double)0.0F);
    }

    public boolean equals(String path) throws IOException {
        return this.equals(path, (double)0.0F);
    }

    public boolean equals(BufferedImage image, double deviation) {
        if (this.getImage() == image) {
            return true;
        } else {
            return this.getImage() != null ? ImageProcessor.imagesAreEquals(this.getImage(), image, deviation) : image == null;
        }
    }

    public boolean equals(String path, double deviation) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        if (this.getImage() == image) {
            return true;
        } else {
            return this.getImage() != null ? ImageProcessor.imagesAreEquals(this.getImage(), image, deviation) : image == null;
        }
    }

    public boolean equalsWithDiff(BufferedImage image, String resultingImagePath) {
        return this.equalsWithDiff(image, resultingImagePath, (double)0.0F);
    }

    public boolean equalsWithDiff(BufferedImage image, String resultingImagePath, double deviation) {
        if (this.getImage() == image) {
            return true;
        } else {
            return this.getImage() != null ? ImageProcessor.imagesAreEqualsWithDiff(this.getImage(), image, resultingImagePath, deviation) : image == null;
        }
    }

    public boolean equalsWithDiff(Snapshot image, String resultingImagePath) {
        return this.equalsWithDiff(image, resultingImagePath, (double)0.0F);
    }

    public boolean equalsWithDiff(Snapshot image, String resultingImagePath, double deviation) {
        if (this == image) {
            return true;
        } else {
            return this.getImage() != null ? ImageProcessor.imagesAreEqualsWithDiff(this.getImage(), image.getImage(), resultingImagePath, deviation) : image == null;
        }
    }

    public boolean equalsWithDiff(String path, String resultingImagePath) throws IOException {
        return this.equalsWithDiff(path, resultingImagePath, (double)0.0F);
    }

    public boolean equalsWithDiff(String path, String resultingImagePath, double deviation) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        if (this.getImage() == image) {
            return true;
        } else {
            return this.getImage() != null ? ImageProcessor.imagesAreEqualsWithDiff(this.getImage(), image, resultingImagePath, deviation) : image == null;
        }
    }

    public int hashCode() {
        return this.getImage() != null ? this.getImage().hashCode() : 0;
    }
}