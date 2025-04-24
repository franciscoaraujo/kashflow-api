package br.com.bitewisebytes.kashflowapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletBalanceDto(
        String walletNumber,
        BigDecimal balance,
        LocalDateTime lastUpdated
) {
}
