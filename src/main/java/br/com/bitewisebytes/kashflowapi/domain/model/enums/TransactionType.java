package br.com.bitewisebytes.kashflowapi.domain.model.enums;

public enum TransactionType {

    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    TRANSFER_IN("Transfer In"),
    TRANSFER_OUT("Transfer Out"),
    BALANCE_INQUIRY("Balance Inquiry");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}