package selenium_shutterbug.assertthat.selenium_shutterbug.utils.file;

import selenium_shutterbug.assertthat.selenium_shutterbug.utils.web.UnableTakeSnapshotException;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

public class FileUtil {
    public static String getJsScript(String filePath) {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            if (is == null) {
                is = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
            }

            if (is == null) {
                new UnableTakeSnapshotException("Unable to load JS script, unable to locate resource stream.");
            }

            return IOUtils.toString(is);
        } catch (IOException e) {
            throw new UnableTakeSnapshotException("Unable to load JS script", e);
        }
    }

    public static void writeImage(BufferedImage imageFile, String extension, File fileToWriteTo) {
        try {
            ImageIO.write(imageFile, extension, fileToWriteTo);
        } catch (IOException e) {
            throw new UnableSaveSnapshotException(e);
        }
    }
}
