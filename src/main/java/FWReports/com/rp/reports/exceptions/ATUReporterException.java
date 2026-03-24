package FWReports.com.rp.reports.exceptions;

public class ATUReporterException extends Exception {
    private String message;

    public ATUReporterException() {
    }

    public ATUReporterException(String message) {
        super(message);
        this.message = message;
    }

    public String toString() {
        String exceptionMessage = "[ATU Custom Reporter Step Failed Exception]" + this.message;
        Throwable cause = this.getCause();
        if (cause != null) {
            exceptionMessage = exceptionMessage + " Caused By: " + cause.toString();
        }
        return exceptionMessage;
    }
}