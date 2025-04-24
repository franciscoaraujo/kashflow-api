package br.com.bitewisebytes.kashflowapi.api.v1.controller;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.User;
import br.com.bitewisebytes.kashflowapi.domain.repository.UserRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.WalletRepository;
import br.com.bitewisebytes.kashflowapi.domain.validation.UserRequestValidation;
import br.com.bitewisebytes.kashflowapi.domain.validation.WalletDepositValidate;
import br.com.bitewisebytes.kashflowapi.domain.validation.WalletRequestTransferValidate;
import br.com.bitewisebytes.kashflowapi.domain.validation.WalletWithdrawValidate;
import br.com.bitewisebytes.kashflowapi.dto.*;
import br.com.bitewisebytes.kashflowapi.dto.request.UserResquestDto;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletDepositDto;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletRequestDto;
import br.com.bitewisebytes.kashflowapi.dto.request.WalletRequestTransferDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseCheckBalance;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseWalletWithdrawDto;
import br.com.bitewisebytes.kashflowapi.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/kashflow/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;
    private final DepositService depositService;
    private final WithdrawService withdrawService;
    private final TransferService transferService;
    private final BalanceService balanceService;
    private final ExtractWalletService extractWalletService;

    public WalletController(
            WalletService walletService,
            DepositService depositService,
            WithdrawService withdrawService,
            TransferService transferService,
            BalanceService balanceService,
            ExtractWalletService extractWalletService
    ) {
        this.walletService = walletService;
        this.depositService = depositService;
        this.withdrawService = withdrawService;
        this.transferService = transferService;
        this.balanceService = balanceService;
        this.extractWalletService = extractWalletService;
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("API KASHFLOW HEALTH - OK");
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponseDepositDto> walletDeposit(@RequestBody WalletDepositDto walletDepositDto) {
        WalletDepositValidate.validate(walletDepositDto);
        if (walletDepositDto == null) {
            return ResponseEntity.badRequest().body(null);
        }
        WalletResponseDepositDto depositReturn = depositService.deposit(walletDepositDto);
        return ResponseEntity.ok().body(depositReturn);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponseWalletWithdrawDto> walletDraw(@RequestBody WalletWithdrawDto walletDepositDto) {

        WalletWithdrawValidate.validate(walletDepositDto);

        if (walletDepositDto == null) {
            return ResponseEntity.badRequest().body(null);
        }
        WalletResponseWalletWithdrawDto withdraw = withdrawService.withdraw(walletDepositDto);
        return ResponseEntity.ok().body(withdraw);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody WalletRequestTransferDto walletRequestTransferDto) {

        WalletRequestTransferValidate.validate(walletRequestTransferDto);

        if (walletRequestTransferDto == null) {
            return ResponseEntity.badRequest().body("Invalid transfer request");
        }
        transferService.transfer(walletRequestTransferDto);
        return ResponseEntity.ok().body("Transfer successful");
    }

    @GetMapping("checkbalence/{walletNumber}")
    public ResponseEntity<WalletResponseCheckBalance> checkBalance(@PathVariable String walletNumber) {
        if (walletNumber == null || walletNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        WalletResponseCheckBalance checkBalance = balanceService.getBalance(walletNumber);
        return ResponseEntity.ok().body(checkBalance);
    }

    @GetMapping("/findAllWallets/document/{documentNumber}")
    public ResponseEntity<WalletResponseDto> findAllWalletsByDocument(@PathVariable String documentNumber,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        if (documentNumber == null || documentNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<WalletResponseDto> walletsListDto = walletService.getListWalletsByDocumentNumber(documentNumber, page, size);
        if (walletsListDto == null || walletsListDto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(walletsListDto.get(0));
    }

    @GetMapping("/extract/walletNumber/{walletNumber}")
    public ResponseEntity<List<ExtractWalletProjection>> extractWalletNumber(
            @PathVariable String walletNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (walletNumber == null || walletNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<ExtractWalletProjection> extractWallet = extractWalletService.getExtractWallet(walletNumber, 0, 10);
        return ResponseEntity.ok().body(extractWallet);
    }

    @GetMapping("/extract/walletNumber/{walletNumber}/dataInit/{dataInit}/dataFinal/{dataFinal}")
    public ResponseEntity<List<ExtractWalletProjection>> extractWalletNumberRangerDate(
            @PathVariable String walletNumber,
            @PathVariable LocalDate dataInit,
            @PathVariable LocalDate dataFinal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (walletNumber == null || walletNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<ExtractWalletProjection> extractWallet = extractWalletService.getExtractWalletRangerDate(walletNumber, dataInit, dataFinal, page, size);
        return ResponseEntity.ok().body(extractWallet);
    }

}
