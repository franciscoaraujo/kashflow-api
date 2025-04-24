package br.com.bitewisebytes.kashflowapi.domain.exceptions;

public class UserException extends RuntimeException{
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException(String message, String errorCode) {
        super(message + " - Error code: " + errorCode);
    }
}
