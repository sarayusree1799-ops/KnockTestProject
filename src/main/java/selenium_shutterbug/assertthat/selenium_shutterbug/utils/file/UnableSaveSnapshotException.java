package selenium_shutterbug.assertthat.selenium_shutterbug.utils.file;

public class UnableSaveSnapshotException extends RuntimeException {

    public UnableSaveSnapshotException() {
    }

    public UnableSaveSnapshotException(String message) {
        super(message);
    }

    public UnableSaveSnapshotException(Throwable cause) {
        super(cause);
    }

    public UnableSaveSnapshotException(String message, Throwable cause) {
        super(message, cause);
    }
}
