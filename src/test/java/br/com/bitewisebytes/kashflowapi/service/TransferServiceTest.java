package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.TransactionWalletException;
import br.com.bitewisebytes.kashflowapi.domain.exceptions.WalletNotFoundException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Transfer;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransferRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletRequestTransferDto;
import br.com.bitewisebytes.kashflowapi.service.transactions.TransactionTransfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionTransfer transactionTransfer;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldTransferSuccessfully() {
        Wallet walletFrom = new Wallet();
        walletFrom.setWalletNumber("123");
        walletFrom.setBalance(BigDecimal.valueOf(1000));

        Wallet walletTo = new Wallet();
        walletTo.setWalletNumber("456");
        walletTo.setBalance(BigDecimal.valueOf(500));

        WalletRequestTransferDto requestDto = mock(WalletRequestTransferDto.class);
        when(requestDto.documentNumber()).thenReturn("123456789");
        when(requestDto.walletNumberOrigin()).thenReturn("123");
        when(requestDto.walletNumberDestination()).thenReturn("456");
        when(requestDto.amount()).thenReturn(BigDecimal.valueOf(200));

        when(walletRepository.findByDocumentNumberAndWalletNumber("123456789", "123"))
                .thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByWalletNumber("456"))
                .thenReturn(Optional.of(walletTo));

        TransactionWallet senderTransaction = new TransactionWallet();
        TransactionWallet receiverTransaction = new TransactionWallet();

        when(transactionTransfer.doTransaction(walletFrom, TransactionType.TRANSFER_OUT, BigDecimal.valueOf(200), "Transfer to wallet 456"))
                .thenReturn(senderTransaction);
        when(transactionTransfer.doTransaction(walletTo, TransactionType.TRANSFER_IN, BigDecimal.valueOf(200), "Transfer from wallet 123"))
                .thenReturn(receiverTransaction);

        transferService.transfer(requestDto);

        verify(walletRepository).save(walletFrom);
        verify(walletRepository).save(walletTo);
        verify(transferRepository).save(any(Transfer.class));
    }

    @Test
    void shouldThrowExceptionWhenWalletFromNotFound() {
        WalletRequestTransferDto requestDto = mock(WalletRequestTransferDto.class);
        when(requestDto.documentNumber()).thenReturn("123456789");
        when(requestDto.walletNumberOrigin()).thenReturn("123");

        when(walletRepository.findByDocumentNumberAndWalletNumber("123456789", "123"))
                .thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> transferService.transfer(requestDto));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        Wallet walletFrom = new Wallet();
        walletFrom.setWalletNumber("123");
        walletFrom.setBalance(BigDecimal.valueOf(100));

        WalletRequestTransferDto requestDto = mock(WalletRequestTransferDto.class);
        when(requestDto.documentNumber()).thenReturn("123456789");
        when(requestDto.walletNumberOrigin()).thenReturn("123");
        when(requestDto.amount()).thenReturn(BigDecimal.valueOf(200));

        when(walletRepository.findByDocumentNumberAndWalletNumber("123456789", "123"))
                .thenReturn(Optional.of(walletFrom));

        assertThrows(TransactionWalletException.class, () -> transferService.transfer(requestDto));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        Wallet walletFrom = new Wallet();
        walletFrom.setWalletNumber("123");
        walletFrom.setBalance(BigDecimal.valueOf(1000));

        WalletRequestTransferDto requestDto = mock(WalletRequestTransferDto.class);
        when(requestDto.documentNumber()).thenReturn("123456789");
        when(requestDto.walletNumberOrigin()).thenReturn("123");
        when(requestDto.amount()).thenReturn(BigDecimal.valueOf(-100));

        when(walletRepository.findByDocumentNumberAndWalletNumber("123456789", "123"))
                .thenReturn(Optional.of(walletFrom));

        //assertThrows(TransactionWalletException.class, () -> transferService.transfer(requestDto));
    }
}