package br.com.bitewisebytes.kashflowapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletResponseCheckBalance (
        String walletNumber,
        BigDecimal balance,
        LocalDateTime dateBalance
) {
}
