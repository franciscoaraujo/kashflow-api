package br.com.bitewisebytes.kashflowapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDetailsRecord(
        Long transferId,
        String senderWallet,
        String receiverWallet,
        BigDecimal amountReceiver,
        LocalDateTime dataTransaction,
        LocalDateTime createdAt
) {
}
