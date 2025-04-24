package br.com.bitewisebytes.kashflowapi.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public record WalletTransactionEventDto(
        String transactionId,
        Long walletId,
        String type, // DEPOSIT, WITHDRAW, TRANSFER
        BigDecimal amount,
        String status,
        LocalDateTime timestamp
) {
}
