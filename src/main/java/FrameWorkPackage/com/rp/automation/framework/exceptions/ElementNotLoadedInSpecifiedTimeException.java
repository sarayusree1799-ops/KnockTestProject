package FrameWorkPackage.com.rp.automation.framework.exceptions;

@SuppressWarnings("serial")
public class ElementNotLoadedInSpecifiedTimeException extends RuntimeException {

    public ElementNotLoadedInSpecifiedTimeException(String element, String timeout) {
        super (
                "Element "
                        + element
                        + " was not loaded in specified timeout of "
                        + timeout
                        + " seconds. Kindly refer attached screenshots for more clarification"
        );
    }

    public ElementNotLoadedInSpecifiedTimeException(String element, String timeout, Throwable cause) {
        super (
                "Element "
                        + element
                        + " was not loaded in specified timeout of "
                        + timeout
                        + " seconds. Kindly refer attached screenshots for more clarification", cause
        );
    }

    public ElementNotLoadedInSpecifiedTimeException(String element, int timeout, Throwable cause) {
        super (
                "Element "
                        + element
                        + " was not loaded in specified timeout of "
                        + timeout
                        + " seconds. Kindly refer attached screenshots for more clarification", cause
        );
    }
    public ElementNotLoadedInSpecifiedTimeException() {
        super (
                "Element "
                        + " was not loaded in specified timeout of "
                        + " seconds. Kindly refer attached screenshots for more clarification"
        );
    }
}