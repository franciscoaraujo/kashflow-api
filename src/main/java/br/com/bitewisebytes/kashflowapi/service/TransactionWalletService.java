package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Transfer;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletDepositDto;
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
public class TransactionWalletService {

    private final TransactionRepository transactionRepository;

    private static final Logger log = LoggerFactory.getLogger(TransactionWalletService.class);

    public TransactionWalletService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionWallet> getHistoricalTransactionBalanceByDate(
            String documentNumber,
            LocalDate dateTransaction,
            Pageable pageable
    ) {
        List<TransactionWallet> transactionWallets = transactionRepository
                .findTransactionByDocumentoAndDataTransaction(documentNumber, dateTransaction, pageable)
                .orElseThrow(() -> new TransactionWalletException("Transaction not found."));
        return transactionWallets;
    }
}

