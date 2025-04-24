package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.repository.ExtractWalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.ExtractWalletProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExtractWalletServiceTest {

    private ExtractWalletRepository extractWalletRepository;
    private ExtractWalletService extractWalletService;

    @BeforeEach
    void setUp() {
        extractWalletRepository = Mockito.mock(ExtractWalletRepository.class);
        extractWalletService = new ExtractWalletService(extractWalletRepository);
    }

    @Test
    void testGetExtractWallet_Success() {
        String documentNumber = "123456789";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<ExtractWalletProjection> mockProjections = List.of(mock(ExtractWalletProjection.class));

        when(extractWalletRepository.findExtractByWalletNumber(documentNumber, pageable))
                .thenReturn(Optional.of(mockProjections));

        List<ExtractWalletProjection> result = extractWalletService.getExtractWallet(documentNumber, page, size);

        assertEquals(mockProjections, result);
        verify(extractWalletRepository, times(1)).findExtractByWalletNumber(documentNumber, pageable);
    }

    @Test
    void testGetExtractWallet_NoTransactionsFound() {
        String documentNumber = "123456789";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(extractWalletRepository.findExtractByWalletNumber(documentNumber, pageable))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> extractWalletService.getExtractWallet(documentNumber, page, size));
        verify(extractWalletRepository, times(1)).findExtractByWalletNumber(documentNumber, pageable);
    }

    @Test
    void testGetExtractWalletRangerDate_Success() {
        String documentNumber = "123456789";
        LocalDate initDate = LocalDate.now().minusDays(10);
        LocalDate finalDate = LocalDate.now();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<ExtractWalletProjection> mockProjections = List.of(mock(ExtractWalletProjection.class));

        when(extractWalletRepository.findExtractByWalletNumberAndaDate(documentNumber, initDate, finalDate, pageable))
                .thenReturn(Optional.of(mockProjections));

        List<ExtractWalletProjection> result = extractWalletService.getExtractWalletRangerDate(documentNumber, initDate, finalDate, page, size);

        assertEquals(mockProjections, result);
        verify(extractWalletRepository, times(1)).findExtractByWalletNumberAndaDate(documentNumber, initDate, finalDate, pageable);
    }

    @Test
    void testGetExtractWalletRangerDate_NoTransactionsFound() {
        String documentNumber = "123456789";
        LocalDate initDate = LocalDate.now().minusDays(10);
        LocalDate finalDate = LocalDate.now();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(extractWalletRepository.findExtractByWalletNumberAndaDate(documentNumber, initDate, finalDate, pageable))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> extractWalletService.getExtractWalletRangerDate(documentNumber, initDate, finalDate, page, size));
        verify(extractWalletRepository, times(1)).findExtractByWalletNumberAndaDate(documentNumber, initDate, finalDate, pageable);
    }
}