package br.com.bitewisebytes.kashflowapi.dto.request;

import java.math.BigDecimal;

public record WalletRequestTransferDto(
        String documentNumber,
        String walletNumberOrigin,
        String walletNumberDestination,
        BigDecimal amount,
        String description
) {
}
