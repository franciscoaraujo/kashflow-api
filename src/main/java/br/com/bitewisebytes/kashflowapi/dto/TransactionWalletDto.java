package br.com.bitewisebytes.kashflowapi.dto;

import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionWalletDto(
        Long id,
        String walletId,
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDate dataTransaction,
        LocalDateTime createdAt
) {
}
