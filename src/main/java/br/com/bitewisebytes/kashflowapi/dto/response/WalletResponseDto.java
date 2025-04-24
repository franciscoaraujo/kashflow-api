package br.com.bitewisebytes.kashflowapi.dto.response;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record WalletResponseDto (
        Long id,
        String walletNumber,
        UserResponseDto user,
        BigDecimal balance,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime dataTimeCreated
){

}
