package br.com.bitewisebytes.kashflowapi.dto.request;

public record WalletRequestDto(
        String name,
        String description,
        String type,
        String currency,
        Double initialBalance,
        String userId
) {
}
