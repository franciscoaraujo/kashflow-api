package br.com.bitewisebytes.kashflowapi.domain.exceptions;

public class TransactionWalletException extends RuntimeException {
    public TransactionWalletException(String message) {
        super(message);
    }

    public TransactionWalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionWalletException(Throwable cause) {
        super(cause);
    }

    public TransactionWalletException(String message, String walletId) {
        super(message + walletId);
    }
}

