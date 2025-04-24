package br.com.bitewisebytes.kashflowapi.service.transactions;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionTransfer extends AbstractTransaction {

    public TransactionTransfer(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public TransactionWallet doTransaction(Wallet wallet, TransactionType type, BigDecimal amount, String description) {
        validateTransaction(wallet, type, amount);
        TransactionWallet transactionWallet = builderTransaction(wallet, type, amount, description);
        return transactionWallet;
    }
}
