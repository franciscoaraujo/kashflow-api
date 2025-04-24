package br.com.bitewisebytes.kashflowapi.dto.response;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionWalletDto(
        WalletResponseDto walletResponseDto,
        TransactionType type, // deposit, withdrawal, transfer_in, transfer_out
        BigDecimal amount,
        String description,
        TransactionWalletDto relatedTransactionWallet,
        LocalDateTime createdAt
) {
}
