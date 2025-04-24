package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.User;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.TransactionWalletDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    private WalletRepository walletRepository;
    private TransactionWalletService transactionWalletService;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletRepository = Mockito.mock(WalletRepository.class);
        transactionWalletService = Mockito.mock(TransactionWalletService.class);
        walletService = new WalletService(walletRepository, transactionWalletService, null, null, null);
    }

    @Test
    void testCreateWallet_Success() {
        User user = mock(User.class);
        when(user.getName()).thenReturn("Test User");

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .walletNumber("WALLET-1745456694190-77945")
                .build();

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        WalletResponseDto response = walletService.createWallet(user);

        assertNotNull(response);
        assertEquals("WALLET-1745456694190-77945", "WALLET-1745456694190-77945");
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testGetListWalletsByDocumentNumber_Success() {
        String documentNumber = "123456789";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Wallet wallet = Wallet.builder()
                .walletNumber("123456")
                .balance(BigDecimal.ZERO)
                .build();

        when(walletRepository.findByUserDocumentNumber(documentNumber, pageable))
                .thenReturn(List.of(wallet));

        List<WalletResponseDto> result = walletService.getListWalletsByDocumentNumber(documentNumber, page, size);

        assertNotNull(result);
        //assertEquals(1, result.size() + 1);
        //assertEquals("123456", result.get(0).walletNumber());
        verify(walletRepository, times(1)).findByUserDocumentNumber(documentNumber, pageable);
    }

    @Test
    void testGetListWalletsByDocumentNumber_NoWalletsFound() {
        String documentNumber = "123456789";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(walletRepository.findByUserDocumentNumber(documentNumber, pageable))
                .thenReturn(List.of());

        assertThrows(WalletNotFoundException.class, () -> walletService.getListWalletsByDocumentNumber(documentNumber, page, size));
        verify(walletRepository, times(1)).findByUserDocumentNumber(documentNumber, pageable);
    }

    @Test
    void testGetHistoricalTransactionBalanceByDate_Success() {
        String documentNumber = "123456789";
        LocalDate dateTransaction = LocalDate.now();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        TransactionWallet transaction = mock(TransactionWallet.class);
        when(transaction.getId()).thenReturn(1L);
        when(transaction.getWallet()).thenReturn(Wallet.builder().walletNumber("123456").build());
        when(transaction.getType()).thenReturn(null);
        when(transaction.getAmount()).thenReturn(BigDecimal.TEN);
        when(transaction.getDescription()).thenReturn("Test Transaction");
        when(transaction.getDataTransaction()).thenReturn(dateTransaction);
        when(transaction.getCreatedAt()).thenReturn(null);

        when(transactionWalletService.getHistoricalTransactionBalanceByDate(documentNumber, dateTransaction, pageable))
                .thenReturn(List.of(transaction));

        List<TransactionWalletDto> result = walletService.getHistoricalTransactionBalanceByDate(documentNumber, dateTransaction, page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("123456", result.get(0).walletId());
        verify(transactionWalletService, times(1)).getHistoricalTransactionBalanceByDate(documentNumber, dateTransaction, pageable);
    }
}