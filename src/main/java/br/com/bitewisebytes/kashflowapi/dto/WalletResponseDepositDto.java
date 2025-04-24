package br.com.bitewisebytes.kashflowapi.dto;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;

import java.math.BigDecimal;

public record WalletResponseDepositDto (
        String walletNumber,
        BigDecimal amount,
        String description
        //TransactionWallet transactionWallet
) {
}
