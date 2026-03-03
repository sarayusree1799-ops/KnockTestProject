package FrameWorkPackage.com.rp.automation.framework.exceptions;

@SuppressWarnings("serial")
public class ElementNotDisappearedInException extends RuntimeException {

    public ElementNotDisappearedInException (String element, String timeout) {
        super (
                "Element "
                + element
                + " did not disappear in specified timeout of "
                + timeout
                + " seconds. Kindly refer attached screenshots for more clarification"
        );
    }

    public ElementNotDisappearedInException (String element, String timeout, Throwable cause) {
        super (
                "Element "
                        + element
                        + " did not disappear in specified timeout of "
                        + timeout
                        + " seconds. Kindly refer attached screenshots for more clarification", cause
        );
    }

    public ElementNotDisappearedInException (String element, int timeout, Throwable cause) {
        super (
                "Element "
                        + element
                        + " did not disappear in specified timeout of "
                        + timeout
                        + " seconds. Kindly refer attached screenshots for more clarification", cause
        );
    }

    public ElementNotDisappearedInException () {
        super (
                "Element "
                        + " did not disappear in specified timeout of "
                        + " seconds. Kindly refer attached screenshots for more clarification"
        );
    }
}
