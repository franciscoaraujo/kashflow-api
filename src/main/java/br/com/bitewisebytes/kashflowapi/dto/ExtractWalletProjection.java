package br.com.bitewisebytes.kashflowapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ExtractWalletProjection {
    Long getId();
    LocalDateTime getCreatedAt();
    LocalDate getDataTransaction();
    String getDescription();
    String getType();
    BigDecimal getAmount();
    BigDecimal getTransferAmount();
    String getWalletNumber();
    String getReceiverWalletNumber();
    String getReceiverUserName();
    LocalDateTime getTransferCreatedAt();
}
