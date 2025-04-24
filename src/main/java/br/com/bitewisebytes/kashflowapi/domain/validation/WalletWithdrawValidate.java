package br.com.bitewisebytes.kashflowapi.domain.validation;

import br.com.bitewisebytes.kashflowapi.dto.WalletWithdrawDto;

import java.math.BigDecimal;

public class WalletWithdrawValidate {

    public static void validate(WalletWithdrawDto walletWithdrawDto) {
        if (walletWithdrawDto == null) {
            throw new IllegalArgumentException("Wallet withdraw request cannot be null");
        }
        if (walletWithdrawDto.documentNumber() == null || walletWithdrawDto.documentNumber().isEmpty()) {
            throw new IllegalArgumentException("Document number cannot be null or empty");
        }
        if (walletWithdrawDto.walletNumber() == null || walletWithdrawDto.walletNumber().isEmpty()) {
            throw new IllegalArgumentException("Wallet number cannot be null or empty");
        }
        if (walletWithdrawDto.amount() == null || walletWithdrawDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
