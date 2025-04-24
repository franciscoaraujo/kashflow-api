package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseCheckBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BalanceService {

    private static final Logger log = LoggerFactory.getLogger(BalanceService.class);

    private final WalletRepository walletRepository;

    public BalanceService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletResponseCheckBalance getBalance(String walletNumber) {
        Wallet wallet = walletRepository.findByWalletNumber(walletNumber)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found", "WALLET_NOT_FOUND"));
        log.info("Fetched balance for wallet {}: {}", wallet.getWalletNumber(), wallet.getBalance());
        log.info("Transaction created for balance inquiry: {}", wallet.getWalletNumber());

        return new WalletResponseCheckBalance(
                wallet.getWalletNumber(),
                wallet.getBalance(),
                LocalDateTime.now());
    }

}
