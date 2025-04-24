package br.com.bitewisebytes.kashflowapi.domain.exceptions;

    public class WalletNotFoundException extends RuntimeException {

        public WalletNotFoundException(String message, String walletId) {
            super("Carteira não encontrada: ID " + walletId);
        }
        public WalletNotFoundException(String message) {
            super(message);
        }

        public WalletNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
        public WalletNotFoundException(Throwable cause) {
            super(cause);
        }

}