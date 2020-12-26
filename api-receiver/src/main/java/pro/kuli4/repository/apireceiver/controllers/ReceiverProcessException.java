package pro.kuli4.repository.apireceiver.controllers;

public class ReceiverProcessException extends RuntimeException {
    public ReceiverProcessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ReceiverProcessException(String msg) {
        super(msg);
    }

    public ReceiverProcessException(Throwable cause) {
        super(cause);
    }
}
