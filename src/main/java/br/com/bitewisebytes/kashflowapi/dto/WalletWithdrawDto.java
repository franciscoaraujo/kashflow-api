package br.com.bitewisebytes.kashflowapi.dto;

import java.math.BigDecimal;

public record WalletWithdrawDto(
        String documentNumber,
        String walletNumber,
        BigDecimal amount,
        String description
) {
}
