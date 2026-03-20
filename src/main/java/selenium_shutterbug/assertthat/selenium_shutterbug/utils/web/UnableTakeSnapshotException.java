package selenium_shutterbug.assertthat.selenium_shutterbug.utils.web;

public class UnableTakeSnapshotException extends RuntimeException {

    public UnableTakeSnapshotException() {
    }

    public UnableTakeSnapshotException(String message) {
        super(message);
    }

    public UnableTakeSnapshotException(Throwable cause) {
        super(cause);
    }

    public UnableTakeSnapshotException(String message, Throwable cause) {
        super(message, cause);
    }
}
