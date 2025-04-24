package br.com.bitewisebytes.kashflowapi.dto.response;

import java.math.BigDecimal;

public record WalletResponseWalletWithdrawDto(
        String walletNumber,
        BigDecimal amount,
        String message

) {
}
