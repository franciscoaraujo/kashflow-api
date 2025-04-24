package br.com.bitewisebytes.kashflowapi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionWalletResponseDto(
        Long id,
        String transactionId,
        String walletNumber,
        String type, // DEPOSIT, WITHDRAW, TRANSFER
        BigDecimal amount,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {
}
