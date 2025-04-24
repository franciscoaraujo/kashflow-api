package br.com.bitewisebytes.kashflowapi.service.transactions;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class AbstractTransaction {

    protected final TransactionRepository transactionRepository;

    protected static final Logger log = LoggerFactory.getLogger(AbstractTransaction.class);

    public AbstractTransaction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    protected void validateTransaction(Wallet wallet, TransactionType type, BigDecimal amount) {

        log.info("Validating transaction...");

        if (wallet == null) {
            throw new IllegalArgumentException("Wallet cannot be null.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    /**
     * Builds a transaction for the given wallet.
     * @param wallet
     * @param type
     * @param amount
     * @param description
     * @return
     */
    @Transactional
    protected TransactionWallet builderTransaction(Wallet wallet, TransactionType type, BigDecimal amount, String description) {
        log.info("Building transaction...");

        TransactionWallet transaction = new TransactionWallet();
        transaction.setWallet(wallet);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDataTransaction(LocalDate.now());
        transactionRepository.save(transaction);

        log.info("Transaction built successfully.");

        return transaction;
    }

    /**
     * Creates a transaction for the given wallet.
     *
     * @param wallet      The wallet associated with the transaction.
     * @param type        The type of transaction (e.g., DEPOSIT, WITHDRAW).
     * @param amount      The amount of the transaction.
     * @param description A description of the transaction.
     * @return The created TransactionWallet object.
     */
    @Transactional
    public abstract TransactionWallet doTransaction(Wallet wallet, TransactionType type, BigDecimal amount, String description);
}
