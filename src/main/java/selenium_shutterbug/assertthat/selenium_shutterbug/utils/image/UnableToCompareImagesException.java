package selenium_shutterbug.assertthat.selenium_shutterbug.utils.image;

public class UnableToCompareImagesException extends RuntimeException {

    public UnableToCompareImagesException() {
    }

    public UnableToCompareImagesException(String message) {
        super(message);
    }

    public UnableToCompareImagesException(String message, Throwable cause) {
        super(message, cause);
    }
}
