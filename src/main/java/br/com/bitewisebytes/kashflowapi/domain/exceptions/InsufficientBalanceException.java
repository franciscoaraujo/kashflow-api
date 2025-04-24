package br.com.bitewisebytes.kashflowapi.domain.exceptions;



/**
 * Exceção lançada quando o saldo de uma carteira é insuficiente para realizar uma operação.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Saldo insuficiente para completar a operação");
    }
}