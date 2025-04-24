package br.com.bitewisebytes.kashflowapi.dto.response;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;

import java.util.List;

public record UserResponseLastWalletDto(
        Long id,
        String name,
        String email,
        String document

) {
}
