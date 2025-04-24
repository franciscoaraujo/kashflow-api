package br.com.bitewisebytes.kashflowapi.dto.response;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;

import java.util.List;

public record UserResponseDto(
        String name,
        String email,
        String document,
        List<Wallet>wallets
) {

}
