package selenium_shutterbug.assertthat.selenium_shutterbug.utils.web;

public class ElementOutsideViewportException extends RuntimeException {

    public ElementOutsideViewportException() {
    }

    public ElementOutsideViewportException(String message) {
        super(message);
    }

    public ElementOutsideViewportException(String message, Throwable cause) {
        super(message, cause);
    }
}
