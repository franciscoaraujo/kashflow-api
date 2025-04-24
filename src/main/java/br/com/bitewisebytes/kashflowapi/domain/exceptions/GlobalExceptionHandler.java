package br.com.bitewisebytes.kashflowapi.domain.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletException(WalletNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse( ex.getMessage(), "WALLET_NOT_FOUND");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionWalletException.class)
    public ResponseEntity<ErrorResponse> handleTransactionWalletException(TransactionWalletException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "TRANSACTION_WALLET_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", "GENERIC_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
