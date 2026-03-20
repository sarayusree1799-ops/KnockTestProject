package Reports.com.rp.reports.exceptions;

public class ATUReporterStepFailedException extends RuntimeException {

    public ATUReporterStepFailedException() {
    }

    public ATUReporterStepFailedException(String message) {
        super(message);
    }

    public ATUReporterStepFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String exceptionMessage = this.getMessage();
        if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
            exceptionMessage = "[ATU Custom Reporter Step Failed Exception]" + exceptionMessage;
        } else {
            exceptionMessage = "[ATU Custom Reporter Step Failed Exception]";
        }
        Throwable cause = this.getCause();
        if (cause != null) {
            exceptionMessage = exceptionMessage + " Caused By: " + cause.toString();
        }
        return exceptionMessage;
    }
}