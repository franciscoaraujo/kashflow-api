package br.com.bitewisebytes.kashflowapi.domain.validation;

import br.com.bitewisebytes.kashflowapi.dto.request.WalletDepositDto;

import java.math.BigDecimal;

public class WalletDepositValidate {

    public static void validate(WalletDepositDto walletDepositDto) {
        if (walletDepositDto == null) {
            throw new IllegalArgumentException("Wallet deposit request cannot be null");
        }
        if (walletDepositDto.walletNumber() == null || walletDepositDto.walletNumber().isEmpty()) {
            throw new IllegalArgumentException("Wallet number cannot be null or empty");
        }
        if (walletDepositDto.documentNumber() == null || walletDepositDto.documentNumber().isEmpty()) {
            throw new IllegalArgumentException("Document number cannot be null or empty");
        }
        if (walletDepositDto.amount() == null || walletDepositDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
