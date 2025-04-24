package br.com.bitewisebytes.kashflowapi.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AuditTransactionDto(
        String transactionId,
        Long walletId,
        String type,
        BigDecimal amount,
        String status,
        LocalDateTime timestamp,
        Long fromWalletFrom,
        Long toWalletFrom
) {

}