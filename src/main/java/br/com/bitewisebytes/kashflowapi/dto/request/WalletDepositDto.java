package br.com.bitewisebytes.kashflowapi.dto.request;


import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;

import java.math.BigDecimal;

public record WalletDepositDto(
        String walletNumber,
        String documentNumber,
        BigDecimal amount,
        String description

) {}
