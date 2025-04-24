package br.com.bitewisebytes.kashflowapi.service;


import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletDepositException;
import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Transfer;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.User;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransferRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.TransactionWalletDto;
import br.com.bitewisebytes.kashflowapi.dto.WalletResponseDepositDto;
import br.com.bitewisebytes.kashflowapi.dto.WalletWithdrawDto;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletDepositDto;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletRequestTransferDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseCheckBalance;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseWalletWithdrawDto;
import br.com.bitewisebytes.kashflowapi.utils.WalletTokenGenerator;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class WalletService {

    private static String CODE_ERROR = "";
    private static String CODE_ERROR_INVALID_AMOUNT = "";

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;
    private final TransactionWalletService transactionWalletService;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;


    public WalletService(
            WalletRepository walletRepository,
            TransactionWalletService transactionWalletService,
            TransactionRepository transactionRepository,
            TransferRepository transferRepository,
            BalanceService balanceService
    ) {
        this.walletRepository = walletRepository;
        this.transactionWalletService = transactionWalletService;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;

    }

    @Transactional
    public WalletResponseDto createWallet(User user) {

        log.info("Creating wallet for: {}", user.getName());

        Wallet wallet = null;
        boolean walletCreated = false;

        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                wallet = Wallet.builder()
                        .user(user)
                        .balance(BigDecimal.ZERO)
                        .walletNumber(WalletTokenGenerator.generateTimestampToken())
                        .build();
                walletRepository.save(wallet);
                walletCreated = true;
                log.info("Created wallet successfully on attempt {}: {}", attempt, wallet.getWalletNumber());
                break;
            } catch (Exception e) {
                log.error("Error creating wallet on attempt {}: {}", attempt, e.getMessage());
            }
        }

        if (!walletCreated) {
            throw new RuntimeException("Failed to create wallet after multiple attempts.");
        }
        UserResponseDto userResponseDto = convertToUserResponseDto(user);
        WalletResponseDto walletResponseDto = convertToWalletResponseDto(wallet, userResponseDto);
        log.info("Wallet created successfully: {}", walletResponseDto);
        //TODO criar um evento de auditoria para a criação da wallet
        log.info("Audit event: Wallet created at {} with wallet number {} for user {} (Document: {})",
                wallet.getDateTimeCreated(),
                wallet.getWalletNumber(),
                user.getName(),
                user.getDocumentNumber());
        return walletResponseDto;

    }

    public List<WalletResponseDto> getListWalletsByDocumentNumber(String documentNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Wallet> wallets = walletRepository.findByUserDocumentNumber(documentNumber, pageable);

        if (wallets.isEmpty()) {
            throw new WalletNotFoundException("No wallets found for the provided document number", "WALLET_NOT_FOUND");
        }

        return wallets.stream()
                .filter(wallet -> wallet.getUser() != null) // Filter out wallets with null user
                .map(wallet -> convertToWalletResponseDto(wallet, convertToUserResponseDto(wallet.getUser())))
                .toList();
    }

    public List<TransactionWalletDto> getHistoricalTransactionBalanceByDate(String documentNumber, LocalDate dateTransaction, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TransactionWallet> historicalTransactionBalanceByDate = transactionWalletService
                .getHistoricalTransactionBalanceByDate(documentNumber, dateTransaction, pageable);

        return historicalTransactionBalanceByDate.stream()
                .map(transaction -> new TransactionWalletDto(
                        transaction.getId(),
                        transaction.getWallet().getWalletNumber(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getDataTransaction(),
                        transaction.getCreatedAt()
                ))
                .toList();
    }

    private UserResponseDto convertToUserResponseDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null when converting to UserResponseDto");
        }
        return new UserResponseDto(
                user.getName(),
                user.getEmail(),
                user.getDocumentNumber(),
                user.getWallets()
        );
    }
    private WalletResponseDto convertToWalletResponseDto(Wallet wallet, UserResponseDto userResponseDto) {
        return new WalletResponseDto(
                wallet.getId(),
                wallet.getWalletNumber(),
                userResponseDto,
                wallet.getBalance(),
                wallet.getDateTimeCreated()
                //wallet.getTransactionWallets()
        );
    }
}
