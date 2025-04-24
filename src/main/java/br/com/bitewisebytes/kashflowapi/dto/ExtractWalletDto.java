package br.com.bitewisebytes.kashflowapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExtractWalletDto(
        Long transactionId,
        LocalDateTime createdAt,
        LocalDate dataTransaction,
        String description,
        String type,
        BigDecimal amount,
        BigDecimal transferAmount,
        String currentWalletNumber,
        String receiverWalletNumber,
        String receiverUserName,
        LocalDateTime transferDate

) {
}
