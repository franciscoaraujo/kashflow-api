package br.com.bitewisebytes.kashflowapi.domain.validation;

import br.com.bitewisebytes.kashflowapi.dto.request.WalletRequestTransferDto;

import java.math.BigDecimal;

public class WalletRequestTransferValidate {
    public static void validate(WalletRequestTransferDto walletRequestTransferDto) {
        if (walletRequestTransferDto == null) {
            throw new IllegalArgumentException("Wallet transfer request cannot be null");
        }
        if (walletRequestTransferDto.documentNumber() == null || walletRequestTransferDto.documentNumber().isEmpty()) {
            throw new IllegalArgumentException("Document number cannot be null or empty");
        }
        if (walletRequestTransferDto.walletNumberOrigin() == null || walletRequestTransferDto.walletNumberOrigin().isEmpty()) {
            throw new IllegalArgumentException("Origin wallet number cannot be null or empty");
        }
        if (walletRequestTransferDto.walletNumberDestination() == null || walletRequestTransferDto.walletNumberDestination().isEmpty()) {
            throw new IllegalArgumentException("Destination wallet number cannot be null or empty");
        }
        if (walletRequestTransferDto.amount() == null || walletRequestTransferDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
