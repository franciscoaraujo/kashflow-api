package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.WalletWithdrawDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseWalletWithdrawDto;
import br.com.bitewisebytes.kashflowapi.service.transactions.TransactionWithdraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WithdrawServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionWithdraw transactionWithdraw;

    @InjectMocks
    private WithdrawService withdrawService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWithdraw_Success() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100.00"));

        WalletWithdrawDto walletWithdrawDto = new WalletWithdrawDto("123456789", "987654321", new BigDecimal("50.00"), "Test withdraw");

        when(walletRepository.findByDocumentNumberAndWalletNumber(
                walletWithdrawDto.documentNumber(),
                walletWithdrawDto.walletNumber())).thenReturn(Optional.of(wallet));

        // Act
        WalletResponseWalletWithdrawDto response = withdrawService.withdraw(walletWithdrawDto);

        // Assert
        assertNotNull(response);
        assertEquals("987654321", response.walletNumber());
        assertEquals(new BigDecimal("50.00"), response.amount());
        assertEquals("Withdraw successful", response.message());
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());

        verify(transactionWithdraw, times(1)).doTransaction(wallet, TransactionType.WITHDRAW, new BigDecimal("50.00"), "Test withdraw");
        verify(walletRepository, times(1)).findByDocumentNumberAndWalletNumber(walletWithdrawDto.documentNumber(), walletWithdrawDto.walletNumber());
    }

    @Test
    void testWithdraw_WalletNotFound() {
        // Arrange
        WalletWithdrawDto walletWithdrawDto = new WalletWithdrawDto("123456789", "987654321", new BigDecimal("50.00"), "Test withdraw");

        when(walletRepository.findByDocumentNumberAndWalletNumber(walletWithdrawDto.documentNumber(), walletWithdrawDto.walletNumber()))
                .thenReturn(Optional.empty());

        // Act & Assert
        TransactionWalletException exception = assertThrows(TransactionWalletException.class, () -> withdrawService.withdraw(walletWithdrawDto));
        assertEquals("Wallet not found.WALLET_NOT_FOUND", exception.getMessage());

        verify(walletRepository, times(1)).findByDocumentNumberAndWalletNumber(walletWithdrawDto.documentNumber(), walletWithdrawDto.walletNumber());
        verifyNoInteractions(transactionWithdraw);
    }

    @Test
    void testWithdraw_InsufficientBalance() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("30.00"));

        WalletWithdrawDto walletWithdrawDto = new WalletWithdrawDto("123456789", "987654321", new BigDecimal("50.00"), "Test withdraw");

        when(walletRepository.findByDocumentNumberAndWalletNumber(walletWithdrawDto.documentNumber(), walletWithdrawDto.walletNumber()))
                .thenReturn(Optional.of(wallet));

        // Act & Assert
        TransactionWalletException exception = assertThrows(TransactionWalletException.class, () -> withdrawService.withdraw(walletWithdrawDto));
        assertEquals("Insufficient balance.INSUFFICIENT_BALANCE", exception.getMessage());

        verify(walletRepository, times(1)).findByDocumentNumberAndWalletNumber(walletWithdrawDto.documentNumber(), walletWithdrawDto.walletNumber());
        verifyNoInteractions(transactionWithdraw);
    }

    @Test
    void testWithdraw_InvalidAmount() {
        // Arrange
        WalletWithdrawDto walletWithdrawDto = new WalletWithdrawDto("123456789", "987654321", new BigDecimal("-10.00"), "Test withdraw");

        // Act & Assert
        TransactionWalletException exception = assertThrows(TransactionWalletException.class, () -> withdrawService.withdraw(walletWithdrawDto));
        assertEquals("Invalid withdrawal amount.INVALID_AMOUNT", exception.getMessage());

        verifyNoInteractions(walletRepository);
        verifyNoInteractions(transactionWithdraw);
    }
}