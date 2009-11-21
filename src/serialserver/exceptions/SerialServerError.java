package serialserver.exceptions;

public class SerialServerError extends Error {

    public SerialServerError() {
    }

    public SerialServerError(String message) {
        super(message);
    }

}
