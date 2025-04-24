package br.com.bitewisebytes.kashflowapi.service;


import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletDepositException;
import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.WalletResponseDepositDto;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletDepositDto;
import br.com.bitewisebytes.kashflowapi.service.kafkaservice.AuditService;
import br.com.bitewisebytes.kashflowapi.service.transactions.TransactionDeposit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepositServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionDeposit transactionDeposit;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private DepositService depositService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeposit_Success() {
        // Arrange
        WalletDepositDto walletDepositDto = new WalletDepositDto("123456789", "987654321", BigDecimal.valueOf(100), "Test deposit");
        Wallet wallet = new Wallet();
        wallet.setWalletNumber("987654321");
        wallet.setBalance(BigDecimal.valueOf(500));

        // Ensure the mock returns the wallet when the correct arguments are passed
        when(walletRepository.findByDocumentNumberAndWalletNumber(walletDepositDto.documentNumber(), walletDepositDto.walletNumber()))
                .thenReturn(Optional.of(wallet));

        // Act
        WalletResponseDepositDto response = depositService.deposit(walletDepositDto);

        // Assert
        assertNotNull(response);
        assertEquals("987654321", response.walletNumber());
        assertEquals(BigDecimal.valueOf(600), response.amount());

        verify(walletRepository, times(1)).save(wallet);
        verify(auditService, times(1)).logAudit(wallet, TransactionType.DEPOSIT, BigDecimal.valueOf(100), "Test deposit");
    }

    @Test
    void testDeposit_WalletNotFound() {
        // Arrange
        WalletDepositDto walletDepositDto = new WalletDepositDto("123456789", "987654321", BigDecimal.valueOf(100), "Test deposit");

        when(walletRepository.findByDocumentNumberAndWalletNumber("123456789", "987654321"))
                .thenReturn(Optional.empty());

        // Act & Assert
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () -> {
            depositService.deposit(walletDepositDto);
        });

        assertEquals("Carteira não encontrada: ID ", exception.getMessage());
    }

    @Test
    void testDeposit_InvalidAmount() {
        // Arrange
        WalletDepositDto walletDepositDto = new WalletDepositDto("123456789", "987654321", BigDecimal.valueOf(-100), "Test deposit");

        // Act & Assert
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () -> {
            depositService.deposit(walletDepositDto);
        });

        assertEquals("Carteira não encontrada: ID ", exception.getMessage());
    }

    @Test
    void testDeposit_OptimisticLockException() {
        // Arrange
        WalletDepositDto walletDepositDto = new WalletDepositDto("123456789", "987654321", BigDecimal.valueOf(100), "Test deposit");
        Wallet wallet = new Wallet();
        wallet.setWalletNumber("987654321");
        wallet.setBalance(BigDecimal.valueOf(500));
        when(walletRepository.findByDocumentNumberAndWalletNumber(walletDepositDto.documentNumber(), walletDepositDto.walletNumber()))
                .thenReturn(Optional.of(wallet));

        // Simulate an optimistic lock exception during save
        doThrow(new WalletDepositException("Concurrent update detected. Please retry.", "CONCURRENT_UPDATE"))
                .when(walletRepository).save(wallet);

        // Act & Assert
        WalletDepositException exception = assertThrows(WalletDepositException.class, () -> {
            depositService.deposit(walletDepositDto);
        });

        assertEquals("Erro ao depositar na carteira: Concurrent update detected. Please retry. - ID da carteira: CONCURRENT_UPDATE", exception.getMessage());
    }
}