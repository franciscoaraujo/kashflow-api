package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.repository.ExtractWalletRepository;
import br.com.bitewisebytes.kashflowapi.dto.ExtractWalletDto;
import br.com.bitewisebytes.kashflowapi.dto.ExtractWalletProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExtractWalletService {


    private final ExtractWalletRepository extractWalletRepository;

    public ExtractWalletService(ExtractWalletRepository extractWalletRepository) {
        this.extractWalletRepository = extractWalletRepository;
    }

    public List<ExtractWalletProjection> getExtractWallet(String documentNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ExtractWalletProjection> extractWalletProjections = extractWalletRepository.findExtractByWalletNumber(documentNumber, pageable)
                .orElseThrow(() -> new RuntimeException("No transactions found for this wallet number"));
        return extractWalletProjections;
    }

    public List<ExtractWalletProjection>  getExtractWalletRangerDate(String documentNumber, LocalDate initDate, LocalDate finalDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ExtractWalletProjection> extractWalletProjections = extractWalletRepository.findExtractByWalletNumberAndaDate(documentNumber, initDate, finalDate, pageable)
                .orElseThrow(() -> new RuntimeException("No transactions found for this wallet number"));
        return extractWalletProjections;
    }

}
