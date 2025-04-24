package br.com.bitewisebytes.kashflowapi.utils;


import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public class WalletTokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    // Ex: WALLET-1713521462395-08234
    public static String generateTimestampToken() {
        long timestamp = Instant.now().toEpochMilli();
        int entropy = secureRandom.nextInt(99999);
        return String.format("WALLET-%d-%05d", timestamp, entropy);
    }
}
