package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletDepositException;
import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.WalletResponseDepositDto;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletDepositDto;
import br.com.bitewisebytes.kashflowapi.service.kafkaservice.AuditService;
import br.com.bitewisebytes.kashflowapi.service.transactions.TransactionDeposit;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositService {

    private static String CODE_ERROR = "";
    private static String CODE_ERROR_INVALID_AMOUNT = "";

    private static final Logger log = LoggerFactory.getLogger(DepositService.class);

    private final WalletRepository walletRepository;
    private final TransactionDeposit transactionDeposit;
    private final AuditService auditService;

    public DepositService(WalletRepository walletRepository, TransactionDeposit transactionDeposit, AuditService auditService) {
        this.walletRepository = walletRepository;
        this.transactionDeposit = transactionDeposit;
        this.auditService = auditService;
    }

    @Transactional
    public WalletResponseDepositDto deposit(WalletDepositDto walletDepositDto) {
        Wallet instantWallet = null;
        TransactionWallet transactionUnit = null;
        try {
            // Find wallet by wallet number
            instantWallet = walletRepository.findByDocumentNumberAndWalletNumber(
                    walletDepositDto.documentNumber(),
                    walletDepositDto.walletNumber()
            ).orElseThrow(() -> new WalletNotFoundException("Wallet not found", CODE_ERROR));

            // Validate deposit amount
            BigDecimal amount = walletDepositDto.amount();
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new WalletNotFoundException("Deposit amount must be greater than zero", CODE_ERROR_INVALID_AMOUNT);
            }
            instantWallet.setBalance(instantWallet.getBalance().add(amount));
            walletRepository.save(instantWallet);

            log.info("Deposit successful: {}", walletDepositDto);
            // Create transaction for the deposit
            transactionUnit = transactionDeposit.doTransaction(
                    instantWallet,
                    TransactionType.DEPOSIT,
                    amount,
                    walletDepositDto.description()
            );
            // Log audit event
            log.info("Audit event: Deposit of {} to wallet {} (Document: {})",
                    walletDepositDto.amount(),
                    instantWallet.getWalletNumber(),
                    walletDepositDto.documentNumber());
            auditService.logAudit(
                    instantWallet,
                    TransactionType.DEPOSIT,
                    amount,
                    walletDepositDto.description()
            );
        } catch (OptimisticLockException e) {
            log.error("Optimistic locking failure: {}", e.getMessage());
            //auditService.logAudit(wallet.getId(), TransactionType.DEPOSIT, walletDepositDto.amount(), TransactionStatus.FAILED, null, null);
            throw new WalletDepositException("Concurrent update detected. Please retry.", "CONCURRENT_UPDATE");
        }

        return new WalletResponseDepositDto(
                instantWallet.getWalletNumber(),
                instantWallet.getBalance(),
                "Deposit successful"
        );
    }
}
