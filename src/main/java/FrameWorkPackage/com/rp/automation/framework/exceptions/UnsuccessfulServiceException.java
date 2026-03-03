package FrameWorkPackage.com.rp.automation.framework.exceptions;

@SuppressWarnings("serial")
public class UnsuccessfulServiceException extends RuntimeException {
    public UnsuccessfulServiceException(String message, Exception cause) {
        super (
                message
                + " kindly refer attached screenshot for more clarification \n\n Stack Trace::\n", cause
        );
    }

    public UnsuccessfulServiceException() {
        super ("Service failed "
                + " kindly refer attached screenshot for more clarification"
        );
    }
}