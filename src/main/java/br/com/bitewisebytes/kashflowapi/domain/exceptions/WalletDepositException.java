package br.com.bitewisebytes.kashflowapi.domain.exceptions;

public class WalletDepositException extends RuntimeException {
    public WalletDepositException(String message) {
        super(message);
    }

    public WalletDepositException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletDepositException(Throwable cause) {
        super(cause);
    }
    public WalletDepositException(String message, String walletId) {
        super("Erro ao depositar na carteira: " + message + " - ID da carteira: " + walletId);
    }
}
