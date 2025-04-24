package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionWalletServiceTest {

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private final TransactionWalletService transactionWalletService = new TransactionWalletService(transactionRepository);

    @Test
    void testGetHistoricalTransactionBalanceByDate_Success() {
        String documentNumber = "123456789";
        LocalDate dateTransaction = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);
        List<TransactionWallet> mockTransactions = List.of(new TransactionWallet());

        when(transactionRepository.findTransactionByDocumentoAndDataTransaction(documentNumber, dateTransaction, pageable))
                .thenReturn(Optional.of(mockTransactions));

        List<TransactionWallet> result = transactionWalletService.getHistoricalTransactionBalanceByDate(documentNumber, dateTransaction, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1))
                .findTransactionByDocumentoAndDataTransaction(documentNumber, dateTransaction, pageable);
    }

    @Test
    void testGetHistoricalTransactionBalanceByDate_NotFound() {
        String documentNumber = "123456789";
        LocalDate dateTransaction = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);

        when(transactionRepository.findTransactionByDocumentoAndDataTransaction(documentNumber, dateTransaction, pageable))
                .thenReturn(Optional.empty());

        assertThrows(TransactionWalletException.class, () ->
                transactionWalletService.getHistoricalTransactionBalanceByDate(documentNumber, dateTransaction, pageable));

        verify(transactionRepository, times(1))
                .findTransactionByDocumentoAndDataTransaction(documentNumber, dateTransaction, pageable);
    }
}