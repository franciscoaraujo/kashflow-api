package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.WalletWithdrawDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseWalletWithdrawDto;
import br.com.bitewisebytes.kashflowapi.service.transactions.TransactionWithdraw;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawService {

    private static final Logger log = LoggerFactory.getLogger(DepositService.class);

    private final WalletRepository walletRepository;
    private final TransactionWithdraw transactionWithdraw;

    public WithdrawService(WalletRepository walletRepository, TransactionWithdraw transactionWithdraw) {
        this.walletRepository = walletRepository;
        this.transactionWithdraw = transactionWithdraw;
    }

    @Transactional
    public WalletResponseWalletWithdrawDto withdraw(WalletWithdrawDto walletWithdrawDto) {
        if (walletWithdrawDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionWalletException("Invalid withdrawal amount.", "INVALID_AMOUNT");
        }

        Wallet wallet = walletRepository.findByDocumentNumberAndWalletNumber(
                        walletWithdrawDto.documentNumber(), walletWithdrawDto.walletNumber())
                .orElseThrow(() -> new TransactionWalletException("Wallet not found.", "WALLET_NOT_FOUND"));

        if (wallet.getBalance().compareTo(walletWithdrawDto.amount()) < 0) {
            throw new TransactionWalletException("Insufficient balance.", "INSUFFICIENT_BALANCE");
        }
        if (wallet.getBalance().compareTo(walletWithdrawDto.amount()) == 0) {
            throw new TransactionWalletException("Withdrawal amount equals wallet balance.", "EQUAL_BALANCE");
        }
        if (wallet.getWalletNumber().compareTo(walletWithdrawDto.walletNumber()) == 0) {
            throw new TransactionWalletException("You cannot withdraw from the same wallet.", "SAME_WALLET");
        }

        wallet.setBalance(wallet.getBalance().subtract(walletWithdrawDto.amount()));
        walletRepository.save(wallet);
        log.info("Wallet balance after withdrawal: {}", wallet.getBalance());
        transactionWithdraw.doTransaction(wallet, TransactionType.WITHDRAW, walletWithdrawDto.amount(), walletWithdrawDto.description());

        return new WalletResponseWalletWithdrawDto(
                walletWithdrawDto.walletNumber(),
                walletWithdrawDto.amount(),
                "Withdraw successful"
        );
    }
}
