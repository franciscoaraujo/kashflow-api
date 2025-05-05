package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Transfer;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionStatus;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransferRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletRequestTransferDto;
import br.com.bitewisebytes.kashflowapi.service.kafkaservice.AuditService;
import br.com.bitewisebytes.kashflowapi.service.transactions.TransactionTransfer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferService {

    private static String CODE_ERROR = "CODE_ERROR";
    private static String CODE_ERROR_INVALID_AMOUNT = "CODE_ERROR_INVALID_AMOUNT";

    private final WalletRepository walletRepository;
    private final TransactionTransfer transactionTransfer;
    private final TransferRepository transferRepository;
    private final AuditService auditService;

    public TransferService(
            WalletRepository walletRepository,
            TransactionTransfer transactionTransfer,
            TransactionRepository transactionRepository,
            TransferRepository transferRepository,
            AuditService auditService
    ) {
        this.walletRepository = walletRepository;
        this.transactionTransfer= transactionTransfer;
        this.transferRepository = transferRepository;
        this.auditService = auditService;
    }

    @Transactional
    public void transfer(WalletRequestTransferDto walletRequestTransferDt) {

        Wallet walletFrom = walletRepository.findByDocumentNumberAndWalletNumber(
                walletRequestTransferDt.documentNumber(),
                walletRequestTransferDt.walletNumberOrigin()
        ).orElseThrow(() -> new WalletNotFoundException("Wallet not found", CODE_ERROR));

        if(walletFrom.getBalance().compareTo(walletRequestTransferDt.amount()) < 0) {
            throw new TransactionWalletException("Insufficient funds in the wallet", CODE_ERROR);
        }

        Wallet walletTo = walletRepository.findByWalletNumber(walletRequestTransferDt.walletNumberDestination())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found", CODE_ERROR));
        BigDecimal amount = walletRequestTransferDt.amount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionWalletException("Amount must be greater than zero.", CODE_ERROR_INVALID_AMOUNT);
        }

        if (walletTo.getWalletNumber().compareTo(walletRequestTransferDt.walletNumberDestination()) == 0) {
            throw new TransactionWalletException("You cannot withdraw from the same wallet.", "SAME_WALLET");
        }

        TransactionWallet senderTransaction = transactionTransfer.doTransaction(
                walletFrom,
                TransactionType.TRANSFER_OUT,
                amount,
                "Transfer to wallet " + walletTo.getWalletNumber());

        TransactionWallet receiverTransaction = transactionTransfer.doTransaction(
                walletTo,
                TransactionType.TRANSFER_IN,
                amount,
                "Transfer from wallet " + walletFrom.getWalletNumber());

        Transfer transfer = new Transfer();
        transfer.setSenderWallet(walletFrom);
        transfer.setReceiverWallet(walletTo);
        transfer.setAmount(amount);
        transfer.setSenderTransactionWallet(senderTransaction);
        transfer.setReceiverTransactionWallet(receiverTransaction);
        transfer.setCreatedAt(LocalDateTime.now());

        transferRepository.save(transfer);

        auditService.logAudit(
                walletTo,
                TransactionType.TRANSFER_OUT,
                amount,
                "Transfer to wallet " + walletTo.getWalletNumber()
        );

        //TODO: Log audit event for transfer
        walletFrom.setBalance(walletFrom.getBalance().subtract(amount));
        walletRepository.save( walletFrom);

        //TODO: Log audit event for transfer
        walletTo.setBalance(walletTo.getBalance().add(amount));
        walletRepository.save( walletTo);
    }
}
