package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseCheckBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalance_Success() {
        // Arrange
        String walletNumber = "12345";
        Wallet wallet = new Wallet();
        wallet.setWalletNumber(walletNumber);
        wallet.setBalance(BigDecimal.valueOf(100.00));

        when(walletRepository.findByWalletNumber(walletNumber)).thenReturn(Optional.of(wallet));

        // Act
        WalletResponseCheckBalance response = balanceService.getBalance(walletNumber);

        // Assert
        assertNotNull(response);
        assertEquals(walletNumber, response.walletNumber());
        assertEquals(BigDecimal.valueOf(100.00), response.balance());
        verify(walletRepository, times(1)).findByWalletNumber(walletNumber);
    }

    @Test
    void testGetBalance_WalletNotFound() {
        // Arrange
        String walletNumber = "12345";

        when(walletRepository.findByWalletNumber(walletNumber)).thenReturn(Optional.empty());

        // Act & Assert
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () -> {
            balanceService.getBalance(walletNumber);
        });

        assertEquals("Carteira não encontrada: ID WALLET_NOT_FOUND", exception.getMessage());
        verify(walletRepository, times(1)).findByWalletNumber(walletNumber);
    }
}
